package com.bisontecfacturacion.security.controller;


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
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("funcionario")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Funcionario> getAll(){
		return entityRepository.getFuncionarios();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/false")
	public List<Funcionario> getAllFase(){
		return entityRepository.getFuncionarioFalse();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/enservicio")
	public List<Funcionario> getAllFuncionarioServicio(){
		return entityRepository.getFuncionarioServicio();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="totalfuncionario")
	public Object[] getAllTotales(){
		return entityRepository.findByFuncionario();
	}
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Funcionario getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Funcionario entity){
		try {
			// if (siExistePersona(entity.getPersona())== true) {
			//	return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			// }
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	public boolean siExistePersona(Persona entity){
		if(entityRepository.getIdPersona(entity.getId())!=null){
			return true;
		}
		return false;
	}
	public boolean siExistePersonaEditar(Funcionario entity){
		if(entityRepository.getIdPersonaEditar(entity.getPersona().getId(), entity.getId())!=null){
			return true;
		}
		return false;
	}
	
	/*
	public boolean siExisteUsuario(Usuario entity){
		if(entityRepository.getIdFuncionario(Integer.parseInt(entity.getId()+""))!=null){
			return true;
		}
		return false;
	}
	*/
	public boolean siExisteUsuarioEditar(Funcionario entity){
		if(entityRepository.getIdFuncionarioEditar(entity.getPersona().getId(), entity.getId())!=null){
			return true;
		}
		return false;
	}
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<?> editar(@RequestBody Funcionario entity){
		try {
			if (siExistePersonaEditar(entity)== true) {
				return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya cuenta con credenciales relacionado con otro funcionario dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			} else if (siExisteUsuarioEditar(entity)==false) {
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}else {
//				entityRepository.save(entity);
//				return  new  ResponseEntity<String>(HttpStatus.CREATED);
				return new ResponseEntity<>(new CustomerErrorType("Este Usuario ya cuenta con credenciales relacionado con otro funcionario dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public  ResponseEntity<?> getIdUsuraioFuncionario(@PathVariable int id){
		try {
			int i=0;
			 i=entityRepository.getId(id);
			
			if (i==0) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(i, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("El usuario debe registrar como funcionario para poder hacer la venta!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/buscarIdTodo/{id}")
	public Funcionario getIdUsuraioFuncionarioTodo(@PathVariable int id){
		Funcionario f=entityRepository.getIdFuncionario(id);
		return funcionario(f);
	}

	
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<Funcionario> consultarPorDescripcion(@RequestBody String descripcion){
		List<Object[]> objeto=entityRepository.findByFuncionarioNombrePost("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		List<Funcionario> funcionario=new ArrayList<>();
		for(Object[] ob:objeto){
			Funcionario fun=new Funcionario();
			fun.setId(Integer.parseInt(ob[0].toString()));
			
			fun.getPersona().setNombre(ob[2].toString());
			fun.getPersona().setApellido(ob[3].toString());
			fun.getPersona().setCedula(ob[4].toString()); 
			fun.getPersona().setTelefono(ob[5].toString());
			fun.setSueldoBruto(Double.parseDouble(ob[6].toString()));
			funcionario.add(fun);
		}
		return funcionario;
	}

	public Funcionario funcionario(Funcionario f) {
		Funcionario funcionario=new Funcionario();
		funcionario.setId(f.getId());
		funcionario.getPersona().setNombre(f.getPersona().getNombre());
		funcionario.getPersona().setApellido(f.getPersona().getApellido());
		funcionario.getPersona().setCedula(f.getPersona().getCedula());
		funcionario.getPersona().setTelefono(f.getPersona().getTelefono());
		return funcionario;
	}
}
