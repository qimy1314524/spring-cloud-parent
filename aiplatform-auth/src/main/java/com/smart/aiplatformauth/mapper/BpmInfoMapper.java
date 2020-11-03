package com.smart.aiplatformauth.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.smart.aiplatformauth.dto.BpmInfoDto;
import com.smart.aiplatformauth.dto.BpmNodeInfoDto;
import com.smart.aiplatformauth.model.BpmBasic;
import com.smart.aiplatformauth.model.BpmNodeInfo;
import com.smart.aiplatformauth.model.NodeFormInfo;
import com.smart.aiplatformauth.vo.BpmInfoVo;
import com.smart.aiplatformauth.vo.BpmNodeFormInfoVo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * BPM工作流信息mapper服务接口类
 * @author: chengjz
 */
@Repository
//@Mapper
public interface BpmInfoMapper extends BaseMapper<BpmBasic> {

    //查询基础工作流列表所有信息
    List<BpmBasic> findBpmBasicAll(@Param("bpmInfoDto") BpmInfoDto bpmInfoDto);

    //查询执行工作流列表所有信息
    List<BpmBasic> findExecuteBpmBasicAll(@Param("bpmInfoDto") BpmInfoDto bpmInfoDto);

    //查询基础工作流和其所有节点信息
    List<BpmInfoVo> findBpmBasicInfo(@Param("bpmInfoDto") BpmInfoDto bpmInfoDto);

    //查询执行工作流和其所有节点信息
    List<BpmInfoVo> findBpmExecuteInfo(@Param("bpmInfoDto") BpmInfoDto bpmInfoDto);

    //查询节点对应的表单上报信息
    List<BpmNodeFormInfoVo> findNodeAndFormInfo(@Param("bni_relateexecuteversion") String bni_relateexecuteversion, @Param("bni_id") Integer bni_id, @Param("nfi_type") String nfi_type, @Param("nfi_audituserid") Integer nfi_audituserid);

    //根据工作流执行版本查询所有上报信息
    List<NodeFormInfo> findNodeFormInfoByBpmExecuteVersion(@Param("nfi_belongbpmversion") String nfi_belongbpmversion, @Param("nfi_type") String nfi_type, @Param("nfi_audituserid") Integer nfi_audituserid);

    //根据节点id查询所有上报信息
    List<NodeFormInfo> findNodeFormInfoByNfiNodeId(@Param("nfi_nodeid") Integer nfi_nodeid);

    //根据版本号查询基础工作流信息
    List<BpmBasic> findBpmBasicByBasicVersion(@Param("bpm_version") String bpm_version);

    //根据版本号查询执行工作流信息
    List<BpmBasic> findBpmBasicByExecuteVersion(@Param("bpm_executeversion") String bpm_executeversion);

    //根据执行版本号和节点id查询所有相关节点信息
    List<BpmNodeInfo> findBpmNodeInfoByExecuteVersionAndBniId(@Param("bni_id") Integer bni_id, @Param("bni_relateexecuteversion") String bni_relateexecuteversion);

    //根据用户id查询所有相关正在执行节点信息
    List<BpmNodeInfo> findExecuteBpmNodeInfoByUserIdOrRoleid(@Param("bni_userid") Integer bni_userid, @Param("bni_roleid") Integer bni_roleid, @Param("bni_executestatus") String bni_executestatus, @Param("bni_auditstatus") String bni_auditstatus, @Param("bni_name") String bni_name, @Param("bni_place") String bni_place);

    //根据执行流程版本信息删除所有相关表单上报信息
    boolean deleteNodeFormInfoByExecuteVersion(@Param("nfi_belongbpmversion") String nfi_belongbpmversion);

    //（可以此列表结果数量进行统计）查询节点的不同执行状态或审核状态对应的节点数据,若传递角色id（或用户id）则根据角色id查询所有角色下辖人员（或用户）所参与的相关节点
    List<BpmNodeInfo> findBpmNodeInfoByExecuteOrAuditStatusOrUserRoleId(@Param("bpmNodeInfoDto") BpmNodeInfoDto bpmNodeInfoDto);

    //查询基础工作流按分类分组取id最大的列表
    List<BpmBasic> findBpmBasicGroupByClassification();
}
