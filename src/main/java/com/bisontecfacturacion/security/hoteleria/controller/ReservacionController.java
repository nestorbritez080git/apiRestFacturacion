package com.bisontecfacturacion.security.hoteleria.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.hoteleria.model.Habitaciones;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionDetalle;
import com.bisontecfacturacion.security.hoteleria.repository.HabitacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionDetalleRepository;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.DetalleProductoRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@RestController
@RequestMapping("reservacion")
public class ReservacionController {
	@Autowired
	private ReservacionCabeceraRepository entityRepository;
	
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	@Autowired
	private HabitacionesRepository habitacionesRepository;
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ReporteConfigRepository reporteConfigRepository;
	
	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;
	
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	private Reporte report;
	
	
	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;
	
	@Autowired
	private ProductoCardexRepository compuestoRepository;
	@Autowired
	private ReservacionDetalleRepository detalleRepository;
	
	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;
	@Autowired
	private OperacionCajaRepository operacionRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;
	
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/{fecha}")
	public List<ReservacionCabecera> getAll(@PathVariable String fecha ){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);		
		return listar(entityRepository.getReservacionesAll(ano, mes, dia));
	}
	@RequestMapping(method = RequestMethod.GET, value = "/prereservacion/{fecha}")
	public List<ReservacionCabecera> getAllPreReservado(@PathVariable String fecha ){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);		
		return listar(entityRepository.getReservacionesAllPreReservado(ano, mes, dia));
	}

	@RequestMapping(method=RequestMethod.POST, value="/detalleReservaciones")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<ReservacionDetalle> detalles){
		try {
			if(detalles.size()!=-1) {
				System.out.println("con listado lista");
				for (ReservacionDetalle de : detalles) {				
					detalleRepository.deleteById(de.getId());
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
	
	@RequestMapping(method = RequestMethod.GET, value = "/detalleReservaciones/{id}")
	public List<ReservacionDetalle> getDetalleReservacionPorIdCabecera(@PathVariable int id){
		return listarDetalle(detalleRepository.getDetallePorIdCabecera(id));
	}
	@RequestMapping(method = RequestMethod.GET, value = "/buscarId/{id}")
	public ReservacionCabecera getPorIdCabecera(@PathVariable int id){
		ReservacionCabecera ob = entityRepository.findById(id).orElse(null);
		ReservacionCabecera r=new ReservacionCabecera();
		r.setId(ob.getId());
		r.getFuncionarioRegistro().setId(ob.getFuncionarioRegistro().getId());
		r.getFuncionarioRegistro().getPersona().setNombre(ob.getFuncionarioRegistro().getPersona().getNombre()+ " "+ob.getFuncionarioRegistro().getPersona().getApellido() );
		r.getFuncionarioFinalizacion().setId(ob.getFuncionarioRegistro().getId());
		r.getFuncionarioFinalizacion().getPersona().setNombre(ob.getFuncionarioFinalizacion().getPersona().getNombre()+" "+ ob.getFuncionarioRegistro().getPersona().getApellido());
		r.getCliente().setId(ob.getCliente().getId());
		r.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ob.getCliente().getPersona().getApellido());
		r.getDocumento().setId(ob.getDocumento().getId());
		r.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
		r.setEntrega(ob.getEntrega());
		r.setEstado(ob.getEstado());
		r.setTipo(ob.getTipo());
		r.setTotalHabitacion(ob.getTotalHabitacion());
		r.setTotalProducto(ob.getTotalProducto());
		r.setTotalLetra(ob.getTotalLetra());
		r.setDescripcionCombo(ob.getDescripcionCombo());
		r.setPrecio(ob.getPrecio());
		r.setHabitacionesCategoriaCombo(ob.getHabitacionesCategoriaCombo());
		r.setOperacionCajaEntrega(ob.getOperacionCajaEntrega());
		r.setOperacionCaja(ob.getOperacionCaja());
		r.setObs(ob.getObs());
		return r;
		
	}
	private List<ReservacionCabecera>listar(List<ReservacionCabecera> obj){
		List<ReservacionCabecera> res=new ArrayList<>();
		for(ReservacionCabecera ob:obj){
			ReservacionCabecera r=new ReservacionCabecera();
			r.setId(ob.getId());
			r.getFuncionarioRegistro().setId(ob.getFuncionarioRegistro().getId());
			r.getFuncionarioRegistro().getPersona().setNombre(ob.getFuncionarioRegistro().getPersona().getNombre()+ " "+ob.getFuncionarioRegistro().getPersona().getApellido() );
			r.getFuncionarioFinalizacion().setId(ob.getFuncionarioRegistro().getId());
			r.getFuncionarioFinalizacion().getPersona().setNombre(ob.getFuncionarioFinalizacion().getPersona().getNombre()+" "+ ob.getFuncionarioRegistro().getPersona().getApellido());
			r.getCliente().setId(ob.getCliente().getId());
			r.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ob.getCliente().getPersona().getApellido());
			r.getDocumento().setId(ob.getDocumento().getId());
			r.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			r.setEntrega(ob.getEntrega());
			r.setEstado(ob.getEstado());
			r.setTipo(ob.getTipo());
			r.setTotalHabitacion(ob.getTotalHabitacion());
			r.setTotalProducto(ob.getTotalProducto());
			r.setTotalLetra(ob.getTotalLetra());
			r.setDescripcionCombo(ob.getDescripcionCombo());
			r.setPrecio(ob.getPrecio());
			r.setOperacionCajaEntrega(ob.getOperacionCajaEntrega());
			r.setOperacionCaja(ob.getOperacionCaja());
			
			res.add(r);
		}
		return res;
	}
	private List<ReservacionDetalle>listarDetalle(List<Object[]> obj){
		List<ReservacionDetalle> res=new ArrayList<>();
		for(Object[] ob:obj){
			ReservacionDetalle r=new ReservacionDetalle();
			r.setId(Integer.parseInt(ob[0].toString()));
			r.getProducto().setId(Integer.parseInt(ob[1].toString()));
			r.setDescripcion(ob[2].toString());
			r.setCantidad(Double.parseDouble(ob[3].toString()));
			r.setIva(ob[4].toString());
			r.setPrecio(Double.parseDouble(ob[5].toString()));
			r.setSubTotal(Double.parseDouble(ob[6].toString()));
			r.getReservacionCabecera().setId(Integer.parseInt(ob[7].toString()));
			r.getProducto().setPrecioVenta_1(Double.parseDouble(ob[8].toString()));
			r.getProducto().setPrecioVenta_2(Double.parseDouble(ob[9].toString()));
			r.getProducto().setPrecioVenta_3(Double.parseDouble(ob[10].toString()));
			r.getProducto().setPrecioVenta_4(Double.parseDouble(ob[11].toString()));
			r.setDescuento(Double.parseDouble(ob[12].toString()));
			r.getProducto().getUnidadMedida().setDescripcion(ob[13].toString());
			r.getProducto().setExistencia(Double.parseDouble(ob[14].toString()));
			r.getProducto().setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			r.getProducto().setCodbar(ob[16].toString());
			r.getProducto().getMarca().setDescripcion(ob[17].toString());
			r.setCosto(Double.parseDouble(ob[18].toString()));
			r.setTipoPrecio(ob[19].toString());
			res.add(r);
		}
		return res;
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/finalizar/{idReservacion}/{numeroTerminal}/{idAper}")
	public ResponseEntity<?> Finalizar(@PathVariable int idReservacion, @PathVariable int numeroTerminal,  @PathVariable int idAper){
		ReservacionCabecera r= entityRepository.getOne(idReservacion);
		entityRepository.findByActualizaEstado(idReservacion, "FINALIZADO");
		actualizarHabitacionDisponilidadReservacion(r.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
		return new ResponseEntity<String>("OK", HttpStatus.CREATED);
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/{numeroTerminal}/{idAper}")
	public ResponseEntity<?> guardar(@RequestBody ReservacionCabecera entity, @PathVariable int numeroTerminal,  @PathVariable int idAper){
		try {
			if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(idAper == 0 && entity.getEntrega()>0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO POSEE APERTURA CAJA PARA EFECTUAR ENTREGA INIICAL!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioFinalizacion().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDocumento().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getCliente().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getHabitacionesCategoriaCombo().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE AGREGAR UNA HABITACION PARA PROCEDER A GUARDAR LA RESERVACIÓN!"), HttpStatus.CONFLICT);
			} else if(entity.getTotalHabitacion() <=0 || entity.getTotalHabitacion()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL HABITACIÓN DE LA DE LA RESERVACIÓN DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			} else if(entity.getTotalLetra().equals("") || entity.getTotalLetra()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL MONTO EN LETRA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else {
				for(int ind=0; ind < entity.getReservacionDetalles().size(); ind++) {
					ReservacionDetalle pro = entity.getReservacionDetalles().get(ind);
					if(pro.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE DEVOLUCION ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}
				if(entity.getId() !=0) {
					entity.setFechaRegistro(new Date());
					entity.setHora(hora());
					if(entity.getEstado().equals("FINALIZADO")) {
						entity.setFechaFactura(new Date());
						entity.setHoraFinalizacion(hora());
						//entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId()));
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
					}else if(entity.getEstado().equals("RESERVADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), true, true);
						entity.setFechaFactura(null);
					}else if(entity.getEstado().equals("PRE-RESERVADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, true);
						entity.setFechaFactura(null);
					} else if(entity.getEstado().equals("CANCELADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
						entity.setFechaFactura(null);
					}
					
					int idVent=entity.getId();
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getReservacionDetalles().size()>0){
						if(entity.getEstado().equals("FINALIZADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								totalDescuento = totalDescuento + detalleProducto.getDescuento();
								detalleProducto.getReservacionCabecera().setId(idVent);
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
								detalleRepository.save(detalleProducto);

								this.actualizarProductoBase(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getSubTotal(), detalleProducto.getPrecio(), entity.getFuncionarioRegistro().getId(), entity.getTipo(), idVent);

							}
						}else if(entity.getEstado().equals("RESERVADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								detalleProducto.getReservacionCabecera().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
							
						}else if(entity.getEstado().equals("PRE-RESERVADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								detalleProducto.getReservacionCabecera().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
						}
					}
						
					if(entity.getEntrega()>0 && entity.getOperacionCajaEntrega()==0) {
						OperacionCaja op= new OperacionCaja();
						op.getAperturaCaja().setId(idAper);
						op.getConcepto().setId(13);
						if(entity.getDocumento().getDescripcion().equals("EFECTIVO")) {
							op.getTipoOperacion().setId(1);op.setEfectivo(entity.getEntrega());
							this.aperturaCajaRepository.findByActualizarAperturaSaldo(idAper, entity.getEntrega());

						}
						if(entity.getDocumento().getDescripcion().equals("CHEQUE")) {
							op.getTipoOperacion().setId(2);
							this.aperturaCajaRepository.findByActualizarAperturaSaldoCheque(idAper, entity.getEntrega());							
						}
						if(entity.getDocumento().getDescripcion().equals("TARJETA")) {
							op.getTipoOperacion().setId(3);
							this.aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(idAper, entity.getEntrega());							
						}
						op.setTipo("ENTRADA");
						op.setMonto(entity.getEntrega());
						op.setMotivo("ALOJAMIENTO REF.: "+entity.getId());
						operacionRepository.save(op);
						OperacionCaja opNuevo= operacionRepository.findTop1ByOrderByIdDesc();
						entity.setOperacionCajaEntrega(opNuevo.getId());
					}
					
					entity.setTotalIvaDies(Utilidades.calcularIvaDies(entity.getTotalHabitacion())+total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalDescuento(totalDescuento);
					
					entityRepository.save(entity);

				
					
				}else {
					entity.setFechaRegistro(new Date());
					entity.setHora(hora());
					if(entity.getEstado().equals("FINALIZADO")) {
						entity.setFechaFactura(new Date());
						entity.setHoraFinalizacion(hora());
						//entity.setNroDocumento(getNroDocumento(entity.getDocumento().getId()));
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
					}else if(entity.getEstado().equals("RESERVADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), true, true);
						entity.setFechaFactura(null);
					}else if(entity.getEstado().equals("PRE-RESERVADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, true);
						entity.setFechaFactura(null);
					} else if(entity.getEstado().equals("CANCELADO")) {
						actualizarHabitacionDisponilidadReservacion(entity.getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
						entity.setFechaFactura(null);
					}
					
					entityRepository.save(entity);
					ReservacionCabecera id = entityRepository.getUltimaReservacion();
					System.out.println(id.getFechaRegistro());
					System.out.println(id.getFechaFactura()+"  ******");
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					double total10=0, total5=0, totalDescuento=0;
					if(entity.getReservacionDetalles().size()>0){
						if(entity.getEstado().equals("FINALIZADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								totalDescuento = totalDescuento + detalleProducto.getDescuento();
								detalleProducto.getReservacionCabecera().setId(idVent);
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
								detalleRepository.save(detalleProducto);

								this.actualizarProductoBase(detalleProducto.getProducto().getId(), detalleProducto.getCantidad(), detalleProducto.getSubTotal(), detalleProducto.getPrecio(), entity.getFuncionarioRegistro().getId(), entity.getTipo(), idVent);

							}
						}else if(entity.getEstado().equals("RESERVADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								detalleProducto.getReservacionCabecera().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
							
						}else if(entity.getEstado().equals("PRE-RESERVADO")) {
							for(ReservacionDetalle detalleProducto: entity.getReservacionDetalles()) {
								detalleProducto.getReservacionCabecera().setId(idVent);
								detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
								detalleRepository.save(detalleProducto);
							}
						}
						
						if(entity.getEntrega()>0 && entity.getOperacionCajaEntrega()==0) {
							OperacionCaja op= new OperacionCaja();
							op.getAperturaCaja().setId(idAper);
							op.getConcepto().setId(13);
							if(entity.getDocumento().getDescripcion().equals("EFECTIVO")) {
								op.getTipoOperacion().setId(1);op.setEfectivo(entity.getEntrega());
								this.aperturaCajaRepository.findByActualizarAperturaSaldo(idAper, entity.getEntrega());

							}
							if(entity.getDocumento().getDescripcion().equals("CHEQUE")) {
								op.getTipoOperacion().setId(2);
								this.aperturaCajaRepository.findByActualizarAperturaSaldoCheque(idAper, entity.getEntrega());							
							}
							if(entity.getDocumento().getDescripcion().equals("TARJETA")) {
								op.getTipoOperacion().setId(3);
								this.aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(idAper, entity.getEntrega());							
							}
							op.setTipo("ENTRADA");
							op.setMonto(entity.getEntrega());
							op.setMotivo("ALOJAMIENTO REF.: "+entity.getId());
							operacionRepository.save(op);
							OperacionCaja opNuevo= operacionRepository.findTop1ByOrderByIdDesc();
							entity.setOperacionCajaEntrega(opNuevo.getId());
						}
					}
					entity.setTotalIvaDies(Utilidades.calcularIvaDies(entity.getTotalHabitacion())+total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalDescuento(totalDescuento);
					System.out.println(id.getFechaFactura());
					id.setFechaFactura(id.getFechaFactura());
					entityRepository.save(entity);
					System.out.println("entro  nuevo");
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	private void actualizarHabitacionDisponilidadReservacion(int id, boolean dispo, boolean reser) {
		this.habitacionesRepository.findByActualizaEstadoDisponilidadReservacion(id, dispo, reser);
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
			
			c= conceptoRepository.findById(13).get();
			

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
				
				cc= conceptoRepository.findById(13).get();
				
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
				
				c= conceptoRepository.findById(13).get();
				

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
					
					con= conceptoRepository.findById(13).get();
					

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
				
				c= conceptoRepository.findById(13).get();
				
				mov.setReferencia(c.getDescripcion()+" REF.: "+ idVenta);
				movEntradaSalidaRepository.save(mov);
			}

		}
	}
	public ReservacionCabecera reservacionessss(int idVenta) {
		ReservacionCabecera cv = null;

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
	public List<ReservacionCabecera> getLista(int idVenta ) {

		List<ReservacionCabecera> lista = new ArrayList<>();

		ReservacionCabecera xxx = new ReservacionCabecera();
		List<ReservacionDetalle> detProducto = new ArrayList<>();


		xxx = reservacionessss(idVenta);
		detProducto = listarDetalle(detalleRepository.getDetallePorIdCabecera(idVenta));
		


		for (int i = 0; i < 1; i++) {
			Cliente cli = clienteRepository.getIdCliente(xxx.getCliente().getId());
			Funcionario FunV = funcionarioRepository.getIdFuncionario(xxx.getFuncionarioFinalizacion().getId());
			ReservacionCabecera v = new ReservacionCabecera();
			v.getCliente().getPersona().setNombre(cli.getPersona().getNombre()+ " "+cli.getPersona().getApellido());
			v.getCliente().getPersona().setCedula(cli.getPersona().getCedula());
			v.getCliente().getPersona().setDireccion(cli.getPersona().getDireccion());
			v.setFechaFactura(xxx.getFechaFactura());
			v.setFechaRegistro(xxx.getFechaRegistro());
			v.setHora(xxx.getHora());
			System.out.println("fun veeveveve : "+FunV.getPersona().getNombre());
			v.getFuncionarioFinalizacion().getPersona().setNombre(FunV.getPersona().getNombre()+ " "+FunV.getPersona().getApellido());
			v.setTotalDescuento(xxx.getTotalDescuento());
			v.setTotalIvaCinco(xxx.getTotalIvaCinco());
			v.setTotalIvaDies(xxx.getTotalIvaDies());
			v.setTotalHabitacion(xxx.getTotalHabitacion());
			v.setPrecio(xxx.getPrecio());
			v.setTotalProducto(xxx.getTotalProducto());
			v.setTotalLetra(xxx.getTotalLetra());
			v.setTipo(xxx.getTipo());
			v.getDocumento().setDescripcion(xxx.getDocumento().getDescripcion());
			v.setEntrega(xxx.getEntrega());
			v.setReservacionDetalles(detProducto);
			lista.add(v);
			System.out.println("lista cantidad : "+lista.get(0).getReservacionDetalles().size());
		}

		return lista;

	}
	@RequestMapping(value="/reImprimirMatricial/{id}/{numeroTerminal}/{fecha}", method=RequestMethod.GET)
	public void reImprimirMatricial(@PathVariable int id, @PathVariable int numeroTerminal, @PathVariable String fecha){
		List<ReservacionCabecera> venta = getLista(id);
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
				ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(3);
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
	@RequestMapping(value="/resumenRecepcionRangoFecha/{fechaInicio}/{fechaFin}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenServicioRangoFecha(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaInicio, @PathVariable String fechaFin) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		try {
			Date fecI, fechaFi;
			fecI = FechaUtil.setFechaHoraInicial(fechaInicio);
			fechaFi = FechaUtil.setFechaHoraFinal(fechaFin);
			List<Object []> obb =entityRepository.getResumenRecepcionesRagoFecha(fecI, fechaFi);
			List<ReservacionCabecera> det=new ArrayList<>();

			for(Object[] ob: obb) {
				ReservacionCabecera d = new  ReservacionCabecera();
				d.setDescripcionCombo(ob[0].toString());
				d.getFuncionarioFinalizacion().getPersona().setNombre(ob[1].toString());
				d.getCliente().getPersona().setNombre(ob[2].toString());
				d.setEntrega(Double.parseDouble(ob[3].toString()));
				d.setPrecio(Double.parseDouble(ob[4].toString()));
				d.setTotalProducto(Double.parseDouble(ob[5].toString()));
				d.setTotalHabitacion(Double.parseDouble(ob[6].toString()));
				d.setFechaRegistro(FechaUtil.convertirFechaStringADateUtil(ob[7].toString()));
				d.setHora(ob[8].toString());
				d.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[9].toString()));
				d.setHoraFinalizacion(ob[10].toString());
				det.add(d);
			}
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
			report = new Reporte();
			report.reportPDFDescarga(det, map, "ReporteRecepcionHabitaciones", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value="/resumenRecepcionRangoFechaFuncionario/{fechaInicio}/{fechaFin}/{idFun}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenServicioRangoFechaFuncionario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaInicio, @PathVariable String fechaFin, @PathVariable int idFun) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		try {
			Date fecI, fechaFi;
			fecI = FechaUtil.setFechaHoraInicial(fechaInicio);
			fechaFi = FechaUtil.setFechaHoraFinal(fechaFin);
			List<Object []> obb =entityRepository.getResumenRecepcionesRagoFechaFuncionario(fecI, fechaFi, idFun);
			List<ReservacionCabecera> det=new ArrayList<>();
			Funcionario f = funcionarioRepository.getIdFuncionario(idFun);
			for(Object[] ob: obb) {
				ReservacionCabecera d = new  ReservacionCabecera();
				d.setDescripcionCombo(ob[0].toString());
				d.getFuncionarioFinalizacion().getPersona().setNombre(ob[1].toString());
				d.getCliente().getPersona().setNombre(ob[2].toString());
				d.setEntrega(Double.parseDouble(ob[3].toString()));
				d.setPrecio(Double.parseDouble(ob[4].toString()));
				d.setTotalProducto(Double.parseDouble(ob[5].toString()));
				d.setTotalHabitacion(Double.parseDouble(ob[6].toString()));
				d.setFechaRegistro(FechaUtil.convertirFechaStringADateUtil(ob[7].toString()));
				d.setHora(ob[8].toString());
				d.setFechaFactura(FechaUtil.convertirFechaStringADateUtil(ob[9].toString()));
				d.setHoraFinalizacion(ob[10].toString());
				det.add(d);
			}
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
			map.put("funcionarioRegistro", ""+f.getPersona().getNombre()+" "+f.getPersona().getApellido());

			report = new Reporte();
			report.reportPDFDescarga(det, map, "ReporteRecepcionHabitacionesFuncionario", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
}
