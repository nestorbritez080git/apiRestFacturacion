package com.bisontecfacturacion.security.controller;

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

import com.bisontecfacturacion.security.model.EntregaProduccion;
import com.bisontecfacturacion.security.repository.EntregaProduccionRepository;
import com.bisontecfacturacion.security.repository.OrdenProduccionRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("entregaProduccion")
public class EntregaProduccionController {
	@Autowired
	private EntregaProduccionRepository entityRepository;
	
	@Autowired
	private  OrdenProduccionRepository ordenRepository;
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody EntregaProduccion entity){
		 
		if(entity.getFuncionario().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getFuncionarioEntrega().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENTREGA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getCantidadProduccion()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DE ENTREGA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getCostoTotalMateriaPrima()<= 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL COSTO TOTAL MATERIA PRIMA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getOrdenProduccion().getId()== 0) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES CARGAR UNA ORDEN DE PRODUCCIÓN PARA HACER UNA ENTREGA!"), HttpStatus.CONFLICT);
		} else if(entity.getFecha() == null) {
			return new ResponseEntity<>(new CustomerErrorType("LA FECHA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getHora().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("LA HORA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}
		
		entityRepository.save(entity);
		ordenRepository.actualizarEstadoEntrega(entity.getOrdenProduccion().getId(), true, entity.getCantidadProduccion());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	public List<EntregaProduccion> listado(List<EntregaProduccion> lista) {
		List<EntregaProduccion> listadoRetorno = new ArrayList<EntregaProduccion>();
		for(EntregaProduccion l: lista) {
			EntregaProduccion o =new EntregaProduccion();
			o.setId(l.getId());
			o.getFuncionario().setId(l.getFuncionario().getId());
			o.getFuncionario().getPersona().setNombre(l.getFuncionario().getPersona().getNombre());
			o.getFuncionario().getPersona().setApellido(l.getFuncionario().getPersona().getApellido());
			o.getFuncionarioEntrega().setId(l.getFuncionarioEntrega().getId());
			o.getFuncionarioEntrega().getPersona().setNombre(l.getFuncionarioEntrega().getPersona().getNombre());
			o.getFuncionarioEntrega().getPersona().setApellido(l.getFuncionarioEntrega().getPersona().getApellido());
			o.getOrdenProduccion().getProduccionCostoCabecera().setProduccionDescripcion(l.getOrdenProduccion().getProduccionCostoCabecera().getProduccionDescripcion());
			o.setCantidadProduccion(l.getCantidadProduccion());;
			o.getOrdenProduccion().setCantidad(l.getOrdenProduccion().getCantidad());
			o.setFecha(l.getFecha());
			o.setEstado(l.getEstado());
			o.setHora(l.getHora());
			listadoRetorno.add(o);
		}
		
		return listadoRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<EntregaProduccion> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);

		return listado(entityRepository.getEntregaProduccion(ano, mes, dia));
	}
	@RequestMapping(method=RequestMethod.GET)
	public List<EntregaProduccion> getAll(){
		return listado(entityRepository.findAll());
	}

}
