package com.smart.aiplatformauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.smart.aiplatformauth.annotation.CurrentUser;
import com.smart.aiplatformauth.dto.*;
import com.smart.aiplatformauth.model.BpmBasic;
import com.smart.aiplatformauth.model.BpmNodeInfo;
import com.smart.aiplatformauth.model.NodeFormInfo;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.BpmInfoService;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.smart.aiplatformauth.vo.BpmInfoVo;
import com.smart.aiplatformauth.vo.BpmNodeFormInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Bpm工作流Controller  ---流程暂未支持排他网关处理（以后去做，节点中status值为0代表草稿状态、1代表并行网关、2代表排他网关） ---另 执行流程的bpm_executestatus未开始状态没用上，为预留的存草稿等操作使用
 * @author: chengjz
 */
@RestController
@Api(value = "AiplatformBpmApi")
@Validated
public class BpmController {

    @Autowired
    private BpmInfoService bpmInfoService;

    /**
     * 查询仅基础工作流所有列表
     * @return
     */
    @GetMapping("/findBpmBasicAll")
    @ApiOperation(value = "查询仅基础工作流所有列表", notes = "工作流名称、分类、基础版本号可作为筛选条件")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bpm_name", dataType = "String", value = "工作流名称", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_classification", dataType = "String", value = "工作流分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_version", dataType = "String", value = "工作流基础版本号", required = false)
    })
    public Object findBpmBasicAll(BpmInfoDto bpmInfoDto) {
        List<BpmBasic> list = bpmInfoService.findBpmBasicAll(bpmInfoDto);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }

    /**
     * 查询仅执行工作流所有列表
     * @return
     */
    @GetMapping("/findExecuteBpmBasicAll")
    @ApiOperation(value = "查询仅执行工作流所有列表", notes = "工作流名称、分类、执行版本号可作为筛选条件")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bpm_name", dataType = "String", value = "工作流名称", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_classification", dataType = "String", value = "工作流分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_executeversion", dataType = "String", value = "工作流执行版本号", required = false)
    })
    public Object findExecuteBpmBasicAll(BpmInfoDto bpmInfoDto) {
        List<BpmBasic> list = bpmInfoService.findExecuteBpmBasicAll(bpmInfoDto);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }


    /**
     * 查询基础工作流和其所有节点信息
     * @return
     */
    @GetMapping("/findBpmBasicInfo")
    @ApiOperation(value = "查询基础工作流和其所有节点信息", notes = "工作流名称、分类、基础版本号、节点名称可作为筛选条件")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bpm_name", dataType = "String", value = "工作流名称", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_classification", dataType = "String", value = "工作流分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_version", dataType = "String", value = "工作流基础版本号", required = false),
        @ApiImplicitParam(paramType = "query", name = "bni_name", dataType = "String", value = "节点名称", required = false)
    })
    public Object findBpmBasicInfo(BpmInfoDto bpmInfoDto) {
        List<BpmInfoVo> list = bpmInfoService.findBpmBasicInfo(bpmInfoDto);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }

    /**
     * 查询某个执行工作流和其所有节点信息
     * @return
     */
    @GetMapping("/findBpmExecuteInfo")
    @ApiOperation(value = "查询执行工作流和其所有节点信息", notes = "支持动态参数筛选,返回值data为列表排序数据,data1为树状数据")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bpm_name", dataType = "String", value = "工作流名称", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_classification", dataType = "String", value = "工作流分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_version", dataType = "String", value = "工作流基础版本号", required = false),
        @ApiImplicitParam(paramType = "query", name = "bpm_executeversion", dataType = "String", value = "工作流执行版本号-【注意:若不传则返回所有正在执行流程】", required = true),
        @ApiImplicitParam(paramType = "query", name = "bpm_executestatus", dataType = "String", value = "工作流执行状态-预启动/未开始/进行中/已结束", required = false),
        @ApiImplicitParam(paramType = "query", name = "bni_name", dataType = "String", value = "节点名称", required = false),
        @ApiImplicitParam(paramType = "query", name = "bni_executestatus", dataType = "String", value = "节点执行状态-未开始/进行中/已结束", required = false),
        @ApiImplicitParam(paramType = "query", name = "bni_auditstatus", dataType = "String", value = "节点审核状态-未处理/处理中/已处理", required = false),
        @ApiImplicitParam(paramType = "query", name = "sign", dataType = "String", value = "标识位-只要不为null和空字符串就按照bpm_name分组且只取每组第一条(多用于列表查询所有执行工作流时)", required = false)
    })
    public Object findBpmExecuteInfo(BpmInfoDto bpmInfoDto) {
        Object object = bpmInfoService.findBpmExecuteInfo(bpmInfoDto);
        return object;
    }

    /**
     * 查询执行工作流和其所有节点信息---安丘定制应急详细过程
     * @return
     */
    @GetMapping("/findBpmExecuteInfoForProcessCustom")
    @ApiOperation(value = "查询执行工作流和其所有节点信息---安丘定制应急详细过程", notes = "支持动态参数筛选,返回值data为列表排序数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "bpm_name", dataType = "String", value = "工作流名称", required = false),
            @ApiImplicitParam(paramType = "query", name = "bpm_classification", dataType = "String", value = "工作流分类", required = false),
            @ApiImplicitParam(paramType = "query", name = "bpm_version", dataType = "String", value = "工作流基础版本号", required = false),
            @ApiImplicitParam(paramType = "query", name = "bpm_executeversion", dataType = "String", value = "工作流执行版本号-【注意:若不传则返回所有正在执行流程】", required = true),
            @ApiImplicitParam(paramType = "query", name = "bpm_executestatus", dataType = "String", value = "工作流执行状态-预启动/未开始/进行中/已结束", required = false),
            @ApiImplicitParam(paramType = "query", name = "bni_name", dataType = "String", value = "节点名称", required = false),
            @ApiImplicitParam(paramType = "query", name = "bni_executestatus", dataType = "String", value = "节点执行状态-未开始/进行中/已结束", required = false),
            @ApiImplicitParam(paramType = "query", name = "bni_auditstatus", dataType = "String", value = "节点审核状态-未处理/处理中/已处理", required = false),
            @ApiImplicitParam(paramType = "query", name = "sign", dataType = "String", value = "标识位-只要不为null和空字符串就按照bpm_name分组且只取每组第一条(多用于列表查询所有执行工作流时)", required = false)
    })
    public Object findBpmExecuteInfoForProcessCustom(BpmInfoDto bpmInfoDto) {
        Object object = bpmInfoService.findBpmExecuteInfoForProcessCustom(bpmInfoDto);
        return object;
    }

    /**
     * 查询节点对应的表单上报信息
     * @return
     */
    @GetMapping("/findNodeAndFormInfo")
    @ApiOperation(value = "查询节点对应的表单上报信息", notes = "传递执行工作流版本且不传递节点id则查询所有，不传递执行工作流版本且传递节点id则查询指定节点上报信息")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "bni_relateexecuteversion", dataType = "String", value = "执行工作流版本号", required = false),
        @ApiImplicitParam(paramType = "query", name = "bni_id", dataType = "Integer", value = "节点id", required = false),
        @ApiImplicitParam(paramType = "query", name = "nfi_type", dataType = "String", value = "上报信息分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "nfi_audituserid", dataType = "Integer", value = "上报用户", required = false)
    })
    public Object findNodeAndFormInfo(String bni_relateexecuteversion, Integer bni_id, String nfi_type, Integer nfi_audituserid) {
        List<BpmNodeFormInfoVo> list = bpmInfoService.findNodeAndFormInfo(bni_relateexecuteversion, bni_id, nfi_type, nfi_audituserid);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }

    /**
     * 根据工作流执行版本查询所有上报信息
     * @return
     */
    @GetMapping("/findNodeFormInfoByBpmExecuteVersion")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType = "query", name = "nfi_belongbpmversion", dataType = "Integer", value = "所属执行工作流版本号", required = true),
        @ApiImplicitParam(paramType = "query", name = "nfi_type", dataType = "String", value = "上报信息分类", required = false),
        @ApiImplicitParam(paramType = "query", name = "nfi_audituserid", dataType = "Integer", value = "上报用户", required = false)
    })
    @ApiOperation(value = "根据工作流执行版本查询所有上报信息", notes = "参数所属执行流程版本号nfi_belongbpmversion必传")
    public Object findNodeFormInfoByBpmExecuteVersion(@NotNull String nfi_belongbpmversion, String nfi_type, Integer nfi_audituserid) {
        List<NodeFormInfo> list = bpmInfoService.findNodeFormInfoByBpmExecuteVersion(nfi_belongbpmversion, nfi_type, nfi_audituserid);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }

    /**
     * 启动工作流
     * @param startBpmDto
     * @return
     */
    @PostMapping("/startBpm")
    @ApiOperation(value = "启动工作流", notes = "body体传参,除通知模式参数外其他参数必传\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_version\", dataType = \"String\", value = \"工作流基础版本号\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_describe\", dataType = \"String\", value = \"工作流描述\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_eventflag\", dataType = \"String\", value = \"事件标识-如政府字【202009-01】号应急令\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_noticemode\", dataType = \"String\", value = \"通知模式\", required = false)\n"
        + "    })】")
    public Object startBpm(@RequestBody @Valid StartBpmDto startBpmDto, @CurrentUser String userName) {
        Object object = bpmInfoService.startBpm(startBpmDto.getBpm_version(),startBpmDto.getBpm_describe(),startBpmDto.getBpm_eventflag(),startBpmDto.getBpm_noticemode(), userName);
        return object;
    }

    /**
     * 工作流某节点提交审核（状态变更）
     * @return
     */
    @PostMapping("/submitBpm")
    @ApiOperation(value = "工作流某节点提交审核（状态变更）", notes = "body体传参\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"节点id\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_executestatus\", dataType = \"String\", value = \"变更的状态\", required = true)\n"
        + "    })】")
    public Object submitBpm(@RequestBody @Valid SubmitBpmDto submitBpmDto, @CurrentUser String userName) {
        Object object = bpmInfoService.submitBpm(submitBpmDto.getBni_id(), submitBpmDto.getBni_executestatus(), userName);
        return object;
    }

    /**
     * (强制/正常)结束工作流
     * @param bpm_executeversion
     * @param userName
     * @return
     */
    @PostMapping("/forceStopBpm/{bpm_executeversion}/{sign}")
    @ApiOperation(value = "(强制/正常)结束工作流", notes = "url动态参数传递执行流程版本号bpm_executeversion和sign,[/forceStopBpm/{bpm_executeversion}/{sign}],字符串sign不为1则执行强制结束,为1则正常结束，会验证所有节点是否已结束")
    public Object forceStopBpm(@PathVariable String bpm_executeversion, @PathVariable String sign, @CurrentUser String userName) {
        Object object = bpmInfoService.forceStopBpm(bpm_executeversion, sign, userName);
        return object;
    }

    /**
     * 通过用户id变更流程节点中的用户和角色信息（也可用于为节点指派负责人或处理人）
     * @param editBpmNodeInfoDto
     * @return
     */
    @PostMapping("/editBpmNodeInfoByUserId")
    @ApiOperation(value = "通过用户id变更流程节点中的用户和角色信息（也可用于为节点指派负责人或处理人）", notes = "body体传参\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"节点id\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"userid\", dataType = \"Integer\", value = \"要变更为的用户id或要指派负责人的用户id\", required = true)\n"
        + "    })】")
    public Object editBpmNodeInfoByUserId(@RequestBody @Valid EditBpmNodeInfoDto editBpmNodeInfoDto) {
        Object object = bpmInfoService.editBpmNodeInfoByUserId(editBpmNodeInfoDto.getBni_id(), editBpmNodeInfoDto.getUserid());
        return object;
    }

    /**
     * 根据用户id查询所有相关正在执行节点信息
     * @param userid
     * @return
     */
    @GetMapping("/findExecuteBpmNodeInfoByUserId")
    @ApiOperation(value = "根据用户id查询所有相关正在执行节点信息", notes = "参数用户ID为Integer类型userid,可选过滤参数bni_executestatus节点执行状态：未开始/进行中/已结束, 可选过滤参数bni_auditstatus节点审核状态：未处理/处理中/已处理, 可选过滤参数bni_name名称和bni_place地点")
    public Object findExecuteBpmNodeInfoByUserId(@NotNull Integer userid, String bni_executestatus, String bni_auditstatus, String bni_name, String bni_place) {
        List<BpmNodeInfo> list = bpmInfoService.findExecuteBpmNodeInfoByUserId(userid, bni_executestatus, bni_auditstatus, bni_name, bni_place);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list);
    }

    /**
     * 新增节点表单信息（提交、上报信息）
     * @param nodeFormInfo
     * @param file1
     * @param file2
     * @return
     */
    @PostMapping("/addNodeFormInfo")
    @ApiOperation(value = "新增节点表单信息（提交、上报信息）", notes = "表单信息body体传参,通过file1上传的图片,通过file2上传文件,MultipartFile[]文件流数组的形式接收\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_title\", dataType = \"String\", value = \"标题\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_summary\", dataType = \"String\", value = \"概述\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_content\", dataType = \"String\", value = \"内容\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_opinion\", dataType = \"String\", value = \"意见\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_nodeid\", dataType = \"Integer\", value = \"所属执行节点id\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_belongbpmversion\", dataType = \"String\", value = \"所属执行流程版本号\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"nfi_type\", dataType = \"String\", value = \"信息类型\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"file1\", dataType = \"MultipartFile\", value = \"图片组\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"form\", name = \"file2\", dataType = \"MultipartFile\", value = \"文件组\", required = false)\n"
        + "    })】")
    public Object addNodeFormInfo(NodeFormInfo nodeFormInfo, MultipartFile[] file1, MultipartFile[] file2, @CurrentUser String userName) {
        Object object = bpmInfoService.addNodeFormInfo(nodeFormInfo, file1, file2, userName);
        return object;
    }

    /**
     * 新增基础工作流信息
     * @param bpmBasic
     * @return
     */
    @PostMapping("/addBpmBasic")
    @ApiOperation(value = "新增基础工作流信息", notes = "body体传参,名称、分类、描述参数必传,通知模式选传,仅需传递该4个字段信息\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_name\", dataType = \"String\", value = \"基础工作流名称\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_classification\", dataType = \"String\", value = \"基础工作流分类\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_describe\", dataType = \"String\", value = \"基础工作流描述\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_noticemode\", dataType = \"String\", value = \"工作流消息通知模式(自动模式/手动模式)\", required = false)\n"
        + "    })】")
    public Object addBpmBasic(@RequestBody BpmBasic bpmBasic, @CurrentUser String userName) {
       Object object = bpmInfoService.addBpmBasic(bpmBasic,userName);
       return object;
    }

    /**
     * 更新基础工作流信息
     * @param bpmBasic
     * @return
     */
    @PostMapping("/editBpmBasic")
    @ApiOperation(value = "更新基础工作流信息", notes = "主键bpm_id必传,支持更新的字段见说明\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_id\", dataType = \"Integer\", value = \"基础工作流主键\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_name\", dataType = \"String\", value = \"基础工作流名称\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_classification\", dataType = \"String\", value = \"基础工作流分类\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_describe\", dataType = \"String\", value = \"基础工作流描述\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_status\", dataType = \"String\", value = \"基础工作流启用状态\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_noticemode\", dataType = \"String\", value = \"工作流消息通知模式(自动模式/手动模式)\", required = false)\n"
        + "    })】")
    public Object editBpmBasic(@RequestBody BpmBasic bpmBasic, @CurrentUser String userName) {
        Object object = bpmInfoService.editBpmBasic(bpmBasic, userName);
        return object;
    }

    /**
     * 删除基础工作流及其关联信息
     * @param bpm_id
     * @return
     */
    @PostMapping("/delBpmBasic")
    @ApiOperation(value = "删除基础工作流及其关联信息", notes = "url传参参数为主键bpm_id")
    public Object delBpmBasic(@NotNull Integer bpm_id, @CurrentUser String userName) {
        Object object = bpmInfoService.delBpmBasic(bpm_id, userName);
        return object;
    }

    /**
     * 删除执行流程及其关联信息
     * @param bpm_id
     * @return
     */
    @PostMapping("/delExecuteBpm")
    @ApiOperation(value = "删除执行流程及其关联信息", notes = "url传参参数为主键bpm_id")
    public Object delExecuteBpm(@NotNull Integer bpm_id, @CurrentUser String userName) {
        Object object = bpmInfoService.delExecuteBpm(bpm_id, userName);
        return object;
    }

    /**
     * 新增基础工作流节点信息
     * @param bpmNodeInfo
     * @return
     */
    @PostMapping("/addBasicBpmNodeInfo")
    @ApiOperation(value = "新增基础工作流节点信息", notes = "body体传参\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"父节点id,不传则默认是初始节点\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_name\", dataType = \"String\", value = \"节点名称\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_describe\", dataType = \"String\", value = \"节点描述\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_relatebasicversion\", dataType = \"String\", value = \"关联基础工作流版本号\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_status\", dataType = \"String\", value = \"状态-0草稿/1启用且为并行网关/2启用且为排它网关\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_command\", dataType = \"String\", value = \"目标要求时限命令等\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_userid\", dataType = \"Integer\", value = \"绑定用户ID\", required = false)\n"
        + "    })】")
    public Object addBasicBpmNodeInfo(@RequestBody BpmNodeInfo bpmNodeInfo, @CurrentUser String userName) {
        Object object = bpmInfoService.addBasicBpmNodeInfo(bpmNodeInfo, userName);
        return object;
    }

    /**
     * 更新基础工作流节点信息
     * @param bpmNodeInfo
     * @param userName
     * @return
     */
    @PostMapping("/editBasicBpmNodeInfo")
    @ApiOperation(value = "更新基础工作流节点信息", notes = "body体传参-节点ID必传\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"要更新的节点id\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_name\", dataType = \"String\", value = \"节点名称\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_describe\", dataType = \"String\", value = \"节点描述\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_status\", dataType = \"String\", value = \"状态-0草稿/1启用且为并行网关/2启用且为排它网关\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_userid\", dataType = \"Integer\", value = \"绑定用户ID\", required = false)\n"
        + "    })】")
    public Object editBasicBpmNodeInfo(@RequestBody BpmNodeInfo bpmNodeInfo, @CurrentUser String userName) {
        Object object = bpmInfoService.editBasicBpmNodeInfo(bpmNodeInfo, userName);
        return object;
    }

    /**
     * 删除基础工作流节点信息
     * @param bni_id
     * @return
     */
    @PostMapping("/delBasicBpmNodeInfo")
    @ApiOperation(value = "删除基础工作流节点信息", notes = "参数为主键bni_id")
    public Object delBasicBpmNodeInfo(@NotNull Integer bni_id, @CurrentUser String userName) {
        Object object = bpmInfoService.delBasicBpmNodeInfo(bni_id, userName);
        return object;
    }

    /**
     * 新增执行工作流节点信息
     * @param bpmNodeInfo
     * @return
     */
    @PostMapping("/addExecuteBpmNodeInfo")
    @ApiOperation(value = "新增执行工作流节点信息", notes = "body体传参\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"父节点id,不传则默认是初始节点\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_name\", dataType = \"String\", value = \"节点名称\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_describe\", dataType = \"String\", value = \"节点描述\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_relateexecuteversion\", dataType = \"String\", value = \"关联执行工作流版本号\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_status\", dataType = \"String\", value = \"状态-0草稿/1启用且为并行网关/2启用且为排它网关\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_command\", dataType = \"String\", value = \"目标要求时限命令等\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_userid\", dataType = \"Integer\", value = \"绑定用户ID\", required = false)\n"
        + "    })】")
    public Object addExecuteBpmNodeInfo(@RequestBody BpmNodeInfo bpmNodeInfo, @CurrentUser String userName) {
        Object object = bpmInfoService.addExecuteBpmNodeInfo(bpmNodeInfo, userName);
        return object;
    }

    /**
     * 更新执行工作流节点信息
     * @param bpmNodeInfo
     * @param userName
     * @return
     */
    @PostMapping("/editExecuteBpmNodeInfo")
    @ApiOperation(value = "更新执行工作流节点信息", notes = "body体传参-节点ID必传\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_id\", dataType = \"Integer\", value = \"要更新的节点id\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_name\", dataType = \"String\", value = \"节点名称\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_describe\", dataType = \"String\", value = \"节点描述\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_status\", dataType = \"String\", value = \"状态-0草稿/1启用且为并行网关/2启用且为排它网关\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_command\", dataType = \"String\", value = \"目标要求时限命令等\", required = false),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bni_userid\", dataType = \"Integer\", value = \"绑定用户ID\", required = false)\n"
        + "    })】")
    public Object editExecuteBpmNodeInfo(@RequestBody BpmNodeInfo bpmNodeInfo, @CurrentUser String userName) {
        Object object = bpmInfoService.editExecuteBpmNodeInfo(bpmNodeInfo, userName);
        return object;
    }

    /**
     * 删除执行工作流节点信息
     * @param bni_id
     * @return
     */
    @PostMapping("/delExecuteBpmNodeInfo")
    @ApiOperation(value = "删除执行工作流节点信息", notes = "参数为主键bni_id")
    public Object delExecuteBpmNodeInfo(Integer bni_id, @CurrentUser String userName) {
        Object object = bpmInfoService.delExecuteBpmNodeInfo(bni_id, userName);
        return object;
    }

    /**
     * 预启动工作流（生成执行流和执行节点，但处于草稿状态）
     * @param bpm_version
     * @return
     */
    @PostMapping("/preStartBpm")
    @ApiOperation(value = "预启动工作流（生成执行流和执行节点，但处于草稿状态）", notes = "参数为依据的基础工作流版本号bpm_version")
    public Object preStartBpm(@NotNull String bpm_version, @CurrentUser String userName) {
        Object object = bpmInfoService.preStartBpm(bpm_version, userName);
        return object;
    }

    /**
     * 将预启动工作流正式启动
     * @param startPreStartBpmDto
     * @param userName
     * @return
     */
    @PostMapping("/startPreStartBpm")
    @ApiOperation(value = "将预启动工作流正式启动", notes = "body体传参,除通知模式参数外其他参数必传\n【@ApiImplicitParams({\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_executeversion\", dataType = \"String\", value = \"工作流基础版本号\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_describe\", dataType = \"String\", value = \"工作流描述\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_eventflag\", dataType = \"String\", value = \"事件标识-如政府字【202009-01】号应急令\", required = true),\n"
        + "        @ApiImplicitParam(paramType = \"body\", name = \"bpm_noticemode\", dataType = \"String\", value = \"通知模式\", required = false)\n"
        + "    })】")
    public Object startPreStartBpm(@RequestBody @Valid StartPreStartBpmDto startPreStartBpmDto, String userName) {
      Object object = bpmInfoService.startPreStartBpm(startPreStartBpmDto.getBpm_executeversion(), startPreStartBpmDto.getBpm_describe(), startPreStartBpmDto.getBpm_eventflag(), startPreStartBpmDto.getBpm_noticemode(), userName);
      return object;
    }

    /**
     * 查询指定执行工作流状态-安丘项目定制
     * @param bpm_executeversion
     * @return
     */
    @GetMapping("/findOneExecuteBpmInfoForAnQiuEmergency")
    @ApiOperation(value = "查询指定执行工作流状态-安丘项目定制", notes = "参数为执行工作流版本号bpm_executeversion")
    public Object findOneExecuteBpmInfoForAnQiuEmergency(String bpm_executeversion) {
        JSONObject jsonObjectInfo = bpmInfoService.findPreStartBpm(bpm_executeversion);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), jsonObjectInfo);
    }

    /**
     * 查询指定基础工作流状态-泳道视图
     * @param bpm_version
     * @return
     */
    @GetMapping("/findBpmBasicForOtherView")
    @ApiOperation(value = "查询指定基础工作流状态-泳道视图", notes = "参数为基础工作流版本号bpm_version")
    public Object findBpmBasicForOtherView(String bpm_version) {
        JSONObject jsonObjectInfo = bpmInfoService.findBpmBasicForOtherView(bpm_version);
        return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), jsonObjectInfo);
    }

    /**
     * 查询角色下辖所有人员参与的相关节点任务在不同审核状态下的数量(未处理、处理中、已处理)
     * @param roleid
     * @return
     */
    @GetMapping("/statisticNodeAuditStatusNum")
    @ApiOperation(value = "查询角色下辖所有人员参与的相关节点任务在不同审核状态下的数量(未处理、处理中、已处理)", notes = "参数为角色主键roleid")
    public Object statisticNodeAuditStatusNum(@NotNull Integer roleid) {
        Object object = bpmInfoService.statisticNodeAuditStatusNum(roleid);
        return object;
    }

    /**
     * 查询基础工作流树状按分类分组信息
     * @return
     */
    @GetMapping("/findBpmBasicTreeGroupByClassification")
    @ApiOperation(value = "查询基础工作流树状按分类分组信息", notes = "无参数")
    public Object findBpmBasicTreeGroupByClassification() {
        Object object = bpmInfoService.findBpmBasicTreeGroupByClassification();
        return object;
    }

    /**
     * 交接班
     * @param changeLeaderDto
     * @return
     */
    @PostMapping("/changeLeader")
    @ApiOperation(value = "交接班", notes = "参数userIdOld为当前人员userid,userIdNew为要变更的人员userid")
    public Object changeLeader(@RequestBody @Valid ChangeLeaderDto changeLeaderDto) {
        Object object = bpmInfoService.changeLeader(changeLeaderDto.getUserIdOld(), changeLeaderDto.getUserIdNew());
        return object;
    }

    /**
     * 查询指定执行工作流节点和表单的树状信息 -即使没有表单也会有节点信息
     * @param bpm_executeversion
     * @return
     */
    @GetMapping("/findNodeAndFormInfoAll")
    @ApiOperation(value = "查询指定执行工作流节点和表单的树状信息 -即使没有表单也会有节点信息", notes = "参数为执行工作流版本号bpm_executeversion")
    public Object findNodeAndFormInfoAll(String bpm_executeversion) {
        Object object = bpmInfoService.findNodeAndFormInfoAll(bpm_executeversion);
        return object;
    }
}