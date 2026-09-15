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
		} else if (entity.getFuncionarioA().getId()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("Se debe seleccionar el funcionaro Auotrizado"), HttpStatus.CONFLICT);
		} else if (entity.getFuncionarioR().getId()<=0) {
			return new ResponseEntity<>(new CustomerErrorType("El funcionaro registro no debe quedar vacio"), HttpStatus.CONFLICT);
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
			in.getProducto().setCodoriginal(d.getProducto().getCodoriginal());
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
			
			double diferencia = ob.getCantidad() - ob.getExistencia();

			if (diferencia > 0) {
			actualizarProductoBaseAumentar(
					ob.getProducto().getId(), 
					diferencia, 
					usuario.getFuncionario().getId(), 
					idCab);
			} else if (diferencia < 0) {
			actualizarProductoBaseDescontar(
					ob.getProducto().getId(), 
					Math.abs(diferencia), 
					usuario.getFuncionario().getId(), 
					idCab);
			}
		}
		totalDifere= totalCantidad - totalExistencia;
		entity.setTotalCantidadCosto(totalCantidad);
		entity.setTotalExistenciaCosto(totalExistencia);
		entity.setTotalDiferenciaCosto(totalDifere);
		entity.setFechaFin(new Date());
		entity.setEstado(true);

		entityRepository.save(entity);

	}
	
	public void actualizarProductoBaseAumentar(int id , double cantidad,  int idfuncio, int idCab) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			System.out.println("CANT. ACT. : "+existenciaBase);
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
				
			Producto p = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

			mov.setDescripcion(p.getDescripcion());
			mov.setCantidad(existenciaBase);
			mov.setFecha(new  Date());
			mov.setHora(hora());

			mov.setIngreso(0.0);
			mov.setEgreso(0.0);
			mov.setVentaSalida(0.0);

			mov.setCostoEntrada(p.getPrecioCosto());
			mov.setCostoEntradaAnterior(0.0);
			mov.setCostoSalida(p.getPrecioCosto());

			mov.setVenta_1(p.getPrecioVenta_1());
			mov.setVenta_2(p.getPrecioVenta_2());
			mov.setVenta_3(p.getPrecioVenta_3());
			mov.setVenta_4(p.getPrecioVenta_4());

			mov.setVenta_1_anterior(0.0);
			mov.setVenta_2_anterior(0.0);
			mov.setVenta_3_anterior(0.0);
			mov.setVenta_4_anterior(0.0);

			mov.getTipoMovimiento().setId(1);
			mov.getProducto().setId(p.getId());
			mov.getFuncionario().setId(idfuncio);
			mov.setMarca(p.getMarca().getDescripcion());
			Concepto c= new Concepto();
			c= conceptoRepository.findById(8).get();
			mov.getConcepto().setId(c.getId());
			mov.setReferencia(c.getDescripcion()+" REF.: "+ idCab);
			movEntradaSalidaRepository.save(mov);
			
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("compra - entro tiene compusto actuliza todas los compuesto por base relacionado :");
				Double exi=0.0;
				exi=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();

				productoRepository.findByActualizaA(exi, ob.getProductoCompuesto().getId());
				
				System.out.println("CANT. ACT. : "+exi);
				
				Producto pp = productoRepository.getOne(ob.getProductoCompuesto().getId());
				System.out.println("CANT DESPUES: "+pp.getExistencia());
				MovimientoEntradaSalida movEntr = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				System.out.println("compra - entro tiene compusto actuliza todas los compuesto por base relacionado : :"+exi+ " "+pp.getDescripcion());
				movEntr.setDescripcion(pp.getDescripcion());
				movEntr.setCantidad(exi);
				movEntr.setFecha(new  Date());
				movEntr.setHora(hora());

				movEntr.setIngreso(0.0);
				movEntr.setEgreso(0.0);
				movEntr.setVentaSalida(0.0);

				movEntr.setCostoEntrada(pp.getPrecioCosto());
				movEntr.setCostoEntradaAnterior(0.0);
				movEntr.setCostoSalida(pp.getPrecioCosto());

				movEntr.setVenta_1(pp.getPrecioVenta_1());
				movEntr.setVenta_2(pp.getPrecioVenta_2());
				movEntr.setVenta_3(pp.getPrecioVenta_3());
				movEntr.setVenta_4(pp.getPrecioVenta_4());

				movEntr.setVenta_1_anterior(0.0);
				movEntr.setVenta_2_anterior(0.0);
				movEntr.setVenta_3_anterior(0.0);
				movEntr.setVenta_4_anterior(0.0);

				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(pp.getMarca().getDescripcion());
				Concepto ccc= new Concepto();
				ccc= conceptoRepository.findById(8).get();
				movEntr.getConcepto().setId(ccc.getId());

				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(movEntr);
			}
		} else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				productoRepository.findByActualizaA(cantidad, id);
				Producto pp = productoRepository.getOne(id);
				MovimientoEntradaSalida movEntr = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movEntr.setDescripcion(pp.getDescripcion());
				movEntr.setCantidad(cantidad);
				movEntr.setFecha(new  Date());
				movEntr.setHora(hora());
				movEntr.setVentaSalida(0.0);
				movEntr.setEgreso(0.0);
				movEntr.setVentaSalida(0.0);
				movEntr.setCostoEntrada(pp.getPrecioCosto());
				movEntr.setCostoEntradaAnterior(0.0);
				movEntr.setCostoSalida(pp.getPrecioCosto());
				movEntr.setVenta_1(pp.getPrecioVenta_1());
				movEntr.setVenta_2(pp.getPrecioVenta_2());
				movEntr.setVenta_3(pp.getPrecioVenta_3());
				movEntr.setVenta_4(pp.getPrecioVenta_4());
				movEntr.setVenta_1_anterior(0.0);
				movEntr.setVenta_2_anterior(0.0);
				movEntr.setVenta_3_anterior(0.0);
				movEntr.setVenta_4_anterior(0.0);
				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(pp.getMarca().getDescripcion());
				Concepto ccc= new Concepto();
				ccc= conceptoRepository.findById(8).get();
				movEntr.getConcepto().setId(ccc.getId());
				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(movEntr);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());
					
					Producto prod = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//					, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
					
					mov.setDescripcion(prod.getDescripcion());
					mov.setCantidad(existenciaActual);
					mov.setFecha(new  Date());
					mov.setHora(hora());
					mov.setVentaSalida(0.0);
					mov.setEgreso(0.0);
					mov.setVentaSalida(0.0);
					mov.setCostoEntrada(prod.getPrecioCosto());
					mov.setCostoEntradaAnterior(0.0);
					mov.setCostoSalida(prod.getPrecioCosto());
					mov.setVenta_1(prod.getPrecioVenta_1());
					mov.setVenta_2(prod.getPrecioVenta_2());
					mov.setVenta_3(prod.getPrecioVenta_3());
					mov.setVenta_4(prod.getPrecioVenta_4());

					mov.setVenta_1_anterior(0.0);
					mov.setVenta_2_anterior(0.0);
					mov.setVenta_3_anterior(0.0);
					mov.setVenta_4_anterior(0.0);

					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(prod.getId());
					mov.getFuncionario().setId(idfuncio);
					mov.setMarca(prod.getMarca().getDescripcion());
					Concepto conn= new Concepto();
					conn= conceptoRepository.findById(8).get();
					mov.getConcepto().setId(conn.getId());
					mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
					movEntradaSalidaRepository.save(mov);				
				}
			}else {
				System.out.println("Producto unitario");
				productoRepository.findByActualizaA(cantidad, id);
				Producto p = productoRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

				mov.setDescripcion(p.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new  Date());
				mov.setHora(hora());
				mov.setVentaSalida(0.0);
				mov.setEgreso(0.0);
				mov.setVentaSalida(0.0);
				mov.setCostoEntrada(p.getPrecioCosto());
				mov.setCostoEntradaAnterior(0.0);
				mov.setCostoSalida(p.getPrecioCosto());
				mov.setVenta_1(p.getPrecioVenta_1());
				mov.setVenta_2(p.getPrecioVenta_2());
				mov.setVenta_3(p.getPrecioVenta_3());
				mov.setVenta_4(p.getPrecioVenta_4());
				mov.setVenta_1_anterior(0.0);
				mov.setVenta_2_anterior(0.0);
				mov.setVenta_3_anterior(0.0);
				mov.setVenta_4_anterior(0.0);
				mov.getTipoMovimiento().setId(1);
				mov.getProducto().setId(p.getId());
				mov.getFuncionario().setId(idfuncio);
				mov.setMarca(p.getMarca().getDescripcion());
				Concepto conn= new Concepto();
				conn= conceptoRepository.findById(8).get();
				mov.getConcepto().setId(conn.getId());
				mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(mov);
			}
		}
	}
	
	
	public void actualizarProductoBaseDescontar(int id , double cantidad,  int idfuncio, int idCab) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			System.out.println("tiene compuesto y actualiza base unica : ");
			double cant=0.0;
			cant= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaD(cant, ca.getProductoBase().getId());
			Producto p = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
			mov.setDescripcion(p.getDescripcion());
			mov.setCantidad(cant);
			mov.setFecha(new  Date());
			mov.setHora(hora());
			mov.setVentaSalida(0.0);
			mov.setEgreso(0.0);
			mov.setVentaSalida(0.0);
			mov.setCostoEntrada(p.getPrecioCosto());
			mov.setCostoEntradaAnterior(0.0);
			mov.setCostoSalida(p.getPrecioCosto());
			mov.setVenta_1(p.getPrecioVenta_1());
			mov.setVenta_2(p.getPrecioVenta_2());
			mov.setVenta_3(p.getPrecioVenta_3());
			mov.setVenta_4(p.getPrecioVenta_4());
			mov.setVenta_1_anterior(0.0);
			mov.setVenta_2_anterior(0.0);
			mov.setVenta_3_anterior(0.0);
			mov.setVenta_4_anterior(0.0);

			mov.getTipoMovimiento().setId(2);
			mov.getProducto().setId(p.getId());
			mov.getFuncionario().setId(idfuncio);
			mov.setMarca(p.getMarca().getDescripcion());
			Concepto conn= new Concepto();
			conn= conceptoRepository.findById(8).get();
			mov.getConcepto().setId(conn.getId());
			mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
			movEntradaSalidaRepository.save(mov);
			
			//venta tipo, subtotl, precio, funcionario id, tipo, idVenta
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("tiene compuesto y actualiza compuesto varios : ");
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				Producto pro = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(existenciaActual);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);

				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());

				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.getConcepto().setId(cn.getId());

				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movv);
			}
		}else {
			System.out.println("venta - entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("venta - Producto relacio0nado con un base");
				productoRepository.findByActualizaD(cantidad, id);
				Producto pro = productoRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);
				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());
				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());
				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);
				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.getConcepto().setId(cn.getId());
				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
			
				movEntradaSalidaRepository.save(movv);
	
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					System.out.println("venta - producto base relacion");
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
					Producto p = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					mov.setDescripcion(p.getDescripcion());
					mov.setCantidad(existenciaActual);
					mov.setFecha(new  Date());
					mov.setHora(hora());
					mov.setVentaSalida(0.0);
					mov.setEgreso(0.0);
					mov.setVentaSalida(0.0);
					mov.setCostoEntrada(p.getPrecioCosto());
					mov.setCostoEntradaAnterior(0.0);
					mov.setCostoSalida(p.getPrecioCosto());
					mov.setVenta_1(p.getPrecioVenta_1());
					mov.setVenta_2(p.getPrecioVenta_2());
					mov.setVenta_3(p.getPrecioVenta_3());
					mov.setVenta_4(p.getPrecioVenta_4());

					mov.setVenta_1_anterior(0.0);
					mov.setVenta_2_anterior(0.0);
					mov.setVenta_3_anterior(0.0);
					mov.setVenta_4_anterior(0.0);

					mov.getTipoMovimiento().setId(2);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(idfuncio);
					mov.setMarca(p.getMarca().getDescripcion());
					Concepto conn= new Concepto();
					conn= conceptoRepository.findById(8).get();
					mov.getConcepto().setId(conn.getId());
					mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
					movEntradaSalidaRepository.save(mov);
					
				}
			}else {
				System.out.println("venta - Producto unitario");
				productoRepository.findByActualizaD(cantidad, id);
				Producto pro = productoRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);
				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());
				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());
				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);
				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.getConcepto().setId(cn.getId());

				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movv);
			}
			
		}
	}

	


	


}

