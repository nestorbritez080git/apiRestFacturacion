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
import com.bisontecfacturacion.security.model.CategoriaConsumiciones;
import com.bisontecfacturacion.security.model.Consumiciones;
import com.bisontecfacturacion.security.repository.CategoriaConsumicionesRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("categoriaConsumiciones")
public class CategoriaConsumicionesController {
	@Autowired
	private CategoriaConsumicionesRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<CategoriaConsumiciones> getAll(){
		return entityRepository.findByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<CategoriaConsumiciones> consultarPorDescripcion(@RequestBody String descripcion){
		System.out.println(descripcion);
		return entityRepository.findByTop100DescripcionLike("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	CategoriaConsumiciones getConsumicionesPorId(@PathVariable int id){
		System.out.println("ejecuto getcons id"+id);
		return entityRepository.findById(id).get();

	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody CategoriaConsumiciones entity) {
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("La descripción "+entity.getDescripcion()+" ya existe."), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(CategoriaConsumiciones entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}
	
}
