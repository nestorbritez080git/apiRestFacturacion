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
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.repository.PersonaRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("proveedor")
public class ProveedorController {
	@Autowired
	private ProveedorRepository entityRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	
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
		System.out.println("entr ocomo para guardar proveedor");
		if(entity.getPersona().getId() != null) {
	        // Persona ya existe → buscarla y adjuntarla
	        Persona pExistente = personaRepository.findById(entity.getPersona().getId())
	            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
	        entity.setPersona(pExistente);
	    } else {
	        // Persona nueva → se guarda automáticamente si usas cascade = CascadeType.ALL
	    }
		
		try {
			if(entity.getPersona().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("La persona no debe quedar vacio"), HttpStatus.CONFLICT);		
			}else if (siExistePersona(entity.getPersona())== true) {
				return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya posee credenciales como Cliente dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/compra")
	public ResponseEntity<?> guardarNuevoCompra(@RequestBody Proveedor entity){
		 try {
		        if (entity.getPersona() == null) {
		            return new ResponseEntity<>(
		                new CustomerErrorType("La persona no puede ser nula"),
		                HttpStatus.CONFLICT
		            );
		        }

		        // 🔒 VALIDACIONES
		        if (entity.getPersona().getCedula() == null || entity.getPersona().getCedula().isEmpty()) {
		            return new ResponseEntity<>(
		                new CustomerErrorType("El N° DE CEDULA Y/O RUC NO DEBE QUEDAR VACIO"),
		                HttpStatus.CONFLICT
		            );
		        }

		        if (entity.getPersona().getNombre() == null || entity.getPersona().getNombre().isEmpty()) {
		            return new ResponseEntity<>(
		                new CustomerErrorType("EL NOMBRE NO DEBE QUEDAR VACIO"),
		                HttpStatus.CONFLICT
		            );
		        }

		        // 🧹 NORMALIZACIÓN
		        Persona p = entity.getPersona();
		        p.setNombre(Utilidades.eliminaCaracterIzqDer(p.getNombre().trim().toUpperCase()));
		        if (p.getApellido() != null)
		            p.setApellido(Utilidades.eliminaCaracterIzqDer(p.getApellido().trim().toUpperCase()));
		        if (p.getDireccion() != null)
		            p.setDireccion(Utilidades.eliminaCaracterIzqDer(p.getDireccion().trim().toUpperCase()));
		        if (p.getEmail() != null)
		            p.setEmail(p.getEmail().trim().toUpperCase());
		        if (p.getTipo() != null)
		            p.setTipo(p.getTipo().trim().toUpperCase());

		        // 🚨 CLAVE: fuerza entidad nueva
		        p.setId(null);
		        entity.setId(null);

		        // 🔍 VALIDAR DUPLICADO
		        if (siExiste(p)) {
		            return new ResponseEntity<>(
		                new CustomerErrorType("El N° DE CEDULA " + p.getCedula() + " YA EXISTE."),
		                HttpStatus.CONFLICT
		            );
		        }

		        // 💾 UN SOLO SAVE
		        entityRepository.save(entity);

		        return new ResponseEntity<>(HttpStatus.CREATED);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>(
		            new CustomerErrorType(e.getMessage()),
		            HttpStatus.INTERNAL_SERVER_ERROR
		        );
		    }
	}
	
	public boolean siExiste(Persona entity){
		return personaRepository.findByCedula(entity.getCedula())!=null;
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
			e.printStackTrace();
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
