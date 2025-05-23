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
import com.bisontecfacturacion.security.educacion.model.Procedencia;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.model.Turno;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.ProcedenciaRepository;
import com.bisontecfacturacion.security.educacion.repository.TipoCarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.TurnoRepository;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
@RestController
@RequestMapping("carrera")
public class CarreraController {
	@Autowired
	private CarreraRepository entityRepository;

	
	@RequestMapping(method = RequestMethod.GET)
	public List<Carrera> getAllCarrera(){
		 return listSer(entityRepository.findAll());
	}
	
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Carrera entity){
		try {
			if(entity.getDescripcion()!=null){
				entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().trim().toUpperCase()));			
			}else {
				return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL ES OBLIGATORIO"), HttpStatus.CONFLICT);
			}
			if(entity.getDuracion()>0){
			}else {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR LA DURACIÒN DE LA CARRERA"), HttpStatus.CONFLICT);
			}
			if(entity.getTipoCarrera().getId()!=0) {}else {
				return new ResponseEntity<>(new CustomerErrorType("EL TIPO DE CARRERA ES OBLIGATORIO PARA PODER GUARDAR LOS DATOS"), HttpStatus.CONFLICT);
			}
			
			if(entity.getAplicacion()!=null){
				entity.setAplicacion(Utilidades.eliminaCaracterIzqDer(entity.getAplicacion().trim().toUpperCase()));			
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
	public List<Carrera> consultarPorDescripcion(@PathVariable String descripcion){
		List<Carrera> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public Carrera consultarPorId(@PathVariable int id){
		return entityRepository.findById(id).orElse(null);
	}
	
	public List<Carrera> listSer(List<Carrera> objeto) {
		List<Carrera> servi=new ArrayList<>();
		for(Carrera ob:objeto){   

			Carrera s=new Carrera();
			s.setId(ob.getId());
			s.setDescripcion(ob.getDescripcion());
			s.setAplicacion(ob.getAplicacion());
			s.setCostoMatricula(ob.getCostoMatricula());
			s.setCostoMensual(ob.getCostoMensual());
			s.setCostoAumentoAnual(ob.getCostoAumentoAnual());
			s.setCostoDerechoExamen(ob.getCostoDerechoExamen());
			s.setDuracion(ob.getDuracion());
			s.getTipoCarrera().setId(ob.getTipoCarrera().getId());
			s.getTipoCarrera().setDescripcion(ob.getTipoCarrera().getDescripcion());
			servi.add(s);
		}

		return servi;
	}
}
