package com.smart.aiplatformauth.service;

import com.baomidou.mybatisplus.service.IService;
import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.vo.ResultVO;

import java.util.List;

/**
 *  服务接口类
 * @author chengjz
 */
public interface UserService extends IService<User> {

  /**
   * 更新用户微信信息
   * @param user
   * @return
   */
  Object editUserWchatInfo(User user);

  User getUserByOpenId(String openId);

  ResultVO updateUserOpenId(String username);

}
