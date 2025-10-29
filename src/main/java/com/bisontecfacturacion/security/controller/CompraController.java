package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarDetalle;
import com.bisontecfacturacion.security.model.DetalleCompra;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaChicaRepository;
import com.bisontecfacturacion.security.repository.CompraDetalleRepository;
import com.bisontecfacturacion.security.repository.CompraRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarCabeceraRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarDetalleRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
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
	private CuentaPagarCabeceraRepository cuentaPagarCabeceraRepository;
	
	@Autowired
	private CuentaPagarDetalleRepository cuentaPagarDetalleRepository;
	
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
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private AperturaCajaRepository aperturaRepository;
	
	@Autowired
	private CajaChicaRepository cajaChicaRepository;

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

	@RequestMapping(method=RequestMethod.GET, value="/rastreoProductoProveedorOrderPrecio/{idProducto}")
	public List<DetalleCompra> getRastreoProductoProveedorOrderPrecio(@PathVariable int idProducto){
			return cargarRastreo(this.entityRepository.getRastreoProductoProveedorOrderPrecio(idProducto));
	}
	@RequestMapping(method=RequestMethod.GET, value="/rastreoProductoProveedorOrderFecha/{idProducto}")
	public List<DetalleCompra> getRastreoProductoProveedorOrderFecha(@PathVariable int idProducto){
			return cargarRastreo(this.entityRepository.getRastreoProductoProveedorOrderPrecio(idProducto));
	}
	private List<DetalleCompra> cargarRastreo(List<Object[]> obj) {
		List<DetalleCompra> listRetorno= new ArrayList<DetalleCompra>();
		DetalleCompra det= null;
		for(Object o[]: obj) {
			det= new DetalleCompra();
			det.setId(Integer.parseInt(o[0].toString()));
			det.setDescripcion(o[1].toString());
			det.setCantidad(Double.parseDouble(o[2].toString()));
			det.getCompra().getProveedor().getPersona().setNombre(o[3].toString());
			det.setPrecioCosto(Double.parseDouble(o[4].toString()));
			det.getCompra().setValorCotizacion(Double.parseDouble(o[5].toString()));
			if(o[6].toString()==null) {
				det.getCompra().setFechaFactura(null);
			}else {
				det.getCompra().setFechaFactura(FechaUtil.convertirFechaStringADateUtil(o[6].toString()));
			}
			listRetorno.add(det);
		}
		return listRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{filtro}")
	public List<Compra> getAllsFiltro(@PathVariable String filtro){
		
		List<Compra> objeto=entityRepository.getCompraAllFiltroProveedor("%"+Utilidades.eliminaCaracterIzqDer(filtro.toUpperCase())+"%");
		List<Compra> venta=new ArrayList<>();
		for(Compra ob:objeto){
			Compra ventas=new Compra();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+" "+ ob.getProveedor().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setFechaFactura(ob.getFechaFactura());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			//ventas.getDocumento().setId(ob.getDocumento().getId());
			venta.add(ventas);
			System.out.println(ventas.getProveedor().getPersona().getNombre());
		}
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<Compra> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Compra> objeto=entityRepository.getCompraFechaRegistro(ano, mes, dia);
		List<Compra> venta=new ArrayList<>();
		for(Compra ob:objeto){
			Compra ventas=new Compra();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+" "+ ob.getProveedor().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setFechaFactura(ob.getFechaFactura());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			//ventas.getDocumento().setId(ob.getDocumento().getId());
			venta.add(ventas);
			System.out.println(ventas.getProveedor().getPersona().getNombre());
		}
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/fechaRegistro/{fecha}")
	public List<Compra> getAllsFechaRegistro(@PathVariable String fecha){
		System.out.println("filtro por fecha compra");
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Compra> objeto=entityRepository.getCompraFechaRegistro(ano, mes, dia);
		System.out.println("lista size: "+objeto.size());
		List<Compra> listaRetorno=new ArrayList<>();
		for(Compra ob:objeto){
			Compra ventas=new Compra();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+" "+ ob.getProveedor().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setFechaFactura(ob.getFechaFactura());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			//ventas.getDocumento().setId(ob.getDocumento().getId());
			listaRetorno.add(ventas);
			System.out.println(ventas.getProveedor().getPersona().getNombre());
		}
		return listaRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/fechaFactura/{fecha}")
	public List<Compra> getAllsFechaFactura(@PathVariable String fecha){
		System.out.println("filtro por fecha compra");
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Compra> objeto=entityRepository.getCompraFechaFactura(ano, mes, dia);
		System.out.println("lista size: "+objeto.size());
		List<Compra> listaRetorno=new ArrayList<>();
		for(Compra ob:objeto){
			Compra ventas=new Compra();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+" "+ ob.getProveedor().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setFechaFactura(ob.getFechaFactura());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			//ventas.getDocumento().setId(ob.getDocumento().getId());
			listaRetorno.add(ventas);
			System.out.println(ventas.getProveedor().getPersona().getNombre());
		}
		return listaRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/compraId/{id}")
	public Compra getCompraId(@PathVariable int id){
		
		
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
	@RequestMapping(method=RequestMethod.GET, value="/compraId/lista/{id}")
	public Compra getCompraIdLista(@PathVariable int id){
		
		return entityRepository.getCompraId(id);
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	private ResponseEntity<CustomerErrorType> error(String mensaje) {
	    return new ResponseEntity<>(new CustomerErrorType(mensaje), HttpStatus.CONFLICT);
	}
	private ResponseEntity<?> validarCompra(Compra compra) {
	    // 🔹 Validar cabecera
	    if (compra.getFuncionario() == null || compra.getFuncionario().getId() == 0) {
	        return error("EL FUNCIONARIO NO DEBE QUEDAR VACÍO!");
	    }
	    if (compra.getDocumento() == null || compra.getDocumento().getId() == 0) {
	        return error("EL DOCUMENTO NO DEBE QUEDAR VACÍO!");
	    }
	    if (compra.getProveedor() == null || compra.getProveedor().getId() == 0) {
	        return error("EL PROVEEDOR NO DEBE QUEDAR VACÍO!");
	    }

	    if ("FACTURADO".equalsIgnoreCase(compra.getEstado()) && compra.getFechaFactura() == null) {
	        return error("LA FECHA DE LA FACTURA NO DEBE QUEDAR VACÍA!");
	    }

	    if (compra.getConcepto() == null || compra.getConcepto().getId() == 0) {
	        return error("EL CONCEPTO SE DEBE CARGAR ANTES DE GUARDAR COMPRA!");
	    }

	    // 🔹 Validar detalles
	    if (compra.getDetalleCompra() == null || compra.getDetalleCompra().isEmpty()) {
	        return error("LA COMPRA DEBE CONTENER AL MENOS UN DETALLE!");
	    }

	    int index = 1;
	    double sumaDetalles = 0;
	    for (DetalleCompra d : compra.getDetalleCompra()) {
	        if (d.getCantidad() <= 0) {
	            return error("LA CANTIDAD DEL ITEM N° " + index + " NO DEBE SER <= 0!");
	        }
	        if (d.getDescripcion() == null || d.getDescripcion().trim().isEmpty()) {
	            return error("LA DESCRIPCIÓN DEL ITEM N° " + index + " NO DEBE QUEDAR VACÍA!");
	        }
	        if (d.getPrecioCosto() <= 0) {
	            return error("EL PRECIO DEL ITEM N° " + index + " NO DEBE SER <= 0!");
	        }
	        if (d.getSubTotal() <= 0) {
	            return error("EL SUBTOTAL DEL ITEM N° " + index + " NO DEBE SER <= 0!");
	        }
	        if (d.getProducto() == null || d.getProducto().getId() == 0) {
	            return error("EL PRODUCTO DEL ITEM N° " + index + " NO ES VÁLIDO!");
	        }
	        System.out.println("deta subtotal "+index+ " : "+d.getSubTotal());
	        System.out.println(" total "+index+ " : "+compra.getTotal());

	        sumaDetalles += d.getSubTotal();
	        index++;
	    }
	    for (DetalleCompra d : compra.getDetalleCompra()) {
	        Producto p = productoRepository.findById(d.getProducto().getId())
	                       .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + d.getProducto().getId()));
	        d.setProducto(p);
	    }

	    // 🔹 Validar coherencia de total (opcional)
	    if (compra.getTotal() <= 0) {
	        return error("EL TOTAL DE LA COMPRA NO DEBE SER <= 0!");
	    }
	    System.out.println("Total cabecera sin convertir: " + compra.getTotal());
	    System.out.println("Suma detalles sin convertir: " + sumaDetalles);
	    BigDecimal totalCabecera = BigDecimal.valueOf(compra.getTotal()).setScale(2, RoundingMode.HALF_UP);
	    BigDecimal totalDetalles = BigDecimal.valueOf(sumaDetalles).setScale(2, RoundingMode.HALF_UP);

	    // margen máximo permitido (por ejemplo 1 guaraní)
	    BigDecimal margen = new BigDecimal("1000");

	    if (totalCabecera.subtract(totalDetalles).abs().compareTo(margen) > 0) {
	        return error("EL TOTAL DE LA COMPRA (" + totalCabecera +
	                     ") NO COINCIDE CON LA SUMA DE LOS DETALLES (" + totalDetalles + ")");
	    }

	    return null; // ✅ Validación correcta
	}

	private ResponseEntity<?> validarCaja(OperacionCaja operacionCaja) {
		CajaChica cajaChica = new CajaChica();
		AperturaCaja aper = new AperturaCaja();
		if(operacionCaja.getTipo().equals("T-A")) {
			System.out.println("EJECUTO OPERACION PAERTURA VALIDA");
			System.out.println("IDAPERTURA A CONSULTAR: "+operacionCaja.getAperturaCaja().getId());
			//operacionCaja.getConcepto().getId() aca le mando el id de la caja apertura
			aper = aperturaRepository.getAperturaCajaPorIdCaja(operacionCaja.getAperturaCaja().getId());
			if(aper==null) {
				System.out.println("entrooo null caja chiac");
				return error("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!");
			}else {
				if(operacionCaja.getConcepto().getId()==0) {
					return error("LA OPERACION DEBE ESTAR ASIGNADO UN CONCEPTO PARA PODER PROCESAR!");
				}else if(operacionCaja.getMonto()<=0) {
					return error("EL MONTO DE LA OPERACION DEBE SER MAYOR A ZERO!");
				}else if((aper.getSaldoActual()) < operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==1) {
					return error("EL EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!");
				}else if((aper.getSaldoActualCheque()) < operacionCaja.getMonto()&& operacionCaja.getTipoOperacion().getId()==2){
					return error("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!");
				}else if((aper.getSaldoActualTarjeta())< operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==3){
					return error("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!");
				}else {
					
				}
			}
	
		}else if(operacionCaja.getTipo().equals("T-C")){
			return error("OPERACION NO PERMITDO PAGO DE COMPRA POR CAJA CHICA!");
		}
	    return null;
	}
	private ResponseEntity<?> validarCuentaPagar(CuentaPagarCabecera entity) {
		if(entity.getConcepto().getId() == 0) {
            return error("EL CONCEPTO DE LA CUENTA NO DEBE QUEDAR VACIO!");
        }else if(entity.getFuncionario().getId() == 0) {
	            return error("EL FUNCIONARIO NO DEBE QUEDAR VACIO!");
	     } else if(entity.getTipoPlazo().getId() == 0) {
	            return error("EL TIPO PLAZO NO DEBE QUEDAR VACIO!");
	     } else if(entity.getProveedor().getId()==0) {
	    	  	return error("EL PROVEEDOR NO DEBE QUEDAR VACIO!");
	     } else if(entity.getTotal() <=0 ) {
	    	 	return error("EL TOTAL DE LA CUENTA DEBE SER MAYOR A CERO!");
	     } else if(entity.getFraccionCuota() <=0) {
	    	 	return error("EL NÚMERO DE CUOTA O FRACCIÓN DEBE SER MAYOR A CERO!");
	     } else if(entity.getCuentaPagarDetalle().size() <=0){
             return error("DEBES AGREGAR POR LO MENO UN DETALLE DE CUENTA A PAGAR!");
	     } 
     	  for(int ind=0; ind < entity.getCuentaPagarDetalle().size(); ind++) {
               CuentaPagarDetalle det = entity.getCuentaPagarDetalle().get(ind);
               if(det.getNumeroCuota() <=0 ) {
                   return error("EL NÚMERO DE CUOTA DEL DETALLE ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!");
               }else if(det.getMonto() <= 0){
                   return error("EL MONTO DE LA CUOTA DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!");
               }else if(det.getSubTotal() <= 0){
                   return error("EL SUBTOTAL DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!");
               }else if(det.getFechaVencimiento() == null) {
                   return error("LA FECHA DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!");  
               }
           }
      
	    // más validaciones...
	    return null;
	}
	
	@Transactional
	public void procesarCuentaPagar(Compra compra, CuentaPagarCabecera ent){
		if(ent.getId() !=0) {
			ent.getCompra().setId(compra.getId());
			ent.setFecha(new Date());
			ent.setHora(hora());
			CuentaPagarCabecera saveCuenta = cuentaPagarCabeceraRepository.save(ent);
            if(ent.getCuentaPagarDetalle().size()>0){
                for(CuentaPagarDetalle det: ent.getCuentaPagarDetalle()) {
              	  	CuentaPagarDetalle detalle=new CuentaPagarDetalle();
                    detalle.setId(det.getId());
                    detalle.setNumeroCuota(det.getNumeroCuota());
                    detalle.setMonto(det.getMonto());
                    detalle.setSubTotal(det.getSubTotal());
                    detalle.setFechaVencimiento(det.getFechaVencimiento());
                    detalle.setEstado(det.isEstado());
                    detalle.setImporte(det.getImporte());
                    detalle.getCuentaPagarCabecera().setId(saveCuenta.getId());
                    cuentaPagarDetalleRepository.save(detalle);
                    
                }
            }else{}
  	  } else {
  		ent.getCompra().setId(compra.getId());
		ent.setFecha(new Date());
		ent.setHora(hora());
		CuentaPagarCabecera saveCuenta = cuentaPagarCabeceraRepository.save(ent);
        //eliminarDetallePorCabecera(entity.getId());
        if(ent.getCuentaPagarDetalle().size()>0){
            for(CuentaPagarDetalle det: ent.getCuentaPagarDetalle()) {
          	  CuentaPagarDetalle detalle=new CuentaPagarDetalle();
          	  	detalle.setId(det.getId());
                detalle.setNumeroCuota(det.getNumeroCuota());
                detalle.setMonto(det.getMonto());
                detalle.setSubTotal(det.getSubTotal());
                detalle.setFechaVencimiento(det.getFechaVencimiento());
                detalle.setEstado(det.isEstado());
                detalle.setImporte(det.getImporte());
                detalle.getCuentaPagarCabecera().setId(saveCuenta.getId());
                cuentaPagarDetalleRepository.save(detalle);
            }
        }else {}
  	  }
	}
	@Transactional
	public void procesarOperacionCaja(Compra ent, OperacionCaja ope) {
		if(ope.getTipo().equals("T-A")) {
			System.out.println("EJECUTO OPERACION PAERTURA PROCEDIMINETO");
			Concepto c= new Concepto();
			c= conceptoRepository.findById(ope.getConcepto().getId()).get();//compra contado
			ope.setMotivo(c.getDescripcion()+" REF.: "+ent.getId());
			ope.getAperturaCaja().setId(ope.getAperturaCaja().getId());//id aperturarecibido desdecliente
			ope.setTipo("SALIDA");
			//ope.setMonto(ent.getEntrega());
			//ope.getConcepto().setId(ent.getConcepto().getId());
			OperacionCaja saveOperacion = operacionCajaRepository.save(ope);
			System.out.println("OPERACION ID: "+ope.getTipoOperacion().getId());
			System.out.println("APERTURA ID: "+ope.getAperturaCaja().getId());
			System.out.println("CONCEPTO ID: "+ope.getConcepto().getId());
			if (saveOperacion.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(saveOperacion.getAperturaCaja().getId(), saveOperacion.getMonto());
				System.out.println("DEBERIA CTUALZIAR EFECTIVO ACTUAL EN CAJA");
			}
			if (saveOperacion.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(saveOperacion.getAperturaCaja().getId(), saveOperacion.getMonto());
			}
			if (saveOperacion.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(saveOperacion.getAperturaCaja().getId(), saveOperacion.getMonto());
			}
			entityRepository.findByActualizarCompraOperacion(ent.getId(), saveOperacion.getId());
		}
		if(ope.getTipo().equals("T-C")){
			System.out.println("EJECUTO OPERACION CAJACHICA PROCEDIMINETO");
		}
	}
	@Transactional
	public Compra guardarCompraYDetalles(Compra enti) {
	    // 1. Guardar la cabecera primero
	    if(enti.getId() !=0) {
	    	entityRepository.save(enti);
			double total10=0, total5=0, totalDescuento=0;
			if(enti.getDetalleCompra().size()>0){
				if (enti.getEstado().equals("FACTURADO")) {
					enti.setFecha(new Date());
					 if (enti.getFechaFactura() == null) {
						 enti.setFechaFactura(new Date()); // asigna fecha actual si es null
			            } else {
			            	enti.setFechaFactura(new Date(enti.getFechaFactura().getTime()));
			            }					
			            
					 for(DetalleCompra detalleProducto: enti.getDetalleCompra()) {
						detalleProducto.getCompra().setId(enti.getId());
						//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecioCosto()));
						System.out.println("ivaaa "+detalleProducto.getIva());
						if(detalleProducto.getIva().equals("10 %")) {
							total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
							detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
						}
						if(detalleProducto.getIva().equals("5 %")) {
							total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
							detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
						}
						if(detalleProducto.getIva().equals("Exenta")) {
							detalleProducto.setMontoIva(0.0);
						}
						detalleRepository.save(detalleProducto);
						this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), enti.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), enti.getTipo(), enti.getId(), enti.getProveedor().getId());

					}

				}

				if (enti.getEstado().equals("FACTURAR")) {
					for(DetalleCompra detalleProducto: enti.getDetalleCompra()) {
						detalleProducto.getCompra().setId(enti.getId());
						///detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
						detalleRepository.save(detalleProducto);
					}
				}

			}
			enti.setTotalIvaDies(total10);
			enti.setTotalIvaCinco(total5);
			//entityRepository.save(savedCompra);
			return entityRepository.save(enti);
		}else {
		    Compra savedCompra = entityRepository.save(enti);
			
			double total10=0, total5=0, totalDescuento=0, totalExcenta=0;
			if(savedCompra.getDetalleCompra().size()>0){
				if (savedCompra.getEstado().equals("FACTURADO")) {
					savedCompra.setFecha(new Date());
					savedCompra.setFecha(new Date());
			            if (savedCompra.getFechaFactura() == null) {
			            	savedCompra.setFechaFactura(new Date()); // asigna fecha actual si es null
			            } else {
			            	savedCompra.setFechaFactura(new Date(savedCompra.getFechaFactura().getTime()));
			            }					
			            
			            for(DetalleCompra detalleProducto: savedCompra.getDetalleCompra()) {
						detalleProducto.setId(0);
						detalleProducto.getCompra().setId(savedCompra.getId());
						//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
						System.out.println("ivaaa "+detalleProducto.getIva());
						if(detalleProducto.getIva().equals("10 %")) {
							total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
							detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
						}
						if(detalleProducto.getIva().equals("5 %")) {
							total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
							detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
						}
						if(detalleProducto.getIva().equals("Exenta")) {
							detalleProducto.setMontoIva(0.0);
						}
						detalleRepository.save(detalleProducto);
						//this.actualizarProductoBaseCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad());
						this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), savedCompra.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), savedCompra.getTipo(), savedCompra.getId(), savedCompra.getProveedor().getId());


					}
				}
				if (savedCompra.getEstado().equals("FACTURAR")) {
					for (DetalleCompra detalleProducto : savedCompra.getDetalleCompra()) {
						detalleProducto.getCompra().setId(savedCompra.getId());
						//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
						detalleRepository.save(detalleProducto);
					}
				}
			}	
			savedCompra.setTotalIvaDies(total10);
			savedCompra.setTotalIvaCinco(total5);
			return entityRepository.save(savedCompra);
		}
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(
	        @RequestPart("compra") Compra compra,
	        @RequestPart("operacionCaja") OperacionCaja operacionCaja,
	        @RequestPart("cuentaPagar") CuentaPagarCabecera cuentaPagarCabecera) {

	    try {
	        // 1. Validar compra y detalles
	        ResponseEntity<?> validacionCompra = validarCompra(compra);
	        if (validacionCompra != null) return validacionCompra;

	        // 2. Validar flujo según tipo (contado/crédito)
	        if ("CONTADO".equals(compra.getTipo()) && "FACTURADO".equals(compra.getEstado())) {
	            ResponseEntity<?> validacionCaja = validarCaja(operacionCaja);
	            if (validacionCaja != null) return validacionCaja;
	        } else if ("CREDITO".equals(compra.getTipo())&& "FACTURADO".equals(compra.getEstado())) {
	            if (compra.getEntrega() > 0) {
	                ResponseEntity<?> validacionCajaEntrega = validarCaja(operacionCaja);
	                if (validacionCajaEntrega != null) return validacionCajaEntrega;
	            }
	            ResponseEntity<?> validacionCuenta = validarCuentaPagar(cuentaPagarCabecera);
	            if (validacionCuenta != null) return validacionCuenta;
	        }

	        // 3. Guardar compra y detalles
	        Compra saved = guardarCompraYDetalles(compra);

	        // 4. Procesar movimientos financieros
	        if ("CONTADO".equals(compra.getTipo()) && "FACTURADO".equals(compra.getEstado())) {
	            procesarOperacionCaja(saved, operacionCaja);
	            
	        } else if ("CREDITO".equals(compra.getTipo()) && "FACTURADO".equals(compra.getEstado())) {
	            if (compra.getEntrega() > 0) {
	                procesarOperacionCaja(saved, operacionCaja); // entrega
	            }
	            procesarCuentaPagar(saved, cuentaPagarCabecera);
	        }

	        return new ResponseEntity<>(saved, HttpStatus.CREATED);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(new CustomerErrorType("Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	/*
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestPart("compra") Compra entity,  @RequestPart("operacionCaja") OperacionCaja operacionCaja, @RequestPart("cuentaPagar") CuentaPagarCabecera cuentaPagarCabecera){
		System.out.println();
		
		CajaChica cajaChica = new CajaChica();
		AperturaCaja aper = new AperturaCaja();

		entity.setHora(hora());
		try {
			if(!entity.getNroDocumento().equals("")){
				Optional<Compra> existente = entityRepository.findByProveedorAndNumeroFactura(
				        entity.getProveedor().getId(), entity.getNroDocumento());
				if (existente.isPresent()) {
					return new ResponseEntity<>(new CustomerErrorType("YA EXISTE UNA FACTURA CON ESE NUMERO PARA EL PROVEEDOR SELECCIONADO"), HttpStatus.CONFLICT);
				      
				}
			}
			if(operacionCaja.getTipo().equals("T-C")) {
			
			//operacionCaja.getConcepto().getId() aca le mando el id de la caja chica
			cajaChica=cajaChicaRepository.getCajaChicaPorIdCaja(operacionCaja.getAperturaCaja().getId());
			if(cajaChica==null) {
				System.out.println("entrooo null caja chiac");
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO  NO POSEE CAJA CHICA PARAR PODER FINALIZAR COMPRA!"), HttpStatus.CONFLICT);
			}else {
				if((cajaChica.getMonto()) < operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==1) {
					return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((cajaChica.getMontoCheque()) < operacionCaja.getMonto()&& operacionCaja.getTipoOperacion().getId()==2){
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((cajaChica.getMontoTarjeta())< operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==3){
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else {
					
				}
			}
			}
			if(operacionCaja.getTipo().equals("T-A")) {
			//operacionCaja.getConcepto().getId() aca le mando el id de la caja apertura
			aper=aperturaRepository.getAperturaCajaPorIdCaja(operacionCaja.getConcepto().getId());
			if(aper==null) {
				System.out.println("entrooo null caja chiac");
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
			}else {
				if((aper.getSaldoActual()) < operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==1) {
					return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((aper.getSaldoActualCheque()) < operacionCaja.getMonto()&& operacionCaja.getTipoOperacion().getId()==2){
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((aper.getSaldoActualTarjeta())< operacionCaja.getMonto() && operacionCaja.getTipoOperacion().getId()==3){
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else {
					
				}
			}
	
			}
			
			
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getProveedor().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getEstado().equals("FACTURADO") && entity.getFechaFactura() == null){
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA DE LA FACTURA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getConcepto().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("EL CONCEPTO DE SE DEBE CARGAR ANTES DE GUARDAR COMPRA!"), HttpStatus.CONFLICT);
			}else{
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
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							entity.setFecha(new Date());
							entity.setFechaFactura(new Date(entity.getFechaFactura().getTime()));
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecioCosto()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());

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
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entityRepository.save(entity);
					

				}else {
					entityRepository.save(entity);
					Compra id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							entity.setFecha(new Date());
							entity.setFechaFactura(new Date(entity.getFechaFactura().getTime()));
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.setId(0);
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
								//this.actualizarProductoBaseCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad());
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());


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
					
					
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entityRepository.save(entity);
				}
				if(operacionCaja.getTipo().equals("T-A")) {
					Concepto c= new Concepto();
					c= conceptoRepository.findById(entity.getConcepto().getId()).get();//compra contado
					operacionCaja.setMotivo(c.getDescripcion()+" REF.: "+entity.getId());
					operacionCaja.getAperturaCaja().setId(aper.getId());
					operacionCaja.setTipo("SALIDA");
					operacionCaja.getConcepto().setId(entity.getConcepto().getId());
					operacionCajaRepository.save(operacionCaja);
					if (operacionCaja.getTipoOperacion().getId() == 1) {
						aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(aper.getId(), operacionCaja.getMonto());
					}
					if (operacionCaja.getTipoOperacion().getId() == 2) {
						aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(aper.getId(), operacionCaja.getMonto());
					}
					if (operacionCaja.getTipoOperacion().getId() == 3) {
						aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(aper.getId(), operacionCaja.getMonto());
					}
					entityRepository.findByActualizarCompraOperacion(entity.getId(), operacionCaja.getId());
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Error: "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);


	}
	*/
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value = "/saveCredito")
	public ResponseEntity<?> guardarCompraCredito(@RequestPart("compra") Compra entity,  @RequestPart("cuentaPagar") CuentaPagarCabecera cuentaPagarCabecera){
		System.out.println();
		CajaChica cajaChica = new CajaChica();
		AperturaCaja aper = new AperturaCaja();

		entity.setHora(hora());
		try {
			if(!entity.getNroDocumento().equals("")){
				Optional<Compra> existente = entityRepository.findByProveedorAndNumeroFactura(
				        entity.getProveedor().getId(), entity.getNroDocumento());
				if (existente.isPresent()) {
					return new ResponseEntity<>(new CustomerErrorType("YA EXISTE UNA FACTURA CON ESE NUMERO PARA EL PROVEEDOR SELECCIONADO"), HttpStatus.CONFLICT);
				      
				} 
			}
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getProveedor().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getEstado().equals("FACTURADO") && entity.getFechaFactura() == null){
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA DE LA FACTURA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getConcepto().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("EL CONCEPTO DE SE DEBE CARGAR ANTES DE GUARDAR COMPRA!"), HttpStatus.CONFLICT);
			}else{
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
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							entity.setFecha(new Date());
							entity.setFechaFactura(new Date(entity.getFechaFactura().getTime()));
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecioCosto()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());

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
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entityRepository.save(entity);
					

				}else {
					entityRepository.save(entity);
					Compra id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleCompra().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							entity.setFecha(new Date());
							entity.setFechaFactura(new Date(entity.getFechaFactura().getTime()));
							for(DetalleCompra detalleProducto: entity.getDetalleCompra()) {
								detalleProducto.setId(0);
								detalleProducto.getCompra().setId(idVent);
								//detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								System.out.println("ivaaa "+detalleProducto.getIva());
								if(detalleProducto.getIva().equals("10 %")) {
									total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal()); 
									detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
									detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
								}
								if(detalleProducto.getIva().equals("Exenta")) {
									detalleProducto.setMontoIva(0.0);
								}
								detalleRepository.save(detalleProducto);
								//this.actualizarProductoBaseCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad());
								this.actualizarProductoBaseAumentarCorregido(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getPrecioCosto(), detalleProducto.getSubTotal(), detalleProducto.getPrecioVenta_1(), detalleProducto.getPrecioVenta_2(), detalleProducto.getPrecioVenta_3(), detalleProducto.getPrecioVenta_4(), entity.getFuncionario().getId(), detalleProducto.getProducto().getMarca().getDescripcion(), entity.getTipo(), idVent, entity.getProveedor().getId());


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
					
					
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entityRepository.save(entity);
				}
				
			}


		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Error: "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);


	}



	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DetalleCompra> detalle){
		System.out.println("entroo eliminar compra");
		try {
			System.out.println("entroo eliminar compra try ");
			for (DetalleCompra de : detalle) {		
				System.out.println("entroo eliminar compra for");
				detalleRepository.deleteById(de.getId());
			}
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
			System.out.println("producto que viene tiene compuesto");
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
				mov.getConcepto().setId(c.getId());
			}else {
				c= conceptoRepository.findById(4).get();
				mov.getConcepto().setId(c.getId());
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
					movEntr.getConcepto().setId(ccc.getId());
				}else {
					ccc= conceptoRepository.findById(4).get();
					movEntr.getConcepto().setId(ccc.getId());
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
				System.out.println("Producto relacio0nado con una base");
				
				
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
					movEntr.getConcepto().setId(ccc.getId());

				}else {
					ccc= conceptoRepository.findById(4).get();
					movEntr.getConcepto().setId(ccc.getId());

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
						mov.getConcepto().setId(c.getId());
					}else {
						c= conceptoRepository.findById(4).get();
						mov.getConcepto().setId(c.getId());
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
					mov.getConcepto().setId(c.getId());
				}else {
					c= conceptoRepository.findById(4).get();
					mov.getConcepto().setId(c.getId());
				}

				mov.setReferencia(c.getDescripcion()+" REF.: "+ idCompra);
				movEntradaSalidaRepository.save(mov);
				
				p.setPrecioCosto(costo);
				p.setPrecioVenta_1(preVen1);
				p.setPrecioVenta_2(preVen2);
				p.setPrecioVenta_3(preVen3);
				p.setPrecioVenta_4(preVen4); 
				productoRepository.updateProveedorId(idProvee, p.getId());
				productoRepository.save(p);
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
				report.reportPDFDescarga(listado, map, "ReporteCompraRangoPorProveedor", response);
				//report.reportPDFDescarga(listado, map, "ReporteCompraRangoPorProveedor",  "Microsoft Print to PDF");
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