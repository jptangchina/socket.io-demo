package com.example.socketio.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author jp.tang
 */
@Configuration
@EnableWebSocket
@Setter(onMethod_ = @Autowired)
public class MyWebSocketConfigurer implements WebSocketConfigurer {

    private MyWebSocketHandler webSocketHandler;

    private MyWebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/socket.io/")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketHandshakeInterceptor);

    }
}
