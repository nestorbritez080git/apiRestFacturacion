package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.InformeHistoricoMovimiento;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionAnulada;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.repository.HabitacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionAnuladaRepository;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaCabeceraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAperturaCajaRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;


@EnableAsync
@Transactional
@RestController
@RequestMapping("operacionCaja")
public class OperacionCajaController {
	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;
	@Autowired
	private OperacionCajaRepository entityRepository;
	@Autowired
	private OperacionCajaCabeceraRepository cabeceraRepository;
	
	
	@Autowired
	private NotaCreditoRepository notaCreditoRepository;
	@Autowired
	private VentaRepository ventaRepositoty;
	@Autowired
	private ReservacionCabeceraRepository reservacionRepositoty;
	
	@Autowired
	private ReservacionAnuladaRepository reservacionAnuladaRepositoty;
	
	@Autowired
	private AperturaCajaRepository aperturaRepository;
	@Autowired
	private HabitacionesRepository habitacionesRepository;
	@Autowired
	private ConceptoRepository conceptoRepository;
	@Autowired
	private CobrosClienteRepository cobrosRepository;
	@Autowired
	private TransferenciaAperturaCajaRepository transferenciaAperturaCajaRepository;
	@Autowired
	private IUsuarioService usuarioService;
	private Reporte report;
	
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/detalleOperacion/{idApertura}")
	public List<OperacionCaja> getOperacionVentaContadoPorIdApertura(@PathVariable int idApertura){
		List<Object[]> objeto=entityRepository.getOperacionProIdApertura(idApertura);
		System.out.println();
		List<OperacionCaja> lisOperacion=new ArrayList<>();
		for(Object[] ob:objeto){
			OperacionCaja opCaja= new  OperacionCaja();
			opCaja.setId(Integer.parseInt(ob[0].toString()));
			opCaja.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			opCaja.setMonto(Double.parseDouble(ob[2].toString()));
			opCaja.setMotivo(ob[3].toString());
			opCaja.setTipo(ob[4].toString());
			opCaja.getTipoOperacion().setDescripcion(ob[5].toString());
			opCaja.getTipoOperacion().setId(Integer.parseInt(ob[6].toString()));
			opCaja.getConcepto().setId(Integer.parseInt(ob[7].toString()));
			opCaja.setReferenciaOperacion(Integer.parseInt(ob[8].toString()));
			opCaja.getAperturaCaja().setId(Integer.parseInt(ob[9].toString()));
			lisOperacion.add(opCaja);
		}
		return  lisOperacion;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/detalleOperacionCobros/{idApertura}")
	public List<CobrosCliente> getOperacionCobrosPorIdApertura(@PathVariable int idApertura){
		List<Object[]> objeto=entityRepository.getOperacionCobrosClienteProIdApertura(idApertura);
		System.out.println();
		List<CobrosCliente> lisOperacion=new ArrayList<>();
		for(Object[] ob:objeto){
			CobrosCliente c= new  CobrosCliente();
			c.getOperacionCaja().setId(Integer.parseInt(ob[0].toString()));
			c.getOperacionCaja().setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			c.getOperacionCaja().setMonto(Double.parseDouble(ob[2].toString()));
			c.getOperacionCaja().setMotivo(ob[3].toString());
			c.getOperacionCaja().setTipo(ob[4].toString());
			c.getOperacionCaja().getTipoOperacion().setDescripcion(ob[5].toString());
			c.getOperacionCaja().getTipoOperacion().setId(Integer.parseInt(ob[6].toString()));
			c.getOperacionCaja().getConcepto().setId(Integer.parseInt(ob[7].toString()));
			c.getCuentaCobrarCabecera().getCliente().getPersona().setNombre(ob[8].toString()+ " "+ob[9].toString());
			lisOperacion.add(c);
			
		}
		return  lisOperacion;
	}
	@RequestMapping(method=RequestMethod.GET)
	public List<OperacionCaja> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/eliminarOperacion/{id}/{idC}")
	public ResponseEntity<?> eliminarOperacionPorAperturaPorConcepto(@PathVariable int id, @PathVariable int idC){
		try {
			entityRepository.borraDatosSalidaCapital(id, idC);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR AL INTENTAR BORRAR OPERACIÓN: "+e.getMessage()), HttpStatus.CONFLICT);
			
		}
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public OperacionCaja getPorId(@PathVariable int id){
		OperacionCaja ope=entityRepository.findById(id).get();
		OperacionCaja operacion=null;
		if(ope!= null) {
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
		}else {
			operacion = null;
		}
		return operacion;
	
	}
	@RequestMapping(method=RequestMethod.GET,value="/cabecera/{id}")
	public OperacionCajaCabecera getPorIdCabecera(@PathVariable int id){
		OperacionCajaCabecera ope=cabeceraRepository.consultarOperacionCajaCabeceraPorId(id);
		OperacionCajaCabecera operacion=null;
		if(ope!= null) {
			operacion = new OperacionCajaCabecera();
			operacion.setId(ope.getId());
			operacion.setFecha(ope.getFecha());
			operacion.setEstado(ope.getEstado());
			operacion.setMonto(ope.getMonto());
			operacion.setMotivo(ope.getMotivo());
			operacion.setReferenciaOperacion(ope.getReferenciaOperacion());
			operacion.setAperturaCaja(ope.getAperturaCaja());
			operacion.setConcepto(ope.getConcepto());
			operacion.setOperacionCajas(ope.getOperacionCajas());
			
		}else {
			operacion = null;
		}
		return operacion;
	}
/*
	@RequestMapping(method=RequestMethod.POST, value="/{operacion}")
	public ResponseEntity<?> guardar(HttpServletResponse response, @RequestBody OperacionCaja entity, @PathVariable int operacion){
		// Impresora impre=new Impresora();
		// impre=impreRepository.findTop1ByOrderByIdAsc();
		// String tipo = impre.getDescripcion();
/****************************************************
					Org org = new Org();
					org=orgRepository.findOne(1);
					
					
					List<Venta> venta = new ArrayList<>();
/************************************************
		if (operacion == 0) {
			Venta v = new Venta();
			v=ventaRepositoty.findTop1ByOrderByIdDesc();
			System.out.println(""+v.getId());
			Concepto c= new Concepto();
			c= conceptoRepository.findOne(entity.getConcepto().getId());
			entity.setMotivo(c.getDescripcion()+" REF.: "+v.getId());
			
			entity.setTipo("ENTRADA");
			entity.setMonto(v.getTotal());
			aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
			entityRepository.save(entity);
			OperacionCaja op=  entityRepository.findTop1ByOrderByIdDesc();
			ventaRepositoty.findByActualizarVentaOperacion(v.getId(),op.getId());
			/************************************* *
			venta =ventaRepository.findById(v.getId());
			OperacionCaja opa= new OperacionCaja();
			Double totalProducto = 0.0;
			Double totalServicio = 0.0;
			Double total = 0.0;
			for(Venta vs: venta) {
				for(DetalleProducto detl1 : vs.getDetalleProducto()){
					totalProducto +=detl1.getSubTotal();
				}
				for(DetalleServicios detl2 : vs.getDetalleServicio()){
					totalServicio +=detl2.getSubTotal();
				}
				System.out.println("   "+vs.getOperacionCaja());
				op= entityRepository.findOne(vs.getOperacionCaja());
				total = vs.getTotal();
				Map<String, Object> map = new HashMap<>();
				
				map.put("cajero", opa.getAperturaCaja().getFuncionario().getUser().getUsername());
				map.put("TipoOperacion", opa.getTipoOperacion().getDescripcion());
				map.put("vuelto", opa.getVuelto());
				map.put("efectivo", opa.getEfectivo());
				map.put("motivo", opa.getMotivo());

				map.put("totalProducto", totalProducto);
				map.put("totalServicio", totalServicio);
				map.put("org", org.getNombre());
				map.put("telefono", org.getTelefono());
				map.put("ruc", org.getRuc());
				map.put("direccion", org.getDireccion());
				map.put("total", total);
				Reporte report=new Reporte();
				try {
					report.reportPDFDescarga(venta, map, "TicketVentaContadoReport", response);
				} catch (Exception e) {
					
				}
			}
			/************************************** 

		} else {
			
			Concepto c= new Concepto();
			c= conceptoRepository.findOne(entity.getConcepto().getId());
			entity.setTipo("ENTRADA");
			aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
			entityRepository.save(entity);
			OperacionCaja op= new OperacionCaja();
			op=  entityRepository.findTop1ByOrderByIdDesc();
			System.out.println("id operacion si es igual a dos : "+ op.getId());
			Venta vv = new Venta();
			vv=ventaRepositoty.getVentaPorOperacionId(operacion);
			System.out.println("vneta id: "+vv.getId());
			op.setMotivo(c.getDescripcion()+" REF.: "+vv.getId());
			op.setMonto(vv.getTotal());
			entityRepository.save(op);
			ventaRepositoty.findByActualizarVentaOperacion(vv.getId(),op.getId());
			/**********************************************
			venta =ventaRepository.findById(vv.getId());
			OperacionCaja ope= new OperacionCaja();
			Double totalProducto = 0.0;
			Double totalServicio = 0.0;
			Double total = 0.0;
			for(Venta v: venta) {
				for(DetalleProducto detl1 : v.getDetalleProducto()){
					totalProducto +=detl1.getSubTotal();
				}
				for(DetalleServicios detl2 : v.getDetalleServicio()){
					totalServicio +=detl2.getSubTotal();
				}
				System.out.println("   "+v.getOperacionCaja());
				op= entityRepository.findOne(v.getOperacionCaja());
				total = v.getTotal();
				Map<String, Object> map = new HashMap<>();
				
				map.put("cajero", ope.getAperturaCaja().getFuncionario().getUser().getUsername());
				map.put("TipoOperacion", ope.getTipoOperacion().getDescripcion());
				map.put("vuelto", ope.getVuelto());
				map.put("efectivo", ope.getEfectivo());
				map.put("motivo", ope.getMotivo());

				map.put("totalProducto", totalProducto);
				map.put("totalServicio", totalServicio);
				map.put("org", org.getNombre());
				map.put("telefono", org.getTelefono());
				map.put("ruc", org.getRuc());
				map.put("direccion", org.getDireccion());
				map.put("total", total);
				Reporte report=new Reporte();
				try {
					report.reportPDFDescarga(venta, map, "TicketVentaContadoReport", response);
				} catch (Exception e) {
					
				}
			}
			/***********************************************

		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}*/
	@RequestMapping(method=RequestMethod.GET, value="/arqueoCajaActivo/{idApertura}")
	public List<OperacionCaja> getArqueoCajaActivo(@PathVariable int idApertura){
		List<Object[]> listado= new ArrayList<>();
		listado = aperturaRepository.arqueoCajaActivo(idApertura);
		List<OperacionCaja> listadoRetorno= new ArrayList<>();
		for(Object[] ob:listado){
			OperacionCaja ope= new OperacionCaja();
			String fech=ob[0].toString();
			ope.setFecha(FechaUtil.convertirFechaStringADateUtil(fech));
			ope.setMonto(Double.parseDouble(ob[1].toString()));
			ope.setEfectivo(Double.parseDouble(ob[2].toString()));
			ope.setVuelto(Double.parseDouble(ob[3].toString()));
			ope.setMotivo(ob[4].toString());
			listadoRetorno.add(ope);
		}
		return listadoRetorno;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/reservacion/anulacion")
	public ResponseEntity<?>  guardarOperacionReservacionAnulacion(
			@RequestPart("reservacion") ReservacionAnulada entity,
		    @RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista){
		
		if(entity.getReservacionCabecera().getId() > 0) {
			System.out.println("entro id mayor a cerpp: "+entity.getReservacionCabecera().getId()+ " , "+entity.getReservacionCabecera().getDescripcionCombo());
			ResponseEntity<?> validacionCaja = validarCajaSalida(operacionCajaLista);
            if (validacionCaja != null) return validacionCaja;
            procesarOperacionCajaAnulacionReservaciones(entity, operacionCajaLista);

		}else {
			/*
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setTipo("SALIDA");
			if (entity.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			
			
			entityRepository.save(entity);
			OperacionCaja op= new OperacionCaja();
			op=  entityRepository.findTop1ByOrderByIdDesc();
			System.out.println("id operacion si es igual a dos : "+ op.getId());
			ReservacionCabecera vv = new ReservacionCabecera();
			vv=reservacionRepositoty.getOne(idReservacion);
			System.out.println("vneta id: "+vv.getId());
			op.setMotivo(c.getDescripcion()+": "+vv.getDescripcionCombo()+ " Ref.:"+vv.getId());
			entityRepository.save(op);
			//reservacionRepositoty.findByActualizarReservacionOperacion(vv.getId(),op.getId());
			*/
		}
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.POST, value="/reservacion/{idReservacion}")
	public ResponseEntity<?>  guardarOperacionReservacion(@RequestBody OperacionCaja entity, @PathVariable int idReservacion){
		if(idReservacion==0) {
			System.out.println("entroo id recepcion 0 nueva recepcion");
			ReservacionCabecera v = new ReservacionCabecera();
			v = reservacionRepositoty.getUltimaReservacion();
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
			entity.setReferenciaOperacion(v.getId());
			entity.setTipo("ENTRADA");
			
			List<OperacionCaja> listOperacion = new  ArrayList<>();
			listOperacion.add(entity);
			ResponseEntity<?> validacionCaja = validarCajaEntrada(listOperacion);
            if (validacionCaja != null) return validacionCaja;
            procesarOperacionCajaReservaciones(v, listOperacion);

		
		}else {
			ReservacionCabecera v = new ReservacionCabecera();
			v = reservacionRepositoty.getOne(idReservacion);
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
			entity.setReferenciaOperacion(v.getId());
			entity.setTipo("ENTRADA");
			
			List<OperacionCaja> listOperacion = new  ArrayList<>();
			listOperacion.add(entity);
			ResponseEntity<?> validacionCaja = validarCajaEntrada(listOperacion);
            if (validacionCaja != null) return validacionCaja;
			
            procesarOperacionCajaReservaciones(v, listOperacion);
			
		
		}
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/cobrosClientess")
	public ResponseEntity<?>  guardarOperacionCobros(@RequestBody OperacionCaja entity){
		
		if (entity.getTipoOperacion().getId() == 1) {
			System.out.println("adfasdfasfasdf"+entity.getAperturaCaja().getId());
			aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
		}
		
		if (entity.getTipoOperacion().getId() == 2) {
			aperturaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getId(), entity.getMonto());
		}
		
		if (entity.getTipoOperacion().getId() == 3) {
			aperturaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
		}
//		CobrosCliente cobros= 0;
		CobrosCliente cobros= cobrosRepository.findTop1ByOrderByIdDesc();
		System.out.println("ID OPE ANT : "+cobros.getOperacionCaja().getId());
		
		System.out.println("idCuentaCabe "+cobros.getCuentaCobrarCabecera().getId());
		cuentaCobrarRepository.findByActualizarPagadoCuenta(cobros.getCuentaCobrarCabecera().getId(), entity.getMonto());
		Concepto c= new Concepto();
		c= conceptoRepository.findById(entity.getConcepto().getId()).get();
		entity.setMotivo(c.getDescripcion()+" REF.: "+ cobros.getId());
		entity.setTipo("ENTRADA");
		entityRepository.save(entity);
		OperacionCaja opulrt = entityRepository.findTop1ByOrderByIdDesc();
		System.out.println("ID OPE ACT : "+opulrt.getId());
		//cobrosRepository.findByActualizarCobrosOperacion(cobros.getId(), opulrt.getId());
		

		
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	public OperacionCaja operacion() {
		return entityRepository.findTop1ByOrderByIdDesc();
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value="/guardarTransferenciaAperturaCajaActivo/{operacion}/{monto}/{montoCheque}/{montoTarjeta}")
	public ResponseEntity<?> guardarTransferenciaAperturaActivo(@RequestBody OperacionCaja entity, @PathVariable int operacion, @PathVariable Double monto, @PathVariable Double montoCheque, @PathVariable Double montoTarjeta){
		try {
			if(entity.getAperturaCaja().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR  LOS DATOS DE LA APERTURA CAJA ORIGEN"), HttpStatus.CONFLICT);
			}if(entity.getAperturaCaja().getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR  LOS DATOS DE LA APERTURA CAJA DESTINO"), HttpStatus.CONFLICT);
			}else if(entity.getConcepto().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR EL CONCEPTO DE LA OPERACION CAJA"), HttpStatus.CONFLICT);
			}else {
				AperturaCaja cOrigen = new AperturaCaja();
				cOrigen=aperturaRepository.getAperturaCajaPorIdCaja(entity.getAperturaCaja().getId());
				AperturaCaja cDestino = new AperturaCaja();
				cDestino=aperturaRepository.getAperturaCajaPorIdCaja(entity.getAperturaCaja().getFuncionario().getId());
				if(cOrigen==null) {
					System.out.println("entrooo null caja chiac");
					return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
				}else {
					// Validar saldos disponibles
					if (monto > 0 && cOrigen.getSaldoActual() < monto) {
					    return new ResponseEntity<>(
					        new CustomerErrorType("El efectivo disponible en la caja de origen es insuficiente para el monto solicitado."),
					        HttpStatus.CONFLICT
					    );
					}

					if (montoCheque > 0 && cOrigen.getSaldoActualCheque() < montoCheque) {
					    return new ResponseEntity<>(
					        new CustomerErrorType("El monto en cheque disponible en la caja de origen es insuficiente para el monto solicitado."),
					        HttpStatus.CONFLICT
					    );
					}

					if (montoTarjeta > 0 && cOrigen.getSaldoActualTarjeta() < montoTarjeta) {
					    return new ResponseEntity<>(
					        new CustomerErrorType("El monto en tarjeta disponible en la caja de origen es insuficiente para el monto solicitado."),
					        HttpStatus.CONFLICT
					    );
					}
					
						List<OperacionCaja> listOperacionOrigen = new ArrayList<>();
						List<OperacionCaja> listOperacionDestino = new ArrayList<>();
						
						List<OperacionCaja> listOperacionOrigenRetorno = new ArrayList<>();
						List<OperacionCaja> listOperacionDestinoRetorno = new ArrayList<>();
						if(operacion ==0) {
							Concepto c= new Concepto();
							c= conceptoRepository.findById(23).get();
							if(monto > 0) {
								System.out.println("ENTRO IFFF MONTO efe");
								OperacionCaja opOrigenEfectivo= new OperacionCaja();
								opOrigenEfectivo.getAperturaCaja().setId(cOrigen.getId());
								opOrigenEfectivo.getConcepto().setId(23);
								opOrigenEfectivo.setMonto(monto);
								opOrigenEfectivo.setFecha(new Date());
								opOrigenEfectivo.setTipo("SALIDA");
								opOrigenEfectivo.getTipoOperacion().setId(1);//efectivo
								opOrigenEfectivo.setMotivo(c.getDescripcion()+" REF. DESTINO(#): "+cDestino.getId()+ ", CAJERO/A: " +cDestino.getFuncionario().getPersona().getNombre()+ " "+cDestino.getFuncionario().getPersona().getApellido());
								opOrigenEfectivo.setReferenciaOperacion(cDestino.getId());
								opOrigenEfectivo.setReferenciaTipoOperacion("");
								listOperacionOrigen.add(opOrigenEfectivo);
								//OperacionCaja opSaveOrigenEfectivo = entityRepository.save(opOrigenEfectivo);
								//aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(cOrigen.getId(), monto);

								OperacionCaja opDestinoEfectivo= new OperacionCaja();
								opDestinoEfectivo.getAperturaCaja().setId(cDestino.getId());
								opDestinoEfectivo.getConcepto().setId(23);
								opDestinoEfectivo.setMonto(monto);
								opDestinoEfectivo.setFecha(new Date());
								opDestinoEfectivo.setTipo("ENTRADA");
								opDestinoEfectivo.getTipoOperacion().setId(1);//efectivo
								opDestinoEfectivo.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoEfectivo.setReferenciaOperacion(cOrigen.getId());
								opDestinoEfectivo.setReferenciaTipoOperacion("");
								listOperacionDestino.add(opDestinoEfectivo);
								//OperacionCaja opSaveDestinoEfectivo = entityRepository.save(opDestinoEfectivo);
								//aperturaRepository.findByActualizarAperturaSaldo(cDestino.getId(), monto);
								//opSaveOrigenEfectivo.setReferenciaOperacion(opSaveDestinoEfectivo.getId());
								//opSaveDestinoEfectivo.setReferenciaOperacion(opSaveOrigenEfectivo.getId());
								//entityRepository.save(opSaveOrigenEfectivo);
								//entityRepository.save(opSaveDestinoEfectivo);
							}
							if (montoCheque > 0) {
								System.out.println("ENTRO IFFF MONTO che");
								OperacionCaja opOrigenCheque= new OperacionCaja();
								opOrigenCheque.getAperturaCaja().setId(cOrigen.getId());
								opOrigenCheque.getConcepto().setId(23);
								opOrigenCheque.setMonto(montoCheque);
								opOrigenCheque.setFecha(new Date());
								opOrigenCheque.setTipo("SALIDA");
								opOrigenCheque.getTipoOperacion().setId(2);//cheque
								opOrigenCheque.setMotivo(c.getDescripcion()+" REF. DESTINO(#): "+cDestino.getId()+ ", CAJERO/A: " +cDestino.getFuncionario().getPersona().getNombre()+ " "+cDestino.getFuncionario().getPersona().getApellido());
								opOrigenCheque.setReferenciaOperacion(cDestino.getId());
								opOrigenCheque.setReferenciaTipoOperacion("");
								listOperacionOrigen.add(opOrigenCheque);
								//OperacionCaja opSaveOrigenCheque = entityRepository.save(opOrigenCheque);
								//aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(cOrigen.getId(), montoCheque);

								OperacionCaja opDestinoCheque= new OperacionCaja();
								opDestinoCheque.getAperturaCaja().setId(cDestino.getId());
								opDestinoCheque.getConcepto().setId(23);
								opDestinoCheque.setMonto(montoCheque);
								opDestinoCheque.setFecha(new Date());
								opDestinoCheque.setTipo("ENTRADA");
								opDestinoCheque.getTipoOperacion().setId(2);//efectivo
								opDestinoCheque.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoCheque.setReferenciaOperacion(cOrigen.getId());
								opDestinoCheque.setReferenciaTipoOperacion("");
								listOperacionDestino.add(opDestinoCheque);
								//OperacionCaja opSaveDestinoCheque = entityRepository.save(opDestinoCheque);
								//aperturaRepository.findByActualizarAperturaSaldoCheque(cDestino.getId(), montoCheque);
								//opSaveOrigenCheque.setReferenciaOperacion(opSaveDestinoCheque.getId());
								//opSaveDestinoCheque.setReferenciaOperacion(opSaveOrigenCheque.getId());
								//entityRepository.save(opSaveOrigenCheque);
								//entityRepository.save(opSaveDestinoCheque);
							
							}
							if (montoTarjeta>0) {
								System.out.println("ENTRO IFFF MONTO tar");
								OperacionCaja opOrigenTarjeta= new OperacionCaja();
								opOrigenTarjeta.getAperturaCaja().setId(cOrigen.getId());
								opOrigenTarjeta.getConcepto().setId(23);
								opOrigenTarjeta.setMonto(montoTarjeta);
								opOrigenTarjeta.setFecha(new Date());
								opOrigenTarjeta.setTipo("SALIDA");
								opOrigenTarjeta.getTipoOperacion().setId(3);//tarjeta
								opOrigenTarjeta.setMotivo(c.getDescripcion()+" REF. DESTINO(#): "+cDestino.getId()+ ", CAJERO/A: " +cDestino.getFuncionario().getPersona().getNombre()+ " "+cDestino.getFuncionario().getPersona().getApellido());
								opOrigenTarjeta.setReferenciaOperacion(cDestino.getId());
								opOrigenTarjeta.setReferenciaTipoOperacion("");
								listOperacionOrigen.add(opOrigenTarjeta);
								//OperacionCaja opSaveOrigenTarjeta = entityRepository.save(opOrigenTarjeta);
								//aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(cOrigen.getId(), montoTarjeta);

								OperacionCaja opDestinoTarjeta= new OperacionCaja();
								opDestinoTarjeta.getAperturaCaja().setId(cDestino.getId());
								opDestinoTarjeta.getConcepto().setId(23);
								opDestinoTarjeta.setMonto(montoTarjeta);
								opDestinoTarjeta.setFecha(new Date());
								opDestinoTarjeta.setTipo("ENTRADA");
								opDestinoTarjeta.getTipoOperacion().setId(3);//efectivo
								opDestinoTarjeta.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoTarjeta.setReferenciaOperacion(cOrigen.getId());
								opDestinoTarjeta.setReferenciaTipoOperacion("");
								listOperacionDestino.add(opDestinoTarjeta);
								//OperacionCaja opSaveDestinoTarjeta = entityRepository.save(opDestinoTarjeta);
								//aperturaRepository.findByActualizarAperturaSaldoTarjeta(cDestino.getId(), montoTarjeta);
								//opSaveOrigenTarjeta.setReferenciaOperacion(opSaveDestinoTarjeta.getId());
								//opSaveDestinoTarjeta.setReferenciaOperacion(opSaveOrigenTarjeta.getId());
								//entityRepository.save(opSaveOrigenTarjeta);
								//entityRepository.save(opSaveDestinoTarjeta);
							}
							listOperacionOrigenRetorno = procesarOperacionCajaTransferenciaOrigen(listOperacionOrigen);
							listOperacionDestinoRetorno = procesarOperacionCajaTransferenciaDestino(listOperacionDestino);
							for (OperacionCaja op: listOperacionOrigenRetorno) {
								System.out.println("caja destino: "+op.getReferenciaTipoOperacion());
								System.out.println("caaj origen: "+op.getAperturaCaja().getId());
							}
							for (OperacionCaja op: listOperacionDestinoRetorno) {
								System.out.println("caja origen: "+op.getReferenciaTipoOperacion());
								System.out.println("caaj destino: "+op.getAperturaCaja().getId());
							}
						}else {
							System.out.println("ELSE ID: TRANSF");
							
						}
						
						
					
					
				}
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
		
		
	}
	@RequestMapping(method=RequestMethod.POST, value="/notaCredito/{nota}/{idVenta}")
	public ResponseEntity<?> guardarNotaCreditoOperracion(@RequestBody OperacionCaja entity, @PathVariable int nota, @PathVariable int idVenta){
		try {
			if(nota==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL NUMERO DENOTA CREDITO NO SE HA PODIDO CARGAR"), HttpStatus.CONFLICT);
			}else if(idVenta==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL NUMERO DE VENTA ACTUAL NO SE HA PODIDO CARGAR"), HttpStatus.CONFLICT);
			}else if(entity.getAperturaCaja().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR  LOS DATOS DE LA APERTURA CAJA"), HttpStatus.CONFLICT);
			}else if(entity.getConcepto().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR EL CONCEPTO DE LA OPERACION CAJA"), HttpStatus.CONFLICT);
			}else if(entity.getTipoOperacion().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE CARGAR EL TIPO DE OPERACION EN CAJA"), HttpStatus.CONFLICT);
			}else if(entity.getMonto()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO DEBE SER MAYOR A CERO"), HttpStatus.CONFLICT);
			}else {
				AperturaCaja cC = new AperturaCaja();
				cC=aperturaRepository.getAperturaCajaPorIdCaja(entity.getAperturaCaja().getId());
				if(cC==null) {
					System.out.println("entrooo null caja chiac");
					return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
				}else {
					if(entity.getConcepto().getId()==28) {
						if (entity.getTipoOperacion().getId() == 1) {
							aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
						}
						
						if (entity.getTipoOperacion().getId() == 2) {
							aperturaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getId(), entity.getMonto());
						}
						
						if (entity.getTipoOperacion().getId() == 3) {
							aperturaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
						}
						Concepto c= new Concepto();
						c= conceptoRepository.findById(entity.getConcepto().getId()).get();
						entity.setMotivo(c.getDescripcion()+" REF.: "+nota);
						entity.setTipo("ENTRADA");
					}
					if(entity.getConcepto().getId()==29) {
						if((cC.getSaldoActual()) < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
							return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((cC.getSaldoActualCheque()) < entity.getMonto()&& entity.getTipoOperacion().getId()==2){
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((cC.getSaldoActualTarjeta())< entity.getMonto() && entity.getTipoOperacion().getId()==3){
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else {
							if (entity.getTipoOperacion().getId() == 1) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(entity.getAperturaCaja().getId(), entity.getMonto());
							}
							if (entity.getTipoOperacion().getId() == 2) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(entity.getAperturaCaja().getId(), entity.getMonto());
							}
							if (entity.getTipoOperacion().getId() == 3) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
							}
							Concepto c= new Concepto();
							c= conceptoRepository.findById(entity.getConcepto().getId()).get();
							entity.setMotivo(c.getDescripcion()+" REF.: "+nota);
							entity.setTipo("SALIDA");
						}
					}
					notaCreditoRepository.findByActualizarEstadoNotaCredito(nota, idVenta, "CERRADO");
					entityRepository.save(entity);
					OperacionCaja op=  entityRepository.findTop1ByOrderByIdDesc();
					ventaRepositoty.findByActualizarVentaOperacion(idVenta, op.getId());
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  new  ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);

	}

	private ResponseEntity<CustomerErrorType> error(String mensaje) {
		return new ResponseEntity<>(new CustomerErrorType(mensaje), HttpStatus.CONFLICT);
	}
	private ResponseEntity<?> validarCajaSalida(List<OperacionCaja> lista) {

	    if (lista == null || lista.isEmpty()) {
	        return error("No existen formas de pago para validar caja");
	    }

	    // 🔹 Obtener apertura una sola vez (asumo misma caja)
	    AperturaCaja aper = aperturaRepository
	            .getAperturaCajaPorIdCaja(lista.get(0).getAperturaCaja().getId());

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
	        return error("NO HAY SALDO SUFICIENTE EN EFECTIVO");
	    }

	    if (totalCheque > aper.getSaldoActualCheque()) {
	        return error("NO HAY SALDO SUFICIENTE EN CHEQUE");
	    }

	    if (totalTarjeta > aper.getSaldoActualTarjeta()) {
	        return error("NO HAY SALDO SUFICIENTE EN TARJETA");
	    }

	    return null;
	}
	
	private ResponseEntity<?> validarCajaEntrada(List<OperacionCaja> operacionCajaLista) {
	      System.out.println("entro validacion de cajas ");

		    if (operacionCajaLista == null || operacionCajaLista.isEmpty()) {
		        return error("No existen formas de pago para validar caja");
		    }

		    for (OperacionCaja op : operacionCajaLista) {
		    	System.out.println("apertura : " +  op.getAperturaCaja().getId());
		        if (op.getAperturaCaja() == null ||
		            op.getAperturaCaja().getId() <= 0) {
		            return error("El funcionario no posee una apertura de caja asignada");
		        }

		        AperturaCaja aper = aperturaRepository.getAperturaCajaPorIdCaja(op.getAperturaCaja().getId()
		                );

		        if (aper == null) {
		            return error("EL FUNCIONARIO NO POSEE UNA APERTURA CAJA A SU NOMBRE");
		        }
		    }

		    return null;
		}
	
	@RequestMapping(method=RequestMethod.POST, value="/finalizarEmpaqueContado/{operacion}")
	public ResponseEntity<?> guardarFinalizacionEmpaque(@RequestBody List<OperacionCaja> listOperacionCaja, @PathVariable int operacion){
		 List<OperacionCaja> resultado = new ArrayList<>();
		if (operacion > 0) {
			ResponseEntity<?> validacionCaja = validarCajaEntrada(listOperacionCaja);
            if (validacionCaja != null) return validacionCaja;
			Venta vv = new Venta();
			vv=ventaRepositoty.getVentaPorCabeceraId(operacion);
			System.out.println("venta id: "+vv.getId());
			resultado = procesarOperacionCaja(vv, listOperacionCaja);
		}			
		return new ResponseEntity<Object>(resultado,HttpStatus.OK);
		//return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaAnulacionReservaciones(ReservacionAnulada ent, List<OperacionCaja> listaOperacion) {
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
	    cab.setReferenciaOperacion(ent.getReservacionCabecera().getId());
	    cab.setTipo("SALIDA");
	    cab.setMotivo(concepto.getDescripcion()+": "+ent.getReservacionCabecera().getDescripcionCombo() + " REF.: "+ent.getReservacionCabecera().getId());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
		AperturaCaja ape= aperturaRepository.getAperturaCajaPorIdCaja(listaOperacion.get(0).getAperturaCaja().getId());

	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : listaOperacion) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            ope.setTipo("SALIDA");
	    	    ope.setMotivo(concepto.getDescripcion()+": "+ent.getReservacionCabecera().getDescripcionCombo() + " REF.: "+ent.getReservacionCabecera().getId());
	            ope.setReferenciaOperacion(ent.getReservacionCabecera().getId());
	            ope.setFecha(new Date());
	            ope.setAperturaCaja(ape);
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = entityRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idApertura,monto);
		        }
		        
		        resultado.add(saveOperacion);
	    	}
	  //OperacionCaja saved = entityRepository.save(ope);
        if(cab.getConcepto().getId()==31) {
			reservacionRepositoty.findByActualizarReservacionOperacionEntrega(ent.getReservacionCabecera().getId(),savedCabecera.getId());
		}else if(cab.getConcepto().getId()==13) {
			reservacionRepositoty.findByActualizarReservacionOperacion(ent.getReservacionCabecera().getId(),savedCabecera.getId());
		}else if(cab.getConcepto().getId() == 30) {
			reservacionRepositoty.findByActualizaEstado(ent.getReservacionCabecera().getId(), "ANULADO");
			actualizarHabitacionDisponilidadReservacion(ent.getReservacionCabecera().getHabitacionesCategoriaCombo().getHabitaciones().getId(), false, false);
		}
        reservacionAnuladaRepositoty.save(ent);
 
	    return resultado;
	}
	private void actualizarHabitacionDisponilidadReservacion(int id, boolean dispo, boolean reser) {
		this.habitacionesRepository.findByActualizaEstadoDisponilidadReservacion(id, dispo, reser);
	}
	private void actualizarHabitacionPreReservacion(int id, boolean reser) {
		this.habitacionesRepository.findByActualizaEstadoPreReservacion(id, reser);
	}
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaReservaciones(ReservacionCabecera reservacion, List<OperacionCaja> listaOperacion) {
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
	    cab.setReferenciaOperacion(reservacion.getId());
	    cab.setTipo("ENTRADA");
	    cab.setMotivo(concepto.getDescripcion()+": "+reservacion.getDescripcionCombo() + " REF.: "+reservacion.getId());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
		AperturaCaja ape= aperturaRepository.getAperturaCajaPorIdCaja(listaOperacion.get(0).getAperturaCaja().getId());

	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : listaOperacion) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            ope.setTipo("ENTRADA");
	    	    ope.setMotivo(concepto.getDescripcion()+": "+reservacion.getDescripcionCombo() + " REF.: "+reservacion.getId());
	            ope.setReferenciaOperacion(reservacion.getId());
	            ope.setFecha(new Date());
	            ope.setAperturaCaja(ape);
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = entityRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            aperturaRepository.findByActualizarAperturaSaldo(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	aperturaRepository.findByActualizarAperturaSaldoCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	aperturaRepository.findByActualizarAperturaSaldoTarjeta(idApertura,monto);
		        }
		        
		        resultado.add(saveOperacion);
	    	}
	  //OperacionCaja saved = entityRepository.save(ope);
        if(cab.getConcepto().getId()==31) {
			reservacionRepositoty.findByActualizarReservacionOperacionEntrega(reservacion.getId(),savedCabecera.getId());
		}else if(cab.getConcepto().getId()==13) {
			reservacionRepositoty.findByActualizarReservacionOperacion(reservacion.getId(),savedCabecera.getId());
		}
	    //ent.setEstado("FACTURADO");
		//ventaRepositoty.findByActualizarVentaOperacion(idReservacion,savedCabecera.getId());
		//ventaRepositoty.findByActualizarFacturas(ent.getId(), "FACTURADO");
	 
	    
	    return resultado;
	}
	@Transactional
	public List<OperacionCaja> procesarOperacionCaja(Venta ent, List<OperacionCaja> listaOperacion) {
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
	    cab.setTipo("ENTRADA");
	    cab.setMotivo(concepto.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
		AperturaCaja ape= aperturaRepository.getAperturaCajaPorIdCaja(listaOperacion.get(0).getAperturaCaja().getId());

	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : listaOperacion) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            ope.setTipo("ENTRADA");
	            ope.setMotivo(concepto.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
	            ope.setReferenciaOperacion(ent.getId());
	            ope.setFecha(new Date());
	            ope.setAperturaCaja(ape);
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = entityRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            aperturaRepository.findByActualizarAperturaSaldo(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	aperturaRepository.findByActualizarAperturaSaldoCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	aperturaRepository.findByActualizarAperturaSaldoTarjeta(idApertura,monto);
		        }
		        //OperacionCaja saved = entityRepository.save(ope);

		        resultado.add(saveOperacion);
	    }
	    ent.setEstado("FACTURADO");
		ventaRepositoty.findByActualizarVentaOperacion(ent.getId(),savedCabecera.getId());
		ventaRepositoty.findByActualizarFacturas(ent.getId(), "FACTURADO");
	 
	    
	    return resultado;
	}
	@RequestMapping(method=RequestMethod.POST, value="/{operacion}")
	public ResponseEntity<?> guardar(@RequestBody OperacionCaja entity, @PathVariable int operacion){
		if (operacion == 0) {
			Venta v = new Venta();
			v=ventaRepositoty.getUltimaVenta();
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+" REF.: "+v.getId());
			entity.setTipo("ENTRADA");
		
			if (entity.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			
			entityRepository.save(entity);
			OperacionCaja op=  entityRepository.findTop1ByOrderByIdDesc();
			ventaRepositoty.findByActualizarVentaOperacion(v.getId(),op.getId());
			return new ResponseEntity<Object>(v,HttpStatus.OK);

		} else {
			
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setTipo("ENTRADA");
			if (entity.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			
			if (entity.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			entityRepository.save(entity);
			OperacionCaja op= new OperacionCaja();
			op=  entityRepository.findTop1ByOrderByIdDesc();
			System.out.println("id operacion si es igual a dos : "+ op.getId());
			Venta vv = new Venta();
			vv=ventaRepositoty.getVentaPorCabeceraId(operacion);
			System.out.println("vneta id: "+vv.getId());
			op.setMotivo(c.getDescripcion()+" REF.: "+vv.getId());
			entityRepository.save(op);
			ventaRepositoty.findByActualizarVentaOperacion(vv.getId(),op.getId());
			return new ResponseEntity<Object>(vv,HttpStatus.OK);

		}
		//return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value = "/test/{id}")
	public void test(@PathVariable int id){
		//ventaRepositoty.getVentaPorOperacionId(id);
	}


	
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaTransferenciaOrigen(List<OperacionCaja> opeOrigen) {
	    if (opeOrigen == null || opeOrigen.isEmpty()) {
	        throw new RuntimeException("No existen operaciones de caja para procesar");
	    }
	    List<OperacionCaja> resultado = new ArrayList<>();
	    OperacionCaja primera = opeOrigen.get(0);
	    Concepto concepto = conceptoRepository.findById(primera.getConcepto().getId())
	            .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
	    // 🔹 Crear cabecera
	    OperacionCajaCabecera cab = new OperacionCajaCabecera();
	    cab.setFecha(new Date());
	    cab.setMonto(opeOrigen.stream().mapToDouble(OperacionCaja::getMonto).sum());
	    cab.setReferenciaOperacion(primera.getReferenciaOperacion());
	    cab.setTipo("SALIDA");
	    cab.setMotivo(concepto.getDescripcion());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
		AperturaCaja ape= aperturaRepository.getAperturaCajaPorIdCaja(primera.getAperturaCaja().getId());

	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : opeOrigen) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            //ope.setTipo("ENTRADA");
	            //ope.setMotivo(concepto.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
	            //ope.setReferenciaOperacion(ent.getId());
	            //ope.setFecha(new Date());
	            ope.setAperturaCaja(ape);
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = entityRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idApertura,monto);
		        }
		        //OperacionCaja saved = entityRepository.save(ope);

		        resultado.add(saveOperacion);
	    }
//	    ent.setEstado("FACTURADO");
//		ventaRepositoty.findByActualizarVentaOperacion(ent.getId(),savedCabecera.getId());
//		ventaRepositoty.findByActualizarFacturas(ent.getId(), "FACTURADO");
//	 
//	    
	    return resultado;
	}
	
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaTransferenciaDestino(List<OperacionCaja> opeDestino) {
	    if (opeDestino == null || opeDestino.isEmpty()) {
	        throw new RuntimeException("No existen operaciones de caja para procesar");
	    }
	    List<OperacionCaja> resultado = new ArrayList<>();
	    OperacionCaja primera = opeDestino.get(0);
	    Concepto concepto = conceptoRepository.findById(primera.getConcepto().getId())
	            .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
	    // 🔹 Crear cabecera
	    OperacionCajaCabecera cab = new OperacionCajaCabecera();
	    cab.setFecha(new Date());
	    cab.setMonto(opeDestino.stream().mapToDouble(OperacionCaja::getMonto).sum());
	    cab.setReferenciaOperacion(primera.getReferenciaOperacion());
	    cab.setTipo("ENTRADA");
	    cab.setMotivo(concepto.getDescripcion());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
		AperturaCaja ape= aperturaRepository.getAperturaCajaPorIdCaja(primera.getAperturaCaja().getId());

	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : opeDestino) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            //ope.setTipo("ENTRADA");
	            //ope.setMotivo(concepto.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
	            //ope.setReferenciaOperacion(ent.getId());
	            //ope.setFecha(new Date());
	            ope.setAperturaCaja(ape);
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = entityRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            aperturaRepository.findByActualizarAperturaSaldo(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	aperturaRepository.findByActualizarAperturaSaldoCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	aperturaRepository.findByActualizarAperturaSaldoTarjeta(idApertura,monto);
		        }
		        //OperacionCaja saved = entityRepository.save(ope);

		        resultado.add(saveOperacion);
	    }
