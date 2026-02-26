package com.bisontecfacturacion.security.auxiliar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ruc")
@CrossOrigin(origins = "*") // si Angular está separado
public class RucParaguayServiceController {
	 		
	   @Autowired
	    private RucParaguayService service;

	   @GetMapping("/{ruc}")
	    public ResponseEntity<?> getContribuyente(@PathVariable String ruc) {
	        return service.consultarContribuyente(ruc);
	    }
}
