package com.bisontecfacturacion.security.auxiliar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.repository.AddListRouterCamposRepository;
import com.bisontecfacturacion.security.repository.AddListRouterRepository;

@RestController
@RequestMapping("addListRouter")
public class ListRouterController {
	@Autowired
	private AddListRouterRepository addListRouterRepository;
	@Autowired
	private AddListRouterCamposRepository addListRouterCamposRepository;
	
	@RequestMapping(method=RequestMethod.GET, value = "/verCampoPorCabecera/{id}")
	public List<AddListRouterCampos> getAll(@PathVariable int id){
		List<AddListRouterCampos> list= addListRouterCamposRepository.getRouterCampos(id);
		AddListRouterCampos [][] array = new AddListRouterCampos [0][10];
		
		System.out.println(array.length);
		for (int i = 0; i < list.size(); i++) {
			array[0][i]=list.get(i);
		}
		for (int i = 0; i < array.length; i++) {
			//System.out.println(array[i].getDescripcion()+" "+array[i].isEstado() );
		}
		System.out.println(array.toString());
		
		return list;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value = "/activarCampos/{id}")
	public void activarVisibilidad(@PathVariable int id){
		addListRouterCamposRepository.activarCampoVisiblidad(id);
		System.out.println("activado");
	}

	@RequestMapping(method=RequestMethod.GET, value = "/desactivarCampos/{id}")
	public void desactivarVisibilidad(@PathVariable int id){
		addListRouterCamposRepository.desactivarCampoVisiblidad(id);
		System.out.println("desactivado");
	}
	
}
