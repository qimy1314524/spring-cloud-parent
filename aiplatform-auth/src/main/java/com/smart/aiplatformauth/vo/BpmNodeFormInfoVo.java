package com.smart.aiplatformauth.vo;

import lombok.Data;

/**
 * Bpm流程节点和对应表单信息VO
 * @author chengjz
 */
@Data
public class BpmNodeFormInfoVo {

  /**
   * 表主键
   */
  private Integer bni_id;
  /**
   * 节点名称
   */
  private String bni_name;
  /**
   * 节点顺序号
   */
  private String bni_ordernumber;
  /**
   * 节点描述
   */
  private String bni_describe;
  /**
   * 关联基础流程版本号
   */
  private String bni_relatebasicversion;
  /**
   * 节点启用状态
   */
  private String bni_status;
  /**
   * 节点创建时间
   */
  private String bni_createtime;
  /**
   * 节点处理角色id
   */
  private Integer bni_roleid;
  /**
   * 节点处理角色中文名
   */
  private String bni_rolechinesename;
  /**
   * 节点处理负责人用户id
   */
  private Integer bni_userid;
  /**
   * 节点处理负责人用户中文名
   */
  private String bni_userchinesename;
  /**
   * 节点绑定数据表单名
   */
  private String bni_tableform;
  /**
   * 节点关联执行流程版本号
   */
  private String bni_relateexecuteversion;
  /**
   * 节点执行状态
   */
  private String bni_executestatus;
  /**
   * 节点审核状态
   */
  private String bni_auditstatus;
  /**
   * 节点任务和工作的目标命令要求
   */
  private String bni_command;
  /**
   * 地点经度
   */
  private Double bni_lon;
  /**
   * 地点纬度
   */
  private Double bni_lat;
  /**
   * 地点描述
   */
  private String bni_place;
  /**
   * 绑定的资源id
   */
  private String bni_sourceids;
  /**
   * 节点结束时间
   */
  private String bni_overtime;

  /**
   * 表主键
   */
  private Integer nfi_id;
  /**
   * 信息标题
   */
  private String nfi_title;
  /**
   * 信息概述
   */
  private String nfi_summary;
  /**
   * 信息内容
   */
  private String nfi_content;
  /**
   * 上传的图片地址-英文逗号拼接
   */
  private String nfi_image;
  /**
   * 上传的文件地址-英文逗号拼接
   */
  private String nfi_file;
  /**
   * 审核意见
   */
  private String nfi_opinion;
  /**
   * 审核用户中文名
   */
  private String nfi_audituser;
  /**
   * 审核用户id
   */
  private Integer nfi_audituserid;
  /**
   * 信息关联执行节点主键id
   */
  private Integer nfi_nodeid;
  /**
   * 信息创建时间
   */
  private String nfi_createtime;
  /**
   * 所属执行工作流版本
   */
  private String nfi_belongbpmversion;
  /**
   * 分类
   */
  private String nfi_type;
  /**
   * 经度
   */
  private String nfi_lon;
  /**
   * 维度
   */
  private String nfi_lat;
}
