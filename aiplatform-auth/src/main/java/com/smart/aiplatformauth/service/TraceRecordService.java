package com.smart.aiplatformauth.service;

import com.smart.aiplatformauth.model.TraceRecord;
import com.smart.aiplatformauth.dto.PageDto;
import com.baomidou.mybatisplus.service.IService;
import java.util.List;

/**
 *  服务接口类
 * @author chengjz
 */
public interface TraceRecordService extends IService<TraceRecord> {

  /**
   * 复杂条件查询数据操作记录信息
   * @param pageDto
   * @param traceRecord
   * @return
   */
  List<TraceRecord> findTraceRecord(PageDto pageDto, TraceRecord traceRecord);

  /**
   * 添加数据操作信息
   * @param traceRecord
   * @return
   */
  Object addTraceRecord(TraceRecord traceRecord);

  /**
   * 删除60天前痕迹记录数据
   * @return
   */
  boolean delTraceRecord();

}
