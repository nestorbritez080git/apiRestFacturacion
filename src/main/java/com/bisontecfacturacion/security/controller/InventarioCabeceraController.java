package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.InventarioCabecera;
import com.bisontecfacturacion.security.model.InventarioDetalle;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.TipoInventario;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.InventarioCabeceraRepository;
import com.bisontecfacturacion.security.repository.InventarioDetalleRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.TipoInventarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@Transactional
@RestController
@RequestMapping("inventario")
public class InventarioCabeceraController {

	@Autowired
	private TipoInventarioRepository tipoInventarioRepository;

	@Autowired
	private InventarioCabeceraRepository entityRepository;

	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private InventarioDetalleRepository detalleRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;

	@RequestMapping(method=RequestMethod.GET, value="/tipoInventario")
	public List<TipoInventario> getAllTipoInventario(){
		return tipoInventarioRepository.findAll();
	}

	@RequestMapping(method=RequestMethod.GET, value="/ultimoInventario")
	public InventarioCabecera getUltimoInventario(){
		InventarioCabecera l = entityRepository.findTop1ByOrderByIdDesc();
		InventarioCabecera li=new InventarioCabecera();
		li.setId(l.getId());
		li.setPeriodo(l.getPeriodo());
		li.getFuncionarioA().getPersona().setNombre(l.getFuncionarioA().getPersona().getNombre());
		li.getFuncionarioA().getPersona().setApellido(l.getFuncionarioA().getPersona().getApellido());
		li.getFuncionarioR().getPersona().setNombre(l.getFuncionarioR().getPersona().getNombre());
		li.getFuncionarioR().getPersona().setApellido(l.getFuncionarioR().getPersona().getApellido());
		li.setFechaInicio(l.getFechaInicio());
		li.getTipoInventario().setDescripcion(l.getTipoInventario().getDescripcion());
		li.setEstado(l.getEstado());
		return li;
	}

	@RequestMapping(method=RequestMethod.POST)
	public  ResponseEntity<?> guardar(@RequestBody InventarioCabecera entity){
		if (entity.getFechaInicio() == null) {
			return new ResponseEntity<>(new CustomerErrorType("La fecha no debe quedar vacio!"), HttpStatus.CONFLICT);
		} else if (entity.getPeriodo().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("El periodo no debe quedar vacio!"), HttpStatus.CONFLICT);
		} else {
			entityRepository.save(entity);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.POST, value="/detalle")
	public InventarioDetalle saveDetalle(@RequestBody InventarioDetalle entity){
		InventarioDetalle v = new InventarioDetalle();
		v= detalleRepository.getDetallePorCabProducto(entity.getInventarioCabecera().getId(), entity.getProducto().getId());
		if(v != null) {
			entity.setCantidad(v.getCantidad()+ entity.getCantidad());
			entity.setId(v.getId());
		}else {
			entity.setId(0);
		}
		entity.setFecha(new Date());
		entity.setHora(hora());
		return detalleRepository.save(entity);	

	}
	@RequestMapping(method=RequestMethod.POST, value="/detalleCantidad")
	public InventarioDetalle saveDetalleCantidad(@RequestBody InventarioDetalle entity){
		entity.setFecha(new Date());
		entity.setHora(hora());
		return detalleRepository.save(entity);	

	}
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	@RequestMapping(method=RequestMethod.GET)
	public List<InventarioCabecera> getAll(){
		return cargarCabecera(entityRepository.findTop50ByOrderByIdDesc());
	}

