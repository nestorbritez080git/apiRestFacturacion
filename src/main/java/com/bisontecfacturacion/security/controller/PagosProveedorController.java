package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.CuentaProveedor;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.PagosProveedor;
import com.bisontecfacturacion.security.model.PagosProveedorCabecera;
import com.bisontecfacturacion.security.model.PagosProveedorCompra;
import com.bisontecfacturacion.security.model.PagosProveedoresReferenciaOperacionCaja;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarCabeceraRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarDetalleRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.PagoProveedorRepository;
import com.bisontecfacturacion.security.repository.PagosProveedorCompraRepository;
import com.bisontecfacturacion.security.repository.PagosProveedorReferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.PagosProveedorReferenciaOperacionCajaRepository;
import com.bisontecfacturacion.security.repository.PagosProveedoresCabeceraRepository;
import com.bisontecfacturacion.security.repository.TransferenciaPagosProveedorRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;


@Transactional
@RestController
@RequestMapping("pagosProveedor")
public class PagosProveedorController {

	@Autowired
	private PagoProveedorRepository entityRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private CajaMayorRepository cajaMayorRepository;

	@Autowired
	private PagosProveedorCompraRepository  pagosProveedorCompraRepository;
	
	@Autowired
	private PagosProveedoresCabeceraRepository pagosProveedorCabeceraRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	
	@Autowired
	private PagosProveedorReferenciaCajaChicaRepository pagosProveedorReferenciaCajaChicaRepository;
	
	
	@Autowired
	private PagosProveedorReferenciaOperacionCajaRepository pagosProveedorReferenciaOperacionCajaRepository;
	
	
	
	@Autowired
	private TransferenciaPagosProveedorRepository  transferenciaPagosRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private AperturaCajaRepository aperturaRepository;
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@Autowired
	private CuentaPagarCabeceraRepository cuentaPagarRepository;


