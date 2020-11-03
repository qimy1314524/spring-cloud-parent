package com.smart.aiplatformauth.aspect;

import com.smart.aiplatformauth.mapper.ApiUserInfoMapper;
import com.smart.aiplatformauth.model.TraceRecord;
import com.smart.aiplatformauth.model.User;
import com.smart.aiplatformauth.result.Code;
import com.smart.aiplatformauth.service.TraceRecordService;
import com.smart.aiplatformauth.utils.ResultUtil;
import com.smart.aiplatformauth.utils.ServletUtils;
import com.alibaba.fastjson.JSONObject;
import com.smart.aiplatformauth.vo.UserRoleVo;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @desc: Aspect切面类
 * @author: chengjz
 */
//@Aspect
//@Component
public class HttpAspect {

    @Autowired
    private ApiUserInfoMapper apiUserInfoMapper;
    @Autowired
    private TraceRecordService traceRecordService;

    private final static Logger log = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.smart.aiplatformauth.controller..*(..))")
    public void log() {

    }
    /**
     * @desc: 记录请求
     * @author: chengjz
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        authFilter(joinPoint);
    }

    /**
     * @desc: 响应请求
     * @author: chengjz
     */
    @After("log()")
    public void doAfter() {
        log.info("========================== ↓响应请求↓ ==========================");
    }

    /**
     * @desc: 打印返回值
     * @author: chengjz
     */
    @AfterReturning(returning = "obj",pointcut = "log()")
    public void doAfterReturnning(Object obj) {
        log.info("请求返回值：{}",obj);
    }


    /**
     * @desc: 统一参数验证处理
     * @author: chengjz
     */
    @Around("execution(* com.smart.aiplatformauth.controller..*(..)) && args(..,bindingResult)")
    public Object doAround(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        authFilter(pjp);
        Object retVal;
        if (bindingResult.hasErrors()) {
            return ResultUtil.result(Code.BAD_REQUEST.getCode(),bindingResult.getFieldError().getDefaultMessage(),null);
        } else {
            retVal = pjp.proceed();
        }
        return retVal;
    }

    /**
     * @desc: 请求拦截器
     * @author: chengjz
     */
    public void authFilter(JoinPoint joinPoint){
        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
        String params = JSONObject.toJSONString(map);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURI().replaceAll(request.getContextPath(), "");
        String remoteAddr = request.getRemoteAddr();
        String method = request.getMethod();
        String args = Arrays.toString(joinPoint.getArgs());
        //String token=request.getHeader("token");
        String authorization = "";
        if(request.getHeader("Authorization") != null && !request.getHeader("Authorization").equals("")) {
            if(request.getHeader("Authorization").contains("Basic")) {
                authorization=request.getHeader("Authorization").split(" ")[1];
            } else {
                authorization=request.getHeader("Authorization");
            }
        }
        String userName = "";
        if(request.getHeader("Authorization-UserName") != null && !request.getHeader("Authorization-UserName").equals("")) {
            userName=request.getHeader("Authorization-UserName");
        }

        System.out.println(authorization);
        log.info("========================== ↓收到请求↓ ==========================");
        log.info("请求的token:{}",authorization);
        log.info("请求url:{}",requestUrl);
        log.info("请求源ip:{}",remoteAddr);
        log.info("请求方式:{}",method);
        log.info("请求参数:{}", args);
        log.info("请求参数备用参考:{}", params);
        log.info("getContextPath:{}",request.getContextPath());
        log.info("========================== ↑收到请求↑ ==========================");


        if(!"/swagger-resources".equals(requestUrl) && !"/v2/api-docs".equals(requestUrl) && !"/webjars/springfox-swagger-ui/**".equals(requestUrl) && !"/swagger-ui.html".equals(requestUrl)){

        }

        if("/auth".equals(requestUrl) || requestUrl.contains("add") || requestUrl.contains("edit") || requestUrl.contains("del")) {
            //TODO 痕迹记录
            TraceRecord traceRecord = new TraceRecord();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date=new java.util.Date();
            String datestr=sdf.format(date);
            User user = new User();
            if("/auth".equals(requestUrl)) {
                user.setUsername(args.substring(1,args.length()-1).split(",")[0].trim());
            } else {
                user.setUsername(userName);
            }

            List<UserRoleVo> list = apiUserInfoMapper.findUserRoleInfoByUP(user);
            if(list.size() > 0) {
                traceRecord.setTr_userid(list.get(0).getUserid());
                traceRecord.setTr_realname(list.get(0).getRealname());
                traceRecord.setTr_department(list.get(0).getDepartment());
            }

            String[] ru = requestUrl.split("\\/");
            traceRecord.setTr_interface(ru[ru.length-1]);
            if("auth".equals(requestUrl)) {
                traceRecord.setTr_operation("用户登录");
            } else if(requestUrl.contains("add")) {
                traceRecord.setTr_operation("数据新增");
            } else if(requestUrl.contains("edit")) {
                traceRecord.setTr_operation("数据更新");
            } else if(requestUrl.contains("del")) {
                traceRecord.setTr_operation("数据删除");
            }
            traceRecord.setTr_requesturl(requestUrl);
            traceRecord.setTr_remoteip(remoteAddr);
            traceRecord.setTr_params(params);
            traceRecord.setTr_method(method);
            traceRecord.setTr_permname("预留字段-暂时无值");
            traceRecord.setTr_flag("预留字段-暂时无值");
            traceRecord.setTr_recordtime(datestr);

            traceRecordService.addTraceRecord(traceRecord);
        }
    }
}
