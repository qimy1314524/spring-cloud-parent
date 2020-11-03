package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.Data;

/**
 * @author chengjz
 * @since  痕迹记录表实体类
 */
@TableName("tracerecord")
@Data
public class TraceRecord extends Model<TraceRecord> {

  private static final long serialVersionUID = 1L;

  /**
   * 痕迹记录表主键
   */
  @TableId(type = IdType.AUTO)
  private BigInteger tr_id;
  /**
   * 用户id
   */
  private Integer tr_userid;
  /**
   * 用户真实姓名
   */
  private String tr_realname;
  /**
   * 用户所属部门
   */
  private String tr_department;
  /**
   * 调用接口
   */
  private String tr_interface;
  /**
   * 操作内容
   */
  private String tr_operation;
  /**
   * 请求url
   */
  private String tr_requesturl;
  /**
   * 请求的远程ip
   */
  private String tr_remoteip;
  /**
   * 请求参数
   */
  private String tr_params;
  /**
   * 请求方式
   */
  private String tr_method;
  /**
   * 菜单或功能名称
   */
  private String tr_permname;
  /**
   * 标识
   */
  private String tr_flag;
  /**
   * 记录时间
   */
  private String tr_recordtime;

  @Override
  protected Serializable pkVal() {
    return this.tr_id;
  }
}