	public List<InventarioCabecera> cargarCabecera(List<InventarioCabecera> lista) {
		List<InventarioCabecera> listado=new ArrayList<InventarioCabecera>();
		for(InventarioCabecera l: lista) {
			InventarioCabecera li=new InventarioCabecera();
			li.setId(l.getId());
			li.getFuncionarioA().getPersona().setNombre(l.getFuncionarioA().getPersona().getNombre());
			li.getFuncionarioA().getPersona().setApellido(l.getFuncionarioA().getPersona().getApellido());
			li.getFuncionarioR().getPersona().setNombre(l.getFuncionarioR().getPersona().getNombre());
			li.getFuncionarioR().getPersona().setApellido(l.getFuncionarioR().getPersona().getApellido());
			li.setFechaInicio(l.getFechaInicio());
			li.setFechaFin(l.getFechaFin());
			li.setEstado(l.getEstado());
			listado.add(li);
		}
		return listado;
	}

	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public InventarioCabecera geIdCabecera(@PathVariable int id){
		InventarioCabecera l = entityRepository.findById(id).get();
		InventarioCabecera li=new InventarioCabecera();
		li.setId(l.getId());
		li.setPeriodo(l.getPeriodo());
		li.getFuncionarioA().getPersona().setNombre(l.getFuncionarioA().getPersona().getNombre());
		li.getFuncionarioA().getPersona().setApellido(l.getFuncionarioA().getPersona().getApellido());
		li.setFechaInicio(l.getFechaInicio());
		li.getTipoInventario().setDescripcion(l.getTipoInventario().getDescripcion());
		li.setEstado(l.getEstado());
		return li;
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalle/{id}")
	public InventarioCabecera geIdCabeceraDetalle(@PathVariable int id){
		InventarioCabecera l = entityRepository.findById(id).get();
		InventarioCabecera li=new InventarioCabecera();
		li.setId(l.getId());
		li.setPeriodo(l.getPeriodo());
		li.getFuncionarioA().getPersona().setNombre(l.getFuncionarioA().getPersona().getNombre());
		li.getFuncionarioA().getPersona().setApellido(l.getFuncionarioA().getPersona().getApellido());
		li.getFuncionarioR().getPersona().setNombre(l.getFuncionarioR().getPersona().getNombre());
		li.getFuncionarioR().getPersona().setApellido(l.getFuncionarioR().getPersona().getApellido());
		li.setFechaInicio(l.getFechaInicio());
		li.setFechaFin(l.getFechaFin());
		li.setItemCantidad(l.getItemCantidad());
		li.setTotalCantidadCosto(l.getTotalCantidadCosto());
		li.setTotalDiferenciaCosto(l.getTotalDiferenciaCosto());
		li.setTotalExistenciaCosto(l.getTotalExistenciaCosto());
		li.getTipoInventario().setDescripcion(l.getTipoInventario().getDescripcion());
		li.setEstado(l.getEstado());
		return li;
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleIdCabecera/{id}")
	public List<InventarioDetalle> getDetalleIdCabecera(@PathVariable int id){
		return cargarDetalle(detalleRepository.getInventarioDetalleIdCabecera(id));
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleIdCabeceraUno/{id}")
	public List<InventarioDetalle> getDetalleIdCabeceraUno(@PathVariable int id){
		return cargarDetalle(detalleRepository.getInventarioDetalleIdCabeceraUno(id));
	}



	public List<InventarioDetalle> cargarDetalle(List<InventarioDetalle> lista) {
		List<InventarioDetalle> listado = new ArrayList<InventarioDetalle>();
		for(InventarioDetalle d: lista) {
			InventarioDetalle in=new InventarioDetalle();
			in.setId(d.getId());
			in.setDescripcion(d.getDescripcion());
			in.setCantidad(d.getCantidad());
			in.getProducto().setId(d.getProducto().getId());
			in.setPrecioCosto(d.getPrecioCosto());
			in.setPrecioVenta1(d.getPrecioVenta1());
			in.setPrecioVenta2(d.getPrecioVenta2());
			in.setPrecioVenta3(d.getPrecioVenta3());
			in.setPrecioVenta4(d.getPrecioVenta4());
			in.setExistencia(d.getExistencia());
			in.setFecha(d.getFecha());
			in.getProducto().setCodbar(d.getProducto().getCodbar());
			in.setHora(d.getHora());

			listado.add(in);
		}
		return listado;
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		detalleRepository.deleteById(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/detalleIdCabeceraUno/{idCabe}/{idProd}")
	public InventarioDetalle getDetId(@PathVariable int idCabe, @PathVariable int idProd){
		return detalleRepository.getDetallePorCabProducto(idCabe, idProd);
	}

	@RequestMapping(method=RequestMethod.GET, value = "terminarInventario/{idCab}")
	public void terminarInventario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int idCab){
		InventarioCabecera entity= entityRepository.getOne(idCab);
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		List<InventarioDetalle> lis= new ArrayList<InventarioDetalle>();
		lis = detalleRepository.getInventarioDetalleIdCabecera(entity.getId());
		Double totalCantidad=0.0, totalExistencia=0.0, totalDifere=0.0;
		for(InventarioDetalle ob: lis) {
			totalCantidad += ob.getCantidad() * ob.getPrecioCosto();
			totalExistencia += ob.getExistencia() * ob.getPrecioCosto();

			actualizarProductoBase(ob.getProducto().getId(), ob.getCantidad(), idCab, usuario.getFuncionario().getId());	

			// productoRepository.findByActualizaE(ob.getCantidad(), ob.getProducto().getId());

		}
		totalDifere= totalCantidad - totalExistencia;
		entity.setTotalCantidadCosto(totalCantidad);
		entity.setTotalExistenciaCosto(totalExistencia);
		entity.setTotalDiferenciaCosto(totalDifere);
		entity.setFechaFin(new Date());
		entity.setEstado(true);

		entityRepository.save(entity);

	}

	public void actualizarProductoBase(int id , double cantidad, int idCab, int idFunc) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaCantidadInventario(existenciaBase, ca.getProductoBase().getId());
			Producto pro = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();

			movEnt.setDescripcion(pro.getDescripcion());
			movEnt.setCantidad(existenciaBase);
			movEnt.setFecha(new  Date());
			movEnt.setHora(hora());

			movEnt.setIngreso(0.0);
			movEnt.setEgreso(0.0);
			movEnt.setVentaSalida(0.0);

			movEnt.setCostoEntrada(0.0);
			movEnt.setCostoEntradaAnterior(0.0);
			movEnt.setCostoSalida(pro.getPrecioCosto());

			movEnt.setVenta_1(pro.getPrecioVenta_1());
			movEnt.setVenta_2(pro.getPrecioVenta_2());
			movEnt.setVenta_3(pro.getPrecioVenta_3());
			movEnt.setVenta_4(pro.getPrecioVenta_4());

			movEnt.setVenta_1_anterior(0.0);
			movEnt.setVenta_2_anterior(0.0);
			movEnt.setVenta_3_anterior(0.0);
			movEnt.setVenta_4_anterior(0.0);

			if(cantidad >= pro.getExistencia()) {
				
				movEnt.getTipoMovimiento().setId(1);
			}else {
				movEnt.getTipoMovimiento().setId(2);
			}
			movEnt.getProducto().setId(pro.getId());
			movEnt.getFuncionario().setId(idFunc);
			movEnt.setMarca(pro.getMarca().getDescripcion());
			Concepto conce= new Concepto();

			conce = conceptoRepository.findById(8).get();	

			movEnt.setReferencia(conce.getDescripcion()+" REF.: "+ idCab);
			movEntradaSalidaRepository.save(movEnt);
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaCantidadInventario(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto

				Producto pp = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

				mov.setDescripcion(pp.getDescripcion());
				mov.setCantidad(existenciaActual);
				mov.setFecha(new  Date());
				mov.setHora(hora());

				mov.setIngreso(0.0);
				mov.setEgreso(0.0);
				mov.setVentaSalida(0.0);

				mov.setCostoEntrada(0.0);
				mov.setCostoEntradaAnterior(0.0);
				mov.setCostoSalida(pp.getPrecioCosto());

				mov.setVenta_1(pp.getPrecioVenta_1());
				mov.setVenta_2(pp.getPrecioVenta_2());
				mov.setVenta_3(pp.getPrecioVenta_3());
				mov.setVenta_4(pp.getPrecioVenta_4());

				mov.setVenta_1_anterior(0.0);
				mov.setVenta_2_anterior(0.0);
				mov.setVenta_3_anterior(0.0);
				mov.setVenta_4_anterior(0.0);

				if(cantidad >= pp.getExistencia()) {

					mov.getTipoMovimiento().setId(1);
				}else {
					mov.getTipoMovimiento().setId(2);
				}
				mov.getProducto().setId(pp.getId());
				mov.getFuncionario().setId(idFunc);
				mov.setMarca(pp.getMarca().getDescripcion());
				Concepto co= new Concepto();

				co = conceptoRepository.findById(8).get();	
				mov.setReferencia(co.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(mov);
			} 
		}else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {

				productoRepository.findByActualizaCantidadInventario(cantidad, id);
				Producto pp = productoRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

				mov.setDescripcion(pp.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new  Date());
				mov.setHora(hora());

				mov.setIngreso(0.0);
				mov.setEgreso(0.0);
				mov.setVentaSalida(0.0);

				mov.setCostoEntrada(0.0);
				mov.setCostoEntradaAnterior(0.0);
				mov.setCostoSalida(pp.getPrecioCosto());

				mov.setVenta_1(pp.getPrecioVenta_1());
				mov.setVenta_2(pp.getPrecioVenta_2());
				mov.setVenta_3(pp.getPrecioVenta_3());
				mov.setVenta_4(pp.getPrecioVenta_4());

				mov.setVenta_1_anterior(0.0);
				mov.setVenta_2_anterior(0.0);
				mov.setVenta_3_anterior(0.0);
				mov.setVenta_4_anterior(0.0);

				if(cantidad >= pp.getExistencia()) {
					mov.getTipoMovimiento().setId(1);
				}else {
					mov.getTipoMovimiento().setId(2);
				}
				mov.getProducto().setId(pp.getId());
				mov.getFuncionario().setId(idFunc);
				mov.setMarca(pp.getMarca().getDescripcion());
				Concepto co= new Concepto();

				co = conceptoRepository.findById(8).get();	
				mov.setReferencia(co.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(mov);

				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaCantidadInventario(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
					Producto pro = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();

					movEnt.setDescripcion(pro.getDescripcion());
					movEnt.setCantidad(existenciaActual);
					movEnt.setFecha(new  Date());
					movEnt.setHora(hora());

					movEnt.setIngreso(0.0);
					movEnt.setEgreso(0.0);
					movEnt.setVentaSalida(0.0);

					movEnt.setCostoEntrada(0.0);
					movEnt.setCostoEntradaAnterior(0.0);
					movEnt.setCostoSalida(pro.getPrecioCosto());

					movEnt.setVenta_1(pro.getPrecioVenta_1());
					movEnt.setVenta_2(pro.getPrecioVenta_2());
					movEnt.setVenta_3(pro.getPrecioVenta_3());
					movEnt.setVenta_4(pro.getPrecioVenta_4());

					movEnt.setVenta_1_anterior(0.0);
					movEnt.setVenta_2_anterior(0.0);
					movEnt.setVenta_3_anterior(0.0);
					movEnt.setVenta_4_anterior(0.0);

					if(cantidad >= pro.getExistencia()) {

						movEnt.getTipoMovimiento().setId(1);
					}else {
						movEnt.getTipoMovimiento().setId(2);
					}
					movEnt.getProducto().setId(pro.getId());
					movEnt.getFuncionario().setId(idFunc);
					movEnt.setMarca(pro.getMarca().getDescripcion());
					Concepto conce= new Concepto();

					conce = conceptoRepository.findById(8).get();	

					movEnt.setReferencia(conce.getDescripcion()+" REF.: "+ idCab);
					movEntradaSalidaRepository.save(movEnt);

				}
			}else {
				System.out.println("venta - Producto unitario");
				productoRepository.findByActualizaCantidadInventario(cantidad,id);// actualiza pro compuesto
				Producto pro = productoRepository.getOne(id);
				MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();

				movEnt.setDescripcion(pro.getDescripcion());
				movEnt.setCantidad(cantidad);
				movEnt.setFecha(new  Date());
				movEnt.setHora(hora());

				movEnt.setIngreso(0.0);
				movEnt.setEgreso(0.0);
				movEnt.setVentaSalida(0.0);

				movEnt.setCostoEntrada(0.0);
				movEnt.setCostoEntradaAnterior(0.0);
				movEnt.setCostoSalida(pro.getPrecioCosto());

				movEnt.setVenta_1(pro.getPrecioVenta_1());
				movEnt.setVenta_2(pro.getPrecioVenta_2());
				movEnt.setVenta_3(pro.getPrecioVenta_3());
				movEnt.setVenta_4(pro.getPrecioVenta_4());

				movEnt.setVenta_1_anterior(0.0);
				movEnt.setVenta_2_anterior(0.0);
				movEnt.setVenta_3_anterior(0.0);
				movEnt.setVenta_4_anterior(0.0);

				movEnt.getTipoMovimiento().setId(2);
				movEnt.getProducto().setId(pro.getId());
				movEnt.getFuncionario().setId(idFunc);
				if(cantidad >= pro.getExistencia()) {

					movEnt.getTipoMovimiento().setId(1);
				}else {
					movEnt.getTipoMovimiento().setId(2);
				}

				movEnt.setMarca(pro.getMarca().getDescripcion());
				Concepto conce= new Concepto();

				conce = conceptoRepository.findById(8).get();	

				movEnt.setReferencia(conce.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(movEnt);

			}

		}


	}


}

