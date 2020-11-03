package com.smart.aiplatformauth.service.impl;

import static com.smart.aiplatformauth.service.QueenTaskService.noticeSendInfoQueue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.smart.aiplatformauth.dto.BpmInfoDto;
import com.smart.aiplatformauth.dto.BpmNodeInfoDto;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.mapper.ApiUserInfoMapper;
import com.smart.aiplatformauth.mapper.BpmInfoMapper;
import com.smart.aiplatformauth.mapper.BpmNodeInfoMapper;
import com.smart.aiplatformauth.mapper.NodeFormInfoMapper;
import com.smart.aiplatformauth.model.BpmBasic;
import com.smart.aiplatformauth.model.BpmNodeInfo;
import com.smart.aiplatformauth.model.NodeFormInfo;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.BpmInfoService;
import com.smart.aiplatformauth.service.SendMessageService;
import com.smart.aiplatformauth.service.WebSocketService;
import com.smart.aiplatformauth.utils.ImageDetailUtil;
import com.smart.aiplatformauth.utils.JsonUti;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.smart.aiplatformauth.utils.UpdateUtil;
import com.smart.aiplatformauth.vo.BpmInfoVo;
import com.smart.aiplatformauth.vo.BpmNodeFormInfoVo;
import com.smart.aiplatformauth.vo.UserRoleVo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * BPM流程信息服务接口实现类
 * @author: chengjz
 */
@Service
@Slf4j
public class BpmInfoServiceImpl extends ServiceImpl<BpmInfoMapper, BpmBasic> implements BpmInfoService {

    @Autowired
    private BpmInfoMapper bpmInfoMapper;
    @Autowired
    private BpmNodeInfoMapper bpmNodeInfoMapper;
    @Autowired
    private NodeFormInfoMapper nodeFormInfoMapper;
    @Autowired
    private ApiUserInfoMapper apiUserInfoMapper;
    @Autowired
    private ImageDetailUtil imageDetailUtil;
    @Autowired
    private SendMessageService sendMessageService;

    //查询基础工作流和其所有节点信息
    @Override
    public List<BpmInfoVo> findBpmBasicInfo(BpmInfoDto bpmInfoDto) {
        List<BpmInfoVo> list = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        return list;
    }

