package com.bisontecfacturacion.security.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.MovimientoPorConceptosAuxiliar;
import com.bisontecfacturacion.security.auxiliar.NroDocumento;
import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.config.ExcelGenerator;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.AutoImpresorDetalleVenta;
import com.bisontecfacturacion.security.model.CierreCaja;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Documento;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.LoteBoleta;
import com.bisontecfacturacion.security.model.LoteFactura;
import com.bisontecfacturacion.security.model.LoteTicket;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
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
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.GrupoRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.LoteBoletaRepository;
import com.bisontecfacturacion.security.repository.LoteFacturaRepository;
import com.bisontecfacturacion.security.repository.LoteTicketRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
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
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Transactional
@RestController
@RequestMapping("venta")
public class VentaController {
	private static Formatter ft;
	private Reporte report;

	@Autowired
	private AnulacionesVentaRepository anulacionVentaRepository;

	@Autowired
	private VentaRepository entityRepository;

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
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private LoteBoletaRepository loteBoletaRepository;

	@Autowired
	private LoteTicketRepository loteTicketRepository;

	@Autowired
	private ImpresoraRepository impresoraRepository;

	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository
	
	;

	

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
	private CierreCajaRepository cierreCajaRepository;

	@Autowired
	private TesoreriaRepository tesoreriaRepository;
	@Autowired
	private OrdenPagareRepository ordenPagareRepository;
	

	@Autowired
	private AutoImpresorRepository autoImpresorRepository;
	@Autowired

	private AutoImpresorDetalleVentaRepository autoImpresorDetalleVentaRepository;


	@Autowired
	private ClienteRepository clienteRepository; 

	private List<Object[]> lis;

