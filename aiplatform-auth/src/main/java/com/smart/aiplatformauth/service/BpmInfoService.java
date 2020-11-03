package com.smart.aiplatformauth.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.smart.aiplatformauth.dto.BpmInfoDto;
import com.smart.aiplatformauth.model.BpmBasic;
import com.smart.aiplatformauth.model.BpmNodeInfo;
import com.smart.aiplatformauth.model.NodeFormInfo;
import com.smart.aiplatformauth.vo.BpmInfoVo;
import com.smart.aiplatformauth.vo.BpmNodeFormInfoVo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * BPM流程信息服务接口类
 * @author: chengjz
 */
public interface BpmInfoService extends IService<BpmBasic> {

    //查询基础工作流和其所有节点信息
    List<BpmInfoVo> findBpmBasicInfo(BpmInfoDto bpmInfoDto);

    //查询执行工作流和其所有节点信息
    Object findBpmExecuteInfo(BpmInfoDto bpmInfoDto);

    //查询执行工作流和其所有节点信息---安丘定制应急详细过程
    Object findBpmExecuteInfoForProcessCustom(BpmInfoDto bpmInfoDto);

    //查询节点对应的表单上报信息
    List<BpmNodeFormInfoVo> findNodeAndFormInfo(String bni_relateexecuteversion, Integer bni_id, String nfi_type, Integer nfi_audituserid);

    //根据工作流执行版本查询所有上报信息
    List<NodeFormInfo> findNodeFormInfoByBpmExecuteVersion(String nfi_belongbpmversion, String nfi_type, Integer nfi_audituserid);

    //启动工作流
    Object startBpm(String bpm_version, String bpm_describe, String bpm_eventflag, String bpm_noticemode, String userName);

    //工作流某节点提交审核（状态变更）
    Object submitBpm(Integer bni_id, String bni_executestatus, String userName);

    //强制结束工作流（用于流程各节点未全部完结时的结束）
    Object forceStopBpm(String bpm_executeversion, String userName, String sign);

    //通过用户id变更流程节点中的用户和角色信息
    Object editBpmNodeInfoByUserId(Integer bni_id, Integer userid);

    //根据用户id查询所有相关正在执行节点信息
    List<BpmNodeInfo> findExecuteBpmNodeInfoByUserId(Integer bni_userid, String bni_executestatus, String bni_auditstatus, String bni_name, String bni_place);

    //新增节点表单信息（提交、上报信息）
    Object addNodeFormInfo(NodeFormInfo nodeFormInfo, MultipartFile[] file1, MultipartFile[] file2, String userName);

    //新增基础工作流信息
    Object addBpmBasic(BpmBasic bpmBasic, String userName);

    //更新基础工作流信息
    Object editBpmBasic(BpmBasic bpmBasic, String userName);

    //删除基础工作流及其关联信息
    Object delBpmBasic(Integer bpm_id, String userName);

    //删除执行流程及其关联信息
    Object delExecuteBpm(Integer bpm_id, String userName);

    //新增基础工作流节点信息
    Object addBasicBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName);

    //更新基础工作流节点信息
    Object editBasicBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName);

    //删除基础工作流节点信息
    Object delBasicBpmNodeInfo(Integer bni_id, String userName);

    //新增执行工作流节点信息
    Object addExecuteBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName);

    //更新执行工作流节点信息
    Object editExecuteBpmNodeInfo(BpmNodeInfo bpmNodeInfo, String userName);

    //删除执行工作流节点信息
    Object delExecuteBpmNodeInfo(Integer bni_id, String userName);

    //预启动工作流（生成执行流和执行节点，但处于草稿状态）
    Object preStartBpm(String bpm_version, String userName);

    //查询指定执行工作流状态-安丘项目定制
    JSONObject findPreStartBpm(String bpm_executeversion);

    //查询指定基础工作流状态-安丘项目定制泳道视图
    JSONObject findBpmBasicForOtherView(String bpm_version);

    //将预启动工作流正式启动
    Object startPreStartBpm(String bpm_executeversion, String bpm_describe, String bpm_eventflag, String bpm_noticemode, String userName);

    //查询仅基础工作流所有列表
    List<BpmBasic> findBpmBasicAll(BpmInfoDto bpmInfoDto);

    //查询仅执行工作流所有列表
    List<BpmBasic> findExecuteBpmBasicAll(BpmInfoDto bpmInfoDto);

    //查询角色下辖所有人员参与的相关节点任务在不同审核状态下的数量(未处理、处理中、已处理)
    Object statisticNodeAuditStatusNum(Integer roleid);

    //查询基础工作流树-按照分类分组
    Object findBpmBasicTreeGroupByClassification();

    //交接班
    Object changeLeader(Integer userIdOld, Integer userIdNew);

    //查询指定执行工作流节点和表单的树状信息 -即使没有表单也会有节点信息
    Object findNodeAndFormInfoAll(String bpm_executeversion);
}
