package com.sky.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置类，用于注册WebSocket的Bean，启用WebSocket功能
 * 自动扫描和注册WebSocket端点：Spring Boot会自动扫描所有使用@ServerEndpoint注解的类
 * 管理WebSocket生命周期：负责WebSocket连接的创建、销毁等生命周期管理
 * 集成Spring容器：让WebSocket组件能够使用Spring的依赖注入功能
 */
@Configuration
public class WebSocketConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
