package com.smart.aiplatformauth.mapper;

import com.smart.aiplatformauth.model.TraceRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *  Mapper 接口
 * @author chengjz
 */
@Repository
//@Mapper
public interface TraceRecordMapper extends BaseMapper<TraceRecord> {

  /**
   * 复杂条件查询数据操作记录表所有数据
   * @param dto
   * @return
   */
  List<TraceRecord> findTraceRecord(Page<TraceRecord> page, @Param("dto") TraceRecord dto);

  /**
   * 删除60天前痕迹数据
   * @return
   */
  boolean deleteTraceRecord();

}
