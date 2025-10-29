package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;
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
	private NotaCreditoRepository notaCreditoRepository;
	@Autowired
	private VentaRepository ventaRepositoty;
	@Autowired
	private ReservacionCabeceraRepository reservacionRepositoty;
	
	@Autowired
	private AperturaCajaRepository aperturaRepository;
	
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
	@RequestMapping(method=RequestMethod.POST, value="/reservacion/anulacion/{idReservacion}")
	public ResponseEntity<?>  guardarOperacionReservacionAnulacion(@RequestBody OperacionCaja entity, @PathVariable int idReservacion){
		if(idReservacion==0) {
			ReservacionCabecera v = new ReservacionCabecera();
			v=reservacionRepositoty.getUltimaReservacion();
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
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
			OperacionCaja op=  entityRepository.findTop1ByOrderByIdDesc();
			//reservacionRepositoty.findByActualizarReservacionOperacionEntrega(v.getId(),op.getId());
			
			
		}else {
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
			
		}
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.POST, value="/reservacion/{idReservacion}")
	public ResponseEntity<?>  guardarOperacionReservacion(@RequestBody OperacionCaja entity, @PathVariable int idReservacion){
		if(idReservacion==0) {
			System.out.println("entroo id recepcion 0 nueva recepcion");
			ReservacionCabecera v = new ReservacionCabecera();
			v=reservacionRepositoty.getUltimaReservacion();
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
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
			
			OperacionCaja opp = new OperacionCaja();
			opp = entityRepository.save(entity);
			if(entity.getConcepto().getId()==31) {
				reservacionRepositoty.findByActualizarReservacionOperacionEntrega(v.getId(),opp.getId());
			}else if(entity.getConcepto().getId()==13) {
				reservacionRepositoty.findByActualizarReservacionOperacion(v.getId(),opp.getId());
			}
			//reservacionRepositoty.findByActualizarReservacionOperacionEntrega(v.getId(),opp.getId());
			
			
		}else {
			ReservacionCabecera v = new ReservacionCabecera();
			
			v = reservacionRepositoty.getOne(idReservacion);
			Concepto c= new Concepto();
			c= conceptoRepository.findById(entity.getConcepto().getId()).get();
			entity.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
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
			
			OperacionCaja opp= new OperacionCaja();
			opp = entityRepository.save(entity);
			//op=  entityRepository.findTop1ByOrderByIdDesc();
			System.out.println("id operacion si es igual a dos : "+ opp.getId());
			opp.setMotivo(c.getDescripcion()+": "+v.getDescripcionCombo()+ " Ref.:"+v.getId());
			entityRepository.save(opp);
			if(entity.getConcepto().getId()==31) {
				reservacionRepositoty.findByActualizarReservacionOperacionEntrega(v.getId(),opp.getId());
			}else if(entity.getConcepto().getId()==13) {
				reservacionRepositoty.findByActualizarReservacionOperacion(v.getId(),opp.getId());
			}
			
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
								opOrigenEfectivo.setReferenciaTipoOperacion("");
								entityRepository.save(opOrigenEfectivo);
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(cOrigen.getId(), monto);

								OperacionCaja opDestinoEfectivo= new OperacionCaja();
								opDestinoEfectivo.getAperturaCaja().setId(cDestino.getId());
								opDestinoEfectivo.getConcepto().setId(23);
								opDestinoEfectivo.setMonto(monto);
								opDestinoEfectivo.setFecha(new Date());
								opDestinoEfectivo.setTipo("ENTRADA");
								opDestinoEfectivo.getTipoOperacion().setId(1);//efectivo
								opDestinoEfectivo.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoEfectivo.setReferenciaTipoOperacion("");
								entityRepository.save(opDestinoEfectivo);
								aperturaRepository.findByActualizarAperturaSaldo(cDestino.getId(), monto);
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
								opOrigenCheque.setReferenciaTipoOperacion("");
								entityRepository.save(opOrigenCheque);
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(cOrigen.getId(), montoCheque);

								OperacionCaja opDestinoCheque= new OperacionCaja();
								opDestinoCheque.getAperturaCaja().setId(cDestino.getId());
								opDestinoCheque.getConcepto().setId(23);
								opDestinoCheque.setMonto(montoCheque);
								opDestinoCheque.setFecha(new Date());
								opDestinoCheque.setTipo("ENTRADA");
								opDestinoCheque.getTipoOperacion().setId(2);//efectivo
								opDestinoCheque.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoCheque.setReferenciaTipoOperacion("");
								entityRepository.save(opDestinoCheque);
								aperturaRepository.findByActualizarAperturaSaldoCheque(cDestino.getId(), montoCheque);
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
								opOrigenTarjeta.setReferenciaTipoOperacion("");
								entityRepository.save(opOrigenTarjeta);
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(cOrigen.getId(), montoTarjeta);

								OperacionCaja opDestinoTarjeta= new OperacionCaja();
								opDestinoTarjeta.getAperturaCaja().setId(cDestino.getId());
								opDestinoTarjeta.getConcepto().setId(23);
								opDestinoTarjeta.setMonto(montoTarjeta);
								opDestinoTarjeta.setFecha(new Date());
								opDestinoTarjeta.setTipo("ENTRADA");
								opDestinoTarjeta.getTipoOperacion().setId(3);//efectivo
								opDestinoTarjeta.setMotivo(c.getDescripcion()+" REF. ORIGEN(#): "+cOrigen.getId()+ ", CAJERO/A: " +cOrigen.getFuncionario().getPersona().getNombre()+ " "+cOrigen.getFuncionario().getPersona().getApellido());
								opDestinoTarjeta.setReferenciaTipoOperacion("");
								entityRepository.save(opDestinoTarjeta);
								aperturaRepository.findByActualizarAperturaSaldoTarjeta(cDestino.getId(), montoTarjeta);
				
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
	@RequestMapping(method=RequestMethod.POST, value="/finalizarEmpaqueContado/{operacion}")
	public ResponseEntity<?> guardarFinalizacionEmpaque(@RequestBody OperacionCaja entity, @PathVariable int operacion){
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
			op.setMotivo(c.getDescripcion()+" POR EMPAQUE REF.: "+vv.getId());
			entityRepository.save(op);
			vv.setEstado("FACTURADO");
			ventaRepositoty.findByActualizarVentaOperacion(vv.getId(),op.getId());
			ventaRepositoty.findByActualizarFacturas(vv.getId(), "FACTURADO");
			return new ResponseEntity<Object>(vv,HttpStatus.OK);

		}
		//return  new  ResponseEntity<String>(HttpStatus.CREATED);
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


/*
	
	@Async("guardar")
	public void  imprimir(int id) {
	// id=53;
		Org org = new Org();
		org=orgRepository.findOne(1);
		Impresora impre=new Impresora();
		impre=impreRepository.findTop1ByOrderByIdAsc();
		String tipo = impre.getDescripcion();
		List<Venta> venta = new ArrayList<>();

			venta =ventaRepository.findById(id);
			OperacionCaja op= new OperacionCaja();
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
				
				map.put("cajero", op.getAperturaCaja().getFuncionario().getUser().getUsername());
				map.put("TipoOperacion", op.getTipoOperacion().getDescripcion());
				map.put("vuelto", op.getVuelto());
				map.put("efectivo", op.getEfectivo());
				map.put("motivo", op.getMotivo());

				map.put("totalProducto", totalProducto);
				map.put("totalServicio", totalServicio);
				map.put("org", org.getNombre());
				map.put("telefono", org.getTelefono());
				map.put("ruc", org.getRuc());
				map.put("direccion", org.getDireccion());
				map.put("total", total);
				Reporte report=new Reporte();
				try {
					report.report(venta, map, "TicketVentaContadoReport", tipo);
				} catch (Exception e) {
					
				}
	
			}
		
	}
	*/
	
	

}
