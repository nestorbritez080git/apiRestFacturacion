package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
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
				
				listadoRetorno.add(cob);
			}
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	////////////////////////////////////////////// operacion cobros
	////////////////////////////////////////////// cliente/////////////////////////////////////////////////////////////

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

		System.out.println("Entro 1*******************");
		Object[][] obResult = new Object[cuentas.size()][5];
		int contador = 0;
		List<Object[][]> listRes = new ArrayList<>();
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		int idApertura = aperturaCajaRepository.getAperturaActivoCajaId(f.getId());
		Double cobradoActual = 0.0;
		for (int i = 0; i < cuentas.size(); i++) {
			CuentaCobrarCabecera cue = cuentas.get(i);
			System.out.println("cue.getCliente().getId(): "+cue.getCliente().getId());
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
				cuentaCobrarRepository.findByActualizarPagadoCuenta(cobros.getCuentaCobrarCabecera().getId(),
						op.getMonto());

				// CobrosCliente cobrosActualizar = entityRepository.findTop1ByOrderByIdDesc();
				// entityRepository.findByActualizarCobrosOperacion(cobrosActualizar.getId(),
				// operacioActualziar.getId());;

				Concepto c = new Concepto();
				c = conceptoRepository.findById(operacioActualziar.getConcepto().getId()).get();
				operacioActualziar.setMotivo(c.getDescripcion() + " REF.: " + cobros.getId());
				operacioActualziar.setTipo("ENTRADA");
				if (operacioActualziar.getTipoOperacion().getId() == 1) {
					aperturaCajaRepository.findByActualizarAperturaSaldo(operacioActualziar.getAperturaCaja().getId(),
							operacioActualziar.getMonto());
				}
				if (operacioActualziar.getTipoOperacion().getId() == 2) {
					aperturaCajaRepository.findByActualizarAperturaSaldoCheque(
							operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
				}
				if (operacioActualziar.getTipoOperacion().getId() == 3) {
					aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(
							operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
				}
				// aperturaCajaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(),
				// op.getMonto());
				operacionCajaRepository.save(operacioActualziar);

				// d: id, total: total, pagado: pagado, importe: importe
				obResult[i][0] = cue.getId();
				obResult[i][1] = cue.getTotal();
				obResult[i][2] = cue.getPagado();
				obResult[i][3] = cue.getSaldo();
				obResult[i][4] = cue.getCliente().getId();
				
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
					// cobradoActual = cobradoActual + cue.getSaldo();
					cobros.getFuncionario().setId(f.getId());
					cobros.getOperacionCaja().setId(operacioActualziar.getId());
					entityRepository.save(cobros);
					List<Object[]> det = cuentaCobrarDetalleRepository.consultarDetalleCuentaPorIdCabecera(cue.getId());
					List<CuentaCobrarDetalle> listadoDetalleActualizados = new ArrayList<CuentaCobrarDetalle>();
					for (int index = 0; index < det.size() && saldoPositivoaCobrar > 0; index++) {
						Object[] ob = det.get(index);
						montoCuotaActualizados = Double.parseDouble(ob[7].toString())
								- Double.parseDouble(ob[4].toString());
			
						if (saldoPositivoaCobrar >= montoCuotaActualizados) {
							saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
							montoIMporteActualizados = montoCuotaActualizados;
							cobradoActual = cobradoActual + montoIMporteActualizados;
						} else {
							montoIMporteActualizados = saldoPositivoaCobrar;
							saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaActualizados;
							cobradoActual = cobradoActual + montoIMporteActualizados;
						}
						CuentaCobrarDetalle detalleCuenta = new CuentaCobrarDetalle();
						detalleCuenta.getCuentaCobrarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
						detalleCuenta.setNumeroCuota(Integer.parseInt(ob[1].toString()));
						if (ob[2].toString() == null) {
							detalleCuenta.setFechaVencimiento(null);
						} else {
							detalleCuenta
									.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
						}
						detalleCuenta.setMonto(Double.parseDouble(ob[3].toString()));
						detalleCuenta.setImporte(montoIMporteActualizados + Double.parseDouble(ob[4].toString()));
						detalleCuenta.setInteresMora(Double.parseDouble(ob[5].toString()));
						detalleCuenta.setId(Integer.parseInt(ob[6].toString()));
						detalleCuenta.setSubTotal(Double.parseDouble(ob[7].toString()));
						detalleCuenta.getCuentaCobrarCabecera().setId(Integer.parseInt(ob[8].toString()));
						listadoDetalleActualizados.add(detalleCuenta);

					}

					cuentaCobrarDetalleRepository.saveAll(listadoDetalleActualizados);

					cuentaCobrarRepository.findByActualizarPagadoCuenta(cobros.getCuentaCobrarCabecera().getId(),
							op.getMonto());
					Concepto c = new Concepto();
					c = conceptoRepository.findById(operacioActualziar.getConcepto().getId()).get();
					operacioActualziar.setMotivo(c.getDescripcion() + " REF.: " + cobros.getId());
					operacioActualziar.setTipo("ENTRADA");
					if (operacioActualziar.getTipoOperacion().getId() == 1) {
						aperturaCajaRepository.findByActualizarAperturaSaldo(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
					}
					if (operacioActualziar.getTipoOperacion().getId() == 2) {
						aperturaCajaRepository.findByActualizarAperturaSaldoCheque(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
					}
					if (operacioActualziar.getTipoOperacion().getId() == 3) {
						aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(
								operacioActualziar.getAperturaCaja().getId(), operacioActualziar.getMonto());
					}
					// aperturaCajaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(),
					// op.getMonto());
					operacionCajaRepository.save(operacioActualziar);

					obResult[i][0] = cue.getId();
					obResult[i][1] = cue.getTotal();
					obResult[i][2] = cue.getPagado();
					obResult[i][3] = cobros.getTotal();
					obResult[i][4] = cue.getCliente().getId();
					
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
		Object[][] obResultadoNuevo = new Object[contador][5];
		List<Object[][]> listResNuevo = new ArrayList<>();
		for (int i = 0; i < obResult.length; i++) {
			if (obResult[i][0] != null) {
				obResultadoNuevo[i][0] = obResult[i][0];
				obResultadoNuevo[i][1] = obResult[i][1];
				obResultadoNuevo[i][2] = obResult[i][2];
				obResultadoNuevo[i][3] = obResult[i][3];
				obResultadoNuevo[i][4] = obResult[i][4];

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
		if (siImprimir.equals("true")) {

			Reporte report = new Reporte();
			Cliente cl = clienteRepository.getIdCliente(cobros.get(0).getCuentaCobrarCabecera().getCliente().getId());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setNombre(cl.getPersona().getNombre()+ " "+cl.getPersona().getApellido());
			cobros.get(0).getCuentaCobrarCabecera().getCliente().getPersona().setCedula(cl.getPersona().getCedula());
			cobros.get(0).setFecha(new Date());
			TerminalConfigImpresora t = new TerminalConfigImpresora();
			t= terminalRepository.consultarTerminal(numeroTerminal);
			if (t==null) {
				System.out.println("Se debe cargar numero terminal dentro de la base de datos");
			}else {
				ReporteConfig reportConfig = reporteConfigRepository.getOne(3);
				
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
						report.reportPDFImprimir(cobros, map, reportConfig.getNombreReporte(), t.getNombreImpresora());	
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			System.out.println("asdf  fdf asdf+asdf+a-sdf-asfasd+fa+sdf -asf-as+ f-asf-a + -a-fa -");
		}
		
	}
	
	public void pdfPrintss(String desc, int idVenta, int tipoDocumento, String tipoImpresora) {
		Reporte report=new Reporte();
		Impresora imNombreImpresora = impresoraRepository.getOne(8);
		ReporteConfig reportConfig = reporteConfigRepository.getOne(1);
		List<CobrosCliente> venta = getLista(idVenta);
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
							report.reportPDFImprimir(venta, map, reportConfig.getNombreReporte(), imNombreImpresora.getDescripcion());				

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
