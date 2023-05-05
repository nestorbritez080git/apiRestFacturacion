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

import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("proveedor")
public class ProveedorController {
	@Autowired
	private ProveedorRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Proveedor> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.GET, value="/primerId")
	public int getPrimerId(){
		Proveedor p=new Proveedor();
		p=entityRepository.findTop1ByOrderByIdAsc();
		int id=p.getId();
		return id;
	}
	@RequestMapping(method=RequestMethod.GET, value="totalproveedor")
	public Object[] getAllTotales(){
		return entityRepository.findByProveedor();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Proveedor getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Proveedor entity){
		try {
			if (siExistePersona(entity.getPersona())== true) {
				return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya posee credenciales como Proveedor dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}
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
	public boolean siExistePersonaEditar(Proveedor entity){
		if(entityRepository.getIdPersonaEditar(entity.getPersona().getId(), entity.getId())!=null){
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<?> editar(@RequestBody Proveedor entity){
		try {
			if (siExistePersonaEditar(entity)== true) {
				return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya posee credenciales relacionado con otro Proveedor dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
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


	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Proveedor> consultarPorDescripcion(@PathVariable String descripcion){
		List<Object[]> objeto=entityRepository.getBuscarPorDescripcion("%"+descripcion.toUpperCase()+"%");
		List<Proveedor> cliente=new ArrayList<>();
		for(Object[] ob:objeto){
			Proveedor pro=new Proveedor();
			pro.setId(Integer.parseInt(ob[0].toString()));
			pro.getPersona().setNombre(ob[1].toString());
			pro.getPersona().setApellido(ob[2].toString());
			pro.getPersona().setCedula(ob[3].toString());
			cliente.add(pro);
		}
		return cliente;
	}

}
