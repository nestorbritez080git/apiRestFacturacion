package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.Marca;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;

@Transactional
@RestController
@RequestMapping("org")
public class OrgController {

	@Autowired
	private OrgRepository entityRepository;
	@Autowired
	private ImpresoraRepository impresoraRepository;

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
		o.setNombreImagen(orga.getNombreImagen());
		return o;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public Org update(@RequestBody Org entity){
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.POST, value="/subirImagen")
	public Marca guardarImagen(@RequestParam("image") MultipartFile imagen) {
		
		Marca nombreEntity = new Marca();
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append(UUID.randomUUID().toString().replace("-", ""));
		String type = imagen.getContentType();
		if("image/png".equals(type)) {
			fileName.append(".png");
		} else
		if("image/jpeg".equals(type)) {
			fileName.append(".jpeg");
		} else
		if("image/gif".equals(type)) {
			fileName.append(".git");
		} else {
			nombreEntity.setId(504);
			nombreEntity.setDescripcion("Elige un tipo de imagen valido...");
		}
		
		Path directorioImagen= null;
		Impresora imp = impresoraRepository.findById(10).orElse(null);
		if (imp==null) {
			System.err.println("SE DEBE AGREGAR REGISTRO EN LA BD impresora ID=10, DESCRIPCION=Produccion, estado=false");
		}else {
			if(imp.isEstado()) {
				  directorioImagen = Paths.get("webapps//imagen");
				  
			}else {
				 directorioImagen = Paths.get("src//main//resources//static//imagen");
			}
		}
		
			String rutaAbsoluta = directorioImagen.toFile().getAbsolutePath();
			System.out.println(rutaAbsoluta);
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCompleta = Paths.get(directorioImagen + "//" + fileName.toString());
				System.out.println(rutaCompleta);
				Files.write(rutaCompleta, bytesImg);
				nombreEntity.setId(204);
				nombreEntity.setDescripcion(fileName.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return nombreEntity;
	}

}
