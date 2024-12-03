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
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.repository.PersonaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("persona")
public class PersonaController {
	@Autowired
	private PersonaRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Persona> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Persona getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Persona entity){
		   if(entity.getNombre()!=null){
				entity.setNombre(Utilidades.eliminaCaracterIzqDer(entity.getNombre().trim().toUpperCase()));			
	        }
	        if(entity.getApellido()!=null){
				entity.setApellido(Utilidades.eliminaCaracterIzqDer(entity.getApellido().trim().toUpperCase()));			
	        }
	        if(entity.getCedula()!=null){
				entity.setCedula(Utilidades.eliminaCaracterIzqDer(entity.getCedula().trim().toUpperCase()));			
	        }
	        if(entity.getDireccion()!=null){
				entity.setDireccion(Utilidades.eliminaCaracterIzqDer(entity.getDireccion().trim().toUpperCase()));			
	        }
	        if(entity.getEmail()!=null){
				entity.setEmail(entity.getEmail().trim().toUpperCase());			
	        }
	        if(entity.getTipo()!=null){
				entity.setTipo(entity.getTipo().trim().toUpperCase());			
	        }

	        if(entity.getId() != 0){
	                Persona p = new Persona();
	                p = siExisteEditar(entity);
	                if (p!=null) {
	                    if(entity.getId()==p.getId()){
	                            entityRepository.save(entity);
	                
	                       // entityRepository.save(entity);
	                    }else {
	                        return new ResponseEntity<>(new CustomerErrorType("EL N° DE CEDULA : "+entity.getCedula()+", PERTENECE A UNA PERSONA YA REGISTRADO ANTERIORMENTE"), HttpStatus.CONFLICT);
	                    }
	                }else {
	                    entityRepository.save(entity);
	                }
	            

	        } else {
	            if (siExiste(entity)) {
	                return new ResponseEntity<>(new CustomerErrorType("El N° DE CEDULA " + entity.getCedula() + " YA EXISTE."),
	                        HttpStatus.CONFLICT);
	            }else {
	                entityRepository.save(entity);
	            }
	        }
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	public boolean siExiste(Persona entity){
		return entityRepository.findByCedula(entity.getCedula())!=null;
	}
	
	public Persona siExisteEditar(Persona entity){
		return entityRepository.findByCedula(entity.getCedula());
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

		@RequestMapping(method=RequestMethod.GET, value="/buscarAll/{descripcion}")
	public List<Persona> getPersonaAll(@PathVariable String descripcion){
		List<Persona> lis= new ArrayList<>();
		if(descripcion.equals("9999999999")){
			lis=entityRepository.findTop100ByOrderByIdDesc();
		   }else{
			lis=entityRepository.findByTop100DescripcionLike("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			for (int i = 0; i < lis.size(); i++) {
				System.out.println(lis.get(i).getNombre()+ " nombreaaa");
			}
		   }
		
		return lis;

	}
	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}/{param}")
	public List<Persona> consultarPorDescripcion(@PathVariable String descripcion, @PathVariable int param){
		List<Persona> lis= new ArrayList<>();
		if(param==1){
			if(descripcion.equals("9999999999")){
			 lis=entityRepository.getListadoPersonaFuncionarioAll();
			}else{
				lis=entityRepository.getListadoPersonaFuncionario("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			}
		}
		if(param==2){
			if(descripcion.equals("9999999999")){
				lis=entityRepository.getListadoPersonaClienteAll();
			}else{
				lis=entityRepository.getListadoPersonaCliente("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			}
		}
		if(param==3){
			if(descripcion.equals("9999999999")){
				lis=entityRepository.getListadoPersonaProveedorAll();
			}else{
				lis=entityRepository.getListadoPersonaProveedor("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			}
			
		}
		if(param==4){
			if(descripcion.equals("9999999999")){
				lis=entityRepository.getListadoPersonaAlumnoAll();
			}else{
				lis=entityRepository.getListadoPersonaAlumno("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			}
			
		}
		if(param==5){
			if(descripcion.equals("9999999999")){
				lis=entityRepository.getListadoPersonaDocenteAll();
			}else{
				lis=entityRepository.getListadoPersonaDocente("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			}
			
		}


		return lis;

	}
	@RequestMapping(method=RequestMethod.GET, value="/ejecutarLimpieza")
	public void ejecutarLimpieza() {
		List<Persona> lis =  entityRepository.findAll();
		for(Persona en:lis) {
			en.setNombre(Utilidades.eliminaCaracterIzqDer(en.getNombre()));
			en.setNombre(Utilidades.eliminaCaracterIzqDer(en.getApellido()));
			en.setCedula(Utilidades.eliminaCaracterIzqDer(en.getCedula()));
			en.setCedula(Utilidades.eliminaCaracterIzqDer(en.getCedula()));
			entityRepository.save(en);	
		}
	}
	
		
}
