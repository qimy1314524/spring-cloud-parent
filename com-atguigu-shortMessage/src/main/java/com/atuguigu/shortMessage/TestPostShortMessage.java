/*
package com.atuguigu.shortMessage;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;

*/
/**
 * @author tey
 * @version V1.0
 * @date 2020/8/11- 15:32
 * @desc
 **//*

public class TestPostShortMessage {

  public static void main(String[] args) {
    String host = "https://cdcxdxjk.market.alicloudapi.com";
    String path = "/chuangxin/dxjk";
    String method = "POST";
    String appcode = "8907c0d5fedf4b229284281e52719c3f";
    Map<String, String> headers = new HashMap<String, String>();
    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
    headers.put("Authorization", "APPCODE " + appcode);
    Map<String, String> querys = new HashMap<String, String>();
    querys.put("content", "【创信】你的验证码是：5873，3分钟内有效！");
    querys.put("mobile", "18763420437");
    Map<String, String> bodys = new HashMap<String, String>();


    try {
      */
/**
       * 重要提示如下:
       * HttpUtils请从
       * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
       * 下载
       *
       * 相应的依赖请参照
       * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
       *//*

      HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
      System.out.println(response.toString());
      //获取response的body
      //System.out.println(EntityUtils.toString(response.getEntity()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
*/
