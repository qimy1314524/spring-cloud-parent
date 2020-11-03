package com.smart.aiplatformauth.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 编辑节点信息参数dto
 * @author chengjz
 */
@Data
public class EditBpmNodeInfoDto {

  /**
   * 节点id
   */
  @NotNull(message = "bni_id is null")
  private Integer bni_id;
  /**
   * 变更为的用户id
   */
  @NotNull(message = "userid is null")
  private Integer userid;
}
