package com.smart.aiplatformauth.service;

import static com.smart.aiplatformauth.service.WebSocketService.webSocketOnlyTaskSet;
import static com.smart.aiplatformauth.service.WebSocketService.webSocketIotOnlyTaskSet;

import com.alibaba.fastjson.JSONObject;
import com.smart.aiplatformauth.utils.RedisUtil;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务服务
 * @author chengjz
 */
@Component
@EnableScheduling
@Slf4j
public class ScheduleTaskService {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private RedisUtil redisUtil;

    private static volatile Integer count = 0;
    private static AtomicInteger countAtomic = new AtomicInteger(0);

    /**
     * 3百分之1几率随机产生器
     * @return
     */
    public Boolean randomNumberGenerate1() {
        Random random = new Random();
        int number = random.nextInt(300);
        if(number == 0) {
            return true;
        }
        return false;
    }

    /**
     * 3分之1几率随机产生器
     * @return
     */
    public Integer randomNumberGenerate2() {
        Random random = new Random();
        int number = random.nextInt(3);
        return number;
    }

    /**
     * 样例
     */
    //@Scheduled(cron = "0/10 * * * * ? ") //每10秒一次
    public void sendData() {
        if(webSocketOnlyTaskSet.containsKey("IOT") || webSocketIotOnlyTaskSet.containsKey("IOT")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("test", "测试");
            webSocketService.sendMessageIOTByTaskId("WARNTSET###" + jsonObject, "IOT");
        }
    }

    /**
     * 初始化redis中交通数据-每天23:59:50秒清理所有key
     */
    //@Scheduled(cron = "50 59 23 1/1 * ? ") //每天23:59:50秒执行
    public void initRedisTrafficData() {
        redisUtil.deleteRedisKey("IOT");
    }

    public static synchronized void syncTask() {

        //测试发现某些计算操作AtomicInteger是线程安全的,效率方面使用synchronized关键字最高
        //TODO JUSTPLAY哈哈
    }

}
