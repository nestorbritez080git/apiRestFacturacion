package com.bisontecfacturacion.security.contabilidad.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaDTO;
import com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaPorConceptoDTO;
import com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaPorConceptoDTODetalle;
import com.bisontecfacturacion.security.contabilidad.repository.AsientoContableRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("cuentaContable")
public class CuentaContableController {
	@Autowired
    private AsientoContableRepository asientoContableRepository;
	
	private final ObjectMapper objectMapper = new ObjectMapper();

	
	@RequestMapping(method=RequestMethod.GET, value="/resumenCuentaContable/{inicio}/{fin}")
	public List<ResumenCuentaDTO> getResumenContable(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime inicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime fin){
		List<ResumenCuentaDTO>listRetorno = new ArrayList<ResumenCuentaDTO>();
		listRetorno= asientoContableRepository.obtenerResumenContable(inicio, fin);
		System.out.println("Resumen size: "+listRetorno.size());
		return listRetorno;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/resumenCuentaContablePorConcepto/{inicio}/{fin}")
	public ResumenCuentaPorConceptoDTO getResumenMovimientoPorTipoCuenta(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime inicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime fin) throws JsonParseException, JsonMappingException, IOException{
		List<ResumenCuentaPorConceptoDTODetalle> ingresos = new ArrayList<>();
		List<ResumenCuentaPorConceptoDTODetalle> costos = new ArrayList<>();
		List<Object[]> listaResultados = asientoContableRepository.obtenerResumenContablePorConcepto(inicio, fin);
		for (Object[] row : listaResultados) {
		    ResumenCuentaPorConceptoDTODetalle detalle = new ResumenCuentaPorConceptoDTODetalle();
		    detalle.setConcepto((String) row[0]);
		    detalle.setTipoCuenta((String) row[1]);
		    detalle.setItems(((BigInteger) row[2]).longValue());
		    detalle.setTotalDebe((BigDecimal) row[3]);
		    detalle.setTotalHaber((BigDecimal) row[4]);
		    detalle.setSaldo((BigDecimal) row[5]);
		    if (detalle.getTipoCuenta().equals("INGRESO") || detalle.getTipoCuenta().equals("ACTIVO") ) {
		        ingresos.add(detalle);
		    } else {
		        costos.add(detalle);
		    }
		}
		// Finalmente armas el DTO que agrupa las listas ingresos y costos
		ResumenCuentaPorConceptoDTO resumen = new ResumenCuentaPorConceptoDTO();
		resumen.setIngreso(ingresos);
		resumen.setCosto(costos);
		return resumen;
	}

}
