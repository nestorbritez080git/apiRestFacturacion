package com.bisontecfacturacion.security.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadFolderConfig {
	private static final Logger logger = LoggerFactory.getLogger(UploadFolderConfig.class);

    @Value("${spring.servlet.multipart.location}")
    private String uploadLocation;

    @PostConstruct
    public void init() {
        File dir = new File(uploadLocation).getAbsoluteFile();
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                logger.info("✅ Carpeta de uploads creada en: {}", dir.getAbsolutePath());
            } else {
                logger.error("❌ No se pudo crear la carpeta de uploads en: {}", dir.getAbsolutePath());
            }
        } else {
            logger.info("✅ Carpeta de uploads ya existe: {}", dir.getAbsolutePath());
        }
    }
}
