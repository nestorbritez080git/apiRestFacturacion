package com.bisontecfacturacion.security.config;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketConfigController {

	@MessageMapping("/envio") // Mapea el endpoint de entrada de mensajes
    @SendTo("/tema/mensajes") //
	 public Messag sendMessage(Messag message) {
			System.out.println("ESSSSSS mensajeeee");
	        return message;
	   }
}
