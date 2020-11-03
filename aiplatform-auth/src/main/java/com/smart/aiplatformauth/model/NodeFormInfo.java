package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * BPM流程节点审核或上报信息表实体类
 * @author chengjz
 */
@TableName("nodeforminfo")
@Data
public class NodeFormInfo extends Model<NodeFormInfo> {

  private static final long serialVersionUID = 1L;

  /**
   * 表主键
   */
  @TableId(type = IdType.AUTO)
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
  private Double nfi_lon;
  /**
   * 维度
   */
  private Double nfi_lat;

  @Override
  protected Serializable pkVal() {
    return this.nfi_id;
  }

}
