package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.DetalleCompra;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.CompraDetalleRepository;
import com.bisontecfacturacion.security.repository.CompraRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;



@RestController
@RequestMapping("compra")
public class CompraController {
	private Reporte report;
	@Autowired
	private CompraRepository entityRepository;
	@Autowired
	private ProveedorRepository proveedorRepository;
	@Autowired
	private CompraDetalleRepository detalleRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;
	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;


	@RequestMapping(method=RequestMethod.GET, value="/compras")
	public List<Compra> get(){
		return entityRepository.findAll();
	}

	@RequestMapping(method=RequestMethod.GET, value="/ultimoDocumento/{id}")
	public Compra getUltimoDocumento(@PathVariable int id){
		return cargarUltimoDocumento(entityRepository.getUltimoDocumento(id));
	}

	public Compra cargarUltimoDocumento(Compra entity) {
		Compra compra=new Compra();
		compra.setTimbrado(entity.getTimbrado());
		compra.setTimbradoInicio(entity.getTimbradoInicio());
		compra.setTimbradoFin(entity.getTimbradoFin());
		compra.setNroDocumento(entity.getNroDocumento());
		return compra; 
	}
	@RequestMapping(method=RequestMethod.GET, value="/totalCompra/{fecha}")
	public Object[] getTotalVenta(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		return entityRepository.findByTotalCompra(ano, mes, dia);
	}


	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<Compra> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Compra> objeto=entityRepository.getCompra(ano, mes, dia);
		List<Compra> venta=new ArrayList<>();
		for(Compra ob:objeto){
			Compra ventas=new Compra();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+" "+ ob.getProveedor().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			//ventas.getDocumento().setId(ob.getDocumento().getId());
			venta.add(ventas);
			System.out.println(ventas.getProveedor().getPersona().getNombre());
		}
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/compraId/{id}")
	public Compra getVentaId(@PathVariable int id){
		return entityRepository.findById(id).get();
		/*
		Compra v=entityRepository.findOne(id);
		Compra venta=new Compra();
		venta.setId(v.getId());
        venta.setTipo(v.getTipo()); 
		venta.setNroDocumento(v.getNroDocumento());
		venta.setTotal(v.getTotal());
		venta.getFuncionario().setId(v.getFuncionario().getId());
		venta.getProveedor().setId(v.getProveedor().getId());
		venta.getProveedor().getPersona().setNombre(v.getProveedor().getPersona().getNombre());
		venta.getProveedor().getPersona().setApellido(v.getProveedor().getPersona().getApellido());
		venta.getDocumento().setId(v.getDocumento().getId());
		venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
		venta.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre());
		return venta;
		 */
	}





	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Compra entity){
		entity.setHora(hora());
		try {
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getProveedor().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else {
				for(int ind=0; ind < entity.getDetalleCompra().size(); ind++) {
					DetalleCompra pro = entity.getDetalleCompra().get(ind);
					if(pro.getCantidad() <= 0) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE PRODUCTO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecioCosto() <= 0){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getSubTotal() <= 0){
						return new ResponseEntity<>(new CustomerErrorType("EL SUBTOTAL DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}


				if(entity.getId() !=0) {
					//entity.setHora(hora());
					entityRepository.save(entity);
					int idVent=entity.getId();
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecioCosto()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
//								this.actualizarProductoBaseCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad());	
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());

//								Producto p = productoRepository.getOne(detalleProducto.getProducto().getId());
//								MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
//
//								mov.setDescripcion(detalleProducto.getDescripcion());
//								mov.setCantidad(detalleProducto.getCantidad());
//								mov.setFecha(new  Date());
//								mov.setHora(hora());
//
//								mov.setVentaSalida(0.0);
//
//								mov.setCostoEntrada(detalleProducto.getPrecioCosto());
//								mov.setEgreso(detalleProducto.getSubTotal());
//								mov.setCostoEntradaAnterior(p.getPrecioCosto());
//
//								mov.setVenta_1(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_2(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_3(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_4(detalleProducto.getPrecioVenta_1());
//
//								mov.setVenta_1_anterior(p.getPrecioVenta_1());
//								mov.setVenta_2_anterior(p.getPrecioVenta_2());
//								mov.setVenta_3_anterior(p.getPrecioVenta_3());
//								mov.setVenta_4_anterior(p.getPrecioVenta_4());
//								mov.getTipoMovimiento().setId(1);
//								mov.getProducto().setId(p.getId());
//								mov.getFuncionario().setId(entity.getFuncionario().getId());
//								mov.setMarca(detalleProducto.getProducto().getMarca().getDescripcion());
//								Concepto c= new Concepto();
//								if(entity.getTipo().equals("CONTADO")){
//									c= conceptoRepository.findById(3).get();	
//								}else {
//									c= conceptoRepository.findById(4).get();
//								}
//
//								mov.setReferencia(c.getDescripcion()+" REF.: "+ idVent);
//
//								movEntradaSalidaRepository.save(mov);
//
//								p.setPrecioCosto(detalleProducto.getPrecioCosto());
//								p.setPrecioVenta_1(detalleProducto.getPrecioVenta_1());
//								p.setPrecioVenta_2(detalleProducto.getPrecioVenta_2());
//								p.setPrecioVenta_3(detalleProducto.getPrecioVenta_3());
//								p.setPrecioVenta_4(detalleProducto.getPrecioVenta_4());
//
//
//								productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
//								productoRepository.save(p);
								//productoRepository.save(p);
							}

						}

						if (entity.getEstado().equals("FACTURAR")) {
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								///detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
						}

					}

				}else {
					entityRepository.save(entity);
					Compra id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					//eliminarDetallePorCabecera(entity.getId());
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
								//this.actualizarProductoBaseCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad());
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());
//costo, subtotal,preVen1,preVen2,preVen3,preVen4, idfuncio, marca, tipo
//								Producto p = productoRepository.getOne(detalleProducto.getProducto().getId());
//								MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
//
//								mov.setDescripcion(p.getDescripcion());
//								mov.setCantidad(detalleProducto.getCantidad());
//								mov.setFecha(new  Date());
//								mov.setHora(hora());
//								mov.setVentaSalida(0.0);
//
//								mov.setCostoEntrada(detalleProducto.getPrecioCosto());
//								mov.setEgreso(detalleProducto.getSubTotal());
//								mov.setCostoEntradaAnterior(p.getPrecioCosto());
//
//								mov.setVenta_1(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_2(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_3(detalleProducto.getPrecioVenta_1());
//								mov.setVenta_4(detalleProducto.getPrecioVenta_1());
//
//								mov.setVenta_1_anterior(p.getPrecioVenta_1());
//								mov.setVenta_2_anterior(p.getPrecioVenta_2());
//								mov.setVenta_3_anterior(p.getPrecioVenta_3());
//								mov.setVenta_4_anterior(p.getPrecioVenta_4());
//								mov.getTipoMovimiento().setId(1);
//								mov.getProducto().setId(p.getId());
//								mov.getFuncionario().setId(entity.getFuncionario().getId());
//								mov.setMarca(detalleProducto.getProducto().getMarca().getDescripcion());
//								Concepto c= new Concepto();
//								if(entity.getTipo().equals("CONTADO")){
//									c= conceptoRepository.findById(3).get();	
//								}else {
//									c= conceptoRepository.findById(4).get();
//								}
//
//								mov.setReferencia(c.getDescripcion()+" REF.: "+ idVent);
//
//								movEntradaSalidaRepository.save(mov);
//
//								p.setPrecioCosto(detalleProducto.getPrecioCosto());
//								p.setPrecioVenta_1(detalleProducto.getPrecioVenta_1());
//								p.setPrecioVenta_2(detalleProducto.getPrecioVenta_2());
//								p.setPrecioVenta_3(detalleProducto.getPrecioVenta_3());
//								p.setPrecioVenta_4(detalleProducto.getPrecioVenta_4()); 
//								//p.getProveedor().setId(entity.getProveedor().getId());
//								//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
//								productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
//								productoRepository.save(p);
//								// productoRepository.save(p);

							}
						}
						if (entity.getEstado().equals("FACTURAR")) {
							for (DetalleCompra detalleProducto : entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
						}
					}
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Error: "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);


	}



	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody DetalleCompra detalle){
		try {
			detalleRepository.deleteById(detalle.getId());
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@Transactional
	@RequestMapping(method=RequestMethod.PUT, value="/prueba/{id}/{idProducto}")
	public void updPro(@PathVariable int id, @PathVariable int idProducto ) {
		productoRepository.updateProveedorId(id, idProducto);
	}


	@RequestMapping(method=RequestMethod.GET, value ="/detalleCompra/{id}")
	public List<DetalleCompra> getDetalleList(@PathVariable int id) {
		List<Object[]> detalle=detalleRepository.getDetallePorCabecera(id);

		List<DetalleCompra> listaRetorno=new ArrayList<>();
		for(Object[] d: detalle) {
			DetalleCompra det=new DetalleCompra();
			det.setId(Integer.parseInt(d[0].toString()));
			det.getProducto().setId(Integer.parseInt(d[1].toString()));
			det.getProducto().setCodbar(d[2].toString());
			det.setDescripcion(d[3].toString());
			det.setCantidad(Double.parseDouble(d[4].toString()));
			det.setPrecioCosto(Double.parseDouble(d[5].toString()));
			det.setIva(d[6].toString());
			det.setSubTotal(Double.parseDouble(d[7].toString()));
			det.setPrecioVenta_1(Double.parseDouble(d[8].toString()));
			det.setPrecioVenta_2(Double.parseDouble(d[9].toString()));
			det.setPrecioVenta_3(Double.parseDouble(d[10].toString()));
			det.setPrecioVenta_4(Double.parseDouble(d[11].toString()));
			det.getCompra().setId(Integer.parseInt(d[12].toString()));
			det.getProducto().getUnidadMedida().setDescripcion(d[13].toString());
			det.getProducto().getMarca().setDescripcion(d[14].toString());

			listaRetorno.add(det);
		}
		return listaRetorno;
	}
	
	public void actualizarProductoBaseAumentarCorregido(int id , double cantidad, double costo, double subtotal, double preVen1, double preVen2, double preVen3, double preVen4, int idfuncio, String marca, String tipo, int idCompra, int idProvee) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			System.out.println("CANT. ACT. : "+existenciaBase);
			
		
			Producto p = productoRepository.getOne(ca.getProductoBase().getId());
			System.out.println("CANT DESPUES: "+p.getExistencia());
			MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
			//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//			, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
			System.out.println("compra - entro tiene compusto actuliza base :"+existenciaBase+ " "+p.getDescripcion());
			
			mov.setDescripcion(p.getDescripcion());
			mov.setCantidad(existenciaBase);
			mov.setFecha(new  Date());
			mov.setHora(hora());
			mov.setVentaSalida(0.0);
			System.out.println("SUBTOTAL: "+subtotal +" EXITE"+ existenciaBase);
			mov.setCostoEntrada(subtotal/existenciaBase);
			mov.setEgreso(subtotal);
			mov.setCostoEntradaAnterior(p.getPrecioCosto());
			System.out.println("venta1: "+preVen1+" venta1: "+preVen2+" venta3: "+preVen3+" venta4: "+preVen4);
			mov.setVenta_1(preVen1/ca.getCantidadAplicacion());
			mov.setVenta_2(preVen2/ca.getCantidadAplicacion());
			mov.setVenta_3(preVen3/ca.getCantidadAplicacion());
			mov.setVenta_4(preVen4/ca.getCantidadAplicacion());

			mov.setVenta_1_anterior(p.getPrecioVenta_1());
			mov.setVenta_2_anterior(p.getPrecioVenta_2());
			mov.setVenta_3_anterior(p.getPrecioVenta_3());
			mov.setVenta_4_anterior(p.getPrecioVenta_4());
			mov.getTipoMovimiento().setId(1);
			mov.getProducto().setId(p.getId());
			mov.getFuncionario().setId(idfuncio);
			mov.setMarca(marca);
			Concepto c= new Concepto();
			if(tipo.equals("CONTADO")){
				c= conceptoRepository.findById(3).get();	
			}else {
				c= conceptoRepository.findById(4).get();
			}

			mov.setReferencia(c.getDescripcion()+" REF.: "+ idCompra);

			movEntradaSalidaRepository.save(mov);
			
			System.out.println("COSTO : "+costo+ " aplicacion: "+ca.getCantidadAplicacion());
			p.setPrecioCosto(subtotal/existenciaBase);
			p.setPrecioVenta_1(preVen1/ca.getCantidadAplicacion());
			p.setPrecioVenta_2(preVen2/ca.getCantidadAplicacion());
			p.setPrecioVenta_3(preVen3/ca.getCantidadAplicacion());
			p.setPrecioVenta_4(preVen4/ca.getCantidadAplicacion()); 
			System.out.println("Existemcia ************"+ p.getExistencia());
			//p.getProveedor().setId(entity.getProveedor().getId());
			//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
			productoRepository.updateProveedorId(idProvee, p.getId());
			productoRepository.save(p);
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("compra - entro tiene compusto actuliza todas los compuesto por base relacionado :");
				Double exi=0.0;
				exi=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				
				
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
				movEntr.setVentaSalida(0.0);
				System.out.println("SUBTOTAL: "+subtotal +" EXITE"+ exi);

				movEntr.setCostoEntrada(subtotal/exi);
				movEntr.setEgreso(subtotal);
				movEntr.setCostoEntradaAnterior(pp.getPrecioCosto());

				movEntr.setVenta_1((preVen1/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				movEntr.setVenta_2((preVen2/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				movEntr.setVenta_3((preVen3/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				movEntr.setVenta_4((preVen4/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());

				movEntr.setVenta_1_anterior(pp.getPrecioVenta_1());
				movEntr.setVenta_2_anterior(pp.getPrecioVenta_2());
				movEntr.setVenta_3_anterior(pp.getPrecioVenta_3());
				movEntr.setVenta_4_anterior(pp.getPrecioVenta_4());
				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(marca);
				
				Concepto ccc= new Concepto();
				if(tipo.equals("CONTADO")){
					ccc= conceptoRepository.findById(3).get();	
				}else {
					ccc= conceptoRepository.findById(4).get();
				}

				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCompra);
				movEntradaSalidaRepository.save(movEntr);
				
				System.out.println("COSTO : "+costo+ " aplicacion: "+ob.getCantidadAplicacion());
				pp.setPrecioCosto(subtotal/exi);
				pp.setPrecioVenta_1((preVen1/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_2((preVen2/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_3((preVen3/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_4((preVen4/ca.getCantidadAplicacion())*ob.getCantidadAplicacion()); 
				
				//p.getProveedor().setId(entity.getProveedor().getId());
				//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
				System.out.println("Existemcia forddd ************"+ pp.getExistencia());
				productoRepository.updateProveedorId(idProvee, pp.getId());
				productoRepository.save(pp);
				productoRepository.findByActualizaA(exi, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				
			}
		}else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				
				
				Producto pp = productoRepository.getOne(id);
				MovimientoEntradaSalida movEntr = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movEntr.setDescripcion(pp.getDescripcion());
				movEntr.setCantidad(cantidad);
				movEntr.setFecha(new  Date());
				movEntr.setHora(hora());
				movEntr.setVentaSalida(0.0);

				movEntr.setCostoEntrada(subtotal/cantidad);
				movEntr.setEgreso(subtotal);
				movEntr.setCostoEntradaAnterior(pp.getPrecioCosto());

				movEntr.setVenta_1(preVen1);
				movEntr.setVenta_2(preVen2);
				movEntr.setVenta_3(preVen3);
				movEntr.setVenta_4(preVen4);

				movEntr.setVenta_1_anterior(pp.getPrecioVenta_1());
				movEntr.setVenta_2_anterior(pp.getPrecioVenta_2());
				movEntr.setVenta_3_anterior(pp.getPrecioVenta_3());
				movEntr.setVenta_4_anterior(pp.getPrecioVenta_4());
				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(marca);
				
				Concepto ccc= new Concepto();
				if(tipo.equals("CONTADO")){
					ccc= conceptoRepository.findById(3).get();	
				}else {
					ccc= conceptoRepository.findById(4).get();
				}

				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCompra);
				movEntradaSalidaRepository.save(movEntr);
				
				pp.setPrecioCosto(subtotal/cantidad);
				pp.setPrecioVenta_1(preVen1);
				pp.setPrecioVenta_2(preVen2);
				pp.setPrecioVenta_3(preVen3);
				pp.setPrecioVenta_4(preVen4); 
				//p.getProveedor().setId(entity.getProveedor().getId());
				//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
				productoRepository.updateProveedorId(idProvee, pp.getId());
				productoRepository.save(pp);
				productoRepository.findByActualizaA(cantidad, id);
			
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					
					Producto p = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//					, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
					
					mov.setDescripcion(p.getDescripcion());
					mov.setCantidad(existenciaActual);
					mov.setFecha(new  Date());
					mov.setHora(hora());
					mov.setVentaSalida(0.0);

					mov.setCostoEntrada(subtotal/existenciaActual);
					mov.setEgreso(subtotal);
					mov.setCostoEntradaAnterior(p.getPrecioCosto());

					mov.setVenta_1(preVen1 * ob.getCantidadAplicacion());
					mov.setVenta_2(preVen2 * ob.getCantidadAplicacion());
					mov.setVenta_3(preVen3 * ob.getCantidadAplicacion());
					mov.setVenta_4(preVen4 * ob.getCantidadAplicacion());

					mov.setVenta_1_anterior(p.getPrecioVenta_1());
					mov.setVenta_2_anterior(p.getPrecioVenta_2());
					mov.setVenta_3_anterior(p.getPrecioVenta_3());
					mov.setVenta_4_anterior(p.getPrecioVenta_4());
					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(idfuncio);
					mov.setMarca(marca);
					Concepto c= new Concepto();
					if(tipo.equals("CONTADO")){
						c= conceptoRepository.findById(3).get();	
					}else {
						c= conceptoRepository.findById(4).get();
					}

					mov.setReferencia(c.getDescripcion()+" REF.: "+ idCompra);

					movEntradaSalidaRepository.save(mov);
					
					p.setPrecioCosto(subtotal / existenciaActual);
					p.setPrecioVenta_1(preVen1 * ob.getCantidadAplicacion());
					p.setPrecioVenta_2(preVen2 * ob.getCantidadAplicacion());
					p.setPrecioVenta_3(preVen3 * ob.getCantidadAplicacion());
					p.setPrecioVenta_4(preVen4 * ob.getCantidadAplicacion()); 
					//p.getProveedor().setId(entity.getProveedor().getId());
					//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
					productoRepository.updateProveedorId(idProvee, p.getId());
					productoRepository.save(p);
					productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
										
				}
			}else {
				System.out.println("Producto unitario");
				
				
				Producto p = productoRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				
				mov.setDescripcion(p.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new  Date());
				mov.setHora(hora());
				mov.setVentaSalida(0.0);

				mov.setCostoEntrada(subtotal/cantidad);
				mov.setEgreso(subtotal);
				mov.setCostoEntradaAnterior(p.getPrecioCosto());

				mov.setVenta_1(preVen1);
				mov.setVenta_2(preVen2);
				mov.setVenta_3(preVen3);
				mov.setVenta_4(preVen4);

				mov.setVenta_1_anterior(p.getPrecioVenta_1());
				mov.setVenta_2_anterior(p.getPrecioVenta_2());
				mov.setVenta_3_anterior(p.getPrecioVenta_3());
				mov.setVenta_4_anterior(p.getPrecioVenta_4());
				mov.getTipoMovimiento().setId(1);
				mov.getProducto().setId(p.getId());
				mov.getFuncionario().setId(idfuncio);
				mov.setMarca(marca);
				Concepto c= new Concepto();
				if(tipo.equals("CONTADO")){
					c= conceptoRepository.findById(3).get();	
				}else {
					c= conceptoRepository.findById(4).get();
				}

				mov.setReferencia(c.getDescripcion()+" REF.: "+ idCompra);

				movEntradaSalidaRepository.save(mov);
				

				p.setPrecioCosto(costo);
				p.setPrecioVenta_1(preVen1);
				p.setPrecioVenta_2(preVen2);
				p.setPrecioVenta_3(preVen3);
				p.setPrecioVenta_4(preVen4); 
				//p.getProveedor().setId(entity.getProveedor().getId());
				//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
				productoRepository.updateProveedorId(idProvee, p.getId());
				productoRepository.save(p);
				productoRepository.findByActualizaA(cantidad, id);
				
			}
			
		}
	}


	public void actualizarProductoBaseCorregidosss(int id , double cantidad) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			
			List<ProductoCardex> listadoDeCompuestoPorBase = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex obCompuesto: listadoDeCompuestoPorBase) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion())/obCompuesto.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, obCompuesto.getProductoCompuesto().getId());// actualiza pro compuesto
			}
			
		}else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				productoRepository.findByActualizaA(cantidad, id);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				//actualiza producto base
				for(ProductoCardex ob: list) {
					System.out.println("Producto relacio0nado con un base relacionado.!!!");
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				}
			}else {
				System.out.println("Producto unitario");
				productoRepository.findByActualizaA(cantidad, id);
			}

		}


	}
	@RequestMapping(value="/resumenCompraTodosRangoFecha/{fechaInicio}/{fechaFin}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenCompraTodos(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaInicio, @PathVariable String fechaFin) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		List<Compra> listado = new ArrayList<>();
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			fecI = formater.parse(fechaInicio);
			Date fecF=formater.parse(fechaFin);
			Date fechaFi = sumarDia(fecF, 24);
			List<Object []> obb =entityRepository.getResumenCompraFechaDetallado(fecI, fechaFi);
			for(Object[] ob: obb) {
				Compra venta = new  Compra();
				venta.getProveedor().getPersona().setNombre(ob[1].toString());
				venta.getFuncionario().getPersona().setNombre(ob[0].toString());
				venta.getDocumento().setDescripcion(ob[2].toString());
				venta.setNroDocumento(ob[3].toString());
				venta.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[4].toString()));
				venta.setTotal(Double.parseDouble(ob[5].toString()));	
				venta.setTipo(ob[6].toString());
				listado.add(venta);
			}
			Object [][] objeto=entityRepository.getResumenCompraFecha(fecI, fechaFi);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fechaFi);
			map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
			map.put("totalVenta", 0.0);
			map.put("totalUtilidad", 0.0);
			//map.put("proveedor", proveedor.getPersona().getNombre()+ " "+ proveedor.getPersona().getApellido());
			Compra com= new  Compra();
			report = new Reporte();
			report.reportPDFDescarga(listado, map, "ReporteCompraRango", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value="/resumenCompra/{idProveedor}/{fechaInicio}/{fechaFin}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenCompra(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int idProveedor, @PathVariable String fechaInicio, @PathVariable String fechaFin, @PathVariable int detallado) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Proveedor proveedor= proveedorRepository.getOne(idProveedor);
		List<Compra> listado = new ArrayList<>();
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			fecI = formater.parse(fechaInicio);
			Date fecF=formater.parse(fechaFin);
			Date fechaFi = sumarDia(fecF, 24);
			Object [][] objeto=entityRepository.getResumenCompraRagoFechaProveedor(idProveedor, fecI, fechaFi);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fechaFi);
			map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
			map.put("totalVenta", 0.0);
			map.put("totalUtilidad", 0.0);
			map.put("proveedor", proveedor.getPersona().getNombre()+ " "+ proveedor.getPersona().getApellido());
			Compra com= new  Compra();
			report = new Reporte();
			if(detallado==1) {
				System.out.println("ENTROO FALSE");
				com.getProveedor().getPersona().setNombre("");
				com.getFuncionario().getPersona().setNombre("");
				com.getDocumento().setDescripcion("");
				com.setNroDocumento("");
				com.setFechaFactura(FechaUtil.convertirFechaStringADateUtil("2020-11-11"));
				com.setTotal(1200.0);
				listado.add(com);
				//report.reportPDFDescarga(listado, map, "ReporteCompraRangoPorProveedor", response);
				report.reportPDFImprimir(listado, map, "ReporteCompraRangoPorProveedor", "Microsoft Print to PDF");
			}
			if(detallado==2) {
				System.out.println("ENTROO TRUE");
				List<Object []> obb= entityRepository.getResumenCompraRagoFechaProveedorDetallado(idProveedor, fecI, fechaFi);
				for(Object[] ob: obb) {
					Compra venta = new  Compra();
					venta.getProveedor().getPersona().setNombre(ob[1].toString());
					venta.getFuncionario().getPersona().setNombre(ob[0].toString());
					venta.getDocumento().setDescripcion(ob[2].toString());
					venta.setNroDocumento(ob[3].toString());
					venta.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[4].toString()));
					venta.setTotal(Double.parseDouble(ob[5].toString()));	
					venta.setTipo(ob[6].toString());
					listado.add(venta);
				}
				report.reportPDFDescarga(listado, map, "ReporteCompraRangoPorProveedorDetallado", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoPorProveedorDetallado", "Microsoft Print to PDF");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	@RequestMapping(method=RequestMethod.GET, value="/libroIva/{fechaInicio}/{fechaFin}")
	public List<Compra> getLibroIva(@PathVariable String fechaInicio, @PathVariable String fechaFin){
		try {
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI=formater.parse(fechaInicio);
			Date fecF=formater.parse(fechaFin);
			Date fechaFi = sumarDia(fecF, 24);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return null;
	}

} 