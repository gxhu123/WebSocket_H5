package com.huqingzhong.config;
 
import com.huqingzhong.service.MyHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

 
//实现接口来配置Websocket请求的路径和拦截器。
@Configuration
@EnableWebSocket
public class WebSocketH5Config implements WebSocketConfigurer{
 
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
	//handler是webSocket的核心，配置入口
	registry.addHandler(new MyHandler(), "/myHandler/").setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor());
		//System.out.println("registerWebSocketHandlers ..... ..");
	}
 
	
}
