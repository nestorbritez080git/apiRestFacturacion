package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnulacionesVentaRepository;

@Transactional()
@RestController
@RequestMapping("anulacionesVentas")
public class AnulacionesController {
	@Autowired
	private AnulacionesVentaRepository entityRepository;

	@RequestMapping(method=RequestMethod.GET)
	public List<AnulacionesVenta> get(){
		return listarAnulaciones(entityRepository.getListadoAnulaciones());
	}
	
	private List<AnulacionesVenta> listarAnulaciones (List<Object[]> list){
		List<AnulacionesVenta> listaRetorno= new ArrayList<AnulacionesVenta>();
		for(Object[] ob: list) {
			AnulacionesVenta v = new AnulacionesVenta();
			v.setId(Integer.parseInt(ob[0].toString()));
			v.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			v.setTotal(Double.parseDouble(ob[2].toString()));
			v.getFuncionario().getPersona().setNombre(ob[3].toString());
			v.setMotivo(ob[4].toString());
			v.getVenta().setId(Integer.parseInt(ob[5].toString()));
			
			listaRetorno.add(v);
		}
		return listaRetorno;
	}
}
