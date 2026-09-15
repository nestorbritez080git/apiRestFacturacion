package com.bisontecfacturacion.security.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.query.criteria.internal.ValueHandlerFactory.DoubleValueHandler;
import org.jboss.jandex.TypeTarget.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bisontecfacturacion.security.auxiliar.ComparativaVentasDTO;
import com.bisontecfacturacion.security.auxiliar.InformeVentaTotalPorProducto;
import com.bisontecfacturacion.security.auxiliar.MovimientoPorConceptosAuxiliar;
import com.bisontecfacturacion.security.auxiliar.NroDocumento;
import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.auxiliar.VentaMensualDTO;
import com.bisontecfacturacion.security.config.ExcelGenerator;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.contabilidad.controller.AsientoContableServices;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDTO;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionAnulada;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.AutoImpresorDetalleVenta;
import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarDetalle;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
import com.bisontecfacturacion.security.model.EmpaqueDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.LoteBoleta;
import com.bisontecfacturacion.security.model.LoteFactura;
import com.bisontecfacturacion.security.model.LoteTicket;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;
import com.bisontecfacturacion.security.model.OrdenPagare;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.model.Zona;
import com.bisontecfacturacion.security.modeloIA.ProductoVentaIA;
import com.bisontecfacturacion.security.modeloIA.ProductoVentaRentabilidad;
import com.bisontecfacturacion.security.modeloIA.VentaModeloIA;
import com.bisontecfacturacion.security.modeloIA.VentaPrediccionIA;
import com.bisontecfacturacion.security.modeloIA.VentaPrediccionPorProductoIA;
import com.bisontecfacturacion.security.repository.AnulacionesVentaRepository;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.AutoImpresorDetalleVentaRepository;
import com.bisontecfacturacion.security.repository.AutoImpresorRepository;
import com.bisontecfacturacion.security.repository.CierreCajaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.DetalleProductoRepository;
import com.bisontecfacturacion.security.repository.DetalleServicioRepository;
import com.bisontecfacturacion.security.repository.EmpaqueCabeceraRepository;
import com.bisontecfacturacion.security.repository.EmpaqueDetalleRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.GrupoRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.LoteBoletaRepository;
import com.bisontecfacturacion.security.repository.LoteFacturaRepository;
import com.bisontecfacturacion.security.repository.LoteTicketRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaCabeceraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrdenPagareRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ParametroTipoHojaRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.repository.TesoreriaRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.repository.ZonaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Transactional
@RestController
@RequestMapping("venta")
public class VentaController {
	private static Formatter ft;
	private Reporte report;
	@Value("${python.api.url}")
	private String pythonUrl;

	@Autowired
	private AnulacionesVentaRepository anulacionVentaRepository;

	@Autowired
	private VentaRepository entityRepository;
	@Autowired
	private EmpaqueCabeceraRepository empaqueRepository;
	@Autowired
	private EmpaqueDetalleRepository empaqueDetalleRepository;

	@Autowired
	private AsientoContableServices asienotContableServices;

	@Autowired
	private DetalleProductoRepository detalleProductoRepository;

	@Autowired
	private DetalleServicioRepository detalleServicioRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;
	@Autowired
	private OperacionCajaRepository operacionRepository;
	@Autowired
	private OperacionCajaCabeceraRepository operacionCajaCabeceraRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private LoteFacturaRepository loteFacturaRepository;

	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private GrupoRepository grupoService;

	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private LoteBoletaRepository loteBoletaRepository;

	@Autowired
	private LoteTicketRepository loteTicketRepository;

	@Autowired
	private ImpresoraRepository impresoraRepository;

	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;

	@Autowired
	private ParametroTipoHojaRepository parametroTipoHoja;
	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;

	@Autowired
	private ReporteConfigRepository reporteConfigRepository;

	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;

	@Autowired
	private CuentaAcobrarDetalleRepository cuentaCobrarDetalleRepository;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;

	@Autowired
	private NotaCreditoRepository notaCreditoRepository;

	@Autowired
	private CierreCajaRepository cierreCajaRepository;

	@Autowired
	private TesoreriaRepository tesoreriaRepository;
	@Autowired
	private OrdenPagareRepository ordenPagareRepository;

	@Autowired
	private AutoImpresorRepository autoImpresorRepository;

	@Autowired
	private ZonaRepository zonaRepository;

	@Autowired
	private AutoImpresorDetalleVentaRepository autoImpresorDetalleVentaRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	private List<Object[]> lis;

