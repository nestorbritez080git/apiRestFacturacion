package com.bisontecfacturacion.security.hoteleria.controller;

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
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.model.Habitaciones;
import com.bisontecfacturacion.security.hoteleria.model.HabitacionesCategoriaCombo;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.HabitacionesCategoriaComboRepository;
import com.bisontecfacturacion.security.hoteleria.repository.HabitacionesRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("habitaciones")
public class HabitacionesController {
	@Autowired
	private HabitacionesRepository entityRepository;
	
	@Autowired
	private HabitacionesCategoriaComboRepository comboRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Habitaciones> getAll(){
		 return entityRepository.findAll();
	}
	@RequestMapping(method = RequestMethod.GET, value = "/combo")
	public List<HabitacionesCategoriaCombo> getAllHabitacionesCombo(){
		 return listHabitacionCombo(comboRepository.getAll());
	}
	@RequestMapping(method = RequestMethod.GET, value = "/combo/disponilidad")
	public List<HabitacionesCategoriaCombo> getAllHabitacionesComboDisponibilidad(){
		 return listHabitacionCombo(comboRepository.getAllComboDisponilidadHabitacion());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/combo/buscar/disponilidad/{descripcion}")
	public List<HabitacionesCategoriaCombo> consultarComboDisponilidadPorDescripcion(@PathVariable String descripcion){
		return listHabitacionCombo(comboRepository.getAllDescripcionDisponilidad("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Habitaciones entity){
		try {
			//entity.getFuncionarioAutorizado().setId(0);
			if(entity.getDescripcion()!=null){
				entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().trim().toUpperCase()));			
			}else {
				return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL ES OBLIGATORIO"), HttpStatus.CONFLICT);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/combo")
	public ResponseEntity<?> guardar(@RequestBody HabitacionesCategoriaCombo entity){
		try {
			//entity.getFuncionarioAutorizado().setId(0);
			if(entity.getHabitaciones().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UNA HABITACION PARA EL COMBO"), HttpStatus.CONFLICT);
			}else if(entity.getCategoriaHabitaciones().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR CATEGORIA DE HABITACION PARA EL COMBO"), HttpStatus.CONFLICT);
			}
			if(entity.getId()==0) {
				HabitacionesCategoriaCombo d= comboRepository.getHabitacionesComboRegistrado(entity.getCategoriaHabitaciones().getId(), entity.getHabitaciones().getId());
				if(d==null) {}else {return new ResponseEntity<>(new CustomerErrorType("ESTA COMBINACION NUEVA YA ESTÁ DISPONIBLE DENTRO DEL REGISTRO"), HttpStatus.CONFLICT);}	
			}else {
				HabitacionesCategoriaCombo d= comboRepository.getHabitacionesComboRegistradoEditar(entity.getCategoriaHabitaciones().getId(), entity.getHabitaciones().getId(), entity.getId());
				if(d==null) {}else {return new ResponseEntity<>(new CustomerErrorType("ESTA COMBINACION MODIFICADO YA ESTÁ DISPONIBLE DENTRO DEL REGISITRO"), HttpStatus.CONFLICT);}
			}
			entity.setAplicacion(Utilidades.eliminaCaracterIzqDer(entity.getAplicacion().toUpperCase()));
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		comboRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Habitaciones> consultarPorDescripcion(@PathVariable String descripcion){
		List<Habitaciones> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	}
	public List<Habitaciones> listSer(List<Habitaciones> objeto) {
		List<Habitaciones> servi=new ArrayList<>();
		for(Habitaciones ob:objeto){
			Habitaciones s=new Habitaciones();
			s.setId(ob.getId());
			s.setDescripcion(ob.getDescripcion());
			servi.add(s);
		}

		return servi;
	}
	@RequestMapping(method=RequestMethod.GET, value="/combo/validarRegistro/{idCat}/{idHab}")
	public HabitacionesCategoriaCombo validarRegistroCombo(@PathVariable int idCat,  @PathVariable int idHab){
		return comboRepository.getHabitacionesComboRegistrado(idCat, idHab);
	}
	@RequestMapping(method=RequestMethod.GET, value="/combo/buscar/{descripcion}")
	public List<HabitacionesCategoriaCombo> consultarPorDescripcionCombo(@PathVariable String descripcion){
		List<Object[]> objeto=comboRepository.getAllDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listHabitacionCombo(objeto);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/updateEstado/{id}/{estDispo}/{estReser}")
	public ResponseEntity<?> udapteEstadoHabitacion(@PathVariable int id,@PathVariable Boolean estDispo, @PathVariable Boolean estReser){
		try {
			entityRepository.findByActualizaEstadoDisponilidadReservacion(id, estDispo, estReser);
			
		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("HUBO UN ERROR AL ACTUALIZAR ESTADO HABITACIÓN"), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	public List<HabitacionesCategoriaCombo> listHabitacionCombo(List<Object[]> objeto) {
		List<HabitacionesCategoriaCombo> servi=new ArrayList<>();
		for(Object[] ob:objeto){
			HabitacionesCategoriaCombo s=new HabitacionesCategoriaCombo();
			s.setId(Integer.parseInt(ob[0].toString()));
			s.setAplicacion(ob[1].toString());
			s.setPrecioMinimo(Double.parseDouble(ob[2].toString()));
			s.setPrecioNormal(Double.parseDouble(ob[3].toString()));
			s.getHabitaciones().setId(Integer.parseInt(ob[4].toString()));
			s.getHabitaciones().setDescripcion(ob[5].toString());
			s.getCategoriaHabitaciones().setId(Integer.parseInt(ob[6].toString()));
			s.getCategoriaHabitaciones().setDescripcion(ob[7].toString());
			s.getHabitaciones().setEstadoDisponibilidad(Boolean.parseBoolean(ob[8].toString()));
			s.getHabitaciones().setEstadoReservacion(Boolean.parseBoolean(ob[9].toString()));
			servi.add(s);
		}

		return servi;
	}
	
	
}
