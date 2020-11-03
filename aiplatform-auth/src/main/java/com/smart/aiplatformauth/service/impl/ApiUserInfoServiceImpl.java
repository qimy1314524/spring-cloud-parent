package com.smart.aiplatformauth.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.smart.aiplatformauth.dto.WeChatBindingDto;
import com.smart.aiplatformauth.exception.MyException;
import com.smart.aiplatformauth.mapper.ApiUserInfoMapper;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.service.ApiUserInfoService;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.UserService;
import com.smart.aiplatformauth.service.WeixinService;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.smart.aiplatformauth.utils.ResultVOUtil;
import com.smart.aiplatformauth.vo.ResultVO;
import com.smart.aiplatformauth.vo.UserRoleVo;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 登录鉴权服务接口实现类
 * @author: chengjz
 */
@Service
public class ApiUserInfoServiceImpl implements ApiUserInfoService {

    @Value("${jwt.secret.key}")
    private String secretKey;//秘钥

    @Value("${token.expire.time}")
    private long tokenExpireTime;//token超时时间

    @Value("${refresh.token.expire.time}")
    private long refreshTokenExpireTime;

    @Value("${jwt.refresh.token.key.format}")
    private String jwtRefreshTokenKeyFormat;

    @Value("${jwt.blacklist.key.format}")
    private String jwtBlacklistKeyFormat;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;//缓存redis连接

    @Autowired
    private ApiUserInfoMapper apiUserInfoMapper;

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private UserService userService;

    /**
     * 登陆授权，并获取token
     * @param user
     * @return
     */
    @Override
    public ResultVO auth(User user) {
        //数据的合法性校验
        if(!check(user))
            return ResultVOUtil.error("20001", "授权用户信息错误,用户名和密码等信息不正确！");
        //生成JWT
        String token = buildJWT(user.getUsername());
        //生成refreshToken
        String refreshToken = UUID.randomUUID().toString().replaceAll("-", "");
        //保存refreshToken至redis，使用hash结构保存使用中的token以及用户标识
        String refreshTokenKey = String.format(jwtRefreshTokenKeyFormat, refreshToken);
        stringRedisTemplate.opsForHash().put(refreshTokenKey, "token", token);
        stringRedisTemplate.opsForHash().put(refreshTokenKey, "userName", user.getUsername());
        //refreshToken设置过期时间
        stringRedisTemplate.expire(refreshTokenKey, refreshTokenExpireTime, TimeUnit.MILLISECONDS);
        //返回结果
        /*Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("token", token);
        dataMap.put("refreshToken", refreshToken);
        return ResultVOUtil.success(dataMap);*/
        List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByUP(user);
        UserRoleVo userRoleVo = list.get(0);
        userRoleVo.setToken(token);
        userRoleVo.setRefreshToken(refreshToken);
        return ResultVOUtil.success(userRoleVo);
    }

    /**
     * 校验授权用户和授权码的合法性
     * @param user
     * @return
     */
    private boolean check(User user) {
        List<User> apiUserInfos = apiUserInfoMapper.findUserInfo(user);
        if(apiUserInfos.size() == 0) {
            return false;
        }
        User apiUserInfo = apiUserInfos.get(0);
        if (apiUserInfo == null)  //不存在此用户
            return false;
        if(!(user.getPassword()).equals(SecureUtil.md5(apiUserInfo.getPassword()))) {
            return false;
            //throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "用户名或密码不正确"));
        }
        /*if (!StringUtils.equals(user.getPassword(), apiUserInfo.getPassword()))
            return false;   //授权码填写错误*/
        return true;
    }

