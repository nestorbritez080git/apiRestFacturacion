package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.OrdenProduccion;
import com.bisontecfacturacion.security.model.OrdenProduccionDetalle;
import com.bisontecfacturacion.security.repository.OrdenProduccionDetalleRepository;
import com.bisontecfacturacion.security.repository.OrdenProduccionRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("ordenProduccion")
public class OrdenProduccionController {

	@Autowired
	private OrdenProduccionRepository entityRepository;
	
	@Autowired
	private OrdenProduccionDetalleRepository detalleRepository;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<OrdenProduccion> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);

		return listado(entityRepository.getOrdenProduccion(ano, mes, dia));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<OrdenProduccion> getAll(){
		return listado(entityRepository.findAll());
	}

	public List<OrdenProduccion> listado(List<OrdenProduccion> lista) {
		List<OrdenProduccion> listadoRetorno = new ArrayList<OrdenProduccion>();
		for(OrdenProduccion l: lista) {
			OrdenProduccion o =new OrdenProduccion();
			o.setId(l.getId());
			o.getFuncionario().setId(l.getFuncionario().getId());
			o.getFuncionario().getPersona().setNombre(l.getFuncionario().getPersona().getNombre());
			o.getFuncionario().getPersona().setApellido(l.getFuncionario().getPersona().getApellido());
			o.getFuncionarioA().setId(l.getFuncionarioA().getId());
			o.getFuncionarioA().getPersona().setNombre(l.getFuncionarioA().getPersona().getNombre());
			o.getFuncionarioA().getPersona().setApellido(l.getFuncionarioA().getPersona().getApellido());
			o.getProduccionCostoCabecera().setProduccionDescripcion(l.getProduccionCostoCabecera().getProduccionDescripcion());
			o.setCantidad(l.getCantidad());
			o.setCantidadEntregada(l.getCantidadEntregada());
			o.setFecha(l.getFecha());
			o.setFechaEntrega(l.getFechaEntrega());
			o.setHora(l.getHora());
			o.getProduccionCostoCabecera().setCostoTotalMateriaPrima(l.getProduccionCostoCabecera().getCostoTotalMateriaPrima());
			o.setEstado(l.getEstado());
			listadoRetorno.add(o);
		}
		return listadoRetorno;
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody OrdenProduccion entity){
		 
		if(entity.getFuncionario().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getFuncionarioA().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO AUTORIZACION NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getCantidad()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getProduccionCostoCabecera().getId()== 0) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN  PRODUCTO PARA LA ORDENAR PRODUCCION!"), HttpStatus.CONFLICT);
		} else if(entity.getOrdenProduccionDetalles().size()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES AGREGAR POR LO MENO UN DETALLE DE INGREDIENTE PARA GENERAR PRODUCCION!"), HttpStatus.CONFLICT);
		}else {
			for(int ind=0; ind < entity.getOrdenProduccionDetalles().size(); ind++) {
                OrdenProduccionDetalle det = entity.getOrdenProduccionDetalles().get(ind);
                if(det.getPrecioUnitario() <= 0  || det.getPrecioUnitario() ==null) {
                    return new ResponseEntity<>(new CustomerErrorType("EL PRECIO UNITARIO DEL ITEM N°: "+(ind+1)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getCantidad()<=0 || det.getCantidad() == null) {
                    return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL ITEM N°: "+(ind+1)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getSubTotal() == null || det.getSubTotal() <=0){
                    return new ResponseEntity<>(new CustomerErrorType("EL SUBTOTAL DEL ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getDescripcionIngrediente().equals("") || det.getDescripcionIngrediente() == null){
                    return new ResponseEntity<>(new CustomerErrorType("LA DESCRICÓN DEL ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getDescripcionUnidadMedida().equals("") || det.getDescripcionUnidadMedida() == null) {
                	 return new ResponseEntity<>(new CustomerErrorType("LA DESCRICÓN DE LA UD. MEDIDA DEL ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getProducto().getId() <= 0  ||det.getProducto() ==null){
                	 return new ResponseEntity<>(new CustomerErrorType("EL PRODUCTO DEL ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
                }else if(det.getUnidadMedida().getId() <= 0  ||det.getUnidadMedida() ==null){
               	 return new ResponseEntity<>(new CustomerErrorType("EL UNIDAD MEDIDA DEL ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
               }
                det.setDescripcionIngrediente(Utilidades.eliminaCaracterIzqDer(det.getDescripcionIngrediente()));
                det.setDescripcionUnidadMedida(Utilidades.eliminaCaracterIzqDer(det.getDescripcionUnidadMedida()));
            }
		}
		entity.setFecha(new Date());
		entity.setHora(hora());
		 if(entity.getId() !=0) {
			 System.out.println("Entrooo editart	");
			 entityRepository.save(entity);
			 for(OrdenProduccionDetalle det: entity.getOrdenProduccionDetalles()) {
                 det.getOrdenProduccion().setId(entity.getId());
                 detalleRepository.save(det);
             }
		 } else {
			 System.out.println("Entroo nuevo");
			 entityRepository.save(entity);
			 OrdenProduccion id = entityRepository.findTop1ByOrderByIdDesc();
	            int idFle=0;
	            if(id == null){idFle=1;}else{idFle=id.getId();}
			 for(OrdenProduccionDetalle det: entity.getOrdenProduccionDetalles()) {
                 det.getOrdenProduccion().setId(idFle);
                 detalleRepository.save(det);
             }
		 }
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/revertirEntrega/{id}")
	public void revertirEntrega(@PathVariable int id){
		entityRepository.actualizarEstadoEntrega(id, false, (double) 0);
	}
	
}
