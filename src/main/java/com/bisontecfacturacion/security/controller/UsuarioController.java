package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.User;
import com.bisontecfacturacion.security.model.ControlUsuario;
import com.bisontecfacturacion.security.model.PermisoUser;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.ControlUsuarioRepository;
import com.bisontecfacturacion.security.repository.PermisoUserRepository;
import com.bisontecfacturacion.security.repository.UserRepository;

@Transactional
@RestController
@RequestMapping("usuario")
public class UsuarioController {
	@Autowired
	private ControlUsuarioRepository controlUsuarioRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PermisoUserRepository permisoUserRepository;
	
	@RequestMapping(method=RequestMethod.GET, value="/controlUsuario")
	public List<ControlUsuario> getControlUsuario() {
		List<ControlUsuario> u=controlUsuarioRepository.findTop50ByOrderByIdDesc();
		List<ControlUsuario> lista=new ArrayList<ControlUsuario>();
		for(ControlUsuario c: u) {
			ControlUsuario control=new ControlUsuario();
			control.setHora(c.getHora());
			control.setFecha(c.getFecha());
			control.getUsuario().getFuncionario().getPersona().setNombre(c.getUsuario().getFuncionario().getPersona().getNombre());
			control.getUsuario().getFuncionario().getPersona().setApellido(c.getUsuario().getFuncionario().getPersona().getApellido());
			lista.add(control);
		}
		return lista;
	}
	

	@RequestMapping(method=RequestMethod.GET, value="/userId")
	public ResponseEntity<Collection<Usuario>> getIdUser() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Usuario> getAll(){
		return userRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public Usuario getUserId(@PathVariable int id){
		return userRepository.findById(Long.parseLong(id + "")).get();
	}
	@RequestMapping(method=RequestMethod.POST, value = "/crear")
	public void pruebaFlutter(@RequestBody User user){
		System.out.println("Enbtrooooo");
		/*
		System.out.println("asfd asdfasdf  asdfas asf asdf as desde flutter");
		BCryptPasswordEncoder password=new BCryptPasswordEncoder();
		user.setPassword(password.encode(user.getPassword()));		
		userRepository.save(user);
		
		Usuario u=userRepository.findTop1ByOrderByIdDesc();
		List<PermisoUser> permisoUser = permisoUserRepository.getListPermisoUser(1);
		for(PermisoUser p: permisoUser) {
			PermisoUser permisoUsers=new PermisoUser();
			permisoUsers.setEstado(false);
			permisoUsers.getPermiso().setId(p.getPermiso().getId());
			permisoUsers.getUser().setId(u.getId());
			permisoUserRepository.save(permisoUsers);
			
		}
		
	*
	*/
		System.out.println("Ejecut prueba flutter desde *1*0*0*0*0");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void guardar(@RequestBody Usuario user){
		System.out.println("asfd asdfasdf  asdfas asf asdf as desde flutter");
		BCryptPasswordEncoder password=new BCryptPasswordEncoder();
		user.setPassword(password.encode(user.getPassword()));		
		userRepository.save(user);
		
		Usuario u=userRepository.findTop1ByOrderByIdDesc();
		List<PermisoUser> permisoUser = permisoUserRepository.getListPermisoUser(1);
		for(PermisoUser p: permisoUser) {
			PermisoUser permisoUsers=new PermisoUser();
			permisoUsers.setEstado(false);
			permisoUsers.getPermiso().setId(p.getPermiso().getId());
			permisoUsers.getUser().setId(u.getId());
			permisoUserRepository.save(permisoUsers);
			
		}
		
		
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public void editar(@RequestBody Usuario user){	
		userRepository.findByActualizarUsuario(user.getEnabled(), user.getUsername(), user.getFuncionario().getId(), user.getAdministrador(), user.getId());
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/actualizarPassword")
	public void actualizarPassword(@RequestBody Usuario user){	
		BCryptPasswordEncoder password=new BCryptPasswordEncoder();
		user.setPassword(password.encode(user.getPassword()));
		userRepository.findByActualizarPassword(user.getPassword(), user.getId());
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable Long id){
		userRepository.deleteById(id);
	}

		
}
