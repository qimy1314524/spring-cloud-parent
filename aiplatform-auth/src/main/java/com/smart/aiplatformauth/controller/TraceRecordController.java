package com.smart.aiplatformauth.controller;

import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.model.TraceRecord;
import com.smart.aiplatformauth.dto.PageDto;
import com.smart.aiplatformauth.service.TraceRecordService;
import com.smart.aiplatformauth.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc: 数据操作记录Controler
 * @author: chengjz
 */
@RestController
@Api(value = "AiplatformTraceRecordApi")
@Validated
public class TraceRecordController {

  @Autowired
  private TraceRecordService traceRecordService;


  /**
   * 复杂条件查询数据痕迹记录信息
   * @param pageDto
   * @param traceRecord
   * @return
   */
  @GetMapping("findTraceRecord")
  @ApiOperation(value = "复杂条件查询留言反馈信息", notes = "")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "query", name = "startPage", dataType = "Integer", value = "第几页", required = false),
      @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", value = "每页多少条", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_id", dataType = "Integer", value = "主键id", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_realname", dataType = "String", value = "用户姓名-模糊查询", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_operation", dataType = "String", value = "操作内容-模糊查询", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_interface", dataType = "String", value = "调用接口-模糊查询", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_recordtime", dataType = "String", value = "记录时间-模糊查询", required = false),
      @ApiImplicitParam(paramType = "query", name = "tr_userid", dataType = "String", value = "用户id", required = false)
  })
  public Object findTraceRecord(PageDto pageDto, TraceRecord traceRecord) {
    PageDto pageDtoAll = new PageDto();
    pageDtoAll.setStartPage(0);
    pageDtoAll.setPageSize(99999);
    List<TraceRecord> list = traceRecordService.findTraceRecord(pageDto, traceRecord);
    List<TraceRecord> listAll = traceRecordService.findTraceRecord(pageDtoAll, traceRecord);
    return ResultUtil.result(Code.OK.getCode(), Code.OK.getMessage(), list, listAll.size());
  }
}
