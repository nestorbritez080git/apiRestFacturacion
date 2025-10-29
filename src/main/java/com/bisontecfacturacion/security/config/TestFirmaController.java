package com.bisontecfacturacion.security.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("controlSerial")
public class TestFirmaController {
	 private final XmlSignerService xmlSignerService;

	    public TestFirmaController(XmlSignerService xmlSignerService) {
	        this.xmlSignerService = xmlSignerService;
	    }
		@RequestMapping(method=RequestMethod.GET , value="/firmar")
	    public String firmar() {
	        try {
	            String certificado = "C:/certificados/mi_certificado.p12";
	            String clave = "123456"; // clave del .p12
	            String xmlEntrada = "C:/facturas/factura.xml";
	            String xmlSalida = "C:/facturas/factura_firmada.xml";

	            xmlSignerService.firmarXml(certificado, clave, xmlEntrada, xmlSalida);
	            return "Factura firmada con éxito";
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "Error al firmar: " + e.getMessage();
	        }
	    }
}
