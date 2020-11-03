package com.atguigu.spring.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tey
 * @version V1.0
 * @date 2020/10/27- 15:37
 * @desc
 **/
@RestController
@Slf4j
public class FlowLimitController {

  @GetMapping("/testA")
  public String testA() throws InterruptedException {
    log.info(Thread.currentThread().getName()+"\t"+"....testA");
    return "-----testA";
  }

  @GetMapping("/testB")
  public String testB() throws  InterruptedException {
    TimeUnit.SECONDS.sleep(6);
    log.info("testB RT测试");
    return "-------testB";
  }

  @GetMapping("/testD")
  public String testD() throws NoSuchMethodException {
    log.info("testD,测试RT");
    int age=10/0;
    return "-------testD";
  }

  @GetMapping("/testHotKey")
  @SentinelResource(value = "testHotKey",blockHandler ="deal_testHotKey" )
  public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                          @RequestParam(value = "p2",required = false) String p2){
    System.out.println(10/Integer.parseInt(p2));
    return "-------------testHotKey";
  }


  public String deal_testHotKey( String p1,String p2,BlockException exception){
    System.out.println("进入deal_testHotKey");
    System.out.println(exception);
    System.out.println("++++++++++++++");
    System.out.println(exception.getMessage()+"============");
    System.out.println("出deal_testHotKey");
    return "-------------deal_testHotKey";
  }




  public static void main(String[] args) throws NoSuchMethodException {
    FlowLimitController flowLimitController=new FlowLimitController();
    Method testA = flowLimitController.getClass().getMethod("testA", new Class[]{});
    GetMapping declaredAnnotation = testA.getDeclaredAnnotation(GetMapping.class);
    String[] value = declaredAnnotation.value();
    Arrays.stream(value).forEach(System.out::println);
    System.out.println("---------------------");
    String name = declaredAnnotation.name();
    System.out.println(name);
    System.out.println("---------------------");
    String[] path = declaredAnnotation.path();
    Arrays.stream(path).forEach(System.out::println);
    System.out.println("---------------------");
    String[] consumes = declaredAnnotation.consumes();
    Arrays.stream(consumes).forEach(System.out::println);
    System.out.println("---------------------");
    String[] headers = declaredAnnotation.headers();
    Arrays.stream(headers).forEach(System.out::println);
    String[] params = declaredAnnotation.params();
    Arrays.stream(params).forEach(System.out::println);
    System.out.println("---------------------");
    String[] produces = declaredAnnotation.produces();
    Arrays.stream(produces).forEach(System.out::println);
    System.out.println("---------------------");
    Class<? extends Annotation> aClass = declaredAnnotation.annotationType();
    System.out.println(aClass);
  }


}
