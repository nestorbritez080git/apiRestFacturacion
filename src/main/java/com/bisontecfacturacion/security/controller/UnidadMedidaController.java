package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.UnidadMedida;
import com.bisontecfacturacion.security.repository.UnidadMedidaRepository;

@Transactional
@RestController
@RequestMapping("unidadMedida")
public class UnidadMedidaController {
	@Autowired
	private UnidadMedidaRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<UnidadMedida> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public UnidadMedida getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public UnidadMedida guardar(@RequestBody UnidadMedida entity){
		 return  entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.PUT)
	public UnidadMedida editar(@RequestBody UnidadMedida entity){
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	/*
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Cargo> consultarPorDescripcion(@PathVariable String descripcion){
		return cargoRepository.findByTop100DescripcionLike(descripcion.toUpperCase());
	}
*/
		
}
