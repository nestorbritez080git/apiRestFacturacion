package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.InformeBalanceReservacionAuxiliar;
import com.bisontecfacturacion.security.auxiliar.MovimientoPorConceptosAuxiliar;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.TransferenciaAperturaCaja;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
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

	@RequestMapping(method=RequestMethod.POST, value="/reservacion/{idReservacion}")
	public ResponseEntity<?>  guardarOperacionReservacion(@RequestBody OperacionCaja entity, @PathVariable int idReservacion){
		if(idReservacion==0) {
			ReservacionCabecera v = new ReservacionCabecera();
			v=reservacionRepositoty.getUltimaReservacion();
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
			reservacionRepositoty.findByActualizarReservacionOperacionEntrega(v.getId(),op.getId());
			
			
		}else {
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
			ReservacionCabecera vv = new ReservacionCabecera();
			vv=reservacionRepositoty.getOne(idReservacion);
			System.out.println("vneta id: "+vv.getId());
			op.setMotivo(c.getDescripcion()+" REF.: "+vv.getId());
			entityRepository.save(op);
			reservacionRepositoty.findByActualizarReservacionOperacion(vv.getId(),op.getId());
			
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
	@RequestMapping(method=RequestMethod.POST, value="/guardarTransferenciaAperturaCajaActivo/{operacion}")
	public ResponseEntity<?> guardarTransferenciaAperturaActivo(@RequestBody OperacionCaja entity, @PathVariable int operacion){
		try {
			if(entity.getAperturaCaja().getId() == 0) {
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
					if((cC.getSaldoActual()) < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
						return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if((cC.getSaldoActualCheque()) < entity.getMonto()&& entity.getTipoOperacion().getId()==2){
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if((cC.getSaldoActualTarjeta())< entity.getMonto() && entity.getTipoOperacion().getId()==3){
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else {
						if(operacion ==0) {
							System.out.println("ENTRO IFFF");
							entityRepository.save(entity);
							OperacionCaja operacionActual= new OperacionCaja();
							operacionActual=entityRepository.findTop1ByOrderByIdDesc();
							
							Concepto c= new Concepto();
							c= conceptoRepository.findById(23).get();
							operacionActual.setMotivo(c.getDescripcion()+" REF.: "+operacionActual.getId());
							operacionActual.setTipo("SALIDA");
							TransferenciaAperturaCaja t = new TransferenciaAperturaCaja();
							if(entity.getTipoOperacion().getId() == 1) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(entity.getAperturaCaja().getId(), entity.getMonto());
								aperturaRepository.findByActualizarAperturaSaldo(entity.getAperturaCaja().getFuncionario().getId(), entity.getMonto());
								OperacionCaja opeAux= new OperacionCaja();
								opeAux.getAperturaCaja().setId(entity.getAperturaCaja().getFuncionario().getId());
								opeAux.getConcepto().setId(23);
								opeAux.getTipoOperacion().setId(1);
								opeAux.setTipo("ENTRADA");
								opeAux.setMonto(entity.getMonto());
								entityRepository.save(opeAux);
								OperacionCaja opeAuxActualizar=entityRepository.findTop1ByOrderByIdDesc();
								opeAuxActualizar.setMotivo(c.getDescripcion()+" REF.: "+opeAuxActualizar.getId());
								entityRepository.save(opeAuxActualizar);
								t.setReferencia(entity.getReferenciaTipoOperacion());
								t.setMonto(entity.getMonto());
							}
							if (entity.getTipoOperacion().getId() == 2) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(entity.getAperturaCaja().getId(), entity.getMonto());
								aperturaRepository.findByActualizarAperturaSaldoCheque(entity.getAperturaCaja().getFuncionario().getId(), entity.getMonto());
								OperacionCaja opeAux= new OperacionCaja();
								opeAux.getAperturaCaja().setId(entity.getAperturaCaja().getFuncionario().getId());
								opeAux.getConcepto().setId(23);
								opeAux.getTipoOperacion().setId(2);
								opeAux.setTipo("ENTRADA");
								opeAux.setMonto(entity.getMonto());
								entityRepository.save(opeAux);
								OperacionCaja opeAuxActualizar=entityRepository.findTop1ByOrderByIdDesc();
								opeAuxActualizar.setMotivo(c.getDescripcion()+" REF.: "+opeAuxActualizar.getId());
								entityRepository.save(opeAuxActualizar);
								t.setReferencia(entity.getReferenciaTipoOperacion());
								t.setMontoCheque(entity.getMonto());
							}
							if (entity.getTipoOperacion().getId() == 3) {
								aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(entity.getAperturaCaja().getId(), entity.getMonto());
								aperturaRepository.findByActualizarAperturaSaldoTarjeta(entity.getAperturaCaja().getFuncionario().getId(), entity.getMonto());
								OperacionCaja opeAux= new OperacionCaja();
								opeAux.getAperturaCaja().setId(entity.getAperturaCaja().getFuncionario().getId());
								opeAux.getConcepto().setId(23);
								opeAux.getTipoOperacion().setId(3);
								opeAux.setTipo("ENTRADA");
								opeAux.setMonto(entity.getMonto());
								entityRepository.save(opeAux);
								OperacionCaja opeAuxActualizar=entityRepository.findTop1ByOrderByIdDesc();
								opeAuxActualizar.setMotivo(c.getDescripcion()+" REF.: "+opeAuxActualizar.getId());
								entityRepository.save(opeAuxActualizar);
								t.setReferencia(entity.getReferenciaTipoOperacion());
								t.setMontoTarjeta(entity.getMonto());
							}
							System.out.println(entity.getAperturaCaja().getFuncionario().getId()+" ID APERTURA DESTINO");
							t.getAperturaCajaDestino().setId(entity.getAperturaCaja().getFuncionario().getId());//RESIVO DESDE EL CLIENTE LA APERTURA DESDTINO
							t.getAperturaCajaOrigen().setId(entity.getAperturaCaja().getId());
							t.getConcepto().setId(23);
							t.getFuncionario().setId(entity.getAperturaCaja().getFuncionario().getPersona().getId());//RECIBO DESDE EL CLIENTE EL FUNCIONARIO
							t.getOperacionCaja().setId(operacionActual.getId());//relacionamiento de la operacion en caja
							transferenciaAperturaCajaRepository.save(t);
							
							
						}else {
							System.out.println("ELSE ID: TRANSF");
							
						}
						
						
					}
					
				}
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
		
		
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
			vv=ventaRepositoty.getVentaPorOperacionId(operacion);
			System.out.println("vneta id: "+vv.getId());
			op.setMotivo(c.getDescripcion()+" REF.: "+vv.getId());
			entityRepository.save(op);
			ventaRepositoty.findByActualizarVentaOperacion(vv.getId(),op.getId());
			

		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value = "/test/{id}")
	public void test(@PathVariable int id){
		ventaRepositoty.getVentaPorOperacionId(id);
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
