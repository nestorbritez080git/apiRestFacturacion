package com.bisontecfacturacion.security.hoteleria.controller;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.hoteleria.model.SetingRecepciones;
import com.bisontecfacturacion.security.hoteleria.repository.SetingRecepcionesRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("setingRecepciones")
public class SetingRecepcionesController {
	@Autowired
	private SetingRecepcionesRepository entityRepository;
	@RequestMapping(method = RequestMethod.GET, value = "/obtenerSetingRecepciones")
	public SetingRecepciones getSetingRecepciones(){
		 return entityRepository.findFirstByOrderByIdAsc();
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody SetingRecepciones entity){
		try {
			 // Si viene nulo, inicializamos en false
	        if (entity.getEstadoSumaManualEstadia() == null) {
	            entity.setEstadoSumaManualEstadia(false);
	        }
	        // Si la hora es nula y suma manual está activa, ponemos hora por defecto
	        if (entity.getHoraFinalizacionDiaria() == null) {
	            entity.setHoraFinalizacionDiaria(LocalTime.of(0, 0, 0)); // 00:00:00
	        }
	        // Guardar la configuración
	        entityRepository.save(entity);
	        return new ResponseEntity<>(entity, HttpStatus.CREATED);
			
		}catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		
	}
}
