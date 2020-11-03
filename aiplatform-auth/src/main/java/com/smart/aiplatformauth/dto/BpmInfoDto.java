package com.smart.aiplatformauth.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Bpm流程和节点信息查询参数dto
 * @author chengjz
 */
@Data
public class BpmInfoDto {

  private String bpm_name;
  /**
   * 工作流所属分类
   */
  private String bpm_classification;
  /**
   * 工作流基础版本号
   */
  private String bpm_version;
  /**
   * 工作流执行状态
   */
  private String bpm_executestatus;
  /**
   * 工作流执行版本号
   */
  private String bpm_executeversion;
  /**
   * 节点名称
   */
  private String bni_name;
  /**
   * 节点执行状态
   */
  private String bni_executestatus;
  /**
   * 节点审核状态
   */
  private String bni_auditstatus;
  /**
   * 标识位
   */
  private String sign;
}