	@Autowired
	private CuentaPagarDetalleRepository cuentaPagarDetalleRepository;


	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?>  save(@RequestBody PagosProveedor entity ){
		/*
		entity.getCajaMayor().setId(1);
		Concepto conc= conceptoRepository.consultarIDConcepto(12);
		CajaMayor caj= cajaMayorRepository.consultarIDCajaMayor(1);
		if(conc==null) {
			return new ResponseEntity<>(new CustomerErrorType("EL NUMERO DE CONCEPTO <12> NO ESTÁ PRESENTE EN LA BASE DE DATOS"), HttpStatus.CONFLICT);
		}else {entity.getConcepto().setId(conc.getId());}
		if(caj==null) {
			return new ResponseEntity<>(new CustomerErrorType("EL NUMERO DE LA CAJA MAYOR <1> NO ESTÁ PRESENTE EN LA BASE DE DATOS"), HttpStatus.CONFLICT);
		}else {entity.getCajaMayor().setId(caj.getId());}
		
		try {
			if (entity.getFuncionarioR().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioA().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENCARGADO NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if(entity.getCuentaCabecera().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("LA CUENTA CABECERA NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if (caj.getMonto() < entity.getImporte() &&  entity.getTipoOperacion().getId()==1) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA MAYOR SUPERA EL MONTO A PAGAR A PROVEEDOR!"), HttpStatus.CONFLICT);
			}else if(caj.getMontoCheque() < entity.getImporte() &&  entity.getTipoOperacion().getId()==2) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA MAYOR SUPERA EL MONTO A PAGAR A PROVEEDOR!"), HttpStatus.CONFLICT);
			}else if (caj.getMontoTarjeta() < entity.getImporte() && entity.getTipoOperacion().getId()==3) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA MAYOR SUPERA EL MONTO A PAGAR A PROVEEDOR!"), HttpStatus.CONFLICT);
			}else {
//				Verifica la cabecera si importe es mayor al saldo a pagar entonces directamente se cancela
//				/*
				
				CuentaPagarCabecera cuen=cuentaPagarRepository.getOne(entity.getCuentaCabecera().getId());
				double pagadoCabecera=0, saldoCabecera=0;
				System.out.println("monto importe: "+entity.getImporte()+ ", monto saldo cuenta: "+cuen.getSaldo());
				if(entity.getImporte()>=cuen.getSaldo()) {
					pagadoCabecera=cuen.getTotal();
					saldoCabecera=0;
					cuen.setPagado(pagadoCabecera);
					cuen.setSaldo(0.0);
				}else {
					pagadoCabecera=cuen.getPagado() + entity.getImporte();
					saldoCabecera=cuen.getTotal()-pagadoCabecera;
					cuen.setPagado(pagadoCabecera);
					cuen.setSaldo(saldoCabecera);
				}
				List<CuentaPagarDetalle> detalleCuenta = cuentaPagarDetalleRepository.getDetalleXIdCabecera(entity.getCuentaCabecera().getId());
				double restoPositivo=entity.getImporte();
				System.out.println("total envio monto:"+ entity.getImporte());
				//		int index=0; index < det.size()  && saldoPositivoaCobrar > 0; index++
				for (int index=0; index < detalleCuenta.size() && restoPositivo > 0; index++) {
					CuentaPagarDetalle cueDet= detalleCuenta.get(index);
					System.out.println("cuenta deta id: "+cueDet.getId());
					double pagadoDet=0;
					if (restoPositivo>=(cueDet.getMonto()-cueDet.getImporte())) {
						System.out.println("Detalle cuenta cancelar");
						pagadoDet = cueDet.getMonto() - cueDet.getImporte();
						restoPositivo = restoPositivo - (cueDet.getMonto() - cueDet.getImporte());
						//System.out.println("Monto Cobrado Actualizado:  "+pagadoDet);
						//System.out.println("Monto total en cancelacion: "+(cueDet.getImporte()+pagadoDet));
						cueDet.setImporte((cueDet.getImporte()+pagadoDet));
						//System.out.println("Resto Psitivo: "+restoPositivo);
					}else {
						pagadoDet =  restoPositivo;
						restoPositivo = restoPositivo - restoPositivo;
						cueDet.setImporte((cueDet.getImporte()+pagadoDet));
					}
					cuentaPagarDetalleRepository.save(cueDet);
				}
					entityRepository.save(entity);
					
					PagosProveedor id = entityRepository.getUltimoPagos();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					TransferenciaPagosProveedor tgasto= new TransferenciaPagosProveedor();
					tgasto.getCajaMayor().setId(caj.getId());
					tgasto.getFuncionario().setId(entity.getFuncionarioR().getId());
					tgasto.getPagosProveedor().setId(idVent);
					tgasto.setReferencia(entity.getReferencia());
					tgasto.setFecha(new Date());
					if (entity.getTipoOperacion().getId()==1) {
						tgasto.setMonto(entity.getImporte());
					}	
					if (entity.getTipoOperacion().getId()==2) {
						tgasto.setMontoCheque(entity.getImporte());
					}	
					if (entity.getTipoOperacion().getId()==3) {
						tgasto.setMontoTarjeta(entity.getImporte());
					}
					cajaMayorRepository.findByActualizaCajaMayorNegativo(1, tgasto.getMonto(), tgasto.getMontoCheque(), tgasto.getMontoTarjeta());
					transferenciaPagosRepository.save(tgasto);
					
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@RequestMapping(method = RequestMethod.POST, value = "/pagarPorCuenta/{idCuenta}")
	public ResponseEntity<?> savePagosPorCuentas(@RequestBody PagosProveedorCabecera entity, @PathVariable int idCuenta ) {
		Integer apertura=0, cajaChica=0;
		if(entity.getTipo().equals("T-A")){
			apertura=entity.getConcepto().getId();
			AperturaCaja XX = new AperturaCaja();
			XX=aperturaCajaRepository.getAperturaCajaPorIdCaja(apertura);
			if(XX==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
			}else {
				if((XX.getSaldoActual()) < entity.getTotal() && entity.getTipoOperacion().getId()==1) {
					System.out.println("entrooo monto superaod efe");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((XX.getSaldoActualCheque()) < entity.getTotal()&& entity.getTipoOperacion().getId()==2){
					System.out.println("entrooo monto superaod che");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((XX.getSaldoActualTarjeta())< entity.getTotal() && entity.getTipoOperacion().getId()==3){
					System.out.println("entrooo monto superaod tarj");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else{
					entity.getConcepto().setId(12);
					entity.setFechaRegistro(new Date());
					pagosProveedorCabeceraRepository.save(entity);
					PagosProveedorCabecera pagosActualizar = pagosProveedorCabeceraRepository.findTop1ByOrderByIdDesc();
					OperacionCaja op = new OperacionCaja();
					op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
					op.getAperturaCaja().setId(apertura);
					op.getConcepto().setId(12);
					op.setEfectivo(0.0);
					op.setVuelto(0.0);
					op.setMonto(entity.getTotal());
					op.setReferenciaTipoOperacion(entity.getReferencia());
					op.setTipo("SALIDA");
					Concepto c = new Concepto();
					c = conceptoRepository.findById(12).get();
					op.setMotivo(c.getDescripcion() + " REF.: " + pagosActualizar.getId());
					operacionCajaRepository.save(op);
					if (op.getTipoOperacion().getId() == 1) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(op.getAperturaCaja().getId(), entity.getTotal());
					}
					if (op.getTipoOperacion().getId() == 2) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(op.getAperturaCaja().getId(), entity.getTotal());
					}
					if (op.getTipoOperacion().getId() == 3) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(op.getAperturaCaja().getId(), entity.getTotal());
					}
					OperacionCaja operacioActualziar = operacionCajaRepository.findTop1ByOrderByIdDesc();
					PagosProveedoresReferenciaOperacionCaja rfPagos=new PagosProveedoresReferenciaOperacionCaja();
					rfPagos.getPagosProveedorCabecera().setId(pagosActualizar.getId());
					rfPagos.getOperacionCaja().setId(operacioActualziar.getId());
					pagosProveedorReferenciaOperacionCajaRepository.save(rfPagos);
					operacionPorCuenta(idCuenta, entity.getTotal(), pagosActualizar.getId(), entity.getFuncionarioR().getId());
					new ResponseEntity<>(HttpStatus.CREATED);
				}
			}
		}else {
			 new ResponseEntity<>(new CustomerErrorType("ESTE OPERACIÒN NO TIENE SOPORTE PARA ANULACIÒN ANTICIPO TIPO OPERACIÒN T-A"), HttpStatus.CONFLICT);
		}
		 return new ResponseEntity<String>(HttpStatus.OK);

	}
	
	public List<Object[][]> operacionPorCuenta(int idCuenta, Double monto, int idCobrosCabecera, int idFun) {
		CuentaPagarCabecera cue = cuentaPagarRepository.getOne(idCuenta);
		Double cobradoActual = 0.0;
		if ((monto - cobradoActual) >= cue.getSaldo()) {
			System.out.println("Entro en 1*******************************");
			PagosProveedor pagos = new PagosProveedor();
			pagos.getPagosProveedorCabecera().setId(idCobrosCabecera);
			pagos.getCuentaPagarCabecera().setId(cue.getId());
			pagos.setImporte(cue.getSaldo());
			cobradoActual = cobradoActual + cue.getSaldo();
			pagos.getFuncionario().setId(idFun);
			entityRepository.save(pagos);
			cuentaPagarDetalleRepository.liquidarDetalleCuentaProveedor(cue.getId(), new Date(), true);
			cuentaPagarRepository.findByActualizarPagadoCuentaProveedor(pagos.getCuentaPagarCabecera().getId(), cue.getSaldo());
		
			
		}else {
			Double saldoPositivoaCobrar = 0.0;
			Double montoCuotaActualizados = 0.0;
			Double montoIMporteActualizados = 0.0;
			if ((monto - cobradoActual) > 0) {
				saldoPositivoaCobrar = monto - cobradoActual;
				PagosProveedor pagos = new PagosProveedor();
				pagos.getPagosProveedorCabecera().setId(idCobrosCabecera);
				pagos.getCuentaPagarCabecera().setId(cue.getId());
				pagos.setImporte(saldoPositivoaCobrar);
				// cobradoActual = cobradoActual + cue.getSaldo();
				pagos.getFuncionario().setId(idFun);
				entityRepository.save(pagos);
				cuentaPagarRepository.findByActualizarPagadoCuentaProveedor(pagos.getCuentaPagarCabecera().getId(),	saldoPositivoaCobrar);
				
				List<Object[]> det = cuentaPagarDetalleRepository.consultarDetalleCuentaPorIdCabecera(cue.getId());
				List<CuentaPagarDetalle> listadoDetalleActualizados = new ArrayList<CuentaPagarDetalle>();
//				 cuenta_pagar_cabecera.fraccion_cuota,  cuenta_pagar_detalle.numero_cuota,  cuenta_pagar_detalle.fecha_vencimiento,  cuenta_pagar_detalle.monto, cuenta_pagar_detalle.importe,  cuenta_pagar_detalle.id, cuenta_pagar_detalle.sub_total, cuenta_pagar_detalle.cuenta_pagar_cabecera_id from cuenta_pagar_detalle inner join cuenta_pagar_cabecera on cuenta_pagar_cabecera.id=cuenta_pagar_detalle.cuenta_pagar_cabecera_id  where cuenta_pagar_detalle.cuenta_pagar_cabecera_id=:idCabecera order by cuenta_pagar_detalle.numero_cuota ASC", nativeQuery = true)
//				cuenta_cobrar_cabecera.fraccion_cuota, cuenta_cobrar_detalle.numero_cuota, cuenta_cobrar_detalle.fecha_vencimiento, cuenta_cobrar_detalle.monto, cuenta_cobrar_detalle.importe, cuenta_cobrar_detalle.interes_mora, cuenta_cobrar_detalle.id, cuenta_cobrar_detalle.sub_total, cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id from cuenta_cobrar_detalle inner join cuenta_cobrar_cabecera on cuenta_cobrar_cabecera.id=cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id  where cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id=:idCabecera order by cuenta_cobrar_detalle.numero_cuota ASC ", nativeQuery = true)

				for (int index = 0; index < det.size() && saldoPositivoaCobrar > 0; index++) {
					Object[] ob = det.get(index);
					montoCuotaActualizados = Double.parseDouble(ob[6].toString()) - Double.parseDouble(ob[4].toString());
		
					if (saldoPositivoaCobrar >= montoCuotaActualizados) {
						saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
						montoIMporteActualizados = montoCuotaActualizados;
						cobradoActual = cobradoActual + montoIMporteActualizados;
					} else {
						montoIMporteActualizados = saldoPositivoaCobrar;
						saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
						cobradoActual = cobradoActual + montoIMporteActualizados;
					}
					CuentaPagarDetalle detalleCuenta = new CuentaPagarDetalle();
					detalleCuenta.getCuentaPagarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
					detalleCuenta.setNumeroCuota(Integer.parseInt(ob[1].toString()));
					if (ob[2].toString() == null) {
						detalleCuenta.setFechaVencimiento(null);
					} else {
						detalleCuenta.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
					}
					detalleCuenta.setMonto(Double.parseDouble(ob[3].toString()));
					detalleCuenta.setImporte(Double.parseDouble(ob[4].toString())+ montoIMporteActualizados);
					detalleCuenta.setId(Integer.parseInt(ob[5].toString()));
					detalleCuenta.setSubTotal(Double.parseDouble(ob[6].toString()));
					detalleCuenta.getCuentaPagarCabecera().setId(Integer.parseInt(ob[7].toString()));
					listadoDetalleActualizados.add(detalleCuenta);
				}
				cuentaPagarDetalleRepository.saveAll(listadoDetalleActualizados);
			}
		}
		return null;
	}

		
	@RequestMapping(method = RequestMethod.POST, value = "/pagarPorProveedor")
	public ResponseEntity<?> savePagosPorProveedor(@RequestBody PagosProveedorCabecera entity ) {
		Integer apertura=0, cajaChica=0;
		if(entity.getTipo().equals("T-A")){
			apertura=entity.getConcepto().getId();
			AperturaCaja XX = new AperturaCaja();
			XX=aperturaCajaRepository.getAperturaCajaPorIdCaja(apertura);
			if(XX==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
			}else {
				if((XX.getSaldoActual()) < entity.getTotal() && entity.getTipoOperacion().getId()==1) {
					System.out.println("entrooo monto superaod efe");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((XX.getSaldoActualCheque()) < entity.getTotal()&& entity.getTipoOperacion().getId()==2){
					System.out.println("entrooo monto superaod che");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else if((XX.getSaldoActualTarjeta())< entity.getTotal() && entity.getTipoOperacion().getId()==3){
					System.out.println("entrooo monto superaod tarj");
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
				}else{
					entity.getConcepto().setId(12);
					entity.setFechaRegistro(new Date());
					pagosProveedorCabeceraRepository.save(entity);
					PagosProveedorCabecera pagosActualizar = pagosProveedorCabeceraRepository.findTop1ByOrderByIdDesc();
					OperacionCaja op = new OperacionCaja();
					op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
					op.getAperturaCaja().setId(apertura);
					op.getConcepto().setId(12);
					op.setEfectivo(0.0);
					op.setVuelto(0.0);
					op.setMonto(entity.getTotal());
					op.setReferenciaTipoOperacion(entity.getReferencia());
					op.setTipo("SALIDA");
					
					Concepto c = new Concepto();
					c = conceptoRepository.findById(12).get();
					op.setMotivo(c.getDescripcion() + " REF.: " + pagosActualizar.getId());
					operacionCajaRepository.save(op);
					if (op.getTipoOperacion().getId() == 1) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(op.getAperturaCaja().getId(), entity.getTotal());
					}
					if (op.getTipoOperacion().getId() == 2) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(op.getAperturaCaja().getId(), entity.getTotal());
					}
					if (op.getTipoOperacion().getId() == 3) {
						aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(op.getAperturaCaja().getId(), entity.getTotal());

					}
					OperacionCaja operacioActualziar = operacionCajaRepository.findTop1ByOrderByIdDesc();
					PagosProveedoresReferenciaOperacionCaja rfPagos=new PagosProveedoresReferenciaOperacionCaja();
					rfPagos.getPagosProveedorCabecera().setId(pagosActualizar.getId());
					rfPagos.getOperacionCaja().setId(operacioActualziar.getId());
					pagosProveedorReferenciaOperacionCajaRepository.save(rfPagos);
					System.out.println("Entro en cero^^^^^^^^^^^^^^^^^^^^");
					List<CuentaPagarCabecera> cuentaCabecera = cuentaPagarRepository.findByCuentaPorIdACobrars(entity.getProveedor().getId());
					operacionPorPooveedor(cuentaCabecera, entity.getTotal(), pagosActualizar.getId(), entity.getFuncionarioR().getId());
				}
			}
		}else {
			return new ResponseEntity<>(new CustomerErrorType("TIPO CONFIGURACION DE PAGOS PROVEEDOR NO APLICABLE DESDE LA CAJA CHICAS!"), HttpStatus.CONFLICT);

		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	public List<Object[][]> operacionPorPooveedor(List<CuentaPagarCabecera> cuentas, Double monto, int idCobrosCabecera, int idFun) {
		String tipoPago="";
		Object[][] obResult = new Object[cuentas.size()][12];
		int contador = 0;
		List<Object[][]> listRes = new ArrayList<>();
		Double cobradoActual = 0.0;
		for (int i = 0; i < cuentas.size(); i++) {
			CuentaPagarCabecera cue = cuentas.get(i);
			System.out.println("cue.getCliente().getId(): veeveve "+cue.getCompra().getId());
			
			
				if ((monto - cobradoActual) >= cue.getSaldo()) {
					System.out.println("Entro en 1*******************************");
				
					PagosProveedor pagos = new PagosProveedor();
					pagos.getPagosProveedorCabecera().setId(idCobrosCabecera);
					pagos.getCuentaPagarCabecera().setId(cue.getId());
					pagos.setImporte(cue.getSaldo());
					cobradoActual = cobradoActual + cue.getSaldo();
					pagos.getFuncionario().setId(idFun);
					entityRepository.save(pagos);
					cuentaPagarDetalleRepository.liquidarDetalleCuentaProveedor(cue.getId(), new Date(), true);
					cuentaPagarRepository.findByActualizarPagadoCuentaProveedor(pagos.getCuentaPagarCabecera().getId(), cue.getSaldo());
				
					
				} else {
					System.out.println("Entro en 2*******************************");
					Double saldoPositivoaCobrar = 0.0;
					Double montoCuotaActualizados = 0.0;
					Double montoIMporteActualizados = 0.0;
					if ((monto - cobradoActual) > 0) {
						saldoPositivoaCobrar = monto - cobradoActual;
						PagosProveedor pagos = new PagosProveedor();
						pagos.getPagosProveedorCabecera().setId(idCobrosCabecera);
						pagos.getCuentaPagarCabecera().setId(cue.getId());
						pagos.setImporte(saldoPositivoaCobrar);
						// cobradoActual = cobradoActual + cue.getSaldo();
						pagos.getFuncionario().setId(idFun);
						entityRepository.save(pagos);
						PagosProveedor pagAct = entityRepository.findTop1ByOrderByIdDesc();
						cuentaPagarRepository.findByActualizarPagadoCuentaProveedor(pagos.getCuentaPagarCabecera().getId(),	saldoPositivoaCobrar);
						
						
						List<Object[]> det = cuentaPagarDetalleRepository.consultarDetalleCuentaPorIdCabecera(cue.getId());
						List<CuentaPagarDetalle> listadoDetalleActualizados = new ArrayList<CuentaPagarDetalle>();
//						 cuenta_pagar_cabecera.fraccion_cuota,  cuenta_pagar_detalle.numero_cuota,  cuenta_pagar_detalle.fecha_vencimiento,  cuenta_pagar_detalle.monto, cuenta_pagar_detalle.importe,  cuenta_pagar_detalle.id, cuenta_pagar_detalle.sub_total, cuenta_pagar_detalle.cuenta_pagar_cabecera_id from cuenta_pagar_detalle inner join cuenta_pagar_cabecera on cuenta_pagar_cabecera.id=cuenta_pagar_detalle.cuenta_pagar_cabecera_id  where cuenta_pagar_detalle.cuenta_pagar_cabecera_id=:idCabecera order by cuenta_pagar_detalle.numero_cuota ASC", nativeQuery = true)
//						cuenta_cobrar_cabecera.fraccion_cuota, cuenta_cobrar_detalle.numero_cuota, cuenta_cobrar_detalle.fecha_vencimiento, cuenta_cobrar_detalle.monto, cuenta_cobrar_detalle.importe, cuenta_cobrar_detalle.interes_mora, cuenta_cobrar_detalle.id, cuenta_cobrar_detalle.sub_total, cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id from cuenta_cobrar_detalle inner join cuenta_cobrar_cabecera on cuenta_cobrar_cabecera.id=cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id  where cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id=:idCabecera order by cuenta_cobrar_detalle.numero_cuota ASC ", nativeQuery = true)

						for (int index = 0; index < det.size() && saldoPositivoaCobrar > 0; index++) {
							Object[] ob = det.get(index);
							montoCuotaActualizados = Double.parseDouble(ob[6].toString()) - Double.parseDouble(ob[4].toString());
				
							if (saldoPositivoaCobrar >= montoCuotaActualizados) {
								saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
								montoIMporteActualizados = montoCuotaActualizados;
								cobradoActual = cobradoActual + montoIMporteActualizados;
							} else {
								montoIMporteActualizados = saldoPositivoaCobrar;
								saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
								cobradoActual = cobradoActual + montoIMporteActualizados;
							}
							CuentaPagarDetalle detalleCuenta = new CuentaPagarDetalle();
							detalleCuenta.getCuentaPagarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
							detalleCuenta.setNumeroCuota(Integer.parseInt(ob[1].toString()));
							if (ob[2].toString() == null) {
								detalleCuenta.setFechaVencimiento(null);
							} else {
								detalleCuenta.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
							}
							detalleCuenta.setMonto(Double.parseDouble(ob[3].toString()));
							detalleCuenta.setImporte(Double.parseDouble(ob[4].toString())+ montoIMporteActualizados);
							detalleCuenta.setId(Integer.parseInt(ob[5].toString()));
							detalleCuenta.setSubTotal(Double.parseDouble(ob[6].toString()));
							detalleCuenta.getCuentaPagarCabecera().setId(Integer.parseInt(ob[7].toString()));
							listadoDetalleActualizados.add(detalleCuenta);
						}
						cuentaPagarDetalleRepository.saveAll(listadoDetalleActualizados);
					}
				}
			
			
			
		}
		
		return null;
	}
	
	

	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagos/{idCuenta}")
	public ResponseEntity<?> getPagosPorIdCuenta(@PathVariable int idCuenta) {
		List<PagosProveedor> listado=entityRepository.getPagosPorIdCuentas(idCuenta); 
		List<PagosProveedor> listadoRetorno = new ArrayList<>();
		if(listado.size() < 0){
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		}else {

			for(PagosProveedor cobros : listado){
				PagosProveedor cob= new PagosProveedor();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				cob.getCuentaPagarCabecera().setTotal(cobros.getCuentaPagarCabecera().getTotal());
				cob.getCuentaPagarCabecera().setPagado(cobros.getCuentaPagarCabecera().getPagado());
				cob.setImporte(cobros.getImporte());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre()+" "+cobros.getFuncionario().getPersona().getApellido());
				listadoRetorno.add(cob);			
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}


	@RequestMapping(method=RequestMethod.GET, value = "/cuentaCliente/{idProveedor}")
	public CuentaProveedor getCuentaProveedorIdCuenta(@PathVariable int idProveedor){
		CuentaProveedor c=new CuentaProveedor();
		List<Object[]> ob = entityRepository.getCabeceraCuentaClienteId(idProveedor);
		for(Object[] o: ob) {
			c.setProveedor(o[0].toString());
			c.setFecha(o[1].toString());
			c.setFechas(o[2].toString());

			Date actual=new Date();
			Date ultima=FechaUtil.convertirFechaStringADateUtil(o[2].toString());

			int dias = (int) ((actual.getTime()-ultima.getTime())/86400000);
			c.setDiaAtrasado(dias);

		}
		return  c;	
	}
	@RequestMapping(method = RequestMethod.GET, value = "/cabecera")
	public List<PagosProveedorCabecera> getAllCabecera() {
		List<PagosProveedorCabecera> cobroCliente = pagosProveedorCabeceraRepository.getPagosProveedoresCabeceraAll();
		List<PagosProveedorCabecera> cobro = new ArrayList<>();
		for (PagosProveedorCabecera c : cobroCliente) {
			PagosProveedorCabecera co = new PagosProveedorCabecera();
			co.setId(c.getId());
			co.getProveedor().setPersona(c.getProveedor().getPersona());
			co.getFuncionarioR().setPersona(c.getFuncionarioR().getPersona());
			co.getFuncionarioA().setPersona(c.getFuncionarioA().getPersona());
			co.setFechaPagos(c.getFechaPagos());
			co.setFechaRegistro(c.getFechaRegistro());
			co.setTipoOperacion(c.getTipoOperacion());
			co.setTotal(c.getTotal());
			co.setComprobante(c.getComprobante());
			cobro.add(co);
		}
		return cobro;
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/buscarPagos/cabeceraPagos/{idCabecera}")
	public ResponseEntity<?> getPagosProveedorPorCobrosCabeceraId(@PathVariable int idCabecera) {
		List<PagosProveedor> listado = entityRepository.getPagosProveedorPorPagosCabeceraId(idCabecera);
		List<PagosProveedor> listadoRetorno = new ArrayList<>();
		if (listado.size() < 0) {
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		} else {

			for (PagosProveedor cobros : listado) {
				PagosProveedor cob = new PagosProveedor();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				
				cob.setImporte(cobros.getImporte());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre());
				cob.getFuncionario().getPersona().setApellido(cobros.getFuncionario().getPersona().getApellido());
				cob.getCuentaPagarCabecera().getCompra().setId(cobros.getCuentaPagarCabecera().getCompra().getId());
				cob.getPagosProveedorCabecera().setId(cobros.getPagosProveedorCabecera().getId());
				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/cabecera/id/{id}")
	public PagosProveedorCabecera buscarPAgosProveedorCabeceraPorId(@PathVariable int id) {		
		return cargarCabecera(pagosProveedorCabeceraRepository.buscarPagosCabeceraPorId(id));
	}
	private PagosProveedorCabecera cargarCabecera(PagosProveedorCabecera ob) {
		PagosProveedorCabecera c= new PagosProveedorCabecera();
		c.setId(ob.getId());
		c.getProveedor().getPersona().setNombre(ob.getProveedor().getPersona().getNombre()+ " "+ob.getProveedor().getPersona().getApellido());
		c.getProveedor().getPersona().setCedula(ob.getProveedor().getPersona().getCedula());
		c.getProveedor().getPersona().setDireccion(ob.getProveedor().getPersona().getDireccion());
		c.getProveedor().getPersona().setTelefono(ob.getProveedor().getPersona().getTelefono());
		c.getFuncionarioR().getPersona().setNombre(ob.getFuncionarioR().getPersona().getNombre()+" "+ob.getFuncionarioR().getPersona().getApellido());
		c.getFuncionarioA().getPersona().setNombre(ob.getFuncionarioA().getPersona().getNombre()+" "+ob.getFuncionarioA().getPersona().getApellido());
		c.setTipo(ob.getTipo());
		c.setTipoOperacion(ob.getTipoOperacion());
		c.setFechaRegistro(ob.getFechaRegistro());
		c.setFechaPagos(ob.getFechaPagos());
		c.setTotal(ob.getTotal());
		return c;
	}




	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagos/idProveedor/{idProveedor}")
	public List<PagosProveedorCabecera> getPagosPorProveedorId(@PathVariable int idProveedor){
		PagosProveedorCabecera ccc=null;
		List<PagosProveedorCabecera> listaRetorno= new ArrayList<PagosProveedorCabecera>();
		List<Object[]> ob = entityRepository.getPagosPorIdProveedor(idProveedor);
		for(Object[] ovv: ob) {
			ccc= new PagosProveedorCabecera();
			ccc.setFechaPagos(FechaUtil.convertirFechaStringADateUtil(ovv[0].toString()));
			ccc.setFechaRegistro(FechaUtil.convertirFechaStringADateUtil(ovv[1].toString()));
			ccc.getFuncionarioA().getPersona().setNombre(ovv[2].toString());
			ccc.getFuncionarioR().getPersona().setNombre(ovv[3].toString());
			ccc.setTotal(Double.parseDouble(ovv[4].toString()));
			ccc.setComprobante(ovv[5].toString());
			ccc.setId(Integer.parseInt(ovv[6].toString()));
			listaRetorno.add(ccc);
		}
		return  listaRetorno;	
	}

	/**
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagosProveedor/compra")
	public List<PagosProveedorCompra> getRegistroPagosroCompra(){
		PagosProveedorCompra ccc=null;
		List<PagosProveedorCompra> listaRetorno= new ArrayList<PagosProveedorCompra>();
		List<Object[]> ob = pagosProveedorCompraRepository.getPagosProveedorCompras();
		for(Object[] ovv: ob) {
			ccc= new PagosProveedorCompra();
			ccc.getProveedor().setId(Integer.parseInt(ovv[0].toString()));
			ccc.getProveedor().getPersona().setNombre(ovv[1].toString()+ " "+ovv[2].toString());
			ccc.getProveedor().getPersona().setCedula(ovv[3].toString());
			ccc.setMonto(Double.parseDouble(ovv[4].toString()));
			ccc.setMontoCheque(Double.parseDouble(ovv[5].toString()));
			ccc.setMontoTarjeta(Double.parseDouble(ovv[6].toString()));
			ccc.setId(Integer.parseInt(ovv[7].toString()));
			listaRetorno.add(ccc);
		}
		return  listaRetorno;	
	}
	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagosProveedor/compra/detalle/{id}")
	public List<PagosProveedorCompra> getRegistroDetallePagosCompra(@PathVariable int id){
		PagosProveedorCompra ccc=null;
		List<PagosProveedorCompra> listaRetorno= new ArrayList<PagosProveedorCompra>();
		List<Object[]> ob = pagosProveedorCompraRepository.getPagosDetalleProveedorCompras(id);
		for(Object[] ovv: ob) {
			ccc= new PagosProveedorCompra();
			ccc.setId(Integer.parseInt(ovv[0].toString()));
			ccc.getProveedor().getPersona().setNombre(ovv[1].toString()+ " "+ovv[2].toString());
			ccc.getProveedor().getPersona().setCedula(ovv[3].toString());
			ccc.getFuncionario().getPersona().setNombre(ovv[4].toString()+" "+ovv[5].toString());
			ccc.setFechaFactura(com.bisontecfacturacion.security.config.FechaUtil.convertirFechaStringADateUtil(ovv[6].toString()));
			ccc.setDocumento(ovv[7].toString());
			ccc.setMonto(Double.parseDouble(ovv[8].toString()));
			ccc.setMontoCheque(Double.parseDouble(ovv[9].toString()));
			ccc.setMontoTarjeta(Double.parseDouble(ovv[10].toString()));
			listaRetorno.add(ccc);
		}
		return  listaRetorno;	
	}
	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagosProveedor/compra/{id}")
	public PagosProveedorCompra getRegistroPagosroCompraPorId(@PathVariable int id){
		PagosProveedorCompra cccRetorno= null;
		PagosProveedorCompra ccc = pagosProveedorCompraRepository.findById(id).orElse(null);
		if(ccc == null) {
			cccRetorno= ccc;
		}else {
			cccRetorno = new PagosProveedorCompra();
			cccRetorno.setId(ccc.getId());
			cccRetorno.getProveedor().getPersona().setNombre(ccc.getProveedor().getPersona().getNombre()+ " "+ccc.getProveedor().getPersona().getApellido());;
			cccRetorno.getProveedor().getPersona().setCedula(ccc.getProveedor().getPersona().getCedula());
			cccRetorno.setDocumento(ccc.getDocumento());
			cccRetorno.setOperacionCaja(ccc.getOperacionCaja());
			cccRetorno.getFuncionario().getPersona().setNombre(ccc.getFuncionario().getPersona().getNombre()+ " "+ccc.getFuncionario().getPersona().getApellido());
			cccRetorno.setMonto(ccc.getMonto());
			cccRetorno.setMontoCheque(ccc.getMontoCheque());
			cccRetorno.setMontoTarjeta(ccc.getMontoTarjeta());
			cccRetorno.setFechaFactura(ccc.getFechaFactura());			
		}
	 return cccRetorno;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/guardar/pagosCompra")
	public ResponseEntity<?>  saveRegsitroPagosCompra(@RequestBody PagosProveedorCompra entity ){
		try {
			if (entity.getFuncionario().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if (entity.getProveedor().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if (entity.getDocumento()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL DOCUMENTO NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if (entity.getFechaFactura()== null) {
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}else if (entity.getTipoOperacion().getId()== 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL TIPO OPERACION NO DEBE QUEDAR VACIO PARA EFECTUAR PAGOS PROVEEDOR"), HttpStatus.CONFLICT);
			}
				pagosProveedorCompraRepository.save(entity);
				
				OperacionCaja op= new OperacionCaja();
				op.setMonto(entity.getMonto());
				op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
				Integer idApe= aperturaRepository.getAperturaActivoCajaId(entity.getFuncionario().getId());
				System.out.println("idApe edddiiitaaerr: "+idApe);
				System.out.println(aperturaRepository.getAperturaActivoCajaId(entity.getFuncionario().getId()));
				op.getAperturaCaja().setId(idApe);
				
				op.setEfectivo(0.0);
				op.setVuelto(0.0);
				Concepto c= new Concepto();
				op.getConcepto().setId(12);
				c= conceptoRepository.findById(12).get();
				op.setMotivo(c.getDescripcion()+" REF.: "+ entity.getId());
				op.setTipo("SALIDA");
				if (op.getTipoOperacion().getId() == 1) {
					aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idApe, entity.getMonto());
				}
				if (op.getTipoOperacion().getId() == 2) {
					aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idApe, entity.getMonto());
				}
				if (op.getTipoOperacion().getId() == 3) {
					aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idApe, entity.getMonto());
				}
				operacionCajaRepository.save(op);
				OperacionCaja opera =  operacionCajaRepository.findTop1ByOrderByIdDesc();
				System.out.println(opera.getId()+ " ID OPERA");
				PagosProveedorCompra pagoCompra= pagosProveedorCompraRepository.findTop1ByOrderByIdDesc();
				System.out.println(pagoCompra.getId()+ " ID pagGG");
				pagosProveedorCompraRepository.findByActualizarPagosCompraCajaCabeceraOperacion(pagoCompra.getId(), opera.getId());

			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
