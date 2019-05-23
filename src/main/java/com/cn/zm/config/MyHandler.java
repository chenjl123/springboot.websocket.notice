package com.cn.zm.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


@Service
public class MyHandler implements WebSocketHandler {

	//保存在线用户
	private final static List<WebSocketSession> SESSIONS = Collections.synchronizedList(new ArrayList<>());
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		System.out.println("链接关闭......" + closeStatus.toString());
	    SESSIONS.remove(session);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SESSIONS.add(session);
        String userName = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        if (userName != null) {
        	System.out.println(userName + "登录系统成功");
        }
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		System.out.println("处理要发送的消息");
		JSONObject msg = JSON.parseObject(message.getPayload().toString());
		JSONObject obj = new JSONObject();
        if (msg.getInteger("type") == 1) {
            //给所有人
            obj.put("msg", msg.getString("msg"));
            sendMessageToUsers(new TextMessage(obj.toJSONString()));
        } else {
            //给个人
            String to = msg.getString("to");
            obj.put("msg", msg.getString("msg"));
            sendMessageToUser(to, new TextMessage(obj.toJSONString()));
        }
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
            session.close();
        }
	    System.out.println("链接出错，关闭链接......");
	    SESSIONS.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	/**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession user : SESSIONS) {
            if (user.getAttributes().get("WEBSOCKET_USERNAME").equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
