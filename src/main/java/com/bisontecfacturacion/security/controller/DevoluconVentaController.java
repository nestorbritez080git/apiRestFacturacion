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
import com.bisontecfacturacion.security.model.DevolucionVenta;
import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
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
			det.getDetalleProducto().setId(Integer.parseInt(ob[10].toString()));
			det.getDetalleProducto().getProducto().setId(Integer.parseInt(ob[11].toString()));
			det.setPrecio(Double.parseDouble(ob[12].toString()));
			listaRetorno.add(det);
		}
		return listaRetorno;
	}



	@RequestMapping(method=RequestMethod.GET, value="/{tipo}/{fecha}")
	public List<DevolucionVenta> getDevolucion(@PathVariable int tipo, @PathVariable String fecha){
		List<DevolucionVenta> listado = new ArrayList<DevolucionVenta>();
		if (tipo == 1) {
			System.out.println();
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

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{filtro}")
	public List<DevolucionVenta> getDevolucionAllPorFiltro(@PathVariable String filtro){
		List<DevolucionVenta> listado = new ArrayList<DevolucionVenta>();
		listado = cargarObjetos(entityRepository.getVentaFiltro("%" + filtro + "%"));
		return listado;
	}


	public DevolucionVenta cargarEdicionId(DevolucionVenta objeto) {
		DevolucionVenta dev = new DevolucionVenta();
		dev.getVenta().setId(objeto.getVenta().getId());
		dev.getVenta().getCliente().getPersona().setNombre(objeto.getVenta().getCliente().getPersona().getNombre() + " "+objeto.getVenta().getCliente().getPersona().getApellido());
		dev.getVenta().setTotal(objeto.getVenta().getTotal());
		dev.getVenta().getCliente().setId(objeto.getVenta().getCliente().getId());
		dev.getFuncionario().setId(objeto.getFuncionario().getId());
		dev.getFuncionario().setPersona(objeto.getFuncionario().getPersona());
		dev.getVenta().setTipo(objeto.getVenta().getTipo());
		dev.getVenta().setFecha(objeto.getVenta().getFecha());
		dev.getVenta().setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setId(objeto.getId());
		dev.setFecha(objeto.getFecha());
		dev.setTotal(objeto.getTotal());
		dev.getTipoDevolucion().setDescripcion(objeto.getTipoDevolucion().getDescripcion());
		dev.getTipoDevolucion().setId(objeto.getTipoDevolucion().getId());
		dev.setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setNumeroOperacion(objeto.getVenta().getOperacionCaja());
		dev.getVenta().setOperacionCaja(objeto.getVenta().getOperacionCaja()); 
		dev.getVenta().setEntrega(objeto.getVenta().getEntrega());


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
			dev.getVenta().setId(Integer.parseInt(o[9].toString()));
			dev.getVenta().setTotal(Double.parseDouble(o[10].toString()));
			dev.getVenta().setFechaFactura(FechaUtil.convertirFechaStringADateUtil(o[11].toString()));			
			if (o[12].toString().equals("1") || o[12].toString().toLowerCase().equals("contado")) {
				dev.getVenta().setTipo("1");
			} else if(o[12].toString().equals("2") || o[12].toString().toLowerCase().equals("credito")) {
				dev.getVenta().setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if(o[12].toString().equals("3") || o[12].toString().toLowerCase().equals("nota credito")) {
				dev.getVenta().setTipo("3");
				System.out.println("entro verificacion de cuenta credito");
			}
			
			dev.getVenta().setEntrega(Double.parseDouble(o[13].toString()));
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
			if (d.getVenta().getTipo().equals("1") || d.getVenta().getTipo().toLowerCase().equals("contado")) {
				devol.getVenta().setTipo("1");
			} else if(d.getVenta().getTipo().equals("2") || d.getVenta().getTipo().toLowerCase().equals("credito")) {
				devol.getVenta().setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if(d.getVenta().getTipo().equals("3") || d.getVenta().getTipo().toLowerCase().equals("nota credito")) {
				devol.getVenta().setTipo("3");
				System.out.println("entro verificacion de cuenta credito");
			}
			devol.getVenta().setEntrega(d.getVenta().getEntrega());
			System.out.println(devol.getVenta().getTipo()+"   *+*+ tipo ");
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
	@RequestMapping(method=RequestMethod.GET, value = "/verifiarCuentaSaldo/{id}")
	public ResponseEntity<?> verificarCuentaSaldo (@PathVariable int id){
		CuentaCobrarCabecera est = null;
		est = entityRepository.getSaldoDisponibleVentaCredito(id);

		Map<String, CuentaCobrarCabecera> response = new HashMap<>();
		response.put("cuenta", est);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value = "/confirmar/{id}/{tpDevol}/{idAper}/{tpCaja}/{tComp}/{tEfe}/{tNota}/{terminal}/{imp}")
	public ResponseEntity<?> pruebaConfirmarDevolucion (
	        @RequestBody List<CuentaCobrarCabecera> cue,
	        @PathVariable int id,
	        @PathVariable int tpDevol,
	        @PathVariable int idAper,
	        @PathVariable String tpCaja,
	        @PathVariable Double tComp,
	        @PathVariable Double tEfe,
	        @PathVariable Double tNota,
	        @PathVariable int terminal,
	        @PathVariable String imp) {

	    AperturaCaja ape = aperturaCajaRepository.getAperturaCajaPorIdCaja(idAper);
	    if (tEfe > 0 && ape.getSaldoActual() < tEfe) {
	        return new ResponseEntity<>(new CustomerErrorType("EL MONTO DEVOLUCIÓN ES MAYOR AL MONTO DISPONIBLE EN CAJA"), HttpStatus.CONFLICT);
	    }

	    try {
	        DevolucionVenta ccc = entityRepository.getDevolucionPorId(id);

	        // 🔹 Validación previa de cantidades
	        for (DevolucionVentaDetalle det : ccc.getDevolucionVentaDetalle()) {
	            int idDetalle = det.getDetalleProducto().getId();
	            double cantidadSolicitada = det.getCantidad();

	            // Cantidad ya devuelta previamente en otras devoluciones confirmadas
	            Double cantidadYaDevuelta = detalleRepository.cantidadDevueltaConfirmada(idDetalle);
	            if (cantidadYaDevuelta == null) cantidadYaDevuelta = 0.0;

	            // Cantidad vendida en ese detalle
	            double cantidadVendida = det.getDetalleProducto().getCantidad();

	            // Disponible para devolver
	            double disponible = cantidadVendida - cantidadYaDevuelta;
	            String msg="";
	            if(disponible<=0) {
	            	  msg= "El producto: "+det.getDescripcion()+ ", ya se devolvió la totalidad en otra devolución anterior";
	            }else if(cantidadSolicitada> disponible) {
	            	  msg = "La cantidad a devolver (" + cantidadSolicitada + 
	                             ") supera lo disponible (" + disponible + 
	                             ") en el detalle producto ID " + idDetalle;
	            }
	            if (!msg.isEmpty()) {
	                return new ResponseEntity<>(new CustomerErrorType(msg), HttpStatus.CONFLICT);
	            }

	        }

	        // 🔹 Si todo bien, procesar devolución
	        for (DevolucionVentaDetalle det : ccc.getDevolucionVentaDetalle()) {
	            int idProducto = det.getDetalleProducto().getProducto().getId();
	            double cantidadDevuelta = det.getCantidad();
	            Double costo = det.getDetalleProducto().getCosto();
	            double subtotal = costo * cantidadDevuelta;

	            actualizarProductoBaseAumentarCorregido(
	                idProducto,
	                cantidadDevuelta,
	                costo,
	                subtotal,
	                det.getDetalleProducto().getProducto().getPrecioVenta_1(),
	                det.getDetalleProducto().getProducto().getPrecioVenta_2(),
	                det.getDetalleProducto().getProducto().getPrecioVenta_3(),
	                det.getDetalleProducto().getProducto().getPrecioVenta_4(),
	                ccc.getFuncionario().getId(),
	                det.getDetalleProducto().getProducto().getMarca().getDescripcion(),
	                "DEVOLUCION ",
	                ccc.getId()
	            );
	            this.detalleProductoRepository.actualizarCantidadDevolvida(det.getDetalleProducto().getId(), cantidadDevuelta);
	        }

	        // 🔹 Confirmar devolución
	        entityRepository.confirmarDevolucion(id, tpDevol);

	        // 🔹 Actualizar venta
	        entityRepository.findeByTotalDevolucionVenta(ccc.getVenta().getId(), ccc.getTotal());

	        // 🔹 Aplicar compensación a cuentas por cobrar
	        for (CuentaCobrarCabecera cuentaReferencia : cue) {
	            Double resto = cuentaReferencia.getTotalDevolucion();
	            cuentaCobrarRepository.findByActualizarTotalDevolucionCuenta(cuentaReferencia.getId(), cuentaReferencia.getTotalDevolucion());

	            List<CuentaCobrarDetalle> dets = cuentaCobrarDetalleRepository.getCuentaCobrarDetalle(cuentaReferencia.getId());
	            for (CuentaCobrarDetalle detalle : dets) {
	                if (resto <= 0) break;

	                Double saldoDisponible = detalle.getSubTotal() - detalle.getImporte();
	                Double montoAplicado = Math.min(saldoDisponible, resto);

	                if (montoAplicado > 0) {
	                    cuentaCobrarDetalleRepository.actualizarImporteAplciado(detalle.getId(), montoAplicado);
	                    resto -= montoAplicado;
	                }
	            }
	        }

	        // 🔹 Operaciones de caja (efectivo/cheque/tarjeta)
	        if (tEfe > 0) {
	            OperacionCaja ope = new OperacionCaja();
	            ope.getAperturaCaja().setId(idAper);
	            ope.setMonto(tEfe);
	            ope.getConcepto().setId(6); // devolucionVenta
	            ope.setFecha(new Date());
	            ope.setTipo("SALIDA");
	            ope.setMotivo("DEVOLUCION VENTA REF.: " + ccc.getId());

	            if (tpCaja.equals("EFECTIVO")) ope.getTipoOperacion().setId(1);
	            if (tpCaja.equals("CHEQUE")) ope.getTipoOperacion().setId(2);
	            if (tpCaja.equals("TARJETA")) ope.getTipoOperacion().setId(3);

	            operacionCajaRepository.save(ope);

	            if (tpCaja.equals("EFECTIVO")) aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idAper, tEfe);
	            if (tpCaja.equals("CHEQUE")) aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idAper, tEfe);
	            if (tpCaja.equals("TARJETA")) aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idAper, tEfe);
	        }

	        // 🔹 Nota de crédito
	        if (tNota > 0) {
	            NotaCredito nota = new NotaCredito();
	            nota.setCliente(ccc.getVenta().getCliente());
	            nota.setDevolucionVenta(ccc);
	            nota.setTotal(tNota);
	            nota.setHora(hora());
	            nota.setTotalLetra(NumerosALetras.convertirNumeroALetras(ccc.getTotal()));
	            nota.setFecha(new Date());
	            nota.getFuncionario().setId(2);
	            notaCreditoRepository.save(nota);
	        }

	        // 🔹 Imprimir
	        printimpMatricial(id, terminal, imp);

	        return new ResponseEntity<>(HttpStatus.CREATED);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(new CustomerErrorType("Error al confirmar la devolución: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	public void actualizarProductoBaseAumentarCorregido(int id , double cantidad, double costo, double subtotal, double preVen1, double preVen2, double preVen3, double preVen4, int idfuncio, String marca, String tipo, int idDevolucion) {
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
			Concepto ccc= new Concepto();
			ccc= conceptoRepository.findById(6).get();
			mov.getConcepto().setId(ccc.getId());
			mov.setReferencia(ccc.getDescripcion()+" REF.: "+ idDevolucion);

			movEntradaSalidaRepository.save(mov);

			System.out.println("COSTO : "+costo+ " aplicacion: "+ca.getCantidadAplicacion());
			//p.setPrecioCosto(costo/ca.getCantidadAplicacion());
			p.setPrecioVenta_1(preVen1/ca.getCantidadAplicacion());
			p.setPrecioVenta_2(preVen2/ca.getCantidadAplicacion());
			p.setPrecioVenta_3(preVen3/ca.getCantidadAplicacion());
			p.setPrecioVenta_4(preVen4/ca.getCantidadAplicacion()); 
			System.out.println("Existemcia ************"+ p.getExistencia());
			//p.getProveedor().setId(entity.getProveedor().getId());
			//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
			//productoRepository.updateProveedorId(idProvee, p.getId());
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

				Concepto conc= new Concepto();
				conc= conceptoRepository.findById(6).get();
				movEntr.getConcepto().setId(conc.getId());
				movEntr.setReferencia(conc.getDescripcion()+" REF.: "+ idDevolucion);
				movEntradaSalidaRepository.save(movEntr);

				System.out.println("COSTO : "+costo+ " aplicacion: "+ob.getCantidadAplicacion());
				//pp.setPrecioCosto(costo/(ca.getCantidadAplicacion()*ob.getCantidadAplicacion()));
				pp.setPrecioVenta_1((preVen1/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_2((preVen2/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_3((preVen3/ca.getCantidadAplicacion())*ob.getCantidadAplicacion());
				pp.setPrecioVenta_4((preVen4/ca.getCantidadAplicacion())*ob.getCantidadAplicacion()); 

				//p.getProveedor().setId(entity.getProveedor().getId());
				//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
				System.out.println("Existemcia forddd ************"+ pp.getExistencia());
				//productoRepository.updateProveedorId(idProvee, pp.getId());
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

				Concepto coc= new Concepto();
				coc= conceptoRepository.findById(6).get();
				movEntr.getConcepto().setId(coc.getId());
				movEntr.setReferencia(coc.getDescripcion()+" REF.: "+ idDevolucion);

				movEntradaSalidaRepository.save(movEntr);

				//pp.setPrecioCosto(costo);
				pp.setPrecioVenta_1(preVen1);
				pp.setPrecioVenta_2(preVen2);
				pp.setPrecioVenta_3(preVen3);
				pp.setPrecioVenta_4(preVen4); 
				//p.getProveedor().setId(entity.getProveedor().getId());
				//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
				//productoRepository.updateProveedorId(idProvee, pp.getId());
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
					Concepto cocc= new Concepto();
					cocc= conceptoRepository.findById(6).get();
					mov.getConcepto().setId(cocc.getId());
					mov.setReferencia(cocc.getDescripcion()+" REF.: "+ idDevolucion);
					movEntradaSalidaRepository.save(mov);

					//p.setPrecioCosto(costo * ob.getCantidadAplicacion());
					p.setPrecioVenta_1(preVen1 * ob.getCantidadAplicacion());
					p.setPrecioVenta_2(preVen2 * ob.getCantidadAplicacion());
					p.setPrecioVenta_3(preVen3 * ob.getCantidadAplicacion());
					p.setPrecioVenta_4(preVen4 * ob.getCantidadAplicacion()); 
					//p.getProveedor().setId(entity.getProveedor().getId());
					//productoRepository.updateProveedorId(entity.getProveedor().getId(), p.getId());
					//productoRepository.updateProveedorId(idProvee, p.getId());
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
				Concepto cc= new Concepto();
				cc= conceptoRepository.findById(6).get();
				mov.getConcepto().setId(cc.getId());
				mov.setReferencia(cc.getDescripcion()+" REF.: "+ idDevolucion);
				movEntradaSalidaRepository.save(mov);

				//p.setPrecioCosto(costo);
				p.setPrecioVenta_1(preVen1);
				p.setPrecioVenta_2(preVen2);
				p.setPrecioVenta_3(preVen3);
				p.setPrecioVenta_4(preVen4); 
				//productoRepository.updateProveedorId(idProvee, p.getId());
				productoRepository.save(p);
				productoRepository.findByActualizaA(cantidad, id);

			}

		}
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
		double totalDevlucionOriginal = cuenta.getTotalDevolucion();
		double aplicado = montoDevolucion - montoRestante;

		cuenta.setTotalDevolucion(totalDevlucionOriginal + aplicado);
		cuenta.setPagado(pagadoOriginal);
		cuenta.setSaldo(saldoOriginal - aplicado);
		cuentaCobrarRepository.save(cuenta);
		if (montoRestante > 0) {
			System.out.println("DEVOLUCIÓN MAYOR AL SALDO PENDIENTE de: " + montoRestante);
			return "DEVOLUCIÓN MAYOR AL SALDO PENDIENTE DE PAGO: " + montoRestante;
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
		double pagadoOriginal = cuenta.getPagado();
		double saldoOriginal = cuenta.getSaldo();
		double totalDevlucionOriginal = cuenta.getTotalDevolucion();
		double aplicado = montoDevolucion;

		cuenta.setTotalDevolucion(totalDevlucionOriginal + aplicado);
		cuenta.setPagado(pagadoOriginal);
		cuenta.setSaldo(saldoOriginal - aplicado);
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



	public void printimpMatricial(int idVenta, int numeroTerminal, String siImpresion ) {
		if (siImpresion.equals("true")) {
			System.out.println(numeroTerminal);
			Reporte report = new Reporte();
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminalPorNumero(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				List<DevolucionVenta> venta = getLista(idVenta);
				ReporteConfig reportConfig = new ReporteConfig();
				reportConfig = reporteConfigRepository.getOne(6);
				if(reportConfig == null){
					System.out.println("no se puede mandar impresion de falta agregar REPORTCONFIG ID=6");
				}else{
					
				}
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
			Funcionario FunV = funcionarioRepository.getIdFuncionario(xxx.getFuncionario().getId());

			DevolucionVenta v = new DevolucionVenta();
			v.getVenta().getCliente().getPersona().setNombre(cli.getPersona().getNombre()+ " "+cli.getPersona().getApellido());
			v.getVenta().getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getVenta().getCliente().getPersona().setTelefono(cli.getPersona().getTelefono());
			v.getVenta().getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			
			v.getFuncionario().getPersona().setNombre(FunV.getPersona().getNombre()+ " "+FunV.getPersona().getApellido());
			v.getFuncionario().getPersona().setCedula(FunV.getPersona().getCedula());
			v.getFuncionario().getPersona().setTelefono(FunV.getPersona().getTelefono());
			v.getFuncionario().getPersona().setDireccion(FunV.getPersona().getDireccion());
			
			
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
			return  new ResponseEntity<>(new CustomerErrorType("Erro interno"+e.getMessage()), HttpStatus.BAD_REQUEST);
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
		if(pre.getVenta().getTipo().equals("3")) {pre.getVenta().setTipo("NOTA CREDITO");}
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
