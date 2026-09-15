package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.CobrosTicketDTO;
import com.bisontecfacturacion.security.auxiliar.CuentaCliente;
import com.bisontecfacturacion.security.auxiliar.DTOticketCobros;
import com.bisontecfacturacion.security.auxiliar.OperacionTicketDTO;
import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.contabilidad.controller.AsientoContableServices;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDTO;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteCabeceraRepository;
import com.bisontecfacturacion.security.repository.CobrosClienteRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaCabeceraRepository;
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
	private OperacionCajaCabeceraRepository operacionCajaCabeceraRepository;

	@Autowired
	private ImpresoraRepository impresoraRepository;

	@Autowired
	private AsientoContableServices asienotContableServices; 

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
			ventas.getCuentaCobrarCabecera().getCliente().getPersona().setCedula(ob[5].toString());
			ventas.getFuncionario().getPersona().setCedula(ob[6].toString());
			venta.add(ventas);
		}
		return venta;

	}


	@RequestMapping(method = RequestMethod.GET, value = "/buscar/cabecera/{descripcion}")
	public List<CobrosClienteCabecera> getBuscarNombreApellidoClienteCobrosCabecera(@PathVariable String descripcion) {
		List<Object[]> objeto = entityRepository.getBuscarClienteNombreApellidoCabecera("%" + descripcion.toUpperCase() + "%");
		System.out.println();
		List<CobrosClienteCabecera> venta = new ArrayList<>();
		for (Object[] ob : objeto) {
			CobrosClienteCabecera ventas = new CobrosClienteCabecera();
			ventas.setId(Integer.parseInt(ob[0].toString()));
			ventas.getFuncionario().getPersona().setNombre(ob[1].toString());
			ventas.getCliente().getPersona().setNombre(ob[2].toString());
			ventas.setTotal(Double.parseDouble(ob[3].toString()));
			String fech = ob[4].toString();
			ventas.setFecha(FechaUtil.convertirFechaStringADateUtil(fech));
			ventas.getCliente().getPersona().setCedula(ob[5].toString());
			ventas.getFuncionario().getPersona().setCedula(ob[6].toString());
			venta.add(ventas);
		}
		return venta;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscarCabecera/detalleCobros/{idDetalleCobros}")
	public ResponseEntity<?> getCobrosCabceraPorDetalleCobrosId(@PathVariable int idDetalleCobros) {
		try {
			// Llamada al repositorio
			CobrosClienteCabecera cabeceraReturn = null;
			CobrosClienteCabecera cabecera = entityRepository.getCabeceraCobrosPorCobrosDetalle(idDetalleCobros);

			if (cabecera == null) {
				return ResponseEntity.status(404).body("Cabecera no encontrada para el detalle de cobro ID: " + idDetalleCobros);
			}else {
				cabeceraReturn = new CobrosClienteCabecera();
				cabeceraReturn.setId(cabecera.getId());
			}

			return new ResponseEntity<>(cabeceraReturn, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			// Manejo de error
			return new ResponseEntity<>(new CustomerErrorType("Ocurrió un error al buscar la cabecera: "+e.getMessage()), HttpStatus.CONFLICT);
		}
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


	private ResponseEntity<CustomerErrorType> error(String mensaje) {
		return new ResponseEntity<>(new CustomerErrorType(mensaje), HttpStatus.CONFLICT);
	}
	private ResponseEntity<?> validarCaja(List<OperacionCaja> operacionCajaLista) {

		if (operacionCajaLista == null || operacionCajaLista.isEmpty()) {
			return error("No existen formas de pago para validar caja");
		}

		for (OperacionCaja op : operacionCajaLista) {

			if (op.getAperturaCaja() == null ||
					op.getAperturaCaja().getId() <= 0) {
				return error("El funcionario no posee una apertura de caja asignada");
			}

			AperturaCaja aper = aperturaCajaRepository
					.getAperturaCajaPorIdCaja(
							op.getAperturaCaja().getId()
							);

			if (aper == null) {
				return error("EL FUNCIONARIO NO POSEE UNA APERTURA CAJA A SU NOMBRE");
			}

			// 🔥 OPCIONAL PRO (MUY RECOMENDADO)
			// if (!aper.getEstado().equals("ABIERTO")) {
			//  return error("La apertura de caja no está activa");
			//}
		}

		return null;
	}




	@Transactional
	public List<OperacionCaja> procesarOperacionCaja(CobrosClienteCabecera ent, List<OperacionCaja> listaOperacion) {
		List<OperacionCaja> resultado = new ArrayList<>();
		if (listaOperacion == null || listaOperacion.isEmpty()) {
			throw new RuntimeException("No existen operaciones de caja para procesar");
		}
		// 🔥 Crear cabecera si todavía no existe
		OperacionCajaCabecera cabecera = new OperacionCajaCabecera();
		cabecera.setFecha(new Date());
		cabecera.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cabecera.setReferenciaOperacion(ent.getId());
		cabecera.getAperturaCaja().setId((listaOperacion.get(0).getAperturaCaja().getId()));
		Concepto c = conceptoRepository.findById(listaOperacion.get(0).getConcepto().getId()).orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		cabecera.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
		cabecera.getConcepto().setId(c.getId());
		cabecera.setTipo("ENTRADA");
		AperturaCaja ape= aperturaCajaRepository.getAperturaCajaPorIdCaja(listaOperacion.get(0).getAperturaCaja().getId());

		OperacionCajaCabecera savedCabeceraOperacion = operacionCajaCabeceraRepository.save(cabecera);

		for (OperacionCaja ope : listaOperacion) {
			ope.setTipo("ENTRADA");
			ope.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
			ope.setReferenciaOperacion(ent.getId());
			ope.setFecha(new Date());
			ope.setAperturaCaja(ape);
			ope.getOperacionCajaCabecera().setId(savedCabeceraOperacion.getId());
			// 🔥 Actualizar saldos según tipo operación
			if (ope.getTipoOperacion().getId() == 1) {
				aperturaCajaRepository.findByActualizarAperturaSaldo(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			if (ope.getTipoOperacion().getId() == 2) {
				aperturaCajaRepository.findByActualizarAperturaSaldoCheque(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			if (ope.getTipoOperacion().getId() == 3) {
				aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			OperacionCaja saved = operacionCajaRepository.save(ope);

			resultado.add(saved);
		}
		//actualiza el id de la operacion en referencia
		//entityRepository.findByActualizarVentaOperacion(ent.getId(), cabecera.getId());
		return resultado;
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/idcuenta/{idCuenta}/{monto}/{idUser}")
	public ResponseEntity<?> operacionCobrosPorCuenta(
			@RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista,
			@PathVariable int idCuenta, 
			@PathVariable Double monto,
			@PathVariable int idUser 
			) throws Exception {
		// 1️⃣ Validar caja
		CuentaCobrarCabecera cuentaCabecera =  cuentaCobrarRepository.getCuentaCabeceraPorId(idCuenta);
		//List<OperacionCaja> lisRetorno = new  ArrayList<>();
		System.out.println("ENTROOO COBROS ID-CLIENTE RUTA");
		// 2️⃣ Obtener funcionario
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		// 3️⃣ Crear cabecera de cobro
		CobrosClienteCabecera cab = new CobrosClienteCabecera();
		cab.getCliente().setId(cuentaCabecera.getCliente().getId());
		cab.getFuncionario().setId(f.getId());
		cab.setFecha(new Date());
		cab.setTotal(monto);
		CobrosClienteCabecera savedCabecera = cobrosClienteCabeceraRepository.save(cab);
		System.out.println("monto ccc operacio: " + monto);
		//lisRetorno = procesarOperacionCaja(savedCabecera, operacionCajaLista);
		// 4️⃣ Verificar si contabilidad está activa
		Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);

		if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado()) {

			AsientoContableDTO dto = new AsientoContableDTO();

			dto.setConceptoId(5);
			dto.setTipoReferencia("COBROS CLIENTES");

			Map<String, BigDecimal> mon = new HashMap<>();
			mon.put("BASE_COBRO", BigDecimal.valueOf(monto));

			dto.setMontos(mon);

			dto.setReferenciaId(savedCabecera.getId());
			dto.setFuncionarioRegistroId(savedCabecera.getFuncionario().getId());
			dto.setFuncionarioModificacionId(savedCabecera.getFuncionario().getId());

			ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);

			Object body = retorString.getBody();

			if (body instanceof CustomerErrorType) {

				String mensaje = ((CustomerErrorType) body).getErrorMessage();

				if (!"SAVE".equals(mensaje)) {
					return new ResponseEntity<>(
							new CustomerErrorType("ERROR AL GENERAR ASIENTO CONTABLE DE COBRO"),
							HttpStatus.CONFLICT
							);
				}

				System.out.println("Asiento contable guardado correctamente");
			}
		}
		// 5️⃣ Obtener cuentas del cliente
		List<CuentaCobrarCabecera> lista = new ArrayList<>();
		lista.add(cuentaCabecera);
		// 6️⃣ Procesar cobro
		return operacion(lista, savedCabecera, monto,idUser, operacionCajaLista);

	}


	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/idcliente/{idcliente}/{monto}/{idUser}")
	public ResponseEntity<?> operacionCobrosPorCliente(
			@RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista,
			@PathVariable int idcliente,
			@PathVariable Double monto,
			@PathVariable int idUser) throws Exception {
		// 1️⃣ Validar caja

		//List<OperacionCaja> lisRetorno = new  ArrayList<>();
		System.out.println("ENTROOO COBROS ID-CLIENTE RUTA");
		// 2️⃣ Obtener funcionario
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		// 3️⃣ Crear cabecera de cobro
		CobrosClienteCabecera cab = new CobrosClienteCabecera();
		cab.getCliente().setId(idcliente);
		cab.getFuncionario().setId(f.getId());
		cab.setFecha(new Date());
		cab.setTotal(monto);
		CobrosClienteCabecera savedCabecera = cobrosClienteCabeceraRepository.save(cab);
		System.out.println("monto ccc operacio: " + monto);
		//lisRetorno = procesarOperacionCaja(savedCabecera, operacionCajaLista);
		// 4️⃣ Verificar si contabilidad está activa
		Impresora ipmCfgContabilidad = impresoraRepository.getOne(23);

		if (ipmCfgContabilidad != null && ipmCfgContabilidad.isEstado()) {

			AsientoContableDTO dto = new AsientoContableDTO();

			dto.setConceptoId(5);
			dto.setTipoReferencia("COBROS CLIENTES");

			Map<String, BigDecimal> mon = new HashMap<>();
			mon.put("BASE_COBRO", BigDecimal.valueOf(monto));

			dto.setMontos(mon);

			dto.setReferenciaId(savedCabecera.getId());
			dto.setFuncionarioRegistroId(savedCabecera.getFuncionario().getId());
			dto.setFuncionarioModificacionId(savedCabecera.getFuncionario().getId());

			ResponseEntity<?> retorString = asienotContableServices.guardarAsiento(dto);

			Object body = retorString.getBody();

			if (body instanceof CustomerErrorType) {

				String mensaje = ((CustomerErrorType) body).getErrorMessage();

				if (!"SAVE".equals(mensaje)) {
					return new ResponseEntity<>(
							new CustomerErrorType("ERROR AL GENERAR ASIENTO CONTABLE DE COBRO"),
							HttpStatus.CONFLICT
							);
				}

				System.out.println("Asiento contable guardado correctamente");
			}
		}
		// 5️⃣ Obtener cuentas del cliente
		List<CuentaCobrarCabecera> listCuentaTotal = cuentaCobrarRepository.findByCuentaPorIdClienteACobrars(idcliente);
		// 6️⃣ Procesar cobro
		return operacion(listCuentaTotal, savedCabecera, monto,idUser, operacionCajaLista);
	}
	@Transactional
	public ResponseEntity<?> operacion(
			List<CuentaCobrarCabecera> cuentas,
			CobrosClienteCabecera ent,
			Double monto,
			int idUser,
			List<OperacionCaja> listCajaOperacion) {

		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		List<OperacionCaja> resultado = new ArrayList<>();
		List<CobrosTicketDTO> resultDTOCobros  = new ArrayList<>();
		List<OperacionTicketDTO> resultDTOOperacion = new ArrayList<>();

		List<CobrosCliente> resultadoCobros = new ArrayList<>();

		// Guardar cabecera de operación
		OperacionCajaCabecera cab = new OperacionCajaCabecera();
		cab.setFecha(new Date());
		cab.setMonto(listCajaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cab.setReferenciaOperacion(ent.getId());
		cab.setAperturaCaja(listCajaOperacion.get(0).getAperturaCaja());
		Concepto c = conceptoRepository.findById(listCajaOperacion.get(0).getConcepto().getId())
				.orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		cab.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
		cab.setConcepto(c);
		cab.setTipo("ENTRADA");
		OperacionCajaCabecera savedCabecera = operacionCajaCabeceraRepository.save(cab);

		// Ordenar cuentas
		int indexCuenta = 0;

		for (OperacionCaja ope : listCajaOperacion) {

			ope.setTipo("ENTRADA");
			ope.setMotivo(c.getDescripcion() + " REF.: " + ent.getId());
			ope.setReferenciaOperacion(ent.getId());
			ope.setFecha(new Date());
			ope.setOperacionCajaCabecera(savedCabecera);

			OperacionCaja saved = operacionCajaRepository.save(ope);
			resultado.add(saved);

			double montoRestanteOperacion = ope.getMonto();

			while (montoRestanteOperacion > 0 && indexCuenta < cuentas.size()) {

				CuentaCobrarCabecera cuenta = cuentas.get(indexCuenta);

				double saldoCuenta = cuenta.getSaldo();
				if (saldoCuenta <= 0) {
					indexCuenta++;
					continue;
				}

				double importeAplicar = Math.min(saldoCuenta, montoRestanteOperacion);

				// CobrosCliente
				CobrosCliente cobros = new CobrosCliente();
				cobros.setCobrosClienteCabecera(ent);
				cobros.setFuncionario(f);
				cobros.setOperacionCaja(saved);
				cobros.setTotal(importeAplicar);
				cobros.setCuentaCobrarCabecera(cuenta);
				CobrosCliente saveCobrosCliente = entityRepository.save(cobros);
				resultadoCobros.add(saveCobrosCliente);
				// Actualizar cuotas reales
				List<CuentaCobrarDetalle> detalles = cuentaCobrarDetalleRepository.getCuentaCobrarDetalles(cuenta.getId());

				double saldoDistribuir = importeAplicar;
				for (CuentaCobrarDetalle detalle : detalles) {
					if (saldoDistribuir <= 0) break;
					double saldoDetalle = detalle.getSubTotal() - detalle.getImporte();
					if (saldoDetalle <= 0) continue;

					double montoAplicarDetalle = Math.min(saldoDetalle, saldoDistribuir);
					detalle.setImporte(detalle.getImporte() + montoAplicarDetalle);
					saldoDistribuir -= montoAplicarDetalle;
				}
				cuentaCobrarDetalleRepository.saveAll(detalles);
				// Actualizar cabecera de cuenta
				cuenta.setPagado(cuenta.getPagado() + importeAplicar);
				cuenta.setSaldo(cuenta.getSaldo() - importeAplicar);
				cuentaCobrarRepository.save(cuenta);
				montoRestanteOperacion -= importeAplicar;
				if (cuenta.getSaldo() <= 0) indexCuenta++;
			}
			// Actualizar saldos de caja
			if (ope.getTipoOperacion().getId() == 1)
				aperturaCajaRepository.findByActualizarAperturaSaldo(ope.getAperturaCaja().getId(), ope.getMonto());
			else if (ope.getTipoOperacion().getId() == 2)
				aperturaCajaRepository.findByActualizarAperturaSaldoCheque(ope.getAperturaCaja().getId(), ope.getMonto());
			else if (ope.getTipoOperacion().getId() == 3)
				aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(ope.getAperturaCaja().getId(), ope.getMonto());
		}
		// =====================================================
		// ARMAR LAS CUENTAS COBRADAS
		// Una misma cuenta puede recibir varios tipos de pago.
		// Ejemplo:
		// EFECTIVO = 1500
		// TARJETA  = 200
		//
		// Entonces agrupamos por cuenta y sumamos el importe.
		// =====================================================

		Map<Integer, CobrosTicketDTO> cuentasMap = new LinkedHashMap<>();

		for (CobrosCliente cobros : resultadoCobros) {
		    if (cobros.getCuentaCobrarCabecera() == null) {
		        continue;
		    }
		    Integer idCuenta =  cobros.getCuentaCobrarCabecera().getId();

		    CobrosTicketDTO ccc =  cuentasMap.get(idCuenta);

		    // =============================================
		    // PRIMER REGISTRO DE ESTA CUENTA
		    // =============================================
		    if (ccc == null) {
		        ccc = new CobrosTicketDTO();
		        ccc.setIdCuenta(idCuenta);
		        ccc.setNroDocumento(cobros.getCuentaCobrarCabecera().getVenta().getNroDocumento());
		        // Total pagado acumulado de la cuenta
		        ccc.setPagado( cobros.getCuentaCobrarCabecera().getPagado());
		        // Importe aplicado en ESTE cobro
		        ccc.setImporte(cobros.getTotal() != null ? cobros.getTotal() : 0.0 );
		        ccc.setSaldo(cobros.getCuentaCobrarCabecera().getSaldo());
		        ccc.setTotal(cobros.getCuentaCobrarCabecera().getTotal());
		        ccc.setTotalInteres(cobros.getCuentaCobrarCabecera().getTotalInteresMora());
		        ccc.setTipoDocumento(cobros.getCuentaCobrarCabecera().getVenta().getDocumento().getDescripcion());
		        ccc.setIdVenta( cobros.getCuentaCobrarCabecera().getVenta() .getId());
		        cuentasMap.put(idCuenta, ccc);
		    } else {
		        // =============================================
		        // LA MISMA CUENTA FUE PAGADA CON OTRO
		        // TIPO DE PAGO.
		        //
		        // Sumamos el importe aplicado.
		        // =============================================

		        double importeAnterior = ccc.getImporte() != null  ? ccc.getImporte()        : 0.0;

		        double importeNuevo = cobros.getTotal() != null   ? cobros.getTotal()
		                        : 0.0;

		        ccc.setImporte(
		                importeAnterior + importeNuevo
		        );
		    }
		}
		// Pasamos las cuentas agrupadas al listado final
		resultDTOCobros.addAll(cuentasMap.values());
		for (OperacionCaja operacion: resultado ) {
			OperacionTicketDTO ccc = new OperacionTicketDTO();
			ccc.setDescripcion(operacion.getTipoOperacion().getDescripcion());
			ccc.setMonto(operacion.getMonto());
			ccc.setEfectivo(operacion.getEfectivo());
			ccc.setVuelto(operacion.getVuelto());
			ccc.setId(operacion.getTipoOperacion().getId());
			resultDTOOperacion.add(ccc);
		}

		DTOticketCobros DTO= new DTOticketCobros();
		DTO.setListCobros(resultDTOCobros);
		DTO.setListOperacion(resultDTOOperacion);
		return ResponseEntity.ok(DTO);
	}
	@RequestMapping(
	        method = RequestMethod.GET,
	        value = "/reimpresionticketCobros/{idCobro}"
	)
	public ResponseEntity<?> getReimprimirTicketCobros(
	        @PathVariable int idCobro) {

	    List<CobrosTicketDTO> resultDTOCobros =
	            new ArrayList<>();

	    List<OperacionTicketDTO> resultDTOOperacion =
	            new ArrayList<>();

	    // =====================================================
	    // 1. BUSCAR TODOS LOS DETALLES DEL COBRO
	    // =====================================================

	    List<CobrosCliente> resultadoCobros =
	            entityRepository
	                    .getCobrosClientePorIdCabecera(idCobro);

	    if (resultadoCobros == null || resultadoCobros.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    // =====================================================
	    // 2. RECONSTRUIR LAS CUENTAS COBRADAS
	    //
	    // Una misma cuenta puede tener varios CobrosCliente
	    // porque puede pagarse con:
	    //
	    // EFECTIVO  1500
	    // TARJETA    200
	    //
	    // Por eso agrupamos por cuenta y sumamos el importe.
	    // =====================================================

	    Map<Integer, CobrosTicketDTO> cuentasProcesadas =   new LinkedHashMap<>();

	    for (CobrosCliente cobros : resultadoCobros) {

	        if (cobros.getCuentaCobrarCabecera() == null) {
	            continue;
	        }

	        Integer idCuenta =
	                cobros.getCuentaCobrarCabecera().getId();

	        CobrosTicketDTO ccc =
	                cuentasProcesadas.get(idCuenta);

	        // ============================================
	        // PRIMER COBRO DE ESTA CUENTA
	        // ============================================

	        if (ccc == null) {

	            ccc = new CobrosTicketDTO();

	            ccc.setIdCuenta(idCuenta);

	            ccc.setNroDocumento(
	                    cobros.getCuentaCobrarCabecera()
	                            .getVenta()
	                            .getNroDocumento()
	            );

	            // Pagado acumulado de la cuenta
	            ccc.setPagado(
	                    cobros.getCuentaCobrarCabecera()
	                            .getPagado()
	            );

	            // Importe aplicado EN ESTE COBRO
	            ccc.setImporte(
	                    cobros.getTotal() != null
	                            ? cobros.getTotal()
	                            : 0.0
	            );

	            ccc.setSaldo(
	                    cobros.getCuentaCobrarCabecera()
	                            .getSaldo()
	            );

	            ccc.setTotal(
	                    cobros.getCuentaCobrarCabecera()
	                            .getTotal()
	            );

	            ccc.setTotalInteres(
	                    cobros.getCuentaCobrarCabecera()
	                            .getTotalInteresMora()
	            );

	            ccc.setTipoDocumento(
	                    cobros.getCuentaCobrarCabecera()
	                            .getVenta()
	                            .getDocumento()
	                            .getDescripcion()
	            );

	            ccc.setIdVenta(
	                    cobros.getCuentaCobrarCabecera()
	                            .getVenta()
	                            .getId()
	            );

	            cuentasProcesadas.put(idCuenta, ccc);

	        } else {

	            // ============================================
	            // LA MISMA CUENTA APARECE OTRA VEZ
	            //
	            // Ejemplo:
	            //
	            // Efectivo = 1500
	            // Tarjeta  = 200
	            //
	            // Importe total aplicado = 1700
	            // ============================================

	            double importeActual =
	                    ccc.getImporte() != null
	                            ? ccc.getImporte()
	                            : 0.0;

	            double importeNuevo =
	                    cobros.getTotal() != null
	                            ? cobros.getTotal()
	                            : 0.0;

	            ccc.setImporte(
	                    importeActual + importeNuevo
	            );
	        }
	    }

	    resultDTOCobros.addAll(
	            cuentasProcesadas.values()
	    );


	    // =====================================================
	    // 3. OBTENER LAS OPERACIONES DEL COBRO
	    //
	    // Aquí conservamos cada forma de pago.
	    // =====================================================

	    Set<Integer> operacionesProcesadas =
	            new HashSet<>();

	    for (CobrosCliente cobros : resultadoCobros) {

	        if (cobros.getOperacionCaja() == null) {
	            continue;
	        }

	        OperacionCaja operacion =
	                cobros.getOperacionCaja();

	        Integer idOperacion =
	                operacion.getId();

	        // Evitar repetir la misma operación
	        if (operacionesProcesadas.contains(idOperacion)) {
	            continue;
	        }

	        if (operacion.getTipoOperacion() == null) {
	            continue;
	        }

	        OperacionTicketDTO ccc =
	                new OperacionTicketDTO();

	        ccc.setDescripcion(
	                operacion.getTipoOperacion()
	                        .getDescripcion()
	        );

	        ccc.setMonto(
	                operacion.getMonto()
	        );

	        ccc.setEfectivo(
	                operacion.getEfectivo()
	        );

	        ccc.setVuelto(
	                operacion.getVuelto()
	        );

	        ccc.setId(
	                operacion.getTipoOperacion()
	                        .getId()
	        );

	        resultDTOOperacion.add(ccc);

	        operacionesProcesadas.add(idOperacion);
	    }


	    // =====================================================
	    // 4. ARMAR RESPUESTA
	    // =====================================================

	    DTOticketCobros DTO =
	            new DTOticketCobros();

	    DTO.setListCobros(resultDTOCobros);

	    DTO.setListOperacion(resultDTOOperacion);

	    return ResponseEntity.ok(DTO);
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
			t= terminalRepository.consultarTerminalPorNumeros(numeroTerminal);
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
		t= terminalRepository.consultarTerminalPorNumeros(numeroterminal);
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

	@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabeceraListado/{id}")
	public  List<CobrosClienteCabecera> getReporteCobrosClienteListado(OAuth2Authentication authentication,@PathVariable int id) throws IOException{
		List<CobrosClienteCabecera> listado= listado(cobrosClienteCabeceraRepository.findByCobrosCabeceraPorIdCliente(id));
		return  listado;
	}	

	@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabecera/{id}")
	public  ResponseEntity<?> getReporteCobrosClienteAll(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id) throws IOException{
		List<CobrosClienteCabecera> lis =new ArrayList<>();

		List<CobrosClienteCabecera> listado= listado(cobrosClienteCabeceraRepository.findByCobrosCabeceraPorIdCliente(id));
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

	@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabeceraListado/rango/{id}/{fechaI}/{fechaF}")
	public  List<CobrosClienteCabecera> getReporteCobrosClienteRangoListado(OAuth2Authentication authentication,@PathVariable int id, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException{
		List<CobrosClienteCabecera> lis =new ArrayList<>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Date fecI = formatter.parse(fechaI);
			Date fecF = formatter.parse(fechaF);

			// INICIO DEL DÍA
			Calendar calStart = Calendar.getInstance();
			calStart.setTime(fecI);
			calStart.set(Calendar.HOUR_OF_DAY, 0);
			calStart.set(Calendar.MINUTE, 0);
			calStart.set(Calendar.SECOND, 0);
			calStart.set(Calendar.MILLISECOND, 0);
			fecI = calStart.getTime();

			// FIN DEL DÍA
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(fecF);
			calEnd.set(Calendar.HOUR_OF_DAY, 23);
			calEnd.set(Calendar.MINUTE, 59);
			calEnd.set(Calendar.SECOND, 59);
			calEnd.set(Calendar.MILLISECOND, 999);
			fecF = calEnd.getTime();
			System.out.println("Buscando desde: " + fecI);
			System.out.println("Buscando hasta: " + fecF);
			lis= listado(cobrosClienteCabeceraRepository.findByCobrosClientePorRango(id, fecI, fecF));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lis;
	}
	@RequestMapping(method = RequestMethod.GET, value="/reporteCobrosClienteCabecera/rango/{id}/{fechaI}/{fechaF}/{tipo}")
	public  ResponseEntity<?> getReporteCobrosClienteRango(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id, @PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int tipo) throws IOException{
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
				if (tipo==1) {
					report.reportPDFDescarga(listado, map, "ReporteCobrosClientesCabeceraRango", response);
				}else if(tipo==2) {
					report.reportPDFDescarga(listado, map, "ReporteCobrosClientesCabeceraRangoDetallado", response);

				}

			}else {
				return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);




	}

	public List<CobrosClienteCabecera> listado(List<CobrosClienteCabecera> lis){
		System.out.println("LISTADO SIZE: "+lis.size());
		List<CobrosClienteCabecera> listadoRetorno = new ArrayList<>();
		for(CobrosClienteCabecera cue :lis) {
			CobrosClienteCabecera cuenta= new CobrosClienteCabecera();
			cuenta.setId(cue.getId());
			cuenta.setTotal(cue.getTotal()); 
			cuenta.getCliente().getPersona().setNombre(cue.getCliente().getPersona().getNombre());
			cuenta.getCliente().getPersona().setApellido(cue.getCliente().getPersona().getApellido());
			cuenta.getCliente().getPersona().setCedula(cue.getCliente().getPersona().getCedula());
			cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
			cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
			cuenta.getFuncionario().getPersona().setCedula(cue.getFuncionario().getPersona().getCedula());
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
			cob.getCobrosClienteCabecera().setId(ob.getCobrosClienteCabecera().getId());
			cob.getCuentaCobrarCabecera().setId(ob.getCuentaCobrarCabecera().getId());
			cob.getCuentaCobrarCabecera().getVenta().setId(ob.getCuentaCobrarCabecera().getVenta().getId());
			cob.getCuentaCobrarCabecera().getVenta().setFechaFactura(ob.getCuentaCobrarCabecera().getVenta().getFechaFactura());
			cob.getCuentaCobrarCabecera().setEntrega(ob.getCuentaCobrarCabecera().getEntrega());
			cob.getCuentaCobrarCabecera().getVenta().setTotal(ob.getCuentaCobrarCabecera().getVenta().getTotal());
			//		cob.getCuentaCobrarCabecera().getVenta().set
			cob.getCuentaCobrarCabecera().setTotal(ob.getCuentaCobrarCabecera().getTotal());
			cob.getCuentaCobrarCabecera().setPagado(ob.getCuentaCobrarCabecera().getPagado());
			cob.getCuentaCobrarCabecera().setSaldo(ob.getCuentaCobrarCabecera().getSaldo());
			cob.getCuentaCobrarCabecera().setFechaVencimiento(ob.getCuentaCobrarCabecera().getFechaVencimiento());
			cob.getOperacionCaja().setId(ob.getOperacionCaja().getId());
			cob.getOperacionCaja().getAperturaCaja().setId(ob.getOperacionCaja().getAperturaCaja().getId());
			cob.getOperacionCaja().getTipoOperacion().setDescripcion(ob.getOperacionCaja().getTipoOperacion().getDescripcion());
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
