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

import com.bisontecfacturacion.security.model.Permiso;
import com.bisontecfacturacion.security.model.PermisoUser;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.PermisoRepository;
import com.bisontecfacturacion.security.repository.PermisoUserRepository;
import com.bisontecfacturacion.security.repository.UserRepository;

@RestController
@RequestMapping("permisoUser")
public class PermisoUserController {
	@Autowired
	private PermisoUserRepository entityRepository;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PermisoRepository permisoRepository;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<PermisoUser> getAll(){
		return entityRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/permiso")
	public List<Permiso> getAllPermiso(){
		return permisoRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{id}")
	public ResponseEntity<?> getAllPermiso(@PathVariable long id ){
		System.out.println("entro aqui**********************************");
		List<PermisoUser> permisoEstado= new ArrayList<>();
		try {
			permisoEstado=listadoReporte(entityRepository.getListPermisoUserSql(id));
			if (permisoEstado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(permisoEstado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void guardar(@RequestBody List<PermisoUser> entity){
		
		for(PermisoUser user: entity) {
			entityRepository.actualizarPermiso(user.getId(), user.isEstado());

		}
		
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public PermisoUser update(@RequestBody List<PermisoUser> entity){
		Usuario users=new Usuario();
		users=userRepository.findTop1ByOrderByIdDesc();
		for(PermisoUser user: entity) {
			user.getUser().setId(users.getId());
			return  entityRepository.save(user);
			
		}
		
		return null;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/todo/{id}")
	public ResponseEntity<?> getAllPermisoAll(@PathVariable long id ){
		List<PermisoUser> permisoEstado= new ArrayList<>();
		try {
			permisoEstado=listadoReporte(entityRepository.getListPermisoUserAllSql(id));
			if (permisoEstado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(permisoEstado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	private List<PermisoUser> listadoReporte(List<Object []> listPermisoUserAll) {
		List<PermisoUser> permisoRetorno= new ArrayList<>();
		for(Object [] per: listPermisoUserAll) {
			PermisoUser permisos=new PermisoUser();
			permisos.setId(Integer.parseInt(per [0].toString()));
			permisos.getPermiso().setDescripcion(per [1].toString());
			permisos.setEstado(Boolean.parseBoolean(per [2].toString()));
			permisos.getUser().setId(Long.parseLong(per [3].toString()));
			permisos.getPermiso().getUbicacionPermiso().setDescripcion(per [4].toString());
			permisos.getPermiso().setText(per [5].toString());
			permisos.getPermiso().setId(Integer.parseInt(per [6].toString()));
			permisos.getPermiso().setIcono(per [7].toString());
			permisos.getPermiso().getUbicacionPermiso().setIcono(per [8].toString());
			permisos.getPermiso().getUbicacionPermiso().setPosicion(Integer.parseInt(per [9].toString()));
			permisoRetorno.add(permisos);
		}
		return permisoRetorno;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/guardar")
	public ResponseEntity<?> guardarPermisoUser(@RequestBody List<PermisoUser> entity){
		System.out.println("Entroo permis guardar.!");
		try {
				for(PermisoUser user: entity) {
					if(user.getUser().getId() <= 0 ) {
						return new ResponseEntity<>(new com.bisontecfacturacion.security.config.CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					} else if(user.getPermiso().getId() <= 0 ) {
						return new ResponseEntity<>(new com.bisontecfacturacion.security.service.CustomerErrorType("EL PERMISO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else {
			
						entityRepository.save(user);
					}
					
				}
			} catch (Exception e) {
				return new ResponseEntity<>(new com.bisontecfacturacion.security.service.CustomerErrorType(""+e.getCause().getMessage()), HttpStatus.CONFLICT);
			}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
}
