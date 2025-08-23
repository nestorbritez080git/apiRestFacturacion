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
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;

@RestController
@RequestMapping("notaCredito")
public class NotaCreditoController {
	 @Autowired
	    private NotaCreditoRepository entityRepository;
	 
	 @RequestMapping(method=RequestMethod.GET, value = "/all")
		public List<NotaCredito> getAll(){
		 System.out.println("nota lis all");
		 return cargarLista(entityRepository.findAll());
	}
	 @RequestMapping(method=RequestMethod.GET)
		public List<NotaCredito> getLis(){
		 System.out.println("nota lis 50");
			return cargarLista(entityRepository.getAllLimites());
	}
	 @RequestMapping(method=RequestMethod.POST, value="/buscar")
		public List<NotaCredito> consultarPorDescripcion(@RequestBody String descripcion){
			List<NotaCredito>objeto = new ArrayList<>();	
			objeto=entityRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return cargarLista(objeto);
	
	}
	 @RequestMapping(method=RequestMethod.GET, value="/buscar/ventaId/{idVenta}")
		public NotaCredito consultarPorVentaId(@PathVariable Integer idVenta){
			return cargarObjeto(entityRepository.getNotaCreditoPorVentaCabeceraId(idVenta));
	}
	 @RequestMapping(method=RequestMethod.GET, value="/buscar/devolucionId/{idDevol}")
		public NotaCredito consultarPorDevolucionId(@PathVariable Integer idDevol){
			return cargarObjeto(entityRepository.getNotaCreditoPorDevolucionId(idDevol));
	}
	 public List<NotaCredito> cargarLista(List<NotaCredito> lista) {
			List<NotaCredito> listaRetorno = new ArrayList<NotaCredito>();
			for(NotaCredito d: lista) {
				NotaCredito nota = new NotaCredito();
				nota.setId(d.getId());
				nota.setCliente(d.getCliente());
				nota.getCliente().setPersona(d.getCliente().getPersona());
				nota.setFuncionario(d.getFuncionario());
				nota.getFuncionario().setPersona(d.getFuncionario().getPersona());
				nota.setDevolucionVenta(d.getDevolucionVenta());
				nota.getDevolucionVenta().setVenta(d.getDevolucionVenta().getVenta());
				nota.setFecha(d.getFecha());
				nota.setTotal(d.getTotal());
				nota.setTotalLetra(d.getTotalLetra());
				nota.setHora(d.getHora());
				nota.setNumeroVenta(d.getNumeroVenta());
				nota.setEstado(d.getEstado());
				listaRetorno.add(nota);
			}

			return listaRetorno;
		}
	 public NotaCredito cargarObjeto(NotaCredito n) {
		 NotaCredito nota;
		 	if(n!=null) {
		 		nota = new NotaCredito();
				nota.setId(n.getId());
				nota.setCliente(n.getCliente());
				nota.getCliente().setPersona(n.getCliente().getPersona());
				nota.setFuncionario(n.getFuncionario());
				nota.getFuncionario().setPersona(n.getFuncionario().getPersona());
				nota.setDevolucionVenta(n.getDevolucionVenta());
				nota.getDevolucionVenta().setVenta(n.getDevolucionVenta().getVenta());
				nota.setFecha(n.getFecha());
				nota.setTotal(n.getTotal());
				nota.setHora(n.getHora());
				nota.setNumeroVenta(n.getNumeroVenta());
				nota.setTotalLetra(n.getTotalLetra());
				nota.setEstado(n.getEstado());
				System.out.println("NOTA OBJETO: "+n.getId());
		 	}else {
		 		nota = null;
		 	}
		 	
			return nota;
	 }
}