//	    ent.setEstado("FACTURADO");
//		ventaRepositoty.findByActualizarVentaOperacion(ent.getId(),savedCabecera.getId());
//		ventaRepositoty.findByActualizarFacturas(ent.getId(), "FACTURADO");
//	 
//	    
	    return resultado;
	}
	@RequestMapping(value="/resumenHistorico/rango/{fechaInicio}/{fechaFin}", method=RequestMethod.GET)
	public List<InformeHistoricoMovimiento>  resumenServicioRangoFecha(@PathVariable String fechaInicio, @PathVariable String fechaFin) throws IOException {
		List<InformeHistoricoMovimiento> detRetorno=new ArrayList<>();

		try {
			LocalDate inicio = LocalDate.parse(fechaInicio);
		    LocalDate fin    = LocalDate.parse(fechaFin);

		    LocalDateTime fechaInicioDT = inicio.atStartOfDay();          // 00:00:00
		    LocalDateTime fechaFinDT    = fin.atTime(23, 59, 59, 999_000_000);
			List<Object []> obb =entityRepository.getResumenHistoricoAperturaCaja(fechaInicioDT, fechaFinDT);

			for(Object[] ob: obb) {
				InformeHistoricoMovimiento d = new  InformeHistoricoMovimiento();
				d.setClienteNombre(ob[0].toString());
				d.setAperturaId(Integer.parseInt(ob[1].toString()));
				d.setFechaApertura(com.bisontecfacturacion.security.config.FechaUtil.convertirStrinfALocalDateTim(ob[2].toString()));
				d.setHoraApertura(ob[3].toString());
				d.setSaldoInicialEfe(Double.parseDouble(ob[4].toString()));
				d.setSaldoInicialChe(Double.parseDouble(ob[5].toString()));
				d.setSaldoInicialTar(Double.parseDouble(ob[6].toString()));
				
				d.setTotalEntradaEfe(Double.parseDouble(ob[7].toString()));
				d.setTotalEntradaChe(Double.parseDouble(ob[8].toString()));
				d.setTotalEntradaTar(Double.parseDouble(ob[9].toString()));
				
				d.setTotalSalidaEfe(Double.parseDouble(ob[10].toString()));
				d.setTotalSalidaChe(Double.parseDouble(ob[11].toString()));
				d.setTotalSalidaTar(Double.parseDouble(ob[12].toString()));
				
				d.setSaldoFinalEfe(Double.parseDouble(ob[13].toString()));
				d.setSaldoFinalChe(Double.parseDouble(ob[14].toString()));
				d.setSaldoFinalTar(Double.parseDouble(ob[15].toString()));
				detRetorno.add(d);
			}
			System.out.println("lista size: "+detRetorno.size());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  detRetorno;
	}
	

}
