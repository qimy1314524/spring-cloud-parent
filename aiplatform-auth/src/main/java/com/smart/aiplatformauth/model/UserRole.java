package com.smart.aiplatformauth.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户角色关联表实体类
 * @author chengjz
 */
@TableName("userrole")
@Data
public class UserRole extends Model<UserRole> {

  private static final long serialVersionUID = 1L;

  /**
   * 用户角色关联表主键
   */
  @TableId(type = IdType.AUTO)
  private Integer id;
  /**
   * 用户表主键
   */
  private Integer userid;
  /**
   * 角色表主键
   */
  private Integer roleid;

  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}

