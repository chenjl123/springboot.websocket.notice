package com.cn.zm.config;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Service
public class MyHandshake implements HandshakeInterceptor{

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse arg1, WebSocketHandler arg2,
			Map<String, Object> attributes) throws Exception {
		 if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            //从连接地址获取用户名
            String userName = (String) servletRequest.getSession().getAttribute("WEBSOCKET_USERNAME");
            attributes.put("WEBSOCKET_USERNAME", userName);
         }
	     return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse arg1, WebSocketHandler arg2, Exception arg3) {
	}
}
