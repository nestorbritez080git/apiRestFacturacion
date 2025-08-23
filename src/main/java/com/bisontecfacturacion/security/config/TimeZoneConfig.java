package com.bisontecfacturacion.security.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeZoneConfig {
	 	@Value("${app.timezone}")
	    private String timeZone;

	    @PostConstruct
	    public void init() {
	        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
	        System.out.println("Zona horaria JVM configurada desde propiedades: " + timeZone);
	    }
}
