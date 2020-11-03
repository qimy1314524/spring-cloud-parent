package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * BPM基础工作流信息表实体类
 * @author chengjz
 */
@TableName("bpmbasic")
@Data
public class BpmBasic extends Model<BpmBasic> {

  private static final long serialVersionUID = 1L;

  /**
   * 表主键
   */
  @TableId(type = IdType.AUTO)
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

  @Override
  protected Serializable pkVal() {
    return this.bpm_id;
  }

}
