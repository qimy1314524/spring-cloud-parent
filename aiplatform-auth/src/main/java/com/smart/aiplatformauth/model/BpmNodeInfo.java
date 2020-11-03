package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * BPM流程节点信息表实体类
 * @author chengjz
 */
@TableName("bpmnodeinfo")
@Data
public class BpmNodeInfo extends Model<BpmNodeInfo> {

  private static final long serialVersionUID = 1L;

  /**
   * 表主键
   */
  @TableId(type = IdType.AUTO)
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

  @Override
  protected Serializable pkVal() {
    return this.bni_id;
  }

}
