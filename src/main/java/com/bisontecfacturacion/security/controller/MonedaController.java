package com.bisontecfacturacion.security.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bisontecfacturacion.security.config.CotizacionesDTO;
import com.bisontecfacturacion.security.model.Moneda;
import com.bisontecfacturacion.security.repository.MonedaRepsitory;

@Transactional()
@RestController
@RequestMapping("moneda")
public class MonedaController {
    private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private MonedaRepsitory entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Moneda> getAll(){
		return entityRepository.findByOrderByIdAsc();
	}
	@RequestMapping(method=RequestMethod.GET, value = "/ordenDesc")
	public List<Moneda> getAllOrdenByDesc(){
		return entityRepository.findByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody List<Moneda> LisyEntity){
		try {
			for (int i = 0; i < LisyEntity.size(); i++) {
				Moneda m = LisyEntity.get(i);
				entityRepository.save(m);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("No se ha podido completar las actualizaciones de la contización del día", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	public List<CotizacionesDTO> obtenerCotizacionDelDia() {
        String url = "https://www.bcp.gov.py/webapps/web/cotizacion/monedas";
        ResponseEntity<CotizacionesDTO[]> response =
                restTemplate.getForEntity(url, CotizacionesDTO[].class);
        CotizacionesDTO[] cotizaciones = response.getBody();
        if (cotizaciones != null) {
        	 for (CotizacionesDTO dto : cotizaciones) {
                 LocalDate fecha = LocalDate.parse(dto.getFecha(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                 System.out.println("det: compra venta: "+dto.getCompra()+ ", venta:"+dto.getVenta());
        	 }
        }
       
        throw new RuntimeException("No se pudo obtener la cotización del día");
    }
}
