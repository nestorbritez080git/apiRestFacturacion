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
import com.bisontecfacturacion.security.educacion.model.Alumnos;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.repository.AlumnosRepository;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.TipoCarreraRepository;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
@RestController
@RequestMapping("alumnos")
public class AlumnosController {
	@Autowired
	private AlumnosRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Alumnos> getAll(){
		return  listarAlumnos(entityRepository.findTop100ByOrderByIdDesc());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/primerId")
	public int getPrimerId(){
		Alumnos  c=new Alumnos();
		c=entityRepository.findTop1ByOrderByIdAsc();
		int id=c.getId();
		return id;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public Alumnos getId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Alumnos entity){
		try {
			if(entity.getPersona().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UNA PERSONA PARA GENERA EL ALUMNOS"), HttpStatus.CONFLICT);		
			}else if (siExistePersona(entity.getPersona())== true) {
				return new ResponseEntity<>(new CustomerErrorType("ESTE ALUMNOS YA POSEE CREDENCIALES PARA EL SISTEMA\nSI PERSISTE EL INCOVENIENTE SE DEBE CONSULTAR CON EL ADMINISTRADOR DEL SISTEMA"), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	
	public boolean siExistePersona(Persona entity){
		if(entityRepository.getIdPersona(entity.getId())!=null){
			return true;
		}
		return false;
	}
	private List<Alumnos>listarAlumnos(List<Alumnos> lista){
		List<Alumnos> listaRetorno=new ArrayList<>();
		for (Alumnos ob: lista){
			Alumnos a = new Alumnos();
			a.setId(ob.getId());
			a.setPersona(ob.getPersona());
		}
		return listaRetorno;
	}
/*	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Alumnos> consultarPorDescripcion(@PathVariable String descripcion){
		List<Object[]> objeto=entityRepository.getBuscarPorDescripcion("%"+descripcion.toUpperCase()+"%");
		List<Cliente> cliente=new ArrayList<>();
		for(Object[] ob:objeto){
			Cliente clientes=new Cliente();
			clientes.setId(Integer.parseInt(ob[0].toString()));
			clientes.getPersona().setNombre(ob[1].toString());
			clientes.getPersona().setApellido(ob[2].toString());
			clientes.setLimiteCredito(Double.parseDouble(ob[3].toString()));
			clientes.getPersona().setCedula(ob[4].toString());
			clientes.getPersona().setTelefono(ob[5].toString());
			clientes.setEstadoBloqueo(Boolean.parseBoolean(ob[6].toString()));
			cliente.add(clientes);
		}
		return cliente;
	}
	
	
	
	*/
}
