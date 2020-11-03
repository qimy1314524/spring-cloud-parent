package com.smart.aiplatformauth.mapper;

import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.vo.UserRoleVo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户信息mapper服务接口类
 * @author: chengjz
 */
@Repository
//@Mapper
public interface ApiUserInfoMapper {

    //获取授权用户信息
    List<User> findUserInfo(@Param("user") User user);

    //根据用户名查询用户和所属角色信息
    List<UserRoleVo> findUserRoleInfoByUP(@Param("user") User user);

    //根据角色id查询属于该角色的所有用户信息
    List<UserRoleVo> findUserRoleInfoByRoleid(@Param("roleid") Integer roleid);
}
