package com.atguigu.spring.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/10- 14:58
 * @desc
 **/
@Component
public class MyzuulFilter extends ZuulFilter{

  private Logger logger;

  public  MyzuulFilter(){
    logger = LoggerFactory.getLogger(MyzuulFilter.class);
  }

  @Override
  public String filterType() {
    String filterType="pre";
    return filterType;
  }

  @Override
  public int filterOrder() {
    return 0;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext currentContext = RequestContext.getCurrentContext();
    HttpServletRequest request = currentContext.getRequest();
    String signal = request.getParameter("signal");
    return "hello".equals(signal);
  }

  @Override
  public Object run() throws ZuulException {
    //当前实现会忽略方法的返回值
    return null;
  }
}
