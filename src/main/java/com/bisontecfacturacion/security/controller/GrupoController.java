package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.repository.GrupoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("grupo")
public class GrupoController {
	@Autowired
	private GrupoRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Grupo> getAll(){
		return entityRepository.findByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="totalgrupo")
	public Object[] getAllTotales(){
		return entityRepository.findByGrupo();
}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Grupo getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Grupo entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("La descripción "+entity.getDescripcion()+" ya existe."), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(Grupo entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}
	@RequestMapping(method=RequestMethod.PUT)
	public Grupo editar(@RequestBody Grupo entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Grupo> consultarPorDescripcion(@PathVariable String descripcion){
		System.out.println(descripcion);
		return entityRepository.findByTop100DescripcionLike(Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase()));
	}

		
}
