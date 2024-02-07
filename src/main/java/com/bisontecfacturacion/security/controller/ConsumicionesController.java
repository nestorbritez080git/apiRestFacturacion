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
import com.bisontecfacturacion.security.model.Consumiciones;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.repository.ConsumicionesRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("consumiciones")
public class ConsumicionesController {
	@Autowired
	private ConsumicionesRepository entityRepository;

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Consumiciones entity) {
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("La descripción "+entity.getDescripcion()+" ya existe."), HttpStatus.CONFLICT);
		}else if(entity.getCategoriaConsumiciones().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UNA CATEGORIA DE CONSUMICION PARA PODER GUARDAR"), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(Consumiciones entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}
	@RequestMapping(method=RequestMethod.PUT)
	public Consumiciones editar(@RequestBody Consumiciones entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		
		return entityRepository.save(entity);
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	Consumiciones getConsumicionesPorId(@PathVariable int id){
		System.out.println("ejecuto getcons id"+id);
		return entityRepository.findById(id).get();

	}
	@RequestMapping(method = RequestMethod.GET)
	List<Consumiciones> getConsumiciones(){
		return entityRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<Consumiciones> consultarPorDescripcion(@RequestBody String descripcion){
		System.out.println(descripcion);
		return entityRepository.findByTop100DescripcionLike("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
	}


}
