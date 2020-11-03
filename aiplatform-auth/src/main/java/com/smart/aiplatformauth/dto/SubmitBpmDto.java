package com.smart.aiplatformauth.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * Bpm流程节点审核提交参数dto
 * @author chengjz
 */
@Data
public class SubmitBpmDto {

  /**
   * 节点id
   */
  @NotNull(message = "bni_id is null")
  private Integer bni_id;
  /**
   * 执行状态
   */
  @NotNull(message = "bni_executestatus is null")
  private String bni_executestatus;
}
