package com.bisontecfacturacion.security.config;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/socket")
public class ServerEjemplo1  {
		@OnOpen
		public void onOPen(Session sesion) throws IOException{
			sesion.getBasicRemote().sendText("Hola mundo");// aca obtengo el canal donde me voy a conectar
			
		}
}
