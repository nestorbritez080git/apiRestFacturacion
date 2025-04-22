

package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.InteresCuota;
import com.bisontecfacturacion.security.model.LimitesRegistroLista;
import com.bisontecfacturacion.security.repository.LimitesRegistroListaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("limitesRegistroLista")
public class LimitesRegistroListaController {
	
	@Autowired
	private LimitesRegistroListaRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET, value="/ventaLista/{id}")
	public  LimitesRegistroLista getLimiteRegistroListaVenta(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	@RequestMapping(method=RequestMethod.GET, value="/productoLista/{id}")
	public  LimitesRegistroLista getLimiteRegistroListaProducto(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public  List<LimitesRegistroLista> getLimiteRegistroListaaLL(){
		return entityRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> actualizar(@RequestBody LimitesRegistroLista entity){
		if (entity.getDescripcion().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("SE DEBE AGREGAR UNA DESCRIPCION A MI LIMITES REGISTRO.!"), HttpStatus.CONFLICT);
		}else if(entity.getLimite()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("LÍMITES PARA EL REGISTRO DE LISTA DEBE SER MAYOR A CERO.!"), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
}
