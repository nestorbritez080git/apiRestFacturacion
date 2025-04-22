package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
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

import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.DevolucionVenta;
import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.InteresCuota;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.TipoPlazo;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CierreCajaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.DetalleProductoRepository;
import com.bisontecfacturacion.security.repository.DevolucionVentaDetalleRepository;
import com.bisontecfacturacion.security.repository.DevolucionVentaRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.InteresCuotaRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ParametroTipoHojaRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.repository.TesoreriaRepository;
import com.bisontecfacturacion.security.repository.TipoPlazoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

import sun.util.logging.resources.logging;


@Transactional
@RestController
@RequestMapping("devolucionVenta")
public class DevoluconVentaController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private DevolucionVentaRepository entityRepository;
	
	
	@Autowired
	private ParametroTipoHojaRepository parametroTipoHoja;
	@Autowired
	private DevolucionVentaDetalleRepository detalleRepository;

	@Autowired
	private DetalleProductoRepository detalleProductoRepository;

	
	@Autowired
	private ClienteRepository clienteRepository; 
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;


	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;

	@Autowired
	private CierreCajaRepository cierreCajaRepository;


	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;


	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private CuentaAcobrarDetalleRepository cuentaCobrarDetalleRepository;

	@Autowired
	private NotaCreditoRepository notaCreditoRepository;
	
	@Autowired
	private ReporteConfigRepository reporteConfigRepository;


	
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;

	@Autowired
	private InteresCuotaRepository interesCuotaRepository;

	@Autowired
	private TipoPlazoRepository tipoPlazoRepository;
	
	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;


	@Autowired
	private TesoreriaRepository tesoreriaRepository;

	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;

	private SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");


	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public DevolucionVenta getDevolucionId(@PathVariable int id){
		return cargarEdicionId(entityRepository.getOne(id));
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleDevolucion/{id}")
	public List<DevolucionVentaDetalle> getDevolucionDetallePoCabeceraId(@PathVariable int id){
		List<Object[]> objeto=detalleRepository.getDetalleDevolucionPorIdCabecera(id);
		List<DevolucionVentaDetalle> listaRetorno= new ArrayList<DevolucionVentaDetalle>();
		for(Object[] ob: objeto) {
			DevolucionVentaDetalle det= new DevolucionVentaDetalle();
			det.setId(Integer.parseInt(ob[0].toString()));
			det.setDescripcion(ob[1].toString());
			det.getDetalleProducto().getProducto().getUnidadMedida().setDescripcion(ob[2].toString());
			det.getDetalleProducto().setCantidad(Double.parseDouble(ob[3].toString()));
			det.getDetalleProducto().setPrecio(Double.parseDouble(ob[4].toString()));
			det.getDetalleProducto().setIva(ob[5].toString());
			det.getDetalleProducto().setMontoIva(Double.parseDouble(ob[6].toString()));
			det.getDetalleProducto().setSubTotal(Double.parseDouble(ob[7].toString()));
			det.setCantidad(Double.parseDouble(ob[8].toString()));
			det.setSubTotal(Double.parseDouble(ob[9].toString()));
			listaRetorno.add(det);
		}
		return listaRetorno;
	}
	
	

	@RequestMapping(method=RequestMethod.GET, value="/{tipo}/{fecha}")
	public List<DevolucionVenta> getDevolucion(@PathVariable int tipo, @PathVariable String fecha){
		List<DevolucionVenta> listado = new ArrayList<DevolucionVenta>();
		if (tipo == 1) {
			listado = cargarLista();
		}
		if (tipo == 2) {
			String[] fec=fecha.split("-");
			Integer dia=Integer.parseInt(fec[0]);
			Integer mes=Integer.parseInt(fec[1]);
			Integer ano=Integer.parseInt(fec[2]);
			listado = cargarObjetos(entityRepository.getVentaFecha(ano, mes, dia));
		}

		if (tipo == 3) {
			listado = cargarObjetos(entityRepository.getVentaFiltro("%" + fecha + "%"));
		}

		return listado;
	}


	public DevolucionVenta cargarEdicionId(DevolucionVenta objeto) {
		DevolucionVenta dev = new DevolucionVenta();
		dev.getVenta().setId(objeto.getVenta().getId());
		dev.getVenta().getCliente().getPersona().setNombre(objeto.getVenta().getCliente().getPersona().getNombre() + " "+objeto.getVenta().getCliente().getPersona().getApellido());
		dev.getVenta().setTotal(objeto.getVenta().getTotal());
		dev.getVenta().setTipo(objeto.getVenta().getTipo());
		dev.getVenta().setFecha(objeto.getVenta().getFecha());
		dev.getVenta().setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setId(objeto.getId());
		dev.setFecha(objeto.getFecha());
		dev.setTotal(objeto.getTotal());
		dev.getTipoDevolucion().setDescripcion(objeto.getTipoDevolucion().getDescripcion());
		dev.getTipoDevolucion().setId(objeto.getId());
		dev.setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setNumeroOperacion(objeto.getVenta().getOperacionCaja());

		return dev;
	}

	public List<DevolucionVenta> cargarObjetos(List<Object[]> objeto) {
		List<DevolucionVenta> lista = new ArrayList<DevolucionVenta>();
		for(Object[] o: objeto) {
			DevolucionVenta dev = new DevolucionVenta();
			dev.setId(Integer.parseInt(o[0].toString()));
			dev.getFuncionario().getPersona().setNombre(o[1].toString());
			dev.getVenta().getCliente().getPersona().setNombre(o[2].toString());
			if (o[3].toString() == null) { dev.getVenta().getCliente().getPersona().setCedula(""); } else { dev.getVenta().getCliente().getPersona().setCedula(o[3].toString()); }
			dev.setTotal(Double.parseDouble(o[4].toString()));
			dev.getTipoDevolucion().setDescripcion(o[5].toString());
			dev.setHora(o[6].toString());
			dev.setEstado(o[7].toString());
			dev.getTipoDevolucion().setId(Integer.parseInt(o[8].toString()));
			lista.add(dev);
		}
		return lista;
	}
	public List<DevolucionVenta> cargarLista() {
		List<DevolucionVenta> lista = new ArrayList<DevolucionVenta>();
		List<DevolucionVenta> l = entityRepository.findTop50ByOrderByIdDesc();

		for(DevolucionVenta d: l) {
			DevolucionVenta devol = new DevolucionVenta();
			devol.setId(d.getId());
			devol.getFuncionario().getPersona().setNombre(d.getFuncionario().getPersona().getNombre() + " " + d.getFuncionario().getPersona().getApellido());
			devol.getVenta().getCliente().getPersona().setNombre(d.getVenta().getCliente().getPersona().getNombre() + " " + d.getVenta().getCliente().getPersona().getApellido());
			devol.getVenta().getCliente().getPersona().setCedula(d.getVenta().getCliente().getPersona().getCedula());
			devol.getVenta().setTotal(d.getVenta().getTotal());
			devol.setTotal(d.getTotal());
			devol.getVenta().setId(d.getVenta().getId());
			devol.setFecha(d.getFecha());
			devol.getVenta().setFechaFactura(d.getVenta().getFechaFactura());
			devol.getTipoDevolucion().setDescripcion(d.getTipoDevolucion().getDescripcion());
			devol.getTipoDevolucion().setId(d.getTipoDevolucion().getId());
			devol.setEstado(d.getEstado());
			lista.add(devol);
		}

		return lista;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/ult")
	public int getUltimaDevolucion(){
		return entityRepository.getUltimaDevolucion();
	}

	private List<DevolucionVentaDetalle> listarDetalleDevol(List<Object[]> objeto){
		List<DevolucionVentaDetalle> listaRetorno= new ArrayList<DevolucionVentaDetalle>();
		for(Object[] ob: objeto) {
			DevolucionVentaDetalle det= new DevolucionVentaDetalle();
			det.setId(Integer.parseInt(ob[0].toString()));
			det.setDescripcion(ob[1].toString());
			det.getDetalleProducto().getProducto().getUnidadMedida().setDescripcion(ob[2].toString());
			det.getDetalleProducto().setCantidad(Double.parseDouble(ob[3].toString()));
			det.getDetalleProducto().setPrecio(Double.parseDouble(ob[4].toString()));
			det.getDetalleProducto().setIva(ob[5].toString());
			det.getDetalleProducto().setMontoIva(Double.parseDouble(ob[6].toString()));
			det.getDetalleProducto().setSubTotal(Double.parseDouble(ob[7].toString()));
			det.setCantidad(Double.parseDouble(ob[8].toString()));
			det.setSubTotal(Double.parseDouble(ob[9].toString()));
			det.getDetalleProducto().setId(Integer.parseInt(ob[10].toString()));
			det.getDetalleProducto().getProducto().setId(Integer.parseInt(ob[11].toString()));
			listaRetorno.add(det);
		}
		return listaRetorno;

	}
	@Transactional
	@RequestMapping(method=RequestMethod.GET, value = "/confirmarDevolucion/{id}/{idTipoOperacion}/{terminal}/{siImp}")
	public ResponseEntity<?> confirmarDevolucion (@PathVariable int id, OAuth2Authentication authentication, @PathVariable int idTipoOperacion, @PathVariable int terminal, @PathVariable String siImp){
		
		try {
			
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		DevolucionVenta v= entityRepository.getOne(id);
		OperacionCaja operacionAnterior =null;
		if(v.getVenta().getOperacionCaja()!=0) {
			operacionAnterior = operacionCajaRepository.getOne(v.getVenta().getOperacionCaja());
		}else {
			
		}
		
		System.out.println("TIPO OPERACION: "+idTipoOperacion);
		if(v.getVenta().getTipo().equals("1")) {
			System.out.println("DEVOLUCION TIPO VENTA CONTADO");
			if(idTipoOperacion==1) {
				System.out.println("CON REENVOLSO");
				if(operacionAnterior!=null) {					
					OperacionCaja ope= new OperacionCaja();
					ope.setEfectivo(0.0);
					ope.setFecha(new Date());
					ope.setMonto(v.getTotal());
					ope.setReferenciaTipoOperacion("");
					ope.setVuelto(0.0);
					ope.setTipo("SALIDA");
					Concepto c= new Concepto();
					c = conceptoRepository.getOne(6);
					ope.setMotivo(c.getDescripcion()+" REF.: "+v.getId());
					ope.getTipoOperacion().setId(operacionAnterior.getTipoOperacion().getId());
					ope.getAperturaCaja().setId(operacionAnterior.getAperturaCaja().getId());
					ope.getConcepto().setId(6);
					operacionCajaRepository.save(ope);
					if(operacionAnterior.getTipoOperacion().getId()==1) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					if(operacionAnterior.getTipoOperacion().getId()==2) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					if(operacionAnterior.getTipoOperacion().getId()==3) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					AperturaCaja aper=aperturaCajaRepository.findById(operacionAnterior.getAperturaCaja().getId()).get();
					if (aper.isEstado() == false) {
						int cierreId = cierreCajaRepository.IdCierreCaja(operacionAnterior.getAperturaCaja().getId());
						if(operacionAnterior.getTipoOperacion().getId()==1) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getTotal());
						}
						if(operacionAnterior.getTipoOperacion().getId()==2) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaCheque(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaCheque(cierreId, v.getTotal());
						}
						if(operacionAnterior.getTipoOperacion().getId()==3) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(cierreId, v.getTotal());
						}
					}
					List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
					for (DevolucionVentaDetalle list : lista) {
						System.out.println("entrooo for para aumentar: ");
						this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
						System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
						Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
						MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
						System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

						mov.setDescripcion(list.getDescripcion());
						mov.setCantidad(list.getCantidad());
						mov.setFecha(new  Date());
						mov.setHora(hora());

						mov.setIngreso(list.getSubTotal());
						mov.setEgreso(0.0);
						mov.setVentaSalida(list.getPrecio());

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

						mov.getTipoMovimiento().setId(1);
						mov.getProducto().setId(p.getId());
						mov.getFuncionario().setId(v.getFuncionario().getId());
						mov.setMarca(p.getMarca().getDescripcion());
						Concepto ccc= new Concepto();
						ccc= conceptoRepository.findById(6).get();
						mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
						movEntradaSalidaRepository.save(mov);
					}
				}
				
				pdfPrintss(id, terminal, siImp);
			}else if(idTipoOperacion==2) {
				System.out.println("SIN REENVOLSO");
				NotaCredito notaCredtio = new NotaCredito();
				notaCredtio.setFecha(new Date());
				notaCredtio.setHora(hora());
				notaCredtio.setTotal(v.getTotal());
				notaCredtio.setTotalLetra(v.getTotalLetra());
				notaCredtio.getCliente().setId(v.getVenta().getCliente().getId());
				notaCredtio.getFuncionario().setId(usuario.getFuncionario().getId());
				notaCredtio.getDevolucionVenta().setId(id);
				notaCreditoRepository.save(notaCredtio);
				List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
				for (DevolucionVentaDetalle list : lista) {
					System.out.println("entrooo for para aumentar: ");
					this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
					System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
					Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

					mov.setDescripcion(list.getDescripcion());
					mov.setCantidad(list.getCantidad());
					mov.setFecha(new  Date());
					mov.setHora(hora());

					mov.setIngreso(list.getSubTotal());
					mov.setEgreso(0.0);
					mov.setVentaSalida(list.getPrecio());

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

					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(v.getFuncionario().getId());
					mov.setMarca(p.getMarca().getDescripcion());
					Concepto ccc= new Concepto();
					ccc= conceptoRepository.findById(6).get();
					mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
					movEntradaSalidaRepository.save(mov);
				}
			}

			
			//pdfPrintss(id, terminal, siImp);

		}else if (v.getVenta().getTipo().equals("2")) {
			
		    System.out.println("DEVOLUCIÓN TIPO VENTA CRÉDITO");

		    boolean tieneEntrega = v.getVenta().getEntrega() != null && v.getVenta().getEntrega() > 0;
		    double totalDevolucion = v.getTotal();
		    double entrega = v.getVenta().getEntrega() != null ? v.getVenta().getEntrega() : 0.0;
		    double montoReembolso = 0.0;
		    double montoAplicarACuenta = totalDevolucion;

		    if (idTipoOperacion == 1) { // Con reembolso
		        if (!tieneEntrega) {
		            return new ResponseEntity<>(new CustomerErrorType("DEVOLUCIÓN CON REEMBOLSO NO APLICA A VENTA CRÉDITO SIN ENTREGA!"), HttpStatus.CONFLICT);
		        }

		        // Permitir reembolso hasta el valor de entrega
		        montoReembolso = Math.min(entrega, totalDevolucion);
		        montoAplicarACuenta = totalDevolucion - montoReembolso;

		        System.out.println("REEMBOLSO DE ENTREGA: " + montoReembolso);
		        System.out.println("A APLICAR A CUENTA: " + montoAplicarACuenta);

		        // Registra el reembolso en caja si es necesario (esto depende de tu lógica interna)
		        // registrarReembolsoCaja(v.getFuncionario(), montoReembolso, v.getId());

		    } else if (idTipoOperacion == 2) { // Sin reembolso
		        System.out.println("DEVOLUCIÓN SIN REEMBOLSO - TODO VA A LA CUENTA");
		        
		        
		    }

		    // Aplicar devolución a cuenta corriente
		    if (montoAplicarACuenta > 0) {
		        CuentaCobrarCabecera cu = cuentaCobrarRepository.getCuentaCabeceraPorVentaId(v.getVenta().getId());

		        if (cu != null) {
		            String resultado = aplicarDevolucionACuentaConEntregaInicial(v.getVenta().getId(), montoAplicarACuenta);
		            if (!resultado.equals("OK")) {
		                return new ResponseEntity<>(new CustomerErrorType(resultado), HttpStatus.CONFLICT);
		            }
		        } else {
		            return new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRÓ CUENTA PARA ESTA DEVOLUCIÓN TIPO VENTA CRÉDITO!"), HttpStatus.CONFLICT);
		        }
		    }
		    
		    List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
			for (DevolucionVentaDetalle list : lista) {
				System.out.println("entrooo for para aumentar: ");
				this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
				System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
				Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

				mov.setDescripcion(list.getDescripcion());
				mov.setCantidad(list.getCantidad());
				mov.setFecha(new  Date());
				mov.setHora(hora());

				mov.setIngreso(list.getSubTotal());
				mov.setEgreso(0.0);
				mov.setVentaSalida(list.getPrecio());

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

				mov.getTipoMovimiento().setId(1);
				mov.getProducto().setId(p.getId());
				mov.getFuncionario().setId(v.getFuncionario().getId());
				mov.setMarca(p.getMarca().getDescripcion());
				Concepto ccc= new Concepto();
				ccc= conceptoRepository.findById(6).get();
				mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
				movEntradaSalidaRepository.save(mov);
			}
		
		    
		}
		entityRepository.confirmarDevolucion(id);
		
		} catch (Exception e) {
            return new ResponseEntity<>(new CustomerErrorType("NO SE PUDO GUARDAR LA CONFIRMACION DE DEVOLUCION!"), HttpStatus.CONFLICT);
		}
	
		return new ResponseEntity<String>(HttpStatus.CREATED);
		
	}
	
	
	public String aplicarDevolucionACuentaConEntregaInicial(int idVenta, double montoDevolucion) {
	    CuentaCobrarCabecera cuenta = cuentaCobrarRepository.getCuentaCabeceraPorVentaId(idVenta);

	    if (cuenta == null) {
	        return "No se encontró una cuenta por cobrar asociada a esta venta.";
	    }

	    double montoRestante = montoDevolucion;
	    List<CuentaCobrarDetalle> detalles = cuenta.getCuentaCobrarDetalle();

	    for (CuentaCobrarDetalle detalle : detalles) {
	        double montoCuota = detalle.getMonto();
	        double importeActual = detalle.getImporte();
	        double saldoCuota = montoCuota - importeActual;

	        if (saldoCuota > 0 && montoRestante > 0) {
	            double aplicar = Math.min(saldoCuota, montoRestante);

	            System.out.println("Aplicando devolución a cuota #" + detalle.getNumeroCuota());
	            System.out.println("Monto antes: " + importeActual);
	            System.out.println("Aplicando: " + aplicar);
	            detalle.setImporte(importeActual + aplicar);
	            montoRestante -= aplicar;

	            cuentaCobrarDetalleRepository.save(detalle);
	        }
	    }

	    double pagadoOriginal = cuenta.getPagado();
	    double saldoOriginal = cuenta.getSaldo();

	    double aplicado = montoDevolucion - montoRestante;

	    cuenta.setPagado(pagadoOriginal + aplicado);
	    cuenta.setSaldo(saldoOriginal - aplicado);

	    cuentaCobrarRepository.save(cuenta);

	    if (montoRestante > 0) {
	        System.out.println("DEVOLUCIÓN MAYOR AL SALDO PENDIENTE. EXCEDENTE: " + montoRestante);
	        return "DEVOLUCIÓN APLICADA PARCIALMENTE. EXCEDENTE DE " + montoRestante + " DEBERÍA SER REEMBOLSADO O QUEDAR COMO SALDO A FAVOR.";
	    }

	    return "OK";
	}
	
	 public String aplicarDevolucionACuenta(int ventaId, double montoDevolucion) {
	        CuentaCobrarCabecera cuenta = cuentaCobrarRepository.getCuentaCabeceraPorVentaId(ventaId);
	        
	        if (cuenta == null) {
	            return "NO SE ENCONTRÓ NINGUNA CUENTA ASOCIADA A LA VENTA";
	        }

	        if (montoDevolucion > cuenta.getSaldo()) {
	            return "EL MONTO DE LA DEVOLUCIÓN EXCEDE EL SALDO DE LA CUENTA";
	        }

	        double restante = montoDevolucion;
	        for (CuentaCobrarDetalle detalle : cuenta.getCuentaCobrarDetalle()) {
	            if (restante <= 0) break;
	            if (detalle.getImporte() >= detalle.getSubTotal()) continue;

	            double diferencia = detalle.getSubTotal() - detalle.getImporte();
	            double aplicado = Math.min(restante, diferencia);

	            detalle.setImporte(detalle.getImporte() + aplicado);
	            restante -= aplicado;

	            cuentaCobrarDetalleRepository.save(detalle);
	        }

	        cuenta.setPagado(cuenta.getPagado() + montoDevolucion);
	        cuenta.setSaldo(cuenta.getSaldo() - montoDevolucion);
	        cuentaCobrarRepository.save(cuenta);

	        return "OK";
	 }

	public Date getFechaPlazo(Date fe, int plazo){

		Date hoy = fe;
		long fechacontresdiasmas = hoy.getTime() + (plazo * 24 * 60 * 60 * 1000);
		Date fechacontresdiasmasformatada = new Date(fechacontresdiasmas);
		System.out.println("Nuva Fecha Plazo: "+fechacontresdiasmasformatada);
		return fechacontresdiasmasformatada;
	}

	public void actualizarProductoBaseA(int id , double cantidad) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}else {
			List<ProductoCardex> list = compuestoRepository.getBase(id);
			productoRepository.findByActualizaA(cantidad, id);//actualiza producto base
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual= cantidad / ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}
	}

	public void actualizarProductoBaseAumentar(int id , double cantidad) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}else {
			List<ProductoCardex> list = compuestoRepository.getBase(id);
			productoRepository.findByActualizaA(cantidad, id);//actualiza producto base
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual= cantidad / ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}
	}


	@RequestMapping(method=RequestMethod.GET, value = "/consularNotaCredito/{id}")
	public NotaCredito getNotaDevolucionPorIdDevolucion (@PathVariable int id){

		Object  [][]  objeto =entityRepository.getNotaCreditoPorIdDevolucion(id);
		NotaCredito n = new NotaCredito();
		n.setId(Integer.parseInt(objeto[0][0].toString()));
		n.getCliente().getPersona().setNombre(objeto[0][1].toString());
		n.setTotal(Double.parseDouble(objeto[0][2].toString()));
		n.getFuncionario().getPersona().setNombre(objeto[0][3].toString());
		n.setTotalLetra(objeto[0][4].toString());
		n.getDevolucionVenta().setId(Integer.parseInt(objeto[0][5].toString()));
		n.getDevolucionVenta().setFecha(FechaUtil.convertirFechaStringADateUtil(objeto[0][6].toString()));
		n.getDevolucionVenta().setTotal(Double.parseDouble(objeto[0][7].toString()));
		return n;
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody DevolucionVenta entity){
		 try {
		        // Validaciones principales
		        if (entity.getFuncionario().getId() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACÍO!"), HttpStatus.CONFLICT);
		        } else if (entity.getVenta().getId() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA VENTA A LA CUAL DEVOLVER PRODUCTO!"), HttpStatus.CONFLICT);
		        } else if (entity.getFechaFactura() == null) {
		            return new ResponseEntity<>(new CustomerErrorType("LA FECHA DE FACTURACIÓN NO DEBE QUEDAR VACÍA!"), HttpStatus.CONFLICT);
		        } else if (entity.getTotal() == null || entity.getTotal() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("EL MONTO A DEVOLVER DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
		        } else if (entity.getVenta().getTipo().equals("1") && entity.getNumeroOperacion() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("EL NÚMERO DE OPERACIÓN NO DEBE QUEDAR VACÍO!"), HttpStatus.CONFLICT);
		        } else if (entity.getTipoDevolucion().getId() == 1 && entity.getVenta().getTipo().equals("2") && entity.getVenta().getEntrega()==0) {
		            return new ResponseEntity<>(new CustomerErrorType("NO SE PUEDE GUARDAR DEVOLUCIÓN DE VENTA A CRÉDITO CON REEMBOLSO SIN ENTREGA INICIAL!"), HttpStatus.CONFLICT);
		        }

		        // Validar detalles
		        for (int i = 0; i < entity.getDevolucionVentaDetalle().size(); i++) {
		            DevolucionVentaDetalle dev = entity.getDevolucionVentaDetalle().get(i);
		            int item = i + 1;
		            if (dev.getCantidad() == null) {
		                return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL ITEM N°: " + item + " NO DEBE QUEDAR VACÍA!"), HttpStatus.CONFLICT);
		            } else if (dev.getDescripcion() == null) {
		                return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL ITEM N°: " + item + " NO DEBE QUEDAR VACÍA!"), HttpStatus.CONFLICT);
		            } else if (dev.getPrecio() == null) {
		                return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL ITEM N°: " + item + " NO DEBE QUEDAR VACÍO!"), HttpStatus.CONFLICT);
		            } else if (dev.getSubTotal() == null) {
		                return new ResponseEntity<>(new CustomerErrorType("EL SUBTOTAL DEL ITEM N°: " + item + " NO DEBE QUEDAR VACÍO!"), HttpStatus.CONFLICT);
		            } else if (dev.getDetalleProducto().getId() == 0) {
		                return new ResponseEntity<>(new CustomerErrorType("EL DETALLE DE PRODUCTO DEL ITEM N°: " + item + " NO DEBE QUEDAR VACÍO!"), HttpStatus.CONFLICT);
		            }
		        }
		        // Asignar hora y fecha
		        entity.setHora(hora());
		        entity.setFecha(new Date());

		        // Guardar devolución
		        DevolucionVenta savedEntity = entityRepository.save(entity);

		        // Guardar detalles
		        for (DevolucionVentaDetalle det : entity.getDevolucionVentaDetalle()) {
		        	
		            det.setDevolucionVenta(savedEntity);
		            detalleRepository.save(det);

		            // OPCIONAL: actualizar stock del producto
		            Producto prod = productoRepository.findById(det.getDetalleProducto().getProducto().getId()).orElse(null);
		            if (prod != null) {
		                prod.setExistencia(prod.getExistencia()	 + det.getCantidad());
		                productoRepository.save(prod);
		            }
		        }
		        entityRepository.save(savedEntity);
		        
		        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>("Error interno al procesar la devolución", HttpStatus.INTERNAL_SERVER_ERROR);
		    }
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}


	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DevolucionVentaDetalle> detalles){
		try {
			if(detalles.size()!=-1) {
				System.out.println("con listado lista "+detalles.size());
				for (DevolucionVentaDetalle de : detalles) {
					System.out.println("ID ELIMINADO : "+de.getId());
					detalleRepository.deleteById(de.getId());
				}
				
				return  new  ResponseEntity<String>(HttpStatus.CREATED);

			}else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	
	public void pdfPrintss(int idVenta, int numeroTerminal, String siImpresion ) {
		if (siImpresion.equals("true")) {
			System.out.println(numeroTerminal);
			Reporte report = new Reporte();
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminal(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				List<DevolucionVenta> venta = getLista(idVenta);
				ReporteConfig reportConfig = new ReporteConfig();
				reportConfig = reporteConfigRepository.getOne(6);
				Map<String, Object> map = new HashMap<>();
				report=new Reporte();
				int pageSize = 10;
				int totalPages = (int) Math.ceil((double) venta.get(0).getDevolucionVentaDetalle().size() / pageSize);
				System.out.println("TOTAL DE PAGINAS:"+ totalPages);
				List<DevolucionVenta> listaVentaImpresion= new ArrayList<DevolucionVenta>();
				for (int i = 0; i < totalPages; i++) {	
				    System.out.println("\n--- Página " + (i + 1) + " ---");

				    int start = i * pageSize;
				    int end = Math.min(start + pageSize, venta.get(0).getDevolucionVentaDetalle().size());
				    // Crear una nueva lista con los elementos de la página actual
				    List<DevolucionVentaDetalle> detallesPagina = new ArrayList<>(venta.get(0).getDevolucionVentaDetalle().subList(start, end));
				    Double totalMontoPagina=0.0, totalPaginaIvaCinco=0.0, totalPaginaIvaDies=0.0, totalPaginaIva=0.0,totalPaginaExcenta=0.0;
				    for (int j = 0; j < detallesPagina.size(); j++) {
						totalMontoPagina = totalMontoPagina + detallesPagina.get(j).getSubTotal();
						if(detallesPagina.get(j).getIva().equals("10 %")) {totalPaginaIvaDies = totalPaginaIvaDies +  (detallesPagina.get(j).getSubTotal()/11);}
						if(detallesPagina.get(j).getIva().equals("5 %")) {totalPaginaIvaCinco = totalPaginaIvaCinco +  (detallesPagina.get(j).getSubTotal()/21);}
						if(detallesPagina.get(j).getIva().equals("Excenta")) {totalPaginaExcenta = totalPaginaExcenta +  (detallesPagina.get(j).getSubTotal());}
				    }
				    DevolucionVenta ventaImpresion = new DevolucionVenta();
				    ventaImpresion.setId(venta.get(0).getId());
				    ventaImpresion.setFechaFactura(venta.get(0).getFechaFactura());
				    ventaImpresion.getVenta().setCliente(venta.get(0).getVenta().getCliente());
				    ventaImpresion.setFuncionario(venta.get(0).getFuncionario());
				    ventaImpresion.setTotalLetra(NumerosALetras.convertirNumeroALetras(totalMontoPagina));
				    ventaImpresion.setTotal(totalMontoPagina);
				    ventaImpresion.setTotalIvaDies(totalPaginaIvaDies);
				    ventaImpresion.setTotalIvaCinco(totalPaginaIvaCinco);
				    ventaImpresion.getTipoDevolucion().setDescripcion(venta.get(0).getTipoDevolucion().getDescripcion());
				    //ventaImpresion.setTotalIva(totalPaginaIvaDies +  totalPaginaIvaCinco);

				    ventaImpresion.setDevolucionVentaDetalle(detallesPagina);
				    listaVentaImpresion.add(ventaImpresion);
				    System.out.println("UNA FILA DE LA PAGINA ITEM: "+i+", >>>>>>  " +listaVentaImpresion.get(i).getDevolucionVentaDetalle().get(0).getDescripcion());
				  

				}
				
				if (t.getImpresora().equals("matricial")) {
					ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
					System.out.println("entrooo matricial");
					String urlReporte ="\\reporte\\"+reportConfig.getNombreSubReporte1()+".jasper";
					System.out.println("url SUBREPORT:  "+urlReporte+ " report name : "+reportConfig.getNombreReporte());
					map.put("urlSubRepor", urlReporte);
					map.put("tituloReporte", f.getTitulo());
					map.put("razonSocialReporte", f.getRazonSocial());
					map.put("descripcionMovimiento", f.getDescripcion());
					map.put("direccionReporte", f.getDireccion());
					map.put("telefonoReporte", f.getTelefono());
					map.put("entregaInicial", "");
					map.put("paginaTotal", totalPages+ "");
					try {
						ParametroTipoHoja p = parametroTipoHoja.getOne(1);
			        	System.out.println("total apartido lista :  "+listaVentaImpresion.size());
			        	for (int i=0; i < listaVentaImpresion.size(); i++) {
			        		map.put("paginaActual", (i +1)+ "");
			        		if(p.getDescripcion().equals("A4")) {
				        		report.reportPDFImprimirA4(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
			        		}
			        		if(p.getDescripcion().equals("CORTE")) {
				        		report.reportPDFImprimirLibreCorte(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());

			        		}
			            }
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					System.out.println("false impresora matricial");
				}
			}
		}else {
			System.out.println("entrooo else: flase impresion");
		}
	}
	public DevolucionVenta getDevolucion(int idDevol) {
		DevolucionVenta cv = null;

		cv=entityRepository.findById(idDevol).orElse(null);
		//		System.out.println(""+cv.getCliente().getPersona().);
		/*
		List<Venta> v= new ArrayList<Venta>();
		v.add(cv);
		for(int i = 0; i < 1; i++) {
			cv = new Venta();
			cv = v.get(i);
			System.out.println(cv.getCliente().getPersona().getNombre()+"asdfadsfasdfadsads");

		}*/

		return cv;
	}
	public List<DevolucionVentaDetalle> detalleDevolucion(List<Object[]> objeto) {
		List<DevolucionVentaDetalle> listaRetorno=new ArrayList<>();
		//det.id as iddet,det.descripcion as des, ud.descripcion as ud, detprod.cantidad as cant, detprod.precio as precio, 
		//detprod.iva as iva, detprod.monto_iva as monIva, detprod.sub_total as subtotal, det.cantidad as cantidadDevol, det.sub_total as subTotalDevol, detprod.id as idDetalle, p.id as idProducto
		for(Object[] ob:objeto){
			DevolucionVentaDetalle detDevol=new DevolucionVentaDetalle();
			detDevol.setId(Integer.parseInt(ob[0].toString()));
			detDevol.setDescripcion(ob[1].toString());
			detDevol.getDetalleProducto().getProducto().getUnidadMedida().setDescripcion(ob[2].toString());
			detDevol.getDetalleProducto().setCantidad(Double.parseDouble(ob[3].toString()));
			detDevol.getDetalleProducto().setPrecio(Double.parseDouble(ob[4].toString()));
			detDevol.getDetalleProducto().setIva(ob[5].toString());
			detDevol.getDetalleProducto().setMontoIva(Double.parseDouble(ob[6].toString()));
			detDevol.getDetalleProducto().setSubTotal(Double.parseDouble(ob[7].toString()));
			detDevol.setCantidad(Double.parseDouble(ob[8].toString()));
			detDevol.setSubTotal(Double.parseDouble(ob[9].toString()));
			detDevol.getDetalleProducto().setId(Integer.parseInt(ob[10].toString()));
			detDevol.getDetalleProducto().getProducto().setId(Integer.parseInt(ob[11].toString()));
			listaRetorno.add(detDevol);
		}
		return listaRetorno;
	}
	public List<DevolucionVenta> getLista(int idDevolucion ) {

		List<DevolucionVenta> lista = new ArrayList<>();

		DevolucionVenta xxx = new DevolucionVenta();
		List<DevolucionVentaDetalle> detProducto = new ArrayList<>();


		xxx = getDevolucion(idDevolucion);
		detProducto = detalleDevolucion(detalleRepository.getDetalleDevolucionPorIdCabecera(idDevolucion));


		for (int i = 0; i < 1; i++) {
			Cliente cli = clienteRepository.getIdCliente(xxx.getVenta().getCliente().getId());
			Funcionario FunV = funcionarioRepository.getIdFuncionario(xxx.getVenta().getFuncionario().getId());

			DevolucionVenta v = new DevolucionVenta();
			v.getVenta().getCliente().getPersona().setNombre(cli.getPersona().getNombre()+ " "+cli.getPersona().getApellido());
			v.getVenta().getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getVenta().getCliente().getPersona().setTelefono(cli.getPersona().getTelefono());
			v.getVenta().getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			v.getTipoDevolucion().setDescripcion(xxx.getTipoDevolucion().getDescripcion());
			v.setFechaFactura(xxx.getFechaFactura());
			v.setFecha(xxx.getFecha());
			v.setHora(xxx.getHora());
			v.setId(xxx.getId());
			v.setTotalIvaCinco(xxx.getTotalIvaCinco());
			v.setTotalIvaDies(xxx.getTotalIvaDies());
			v.setTotal(xxx.getTotal());
			v.setTotalLetra(xxx.getTotalLetra());
			v.setTipoDevolucion(xxx.getTipoDevolucion());
			v.setDevolucionVentaDetalle(detProducto);
		
			lista.add(v);
		}

		return lista;

	}
	
	
	@RequestMapping(method=RequestMethod.POST, value = "/validarDetalleDevolucion")
	    public ResponseEntity<?> validarDevoluciones(@RequestBody List<DevolucionVentaDetalle> detallesADevolver) {
	        try {
	        	 for (DevolucionVentaDetalle detalleDevolucion : detallesADevolver) {
	        	        int detalleProductoId = detalleDevolucion.getDetalleProducto().getId();
	        	        int prodId= detalleDevolucion.getDetalleProducto().getProducto().getId();
	        	        double cantidadADevolver = detalleDevolucion.getCantidad();

	        	        // Obtener el detalle de la venta original
	        	        DetalleProducto detalleVenta = detalleProductoRepository.findById(detalleProductoId)
	        	                .orElseThrow(() -> new RuntimeException("Detalle del producto no encontrado: " + detalleProductoId));

	        	        double cantidadVendida = detalleVenta.getCantidad();
	        	        int ventaId = detalleVenta.getVenta().getId();

	        	        // Consultar cuántas unidades ya fueron devueltas y confirmadas
	        	        Double cantidadDevuelta = detalleProductoRepository.cantidadConfirmadaDevueltaPorProducto(detalleProductoId, ventaId);

	        	        if (cantidadDevuelta == null) {
	        	            cantidadDevuelta = 0.0;
	        	        }

	        	        double cantidadRestante = cantidadVendida - cantidadDevuelta;

	        	        // Verificación de devolución completa
	        	        if (cantidadRestante <= 0.0) {
	        	            return new ResponseEntity<>(new CustomerErrorType("El producto con ID: " + prodId + " ya fue devuelto completamente."), HttpStatus.BAD_REQUEST);
	        	        }

	        	        // Verificación de cantidad excedida
	        	        if (cantidadADevolver > cantidadRestante) {
	        	            return new ResponseEntity<>(new CustomerErrorType("La cantidad a devolver del producto con ID: " + prodId + 
	        	                    " del detalle "+detalleProductoId+" excede la cantidad a devolver. Cantidad restante: " + cantidadRestante + ". Intento de devolución: " + cantidadADevolver), HttpStatus.BAD_REQUEST);
	        	        }
	        	    }
	        	    return new ResponseEntity<>(HttpStatus.OK);

	        } catch (RuntimeException e) {
	            // En caso de error, retornamos el mensaje de error con un código HTTP 400 (Bad Request)
	            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	        }
	    }


	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenConcepto(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		DevolucionVenta pre= new DevolucionVenta(); 
		pre=entityRepository.getDevolucionPorId(id);
		if(pre.getVenta().getTipo().equals("1")) {pre.getVenta().setTipo("CONTADO");}
		if(pre.getVenta().getTipo().equals("2")) {pre.getVenta().setTipo("CREDITO");}
		List<DevolucionVenta> listado= new ArrayList<DevolucionVenta>();
		listado.add(pre);
		
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
			report.reportPDFDescarga(listado, map, "ReporteDevolucionVentaPdf", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
}
