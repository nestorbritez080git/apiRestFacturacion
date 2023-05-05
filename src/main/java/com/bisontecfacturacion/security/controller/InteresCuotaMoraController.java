package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.InteresCuota;
import com.bisontecfacturacion.security.model.InteresMora;
import com.bisontecfacturacion.security.model.TipoPlazo;
import com.bisontecfacturacion.security.repository.InteresCuotaRepository;
import com.bisontecfacturacion.security.repository.InteresMoraRepository;
import com.bisontecfacturacion.security.repository.TipoPlazoRepository;

@RestController
@RequestMapping("interes")
public class InteresCuotaMoraController {

	@Autowired
	private InteresCuotaRepository cuotaRepository;
	@Autowired
	private InteresMoraRepository moraRepository;
	@Autowired
	private TipoPlazoRepository tipoPlasoRepository;
	
	@RequestMapping(method=RequestMethod.GET, value="/cuota")
	public List<InteresCuota> getAllCuota(){
		return cuotaRepository.findAll();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/mora")
	public List<InteresMora> getAllMora(){
		return moraRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.GET, value="/tipoPlazo")
	public List<TipoPlazo> getAllPlazoCuota(){
		return tipoPlasoRepository.findByOrderByIdAsc();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/tipoPlazoId/{id}")
	public TipoPlazo getAllPlazoCuota(@PathVariable int id){
		return tipoPlasoRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/moraId/{id}")
	public InteresMora getMoraId(@PathVariable int id){
		return moraRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cuotaId/{id}")
	public InteresCuota getCuotaId(@PathVariable int id){
		return cuotaRepository.findById(id).get();
	}
}
