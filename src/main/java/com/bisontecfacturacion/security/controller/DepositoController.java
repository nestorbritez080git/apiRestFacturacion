package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Deposito;
import com.bisontecfacturacion.security.repository.DepositoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("deposito")
public class DepositoController {
	@Autowired
	private DepositoRepository entityRepository;

	@RequestMapping(method=RequestMethod.GET)
	public List<Deposito> getAll(){
		return entityRepository.findByOrderByIdDesc();
	}

	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Deposito getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Deposito entity){
		if(entity.getDescripcion()!=null) {
			entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));			
		}
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCÓN "+entity.getDescripcion()+" YA EXISTE."), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	public boolean siExiste(Deposito entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}

	@RequestMapping(method=RequestMethod.PUT)
	public Deposito editar(@RequestBody Deposito entity){
		if(entity.getDescripcion()!=null) {
			entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));			
		}
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}


	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Deposito> consultarPorDescripcion(@PathVariable String descripcion){
		return entityRepository.findByTop100DescripcionLike(Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase()));
	}
	 

}
