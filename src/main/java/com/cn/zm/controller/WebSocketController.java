package com.cn.zm.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;
import com.cn.zm.config.MyHandler;

@Controller
@RequestMapping("/socket")
public class WebSocketController {
	
	@Autowired
	private MyHandler myHandler;
	 
	/**
     * 第一个用户
     *
     * @param request
     * @return
     */
    @RequestMapping("/notice1")
    public String chat1(HttpServletRequest request) {
        // 假设用户tom登录,存储到session中
    	//获取数据库中待办的消息
        request.getSession().setAttribute("WEBSOCKET_USERNAME", "tom");
        return "notice1";
    }

    /**
     * 第二个用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping("/notice2")
    public String chat2(HttpServletRequest request) {
        // 假设用户jerry登录,存储到session中
        request.getSession().setAttribute("WEBSOCKET_USERNAME", "jerry");
        return "notice2";
    }
    
    /**
     * 群发通知
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendToAll")
    public String sendToAll(String msg){
    	//先保存数据库
    	JSONObject obj = new JSONObject();
    	obj.put("msg", msg);
    	TextMessage message = new TextMessage(obj.toJSONString());
    	myHandler.sendMessageToUsers(message);
    	return "sucess";
    }
    
    /**
     * 单个发
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendToUser")
    public String sendToUser(String msg){
    	//先保存数据库
    	JSONObject obj = new JSONObject();
    	obj.put("msg", msg);
    	TextMessage message = new TextMessage(obj.toJSONString());
    	myHandler.sendMessageToUser("tom", message);
    	return "sucess";
    }
}
