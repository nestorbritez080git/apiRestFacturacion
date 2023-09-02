package com.bisontecfacturacion.security.config;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@ClientEndpoint
public class ClientEjemplo1 {
	private final AtomicReference<String> message= new AtomicReference<>();
	private  static final Logger log= Logger.getLogger(ClientEjemplo1.class.getName());
	public void onOpen(Session ses, EndpointConfig endC) {
		log.info("Open");
	}
	@OnMessage
	public void onMessage(final String mesage) {
		this.message.set(mesage);
	}
	@OnClose
	public void onClose(Session ses, CloseReason rea) {
		log.info("Close Reason: "+rea);
	}
	public String getMessage() {
		return this.message.get();
	}
}
