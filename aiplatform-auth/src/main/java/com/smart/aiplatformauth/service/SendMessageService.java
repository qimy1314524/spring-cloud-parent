package com.smart.aiplatformauth.service;

import static com.smart.aiplatformauth.service.WebSocketService.webSocketOnlyTaskSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 消息发送、推送、通知服务
 * @author chengjz
 */
@Component
public class SendMessageService {

    @Autowired
    private WebSocketService webSocketService;

    @Async
    public void sendWebsocketMessageByTaskIdInOnlyTaskSet(String taskId, String message) {
      if(webSocketOnlyTaskSet.containsKey(taskId)) {
        webSocketService.sendMessageByTaskId(message, taskId);
      }
    }
}
