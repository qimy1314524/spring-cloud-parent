package com.smart.aiplatformauth.service;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @desc: websocket数据推送服务
 * @desc: 【重要必读】同一个websocket连接需要推送多个task数据时,taskIds为多个任务id的逗号拼接字符串
 * @author: chengjz
 */
@Component
@ServerEndpoint("/webSocket/taskDataStream/{taskIds}")
@Slf4j
public class WebSocketService {

    private Session session;

    //concurrent包的线程安全Set,用来存放每个客户端对应的MyWebSocket对象。可实现服务端与单一或多个客户端的通信,Map的Key是用户连接标识
    public static ConcurrentHashMap<String, WebSocketService> webSocketSet = new ConcurrentHashMap<String, WebSocketService>();

    //该map主要为实现同一websocket连接,需要推送多个task数据时,对mapkey taskId和mapvalue session的管理
    public static HashMap<String, String> webSocketOnlyTaskSet = new HashMap<String, String>();

    //该map同上但专为IOT大数据
    public static HashMap<String, String> webSocketIotOnlyTaskSet = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session, @PathParam("taskIds") String taskIds) {
        this.session = session;
        webSocketSet.put(session.getId(), this);//加入map中
        if(taskIds.contains("IOT")) {//专为IOT大数据
            for (String taskId : taskIds.split(",")) {
                if (webSocketIotOnlyTaskSet.containsKey(taskId)) //如果存在相同的客户端连接,说明有多个客户端需要同一task的数据,需进行处理，实现服务端对多个客户端推送的支持
                    webSocketIotOnlyTaskSet.put(taskId, webSocketIotOnlyTaskSet.get(taskId) + "," + session.getId());
                else
                    webSocketIotOnlyTaskSet.put(taskId, session.getId());
            }

        } else  {
            for (String taskId : taskIds.split(",")) {
                if (webSocketOnlyTaskSet.containsKey(taskId)) //如果存在相同的客户端连接,说明有多个客户端需要同一task的数据,需进行处理，实现服务端对多个客户端推送的支持
                    webSocketOnlyTaskSet.put(taskId, webSocketOnlyTaskSet.get(taskId) + "," + session.getId());
                else
                    webSocketOnlyTaskSet.put(taskId, session.getId());
            }
        }

        log.info("【websocket消息】有新的连接, 总数:{}", webSocketSet.size());
    }

    @OnClose
    public void onClose(Session session, @PathParam("taskIds") String taskIds) {
        webSocketSet.remove(session.getId());//从map中删除
        for (String taskId : taskIds.split(",")) {
            if (webSocketOnlyTaskSet.containsKey(taskId)) {
                String sessionids = webSocketOnlyTaskSet.get(taskId);
                if (sessionids.split(",").length == 1) {
                    webSocketOnlyTaskSet.remove(taskId);
                } else {
                    String newsessionids = "";
                    for (String id : sessionids.split(",")) {
                        if (!id.equals(session.getId()))
                            newsessionids += (id + ",");
                    }
                    webSocketOnlyTaskSet.put(taskId, newsessionids);
                }
            }
            //下面这一小段专为IOT大数据
            if (webSocketIotOnlyTaskSet.containsKey(taskId)) {
                String sessionids = webSocketIotOnlyTaskSet.get(taskId);
                if (sessionids.split(",").length == 1) {
                    webSocketIotOnlyTaskSet.remove(taskId);
                } else {
                    String newsessionids = "";
                    for (String id : sessionids.split(",")) {
                        if (!id.equals(session.getId()))
                            newsessionids += (id + ",");
                    }
                    webSocketIotOnlyTaskSet.put(taskId, newsessionids);
                }
            }
        }
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        //session.getBasicRemote().sendText(message);
        log.info("【websocket消息】收到客户端:{},发来的消息:{}", session.getId(), message);
    }

    /**
     * 通过websocket将消息推送给客户端---通用---sessionId连接组的值通过webSocketOnlyTaskSet.get(taskId)得到
     * @param message
     * @param sessionId
     */
    public void sendMessage(String message, String sessionId) {
        for (String id : sessionId.split(",")) {
            if (webSocketSet.containsKey(id)) {
                try {
                    webSocketSet.get(id).session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过websocket将消息推送给客户端---专用---通过taskId推送消息给一个或多个客户端
     * @param message
     * @param taskId
     */
    public void sendMessageByTaskId(String message, String taskId) {
        String sessionId = webSocketOnlyTaskSet.get(taskId);
        if(sessionId != null) {
            for (String id : sessionId.split(",")) {
                if (webSocketSet.containsKey(id)) {
                    try {
                        webSocketSet.get(id).session.getBasicRemote().sendText(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 通过websocket将消息推送给客户端---专用---通过taskId推送消息给一个或多个客户端--专为IOT大数据
     * @param message
     * @param taskId
     */
    public void sendMessageIOTByTaskId(String message, String taskId) {
        String sessionId = webSocketIotOnlyTaskSet.get(taskId);
        if(sessionId != null) {
            for (String id : sessionId.split(",")) {
                if (webSocketSet.containsKey(id)) {
                    try {
                        webSocketSet.get(id).session.getBasicRemote().sendText(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}