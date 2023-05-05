package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Caja;
import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.CajaChicaRepository;
import com.bisontecfacturacion.security.repository.CajaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

import groovyjarjarasm.asm.commons.Method;

@RestController
@RequestMapping("caja")
public class CajaController {
	@Autowired
	private CajaChicaRepository cajaChicaRepository;
	@Autowired
	private CajaRepository entityRepository;
	

	@Autowired
	private IUsuarioService usuarioService;
	
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Caja> getPAll(){
		return entityRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/false")
	public List<Caja> getFalse(){
		return entityRepository.listCajaFalse();
	}
	@RequestMapping(method=RequestMethod.GET, value="/true")
	public List<Caja> getTrue(){
		return entityRepository.listCajaTrue();
	}
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Caja getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Caja entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		if (siExiste(entity)) {
			return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN "+entity.getDescripcion()+" YA EXISTE"), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	public boolean siExiste(Caja entity){
		return entityRepository.findByDescripcion(entity.getDescripcion())!=null;
	}
	@RequestMapping(method=RequestMethod.PUT)
	public Caja editar(@RequestBody Caja entity){
		entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().toUpperCase()));
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Caja> consultarPorDescripcion(@PathVariable String descripcion){
		return entityRepository.findByTop100DescripcionLike(Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase()));
	}
	@RequestMapping(method=RequestMethod.POST, value="/guardar/cajaChica")
	public ResponseEntity<?> guardar(@RequestBody CajaChica entity){
		try {
			if (entity.getDescripcion().equals("")) {
				return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DE LA CHICA NO DEBE QUEDAR VACIO"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioE().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENCARGADO NO DEBE QUEDAR VACIO"), HttpStatus.CONFLICT);
			}else if (entity.getFuncionarioR().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO"), HttpStatus.CONFLICT);
			}
			cajaChicaRepository.save(entity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica")
	public List<CajaChica> getAllCajaChica(OAuth2Authentication authentication){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		List<CajaChica> list= new ArrayList<>();
		if(usuario.getAdministrador() == true){
			list = cajaChicaRepository.findTop100ByOrderByIdDesc();
		}
		else{
			list = cajaChicaRepository.getCajaChicaPorFuncionario(usuario.getFuncionario().getId());
		}
		return list;
	}
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica/buscarPorFuncionario/{id}")
	public CajaChica getCajaChicaPorID(@PathVariable int id){
		return cajaChicaRepository.getCajaChicaPorIddFuncionario(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica/buscarId/{id}")
	public CajaChica getCajaChicaPorIDCaja(@PathVariable int id){
		return cajaChicaRepository.getCajaChicaPorIdCaja(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica/activo/gasto")
	public List<CajaChica> getFuncionarioCajaChicaActivoGasto(){
		List<CajaChica> lisRetorno = new ArrayList<>();
		List<Object[]> lis= cajaChicaRepository.listarFuncionarioCajaChicaActivoGasto();
		for(Object[] ob: lis){
			CajaChica c = new CajaChica();
			c.getFuncionarioE().setId(Integer.parseInt(ob[0].toString()));
			c.getFuncionarioE().getPersona().setNombre(ob[1].toString());
			c.getFuncionarioE().getPersona().setApellido(ob[2].toString());
			c.setId(Integer.parseInt(ob[3].toString()));			
			lisRetorno.add(c);
		}
		System.out.println("lista:  "+lisRetorno.size());
		return lisRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica/activo")
	public List<Funcionario> getFuncionarioCajaChicaActivo(){
		List<Funcionario> lisRetorno = new ArrayList<>();
		List<Object[]> lis= cajaChicaRepository.listarFuncionarioCajaChicaActivo();
		for(Object[] ob: lis){
			Funcionario c = new Funcionario();
			c.setId(Integer.parseInt(ob[0].toString()));
			c.getPersona().setNombre(ob[1].toString());
			c.getPersona().setApellido(ob[2].toString());
			lisRetorno.add(c);
		}
		System.out.println("lista retttt:  "+lisRetorno.size());
		return lisRetorno;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cajaChica/inactivo")
	public List<Funcionario> getFuncionarioCajaChicaInactivo(){
		List<Funcionario> lisRetorno = new ArrayList<>();
		List<Object[]> lis= cajaChicaRepository.listarFuncionarioCajaChicaInactivo();
		for(Object[] ob: lis){
			Funcionario c = new Funcionario();
			c.setId(Integer.parseInt(ob[0].toString()));
			c.getPersona().setNombre(ob[1].toString());
			c.getPersona().setApellido(ob[2].toString());
			lisRetorno.add(c);
		}
		return lisRetorno;
	}
}
