package com.smart.aiplatformauth.vo;

import lombok.Data;

/**
 * Bpm流程和节点信息VO
 * @author chengjz
 */
@Data
public class BpmInfoVo {

  /**
   * 表主键
   */
  private Integer bpm_id;
  /**
   * 工作流名称
   */
  private String bpm_name;
  /**
   * 工作流所属分类
   */
  private String bpm_classification;
  /**
   * 工作流描述
   */
  private String bpm_describe;
  /**
   * 工作流基础版本号
   */
  private String bpm_version;
  /**
   * 工作流启用状态
   */
  private String bpm_status;
  /**
   * 工作流执行次数
   */
  private Integer bpm_executecount;
  /**
   * 工作流创建时间
   */
  private String bpm_createtime;
  /**
   * 工作流执行版本号
   */
  private String bpm_executeversion;
  /**
   * 执行工作流关联基础工作流版本号
   */
  private String bpm_relateversion;
  /**
   * 工作流执行状态
   */
  private String bpm_executestatus;
  /**
   * 工作流执行到的当前节点id
   */
  private String bpm_nownodeid;
  /**
   * 工作流消息通知模式
   */
  private String bpm_noticemode;
  /**
   * 执行工作流对应的实际事件标识
   */
  private String bpm_eventflag;
  /**
   * 流程结束时间
   */
  private String bpm_overtime;

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
}
