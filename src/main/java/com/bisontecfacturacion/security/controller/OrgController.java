package com.bisontecfacturacion.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.repository.OrgRepository;

@Transactional
@RestController
@RequestMapping("org")
public class OrgController {

	@Autowired
	private OrgRepository entityRepository;

	@RequestMapping(method=RequestMethod.GET)
	public Org getAll(){
		Org orga=entityRepository.findById(1).get();
		Org o=new Org();
		o.setId(orga.getId());
		o.setNombre(orga.getNombre());
		o.setRuc(orga.getRuc());
		o.setTelefono(orga.getTelefono());
		o.setDireccion(orga.getDireccion());
		o.setCiudad(orga.getCiudad());
		o.setPais(orga.getPais());
		return o;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public Org update(@RequestBody Org entity){
		return entityRepository.save(entity);
	}

}
