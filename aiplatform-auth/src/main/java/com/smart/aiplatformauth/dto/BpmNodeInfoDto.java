package com.smart.aiplatformauth.dto;

import lombok.Data;

/**
 * 节点信息参数dto
 * @author chengjz
 */
@Data
public class BpmNodeInfoDto {

  /**
   * 用户id
   */
  private Integer bni_userid;
  /**
   * 角色id
   */
  private Integer bni_roleid;
  /**
   * 执行状态
   */
  private String bni_executestatus;
  /**
   * 审核状态
   */
  private String bni_auditstatus;
  /**
   * 节点名称
   */
  private String bni_name;
  /**
   * 地点
   */
  private String bni_place;
}
