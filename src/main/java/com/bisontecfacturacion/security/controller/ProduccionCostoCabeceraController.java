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
import com.bisontecfacturacion.security.model.ProduccionCostoCabecera;
import com.bisontecfacturacion.security.model.ProduccionMateriaPrima;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.repository.ProduccionCostoCabeceraRepository;
import com.bisontecfacturacion.security.repository.ProduccionMateriaPrimaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;


@RestController
@RequestMapping("produccionCostoCabecera")
public class ProduccionCostoCabeceraController {
	
	@Autowired
	private ProduccionCostoCabeceraRepository entityRepository;
	
	@Autowired
	private ProduccionMateriaPrimaRepository detalleMateriaPrimaRepository;
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody ProduccionCostoCabecera entity){
		 
		if(entity.getFuncionario().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getProducto().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL PRODUCTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getProduccionDescripcion().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCION NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else 	if(entity.getCostoTotalManoObra() <= 0 || entity.getCostoTotalFabricacion() <= 0 || entity.getCostoTotalMateriaPrima() <= 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL CAMPO   NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(entity.getCantidadProduccion() == null || entity.getCantidadProduccion() <=0 ) {
			return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DE PRODUCCIÓN NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getCodigoInterno()== null) {
			return new ResponseEntity<>(new CustomerErrorType("EL CODIGO INTERNO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getProduccionMateriaPrimas().size()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES AGREGAR POR LO MENO UN DETALLE DE MATERIA PRIMAS PARA GUARDAR PRODUCCION INGREDIENTES!"), HttpStatus.CONFLICT);
		}else {
			for(int ind=0; ind < entity.getProduccionMateriaPrimas().size(); ind++) {
                ProduccionMateriaPrima det = entity.getProduccionMateriaPrimas().get(ind);
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
                
            }
		}
		entity.setProduccionDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getProduccionDescripcion()));
		entity.setCodigoInterno(Utilidades.eliminaCaracterIzqDer(entity.getCodigoInterno()));
		
		entity.setFecha(new Date());
		entity.setHora(hora());
		 if(entity.getId() !=0) {
			 System.out.println("entrooo editoarrr");
			 	Producto p = new Producto();
				p=siExisteEditar(entity);
				if (p!=null) {
					 System.out.println("entroo not null ptodd");
					if(entity.getProducto().getId()==p.getId()){
						 
						entityRepository.save(entity);
						for(ProduccionMateriaPrima det: entity.getProduccionMateriaPrimas()) {
							det.getProduccionCostoCabecera().setId(entity.getId());
							det.setDescripcionIngrediente(Utilidades.eliminaCaracterIzqDer(det.getDescripcionIngrediente()));
							det.setDescripcionUnidadMedida(Utilidades.eliminaCaracterIzqDer(det.getDescripcionUnidadMedida()));
							detalleMateriaPrimaRepository.save(det);
						}
						
					}else {
						return new ResponseEntity<>(new CustomerErrorType("EL PRODUCTO :  "+entity.getProducto().getDescripcion()+", PERTENECE A UNA PRODUCCIÓN YA REGISTRADO ANTERIORMENTE"), HttpStatus.CONFLICT);
					}
				}else {
					entityRepository.save(entity);
					for(ProduccionMateriaPrima det: entity.getProduccionMateriaPrimas()) {
						det.getProduccionCostoCabecera().setId(entity.getId());
						det.setDescripcionIngrediente(Utilidades.eliminaCaracterIzqDer(det.getDescripcionIngrediente()));
						det.setDescripcionUnidadMedida(Utilidades.eliminaCaracterIzqDer(det.getDescripcionUnidadMedida()));
						detalleMateriaPrimaRepository.save(det);
					}
				}
		 } else {
			 if (siExiste(entity)) {
					return new ResponseEntity<>(new CustomerErrorType("YA EXISTE EL PRODUCCTO!"), HttpStatus.CONFLICT);
				} else {
				entityRepository.save(entity);
				 ProduccionCostoCabecera id = entityRepository.findTop1ByOrderByIdDesc();
		            int idFle=0;
		            if(id == null){idFle=1;}else{idFle=id.getId();}
				 for(ProduccionMateriaPrima det: entity.getProduccionMateriaPrimas()) {
	                 det.getProduccionCostoCabecera().setId(idFle);
	                 det.setDescripcionIngrediente(Utilidades.eliminaCaracterIzqDer(det.getDescripcionIngrediente()));
					 det.setDescripcionUnidadMedida(Utilidades.eliminaCaracterIzqDer(det.getDescripcionUnidadMedida()));
	                 detalleMateriaPrimaRepository.save(det);
	             }
			}
		 }
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public boolean siExiste(ProduccionCostoCabecera entity){
		return entityRepository.getPorIdProd(entity.getProducto().getId())!=null;
	}
	public Producto siExisteEditar(ProduccionCostoCabecera entity){
		ProduccionCostoCabecera prod= entityRepository.getPorIdProd(entity.getProducto().getId());
		Producto p =prod.getProducto();
		return p;
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<ProduccionCostoCabecera> get(){
		return listado(entityRepository.findAll());
	}
	
	public List<ProduccionCostoCabecera> listado(List<ProduccionCostoCabecera> objeto) {
		List<ProduccionCostoCabecera> lista=new ArrayList<ProduccionCostoCabecera>();
		for(ProduccionCostoCabecera p: objeto) {
			ProduccionCostoCabecera pro=new ProduccionCostoCabecera();
			pro.setId(p.getId());
			pro.setProduccionDescripcion(p.getProduccionDescripcion());
			pro.setCantidadProduccion(p.getCantidadProduccion());
			pro.setCostoTotalMateriaPrima(p.getCostoTotalMateriaPrima());
			pro.setCodigoInterno(p.getCodigoInterno());
			lista.add(pro);
		}
		return lista;
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public ProduccionCostoCabecera getCabeceraId(@PathVariable int id){
		return cargarCabecera(entityRepository.findById(id).orElse(null));
	}
	@Transactional
	@RequestMapping(method=RequestMethod.GET, value="/detalleMateriaPrima/{id}")
	public List<ProduccionMateriaPrima> getDetalleIdCabecera(@PathVariable int id){
		return listadoMateriaPrima(detalleMateriaPrimaRepository.getProduccionMateriaPrimiaIdCabecera(id));
	}
	
	public List<ProduccionMateriaPrima> listadoMateriaPrima(List<ProduccionMateriaPrima> list) {
		List<ProduccionMateriaPrima> lista = new ArrayList<ProduccionMateriaPrima>();
		for(ProduccionMateriaPrima m: list) {
			ProduccionMateriaPrima mat = new ProduccionMateriaPrima();
			mat.setId(m.getId());
			mat.setPrecioUnitario(m.getPrecioUnitario());
			mat.setCantidad(m.getCantidad());
			mat.setSubTotal(m.getSubTotal());
			mat.setDescripcionIngrediente(m.getDescripcionIngrediente());
			mat.setDescripcionUnidadMedida(m.getDescripcionUnidadMedida());
			mat.getProducto().setId(m.getProducto().getId());
			mat.getUnidadMedida().setId(m.getUnidadMedida().getId());
			mat.getUnidadMedida().setDescripcion(m.getUnidadMedida().getDescripcion());
			mat.getProduccionCostoCabecera().setId(m.getProduccionCostoCabecera().getId());
			lista.add(mat);
		}
		return lista;
	}
	
	public ProduccionCostoCabecera cargarCabecera(ProduccionCostoCabecera entity) {
		ProduccionCostoCabecera pro = new ProduccionCostoCabecera();
		pro.setId(entity.getId());
		pro.setFecha(entity.getFecha());
		pro.setHora(entity.getHora());
		pro.setProduccionDescripcion(entity.getProduccionDescripcion());
		pro.setCantidadProduccion(entity.getCantidadProduccion());
		pro.setCostoTotalManoObra(entity.getCostoTotalManoObra());
		pro.setCostoTotalFabricacion(entity.getCostoTotalFabricacion());
		pro.setCostoTotalMateriaPrima(entity.getCostoTotalMateriaPrima());
		pro.setCodigoInterno(entity.getCodigoInterno());
		pro.setEstado(entity.getEstado());
		pro.getProducto().setId(entity.getProducto().getId());
		pro.getFuncionario().setId(entity.getFuncionario().getId());
		return pro;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/eliminarDetalle")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<ProduccionMateriaPrima> detalle){
		//ResponseEntity<?> rep= new ResponseEntity<?>(null);
		try {
			for (ProduccionMateriaPrima ob: detalle) {
				detalleMateriaPrimaRepository.deleteById(ob.getId());				
			}
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
