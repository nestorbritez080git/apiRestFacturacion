package com.bisontecfacturacion.security.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.service.FechaUtil;


@RestController
@RequestMapping("movEntradaSalida")
public class MovimientoE_SController {
    @Autowired
    private MovimientoE_SRepository entityRepository;
    
    @RequestMapping(method=RequestMethod.POST)
    public void guardar(@RequestBody MovimientoEntradaSalida entity) {
        entityRepository.save(entity);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    List<MovimientoEntradaSalida> getMovimientoIdProducto(@PathVariable int id){
        return entityRepository.getMovimientoPorIdProducto(id);

    }
    public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
    @RequestMapping(method=RequestMethod.GET, value="/rango/{fechaI}/{fechaF}/{tipo}/{id}")
	public List<MovimientoEntradaSalida> getRangoMovimiento(@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int tipo, @PathVariable int id) throws ParseException{
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI=formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		Date fechaFi = sumarDia(fecF, 24);
		return entityRepository.getMovimientoPorRango(fecI, fechaFi, tipo, id);
    }	
    
    
    @RequestMapping(method=RequestMethod.GET, value="/lista/{fechaI}/{fechaF}/{tipo}/{marca}/{isTodo}")
   	public List<MovimientoEntradaSalida> getMovEntradaSalidaLista(@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int tipo, @PathVariable String marca, @PathVariable boolean isTodo) throws ParseException{
    	List<MovimientoEntradaSalida> lista = new ArrayList<MovimientoEntradaSalida>();
   		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
   		Date fecI=formater.parse(fechaI);
   		Date fecF=formater.parse(fechaF);
   		Date fechaFi = sumarDia(fecF, 24);
   		System.out.println(tipo+ " tipoooooo");
   		if (isTodo == true) {
   		if (tipo == 1) {
   			lista = listado(entityRepository.getMovEntradaSalidaListaTodo(fecI, fechaFi));
   		}
   		if (tipo == 2) {
   			lista = listado(entityRepository.getMovEntradaSalidaListaEgreso(fecI, fechaFi));
   		}
   		if (tipo == 3) {
   			lista = listado(entityRepository.getMovEntradaSalidaListaIngreso(fecI, fechaFi));
   		}
   		} else {
   			if (tipo == 1) {
   	   			lista = listado(entityRepository.getMovEntradaSalidaListaTodoMarca(fecI, fechaFi, marca));
   	   		}
   	   		if (tipo == 2) {
   	   			lista = listado(entityRepository.getMovEntradaSalidaListaEgresoMarca(fecI, fechaFi, marca));
   	   		}
   	   		if (tipo == 3) {
   	   			lista = listado(entityRepository.getMovEntradaSalidaListaIngresoMarca(fecI, fechaFi, marca));
   	   		}
   		}
   		return lista;
       }	
    
  
    public List<MovimientoEntradaSalida> listado(List<Object[]> obj) {
    	List<MovimientoEntradaSalida> lista = new ArrayList<MovimientoEntradaSalida>();
   		for(Object[] o: obj) {
   			MovimientoEntradaSalida m = new MovimientoEntradaSalida();
   			if (o[0]==null) {m.setDescripcion("");} else {m.setDescripcion(o[0].toString());}
   			if (o[1]==null) {m.setCantidad(0.0);} else {m.setCantidad(Double.parseDouble(o[1].toString()));}
   			if (o[2]==null) {m.setEgreso(0.0);} else {m.setEgreso(Double.parseDouble(o[2].toString()));}
   			if (o[3]==null) {m.setIngreso(0.0);} else {m.setIngreso(Double.parseDouble(o[3].toString()));}
   			if (o[4]==null) {m.setReferencia("");} else {m.setReferencia(o[4].toString());};
   			if (o[5]==null) {m.setFecha(null);} else {m.setFecha((FechaUtil.convertirFechaStringADateUtil(o[5].toString())));}
   			if (o[6]==null) {m.setHora(null);} else {m.setHora(o[6].toString());}
   			if (o[7]==null) {m.setMarca("");} else {m.setMarca(o[7].toString());}
   			if (o[8]==null) {m.setCostoSalida(0.0);} else {m.setCostoSalida(Double.parseDouble(o[8].toString()));}
   			if (o[9]==null) {m.getFuncionario().getPersona().setNombre("SIN NOMBRE");}else {m.getFuncionario().getPersona().setNombre(o[9].toString());}
   			if (o[10]==null) {m.getTipoMovimiento().setDescripcion("SIN TIPO");}else {m.getTipoMovimiento().setDescripcion(o[10].toString());}
   			if (o[11]==null) {m.getTipoMovimiento().setDescripcion("SIN TIPO");}else {m.getTipoMovimiento().setId(Integer.parseInt(o[11].toString()));}
   			
   			lista.add(m);
   		}
   		return lista;
    }
    
    
    @RequestMapping(method=RequestMethod.GET, value="/listaDetallePorIdProducto/{id}")
   	public List<MovimientoEntradaSalida> getMovEntradaSalidaLista(@PathVariable int  id) throws ParseException{
    	return listado(entityRepository.getMovimientoProductoPorIdPRoducto(id));
    }
 
    	

    
}