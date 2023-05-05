package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.CustomerErrorType;
import com.bisontecfacturacion.security.model.Permiso;
import com.bisontecfacturacion.security.model.UbicacionPermiso;
import com.bisontecfacturacion.security.repository.PermisoRepository;
import com.bisontecfacturacion.security.repository.UbicacionPermisoRepository;

@RestController
@RequestMapping("permiso")
public class PermisoController{
    @Autowired
    private PermisoRepository entityRepository;

    @Autowired
    private UbicacionPermisoRepository ubicacionRepository;

    @RequestMapping(method=RequestMethod.GET, value = "/{id}")
    public Permiso getPermiso(@PathVariable int id) {
        return entityRepository.getOne(id);
    }
    @RequestMapping(method=RequestMethod.GET)
	public List<Permiso> getAllPermiso(){
		return entityRepository.findAll();
    }
    @RequestMapping(method=RequestMethod.GET, value = "/ubicacion/{id}")
    public UbicacionPermiso getUbicacionId(@PathVariable int id) {
        return ubicacionRepository.getOne(id);
    }
    @RequestMapping(method=RequestMethod.GET, value = "/ubicacion")
	public List<UbicacionPermiso> getAllUbicaion(){
		return ubicacionRepository.findByOrderByPosicionAsc();
    }
    @RequestMapping(method = RequestMethod.POST, value = "/ubicacion")
    public ResponseEntity<?> guardarUbicacion(@RequestBody UbicacionPermiso entity) {
        try {
            if(entity.getDescripcion()!=null){
               entity.setDescripcion(entity.getDescripcion().trim().toUpperCase());			
            }
            ubicacionRepository.save(entity);   
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomerErrorType(""+e.getCause().getMessage()), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST)
public ResponseEntity<?> guardar(@RequestBody Permiso entity) {
    if(entity.getDescripcion()!=null){
        entity.setDescripcion(entity.getDescripcion().trim().toUpperCase());			
    }
    try {
        if(entity.getDescripcion() == null ) {
            return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPICÒN NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
        } else if(entity.getUbicacionPermiso().getId() <= 0 ) {
            return new ResponseEntity<>(new CustomerErrorType("LA UBICACIÒN NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
        }else {
            if(entity.getId() != 0){
                Permiso p = new Permiso();
                p = siExisteEditar(entity);
                if (p!=null) {
                    if(entity.getId()==p.getId()){
                    
                }else{
                    return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL PERIODO QUE ESTAS INTENTANDO ACTUALIZAR YA PERTENCE A UNA DESCRIPCIÓN ANTERIORMENTE GUARDADO.!"),
                                    HttpStatus.CONFLICT); 
                }
            }
            }else{
                if (siExiste(entity)) {
                    return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL PERIODO: " + entity.getDescripcion() + " YA EXISTE."),
                            HttpStatus.CONFLICT);
                }else {
                    //entityRepository.save(entity);
                }
            }

            entityRepository.save(entity);
           
        }
    } catch (Exception e) {
        return new ResponseEntity<>(new CustomerErrorType(""+e.getCause().getMessage()), HttpStatus.CONFLICT);
    }
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    public boolean siExiste(Permiso entity) {
        return entityRepository.findByDescripcion(entity.getDescripcion()) != null;
    }
    public Permiso siExisteEditar(Permiso entity){
        
		return entityRepository.findByDescripcion(entity.getDescripcion());
    }


}