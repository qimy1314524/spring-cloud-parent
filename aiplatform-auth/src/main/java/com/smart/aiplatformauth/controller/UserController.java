package com.smart.aiplatformauth.controller;

import com.smart.aiplatformauth.annotation.CurrentUser;
import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.mapper.ApiUserInfoMapper;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.UserService;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.smart.aiplatformauth.utils.ResultVOUtil;
import com.smart.aiplatformauth.vo.ResultVO;
import com.smart.aiplatformauth.vo.UserRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @desc: 用户服务Controler
 * @author: chengjz
 */
@RestController
@Api(value = "AiplatformUserApi")
@Validated
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ApiUserInfoMapper apiUserInfoMapper;

  /**
   * 更新用户微信信息
   * @param user
   * @return
   */
  @PostMapping("editUserWchatInfo")
  @ApiOperation(value = "更新用户微信信息", notes = "openid和formid至少传一个\n【@ApiImplicitParams({\n"
      + "      @ApiImplicitParam(paramType = \"body\", name = \"userid\", dataType = \"Integer\", value = \"用户id\", required = true),\n"
      + "      @ApiImplicitParam(paramType = \"body\", name = \"openid\", dataType = \"String\", value = \"微信用户openid\", required = false),\n"
      + "      @ApiImplicitParam(paramType = \"body\", name = \"formid\", dataType = \"String\", value = \"微信表单id\", required = false)\n"
      + "  })】")
  public Object editUserWchatInfo(@RequestBody User user) {
    Object object = userService.editUserWchatInfo(user);
    return object;
  }
  /**
   * 根据用户名或用户id查询用户及其关联角色信息
   * @param user
   * @return
   */
  @GetMapping("findUserRoleInfo")
  @ApiOperation(value = "更新用户微信信息", notes = "username和userid至少传一个")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "query", name = "userid", dataType = "Integer", value = "用户id", required = false),
      @ApiImplicitParam(paramType = "query", name = "username", dataType = "String", value = "用户名", required = false)
  })
  public Object findUserRoleInfo(User user) {
    List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByUP(user);
    if(list.size() != 1) {
      throw new MyException(
          ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "userid或username不存在"));
    }
    return ResultUtil.result(Code.OK.getCode(), "请求成功", list.get(0));
  }

  /**
   * 根据角色id查询属于该角色的所有用户信息
   * @param roleid
   * @return
   */
  @GetMapping("findUserRoleInfoByRoleid")
  @ApiOperation(value = "根据角色id查询属于该角色的所有用户信息", notes = "参数为Integer类型的角色主键roleid")
  public Object findUserRoleInfoByRoleid(Integer roleid) {
    List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByRoleid(roleid);
    return ResultUtil.result(Code.OK.getCode(), "请求成功", list);
  }
  /**
   * 微信解绑用户
   * @return
   */
  @PostMapping("/binding-relieve")
  @ApiOperation(value = "微信解绑用户", notes = "")
  public ResultVO relieveWeChat(@CurrentUser String userName) {
    return userService.updateUserOpenId(userName);
  }

  /**
   * 查询所有应急有关部门列表--安丘定制
   * @return
   */
  @GetMapping("findEmergencyDepartmentAll")
  @ApiOperation(value = "查询所有应急有关部门列表--安丘定制", notes = "无参数")
  public Object findEmergencyDepartmentAll() {
    String[] array = {"市水利局","市气象局","市应急局","市卫健局","市交通运输局","市防办","事发地镇街区防指","市财政局","市公安局","市委宣传部","市人武部","市住建局","市工信局","市供电公司","人民解放军(驻安)部队","市自然资源与规划局"};
    return ResultUtil.result(Code.OK.getCode(), "请求成功", array);
  }
}
