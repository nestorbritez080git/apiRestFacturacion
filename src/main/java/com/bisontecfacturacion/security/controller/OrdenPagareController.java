package com.bisontecfacturacion.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OrdenPagare;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.OrdenPagareRepository;

@EnableAsync
@Transactional
@RestController
@RequestMapping("ordenPagare")
public class OrdenPagareController {
	@Autowired
	private OrdenPagareRepository entityRepository;
	
	
	@RequestMapping(method=RequestMethod.GET, value ="/{id}")
	public OrdenPagare getPorId(@PathVariable int id){
		OrdenPagare ope=entityRepository.getOrdenPagarePorId(id);
		OrdenPagare opeRest=null;
		if(ope!=null) {
			 	opeRest=new OrdenPagare();
			 	opeRest.setId(ope.getId());
				opeRest.setFuncionario(ope.getFuncionario());
				opeRest.setCliente(ope.getCliente());
				opeRest.getCuentaCobrarCabecera().setId(ope.getCuentaCobrarCabecera().getId());
				opeRest.setFecha(ope.getFecha());
				opeRest.setFechaVencimiento(ope.getFechaVencimiento());
				opeRest.setTotal(ope.getTotal());
				opeRest.setTotalLetra(ope.getTotalLetra());
		}else {
			opeRest = null;
		}
		
		
		return opeRest;
	}
	@RequestMapping(method=RequestMethod.GET, value ="/porIdCuenteCobrarCabecera/{id}")
	public OrdenPagare getPorIdCabeceraCuenta(@PathVariable int id){
		OrdenPagare ope=entityRepository.getOrdenPorCuentaId(id);
		OrdenPagare opeRest=null;
		if(ope!=null) {
			 	opeRest=new OrdenPagare();
			 	opeRest.setId(ope.getId());
				opeRest.setFuncionario(ope.getFuncionario());
				opeRest.setCliente(ope.getCliente());
				opeRest.getCuentaCobrarCabecera().setId(ope.getCuentaCobrarCabecera().getId());
				opeRest.setFecha(ope.getFecha());
				opeRest.setFechaVencimiento(ope.getFechaVencimiento());
				opeRest.setTotal(ope.getTotal());
				opeRest.setTotalLetra(ope.getTotalLetra());
		}else {
			opeRest = null;
		}
		
		
		return opeRest;
	}
}
