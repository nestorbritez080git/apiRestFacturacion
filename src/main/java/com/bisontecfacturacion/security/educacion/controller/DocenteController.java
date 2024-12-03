package com.bisontecfacturacion.security.educacion.controller;

import java.util.ArrayList;
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
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.Docente;
import com.bisontecfacturacion.security.educacion.repository.AlumnoRepository;
import com.bisontecfacturacion.security.educacion.repository.DocenteRepository;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.service.CustomerErrorType;
@RestController
@RequestMapping("docente")
public class DocenteController {
	@Autowired
	private DocenteRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Docente> getAll(){
		return  listarDocente(entityRepository.findTop100ByOrderByIdDesc());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/primerId")
	public int getPrimerId(){
		Docente  c=new Docente();
		c=entityRepository.findTop1ByOrderByIdAsc();
		int id=c.getId();
		return id;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public Docente getId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion")
	public List<Docente> consultarPorDescripcion(@RequestBody String descripcion){
		List<Docente> objeto=entityRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listarDocente(objeto);
	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Docente entity){
		try {
			if(entity.getId()==0) {
				if(entity.getTipoSangre() ==null || entity.getTipoSangre().equals("")) {}else {entity.setTipoSangre(entity.getTipoSangre().toUpperCase());}
				if(entity.getDatosClinico() ==null || entity.getDatosClinico().equals("")) {}else {entity.setDatosClinico(entity.getDatosClinico().toUpperCase());}

				if(entity.getPersona().getId()==0){
					return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UNA PERSONA PARA GENERA EL ALUMNOS"), HttpStatus.CONFLICT);		
				}else if (siExistePersona(entity.getPersona())== true) {
					return new ResponseEntity<>(new CustomerErrorType("ESTE ALUMNOS YA POSEE CREDENCIALES PARA EL SISTEMA\nSI PERSISTE EL INCOVENIENTE SE DEBE CONSULTAR CON EL ADMINISTRADOR DEL SISTEMA"), HttpStatus.CONFLICT);
//						return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
				}else {
					entityRepository.save(entity);
					return  new  ResponseEntity<String>(HttpStatus.CREATED);
				}
			}else {
				if(entity.getTipoSangre() ==null || entity.getTipoSangre().equals("")) {}else {entity.setTipoSangre(entity.getTipoSangre().toUpperCase());}
				if(entity.getDatosClinico() ==null || entity.getDatosClinico().equals("")) {}else {entity.setDatosClinico(entity.getDatosClinico().toUpperCase());}

				if(entity.getPersona().getId()==0){
					return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UNA PERSONA PARA GENERA EL ALUMNOS"), HttpStatus.CONFLICT);		
				}else if (siExistePersonaEditar(entity)== true) {
					return new ResponseEntity<>(new CustomerErrorType("ESTE ALUMNOS YA POSEE CREDENCIALES PARA EL SISTEMA\nSI PERSISTE EL INCOVENIENTE SE DEBE CONSULTAR CON EL ADMINISTRADOR DEL SISTEMA"), HttpStatus.CONFLICT);
//						return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
				}else {
					entityRepository.save(entity);
					return  new  ResponseEntity<String>(HttpStatus.CREATED);
				}
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
	public boolean siExistePersonaEditar(Docente entity){
		if(entityRepository.getIdPersonaEditar(entity.getPersona().getId(), entity.getId())!=null){
			return true;
		}
		return false;
	}
	private List<Docente>listarDocente(List<Docente> lista){
		List<Docente> listaRetorno=new ArrayList<>();
		for (Docente ob: lista){
			Docente a = new Docente();
			a.setId(ob.getId());
			a.setTipoSangre(ob.getTipoSangre());
			a.setDatosClinico(ob.getDatosClinico());
			a.setPersona(ob.getPersona());
			listaRetorno.add(a);
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
