package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.groovy.tools.shell.commands.SetCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.CuentaCliente;
import com.bisontecfacturacion.security.auxiliar.CuentaProveedor;
import com.bisontecfacturacion.security.model.CajaMayor;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarDetalle;
import com.bisontecfacturacion.security.model.EntradaSalidaCaja;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.PagosProveedor;
import com.bisontecfacturacion.security.model.PagosProveedorCompra;
import com.bisontecfacturacion.security.model.TransferenciaGastos;
import com.bisontecfacturacion.security.model.TransferenciaPagosProveedor;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarCabeceraRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarDetalleRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.PagoProveedorRepository;
import com.bisontecfacturacion.security.repository.PagosProveedorCompraRepository;
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
	private TransferenciaPagosProveedorRepository  transferenciaPagosRepository;

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
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagos/{idCuenta}")
	public ResponseEntity<?> getPagosPorIdCuenta(@PathVariable int idCuenta) {
		List<PagosProveedor> listado=entityRepository.getPagosPorIdCuenta(idCuenta); 
		List<PagosProveedor> listadoRetorno = new ArrayList<>();
		if(listado.size() < 0){
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		}else {

			for(PagosProveedor cobros : listado){
				PagosProveedor cob= new PagosProveedor();
				cob.setId(cobros.getId());
				cob.setFechaPagos(cobros.getFechaPagos());
				cob.setFechaRegistro(cobros.getFechaRegistro());
				cob.getFuncionarioA().getPersona().setNombre(cobros.getFuncionarioA().getPersona().getNombre());
				cob.getFuncionarioA().getPersona().setApellido(cobros.getFuncionarioA().getPersona().getApellido());
				cob.getFuncionarioR().getPersona().setNombre(cobros.getFuncionarioR().getPersona().getNombre());
				cob.getFuncionarioR().getPersona().setApellido(cobros.getFuncionarioR().getPersona().getApellido());
				cob.setImporte(cobros.getImporte());
				//				cob.setOperacionCaja(cobros.getOperacionCaja());
				cob.setComprobante(cobros.getComprobante());


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

	@RequestMapping(method=RequestMethod.GET, value = "/buscarPagos/idProveedor/{idProveedor}")
	public List<PagosProveedor> getPagosPorProveedorId(@PathVariable int idProveedor){
		PagosProveedor ccc=null;
		List<PagosProveedor> listaRetorno= new ArrayList<PagosProveedor>();
		List<Object[]> ob = entityRepository.getPagosPorIdProveedor(idProveedor);
		for(Object[] ovv: ob) {
			ccc= new PagosProveedor();
			ccc.setFechaPagos(FechaUtil.convertirFechaStringADateUtil(ovv[0].toString()));
			ccc.setFechaRegistro(FechaUtil.convertirFechaStringADateUtil(ovv[1].toString()));
			ccc.getFuncionarioA().getPersona().setNombre(ovv[2].toString());
			ccc.getFuncionarioA().getPersona().setApellido(ovv[3].toString());
			ccc.getFuncionarioR().getPersona().setNombre(ovv[4].toString());
			ccc.getFuncionarioR().getPersona().setApellido(ovv[5].toString());
			ccc.setImporte(Double.parseDouble(ovv[6].toString()));
			ccc.setComprobante(ovv[7].toString());
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
