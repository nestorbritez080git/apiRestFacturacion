package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.repository.ServicioRepository;

@RestController
@RequestMapping("servicio")
public class ServicioController {
	@Autowired
	private ServicioRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Servicio> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public Servicio guardar(@RequestBody Servicio entity){
		entity.setDescripcion(entity.getDescripcion().toUpperCase());
		entity.setAplicacion(entity.getAplicacion().toUpperCase());
		
		return  entityRepository.save(entity);
	}

	@RequestMapping(method=RequestMethod.PUT)
	public Servicio editar(@RequestBody Servicio entity){
		if(!entity.getDescripcion().equals("")) {entity.setDescripcion(entity.getDescripcion().toUpperCase());}
		if(!entity.getAplicacion().equals("")){entity.setAplicacion(entity.getAplicacion().toUpperCase());}
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Servicio> consultarPorDescripcion(@PathVariable String descripcion){
		List<Servicio> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	}
	public List<Servicio> listSer(List<Servicio> objeto) {
		List<Servicio> servi=new ArrayList<>();
		for(Servicio ob:objeto){
			Servicio s=new Servicio();
			s.setId(ob.getId());
			s.setDescripcion(ob.getDescripcion());
			s.setPrecio(ob.getPrecio());
			s.setMinimo(ob.getMinimo());
			s.setAplicacion(ob.getAplicacion());
			s.setIva(ob.getIva());
			s.setPorcentaje(ob.getPorcentaje());
			servi.add(s);
		}

		return servi;
	}

}