    /**
     * 刷新JWT
     * @param refreshToken
     * @return
     */
    @Override
    public ResultVO refreshToken(String refreshToken) {
        //参数校验
        if (StringUtils.isEmpty(refreshToken)) {
            throw new MyException(ResultUtil.result(Code.INTERNAL_SERVER_ERROR.getCode(), "参数缺失"));
        }
        Map<String, Object> resultMap = new HashMap<>();
        String refreshTokenKey = String.format(jwtRefreshTokenKeyFormat, refreshToken);
        String userName = (String) stringRedisTemplate.opsForHash().get(refreshTokenKey, "userName");
        if (StringUtils.isBlank(userName)) {
            return ResultVOUtil.error("20002", "refreshToken过期");
        }
        String newToken = buildJWTForMoreSecurityTime(userName);
        //替换当前token，并将旧token添加到黑名单
        String oldToken = (String) stringRedisTemplate.opsForHash().get(refreshTokenKey, "token");
        stringRedisTemplate.opsForHash().put(refreshTokenKey, "token", newToken);//若不允许无限置换，则此处改为stringRedisTemplate.delete(refreshTokenKey)
        stringRedisTemplate.opsForValue().set(String.format(jwtBlacklistKeyFormat, oldToken), "", tokenExpireTime, TimeUnit.MILLISECONDS);
        resultMap.put("newToken", newToken);
        return ResultVOUtil.success(resultMap);
    }

    @Override
    public ResultVO authWeChat(String code) {
        // 1.获取openId
        WxMaJscode2SessionResult openIdAndSession = weixinService.getOpenIdAndSession(code);
        // 2.根据openId查询用户
        User userByOpenId = userService.getUserByOpenId(openIdAndSession.getOpenid());
        // 3.有，调用授权验证，生成token
        // 4.无，返回未绑定
        if(userByOpenId!=null){
            userByOpenId.setPassword(SecureUtil.md5(userByOpenId.getPassword()));
            return auth(userByOpenId);
        }else {
            return ResultVOUtil.error(Code.UNAUTHORIZED.getCode(),"未绑定系统账号，请绑定");
        }
    }

    private String buildJWT(String userName) {
        //生成jwt
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
                .withIssuer("GEOVIS")
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + tokenExpireTime))
                .withClaim("userName", userName)//保存身份标识
                .sign(algo);
        return token;
    }

    private String buildJWTForMoreSecurityTime(String userName) {
        //生成jwt
        Date now = new Date();
        Algorithm algo = Algorithm.HMAC256(secretKey);
        String token = JWT.create()
            .withIssuer("GEOVIS")
            .withIssuedAt(now)
            .withExpiresAt(new Date(now.getTime() + tokenExpireTime + tokenExpireTime))
            .withClaim("userName", userName)//保存身份标识
            .sign(algo);
        return token;
    }
    @Override
    public ResultVO bindingWeChat(WeChatBindingDto weChatBindingDto) {
        // 1.查询该用户名和密码是否正确
        User result = userService.selectOne(new EntityWrapper<User>()
                .eq("username", weChatBindingDto.getUsername())
                .eq("state", "1")
        );
        if(result == null ||!(weChatBindingDto.getPassword()).equals(SecureUtil.md5(result.getPassword()))){
            return ResultVOUtil.error(Code.LOGIN_FAIL.getCode(),"用户名或密码错误");
        }else if(result.getOpenid()!=null&&!"".equals(result.getOpenid())){
            // 2.查询该用户名密码是否已经绑定微信
            return ResultVOUtil.error(Code.LOGIN_FAIL.getCode(),"该账号已经绑定微信，请将原账号解绑后操作");
        }
        // 3.查询该微信是否已经绑定其他 用户。
        WxMaJscode2SessionResult openIdAndSession = weixinService.getOpenIdAndSession(weChatBindingDto.getCode());
        User userByOpenId = userService.getUserByOpenId(openIdAndSession.getOpenid());
        if(userByOpenId!=null){
            return ResultVOUtil.error(Code.BAD_REQUEST.getCode(),"该微信已经被绑定，请退出小程序稍后再试。");
        }
        // 4.进行绑定操作。
        result.setOpenid(openIdAndSession.getOpenid());
        if(userService.updateById(result)){
            // auth验证md5加密后的密码
            result.setPassword(weChatBindingDto.getPassword());
            return auth(result);
        }
        return ResultVOUtil.error(Code.SYSTEM_EXCEPTION.getCode(),Code.SYSTEM_EXCEPTION.getMessage());
    }

}