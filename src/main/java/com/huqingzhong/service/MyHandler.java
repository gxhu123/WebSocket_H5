package com.huqingzhong.service;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
public class MyHandler implements WebSocketHandler {
	
    //在线用户列表
    private static final Map<String, WebSocketSession> users;


    static {
        users = new HashMap<>();
    }
    //新增socket
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String ID=this.getClientId(session);
         if (ID != null) {
             users.put(ID, session);
             session.sendMessage(new TextMessage("成功建立socket连接"));
         }
         System.out.println("当前在线人数："+users.size());

         while (true){
             int c=new Random().nextInt(20);
             sendMessageToAllUsers(c+"");
             Thread.sleep(1000);
         }
    }
 
    //接收socket信息
    @Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
    	try{
    	    if (webSocketMessage.getPayloadLength()>0){
                JSONObject json=JSONObject.parseObject((String)webSocketMessage.getPayload());
                System.out.println("json="+json);
                String message=json.getString("message");

                //如果前端有消息过来则发送前端消息，否则发送后台服务器实时消息
                if(message!="" || message!=null){
                    sendMessageToAllUsers(message);
                    //sendMessageToUser(webSocketSession.getId(), message); 单播
                }
            }
    	 }catch(Exception e){
      	   e.printStackTrace();
         }
	}
 
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, String message) {
        if (users.get(clientId) == null) return false;

        TextMessage textMessage=new TextMessage(message);
        WebSocketSession session = users.get(clientId);
        System.out.println("sendMessage:" + session);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
    /**
     * 广播信息
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(String message) {
        boolean allSendSuccess = true;
        Set<String> clientIds = users.keySet();

        TextMessage textMessage=new TextMessage(message);
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = users.get(clientId);
                System.out.println(session.getClass().getName());
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
 
        return  allSendSuccess;
    }
 
 
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("连接出错");
        users.remove(getClientId(session));
    }
 
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接已关闭：" + status);
        users.remove(getClientId(session));
    }
 
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
 
    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private String getClientId(WebSocketSession session) {
        try {
            //Integer clientId = (Integer) session.getAttributes().get("WEBSOCKET_USERID");
            String clientId =  session.getId();
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
}
