package com.example.service;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Lex
 */

@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    static Log log = LogFactory.getLog(WebSocketServer.class);


    /**
     * 静态变量,记录当前在线连接数,其对应的操作方法是线程安全的
     */
    private static int onlineCount = 0;

    /**
     * 静态变量,线程安全的Set,存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    private String sid = "";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听,id为:" + sid + ",当前在线人数为:" + getOnlineCount());
        this.sid = sid;
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("窗口" + sid + "关闭连接,当前在线人数为:" + getOnlineCount());
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage("全服广播: " + "窗口" + sid + "已断开连接!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 服务器接收到某个客户端的消息后,将其群发给所有客户端
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息: \"" + message + "\"");
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage("全服广播: \"" + message + "\"(来自客户端" + sid + ")");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务器主动向客户端推送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 向指定sid的客户端发送自定义消息,若没有指定客户端,则群发至所有客户端
     * 此方法还没有被正式调用过,存在一些问题,暂勿使用
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + ",推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebSocketServer that = (WebSocketServer) o;

        if (!Objects.equals(session, that.session)) {
            return false;
        }
        return Objects.equals(sid, that.sid);
    }

    @Override
    public int hashCode() {
        int result = session != null ? session.hashCode() : 0;
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        return result;
    }
}
