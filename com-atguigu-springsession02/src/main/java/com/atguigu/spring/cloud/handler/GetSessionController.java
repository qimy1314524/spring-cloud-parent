package com.atguigu.spring.cloud.handler;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/14- 10:50
 * @desc
 **/
@RestController
public class GetSessionController {

  @RequestMapping("/getSession")
  public String saveSession(HttpSession session,String key) {
    String king = (String) session.getAttribute(key);
    System.out.println(king);
    return king;
  }

}

