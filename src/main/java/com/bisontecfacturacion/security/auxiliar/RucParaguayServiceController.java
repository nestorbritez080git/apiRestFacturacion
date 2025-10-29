package com.bisontecfacturacion.security.auxiliar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ruc")
public class RucParaguayServiceController {
	 private  RucParaguayService service = new RucParaguayService();

	   
	    @GetMapping("/{ruc}")
	    public String getContribuyente(@PathVariable String ruc) {
	        return service.consultarContribuyente(ruc);
	    }
}
