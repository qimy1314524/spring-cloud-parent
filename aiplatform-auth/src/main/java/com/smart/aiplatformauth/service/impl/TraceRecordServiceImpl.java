package com.smart.aiplatformauth.service.impl;

import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.model.TraceRecord;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.mapper.TraceRecordMapper;
import com.smart.aiplatformauth.dto.PageDto;
import com.smart.aiplatformauth.service.TraceRecordService;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 * @author chengjz
 */
@Slf4j
@Service
public class TraceRecordServiceImpl extends ServiceImpl<TraceRecordMapper, TraceRecord> implements TraceRecordService {

  /**
   * 复杂条件查询数据操作记录信息
   * @param pageDto
   * @param traceRecord
   * @return
   */
  @Override
  public List<TraceRecord> findTraceRecord(PageDto pageDto, TraceRecord traceRecord) {
    if(pageDto.getPageSize() == null && pageDto.getStartPage() == null) {
      pageDto.setStartPage(0);
      pageDto.setPageSize(99999);
    }
    Page<TraceRecord> page = new Page<>(pageDto.getStartPage(),pageDto.getPageSize());
    List<TraceRecord> list = super.baseMapper.findTraceRecord(page, traceRecord);
    return list;
  }

  /**
   * 新增数据操作记录信息
   * @param traceRecord
   * @return
   */
  @Override
  public Object addTraceRecord(TraceRecord traceRecord) {
    Integer result = super.baseMapper.insert(traceRecord);
    if (result != 1) {
      throw new MyException(
          ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "新增数据操作记录信息失败"));
    }
    return ResultUtil.result(Code.OK.getCode(), "新增数据操作记录信息成功");
  }

  /**
   * 删除60天前痕迹记录数据
   * @return
   */
  @Override
  public boolean delTraceRecord() {
    try {
      super.baseMapper.deleteTraceRecord();
      return true;
    } catch (Exception e) {
      log.error("60天前痕迹数据批量删除出错，异常为：" + e.getMessage());
      return false;
    }
  }
}
