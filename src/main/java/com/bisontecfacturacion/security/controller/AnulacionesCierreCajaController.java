package com.bisontecfacturacion.security.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.AnulacionesCierreCaja;
import com.bisontecfacturacion.security.repository.AnulacionesCierreCajaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional()
@RestController
@RequestMapping("anulacionesCierreCaja")
public class AnulacionesCierreCajaController {
	@Autowired
	private AnulacionesCierreCajaRepository entityRepository;
	@RequestMapping(method=RequestMethod.POST)
	public  ResponseEntity<?>  guardar(@RequestBody AnulacionesCierreCaja entity){
		if(entity.getFuncionario().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(entity.getCierreCaja().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("EL NÚERO DE CIERRE QUE SE HA ANULADO NO PUEDE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return new ResponseEntity<Map<String, String>>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.GET, value = "/consultarId/{id}")
	public  AnulacionesCierreCaja  consultarID(@PathVariable int id){
		return entityRepository.getIdCierreCabecera(id);
	}
}
