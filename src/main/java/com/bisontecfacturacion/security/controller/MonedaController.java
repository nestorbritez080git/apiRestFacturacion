package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Moneda;
import com.bisontecfacturacion.security.repository.MonedaRepsitory;

@Transactional()
@RestController
@RequestMapping("moneda")
public class MonedaController {
	@Autowired
	private MonedaRepsitory entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Moneda> getAll(){
		return entityRepository.findByOrderByIdAsc();
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody List<Moneda> LisyEntity){
		try {
			for (int i = 0; i < LisyEntity.size(); i++) {
				Moneda m = LisyEntity.get(i);
				entityRepository.save(m);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("No se ha podido completar las actualizaciones de la contización del día", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
}
