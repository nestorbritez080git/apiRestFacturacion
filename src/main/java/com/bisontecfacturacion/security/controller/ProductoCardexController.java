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
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("productoCardex")
public class ProductoCardexController {
	@Autowired
	private ProductoCardexRepository entityRepository;
	
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody ProductoCardex entity){
		if(entity.getFuncionario().getId()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO.!"), HttpStatus.CONFLICT);
		}else if(entity.getProductoCompuesto().getId()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("EL PRODUCTO COMPUESTO NO DEBE QUEDAR VACIO.!"), HttpStatus.CONFLICT);
		} else if(entity.getProductoBase().getId()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("EL PRODUCTO BASE NO DEBE QUEDAR VACIO.!"), HttpStatus.CONFLICT);
		} else if(entity.getCantidadAplicacion()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DE APLICACION DEL PRODUCTO COMPUESTO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
		}
		entity.setFecha(new Date());
		entity.setHora(hora());
		if(entity.getId()!=0) {
			ProductoCardex pp= entityRepository.getOne(entity.getId());
			if(pp.getProductoCompuesto().getId()==entity.getProductoCompuesto().getId()) {
				entityRepository.save(entity);
				
			}else {
				System.out.println("Entro else editar:   "+pp.getProductoCompuesto().getId()+"   : "+entity.getProductoCompuesto().getId());
				if(validarProductoCompuestoNuevo(entity.getProductoCompuesto().getId())==true) {
					return  new  ResponseEntity<>(new CustomerErrorType("EL PRODUCTO COMPUESTO SELECCIONADO YA ESTÁ REGISTRADO COMO PRODUCTO COMPUESTO DENTRO DEL SISTEMA"),HttpStatus.CONFLICT);
							
				}else {
					entityRepository.findByActualizarProductoEstadoCompuesto(true, pp.getProductoCompuesto().getId());
					entityRepository.save(entity);
					entityRepository.findByActualizarProductoEstadoCompuesto(false, entity.getProductoCompuesto().getId());
					
				}
			}
		}else {	
			if (validarProductoCompuestoNuevo(entity.getProductoCompuesto().getId())==true) {
				return  new  ResponseEntity<>(new CustomerErrorType("EL PRODUCTO COMPUESTO SELECCIONADO YA ESTÁ REGISTRADO COMO PRODUCTO COMPUESTO DENTRO DEL SISTEMA"),HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
				entityRepository.findByActualizarProductoEstadoCompuesto(false, entity.getProductoCompuesto().getId());				
			}
			
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);			
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	private boolean validarProductoCompuestoNuevo(int idCompuesto) {
		boolean ope=false;
		ProductoCardex prodCompu= entityRepository.consProCompu(idCompuesto);
		if(prodCompu!=null) {ope=true;}else {ope= false;}
		return ope;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/descripcion")
	public List<ProductoCardex> getAllProducto(@RequestBody String descripcion){
		if (descripcion.equals("9999999999")) {
			List<ProductoCardex> objeto=entityRepository.lista();
			return product(objeto);
		} else {
			System.out.println("entroooo acaaaa");
			List<ProductoCardex> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
		}
	}
	
	public List<ProductoCardex> product(List<ProductoCardex> objeto) {
		List<ProductoCardex> producto=new ArrayList<>();
		
		System.out.println("entroooooo ***********");
		for(ProductoCardex ob:objeto){
			ProductoCardex productos=new ProductoCardex();
			productos.setId(ob.getId());
			productos.getProductoCompuesto().setId(ob.getProductoCompuesto().getId());
			productos.getProductoCompuesto().setDescripcion(ob.getProductoCompuesto().getDescripcion());
			productos.getProductoCompuesto().getMarca().setDescripcion(ob.getProductoCompuesto().getMarca().getDescripcion());
			productos.getProductoBase().setId(ob.getProductoBase().getId());
			productos.getProductoBase().setDescripcion(ob.getProductoBase().getDescripcion());
			productos.getProductoBase().getMarca().setDescripcion(ob.getProductoBase().getMarca().getDescripcion());
			productos.setCantidadAplicacion(ob.getCantidadAplicacion());
			productos.setFecha(new Date());
			producto.add(productos);
		}
		
		return producto;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ProductoCardex getUltReg(){
		ProductoCardex ob=entityRepository.findTop1ByOrderByIdDesc();
		ProductoCardex productos=new ProductoCardex();
		productos.setId(ob.getId());
		productos.getProductoCompuesto().setId(ob.getProductoCompuesto().getId());
		productos.getProductoCompuesto().setDescripcion(ob.getProductoCompuesto().getDescripcion());
		productos.getProductoCompuesto().getMarca().setDescripcion(ob.getProductoCompuesto().getMarca().getDescripcion());
		productos.getProductoBase().setId(ob.getProductoBase().getId());
		productos.getProductoBase().setDescripcion(ob.getProductoBase().getDescripcion());
		productos.getProductoBase().getMarca().setDescripcion(ob.getProductoBase().getMarca().getDescripcion());
		productos.setCantidadAplicacion(ob.getCantidadAplicacion());
		return productos;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
	public ProductoCardex getId(@PathVariable int id){
		ProductoCardex ob=entityRepository.findById(id).get();
		ProductoCardex productos=new ProductoCardex();
		productos.setId(ob.getId());
		productos.getProductoCompuesto().setId(ob.getProductoCompuesto().getId());
		productos.getProductoCompuesto().setDescripcion(ob.getProductoCompuesto().getDescripcion());
		productos.getProductoCompuesto().getMarca().setDescripcion(ob.getProductoCompuesto().getMarca().getDescripcion());
		productos.getProductoBase().setId(ob.getProductoBase().getId());
		productos.getProductoBase().setDescripcion(ob.getProductoBase().getDescripcion());
		productos.getProductoBase().getMarca().setDescripcion(ob.getProductoBase().getMarca().getDescripcion());
		productos.setCantidadAplicacion(ob.getCantidadAplicacion());
		return productos;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/consolidarProductoUnidades/{id}")
	public ResponseEntity<?> getProductoCompuestoPorProductoBase(@PathVariable int id){
		System.out.println("consolidar unidaddes");
		List<ProductoCardex> objeto=entityRepository.getBase(id);
		Producto pExistencia = productoRepository.getOne(id);
		for(ProductoCardex ob:objeto){
			System.out.println("id Compuesto : "+ob.getProductoCompuesto().getId());
			System.out.println("Cantidad de aplicacionn: "+ ob.getCantidadAplicacion());
			productoRepository.findByActualizaCantidadInventario(pExistencia.getExistencia()/ob.getCantidadAplicacion(), ob.getProductoCompuesto().getId());
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/consolidarProductoUnidadesCero/{id}")
	public ResponseEntity<?> getProductoCompuestoPorProductoBaseCero(@PathVariable int id){
		System.out.println("consolidad unidades cerro");
		List<ProductoCardex> objeto=entityRepository.getBase(id);
		for(ProductoCardex ob:objeto){			
			productoRepository.findByActualizaCantidadInventario(0, ob.getProductoCompuesto().getId());
		}
		productoRepository.findByActualizaCantidadInventario(0, id);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		System.out.println("consultar id : "+ id);
		ProductoCardex pp = entityRepository.getOne(id);
		System.out.println(" * ***   "+pp.getProductoCompuesto().getId());
		entityRepository.deleteById(id);
		entityRepository.findByActualizarProductoEstadoCompuesto(true, pp.getProductoCompuesto().getId());
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/verificador/{id}/{tipo}")
	public Boolean validarCampoIsExist(@PathVariable int id, @PathVariable int tipo){
		Boolean estado = false;
		if (tipo == 1) {
			ProductoCardex proBase=entityRepository.consProBase(id);
			if (proBase == null) {
				estado = false;
			} else {
				estado = true;
			}
		}
		
		if (tipo == 2) {
			ProductoCardex proBase=entityRepository.consProCompu(id);
			if (proBase == null) {
				estado = false;
			} else {
				estado = true;
			}
		}
		
		return estado;
	}
	
	
	
}