	@RequestMapping(method=RequestMethod.GET, value="/ventas")
	public List<Venta> get(){
		return entityRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/entrega/{idVenta}")
	public Double getEntregaVenta(@PathVariable int idVenta){
		return operacionRepository.getMontoEntregaPorVenta(idVenta);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{filtro}")
	public List<Venta> getAllsFiltro(@PathVariable String filtro){
		
		List<Venta> objeto=entityRepository.getVentaAllFiltroCliente("%"+Utilidades.eliminaCaracterIzqDer(filtro.toUpperCase())+"%");
		List<Venta> venta=new ArrayList<>();
		for(Venta ob:objeto){
			Venta ventas=new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre()+" "+ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre()+" "+ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			venta.add(ventas);
			
		}
		return venta;
	}


	@RequestMapping(method=RequestMethod.GET, value="/totalventa/{fecha}")
	public Object[] getTotalVenta(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		return entityRepository.findByTotalVenta(ano, mes, dia);
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<Venta> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Venta> objeto=entityRepository.getVenta(ano, mes, dia);
		List<Venta> venta=new ArrayList<>();
		for(Venta ob:objeto){
			Venta ventas=new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre()+" "+ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre()+" "+ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			venta.add(ventas);
		}
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}/{limite}")
	public List<Venta> getAllsLimite(@PathVariable String fecha, @PathVariable int  limite){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Venta> objeto=entityRepository.getVentaLimitessssss(ano, mes, dia, limite);
		List<Venta> venta=new ArrayList<>();
		for(Venta ob:objeto){
			Venta ventas=new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getFuncionarioV().getPersona().setNombre(ob.getFuncionarioV().getPersona().getNombre()+" "+ob.getFuncionarioV().getPersona().getApellido());
			ventas.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre()+" "+ob.getFuncionarioR().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			if (ob.getTipo().equals("1") || ob.getTipo().toLowerCase().equals("contado")) {
				ventas.setTipo("1");
			} else if(ob.getTipo().equals("2") || ob.getTipo().toLowerCase().equals("credito")) {
				ventas.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			}			
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			ventas.setEstado(ob.getEstado());
			ventas.setObs(ob.getObs());
			venta.add(ventas);
		}
		return venta;
	}
	/*@RequestMapping(method=RequestMethod.GET, value="/detalle/{fecha}/{desc}")
	public List<Venta> getAllsDescricpion(@PathVariable String fecha, @PathVariable String desc){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Venta> objeto=entityRepository.getVentaDescripcion(ano, mes, dia,  "%"+Utilidades.eliminaCaracterIzqDer(desc)+"%");
		List<Venta> venta=new ArrayList<>();
		for(Venta ob:objeto){
			Venta ventas=new Venta();
			ventas.setId(ob.getId());
			ventas.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
			ventas.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ ob.getCliente().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setEstado(ob.getEstado());
			ventas.setTipo(ob.getTipo());
			ventas.setHora(ob.getHora());
			ventas.getDocumento().setId(ob.getDocumento().getId());
			ventas.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			ventas.setNroDocumento(ob.getNroDocumento());
			venta.add(ventas);
		}
		return venta;
	}
	 */
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	@RequestMapping(method=RequestMethod.GET, value="/utilidad")
	public List<Object[]> getUtilidad(){
		return entityRepository.findByUtilidad();
	}

	@RequestMapping(method=RequestMethod.GET, value="/totalventaxmes")
	public List<Object[]> getVentaTotalxmes(){
		Date fecha=new Date();
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		String fec=formater.format(fecha);
		String[] fechas=fec.split("-");
		Integer ano=Integer.parseInt(fechas[0]);
		return entityRepository.findByTotalVentaXmes(ano);
	}
	@RequestMapping(method=RequestMethod.GET, value="/ventaIdFacturado/{id}")
	public Venta getVentaIdFacturado(@PathVariable int id){

		Venta v=entityRepository.getVentaIdFacturado(id);
		Venta venta=null;
		if(v != null) {
			venta=new Venta();
			venta.setEstado(v.getEstado());
			venta.setId(v.getId());
			if (v.getTipo().equals("1") || v.getTipo().toLowerCase().equals("contado")) {
				venta.setTipo("1");
				System.out.println("entro verificacion de cuenta contado");
			} else if(v.getTipo().equals("2") || v.getTipo().toLowerCase().equals("credito")) {
				venta.setTipo("2");
				System.out.println("entro verificacion de cuenta credito");
			}			venta.setNroDocumento(v.getNroDocumento());
			venta.setTotal(v.getTotal());
			venta.getFuncionario().setId(v.getFuncionario().getId());
			venta.getCliente().setId(v.getCliente().getId());
			venta.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre()+ " " +v.getCliente().getPersona().getApellido());
			venta.getCliente().getPersona().setCedula(v.getCliente().getPersona().getCedula());
			venta.getDocumento().setId(v.getDocumento().getId());
			venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
			venta.getFuncionarioV().setId(v.getFuncionarioV().getId());

			venta.getFuncionarioR().setId(v.getFuncionarioR().getId());

			venta.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre() +" "+ v.getFuncionario().getPersona().getApellido());
			venta.getFuncionarioV().getPersona().setNombre(v.getFuncionarioV().getPersona().getNombre() +" "+ v.getFuncionarioV().getPersona().getApellido());
			venta.getFuncionarioR().getPersona().setNombre(v.getFuncionarioR().getPersona().getNombre() +" "+ v.getFuncionarioR().getPersona().getApellido());

			venta.setOperacionCaja(v.getOperacionCaja());
			venta.setFechaFactura(v.getFechaFactura());
			System.out.println("vvvvv: "+v.getFecha()+ "  +  :"+v.getFechaFactura());
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
		}else {
			venta = null;
		}

		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/ventaId/{id}")
	public Venta getVentaId(@PathVariable int id){

		Venta v=entityRepository.findById(id).orElse(null);

		Venta venta=new Venta();
		venta.setEstado(v.getEstado());
		venta.setId(v.getId());		
		if (v.getTipo().equals("1") || v.getTipo().toLowerCase().equals("contado")) {
			venta.setTipo("1");
			System.out.println("entro verificacion de cuenta contado");
		} else if(v.getTipo().equals("2") || v.getTipo().toLowerCase().equals("credito")) {
			venta.setTipo("2");
			System.out.println("entro verificacion de cuenta credito");
		}
		venta.setNroDocumento(v.getNroDocumento());
		venta.setTotal(v.getTotal());
		venta.getFuncionario().setId(v.getFuncionario().getId());
		venta.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre() +" "+ v.getFuncionario().getPersona().getApellido());
		venta.getCliente().setId(v.getCliente().getId());
		venta.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre()+ " " +v.getCliente().getPersona().getApellido());
		venta.getCliente().getPersona().setCedula(v.getCliente().getPersona().getCedula());
		venta.getDocumento().setId(v.getDocumento().getId());
		venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
		venta.getFuncionarioV().setId(v.getFuncionarioV().getId());
		venta.getFuncionarioV().getPersona().setNombre(v.getFuncionarioV().getPersona().getNombre() +" "+ v.getFuncionarioV().getPersona().getApellido());
		venta.getFuncionarioR().setId(v.getFuncionarioR().getId());
		venta.getFuncionarioR().getPersona().setNombre(v.getFuncionarioR().getPersona().getNombre() +" "+ v.getFuncionarioR().getPersona().getApellido());
		venta.setOperacionCaja(v.getOperacionCaja());
		venta.setFechaFactura(v.getFechaFactura());
		System.out.println("vvvvv: "+v.getFecha()+ "  +  :"+v.getFechaFactura());
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
		return venta;
	}



	private static String padF(int numero, int size) {
		ft = new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}
	private static String padFAutoFactura(int numer, int size) {
		ft = new Formatter();
		numer = numer ;
		ft.format("%0"+size+"d", numer);
		return ft.toString();
	}

	public List<NroDocumento> getNroLoteDocumento(){
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

				String [] part= loteFactura.getSerieActual().split("-");
				String cod= part[2];
				String codActual=part[0]+"-"+part[1]+"-" + padF(Integer.parseInt(cod),7);
				n.setNro(codActual);
			}
			if (i == tipo1) {
				n.setDescripcion("2");
				n.setNro(padF(Integer.parseInt(loteBoleta.getNumeroActual()),12));
			}
			if (i == tipo2) {
				n.setDescripcion("3");
				n.setNro(padF(Integer.parseInt(loteTicket.getNumeroActual()),12));
			}

			lista.add(n);
		}

		return lista;
	}


	private void actualizarNumeracionAutoImpresor(int numeroTerminal, int idVenta) {



		/*
		NroDocumento n = new NroDocumento();
		if (i == tipo0) {
			n.setDescripcion("1");

			String [] part= loteFactura.getSerieActual().split("-");
			String cod= part[2];
			String codActual=part[0]+"-"+part[1]+"-" + padF(Integer.parseInt(cod),7);
			n.setNro(codActual);
		}

		 */
	}
	private void actualizarLoteDocumentos(int idDocumento, int numeroterminal, int idVenta){
		String idDoc = idDocumento+"";
		for(NroDocumento nro: getNroLoteDocumento()) {
			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("1")) {
				TerminalConfigImpresora c = terminalRepository.consultarTerminal(numeroterminal);
				if(c.getImpresora().equals("ticket") & c.getEstadoAutoImpresor()==true) {
					AutoImpresor aact = new AutoImpresor();
					aact = autoImpresorRepository.consultarAutoImpresorTerminal(numeroterminal);
					aact.setNumeroActual(aact.getNumeroActual()+1);
					autoImpresorRepository.save(aact);
				}else {
					loteFacturaRepository.actualizarSeriaActual(nro.getNro(), 1);
				}


			}

			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("2")) {
				loteBoletaRepository.actualizarNumeroActual(nro.getNro(), 1);
			}

			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("3")) {
				loteTicketRepository.actualizarNumeroActual(nro.getNro(), 1);
			}

		}
	}

	private String getNroDocumento(int idDocumento, int numeroterminal, int idVenta){
		String idDoc = idDocumento+"";
		String nro = "";
		System.out.println("NUMERO DE TERMINAL = "+numeroterminal+ " ID VENTA= "+idVenta);
		for(NroDocumento nro1: getNroLoteDocumento()) {
			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("1")) {
				System.out.println("NUMERO TERMINAL: "+numeroterminal);
				TerminalConfigImpresora c = terminalRepository.consultarTerminal(numeroterminal);
				if(c.getImpresora().equals("ticket") & c.getEstadoAutoImpresor()==true) {
					AutoImpresor aact = new AutoImpresor();
					aact= autoImpresorRepository.consultarAutoImpresorTerminal(numeroterminal);
					if(aact==null) {System.out.println("si es null");}else {System.out.println("no es null");}
					System.out.println(aact.getId()+" id autoimpresor");
					AutoImpresorDetalleVenta detAutoImpresro= new AutoImpresorDetalleVenta();
					detAutoImpresro.getAutoImpresor().setId(aact.getId());
					detAutoImpresro.getVenta().setId(idVenta);
					detAutoImpresro.setFecha(LocalDateTime.now());
					String codigoEstablecimiento= aact.getCodigoEstablecimiento();
					String puntoExpedicion= aact.getPuntoExpedicion();
					String codActual=codigoEstablecimiento+"-"+puntoExpedicion+"-" + padFAutoFactura(aact.getNumeroActual()+1,7);
					detAutoImpresro.setNumeroFactura(codActual);
					autoImpresorDetalleVentaRepository.save(detAutoImpresro);
					aact.setNumeroActual(aact.getNumeroActual()+1);
					autoImpresorRepository.save(aact);
					nro = codActual;
				}else {
					nro = nro1.getNro();	
				}

			}

			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("2")) {
				nro = nro1.getNro();
				loteBoletaRepository.actualizarNumeroActual(nro1.getNro(), 1);
			}

			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("3")) {
				nro = nro1.getNro();
				loteTicketRepository.actualizarNumeroActual(nro1.getNro(), 1);

			}
		}
		return nro;
	}

	private boolean estadoClienteBloqueo(int id, double total) {
		lis = cuentaCobrarRepository.getCLienteCuentaACobrarPorIdCliente(id);

		if (lis == null) {
			for(Object[] ob: lis) {
				if (ob != null) {		
					Double totalMonto = Double.parseDouble(ob[0].toString()) + total;
					if (totalMonto > Double.parseDouble(ob[1].toString()) && Boolean.parseBoolean(ob[2].toString()) == true) {
						return true;
					}
				}
			}
		} else {
			Cliente cliente = clienteRepository.findById(id).get();
			if (cliente !=null) {
				if (total > cliente.getLimiteCredito() && cliente.isEstadoBloqueo() == true) {
					return true;
				}
			}
		}

		return false;
	}

	@Transactional
	@RequestMapping(method=RequestMethod.POST, value = "/{numeroTerminal}")
	public ResponseEntity<?>  guardar(@RequestBody Venta entity, @PathVariable int numeroTerminal ){
		String numeroFacturaRetorno="";

		AutoImpresor autoImpresor = new AutoImpresor();
		autoImpresor = autoImpresorRepository.consultarAutoImpresorTerminal(numeroTerminal);
		TerminalConfigImpresora terminal = terminalRepository.consultarTerminal(numeroTerminal);
		System.out.println(terminal.getImpresora().equals("ticket"));
		System.out.println(terminal.getEstadoAutoImpresor());
		System.out.println(entity.getDocumento().getId());
		try {
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getFuncionarioV().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO VENDEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getFuncionarioR().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REPARTIDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getCliente().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDetalleProducto().size() == 0 && entity.getDetalleServicio().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getTotal() <=0 || entity.getTotal()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA VENTA DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			} else if(entity.getTotalLetra().equals("") || entity.getTotalLetra()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL MONTO EN LETRA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(estadoClienteBloqueo(entity.getCliente().getId(), entity.getTotal())==true && entity.getTipo().equals("2")) {
				return new ResponseEntity<>(new CustomerErrorType("NO SE PUEDE FACTURAR VENTAS, CLIENTE BLOQUEADO POR EXCEDER LINEA DE CREDITO!!"), HttpStatus.CONFLICT);
			} else if(entity.getTipo().equals("1") || entity.getTipo().equals("Contado")) {
				entity.setTipo("1");
				System.out.println("entro validacion tipo venta ct o 1");
			}else if(entity.getTipo().equals("2") || entity.getTipo().equals("Credito")) {
				entity.setTipo("2");
				System.out.println("entro validacion tipo venta cr o 2");

			}else if(terminal.getImpresora().equals("ticket") & terminal.getEstadoAutoImpresor()==true & entity.getDocumento().getId()==1){
				if(autoImpresor.getNumeroActual()>=autoImpresor.getRangoFin()) {
					autoImpresor= null;
					return new ResponseEntity<>(new CustomerErrorType("CANTIDAD DE EXPEDICIÓN SOBREPASADA DEL AUTO IMPRESOR PARA ESTA TERMINAL.!"), HttpStatus.CONFLICT);

				}
			} else if(entity.getObs() != null){
				entity.setObs(entity.getObs().toUpperCase());
			}

			{
				for(int ind=0; ind < entity.getDetalleProducto().size(); ind++) {
					DetalleProducto pro = entity.getDetalleProducto().get(ind);
					if(pro.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE DEVOLUCION ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}
				for(int ind=0; ind < entity.getDetalleServicio().size(); ind++) {
					DetalleServicios ser=entity.getDetalleServicio().get(ind);
					if(ser.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE SERVICIO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE SERVICIO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE SERVICIO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getFuncionario().getId() == 0){
						return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO DEL DETALLE SERVICIO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}


				if(entity.getId() !=0) {
					entity.setFecha(new Date());
					entity.setHora(hora());
					if(entity.getEstado().equals("FACTURADO")) {
						entity.setFechaFactura(new Date());
						entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(), numeroTerminal, entity.getId()));
						actualizarLoteDocumentos(entity.getDocumento().getId(), numeroTerminal, entity.getId());
						numeroFacturaRetorno = entity.getNroDocumento();


					}else if(entity.getEstado().equals("FACTURAR")) {
						entity.setNroDocumento("");
						entity.setFechaFactura(null);
					}
					//entityRepository.save(entity);
					int idVent=entity.getId();
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleProducto().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {

							for(DetalleProducto detalleProducto: entity.getDetalleProducto()) {
								totalDescuento = totalDescuento + detalleProducto.getDescuento();
								detalleProducto.getVenta().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));

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
								detalleProducto.setCosto(detalleProducto.getCosto()*detalleProducto.getCantidad());
								detalleProductoRepository.save(detalleProducto);

								this.actualizarProductoBase(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getSubTotal(), detalleProducto.getPrecio(), entity.getFuncionario().getId(), entity.getTipo(), idVent);

							}

						}

						if (entity.getEstado().equals("FACTURAR")) {
							for(DetalleProducto detalleProducto: entity.getDetalleProducto()) {
								detalleProducto.getVenta().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleProductoRepository.save(detalleProducto);
							}
						}

					}
					if(entity.getDetalleServicio().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							for(DetalleServicios detalleServicio: entity.getDetalleServicio()) {
								detalleServicio.getVenta().setId(idVent);
								if(detalleServicio.getIva().equals("10 %")) {
									total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal()); 
									detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
								}
								if(detalleServicio.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
									detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
								}
								if(detalleServicio.getIva().equals("Exenta")) {
									detalleServicio.setMontoIva(0.0);
								}
								detalleServicioRepository.save(detalleServicio);
							}
						}
						if (entity.getEstado().equals("FACTURAR")) {
							for(DetalleServicios detalleServicio: entity.getDetalleServicio()) {
								detalleServicio.getVenta().setId(idVent);
								if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
								//detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(), detalleServicio.getPrecio()));
								detalleServicioRepository.save(detalleServicio);
							}
						}
					}
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalIva(total10+total5);
					entity.setTotalDescuento(totalDescuento);

					entityRepository.save(entity);

					pdfPrintss(idVent, numeroTerminal, entity.getDocumento().getDescripcion(), entity.getDocumento().getId());

				} else {
					entity.setFecha(new Date());
					entity.setHora(hora());
					if(entity.getEstado().equals("FACTURADO")) {
						entity.setFechaFactura(new Date());
						//entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(), numeroTerminal, idv));
						//actualizarLoteDocumentos(entity.getDocumento().getId());
					}else if(entity.getEstado().equals("FACTURAR")) {
						entity.setNroDocumento("");
						entity.setFechaFactura(null);

					}
					entityRepository.save(entity);
					Venta id = entityRepository.getUltimaVenta();
					if(entity.getEstado().equals("FACTURADO")) {
						entity.setFechaFactura(new Date());
						System.out.println("ejecuto id=0");
						entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId(), numeroTerminal, id.getId()));
						numeroFacturaRetorno = entity.getNroDocumento();
						//actualizarLoteDocumentos(entity.getDocumento().getId(), numeroTerminal, id.getId());
					}
					System.out.println(id.getFecha());
					System.out.println(id.getFechaFactura()+"  ******");
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getDetalleProducto().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {

							for(DetalleProducto detalleProducto: entity.getDetalleProducto()) {
								System.out.println("ctr * "+detalleProducto.getId());
								if (detalleProducto.getId() == 0) {
							        // Es un nuevo detalle, no asignamos el ID
							        detalleProducto.setId(0); // Permitir que JPA lo maneje
							    } else {
							        // Si el detalleProducto ya tiene un ID, no lo alteramos
							        // No necesitas hacer nada aquí
							    }
								totalDescuento = totalDescuento + detalleProducto.getDescuento();
								detalleProducto.getVenta().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
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
								detalleProducto.setCosto(detalleProducto.getCosto()*detalleProducto.getCantidad());
								detalleProductoRepository.save(detalleProducto);
								//								subtotal, precio, idFuncionario, tipo, idVenta
								this.actualizarProductoBase(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getSubTotal(), detalleProducto.getPrecio(), id.getFuncionario().getId(), entity.getTipo(), idVent);
							}




						}
						if (entity.getEstado().equals("FACTURAR")) {
							for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
								if (detalleProducto.getId() == 0) {
							        // Es un nuevo detalle, no asignamos el ID
							        detalleProducto.setId(0); // Permitir que JPA lo maneje
							    } else {
							        // Si el detalleProducto ya tiene un ID, no lo alteramos
							        // No necesitas hacer nada aquí
							    }
								detalleProducto.getVenta().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleProductoRepository.save(detalleProducto);
							}
						}
					}

					if(entity.getDetalleServicio().size()>0){
						if (entity.getEstado().equals("FACTURADO")) {
							for(DetalleServicios detalleServicio: entity.getDetalleServicio()) {
								detalleServicio.getVenta().setId(idVent);
								detalleServicio.setId(0);
								if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
								if(detalleServicio.getIva().equals("10 %")) {

									total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal()); 
									detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
								}
								if(detalleServicio.getIva().equals("5 %")) {
									total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
									detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
								}
								if(detalleServicio.getIva().equals("Exenta")) {
									detalleServicio.setMontoIva(0.0);
								}
								detalleServicioRepository.save(detalleServicio);
							}
						}
						if (entity.getEstado().equals("FACTURAR")) {
							for(DetalleServicios detalleServicio: entity.getDetalleServicio()) {
								detalleServicio.setId(0);
								detalleServicio.getVenta().setId(idVent);
								if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
								//detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(), detalleServicio.getPrecio()));
								detalleServicioRepository.save(detalleServicio);
							}
						}

					}
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalIva(total10+total5);
					entity.setTotalDescuento(totalDescuento);
					System.out.println(id.getFechaFactura());

					entityRepository.save(entity);
					System.out.println("entro udpate nuevo");
					pdfPrintss(idVent, numeroTerminal, entity.getDocumento().getDescripcion(), entity.getDocumento().getId());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println(entity.getTotalIvaDies() + " "+ entity.getTotalIvaCinco());
		Map<String, String> map = new HashMap<>();
		map.put("nroDocumento", numeroFacturaRetorno);
		if(autoImpresor !=null) {
			map.put("timbrado", autoImpresor.getTimbrado());
		}
		map.put("fechaFactura", entity.getFechaFactura()+"");
		map.put("totalIvaCinco", entity.getTotalIvaCinco()+"");
		map.put("totalIvaDies", entity.getTotalIvaDies()+"");
		map.put("totalIva", entity.getTotalIva()+"");

		return new ResponseEntity<Map<String, String>>(map,HttpStatus.OK);

	}

	public String validarPrecio(int id, double precio) {
		Producto pro=productoRepository.findById(id).get();
		String op="P1";
		if (pro.getPrecioVenta_4() == precio) {
			op="P4";
		} 
		if (pro.getPrecioVenta_3() == precio) {
			op= "P3";
		}
		if (pro.getPrecioVenta_2() == precio) {
			op= "P2";
		}
		if (pro.getPrecioVenta_1() == precio) {
			op= "P1";
		}
		return op;
	}






	public void actualizarProductoBase(int id , double cantidad, double subtotal, double precio, int idFuncionario, String tipo, int idVenta) {
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
			if(tipo.equals("1")){
				c= conceptoRepository.findById(1).get();	
			}else {
				c= conceptoRepository.findById(2).get();
			}

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
				if(tipo.equals("1")){
					cc= conceptoRepository.findById(1).get();	
				}else {
					cc= conceptoRepository.findById(2).get();
				}
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
				}else {
					c= conceptoRepository.findById(2).get();
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
					if(tipo.equals("1")){
						con= conceptoRepository.findById(1).get();	
					}else {
						con= conceptoRepository.findById(2).get();
					}

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
				if(tipo.equals("1")){
					c= conceptoRepository.findById(1).get();	
				}else {
					c= conceptoRepository.findById(2).get();
				}

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
			ccc= conceptoRepository.findById(7).get();
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
				con= conceptoRepository.findById(7).get();
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
				conce= conceptoRepository.findById(7).get();
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
					ccc= conceptoRepository.findById(7).get();
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
				con= conceptoRepository.findById(7).get();
				movv.setReferencia(con.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(movv);
			}

		}
	}

	public List<DetalleServicios> detalleServicio(int idVenta) {
		List<Object[]> objeto=detalleServicioRepository.lista(idVenta);
		List<DetalleServicios> detalleServicio=new ArrayList<>();
		for(Object[] ob:objeto){
			DetalleServicios detalleServicios=new DetalleServicios();
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

	public List<DetalleProducto> detalleProducto(List<Object[]> objeto) {
		List<DetalleProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetalleProducto detalleProductos=new DetalleProducto();
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
			if (ob[14].toString()==null) { detalleProductos.getProducto().setExistencia(0.0); } else { detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[14].toString())); }
			detalleProductos.setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			detalleProductos.getProducto().setCodbar(ob[16].toString());
			detalleProductos.getProducto().getMarca().setDescripcion(ob[17].toString());
			detalleProductos.setMontoIva(Double.parseDouble(ob[18].toString()));
			System.out.println("IVA DETALLE: "+detalleProductos.getIva());
			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}

	public Venta ventass(int idVenta) {
		Venta cv = null;

		cv=entityRepository.findById(idVenta).orElse(null);
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

	public List<Venta> getLista(int idVenta ) {

		List<Venta> lista = new ArrayList<>();

		Venta xxx = new Venta();
		List<DetalleProducto> detProducto = new ArrayList<>();
		List<DetalleServicios> detServicio = new ArrayList<>();


		xxx = ventass(idVenta);
		detProducto = detalleProducto(detalleProductoRepository.lista(idVenta));
		detServicio = detalleServicio(idVenta);


		for (int i = 0; i < 1; i++) {
			Cliente cli = clienteRepository.getIdCliente(xxx.getCliente().getId());
			Funcionario FunV = funcionarioRepository.getIdFuncionario(xxx.getFuncionarioV().getId());
			Funcionario FunR = funcionarioRepository.getIdFuncionario(xxx.getFuncionarioR().getId());

			Venta v = new Venta();
			v.getCliente().getPersona().setNombre(cli.getPersona().getNombre()+ " "+cli.getPersona().getApellido());
			v.getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getCliente().getPersona().setTelefono(cli.getPersona().getTelefono());
			v.getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			v.setFechaFactura(xxx.getFechaFactura());
			v.setFecha(xxx.getFecha());
			v.setHora(xxx.getHora());
			v.setObs(xxx.getObs());
			v.setId(xxx.getId());
			v.getDocumento().setId(xxx.getDocumento().getId());
			
			System.out.println("fun veeveveve : "+FunV.getPersona().getNombre());
			v.getFuncionarioV().getPersona().setNombre(FunV.getPersona().getNombre()+ " "+FunV.getPersona().getApellido());
			v.getFuncionarioV().getPersona().setTelefono(FunV.getPersona().getTelefono());
			v.getFuncionarioR().getPersona().setNombre(FunR.getPersona().getNombre()+ " "+FunR.getPersona().getApellido());
			v.getFuncionarioR().getPersona().setTelefono(FunR.getPersona().getTelefono());

			v.setTotalDescuento(xxx.getTotalDescuento());
			v.setTotalIvaCinco(xxx.getTotalIvaCinco());
			v.setTotalIvaDies(xxx.getTotalIvaDies());
			v.setTotal(xxx.getTotal());
			v.setTotalLetra(xxx.getTotalLetra());
			v.setTipo(xxx.getTipo());
			v.setNroDocumento(xxx.getNroDocumento());
			v.getDocumento().setDescripcion(xxx.getDocumento().getDescripcion());
			v.setEntrega(xxx.getEntrega());
			if (xxx.getTipo().equals("1")) {
				v.setTipo("CONTADO");				
			}
			if (xxx.getTipo().equals("2")) {
				v.setTipo("CREDITO");				
			}

			v.setDetalleProducto(detProducto);
			for(DetalleServicios det: detServicio) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.getProducto().setId(det.getId());
				detalleProducto.setDescripcion("SRV.: "+det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				v.getDetalleProducto().add(detalleProducto);
				System.out.println(det.getIva()+" *8*8*8*8*");

			}
			lista.add(v);
			System.out.println("lista cantidad : "+lista.get(0).getDetalleProducto().size());
		}

		return lista;

	}


	public void pdfPrintss(int idVenta, int numeroTerminal, String siImpresion, int tipoDocumento) {
		if (siImpresion.equals("true")) {

			Reporte report = new Reporte();
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminal(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				List<Venta> venta = getLista(idVenta);
				ReporteConfig reportConfig = new ReporteConfig();
				System.out.println("doc:  "+venta.get(0).getDocumento().getId());
				if(venta.get(0).getDocumento().getId()==1) {reportConfig = reporteConfigRepository.getOne(5);}
				if(venta.get(0).getDocumento().getId()==2) {reportConfig = reporteConfigRepository.getOne(1);}
				if(venta.get(0).getDocumento().getId()==3) {reportConfig = reporteConfigRepository.getOne(1);}
				Map<String, Object> map = new HashMap<>();
				report=new Reporte();
				int pageSize = 10;
				int totalPages = (int) Math.ceil((double) venta.get(0).getDetalleProducto().size() / pageSize);
				System.out.println("TOTAL DE PAGINAS:"+ totalPages);
				
				List<Venta> listaVentaImpresion= new ArrayList<Venta>();
				
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

				    ventaImpresion.setDetalleProducto(detallesPagina);
				    listaVentaImpresion.add(ventaImpresion);
				    System.out.println("UNA FILA DE LA PAGINA" +listaVentaImpresion.get(i).getDetalleProducto().get(0).getDescripcion());
				  

				}
				
				if (t.getImpresora().equals("matricial")) {
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
			        		if(p.getDescripcion().equals("PRUEBA-JOB")) {
				        		report.reportPDFImprimirPrueba(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());

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




	@RequestMapping(value="/reImprimirMatricial/{id}/{numeroTerminal}/{fecha}", method=RequestMethod.GET)
	public void reImprimirMatricial(@PathVariable int id, @PathVariable int numeroTerminal, @PathVariable String fecha){
		List<Venta> venta = getLista(id);
		venta.get(0).setFechaFactura(FechaUtil.convertirFechaStringADateUtil(fecha));
		
		Reporte report = new Reporte();
		TerminalConfigImpresora t = new TerminalConfigImpresora();
		t= terminalRepository.consultarTerminal(numeroTerminal);
		if (t==null) {
			System.out.println("Se debe cargar numero terminal dentro de la base de datos");
		}else {
			ReporteConfig reportConfig = new ReporteConfig();
			System.out.println("doc:  "+venta.get(0).getDocumento().getId());
			if(venta.get(0).getDocumento().getId()==1) {reportConfig = reporteConfigRepository.getOne(5);}
			if(venta.get(0).getDocumento().getId()==2) {reportConfig = reporteConfigRepository.getOne(1);}
			if(venta.get(0).getDocumento().getId()==3) {reportConfig = reporteConfigRepository.getOne(1);}
			Map<String, Object> map = new HashMap<>();
			report=new Reporte();
			int pageSize = 10;
			int totalPages = (int) Math.ceil((double) venta.get(0).getDetalleProducto().size() / pageSize);
			System.out.println("TOTAL DE PAGINAS:"+ totalPages);
			
			List<Venta> listaVentaImpresion= new ArrayList<Venta>();
			
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
			    ventaImpresion.setDetalleProducto(detallesPagina);
			    listaVentaImpresion.add(ventaImpresion);
			    System.out.println("UNA FILA DE LA PAGINA" +listaVentaImpresion.get(i).getDetalleProducto().get(0).getDescripcion());
			}
			 if (t.getImpresora().equals("matricial")) {
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
			        		if(p.getDescripcion().equals("PRUEBA-JOB")) {
				        		report.reportPDFImprimirPrueba(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
			        		}
			            }
			            // Pasamos solo los detalles de la página actual a la impresión
			            //report.reportPDFImprimir(listaVentaImpresion, map, reportConfig.getNombreReporte(), t.getNombreImpresora());
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
		}
	}

	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	@RequestMapping(method=RequestMethod.GET, value="/libroIva/{fechaInicio}/{fechaFin}")
	public List<Venta> getLibroIva(@PathVariable String fechaInicio, @PathVariable String fechaFin){
		return listaLibroVenta(fechaInicio, fechaFin);
	}

	public List<Venta> listaLibroVenta(String fechaInicio, String fechaFin) {
		List<Venta> listaRetorno=new ArrayList<>();
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			fecI = formater.parse(fechaInicio);
			Date fecF=formater.parse(fechaFin);
			Date fechaFi = sumarDia(fecF, 24);
			List<Object []> objeto=entityRepository.getLibroVenta(fecI, fechaFi);
			for(Object [] ob: objeto){
				Venta v=new Venta();
				v.setNroDocumento(ob[0].toString());
				v.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
				v.getCliente().getPersona().setNombre(ob[2].toString());
				v.getCliente().getPersona().setCedula(ob[3].toString());
				if (ob[4] == null) {v.setTimbrado("");} else {v.setTimbrado(ob[4].toString());}
				if (ob[5] == null) {v.setFecha(null);} else {v.setTimbradoFin(FechaUtil.convertirFechaStringADateUtil(ob[5].toString()));}
				v.setTotal(Double.parseDouble(ob[6].toString()));
				if (ob[7] == null) {v.setTotalIvaCinco(0.0);} else {v.setTotalIvaCinco(Double.parseDouble(ob[7].toString()));}
				if (ob[8] == null) {v.setTotalIvaDies(0.0);} else {v.setTotalIvaDies(Double.parseDouble(ob[8].toString()));}
				if (ob[9] == null) {v.setTotalExcenta(0.0);} else {v.setTotalExcenta(Double.parseDouble(ob[9].toString()));}

				listaRetorno.add(v);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaRetorno;
	}

	@RequestMapping(value="/libroVentaIvaPDF/{fechaI}/{fechaF}", method=RequestMethod.GET)
	public @ResponseBody void clientePDF(HttpServletResponse response, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException {
		List<Venta> venta=new ArrayList<>();
		venta =listaLibroVenta(fechaI, fechaF);
		Double totalIvaCinco = 0.0, totalIvaDies=0.0, totalExcenta=0.0, totalMonto=0.0; 
		for (Venta v: venta) {
			totalExcenta = totalExcenta + v.getTotalExcenta();
			totalMonto = totalMonto + v.getTotal();
			totalIvaCinco = totalIvaCinco + v.getTotalIvaCinco();
			totalIvaDies = totalIvaDies + v.getTotalIvaDies();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("inicio", ""+fechaI);
		map.put("fin", ""+fechaF);
		map.put("totalIvaCinco", totalIvaCinco);
		map.put("totalIvaDies", totalIvaDies);
		map.put("totalExcenta", totalExcenta);
		map.put("totalMonto", totalMonto);


		report = new Reporte();
		report.reportPDFDescarga(venta, map, "LibroVentaIva", response);
	}

	@RequestMapping(value="/libroVentaIvaXML/{fechaI}/{fechaF}", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> clienteXML(@PathVariable String fechaI, @PathVariable String fechaF) throws IOException {
		List<Venta> ventas = listaLibroVenta(fechaI, fechaF);

		ByteArrayInputStream in = ExcelGenerator.ventaToExcel(ventas);
		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(new InputStreamResource(in));
	}

	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DetalleProducto> detalles){
		try {
			if(detalles.size()!=-1) {
				System.out.println("con listado lista");
				for (DetalleProducto de : detalles) {				
					detalleProductoRepository.deleteById(de.getId());
				}
				System.out.println("sin lista");
				return  new  ResponseEntity<String>(HttpStatus.CREATED);

			}else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@RequestMapping(method=RequestMethod.POST, value="/servicio")
	public ResponseEntity<?> eliminarServicio(@RequestBody List<DetalleServicios> detalles){
		System.out.println("entroo eliminar servicio");
		try {
			System.out.println("entroo eliminar servicio try ");
			for (DetalleServicios de : detalles) {		
				System.out.println("entroo eliminar servicio for");
				detalleServicioRepository.deleteById(de.getId());
			}
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/pruebaHql", method=RequestMethod.GET)
	public List<Venta>  pruebaHql() throws IOException {
		//		List<Venta> lisObj= entityRepository.getPruebaHql(2);
		//		for (int i = 0; i < lisObj.size(); i++) {
		//			//lisObj.get(i).setDetalleProducto(detalleProducto(detalleProductoRepository.lista(lisObj.get(i).getId())));
		//		}
		//System.out.println(lisObj.get(0).getDetalleProducto().size()+"SIZE DETALLE");
		//System.out.println("SIZE: "+lisObj.size());
		return null;
	}

	@RequestMapping(value="/reporteVentaRangoFecha/{fechaI}/{fechaF}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  getReporteVentaRangoFecha(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int detallado) throws IOException {
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Double totalCostoProd=0.0, totalProducto=0.0, totalUtilidadProducto=0.0, totalServicio=0.0, totalVenta=0.0 ;
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		try {
			Calendar cc= Calendar.getInstance();
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
			fecI = formater.parse(fechaI);
			Date fecF=formater.parse(fechaF);
			System.out.println(fecF.getDate());
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("hora final fechas::: "+fecF+ " hora inicio finbal: "+fecI);


			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);
			report = new Reporte();
			if (detallado==1) {
				List<Venta> obb= entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
				System.out.println(obb.size()+" ************lis obb");
				if(obb.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						if(obb.get(i).getTipo().equals("1")) {obb.get(i).setTipo("CONTADO");}
						if(obb.get(i).getTipo().equals("2")) {obb.get(i).setTipo("CREDITO"
								+ "");}
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");;
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);	
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRango", response);
					return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}

			}
			if (detallado==2) {
				System.out.println("ENTROO TRUE");
				List<Venta> obb= entityRepository.getVentaPorRangoFechaHql(fecI, fecF);
				System.out.println(obb.size()+" ************lis obb");
				if(obb.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						if(obb.get(i).getTipo().equals("1")) {obb.get(i).setTipo("CONTADO");}
						if(obb.get(i).getTipo().equals("2")) {obb.get(i).setTipo("CREDITO");}

						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.setCosto(0.0);
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");;
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);	
					listadoDetallado = obb;
					System.out.println("lista size: "+listadoDetallado.size());
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoDetallado", response);
					return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value="/reporteVentaRangoFechaPorFuncionario/{fechaI}/{fechaF}/{idFuncionario}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  getReporteVentaRangoFechaFuncionario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idFuncionario ,  @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: "+fechaI+":::: " + fechaF);
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Funcionario funcionario= funcionarioRepository.getIdFuncionario(idFuncionario);
		Org org = orgRepository.findById(1).get();
		Double totalCostoProd=0.0, totalProducto=0.0, totalUtilidadProducto=0.0, totalServicio=0.0, totalVenta=0.0 ;

		try {
			System.out.println("entroo tryy"+detallado);
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF=formater.parse(fechaF);
			System.out.println("pasoo3");
			//Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: "+fecI+ "  : : : FFIN: "+fecF+ " : id Func:  "+idFuncionario);
			//			objeto = entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);
			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);

			map.put("vendedor", funcionario.getPersona().getNombre()+ " "+ funcionario.getPersona().getApellido());

			System.out.println("Tipo detallado:   "+detallado);
			report = new Reporte();

			if (detallado==1) {
				List<Venta> obb= entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);
				if(listado.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");;
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					//				Double totalCostoProd=0.0, totalProducto=0.0, totalUtilidadProducto=0.0, totalServicio=0.0, totalVenta=0.0 ;

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);					
					System.out.println(obb.size()+" ************lis obb");
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
					return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}

			}
			if (detallado==2) {
				List<Venta> obb= entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);
				if(listado.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}
					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);
					System.out.println(obb.size()+" ************lis obb 22222");
					listadoDetallado = obb;
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoPorFuncionarioDetallado", response);
					return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);

				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}
			}

			//				report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);

	}


	@RequestMapping(value="/reporteVentaRangoFechaPorCliente/{fechaI}/{fechaF}/{idCliente}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  getReporteVentaRangoFechaCliente(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idCliente ,  @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: "+fechaI+":::: " + fechaF);
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Cliente cl= clienteRepository.getIdCliente(idCliente);
		Org org = orgRepository.findById(1).get();
		List<Object[]> listUltimaVentas= new ArrayList<Object[]>();

		Double totalCostoProd=0.0, totalProducto=0.0, totalUtilidadProducto=0.0, totalServicio=0.0, totalVenta=0.0 ;


		try {
			System.out.println("entroo tryy"+detallado);
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF=formater.parse(fechaF);
			System.out.println("pasoo3");
			//Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: "+fecI+ "  : : : FFIN: "+fecF+ " : id Func:  "+idCliente);
			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);

			map.put("cliente", cl.getPersona().getNombre()+ " "+ cl.getPersona().getApellido());

			System.out.println("Tipo detallado:   "+detallado);
			report = new Reporte();

			if (detallado==1) {
				List<Venta> obb= entityRepository.getVentaPorRangoFechaClienteHql(fecI, fecF, idCliente);
				System.out.println(obb.size()+" ************lis obb");
				if(obb.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");;
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);	
					listado = obb;
					report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorCliente", response);
					return  new  ResponseEntity<String>(HttpStatus.OK);
				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}

			}

			if (detallado==2) {
				List<Venta> obb= entityRepository.getVentaPorRangoFechaClienteHql(fecI, fecF, idCliente);
				System.out.println(obb.size()+" ************lis obb");
				if(obb.size()>0) {
					for (int i = 0; i < obb.size(); i++) {
						totalVenta = totalVenta + obb.get(i).getTotal();
						for (int j = 0; j < obb.get(i).getDetalleProducto().size(); j++) {
							totalCostoProd= totalCostoProd + obb.get(i).getDetalleProducto().get(j).getCosto();
							totalProducto= totalProducto + obb.get(i).getDetalleProducto().get(j).getSubTotal();
						}
						for (int j = 0; j < obb.get(i).getDetalleServicio().size(); j++) {
							totalServicio = totalServicio + obb.get(i).getDetalleServicio().get(j).getSubTotal();
							DetalleServicios detAux = obb.get(i).getDetalleServicio().get(j);
							DetalleProducto detalleProducto = new DetalleProducto();
							detalleProducto.setDescripcion("SER - "+detAux.getDescripcion());
							detalleProducto.getProducto().setCodbar(detAux.getServicio().getId()+"");
							detalleProducto.setCantidad(detAux.getCantidad());
							detalleProducto.setPrecio(detAux.getPrecio());
							detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");;
							detalleProducto.setIva(detAux.getIva()+"");
							detalleProducto.setSubTotal(detAux.getSubTotal());
							detalleProducto.setMontoIva(detAux.getMontoIva());
							obb.get(i).getDetalleProducto().add(detalleProducto);
						}
					}

					map.put("totalCostoProducto", totalCostoProd);
					map.put("totalProducto", totalProducto);
					map.put("totalUtilidadProducto", totalProducto-totalCostoProd);
					map.put("totalServicio", totalServicio);
					map.put("totalVenta", totalVenta);	
					listadoDetallado = obb;
					report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoPorClienteDetallado", response);
					return  new  ResponseEntity<String>(HttpStatus.OK);

				}else {
					return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);

	}
	@RequestMapping(value="/reporteVentaRangoFechaExtractoCliente/{fechaI}/{fechaF}/{idCliente}", method=RequestMethod.GET)
	public List<DetalleProducto>  getReporteExtractoCliente(@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idCliente) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: "+fechaI+":::: " + fechaF);
		List<DetalleProducto> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();
		List<Object[]> listUltimaVentas= new ArrayList<Object[]>();
		List<DetalleProducto> listRetornoProducto=new ArrayList<>();
		List<DetalleServicios> listRetornoServicio=new ArrayList<>();
		ReporteConfig reportConfig = reporteConfigRepository.getOne(4);

		Double  obProducto = 0.0;
		Double obServicio= 0.0;


		try {

			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF=formater.parse(fechaF);
			System.out.println("pasoo3");
			//Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("FI: "+fecI+ "  : : : FFIN: "+fecF+ " : id Func:  "+idCliente);
			obProducto = entityRepository.getReporteVentaRangoPorClienteProductoDetalle(fecI, fecF, idCliente);
			System.out.println("ejecutoo el metodo de reangoooo");
			obServicio = entityRepository.getReporteVentaRangoPorClienteServicioDetalle(fecI, fecF, idCliente);

			if(obProducto!=0) {
				//map.put("totalProducto", obProducto);
			}else {//map.put("totalProducto", 0.0);}
			}

			if(obServicio !=0) {
				//map.put("totalServicio",obServicio);
			}else {//map.put("totalServicio", 0.0);
			}
			listUltimaVentas= entityRepository.getReporteVentaRangoPorClienteultimaVenta(idCliente);
			//map.put("ultimaVenta", listUltimaVentas.get(0)[1].toString());

			//map.put("cliente", cl.getPersona().getNombre()+ " "+ cl.getPersona().getApellido());
			//map.put("totalVenta", (obProducto+obServicio));


			report = new Reporte();


			List<Object[]> dpProducto=detalleProductoRepository.listaDetalleProductoAllPorCliente(fecI, fecF, idCliente);
			for(Object[] ob:dpProducto){
				DetalleProducto dp=new DetalleProducto();
				dp.getVenta().setId(Integer.parseInt(ob[0].toString()));
				dp.setDescripcion("PROD: "+ob[1].toString());
				dp.getProducto().setCodbar(ob[2].toString()+"/"+ob[0].toString());
				dp.getProducto().getMarca().setDescripcion(ob[3].toString());
				dp.setCantidad(Double.parseDouble(ob[4].toString()));
				dp.setPrecio(Double.parseDouble(ob[5].toString()));
				dp.setDescuento(Double.parseDouble(ob[6].toString()));
				dp.setSubTotal(Double.parseDouble(ob[7].toString()));
				dp.setIva(ob[8].toString());
				listRetornoProducto.add(dp);
			}

			List<Object[]> dpServicio=detalleProductoRepository.listaDetalleServicioAllPorCliente(fecI, fecF, idCliente);
			for(Object[] ob:dpServicio){
				DetalleServicios dp=new DetalleServicios();
				dp.getServicio().setId(Integer.parseInt(ob[0].toString()));
				dp.setDescripcion(ob[1].toString());
				dp.setCantidad(Double.parseDouble(ob[2].toString()));
				dp.setPrecio(Double.parseDouble(ob[3].toString()));
				dp.setSubTotal(Double.parseDouble(ob[4].toString()));
				listRetornoServicio.add(dp);
			}
			Venta ven = new  Venta();
			System.out.println("ENTROO FALSE");
			ven.getCliente().getPersona().setNombre("");
			ven.getFuncionarioV().getPersona().setNombre("");
			ven.getDocumento().setDescripcion("");
			ven.setNroDocumento("");
			ven.setFechaFactura(FechaUtil.convertirFechaStringADateUtil("2020-11-11"));
			ven.setTotal(1200.0);
			ven.setDetalleProducto(listRetornoProducto);

			for(DetalleServicios det: listRetornoServicio) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.setDescripcion("SER: "+det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				ven.getDetalleProducto().add(detalleProducto);

			}

			listado=ven.getDetalleProducto();



		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("size: "+listado.size());

		return  listado;

	}

	@SuppressWarnings("unused")
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value="/anularFactura/{id}")
	public ResponseEntity<?> anularVentar(@RequestBody OperacionCaja operacionEntidad, @PathVariable int id, OAuth2Authentication authentication){
		try {
			
			Venta v = entityRepository.findById(id).get();
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if(v.getTipo().equals("1")) {
				System.out.println("entro tipo contado");
				OperacionCaja operacion = operacionRepository.findById(v.getOperacionCaja()).get();
				OperacionCaja nuevaOperacion = new OperacionCaja();
				nuevaOperacion.setFecha(new Date());
				nuevaOperacion.setMonto(v.getTotal());
				nuevaOperacion.setTipo("SALIDA"); // Marca que es una salida de dinero (devolución)
				nuevaOperacion.setMotivo("ANULACIÓN VENTA CONTADO REF.: " + v.getId());
				nuevaOperacion.setEfectivo(0.0); // Dependerá del medio de pago original
				nuevaOperacion.setVuelto(0.0);
				nuevaOperacion.getAperturaCaja().setId(operacion.getAperturaCaja().getId());
				nuevaOperacion.getConcepto().setId(7); // "Devolución" u otro concepto relacionado
				nuevaOperacion.getTipoOperacion().setId(1); // Efectivo, Cheque, Tarjeta, etc.
				if (operacion.getTipoOperacion().getId() == 1) {
				    aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(operacion.getAperturaCaja().getId(), v.getTotal());
				}
				if (operacion.getTipoOperacion().getId() == 2) {
				    aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(operacion.getAperturaCaja().getId(), v.getTotal());
				}
				if (operacion.getTipoOperacion().getId() == 3) {
				    aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(operacion.getAperturaCaja().getId(), v.getTotal());
				}
				
				operacionRepository.save(nuevaOperacion);
				AperturaCaja aper = aperturaCajaRepository.findById(operacion.getAperturaCaja().getId()).get();
				if (!aper.isEstado()) {
				    cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(aper.getId(), v.getTotal());
				    int cierreId = cierreCajaRepository.IdCierreCaja(aper.getId());
				    tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getTotal());
				}else {
					System.out.println("no esta cerrado tdoadçvia");
				}
				List<DetalleProducto> detalle= detalleProducto(detalleProductoRepository.lista(id));
				for(DetalleProducto det: detalle) {
					actualizarProductoBaseAumentarCorregido(det.getProducto().getId(), det.getCantidad(), det.getSubTotal(), det.getPrecio(), usuario.getFuncionario().getId(), "", v.getId());
				
				}
				
				AnulacionesVenta vvv = new  AnulacionesVenta();
				vvv.setFecha(new Date());
				vvv.getFuncionario().setId(usuario.getFuncionario().getId());
				vvv.setTotal(v.getTotal());
				vvv.setMotivo("ANULACIÓN VENTA REF.:  "+v.getId());
				vvv.getVenta().setId(v.getId());;
				anulacionVentaRepository.save(vvv);
				entityRepository.findByActualizarFacturas(id, "ANULADO");
			}
			if(v.getTipo().equals("2")) {
				System.out.println("entro tipo credito");
				 if (v.getEntrega() > 0) {
					 AperturaCaja cC = new AperturaCaja();
						cC=aperturaCajaRepository.getAperturaCajaPorIdCaja(operacionEntidad.getAperturaCaja().getId());
						if(cC==null) {
							System.out.println("entrooo null caja chiac");
							return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
						}else {
							if((cC.getSaldoActual()) < operacionEntidad.getMonto() && operacionEntidad.getTipoOperacion().getId()==1) {
								return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if((cC.getSaldoActualCheque()) < operacionEntidad.getMonto()&& operacionEntidad.getTipoOperacion().getId()==2){
								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if((cC.getSaldoActualTarjeta())< operacionEntidad.getMonto() && operacionEntidad.getTipoOperacion().getId()==3){
								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else {
								 	OperacionCaja nuevaOperacion = new OperacionCaja();
							        nuevaOperacion.setFecha(new Date());
							        nuevaOperacion.setMonto(v.getEntrega());
							        nuevaOperacion.setTipo("SALIDA"); // devolución
							        nuevaOperacion.setMotivo("DEVOLUCIÓN DE ENTREGA INICIAL - VENTA CRÉDITO REF.: " + v.getId());
							        nuevaOperacion.setEfectivo(0.0); // opcional según tipo
							        nuevaOperacion.setVuelto(0.0);
							        nuevaOperacion.getAperturaCaja().setId(operacionEntidad.getAperturaCaja().getId());
							        nuevaOperacion.getConcepto().setId(7); // Devolución
							        nuevaOperacion.getTipoOperacion().setId(operacionEntidad.getTipoOperacion().getId());

							        operacionRepository.save(nuevaOperacion);

							        if (operacionEntidad.getTipoOperacion().getId() == 1) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(operacionEntidad.getAperturaCaja().getId(), operacionEntidad.getMonto());
									}
									
									if (operacionEntidad.getTipoOperacion().getId() == 2) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(operacionEntidad.getAperturaCaja().getId(), operacionEntidad.getMonto());
									}
									
									if (operacionEntidad.getTipoOperacion().getId() == 3) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(operacionEntidad.getAperturaCaja().getId(), operacionEntidad.getMonto());
									}
									

							        AperturaCaja aper = aperturaCajaRepository.findById(operacionEntidad.getAperturaCaja().getId()).get();
							        if (!aper.isEstado()) {
							            int cierreId = cierreCajaRepository.IdCierreCaja(operacionEntidad.getAperturaCaja().getId());
							            cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(operacionEntidad.getAperturaCaja().getId(), v.getEntrega());
							            tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getEntrega());
							        }
								
							}
						}
				       
				    }
				 
				 	System.out.println("vino aca");
				    CuentaCobrarCabecera cuenta = cuentaCobrarRepository.getCuentaCabeceraPorVentaId(v.getId());
				 	ordenPagareRepository.eliminarPorCuentaCobrarCabeceraId(cuenta.getId());
				 	
				 
				    // Eliminar cuenta por cobrar asociada
				    if (cuenta != null) {
						 System.out.println("cuenta distinto nulla");

				    	if(cuenta.getSaldo() > 0) {
							 System.out.println("cuenta saldo mayor a cero");

				    		cuentaCobrarDetalleRepository.liquidarDetalle(cuenta.getId(), new Date(), true);
					        cuentaCobrarRepository.liquidarCuentaCabecera(cuenta.getId());
					        List<DetalleProducto> detalle = detalleProducto(detalleProductoRepository.lista(v.getId()));
						    for (DetalleProducto det : detalle) {
						    	System.out.println("entro for update detalle prododsf");
						        actualizarProductoBaseAumentarCorregido(
						            det.getProducto().getId(), det.getCantidad(), det.getSubTotal(),
						            det.getPrecio(), usuario.getFuncionario().getId(), "", v.getId()
						        );
						    }
						    // Registrar anulación
						    AnulacionesVenta anulacion = new AnulacionesVenta();
						    anulacion.setFecha(new Date());
						    anulacion.getFuncionario().setId(usuario.getFuncionario().getId());
						    anulacion.setTotal(v.getTotal());
						    anulacion.setMotivo("ANULACIÓN VENTA CRÉDITO REF.: " + v.getId());
						    anulacion.getVenta().setId(v.getId());
						    anulacionVentaRepository.save(anulacion);
						    // Cambiar estado de la venta
						    entityRepository.findByActualizarFacturas(v.getId(), "ANULADO"); 
				    	}else {
				    		return  new  ResponseEntity<>(new CustomerErrorType("NO SE PUEDE ANULAR VENTA CREDITO QUE YA SE PAGO POR LA TOTALIDAD"), HttpStatus.CONFLICT);
				    	}
				    }else {
			    		return  new  ResponseEntity<>(new CustomerErrorType("NO SE HA ENCONTRADO LA CUENTA DE LA VENTA A CREDITO"), HttpStatus.CONFLICT);

				    }
				    	
			
				  
			}

		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value="/reporteVentaRangoGrupo/{fechaI}/{fechaF}/{idGrupo}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  getReporteVentaRangoGrupo(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int idGrupo ,  @PathVariable int detallado) throws IOException {
		System.out.println("Entro metodo funcionaajnaaaoao::: "+fechaI+":::: " + fechaF+" ::::" + idGrupo+ " :::: "+detallado);
		List<Venta> listado = new ArrayList<>();
		List<DetalleProducto> listadoDetalle = new ArrayList<>();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		try {
			System.out.println("entroo tryy"+detallado);
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI, fecF;
			fecI= FechaUtil.setFechaHoraInicial(fechaI);
			fecF= FechaUtil.setFechaHoraFinal(fechaF);
			Object [][] objeto;
			Grupo g=null;
			if(idGrupo==0) {
				objeto = entityRepository.getReporteVentaRangoGrupoAll(fecI, fecF);
				g= new Grupo();
				g.setDescripcion("TODOS");
			}else {
				//listado = entityRepository.getReporteVentaRangoGrupoHql(fecI, fecF, idGrupo);
				g= grupoService.getOne(idGrupo);
				System.out.println("SIZE LISTADO: "+listado.size());

			}
			/*
			System.out.println("ejecutoo el metodo de reangoooo");
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fecF);
//			map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
//			map.put("totalVenta", Double.parseDouble(objeto[0][1].toString()));
//			map.put("totalUtilidad", Double.parseDouble(objeto[0][2].toString()));
			map.put("grupo", g.getDescripcion());

			System.out.println("Tipo detallado:   "+detallado);
			report = new Reporte();

			if (detallado==1) {
				Venta ven = new  Venta();
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
			if (detallado==2) {

				/*System.out.println("ENTROO TRUE");
				List<Object []> obb=null;
				if(idGrupo==0) {
					 obb= entityRepository.getReporteVentaRangoGrupoDetalladoAll(fecI, fecF);

				}else {
					obb= entityRepository.getReporteVentaRangoGrupoDetallado(fecI, fecF, idGrupo);
				}
				for(Object[] ob: obb) {
					DetalleProducto dt = new DetalleProducto();
					dt.setDescripcion(ob[0].toString());
					dt.setPrecio(Double.parseDouble(ob[1].toString()));
					dt.setCantidad(Double.parseDouble(ob[2].toString()));
					dt.setSubTotal(Double.parseDouble(ob[3].toString()));
					dt.setCosto(Double.parseDouble(ob[4].toString()));
					dt.setTipoPrecio(ob[5].toString());
					listadoDetalle.add(dt);
				}

				report.reportPDFDescarga(listado, map, "ReporteVentaRangoGrupoDetallado", response);
			}
			 */
			//				report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenConcepto(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Venta pre= new Venta(); 
		pre=entityRepository.getOne(id);
		if(pre.getTipo().equals("1")) {pre.setTipo("CONTADO");}
		if(pre.getTipo().equals("2")) {pre.setTipo("CREDITO");}
		List<Venta> listado= new ArrayList<Venta>();
		listado.add(pre);
		List<Presupuesto> listadoRetorno= new ArrayList<Presupuesto>();

		for (int i = 0; i < listado.size(); i++) {

			for (int j = 0; j < listado.get(i).getDetalleServicio().size(); j++) {
				DetalleServicios detAux = listado.get(i).getDetalleServicio().get(j);
				DetalleProducto detAgregar =new DetalleProducto();


				detAgregar.setDescripcion("SER - "+detAux.getDescripcion());
				detAgregar.getProducto().setCodbar(detAux.getServicio().getId()+"");
				detAgregar.setCantidad(detAux.getCantidad());
				detAgregar.setPrecio(detAux.getPrecio());
				detAgregar.getProducto().getUnidadMedida().setDescripcion("UN");;
				detAgregar.setIva(detAux.getIva()+"");
				detAgregar.setSubTotal(detAux.getSubTotal());
				detAgregar.setMontoIva(detAux.getMontoIva());
				listado.get(i).getDetalleProducto().add(detAgregar);
			}
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
			report.reportPDFDescarga(listado, map, "ReporteVentaPdf", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}


	@RequestMapping(value="/resumenDiarioPdf/{fechaI}/{fechaF}", method=RequestMethod.GET)
	public ResponseEntity<?>  getResumenDiario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		List<Object[]> listObjVenCre= new ArrayList<>();
		List<Object[]> listObjVenCon= new ArrayList<>();
		List<Object[]> listObjComCre= new ArrayList<>();
		List<Object[]> listObjComCon= new ArrayList<>();
		List<Object[]> listObjCob=new ArrayList<>();
		List<Object[]> listObjPago=new ArrayList<>();
		List<Object[]> listObjCuentaCobrar=new ArrayList<>();
		List<Object[]> listObjCuentaPagar=new ArrayList<>();
		List<Object[]> listObjEntregaInicialVentaCredito=new ArrayList<>();

		List<MovimientoPorConceptosAuxiliar> listRetorno= new ArrayList<>();

		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("pasoo1");
			fecI = formater.parse(fechaI);
			System.out.println("pasoo2");
			Date fecF=formater.parse(fechaF);
			System.out.println("pasoo3");
			//Date fechaFi = sumarDia(fecF, 24);
			fecF.setHours(23);
			fecI.setHours(0);
			System.out.println("FI: "+fecI+ "  : : : FFIN: "+fecF+ " : id Func:  ");			

			listObjVenCon = entityRepository.getResumenVentaContado(fecI, fecF);
			for (int i = 0; i < listObjVenCon.size(); i++) {
				Object[] ob= listObjVenCon.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				if(ob[1].toString().equals("1")) { v.setDescripcion("VENTA CONTADO");}
				listRetorno.add(v);
			}

			listObjVenCre = entityRepository.getResumenVentaCredito(fecI, fecF);
			for (int i = 0; i < listObjVenCre.size(); i++) {
				Object[] ob= listObjVenCre.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				if(ob[1].toString().equals("2")) { v.setDescripcion("VENTA CREDITO");}
				listRetorno.add(v);
			}
			listObjComCon = entityRepository.getResumenCompraContado(fecI, fecF);
			for (int i = 0; i < listObjComCon.size(); i++) {
				Object[] ob= listObjComCon.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjComCre = entityRepository.getResumenCompraCredito(fecI, fecF);
			for (int i = 0; i < listObjComCre.size(); i++) {
				Object[] ob= listObjComCre.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}



			listObjCob = entityRepository.getResumenCobros(fecI, fecF);
			for (int i = 0; i < listObjCob.size(); i++) {
				Object[] ob= listObjCob.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			listObjPago = entityRepository.getResumenPagos(fecI, fecF);
			for (int i = 0; i < listObjPago.size(); i++) {
				Object[] ob= listObjPago.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}

			listObjCuentaCobrar = entityRepository.getResumenCuentaCobrar(fecI, fecF);
			for (int i = 0; i < listObjCuentaCobrar.size(); i++) {
				Object[] ob= listObjCuentaCobrar.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjCuentaPagar = entityRepository.getResumenCuentaPagar(fecI, fecF);
			for (int i = 0; i < listObjCuentaPagar.size(); i++) {
				Object[] ob= listObjCuentaPagar.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}
			listObjEntregaInicialVentaCredito = entityRepository.getResumenEntregaInicialVentaCredito(fecI, fecF);
			for (int i = 0; i < listObjEntregaInicialVentaCredito.size(); i++) {
				Object[] ob= listObjEntregaInicialVentaCredito.get(i);
				MovimientoPorConceptosAuxiliar v = new MovimientoPorConceptosAuxiliar();
				v.setMonto(Double.parseDouble(ob[0].toString()));
				v.setDescripcion(ob[1].toString());
				listRetorno.add(v);
			}



			for (int i = 0; i < listRetorno.size(); i++) {
				System.out.println(listRetorno.get(i).getDescripcion()+ " = "+listRetorno.get(i).getMonto());
			}
			System.out.println(listRetorno.size()+" *SIXE RETRONO LISTA");



			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("desde", fecI);
			map.put("hasta", fecF);
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());

			report = new Reporte();
			report.reportPDFDescarga(listRetorno, map, "ReporteBalanceGeneral", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

}
