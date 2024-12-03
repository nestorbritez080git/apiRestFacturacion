package com.bisontecfacturacion.security.educacion.controller;

import java.util.ArrayList;
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
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.TipoCarreraRepository;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
@RestController
@RequestMapping("tipoCarrera")
public class TipoCarreraController {
	@Autowired
	private TipoCarreraRepository entityRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<TipoCarrera> getAllTipoCarrera(){
		 return listSer(entityRepository.findAll());
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody TipoCarrera entity){
		try {
			if(entity.getDescripcion()!=null){
				entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().trim().toUpperCase()));			
			}else {
				return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL ES OBLIGATORIO"), HttpStatus.CONFLICT);
			}
			if(entity.getObservacion()!=null){
				entity.setObservacion(Utilidades.eliminaCaracterIzqDer(entity.getObservacion().trim().toUpperCase()));			
			}else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<TipoCarrera> consultarPorDescripcion(@PathVariable String descripcion){
		List<TipoCarrera> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public TipoCarrera consultarPorId(@PathVariable int id){
		return entityRepository.findById(id).orElse(null);
	}
	public List<TipoCarrera> listSer(List<TipoCarrera> objeto) {
		List<TipoCarrera> servi=new ArrayList<>();
		for(TipoCarrera ob:objeto){
			TipoCarrera s=new TipoCarrera();
			s.setId(ob.getId());
			s.setDescripcion(ob.getDescripcion());
			s.setObservacion(ob.getObservacion());
			servi.add(s);
		}
		return servi;
	}
}
