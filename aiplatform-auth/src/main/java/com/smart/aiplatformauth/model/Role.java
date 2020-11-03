package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * 角色信息表实体类
 * @author chengjz
 */
@TableName("role")
@Data
public class Role extends Model<Role> {

  private static final long serialVersionUID = 1L;

  /**
   * 角色信息表主键
   */
  @TableId(type = IdType.AUTO)
  private Integer roleid;
  /**
   * 角色名称
   */
  private String rolename;
  /**
   * 角色中文名
   */
  private String chinesename;
  /**
   * 角色描述
   */
  private String describe;
  /**
   * 角色管辖范围
   */
  private String managerange;
  /**
   * 角色权限范围
   */
  private String permissionrange;
  /**
   * 角色可用菜单权限
   */
  private String menupermission;
  /**
   * 角色可用GIS服务图层权限
   */
  private String layerpermission;
  /**
   * 角色可用服务或专题或系统权限
   */
  private String servicepermission;
  /**
   * 角色分类
   */
  private String classification;
  /**
   * 上级角色id
   */
  private String superior;
  /**
   * 创建时间
   */
  private String recordtime;

  @Override
  protected Serializable pkVal() {
    return this.roleid;
  }
}

