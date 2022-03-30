package com.j23.server.configuration;

import com.j23.server.handler.WaitingListWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final static String WAITING_LIST_ENDPOINT = "/waitingList";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWaitingListWebSocketHandler(), WAITING_LIST_ENDPOINT).
                setAllowedOrigins("http://localhost:4200", "http://127.0.0.1:4200");
    }

    @Bean
    public WebSocketHandler getWaitingListWebSocketHandler() {
        return new WaitingListWebSocketHandler();
    }

}
