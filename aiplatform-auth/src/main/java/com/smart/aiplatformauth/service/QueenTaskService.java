package com.smart.aiplatformauth.service;

import com.alibaba.fastjson.JSONObject;
import com.smart.aiplatformauth.mapper.ApiUserInfoMapper;
import com.smart.aiplatformauth.mapper.BpmInfoMapper;
import com.smart.aiplatformauth.model.BpmBasic;
import com.smart.aiplatformauth.model.BpmNodeInfo;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.vo.BpmInfoVo;
import com.smart.aiplatformauth.vo.UserRoleVo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 并发队列任务服务
 * @author chengjz
 */
@Slf4j
@Component
public class QueenTaskService implements Runnable {

    @Autowired
    private WeixinService weixinService;
    @Autowired
    private ApiUserInfoMapper apiUserInfoMapper;
    @Autowired
    private BpmInfoMapper bpmInfoMapper;

    public static LinkedBlockingQueue<String> noticeSendInfoQueue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        try {
            while (true) {
                if(!noticeSendInfoQueue.isEmpty()) {
                    //TODO 调用极光推送、微信消息通知、短信、邮件等接口发送消息
                    String noticeMessage = noticeSendInfoQueue.take();//take是阻塞方法，若队列为空，发生阻塞，等待新元素进入激活，poll则返回null，remove则产生异常  【相应的添加元素方法中，超出队列长度（默认65535），add会抛异常，offer直接返回false，put会阻塞等待】
                    log.info("并发队列noticeSendInfoQueue元素取出成功,内容为:" + noticeMessage);
                    String[] array = noticeMessage.split("###");
                    if(array[0].equals("nodestart")) {
                        this.sendWxNoticeWhenNodeStart(array[1]);
                    } else if(array[0].equals("bpmstart")) {
                        this.sendWxNoticeWhenBpmStart(array[1]);
                    } else if(array[0].equals("changeleader")) {
                        this.sendWxNoticeWhenChangeLeader(array[1]);
                    }
                }
            }
        } catch (Exception e) {
            log.info("并发队列noticeSendInfoQueue发生未知异常,内容为:" + e.getMessage());
        }
    }

    @PostConstruct
    private void init() {
        Thread thread = new Thread(new QueenTaskService());
        thread.start();
        log.info("【--------------------------------------并发队列服务初始化成功----------------------------------------】");
    }

    //当节点启动时发送微信消息通知给对应节点负责人
    private void sendWxNoticeWhenNodeStart(String infoData) {
        JSONObject jsonObject = JSONObject.parseObject(infoData).getJSONObject("value");
        User user = new User();
        user.setUserid(jsonObject.getInteger("bni_userid"));
        List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByUP(user);
        String openid = "";
        if(list.size() > 0) {
            if(null != list.get(0).getOpenid() && !"".equals(list.get(0).getOpenid())) {
                openid = list.get(0).getOpenid();
                List<String> listOpenId = new ArrayList<>();
                listOpenId.add(openid);

                List<String> listFeilds = new ArrayList<>();
                String templateId = "tiDKhU7y1iXFHRYjQT4wphBmPOhG-pGZ6WCJse46vFA";
                //TODO 根据审核通过的模板 字段组装listFeilds参数 和 模板id
                listFeilds.add("thing1");
                listFeilds.add("thing2");
                listFeilds.add("thing3");

                //此处根据项目应急流程业务组装内容,以后其他项目根据实际情况修改
                List<String> listValues = new ArrayList<>();
                listValues.add("应急任务待处理通知");
                listValues.add("安丘市自然灾害应急预案");
                listValues.add(jsonObject.getString("bpm_name") + "," + jsonObject.getString("bpm_eventflag") + ",任务流程正在进行中,目前需要您立即处理,当前任务:" + jsonObject.getString("bni_name") + ",详情请进入任务列表查看");
                int sendSuccessNum = weixinService.sendSubscribeMsg(listOpenId, templateId, listFeilds, listValues);
                log.info("应急任务待处理通知成功发送" + String.valueOf(sendSuccessNum) + "条");
            }
        }
    }

    //当流程启动时发送微信消息通知给所有流程参与人
    private void sendWxNoticeWhenBpmStart(String executeBpmVersion) {
        List<BpmBasic> listBpmBasic = bpmInfoMapper.findBpmBasicByExecuteVersion(executeBpmVersion);
        List<BpmNodeInfo> listBpmNodeInfo = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, executeBpmVersion);
        List<String> listOpenId = new ArrayList<>();
        for(BpmNodeInfo bpmNodeInfo: listBpmNodeInfo) {
            User user = new User();
            user.setUserid(bpmNodeInfo.getBni_userid());
            List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(list.size() > 0) {
                if(null != list.get(0).getOpenid() && !"".equals(list.get(0).getOpenid())) {
                    listOpenId.add(list.get(0).getOpenid());
                }
            }
        }
        List<String> listFeilds = new ArrayList<>();
        String templateId = "tiDKhU7y1iXFHRYjQT4wphBmPOhG-pGZ6WCJse46vFA";
        //TODO 根据审核通过的模板 字段组装listFeilds参数 和 模板id
        listFeilds.add("thing1");
        listFeilds.add("thing2");
        listFeilds.add("thing3");

        //此处根据项目应急流程业务组装内容,以后其他项目根据实际情况修改
        List<String> listValues = new ArrayList<>();
        listValues.add("应急任务启动通知");
        listValues.add("安丘市自然灾害应急预案");
        listValues.add(listBpmBasic.get(0).getBpm_name() + "," + listBpmBasic.get(0).getBpm_eventflag() + ",任务流程已启动,请立即到岗签到,并及时留意个人任务列表中的任务");
        int sendSuccessNum = weixinService.sendSubscribeMsg(listOpenId, templateId, listFeilds, listValues);
        log.info("应急任务启动通知成功发送" + String.valueOf(sendSuccessNum) + "条");
    }

    //当交接班时发送微信消息通知给交接人
    private void sendWxNoticeWhenChangeLeader(String info) {
        List<String> listOpenId = new ArrayList<>();
        User user = new User();
        user.setUserid(Integer.parseInt(info.split("-")[1]));
        List<UserRoleVo> listNewUser = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listNewUser.size() > 0) {
            if(null != listNewUser.get(0).getOpenid() && !"".equals(listNewUser.get(0).getOpenid())) {
                listOpenId.add(listNewUser.get(0).getOpenid());
            }
        }
        User user1 = new User();
        user.setUserid(Integer.parseInt(info.split("-")[0]));
        List<UserRoleVo> listOldUser = apiUserInfoMapper.findUserRoleInfoByUP(user1);
        List<String> listFeilds = new ArrayList<>();
        String templateId = "tiDKhU7y1iXFHRYjQT4wphBmPOhG-pGZ6WCJse46vFA";
        //TODO 根据审核通过的模板 字段组装listFeilds参数 和 模板id
        listFeilds.add("thing1");
        listFeilds.add("thing2");
        listFeilds.add("thing3");

        //此处根据项目应急流程业务组装内容,以后其他项目根据实际情况修改
        List<String> listValues = new ArrayList<>();
        listValues.add("应急任务交接班通知");
        listValues.add("安丘市自然灾害应急预案");
        listValues.add("您有任务已经由" + listOldUser.get(0).getRealname() + "交接给您,请及时处理,如需变更请登录微信小程序重新交接!");
        int sendSuccessNum = weixinService.sendSubscribeMsg(listOpenId, templateId, listFeilds, listValues);
        log.info("应急任务交接班通知成功发送" + String.valueOf(sendSuccessNum) + "条");
    }
}
