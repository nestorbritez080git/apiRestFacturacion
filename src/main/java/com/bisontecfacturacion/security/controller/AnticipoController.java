package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.Periodo;
import com.bisontecfacturacion.security.model.TransferenciaAnticipo;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("anticipo")
public class AnticipoController {
	@Autowired
	private AnticipoRepository entityRepository;
	@Autowired
	private CajaMayorRepository cajaMayorRepository;
	
	@Autowired
	private TransferenciaAnticipoRepository transferenciaAnticipoRepository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Anticipo> getAnticipo(){
		return cargarListado(entityRepository.consultarTodo());
	}
	@RequestMapping(method = RequestMethod.GET, value = "/consultarId/{id}")
	public Anticipo getId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable int id){
		
		entityRepository.deleteById(id);
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Anticipo entity){
		try {
			//entity.getFuncionarioAutorizado().setId(0);
			if(entity.getFuncionarioRegistro().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioAutorizado().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO AUTORIZADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioEncargado().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENCARGADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getPeriodo().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PERIODO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getMonto()<= 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO DEBE SER MAYOR A CERO PARA PODER GUARDAR ANTICIPO.!"), HttpStatus.CONFLICT);
			}
			if(entity.isEstado()==true) {
				System.out.println("Confirmado");
				/*
				TransferenciaAnticipo t = new TransferenciaAnticipo();
				t.getFuncionario().setId(entity.getFuncionarioRegistro().getId());
				t.getAnticipo().setId(entity.getId());
				t.getCajaMayor().setId(1);
				if (entity.getTipoOperacion().getId()==1) {
					t.setMonto(entity.getMonto());
				}	
				if (entity.getTipoOperacion().getId()==2) {
					t.setMontoCheque(entity.getMonto());
				}	
				if (entity.getTipoOperacion().getId()==3) {
					t.setMontoTarjeta(entity.getMonto());
				}
				cajaMayorRepository.findByActualizaCajaMayorNegativo(1, t.getMonto(), t.getMontoCheque(), t.getMontoTarjeta());
				transferenciaAnticipoRepository.save(t);
				*/
			}
			if(entity.isEstado()==false) {
				System.out.println("activo");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<Anticipo> getPorFiltro(@RequestBody String descripcion){
		return cargarListado(entityRepository.consultarTodoPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));
	}
	private List<Anticipo>cargarListado(List<Object[]> lista){
		List<Anticipo> listaRetrono= new  ArrayList<Anticipo>();
		for (Object[] ob: lista) {
			Anticipo a = new Anticipo();
			a.setId(Integer.parseInt(ob[0].toString()));
			a.getFuncionarioRegistro().getPersona().setNombre(ob[1].toString() + " "+ob[2].toString());
			a.getFuncionarioAutorizado().getPersona().setNombre(ob[3].toString() + " "+ob[4].toString());
			a.getFuncionarioEncargado().getPersona().setNombre(ob[5].toString() + " "+ob[6].toString());
			a.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[7].toString()));
			a.setMonto(Double.parseDouble(ob[8].toString()));
			a.setEstado(Boolean.parseBoolean(ob[9].toString()));
			a.getFuncionarioRegistro().setId(Integer.parseInt(ob[10].toString()));
			a.getFuncionarioAutorizado().setId(Integer.parseInt(ob[11].toString()));
			a.getFuncionarioEncargado().setId(Integer.parseInt(ob[12].toString()));
			a.getPeriodo().setId(Integer.parseInt(ob[13].toString()));
			a.getTipoOperacion().setId(Integer.parseInt(ob[14].toString()));
			listaRetrono.add(a);
		}
		return listaRetrono;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/anticipo")
	public void confirmar(@RequestBody Anticipo entity){
		System.out.println("en5troooo confirmarafasdfasf");
		entityRepository.save(entity);
		
		
	}
}
