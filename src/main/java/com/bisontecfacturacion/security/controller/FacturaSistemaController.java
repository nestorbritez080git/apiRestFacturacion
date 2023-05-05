

package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Serial;
import com.bisontecfacturacion.security.repository.FacturaSistemaRepository;
import com.bisontecfacturacion.security.repository.SerialRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("facturaSistema")
public class FacturaSistemaController {
	SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
	@Autowired
	private FacturaSistemaRepository entityRepository;
	@Autowired
	private SerialRepository serialRepository;
	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody FacturaSistema entity){
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		Serial serie = serialRepository.getSerieFechaActual(anho, mes+1);
		entity.setFechaPago(null);
		entity.setMonto(250000);
		entity.setHoraPago("08:08");
		entity.setSerial(serie);
		if(entity.getSerial()== null) {
			return new ResponseEntity<>(new CustomerErrorType("NO SE HA PODIDO CARGAR SERIE PARA ESTA FACTURA!!"), HttpStatus.CONFLICT);
		} else {
			FacturaSistema f=entityRepository.getFacturaSistemaFechaActual(anho, mes);
			if(f == null) {
				String fecha1 = formater.format(new Date());
				String[] fec1=fecha.split("-");
				Integer mes1=Integer.parseInt(fec[1]);
				Integer anho1=Integer.parseInt(fec[2]);
				String fechaFactura = mes1 + "-" + anho1;
				String fechaActuales = mes + "-" + anho;
				entityRepository.save(entity);
				
			} else {
				String fecha1 = formater.format(f.getFechaFactura());
				String[] fec1=fecha.split("-");
				Integer mes1=Integer.parseInt(fec[1]);
				Integer anho1=Integer.parseInt(fec[2]);
				String fechaFactura = mes1 + "-" + anho1;
				String fechaActuales = mes + "-" + anho;
				if (!fechaFactura.equals(fechaActuales)){
					entityRepository.save(entity);
				}
				
			}
		
			
		}
		
		
		return  new ResponseEntity<String>(HttpStatus.CREATED);
	}

}
