package com.smart.aiplatformauth.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @desc: 交接班参数dto
 * @author: 程揭章
 */
@Data
public class ChangeLeaderDto implements Serializable {

  /**
   * 原用户id
   */
  @NotNull(message = "userIdOld is null")
  private Integer userIdOld;

  /**
   * 新用户id
   */
  @NotNull(message = "userIdNew is null")
  private Integer userIdNew;
}
