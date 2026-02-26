package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.Now;
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

import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.OrdenProduccion;
import com.bisontecfacturacion.security.model.OrdenProduccionDetalle;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.OrdenProduccionDetalleRepository;
import com.bisontecfacturacion.security.repository.OrdenProduccionRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@Transactional
@RestController
@RequestMapping("ordenProduccion")
public class OrdenProduccionController {

	private Reporte report;
	
	@Autowired
	private OrdenProduccionRepository entityRepository;
	

	@Autowired
	private OrgRepository orgRepository;
	
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OrdenProduccionDetalleRepository detalleRepository;
	
	
	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository; 
	
	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<OrdenProduccion> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);

		return listado(entityRepository.getOrdenProduccion(ano, mes, dia));
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{id}")
	public OrdenProduccion getPorId(@PathVariable Integer id){
		OrdenProduccion v=entityRepository.getOrdenProduccionPorId(id);
		//EmpaqueCabecera pre=new EmpaqueCabecera();
		return v;
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
	@RequestMapping(method=RequestMethod.POST, value = "/procesarEntrega")
	public ResponseEntity<?> procesarEntrega(OAuth2Authentication authentication, @RequestBody OrdenProduccion entity){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());

		if(entity.getCantidadEntregada() <= 0) {
			return new ResponseEntity<>(new CustomerErrorType("La cantidad de entrega debe ser mayor a cero para procesar entrega orden producción"), HttpStatus.CONFLICT);
		}
		entity.setEstado("ENTREGADO");
		entity.setFechaEntrega(new Date());
		entityRepository.save(entity);
		entity.getOrdenProduccionDetalles();
		for (OrdenProduccionDetalle det: entity.getOrdenProduccionDetalles()) {
			this.actualizarProductoBaseDescontarCorregido(det.getProducto().getId(), det.getCantidad(), det.getSubTotal(), det.getProducto().getPrecioCosto(), entity.getFuncionario().getId(), "", entity.getId());

		}
		this.actualizarProductoBaseAumentarCorregido(
				entity.getProduccionCostoCabecera().getProducto().getId(), 
				entity.getCantidadEntregada(), 
				entity.getCostoTotalProduccion(), 
				entity.getProduccionCostoCabecera().getProducto().getPrecioVenta_1(), 
				usuario.getFuncionario().getId(), "", 
				entity.getId());

		
		return new ResponseEntity<String>(HttpStatus.CREATED);

	}
	
	public void actualizarProductoBaseDescontarCorregido(int id , double cantidad, double subtotal, double precio, int idFuncionario, String tipo, int idVenta) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		Funcionario f = funcionarioRepository.getIdFuncionario(idFuncionario);
		if(ca!=null) {
			System.out.println("tiene compuesto y actualiza base unica : "+ idFuncionario);
			double cant=0.0;
			cant= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaD(cant, ca.getProductoBase().getId());
			Producto p = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida m = new MovimientoEntradaSalida();

			m.setDescripcion(p.getDescripcion());
			m.setCantidad(cant);
			m.setFecha(new  Date());
			m.setHora(hora());

			m.setIngreso(subtotal);
			m.setEgreso(0.0);
			m.setVentaSalida(subtotal/cant);

			m.setCostoEntrada(0.0);
			m.setCostoEntradaAnterior(0.0);
			m.setCostoSalida(p.getPrecioCosto());

			m.setVenta_1(p.getPrecioVenta_1());
			m.setVenta_2(p.getPrecioVenta_2());
			m.setVenta_3(p.getPrecioVenta_3());
			m.setVenta_4(p.getPrecioVenta_4());

			m.setVenta_1_anterior(0.0);
			m.setVenta_2_anterior(0.0);
			m.setVenta_3_anterior(0.0);
			m.setVenta_4_anterior(0.0);

			m.getTipoMovimiento().setId(2);
			m.getProducto().setId(p.getId());
			m.getFuncionario().setId(idFuncionario);
			m.setMarca(p.getMarca().getDescripcion());
			Concepto c= new Concepto();
			c= conceptoRepository.findById(35).get();
			m.getConcepto().setId(c.getId());
			

			m.setReferencia(c.getDescripcion()+" REF.: "+ idVenta);
			movEntradaSalidaRepository.save(m);
			//venta tipo, subtotl, precio, funcionario id, tipo, idVenta
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("tiene compuesto y actualiza compuesto varios : "+ idFuncionario);

				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				Producto pp = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pp.getDescripcion());
				movv.setCantidad(existenciaActual);
				movv.setFecha(new  Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal/existenciaActual);

				movv.setCostoEntrada(0.0);
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pp.getPrecioCosto());

				movv.setVenta_1(pp.getPrecioVenta_1());
				movv.setVenta_2(pp.getPrecioVenta_2());
				movv.setVenta_3(pp.getPrecioVenta_3());
				movv.setVenta_4(pp.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pp.getId());
				movv.getFuncionario().setId(idFuncionario);
				movv.setMarca(pp.getMarca().getDescripcion());
				Concepto cc= new Concepto();
				
				cc= conceptoRepository.findById(35).get();
				movv.getConcepto().setId(cc.getId());
				
				movv.setReferencia(cc.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(movv);
			}
		}else {
			System.out.println("venta - entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("venta - Producto relacio0nado con un base");
				productoRepository.findByActualizaD(cantidad, id);
				Producto pro = productoRepository.getOne(id);
				MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();

				movEnt.setDescripcion(pro.getDescripcion());
				movEnt.setCantidad(cantidad);
				movEnt.setFecha(new  Date());
				movEnt.setHora(hora());

				movEnt.setIngreso(subtotal);
				movEnt.setEgreso(0.0);
				movEnt.setVentaSalida(subtotal/cantidad);

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
				movEnt.getFuncionario().setId(2);
				movEnt.setMarca(pro.getMarca().getDescripcion());
				Concepto c= new Concepto();
				if(tipo.equals("1")){
					c= conceptoRepository.findById(1).get();
					movEnt.getConcepto().setId(c.getId());
				}else {
					c= conceptoRepository.findById(2).get();
					movEnt.getConcepto().setId(c.getId());
				}

				movEnt.setReferencia(c.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(movEnt);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					System.out.println("venta - producto base relacion");
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
					Producto prod = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida entrada = new MovimientoEntradaSalida();
					entrada.setDescripcion(prod.getDescripcion());
					entrada.setCantidad(existenciaActual);
					entrada.setFecha(new  Date());
					entrada.setHora(hora());

					entrada.setIngreso(subtotal);
					entrada.setEgreso(0.0);
					entrada.setVentaSalida(subtotal/existenciaActual);

					entrada.setCostoEntrada(0.0);
					entrada.setCostoEntradaAnterior(0.0);
					entrada.setCostoSalida(prod.getPrecioCosto());

					entrada.setVenta_1(prod.getPrecioVenta_1());
					entrada.setVenta_2(prod.getPrecioVenta_2());
					entrada.setVenta_3(prod.getPrecioVenta_3());
					entrada.setVenta_4(prod.getPrecioVenta_4());

					entrada.setVenta_1_anterior(0.0);
					entrada.setVenta_2_anterior(0.0);
					entrada.setVenta_3_anterior(0.0);
					entrada.setVenta_4_anterior(0.0);

					entrada.getTipoMovimiento().setId(2);
					entrada.getProducto().setId(prod.getId());
					entrada.getFuncionario().setId(idFuncionario);
					entrada.setMarca(prod.getMarca().getDescripcion());
					Concepto con= new Concepto();
					
						con= conceptoRepository.findById(35).get();
						entrada.getConcepto().setId(con.getId());
					
					entrada.setReferencia(con.getDescripcion()+" REF.: "+ idVenta);
					movEntradaSalidaRepository.save(entrada);
				}
			}else {
				System.out.println("venta - Producto unitario");
				productoRepository.findByActualizaD(cantidad, id);
				Producto p = productoRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

				mov.setDescripcion(p.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new  Date());
				mov.setHora(hora());

				mov.setIngreso(subtotal);
				mov.setEgreso(0.0);
				mov.setVentaSalida(subtotal/cantidad);

				mov.setCostoEntrada(0.0);
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
				mov.getFuncionario().setId(idFuncionario);
				mov.setMarca(p.getMarca().getDescripcion());
				Concepto c= new Concepto();
				
					c= conceptoRepository.findById(35).get();
					mov.getConcepto().setId(c.getId());
				

				mov.setReferencia(c.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(mov);
			}

		}
	}
	
	public void actualizarProductoBaseAumentarCorregido(int id , double cantidad, double subtotal, double precio, int idFuncionario, String tipo, int idVenta) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			Producto pro = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();
			//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
			//			, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
			movEnt.setDescripcion(pro.getDescripcion());
			movEnt.setCantidad(existenciaBase);
			movEnt.setFecha(new  Date());
			movEnt.setHora(hora());

			movEnt.setIngreso(subtotal);
			movEnt.setEgreso(0.0);
			movEnt.setVentaSalida(subtotal/existenciaBase);

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

			movEnt.getTipoMovimiento().setId(1);
			movEnt.getProducto().setId(pro.getId());
			movEnt.getFuncionario().setId(idFuncionario);
			movEnt.setMarca(pro.getMarca().getDescripcion());
			Concepto ccc= new Concepto();
			ccc= conceptoRepository.findById(35).get();
			movEnt.getConcepto().setId(ccc.getId());
			movEnt.setReferencia(ccc.getDescripcion()+" REF.: "+ idVenta);
			movEntradaSalidaRepository.save(movEnt);
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double exi=0.0;
				exi=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(exi, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				Producto produc = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
				//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movv.setDescripcion(produc.getDescripcion());
				movv.setCantidad(exi);
				movv.setFecha(new  Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal/exi);

				movv.setCostoEntrada(0.0);
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(produc.getPrecioCosto());

				movv.setVenta_1(produc.getPrecioVenta_1());
				movv.setVenta_2(produc.getPrecioVenta_2());
				movv.setVenta_3(produc.getPrecioVenta_3());
				movv.setVenta_4(produc.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(1);
				movv.getProducto().setId(produc.getId());
				movv.getFuncionario().setId(idFuncionario);
				movv.setMarca(produc.getMarca().getDescripcion());
				Concepto con= new Concepto();
				con= conceptoRepository.findById(35).get();
				movv.getConcepto().setId(con.getId());
				movv.setReferencia(con.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(movv);
			}
		}else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				productoRepository.findByActualizaA(cantidad, id);
				Producto pp = productoRepository.getOne(id);
				MovimientoEntradaSalida mEntrada = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
				//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				mEntrada.setDescripcion(pp.getDescripcion());
				mEntrada.setCantidad(cantidad);
				mEntrada.setFecha(new  Date());
				mEntrada.setHora(hora());

				mEntrada.setIngreso(subtotal);
				mEntrada.setEgreso(0.0);
				mEntrada.setVentaSalida(subtotal/cantidad);

				mEntrada.setCostoEntrada(0.0);
				mEntrada.setCostoEntradaAnterior(0.0);
				mEntrada.setCostoSalida(pp.getPrecioCosto());

				mEntrada.setVenta_1(pp.getPrecioVenta_1());
				mEntrada.setVenta_2(pp.getPrecioVenta_2());
				mEntrada.setVenta_3(pp.getPrecioVenta_3());
				mEntrada.setVenta_4(pp.getPrecioVenta_4());

				mEntrada.setVenta_1_anterior(0.0);
				mEntrada.setVenta_2_anterior(0.0);
				mEntrada.setVenta_3_anterior(0.0);
				mEntrada.setVenta_4_anterior(0.0);

				mEntrada.getTipoMovimiento().setId(1);
				mEntrada.getProducto().setId(pp.getId());
				mEntrada.getFuncionario().setId(idFuncionario);
				mEntrada.setMarca(pp.getMarca().getDescripcion());
				Concepto conce= new Concepto();
				conce= conceptoRepository.findById(35).get();
				mEntrada.getConcepto().setId(conce.getId());
				mEntrada.setReferencia(conce.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(mEntrada);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
					Producto pro = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
					//					, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
					movEnt.setDescripcion(pro.getDescripcion());
					movEnt.setCantidad(existenciaActual);
					movEnt.setFecha(new  Date());
					movEnt.setHora(hora());

					movEnt.setIngreso(subtotal);
					movEnt.setEgreso(0.0);
					movEnt.setVentaSalida(subtotal/existenciaActual);

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

					movEnt.getTipoMovimiento().setId(1);
					movEnt.getProducto().setId(pro.getId());
					movEnt.getFuncionario().setId(idFuncionario);
					movEnt.setMarca(pro.getMarca().getDescripcion());
					Concepto ccc= new Concepto();
					ccc= conceptoRepository.findById(35).get();
					movEnt.getConcepto().setId(ccc.getId());
					movEnt.setReferencia(ccc.getDescripcion()+" REF.: "+ idVenta);
					movEntradaSalidaRepository.save(movEnt);
				}
			}else {
				System.out.println("Producto unitario");
				productoRepository.findByActualizaA(cantidad, id);
				Producto produc = productoRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
				//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movv.setDescripcion(produc.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new  Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal/cantidad);

				movv.setCostoEntrada(0.0);
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(produc.getPrecioCosto());

				movv.setVenta_1(produc.getPrecioVenta_1());
				movv.setVenta_2(produc.getPrecioVenta_2());
				movv.setVenta_3(produc.getPrecioVenta_3());
				movv.setVenta_4(produc.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(1);
				movv.getProducto().setId(produc.getId());
				movv.getFuncionario().setId(idFuncionario);
				movv.setMarca(produc.getMarca().getDescripcion());
				Concepto con= new Concepto();
				con= conceptoRepository.findById(35).get();
				movv.getConcepto().setId(con.getId());
				movv.setReferencia(con.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(movv);
			}

		}
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
		entity.setFechaEntrega(null);
		entity.setHora(hora());
		 if(entity.getId() !=0) {
			 System.out.println("Entrooo editart	");
			 entityRepository.save(entity);
			 for(OrdenProduccionDetalle det: entity.getOrdenProduccionDetalles()) {
				 System.out.println("iddd detalle que llega: "+det.getId());
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
	
	
	private List<OrdenProduccion>listar(List<OrdenProduccion> obj){
		List<OrdenProduccion> listaRetorno=new ArrayList<>();
		for(OrdenProduccion p:obj){
			OrdenProduccion pre=new OrdenProduccion();
			pre.setId(p.getId());
			pre.getProduccionCostoCabecera().getProducto().setId(p.getProduccionCostoCabecera().getProducto().getId());
			pre.getFuncionario().setPersona(p.getFuncionario().getPersona());
			pre.getFuncionarioA().setPersona(p.getFuncionarioA().getPersona());
			pre.getProduccionCostoCabecera().setId(p.getProduccionCostoCabecera().getId());
			pre.getProduccionCostoCabecera().setProduccionDescripcion(p.getProduccionCostoCabecera().getProduccionDescripcion());
			pre.setFecha(p.getFecha());
			pre.setCantidad(p.getCantidad());
			pre.setFechaEntrega(p.getFechaEntrega());
			pre.setCantidadEntregada(p.getCantidadEntregada());
			pre.setEstado(p.getEstado());
			pre.setCostoTotalProduccion(p.getCostoTotalProduccion());
			listaRetorno.add(pre);
		}
		return listaRetorno;
	}


	@RequestMapping(method=RequestMethod.GET, value="/tipo/{filtro}")
	public List<OrdenProduccion> getAlls(@PathVariable int filtro){
		List<OrdenProduccion> lisRetorno= new ArrayList<OrdenProduccion>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getOrdenProduccionAll());}
		if(filtro==2) { lisRetorno= listar(entityRepository.getOrdenProduccionAllPendiente());}
		if(filtro==3) { lisRetorno= listar(entityRepository.getOrdenProduccionAllEntregado());}

		return lisRetorno;

	}
	@RequestMapping(method=RequestMethod.POST, value="/tipo/{filtro}")
	public List<OrdenProduccion> getAllsPorDescripcion(@RequestBody String descripcion, @PathVariable int filtro){
		List<OrdenProduccion> lisRetorno= new ArrayList<OrdenProduccion>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getOrdenProduccionAllDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==2) { lisRetorno= listar(entityRepository.getOrdenProduccionPendienteDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==3) { lisRetorno= listar(entityRepository.getOrdenProduccionEntregadoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		return lisRetorno;
	}

	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		OrdenProduccion pre= new OrdenProduccion(); 
		pre=entityRepository.getOne(id);
	
		List<OrdenProduccion> listado= new ArrayList<OrdenProduccion>();
		//listado.add(pre);
		if(pre!=null) {
			listado.add(pre);
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		
		
		try {

			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());

			report = new Reporte();
			report.reportPDFDescarga(listado, map, "ReporteOrdenProduccionPdf", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
}
