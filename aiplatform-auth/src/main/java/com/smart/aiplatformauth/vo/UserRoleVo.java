package com.smart.aiplatformauth.vo;

import lombok.Data;

/**
 * 用户角色信息VO
 * @author chengjz
 */
@Data
public class UserRoleVo {

  /**
   * 用户信息表主键
   */
  private Integer userid;
  /**
   * 用户账号
   */
  private String username;
  /**
   * 用户密码
   */
  private String password;
  /**
   * 用户真实姓名
   */
  private String realname;
  /**
   * 用户电话
   */
  private String tel;
  /**
   * 用户性别
   */
  private String sex;
  /**
   * 用户年龄
   */
  private String age;

  /**
   * 用户身份证号
   */
  private String idcard;

  /**
   * 用户所属部门
   */
  private String department;

  /**
   * 用户所属部门组织机构代码
   */
  private String organizationcode;

  /**
   * 用户管辖区域
   */
  private String jurisdiction;

  /**
   * 用户状态
   */
  private String state;

  /**
   * 用户微信唯一id
   */
  private String openid;

  /**
   * 小程序表单id
   */
  private String formid;

  /**
   * 用户创建时间
   */
  private String createtime;

  /**
   * 登录返回值Token--非数据库字段
   */
  private String token;

  /**
   * 登录返回值刷新Token--非数据库字段
   */
  private String refreshToken;

  /**
   * 用户角色关联表主键
   */
  private String id;

  /**
   * 角色信息表主键
   */
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
   * 角色创建时间
   */
  private String recordtime;
}