    //查询执行工作流和其所有节点信息
    @Override
    public Object findBpmExecuteInfo(BpmInfoDto bpmInfoDto) {
        List<BpmInfoVo> list = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);//列表list
        if(bpmInfoDto.getBpm_executeversion() == null || bpmInfoDto.getBpm_executeversion().equals("")) {
            return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(),list);
        }
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(),list);
        //以下处理数据为树状结构
        /*JSONObject jsonObjectTree = new JSONObject();
        List<BpmInfoVo> tempList = list;
        Collections.reverse(tempList);
        String maxOrder = tempList.get(0).getBni_ordernumber();
        int flagNumMaxRecord = maxOrder.length() - maxOrder.replaceAll("\\.", "").length();//序号为最大的节点内包含几个小数点，如1.1.1包含2个
        HashMap<String, JSONObject> hashMapJson = new HashMap<>();
        JSONArray jsonArrayTemp = new JSONArray();
        for(BpmInfoVo bpmInfoVo: tempList) {
            if(bpmInfoVo.getBni_ordernumber().equals("1")) {
                if(hashMapJson.size() > 0 && jsonArrayTemp.size() == 0) {
                    jsonObjectTree = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                    Set<String> set = hashMapJson.keySet();
                    JSONArray jsonArray = new JSONArray();
                    for(String orderKey: set) {
                        jsonArray.add(hashMapJson.get(orderKey));
                    }
                    jsonObjectTree.put("children", jsonArray);
                } else if(jsonArrayTemp.size() > 0 && hashMapJson.size() == 0) {
                    jsonObjectTree = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0; i<jsonArrayTemp.size(); i++) {
                        jsonArray.add(jsonArrayTemp.get(jsonArrayTemp.size() - i - 1));
                    }
                    jsonObjectTree.put("children", jsonArray);
                } else {
                    jsonObjectTree = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                    Set<String> set = hashMapJson.keySet();
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0; i<jsonArrayTemp.size(); i++) {
                        jsonArray.add(jsonArrayTemp.get(jsonArrayTemp.size() - i - 1));
                    }
                    for(String orderKey: set) {
                        jsonArray.add(hashMapJson.get(orderKey));
                    }
                    jsonObjectTree.put("children", jsonArray);
                }
            } else {
                String orderStr = bpmInfoVo.getBni_ordernumber();
                int flagNumRecord = orderStr.length() - orderStr.replaceAll("\\.", "").length();
                if(flagNumMaxRecord > flagNumRecord) {
                    //进入上级节点
                    JSONObject jsonObject = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                    JSONArray jsonArray = new JSONArray();
                    for(int i=0; i<jsonArrayTemp.size(); i++) {
                        jsonArray.add(jsonArrayTemp.get(jsonArrayTemp.size() - i - 1));
                    }
                    //判断hashMapJson中是否存在和刚放进去的同级别的，进行合并操作
                    Set<String> set = hashMapJson.keySet();
                    String orderStrJustPutIn = JSONObject.parseObject(jsonArrayTemp.get(0).toString()).getString("bni_ordernumber");
                    String[] arry1 = orderStrJustPutIn.split("\\.");
                    for(String orderKey: set) {
                        String[] arry2 = orderKey.split("\\.");
                        String orderStrFront = orderStrJustPutIn.substring(0, (orderStrJustPutIn.length()-arry1[arry1.length-1].length()-1));
                        String orderKeyFront = orderKey.substring(0, (orderKey.length()-arry1[arry1.length-1].length()-1));
                        if(arry1.length == arry2.length && orderStrFront.equals(orderKeyFront)) {
                            jsonArray.add(hashMapJson.get(orderKey));
                            hashMapJson.remove(orderKey);
                        }
                    }
                    //汇总数据
                    jsonObject.put("children", jsonArray);
                    hashMapJson.put(orderStr, jsonObject);
                    jsonArrayTemp.clear();
                } else {
                    //进入同级或下级节点
                    JSONObject jsonObject = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                    jsonArrayTemp.add(jsonObject);
                }
                flagNumMaxRecord = flagNumRecord;
            }
        }

        BpmInfoVo bpmInfoVoOriginOne = list.get(0);
        JSONObject jsonObjectBpmTreeEnd = this.changeBpmInfoVoToBpmBasicJSONObject(bpmInfoVoOriginOne);
        jsonObjectBpmTreeEnd.put("treedata", jsonObjectTree);
        Collections.reverse(list);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(),list, jsonObjectBpmTreeEnd);*/
    }

    //查询执行工作流和其所有节点信息---安丘定制应急详细过程
    @Override
    public Object findBpmExecuteInfoForProcessCustom(BpmInfoDto bpmInfoDto) {
        List<BpmInfoVo> list = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        JSONArray jsonArray = new JSONArray();
        for(BpmInfoVo bpmInfoVo: list) {
            if(bpmInfoVo.getBni_executestatus().equals("进行中")) {
                bpmInfoVo.setBni_executestatus("任务下发");
                jsonArray.add(JsonUti.object2JsonObject(bpmInfoVo));
            } else if(bpmInfoVo.getBni_executestatus().equals("已结束")) {
                bpmInfoVo.setBni_executestatus("任务下发");
                JSONObject jsonObject = JsonUti.object2JsonObject(bpmInfoVo);
                jsonArray.add(jsonObject);
                bpmInfoVo.setBni_executestatus("任务结束");
                bpmInfoVo.setBni_createtime(bpmInfoVo.getBni_overtime());
                jsonArray.add(JsonUti.object2JsonObject(bpmInfoVo));
            }
        }
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(),jsonArray);
    }

    public JSONObject changeBpmInfoVoToBpmNodeInfoJSONObject(BpmInfoVo bpmInfoVo) {
        JSONObject jsonObjectX = new JSONObject();
        jsonObjectX.put("bni_id", bpmInfoVo.getBni_id());
        jsonObjectX.put("bni_name", bpmInfoVo.getBni_name());
        jsonObjectX.put("bni_ordernumber", bpmInfoVo.getBni_ordernumber());
        jsonObjectX.put("bni_describe", bpmInfoVo.getBni_describe());
        jsonObjectX.put("bni_roleid", bpmInfoVo.getBni_roleid());
        jsonObjectX.put("bni_rolechinesename", bpmInfoVo.getBni_rolechinesename());
        jsonObjectX.put("bni_userid", bpmInfoVo.getBni_userid());
        jsonObjectX.put("bni_userchinesename", bpmInfoVo.getBni_userchinesename());
        jsonObjectX.put("bni_relateexecuteversion", bpmInfoVo.getBni_relateexecuteversion());
        jsonObjectX.put("bni_executestatus", bpmInfoVo.getBni_executestatus());
        jsonObjectX.put("bni_auditstatus", bpmInfoVo.getBni_auditstatus());
        jsonObjectX.put("bni_status", bpmInfoVo.getBni_status());
        jsonObjectX.put("bni_command", bpmInfoVo.getBni_command());
        jsonObjectX.put("bni_lon", bpmInfoVo.getBni_lon());
        jsonObjectX.put("bni_lat", bpmInfoVo.getBni_lat());
        jsonObjectX.put("bni_place", bpmInfoVo.getBni_place());
        jsonObjectX.put("bni_sourceids", bpmInfoVo.getBni_sourceids());
        jsonObjectX.put("bni_createtime", bpmInfoVo.getBni_createtime());
        jsonObjectX.put("bni_overtime", bpmInfoVo.getBni_overtime());
        return jsonObjectX;
    }

    public JSONObject changeBpmInfoVoToBpmBasicJSONObject(BpmInfoVo bpmInfoVo) {
        JSONObject jsonObjectX = new JSONObject();
        jsonObjectX.put("bpm_name", bpmInfoVo.getBpm_name());
        jsonObjectX.put("bpm_classification", bpmInfoVo.getBpm_classification());
        jsonObjectX.put("bpm_describe", bpmInfoVo.getBpm_describe());
        jsonObjectX.put("bpm_version", bpmInfoVo.getBpm_version());
        jsonObjectX.put("bpm_executeversion", bpmInfoVo.getBpm_executeversion());
        jsonObjectX.put("bpm_executestatus", bpmInfoVo.getBpm_executestatus());
        jsonObjectX.put("bpm_nownodeid", bpmInfoVo.getBpm_nownodeid());
        jsonObjectX.put("bpm_noticemode", bpmInfoVo.getBpm_noticemode());
        jsonObjectX.put("bpm_eventflag", bpmInfoVo.getBpm_eventflag());
        jsonObjectX.put("bpm_createtime", bpmInfoVo.getBpm_createtime());
        jsonObjectX.put("bpm_id", bpmInfoVo.getBpm_id());
        jsonObjectX.put("bpm_overtime", bpmInfoVo.getBni_overtime());
        return jsonObjectX;
    }

    //查询节点对应的表单上报信息
    @Override
    public List<BpmNodeFormInfoVo> findNodeAndFormInfo(String bni_relateexecuteversion, Integer bni_id, String nfi_type, Integer nfi_audituserid) {
        List<BpmNodeFormInfoVo> list = bpmInfoMapper.findNodeAndFormInfo(bni_relateexecuteversion, bni_id, nfi_type, nfi_audituserid);
        return list;
    }

    //根据工作流执行版本查询所有上报信息
    @Override
    public List<NodeFormInfo> findNodeFormInfoByBpmExecuteVersion(String nfi_belongbpmversion, String nfi_type, Integer nfi_audituserid) {
        List<NodeFormInfo> list = bpmInfoMapper.findNodeFormInfoByBpmExecuteVersion(nfi_belongbpmversion, nfi_type, nfi_audituserid);
        return list;
    }

    //启动工作流
    @Override
    public Object startBpm(String bpm_version, String bpm_describe, String bpm_eventflag, String bpm_noticemode, String userName) {
        User user = new User();
        user.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listUserRole.size() > 0) {
            if(listUserRole.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请修改---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法启动工作流程"));
            }
        }

        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_version(bpm_version);
        List<BpmInfoVo> listBasic = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        List<BpmInfoVo> listExecute = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        if(listBasic.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,未创建相应工作流"));
        }
        if(listExecute.size() > 0) {
            for(BpmInfoVo bpmInfoVo : listExecute) {
                if(!bpmInfoVo.getBpm_executestatus().equals("已结束") && !bpmInfoVo.getBpm_executestatus().equals("预启动")) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,流程正在进行中"));
                }
            }
        }
        if("".equals(bpm_describe) || null == bpm_describe || "".equals(bpm_eventflag) || null == bpm_eventflag) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "描述和事件标识必须填写"));
        }
        BpmBasic bpmBasic = new BpmBasic();
        bpmBasic.setBpm_name(listBasic.get(0).getBpm_name());
        bpmBasic.setBpm_classification(listBasic.get(0).getBpm_classification());
        bpmBasic.setBpm_describe(bpm_describe);
        bpmBasic.setBpm_version(bpm_version);
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmBasic.setBpm_createtime(dateStr);
        String executeVersionStr = UUID.randomUUID().toString();
        bpmBasic.setBpm_executeversion(executeVersionStr);
        bpmBasic.setBpm_relateversion(bpm_version);
        bpmBasic.setBpm_executestatus("进行中");
        String bpmNodeIdStr = "";
        bpmBasic.setBpm_nownodeid(bpmNodeIdStr);
        bpmBasic.setBpm_noticemode(bpm_noticemode);
        bpmBasic.setBpm_eventflag(bpm_eventflag);
        int bpmBasicInsertCount = bpmInfoMapper.insert(bpmBasic);
        if(bpmBasicInsertCount != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,创建工作流不成功"));
        }
        for(BpmInfoVo bpmInfoVo : listBasic) {
            BpmNodeInfo bpmNodeInfo = new BpmNodeInfo();
            bpmNodeInfo.setBni_name(bpmInfoVo.getBni_name());
            bpmNodeInfo.setBni_ordernumber(bpmInfoVo.getBni_ordernumber());
            bpmNodeInfo.setBni_describe(bpmInfoVo.getBni_describe());
            bpmNodeInfo.setBni_relatebasicversion(bpmInfoVo.getBni_relatebasicversion());
            bpmNodeInfo.setBni_status(bpmInfoVo.getBni_status());
            bpmNodeInfo.setBni_createtime(dateStr);
            bpmNodeInfo.setBni_roleid(bpmInfoVo.getBni_roleid());
            bpmNodeInfo.setBni_rolechinesename(bpmInfoVo.getBni_rolechinesename());
            bpmNodeInfo.setBni_userid(bpmInfoVo.getBni_userid());
            bpmNodeInfo.setBni_userchinesename(bpmInfoVo.getBni_userchinesename());
            bpmNodeInfo.setBni_tableform(bpmInfoVo.getBni_tableform());
            bpmNodeInfo.setBni_relateexecuteversion(executeVersionStr);
            bpmNodeInfo.setBni_auditstatus("未处理");
            if(bpmInfoVo.getBni_ordernumber().split("\\.").length == 2) {
                bpmNodeInfo.setBni_executestatus("进行中");
            } else if(bpmInfoVo.getBni_ordernumber().split("\\.").length > 2) {
                bpmNodeInfo.setBni_executestatus("未开始");
            } else {
                bpmNodeInfo.setBni_executestatus("已结束");
                bpmNodeInfo.setBni_auditstatus("已处理");
                bpmNodeInfo.setBni_overtime(dateStr);
            }
            int bpmNodeInfoInsertCount = bpmNodeInfoMapper.insert(bpmNodeInfo);
            if(bpmNodeInfoInsertCount != 1) {
                bpmInfoMapper.deleteById(listExecute.get(listExecute.size() - 1).getBpm_id() + 1);
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,创建节点失败,执行工作流创建过程已回滚"));
            }
        }

        //查询创建的节点id信息并更新到主表执行流程数据中
        List<BpmNodeInfo> executeList = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, executeVersionStr);
        for(BpmNodeInfo o : executeList) {
            if(o.getBni_executestatus().equals("进行中")) {
                if(bpmNodeIdStr.equals("")) {
                    bpmNodeIdStr = String.valueOf(o.getBni_id());
                } else {
                    bpmNodeIdStr = bpmNodeIdStr + "," + String.valueOf(o.getBni_id());
                }
            }
        }
        List<BpmBasic> listExecuteBpm = bpmInfoMapper.findBpmBasicByExecuteVersion(executeVersionStr);
        BpmBasic bpmBasicUpdateExecuteBpmUse = listExecuteBpm.get(0);
        bpmBasicUpdateExecuteBpmUse.setBpm_nownodeid(bpmNodeIdStr);
        int countEnd = bpmInfoMapper.updateById(bpmBasicUpdateExecuteBpmUse);
        if(countEnd != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,节点进度监控失败"));
        }

        //更新主表计数信息
        List<BpmBasic> list =  bpmInfoMapper.findBpmBasicByBasicVersion(listBasic.get(0).getBpm_version());
        BpmBasic bpmBasicUpdateUse = list.get(0);
        int executeCount = bpmBasicUpdateUse.getBpm_executecount();
        bpmBasicUpdateUse.setBpm_executecount(executeCount + 1);
        bpmInfoMapper.updateById(bpmBasicUpdateUse);
        return ResultUtil.result(Code.OK.getCode(), "启动流程成功");
    }

    //工作流某节点提交审核（状态变更）
    @Override
    public Object submitBpm(Integer bni_id, String bni_executestatus, String userName) {
        List<BpmNodeInfo> listBpmNodeInfo = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(bni_id, null);
        if(listBpmNodeInfo.size() != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,节点id不存在"));
        }
        User userCheckPermission = new User();
        userCheckPermission.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(userCheckPermission);
        //变更本节点状态
        BpmNodeInfo bpmNodeInfo = listBpmNodeInfo.get(0);
        if(bpmNodeInfo.getBni_roleid() != listUserRole.get(0).getRoleid()) {
            if(listUserRole.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请修改---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "当前用户无权限对该节点提交审核"));
            }
        }
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if(bni_executestatus.equals("未开始") && !bpmNodeInfo.getBni_executestatus().equals("未开始") && !bpmNodeInfo.getBni_executestatus().equals("进行中") && !bpmNodeInfo.getBni_executestatus().equals("已结束")) {
            bpmNodeInfo.setBni_executestatus("未开始");
            bpmNodeInfo.setBni_auditstatus("未处理");
        } else if(bni_executestatus.equals("进行中") && !bpmNodeInfo.getBni_executestatus().equals("进行中") && !bpmNodeInfo.getBni_executestatus().equals("已结束")) {
            bpmNodeInfo.setBni_executestatus("进行中");
            //下面1行逻辑为安丘项目定制，若非定制，则使用下面注释掉的逻辑
            bpmNodeInfo.setBni_createtime(dateStr);
        } else if(bni_executestatus.equals("处理中") && !bpmNodeInfo.getBni_auditstatus().equals("处理中") && !bpmNodeInfo.getBni_auditstatus().equals("已处理")) {
            bpmNodeInfo.setBni_auditstatus("处理中");
        } else if(bni_executestatus.equals("已结束") && !bpmNodeInfo.getBni_executestatus().equals("已结束")) {
            bpmNodeInfo.setBni_executestatus("已结束");
            bpmNodeInfo.setBni_auditstatus("已处理");
            bpmNodeInfo.setBni_overtime(dateStr);
        } else if(bni_executestatus.equals("已处理") && !bpmNodeInfo.getBni_auditstatus().equals("已处理")) {
            bpmNodeInfo.setBni_executestatus("已结束");
            bpmNodeInfo.setBni_auditstatus("已处理");
            bpmNodeInfo.setBni_overtime(dateStr);
        } else if(bni_executestatus.equals("未处理")) {
            return ResultUtil.result(Code.OK.getCode(), "提交审核未执行,状态相同,无需变更");
        } else {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,不存在的节点执行状态变更"));
        }
        /*//下面1行逻辑非定制，暂时注释掉，使用上面的定制逻辑
        bpmNodeInfo.setBni_createtime(dateStr);*/
        int countBpmNodeInfoUpate = bpmNodeInfoMapper.updateById(bpmNodeInfo);
        if(countBpmNodeInfoUpate != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,本节点信息更新失败"));
        }
        List<BpmBasic> listBpmBasic = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
        if(listBpmBasic.size() != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,执行流程版本号不存在"));
        }
        //更新主表信息
        BpmBasic bpmBasic = listBpmBasic.get(0);
        String nowNodeIdStr = bpmBasic.getBpm_nownodeid();
        if(bni_executestatus.equals("已处理") || bni_executestatus.equals("已结束")) {
            if(nowNodeIdStr.contains("," + bni_id + ",")) {
                nowNodeIdStr = bpmBasic.getBpm_nownodeid().replaceAll(("," + bni_id + ","), ",");
            } else if(nowNodeIdStr.contains(bni_id + ",")) {
                nowNodeIdStr = bpmBasic.getBpm_nownodeid().substring(String.valueOf(bni_id).length() + 1);
            } else if(nowNodeIdStr.contains("," + bni_id)) {
                nowNodeIdStr = bpmBasic.getBpm_nownodeid().substring(0, nowNodeIdStr.length() - String.valueOf(bni_id).length() -1);
            } else {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,主表执行流程数据当前节点信息缺失"));
            }
        } else if(bni_executestatus.equals("进行中")) {
            nowNodeIdStr = nowNodeIdStr + "," + bni_id;
        }

        //更新下级节点信息
        List<BpmNodeInfo> listAllThisExecuteVersion = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, bpmNodeInfo.getBni_relateexecuteversion());
        int i = 0;
        String allSameGradeNodeOverSign = "over";
        for(BpmNodeInfo bpmNodeInfo1 : listAllThisExecuteVersion) {
            if(bpmNodeInfo1.getBni_executestatus().equals("已结束") || bpmNodeInfo1.getBni_ordernumber().equals("1")) {
                i++;
            }
            if(bni_executestatus.equals("已处理") || bni_executestatus.equals("已结束")) {
                String[] arry1 = bpmNodeInfo1.getBni_ordernumber().split("\\.");
                String[] arry2 = bpmNodeInfo.getBni_ordernumber().split("\\.");
                if(arry1.length > 1) {
                    String arry1Front = bpmNodeInfo1.getBni_ordernumber().substring(0, (bpmNodeInfo1.getBni_ordernumber().length()-arry1[arry1.length-1].length()-1));
                    if((arry1.length - arry2.length) == 1 && arry1Front.equals(bpmNodeInfo.getBni_ordernumber())) {
                        bpmNodeInfo1.setBni_executestatus("进行中");
                        bpmNodeInfo1.setBni_createtime(dateStr);
                        int countNextBpmNodeInfoUpate = bpmNodeInfoMapper.updateById(bpmNodeInfo1);
                        if(countNextBpmNodeInfoUpate != 1) {
                            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,下级节点信息更新失败"));
                        }
                        nowNodeIdStr = nowNodeIdStr + "," + String.valueOf(bpmNodeInfo1.getBni_id());
                    }
                }
            }
            //若全部同级节点已结束则更新上级节点状态为已结束---安丘项目定制
            String orderNumberTemp = bpmNodeInfo1.getBni_ordernumber();
            int orderNumberTempLength = orderNumberTemp.split("\\.").length;
            String nowOrderNumber = bpmNodeInfo.getBni_ordernumber();
            int nowOrderNumberLength = nowOrderNumber.split("\\.").length;
            if(orderNumberTempLength  == nowOrderNumberLength) {
                if(orderNumberTemp.substring(0, orderNumberTemp.length()-orderNumberTemp.split("\\.")[orderNumberTempLength-1].length()-1).equals(nowOrderNumber.substring(0, nowOrderNumber.length()-nowOrderNumber.split("\\.")[nowOrderNumberLength-1].length()-1))) {
                    if(!bpmNodeInfo1.getBni_executestatus().equals("已结束")) {
                        allSameGradeNodeOverSign = "notover";
                    }
                }
            }
        }

        //若全部同级节点已结束则更新上级节点状态为已结束---安丘项目定制
        if(allSameGradeNodeOverSign.equals("over")) {
            String nowOrderNumber = bpmNodeInfo.getBni_ordernumber();
            int nowOrderNumberLength = nowOrderNumber.split("\\.").length;
            Map<String, Object> map = new HashMap<>();
            map.put("bni_ordernumber", nowOrderNumber.substring(0, nowOrderNumber.length()-nowOrderNumber.split("\\.")[nowOrderNumberLength-1].length()-1));
            map.put("bni_relateexecuteversion", bpmNodeInfo.getBni_relateexecuteversion());
            List<BpmNodeInfo> listSearchForMap= bpmNodeInfoMapper.selectByMap(map);
            if(listSearchForMap.size() > 0) {
                BpmNodeInfo bpmNodeInfoForUpdateAgain = listSearchForMap.get(0);
                bpmNodeInfoForUpdateAgain.setBni_executestatus("已结束");
                bpmNodeInfoForUpdateAgain.setBni_auditstatus("已处理");
                bpmNodeInfoForUpdateAgain.setBni_overtime(dateStr);
                int countBpmNodeInfoUpateAgain = bpmNodeInfoMapper.updateById(bpmNodeInfoForUpdateAgain);
                if(countBpmNodeInfoUpateAgain != 1) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,上级节点信息更新失败"));
                }
                if(nowNodeIdStr.contains("," + bpmNodeInfoForUpdateAgain.getBni_id() + ",")) {
                    nowNodeIdStr = bpmBasic.getBpm_nownodeid().replaceAll(("," + bpmNodeInfoForUpdateAgain.getBni_id() + ","), ",");
                } else if(nowNodeIdStr.contains(bpmNodeInfoForUpdateAgain.getBni_id() + ",")) {
                    nowNodeIdStr = bpmBasic.getBpm_nownodeid().substring(String.valueOf(bpmNodeInfoForUpdateAgain.getBni_id()).length() + 1);
                } else if(nowNodeIdStr.contains("," + bpmNodeInfoForUpdateAgain.getBni_id())) {
                    nowNodeIdStr = bpmBasic.getBpm_nownodeid().substring(0, nowNodeIdStr.length() - String.valueOf(bpmNodeInfoForUpdateAgain.getBni_id()).length() -1);
                } else {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,主表执行流程数据中,当前节点上级进行中节点监控信息缺失"));
                }
            }
        }

        if(i == listAllThisExecuteVersion.size()) {
            //bpmBasic.setBpm_executestatus("已结束");//注释掉则取消自动结束
            bpmBasic.setBpm_overtime(dateStr);
        }

        //将上级 本级 下级 进行中节点信息变更 更新到主表
        bpmBasic.setBpm_nownodeid(nowNodeIdStr);
        int countBpmBasicUpate = bpmInfoMapper.updateById(bpmBasic);
        if(countBpmBasicUpate != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "提交失败,主表执行流程对进行中节点监控信息更新失败"));
        }

        //组装websocket推送的消息调用异步线程方法发送,无需等待结果
        JSONObject jsonObjectValue = new JSONObject();
        jsonObjectValue.put("bpm_id", bpmBasic.getBpm_id());
        jsonObjectValue.put("bpm_name", bpmBasic.getBpm_name());
        jsonObjectValue.put("bpm_classification", bpmBasic.getBpm_classification());
        jsonObjectValue.put("bpm_executeversion", bpmBasic.getBpm_executeversion());
        jsonObjectValue.put("bpm_executestatus", bpmBasic.getBpm_executestatus());
        jsonObjectValue.put("bpm_nownodeid", bpmBasic.getBpm_nownodeid());
        jsonObjectValue.put("bpm_eventflag", bpmBasic.getBpm_eventflag());
        jsonObjectValue.put("bni_id", bni_id);
        jsonObjectValue.put("bni_name", bpmNodeInfo.getBni_name());
        jsonObjectValue.put("bni_executestatus", bpmNodeInfo.getBni_executestatus());
        jsonObjectValue.put("bni_auditstatus", bpmNodeInfo.getBni_auditstatus());
        jsonObjectValue.put("bni_userid", bpmNodeInfo.getBni_userid());
        jsonObjectValue.put("bni_roleid", bpmNodeInfo.getBni_roleid());
        JSONObject jsonObjectWebsocketMessage = new JSONObject();
        jsonObjectWebsocketMessage.put("name", "bpmsubmit");
        jsonObjectWebsocketMessage.put("value", jsonObjectValue);
        sendMessageService.sendWebsocketMessageByTaskIdInOnlyTaskSet(bpmBasic.getBpm_executeversion(), jsonObjectWebsocketMessage.toString());
        //判断是否需要进行极光推送、微信、短信等消息通知,将信息放入解耦的消息队列中
        if(bpmNodeInfo.getBni_executestatus().equals("进行中")) { //根据业务需要,此处可继续扩展,如状态任何改变都发通知到待命消息队列
            try {
                noticeSendInfoQueue.add("nodestart###" + jsonObjectWebsocketMessage);
            } catch (Exception e) {
                log.info("并发队列noticeSendInfoQueue可能已满,异常为:" + e.getMessage());
            }
        }

        //确认全流程状态
        if(i == listAllThisExecuteVersion.size()) {
            //组装websocket推送的消息调用异步线程方法发送,无需等待结果
            JSONObject jsonObjectValueOver = new JSONObject();
            jsonObjectValueOver.put("bpm_id", bpmBasic.getBpm_id());
            jsonObjectValueOver.put("bpm_name", bpmBasic.getBpm_name());
            jsonObjectValueOver.put("bpm_classification", bpmBasic.getBpm_classification());
            jsonObjectValueOver.put("bpm_executeversion", bpmBasic.getBpm_executeversion());
            jsonObjectValueOver.put("bpm_eventflag", bpmBasic.getBpm_eventflag());
            JSONObject jsonObjectWebsocketMessageOver = new JSONObject();
            jsonObjectWebsocketMessageOver.put("name", "bpmover");
            jsonObjectWebsocketMessageOver.put("value", jsonObjectValue);
            sendMessageService.sendWebsocketMessageByTaskIdInOnlyTaskSet(bpmBasic.getBpm_executeversion(), jsonObjectWebsocketMessageOver.toString());
            return ResultUtil.result(Code.OK.getCode(), "提交成功,流程结束");
        }
        return ResultUtil.result(Code.OK.getCode(), "提交成功");
    }

    //强制结束工作流（用于流程各节点未全部完结时的结束） sign不为1时为强制结束逻辑    sign为1时为正常手动结束 会确认所有节点结束状态
    @Override
    public Object forceStopBpm(String bpm_executeversion, String userName, String sign) {
        User user = new User();
        user.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listUserRole.size() > 0) {
            if(listUserRole.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请修改---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法强制结束工作流程"));
            }
        }

        List<BpmBasic> list =  bpmInfoMapper.findBpmBasicByExecuteVersion(bpm_executeversion);
        if(list.size() != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "强制结束流程失败,版本号不正确"));
        }

        if(sign.equals("1")) {
            int checkSign = 0;
            List<BpmNodeInfo> listBpmNodeInfo = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, bpm_executeversion);
            for(BpmNodeInfo bpmNodeInfo: listBpmNodeInfo) {
                if(!bpmNodeInfo.getBni_executestatus().equals("已结束")) {
                    checkSign = 1;
                }
            }
            if(checkSign ==1) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "结束流程失败,有进行中任务未结束"));
            }
        }

        BpmBasic bpmBasic = list.get(0);
        bpmBasic.setBpm_executestatus("已结束");
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmBasic.setBpm_overtime(dateStr);
        int count = bpmInfoMapper.updateById(bpmBasic);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "结束流程失败,更新信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "结束流程成功");
    }

    //通过用户id变更流程节点中的用户和角色信息
    @Override
    public Object editBpmNodeInfoByUserId(Integer bni_id, Integer userid) {
        List<BpmNodeInfo> list = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(bni_id, null);
        if(list.size() != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "处理失败,节点id不存在"));
        }
        BpmNodeInfo bpmNodeInfo = list.get(0);
        User user = new User();
        user.setUserid(bpmNodeInfo.getBni_userid());
        UserRoleVo userRoleVo = new UserRoleVo();
        List<UserRoleVo> list1 = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(list1.size() > 0) {
            userRoleVo = list1.get(0);
        }
        bpmNodeInfo.setBni_roleid(userRoleVo.getRoleid());
        bpmNodeInfo.setBni_rolechinesename(userRoleVo.getChinesename());
        bpmNodeInfo.setBni_userid(userid);
        bpmNodeInfo.setBni_userchinesename(userRoleVo.getRealname());
        int count = bpmNodeInfoMapper.updateById(bpmNodeInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "处理失败,更新节点信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "处理成功");
    }

    //根据用户id查询所有相关正在执行节点信息
    @Override
    public List<BpmNodeInfo> findExecuteBpmNodeInfoByUserId(Integer bni_userid, String bni_executestatus, String bni_auditstatus, String bni_name, String bni_place) {
        //下面五行为定制逻辑1，非定制的时候仅使用注释掉那行（逻辑2）就行
        User user = new User();
        user.setUserid(bni_userid);
        List<UserRoleVo> listUserRoleVo = apiUserInfoMapper.findUserRoleInfoByUP(user);
        Integer roleid = listUserRoleVo.get(0).getRoleid();
        List<BpmNodeInfo> list = bpmInfoMapper.findExecuteBpmNodeInfoByUserIdOrRoleid(null, roleid, bni_executestatus, bni_auditstatus, bni_name, bni_place);
        List<BpmNodeInfo> listEnd = new ArrayList<>();
        for(BpmNodeInfo bpmNodeInfo: list) {
            List<BpmBasic> list2 = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
            if(list2.size() > 0) {
                if(!list2.get(0).getBpm_executestatus().equals("已结束")) {
                    listEnd.add(bpmNodeInfo);
                }
            }
        }
        //List<BpmNodeInfo> list = bpmInfoMapper.findExecuteBpmNodeInfoByUserIdOrRoleid(bni_userid, null, null, null, null, null);
        return listEnd;
    }

    //新增节点表单信息（提交、上报信息）
    @Override
    public Object addNodeFormInfo(NodeFormInfo nodeFormInfo, MultipartFile[] file1, MultipartFile[] file2, String userName) {
        if("".equals(nodeFormInfo.getNfi_title()) ||
            null == nodeFormInfo.getNfi_title() ||
            null == nodeFormInfo.getNfi_nodeid() ||
            "".equals(nodeFormInfo.getNfi_belongbpmversion()) ||
            null == nodeFormInfo.getNfi_belongbpmversion()) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "标题、所属执行节点id、执行流程版本号不允许为空"));
        }

        BpmNodeInfo bpmNodeInfo = bpmNodeInfoMapper.selectById(nodeFormInfo.getNfi_nodeid());
        if(bpmNodeInfo.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "所属节点ID不存在"));
        }
        if(!bpmNodeInfo.getBni_relateexecuteversion().equals(nodeFormInfo.getNfi_belongbpmversion())) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "所属执行流程版本号不存在"));
        }

        User user = new User();
        user.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listUserRole.size() > 0) {
            nodeFormInfo.setNfi_audituser(listUserRole.get(0).getRealname());
            nodeFormInfo.setNfi_audituserid(listUserRole.get(0).getUserid());
        }
        nodeFormInfo.setNfi_image("");
        nodeFormInfo.setNfi_file("");
        if(file1 != null) {
            if(file1.length > 0) {
                String result1 = imageDetailUtil.upload(file1);
                if(result1.equals("error")) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "上传图片失败,请检查图片是否损坏"));
                }
                nodeFormInfo.setNfi_image(result1);
            }
        }

        if(file2 != null) {
            if(file2.length > 0) {
                String result2 = imageDetailUtil.uploadFile1(file2);
                if(result2.equals("error")) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "上传文件失败,请检查文件是否损坏"));
                }
                nodeFormInfo.setNfi_file(result2);
            }
        }
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        nodeFormInfo.setNfi_createtime(dateStr);
        int count = nodeFormInfoMapper.insert(nodeFormInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增信息失败"));
        }
        //组装websocket推送的消息调用异步线程方法发送,无需等待结果
        JSONObject jsonObjectWebsocketMessage = new JSONObject();
        jsonObjectWebsocketMessage.put("name", "nodeforminfosubmit");
        jsonObjectWebsocketMessage.put("value", JsonUti.object2JsonObject(nodeFormInfo));
        sendMessageService.sendWebsocketMessageByTaskIdInOnlyTaskSet(nodeFormInfo.getNfi_belongbpmversion(), jsonObjectWebsocketMessage.toString());
        return ResultUtil.result(Code.OK.getCode(), "新增信息成功");
    }

    //新增基础工作流
    @Override
    public Object addBpmBasic(BpmBasic bpmBasic, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        if(null == bpmBasic.getBpm_name() || "".equals(bpmBasic.getBpm_name()) || null == bpmBasic.getBpm_classification() || "".equals(bpmBasic.getBpm_classification()) || null == bpmBasic.getBpm_describe() || "".equals(bpmBasic.getBpm_describe())) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "名称、分类、描述信息不允许为空"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bpm_name", bpmBasic.getBpm_name());
        List<BpmBasic> list = bpmInfoMapper.selectByMap(map);
        if(list.size() > 0) {
            for(BpmBasic bpmBasic1: list) {
                if(bpmBasic1.getBpm_executeversion() == null || bpmBasic1.getBpm_executeversion().equals("")) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础工作流名称已存在"));
                }
            }
        }
        bpmBasic.setBpm_version(UUID.randomUUID().toString());
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmBasic.setBpm_status("1");
        bpmBasic.setBpm_executecount(0);
        bpmBasic.setBpm_createtime(dateStr);
        bpmBasic.setBpm_executeversion("");
        bpmBasic.setBpm_relateversion("");
        bpmBasic.setBpm_executestatus("");
        bpmBasic.setBpm_nownodeid("");
        bpmBasic.setBpm_eventflag("");
        int count = bpmInfoMapper.insert(bpmBasic);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "新增信息成功");
    }

    //更新基础工作流信息
    @Override
    public Object editBpmBasic(BpmBasic bpmBasic, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        if(bpmBasic.getBpm_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不允许为空"));
        }
        bpmBasic.setBpm_version(null);
        bpmBasic.setBpm_executecount(null);
        bpmBasic.setBpm_createtime(null);
        BpmBasic bpmBasicInDataBase = bpmInfoMapper.selectById(bpmBasic.getBpm_id());
        if(bpmBasicInDataBase.getBpm_executeversion() != null && !bpmBasicInDataBase.getBpm_executeversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行流程信息不允许更新"));
        }
        if(bpmBasicInDataBase.getBpm_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键ID不存在"));
        }
        UpdateUtil.copyNonNullProperties(bpmBasicInDataBase, bpmBasic);
        Map<String, Object> map = new HashMap<>();
        map.put("bpm_name", bpmBasic.getBpm_name());
        List<BpmBasic> list = bpmInfoMapper.selectByMap(map);
        if(list.size() > 0) {
            for(BpmBasic bpmBasic1: list) {
                if(bpmBasic1.getBpm_executeversion() == null || bpmBasic1.getBpm_executeversion().equals("")) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础工作流名称已存在,不允许更新为该名称"));
                }
            }
        }
        bpmBasic.setBpm_executeversion("");
        bpmBasic.setBpm_relateversion("");
        bpmBasic.setBpm_executestatus("");
        bpmBasic.setBpm_nownodeid("");
        bpmBasic.setBpm_eventflag("");
        int count = bpmInfoMapper.updateById(bpmBasic);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "更新信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "更新信息成功");
    }

    //删除基础工作流及其关联信息
    @Override
    public Object delBpmBasic(Integer bpm_id, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        BpmBasic bpmBasicInDataBase = bpmInfoMapper.selectById(bpm_id);
        if(bpmBasicInDataBase.getBpm_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不存在"));
        }
        if(bpmBasicInDataBase.getBpm_executeversion() != null && !bpmBasicInDataBase.getBpm_executeversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id逻辑错误,该信息为执行流程信息,指令拒绝"));
        }
        String bpmBasicVersion = bpmBasicInDataBase.getBpm_version();
        int count = bpmInfoMapper.deleteById(bpm_id);
        if (count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主表基础工作流信息删除失败"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bni_relatebasicversion", bpmBasicVersion);
        map.put("bni_relateexecuteversion", "");
        List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
        int countBpmNodeInfo = 0;
        for(BpmNodeInfo bpmNodeInfo: list) {
            countBpmNodeInfo += bpmNodeInfoMapper.deleteById(bpmNodeInfo.getBni_id());
        }
        if(countBpmNodeInfo != list.size()) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "部分基础工作流节点信息删除失败"));
        }
        return ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础流程信息关联删除成功");
    }

    //删除执行流程及其关联信息
    @Override
    public Object delExecuteBpm(Integer bpm_id, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        BpmBasic bpmBasicInDataBase = bpmInfoMapper.selectById(bpm_id);
        if(bpmBasicInDataBase.getBpm_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不存在"));
        }
        if(bpmBasicInDataBase.getBpm_executeversion() == null || bpmBasicInDataBase.getBpm_executeversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id逻辑错误,该信息为基础工作流信息,指令拒绝"));
        }
        String bpmExecuteVersion = bpmBasicInDataBase.getBpm_executeversion();
        int count = bpmInfoMapper.deleteById(bpm_id);
        if (count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主表执行流程信息删除失败"));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("bni_relateexecuteversion", bpmExecuteVersion);
        List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
        int countBpmNodeInfo = 0;
        for(BpmNodeInfo bpmNodeInfo: list) {
            countBpmNodeInfo += bpmNodeInfoMapper.deleteById(bpmNodeInfo.getBni_id());
        }
        if(countBpmNodeInfo != list.size()) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "部分执行节点信息删除失败"));
        }
        Boolean formInfoDelete = bpmInfoMapper.deleteNodeFormInfoByExecuteVersion(bpmExecuteVersion);
        if(!formInfoDelete) {
            return ResultUtil.result(Code.OK.getCode(), "执行流程信息关联删除成功,但节点关联的表单信息删除失败或未提交过信息");
        }
        return ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行流程信息关联删除成功");
    }

    //新增基础工作流节点信息
    @Override
    public Object addBasicBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        if(null == bpmNodeInfo.getBni_name() || "".equals(bpmNodeInfo.getBni_name()) || null == bpmNodeInfo.getBni_describe() || "".equals(bpmNodeInfo.getBni_describe()) || null == bpmNodeInfo.getBni_relatebasicversion() || "".equals(bpmNodeInfo.getBni_relatebasicversion()) || null == bpmNodeInfo.getBni_status() || "".equals(bpmNodeInfo.getBni_status())) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点名称、描述、关联基础流程版本号、节点属性状态不允许为空"));
        }
        if(!bpmNodeInfo.getBni_status().equals("0") && !bpmNodeInfo.getBni_status().equals("1") && !bpmNodeInfo.getBni_status().equals("2")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点属性状态指定不正确,仅允许0-草稿或1-并行网关或2-排它网关"));
        }

        Map<String, Object> mapCheckName = new HashMap<>();
        mapCheckName.put("bni_name", bpmNodeInfo.getBni_name());
        mapCheckName.put("bni_relatebasicversion", bpmNodeInfo.getBni_relatebasicversion());
        mapCheckName.put("bni_relateexecuteversion", "");
        List<BpmNodeInfo> listCheckName = bpmNodeInfoMapper.selectByMap(mapCheckName);
        if(listCheckName.size() > 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础工作流节点名称已存在"));
        }

        if(bpmNodeInfo.getBni_userid() != null) {
            User user = new User();
            user.setUserid(bpmNodeInfo.getBni_userid());
            List<UserRoleVo> listUserRoleVo = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(listUserRoleVo.size() == 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点要绑定的负责人用户ID不存在"));
            }
            bpmNodeInfo.setBni_userchinesename(listUserRoleVo.get(0).getRealname());
            bpmNodeInfo.setBni_roleid(listUserRoleVo.get(0).getRoleid());
            bpmNodeInfo.setBni_rolechinesename(listUserRoleVo.get(0).getChinesename());
        }

        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_version(bpmNodeInfo.getBni_relatebasicversion());
        List<BpmInfoVo> listBpmInfoVo = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        if(listBpmInfoVo.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "关联工作流基础版本号无效,工作流基础信息表中不存在相关数据"));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("bni_relatebasicversion", bpmNodeInfo.getBni_relatebasicversion());
        List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
        if(bpmNodeInfo.getBni_id() == null) {
            if(list.size() == 0) {
                bpmNodeInfo.setBni_ordernumber("1");
            } else {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "未传参父节点主键bni_id则默认该新增节点为初始节点,但已存在初始节点,请重新定义"));
            }
        } else {
            BpmNodeInfo bpmNodeInfoFather = bpmNodeInfoMapper.selectById(bpmNodeInfo.getBni_id());
            String orderFather = bpmNodeInfoFather.getBni_ordernumber();
            if(orderFather == null || orderFather.equals("")) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "指定的父节点主键bni_id不存在"));
            }
            String maxSameGradeNodeOrder = orderFather + ".0";
            for(BpmInfoVo bpmInfoVo: listBpmInfoVo) {
                String orderNumberTemp = bpmInfoVo.getBni_ordernumber();
                if(orderNumberTemp.split("\\.").length - orderFather.split("\\.").length == 1) {
                    if(orderNumberTemp.substring(0, orderFather.length()).equals(orderFather)) {
                        maxSameGradeNodeOrder = orderNumberTemp;
                    }
                }
            }
            String[] arry = maxSameGradeNodeOrder.split("\\.");
            String orderNumberForUpdateEnd = "";
            for(int i=0; i<arry.length; i++) {
                if(orderNumberForUpdateEnd.equals("")) {
                    orderNumberForUpdateEnd = arry[i];
                } else {
                    if(i == arry.length - 1) {
                        orderNumberForUpdateEnd = orderNumberForUpdateEnd + "." + String.valueOf(Integer.parseInt(arry[i]) + 1);
                    } else {
                        orderNumberForUpdateEnd = orderNumberForUpdateEnd + "." + arry[i];
                    }
                }
            }
            bpmNodeInfo.setBni_ordernumber(orderNumberForUpdateEnd);
        }
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmNodeInfo.setBni_createtime(dateStr);
        bpmNodeInfo.setBni_tableform("nodeforminfo");//注意 此处默认设置了通用表单 如果有特殊需求 请修改这里
        bpmNodeInfo.setBni_relateexecuteversion("");
        bpmNodeInfo.setBni_executestatus("");
        bpmNodeInfo.setBni_auditstatus("");
        int count = bpmNodeInfoMapper.insert(bpmNodeInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增基础节点信息失败"));
        }
        return ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增基础节点信息成功");
    }

    //更新基础工作流节点信息
    @Override
    public Object editBasicBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        if(bpmNodeInfo.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不允许为空"));
        }
        bpmNodeInfo.setBni_ordernumber(null);
        bpmNodeInfo.setBni_relatebasicversion(null);
        bpmNodeInfo.setBni_createtime(null);
        BpmNodeInfo bpmNodeInfoInDataBase = bpmNodeInfoMapper.selectById(bpmNodeInfo.getBni_id());
        if(bpmNodeInfoInDataBase.getBni_relateexecuteversion() != null && !bpmNodeInfoInDataBase.getBni_relateexecuteversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行节点信息不允许更新"));
        }
        if(bpmNodeInfoInDataBase.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键ID不存在"));
        }

        if(bpmNodeInfo.getBni_name() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("bni_name", bpmNodeInfo.getBni_name());
            map.put("bni_relatebasicversion", bpmNodeInfoInDataBase.getBni_relatebasicversion());
            map.put("bni_relateexecuteversion", "");
            List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
            if(list.size() > 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础工作流节点名称已存在,不允许更新为该名称"));
            }
        }

        UpdateUtil.copyNonNullProperties(bpmNodeInfoInDataBase, bpmNodeInfo);
        bpmNodeInfo.setBni_relateexecuteversion("");
        bpmNodeInfo.setBni_executestatus("");
        bpmNodeInfo.setBni_auditstatus("");
        if(bpmNodeInfo.getBni_userid() != null && bpmNodeInfo.getBni_userid() != bpmNodeInfoInDataBase.getBni_userid()) {
            User user = new User();
            user.setUserid(bpmNodeInfo.getBni_userid());
            List<UserRoleVo> listUserRoleVo = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(listUserRoleVo.size() == 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点要绑定的负责人用户ID不存在"));
            }
            bpmNodeInfo.setBni_userchinesename(listUserRoleVo.get(0).getRealname());
            bpmNodeInfo.setBni_roleid(listUserRoleVo.get(0).getRoleid());
            bpmNodeInfo.setBni_rolechinesename(listUserRoleVo.get(0).getChinesename());
        }
        int count = bpmNodeInfoMapper.updateById(bpmNodeInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "更新信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "更新信息成功");
    }

    //删除基础工作流节点信息 ---要考虑同级节点顺序号变化和下级节点删除
    @Override
    public Object delBasicBpmNodeInfo(Integer bni_id, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 2) {//【!!!重要---此出正常来讲是2，为业务需要可写其他，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        BpmNodeInfo bpmNodeInfo = bpmNodeInfoMapper.selectById(bni_id);
        if(bpmNodeInfo.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不存在"));
        }
        if(bpmNodeInfo.getBni_relateexecuteversion() != null && !bpmNodeInfo.getBni_relateexecuteversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id逻辑错误,该信息为执行工作流节点信息,指令拒绝"));
        }
        int count = bpmNodeInfoMapper.deleteById(bni_id);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "删除基础工作流-基础节点信息失败"));
        }
        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_version(bpmNodeInfo.getBni_relatebasicversion());
        List<BpmInfoVo> listBpmInfoVo = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        if(listBpmInfoVo.size() == 0) {
            return ResultUtil.result(Code.OK.getCode(), "无有效关联的基础工作流,此为无效基础节点,信息删除成功");
        }
        for(BpmInfoVo bpmInfoVo: listBpmInfoVo) {
            String orderNumber = bpmInfoVo.getBni_ordernumber();
            String orderNumberNow = bpmNodeInfo.getBni_ordernumber();
            if(orderNumber.split("\\.").length > orderNumberNow.split("\\.").length) {
                if(orderNumber.substring(0, orderNumberNow.length()).equals(orderNumberNow)) {
                    bpmNodeInfoMapper.deleteById(bpmInfoVo.getBni_id());
                }
            }
            //TODO 此处未处理平级的情况,以后去做
        }
        return ResultUtil.result(Code.OK.getCode(), "删除基础工作流-基础节点信息成功");
    }

    //新增执行工作流节点信息
    @Override
    public Object addExecuteBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要此处写4，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        //此处以后非定制时应补充校验描述字段
        if(null == bpmNodeInfo.getBni_name() || "".equals(bpmNodeInfo.getBni_name()) || null == bpmNodeInfo.getBni_relateexecuteversion() || "".equals(bpmNodeInfo.getBni_relateexecuteversion()) || null == bpmNodeInfo.getBni_status() || "".equals(bpmNodeInfo.getBni_status())) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点名称、关联执行流程版本号、节点属性状态不允许为空"));
        }
        if(!bpmNodeInfo.getBni_status().equals("0") && !bpmNodeInfo.getBni_status().equals("1") && !bpmNodeInfo.getBni_status().equals("2")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点属性状态指定不正确,仅允许0-草稿或1-并行网关或2-排它网关"));
        }

        Map<String, Object> mapCheckName = new HashMap<>();
        mapCheckName.put("bni_name", bpmNodeInfo.getBni_name());
        mapCheckName.put("bni_relateexecuteversion", bpmNodeInfo.getBni_relateexecuteversion());
        List<BpmNodeInfo> listCheckName = bpmNodeInfoMapper.selectByMap(mapCheckName);
        if(listCheckName.size() > 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行工作流节点名称已存在"));
        }

        if(bpmNodeInfo.getBni_userid() != null) {
            User user = new User();
            user.setUserid(bpmNodeInfo.getBni_userid());
            List<UserRoleVo> listUserRoleVo = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(listUserRoleVo.size() == 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点要绑定的负责人用户ID不存在"));
            }
            bpmNodeInfo.setBni_userchinesename(listUserRoleVo.get(0).getRealname());
            bpmNodeInfo.setBni_roleid(listUserRoleVo.get(0).getRoleid());
            bpmNodeInfo.setBni_rolechinesename(listUserRoleVo.get(0).getChinesename());
        }

        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_executeversion(bpmNodeInfo.getBni_relateexecuteversion());
        List<BpmInfoVo> listBpmInfoVo = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        if(listBpmInfoVo.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "关联工作流执行版本号无效,工作流执行信息表中不存在相关数据"));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("bni_relateexecuteversion", bpmNodeInfo.getBni_relateexecuteversion());
        List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
        if(bpmNodeInfo.getBni_id() == null) {
            if(list.size() == 0) {
                bpmNodeInfo.setBni_ordernumber("1");
            } else {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "未传参父节点主键bni_id则默认该新增节点为初始节点,但已存在初始节点,请重新定义"));
            }
        } else {
            BpmNodeInfo bpmNodeInfoFather = bpmNodeInfoMapper.selectById(bpmNodeInfo.getBni_id());
            String orderFather = bpmNodeInfoFather.getBni_ordernumber();
            if(orderFather == null || orderFather.equals("")) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "指定的父节点主键bni_id不存在"));
            }

            //下面9行为安丘项目定制
            if(orderFather.split("\\.").length == 1) {
                if(null == bpmNodeInfo.getBni_describe() || "".equals(bpmNodeInfo.getBni_describe())) {
                    bpmNodeInfo.setBni_describe("二级节点描述未定义");
                }
            } else {
                if(null == bpmNodeInfo.getBni_describe() || "".equals(bpmNodeInfo.getBni_describe())) {
                    throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点描述属性不允许为空"));
                }
            }

            String maxSameGradeNodeOrder = orderFather + ".1";
            int sign = 0;
            for(BpmInfoVo bpmInfoVo: listBpmInfoVo) {
                String orderNumberTemp = bpmInfoVo.getBni_ordernumber();
                if(orderNumberTemp.split("\\.").length - orderFather.split("\\.").length == 1) {
                    if(orderNumberTemp.substring(0, orderFather.length()).equals(orderFather)) {
                        maxSameGradeNodeOrder = orderNumberTemp;
                        sign++;
                    }
                }
            }
            String[] arry = maxSameGradeNodeOrder.split("\\.");
            String orderNumberForUpdateEnd = "";
            for(int i=0; i<arry.length; i++) {
                if(orderNumberForUpdateEnd.equals("")) {
                    orderNumberForUpdateEnd = arry[i];
                } else {
                    if(sign == 0) {
                        orderNumberForUpdateEnd = orderNumberForUpdateEnd + "." + String.valueOf(Integer.parseInt(arry[i]));
                    } else if(i == arry.length - 1) {
                        orderNumberForUpdateEnd = orderNumberForUpdateEnd + "." + String.valueOf(Integer.parseInt(arry[i]) + 1);
                    } else {
                        orderNumberForUpdateEnd = orderNumberForUpdateEnd + "." + arry[i];
                    }
                }
            }
            bpmNodeInfo.setBni_ordernumber(orderNumberForUpdateEnd);
        }
        bpmNodeInfo.setBni_relatebasicversion(listBpmInfoVo.get(0).getBpm_version());
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmNodeInfo.setBni_createtime(dateStr);
        bpmNodeInfo.setBni_tableform("nodeforminfo");//注意 此处默认设置了通用表单 如果有特殊需求 请修改这里
        bpmNodeInfo.setBni_executestatus("未开始");
        bpmNodeInfo.setBni_auditstatus("未处理");
        int count = bpmNodeInfoMapper.insert(bpmNodeInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增执行流程节点信息失败"));
        }
        return ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增执行流程节点信息成功");
    }

    //更新执行工作流节点信息
    @Override
    public Object editExecuteBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要此处写的4，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        if(bpmNodeInfo.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不允许为空"));
        }
        /*if(bpmNodeInfo.getBni_ordernumber() != null && !bpmNodeInfo.getBni_ordernumber().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点顺序号不允许更新,此类操作请删除节点再新增"));
        }*/
        bpmNodeInfo.setBni_ordernumber(null);
        bpmNodeInfo.setBni_relatebasicversion(null);
        bpmNodeInfo.setBni_createtime(null);
        BpmNodeInfo bpmNodeInfoInDataBase = bpmNodeInfoMapper.selectById(bpmNodeInfo.getBni_id());
        /*if(bpmNodeInfoInDataBase.getBni_relateexecuteversion() == null || bpmNodeInfoInDataBase.getBni_relateexecuteversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "基础节点信息不允许更新"));
        }*/
        if(bpmNodeInfoInDataBase.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键ID不存在"));
        }
        bpmNodeInfo.setBni_relateexecuteversion(null);
        bpmNodeInfo.setBni_executestatus(null);
        bpmNodeInfo.setBni_auditstatus(null);

        if("".equals(bpmNodeInfo.getBni_name())) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行工作流节点名称不允许更新为空"));
        }
        /*if(bpmNodeInfo.getBni_name() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("bni_name", bpmNodeInfo.getBni_name());
            map.put("bni_relateexecuteversion", bpmNodeInfoInDataBase.getBni_relateexecuteversion());
            List<BpmNodeInfo> list = bpmNodeInfoMapper.selectByMap(map);
            if(list.size() > 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "执行工作流节点名称已存在,不允许更新为该名称"));
            }
        }*/

        UpdateUtil.copyNonNullProperties(bpmNodeInfoInDataBase, bpmNodeInfo);
        if(!bpmNodeInfo.getBni_executestatus().equals("未开始")) {
            if(bpmNodeInfo.getBni_ordernumber().equals("1") && bpmNodeInfo.getBni_command() != null && !"".equals(bpmNodeInfo.getBni_command())) {
                //安丘项目定制逻辑，用于修改已执行的第一个节点的总时间字段
            } else {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "正在执行任务的节点不允许更新信息"));
            }
        }

        if(bpmNodeInfo.getBni_userid() != null && bpmNodeInfo.getBni_userid() != bpmNodeInfoInDataBase.getBni_userid()) {
            User user = new User();
            user.setUserid(bpmNodeInfo.getBni_userid());
            List<UserRoleVo> listUserRoleVo = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(listUserRoleVo.size() == 0) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "节点要绑定的负责人用户ID不存在"));
            }
            bpmNodeInfo.setBni_userchinesename(listUserRoleVo.get(0).getRealname());
            bpmNodeInfo.setBni_roleid(listUserRoleVo.get(0).getRoleid());
            bpmNodeInfo.setBni_rolechinesename(listUserRoleVo.get(0).getChinesename());
        }

        int count = bpmNodeInfoMapper.updateById(bpmNodeInfo);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "更新信息失败"));
        }
        return ResultUtil.result(Code.OK.getCode(), "更新信息成功");
    }

    //交接班
    @Override
    public Object changeLeader(Integer userIdOld, Integer userIdNew) {
        List<BpmNodeInfo> list = bpmInfoMapper.findExecuteBpmNodeInfoByUserIdOrRoleid(userIdOld, null, "进行中", null, null, null);
        if(list.size() == 0) {
            return ResultUtil.result(Code.OK.getCode(), "当前无需要处理的进行中任务,不需要交接班");
        }
        User user = new User();
        user.setUserid(userIdNew);
        List<UserRoleVo> list1 = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(list1.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "要变更的用户id无对应用户"));
        }
        UserRoleVo userRoleVo = list1.get(0);
        int countAll = 0;
        int listDetail = list.size();
        for(BpmNodeInfo bpmNodeInfo: list) {
            List<BpmBasic> list2 = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
            if(list2.size() > 0) {
                if(!list2.get(0).getBpm_executestatus().equals("已结束")) {
                    bpmNodeInfo.setBni_userid(userRoleVo.getUserid());
                    bpmNodeInfo.setBni_userchinesename(userRoleVo.getRealname());
                    bpmNodeInfo.setBni_roleid(userRoleVo.getRoleid());
                    bpmNodeInfo.setBni_rolechinesename(userRoleVo.getChinesename());
                    countAll+= bpmNodeInfoMapper.updateById(bpmNodeInfo);
                } else {
                    listDetail--;
                }
            }
        }
        if(countAll != listDetail) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "部分节点负责人信息交接班失败"));
        }
        noticeSendInfoQueue.add("changeleader###" + userIdOld + "-" + userIdNew);
        return ResultUtil.result(Code.OK.getCode(), "变更信息成功");
    }

    //删除执行工作流节点信息
    @Override
    public Object delExecuteBpmNodeInfo(Integer bni_id, String userName) {
        User userNowLogin = new User();
        userNowLogin.setUsername(userName);
        List<UserRoleVo> listUserRoleForCheckPermission = apiUserInfoMapper.findUserRoleInfoByUP(userNowLogin);
        if(listUserRoleForCheckPermission.size() > 0) {
            if(listUserRoleForCheckPermission.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要此处写的4，以后其他项目请注意---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法执行该操作"));
            }
        }

        BpmNodeInfo bpmNodeInfo = bpmNodeInfoMapper.selectById(bni_id);
        if(bpmNodeInfo.getBni_id() == null) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id不存在"));
        }
        if(bpmNodeInfo.getBni_relateexecuteversion() == null || bpmNodeInfo.getBni_relateexecuteversion().equals("")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "主键id逻辑错误,该信息为基础工作流节点信息,指令拒绝"));
        }
        int count = bpmNodeInfoMapper.deleteById(bni_id);
        if(count != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "删除基础工作流-基础节点信息失败"));
        }
        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_executeversion(bpmNodeInfo.getBni_relateexecuteversion());
        List<BpmInfoVo> listBpmInfoVo = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        if(listBpmInfoVo.size() == 0) {
            return ResultUtil.result(Code.OK.getCode(), "无有效关联的基础工作流,此为无效基础节点,信息删除成功");
        }
        for(BpmInfoVo bpmInfoVo: listBpmInfoVo) {
            String orderNumber = bpmInfoVo.getBni_ordernumber();
            String orderNumberNow = bpmNodeInfo.getBni_ordernumber();
            if(orderNumber.split("\\.").length > orderNumberNow.split("\\.").length) {
                if(orderNumber.substring(0, orderNumberNow.length()).equals(orderNumberNow)) {
                    bpmNodeInfoMapper.deleteById(bpmInfoVo.getBni_id());
                }
            }
            //TODO 此处未处理平级的情况,以后去做
        }
        return ResultUtil.result(Code.OK.getCode(), "删除基础工作流-基础节点信息成功");
    }

    //预启动工作流（生成执行流和执行节点，但处于草稿状态）
    @Override
    public Object preStartBpm(String bpm_version, String userName) {
        User user = new User();
        user.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listUserRole.size() > 0) {
            if(listUserRole.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请修改---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法启动工作流程"));
            }
        }

        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_version(bpm_version);
        List<BpmInfoVo> listBasic = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        List<BpmInfoVo> listExecute = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        if(listBasic.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,未创建相应工作流"));
        }
        if(listExecute.size() > 0) {
            BpmInfoVo bpmInfoVo = listExecute.get(0);
            if(!bpmInfoVo.getBpm_executestatus().equals("已结束") && !bpmInfoVo.getBpm_executestatus().equals("预启动")) {
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,流程正在进行中"));
            }
            if(bpmInfoVo.getBpm_executestatus().equals("预启动")) {
                this.delExecuteBpm(bpmInfoVo.getBpm_id(), userName);//清除原有预启动信息，重新预启动
            }
        }

        BpmBasic bpmBasic = new BpmBasic();
        bpmBasic.setBpm_name(listBasic.get(0).getBpm_name());
        bpmBasic.setBpm_classification(listBasic.get(0).getBpm_classification());
        bpmBasic.setBpm_describe(listBasic.get(0).getBpm_describe());
        bpmBasic.setBpm_version(bpm_version);
        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmBasic.setBpm_createtime(dateStr);
        String executeVersionStr = UUID.randomUUID().toString();
        bpmBasic.setBpm_executeversion(executeVersionStr);
        bpmBasic.setBpm_relateversion(bpm_version);
        bpmBasic.setBpm_executestatus("预启动");
        bpmBasic.setBpm_nownodeid("1");
        bpmBasic.setBpm_noticemode(listBasic.get(0).getBpm_noticemode());
        bpmBasic.setBpm_eventflag("");
        int bpmBasicInsertCount = bpmInfoMapper.insert(bpmBasic);
        if(bpmBasicInsertCount != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,创建预启动工作流不成功"));
        }
        for(BpmInfoVo bpmInfoVo : listBasic) {
            BpmNodeInfo bpmNodeInfo = new BpmNodeInfo();
            bpmNodeInfo.setBni_name(bpmInfoVo.getBni_name());
            bpmNodeInfo.setBni_ordernumber(bpmInfoVo.getBni_ordernumber());
            bpmNodeInfo.setBni_describe(bpmInfoVo.getBni_describe());
            bpmNodeInfo.setBni_relatebasicversion(bpmInfoVo.getBni_relatebasicversion());
            bpmNodeInfo.setBni_status(bpmInfoVo.getBni_status());
            bpmNodeInfo.setBni_createtime(dateStr);
            bpmNodeInfo.setBni_roleid(bpmInfoVo.getBni_roleid());
            bpmNodeInfo.setBni_rolechinesename(bpmInfoVo.getBni_rolechinesename());
            bpmNodeInfo.setBni_userid(bpmInfoVo.getBni_userid());
            bpmNodeInfo.setBni_userchinesename(bpmInfoVo.getBni_userchinesename());
            bpmNodeInfo.setBni_tableform(bpmInfoVo.getBni_tableform());
            bpmNodeInfo.setBni_relateexecuteversion(executeVersionStr);
            bpmNodeInfo.setBni_auditstatus("未处理");
            bpmNodeInfo.setBni_executestatus("未开始");
            bpmNodeInfo.setBni_command(bpmInfoVo.getBni_command());
            bpmNodeInfo.setBni_lon(bpmInfoVo.getBni_lon());
            bpmNodeInfo.setBni_lat(bpmInfoVo.getBni_lat());
            bpmNodeInfo.setBni_place(bpmInfoVo.getBni_place());
            bpmNodeInfo.setBni_sourceids(bpmInfoVo.getBni_sourceids());
            int bpmNodeInfoInsertCount = bpmNodeInfoMapper.insert(bpmNodeInfo);
            if(bpmNodeInfoInsertCount != 1) {
                bpmInfoMapper.deleteById(listExecute.get(listExecute.size() - 1).getBpm_id() + 1);
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,创建节点失败,执行工作流预启动创建过程已回滚"));
            }
        }
        JSONObject jsonObjectData = this.findPreStartBpm(executeVersionStr);
        return ResultUtil.result(Code.OK.getCode(), "预启动流程成功", jsonObjectData);
    }

    //安丘项目定制---供上一个接口使---当三级节点序号组后一个.后的数字达到2位则需要修改此处逻辑
    @Override
    public JSONObject findPreStartBpm(String bpm_executeversion) {
        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_executeversion(bpm_executeversion);
        List<BpmInfoVo> list = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        JSONObject jsonObjectEnd = this.changeBpmInfoVoToBpmBasicJSONObject(list.get(0));
        JSONArray jsonArrayTemp = new JSONArray();
        Map<String, JSONObject> map = new HashMap<>();
        String check = "";
        int sign = 0;
        for(BpmInfoVo bpmInfoVo: list) {
            if(bpmInfoVo.getBni_ordernumber().equals("1")) {
                jsonObjectEnd.put("bni_command", bpmInfoVo.getBni_command());
            } else if(bpmInfoVo.getBni_ordernumber().split("\\.").length == 2) {
                JSONObject jsonObjectSecondGrade = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                map.put(bpmInfoVo.getBni_ordernumber(), jsonObjectSecondGrade);
                check = bpmInfoVo.getBni_ordernumber();
                sign = 1;
            } else {
                JSONObject jsonObjectThirdGrade = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                String orderNow = bpmInfoVo.getBni_ordernumber();
                if(orderNow.substring(orderNow.length()-2).equals(".1") && !orderNow.substring(0, 3).equals("1.1") && sign == 1) {
                    int i = 1;
                    String key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                    while(!map.containsKey(key)) {
                        i++;
                        key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                    }
                    JSONObject jsonObjectTemp = map.get(key);

                    //根据bni_command字段中存在数值进行jsonArrayTemp升序排序
                    List<JSONObject> jsonObjectList = jsonArrayTemp.toJavaList(JSONObject.class);
                    Collections.sort(jsonObjectList, new Comparator<JSONObject>(){
                        @Override
                        public int compare(JSONObject o1, JSONObject o2) {
                            return o1.getString("bni_command").compareTo(o2.getString("bni_command"));
                        }
                    });
                    JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjectList));

                    /*String jsonArrayTempStr = jsonArrayTemp.toString();//这里为了解决内存刷新问题
                    JSONArray jsonArray = JSONArray.parseArray(jsonArrayTempStr);*/

                    jsonObjectTemp.put("taskInfoList", jsonArray);
                    map.put(key, jsonObjectTemp);
                    jsonArrayTemp.clear();
                    jsonArrayTemp.add(jsonObjectThirdGrade);
                    sign = 0;
                } else {
                    jsonArrayTemp.add(jsonObjectThirdGrade);
                }
            }
        }
        if(jsonArrayTemp.size() > 0) {
            JSONObject jsonObjectTemp = new JSONObject();
            String key = check;
            if(list.get(list.size()-1).getBni_ordernumber().equals(check)) {
                int i = 1;
                key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                while(!map.containsKey(key)) {
                    i++;
                    key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                }
                jsonObjectTemp = map.get(key);
            } else {
                jsonObjectTemp = map.get(key);
            }


            //根据bni_command字段中存在数值进行jsonArrayTemp升序排序
            List<JSONObject> jsonObjectList = jsonArrayTemp.toJavaList(JSONObject.class);
            Collections.sort(jsonObjectList, new Comparator<JSONObject>(){
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    return o1.getString("bni_command").compareTo(o2.getString("bni_command"));
                }
            });
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjectList));

            jsonObjectTemp.put("taskInfoList", jsonArray);
            map.put(key, jsonObjectTemp);
            jsonArrayTemp.clear();
        }
        JSONArray jsonArrayEnd = new JSONArray();
        Set<String> keySet = map.keySet();
        for(String o: keySet) {
            jsonArrayEnd.add(map.get(o));
        }
        jsonObjectEnd.put("nodeInfoList", jsonArrayEnd);
        jsonObjectEnd.put("firstNodeId", list.get(0).getBni_id());
        return jsonObjectEnd;
    }

    //安丘项目定制---查询指定基础工作流状态-泳道视图---当三级节点序号组后一个.后的数字达到2位则需要修改此处逻辑
    @Override
    public JSONObject findBpmBasicForOtherView(String bpm_version) {
        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_version(bpm_version);
        List<BpmInfoVo> list = bpmInfoMapper.findBpmBasicInfo(bpmInfoDto);
        JSONObject jsonObjectEnd = this.changeBpmInfoVoToBpmBasicJSONObject(list.get(0));
        JSONArray jsonArrayTemp = new JSONArray();
        Map<String, JSONObject> map = new HashMap<>();
        String check = "";
        for(BpmInfoVo bpmInfoVo: list) {
            if(bpmInfoVo.getBni_ordernumber().equals("1")) {
                jsonObjectEnd.put("bni_command", bpmInfoVo.getBni_command());
            } else if(bpmInfoVo.getBni_ordernumber().split("\\.").length == 2) {
                JSONObject jsonObjectSecondGrade = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                map.put(bpmInfoVo.getBni_ordernumber(), jsonObjectSecondGrade);
                check = bpmInfoVo.getBni_ordernumber();
            } else {
                JSONObject jsonObjectThirdGrade = this.changeBpmInfoVoToBpmNodeInfoJSONObject(bpmInfoVo);
                String orderNow = bpmInfoVo.getBni_ordernumber();
                if(orderNow.substring(orderNow.length()-2).equals(".1") && !orderNow.substring(0, 3).equals("1.1")) {
                    int i = 1;
                    String key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                    while(!map.containsKey(key)) {
                        i++;
                        key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                    }
                    JSONObject jsonObjectTemp = map.get(key);

                    //根据bni_command字段中存在数值进行jsonArrayTemp升序排序
                    List<JSONObject> jsonObjectList = jsonArrayTemp.toJavaList(JSONObject.class);
                    Collections.sort(jsonObjectList, new Comparator<JSONObject>(){
                        @Override
                        public int compare(JSONObject o1, JSONObject o2) {
                            return o1.getString("bni_command").compareTo(o2.getString("bni_command"));
                        }
                    });
                    JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjectList));

                    /*String jsonArrayTempStr = jsonArrayTemp.toString();//这里为了解决内存刷新问题
                    JSONArray jsonArray = JSONArray.parseArray(jsonArrayTempStr);*/

                    jsonObjectTemp.put("taskInfoList", jsonArray);
                    map.put(key, jsonObjectTemp);
                    jsonArrayTemp.clear();
                    jsonArrayTemp.add(jsonObjectThirdGrade);
                } else {
                    jsonArrayTemp.add(jsonObjectThirdGrade);
                }
            }
        }

        if(jsonArrayTemp.size() > 0) {
            JSONObject jsonObjectTemp = new JSONObject();
            String key = check;
            if(list.get(list.size()-1).getBni_ordernumber().equals(check)) {
                int i = 1;
                key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                while(!map.containsKey(key)) {
                    i++;
                    key = check.split("\\.")[0] + "." + String.valueOf(Integer.parseInt(check.split("\\.")[1]) - i);
                }
                jsonObjectTemp = map.get(key);
            } else {
                jsonObjectTemp = map.get(key);
            }


            //根据bni_command字段中存在数值进行jsonArrayTemp升序排序
            List<JSONObject> jsonObjectList = jsonArrayTemp.toJavaList(JSONObject.class);
            Collections.sort(jsonObjectList, new Comparator<JSONObject>(){
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    return o1.getString("bni_command").compareTo(o2.getString("bni_command"));
                }
            });
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjectList));

            jsonObjectTemp.put("taskInfoList", jsonArray);
            map.put(key, jsonObjectTemp);
            jsonArrayTemp.clear();
        }

        JSONArray jsonArrayEnd = new JSONArray();
        Set<String> keySet = map.keySet();
        for(String o: keySet) {
            jsonArrayEnd.add(map.get(o));
        }
        jsonObjectEnd.put("nodeInfoList", jsonArrayEnd);
        jsonObjectEnd.put("firstNodeId", list.get(0).getBni_id());
        return jsonObjectEnd;
    }

    //将预启动工作流正式启动
    @Override
    public Object startPreStartBpm(String bpm_executeversion, String bpm_describe, String bpm_eventflag, String bpm_noticemode, String userName) {
        if("".equals(bpm_describe) || null == bpm_describe || "".equals(bpm_eventflag) || null == bpm_eventflag) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "描述和事件标识必须填写"));
        }
        User user = new User();
        user.setUsername(userName);
        List<UserRoleVo> listUserRole = apiUserInfoMapper.findUserRoleInfoByUP(user);
        if(listUserRole.size() > 0) {
            if(listUserRole.get(0).getRoleid() > 4) {//【!!!重要---此出正常来讲是2，为业务需要写的4，以后其他项目请修改---注意!!!】
                throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "您的权限等级无法启动工作流程"));
            }
        }

        BpmInfoDto bpmInfoDto = new BpmInfoDto();
        bpmInfoDto.setBpm_executeversion(bpm_executeversion);
        List<BpmInfoVo> listExecute = bpmInfoMapper.findBpmExecuteInfo(bpmInfoDto);
        if(listExecute.size() == 0) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,执行流程版本号不存在"));
        }
        if(!listExecute.get(0).getBpm_executestatus().equals("预启动")) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,该执行版本号对应的流程不在预启动状态"));
        }
        BpmBasic bpmBasicForUpdateExecuteStatus = bpmInfoMapper.selectById(listExecute.get(0).getBpm_id());

        String dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        bpmBasicForUpdateExecuteStatus.setBpm_createtime(dateStr);
        bpmBasicForUpdateExecuteStatus.setBpm_executestatus("进行中");
        bpmBasicForUpdateExecuteStatus.setBpm_describe(bpm_describe);
        bpmBasicForUpdateExecuteStatus.setBpm_eventflag(bpm_eventflag);
        bpmBasicForUpdateExecuteStatus.setBpm_noticemode(bpm_noticemode);
        int bpmBasicInsertCount = bpmInfoMapper.updateById(bpmBasicForUpdateExecuteStatus);
        if(bpmBasicInsertCount != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,更新预启动工作流状态不成功"));
        }

        for(BpmInfoVo bpmInfoVo : listExecute) {
            BpmNodeInfo bpmNodeInfo = bpmNodeInfoMapper.selectById(bpmInfoVo.getBni_id());
            if(bpmInfoVo.getBni_ordernumber().split("\\.").length == 2) {
                bpmNodeInfo.setBni_executestatus("进行中");
            } else if(bpmInfoVo.getBni_ordernumber().split("\\.").length > 2) {
                bpmNodeInfo.setBni_executestatus("未开始");
            } else {
                bpmNodeInfo.setBni_executestatus("已结束");
                bpmNodeInfo.setBni_auditstatus("已处理");
            }
            //安丘项目定制 处理bni_command时间段 为具体时间
            if(!bpmInfoVo.getBni_ordernumber().equals("1") && !bpmInfoVo.getBni_command().equals("") && bpmInfoVo.getBni_command() != null) {
              String bniCommandDateStr = bpmNodeInfo.getBni_command();
              double timecompute = Double.parseDouble(bniCommandDateStr)*60*60*1000;
              Date date = new Date();
              date.setTime(date.getTime() + new Double(timecompute).intValue());
              /*Calendar calendar = Calendar.getInstance();
              calendar.setTime(new Date());
              calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + Integer.parseInt(bniCommandDateStr));*/
              String newBniCommandDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
              bpmNodeInfo.setBni_command(newBniCommandDateStr);
            }
            //------------------------------------//
            bpmNodeInfoMapper.updateById(bpmNodeInfo);
        }

        //查询创建的节点id信息并更新到主表执行流程数据中
        List<BpmNodeInfo> executeList = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, bpm_executeversion);
        String bpmNodeIdStr = "";
        for(BpmNodeInfo o : executeList) {
            if(o.getBni_executestatus().equals("进行中")) {
                if(bpmNodeIdStr.equals("")) {
                    bpmNodeIdStr = String.valueOf(o.getBni_id());
                } else {
                    bpmNodeIdStr = bpmNodeIdStr + "," + String.valueOf(o.getBni_id());
                }
            }
        }
        List<BpmBasic> listExecuteBpm = bpmInfoMapper.findBpmBasicByExecuteVersion(bpm_executeversion);
        BpmBasic bpmBasicUpdateExecuteBpmUse = listExecuteBpm.get(0);
        bpmBasicUpdateExecuteBpmUse.setBpm_nownodeid(bpmNodeIdStr);
        int countEnd = bpmInfoMapper.updateById(bpmBasicUpdateExecuteBpmUse);
        if(countEnd != 1) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "启动失败,节点进度监控失败"));
        }

        //更新主表计数信息
        List<BpmBasic> list =  bpmInfoMapper.findBpmBasicByBasicVersion(bpmBasicUpdateExecuteBpmUse.getBpm_version());
        BpmBasic bpmBasicUpdateUse = list.get(0);
        int executeCount = bpmBasicUpdateUse.getBpm_executecount();
        bpmBasicUpdateUse.setBpm_executecount(executeCount + 1);
        bpmInfoMapper.updateById(bpmBasicUpdateUse);

        //将待发送极光推送、微信、短信、邮件等消息通知必要信息放入待命并发队列
        try {
            noticeSendInfoQueue.add("bpmstart###" + bpm_executeversion);
        } catch (Exception e) {
            log.info("并发队列noticeSendInfoQueue可能已满,异常为:" + e.getMessage());
        }

        return ResultUtil.result(Code.OK.getCode(), "启动流程成功");
    }

    //查询仅基础工作流所有列表
    @Override
    public List<BpmBasic> findBpmBasicAll(BpmInfoDto bpmInfoDto) {
        List<BpmBasic> list = bpmInfoMapper.findBpmBasicAll(bpmInfoDto);
        return list;
    }

    //查询仅执行工作流所有列表
    @Override
    public List<BpmBasic> findExecuteBpmBasicAll(BpmInfoDto bpmInfoDto) {
        List<BpmBasic> list = bpmInfoMapper.findExecuteBpmBasicAll(bpmInfoDto);
        return list;
    }

    //查询角色下辖所有人员参与的相关节点任务在不同审核状态下的数量(未处理、处理中、已处理)
    @Override
    public Object statisticNodeAuditStatusNum(Integer roleid) {
        BpmNodeInfoDto bpmNodeInfoDto = new BpmNodeInfoDto();
        bpmNodeInfoDto.setBni_roleid(roleid);
        bpmNodeInfoDto.setBni_auditstatus("处理中");
        List<BpmNodeInfo> list2 = bpmInfoMapper.findBpmNodeInfoByExecuteOrAuditStatusOrUserRoleId(bpmNodeInfoDto);
        int list2size = list2.size();
        for(BpmNodeInfo bpmNodeInfo: list2) {
            List<BpmBasic> list2BpmBasic = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
            if(list2BpmBasic.size() > 0) {
                if(list2BpmBasic.get(0).getBpm_executestatus().equals("已结束")) {
                    list2size--;
                }
            }
        }
        bpmNodeInfoDto.setBni_auditstatus("已处理");
        List<BpmNodeInfo> list3 = bpmInfoMapper.findBpmNodeInfoByExecuteOrAuditStatusOrUserRoleId(bpmNodeInfoDto);
        int list3size = list3.size();
        for(BpmNodeInfo bpmNodeInfo: list3) {
            List<BpmBasic> list3BpmBasic = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
            if(list3BpmBasic.size() > 0) {
                if(list3BpmBasic.get(0).getBpm_executestatus().equals("已结束")) {
                    list3size--;
                }
            }
        }
        bpmNodeInfoDto.setBni_auditstatus("未处理");
        bpmNodeInfoDto.setBni_executestatus("进行中");
        List<BpmNodeInfo> list1 = bpmInfoMapper.findBpmNodeInfoByExecuteOrAuditStatusOrUserRoleId(bpmNodeInfoDto);
        int list1size = list1.size();
        for(BpmNodeInfo bpmNodeInfo: list1) {
            List<BpmBasic> list1BpmBasic = bpmInfoMapper.findBpmBasicByExecuteVersion(bpmNodeInfo.getBni_relateexecuteversion());
            if(list1BpmBasic.size() > 0) {
                if(list1BpmBasic.get(0).getBpm_executestatus().equals("已结束")) {
                    list1size--;
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        if(list1size > 0) {
          jsonObject.put("未处理", list1size-1);
        } else {
          jsonObject.put("未处理", list1size);
        }
        jsonObject.put("处理中", list2size);
        jsonObject.put("已处理", list3size);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), jsonObject);
    }

    //查询基础工作流树-按照分类分组
    @Override
    public Object findBpmBasicTreeGroupByClassification() {
        List<BpmBasic> list = bpmInfoMapper.findBpmBasicGroupByClassification();
        String[] groupArray = new String[list.size()];
        int i = 0;
        for(BpmBasic bpmBasic: list) {
            groupArray[i] = bpmBasic.getBpm_classification();
            i++;
        }
        JSONArray jsonArray = new JSONArray();
        for(String o: groupArray) {
            JSONObject jsonObject = new JSONObject();
            BpmInfoDto bpmInfoDto = new BpmInfoDto();
            bpmInfoDto.setBpm_classification(o);
            List<BpmBasic> list1 = bpmInfoMapper.findBpmBasicAll(bpmInfoDto);
            jsonObject.put(o, list1);
            jsonArray.add(jsonObject);
        }
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), jsonArray);
    }

    //查询指定执行工作流节点和表单的树状信息 -即使没有表单也会有节点信息
    @Override
    public Object findNodeAndFormInfoAll(String bpm_executeversion) {
        JSONArray jsonArray = new JSONArray();
        List<BpmNodeInfo> list = bpmInfoMapper.findBpmNodeInfoByExecuteVersionAndBniId(null, bpm_executeversion);
        for(BpmNodeInfo bpmNodeInfo: list) {
            if(!bpmNodeInfo.getBni_executestatus().equals("未开始")) {
                JSONObject jsonObject = JsonUti.object2JsonObject(bpmNodeInfo);
                List<NodeFormInfo> list1 = bpmInfoMapper.findNodeFormInfoByNfiNodeId(bpmNodeInfo.getBni_id());
                if(list1.size() > 0) {
                    //下面5行已在数据库层面解决，可以注释掉
                    for(int i=0; i<list1.size(); i++) {
                        if(null == list1.get(i).getNfi_lon() || null == list1.get(i).getNfi_lat()) {
                            list1.get(i).setNfi_lon(0.0);
                            list1.get(i).setNfi_lat(0.0);
                        }
                    }
                    jsonObject.put("nodeFormList", list1);
                } else {
                    jsonObject.put("nodeFormList", new ArrayList<>());
                }
                jsonArray.add(jsonObject);
            }
        }
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), jsonArray);
    }
}