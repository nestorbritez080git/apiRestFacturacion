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
import com.bisontecfacturacion.security.model.Marca;
import com.bisontecfacturacion.security.repository.MarcaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("marca")
public class MarcaController {
	@Autowired
	private MarcaRepository entityRepository;
	

	@RequestMapping(method=RequestMethod.GET)
	public List<Marca> getAll(){
		return entityRepository.findTop30ByOrderByIdDesc();
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/ultMarca")
	public Marca getUltMarca(){
		return entityRepository.findTop1ByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="totalmarca")
	public Object[] getAllTotales(){
		return entityRepository.findByMarca();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Marca getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Marca entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("La descripción "+entity.getDescripcion()+" ya existe."), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(Marca entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}

	@RequestMapping(method=RequestMethod.PUT)
	public Marca editar(@RequestBody Marca entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Marca> consultarPorDescripcion(@PathVariable String descripcion){
		return entityRepository.findByTop100DescripcionLike(Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase()));
	}

		
}
