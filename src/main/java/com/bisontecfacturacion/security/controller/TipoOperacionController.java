package com.bisontecfacturacion.security.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bisontecfacturacion.security.config.CotizacionesDTO;
import com.bisontecfacturacion.security.model.Moneda;
import com.bisontecfacturacion.security.model.TipoOperacion;
import com.bisontecfacturacion.security.repository.MonedaRepsitory;
import com.bisontecfacturacion.security.repository.TipoOperacionRepository;

@Transactional()
@RestController
@RequestMapping("tipoOperacion")
public class TipoOperacionController {

	@Autowired
	private TipoOperacionRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<TipoOperacion> getAll(){
		return entityRepository.findByOrderByIdAsc();
	}

}
