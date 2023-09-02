package com.bisontecfacturacion.security.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/tema"); // Habilita un broker simple
        config.setApplicationDestinationPrefixes("/app"); // Prefijo para los destinos de la aplicación
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*"); // Endpoint para WebSocket
        registry.addEndpoint("/chat").withSockJS(); // Habilita SockJS para compatibilidad con navegadores que no soportan WebSocket
    }
}
