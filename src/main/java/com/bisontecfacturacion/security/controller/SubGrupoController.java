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

import com.bisontecfacturacion.security.model.SubGrupo;
import com.bisontecfacturacion.security.repository.SubGrupoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("subGrupo")
public class SubGrupoController {
	@Autowired
	private SubGrupoRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<SubGrupo> getAll(){
		return entityRepository.findByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public SubGrupo getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody SubGrupo entity){
		entity.setDescripcion(entity.getDescripcion().toUpperCase());
		System.out.println("id: "+ entity.getId());
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("La descripción "+entity.getDescripcion()+" ya existe."), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(SubGrupo entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}
	@RequestMapping(method=RequestMethod.PUT)
	public SubGrupo editar(@RequestBody SubGrupo entity){
		entity.setDescripcion(entity.getDescripcion().toUpperCase());
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<SubGrupo> consultarPorDescripcion(@PathVariable String descripcion){
		return entityRepository.findByTop100DescripcionLike(descripcion.toUpperCase());
	}

		
}
