package com.bisontecfacturacion.security.config;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
	@MessageMapping("/chat/message") // Mapea el endpoint de entrada de mensajes
    @SendTo("/topic/messages") // Envía el mensaje a este topic
    public Messag sendMessage(Messag message) {
		System.out.println("ESSSSSS mensajeeee");
        return message;
    }
}
