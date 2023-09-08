package com.bisontecfacturacion.security.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.bisontecfacturacion.security.auxiliar.NroDocumento;
import com.bisontecfacturacion.security.config.ExcelGenerator;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CierreCaja;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.LoteBoleta;
import com.bisontecfacturacion.security.model.LoteFactura;
import com.bisontecfacturacion.security.model.LoteTicket;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnulacionesVentaRepository;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
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
import com.bisontecfacturacion.security.repository.OrgRepository;
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
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;

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
	@RequestMapping(method=RequestMethod.GET, value="/ventaId/{id}")
	public Venta getVentaId(@PathVariable int id){

		Venta v=entityRepository.findById(id).orElse(null);

		Venta venta=new Venta();
		venta.setEstado(v.getEstado());
		venta.setId(v.getId());
		venta.setTipo(v.getTipo()); 
		venta.setNroDocumento(v.getNroDocumento());
		venta.setTotal(v.getTotal());
		venta.getFuncionario().setId(v.getFuncionario().getId());
		venta.getCliente().setId(v.getCliente().getId());
		venta.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre());
		venta.getCliente().getPersona().setApellido(v.getCliente().getPersona().getApellido());
		venta.getDocumento().setId(v.getDocumento().getId());
		venta.getDocumento().setDescripcion(v.getDocumento().getDescripcion());
		venta.getFuncionarioV().setId(v.getFuncionarioV().getId());
		venta.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre() +" "+ v.getFuncionario().getPersona().getApellido());
		venta.setOperacionCaja(v.getOperacionCaja());
		venta.setFechaFactura(v.getFechaFactura());
		System.out.println("vvvvv: "+v.getFecha()+ "  +  :"+v.getFechaFactura());
		venta.setFecha(v.getFecha());
		venta.setHora(v.getHora());
		venta.setTotalDescuento(v.getTotalDescuento());
		venta.setTotalIvaDies(v.getTotalIvaDies());
		venta.setTotalIvaCinco(v.getTotalIvaCinco());
		venta.setTotalExcenta(v.getTotalExcenta());
		venta.setTotalLetra(v.getTotalLetra());
		venta.setEntrega(v.getEntrega());
		return venta;
	}

	//	@RequestMapping(method=RequestMethod.POST, value="/facturado")
	//	public void facturarVentas(@RequestBody int id){
	//		entityRepository.findByActualizarFacturas(id);
	//	}
	//	@RequestMapping(method=RequestMethod.POST, value="/facturar")
	//	public void facturarVentas(@RequestBody Venta entity){
	//		if (entity.getTipo().equals("2")) {
	//		}
	//	}

	private static String padF(int numero, int size) {
		ft = new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}

	public List<NroDocumento> getNroLoteDocumento(){
		List<NroDocumento> lista = new ArrayList<>();
		int tipo0 = 0;
		int tipo1 = 1;
		int tipo2 = 2;

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



	private void actualizarLoteDocumentos(int idDocumento){
		String idDoc = idDocumento+"";
		for(NroDocumento nro: getNroLoteDocumento()) {
			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("1")) {
				loteFacturaRepository.actualizarSeriaActual(nro.getNro(), 1);
			}

			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("2")) {
				loteBoletaRepository.actualizarNumeroActual(nro.getNro(), 1);
			}

			if (idDoc.equals(nro.getDescripcion()) && idDoc.equals("3")) {
				loteTicketRepository.actualizarNumeroActual(nro.getNro(), 1);
			}
		}
	}

	private String getNroDocumento(int idDocumento){
		String idDoc = idDocumento+"";
		String nro = "";
		for(NroDocumento nro1: getNroLoteDocumento()) {
			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("1")) {
				nro = nro1.getNro();
			}

			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("2")) {
				nro = nro1.getNro();
			}

			if (idDoc.equals(nro1.getDescripcion()) && idDoc.equals("3")) {
				nro = nro1.getNro();
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
		System.out.println(entity.getFechaFactura());
		try {
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getFuncionarioV().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
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
			}  
			else
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
						entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId()));
						actualizarLoteDocumentos(entity.getDocumento().getId());
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
					entity.setTotalDescuento(totalDescuento);

					entityRepository.save(entity);

					pdfPrintss(idVent, numeroTerminal, entity.getDocumento().getDescripcion());

				} else {
					entity.setFecha(new Date());
					entity.setHora(hora());
					if(entity.getEstado().equals("FACTURADO")) {
						entity.setFechaFactura(new Date());
						entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId()));
						actualizarLoteDocumentos(entity.getDocumento().getId());
					}else if(entity.getEstado().equals("FACTURAR")) {
						entity.setNroDocumento("");
						entity.setFechaFactura(null);

					}
					entityRepository.save(entity);
					Venta id = entityRepository.getUltimaVenta();
					System.out.println(id.getFecha());
					System.out.println(id.getFechaFactura()+"  ******");
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
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
								//								subtotal, precio, idFuncionario, tipo, idVenta
								this.actualizarProductoBase(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getSubTotal(), detalleProducto.getPrecio(), id.getFuncionario().getId(), entity.getTipo(), idVent);
							}




						}
						if (entity.getEstado().equals("FACTURAR")) {
							for (DetalleProducto detalleProducto : entity.getDetalleProducto()) {
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
								detalleServicio.getVenta().setId(idVent);
								if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
								//detalleServicio.setTipoPrecio(validarPrecio(detalleServicio.getProducto().getId(), detalleServicio.getPrecio()));
								detalleServicioRepository.save(detalleServicio);
							}
						}

					}
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalDescuento(totalDescuento);
					System.out.println(id.getFechaFactura());
					id.setFechaFactura(id.getFecha());
					entityRepository.save(entity);
					System.out.println("entro udpate nuevo");
					pdfPrintss(idVent, numeroTerminal, entity.getDocumento().getDescripcion());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Map<String, String> map = new HashMap<>();
		map.put("nroDocumento", getNroDocumento(entity.getDocumento().getId()));
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
			Venta v = new Venta();
			v.getCliente().getPersona().setNombre(cli.getPersona().getNombre()+ " "+cli.getPersona().getApellido());
			v.getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			v.setFechaFactura(xxx.getFechaFactura());
			v.setFecha(xxx.getFecha());
			v.setHora(xxx.getHora());
			System.out.println("fun veeveveve : "+FunV.getPersona().getNombre());
			v.getFuncionarioV().getPersona().setNombre(FunV.getPersona().getNombre()+ " "+FunV.getPersona().getApellido());
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
				detalleProducto.setDescripcion(det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				v.getDetalleProducto().add(detalleProducto);
				
			}
			lista.add(v);
			System.out.println("lista cantidad : "+lista.get(0).getDetalleProducto().size());
		}

		return lista;

	}


	public void pdfPrintss(int idVenta, int numeroTerminal, String siImpresion) {
		if (siImpresion.equals("true")) {

			Reporte report = new Reporte();
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminal(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				ReporteConfig reportConfig = reporteConfigRepository.getOne(1);
				List<Venta> venta = getLista(idVenta);
				Map<String, Object> map = new HashMap<>();
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
					try {
						report.reportPDFImprimir(venta, map, reportConfig.getNombreReporte(), t.getNombreImpresora());	
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
		
			ReporteConfig reportConfig = reporteConfigRepository.getOne(1);
			Map<String, Object> map = new HashMap<>();
			report=new Reporte();
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
				try {
					report.reportPDFImprimir(venta, map, reportConfig.getNombreReporte(), t.getNombreImpresora());	
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



	@RequestMapping(value="/reporteVentaRangoFecha/{fechaI}/{fechaF}/{detallado}", method=RequestMethod.GET)
	public ResponseEntity<?>  getReporteVentaRangoFecha(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int detallado) throws IOException {
		List<Venta> listado = new ArrayList<>();
		List<Venta> listadoDetallado = new ArrayList<>();

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
			//
			//			Date fechaFi = sumarDia(fecF, 24);
			//			System.out.println("fecha I S: "+FechaUtil.convertirFechaUtilATimeZone(fecF)+ " , fecha F S: "+FechaUtil.convertirFechaUtilATimeZone(fechaFi));
			//			System.out.println("fecha I: "+fecF+ " , fecha F: "+fecF);
			Object [][] objeto = new Object[1][3];
			objeto[0][0]=null;
			objeto[0][1]=null;
			objeto[0][2]=null;

			objeto=entityRepository.getReporteVentaRango(fecI, fecF);

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
			if(objeto[0][0]!=null) {
				map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
			}else {map.put("totalCosto", 0.0);
			}
			if(objeto[0][1]!= null){
				map.put("totalVenta", Double.parseDouble(objeto[0][1].toString()));
			}else {	map.put("totalVenta", 0.0);
			}
			if (objeto[0][2]!=null) {
				map.put("totalUtilidad", Double.parseDouble(objeto[0][2].toString()));
			}else {	map.put("totalUtilidad", 0.0);
			}
			System.out.println();
			System.out.println();

			Venta ven = new  Venta();

			report = new Reporte();
			if (detallado==1) {
				System.out.println("ENTROO FALSE");
				ven.getCliente().getPersona().setNombre("");
				ven.getFuncionarioV().getPersona().setNombre("");
				ven.getDocumento().setDescripcion("");
				ven.setNroDocumento("");
				ven.setFechaFactura(FechaUtil.convertirFechaStringADateUtil("2020-11-11"));
				ven.setTotal(1200.0);
				listado.add(ven);
				System.out.println("lista size: "+listado.size());
				report.reportPDFDescarga(listado, map, "ReporteVentaRango", response);

			}
			if (detallado==2) {
				System.out.println("ENTROO TRUE");
				List<Object []> obb= entityRepository.getReporteVentaRangoDetallado(fecI, fecF);
				System.out.println(obb.size()+" ************lis obb");
				for(Object[] ob: obb) {
					Venta venta = new  Venta();
					venta.getCliente().getPersona().setNombre(ob[0].toString());
					venta.getFuncionarioV().getPersona().setNombre(ob[1].toString());
					venta.getDocumento().setDescripcion(ob[2].toString());
					venta.setNroDocumento(ob[3].toString());
					venta.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[4].toString()));
					venta.setTotal(Double.parseDouble(ob[5].toString()));	
					listadoDetallado.add(venta);
				}
				System.out.println("lista size: "+listadoDetallado.size());
				report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoDetallado", response);
			}
			//				report.reportPDFImprimir(listado, map, "ReporteVentaRango", "Microsoft Print to PDF");

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
		Object [][] objeto = new Object[1][3];
		objeto[0][0]=null;
		objeto[0][1]=null;
		objeto[0][2]=null;
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
			objeto = entityRepository.getReporteVentaRangoPorFuncionarios(fecI, fecF, idFuncionario);
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
			if(objeto[0][0]!=null) {
				map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
			}else {map.put("totalCosto", 0.0);
			}
			if(objeto[0][1]!= null){
				map.put("totalVenta", Double.parseDouble(objeto[0][1].toString()));
			}else {	map.put("totalVenta", 0.0);
			}
			if (objeto[0][2]!=null) {
				map.put("totalUtilidad", Double.parseDouble(objeto[0][2].toString()));
			}else {	map.put("totalUtilidad", 0.0);
			}
			map.put("vendedor", funcionario.getPersona().getNombre()+ " "+ funcionario.getPersona().getApellido());

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
				report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);

			}
			if (detallado==2) {
				System.out.println("ENTROO TRUE");
				List<Object []> obb= entityRepository.getReporteVentaRangoPorFuncionariosDetallado(fecI, fecF, idFuncionario);
				for(Object[] ob: obb) {
					Venta venta = new  Venta();
					venta.getCliente().getPersona().setNombre(ob[0].toString());
					venta.getFuncionarioV().getPersona().setNombre(ob[1].toString());
					venta.getDocumento().setDescripcion(ob[2].toString());
					venta.setNroDocumento(ob[3].toString());
					venta.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[4].toString()));
					venta.setTotal(Double.parseDouble(ob[5].toString()));	
					listadoDetallado.add(venta);
				}
				report.reportPDFDescarga(listadoDetallado, map, "ReporteVentaRangoPorFuncionarioDetallado", response);
			}

			//				report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);

	}

	@Transactional
	@RequestMapping(method=RequestMethod.GET, value="/anularFactura/{id}")
	public ResponseEntity<?> anularVentar(@PathVariable int id, OAuth2Authentication authentication){

		try {
			entityRepository.findByActualizarFacturas(id, "ANULADO");
			Venta v = entityRepository.findById(id).get();
			if(v.getTipo().equals("1")) {

			}
			if(v.getTipo().equals("2")) {
				CuentaCobrarCabecera cu= cuentaCobrarRepository.getCuentaCabeceraPorVentaId(v.getId());
				if(cu!=null) {
					cuentaCobrarDetalleRepository.eliminarDetalleCuentaPorCabeceraId(cu.getId());
					cuentaCobrarRepository.deleteById(cu.getId());
					System.out.println("cuenta eliminada:");
				}
			}

			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			AnulacionesVenta vvv = new  AnulacionesVenta();
			vvv.setFecha(new Date());
			vvv.getFuncionario().setId(usuario.getFuncionario().getId());
			vvv.setTotal(v.getTotal());
			vvv.setMotivo("ANULACIÓN VENTA REF.:  "+v.getId());
			vvv.getVenta().setId(v.getId());;
			anulacionVentaRepository.save(vvv);
			OperacionCaja operacion = operacionRepository.findById(v.getOperacionCaja()).get();
			OperacionCaja ope=new OperacionCaja();
			ope.setEfectivo(0.0);
			ope.setFecha(new Date());
			ope.setMonto(v.getTotal());
			Concepto c= new Concepto();
			c = conceptoRepository.getOne(7);
			ope.setMotivo(c.getDescripcion()+" REF.: "+anulacionVentaRepository.getUltimaDevolucion());
			ope.setTipo("SALIDA");
			ope.setVuelto(0.0);	
			ope.getAperturaCaja().setId(operacion.getAperturaCaja().getId());
			ope.getConcepto().setId(7);
			ope.getTipoOperacion().setId(1);
			operacionRepository.save(ope);

			if(operacion.getTipoOperacion().getId()==1) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(operacion.getAperturaCaja().getId(), v.getTotal());}
			if(operacion.getTipoOperacion().getId()==2) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(operacion.getAperturaCaja().getId(), v.getTotal());}
			if(operacion.getTipoOperacion().getId()==3) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(operacion.getAperturaCaja().getId(), v.getTotal());}
			AperturaCaja aper=aperturaCajaRepository.findById(operacion.getAperturaCaja().getId()).get();

			if (aper.isEstado() == false) {

				cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(operacion.getAperturaCaja().getId(), v.getTotal());

				int cierreId = cierreCajaRepository.IdCierreCaja(operacion.getAperturaCaja().getId());

				tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getTotal());

			}
			List<DetalleProducto> detalle= detalleProducto(detalleProductoRepository.lista(id));
			for(DetalleProducto det: detalle) {
				actualizarProductoBaseAumentarCorregido(det.getProducto().getId(), det.getCantidad(), det.getSubTotal(), det.getPrecio(), usuario.getFuncionario().getId(), "", v.getId());
				//				Producto p = productoRepository.getOne(det.getProducto().getId());
				//				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
				//				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
				////				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				//				mov.setDescripcion(p.getDescripcion());
				//				mov.setCantidad(det.getCantidad());
				//				mov.setFecha(new  Date());
				//				mov.setHora(hora());
				//
				//				mov.setIngreso(det.getSubTotal());
				//				mov.setEgreso(0.0);
				//				mov.setVentaSalida(det.getPrecio());
				//
				//				mov.setCostoEntrada(0.0);
				//				mov.setCostoEntradaAnterior(0.0);
				//				mov.setCostoSalida(p.getPrecioCosto());
				//
				//				mov.setVenta_1(p.getPrecioVenta_1());
				//				mov.setVenta_2(p.getPrecioVenta_2());
				//				mov.setVenta_3(p.getPrecioVenta_3());
				//				mov.setVenta_4(p.getPrecioVenta_4());
				//
				//				mov.setVenta_1_anterior(0.0);
				//				mov.setVenta_2_anterior(0.0);
				//				mov.setVenta_3_anterior(0.0);
				//				mov.setVenta_4_anterior(0.0);
				//
				//				mov.getTipoMovimiento().setId(1);
				//				mov.getProducto().setId(p.getId());
				//				mov.getFuncionario().setId(usuario.getFuncionario().getId());
				//				mov.setMarca(p.getMarca().getDescripcion());
				//				Concepto ccc= new Concepto();
				//				ccc= conceptoRepository.findById(7).get();
				//				mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
				//				movEntradaSalidaRepository.save(mov);
				//this.actualizarProductoBaseAumentar(p.getId(), det.getCantidad());
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
				objeto = entityRepository.getReporteVentaRangoGrupo(fecI, fecF, idGrupo);
				g= grupoService.getOne(idGrupo);

			}
			
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
			map.put("totalCosto", Double.parseDouble(objeto[0][0].toString()));
			map.put("totalVenta", Double.parseDouble(objeto[0][1].toString()));
			map.put("totalUtilidad", Double.parseDouble(objeto[0][2].toString()));
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
				System.out.println("ENTROO TRUE");
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
				report.reportPDFDescarga(listadoDetalle, map, "ReporteVentaRangoGrupoDetallado", response);
			}

			//				report.reportPDFDescarga(listado, map, "ReporteVentaRangoPorFuncionario", response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return  new  ResponseEntity<String>(HttpStatus.OK);

	}
}
