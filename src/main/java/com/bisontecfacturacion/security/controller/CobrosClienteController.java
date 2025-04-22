package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.bisontecfacturacion.security.auxiliar.CuentaCliente;
import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteCabeceraRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ParametroTipoHojaRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;

@Transactional
@RestController
@RequestMapping("cobrosCliente")
public class CobrosClienteController {
	private Reporte report;
	@Autowired
	private CobrosClienteRepository entityRepository;
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private CobrosClienteCabeceraRepository cobrosClienteCabeceraRepository;

	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private CuentaAcobrarDetalleRepository cuentaCobrarDetalleRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@Autowired
	private ImpresoraRepository impresoraRepository;
	@Autowired
	
	private TerminalConfigImpresoraRepository terminalRepository;
	
	@Autowired
	private ParametroTipoHojaRepository parametroTipoHoja;

	
	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;
	
	

	@Autowired
	private ReporteConfigRepository reporteConfigRepository;
	

	@RequestMapping(method = RequestMethod.GET, value = "/{fecha}")
	public List<CobrosCliente> getAlls(@PathVariable String fecha) {
		String[] fec = fecha.split("-");
		Integer dia = Integer.parseInt(fec[0]);
		Integer mes = Integer.parseInt(fec[1]);
		Integer ano = Integer.parseInt(fec[2]);
		List<Object[]> objeto = entityRepository.getCobros(ano, mes, dia);
		System.out.println();
		List<CobrosCliente> venta = new ArrayList<>();
		for (Object[] ob : objeto) {
			CobrosCliente ventas = new CobrosCliente();
			ventas.setId(Integer.parseInt(ob[0].toString()));
			ventas.getFuncionario().getPersona().setNombre(ob[1].toString());
			System.out.println(ob[2].toString() + " possss 222222");
			ventas.getCuentaCobrarCabecera().getCliente().getPersona().setNombre(ob[2].toString());
			ventas.setTotal(Double.parseDouble(ob[3].toString()));
			String fech = ob[4].toString();
			ventas.setFecha(FechaUtil.convertirFechaStringADateUtil(fech));
//			ventas.setEstado(ob[5].toString());
			venta.add(ventas);
		}
		return venta;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody CobrosCliente entity) {
		Funcionario f = funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId());
		entity.getFuncionario().setId(f.getId());
		//int op = operacionCajaRepository.findTop1ByOrderByIdDesc().getId();
		//System.out.println("idOp ult" + op);
		//entity.getOperacionCaja().setId(op);
		entityRepository.save(entity);
//		OperacionCaja v = new OperacionCaja();
//		aperturaCajaRepository.
//		v.aperturaCaja.id = this.aperturaId;
//	    v.concepto.id = 5; // concepto es cobros clientes
//	    v.vuelto = this.vuelto;
//	    v.efectivo = this.monto;
//	    v.monto = this.verificarMontoAPagar();
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<CobrosCliente> getAll() {
		List<CobrosCliente> cobroCliente = entityRepository.findAll();
		List<CobrosCliente> cobro = new ArrayList<>();
		for (CobrosCliente c : cobroCliente) {
			CobrosCliente co = new CobrosCliente();
			co.setId(c.getId());
			co.getFuncionario().getPersona().setNombre(
					c.getFuncionario().getPersona().getNombre() + " " + c.getFuncionario().getPersona().getApellido());
			co.getCuentaCobrarCabecera().getCliente().getPersona()
					.setNombre(c.getCuentaCobrarCabecera().getCliente().getPersona().getNombre() + " "
							+ c.getCuentaCobrarCabecera().getCliente().getPersona().getApellido());
			co.setTotal(c.getTotal());
			co.setFecha(c.getFecha());
			cobro.add(co);
		}
		return cobro;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cabecera")
	public List<CobrosClienteCabecera> getAllCabecera() {
		List<CobrosClienteCabecera> cobroCliente = entityRepository.getCobrosClienteCabeceraAll();
		List<CobrosClienteCabecera> cobro = new ArrayList<>();
		for (CobrosClienteCabecera c : cobroCliente) {
			CobrosClienteCabecera co = new CobrosClienteCabecera();
			co.setId(c.getId());
			co.getCliente().setPersona(c.getCliente().getPersona());;
			co.getFuncionario().setPersona(c.getFuncionario().getPersona());
			co.setFecha(c.getFecha());
			co.setTotal(c.getTotal());
			
			cobro.add(co);
		}
		return cobro;
	}


	@RequestMapping(method = RequestMethod.GET, value = "/buscar/{descripcion}")
	public List<CobrosCliente> getBuscarNombreApellidoCliente(@PathVariable String descripcion) {
		List<Object[]> objeto = entityRepository.getBuscarClienteNombreApellido("%" + descripcion.toUpperCase() + "%");
		System.out.println();
		List<CobrosCliente> venta = new ArrayList<>();
		for (Object[] ob : objeto) {
			CobrosCliente ventas = new CobrosCliente();
			ventas.setId(Integer.parseInt(ob[0].toString()));
			ventas.getFuncionario().getPersona().setNombre(ob[1].toString());
			ventas.getCuentaCobrarCabecera().getCliente().getPersona().setNombre(ob[2].toString());
			ventas.setTotal(Double.parseDouble(ob[3].toString()));
			String fech = ob[4].toString();
			ventas.setFecha(FechaUtil.convertirFechaStringADateUtil(fech));
			venta.add(ventas);
		}
		return venta;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarCobros/{idCuenta}")
	public ResponseEntity<?> getCobrosPorIdCuenta(@PathVariable int idCuenta) {
		List<CobrosCliente> listado = entityRepository.getCobrosPorIdCuenta(idCuenta);
		List<CobrosCliente> listadoRetorno = new ArrayList<>();
		if (listado.size() < 0) {
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		} else {

			for (CobrosCliente cobros : listado) {
				CobrosCliente cob = new CobrosCliente();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				cob.setTotal(cobros.getTotal());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre());
				cob.getFuncionario().getPersona().setApellido(cobros.getFuncionario().getPersona().getApellido());
				cob.setOperacionCaja(cobros.getOperacionCaja());
				cob.getCobrosClienteCabecera().setId(cobros.getCobrosClienteCabecera().getId());
				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/buscarCobros/cabeceraCobros/{idCabecera}")
	public ResponseEntity<?> getCobrosClientePorCobrosCabeceraId(@PathVariable int idCabecera) {
		List<CobrosCliente> listado = entityRepository.getCobrosClientePorCobrosCabeceraId(idCabecera);
		List<CobrosCliente> listadoRetorno = new ArrayList<>();
		if (listado.size() < 0) {
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		} else {

			for (CobrosCliente cobros : listado) {
				CobrosCliente cob = new CobrosCliente();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				cob.setTotal(cobros.getTotal());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre());
				cob.getFuncionario().getPersona().setApellido(cobros.getFuncionario().getPersona().getApellido());
				cob.setOperacionCaja(cobros.getOperacionCaja());
				cob.getCuentaCobrarCabecera().getVenta().setId(cobros.getCuentaCobrarCabecera().getVenta().getId());
				cob.getCobrosClienteCabecera().setId(cobros.getCobrosClienteCabecera().getId());
				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/buscarCobros/cliente/{idCliente}")
	public ResponseEntity<?> getCobrosClienteCabeceraPorIdCliente(@PathVariable int idCliente) {
		List<CobrosClienteCabecera> listado = entityRepository.getCobrosClienteCabeceraPorIdCliente(idCliente);
		List<CobrosClienteCabecera> listadoRetorno = new ArrayList<>();
		if (listado.size() < 0) {
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		} else {

			for (CobrosClienteCabecera cobros : listado) {
				CobrosClienteCabecera cob = new CobrosClienteCabecera();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				cob.setTotal(cobros.getTotal());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre());
				cob.getFuncionario().getPersona().setApellido(cobros.getFuncionario().getPersona().getApellido());
				cob.getCliente().getPersona().setNombre(cobros.getCliente().getPersona().getNombre());
				cob.getCliente().getPersona().setApellido(cobros.getCliente().getPersona().getApellido());
				cob.getCliente().getPersona().setDireccion(cobros.getCliente().getPersona().getDireccion());
				cob.getCliente().setId(cobros.getCliente().getId());

				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	////////////////////////////////////////////// operacion cobros
	////////////////////////////////////////////// cliente/////////////////////////////////////////////////////////////

	@RequestMapping(method = RequestMethod.GET, value = "/buscarCobros/cabecera/{idCabecera}")
	public ResponseEntity<?> getCobrosClienteCabeceraPorId(@PathVariable int idCabecera) {
		List<CobrosClienteCabecera> listado = entityRepository.getCobrosClienteCabeceraPorId(idCabecera);
		List<CobrosClienteCabecera> listadoRetorno = new ArrayList<>();
		if (listado.size() < 0) {
			return new ResponseEntity<>(new CustomerErrorType("Esta cuenta aún no posee Cobros"), HttpStatus.CONFLICT);
		} else {
			for (CobrosClienteCabecera cobros : listado) {
				CobrosClienteCabecera cob = new CobrosClienteCabecera();
				cob.setId(cobros.getId());
				cob.setFecha(cobros.getFecha());
				cob.setTotal(cobros.getTotal());
				cob.getFuncionario().getPersona().setNombre(cobros.getFuncionario().getPersona().getNombre());
				cob.getFuncionario().getPersona().setApellido(cobros.getFuncionario().getPersona().getApellido());
				cob.getCliente().getPersona().setNombre(cobros.getCliente().getPersona().getNombre());
				cob.getCliente().getPersona().setApellido(cobros.getCliente().getPersona().getApellido());
				cob.getCliente().getPersona().setDireccion(cobros.getCliente().getPersona().getDireccion());
				cob.getCliente().setId(cobros.getCliente().getId());
				cob.setCobrosClientes(cobros.getCobrosClientes());
				//cob.setCobrosClientes(entityRepository.getCobrosClientePorIdCabecera(idCabecera));
				//cob.setCobrosClientes(cobrosClienteCabeceraRepository.findByCobrosDetalladoPorIdCabecera(idCabecera));
				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listado, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idcuenta/{idCuenta}/{monto}/{idUser}/{idOpe}")
	public List<Object[][]> operacionCobrosPorCuenta(@PathVariable int idCuenta, @PathVariable Double monto,@PathVariable int idUser, @PathVariable int idOpe) {
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		
		CobrosClienteCabecera cab= new CobrosClienteCabecera();
		cab.getCliente().setId(cuentaCobrarRepository.getOne(idCuenta).getCliente().getId());
		cab.getFuncionario().setId(f.getId());
		cab.setFecha(new Date());
		cab.setTotal(monto);
		cobrosClienteCabeceraRepository.save(cab);
		
		System.out.println("Entro en desde ceutna areglo^^^^^^^^^^^^^^^^^^^^");
		CuentaCobrarCabecera cabecera = new CuentaCobrarCabecera();
		cabecera=cuentaCobrarRepository.getOne(idCuenta);
		List<CuentaCobrarCabecera> cuentaCabecera = new ArrayList<CuentaCobrarCabecera>();
		cuentaCabecera.add(cabecera);
		return operacion(cuentaCabecera, monto, idUser, idOpe, cobrosClienteCabeceraRepository.getUltimoCobrosClienteCab().getId());

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/idcliente/{idcliente}/{monto}/{idUser}/{idOpe}")
	public List<Object[][]> operacionCobrosPorCliente(@PathVariable int idcliente, @PathVariable Double monto,
			@PathVariable int idUser, @PathVariable int idOpe) {
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		
		CobrosClienteCabecera cab= new CobrosClienteCabecera();
		cab.getCliente().setId(idcliente);
		cab.getFuncionario().setId(f.getId());
		cab.setFecha(new Date());
		cab.setTotal(monto);
		cobrosClienteCabeceraRepository.save(cab);
		
		System.out.println("Entro en cero^^^^^^^^^^^^^^^^^^^^");
		List<CuentaCobrarCabecera> cuentaCabecera = cuentaCobrarRepository.findByCuentaPorIdACobrars(idcliente);
		return operacion(cuentaCabecera, monto, idUser, idOpe, cobrosClienteCabeceraRepository.getUltimoCobrosClienteCab().getId());

	}

	public List<Object[][]> operacion(List<CuentaCobrarCabecera> cuentas, Double monto, int idUser, int idOpe, int idCabCobros) {

		String tipoPago="";
		System.out.println("Entro 1*******************");
		Object[][] obResult = new Object[cuentas.size()][12];
		int contador = 0;
		List<Object[][]> listRes = new ArrayList<>();
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		int idApertura = aperturaCajaRepository.getAperturaActivoCajaId(f.getId());
		Double cobradoActual = 0.0;
		for (int i = 0; i < cuentas.size() && cobradoActual < monto; i++) {
			CuentaCobrarCabecera cue = cuentas.get(i);
			System.out.println("cue.getCliente().getId(): veeveve "+cue.getVenta().getId());
			if ((monto - cobradoActual) >= cue.getSaldo()) {

				System.out.println("Entro en 2*******************************");
				OperacionCaja op = new OperacionCaja();
				op.getTipoOperacion().setId(idOpe);
				op.getAperturaCaja().setId(idApertura);
				op.getConcepto().setId(5);
				op.setEfectivo(0.0);
				op.setVuelto(0.0);
				op.setMonto(cue.getSaldo());

				operacionCajaRepository.save(op);
				OperacionCaja operacioActualziar = operacionCajaRepository.findTop1ByOrderByIdDesc();
				CobrosCliente cobros = new CobrosCliente();
				cobros.getCobrosClienteCabecera().setId(idCabCobros);
				cobros.getCuentaCobrarCabecera().setId(cue.getId());
				cobros.setTotal(cue.getSaldo());
				cobradoActual = cobradoActual + cue.getSaldo();
				cobros.getFuncionario().setId(f.getId());
				cobros.getOperacionCaja().setId(operacioActualziar.getId());
				entityRepository.save(cobros);
				cuentaCobrarDetalleRepository.liquidarDetalle(cue.getId(), new Date(), true);
				cuentaCobrarRepository.findByActualizarPagadoCuenta(cobros.getCuentaCobrarCabecera().getId(),op.getMonto());
				
				CobrosCliente coACt = entityRepository.findTop1ByOrderByIdDesc();
			//	 entityRepository.findByActualizarCobrosOperacion(coACt.getId(), operacioActualziar.getId());;

				Concepto c = new Concepto();
				c = conceptoRepository.findById(operacioActualziar.getConcepto().getId()).get();
				operacioActualziar.setMotivo(c.getDescripcion() + " REF.: " + cobros.getId());
				operacioActualziar.setTipo("ENTRADA");
				if (operacioActualziar.getTipoOperacion().getId() == 1) {
					aperturaCajaRepository.findByActualizarAperturaSaldo(operacioActualziar.getAperturaCaja().getId(),
							operacioActualziar.getMonto());
					tipoPago="EFECTIVO";
				}
				if (operacioActualziar.getTipoOperacion().getId() == 2) {
					aperturaCajaRepository.findByActualizarAperturaSaldoCheque(
							operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
					tipoPago="CHEQUE";
				}
				if (operacioActualziar.getTipoOperacion().getId() == 3) {
					aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(
							operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
					tipoPago="TARJETA";
				}
				// aperturaCajaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(),
				// op.getMonto());
				operacionCajaRepository.save(operacioActualziar);

				// d: id, total: total, pagado: pagado, importe: importe
//				idCuenta, totalCuenta, pagadoCuenta, saldoCuenta, idCli, tipoOperacion, numeroVenta, idCobroCabecera, totalCobrado, numeroCobrosçItem, cobradoPorCuenta
				
				obResult[i][0] = cue.getId();
				obResult[i][1] = cue.getTotal();
				obResult[i][2] = cue.getPagado();
				obResult[i][3] = cue.getSaldo();
				obResult[i][4] = cue.getCliente().getId();
				obResult[i][5] = tipoPago;
				obResult[i][6] = cue.getVenta().getId();
				obResult[i][7] = idCabCobros;
				obResult[i][8] = monto;
				obResult[i][9] = coACt.getId();
				obResult[i][10] = coACt.getTotal();
				obResult[i][11] = f.getId();

				System.out.println(obResult[i][0] + "" + obResult[i][1] + obResult[i][2] + obResult[i][3]);
			} else {
				System.out.println("Entro en 3***********************************");
				Double saldoPositivoaCobrar = 0.0;
				Double montoCuotaActualizados = 0.0;
				Double montoIMporteActualizados = 0.0;
				System.out.println("monto ya cobrado : " + cobradoActual + ", " + monto);
				if ((monto - cobradoActual) > 0) {
					saldoPositivoaCobrar = monto - cobradoActual;
					System.out.println("entroooooo iffaafafafafffffffffffffffff");
					OperacionCaja op = new OperacionCaja();
					op.getTipoOperacion().setId(idOpe);
					op.getAperturaCaja().setId(idApertura);
					op.getConcepto().setId(5);
					op.setEfectivo(0.0);
					op.setVuelto(0.0);
					op.setMonto(saldoPositivoaCobrar);
					operacionCajaRepository.save(op);
					OperacionCaja operacioActualziar = operacionCajaRepository.findTop1ByOrderByIdDesc();
					System.out.println("OPERACION IDDDD:  "+operacioActualziar.getId());
					CobrosCliente cobros = new CobrosCliente();
					cobros.getCobrosClienteCabecera().setId(idCabCobros);
					cobros.getCuentaCobrarCabecera().setId(cue.getId());
					cobros.setTotal(saldoPositivoaCobrar);
					cobradoActual = cobradoActual + saldoPositivoaCobrar;
					cobros.getFuncionario().setId(f.getId());
					cobros.getOperacionCaja().setId(operacioActualziar.getId());
					entityRepository.save(cobros);
					
					CobrosCliente coAc = entityRepository.findTop1ByOrderByIdDesc();
					//entityRepository.findByActualizarCobrosOperacion(coAc.getId(), operacioActualziar.getId());;
					
					List<Object[]> det = cuentaCobrarDetalleRepository.consultarDetalleCuentaPorIdCabecera(cue.getId());
					List<CuentaCobrarDetalle> listadoDetalleActualizados = new ArrayList<CuentaCobrarDetalle>();
					for (int index = 0; index < det.size() && saldoPositivoaCobrar > 0; index++) {
						System.out.println("revision detalle cuenta patraajustar con el pagado");
						Object[] ob = det.get(index);
						montoCuotaActualizados = Double.parseDouble(ob[7].toString())
								- Double.parseDouble(ob[4].toString());
			
						if (saldoPositivoaCobrar >= montoCuotaActualizados) {
							saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
							montoIMporteActualizados = montoCuotaActualizados;
							//cobradoActual = cobradoActual + montoIMporteActualizados;
						} else {
							montoIMporteActualizados = saldoPositivoaCobrar;
							saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
							//cobradoActual = cobradoActual + montoIMporteActualizados;
						}
						System.out.println("Cobrado cuota actualizados:  "+montoCuotaActualizados);

						System.out.println("Cobrado por detalle de cuenta:  "+montoIMporteActualizados);
						CuentaCobrarDetalle detalleCuenta = new CuentaCobrarDetalle();
						detalleCuenta.getCuentaCobrarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
						detalleCuenta.setNumeroCuota(Integer.parseInt(ob[1].toString()));
						if (ob[2].toString() == null) {
							detalleCuenta.setFechaVencimiento(null);
						} else {
							detalleCuenta.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
						}
						detalleCuenta.setImporte(montoIMporteActualizados + Double.parseDouble(ob[4].toString()));
						detalleCuenta.setInteresMora(Double.parseDouble(ob[5].toString()));
						detalleCuenta.setSubTotal(Double.parseDouble(ob[7].toString()));
						detalleCuenta.getCuentaCobrarCabecera().setId(Integer.parseInt(ob[8].toString()));
						detalleCuenta.setId(Integer.parseInt(ob[6].toString()));
						detalleCuenta.setMonto(Double.parseDouble(ob[3].toString()));
						System.out.println(detalleCuenta.getMonto()+ "  monto verificar");
						listadoDetalleActualizados.add(detalleCuenta);

					}

					cuentaCobrarDetalleRepository.saveAll(listadoDetalleActualizados);

					cuentaCobrarRepository.findByActualizarPagadoCuenta(cobros.getCuentaCobrarCabecera().getId(), op.getMonto());
					Concepto c = new Concepto();
					c = conceptoRepository.findById(operacioActualziar.getConcepto().getId()).get();
					operacioActualziar.setMotivo(c.getDescripcion() + " REF.: " + cobros.getId());
					operacioActualziar.setTipo("ENTRADA");
					if (operacioActualziar.getTipoOperacion().getId() == 1) {
						aperturaCajaRepository.findByActualizarAperturaSaldo(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
						tipoPago="EFECTIVO";

					}
					if (operacioActualziar.getTipoOperacion().getId() == 2) {
						aperturaCajaRepository.findByActualizarAperturaSaldoCheque(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
						tipoPago="CHEQUE";

					}
					if (operacioActualziar.getTipoOperacion().getId() == 3) {
						aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
						tipoPago="TARJETA";
					}
					// aperturaCajaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(),
					// op.getMonto());
					operacionCajaRepository.save(operacioActualziar);

				
					
					obResult[i][0] = cue.getId();
					obResult[i][1] = cue.getTotal();
					obResult[i][2] = cue.getPagado();
					obResult[i][3] = cue.getSaldo();
					obResult[i][4] = cue.getCliente().getId();
					obResult[i][5] = tipoPago;
					obResult[i][6] = cue.getVenta().getId();
					obResult[i][7] = idCabCobros;
					obResult[i][8] = monto;
					obResult[i][9] = coAc.getId();
					obResult[i][10] = operacioActualziar.getMonto();	
					obResult[i][11] = f.getId();
					
					System.out.println(obResult[i][0] + "" + obResult[i][1] + obResult[i][2] + obResult[i][3]);
				}
			}

		}
		listRes.add(obResult);
		for (int i = 0; i < obResult.length; i++) {
			if (obResult[i][0] != null) {
				//System.out.println("saldo cobrado : "+ listRes.get(i)[i][3]);
				contador++;
			}
		}
		System.out.println("Contador de lam matriz nueva " + contador);
		Object[][] obResultadoNuevo = new Object[contador][12];
		List<Object[][]> listResNuevo = new ArrayList<>();
		for (int i = 0; i < obResult.length; i++) {
			if (obResult[i][0] != null) {
				obResultadoNuevo[i][0] = obResult[i][0];
				obResultadoNuevo[i][1] = obResult[i][1];
				obResultadoNuevo[i][2] = obResult[i][2];
				obResultadoNuevo[i][3] = obResult[i][3];
				obResultadoNuevo[i][4] = obResult[i][4];
				obResultadoNuevo[i][5] = obResult[i][5];
				obResultadoNuevo[i][6] = obResult[i][6];
				obResultadoNuevo[i][7] = obResult[i][7];
				obResultadoNuevo[i][8] = obResult[i][8];
				obResultadoNuevo[i][9] = obResult[i][9];
				obResultadoNuevo[i][10] = obResult[i][10];
				obResultadoNuevo[i][11] = obResult[i][11];

			}
		}
		listResNuevo.add(obResultadoNuevo);
		return listResNuevo;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cuentaCliente/{idcliente}")
	public CuentaCliente getCuentaClienteId(@PathVariable int idcliente) {
		CuentaCliente c = new CuentaCliente();
		List<Object[]> ob = entityRepository.getCabeceraCuentaClienteId(idcliente);
		for (Object[] o : ob) {
			c.setCliente(o[0].toString());
			c.setFecha(o[1].toString());
			c.setFechas(o[2].toString());

			Date actual = new Date();
			Date ultima = FechaUtil.convertirFechaStringADateUtil(o[2].toString());

			int dias = (int) ((actual.getTime() - ultima.getTime()) / 86400000);
			c.setDiaAtrasado(dias);

		}
		return c;
	}
	private CobrosClienteCabecera cargarCabecera(CobrosClienteCabecera ob) {
		CobrosClienteCabecera c= new CobrosClienteCabecera();
		c.setId(ob.getId());
		c.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+ " "+ob.getCliente().getPersona().getApellido());
		c.getCliente().getPersona().setCedula(ob.getCliente().getPersona().getCedula());
		c.getCliente().getPersona().setDireccion(ob.getCliente().getPersona().getDireccion());
		c.getCliente().getPersona().setTelefono(ob.getCliente().getPersona().getTelefono());
		c.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+" "+ob.getFuncionario().getPersona().getApellido());
		c.setTotal(ob.getTotal());
		c.setFecha(ob.getFecha());
		return c;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/cabecera/id/{id}")
	public CobrosClienteCabecera buscarCobrosClienteCabeceraPorId(@PathVariable int id) {		
		return cargarCabecera(cobrosClienteCabeceraRepository.buscarCobrosCabeceraPorId(id));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cuentaClienteAll/{idCliente}")
	public List<CobrosClienteCabecera> getCobrosClienteCabecera(@PathVariable int idCliente) {
		return cargarListadoCobrosCabecera(entityRepository.getCobrosClienteCabecera(idCliente));
	}

	public List<CuentaCliente> cargarListado(List<Object[]> objeto) {
		List<CuentaCliente> c = new ArrayList<CuentaCliente>();
		for (Object[] ob : objeto) {
			CuentaCliente cuenta = new CuentaCliente();
			cuenta.setFecha(ob[0].toString());
			cuenta.setTotal(Double.parseDouble(ob[1].toString()));
			cuenta.setPagado(Double.parseDouble(ob[2].toString()));
			cuenta.setSaldo(Double.parseDouble(ob[3].toString()));
			c.add(cuenta);
		}
		return c;
	}
	public List<CobrosClienteCabecera> cargarListadoCobrosCabecera(List<Object[]> objeto) {
		List<CobrosClienteCabecera> c = new ArrayList<CobrosClienteCabecera>();
		for (Object[] ob : objeto) {
			CobrosClienteCabecera cob = new CobrosClienteCabecera();
			cob.getFuncionario().getPersona().setNombre(ob[0].toString());
			cob.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			cob.setTotal(Double.parseDouble(ob[2].toString()));
			cob.setId(Integer.parseInt(ob[3].toString()));
			c.add(cob);
		}
		return c;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/cobroCliente")
	public List<CuentaCliente> getCobroCliente() {
		List<CuentaCliente> cuentaCliente = new ArrayList<CuentaCliente>();

		List<Object[]> objeto = entityRepository.getListadoClienteCobrosCliente();
		for (Object[] ob : objeto) {
			CuentaCliente c = new CuentaCliente();
			List<Object[]> listado = entityRepository.getCabeceraCuentaClienteId(Integer.parseInt(ob[0].toString()));
			Double saldo = entityRepository.getSaldoIdCliente(Integer.parseInt(ob[0].toString()));
			if (listado.size() == 0) {
				Cliente cli = clienteRepository.findById(Integer.parseInt(ob[0].toString())).get();
				c.setCliente(cli.getPersona().getNombre() + " " + cli.getPersona().getApellido());
				c.setFecha(null);
				c.setFechas(null);
				c.setSaldo(saldo);
				c.setDiaAtrasado(0);
			} else {
				for (Object[] o : listado) {
					c.setCliente(o[0].toString());
					c.setFecha(o[1].toString());
					c.setFechas(o[2].toString());
					c.setSaldo(saldo);
					Date actual = new Date();
					Date ultima = FechaUtil.convertirFechaStringADateUtil(o[2].toString());
					int dias = (int) ((actual.getTime() - ultima.getTime()) / 86400000);
					c.setDiaAtrasado(dias);
				}
			}
			cuentaCliente.add(c);
		}

		return cuentaCliente;
	}
	private List<CobrosCliente> getLista(int id) {
		// TODO Auto-generated method stub
		
		return null;
	}
	

	@RequestMapping(method = RequestMethod.POST, value = "/impresionMatricial/{numeroTerminal}/{siImprimir}")
	public void getCobroCliente(@RequestBody List<CobrosCliente> cobros, @PathVariable int numeroTerminal, @PathVariable String siImprimir) {
		System.out.println("lista sise"+cobros.size());
		if (siImprimir.equals("true")) {
			System.out.println(cobros.size());
			
			Reporte report = new Reporte();
			Cliente cl = clienteRepository.getIdCliente(cobros.get(0).getCuentaCobrarCabecera().getCliente().getId());
			Funcionario fu = funcionarioRepository.getIdFuncionario(cobros.get(0).getFuncionario().getId());
			System.out.println("ID FUNCIONARIO: "+fu.getPersona().getNombre());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setNombre(cl.getPersona().getNombre()+ " "+cl.getPersona().getApellido());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setCedula(cl.getPersona().getCedula());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setTelefono(cl.getPersona().getTelefono());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setDireccion(cl.getPersona().getDireccion());

			cobros.get(0).getFuncionario().getPersona().setNombre(fu.getPersona().getNombre()+ " "+fu.getPersona().getApellido());
			cobros.get(0).getFuncionario().getPersona().setTelefono(fu.getPersona().getTelefono());

			
			cobros.get(0).setFecha(new Date());
			System.out.println("D-: "+cobros.get(0).getOperacionCaja().getTipoOperacion().getDescripcion());
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminal(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				
				List<CobrosCliente> listaVentaImpresion= new ArrayList<CobrosCliente>();

				ReporteConfig reportConfig = reporteConfigRepository.getOne(3);
				int pageSize = 10;
				int totalPages = (int) Math.ceil((double) cobros.size() / pageSize);
				System.out.println("TOTAL DE PAGINAS:"+ totalPages);
				
				
				for (int i = 0; i < totalPages; i++) {
				    System.out.println("\n--- Página " + (i + 1) + " ---");

				    int start = i * pageSize;
				    int end = Math.min(start + pageSize, cobros.size());
				    // Crear una nueva lista con los elementos de la página actual
				    List<CobrosCliente> detallesPagina = new ArrayList<>(cobros.subList(start, end));
				    System.out.println("total item : "+detallesPagina.size());
				    Double totalMontoPagina=0.0, totalPaginaIvaCinco=0.0, totalPaginaIvaDies=0.0, totalPaginaIva=0.0,totalPaginaExcenta=0.0;
				    for (int jjj = 0; jjj < detallesPagina.size(); jjj++) {
						totalMontoPagina = totalMontoPagina + detallesPagina.get(jjj).getTotal();
						CobrosCliente ventaImpresion = new CobrosCliente();
						 ventaImpresion.setCuentaCobrarCabecera(cobros.get(jjj).getCuentaCobrarCabecera());
						    ventaImpresion.setId(cobros.get(jjj).getId());
						    ventaImpresion.setFuncionario(cobros.get(jjj).getFuncionario());
						    ventaImpresion.getCuentaCobrarCabecera().setCliente(cobros.get(jjj).getCuentaCobrarCabecera().getCliente());
						    ventaImpresion.setCobrosClienteCabecera(cobros.get(jjj).getCobrosClienteCabecera());
						    ventaImpresion.setOperacionCaja(cobros.get(jjj).getOperacionCaja());
						    ventaImpresion.setTotal(cobros.get(jjj).getTotal());
						    listaVentaImpresion.add(ventaImpresion);
						    System.out.println("UNA FILA DE LA PAGINA" +listaVentaImpresion.get(jjj).getTotal());
				    }
				   
				  

				}
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
					try {
						
						ParametroTipoHoja p = parametroTipoHoja.getOne(1);
						if(p.getDescripcion().equals("A4")) {
			        		report.reportPDFImprimirA4(listaVentaImpresion, map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
		        		}
		        		if(p.getDescripcion().equals("CORTE")) {
			        		report.reportPDFImprimirLibreCorte(listaVentaImpresion, map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());

		        		}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			System.out.println("asdf  fdf asdf+asdf+a-sdf-asfasd+fa+sdf -asf-as+ f-asf-a + -a-fa -");
		}
		
	}
	
	public void pdfPrintss(String desc, int idVenta, int tipoDocumento, String tipoImpresora, int numeroterminal) {
		Reporte report=new Reporte();
		Impresora imNombreImpresora = impresoraRepository.getOne(8);
		ReporteConfig reportConfig = reporteConfigRepository.getOne(1);
		List<CobrosCliente> venta = getLista(idVenta);
		TerminalConfigImpresora t = new TerminalConfigImpresora();
		t= terminalRepository.consultarTerminal(numeroterminal);
		Map<String, Object> map = new HashMap<>();
		System.out.println("entroooo imprimir "+ tipoImpresora);
		
		if (desc.equals("true")) {
//			\reporte\venta-detalle-productoImpresionformato.jasper report name : ImpresionVentaFormato
//			\reporte\venta-detalle-productoMariSant.jasper report name : ImpresionVentaFormato
			if(tipoImpresora.equals("true")) {
				System.out.println("true impresora matricial");
				if(tipoDocumento==1) {System.out.println("factura");}
				if(tipoDocumento==1) {System.out.println("boleta");}
				if(tipoDocumento==1) {System.out.println("ticke");}
					Impresora im= new Impresora();
					im = impresoraRepository.getOne(8);
					if(im.isEstado()==true) {
						ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
						
						String urlReporte ="\\reporte\\"+reportConfig.getNombreSubReporte1()+".jasper";
						System.out.println("url SUBREPORT:  "+urlReporte+ " report name : "+reportConfig.getNombreReporte());
						map.put("urlSubRepor", urlReporte);
						map.put("tituloReporte", f.getTitulo());
						map.put("razonSocialReporte", f.getRazonSocial());
						map.put("descripcionMovimiento", f.getDescripcion());
						map.put("direccionReporte", f.getDireccion());
						map.put("telefonoReporte", f.getTelefono());
						
						try {
							ParametroTipoHoja p = parametroTipoHoja.getOne(1);
							if(p.getDescripcion().equals("A4")) {
				        		report.reportPDFImprimirA4(venta, map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
			        		}
			        		if(p.getDescripcion().equals("CORTE")) {
				        		report.reportPDFImprimirLibreCorte(venta, map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());

			        		}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}else {
						System.out.println("Dentro de la base de datos Reporte Config id=8 esta desabilitado. ");
					}
				
			}else if(tipoImpresora.equals("true")) {
				System.out.println("False impresora matricial");
			}
			
		}
	}
	

@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabecera/{id}")
public  ResponseEntity<?> getReporteCobrosClienteAll(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id) throws IOException{
	List<CobrosClienteCabecera> lis =new ArrayList<>();
	
	List<CobrosClienteCabecera> listado= listado(cobrosClienteCabeceraRepository.findByCuentaPorIdCliente(id));
	String nombreCliente="";
	if(listado.size()>0) {
		nombreCliente = listado.get(0).getCliente().getPersona().getNombre()+" "+listado.get(0).getCliente().getPersona().getApellido();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		Map<String, Object> map = new HashMap<>();
		map.put("org", ""+org.getNombre());
		map.put("direccion", ""+org.getDireccion());
		map.put("ruc", ""+org.getRuc());
		map.put("telefono", ""+org.getTelefono());
		map.put("ciudad", ""+org.getCiudad());
		map.put("pais", ""+org.getPais());
		map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		map.put("cliente", nombreCliente);

		report = new Reporte();
		report.reportPDFDescarga(listado, map, "ReporteCobrosClientesCabecera", response);

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}



}
public Date sumarDia(Date fecha, int hora) {
	Calendar calendar=Calendar.getInstance();
	calendar.setTime(fecha);
	calendar.add(Calendar.HOUR, hora);
	return calendar.getTime();
}

@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabecera/rango/{id}/{fechaI}/{fechaF}")
public  ResponseEntity<?> getReporteCobrosClienteRango(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException{
	List<CobrosClienteCabecera> lis =new ArrayList<>();
	
	//List<CobrosClienteCabecera> listado= listado(cobrosClienteCabeceraRepository.findByCuentaPorIdCliente(id));
	String nombreCliente="";
	
		//nombreCliente = listado.get(0).getCliente().getPersona().getNombre()+" "+listado.get(0).getCliente().getPersona().getApellido();
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();

		Calendar cc= Calendar.getInstance();
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI;
		System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
		try {
			fecI = formater.parse(fechaI);
		
		Date fecF=formater.parse(fechaF);
		System.out.println(fecF.getDate());
		fecF.setHours(23);
		fecF.setSeconds(59);
		fecI.setHours(0);
		fecI.setSeconds(1);
		System.out.println("hora final fechas::: "+fecF+ " hora inicio finbal: "+fecI);
		List<CobrosClienteCabecera> listado= listado(cobrosClienteCabeceraRepository.findByCobrosClientePorRango(id, fecI, fecF));
		if(listado.size()>0) {
		nombreCliente = listado.get(0).getCliente().getPersona().getNombre()+" "+listado.get(0).getCliente().getPersona().getApellido();

		Map<String, Object> map = new HashMap<>();
		map.put("org", ""+org.getNombre());
		map.put("direccion", ""+org.getDireccion());
		map.put("ruc", ""+org.getRuc());
		map.put("telefono", ""+org.getTelefono());
		map.put("ciudad", ""+org.getCiudad());
		map.put("pais", ""+org.getPais());
		map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		map.put("cliente", nombreCliente);
		map.put("fechaInicio", fechaI);
		map.put("fechaFin", fechaF);

		report = new Reporte();
		report.reportPDFDescarga(listado, map, "ReporteCobrosClientesCabeceraRango", response);
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}

		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	



}

public List<CobrosClienteCabecera> listado(List<CobrosClienteCabecera> lis){
	List<CobrosClienteCabecera> listadoRetorno = new ArrayList<>();
	for(CobrosClienteCabecera cue :lis) {
		CobrosClienteCabecera cuenta= new CobrosClienteCabecera();
		cuenta.setId(cue.getId());
		cuenta.setTotal(cue.getTotal()); 
		cuenta.getCliente().getPersona().setNombre(cue.getCliente().getPersona().getNombre());
		cuenta.getCliente().getPersona().setApellido(cue.getCliente().getPersona().getApellido());
		cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
		cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
		cuenta.setFecha(cue.getFecha());
		cuenta.setCobrosClientes(listaCobroClienteDetallado(cobrosClienteCabeceraRepository.findByCobrosDetalladoPorIdCabecera(cue.getId())));
		System.out.println("LISTA TOTAL DE DETALLE COBROS: "+cuenta.getCobrosClientes().size());
		listadoRetorno.add(cuenta);
	}
	return listadoRetorno;
}
private List<CobrosCliente> listaCobroClienteDetallado(List<CobrosCliente> lis){
	List<CobrosCliente> listaRetorno = new  ArrayList<CobrosCliente>();
	System.out.println("lisado detlle cobro cliente: ++++" + lis.size());
	for(CobrosCliente ob: lis) {
		CobrosCliente cob= new CobrosCliente();
		cob.setId(ob.getId());
		cob.getCuentaCobrarCabecera().setId(ob.getCuentaCobrarCabecera().getId());
		cob.getCuentaCobrarCabecera().getVenta().setId(ob.getCuentaCobrarCabecera().getVenta().getId());
		cob.getCuentaCobrarCabecera().setEntrega(ob.getCuentaCobrarCabecera().getEntrega());
		cob.getCuentaCobrarCabecera().setTotal(ob.getCuentaCobrarCabecera().getTotal());
		cob.getCuentaCobrarCabecera().setPagado(ob.getCuentaCobrarCabecera().getPagado());
		cob.getCuentaCobrarCabecera().setSaldo(ob.getCuentaCobrarCabecera().getSaldo());
		cob.getOperacionCaja().setId(ob.getOperacionCaja().getId());
		cob.getCuentaCobrarCabecera().setFraccionCuota(ob.getCuentaCobrarCabecera().getFraccionCuota());
		cob.setTotal(ob.getTotal());
		listaRetorno.add(cob);
		
		
		
	}
	return listaRetorno;
}



@RequestMapping(value="/descargarCobros/{idCobros}", method=RequestMethod.GET)
public ResponseEntity<?>  descargarPdfCobros(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int idCobros) throws IOException {
	List<CobrosClienteCabecera> listado = new ArrayList<>();
	CobrosClienteCabecera c= entityRepository.getCobrosCabeceraPorId(idCobros);
	System.out.println("COBROS ID: :::: "+idCobros);
	CobrosClienteCabecera cob= null;
	if(c==null) {
		return  new  ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRO NINGUN COBROS"), HttpStatus.CONFLICT);
	}else {
		cob= new CobrosClienteCabecera();
		cob.getFuncionario().getPersona().setNombre(c.getFuncionario().getPersona().getNombre()+" ");
		cob.getFuncionario().getPersona().setApellido(c.getFuncionario().getPersona().getApellido()+" ");
		cob.setFecha(c.getFecha());
		cob.setTotal(c.getTotal());
		cob.setId(c.getId());
		cob.getCliente().getPersona().setNombre(c.getCliente().getPersona().getNombre()+" "+c.getCliente().getPersona().getApellido());
		cob.getCliente().getPersona().setDireccion(c.getCliente().getPersona().getDireccion());
		cob.getCliente().getPersona().setTelefono(c.getCliente().getPersona().getTelefono());
		
		cob.setCobrosClientes(listaCobroClienteDetallado(entityRepository.getCobrosClientePorIdCabecera(idCobros)));
		
		listado.add(cob);
		System.out.println("LISTA.SIZE::::"+ listado.size());
	}
	Usuario usuario = usuarioService.findByUsername(authentication.getName());
	Cliente cl= clienteRepository.getIdCliente(c.getCliente().getId());
	Org org = orgRepository.findById(1).get();
	
	try {
		
		Map<String, Object> map = new HashMap<>();
		map.put("org", ""+org.getNombre());
		map.put("direccion", ""+org.getDireccion());
		map.put("ruc", ""+org.getRuc());
		map.put("telefono", ""+org.getTelefono());
		map.put("ciudad", ""+org.getCiudad());
		map.put("pais", ""+org.getPais());
		map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		map.put("cliente", ""+cl.getPersona().getNombre()+" "+cl.getPersona().getApellido());
		report = new Reporte();
		report.reportPDFDescarga(listado, map, "ReporteCobrosClientesPdf", response);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return  new  ResponseEntity<String>(HttpStatus.OK);
}



@RequestMapping(value="/descargarCobros/unificado/{id}", method=RequestMethod.GET)
public ResponseEntity<?>  resumenConcepto(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
	Usuario usuario = usuarioService.findByUsername(authentication.getName());
	Org org = orgRepository.findById(1).get();
	CobrosCliente pre= new CobrosCliente(); 
	pre=entityRepository.getOne(id);
	
	List<CobrosCliente> listado= new ArrayList<CobrosCliente>();
	listado.add(pre);
	Cliente cl= clienteRepository.getIdCliente(pre.getCobrosClienteCabecera().getCliente().getId());
	
	try {
	
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("cliente", ""+cl.getPersona().getNombre()+" "+cl.getPersona().getApellido());
			report = new Reporte();
			report.reportPDFDescarga(listado, map, "ReporteCobrosClientesUnificado", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
		
	} catch (Exception e) {
		e.printStackTrace();
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
	return  new  ResponseEntity<String>(HttpStatus.OK);
}

}