	@RequestMapping(method = RequestMethod.GET, value = "/ventas")
	public List<Venta> get() {
		return entityRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscar/entrega/{idVenta}")
	public Double getEntregaVenta(@PathVariable int idVenta) {
		return operacionRepository.getMontoEntregaPorVenta(idVenta);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscar/{filtro}")
	public List<Venta> getAllsFiltro(@PathVariable String filtro) {

		List<Venta> objeto = entityRepository
				.getVentaAllFiltroCliente("%" + Utilidades.eliminaCaracterIzqDer(filtro.toUpperCase()) + "%");
		List<Venta> venta = new ArrayList<>();
		for (Venta ob : objeto) {
			Venta ventas = new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre() + " "
					+ ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre() + " "
					+ ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre() + " "
					+ ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(
					ob.getCliente().getPersona().getNombre() + " " + ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			if (ob.getTipo().equals("1") || ob.getTipo().toLowerCase().equals("contado")
					|| ob.getTipo().toLowerCase().equals("CONTADO")) {
				ventas.setTipo("1");
			} else if (ob.getTipo().equals("2") || ob.getTipo().toLowerCase().equals("credito")
					|| ob.getTipo().toLowerCase().equals("CREDITO")) {
				ventas.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if (ob.getTipo().equals("3") || ob.getTipo().toLowerCase().equals("nota credito")
					|| ob.getTipo().toLowerCase().equals("NOTA CREDITO")) {
				ventas.setTipo("3");
			}
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			ventas.setZona(ob.getZona());
			venta.add(ventas);

		}
		return venta;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/totalventa")
	public Object[] getTotalVenta() {

		return entityRepository.findByTotalVentas();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{fecha}")
	public List<Venta> getAlls(@PathVariable String fecha) {
		String[] fec = fecha.split("-");
		Integer dia = Integer.parseInt(fec[0]);
		Integer mes = Integer.parseInt(fec[1]);
		Integer ano = Integer.parseInt(fec[2]);
		List<Venta> objeto = entityRepository.getVenta(ano, mes, dia);
		List<Venta> venta = new ArrayList<>();
		for (Venta ob : objeto) {
			Venta ventas = new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre() + " "
					+ ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre() + " "
					+ ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre() + " "
					+ ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(
					ob.getCliente().getPersona().getNombre() + " " + ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			if (ob.getTipo().equals("1") || ob.getTipo().toLowerCase().equals("contado")) {
				ventas.setTipo("1");
			} else if (ob.getTipo().equals("2") || ob.getTipo().toLowerCase().equals("credito")) {
				ventas.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if (ob.getTipo().equals("3") || ob.getTipo().toLowerCase().equals("nota credito")) {
				ventas.setTipo("3");

			}
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			ventas.setZona(ob.getZona());
			venta.add(ventas);
		}
		return venta;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{fecha}/{limite}")
	public List<Venta> getAllsLimite(@PathVariable String fecha, @PathVariable int limite) {
		String[] fec = fecha.split("-");
		Integer dia = Integer.parseInt(fec[0]);
		Integer mes = Integer.parseInt(fec[1]);
		Integer ano = Integer.parseInt(fec[2]);
		List<Venta> objeto = entityRepository.getVentaLimitessssss(ano, mes, dia, limite);
		List<Venta> venta = new ArrayList<>();
		for (Venta ob : objeto) {
			Venta ventas = new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre() + " "
					+ ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre() + " "
					+ ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre() + " "
					+ ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(
					ob.getCliente().getPersona().getNombre() + " " + ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			if (ob.getTipo().equals("1") || ob.getTipo().toLowerCase().equals("contado")) {
				ventas.setTipo("1");
			} else if (ob.getTipo().equals("2") || ob.getTipo().toLowerCase().equals("credito")) {
				ventas.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if (ob.getTipo().equals("3") || ob.getTipo().toLowerCase().equals("nota credito")) {
				ventas.setTipo("3");
			}
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			ventas.setZona(ob.getZona());
			ventas.setTimbrado(ob.getTimbrado());
			ventas.setTimbradoInicio(ob.getTimbradoInicio());
			ventas.setTimbradoFin(ob.getTimbradoFin());
			ventas.setGrabadoIvaDies(ob.getGrabadoIvaDies());
			ventas.setGrabadoIvaCinco(ob.getGrabadoIvaCinco());
			ventas.setGrabadoExcenta(ob.getGrabadoExcenta());
			venta.add(ventas);
		}
		return venta;
	}

	/*
	 * @RequestMapping(method=RequestMethod.GET, value="/detalle/{fecha}/{desc}")
	 * public List<Venta> getAllsDescricpion(@PathVariable String
	 * fecha, @PathVariable String desc){ String[] fec=fecha.split("-"); Integer
	 * dia=Integer.parseInt(fec[0]); Integer mes=Integer.parseInt(fec[1]); Integer
	 * ano=Integer.parseInt(fec[2]); List<Venta>
	 * objeto=entityRepository.getVentaDescripcion(ano, mes, dia,
	 * "%"+Utilidades.eliminaCaracterIzqDer(desc)+"%"); List<Venta> venta=new
	 * ArrayList<>(); for(Venta ob:objeto){ Venta ventas=new Venta();
	 * ventas.setId(ob.getId());
	 * ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona
	 * ().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
	 * ventas.getCliente().getPersona().setNombre(ob.getCliente().getPersona().
	 * getNombre()+" "+ ob.getCliente().getPersona().getApellido());
	 * ventas.setTotal(ob.getTotal()); ventas.setFecha(ob.getFecha());
	 * ventas.setEstado(ob.getEstado()); ventas.setTipo(ob.getTipo());
	 * ventas.setHora(ob.getHora());
	 * ventas.getDocumento().setId(ob.getDocumento().getId());
	 * ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
	 * ventas.setNroDocumento(ob.getNroDocumento()); venta.add(ventas); } return
	 * venta; }
	 */
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/utilidad")
	public List<Object[]> getUtilidad() {
		return entityRepository.findByUtilidad();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/totalventaxmes")
	public List<Object[]> getVentaTotalxmes() {
		Date fecha = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		String fec = formater.format(fecha);
		String[] fechas = fec.split("-");
		Integer ano = Integer.parseInt(fechas[0]);
		return entityRepository.findByTotalVentaXmes(ano);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/totalventaXFuncionario/{fecha}")
	public List<Object[]> getVentaTotalxFuncionarioFecha(@PathVariable String fecha) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate fec = LocalDate.parse(fecha, formatter);
			return entityRepository.findTotalVentaXFuncionarioEnFecha(fec);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ventaIdFacturado/{id}")
	public Venta getVentaIdFacturado(@PathVariable int id) {

		Venta v = entityRepository.getVentaIdFacturado(id);
		Venta venta = null;
		if (v != null) {
			venta = new Venta();
			venta.setEstado(v.getEstado());
			venta.setId(v.getId());
			if (v.getTipo().equals("1") || v.getTipo().toLowerCase().equals("contado")) {
				venta.setTipo("1");
				System.out.println("entro verificacion de cuenta contado");
			} else if (v.getTipo().equals("2") || v.getTipo().toLowerCase().equals("credito")) {
				venta.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			} else if (v.getTipo().equals("3") || v.getTipo().toLowerCase().equals("nota credito")) {
				venta.setTipo("3");
				System.out.println("entro verificacion de cuenta credito");
			}
			venta.setNroDocumento(v.getNroDocumento());
			venta.setTotal(v.getTotal());
			venta.getFuncionario().setId(v.getFuncionario().getId());
			venta.getCliente().setId(v.getCliente().getId());
			venta.getCliente().getPersona().setNombre(
					v.getCliente().getPersona().getNombre() + " " + v.getCliente().getPersona().getApellido());
			venta.getCliente().getPersona().setCedula(v.getCliente().getPersona().getCedula());
			venta.getCliente().setEstadoBloqueo(v.getCliente().isEstadoBloqueo());
			venta.getCliente().setLimiteCredito(v.getCliente().getLimiteCredito());
			venta.getDocumento().setId(v.getDocumento().getId());
			venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
			venta.getFuncionarioV().setId(v.getFuncionarioV().getId());

			venta.getFuncionarioR().setId(v.getFuncionarioR().getId());

			venta.getFuncionario().getPersona().setNombre(
					v.getFuncionario().getPersona().getNombre() + " " + v.getFuncionario().getPersona().getApellido());
			venta.getFuncionario().getPersona().setCedula(v.getFuncionario().getPersona().getCedula());
			venta.getFuncionarioV().getPersona().setCedula(v.getFuncionarioV().getPersona().getCedula());
			venta.getFuncionarioR().getPersona().setCedula(v.getFuncionarioR().getPersona().getCedula());
			venta.getFuncionarioV().getPersona().setNombre(v.getFuncionarioV().getPersona().getNombre() + " "
					+ v.getFuncionarioV().getPersona().getApellido());
			venta.getFuncionarioR().getPersona().setNombre(v.getFuncionarioR().getPersona().getNombre() + " "
					+ v.getFuncionarioR().getPersona().getApellido());

			venta.setOperacionCaja(v.getOperacionCaja());
			venta.setFechaFactura(v.getFechaFactura());
			System.out.println("vvvvv: " + v.getFecha() + "  +  :" + v.getFechaFactura());
			venta.setFecha(v.getFecha());
			venta.setHora(v.getHora());
			venta.setTotalDescuento(v.getTotalDescuento());
			venta.setTotalIvaDies(v.getTotalIvaDies());
			venta.setTotalIvaCinco(v.getTotalIvaCinco());
			venta.setTotalIva(v.getTotalIva());
			venta.setTotalExcenta(v.getTotalExcenta());
			venta.setTotalLetra(v.getTotalLetra());
			venta.setEntrega(v.getEntrega());
			venta.setObs(v.getObs());
			venta.setZona(v.getZona());
			venta.setGrabadoIvaDies(v.getGrabadoIvaDies());
			venta.setGrabadoIvaCinco(v.getGrabadoIvaCinco());
			venta.setGrabadoExcenta(v.getGrabadoExcenta());
			venta.setTimbrado(v.getTimbrado());
			venta.setTimbradoInicio(v.getTimbradoInicio());
			venta.setTimbradoFin(v.getTimbradoFin());

		} else {
			venta = null;
		}

		return venta;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ventaId/{id}")
	public Venta getVentaId(@PathVariable int id) {

		Venta v = entityRepository.findById(id).orElse(null);

		Venta venta = new Venta();
		venta.setEstado(v.getEstado());
		venta.setId(v.getId());
		if (v.getTipo().equals("1") || v.getTipo().toLowerCase().equals("contado")) {
			venta.setTipo("1");
			System.out.println("entro verificacion de cuenta contado");
		} else if (v.getTipo().equals("2") || v.getTipo().toLowerCase().equals("credito")) {
			venta.setTipo("2");
			System.out.println("entro verificacion de cuenta credito");
		} else if (v.getTipo().equals("3") || v.getTipo().toLowerCase().equals("nota credito")) {
			venta.setTipo("3");
			System.out.println("entro verificacion de cuenta credito");
		}
		venta.setNroDocumento(v.getNroDocumento());
		venta.setTotal(v.getTotal());
		venta.setTotalDevolucion(v.getTotalDevolucion());
		venta.getFuncionario().setId(v.getFuncionario().getId());
		venta.getFuncionario().getPersona().setNombre(
				v.getFuncionario().getPersona().getNombre() + " " + v.getFuncionario().getPersona().getApellido());
		venta.getCliente().setId(v.getCliente().getId());
		venta.getCliente().getPersona()
				.setNombre(v.getCliente().getPersona().getNombre() + " " + v.getCliente().getPersona().getApellido());
		venta.getCliente().getPersona().setCedula(v.getCliente().getPersona().getCedula());
		venta.getCliente().setLimiteCredito(v.getCliente().getLimiteCredito());
		venta.getCliente().setEstadoBloqueo(v.getCliente().isEstadoBloqueo());
		venta.getDocumento().setId(v.getDocumento().getId());
		venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
		venta.getFuncionario().getPersona().setCedula(v.getFuncionario().getPersona().getCedula());
		venta.getFuncionarioV().getPersona().setCedula(v.getFuncionarioV().getPersona().getCedula());
		venta.getFuncionarioR().getPersona().setCedula(v.getFuncionarioR().getPersona().getCedula());
		venta.getFuncionarioV().setId(v.getFuncionarioV().getId());
		venta.getFuncionarioV().getPersona().setNombre(
				v.getFuncionarioV().getPersona().getNombre() + " " + v.getFuncionarioV().getPersona().getApellido());
		venta.getFuncionarioR().setId(v.getFuncionarioR().getId());
		venta.getFuncionarioR().getPersona().setNombre(
				v.getFuncionarioR().getPersona().getNombre() + " " + v.getFuncionarioR().getPersona().getApellido());
		venta.setOperacionCaja(v.getOperacionCaja());
		venta.setFechaFactura(v.getFechaFactura());
		System.out.println("vvvvv: " + v.getFecha() + "  +  :" + v.getFechaFactura());
		venta.setFecha(v.getFecha());
		venta.setHora(v.getHora());
		venta.setTotalDescuento(v.getTotalDescuento());
		venta.setTotalIvaDies(v.getTotalIvaDies());
		venta.setTotalIvaCinco(v.getTotalIvaCinco());
		venta.setTotalIva(v.getTotalIva());
		venta.setTotalExcenta(v.getTotalExcenta());
		venta.setTotalLetra(v.getTotalLetra());
		venta.setEntrega(v.getEntrega());
		venta.setObs(v.getObs());
		venta.setZona(v.getZona());
		venta.setGrabadoIvaDies(v.getGrabadoIvaDies());
		venta.setGrabadoIvaCinco(v.getGrabadoIvaCinco());
		venta.setGrabadoExcenta(v.getGrabadoExcenta());
		venta.setTimbrado(v.getTimbrado());
		venta.setTimbradoInicio(v.getTimbradoInicio());
		venta.setTimbradoFin(v.getTimbradoFin());
		return venta;
	}

	private static String padF(int numero, int size) {
		ft = new Formatter();
		numero = numero + 1;
		ft.format("%0" + size + "d", numero);
		return ft.toString();
	}

	private static String padFAutoFactura(int numer, int size) {
		ft = new Formatter();
		numer = numer;
		ft.format("%0" + size + "d", numer);
		return ft.toString();
	}

	@Transactional
	public synchronized Map<String, Object> generarDocumentoUnificado(int idDocumento, int numeroTerminal,
			int idVenta) {
		Map<String, Object> resultado = new HashMap<>();
		String numeroCompleto = "";
		TerminalConfigImpresora terminal = terminalRepository
				.consultarTerminalEmisonFacturaPorTerminales(numeroTerminal);
		System.out.println("ID Tipo Remision: " + terminal.getAutoImpresor().getAutoImpresorTipoRemision().getId());
		System.out.println("Estado Emision Factura: " + terminal.getEstadoEmisionFactura());
		// Solo Factura puede usar AutoImpresor
		if (idDocumento == 1) {
			System.out.println("entroo*-*-*- documento id=1 ");

			AutoImpresor auto = null;
			if (Boolean.TRUE.equals(terminal.getEstadoEmisionFactura())
					&& (terminal.getAutoImpresor().getAutoImpresorTipoRemision().getId() == 2)) {
				auto = terminal.getAutoImpresor();
				LocalDate hoy = LocalDate.now();
				if (auto != null && !hoy.isBefore(auto.getFechaInicioVigencia())
						&& !hoy.isAfter(auto.getFechaFinVigencia())) {
					System.out.println("entroo*-*-*- comp para generar");
					numeroCompleto = auto.getCodigoEstablecimiento() + "-" + auto.getPuntoExpedicion() + "-"
							+ padFAutoFactura(auto.getNumeroActual(), 7);

					AutoImpresorDetalleVenta detalleAuto = new AutoImpresorDetalleVenta();
					detalleAuto.getAutoImpresor().setId(auto.getId());
					detalleAuto.getVenta().setId(idVenta);
					detalleAuto.setNumeroFactura(numeroCompleto);
					detalleAuto.setTerminalGrabado(numeroTerminal);
					detalleAuto.setFecha(LocalDateTime.now());

					autoImpresorDetalleVentaRepository.save(detalleAuto);
					autoImpresorRepository.actualizarNumeroActualAutoImpresor(auto.getNumeroActual() + 1, auto.getId());

					resultado.put("numeroDocumento", numeroCompleto);
					resultado.put("numeroAutorizacionAutoimpresor", auto.getNumeroAutorizacion());
					resultado.put("timbrado", auto.getTimbrado());
					resultado.put("fechaInicioVigencia", auto.getFechaInicioVigencia());
					resultado.put("fechaFinVigencia", auto.getFechaFinVigencia());
					return resultado;
				}
			}
		} else if (idDocumento == 2) {
			// Tomar el último registro del lote con FOR UPDATE
			LoteBoleta loteBoleta = loteBoletaRepository.findTop1ByOrderByIdDescForUpdate(); // método personalizado
			if (loteBoleta == null) {
				throw new RuntimeException("No existe lote de tickets configurado.");
			}

			// Incrementar número
			int numeroActual = Integer.parseInt(loteBoleta.getNumeroActual());
			numeroActual++;
			numeroCompleto = padF(numeroActual, 12);

			// Guardar inmediatamente el número incrementado
			loteBoleta.setNumeroActual(String.valueOf(numeroActual));
			loteBoletaRepository.save(loteBoleta);

			resultado.put("numeroDocumento", numeroCompleto);
			resultado.put("numeroAutorizacionAutoimpresor", "");
			resultado.put("timbrado", "");
			resultado.put("fechaInicioVigencia", "");
			resultado.put("fechaFinVigencia", "");
		} else if (idDocumento == 3) {
			// Tomar el último registro del lote con FOR UPDATE
			LoteTicket ticketLote = loteTicketRepository.findTop1ByOrderByIdDescForUpdate(); // método personalizado
			if (ticketLote == null) {
				throw new RuntimeException("No existe lote de tickets configurado.");
			}

			// Incrementar número
			int numeroActual = Integer.parseInt(ticketLote.getNumeroActual());
			numeroActual++;
			numeroCompleto = padF(numeroActual, 12);

			// Guardar inmediatamente el número incrementado
			ticketLote.setNumeroActual(String.valueOf(numeroActual));
			loteTicketRepository.save(ticketLote);

			resultado.put("numeroDocumento", numeroCompleto);
			resultado.put("numeroAutorizacionAutoimpresor", "");
			resultado.put("timbrado", "");
			resultado.put("fechaInicioVigencia", "");
			resultado.put("fechaFinVigencia", "");

			System.out.println("TICKET generado: " + numeroCompleto);
		}

		return resultado;
	}

	public List<NroDocumento> getNroLoteDocumento() {
		List<NroDocumento> lista = new ArrayList<>();
		int tipo0 = 0;
		int tipo1 = 1;
		int tipo2 = 2;
		int tipo3 = 3;

		LoteBoleta loteBoleta = loteBoletaRepository.findTop1ByOrderByIdAsc();
		LoteTicket loteTicket = loteTicketRepository.findTop1ByOrderByIdAsc();
		LoteFactura loteFactura = loteFacturaRepository.findTop1ByOrderByIdAsc();

		for (int i = 0; i < 3; i++) {
			NroDocumento n = new NroDocumento();
			if (i == tipo0) {
				n.setDescripcion("1");

				String[] part = loteFactura.getSerieActual().split("-");
				String cod = part[2];
				String codActual = part[0] + "-" + part[1] + "-" + padF(Integer.parseInt(cod), 7);
				n.setNro(codActual);
			}
			if (i == tipo1) {
				n.setDescripcion("2");
				n.setNro(padF(Integer.parseInt(loteBoleta.getNumeroActual()), 12));
			}
			if (i == tipo2) {
				n.setDescripcion("3");
				n.setNro(padF(Integer.parseInt(loteTicket.getNumeroActual()), 12));
			}

			lista.add(n);
		}

		return lista;
	}

	private void actualizarNumeracionAutoImpresor(int numeroTerminal, int idVenta) {

		/*
		 * NroDocumento n = new NroDocumento(); if (i == tipo0) { n.setDescripcion("1");
		 * 
		 * String [] part= loteFactura.getSerieActual().split("-"); String cod= part[2];
		 * String codActual=part[0]+"-"+part[1]+"-" + padF(Integer.parseInt(cod),7);
		 * n.setNro(codActual); }
		 * 
		 */
	}
	/*
	 * private String actualizarLoteDocumentosNuevoModelo(int idDocumento, int
	 * numeroTerminal, int idVenta) { String idDoc = String.valueOf(idDocumento);
	 * String numeroCompleto = "";
	 * 
	 * for (NroDocumento nro : getNroLoteDocumento()) {
	 * 
	 * if (idDoc.equals(nro.getDescripcion())) {
	 * 
	 * switch (idDoc) { case "1": // Factura TerminalConfigImpresora terminal =
	 * terminalRepository.consultarAutoImpresorTerminal(numeroTerminal);
	 * 
	 * if ("ticket".equalsIgnoreCase(terminal.getImpresora()) &&
	 * Boolean.TRUE.equals(terminal.getEstadoAutoImpresor())) { // Obtener el
	 * autoimpresor asignado AutoImpresor auto =
	 * autoImpresorRepository.consultarAutoImpresorTerminal(numeroTerminal);
	 * 
	 * // Número que se usará en esta operación int numeroUsado =
	 * auto.getNumeroActual();
	 * 
	 * // Incrementar para la siguiente operación auto.setNumeroActual(numeroUsado +
	 * 1); autoImpresorRepository.save(auto);
	 * 
	 * // Formatear número completo: establecimiento-expedición-número
	 * numeroCompleto = nro.getNroEstablecimiento() + "-" + nro.getNroExpedicion() +
	 * "-" + String.format("%08d", numeroUsado);
	 * 
	 * // Registrar número usado en la venta registrarNumeroFactura(idVenta,
	 * numeroUsado);
	 * 
	 * } else { // Si no es autoimpresor, actualizar lote normal int numeroActual =
	 * nro.getNroActual(); loteFacturaRepository.actualizarSeriaActual(nro.getNro(),
	 * 1);
	 * 
	 * numeroCompleto = nro.getNroEstablecimiento() + "-" + nro.getNroExpedicion() +
	 * "-" + String.format("%08d", numeroActual); } break;
	 * 
	 * case "2": // Boleta int numeroBoleta = nro.getNroActual();
	 * loteBoletaRepository.actualizarNumeroActual(nro.getNro(), 1);
	 * 
	 * numeroCompleto = nro.getNroEstablecimiento() + "-" + nro.getNroExpedicion() +
	 * "-" + String.format("%08d", numeroBoleta); break;
	 * 
	 * case "3": // Ticket int numeroTicket = nro.getNroActual();
	 * loteTicketRepository.actualizarNumeroActual(nro.getNro(), 1);
	 * 
	 * numeroCompleto = nro.getNroEstablecimiento() + "-" + nro.getNroExpedicion() +
	 * "-" + String.format("%08d", numeroTicket); break;
	 * 
	 * default: // Otros documentos break; }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return numeroCompleto; }
	 */

	/*
	 * void actualizarLoteDocumentos(int idDocumento, int numeroterminal, int
	 * idVenta){ String idDoc = idDocumento+""; for(NroDocumento nro:
	 * getNroLoteDocumento()) { if (idDoc.equals(nro.getDescripcion()) &&
	 * idDoc.equals("1")) { TerminalConfigImpresora c =
	 * terminalRepository.consultarAutoImpresorTerminal(numeroterminal);
	 * if(c.getImpresora().equals("ticket") & c.getEstadoAutoImpresor()==true) {
	 * AutoImpresor aact = new AutoImpresor(); aact = c.getAutoImpresor();
	 * aact.setNumeroActual(aact.getNumeroActual()+1);
	 * autoImpresorRepository.save(aact); }else {
	 * loteFacturaRepository.actualizarSeriaActual(nro.getNro(), 1); }
	 * 
	 * 
	 * }
	 * 
	 * if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("2")) {
	 * loteBoletaRepository.actualizarNumeroActual(nro.getNro(), 1); }
	 * 
	 * if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("3")) {
	 * loteTicketRepository.actualizarNumeroActual(nro.getNro(), 1); }
	 * 
	 * } }
	 */
	/*
	 * private String getNroDocumento(int idDocumento, int numeroterminal, int
	 * idVenta){ String idDoc = idDocumento+""; String nro = "";
	 * System.out.println("NUMERO DE TERMINAL = "+numeroterminal+
	 * " ID VENTA= "+idVenta); for(NroDocumento nro1: getNroLoteDocumento()) { if
	 * (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("1")) {
	 * System.out.println("NUMERO TERMINAL: "+numeroterminal);
	 * TerminalConfigImpresora c =
	 * terminalRepository.consultarAutoImpresorTerminal(numeroterminal);
	 * if(c.getImpresora().equals("ticket") & c.getEstadoEmisionFactura()==true) {
	 * AutoImpresor aact = new AutoImpresor();
	 * 
	 * aact= c.getAutoImpresor(); if(aact==null)
	 * {System.out.println("si es null");}else {System.out.println("no es null");}
	 * System.out.println(aact.getId()+" id autoimpresor"); AutoImpresorDetalleVenta
	 * detAutoImpresro= new AutoImpresorDetalleVenta();
	 * detAutoImpresro.getAutoImpresor().setId(aact.getId());
	 * detAutoImpresro.getVenta().setId(idVenta);
	 * detAutoImpresro.setFecha(LocalDateTime.now()); String codigoEstablecimiento=
	 * aact.getCodigoEstablecimiento(); String puntoExpedicion=
	 * aact.getPuntoExpedicion(); String
	 * codActual=codigoEstablecimiento+"-"+puntoExpedicion+"-" +
	 * padFAutoFactura(aact.getNumeroActual()+1,7);
	 * detAutoImpresro.setNumeroFactura(codActual);
	 * autoImpresorDetalleVentaRepository.save(detAutoImpresro);
	 * aact.setNumeroActual(aact.getNumeroActual()+1);
	 * autoImpresorRepository.save(aact); nro = codActual; }else { nro =
	 * nro1.getNro(); }
	 * 
	 * }
	 * 
	 * if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("2")) { nro =
	 * nro1.getNro(); loteBoletaRepository.actualizarNumeroActual(nro1.getNro(), 1);
	 * }
	 * 
	 * if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("3")) { nro =
	 * nro1.getNro(); loteTicketRepository.actualizarNumeroActual(nro1.getNro(), 1);
	 * 
	 * } } return nro; }
	 */

	private boolean estadoClienteBloqueo(int id, double total) {
		lis = cuentaCobrarRepository.getCLienteCuentaACobrarPorIdCliente(id);

		if (lis == null) {
			for (Object[] ob : lis) {
				if (ob != null) {
					Double totalMonto = Double.parseDouble(ob[0].toString()) + total;
					if (totalMonto > Double.parseDouble(ob[1].toString())
							&& Boolean.parseBoolean(ob[2].toString()) == true) {
						return true;
					}
				}
			}
		} else {
			Cliente cliente = clienteRepository.findById(id).get();
			if (cliente != null) {
				if (total > cliente.getLimiteCredito() && cliente.isEstadoBloqueo() == true) {
					return true;
				}
			}
		}

		return false;
	}

	public ResponseEntity<?> validarCuentaCobrar(CuentaCobrarCabecera entity) {
		if (entity.getConcepto().getId() == 0) {
			return error("EL CONCEPTO DE LA CUENTA NO DEBE QUEDAR VACIO!");
		} else if (entity.getFuncionario().getId() == 0) {
			return error("EL FUNCIONARIO NO DEBE QUEDAR VACIO!");
		} else if (entity.getTipoPlazo().getId() == 0) {
			return error("EL TIPO PLAZO NO DEBE QUEDAR VACIO!");
		} else if (entity.getCliente().getId() == 0) {
			return error("EL PROVEEDOR NO DEBE QUEDAR VACIO!");
		} else if (entity.getTotal() <= 0) {
			return error("EL TOTAL DE LA CUENTA DEBE SER MAYOR A CERO!");
		} else if (entity.getFraccionCuota() <= 0) {
			return error("EL NÚMERO DE CUOTA O FRACCIÓN DEBE SER MAYOR A CERO!");
		} else if (entity.getCuentaCobrarDetalle().size() <= 0) {
			return error("DEBES AGREGAR POR LO MENO UN DETALLE DE CUENTA A PAGAR!");
		}
		System.out.println("cabece fecha vencimii: " + entity.getFechaVencimiento());
		for (int ind = 0; ind < entity.getCuentaCobrarDetalle().size(); ind++) {
			CuentaCobrarDetalle det = entity.getCuentaCobrarDetalle().get(ind);
			System.out.println("fecha  vencim:  " + det.getFechaVencimiento());
			if (det.getNumeroCuota() <= 0) {
				return error("EL NÚMERO DE CUOTA DEL DETALLE ITEM N°: " + (ind + 1) + ", NO DEBE QUEDAR VACIO!");
			} else if (det.getMonto() <= 0) {
				return error("EL MONTO DE LA CUOTA DEL DETALLE ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!");
			} else if (det.getSubTotal() <= 0) {
				return error("EL SUBTOTAL DEL DETALLE ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!");
			} else if (det.getFechaVencimiento() == null) {
				return error("LA FECHA DEL DETALLE ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!");
			}
		}

		// más validaciones...
		return null;
	}

	public ResponseEntity<?> validarVenta(Venta entity) {
		System.out.println("validando venta entrante.");
		if (entity.getFuncionario().getId() == 0) {
			return error("EL FUNCIONARIO NO DEBE QUEDAR VACIO!");
		} else if (entity.getFuncionarioV().getId() == 0) {
			return error("EL FUNCIONARIO VENDEDOR NO DEBE QUEDAR VACIO!");
		} else if (entity.getFuncionarioR().getId() == 0) {
			return error("EL FUNCIONARIO REPARTIDOR NO DEBE QUEDAR VACIO!");
		} else if (entity.getDocumento().getId() == 0) {
			return error("EL DOCUMENTO NO DEBE QUEDAR VACIO!");
		} else if (entity.getCliente().getId() == 0) {
			return error("EL CLIENTE NO DEBE QUEDAR VACIO!");
		} else if (entity.getDetalleProducto().size() == 0 && entity.getDetalleServicio().size() == 0) {
			return error("LA GRILLA NO DEBE QUEDAR VACIO!");
		} else if (entity.getTotal() <= 0 || entity.getTotal() == null) {
			return error("EL TOTAL DE LA VENTA DEBE SER MAYOR A CERO!");
		} else if (entity.getTotalLetra().equals("") || entity.getTotalLetra() == null) {
			return error("EL TOTAL MONTO EN LETRA NO DEBE QUEDAR VACIO!");
		} else if (estadoClienteBloqueo(entity.getCliente().getId(), entity.getTotal()) == true
				&& entity.getTipo().equals("2") && entity.getEstado().equals("FACTURADO")) {
			return error("NO SE PUEDE FACTURAR VENTAS, CLIENTE BLOQUEADO POR EXCEDER LINEA DE CREDITO!");
		}

		// 🔹 Validar coherencia de total de la venta
		Double sumaDetalles = 0.0;
		Double descuento = 0.0;
		int index = 1;

		// sumar productos
		for (DetalleProducto d : entity.getDetalleProducto()) {
			if (d.getCantidad() == null || d.getCantidad() <= 0) {
				return error("LA CANTIDAD DEL PRODUCTO ITEM N° " + (index + 1) + " NO DEBE SER <= 0!");
			}
			if (d.getDescripcion() == null || d.getDescripcion().trim().isEmpty()) {
				return error("LA DESCRIPCIÓN DEL PRODUCTO ITEM N° " + (index + 1) + " NO DEBE QUEDAR VACÍA!");
			}
			if (d.getPrecio() == null || d.getPrecio() <= 0) {
				return error("EL PRECIO DEL PRODUCTO ITEM N° " + (index + 1) + " NO DEBE SER <= 0!");
			}
			if (d.getSubTotal() == null || d.getSubTotal() <= 0) {
				return error("EL SUBTOTAL DEL PRODUCTO ITEM N° " + (index + 1) + " NO DEBE SER <= 0!");
			}
			sumaDetalles += d.getSubTotal();
			descuento += d.getDescuento();
			index++;
		}

		// sumar servicios
		for (DetalleServicios d : entity.getDetalleServicio()) {
			if (d.getCantidad() == null || d.getCantidad() <= 0) {
				return error("LA CANTIDAD DEL SERVICIO ITEM N° " + index + " NO DEBE SER <= 0!");
			}
			if (d.getDescripcion() == null || d.getDescripcion().trim().isEmpty()) {
				return error("LA DESCRIPCIÓN DEL SERVICIO ITEM N° " + index + " NO DEBE QUEDAR VACÍA!");
			}
			if (d.getPrecio() == null || d.getPrecio() <= 0) {
				return error("EL PRECIO DEL SERVICIO ITEM N° " + index + " NO DEBE SER <= 0!");
			}
			if (d.getSubTotal() == null || d.getSubTotal() <= 0) {
				return error("EL SUBTOTAL DEL SERVICIO ITEM N° " + index + " NO DEBE SER <= 0!");
			}
			sumaDetalles += d.getSubTotal();
			index++;
		}

		// validar total contra suma
		if (entity.getTotal() == null || entity.getTotal() <= 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA VENTA NO DEBE SER <= 0!"),
					HttpStatus.CONFLICT);
		}
		double total = Math.round(entity.getTotal());
		double detalle = Math.round(sumaDetalles);
		System.out.println("SUMA DE DETALLE: " + sumaDetalles + " DESCU: " + descuento);

		if (Math.abs(total - detalle) > 1) {
			return error(
					"EL TOTAL DE LA VENTA (" + total + ") NO COINCIDE CON LA SUMA DE LOS DETALLES (" + detalle + ")");
		}
		return null;

	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/refactorizados/{ter}")
	public ResponseEntity<?> guardarRefactorizado(@RequestPart("venta") Venta venta,
			@RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista,
			@RequestPart("cuentaCobrar") CuentaCobrarCabecera cuentaCobrarCabecera,
			@RequestPart("notaCredito") NotaCredito notaCredito, @PathVariable int ter) {
		Venta savedRetotno = null;
		CuentaCobrarCabecera cuRetotno = null;
		OrdenPagare orRetotno = null;
		List<CuentaCobrarCabecera> listRetorno = new ArrayList<>();
		List<OperacionCaja> opRetotno = new ArrayList<>();
		try {

			if (venta.getTipo().equals("CONTADO") || venta.getTipo().equals("1")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + venta.getTipo());
				venta.setTipo("1");
			}
			if (venta.getTipo().equals("CREDITO") || venta.getTipo().equals("2")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + venta.getTipo());
				venta.setTipo("2");
			}
			if (venta.getTipo().equals("NOTA CREDITO") || venta.getTipo().equals("3")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + venta.getTipo());
				venta.setTipo("3");
			}
			// 1. Validar si hay al menos una zona registrada
			TerminalConfigImpresora terminal = terminalRepository.consultarTerminalEmisonFacturaPorTerminales(ter);

			if (zonaRepository.count() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR AL MENOS UNA ZONA EN EL SISTEMA"),
						HttpStatus.CONFLICT);
			}
			// 2. Si no hay zona asignada o id es 0, asignar la primera zona registrada
			if (venta.getZona() == null || venta.getZona().getId() == 0) {
				// Buscar la primera zona (puede ser la de menor ID)
				System.out.println("entro id 0 o null y asina el primer valor");
				Optional<Zona> primeraZonaOpt = zonaRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
						.findFirst();
				if (!primeraZonaOpt.isPresent()) {
					return new ResponseEntity<>(new CustomerErrorType("ERROR AL ASIGNAR ZONA POR DEFECTO"),
							HttpStatus.CONFLICT);
				}
				venta.setZona(primeraZonaOpt.get());
			}
			System.out.println(
					"auatorimpresor tipo: " + terminal.getAutoImpresor().getAutoImpresorTipoRemision().getId());
			if (terminal != null && venta.getDocumento().getId() == 1 && terminal.getAutoImpresor().getId() == 2) {
				// Validar que tenga autoimpresor asignado
				if (terminal.getEstadoEmisionFactura() == false) {
					return new ResponseEntity<>(
							new CustomerErrorType("Esta terminal no está hablitado para emitir factura"),
							HttpStatus.CONFLICT);
				}
				// Validar que el autoimpresor esté activo
				if (!terminal.getAutoImpresor().isEstado()) {
					return new ResponseEntity<>(new CustomerErrorType("La autorización del timbrado esta desabilitado"),
							HttpStatus.CONFLICT);
				}
				// Validar rango de numeración
				if (terminal.getAutoImpresor().getNumeroActual() < terminal.getAutoImpresor().getRangoInicio()) {
					return new ResponseEntity<>(
							new CustomerErrorType("La numeración actual del timbrado está fuera de rango inicial"),
							HttpStatus.CONFLICT);
				}
				if (terminal.getAutoImpresor().getNumeroActual() > terminal.getAutoImpresor().getRangoFin()) {
					// Ya no se puede emitir más facturas
					return new ResponseEntity<>(new CustomerErrorType(
							"La numeración actual del timbrado está fuera de rango final o ya alcanzo cantidad para emitir"),
							HttpStatus.CONFLICT);
				}

				// Validar timbrado
				if (terminal.getAutoImpresor().getTimbrado() == null
						|| terminal.getAutoImpresor().getTimbrado().isEmpty()) {
					return new ResponseEntity<>(new CustomerErrorType("El auto impresor no tiene un timbrado válido"),
							HttpStatus.CONFLICT);
				}

				// Validar fechas
				if (terminal.getAutoImpresor().getFechaInicioVigencia() == null
						|| terminal.getAutoImpresor().getFechaFinVigencia() == null) {
					return new ResponseEntity<>(
							new CustomerErrorType("El auto impresor no tiene fecha de vigencia definida"),
							HttpStatus.CONFLICT);
				}

				LocalDate hoy = LocalDate.now();
				if (hoy.isBefore(terminal.getAutoImpresor().getFechaInicioVigencia())
						|| hoy.isAfter(terminal.getAutoImpresor().getFechaFinVigencia())) {
					return new ResponseEntity<>(new CustomerErrorType("EL TIMBRADO DEL AUTOIMPRESOR NO ESTÁ VIGENTE"),
							HttpStatus.CONFLICT);
				}
			}
			// 1. Validar compra y detalles
			ResponseEntity<?> validacionCompra = validarVenta(venta);
			if (validacionCompra != null)
				return validacionCompra;

			if (("1".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("contado"))
					&& "FACTURADO".equals(venta.getEstado())) {
				System.out.println("entroo sistema de verificacion venta contado: ");
				ResponseEntity<?> validacionCaja = validarCaja(operacionCajaLista);
				if (validacionCaja != null)
					return validacionCaja;
			} else if (("2".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("credito"))
					&& "FACTURADO".equals(venta.getEstado())) {
				if (venta.getEntrega() > 0) {
					ResponseEntity<?> validacionCajaEntrega = validarCaja(operacionCajaLista);
					if (validacionCajaEntrega != null)
						return validacionCajaEntrega;
				}
				ResponseEntity<?> validacionCuenta = validarCuentaCobrar(cuentaCobrarCabecera);
				if (validacionCuenta != null)
					return validacionCuenta;

			} else if (("3".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("nota credito"))
					&& "FACTURADO".equals(venta.getEstado())) {
				ResponseEntity<?> validarNotaCredito = validarNotaCredito(operacionCajaLista, notaCredito.getId());
				if (validarNotaCredito != null)
					return validarNotaCredito;
			}
			// 2. Validar flujo según tipo (contado/crédito)
			// ResponseEntity<?> validacionCuenta = validarCuentaPagar(cuentaPagarCabecera);
			// if (validacionCuenta != null) return validacionCuenta;
			savedRetotno = guardarVentaYDetalles(venta, ter);
			// pdfPrintss(savedRetotno.getId(), ter,
			// savedRetotno.getDocumento().getDescripcion(),
			// savedRetotno.getDocumento().getId(),
			// savedRetotno.getZona().getDescripcion());

			// 4. Proc Compra saved = guardarCompraYDetalles(compra);esar movimientos
			// financieros
			if (("1".equals(savedRetotno.getTipo()) || savedRetotno.getTipo().toLowerCase().equals("contado"))
					&& "FACTURADO".equals(venta.getEstado())) {
				opRetotno = procesarOperacionCajaVenta(savedRetotno, operacionCajaLista);

			} else if (("2".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("credito"))
					&& "FACTURADO".equals(venta.getEstado())) {
				if (venta.getEntrega() > 0) {
					opRetotno = procesarOperacionCajaVenta(savedRetotno, operacionCajaLista);
				}
				cuRetotno = procesarCuentaCobrar(savedRetotno, cuentaCobrarCabecera);
				orRetotno = procesarOrdenPagared(cuRetotno);
				listRetorno = listadoCargarCuenta(
						cuentaCobrarRepository.findByCuentaPorIdClienteACobrarListasss(cuRetotno.getCliente().getId()));

			} else if (("3".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("nota credito"))
					&& "FACTURADO".equals(venta.getEstado())) {
				opRetotno = procesarNotaCreditoOperacion(operacionCajaLista, notaCredito.getId(), savedRetotno.getId());
			}
			System.out.println(savedRetotno.getNroDocumento() + " nro factura ");
			// agregarDatosCliente:
			savedRetotno.setCliente(clienteRepository.getIdCliente(savedRetotno.getCliente().getId()));
			// agregarDatosFuncionarioVendedor:
			savedRetotno
					.setFuncionarioV(funcionarioRepository.getIdFuncionario(savedRetotno.getFuncionarioV().getId()));
			// agregarDatosFuncionarioRegistro:
			savedRetotno.setFuncionario(funcionarioRepository.getIdFuncionario(savedRetotno.getFuncionario().getId()));
			// agregarDatosFuncionarioReparto:
			savedRetotno
					.setFuncionarioR(funcionarioRepository.getIdFuncionario(savedRetotno.getFuncionarioR().getId()));
			System.out.println(savedRetotno.getTotalIvaDies() + " " + savedRetotno.getTotalIvaCinco());
			// agregarDatosDetalleProducto:
			savedRetotno.setDetalleProducto(detalleProductoRepository.getDetallePorCabecera(savedRetotno.getId()));
			// agregarDatosDetalleServicios:
			savedRetotno.setDetalleServicio(detalleServicioRepository.getDetallePorCabecera(savedRetotno.getId()));
			System.out.println("ID VET: RESS: " + savedRetotno.getId());
			// 🔹 Armar el mapa de respuesta
			Map<String, Object> mapa = new HashMap<>();
			mapa.put("venta", savedRetotno);
			mapa.put("operacionCaja", opRetotno);
			mapa.put("cuenta", listRetorno);
			mapa.put("pagare", orRetotno);
			// 🔹 Devolver todo junto
			return new ResponseEntity<>(mapa, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Error: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public OperacionCaja getOperacionCaja(int id) {
		OperacionCaja ope = operacionRepository.getOperacionCajaPorId(id);
		OperacionCaja operacion = null;
		if (ope != null) {
			operacion = new OperacionCaja();
			operacion.setId(ope.getId());
			operacion.setVuelto(ope.getVuelto());
			operacion.setEfectivo(ope.getEfectivo());
			operacion.setAperturaCaja(ope.getAperturaCaja());
			operacion.setTipoOperacion(ope.getTipoOperacion());
			operacion.setConcepto(ope.getConcepto());
			operacion.setReferenciaTipoOperacion(ope.getReferenciaTipoOperacion());
			operacion.setMotivo(ope.getMotivo());
			operacion.setMonto(ope.getMonto());
		} else {
			operacion = null;
		}
		return operacion;
	}

	@Transactional
	public CuentaCobrarCabecera procesarCuentaCobrar(Venta venta, CuentaCobrarCabecera ent) {
		if (ent.getId() != 0) {
			ent.getVenta().setId(venta.getId());
			ent.setFecha(new Date());
			// 1) Obtener fecha de vencimiento de la última cuota
			Date ultimaFecha = null;
			for (CuentaCobrarDetalle d : ent.getCuentaCobrarDetalle()) {
				if (ultimaFecha == null || d.getFechaVencimiento().after(ultimaFecha)) {
					ultimaFecha = d.getFechaVencimiento();
				}
			}

			// Setear en cabecera
			ent.setFechaVencimiento(ultimaFecha);
			CuentaCobrarCabecera saveCuenta = cuentaCobrarRepository.save(ent);
			for (CuentaCobrarDetalle det : ent.getCuentaCobrarDetalle()) {
				CuentaCobrarDetalle detalle = new CuentaCobrarDetalle();
				detalle.setId(det.getId());
				detalle.setNumeroCuota(det.getNumeroCuota());
				detalle.setMonto(det.getMonto());
				detalle.setSubTotal(det.getSubTotal());
				detalle.setFechaVencimiento(det.getFechaVencimiento());
				detalle.setEstado(det.isEstado());
				detalle.setImporte(det.getImporte());
				detalle.getCuentaCobrarCabecera().setId(saveCuenta.getId());
				cuentaCobrarDetalleRepository.save(detalle);

			}
			return saveCuenta;
		} else {
			ent.getVenta().setId(venta.getId());
			ent.setFecha(new Date());
			// 1) Obtener fecha de vencimiento de la última cuota
			Date ultimaFecha = null;
			for (CuentaCobrarDetalle d : ent.getCuentaCobrarDetalle()) {
				if (ultimaFecha == null || d.getFechaVencimiento().after(ultimaFecha)) {
					ultimaFecha = d.getFechaVencimiento();
				}
			}
			// Setear en cabecera
			ent.setFechaVencimiento(ultimaFecha);
			CuentaCobrarCabecera saveCuenta = cuentaCobrarRepository.save(ent);
			// eliminarDetallePorCabecera(entity.getId());
			for (CuentaCobrarDetalle det : ent.getCuentaCobrarDetalle()) {
				CuentaCobrarDetalle detalle = new CuentaCobrarDetalle();
				detalle.setId(det.getId());
				detalle.setNumeroCuota(det.getNumeroCuota());
				detalle.setMonto(det.getMonto());
				detalle.setSubTotal(det.getSubTotal());
				detalle.setFechaVencimiento(det.getFechaVencimiento());
				detalle.setEstado(det.isEstado());
				detalle.setImporte(det.getImporte());
				detalle.getCuentaCobrarCabecera().setId(saveCuenta.getId());
				cuentaCobrarDetalleRepository.save(detalle);
			}
			return saveCuenta;
		}
	}

	@Transactional
	public OrdenPagare procesarOrdenPagared(CuentaCobrarCabecera cuc) {
		OrdenPagare op = new OrdenPagare();
		op.setTotal(cuc.getTotal());
		op.setTotalLetra(cuc.getTotalLetra());
		op.setFecha(new Date());
		op.setFechaVencimiento(cuc.getFechaVencimiento());
		op.getCliente().setId(cuc.getCliente().getId());
		op.getFuncionario().setId(cuc.getFuncionario().getId());
		op.getCuentaCobrarCabecera().setId(cuc.getId());
		op.setEstado("PENDIENTE");
		return ordenPagareRepository.save(op);
	}

	public List<CuentaCobrarCabecera> listadoCargarCuenta(List<CuentaCobrarCabecera> lis) {
		List<CuentaCobrarCabecera> listadoRetorno = new ArrayList<>();
		for (CuentaCobrarCabecera x : lis) {
			CuentaCobrarCabecera cuenta = new CuentaCobrarCabecera();
			cuenta.setId(x.getId());
			cuenta.setTotal(x.getTotal());
			cuenta.getVenta().getDocumento().setDescripcion(x.getVenta().getDocumento().getDescripcion());
			cuenta.getVenta().setNroDocumento(x.getVenta().getNroDocumento());
			cuenta.setPagado(x.getPagado());
			cuenta.setSaldo(x.getSaldo());
			cuenta.setTotalDevolucion(x.getTotalDevolucion());
			cuenta.setEntrega(x.getEntrega());
			cuenta.setFechaVencimiento(x.getFechaVencimiento());
			cuenta.getCliente().setId(x.getCliente().getId());
			cuenta.getCliente().getPersona().setNombre(x.getCliente().getPersona().getNombre());
			cuenta.getCliente().getPersona().setApellido(x.getCliente().getPersona().getApellido());
			cuenta.getCliente().getPersona().setCedula(x.getCliente().getPersona().getCedula());
			cuenta.getFuncionario().getPersona().setNombre(x.getFuncionario().getPersona().getNombre());
			cuenta.getFuncionario().getPersona().setApellido(x.getFuncionario().getPersona().getApellido());
			cuenta.setFecha(x.getFecha());
			cuenta.getVenta().setId(x.getVenta().getId());
			cuenta.getVenta().setFechaFactura(x.getVenta().getFechaFactura());
			cuenta.getVenta().setDetalleProducto(null);
			cuenta.getVenta().setEntrega(x.getVenta().getEntrega());
			cuenta.getVenta().setDetalleServicio(null);
			cuenta.getVenta().setTotal(x.getVenta().getTotal());
			cuenta.getVenta().setTotalDevolucion(x.getVenta().getTotalDevolucion());
			cuenta.getVenta().setFecha(sumarDia(x.getFecha(), (24 * x.getTipoPlazo().getValor())));
			cuenta.getTipoPlazo().setValor(validarDiaAtraso(x.getVenta().getFecha()));
			cuenta.setEntrega(x.getEntrega());
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
	}

	public int validarDiaAtraso(Date fecha2) {
		int diaAtraso = 0;
		Date fechaHoy = new Date();
		if (fechaHoy.getTime() <= fecha2.getTime()) {
			diaAtraso = 0;
		} else {
			diaAtraso = diferenciaDias(new Date(), fecha2);
		}
		return diaAtraso;
	}

	public int diferenciaDias(Date fechaMayor, Date fechaMenor) {
		long diferencia = sumarDia(fechaMayor, 24).getTime() - fechaMenor.getTime();
		long dias = diferencia / (1000 * 60 * 60 * 24);
		return (int) dias;
	}

	private ResponseEntity<?> validarNotaCredito(List<OperacionCaja> listaOperacion, int nota) {
		if (nota == 0) {
			return error("EL NUMERO DE NOTA CREDITO NO SE HA PODIDO CARGAR");
		}
		if (listaOperacion == null || listaOperacion.isEmpty()) {
			return error("NO EXISTEN OPERACIONES DE CAJA PARA VALIDAR");
		}
		for (OperacionCaja entity : listaOperacion) {
			if (entity.getAperturaCaja() == null || entity.getAperturaCaja().getId() > 0) {
				return error("SE DEBE CARGAR LOS DATOS DE LA APERTURA CAJA");
			}
			if (entity.getConcepto() == null || entity.getConcepto().getId() > 0) {
				return error("SE DEBE CARGAR EL CONCEPTO DE LA OPERACION CAJA");
			}
			if (entity.getTipoOperacion() == null || entity.getTipoOperacion().getId() > 0) {
				return error("SE DEBE CARGAR EL TIPO DE OPERACION EN CAJA");
			}

			if (entity.getMonto() <= 0) {
				return error("EL MONTO DEBE SER MAYOR A CERO");
			}

			AperturaCaja cC = aperturaCajaRepository.getAperturaCajaPorIdCaja(entity.getAperturaCaja().getId());

			if (cC == null) {
				return error("EL FUNCIONARIO NO POSEE UNA APERTURA CAJA A SU NOMBRE");
			}

			if (entity.getConcepto().getId() == 29) {

				if (entity.getTipoOperacion().getId() == 1 && cC.getSaldoActual() < entity.getMonto()) {
					return error("EL EFECTIVO DISPONIBLE EN LA CAJA NO ES SUFICIENTE");
				}
				if (entity.getTipoOperacion().getId() == 2 && cC.getSaldoActualCheque() < entity.getMonto()) {
					return error("EL SALDO DE CHEQUE DISPONIBLE NO ES SUFICIENTE");
				}
				if (entity.getTipoOperacion().getId() == 3 && cC.getSaldoActualTarjeta() < entity.getMonto()) {

					return error("EL SALDO DE TARJETA DISPONIBLE NO ES SUFICIENTE");
				}
			}
		}

		return null;
	}

	@Transactional
	private List<OperacionCaja> procesarNotaCreditoOperacion(List<OperacionCaja> listaOperacion, int idNota,
			int idVenta) {

		if (listaOperacion == null || listaOperacion.isEmpty()) {
			throw new RuntimeException("No existen operaciones de caja para procesar");
		}

		List<OperacionCaja> resultado = new ArrayList<>();

		// 🔥 Crear cabecera si todavía no existe
		OperacionCajaCabecera cabecera = new OperacionCajaCabecera();

		cabecera.setFecha(new Date());
		cabecera.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cabecera.setReferenciaOperacion(idNota);
		cabecera.getAperturaCaja().setId((listaOperacion.get(0).getAperturaCaja().getId()));
		Concepto c = conceptoRepository.findById(listaOperacion.get(0).getConcepto().getId())
				.orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		cabecera.setMotivo(c.getDescripcion() + " REF.: " + idNota);
		if (c.getId() == 28) {
			cabecera.setTipo("ENTRADA");
		}
		if (c.getId() == 29) {
			cabecera.setTipo("SALIDA");
		}
		cabecera.getConcepto().setId(c.getId());
		cabecera = operacionCajaCabeceraRepository.save(cabecera);

		for (OperacionCaja entity : listaOperacion) {
			entity.getOperacionCajaCabecera().setId(cabecera.getId());
			if (entity.getConcepto().getId() == 28) {
				if (entity.getTipoOperacion().getId() == 1) {
					aperturaCajaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(),
							entity.getMonto());
				}
				if (entity.getTipoOperacion().getId() == 2) {
					aperturaCajaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getId(),
							entity.getMonto());
				}
				if (entity.getTipoOperacion().getId() == 3) {
					aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getId(),
							entity.getMonto());
				}
				entity.setTipo("ENTRADA");
			}

			else if (entity.getConcepto().getId() == 29) {
				if (entity.getTipoOperacion().getId() == 1) {
					aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(
							entity.getAperturaCaja().getId(), entity.getMonto());
				}
				if (entity.getTipoOperacion().getId() == 2) {
					aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(
							entity.getAperturaCaja().getId(), entity.getMonto());
				}
				if (entity.getTipoOperacion().getId() == 3) {
					aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(
							entity.getAperturaCaja().getId(), entity.getMonto());
				}
				entity.setTipo("SALIDA");
			}
			entity.setMotivo(c.getDescripcion() + " REF.: " + idNota);
			entity.setReferenciaOperacion(idVenta);
			entity.setFecha(new Date());
			OperacionCaja op = operacionRepository.save(entity);
			resultado.add(op);
		}

		// 🔥 Actualizar estado nota crédito una sola vez
		notaCreditoRepository.findByActualizarEstadoNotaCredito(idNota, idVenta, "CERRADO");

		return resultado;
	}

	@Transactional
	public List<OperacionCaja> procesarOperacionCajaVenta(Venta ent, List<OperacionCaja> listaOperacion) {

		List<OperacionCaja> resultado = new ArrayList<>();

		if (listaOperacion == null || listaOperacion.isEmpty()) {
			throw new RuntimeException("No existen operaciones de caja para procesar");
		}

		// 🔥 Crear cabecera si todavía no existe
		OperacionCajaCabecera cab = new OperacionCajaCabecera();
		cab.setFecha(new Date());
		cab.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cab.setReferenciaOperacion(ent.getId());
		cab.getAperturaCaja().setId((listaOperacion.get(0).getAperturaCaja().getId()));
		Concepto c = conceptoRepository.findById(listaOperacion.get(0).getConcepto().getId())
				.orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		cab.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
		cab.getConcepto().setId(c.getId());
		cab.setTipo("ENTRADA");
		AperturaCaja ape = aperturaCajaRepository
				.getAperturaCajaPorIdCaja(listaOperacion.get(0).getAperturaCaja().getId());
		OperacionCajaCabecera savedCabecera = operacionCajaCabeceraRepository.save(cab);
		for (OperacionCaja ope : listaOperacion) {
			ope.setTipo("ENTRADA");
			ope.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
			ope.setReferenciaOperacion(ent.getId());
			ope.setFecha(new Date());
			ope.setAperturaCaja(ape);
			// 🔥 Asociar cabecera
			ope.getOperacionCajaCabecera().setId(savedCabecera.getId());
			// 🔥 Actualizar saldos según tipo operación
			if (ope.getTipoOperacion().getId() == 1) {
				aperturaCajaRepository.findByActualizarAperturaSaldo(ope.getAperturaCaja().getId(), ope.getMonto());
			}
			if (ope.getTipoOperacion().getId() == 2) {
				aperturaCajaRepository.findByActualizarAperturaSaldoCheque(ope.getAperturaCaja().getId(),
						ope.getMonto());
			}
			if (ope.getTipoOperacion().getId() == 3) {
				aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(ope.getAperturaCaja().getId(),
						ope.getMonto());
			}
			OperacionCaja saved = operacionRepository.save(ope);

			resultado.add(saved);
		}
		// actualiza el id de la operacion en referencia
		entityRepository.findByActualizarVentaOperacion(ent.getId(), savedCabecera.getId());
		return resultado;
	}

	@Transactional
	public Venta guardarVentaYDetalles(Venta entity, int ter) {
		Double totalGenerales = 0.0;
		Double descuentoGenerales = 0.0;
		Map<String, Object> map = new HashMap<>();
		try {
			if (!entity.getObs().equals("")) {
				entity.setObs(entity.getObs().toUpperCase());
			}
			if (entity.getId() != 0) {
				entity.setFecha(new Date());
				entity.setHora(hora());
				System.out.println("EDITA VENTA SI VIENE ENTREGA Y EL TIPO " + entity.getTipo() + " ENTREGA "
						+ entity.getEntrega());
				if (entity.getEstado().equals("FACTURADO")) {
					entity.setFechaFactura(new Date());

				} else if (entity.getEstado().equals("FACTURAR")) {
					entity.setNroDocumento("");
					entity.setFechaFactura(null);
				}
				
				Map<String, DetalleProducto> productosAgrupados = new LinkedHashMap<>();
				List<DetalleProducto> listaFinal = new ArrayList<>();
				List<Integer> idsParaEliminar = new ArrayList<>();
				for (DetalleProducto detalle : entity.getDetalleProducto()) {
				    Integer idProducto = detalle.getProducto().getId();
				    Double descuento = detalle.getDescuento() == null ? 0.0  : detalle.getDescuento();
				    // RECALCULAR SUBTOTAL
				    detalle.setSubTotal((detalle.getCantidad() * detalle.getPrecio())
				        - (descuento * detalle.getCantidad())
				    );
				    // ARTICULO VARIOS -> NO AGRUPAR
				    if (Integer.valueOf(1).equals(idProducto)
				            || "15".equals(detalle.getProducto().getCodbar())) {

				        listaFinal.add(detalle);
				        continue;
				    }
				    // AGRUPAR SOLO POR PRODUCTO
				    String key = String.valueOf(idProducto);
				    if (productosAgrupados.containsKey(key)) {
				        DetalleProducto existente =  productosAgrupados.get(key);
				        DetalleProducto conservar;
				        DetalleProducto eliminar;
				        // ==========================================
				        // SI EL NUEVO TIENE ID
				        // CONSERVAR EL NUEVO
				        // ==========================================
				        if (detalle.getId() != null) {
				            conservar = detalle;
				            eliminar = existente;
				            productosAgrupados.put(key, conservar);
				        } else {
				            // ==========================================
				            // SI EL NUEVO NO TIENE ID
				            // CONSERVAR EL EXISTENTE
				            // ==========================================
				            conservar = existente;
				            eliminar = detalle;
				        }
				        // ==========================================
				        // SUMAR CANTIDAD
				        // ==========================================
				        conservar.setCantidad(conservar.getCantidad() + eliminar.getCantidad()
				        );
				        // ==========================================
				        // RECALCULAR SUBTOTAL
				        // ==========================================
				        Double descuentoConservar =conservar.getDescuento() == null ? 0.0 : conservar.getDescuento();
				        conservar.setSubTotal((conservar.getCantidad() * conservar.getPrecio())
				            - (descuentoConservar * conservar.getCantidad())
				        );
				        // ==========================================
				        // SI EL QUE SE ELIMINA YA EXISTE EN BD
				        // MARCAR PARA ELIMINAR
				        // ==========================================
				        if (eliminar.getId() != null) {
				            idsParaEliminar.add(eliminar.getId());
				        }
				    } else {
				        productosAgrupados.put(key, detalle);
				    }
				}
				// ==========================================
				// ELIMINAR DE BD LOS DETALLES REPETIDOS
				// ==========================================
				for (Integer id : idsParaEliminar) {
				    detalleProductoRepository.deleteById(id);
				    //console.log();
				    System.out.println("id eliminado: "+id);
				}
				// ==========================================
				// ARMAR LISTA FINAL
				// ==========================================
				listaFinal.addAll(productosAgrupados.values());
				entity.setDetalleProducto(listaFinal);
				
				double total10 = 0, total5 = 0, totalCostoPromedio = 0, costoReal = 0.0;
				double grabado10 = 0, grabado5 = 0, grabadoExcenta = 0;
				if (entity.getDetalleProducto().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							costoReal = costoReal + detalleProducto.getSubTotalCosto();
							totalGenerales = totalGenerales
									+ (detalleProducto.getPrecio() * detalleProducto.getCantidad());
							descuentoGenerales = descuentoGenerales
									+ (detalleProducto.getDescuento() * detalleProducto.getCantidad());
							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));

							if (detalleProducto.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleProducto.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleProducto.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleProducto.getSubTotal();
								detalleProducto.setMontoIva(0.0);
							}

							Double cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							System.out.println("ccoçp: montos: " + cpp);
							if (cpp == null) {
								if (detalleProducto.getCosto() <= 0 || detalleProducto.getCosto() == null) {
									cpp = detalleProducto.getPrecio() * 0.50;// por defecto agarrar el 50 poricento del
																				// precio
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
								} else {
									cpp = detalleProducto.getCosto();
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());

								}
							} else {
								detalleProducto.setCostoPromedio(cpp);
								totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
								detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
							}

							detalleProducto.setCosto(detalleProducto.getCosto() * detalleProducto.getCantidad());
							DetalleProducto detpro = detalleProductoRepository.save(detalleProducto);
							this.actualizarProductoBase(detpro.getProducto().getId(), detpro.getCantidad(),
									detpro.getSubTotal(), detpro.getPrecio(), entity.getFuncionario().getId(),
									entity.getTipo(), entity.getId());

						}

					}

					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							totalGenerales = totalGenerales
									+ (detalleProducto.getPrecio() * detalleProducto.getCantidad());
							costoReal = costoReal + detalleProducto.getSubTotalCosto();
							descuentoGenerales = descuentoGenerales
									+ (detalleProducto.getDescuento() * detalleProducto.getCantidad());
							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							detalleProductoRepository.save(detalleProducto);
						}
					}

				}
				if (entity.getDetalleServicio().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							totalGenerales = totalGenerales
									+ (detalleServicio.getPrecio() * detalleServicio.getCantidad());
							// = descuentoGenerales + (detalleServicio.getDescuento() *
							// detalleProducto.getCantidad());

							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleServicio.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleServicio.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleServicio.getSubTotal();
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);
						}
					}
					if (entity.getEstado().equals("FACTURAR") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							detalleServicio.getVenta().setId(entity.getId());
							totalGenerales = totalGenerales
									+ (detalleServicio.getPrecio() * detalleServicio.getCantidad());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							// detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(),
							// detalleServicio.getPrecio()));
							detalleServicioRepository.save(detalleServicio);
						}
					}
				}
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());
					Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());

					if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado() == true) {
						AsientoContableDTO dto = new AsientoContableDTO();
						if (entity.getTipo().equals("1")) {
							dto.setConceptoId(1);
							dto.setTipoReferencia("VENTA CONTADO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() == 0) {
							dto.setConceptoId(2);
							dto.setTipoReferencia("VENTA CREDITO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() > 0) {
							dto.setConceptoId(24);
							dto.setTipoReferencia("VENTA CREDITO CON ENTREGA INICIAL");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							mon.put("BASE_ENTREGA", BigDecimal.valueOf(entity.getEntrega()));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}

						dto.setReferenciaId(entity.getId());
						dto.setFuncionarioRegistroId(entity.getFuncionario().getId());
						dto.setFuncionarioModificacionId(entity.getFuncionario().getId());
						ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);
						System.out.println(retorString.getBody().equals("SAVE") + " 88888          ");
						Object body = retorString.getBody();
						if (body instanceof CustomerErrorType) {
							String mensaje = ((CustomerErrorType) body).getErrorMessage();
							if ("SAVE".equals(mensaje)) {
								// correcto
								System.out.println("asientoooo guardado");
							} else {
								// return new ResponseEntity<>(new CustomerErrorType("HUBO UN ERROR AL INTENTAR
								// GUARDAR ASIENTO DE VENTA"), HttpStatus.CONFLICT);
							}
						}
					}

				}
				entity.setTotalCosto(costoReal);
				entity.setTotalIvaDies(total10);
				entity.setTotalIvaCinco(total5);
				entity.setGrabadoIvaDies(grabado10);
				entity.setGrabadoIvaCinco(grabado5);
				entity.setGrabadoExcenta(grabadoExcenta);
				entity.setTotalIva(total10 + total5);
				entity.setTotalDescuento(descuentoGenerales);
				entity.setTotal(totalGenerales - descuentoGenerales);
				entity.setTotalCostoPromedio(totalCostoPromedio);
				System.out.println(entity.getFechaFactura());
				if ((entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA"))
						&& entity.getNroDocumento().equals("")) {
					map = generarDocumentoUnificado(entity.getDocumento().getId(), ter, entity.getId());
					if (map.get("numeroDocumento") != null && !map.get("numeroDocumento").toString().isEmpty()) {
						entity.setNroDocumento(map.get("numeroDocumento").toString());
					}
					if (map.get("timbrado") != null && !map.get("timbrado").toString().isEmpty()) {
						entity.setTimbrado(map.get("timbrado").toString());
					}
					if (map.get("fechaInicioVigencia") != null
							&& !map.get("fechaInicioVigencia").toString().isEmpty()) {
						entity.setTimbradoInicio(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaInicioVigencia").toString()));
					}
					if (map.get("fechaFinVigencia") != null && !map.get("fechaFinVigencia").toString().isEmpty()) {
						entity.setTimbradoFin(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaFinVigencia").toString()));
					}

				}

				entity = entityRepository.save(entity);
				System.out.println("Venta ID: " + entity.getId() + " - TipoDocumento: " + entity.getDocumento().getId()
						+ " - NroDocumento: " + entity.getNroDocumento());

				pdfPrintss(entity.getId(), ter, entity.getDocumento().getDescripcion(), entity.getDocumento().getId());

			} else {

				entity.setFecha(new Date());
				entity.setHora(hora());
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					entity.setFechaFactura(new Date());
					// entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(),
					// numeroTerminal, idv));
					// actualizarLoteDocumentos(entity.getDocumento().getId());
				} else if (entity.getEstado().equals("FACTURAR")) {
					entity.setNroDocumento("");
					entity.setFechaFactura(null);

				}
				entity = entityRepository.save(entity); // ahora entity tiene el ID asignado
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					entity.setFechaFactura(new Date());
					System.out.println("ejecuto id=0");
					// entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(),
					// numeroTerminal, id.getId()));
					// numeroFacturaRetorno = entity.getNroDocumento();
					// actualizarLoteDocumentos(entity.getDocumento().getId(), numeroTerminal,
					// id.getId());
				}
				System.out.println(entity.getFecha());
				System.out.println(entity.getFechaFactura() + "  ******");
				double total10 = 0, total5 = 0, totalCostoPromedio = 0.0, costoReal = 0.0;
				double grabado10 = 0, grabado5 = 0, grabadoExcenta = 0;
				if (entity.getDetalleProducto().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							totalGenerales = totalGenerales
									+ (detalleProducto.getPrecio() * detalleProducto.getCantidad());
							costoReal = costoReal + detalleProducto.getSubTotalCosto();
							descuentoGenerales = descuentoGenerales
									+ (detalleProducto.getDescuento() * detalleProducto.getCantidad());

							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							if (detalleProducto.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleProducto.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleProducto.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleProducto.getSubTotal();
								detalleProducto.setMontoIva(0.0);
							}
							Double cpp = 0.0;
							cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							System.out.println("ccoçp: montos: " + cpp);
							if (cpp == null) {
								if (detalleProducto.getCosto() <= 0 || detalleProducto.getCosto() == null) {
									cpp = detalleProducto.getPrecio() * 0.50;// por defecto agarrar el 50 poricento del
																				// precio
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
								} else {
									cpp = detalleProducto.getCosto();
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());

								}
							} else {
								detalleProducto.setCostoPromedio(cpp);
								totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
								detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
							}

							detalleProducto.setCosto(detalleProducto.getCosto() * detalleProducto.getCantidad());
							detalleProductoRepository.save(detalleProducto);
							// subtotal, precio, idFuncionario, tipo, idVenta
							this.actualizarProductoBase(detalleProducto.getProducto().getId(),
									detalleProducto.getCantidad(), detalleProducto.getSubTotal(),
									detalleProducto.getPrecio(), entity.getFuncionario().getId(), entity.getTipo(),
									entity.getId());
						}

					}
					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							totalGenerales = totalGenerales
									+ (detalleProducto.getPrecio() * detalleProducto.getCantidad());
							costoReal = costoReal + detalleProducto.getSubTotalCosto();
							descuentoGenerales = descuentoGenerales
									+ (detalleProducto.getDescuento() * detalleProducto.getCantidad());

							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							detalleProductoRepository.save(detalleProducto);
						}
					}
				}

				if (entity.getDetalleServicio().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							totalGenerales = totalGenerales
									+ (detalleServicio.getPrecio() * detalleServicio.getCantidad());
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							if (detalleServicio.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleServicio.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleServicio.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleServicio.getSubTotal();
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);
						}

					}
					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							totalGenerales = totalGenerales
									+ (detalleServicio.getPrecio() * detalleServicio.getCantidad());
							detalleServicio.setId(0);
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							// detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(),
							// detalleServicio.getPrecio()));
							detalleServicioRepository.save(detalleServicio);
						}
					}

				}
				System.out.println("");
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());
					Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());

					if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado() == true) {
						AsientoContableDTO dto = new AsientoContableDTO();
						if (entity.getTipo().equals("1")) {
							dto.setConceptoId(1);
							dto.setTipoReferencia("VENTA CONTADO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() == 0) {
							dto.setConceptoId(2);
							dto.setTipoReferencia("VENTA CREDITO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() > 0) {
							dto.setConceptoId(24);
							dto.setTipoReferencia("VENTA CREDITO CON ENTREGA INICIAL");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							mon.put("BASE_ENTREGA", BigDecimal.valueOf(entity.getEntrega()));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}

						dto.setReferenciaId(entity.getId());
						dto.setFuncionarioRegistroId(entity.getFuncionario().getId());
						dto.setFuncionarioModificacionId(entity.getFuncionario().getId());
						AsientoContable c = new AsientoContable();
						ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);
						System.out.println(retorString.getBody().equals("SAVE") + " 88888          ");
						Object body = retorString.getBody();
						if (body instanceof CustomerErrorType) {
							String mensaje = ((CustomerErrorType) body).getErrorMessage();
							if ("SAVE".equals(mensaje)) {
								// correcto
								System.out.println("asientoooo guardado");
							} else {
								// return new ResponseEntity<>(new CustomerErrorType("HUBO UN ERROR AL INTENTAR
								// GUARDAR ASIENTO DE VENTA"), HttpStatus.CONFLICT);
							}
						}
					}
				}
				System.out.println("TOTAL ENVIADO: " + entity.getTotal() + " TOTAL CALCULADO: " + totalGenerales);
				entity.setTotalCosto(costoReal);
				entity.setTotalIvaDies(total10);
				entity.setTotalIvaCinco(total5);
				entity.setGrabadoIvaDies(grabado10);
				entity.setGrabadoIvaCinco(grabado5);
				entity.setGrabadoExcenta(grabadoExcenta);
				entity.setTotalIva(total10 + total5);
				entity.setTotalDescuento(descuentoGenerales);
				entity.setTotal(totalGenerales - descuentoGenerales);
				entity.setTotalCostoPromedio(totalCostoPromedio);
				System.out.println(entity.getFechaFactura());
				if ((entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA"))
						&& entity.getNroDocumento().equals("")) {
					map = generarDocumentoUnificado(entity.getDocumento().getId(), ter, entity.getId());
					if (map.get("numeroDocumento") != null && !map.get("numeroDocumento").toString().isEmpty()) {
						entity.setNroDocumento(map.get("numeroDocumento").toString());
					}
					if (map.get("timbrado") != null && !map.get("timbrado").toString().isEmpty()) {
						entity.setTimbrado(map.get("timbrado").toString());
					}
					if (map.get("fechaInicioVigencia") != null
							&& !map.get("fechaInicioVigencia").toString().isEmpty()) {
						entity.setTimbradoInicio(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaInicioVigencia").toString()));
					}
					if (map.get("fechaFinVigencia") != null && !map.get("fechaFinVigencia").toString().isEmpty()) {
						entity.setTimbradoFin(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaFinVigencia").toString()));
					}
				}
				entity = entityRepository.save(entity);
				System.out.println("Venta ID: " + entity.getId() + " - TipoDocumento: " + entity.getDocumento().getId()
						+ " - NroDocumento: " + entity.getNroDocumento());
				pdfPrintss(entity.getId(), ter, entity.getDocumento().getDescripcion(), entity.getDocumento().getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// agregarDatosCliente:
		// entity.setCliente(clienteRepository.getIdCliente(entity.getCliente().getId()));
		// agregarDatosFuncionarioVendedor:
		// entity.setFuncionarioV(funcionarioRepository.getIdFuncionario(entity.getFuncionarioV().getId()));
		// agregarDatosFuncionarioRegistro:
		// entity.setFuncionario(funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId()));
		// agregarDatosFuncionarioReparto:
		// entity.setFuncionarioR(funcionarioRepository.getIdFuncionario(entity.getFuncionarioR().getId()));
		// System.out.println(entity.getTotalIvaDies() + " "+
		// entity.getTotalIvaCinco());
		// agregarDatosDetalleProducto:
		// entity.setDetalleProducto(detalleProductoRepository.getDetallePorCabecera(entity.getId()));
		// agregarDatosDetalleServicios:
		// entity.setDetalleServicio(detalleServicioRepository.getDetallePorCabecera(entity.getId()));
		// System.out.println("ID VET: RESS: "+entity.getId());
		return entity;
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "scs/{numeroTerminal}")
	public ResponseEntity<?> guardar(@RequestBody Venta entity, @PathVariable int numeroTerminal) {
		Double totalGenerales = 0.0;
		Double descuentoGenerales = 0.0;
		Map<String, Object> map = new HashMap<>();
		TerminalConfigImpresora terminal = terminalRepository
				.consultarTerminalEmisonFacturaPorTerminales(numeroTerminal);
		System.out.println(terminal.getEstadoEmisionFactura());
		System.out.println(entity.getDocumento().getId());
		try {
			if (entity.getTipo().equals("CONTADO") || entity.getTipo().equals("1")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + entity.getTipo());
				entity.setTipo("1");
			}
			if (entity.getTipo().equals("CREDITO") || entity.getTipo().equals("2")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + entity.getTipo());
				entity.setTipo("2");
			}
			if (entity.getTipo().equals("NOTA CREDITO") || entity.getTipo().equals("3")) {
				System.out.println("VINO ESTE EL TIPO VENTA: " + entity.getTipo());
				entity.setTipo("3");
			}
			// 1. Validar si hay al menos una zona registrada
			if (zonaRepository.count() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR AL MENOS UNA ZONA EN EL SISTEMA"),
						HttpStatus.CONFLICT);
			}

			// 2. Si no hay zona asignada o id es 0, asignar la primera zona registrada
			if (entity.getZona() == null || entity.getZona().getId() == 0) {
				// Buscar la primera zona (puede ser la de menor ID)
				System.out.println("entro id 0 o null y asina el primer valor");
				Optional<Zona> primeraZonaOpt = zonaRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
						.findFirst();
				if (!primeraZonaOpt.isPresent()) {
					return new ResponseEntity<>(new CustomerErrorType("ERROR AL ASIGNAR ZONA POR DEFECTO"),
							HttpStatus.CONFLICT);
				}
				entity.setZona(primeraZonaOpt.get());
			}
			if (terminal != null && entity.getDocumento().getId() == 1) {
				// Validar que tenga autoimpresor asignado
				if (terminal.getEstadoEmisionFactura() == false) {
					return new ResponseEntity<>(
							new CustomerErrorType("Esta terminal no está hablitado para emitir factura"),
							HttpStatus.CONFLICT);
				}
				// Validar que el autoimpresor esté activo
				if (!terminal.getAutoImpresor().isEstado()) {
					return new ResponseEntity<>(new CustomerErrorType("La autorización del timbrado esta desabilitado"),
							HttpStatus.CONFLICT);
				}

				// Validar rango de numeración
				if (terminal.getAutoImpresor().getNumeroActual() < terminal.getAutoImpresor().getRangoInicio()) {
					return new ResponseEntity<>(
							new CustomerErrorType("La numeración actual del timbrado está fuera de rango inicial"),
							HttpStatus.CONFLICT);
				}
				if (terminal.getAutoImpresor().getNumeroActual() > terminal.getAutoImpresor().getRangoFin()) {
					// Ya no se puede emitir más facturas
					return new ResponseEntity<>(new CustomerErrorType(
							"La numeración actual del timbrado está fuera de rango final o ya alcanzo cantidad para emitir"),
							HttpStatus.CONFLICT);
				}

				// Validar timbrado
				if (terminal.getAutoImpresor().getTimbrado() == null
						|| terminal.getAutoImpresor().getTimbrado().isEmpty()) {
					return new ResponseEntity<>(new CustomerErrorType("El auto impresor no tiene un timbrado válido"),
							HttpStatus.CONFLICT);
				}

				// Validar fechas
				if (terminal.getAutoImpresor().getFechaInicioVigencia() == null
						|| terminal.getAutoImpresor().getFechaFinVigencia() == null) {
					return new ResponseEntity<>(
							new CustomerErrorType("El auto impresor no tiene fecha de vigencia definida"),
							HttpStatus.CONFLICT);
				}

				LocalDate hoy = LocalDate.now();
				if (hoy.isBefore(terminal.getAutoImpresor().getFechaInicioVigencia())
						|| hoy.isAfter(terminal.getAutoImpresor().getFechaFinVigencia())) {
					return new ResponseEntity<>(new CustomerErrorType("EL TIMBRADO DEL AUTOIMPRESOR NO ESTÁ VIGENTE"),
							HttpStatus.CONFLICT);
				}
			}

			if (entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getFuncionarioV().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO VENDEDOR NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getFuncionarioR().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REPARTIDOR NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getCliente().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getDetalleProducto().size() == 0 && entity.getDetalleServicio().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getTotal() <= 0 || entity.getTotal() == null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA VENTA DEBE SER MAYOR A CERO!"),
						HttpStatus.CONFLICT);
			} else if (entity.getTotalLetra().equals("") || entity.getTotalLetra() == null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL MONTO EN LETRA NO DEBE QUEDAR VACIO!"),
						HttpStatus.CONFLICT);
			} else if (estadoClienteBloqueo(entity.getCliente().getId(), entity.getTotal()) == true
					&& entity.getTipo().equals("2") && entity.getEstado().equals("FACTURADO")) {
				return new ResponseEntity<>(
						new CustomerErrorType(
								"NO SE PUEDE FACTURAR VENTAS, CLIENTE BLOQUEADO POR EXCEDER LINEA DE CREDITO!!"),
						HttpStatus.CONFLICT);
			} else if (entity.getTipo().equals("1") || entity.getTipo().equals("Contado")
					|| entity.getTipo().equals("CONTADO")) {
				entity.setTipo("1");
				System.out.println("entro validacion tipo venta ct o 1");
			} else if (entity.getTipo().equals("2") || entity.getTipo().equals("Credito")
					|| entity.getTipo().equals("CREDITO")) {
				entity.setTipo("2");
				System.out.println("entro validacion tipo venta cr o 2");

			} else if (entity.getTipo().equals("3") || entity.getTipo().equals("Nota Credito")
					|| entity.getTipo().equals("NOTA CREDITO")) {
				entity.setTipo("3");
				System.out.println("entro validacion tipo venta nota cr o 3");
			} else if (entity.getObs() != null) {
				entity.setObs(entity.getObs().toUpperCase());
			}

			for (int ind = 0; ind < entity.getDetalleProducto().size(); ind++) {
				DetalleProducto pro = entity.getDetalleProducto().get(ind);

				if (pro.getCantidad() == null || pro.getCantidad() <= 0) {
					return new ResponseEntity<>(new CustomerErrorType(
							"LA CANTIDAD DEL DETALLE DEVOLUCION ITEM N°: " + (ind + 1) + ", NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				} else if (pro.getDescripcion() == null) {
					return new ResponseEntity<>(new CustomerErrorType(
							"LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				} else if (pro.getPrecio() == null || pro.getPrecio() <= 0) {
					return new ResponseEntity<>(
							new CustomerErrorType(
									"EL PRECIO DEL DETALLE PRODUCTO ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				}
				totalGenerales = totalGenerales + pro.getPrecio() * pro.getCantidad();
				descuentoGenerales = descuentoGenerales + pro.getDescuento() * pro.getCantidad();
			}
			for (int ind = 0; ind < entity.getDetalleServicio().size(); ind++) {
				DetalleServicios ser = entity.getDetalleServicio().get(ind);
				if (ser.getCantidad() == null || ser.getCantidad() <= 0) {
					return new ResponseEntity<>(new CustomerErrorType(
							"LA CANTIDAD DEL DETALLE SERVICIO ITEM N°: " + (ind + 1) + ", NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				} else if (ser.getDescripcion() == null) {
					return new ResponseEntity<>(new CustomerErrorType(
							"LA DESCRIPCIÓN DEL DETALLE SERVICIO ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				} else if (ser.getPrecio() == null || ser.getPrecio() <= 0) {
					return new ResponseEntity<>(
							new CustomerErrorType(
									"EL PRECIO DEL DETALLE SERVICIO ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				} else if (ser.getFuncionario().getId() == 0) {
					return new ResponseEntity<>(new CustomerErrorType(
							"EL FUNCIONARIO DEL DETALLE SERVICIO ITEM N°: " + (ind + 1) + " NO DEBE QUEDAR VACIO!"),
							HttpStatus.CONFLICT);
				}
				totalGenerales = totalGenerales + ser.getPrecio() * ser.getCantidad();
			}
			if (entity.getTotal() <= 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA VENTA NO DEBE SER <= 0!"),
						HttpStatus.CONFLICT);

			}
			System.out.println("Total cabecera sin convertir: " + entity.getTotal());
			System.out.println("Suma detalles sin convertir: " + totalGenerales);
			BigDecimal totalCabecera = BigDecimal.valueOf(entity.getTotal()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal totalDetalles = BigDecimal.valueOf(totalGenerales).setScale(2, RoundingMode.HALF_UP);
			BigDecimal totalDescuento = BigDecimal.valueOf(descuentoGenerales).setScale(2, RoundingMode.HALF_UP);

			// margen máximo permitido (por ejemplo 1 guaraní)
			BigDecimal totalCalculado = totalDetalles.subtract(totalDescuento);

			if (totalCabecera.subtract(totalCalculado).abs().compareTo(totalCabecera) > 0) {
				return error("EL TOTAL DE LA VENTA (" + totalCabecera
						+ ") NO COINCIDE CON LA SUMA DE LOS DETALLES MENOS EL DESCUENTO (" + totalCalculado + ")");
			}
			entity.setTotal(Double.parseDouble(totalCalculado + ""));
			if (entity.getId() != 0) {
				entity.setFecha(new Date());
				entity.setHora(hora());
				System.out.println("EDITA VENTA SI VIENE ENTREGA Y EL TIPO " + entity.getTipo() + " ENTREGA "
						+ entity.getEntrega());
				if (entity.getEstado().equals("FACTURADO")) {
					entity.setFechaFactura(new Date());

				} else if (entity.getEstado().equals("FACTURAR")) {
					entity.setNroDocumento("");
					entity.setFechaFactura(null);
				}
				double total10 = 0, total5 = 0, totalCostoPromedio = 0;
				double grabado10 = 0, grabado5 = 0, grabadoExcenta = 0;
				if (entity.getDetalleProducto().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {

						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {

							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));

							if (detalleProducto.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleProducto.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleProducto.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleProducto.getSubTotal();
								detalleProducto.setMontoIva(0.0);
							}
							Double cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							System.out.println("ccoçp: montos: " + cpp);
							if (cpp == null) {
								if (detalleProducto.getCosto() <= 0 || detalleProducto.getCosto() == null) {
									cpp = detalleProducto.getPrecio() * 0.50;// por defecto agarrar el 50 poricento del
																				// precio
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
								} else {
									cpp = detalleProducto.getCosto();
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());

								}
							} else {
								detalleProducto.setCostoPromedio(cpp);
								totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
								detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
							}
							detalleProducto.setCosto(detalleProducto.getCosto() * detalleProducto.getCantidad());
							DetalleProducto detpro = detalleProductoRepository.save(detalleProducto);
							this.actualizarProductoBase(detpro.getProducto().getId(), detpro.getCantidad(),
									detpro.getSubTotal(), detpro.getPrecio(), entity.getFuncionario().getId(),
									entity.getTipo(), entity.getId());

						}

					}

					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							detalleProductoRepository.save(detalleProducto);
						}
					}

				}
				if (entity.getDetalleServicio().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleServicio.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleServicio.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleServicio.getSubTotal();
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);
						}
					}
					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							// detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(),
							// detalleServicio.getPrecio()));
							detalleServicioRepository.save(detalleServicio);
						}
					}
				}
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());
					Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());

					if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado() == true) {
						AsientoContableDTO dto = new AsientoContableDTO();
						if (entity.getTipo().equals("1")) {
							dto.setConceptoId(1);
							dto.setTipoReferencia("VENTA CONTADO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() == 0) {
							dto.setConceptoId(2);
							dto.setTipoReferencia("VENTA CREDITO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() > 0) {
							dto.setConceptoId(24);
							dto.setTipoReferencia("VENTA CREDITO CON ENTREGA INICIAL");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(entity.getTotal()));
							mon.put("BASE_NETO", BigDecimal.valueOf(entity.getTotal() - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							mon.put("BASE_ENTREGA", BigDecimal.valueOf(entity.getEntrega()));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}

						dto.setReferenciaId(entity.getId());
						dto.setFuncionarioRegistroId(entity.getFuncionario().getId());
						dto.setFuncionarioModificacionId(entity.getFuncionario().getId());
						ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);
						System.out.println(retorString.getBody().equals("SAVE") + " 88888          ");
						Object body = retorString.getBody();
						if (body instanceof CustomerErrorType) {
							String mensaje = ((CustomerErrorType) body).getErrorMessage();
							if ("SAVE".equals(mensaje)) {
								// correcto
								System.out.println("asientoooo guardado");
							} else {
								return new ResponseEntity<>(
										new CustomerErrorType("HUBO UN ERROR AL INTENTAR GUARDAR ASIENTO DE VENTA"),
										HttpStatus.CONFLICT);
							}
						}
					}

				}
				System.out.println("TOTAL ENVIADO: " + entity.getTotal() + " TOTAL CALCULADO: " + totalGenerales);
				entity.setTotalIvaDies(total10);
				entity.setTotalIvaCinco(total5);
				entity.setGrabadoIvaDies(grabado10);
				entity.setGrabadoIvaCinco(grabado5);
				entity.setGrabadoExcenta(grabadoExcenta);
				entity.setTotalIva(total10 + total5);
				entity.setTotalDescuento(descuentoGenerales);
				entity.setTotal(totalGenerales - descuentoGenerales);
				entity.setTotalCostoPromedio(totalCostoPromedio);
				System.out.println(entity.getFechaFactura());
				if ((entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA"))
						&& entity.getNroDocumento().equals("")) {
					map = generarDocumentoUnificado(entity.getDocumento().getId(), numeroTerminal, entity.getId());
					if (map.get("numeroDocumento") != null && !map.get("numeroDocumento").toString().isEmpty()) {
						entity.setNroDocumento(map.get("numeroDocumento").toString());
					}
					if (map.get("timbrado") != null && !map.get("timbrado").toString().isEmpty()) {
						entity.setTimbrado(map.get("timbrado").toString());
					}
					if (map.get("fechaInicioVigencia") != null
							&& !map.get("fechaInicioVigencia").toString().isEmpty()) {
						entity.setTimbradoInicio(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaInicioVigencia").toString()));
					}
					if (map.get("fechaFinVigencia") != null && !map.get("fechaFinVigencia").toString().isEmpty()) {
						entity.setTimbradoFin(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaFinVigencia").toString()));
					}

				}

				entity = entityRepository.save(entity);
				System.out.println("Venta ID: " + entity.getId() + " - TipoDocumento: " + entity.getDocumento().getId()
						+ " - NroDocumento: " + entity.getNroDocumento());

				pdfPrintss(entity.getId(), numeroTerminal, entity.getDocumento().getDescripcion(),
						entity.getDocumento().getId());

			} else {

				entity.setFecha(new Date());
				entity.setHora(hora());
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					entity.setFechaFactura(new Date());
					// entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(),
					// numeroTerminal, idv));
					// actualizarLoteDocumentos(entity.getDocumento().getId());
				} else if (entity.getEstado().equals("FACTURAR")) {
					entity.setNroDocumento("");
					entity.setFechaFactura(null);

				}
				entity = entityRepository.save(entity); // ahora entity tiene el ID asignado
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					entity.setFechaFactura(new Date());
					System.out.println("ejecuto id=0");
					// entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(),
					// numeroTerminal, id.getId()));
					// numeroFacturaRetorno = entity.getNroDocumento();
					// actualizarLoteDocumentos(entity.getDocumento().getId(), numeroTerminal,
					// id.getId());
				}
				System.out.println(entity.getFecha());
				System.out.println(entity.getFechaFactura() + "  ******");
				double total10 = 0, total5 = 0, totalCostoPromedio = 0.0;
				double grabado10 = 0, grabado5 = 0, grabadoExcenta = 0;
				if (entity.getDetalleProducto().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							if (detalleProducto.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleProducto.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleProducto.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleProducto.getSubTotal());
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if (detalleProducto.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleProducto.getSubTotal();
								detalleProducto.setMontoIva(0.0);
							}
							Double cpp = 0.0;
							cpp = movEntradaSalidaRepository
									.getCostoPromedioPonderado(detalleProducto.getProducto().getId());
							System.out.println("ccoçp: montos: " + cpp);
							if (cpp == null) {
								if (detalleProducto.getCosto() <= 0 || detalleProducto.getCosto() == null) {
									cpp = detalleProducto.getPrecio() * 0.50;// por defecto agarrar el 50 poricento del
																				// precio
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
								} else {
									cpp = detalleProducto.getCosto();
									detalleProducto.setCostoPromedio(cpp);
									totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
									detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());

								}
							} else {
								detalleProducto.setCostoPromedio(cpp);
								totalCostoPromedio = totalCostoPromedio + (cpp * detalleProducto.getCantidad());
								detalleProducto.setSubTotalCostoPromedio(cpp * detalleProducto.getCantidad());
							}

							detalleProducto.setCosto(detalleProducto.getCosto() * detalleProducto.getCantidad());
							detalleProductoRepository.save(detalleProducto);
							// subtotal, precio, idFuncionario, tipo, idVenta
							this.actualizarProductoBase(detalleProducto.getProducto().getId(),
									detalleProducto.getCantidad(), detalleProducto.getSubTotal(),
									detalleProducto.getPrecio(), entity.getFuncionario().getId(), entity.getTipo(),
									entity.getId());
						}

					}
					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
							detalleProducto.getVenta().setId(entity.getId());
							detalleProducto.setTipoPrecio(
									validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							detalleProductoRepository.save(detalleProducto);
						}
					}
				}

				if (entity.getDetalleServicio().size() > 0) {
					if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							if (detalleServicio.getIva().equals("10 %")) {
								grabado10 = grabado10 + detalleServicio.getSubTotal();
								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("5 %")) {
								grabado5 = grabado5 + detalleServicio.getSubTotal();
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if (detalleServicio.getIva().equals("Exenta")) {
								grabadoExcenta = grabadoExcenta + detalleServicio.getSubTotal();
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);
						}

					}
					if (entity.getEstado().equals("FACTURAR")) {
						for (DetalleServicios detalleServicio : entity.getDetalleServicio()) {
							detalleServicio.setId(0);
							detalleServicio.getVenta().setId(entity.getId());
							if (detalleServicio.getObs() != null) {
								detalleServicio.setObs(detalleServicio.getObs().toUpperCase());
							} else {
								detalleServicio.setObs("");
							}
							// detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(),
							// detalleServicio.getPrecio()));
							detalleServicioRepository.save(detalleServicio);
						}
					}

				}
				System.out.println("");
				if (entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA")) {
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());
					Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);
					System.out.println("tipo : " + entity.getTipo() + " entrega " + entity.getEntrega());

					if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado() == true) {
						AsientoContableDTO dto = new AsientoContableDTO();
						if (entity.getTipo().equals("1")) {
							dto.setConceptoId(1);
							dto.setTipoReferencia("VENTA CONTADO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() == 0) {
							dto.setConceptoId(2);
							dto.setTipoReferencia("VENTA CREDITO");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}
						if (entity.getTipo().equals("2") && entity.getEntrega() > 0) {
							dto.setConceptoId(24);
							dto.setTipoReferencia("VENTA CREDITO CON ENTREGA INICIAL");
							Map<String, BigDecimal> mon = new HashMap<>();
							mon.put("BASE_VENTA", BigDecimal.valueOf(totalGenerales));
							mon.put("BASE_NETO", BigDecimal.valueOf(totalGenerales - (total10 + total5)));
							mon.put("BASE_IVA", BigDecimal.valueOf(total10 + total5));
							mon.put("BASE_COSTO", BigDecimal.valueOf(totalCostoPromedio));
							mon.put("BASE_ENTREGA", BigDecimal.valueOf(entity.getEntrega()));
							for (Map.Entry<String, BigDecimal> entry : mon.entrySet()) {
								System.out.println("Base: " + entry.getKey() + " → Valor: " + entry.getValue());
							}
							dto.setMontos(mon);
						}

						dto.setReferenciaId(entity.getId());
						dto.setFuncionarioRegistroId(entity.getFuncionario().getId());
						dto.setFuncionarioModificacionId(entity.getFuncionario().getId());
						AsientoContable c = new AsientoContable();
						ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);
						System.out.println(retorString.getBody().equals("SAVE") + " 88888          ");
						Object body = retorString.getBody();
						if (body instanceof CustomerErrorType) {
							String mensaje = ((CustomerErrorType) body).getErrorMessage();
							if ("SAVE".equals(mensaje)) {
								// correcto
								System.out.println("asientoooo guardado");
							} else {
								return new ResponseEntity<>(
										new CustomerErrorType("HUBO UN ERROR AL INTENTAR GUARDAR ASIENTO DE VENTA"),
										HttpStatus.CONFLICT);
							}
						}
					}
				}
				System.out.println("TOTAL ENVIADO: " + entity.getTotal() + " TOTAL CALCULADO: " + totalGenerales);
				entity.setTotalIvaDies(total10);
				entity.setTotalIvaCinco(total5);
				entity.setGrabadoIvaDies(grabado10);
				entity.setGrabadoIvaCinco(grabado5);
				entity.setGrabadoExcenta(grabadoExcenta);
				entity.setTotalIva(total10 + total5);
				entity.setTotalDescuento(descuentoGenerales);
				entity.setTotal(totalGenerales - descuentoGenerales);
				entity.setTotalCostoPromedio(totalCostoPromedio);
				System.out.println(entity.getFechaFactura());
				if ((entity.getEstado().equals("FACTURADO") || entity.getEstado().equals("PREVENTA"))
						&& entity.getNroDocumento().equals("")) {
					map = generarDocumentoUnificado(entity.getDocumento().getId(), numeroTerminal, entity.getId());
					if (map.get("numeroDocumento") != null && !map.get("numeroDocumento").toString().isEmpty()) {
						entity.setNroDocumento(map.get("numeroDocumento").toString());
					}
					if (map.get("timbrado") != null && !map.get("timbrado").toString().isEmpty()) {
						entity.setTimbrado(map.get("timbrado").toString());
					}
					if (map.get("fechaInicioVigencia") != null
							&& !map.get("fechaInicioVigencia").toString().isEmpty()) {
						entity.setTimbradoInicio(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaInicioVigencia").toString()));
					}
					if (map.get("fechaFinVigencia") != null && !map.get("fechaFinVigencia").toString().isEmpty()) {
						entity.setTimbradoFin(
								FechaUtil.convertirFechaStringADateUtil(map.get("fechaFinVigencia").toString()));
					}

				}
				entity = entityRepository.save(entity);
				System.out.println("Venta ID: " + entity.getId() + " - TipoDocumento: " + entity.getDocumento().getId()
						+ " - NroDocumento: " + entity.getNroDocumento());

				pdfPrintss(entity.getId(), numeroTerminal, entity.getDocumento().getDescripcion(),
						entity.getDocumento().getId());

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// agregarDatosCliente:
		entity.setCliente(clienteRepository.getIdCliente(entity.getCliente().getId()));
		// agregarDatosFuncionarioVendedor:
		entity.setFuncionarioV(funcionarioRepository.getIdFuncionario(entity.getFuncionarioV().getId()));
		// agregarDatosFuncionarioRegistro:
		entity.setFuncionario(funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId()));
		// agregarDatosFuncionarioReparto:
		entity.setFuncionarioR(funcionarioRepository.getIdFuncionario(entity.getFuncionarioR().getId()));
		System.out.println(entity.getTotalIvaDies() + " " + entity.getTotalIvaCinco());
		// agregarDatosDetalleProducto:
		entity.setDetalleProducto(detalleProductoRepository.getDetallePorCabecera(entity.getId()));
		// agregarDatosDetalleServicios:
		entity.setDetalleServicio(detalleServicioRepository.getDetallePorCabecera(entity.getId()));
		System.out.println("ID VET: RESS: " + entity.getId());
		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}

	public String validarPrecio(int id, double precio) {
		Producto pro = productoRepository.findById(id).get();
		String op = "P1";
		if (pro.getPrecioVenta_4() == precio) {
			op = "P4";
		}
		if (pro.getPrecioVenta_3() == precio) {
			op = "P3";
		}
		if (pro.getPrecioVenta_2() == precio) {
			op = "P2";
		}
		if (pro.getPrecioVenta_1() == precio) {
			op = "P1";
		}
		return op;
	}

	public void actualizarProductoBase(int id, double cantidad, double subtotal, double precio, int idFuncionario,
			String tipo, int idVenta) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		Funcionario f = funcionarioRepository.getIdFuncionario(idFuncionario);
		if (ca != null) {
			System.out.println("tiene compuesto y actualiza base unica : " + idFuncionario);
			double cant = 0.0;
			cant = cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaD(cant, ca.getProductoBase().getId());
			Producto p = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida m = new MovimientoEntradaSalida();

			m.setDescripcion(p.getDescripcion());
			m.setCantidad(cant);
			m.setFecha(new Date());
			m.setHora(hora());

			m.setIngreso(subtotal);
			m.setEgreso(0.0);
			m.setVentaSalida(subtotal / cant);

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
			Concepto c = new Concepto();
			if (tipo.equals("1")) {
				c = conceptoRepository.findById(1).get();
				m.getConcepto().setId(c.getId());
			} else {
				c = conceptoRepository.findById(2).get();
				m.getConcepto().setId(c.getId());
			}

			m.setReferencia(c.getDescripcion() + " REF.: " + idVenta);
			movEntradaSalidaRepository.save(m);
			// venta tipo, subtotl, precio, funcionario id, tipo, idVenta
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for (ProductoCardex ob : list) {
				System.out.println("tiene compuesto y actualiza compuesto varios : " + idFuncionario);

				Double existenciaActual = 0.0;
				existenciaActual = (cantidad * ca.getCantidadAplicacion()) / ob.getCantidadAplicacion();
				productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza
																											// pro
																											// compuesto
				Producto pp = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pp.getDescripcion());
				movv.setCantidad(existenciaActual);
				movv.setFecha(new Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal / existenciaActual);

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
				Concepto cc = new Concepto();
				if (tipo.equals("1")) {
					cc = conceptoRepository.findById(1).get();
					movv.getConcepto().setId(cc.getId());
				} else {
					cc = conceptoRepository.findById(2).get();
					movv.getConcepto().setId(cc.getId());
				}
				movv.setReferencia(cc.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(movv);
			}
		} else {
			System.out.println("venta - entrooo else no tiene compusto el id: " + id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if (pBase != null) {
				System.out.println("venta - Producto relacio0nado con un base");
				productoRepository.findByActualizaD(cantidad, id);
				Producto pro = productoRepository.getOne(id);
				MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();

				movEnt.setDescripcion(pro.getDescripcion());
				movEnt.setCantidad(cantidad);
				movEnt.setFecha(new Date());
				movEnt.setHora(hora());

				movEnt.setIngreso(subtotal);
				movEnt.setEgreso(0.0);
				movEnt.setVentaSalida(subtotal / cantidad);

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
				Concepto c = new Concepto();
				if (tipo.equals("1")) {
					c = conceptoRepository.findById(1).get();
					movEnt.getConcepto().setId(c.getId());
				} else {
					c = conceptoRepository.findById(2).get();
					movEnt.getConcepto().setId(c.getId());
				}

				movEnt.setReferencia(c.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(movEnt);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for (ProductoCardex ob : list) {
					System.out.println("venta - producto base relacion");
					Double existenciaActual = 0.0;
					existenciaActual = cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza
																												// pro
																												// compuesto
					Producto prod = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida entrada = new MovimientoEntradaSalida();
					entrada.setDescripcion(prod.getDescripcion());
					entrada.setCantidad(existenciaActual);
					entrada.setFecha(new Date());
					entrada.setHora(hora());

					entrada.setIngreso(subtotal);
					entrada.setEgreso(0.0);
					entrada.setVentaSalida(subtotal / existenciaActual);

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
					Concepto con = new Concepto();
					if (tipo.equals("1")) {
						con = conceptoRepository.findById(1).get();
						entrada.getConcepto().setId(con.getId());
					} else {
						con = conceptoRepository.findById(2).get();
						entrada.getConcepto().setId(con.getId());
					}
					entrada.setReferencia(con.getDescripcion() + " REF.: " + idVenta);
					movEntradaSalidaRepository.save(entrada);
				}
			} else {
				System.out.println("venta - Producto unitario");
				productoRepository.findByActualizaD(cantidad, id);
				Producto p = productoRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

				mov.setDescripcion(p.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new Date());
				mov.setHora(hora());

				mov.setIngreso(subtotal);
				mov.setEgreso(0.0);
				mov.setVentaSalida(subtotal / cantidad);

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
				Concepto c = new Concepto();
				if (tipo.equals("1")) {
					c = conceptoRepository.findById(1).get();
					mov.getConcepto().setId(c.getId());
				} else {
					c = conceptoRepository.findById(2).get();
					mov.getConcepto().setId(c.getId());
				}

				mov.setReferencia(c.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(mov);
			}

		}
	}

	public void actualizarProductoBaseAumentarCorregido(int id, double cantidad, double subtotal, double precio,
			int idFuncionario, String tipo, int idVenta) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if (ca != null) {
			double existenciaBase = 0.0;
			existenciaBase = cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			Producto pro = productoRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();
			// System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta
			// 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca:
			// "+p.getMarca().getDescripcion());
			// , double subtotal, double precio, int idFuncionario, String tipo, int idVenta
			movEnt.setDescripcion(pro.getDescripcion());
			movEnt.setCantidad(existenciaBase);
			movEnt.setFecha(new Date());
			movEnt.setHora(hora());

			movEnt.setIngreso(subtotal);
			movEnt.setEgreso(0.0);
			movEnt.setVentaSalida(subtotal / existenciaBase);

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
			Concepto ccc = new Concepto();
			ccc = conceptoRepository.findById(7).get();
			movEnt.getConcepto().setId(ccc.getId());
			movEnt.setReferencia(ccc.getDescripcion() + " REF.: " + idVenta);
			movEntradaSalidaRepository.save(movEnt);
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for (ProductoCardex ob : list) {
				Double exi = 0.0;
				exi = (cantidad * ca.getCantidadAplicacion()) / ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(exi, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				Producto produc = productoRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				// System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta
				// 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca:
				// "+p.getMarca().getDescripcion());
				// , double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movv.setDescripcion(produc.getDescripcion());
				movv.setCantidad(exi);
				movv.setFecha(new Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal / exi);

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
				Concepto con = new Concepto();
				con = conceptoRepository.findById(7).get();
				movv.getConcepto().setId(con.getId());
				movv.setReferencia(con.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(movv);
			}
		} else {
			System.out.println("entrooo else no tiene compusto el id: " + id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if (pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				productoRepository.findByActualizaA(cantidad, id);
				Producto pp = productoRepository.getOne(id);
				MovimientoEntradaSalida mEntrada = new MovimientoEntradaSalida();
				// System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta
				// 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca:
				// "+p.getMarca().getDescripcion());
				// , double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				mEntrada.setDescripcion(pp.getDescripcion());
				mEntrada.setCantidad(cantidad);
				mEntrada.setFecha(new Date());
				mEntrada.setHora(hora());

				mEntrada.setIngreso(subtotal);
				mEntrada.setEgreso(0.0);
				mEntrada.setVentaSalida(subtotal / cantidad);

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
				Concepto conce = new Concepto();
				conce = conceptoRepository.findById(7).get();
				mEntrada.getConcepto().setId(conce.getId());
				mEntrada.setReferencia(conce.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(mEntrada);
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for (ProductoCardex ob : list) {
					Double existenciaActual = 0.0;
					existenciaActual = cantidad / ob.getCantidadAplicacion();
					productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza
																												// pro
																												// compuesto
					Producto pro = productoRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida movEnt = new MovimientoEntradaSalida();
					// System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta
					// 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca:
					// "+p.getMarca().getDescripcion());
					// , double subtotal, double precio, int idFuncionario, String tipo, int idVenta
					movEnt.setDescripcion(pro.getDescripcion());
					movEnt.setCantidad(existenciaActual);
					movEnt.setFecha(new Date());
					movEnt.setHora(hora());

					movEnt.setIngreso(subtotal);
					movEnt.setEgreso(0.0);
					movEnt.setVentaSalida(subtotal / existenciaActual);

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
					Concepto ccc = new Concepto();
					ccc = conceptoRepository.findById(7).get();
					movEnt.getConcepto().setId(ccc.getId());
					movEnt.setReferencia(ccc.getDescripcion() + " REF.: " + idVenta);
					movEntradaSalidaRepository.save(movEnt);
				}
			} else {
				System.out.println("Producto unitario");
				productoRepository.findByActualizaA(cantidad, id);
				Producto produc = productoRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				// System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta
				// 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca:
				// "+p.getMarca().getDescripcion());
				// , double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movv.setDescripcion(produc.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new Date());
				movv.setHora(hora());

				movv.setIngreso(subtotal);
				movv.setEgreso(0.0);
				movv.setVentaSalida(subtotal / cantidad);

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
				Concepto con = new Concepto();
				con = conceptoRepository.findById(7).get();
				movv.getConcepto().setId(con.getId());
				movv.setReferencia(con.getDescripcion() + " REF.: " + idVenta);
				movEntradaSalidaRepository.save(movv);
			}

		}
	}

	public List<DetalleServicios> detalleServicio(int idVenta) {
		List<Object[]> objeto = detalleServicioRepository.lista(idVenta);
		List<DetalleServicios> detalleServicio = new ArrayList<>();
		for (Object[] ob : objeto) {
			DetalleServicios detalleServicios = new DetalleServicios();
			detalleServicios.setId(Integer.parseInt(ob[0].toString()));
			detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
			detalleServicios.setDescripcion(ob[2].toString());
			detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
			detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
			detalleServicios.getVenta().setId(Integer.parseInt(ob[6].toString()));
			detalleServicios.setIva(ob[7].toString());

			detalleServicio.add(detalleServicios);
		}
		return detalleServicio;
	}

	public List<DetalleProducto> getDetalleProducto(List<Object[]> objeto) {
		List<DetalleProducto> detalleProducto = new ArrayList<>();
		for (Object[] ob : objeto) {
			DetalleProducto detalleProductos = new DetalleProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getVenta().setId(Integer.parseInt(ob[7].toString()));

			detalleProductos.getProducto().setPrecioVenta_1(Double.parseDouble(ob[8].toString()));
			detalleProductos.getProducto().setPrecioVenta_2(Double.parseDouble(ob[9].toString()));
			detalleProductos.getProducto().setPrecioVenta_3(Double.parseDouble(ob[10].toString()));
			detalleProductos.getProducto().setPrecioVenta_4(Double.parseDouble(ob[11].toString()));
			detalleProductos.setDescuento(Double.parseDouble((ob[12].toString())));
			detalleProductos.getProducto().getUnidadMedida().setDescripcion(ob[13].toString());
			if (ob[14].toString() == null) {
				detalleProductos.getProducto().setExistencia(0.0);
			} else {
				detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[14].toString()));
			}
			detalleProductos.setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			detalleProductos.getProducto().setCodbar(ob[16].toString());
			detalleProductos.getProducto().getMarca().setDescripcion(ob[17].toString());
			detalleProductos.setMontoIva(Double.parseDouble(ob[18].toString()));
			detalleProductos.setCantidadDevolucion(Double.parseDouble(ob[19].toString()));
			System.out.println("IVA DETALLE: " + detalleProductos.getIva());
			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}

	public Venta ventass(int idVenta) {
		Venta cv = null;

		cv = entityRepository.findById(idVenta).orElse(null);
		// System.out.println(""+cv.getCliente().getPersona().);
		/*
		 * List<Venta> v= new ArrayList<Venta>(); v.add(cv); for(int i = 0; i < 1; i++)
		 * { cv = new Venta(); cv = v.get(i);
		 * System.out.println(cv.getCliente().getPersona().getNombre()+
		 * "asdfadsfasdfadsads");
		 * 
		 * }
		 */

		return cv;
	}

	public List<Venta> getLista(int idVenta) {

		List<Venta> lista = new ArrayList<>();

		Venta xxx = new Venta();
		List<DetalleProducto> detProducto = new ArrayList<>();
		List<DetalleServicios> detServicio = new ArrayList<>();

		xxx = ventass(idVenta);
		detProducto = getDetalleProducto(detalleProductoRepository.lista(idVenta));
		detServicio = detalleServicio(idVenta);

		for (int i = 0; i < 1; i++) {
			Cliente cli = clienteRepository.getIdCliente(xxx.getCliente().getId());
			Funcionario FunV = funcionarioRepository.getIdFuncionario(xxx.getFuncionarioV().getId());
			Funcionario FunR = funcionarioRepository.getIdFuncionario(xxx.getFuncionarioR().getId());

			Venta v = new Venta();
			v.getCliente().getPersona().setNombre(cli.getPersona().getNombre());
			v.getCliente().getPersona().setApellido(cli.getPersona().getApellido());
			v.getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getCliente().getPersona().setTelefono(cli.getPersona().getTelefono());
			v.getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			v.setFechaFactura(xxx.getFechaFactura());
			v.setFecha(xxx.getFecha());
			v.setHora(xxx.getHora());
			v.setObs(xxx.getObs());
			v.setId(xxx.getId());
			v.getDocumento().setId(xxx.getDocumento().getId());

			System.out.println("fun veeveveve : " + FunV.getPersona().getNombre());
			v.getFuncionarioV().getPersona().setNombre(FunV.getPersona().getNombre());
			v.getFuncionarioV().getPersona().setApellido(FunV.getPersona().getApellido());
			v.getFuncionarioV().getPersona().setTelefono(FunV.getPersona().getTelefono());
			v.getFuncionarioR().getPersona().setNombre(FunR.getPersona().getNombre());
			v.getFuncionarioR().getPersona().setApellido(FunR.getPersona().getApellido());
			v.getFuncionarioR().getPersona().setTelefono(FunR.getPersona().getTelefono());
			v.setTotalDescuento(xxx.getTotalDescuento());
			v.setTotalIvaCinco(xxx.getTotalIvaCinco());
			v.setTotalIvaDies(xxx.getTotalIvaDies());
			v.setTotal(xxx.getTotal());
			v.setTotalLetra(xxx.getTotalLetra());
			v.setTipo(xxx.getTipo());
			v.setObs(xxx.getObs());
			v.setNroDocumento(xxx.getNroDocumento());
			v.getDocumento().setDescripcion(xxx.getDocumento().getDescripcion());
			v.setEntrega(xxx.getEntrega());
			if (xxx.getTipo().equals("1")) {
				v.setTipo("CONTADO");
			}
			if (xxx.getTipo().equals("2")) {
				v.setTipo("CREDITO");
			}
			if (xxx.getTipo().equals("3")) {
				v.setTipo("NOTA CREDITO");
			}
			v.getZona().setDescripcion(xxx.getZona().getDescripcion());

			v.setDetalleProducto(detProducto);
			for (DetalleServicios det : detServicio) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.getProducto().setId(det.getId());
				detalleProducto.setDescripcion("SRV.: " + det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId() + "");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
				detalleProducto.setIva(det.getIva() + "");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				v.getDetalleProducto().add(detalleProducto);
				System.out.println(det.getIva() + " *8*8*8*8*");

			}
			lista.add(v);
			System.out.println("lista cantidad : " + lista.get(0).getDetalleProducto().size());
		}

		return lista;

	}
	
	
	public void pdfPrintss(int idVenta, int numeroTerminal, String siImpresion, int tipoDocumento) {
		if (siImpresion.equals("true")) {
			Reporte report = new Reporte();
		    Org org= orgRepository.findTop1ByOrderByIdDesc();
			TerminalConfigImpresora ter = new TerminalConfigImpresora();
			ter= terminalRepository.consultarTerminalPorNumeros(numeroTerminal);
			if (ter==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {

					
				List<Venta> venta = getLista(idVenta);
				ReporteConfig reportConfig = new ReporteConfig();
				System.out.println("doc:  "+venta.get(0).getDocumento().getId());
				if(venta.get(0).getDocumento().getId()==1) {reportConfig = reporteConfigRepository.getOne(5);}
				if(venta.get(0).getDocumento().getId()==2) {reportConfig = reporteConfigRepository.getOne(1);}
				if(venta.get(0).getDocumento().getId()==3) {reportConfig = reporteConfigRepository.getOne(1);}							
				Map<String, Object> map = new HashMap<>();
				report = new Reporte();
				List<Venta> listaVentaImpresion= new ArrayList<Venta>();
				int totalPages = 0;
				if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
				    // No dividir
				    listaVentaImpresion = venta;
				    totalPages = 1;
				} else {
					 // MEDIA
				    int pageSize = 10;
				    totalPages = (int) Math.ceil((double) venta.get(0).getDetalleProducto().size() / pageSize);

				    listaVentaImpresion = new ArrayList<>();
					System.out.println("TOTAL DE PAGINAS:"+ totalPages);
					for (int i = 0; i < totalPages; i++) {	
						System.out.println("\n--- Página " + (i + 1) + " ---");

						int start = i * pageSize;
						int end = Math.min(start + pageSize, venta.get(0).getDetalleProducto().size());
						// Crear una nueva lista con los elementos de la página actual
						List<DetalleProducto> detallesPagina = new ArrayList<>(venta.get(0).getDetalleProducto().subList(start, end));
						Double totalMontoPagina=0.0, totalPaginaIvaCinco=0.0, totalPaginaIvaDies=0.0, totalPaginaIva=0.0,totalPaginaExcenta=0.0;
						for (int j = 0; j < detallesPagina.size(); j++) {
							totalMontoPagina = totalMontoPagina + detallesPagina.get(j).getSubTotal();
							if(detallesPagina.get(j).getIva().equals("10 %")) {totalPaginaIvaDies = totalPaginaIvaDies +  (detallesPagina.get(j).getSubTotal()/11);}
							if(detallesPagina.get(j).getIva().equals("5 %")) {totalPaginaIvaCinco = totalPaginaIvaCinco +  (detallesPagina.get(j).getSubTotal()/21);}
							if(detallesPagina.get(j).getIva().equals("Excenta")) {totalPaginaExcenta = totalPaginaExcenta +  (detallesPagina.get(j).getSubTotal());}
						}
						Venta ventaImpresion = new Venta();
						ventaImpresion.setId(venta.get(0).getId());
						ventaImpresion.setFechaFactura(venta.get(0).getFechaFactura());
						ventaImpresion.setDocumento(venta.get(0).getDocumento());
						ventaImpresion.setCliente(venta.get(0).getCliente());
						ventaImpresion.setFuncionario(venta.get(0).getFuncionario());
						ventaImpresion.setFuncionarioR(venta.get(0).getFuncionarioR());
						ventaImpresion.setFuncionarioV(venta.get(0).getFuncionarioV());
						ventaImpresion.setTipo(venta.get(0).getTipo());
						ventaImpresion.setTotalLetra(NumerosALetras.convertirNumeroALetras(totalMontoPagina));
						ventaImpresion.setTotal(totalMontoPagina);
						ventaImpresion.setTotalIvaDies(totalPaginaIvaDies);
						ventaImpresion.setTotalIvaCinco(totalPaginaIvaCinco);
						ventaImpresion.setTotalIva(totalPaginaIvaDies +  totalPaginaIvaCinco);
						ventaImpresion.setEntrega(venta.get(0).getEntrega());
						ventaImpresion.getZona().setDescripcion(venta.get(0).getZona().getDescripcion());

						ventaImpresion.setDetalleProducto(detallesPagina);
						listaVentaImpresion.add(ventaImpresion);
						System.out.println("UNA FILA DE LA PAGINA" +listaVentaImpresion.get(i).getDetalleProducto().get(0).getDescripcion());
					}
					
				}								
				if (ter.getImpresora().equals("matricial")) {
					ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
					String urlReporte ="\\reporte\\"+reportConfig.getNombreSubReporte1()+".jasper";
					System.out.println("url SUBREPORT:  "+urlReporte+ " report name : "+reportConfig.getNombreReporte());
					map.put("urlSubRepor", urlReporte);
					map.put("tituloReporte", f.getTitulo());
					map.put("razonSocialReporte", f.getRazonSocial());
					map.put("descripcionMovimiento", f.getDescripcion());
					map.put("direccionReporte", f.getDireccion());
					map.put("telefonoReporte", f.getTelefono());
					map.put("entregaInicial", "");
					map.put("ciudad", org.getCiudad());
					
					map.put("paginaTotal", totalPages+ "");
					try {
						ParametroTipoHoja p = parametroTipoHoja.getOne(1);
						System.out.println("total apartido lista :  "+listaVentaImpresion.size());
						for (int i=0; i < listaVentaImpresion.size(); i++) {
							if(ter.getIsCentralizadoImpresion().equals("SI")) {
								System.out.println("SI CENTRALIZADO");
								  map.put("paginaActual", (i + 1) + "");
								  List<Venta> datos;
								    if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
								        datos = venta;
								    } else {
								        datos = Arrays.asList(listaVentaImpresion.get(i));
								    }
								if(p.getDescripcion().equals("A4")) {
									report.reportPDFImprimirA4ServicioLocalPC(datos, map, reportConfig.getNombreReporte(), ter.getIp(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if(p.getDescripcion().equals("CORTE")) {
									report.reportPDFImprimirA4ServicioLocalPC(datos, map, reportConfig.getNombreReporte(), ter.getIp(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if(p.getDescripcion().equals("PRUEBA-JOB")) {
									report.reportPDFImprimirPrueba(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
								        break;
								}
							}else if(ter.getIsCentralizadoImpresion().equals("NO")) {
								System.out.println("NO CENTRALIZADO");
								 map.put("paginaActual", (i + 1) + "");

								    List<Venta> datos;

								    if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
								        datos = venta;
								    } else {
								        datos = Arrays.asList(listaVentaImpresion.get(i));
								    }
			
								if(p.getDescripcion().equals("A4")) {
									report.reportPDFImprimirA4(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if(p.getDescripcion().equals("CORTE")) {
									report.reportPDFImprimirLibreCorte(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if(p.getDescripcion().equals("PRUEBA-JOB")) {
									report.reportPDFImprimirPrueba(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
								}
								if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
								        break;
								}
							}
							 
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			
		}

	}

	@RequestMapping(value = "/reImprimirMatricial/{id}/{numeroTerminal}/{fecha}", method = RequestMethod.GET)
	public void reImprimirMatricial(@PathVariable int id, @PathVariable int numeroTerminal,
			@PathVariable String fecha) {
		List<Venta> venta = getLista(id);
		venta.get(0).setFechaFactura(FechaUtil.convertirFechaStringADateUtil(fecha));

		Reporte report = new Reporte();
		TerminalConfigImpresora ter = new TerminalConfigImpresora();
		ter = terminalRepository.consultarTerminalPorNumeros(numeroTerminal);

		if (ter == null) {
			System.out.println("Se debe cargar numero terminal dentro de la base de datos");
		} else {
			ReporteConfig reportConfig = new ReporteConfig();
			System.out.println("doc:  " + venta.get(0).getDocumento().getId());
			if (venta.get(0).getDocumento().getId() == 1) {
				reportConfig = reporteConfigRepository.getOne(5);
			}
			if (venta.get(0).getDocumento().getId() == 2) {
				reportConfig = reporteConfigRepository.getOne(1);
			}
			if (venta.get(0).getDocumento().getId() == 3) {
				reportConfig = reporteConfigRepository.getOne(1);
			}
			Map<String, Object> map = new HashMap<>();
			report = new Reporte();
			int pageSize = 10;
			int totalPages = (int) Math.ceil((double) venta.get(0).getDetalleProducto().size() / pageSize);
			System.out.println("TOTAL DE PAGINAS:" + totalPages);

			List<Venta> listaVentaImpresion = new ArrayList<Venta>();

			for (int i = 0; i < totalPages; i++) {
				System.out.println("\n--- Página " + (i + 1) + " ---");

				int start = i * pageSize;
				int end = Math.min(start + pageSize, venta.get(0).getDetalleProducto().size());
				// Crear una nueva lista con los elementos de la página actual
				List<DetalleProducto> detallesPagina = new ArrayList<>(
						venta.get(0).getDetalleProducto().subList(start, end));
				Double totalMontoPagina = 0.0, totalPaginaIvaCinco = 0.0, totalPaginaIvaDies = 0.0,
						totalPaginaIva = 0.0, totalPaginaExcenta = 0.0;
				for (int j = 0; j < detallesPagina.size(); j++) {
					totalMontoPagina = totalMontoPagina + detallesPagina.get(j).getSubTotal();
					if (detallesPagina.get(j).getIva().equals("10 %")) {
						totalPaginaIvaDies = totalPaginaIvaDies + (detallesPagina.get(j).getSubTotal() / 11);
					}
					if (detallesPagina.get(j).getIva().equals("5 %")) {
						totalPaginaIvaCinco = totalPaginaIvaCinco + (detallesPagina.get(j).getSubTotal() / 21);
					}
					if (detallesPagina.get(j).getIva().equals("Excenta")) {
						totalPaginaExcenta = totalPaginaExcenta + (detallesPagina.get(j).getSubTotal());
					}
				}
				Venta ventaImpresion = new Venta();
				ventaImpresion.setId(venta.get(0).getId());
				ventaImpresion.setFechaFactura(venta.get(0).getFechaFactura());
				ventaImpresion.setDocumento(venta.get(0).getDocumento());
				ventaImpresion.setCliente(venta.get(0).getCliente());
				ventaImpresion.setFuncionario(venta.get(0).getFuncionario());
				ventaImpresion.setFuncionarioR(venta.get(0).getFuncionarioR());
				ventaImpresion.setFuncionarioV(venta.get(0).getFuncionarioV());
				ventaImpresion.setTipo(venta.get(0).getTipo());
				ventaImpresion.setTotalLetra(NumerosALetras.convertirNumeroALetras(totalMontoPagina));
				ventaImpresion.setTotal(totalMontoPagina);
				ventaImpresion.setTotalIvaDies(totalPaginaIvaDies);
				ventaImpresion.setTotalIvaCinco(totalPaginaIvaCinco);
				ventaImpresion.setTotalIva(totalPaginaIvaDies + totalPaginaIvaCinco);
				ventaImpresion.setDetalleProducto(detallesPagina);
				ventaImpresion.setEntrega(venta.get(0).getEntrega());
				ventaImpresion.getZona().setDescripcion(venta.get(0).getZona().getDescripcion());

				listaVentaImpresion.add(ventaImpresion);
				System.out.println("UNA FILA DE LA PAGINA"
						+ listaVentaImpresion.get(i).getDetalleProducto().get(0).getDescripcion());
			}
			if (ter.getImpresora().equals("matricial")) {
				ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
				String urlReporte = "\\reporte\\" + reportConfig.getNombreSubReporte1() + ".jasper";
				System.out.println("SUBREPORT:  " + urlReporte + " REPORT NOMBRE : " + reportConfig.getNombreReporte());

				map.put("urlSubRepor", urlReporte);
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("entregaInicial", "");
				map.put("paginaTotal", totalPages + "");

				try {
					ParametroTipoHoja p = parametroTipoHoja.getOne(1);
					System.out.println("total apartido lista :  " + listaVentaImpresion.size());
					for (int i = 0; i < listaVentaImpresion.size(); i++) {
						 if(ter.getIsCentralizadoImpresion().equals("SI")) {
							System.out.println("SI CENTRALIZADO");
							  map.put("paginaActual", (i + 1) + "");
							  List<Venta> datos;
							    if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
							        datos = venta;
							    } else {
							        datos = Arrays.asList(listaVentaImpresion.get(i));
							    }
							if(p.getDescripcion().equals("A4")) {
								report.reportPDFImprimirA4ServicioLocalPC(datos, map, reportConfig.getNombreReporte(), ter.getIp(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if(p.getDescripcion().equals("CORTE")) {
								report.reportPDFImprimirA4ServicioLocalPC(datos, map, reportConfig.getNombreReporte(), ter.getIp(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if(p.getDescripcion().equals("PRUEBA-JOB")) {
								report.reportPDFImprimirPrueba(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
							        break;
							}
						}else if(ter.getIsCentralizadoImpresion().equals("NO")) {
							map.put("paginaActual", (i + 1) + "");
							if (p.getDescripcion().equals("A4")) {
								report.reportPDFImprimirA4(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if (p.getDescripcion().equals("CORTE")) {
								report.reportPDFImprimirLibreCorte(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if (p.getDescripcion().equals("JOB")) {
								report.reportPDFImprimirPrueba(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
						}
						
					}
					// Pasamos solo los detalles de la página actual a la impresión
					// report.reportPDFImprimir(listaVentaImpresion, map,
					// reportConfig.getNombreReporte(), t.getNombreImpresora());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@RequestMapping(value = "/reImprimirMatricial/{id}/{idCliente}/{idDocumento}/{numeroTerminal}/{fecha}", method = RequestMethod.GET)
	public void reImprimirMatricialDesdeDetalleVenta(@PathVariable int id, @PathVariable int idCliente,
			@PathVariable int idDocumento, @PathVariable int numeroTerminal, @PathVariable String fecha) {
		List<Venta> venta = getLista(id);
		venta.get(0).setFechaFactura(FechaUtil.convertirFechaStringADateUtil(fecha));
		Org org = orgRepository.findTop1ByOrderByIdDesc();
		Reporte report = new Reporte();
		TerminalConfigImpresora ter = new TerminalConfigImpresora();
		ter = terminalRepository.consultarTerminalPorNumeros(numeroTerminal);
		if (ter == null) {
			System.out.println("Se debe cargar numero terminal dentro de la base de datos");
		} else {
			ReporteConfig reportConfig = new ReporteConfig();
			System.out.println("doc:  " + venta.get(0).getDocumento().getId());
			if (idDocumento == 1) {
				reportConfig = reporteConfigRepository.getOne(5);
			}
			if (idDocumento == 2) {
				reportConfig = reporteConfigRepository.getOne(1);
			}
			if (idDocumento == 3) {
				reportConfig = reporteConfigRepository.getOne(1);
			}
			Map<String, Object> map = new HashMap<>();
			report = new Reporte();
			int pageSize = 10;
			int totalPages = (int) Math.ceil((double) venta.get(0).getDetalleProducto().size() / pageSize);
			System.out.println("TOTAL DE PAGINAS:" + totalPages);
			Cliente cli = clienteRepository.getIdCliente(idCliente);
			List<Venta> listaVentaImpresion = new ArrayList<Venta>();

			for (int i = 0; i < totalPages; i++) {
				System.out.println("\n--- Página " + (i + 1) + " ---");

				int start = i * pageSize;
				int end = Math.min(start + pageSize, venta.get(0).getDetalleProducto().size());
				// Crear una nueva lista con los elementos de la página actual
				List<DetalleProducto> detallesPagina = new ArrayList<>(
						venta.get(0).getDetalleProducto().subList(start, end));
				Double totalMontoPagina = 0.0, totalPaginaIvaCinco = 0.0, totalPaginaIvaDies = 0.0,
						totalPaginaIva = 0.0, totalPaginaExcenta = 0.0;
				for (int j = 0; j < detallesPagina.size(); j++) {
					totalMontoPagina = totalMontoPagina + detallesPagina.get(j).getSubTotal();
					if (detallesPagina.get(j).getIva().equals("10 %")) {
						totalPaginaIvaDies = totalPaginaIvaDies + (detallesPagina.get(j).getSubTotal() / 11);
					}
					if (detallesPagina.get(j).getIva().equals("5 %")) {
						totalPaginaIvaCinco = totalPaginaIvaCinco + (detallesPagina.get(j).getSubTotal() / 21);
					}
					if (detallesPagina.get(j).getIva().equals("Excenta")) {
						totalPaginaExcenta = totalPaginaExcenta + (detallesPagina.get(j).getSubTotal());
					}
				}
				Venta ventaImpresion = new Venta();
				ventaImpresion.setId(venta.get(0).getId());
				ventaImpresion.setFechaFactura(venta.get(0).getFechaFactura());
				ventaImpresion.setDocumento(venta.get(0).getDocumento());
				ventaImpresion.setCliente(cli);
				ventaImpresion.setFuncionario(venta.get(0).getFuncionario());
				ventaImpresion.setFuncionarioR(venta.get(0).getFuncionarioR());
				ventaImpresion.setFuncionarioV(venta.get(0).getFuncionarioV());
				ventaImpresion.setTipo(venta.get(0).getTipo());
				ventaImpresion.setTotalLetra(NumerosALetras.convertirNumeroALetras(totalMontoPagina));
				ventaImpresion.setTotal(totalMontoPagina);
				ventaImpresion.setTotalIvaDies(totalPaginaIvaDies);
				ventaImpresion.setTotalIvaCinco(totalPaginaIvaCinco);
				ventaImpresion.setTotalIva(totalPaginaIvaDies + totalPaginaIvaCinco);
				ventaImpresion.setDetalleProducto(detallesPagina);
				ventaImpresion.setEntrega(venta.get(0).getEntrega());
				ventaImpresion.getZona().setDescripcion(venta.get(0).getZona().getDescripcion());

				listaVentaImpresion.add(ventaImpresion);
				System.out.println("UNA FILA DE LA PAGINA"
						+ listaVentaImpresion.get(i).getDetalleProducto().get(0).getDescripcion());
			}
			if (ter.getImpresora().equals("matricial")) {
				ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
				String urlReporte = "\\reporte\\" + reportConfig.getNombreSubReporte1() + ".jasper";
				System.out.println("SUBREPORT:  " + urlReporte + " REPORT NOMBRE : " + reportConfig.getNombreReporte());

				map.put("urlSubRepor", urlReporte);
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("entregaInicial", "");
				map.put("paginaTotal", totalPages + "");
				map.put("ciudad", org.getCiudad());

				try {
					ParametroTipoHoja p = parametroTipoHoja.getOne(1);
					System.out.println("total apartido lista :  " + listaVentaImpresion.size());
					for (int i = 0; i < listaVentaImpresion.size(); i++) {
						if(ter.getIsCentralizadoImpresion().equals("SI")) {
							System.out.println("NO CENTRALIZADO");
							 map.put("paginaActual", (i + 1) + "");
							    List<Venta> datos;

							    if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
							        datos = venta;
							    } else {
							        datos = Arrays.asList(listaVentaImpresion.get(i));
							    }
							if(p.getDescripcion().equals("A4")) {
								report.reportPDFImprimirA4(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if(p.getDescripcion().equals("CORTE")) {
								report.reportPDFImprimirLibreCorte(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if(p.getDescripcion().equals("PRUEBA-JOB")) {
								report.reportPDFImprimirPrueba(datos, map, reportConfig.getNombreReporte(), ter.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if ("NORMAL".equalsIgnoreCase(reportConfig.getTipoFormato())) {
							        break;
							}
							
						}else if(ter.getIsCentralizadoImpresion().equals("NO")) {
							map.put("paginaActual", (i + 1) + "");
							if (p.getDescripcion().equals("A4")) {
								report.reportPDFImprimirA4(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if (p.getDescripcion().equals("CORTE")) {
								report.reportPDFImprimirLibreCorte(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
							if (p.getDescripcion().equals("JOB")) {
								report.reportPDFImprimirPrueba(Arrays.asList(listaVentaImpresion.get(i)), map,
										reportConfig.getNombreReporte(), ter.getNombreImpresora(),
										reportConfig.getPageWidth(), reportConfig.getPageHeigth());
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/libroIva/{fechaInicio}/{fechaFin}")
	public List<Venta> getLibroIva(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
		return listaLibroVenta(fechaInicio, fechaFin);
	}

	public List<Venta> listaLibroVenta(String fechaInicio, String fechaFin) {
		List<Venta> listaRetorno = new ArrayList<>();
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			fecI = formater.parse(fechaInicio);
			Date fecF = formater.parse(fechaFin);
			Date fechaFi = sumarDia(fecF, 24);
			List<Object[]> objeto = entityRepository.getLibroVenta(fecI, fechaFi);
			for (Object[] ob : objeto) {
				Venta v = new Venta();
				v.setNroDocumento(ob[0].toString());
				v.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
				v.getCliente().getPersona().setNombre(ob[2].toString());
				v.getCliente().getPersona().setCedula(ob[3].toString());
				if (ob[4] == null) {
					v.setTimbrado("");
				} else {
					v.setTimbrado(ob[4].toString());
				}
				if (ob[5] == null) {
					v.setFecha(null);
				} else {
					v.setTimbradoFin(FechaUtil.convertirFechaStringADateUtil(ob[5].toString()));
				}
				v.setTotal(Double.parseDouble(ob[6].toString()));
				if (ob[7] == null) {
					v.setTotalIvaCinco(0.0);
				} else {
					v.setTotalIvaCinco(Double.parseDouble(ob[7].toString()));
				}
				if (ob[8] == null) {
					v.setTotalIvaDies(0.0);
				} else {
					v.setTotalIvaDies(Double.parseDouble(ob[8].toString()));
				}
				if (ob[9] == null) {
					v.setTotalExcenta(0.0);
				} else {
					v.setTotalExcenta(Double.parseDouble(ob[9].toString()));
				}

				listaRetorno.add(v);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaRetorno;
	}

	@RequestMapping(value = "/libroVentaIvaPDF/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public @ResponseBody void clientePDF(HttpServletResponse response, @PathVariable String fechaI,
			@PathVariable String fechaF) throws IOException {
		List<Venta> venta = new ArrayList<>();
		venta = listaLibroVenta(fechaI, fechaF);
		Double totalIvaCinco = 0.0, totalIvaDies = 0.0, totalExcenta = 0.0, totalMonto = 0.0;
		for (Venta v : venta) {
			totalExcenta = totalExcenta + v.getTotalExcenta();
			totalMonto = totalMonto + v.getTotal();
			totalIvaCinco = totalIvaCinco + v.getTotalIvaCinco();
			totalIvaDies = totalIvaDies + v.getTotalIvaDies();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("inicio", "" + fechaI);
		map.put("fin", "" + fechaF);
		map.put("totalIvaCinco", totalIvaCinco);
		map.put("totalIvaDies", totalIvaDies);
		map.put("totalExcenta", totalExcenta);
		map.put("totalMonto", totalMonto);

		report = new Reporte();
		report.reportPDFDescarga(venta, map, "LibroVentaIva", response);
	}

	@RequestMapping(value = "/libroVentaIvaXML/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> clienteXML(@PathVariable String fechaI, @PathVariable String fechaF)
			throws IOException {
		List<Venta> ventas = listaLibroVenta(fechaI, fechaF);

		ByteArrayInputStream in = ExcelGenerator.ventaToExcel(ventas);
		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DetalleProducto> detalles) {
		try {
			if (detalles.size() != -1) {
				System.out.println("con listado lista");
				for (DetalleProducto de : detalles) {
					detalleProductoRepository.deleteById(de.getId());
				}
				System.out.println("sin lista");
				return new ResponseEntity<String>(HttpStatus.CREATED);

			} else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/servicio")
	public ResponseEntity<?> eliminarServicio(@RequestBody List<DetalleServicios> detalles) {
		System.out.println("entroo eliminar servicio");
		try {
			System.out.println("entroo eliminar servicio try ");
			for (DetalleServicios de : detalles) {
				System.out.println("entroo eliminar servicio for");
				detalleServicioRepository.deleteById(de.getId());
			}
			return new ResponseEntity<String>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/pruebaHql", method = RequestMethod.GET)
	public List<Venta> pruebaHql() throws IOException {
		// List<Venta> lisObj= entityRepository.getPruebaHql(2);
		// for (int i = 0; i < lisObj.size(); i++) {
		// //lisObj.get(i).setDetalleProducto(detalleProducto(detalleProductoRepository.lista(lisObj.get(i).getId())));
		// }
		// System.out.println(lisObj.get(0).getDetalleProducto().size()+"SIZE DETALLE");
		// System.out.println("SIZE: "+lisObj.size());
		return null;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaListado/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public List<Venta> getReporteVentaRangoFechaListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF) throws IOException {
		List<Venta> listado = new ArrayList<>();
		Double totalCostoProd = 0.0, totalProducto = 0.0, totalUtilidadProducto = 0.0, totalServicio = 0.0,
				totalVenta = 0.0;
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		try {
			Calendar cc = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("fecha que viene: " + fechaI + ", " + fechaF);
			fecI = formater.parse(fechaI);
			Date fecF = formater.parse(fechaF);
			System.out.println(fecF.getDate());
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("hora final fechas::: " + fecF + " hora inicio finbal: " + fecI);
			List<Venta> obb = entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
			System.out.println(obb.size() + " ************lis obb");
			listado = cargarListaReporte(obb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listado;
	}

	@RequestMapping(value = "/reporteVentaRangoFecha/{fechaI}/{fechaF}/{detallado}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoFecha(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int detallado) throws IOException {
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Double totalCostoProd = 0.0, totalProducto = 0.0, totalUtilidadProducto = 0.0, totalServicio = 0.0,
				totalVenta = 0.0;
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		try {
			Calendar cc = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("fecha que viene: " + fechaI + ", " + fechaF);
			fecI = formater.parse(fechaI);
			Date fecF = formater.parse(fechaF);
			System.out.println(fecF.getDate());
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("hora final fechas::: " + fecF + " hora inicio finbal: " + fecI);

			Map<String, Object> map = new HashMap<>();
			map.put("org", "" + org.getNombre());
			map.put("direccion", "" + org.getDireccion());
			map.put("ruc", "" + org.getRuc());
			map.put("telefono", "" + org.getTelefono());
			map.put("ciudad", "" + org.getCiudad());
			map.put("pais", "" + org.getPais());
			map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
					+ usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);
			report = new Reporte();
			if (detallado == 1) {
				List<Venta> obb = entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
				System.out.println(obb.size() + " ************lis obb");
				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						if (obb.get(i).getTipo().equals("1")) {
							obb.get(i).setTipo("CONTADO");
						}
						if (obb.get(i).getTipo().equals("2")) {
							obb.get(i).setTipo("CREDITO");
						}
						if (obb.get(i).getTipo().equals("3")) {
							obb.get(i).setTipo("NOTA CREDITO");
						}
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRango", response);
					return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}

			}
			if (detallado == 2) {
				System.out.println("ENTROO TRUE");
				List<Venta> obb = entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
				System.out.println(obb.size() + " ************lis obb");
				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						if (obb.get(i).getTipo().equals("1")) {
							obb.get(i).setTipo("CONTADO");
						}
						if (obb.get(i).getTipo().equals("2")) {
							obb.get(i).setTipo("CREDITO");
						}
						if (obb.get(i).getTipo().equals("3")) {
							obb.get(i).setTipo("NOTA CREDITO");
						}

						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.setCosto(0.0);
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							;
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					listadoDetallado = obb;
					System.out.println("lista size: " + listadoDetallado.size());
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoDetallado", response);
					return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorFuncionarioListado/{fechaI}/{fechaF}/{idFuncionario}", method = RequestMethod.GET)
	public List<Venta> getReporteVentaRangoFechaFuncionarioListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idFuncionario)
			throws IOException {
		List<Venta> listado = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Funcionario funcionario = funcionarioRepository.getIdFuncionario(idFuncionario);
		Org org = orgRepository.findById(1).get();
		try {
			Date fecI, fecF;
			fecI = FechaUtil.setFechaHoraInicial(fechaI);
			fecF = FechaUtil.setFechaHoraFinal(fechaF);
			List<Venta> obb = entityRepository.getReporteVentaRangoPorFuncionarioVendedorHql(fecI, fecF, idFuncionario);
			listado = cargarListaReporte(obb);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listado;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorFuncionarioResumenListado/{idFuncionario}/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public List<Object[]> getReporteVentaRangoFechaFuncionarioResumenListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idFuncionario)
			throws IOException {

		List<Object[]> obb = new ArrayList<>();
		try {
			Date fecI = FechaUtil.setFechaHoraInicial(fechaI);
			Date fecF = FechaUtil.setFechaHoraFinal(fechaF);

			// Convertir a Timestamp explícitamente
			java.sql.Timestamp tsInicio = new java.sql.Timestamp(fecI.getTime());
			java.sql.Timestamp tsFin = new java.sql.Timestamp(fecF.getTime());

			obb = entityRepository.getResumenProductoPorFuncionarioYRango(idFuncionario, tsInicio, tsFin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obb;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorFuncionarioResumen/{idFuncionario}/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoFechaFuncionarioResumen(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idFuncionario) throws IOException {
		List<Object[]> obb = new ArrayList<>();
		List<InformeVentaTotalPorProducto> lisRetorno = new ArrayList<InformeVentaTotalPorProducto>();

		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Funcionario funcionario = funcionarioRepository.getIdFuncionario(idFuncionario);
		Org org = orgRepository.findById(1).get();
		try {
			Date fecI = FechaUtil.setFechaHoraInicial(fechaI);
			Date fecF = FechaUtil.setFechaHoraFinal(fechaF);

			// Convertir a Timestamp explícitamente
			java.sql.Timestamp tsInicio = new java.sql.Timestamp(fecI.getTime());
			java.sql.Timestamp tsFin = new java.sql.Timestamp(fecF.getTime());
			obb = entityRepository.getResumenProductoPorFuncionarioYRango(idFuncionario, tsInicio, tsFin);
			if (obb.size() > 0) {
				for (int i = 0; i < obb.size(); i++) {
					InformeVentaTotalPorProducto inf = new InformeVentaTotalPorProducto();
					inf.setDescripcion(obb.get(i)[0].toString());
					inf.setPrecio(Double.parseDouble(obb.get(i)[1].toString()));
					inf.setCantidadVenta(Double.parseDouble(obb.get(i)[2].toString()));
					inf.setCosto(Double.parseDouble(obb.get(i)[3].toString()));
					inf.setSubTotalVenta(Double.parseDouble(obb.get(i)[4].toString()));

					inf.setCantidadDevuelta(Double.parseDouble(obb.get(i)[5].toString()));
					inf.setCostoDevuelta(Double.parseDouble(obb.get(i)[6].toString()));
					inf.setSubTotalDevuelto(Double.parseDouble(obb.get(i)[7].toString()));

					inf.setSubTotalNeta(Double.parseDouble(obb.get(i)[8].toString()));
					inf.setFuncionario(obb.get(i)[9].toString());

					System.out.println("COSTO real : " + inf.getCosto());
					System.out.println("COSTO devolucion: " + inf.getCostoDevuelta());

					lisRetorno.add(inf);
				}

				System.out.println("ejecutoo el metodo de reangoooo");
				Map<String, Object> map = new HashMap<>();
				map.put("org", "" + org.getNombre());
				map.put("direccion", "" + org.getDireccion());
				map.put("ruc", "" + org.getRuc());
				map.put("telefono", "" + org.getTelefono());
				map.put("ciudad", "" + org.getCiudad());
				map.put("pais", "" + org.getPais());
				map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
						+ usuario.getFuncionario().getPersona().getApellido());
				map.put("desde", fecI);
				map.put("hasta", fecF);
				map.put("vendedor",
						funcionario.getPersona().getNombre() + " " + funcionario.getPersona().getApellido());
				report = new Reporte();
				report.reportPDFDescarga(lisRetorno, map, "InformeTotalVentaResumidoProductoPorFuncionario", response);
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorFuncionario/{fechaI}/{fechaF}/{idFuncionario}/{detallado}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoFechaFuncionario(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idFuncionario, @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: " + fechaI + ":::: " + fechaF);
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Funcionario funcionario = funcionarioRepository.getIdFuncionario(idFuncionario);
		Org org = orgRepository.findById(1).get();
		Double totalCostoProd = 0.0, totalProducto = 0.0, totalUtilidadProducto = 0.0, totalServicio = 0.0,
				totalVenta = 0.0;

		try {
			System.out.println("entroo tryy" + detallado);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF = formater.parse(fechaF);
			System.out.println("pasoo3");
			// Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: " + fecI + "  : : : FFIN: " + fecF + " : id Func:  " + idFuncionario);
			// objeto = entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF,
			// idFuncionario);
			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", "" + org.getNombre());
			map.put("direccion", "" + org.getDireccion());
			map.put("ruc", "" + org.getRuc());
			map.put("telefono", "" + org.getTelefono());
			map.put("ciudad", "" + org.getCiudad());
			map.put("pais", "" + org.getPais());
			map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
					+ usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);

			map.put("vendedor", funcionario.getPersona().getNombre() + " " + funcionario.getPersona().getApellido());

			System.out.println("Tipo detallado:   " + detallado);
			report = new Reporte();

			if (detallado == 1) {
				List<Venta> obb = entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);

				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							;
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					// Double totalCostoProd=0.0, totalProducto=0.0, totalUtilidadProducto=0.0,
					// totalServicio=0.0, totalVenta=0.0 ;

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					System.out.println(obb.size() + " ************lis obb");
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
					return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}

			}
			if (detallado == 2) {
				List<Venta> obb = entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);
				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					System.out.println(obb.size() + " ************lis obb 22222");
					listadoDetallado = obb;
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoPorFuncionarioDetallado",
							response);
					return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);

				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}
			}

			// report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario",
			// response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorClienteListado/{fechaI}/{fechaF}/{idCliente}", method = RequestMethod.GET)
	public List<Venta> getReporteVentaRangoFechaClienteListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idCliente) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: " + fechaI + ":::: " + fechaF);
		List<Venta> listado = new ArrayList<>();
		try {
			Date fecI, fecF;
			fecI = FechaUtil.setFechaHoraInicial(fechaI);
			fecF = FechaUtil.setFechaHoraFinal(fechaF);
			List<Venta> obb = entityRepository.getVentaPorRangoFechaClienteHql(fecI, fecF, idCliente);
			listado = cargarListaReporte(obb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listado;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorCliente/{fechaI}/{fechaF}/{idCliente}/{detallado}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoFechaCliente(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idCliente, @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: " + fechaI + ":::: " + fechaF);
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Cliente cl = clienteRepository.getIdCliente(idCliente);
		Org org = orgRepository.findById(1).get();
		List<Object[]> listUltimaVentas = new ArrayList<Object[]>();

		Double totalCostoProd = 0.0, totalProducto = 0.0, totalUtilidadProducto = 0.0, totalServicio = 0.0,
				totalVenta = 0.0;

		try {
			System.out.println("entroo tryy" + detallado);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF = formater.parse(fechaF);
			System.out.println("pasoo3");
			// Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: " + fecI + "  : : : FFIN: " + fecF + " : id Func:  " + idCliente);
			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", "" + org.getNombre());
			map.put("direccion", "" + org.getDireccion());
			map.put("ruc", "" + org.getRuc());
			map.put("telefono", "" + org.getTelefono());
			map.put("ciudad", "" + org.getCiudad());
			map.put("pais", "" + org.getPais());
			map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
					+ usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);

			map.put("cliente", cl.getPersona().getNombre() + " " + cl.getPersona().getApellido());

			System.out.println("Tipo detallado:   " + detallado);
			report = new Reporte();

			if (detallado == 1) {
				List<Venta> obb = entityRepository.getVentaPorRangoFechaClienteHql(fecI, fecF, idCliente);
				System.out.println(obb.size() + " ************lis obb");
				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							;
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorCliente", response);
					return new ResponseEntity<String>(HttpStatus.OK);
				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}

			}

			if (detallado == 2) {
				List<Venta> obb = entityRepository.getVentaPorRangoFechaClienteHql(fecI, fecF, idCliente);
				System.out.println(obb.size() + " ************lis obb");
				if (obb.size() > 0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd = totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto = totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - " + detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId() + "");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							;
							detalleProducto.setIva(detAux.getIva() + "");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto - totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					listadoDetallado = obb;
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoPorClienteDetallado", response);
					return new ResponseEntity<String>(HttpStatus.OK);

				} else {
					return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"),
							HttpStatus.CONFLICT);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "/reporteVentaRangoFechaExtractoCliente/{fechaI}/{fechaF}/{idCliente}", method = RequestMethod.GET)
	public List<DetalleProducto> getReporteExtractoCliente(@PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idCliente) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: " + fechaI + ":::: " + fechaF);
		List<DetalleProducto> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		List<Object[]> listUltimaVentas = new ArrayList<Object[]>();
		List<DetalleProducto> listRetornoProducto = new ArrayList<>();
		List<DetalleServicios> listRetornoServicio = new ArrayList<>();
		ReporteConfig reportConfig = reporteConfigRepository.getOne(4);

		Double obProducto = 0.0;
		Double obServicio = 0.0;

		try {

			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF = formater.parse(fechaF);
			System.out.println("pasoo3");
			// Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: " + fecI + "  : : : FFIN: " + fecF + " : id Func:  " + idCliente);
			obProducto = entityRepository.getReporteVentaRangoPorClienteProductoDetalle(fecI, fecF, idCliente);
			System.out.println("ejecutoo el metodo de reangoooo");
			obServicio = entityRepository.getReporteVentaRangoPorClienteServicioDetalle(fecI, fecF, idCliente);

			if (obProducto != 0) {
				// map.put("totalProducto", obProducto);
			} else {// map.put("totalProducto", 0.0);}
			}

			if (obServicio != 0) {
				// map.put("totalServicio",obServicio);
			} else {// map.put("totalServicio", 0.0);
			}
			listUltimaVentas = entityRepository.getReporteVentaRangoPorClienteultimaVenta(idCliente);
			// map.put("ultimaVenta", listUltimaVentas.get(0)[1].toString());

			// map.put("cliente", cl.getPersona().getNombre()+ " "+
			// cl.getPersona().getApellido());
			// map.put("totalVenta", (obProducto+obServicio));

			report = new Reporte();

			List<Object[]> dpProducto = detalleProductoRepository.listaDetalleProductoAllPorClientes(fecI, fecF,
					idCliente);
			for (Object[] ob : dpProducto) {
				DetalleProducto dp = new DetalleProducto();
				dp.getVenta().setId(Integer.parseInt(ob[0].toString()));
				dp.setDescripcion("PROD: " + ob[1].toString());
				dp.getProducto().setCodbar(ob[2].toString() + "/" + ob[0].toString());
				dp.getProducto().getMarca().setDescripcion(ob[3].toString());
				dp.setCantidad(Double.parseDouble(ob[4].toString()));
				dp.setPrecio(Double.parseDouble(ob[5].toString()));
				dp.setDescuento(Double.parseDouble(ob[6].toString()));
				dp.setSubTotal(Double.parseDouble(ob[7].toString()));
				dp.setIva(ob[8].toString());
				dp.setCantidadDevolucion(Double.parseDouble(ob[9].toString()));
				listRetornoProducto.add(dp);
			}

			List<Object[]> dpServicio = detalleProductoRepository.listaDetalleServicioAllPorCliente(fecI, fecF,
					idCliente);
			for (Object[] ob : dpServicio) {
				DetalleServicios dp = new DetalleServicios();
				dp.getServicio().setId(Integer.parseInt(ob[0].toString()));
				dp.setDescripcion(ob[1].toString());
				dp.setCantidad(Double.parseDouble(ob[2].toString()));
				dp.setPrecio(Double.parseDouble(ob[3].toString()));
				dp.setSubTotal(Double.parseDouble(ob[4].toString()));
				listRetornoServicio.add(dp);
			}
			Venta ven = new Venta();
			System.out.println("ENTROO FALSE");
			ven.getCliente().getPersona().setNombre("");
			ven.getFuncionarioV().getPersona().setNombre("");
			ven.getDocumento().setDescripcion("");
			ven.setNroDocumento("");
			ven.setFechaFactura(FechaUtil.convertirFechaStringADateUtil("2020-11-11"));
			ven.setTotal(1200.0);
			ven.setDetalleProducto(listRetornoProducto);

			for (DetalleServicios det : listRetornoServicio) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.setDescripcion("SER: " + det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId() + "");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva() + "");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				ven.getDetalleProducto().add(detalleProducto);

			}

			listado = ven.getDetalleProducto();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("size: " + listado.size());

		return listado;

	}

	private ResponseEntity<?> validarCajaSalida(List<OperacionCaja> lista) {

		if (lista == null || lista.isEmpty()) {
			return error("No existen formas de pago para validar caja");
		}

		// 🔹 Obtener apertura una sola vez (asumo misma caja)
		AperturaCaja aper = aperturaCajaRepository.getAperturaCajaPorIdCaja(lista.get(0).getAperturaCaja().getId());

		if (aper == null) {
			return error("EL FUNCIONARIO NO POSEE UNA APERTURA DE CAJA");
		}

		double totalEfectivo = 0;
		double totalCheque = 0;
		double totalTarjeta = 0;

		for (OperacionCaja op : lista) {
			if (op.getMonto() <= 0) {
				return error("EL MONTO DE LA OPERACIÓN DEBE SER MAYOR A CERO");
			}

			// 🔹 Solo validar SALIDAS (ajustá según tu lógica)

			switch (op.getTipoOperacion().getId()) {
			case 1:
				totalEfectivo += op.getMonto();
				break;
			case 2:
				totalCheque += op.getMonto();
				break;
			case 3:
				totalTarjeta += op.getMonto();
				break;
			}

		}

		// 🔴 VALIDACIÓN FINAL (clave)
		if (totalEfectivo > aper.getSaldoActual()) {
			return error("Monto en efectivo disponible supera el monto a procesar");
		}

		if (totalCheque > aper.getSaldoActualCheque()) {
			return error("Monto en cheque disponible supera el monto a procesar");
		}

		if (totalTarjeta > aper.getSaldoActualTarjeta()) {
			return error("Monto en tarjeta disponible supera el monto a procesar");
		}

		return null;
	}

	@Transactional
	public List<OperacionCaja> procesarOperacionCajaAnulacionVenta(Funcionario f, Venta ent,
			List<OperacionCaja> listaOperacion) {
		if (listaOperacion == null || listaOperacion.isEmpty()) {
			throw new RuntimeException("No existen operaciones de caja para procesar");
		}

		List<OperacionCaja> resultado = new ArrayList<>();
		OperacionCaja primera = listaOperacion.get(0);
		Concepto concepto = conceptoRepository.findById(primera.getConcepto().getId())
				.orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		// 🔹 Crear cabecera
		OperacionCajaCabecera cab = new OperacionCajaCabecera();
		cab.setFecha(new Date());
		cab.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cab.setReferenciaOperacion(ent.getId());
		cab.setTipo("SALIDA");
		cab.setMotivo(concepto.getDescripcion() + " REF.: " + ent.getId() + " POR: " + f.getPersona().getNombre() + " "
				+ f.getPersona().getApellido());
		cab.setConcepto(concepto);
		cab.setAperturaCaja(primera.getAperturaCaja());
		OperacionCajaCabecera savedCabecera = operacionCajaCabeceraRepository.save(cab);
		// 🔹 Procesar operaciones
		for (OperacionCaja ope : listaOperacion) {
			System.out.println("EJECUTO OPERACION anulacion de venta");
			ope.setTipo("SALIDA");
			ope.setMotivo(concepto.getDescripcion() + " REF.: " + ent.getId() + " POR: " + f.getPersona().getNombre()
					+ " " + f.getPersona().getApellido());
			ope.setReferenciaOperacion(ent.getId());
			ope.setFecha(new Date());
			ope.setOperacionCajaCabecera(savedCabecera);
			OperacionCaja saveOperacion = operacionRepository.save(ope);
			int tipoOperacion = saveOperacion.getTipoOperacion().getId();
			int idApertura = saveOperacion.getAperturaCaja().getId();
			double monto = saveOperacion.getMonto();
			// 🔹 Actualizar saldo según tipo
			// 🔥 Actualizar saldos según tipo operación
			if (tipoOperacion == 1) {
				aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idApertura, monto);
			}
			if (tipoOperacion == 2) {
				aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idApertura, monto);
			}
			if (tipoOperacion == 3) {
				aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idApertura, monto);
			}

			resultado.add(saveOperacion);
		}

		return resultado;
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/anularFactura")
	public ResponseEntity<?> anularVentar(@RequestPart("venta") Venta ventas,
			@RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista,
			@RequestPart("cuentaCobrar") CuentaCobrarCabecera cuentaCobrarCabecera,
			OAuth2Authentication authentication) {
		try {
			Venta v = entityRepository.getVentaPorCabeceraId(ventas.getId());
			AutoImpresorDetalleVenta refeFactura = new AutoImpresorDetalleVenta();
			refeFactura = autoImpresorDetalleVentaRepository.consultaDetalleAutoImpresorPorVentaId(v.getId());
			EmpaqueCabecera emp = empaqueRepository.getEmpaquePorVentaCabecerass(v.getId());
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			System.out.println("XVC: " + usuario.getFuncionario().getPersona().getNombre());
			if (v.getTipo().equals("1") || v.getTipo().equals("CONTADO")) {
				if (v.getTotalDevolucion() > 0) {
					return error("No se puede anular venta contado que ya tuvo devolución parcial.");
				} else {

					ResponseEntity<?> validacionCaja = validarCajaSalida(operacionCajaLista);
					if (validacionCaja != null)
						return validacionCaja;
					procesarOperacionCajaAnulacionVenta(usuario.getFuncionario(), v, operacionCajaLista);
					AperturaCaja aperDeFacturacion = aperturaCajaRepository
							.findById(operacionCajaLista.get(0).getAperturaCaja().getId()).get();

					// genera descuento del cierre si esta cerrrado la caja
					System.out.println("apertura quien facturo estado: " + aperDeFacturacion.isEstado());
					if (!aperDeFacturacion.isEstado()) {
						System.out.println("entro anulacion desde del cierre");
						int cierreId = cierreCajaRepository.IdCierreCaja(aperDeFacturacion.getId());
						System.out.println("id del cierre: " + cierreId);
						for (OperacionCaja opp : operacionCajaLista) {
							System.out.println("monto para descontar del monto cierre y de la tesoreria: "
									+ opp.getOperacionCajaCabecera().getId());
							System.out.println("id para descontar monto cierre y de la tesoreria: " + opp.getMonto());
							if (opp.getTipoOperacion().getId() == 1) {
								System.out.println("anulo en cierre efec por apertura: " + aperDeFacturacion.getId()
										+ " monto: " + v.getTotal());
								cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(
										aperDeFacturacion.getId(), opp.getMonto());
								tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getTotal());

							}
							if (opp.getTipoOperacion().getId() == 2) {
								System.out.println("anulo en cierre che por apertura: " + aperDeFacturacion.getId()
										+ " monto: " + v.getTotal());
								cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaCheque(
										aperDeFacturacion.getId(), opp.getMonto());
								tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaCheque(cierreId,
										v.getTotal());
							}
							if (opp.getTipoOperacion().getId() == 3) {
								System.out.println("anulo en cierre tar por apertura: " + aperDeFacturacion.getId()
										+ " monto: " + v.getTotal());
								cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(
										aperDeFacturacion.getId(), opp.getMonto());
								tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(cierreId,
										v.getTotal());
							}
						}
					} else {
						System.out.println(
								"esta abierto todabia el funcinario de venta no se descuenta de los cierre ni de tesoreria");

					}

					// actualiza stock reposicion de stock
					List<DetalleProducto> detalle = getDetalleProducto(detalleProductoRepository.lista(v.getId()));
					for (DetalleProducto det : detalle) {
						actualizarProductoBaseAumentarCorregido(det.getProducto().getId(), det.getCantidad(),
								det.getSubTotal(), det.getPrecio(), usuario.getFuncionario().getId(), "", v.getId());
					}
					// geenra regsitro de anulaciones de venta por funcionario
					AnulacionesVenta vvv = new AnulacionesVenta();
					vvv.setFecha(new Date());
					vvv.getFuncionario().setId(usuario.getFuncionario().getId());
					vvv.setTotal(v.getTotal());
					vvv.setMotivo("ANULACIÓN VENTA REF.:  " + v.getId() + " FUNCIONARIO: "
							+ usuario.getFuncionario().getPersona().getNombre() + " "
							+ usuario.getFuncionario().getPersona().getApellido());
					vvv.getVenta().setId(v.getId());
					anulacionVentaRepository.save(vvv);
					// acutalzia el estado de la venta en la tabla principa de venta
					entityRepository.findByActualizarFacturas(v.getId(), "ANULADO");
					// verifica si la venta de tuvo factura emitida y anula tambien

					if (refeFactura != null) {
						// refeFactura.setEstado("ANULADO");
						autoImpresorDetalleVentaRepository.findByActualizarEstadoFacturaEmitida(refeFactura.getId(),
								"ANULADO");
					}
					// verifica si se vendio por lote empaque
					if (emp != null) {
						// Buscar el detalle de empaque que corresponde a esta venta
						EmpaqueDetalle detalleEmpaque = emp.getEmpaqueDetalle().stream()
								.filter(d -> d.getVenta() != null && d.getVenta().getId() == v.getId()).findFirst()
								.orElse(null);

						if (detalleEmpaque != null) {
							// Descontar el subtotal de la venta del total del empaque
							double nuevoTotalEmpaque = emp.getTotalFinalizado() - v.getTotal();
							emp.setTotalFinalizado(nuevoTotalEmpaque);
							// Reducir la cantidad de items en el empaque
							int nuevoItems = emp.getItemsVenta() - 1;
							emp.setItemsVenta(Math.max(nuevoItems, 0)); // evitar negativos
							emp.setTotalAnulado(v.getTotal());
							// Actualizar total en letras
							emp.setTotalLetras(NumerosALetras.convertirNumeroALetras(nuevoTotalEmpaque));

							// Cambiar estado si no hay más ventas
							boolean quedanVentas = emp.getEmpaqueDetalle().stream().anyMatch(
									d -> d.getVenta() != null && !"ANULADO".equalsIgnoreCase(d.getVenta().getEstado()));
							if (!quedanVentas) {
								emp.setEstado("ANULADO"); // o el estado que corresponda
							}
							// Guardar cambios
							empaqueRepository.save(emp);
							// También podés actualizar el detalle de empaque directamente
							detalleEmpaque.setSubtotalPresupuesto(0.0);
							detalleEmpaque.setItemsPedidoDetalle(0);
							empaqueDetalleRepository.save(detalleEmpaque);
						}
					} else {
						System.out.println("no tiene empaque asociada");
					}
					return new ResponseEntity<String>(HttpStatus.OK);
				}

			}

			if (v.getTipo().equals("2") || v.getTipo().equals("CREDITO")) {
				if (v.getTotalDevolucion() > 0) {
					return error("No se puede anular venta credito que ya tuvo devolución parcial.");
				} else {
					if (v.getEntrega() > 0) {

						ResponseEntity<?> validacionCaja = validarCajaSalida(operacionCajaLista);
						if (validacionCaja != null)
							return validacionCaja;
						procesarOperacionCajaAnulacionVenta(usuario.getFuncionario(), v, operacionCajaLista);
						AperturaCaja aperDeFacturacion = aperturaCajaRepository
								.findById(operacionCajaLista.get(0).getAperturaCaja().getId()).get();
						// genera descuento del cierre si esta cerrrado la caja
						System.out.println("apertura quien facturo estado: " + aperDeFacturacion.isEstado());
						if (!aperDeFacturacion.isEstado()) {
							System.out.println("entro anulacion desde del cierre");
							int cierreId = cierreCajaRepository.IdCierreCaja(aperDeFacturacion.getId());
							System.out.println("id del cierre: " + cierreId);
							for (OperacionCaja opp : operacionCajaLista) {
								System.out.println("monto para descontar del monto cierre y de la tesoreria: "
										+ opp.getOperacionCajaCabecera().getId());
								System.out
										.println("id para descontar monto cierre y de la tesoreria: " + opp.getMonto());
								if (opp.getTipoOperacion().getId() == 1) {
									System.out.println("anulo en cierre efec por apertura: " + aperDeFacturacion.getId()
											+ " monto: " + v.getTotal());
									cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(
											aperDeFacturacion.getId(), v.getTotal());
									tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId,
											v.getTotal());
								}
								if (opp.getTipoOperacion().getId() == 2) {
									System.out.println("anulo en cierre che por apertura: " + aperDeFacturacion.getId()
											+ " monto: " + v.getTotal());
									cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaCheque(
											aperDeFacturacion.getId(), v.getTotal());
									tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaCheque(cierreId,
											v.getTotal());
								}
								if (opp.getTipoOperacion().getId() == 3) {
									System.out.println("anulo en cierre tar por apertura: " + aperDeFacturacion.getId()
											+ " monto: " + v.getTotal());
									cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(
											aperDeFacturacion.getId(), v.getTotal());
									tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(cierreId,
											v.getTotal());
								}
							}
						} else {
							System.out.println(
									"esta abierto todabia el funcinario de venta no se descuenta de los cierre ni de tesoreria");

						}

					}

					// verifica si la venta de tuvo factura emitida y anula tambien
					if (refeFactura != null) {
						// refeFactura.setEstado("ANULADO");
						autoImpresorDetalleVentaRepository.findByActualizarEstadoFacturaEmitida(refeFactura.getId(),
								"ANULADO");
					}
					// verifica si se vendio por lote empaque
					if (emp != null) {
						// Buscar el detalle de empaque que corresponde a esta venta
						EmpaqueDetalle detalleEmpaque = emp.getEmpaqueDetalle().stream()
								.filter(d -> d.getVenta() != null && d.getVenta().getId() == v.getId()).findFirst()
								.orElse(null);

						if (detalleEmpaque != null) {

							// Descontar el subtotal de la venta del total del empaque
							double nuevoTotalEmpaque = emp.getTotalFinalizado() - v.getTotal();
							emp.setTotalFinalizado(nuevoTotalEmpaque);
							// Reducir la cantidad de items en el empaque
							int nuevoItems = emp.getItemsVenta() - 1;
							emp.setItemsVenta(Math.max(nuevoItems, 0)); // evitar negativos
							emp.setTotalAnulado(v.getTotal());
							// Actualizar total en letras
							emp.setTotalLetras(NumerosALetras.convertirNumeroALetras(nuevoTotalEmpaque));

							// Cambiar estado si no hay más ventas
							boolean quedanVentas = emp.getEmpaqueDetalle().stream().anyMatch(
									d -> d.getVenta() != null && !"ANULADO".equalsIgnoreCase(d.getVenta().getEstado()));
							if (!quedanVentas) {
								emp.setEstado("ANULADO"); // o el estado que corresponda
							}
							// Guardar cambios
							empaqueRepository.save(emp);
							// También podés actualizar el detalle de empaque directamente
							detalleEmpaque.setSubtotalPresupuesto(0.0);
							detalleEmpaque.setItemsPedidoDetalle(0);

							empaqueDetalleRepository.save(detalleEmpaque);
						}
					} else {
						System.out.println("no tiene empaque asociada");
					}

					System.out.println("vino aca");
					CuentaCobrarCabecera cuenta = cuentaCobrarRepository.getCuentaCabeceraPorVentaId(v.getId());
					ordenPagareRepository.eliminarPorCuentaCobrarCabeceraId(cuenta.getId());

					// Eliminar cuenta por cobrar asociada
					if (cuenta != null) {
						System.out.println("cuenta distinto nulla");
						if (cuenta.getSaldo() > 0) {
							System.out.println("cuenta saldo mayor a cero");

							cuentaCobrarDetalleRepository.liquidarDetalle(cuenta.getId(), new Date(), true);
							cuentaCobrarRepository.liquidarCuentaCabecera(cuenta.getId());
							List<DetalleProducto> detalle = getDetalleProducto(
									detalleProductoRepository.lista(v.getId()));
							for (DetalleProducto det : detalle) {
								System.out.println("entro for update detalle prododsf");
								actualizarProductoBaseAumentarCorregido(det.getProducto().getId(), det.getCantidad(),
										det.getSubTotal(), det.getPrecio(), usuario.getFuncionario().getId(), "",
										v.getId());
							}
						} else {
							return new ResponseEntity<>(
									new CustomerErrorType(
											"NO SE PUEDE ANULAR VENTA CREDITO QUE YA SE PAGO POR LA TOTALIDAD"),
									HttpStatus.CONFLICT);
						}
					}
					// geenra regsitro de anulaciones de venta por funcionario
					AnulacionesVenta vvv = new AnulacionesVenta();
					vvv.setFecha(new Date());
					vvv.getFuncionario().setId(usuario.getFuncionario().getId());
					vvv.setTotal(v.getTotal());
					vvv.setMotivo("ANULACIÓN VENTA REF.:  " + v.getId() + " FUNCIONARIO: "
							+ usuario.getFuncionario().getPersona().getNombre() + " "
							+ usuario.getFuncionario().getPersona().getApellido());
					vvv.getVenta().setId(v.getId());
					anulacionVentaRepository.save(vvv);
					// acutalzia el estado de la venta en la tabla principa de venta
					entityRepository.findByActualizarFacturas(v.getId(), "ANULADO");
					return new ResponseEntity<String>(HttpStatus.OK);
				}
			}
			if (v.getTipo().equals("3")) {
				return new ResponseEntity<String>(HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Erro al generar anulación de venta"),
					HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "/reporteVentaRangoGrupoListado/{fechaI}/{fechaF}/{idGrupo}", method = RequestMethod.GET)
	public List<Venta> getReporteVentaRangoGrupoListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idGrupo) throws IOException {
		List<Venta> listado = new ArrayList<>();
		Double totalCostoProd = 0.0, totalProducto = 0.0, totalUtilidadProducto = 0.0, totalServicio = 0.0,
				totalVenta = 0.0;
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI, fecF;
			fecI = FechaUtil.setFechaHoraInicial(fechaI);
			fecF = FechaUtil.setFechaHoraFinal(fechaF);
			List<Venta> obb = new ArrayList<>();
			if (idGrupo == 0) {
				obb = entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
			} else {
				obb = entityRepository.getVentaPorRangoFechaPorGrupoHql(fecI, fecF, idGrupo);
			}
			listado = cargarListaReporte(obb);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listado;
	}

	public List<Venta> cargarListaReporte(List<Venta> obb) {
		List<Venta> listado = new ArrayList<>();
		if (obb.size() > 0) {
			for (int i = 0; i < obb.size(); i++) {
				if (obb.get(i).getTipo().equals("1")) {
					obb.get(i).setTipo("CONTADO");
				}
				if (obb.get(i).getTipo().equals("2")) {
					obb.get(i).setTipo("CREDITO");
				}
				if (obb.get(i).getTipo().equals("3")) {
					obb.get(i).setTipo("NOTA CREDITO");
				}

				for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
					DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
					DetalleProducto d = new DetalleProducto();
					d.getProducto().setId(detAux.getServicio().getId());
					d.setDescripcion("SER - " + detAux.getDescripcion());
					d.getProducto().setCodbar(detAux.getServicio().getId() + "");
					d.setCantidad(detAux.getCantidad());
					d.setPrecio(detAux.getPrecio());
					d.getProducto().getUnidadMedida().setDescripcion("UN");
					d.setIva(detAux.getIva() + "");
					d.setSubTotal(detAux.getSubTotal());
					d.setMontoIva(detAux.getMontoIva());
					obb.get(i).getDetalleProducto().add(d);

					System.out.println("SERRRR:" + d.getDescripcion());
				}
				// obb.get(i).getDetalleServicio().clear();
			}
			listado = obb;
		}
		return listado;
	}

	@RequestMapping(value = "/reporteVentaRangoGrupo/{fechaI}/{fechaF}/{idGrupo}/{detallado}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoGrupo(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idGrupo, @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: " + fechaI + ":::: " + fechaF + " ::::" + idGrupo
				+ " :::: " + detallado);
		List<Venta> listado = new ArrayList<>();
		List<DetalleProducto> listadoDetalle = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		try {
			System.out.println("entroo tryy" + detallado);
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI, fecF;
			fecI = FechaUtil.setFechaHoraInicial(fechaI);
			fecF = FechaUtil.setFechaHoraFinal(fechaF);
			Object[][] objeto;
			Grupo g = null;
			List<Object[]> obb = null;

			if (idGrupo == 0) {
				obb = entityRepository.getReporteVentaRangoGrupoDetalladoAll(fecI, fecF);
				g = new Grupo();
				g.setDescripcion("TODOS");
			} else {
				obb = entityRepository.getReporteVentaRangoGrupoDetallado(fecI, fecF, idGrupo);
				g = grupoService.getOne(idGrupo);
				System.out.println("SIZE LISTADO: " + listado.size());

			}

			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", "" + org.getNombre());
			map.put("direccion", "" + org.getDireccion());
			map.put("ruc", "" + org.getRuc());
			map.put("telefono", "" + org.getTelefono());
			map.put("ciudad", "" + org.getCiudad());
			map.put("pais", "" + org.getPais());
			map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
					+ usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);

			map.put("grupo", g.getDescripcion());

			System.out.println("Tipo detallado:   " + detallado);
			report = new Reporte();

			if (detallado == 1) {
				Venta ven = new Venta();
				System.out.println("ENTROO FALSE");
				ven.getCliente().getPersona().setNombre("");
				ven.getFuncionarioV().getPersona().setNombre("");
				ven.getDocumento().setDescripcion("");
				ven.setNroDocumento("");
				ven.setFechaFactura(FechaUtil.convertirFechaStringADateUtil("2020-11-11"));
				ven.setTotal(1200.0);
				listado.add(ven);
				report.reportPDFDescarga(listado, map, "ReporteVentaRangoGrupo", response);

			}
			if (detallado == 2) {

				System.out.println("ENTROO TRUE");
				if (idGrupo == 0) {
					obb = entityRepository.getReporteVentaRangoGrupoDetalladoAll(fecI, fecF);

				} else {
					obb = entityRepository.getReporteVentaRangoGrupoDetallado(fecI, fecF, idGrupo);
				}
				for (Object[] ob : obb) {
					DetalleProducto dt = new DetalleProducto();
					dt.setDescripcion(ob[0].toString());
					dt.setPrecio(Double.parseDouble(ob[1].toString()));
					dt.setCantidad(Double.parseDouble(ob[2].toString()));
					dt.setSubTotal(Double.parseDouble(ob[3].toString()));
					dt.setCosto(Double.parseDouble(ob[4].toString()));
					dt.setTipoPrecio(ob[5].toString());
					listadoDetalle.add(dt);
				}

				report.reportPDFDescarga(listadoDetalle, map, "ReporteVentaRangoGrupoDetallado", response);
			}

			// report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario",
			// response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "/descargarPdf/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> resumenConcepto(HttpServletResponse response, OAuth2Authentication authentication,
			@PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
		Venta pre = new Venta();
		pre = entityRepository.getOne(id);
		System.out.println("ZONA DESCR: " + pre.getZona().getDescripcion());
		if (pre.getTipo().equals("1")) {
			pre.setTipo("1");
		}
		if (pre.getTipo().equals("2")) {
			pre.setTipo("2");
		}
		if (pre.getTipo().equals("3")) {
			pre.setTipo("3");
		}
		System.out.println("doc:  " + pre.getDocumento().getId());
		ReporteConfig reportConfig = null;
		if (pre.getDocumento().getId() == 1) {
			reportConfig = reporteConfigRepository.findById(5).orElse(null);
		}

		if (pre.getDocumento().getId() == 2) {
			reportConfig = reporteConfigRepository.findById(7).orElse(null);
		}

		if (pre.getDocumento().getId() == 3) {
			reportConfig = reporteConfigRepository.findById(7).orElse(null);
		}

		List<Venta> listado = new ArrayList<Venta>();
		listado.add(pre);

		for (int i = 0; i < listado.size(); i++) {

			for (int j = 0; j < listado.get(i).getDetalleServicio().size(); j++) {
				DetalleServicios detAux = listado.get(i).getDetalleServicio().get(j);
				DetalleProducto detAgregar = new DetalleProducto();

				detAgregar.setDescripcion("SER - " + detAux.getDescripcion());
				detAgregar.getProducto().setCodbar(detAux.getServicio().getId() + "");
				detAgregar.setCantidad(detAux.getCantidad());
				detAgregar.setPrecio(detAux.getPrecio());
				detAgregar.getProducto().getUnidadMedida().setDescripcion("UN");
				;
				detAgregar.setIva(detAux.getIva() + "");
				detAgregar.setSubTotal(detAux.getSubTotal());
				detAgregar.setMontoIva(detAux.getMontoIva());
				listado.get(i).getDetalleProducto().add(detAgregar);
			}
		}
		try {

			report = new Reporte();
			if (reportConfig == null) {
				System.out.println("Entro descarga no configurada impresion por defecto");

				Map<String, Object> map = new HashMap<>();
				map.put("org", "" + org.getNombre());
				map.put("direccion", "" + org.getDireccion());
				map.put("ruc", "" + org.getRuc());
				map.put("telefono", "" + org.getTelefono());
				map.put("ciudad", "" + org.getCiudad());
				map.put("pais", "" + org.getPais());
				map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
						+ usuario.getFuncionario().getPersona().getApellido());

				report.reportPDFDescarga(listado, map, "ReporteVentaPdf", response);
			} else {
				System.out.println("Entro descarga configurada impresion desde ajuste");
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("org", "" + org.getNombre());
				map.put("direccion", "" + org.getDireccion());
				map.put("ruc", "" + org.getRuc());
				map.put("telefono", "" + org.getTelefono());
				map.put("ciudad", "" + org.getCiudad());
				map.put("pais", "" + org.getPais());
				map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
						+ usuario.getFuncionario().getPersona().getApellido());

				report.reportPDFDescarga(listado, map, reportConfig.getNombreReporte(), response);
			}
			// report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft
			// Print to PDF");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/resumenDiarioPdf/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public ResponseEntity<?> getResumenDiario(HttpServletResponse response, OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		List<Object[]> listObjVenCre = new ArrayList<>();
		List<Object[]> listObjVenCon = new ArrayList<>();
		List<Object[]> listObjComCre = new ArrayList<>();
		List<Object[]> listObjComCon = new ArrayList<>();
		List<Object[]> listObjCob = new ArrayList<>();
		List<Object[]> listObjPago = new ArrayList<>();
		List<Object[]> listObjCuentaCobrar = new ArrayList<>();
		List<Object[]> listObjCuentaPagar = new ArrayList<>();
		List<Object[]> listObjEntregaInicialVentaCredito = new ArrayList<>();

		List<MovimientoPorConceptosAuxiliar> listRetorno = new ArrayList<>();

		try {
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF = formater.parse(fechaF);
			System.out.println("pasoo3");
			// Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(0);
			System.out.println("FI: " + fecI + "  : : : FFIN: " + fecF + " : id Func:  ");

			listObjVenCon = entityRepository.getResumenVentaContado(fecI, fecF);
			for (int i = 0; i < listObjVenCon.size(); i++) {
				Object[] ob = listObjVenCon.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				if (ob[1].toString().equals("1")) {
					v.setDescripcion("VENTA CONTADO");
				}
				listRetorno.add(v);
			}

			listObjVenCre = entityRepository.getResumenVentaCredito(fecI, fecF);
			for (int i = 0; i < listObjVenCre.size(); i++) {
				Object[] ob = listObjVenCre.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				if (ob[1].toString().equals("2")) {
					v.setDescripcion("VENTA CREDITO");
				}
				listRetorno.add(v);
			}
			listObjComCon = entityRepository.getResumenCompraContado(fecI, fecF);
			for (int i = 0; i < listObjComCon.size(); i++) {
				Object[] ob = listObjComCon.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjComCre = entityRepository.getResumenCompraCredito(fecI, fecF);
			for (int i = 0; i < listObjComCre.size(); i++) {
				Object[] ob = listObjComCre.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			listObjCob = entityRepository.getResumenCobros(fecI, fecF);
			for (int i = 0; i < listObjCob.size(); i++) {
				Object[] ob = listObjCob.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			listObjPago = entityRepository.getResumenPagos(fecI, fecF);
			for (int i = 0; i < listObjPago.size(); i++) {
				Object[] ob = listObjPago.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			listObjCuentaCobrar = entityRepository.getResumenCuentaCobrar(fecI, fecF);
			for (int i = 0; i < listObjCuentaCobrar.size(); i++) {
				Object[] ob = listObjCuentaCobrar.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjCuentaPagar = entityRepository.getResumenCuentaPagar(fecI, fecF);
			for (int i = 0; i < listObjCuentaPagar.size(); i++) {
				Object[] ob = listObjCuentaPagar.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjEntregaInicialVentaCredito = entityRepository.getResumenEntregaInicialVentaCredito(fecI, fecF);
			for (int i = 0; i < listObjEntregaInicialVentaCredito.size(); i++) {
				Object[] ob = listObjEntregaInicialVentaCredito.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			for (int i = 0; i < listRetorno.size(); i++) {
				System.out.println(listRetorno.get(i).getDescripcion() + " = " + listRetorno.get(i).getMonto());
			}
			System.out.println(listRetorno.size() + " *SIXE RETRONO LISTA");

			Map<String, Object> map = new HashMap<>();
			map.put("org", "" + org.getNombre());
			map.put("direccion", "" + org.getDireccion());
			map.put("ruc", "" + org.getRuc());
			map.put("telefono", "" + org.getTelefono());
			map.put("ciudad", "" + org.getCiudad());
			map.put("pais", "" + org.getPais());
			map.put("desde", fecI);
			map.put("hasta", fecF);
			map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
					+ usuario.getFuncionario().getPersona().getApellido());

			report = new Reporte();
			report.reportPDFDescarga(listRetorno, map, "ReporteBalanceGeneral", response);
			// report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft
			// Print to PDF");

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/actualizarVentaEstadoDesdeFinalizacionEmpaque/{id}/{estado}")
	public ResponseEntity<?> actualizarVentaEstado(@PathVariable int id, @PathVariable String estado) {
		try {
			int rows = entityRepository.actualizarEstadoVenta(id, estado);

			if (rows > 0) {
				return new ResponseEntity<>(new CustomerErrorType("Empaque arreglo registrado en caja"),
						HttpStatus.CREATED);

			} else {
				return new ResponseEntity<>(new CustomerErrorType("No se ha podido actualizar estado"),
						HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("No se ha podido actualizar estado"),
					HttpStatus.CONFLICT);

		}
	}

	private ResponseEntity<CustomerErrorType> error(String mensaje) {
		return new ResponseEntity<>(new CustomerErrorType(mensaje), HttpStatus.CONFLICT);
	}

	public ResponseEntity<?> validarCaja(List<OperacionCaja> operacionCajaLista) {
		System.out.println("entro validacion de cajas ");

		if (operacionCajaLista == null || operacionCajaLista.isEmpty()) {
			return error("No existen formas de pago para validar caja");
		}

		for (OperacionCaja op : operacionCajaLista) {
			System.out.println("apertura : " + op.getAperturaCaja().getId());
			if (op.getAperturaCaja() == null || op.getAperturaCaja().getId() <= 0) {
				return error("El funcionario no posee una apertura de caja asignada");
			}

			AperturaCaja aper = aperturaCajaRepository.getAperturaCajaPorIdCaja(op.getAperturaCaja().getId());

			if (aper == null) {
				return error("EL FUNCIONARIO NO POSEE UNA APERTURA CAJA A SU NOMBRE");
			}

			// 🔥 OPCIONAL PRO (MUY RECOMENDADO)
			// if (!aper.getEstado().equals("ABIERTO")) {
			// return error("La apertura de caja no está activa");
			// }
		}

		return null;
	}

	/*
	 * 
	 * @Transactional
	 * 
	 * @RequestMapping(method=RequestMethod.POST) public ResponseEntity<?>
	 * guardarReformateado(
	 * 
	 * @RequestPart("venta") Venta venta,
	 * 
	 * @RequestPart("operacionCaja") OperacionCaja operacionCaja,
	 * 
	 * @RequestPart("cuentaCobrar") CuentaPagarCabecera cuentaPagarCabecera) {
	 * 
	 * try { // 1. Validar compra y detalles ResponseEntity<?> validacionCompra =
	 * validarVenta(venta); if (validacionCompra != null) return validacionCompra;
	 * 
	 * // 2. Validar flujo según tipo (contado/crédito) if
	 * ("CONTADO".equals(venta.getTipo()) && "FACTURADO".equals(venta.getEstado()))
	 * { ResponseEntity<?> validacionCaja = validarCaja(operacionCaja); if
	 * (validacionCaja != null) return validacionCaja; } else if
	 * ("CREDITO".equals(venta.getTipo()) && "FACTURADO".equals(venta.getEstado()))
	 * { if (venta.getEntrega() > 0) { ResponseEntity<?> validacionCajaEntrega =
	 * validarCaja(operacionCaja); if (validacionCajaEntrega != null) return
	 * validacionCajaEntrega; } ResponseEntity<?> validacionCuenta =
	 * validarCuentaPagar(cuentaPagarCabecera); if (validacionCuenta != null) return
	 * validacionCuenta; }
	 * 
	 * // 3. Guardar compra y detalles Compra saved = (compra);
	 * 
	 * // 4. Procesar movimientos financieros if ("CONTADO".equals(compra.getTipo())
	 * && "FACTURADO".equals(compra.getEstado())) { procesarOperacionCaja(saved,
	 * operacionCaja);
	 * 
	 * } else if ("CREDITO".equals(compra.getTipo()) &&
	 * "FACTURADO".equals(compra.getEstado())) { if (compra.getEntrega() > 0) {
	 * procesarOperacionCaja(saved, operacionCaja); // entrega }
	 * procesarCuentaPagar(saved, cuentaPagarCabecera); }
	 * 
	 * return new ResponseEntity<>(saved, HttpStatus.CREATED);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); return new ResponseEntity<>(new
	 * CustomerErrorType("Error: " + e.getMessage()),
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */

	@RequestMapping(value = "/comparativaVenta/{actual}/{anterior}", method = RequestMethod.GET)
	public ComparativaVentasDTO getVentasPorMes(@PathVariable int actual, @PathVariable int anterior) {
		ComparativaVentasDTO dto = new ComparativaVentasDTO();
		dto.setActual(mapear(entityRepository.getVentasPorMes(actual)));
		dto.setAnterior(mapear(entityRepository.getVentasPorMes(anterior)));
		return dto;
	}

	private List<VentaMensualDTO> mapear(List<Object[]> lista) {

		return lista.stream().map(obj -> {

			String mes = obj[0].toString();

			Double totalVenta = obj[2] != null ? Double.parseDouble(obj[2].toString()) : 0.0;
			Double totalDevolucion = obj[3] != null ? Double.parseDouble(obj[3].toString()) : 0.0;

			return new VentaMensualDTO(mes, totalVenta, totalDevolucion);

		}).collect(Collectors.toList());
	}

	public List<ProductoVentaIA> mapearModeloProductoIA(List<Object[]> datos) {

		Map<Integer, ProductoVentaIA> mapa = new LinkedHashMap<>();

		for (Object[] row : datos) {

			Integer productoId = ((Number) row[0]).intValue();
			String nombre = (String) row[1];
			Double total = ((Number) row[3]).doubleValue();

			ProductoVentaIA prod = mapa.get(productoId);

			if (prod == null) {
				prod = new ProductoVentaIA();
				prod.setId(productoId);
				prod.setNombre(nombre);
				prod.setVentas(new ArrayList<>());
				mapa.put(productoId, prod);
			}

			// 🔥 ESTE ES EL PUNTO CLAVE
			prod.getVentas().add(total);
		}

		return new ArrayList<>(mapa.values());
	}

	private List<VentaModeloIA> mapearVentaModeloIA(List<Object[]> lista) {

		return lista.stream().map(obj -> {

			String mes = obj[0].toString();

			Double totalVenta = obj[2] != null ? Double.parseDouble(obj[2].toString()) : 0.0;
			Double totalDevolucion = obj[3] != null ? Double.parseDouble(obj[3].toString()) : 0.0;
			Double neto = totalVenta - totalDevolucion;

			return new VentaModeloIA(mes, neto);

		}).collect(Collectors.toList());
	}

	@RequestMapping(value = "/prediccionVentaMensaul", method = RequestMethod.GET)
	public VentaPrediccionIA getVentasPorMes() {

		// 1. Datos reales
		List<VentaModeloIA> actual = mapearVentaModeloIA(entityRepository.getVentaPorMes());

		// 2. Python IA
		RestTemplate restTemplate = new RestTemplate();

		String url = pythonUrl + "/prediccion-ventas";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<List<VentaModeloIA>> request = new HttpEntity<>(actual, headers);

		ResponseEntity<VentaPrediccionIA> response = restTemplate.exchange(url, HttpMethod.POST, request,
				VentaPrediccionIA.class);

		// 3. Resultado
		VentaPrediccionIA dto = new VentaPrediccionIA();
		dto.setHistorico(actual);
		dto.setPrediccion(response.getBody().getPrediccion());

		return dto;
	}

	@RequestMapping(value = "/prediccionPorProducto", method = RequestMethod.GET)
	public VentaPrediccionPorProductoIA getPrediccionPorProducto() {

		// 1. HISTÓRICO REAL AGRUPADO POR PRODUCTO
		List<ProductoVentaIA> historico = mapearModeloProductoIA(entityRepository.getVentasAgrupadasPorProducto());
		System.out.println("=== PRODUCTOS ===");

		for (ProductoVentaIA p : historico) {
			System.out.println(p.getNombre() + " -> " + p.getVentas());
		}

		// 2. LLAMADA A PYTHON IA
		RestTemplate restTemplate = new RestTemplate();

		String url = pythonUrl + "/prediccion-ventas-por-producto";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		System.out.println();
		HttpEntity<List<ProductoVentaIA>> request = new HttpEntity<>(historico, headers);

		ResponseEntity<VentaPrediccionPorProductoIA> response = restTemplate.exchange(url, HttpMethod.POST, request,
				VentaPrediccionPorProductoIA.class);

		// 3. RESPUESTA FINAL BI
		VentaPrediccionPorProductoIA dto = new VentaPrediccionPorProductoIA();

		dto.setHistorico(historico);

		if (response.getBody() != null) {
			dto.setPrediccion(response.getBody().getPrediccion());
		}
		for (ProductoVentaIA p : dto.getPrediccion()) {
			System.out.println(p.getNombre());
			System.out.println("Histórico: " + p.getVentas());
			System.out.println("Predicción: " + p.getPrediccion());
		}

		return dto;
	}

	@RequestMapping(value = "/rentabilidadClasificacion", method = RequestMethod.GET)
	public List<ProductoVentaRentabilidad> getRentabilidadClasificado() {

		// 1. HISTÓRICO REAL AGRUPADO POR PRODUCTO
		List<ProductoVentaRentabilidad> rentabilidad = mapearRentabilidad(entityRepository.getRentabilidadProductos());
		System.out.println("=== PRODUCTOS ===");

		/*
		 * // 2. LLAMADA A PYTHON IA RestTemplate restTemplate = new RestTemplate();
		 * 
		 * String url = pythonUrl + "/prediccion-ventas-por-producto";
		 * 
		 * HttpHeaders headers = new HttpHeaders();
		 * headers.setContentType(MediaType.APPLICATION_JSON); System.out.println();
		 * HttpEntity<List<ProductoVentaIA>> request = new HttpEntity<>(historico,
		 * headers);
		 * 
		 * ResponseEntity<VentaPrediccionPorProductoIA> response =
		 * restTemplate.exchange( url, HttpMethod.POST, request,
		 * VentaPrediccionPorProductoIA.class );
		 * 
		 * // 3. RESPUESTA FINAL BI VentaPrediccionPorProductoIA dto = new
		 * VentaPrediccionPorProductoIA();
		 * 
		 * dto.setHistorico(historico);
		 * 
		 * if (response.getBody() != null) {
		 * dto.setPrediccion(response.getBody().getPrediccion()); } for (ProductoVentaIA
		 * p : dto.getPrediccion()) { System.out.println(p.getNombre());
		 * System.out.println("Histórico: " + p.getVentas());
		 * System.out.println("Predicción: " + p.getPrediccion()); }
		 * 
		 */

		return rentabilidad;
	}

	public List<ProductoVentaRentabilidad> mapearRentabilidad(List<Object[]> datos) {

		List<ProductoVentaRentabilidad> lista = new ArrayList<>();

		for (Object[] row : datos) {

			ProductoVentaRentabilidad dto = new ProductoVentaRentabilidad();

			dto.setProductoId(((Number) row[0]).intValue());
			dto.setNombre((String) row[1]);
			dto.setCantidadVendida(row[2] != null ? ((Number) row[2]).doubleValue() : 0);
			dto.setVentas(row[3] != null ? ((Number) row[3]).doubleValue() : 0);
			dto.setCosto(row[4] != null ? ((Number) row[4]).doubleValue() : 0);
			dto.setGanancia(row[5] != null ? ((Number) row[5]).doubleValue() : 0);
			dto.setMargen(row[6] != null ? ((Number) row[6]).doubleValue() : 0);

			dto.setClasificacion((String) row[7]);
			System.out.println("" + row[1] + " MARGEN: " + ((Number) row[6]).doubleValue());
			lista.add(dto);
		}

		return lista;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorZonaResumenListado/{idZona}/{fechaI}/{fechaF}", method = RequestMethod.GET)
	public List<Object[]> getReporteVentaRangoFechaZonaResumenListado(OAuth2Authentication authentication,
			@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idZona) throws IOException {
		List<Object[]> obb = new ArrayList<>();
		try {
			Date fecI = FechaUtil.setFechaHoraInicial(fechaI);
			Date fecF = FechaUtil.setFechaHoraFinal(fechaF);

			// Convertir a Timestamp explícitamente
			java.sql.Timestamp tsInicio = new java.sql.Timestamp(fecI.getTime());
			java.sql.Timestamp tsFin = new java.sql.Timestamp(fecF.getTime());
			obb = entityRepository.getResumenProductoPorZonaYRango(idZona, tsInicio, tsFin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obb;
	}

	@RequestMapping(value = "/reporteVentaRangoFechaPorZona/{fechaI}/{fechaF}/{idZona}", method = RequestMethod.GET)
	public ResponseEntity<?> getReporteVentaRangoFechaZona(HttpServletResponse response,
			OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF,
			@PathVariable int idZona) throws IOException {
		List<InformeVentaTotalPorProducto> lisRetorno = new ArrayList<InformeVentaTotalPorProducto>();
		List<Object[]> obb = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Zona zon = zonaRepository.getOne(idZona);
		Org org = orgRepository.findById(1).get();
		try {
			Date fecI = FechaUtil.setFechaHoraInicial(fechaI);
			Date fecF = FechaUtil.setFechaHoraFinal(fechaF);
			// Convertir a Timestamp explícitamente
			java.sql.Timestamp tsInicio = new java.sql.Timestamp(fecI.getTime());
			java.sql.Timestamp tsFin = new java.sql.Timestamp(fecF.getTime());
			obb = entityRepository.getResumenProductoPorZonaYRango(idZona, tsInicio, tsFin);
			if (obb.size() > 0) {
				for (int i = 0; i < obb.size(); i++) {
					InformeVentaTotalPorProducto inf = new InformeVentaTotalPorProducto();
					inf.setDescripcion(obb.get(i)[0].toString());
					inf.setPrecio(Double.parseDouble(obb.get(i)[1].toString()));
					inf.setCantidadVenta(Double.parseDouble(obb.get(i)[2].toString()));
					inf.setCosto(Double.parseDouble(obb.get(i)[3].toString()));
					inf.setSubTotalVenta(Double.parseDouble(obb.get(i)[4].toString()));
					inf.setCantidadDevuelta(Double.parseDouble(obb.get(i)[5].toString()));
					inf.setCostoDevuelta(Double.parseDouble(obb.get(i)[6].toString()));
					inf.setSubTotalDevuelto(Double.parseDouble(obb.get(i)[7].toString()));
					inf.setSubTotalNeta(Double.parseDouble(obb.get(i)[8].toString()));
					inf.setFuncionario(obb.get(i)[9].toString());
					System.out.println("COSTO real : " + inf.getCosto());
					System.out.println("COSTO devolucion: " + inf.getCostoDevuelta());
					lisRetorno.add(inf);
				}
				System.out.println("ejecutoo el metodo de reangoooo");
				Map<String, Object> map = new HashMap<>();
				map.put("org", "" + org.getNombre());
				map.put("direccion", "" + org.getDireccion());
				map.put("ruc", "" + org.getRuc());
				map.put("telefono", "" + org.getTelefono());
				map.put("ciudad", "" + org.getCiudad());
				map.put("pais", "" + org.getPais());
				map.put("funcionario", "" + usuario.getFuncionario().getPersona().getNombre() + " "
						+ usuario.getFuncionario().getPersona().getApellido());
				map.put("desde", fecI);
				map.put("hasta", fecF);
				map.put("zona", zon.getDescripcion());
				report = new Reporte();
				report.reportPDFDescarga(lisRetorno, map, "InformeTotalVentaResumidoProductoPorZona", response);
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
