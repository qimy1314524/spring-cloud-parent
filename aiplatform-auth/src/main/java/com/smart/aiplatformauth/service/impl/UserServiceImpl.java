package com.smart.aiplatformauth.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.mapper.UserMapper;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.UserService;
import com.smart.aiplatformauth.service.WeixinService;
import com.smart.aiplatformauth.utils.ResultUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smart.aiplatformauth.utils.ResultVOUtil;
import com.smart.aiplatformauth.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务实现类
 * @author chengjz
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  /**
   * 更新用户微信信息
   * @param user
   * @return
   */
  @Override
  public Object editUserWchatInfo(User user) {
    if("".equals(user.getOpenid()) || null == user.getOpenid()) {
      if("".equals(user.getFormid()) || null == user.getFormid()) {
        throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "微信信息openid和formid不允许全部为空"));
      }
    }

    Map<String, Object> map = new HashMap<>();
    map.put("userid", user.getUserid());
    List<User> list = super.baseMapper.selectByMap(map);
    if(list.size() != 1) {
      throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "用户id不存在"));
    }
    User userForUpdate = list.get(0);
    userForUpdate.setOpenid(user.getOpenid());
    userForUpdate.setFormid(user.getFormid());
    int count = super.baseMapper.updateById(userForUpdate);
    if(count != 1) {
      throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "更新数据失败"));
    }
    return ResultUtil.result(Code.OK.getCode(), "更新成功");
  }
  @Override
  public User getUserByOpenId(String openId) {
    List<User> users = super.baseMapper.selectList(new EntityWrapper<User>().eq("openid", openId).eq("state","1"));
    if(users.size()>1){
      log.info("用户表存在脏数据（一个openId对应多个用户），请及时处理");
    }else if(users.size()==0){
      return null;
    }
    return users.get(0);
  }

  @Override
  public ResultVO updateUserOpenId(String username) {
    User user = new User();
    user.setUsername(username);
    User result = super.baseMapper.selectOne(user);
    if(result!=null){
      result.setOpenid("");
      Integer integer = super.baseMapper.updateById(result);
      if(integer>0){
        return ResultVOUtil.success();
      }
    }
    return ResultVOUtil.error(Code.UNAUTHORIZED.getCode(),Code.UNAUTHORIZED.getMessage());
  }


  @Override
  public boolean updateById(User user) {
    return super.baseMapper.updateById(user)>0;
  }
}
