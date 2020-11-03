package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户信息表实体类
 * @author chengjz
 */
@TableName("user")
@Data
public class User extends Model<User> {

  private static final long serialVersionUID = 1L;

  /**
   * 用户信息表主键
   */
  @TableId(type = IdType.AUTO)
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
   * 创建时间
   */
  private String createtime;

  @Override
  protected Serializable pkVal() {
    return this.userid;
  }
}

