package com.smart.aiplatformauth.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @desc: 参数dto
 * @author: 程揭章
 */
@Data
public class PageDto implements Serializable {

  /**
   * 开始页码
   */
  private Integer startPage;

  /**
   * 每页大小
   */
  private Integer pageSize;
}
