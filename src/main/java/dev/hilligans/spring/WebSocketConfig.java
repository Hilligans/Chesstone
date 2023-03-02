package dev.hilligans.spring;

import dev.hilligans.storage.Database;
import dev.hilligans.storage.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketTextHandler(), "/chesstone/play/{id}").setAllowedOrigins("*");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chesstone/play/{id}").setAllowedOrigins("*")
                .withSockJS()
                .setInterceptors(httpSessionHandshakeInterceptor());
    }
    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest servletServerRequest) {
                    HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
                    Cookie tokenKey = WebUtils.getCookie(servletRequest, "chesstone_token");
                    Token token = Database.getAccountToken(tokenKey.getValue());
                    if(token == null) {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false;
                    } else {
                        attributes.put("chesstone_token", tokenKey);
                    }
                }
                return true;
            }
            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }
}