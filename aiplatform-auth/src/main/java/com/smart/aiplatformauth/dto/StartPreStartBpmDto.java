package com.smart.aiplatformauth.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Bpm启动预启动流程的启动参数dto
 * @author chengjz
 */
@Data
public class StartPreStartBpmDto {

  /**
   * 执行流程基础版本号
   */
  @NotNull(message = "bpm_executeversion is null")
  private String bpm_executeversion;
  /**
   * 流程启动事件描述
   */
  private String bpm_describe;
  /**
   * 流程事件标识
   */
  private String bpm_eventflag;
  /**
   * 通知模式
   */
  private String bpm_noticemode;
}
