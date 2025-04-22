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

import javax.print.DocFlavor.READER;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OrdenPagare;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
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
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrdenPagareRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;

import net.sf.jasperreports.engine.JRException;

@EnableAsync
@Transactional
@RestController
@RequestMapping("cuentaCobrar")
public class CuentaCobrarController {
	private Reporte report;
	
	@Autowired
	private CuentaAcobrarRepository entityRepository;
	
	@Autowired
	private OrdenPagareRepository ordenPagareRepository;
	
	@Autowired
	private CobrosClienteCabeceraRepository cobrosClienteCabeceraRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private CuentaAcobrarDetalleRepository detalleRepository;

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private CobrosClienteRepository cobrosClienteRepository;

	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;

	@RequestMapping(method=RequestMethod.POST, value="/detalle")
	public void saveDetalle(@RequestBody List<CuentaCobrarDetalle> detalle){
//		System.out.println("asdfasdfasdf ********* adasfsadfa s 0+"+detalle.get(0).getCuentaCobrarCabecera().getId());
		int idCabecera=0;
		//CuentaCobrarCabecera cuu= entityRepository.getCuentaCabecera(detalle.get(0).getCuentaCobrarCabecera().getId());
		CuentaCobrarCabecera cuc = entityRepository.findTop1ByOrderByIdDesc();
		Double totalImportePorCuenta=0.0;
		for(CuentaCobrarDetalle ob: detalle){
			if(ob.getId() != 0){
				ob.setFechaPago(new Date());
//				ob.getCuentaCobrarCabecera().setId(ob.getCuentaCobrarCabecera().getId());
				totalImportePorCuenta = totalImportePorCuenta + ob.getImporte();
				idCabecera=ob.getCuentaCobrarCabecera().getId();
				
				detalleRepository.save(ob);
			}else {
				ob.setFechaPago(new Date());
				ob.setSubTotal(ob.getMonto());
				ob.getCuentaCobrarCabecera().setId(cuc.getId());
				idCabecera=cuc.getId();
				totalImportePorCuenta = totalImportePorCuenta + ob.getImporte();
				detalleRepository.save(ob);
			}

		}
		System.out.println("idCab: "+idCabecera+"   motoCabeceraCobrado:  "+totalImportePorCuenta);
		System.out.println();
		cuentaCobrarRepository.findByActualizarPagadoCuenta(idCabecera, totalImportePorCuenta);

	}
	
	@RequestMapping(method=RequestMethod.GET, value="/detalles/{tipoOpera}/{idUser}/{importe}/{idCuenta}")
	public List<Object[][]> saveCobroCuenta(@PathVariable int tipoOpera,  @PathVariable int idUser, @PathVariable Double importe, @PathVariable int idCuenta){
		
		
//		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
//		
//		CobrosClienteCabecera cab= new CobrosClienteCabecera();
//		cab.getCliente().setId(entityRepository.getOne(idCuenta).getCliente().getId());
//		cab.getFuncionario().setId(f.getId());
//		cab.setFecha(new Date());
//		cab.setTotal(importe);
//		cobrosClienteCabeceraRepository.save(cab);
//		System.out.println("Entro en cero^^^^^^^^^^^^^^^^^^^^");
//		CuentaCobrarCabecera cuentaCabecera = cuentaCobrarRepository.getOne(idCuenta);
////		List<CuentaCobrarCabecera> cuentas, Double monto, int idUser, int idOpe, int idCabCobros
//		return operacion(cuentaCabecera, importe, idUser, idOpe, cobrosClienteCabeceraRepository.getUltimoCobrosClienteCab().getId());

		
		return null;
		/*		
		
		System.out.println("entrooosadfoasofdoasd ********* adasfsadfa s 0+");
				//idCuenta, totalCuenta, pagadoCuenta, saldoCuenta, idCli, tipoOperacion, numeroVenta, idCobroCabecera, totalCobrado, numeroCobrosItem, cobradoPorCuenta, idFunc
				Object[][] obResult = new Object[1][12];
				List<Object[][]> listRes = new ArrayList<>();
				int contador = 0;	
				String tipoPago="";	
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		int idApertura = aperturaCajaRepository.getAperturaActivoCajaId(f.getId());
		OperacionCaja operacionCaja = new OperacionCaja();
		operacionCaja.getAperturaCaja().setId(idApertura);
		operacionCaja.getConcepto().setId(5);//cobros clientes
		operacionCaja.getTipoOperacion().setId(tipoOpera);
		operacionCaja.setEfectivo(importe);
		operacionCaja.setFecha(new Date());
		operacionCaja.setMonto(importe);
		Concepto c = new Concepto();
		c = conceptoRepository.findById(5).orElse(null);
		operacionCaja.setMotivo(c.getDescripcion() + " REF.: ");
		operacionCaja.setTipo("ENTRADA");

		operacionCajaRepository.save(operacionCaja);

		OperacionCaja opeAux= operacionCajaRepository.findTop1ByOrderByIdDesc();
//		CuentaCobrarCabecera cuenta = entityRepository.getOne(idCuenta).getCliente().getId()
		CobrosClienteCabecera cobCabecera= new CobrosClienteCabecera();
		cobCabecera.getCliente().setId(entityRepository.getOne(idCuenta).getCliente().getId());
		cobCabecera.getFuncionario().setId(f.getId());
		cobCabecera.setTotal(importe);
		cobCabecera.setFecha(new Date());
		cobrosClienteCabeceraRepository.save(cobCabecera);
		
		CobrosClienteCabecera cabeceraAux= cobrosClienteCabeceraRepository.findTop1ByOrderByIdDesc();
		
		CobrosCliente cob = new CobrosCliente();
		cob.getCobrosClienteCabecera().setId(cobrosClienteCabeceraRepository.getUltimoCobrosClienteCab().getId());
		cob.getCuentaCobrarCabecera().setId(idCuenta);
		cob.getOperacionCaja().setId(1);
		cob.getFuncionario().setId(f.getId());
		cob.setTotal(importe);
		cob.setFecha(new Date());
		cob.getOperacionCaja().setId(opeAux.getId());

		cobrosClienteRepository.save(cob);

		CobrosCliente cobAux= cobrosClienteRepository.findTop1ByOrderByIdDesc();

		Concepto cv = new Concepto();
		cv = conceptoRepository.findById(5).orElse(null);
		opeAux.setMotivo(cv.getDescripcion() + " REF.: "+ cobAux.getId());

		operacionCajaRepository.save(opeAux);

		if (tipoOpera == 1) {
			aperturaCajaRepository.findByActualizarAperturaSaldo(idApertura, importe);
			tipoPago="EFECTIVO";
		}
		if (tipoOpera == 2) {
			aperturaCajaRepository.findByActualizarAperturaSaldoCheque(idApertura, importe);
			tipoPago="CHEQUE";
		}
		if (tipoOpera == 3) {
			aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(idApertura, importe);
			tipoPago="TARJETA";
		}
		List<Object[]> obDetalle = detalleRepository.consultarDetalleCuentaPorIdCabecera(idCuenta);
		List<CuentaCobrarDetalle> cuentaDetalle= new ArrayList<>();
		for (Object[] ob: obDetalle) {
			CuentaCobrarDetalle d = new CuentaCobrarDetalle();
			d.getCuentaCobrarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
			d.setNumeroCuota(Integer.parseInt(ob[1].toString()));
			d.setFechaVencimiento(com.bisontecfacturacion.security.config.FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
			d.setMonto(Double.parseDouble(ob[3].toString()));
			d.setImporte(Double.parseDouble(ob[4].toString()));
			d.setInteresMora(Double.parseDouble(ob[5].toString()));
			d.setId(Integer.parseInt(ob[6].toString()));
			d.setSubTotal(Double.parseDouble(ob[7].toString()));
			d.getCuentaCobrarCabecera().setId(Integer.parseInt(ob[8].toString()));
			cuentaDetalle.add(d);
		}
		Double saldoPositivoaCobrar=0.0, montoCobradoActual=0.0;
		saldoPositivoaCobrar= importe;
		for(int i=0; i < cuentaDetalle.size() && saldoPositivoaCobrar > 0; i++) {
			Double montoImporteActualizado=0.0, montoCuotaRestantes=0.0;
			CuentaCobrarDetalle det= cuentaDetalle.get(i);
			montoCuotaRestantes = det.getSubTotal() - det.getImporte();
			System.out.println("Saldo positivo a cobrara == "+ saldoPositivoaCobrar);
			System.out.println("Monto Cuota Restantes == "+ montoCuotaRestantes);
			if(det.getImporte() < det.getSubTotal()) {
				if (saldoPositivoaCobrar >= montoCuotaRestantes) {
					saldoPositivoaCobrar = saldoPositivoaCobrar - montoCuotaRestantes;
					System.out.println("SALDO POSTIIVO FALTANTE A COBRAR POR DETALLE == "+saldoPositivoaCobrar);
					montoImporteActualizado = det.getSubTotal();
					System.out.println("MONTO IMPORTE ACTUALZIADOS== "+montoImporteActualizado);
					montoCobradoActual = montoCobradoActual + montoCuotaRestantes;
					System.out.println("MONTO COBRADO ");
					det.setImporte(montoImporteActualizado);
					det.setFechaPago(new Date());
					detalleRepository.save(det);
				}else {
					montoImporteActualizado = det.getImporte() + saldoPositivoaCobrar;
					System.out.println("MONTO IMPORTE ACTUALIZADO == "+montoImporteActualizado);
					montoCobradoActual = montoCobradoActual + saldoPositivoaCobrar;
					System.out.println("MONTO COBRADO ACTUAL "+ montoCobradoActual);
					saldoPositivoaCobrar = 0.0 ;
					System.out.println("SALDO POSTIVO A COBRAR : "+saldoPositivoaCobrar);
					det.setImporte(montoImporteActualizado);
					det.setFechaPago(new Date());
					detalleRepository.save(det);
				}
			}else {
				System.out.println("Ya se pago todo por el detalle");
			}
			
		}
		System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvnnnnnnnnnnnty");

		cuentaCobrarRepository.findByActualizarPagadoCuenta(idCuenta, montoCobradoActual);
		CuentaCobrarCabecera cue=entityRepository.getOne(idCuenta);
		//idCuenta, totalCuenta, pagadoCuenta, saldoCuenta, idCli, tipoOperacion, numeroVenta, idCobroCabecera, totalCobrado, 
		//numeroCobrosItem, cobradoPorCuenta, idFunc
		obResult[0][0] = cue.getId();
		obResult[0][1] = cue.getTotal();
		obResult[0][2] = cue.getPagado();
		obResult[0][3] = importe;
		obResult[0][4] = cue.getCliente().getId();
		obResult[0][5] = tipoPago;
		obResult[0][6] = cue.getVenta().getId();
		obResult[0][7] = cabeceraAux.getId();
		obResult[0][8] = cabeceraAux.getTotal();
		obResult[0][9] = cabeceraAux.getTotal();
		obResult[0][10] = cabeceraAux.getTotal();
		obResult[0][11] = f.getId();

		
		listRes.add(obResult);
		return listRes;
		*/
	}


@RequestMapping(method=RequestMethod.GET, value = "/liquidarDetalle/{id}")
public void liquidarDetalle(@PathVariable int id){
	this.detalleRepository.liquidarDetalle(id, new Date(), true);
}


@RequestMapping(method=RequestMethod.GET, value = "/cuentaPendienteLista/{id}")
public List<CuentaCobrarCabecera> extraerListaCuentaPendientePorClienteId(@PathVariable int id){
	List<CuentaCobrarCabecera> listRetorno= new ArrayList<>();
	listRetorno= entityRepository.findByCuentaPorIdACobrarListas(id);
	System.out.println("lista cu7enta:  "+listRetorno.size());
	return listadoCargarCuenta(listRetorno);
}

@RequestMapping(method=RequestMethod.POST, value = "/{pagareEst}")
public Map<Object, Object> guardar(@RequestBody CuentaCobrarCabecera entity, @PathVariable boolean pagareEst){
	List<CuentaCobrarCabecera> listRetorno= new ArrayList<>();
	OrdenPagare opRest = new OrdenPagare();

	try {
		Venta v =null;
		if(entity.getVenta().getId()==0) {
			v = ventaRepository.getUltimaVenta();
		}
		else {
			v = entity.getVenta();
		}
		
		Funcionario funcionario=funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId());
		entity.getVenta().setId(v.getId());
		entity.getConcepto().setId(19);
		entity.getFuncionario().setId(funcionario.getId());
		entityRepository.save(entity);	
		CuentaCobrarCabecera cuc = entityRepository.findTop1ByOrderByIdDesc();
		OrdenPagare op = new OrdenPagare();
		op.setTotal(cuc.getTotal());
		op.setTotalLetra(cuc.getTotalLetra());
		op.setFecha(new Date());
		op.setFechaVencimiento(cuc.getFechaVencimiento());
		op.getCliente().setId(cuc.getCliente().getId());
		op.getFuncionario().setId(cuc.getFuncionario().getId());
		op.getCuentaCobrarCabecera().setId(cuc.getId());
		op.setEstado("PENDIENTE");
		ordenPagareRepository.save(op);
		if(pagareEst == true) {
			System.out.println("Entro pagare generar");
			OrdenPagare orden=ordenPagareRepository.findTop1ByOrderByIdDesc();
			System.out.println("CLIENETE: "+orden.getCliente().getPersona().getNombre());
			System.out.println("ID BUSQUEDA:  "+orden.getId());
			OrdenPagare ope = new OrdenPagare();
			ope=ordenPagareRepository.getOrdenPagarePorId(orden.getId());
			System.out.println("CLIENETE: "+ope.getCliente().getPersona().getNombre());

			opRest=new OrdenPagare();
			opRest.setId(ope.getId());
			opRest.setFuncionario(ope.getFuncionario());
			opRest.setCliente(ope.getCliente());
			opRest.getCuentaCobrarCabecera().setId(ope.getCuentaCobrarCabecera().getId());
			opRest.setFecha(ope.getFecha());
			opRest.setFechaVencimiento(ope.getFechaVencimiento());
			opRest.setTotal(ope.getTotal());
			opRest.setTotalLetra(ope.getTotalLetra());
			System.out.println("ORDEN PAGARE: "+ope.getCliente().getPersona().getNombre());
		} 
		else {
			System.out.println("ENTRO NO GENERAR PAGARED");
			opRest=null;
		}
		for(CuentaCobrarDetalle ob: entity.getCuentaCobrarDetalle()){
			ob.getCuentaCobrarCabecera().setId(cuc.getId());
			ob.setSubTotal(ob.getMonto());
			System.out.println("ENTRO GUARDAR dETALLE CUENTA");
			detalleRepository.save(ob);
		}
		
		listRetorno=  listadoCargarCuenta(entityRepository.findByCuentaPorIdACobrarListas(entity.getCliente().getId()));

	} catch (Exception e) {
		e.printStackTrace();
	}
	Map<Object, Object> mapa= new HashMap();
	mapa.put("pagare", opRest);
	mapa.put("cuenta", listRetorno);
	//System.out.println(mapa.get("pagare").toString());
	//System.out.println(mapa.get("cuenta").toString());
	
	return mapa;
}


@RequestMapping(method=RequestMethod.GET, value="/buscar/cliente/{idCliente}")
public  List<CuentaCobrarCabecera>  consultarCuentaPorIdCliente(@PathVariable int idCliente){
	List<CuentaCobrarCabecera> list = entityRepository.consultarCuentaIdCliente(idCliente);
	List<CuentaCobrarCabecera> cuentaCabecera=new ArrayList<>();
	for(CuentaCobrarCabecera c: list) {
		CuentaCobrarCabecera cabecera = new CuentaCobrarCabecera();
		cabecera.setId(c.getId());
		cabecera.setEntrega(c.getEntrega());
		cabecera.setFecha(c.getFecha());
		cabecera.setTotal(c.getTotal());
		cabecera.setPagado(c.getPagado());
		cabecera.setSaldo(c.getSaldo());
		cabecera.getInteresCuota().setDescripcion(c.getInteresCuota().getDescripcion());
		cabecera.getInteresMora().setDescripcion(c.getInteresMora().getDescripcion());
		cuentaCabecera.add(cabecera);
	}
	return cuentaCabecera;

}
@RequestMapping(method=RequestMethod.GET, value="/buscar/detalle/{idCuenta}")
public List<CuentaCobrarDetalle> consultarCuentaDetallePorIdCabecera(@PathVariable int idCuenta){
	List<Object[]> object = detalleRepository.consultarDetalleCuentaPorIdCabecera(idCuenta);
	List<CuentaCobrarDetalle> lista= new ArrayList<>();		  
	for(Object[] ob: object){
		CuentaCobrarDetalle cue= new CuentaCobrarDetalle();
		cue.getCuentaCobrarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
		cue.setNumeroCuota(Integer.parseInt(ob[1].toString()));
		cue.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
		cue.setMonto(Double.parseDouble(ob[3].toString()));
		cue.setImporte(Double.parseDouble(ob[4].toString()));
		cue.setInteresMora(Double.parseDouble(ob[5].toString()));
		cue.setId(Integer.parseInt(ob[6].toString()));
		cue.setSubTotal(Double.parseDouble(ob[7].toString()));
		cue.getCuentaCobrarCabecera().setId(Integer.parseInt(ob[8].toString()));
		lista.add(cue);
	}

	return lista;
}
@RequestMapping(method=RequestMethod.GET, value="/buscar/venta/detalleProducto/{idCuenta}")
public List<DetalleProducto> consultarVentaDetalleCuentaPorIdCabecera(@PathVariable int idCuenta){
	List<Object[]> object = detalleRepository.getDetalleProductoVentaCuenta(idCuenta);
	List<DetalleProducto> lista= new ArrayList<>();		  
	for(Object[] ob: object){
		DetalleProducto cue= new DetalleProducto();
		cue.setDescripcion(ob[0].toString());
		cue.setPrecio(Double.parseDouble(ob[1].toString()));
		cue.setCantidad(Double.parseDouble(ob[2].toString()));
		cue.setSubTotal(Double.parseDouble(ob[3].toString()));
		cue.getProducto().getUnidadMedida().setDescripcion(ob[4].toString());
		cue.setId(Integer.parseInt(ob[5].toString()));
		cue.getProducto().setId(Integer.parseInt(ob[6].toString()));
		lista.add(cue);
	}

	return lista;
}

@RequestMapping(method=RequestMethod.GET, value="/buscar/venta/detalleServicio/{idCuenta}")
public List<DetalleServicios> consultarVentaDetalleServicioCuentaPorIdCabecera(@PathVariable int idCuenta){
	List<Object[]> object = detalleRepository.getDetalleServiciosVentaCuenta(idCuenta);
	List<DetalleServicios> lista= new ArrayList<>();		  
	for(Object[] ob: object){
		DetalleServicios cue= new DetalleServicios();
		cue.setDescripcion(ob[0].toString());
		cue.setPrecio(Double.parseDouble(ob[1].toString()));
		cue.setCantidad(Double.parseDouble(ob[2].toString()));
		cue.setSubTotal(Double.parseDouble(ob[3].toString()));
		lista.add(cue);
	}

	return lista;
}


@RequestMapping(method = RequestMethod.GET, value = "/{desc}/{tipo}")
public List<CuentaCobrarCabecera> getCuentaFiltro(@PathVariable String desc, @PathVariable int tipo) {
	if (tipo == 1) {
		List<Object[]> object = entityRepository.getAllCuentaACobrar("%" + desc.toUpperCase() + "%");
		return cuentaListado(object);
	} else {
		List<Object[]> object = entityRepository.getAllCuentaCobrado("%" + desc.toUpperCase() + "%");
		return cuentaListado(object);
	}
}

public List<CuentaCobrarCabecera> cuentaListado(List<Object[]> object) {
	List<CuentaCobrarCabecera> listadoRetorno= new ArrayList<>();
	for (Object[] cue: object) {
		CuentaCobrarCabecera cuenta= new CuentaCobrarCabecera();
		//cuenta.setId(cue[0].toString();
		cuenta.setTotal(Double.parseDouble(cue[0].toString()));
		cuenta.setPagado(Double.parseDouble(cue[1].toString()));
		cuenta.setSaldo(Double.parseDouble(cue[2].toString()));
		cuenta.getCliente().getPersona().setNombre(cue[3].toString()+" "+ cue[4].toString());
		cuenta.getCliente().setId(Integer.parseInt(cue[5].toString()));
		cuenta.getCliente().getPersona().setCedula(cue[6].toString());
		cuenta.getCliente().getPersona().setTelefono(cue[7].toString());
		cuenta.getCliente().getPersona().setDireccion(cue[8].toString());
		cuenta.getCliente().setLimiteCredito(Double.parseDouble(cue[9].toString()));
		listadoRetorno.add(cuenta);
	}
	return listadoRetorno;
}

@RequestMapping(method = RequestMethod.GET, value="/validacionBloqueo/{id}/{montoFactura}")
public ResponseEntity<?> validacionBloqueCliente(@PathVariable int id,  @PathVariable Double montoFactura){
	List<Object[]> lis= entityRepository.getCLienteCuentaACobrarPorIdCliente(id);
	String msg="";
	if (lis.size()>0) {
		System.out.println("TIENE CUENTA");
		for(Object[] ob: lis) {
			if (ob != null) {
				System.out.println("entro distinto null");
				if(Boolean.parseBoolean(ob[2].toString())==true) {
					if((Double.parseDouble(ob[0].toString()) + montoFactura) > Double.parseDouble(ob[1].toString())){
					return  new ResponseEntity<>(new CustomerErrorType("EL MONTO DE LAS FACTURAS ACUMULADAS, MAS EL MONTO ACTUAL DE LA FACTURA SUPERA LINEA DE CREDITO DEL CLIENTE"), HttpStatus.CONFLICT);
					}else if((Double.parseDouble(ob[0].toString())+ montoFactura) <= Double.parseDouble(ob[1].toString())){
					return  new ResponseEntity<>(new CustomerErrorType(" "), HttpStatus.OK);
					}
				}else {
					return  new ResponseEntity<>(new CustomerErrorType(" "), HttpStatus.OK);
				}
				
			}else {
				return  new ResponseEntity<>(new CustomerErrorType(" "), HttpStatus.OK);
			}
		}			
	} else {
		System.out.println("ENTRO ELSE");

		Cliente cliente = clienteRepository.findById(id).get();
		if (cliente !=null) {
			if(cliente.isEstadoBloqueo()==true) {
				System.out.println("ENTRO  ESTADO BLOQUEO "+montoFactura+ " "+cliente.getLimiteCredito());
				if (montoFactura > cliente.getLimiteCredito()) {
					return new ResponseEntity<>(new CustomerErrorType("EL MONTO DE LA FACTURA ACTUAL HA SUPERADO LINEA DE CREDITO DEL CLIENTE"), HttpStatus.CONFLICT);
				}else {
					return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
				}
			}else {
				return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
			}
			
		}else {
			return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
		}
	}
	System.out.println(msg);

	return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);

}

@RequestMapping(method = RequestMethod.GET, value="/lista/{tipo}")
public List<CuentaCobrarCabecera> getAll(@PathVariable int tipo){
	if (tipo == 1) {
		List<Object []> lis= entityRepository.getCLienteCuentaACobrar();
		return cuentaListado(lis);
	} else {
		List<Object []> lis= entityRepository.getCLienteCuentaCobrado();
		return cuentaListado(lis);
	}
}

@RequestMapping(method = RequestMethod.GET, value="/buscarCuenta/{id}/{filtro}")
public List<CuentaCobrarCabecera> getCuentaPorClienteId(@PathVariable int id, @PathVariable int filtro){
	List<CuentaCobrarCabecera> lis =new ArrayList<>();
	if(filtro == 1){
		lis= entityRepository.findByCuentaPorIdTodo(id);
	}
	if(filtro == 2){
		lis= entityRepository.findByCuentaPorIdACobrarListas(id);
	}
	if(filtro == 3){
		lis= entityRepository.findByCuentaPorIdCobrar(id);
	}
	return listadoCargarCuenta(lis);
}

@RequestMapping(method = RequestMethod.GET, value="/reporteCuentaCliente/{id}/{filtro}/{detallado}")
public  ResponseEntity<?> getReporteCuentaCliente(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id, @PathVariable int filtro, @PathVariable int detallado) throws IOException{
	List<CuentaCobrarCabecera> lis =new ArrayList<>();
	
	if(filtro == 1){
		lis= entityRepository.findByCuentaPorIdTodo(id);
	}
	if(filtro == 2){
		lis= entityRepository.findByCuentaPorIdACobrarListas(id);
	}
	if(filtro == 3){
		lis= entityRepository.findByCuentaPorIdCobrar(id);
	}
	List<CuentaCobrarCabecera> listado= listado(lis);
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
		if(detallado==1) {
			report.reportPDFDescarga(listado, map, "ReporteCuentaCliente", response);
		}else {
			report.reportPDFDescarga(listado, map, "ReporteCuentaClienteDetallado", response);
		}

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
}


@RequestMapping(value="/pruebaHql", method=RequestMethod.GET)
public List<CuentaCobrarCabecera>  pruebaHql() throws IOException {
	List<CuentaCobrarCabecera> lis =new ArrayList<>();
	
		lis= entityRepository.findByCuentaPorIdTodo(4);
	
	return listado(lis);
}
public List<CuentaCobrarCabecera> listadoCargarCuenta(List<CuentaCobrarCabecera> lis){
	List<CuentaCobrarCabecera> listadoRetorno = new ArrayList<>();
	for(CuentaCobrarCabecera x :lis) {
		CuentaCobrarCabecera cuenta= new CuentaCobrarCabecera();
		cuenta.setId(x.getId());
		cuenta.setTotal(x.getTotal()); 
		cuenta.setPagado(x.getPagado());
		cuenta.setSaldo(x.getSaldo());
		cuenta.setFechaVencimiento(x.getFechaVencimiento());
		cuenta.getCliente().setId(x.getCliente().getId());
		cuenta.getCliente().getPersona().setNombre(x.getCliente().getPersona().getNombre());
		cuenta.getCliente().getPersona().setApellido(x.getCliente().getPersona().getApellido());
		cuenta.getCliente().getPersona().setCedula(x.getCliente().getPersona().getCedula());
		cuenta.getFuncionario().getPersona().setNombre(x.getFuncionario().getPersona().getNombre());
		cuenta.getFuncionario().getPersona().setApellido(x.getFuncionario().getPersona().getApellido());
		cuenta.setFecha(x.getFecha());
		cuenta.getVenta().setId(x.getVenta().getId());
		cuenta.getVenta().setFechaFactura(x.getVenta().getFechaFactura());
		cuenta.getVenta().setDetalleProducto(null);
		cuenta.getVenta().setDetalleServicio(null);
		
		cuenta.getVenta().setFecha(sumarDia(x.getFecha(), (24 * x.getTipoPlazo().getValor())));
		cuenta.getTipoPlazo().setValor(validarDiaAtraso(x.getVenta().getFecha()));
		listadoRetorno.add(cuenta);
	}
	return listadoRetorno;
}

public List<CuentaCobrarCabecera> listado(List<CuentaCobrarCabecera> lis){
	List<CuentaCobrarCabecera> listadoRetorno = new ArrayList<>();
	for(CuentaCobrarCabecera x :lis) {
		CuentaCobrarCabecera cuenta= new CuentaCobrarCabecera();
		cuenta.setId(x.getId());
		cuenta.setTotal(x.getTotal()); 
		cuenta.setPagado(x.getPagado());
		cuenta.setSaldo(x.getSaldo());
		cuenta.getCliente().getPersona().setNombre(x.getCliente().getPersona().getNombre());
		cuenta.getCliente().getPersona().setApellido(x.getCliente().getPersona().getApellido());
		cuenta.getFuncionario().getPersona().setNombre(x.getFuncionario().getPersona().getNombre());
		cuenta.getFuncionario().getPersona().setApellido(x.getFuncionario().getPersona().getApellido());
		cuenta.setFecha(x.getFecha());
		cuenta.getVenta().setId(x.getVenta().getId());
		if(x.getVenta().getDetalleProducto().size()!=-1) {
			for(DetalleProducto det: x.getVenta().getDetalleProducto()) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.setDescripcion(det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getProducto().getCodbar());
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				cuenta.getVenta().getDetalleProducto().add(detalleProducto);
				System.out.println(det.getIva()+" *8*8*8*8*");

			}
		}
		cuenta.getVenta().setDetalleProducto(x.getVenta().getDetalleProducto());
		if(x.getVenta().getDetalleServicio().size()!=-1) {
			for(DetalleServicios det: x.getVenta().getDetalleServicio()) {
				DetalleProducto detalleProducto = new DetalleProducto();
				detalleProducto.setDescripcion(det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				cuenta.getVenta().getDetalleProducto().add(detalleProducto);
				System.out.println(det.getIva()+" *8*8*8*8*");

			}
		}else {
			System.out.println("dfpdfpdp ");
		}
		
		cuenta.getVenta().setFecha(sumarDia(x.getFecha(), (24 * x.getTipoPlazo().getValor())));
		cuenta.getTipoPlazo().setValor(validarDiaAtraso(x.getVenta().getFecha()));
		listadoRetorno.add(cuenta);
	}
	return listadoRetorno;
}

public Date sumarDia(Date fecha, int hora) {
	Calendar calendar=Calendar.getInstance();
	calendar.setTime(fecha);
	calendar.add(Calendar.HOUR, hora);
	return calendar.getTime();
}

public int diferenciaDias(Date fechaMayor, Date fechaMenor) {
	long diferencia = sumarDia(fechaMayor, 24).getTime() - fechaMenor.getTime();
	long dias = diferencia/(1000*60*60*24);
	return (int) dias;
}

public int validarDiaAtraso(Date fecha2) {
	int diaAtraso = 0;
	Date fechaHoy=new Date();
	if (fechaHoy.getTime() <= fecha2.getTime()) {
		diaAtraso = 0;
	} else {
		diaAtraso = diferenciaDias(new Date(), fecha2);
	}
	return diaAtraso;
}



@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
public CuentaCobrarCabecera  getCuentaCobrarID(@PathVariable int id){
	CuentaCobrarCabecera c=entityRepository.findById(id).get();
	CuentaCobrarCabecera cuenta=new CuentaCobrarCabecera();
	cuenta.setId(c.getId());
	cuenta.getCliente().setId(c.getCliente().getId());
	cuenta.getCliente().getPersona().setNombre(c.getCliente().getPersona().getNombre());
	cuenta.getCliente().getPersona().setApellido(c.getCliente().getPersona().getApellido());
	cuenta.getCliente().getPersona().setCedula(c.getCliente().getPersona().getCedula());
	cuenta.setTotal(c.getTotal());
	cuenta.getTipoPlazo().setDescripcion(c.getTipoPlazo().getDescripcion());
	cuenta.setPagado(c.getPagado());
	cuenta.getInteresMora().setDescripcion(c.getInteresMora().getDescripcion());
	cuenta.setSaldo(c.getSaldo());
	cuenta.setFecha(c.getFecha());
	cuenta.getVenta().getFuncionario().getPersona().setNombre(c.getFuncionario().getPersona().getNombre() + " " + c.getFuncionario().getPersona().getApellido());
	cuenta.getVenta().setOperacionCaja(c.getVenta().getOperacionCaja());
	cuenta.getVenta().getDocumento().setDescripcion(c.getVenta().getDocumento().getDescripcion());
	cuenta.getVenta().setNroDocumento(c.getVenta().getNroDocumento());
	if (c.getVenta().getTipo().equals("1") || c.getVenta().getTipo().toLowerCase().equals("contado")) {
		cuenta.getVenta().setTipo("CONTADO");
		System.out.println("entro verificacion de cuenta contado");

	} else if(c.getVenta().getTipo().equals("2") || c.getVenta().getTipo().toLowerCase().equals("credito")) {
		cuenta.getVenta().setTipo("CREDITO");
		System.out.println("entro verificacion de cuenta credito");
	}
	return cuenta;

}
@RequestMapping(method=RequestMethod.GET, value="/idVenta/{id}")
public CuentaCobrarCabecera  getCuentaCobrarPorIdVenta(@PathVariable int id){
	CuentaCobrarCabecera c=entityRepository.getCuentaCabeceraPorVentaId(id);
	CuentaCobrarCabecera cuenta=new CuentaCobrarCabecera();
	cuenta.setId(c.getId());
	cuenta.getCliente().setId(c.getCliente().getId());
	cuenta.getCliente().getPersona().setNombre(c.getCliente().getPersona().getNombre());
	cuenta.getCliente().getPersona().setApellido(c.getCliente().getPersona().getApellido());
	cuenta.setTotal(c.getTotal());
	cuenta.getTipoPlazo().setDescripcion(c.getTipoPlazo().getDescripcion());
	cuenta.setPagado(c.getPagado());
	cuenta.getInteresMora().setDescripcion(c.getInteresMora().getDescripcion());
	cuenta.setSaldo(c.getSaldo());
	cuenta.setFecha(c.getFecha());
	cuenta.getVenta().getFuncionario().getPersona().setNombre(c.getFuncionario().getPersona().getNombre() + " " + c.getFuncionario().getPersona().getApellido());
	cuenta.getVenta().setOperacionCaja(c.getVenta().getOperacionCaja());
	cuenta.getVenta().getDocumento().setDescripcion(c.getVenta().getDocumento().getDescripcion());
	cuenta.getVenta().setNroDocumento(c.getVenta().getNroDocumento());
	if (c.getVenta().getTipo().equals("1") || c.getVenta().getTipo().toLowerCase().equals("contado")) {
		cuenta.getVenta().setTipo("CONTADO");
		System.out.println("entro verificacion de cuenta contado");

	} else if(c.getVenta().getTipo().equals("2") || c.getVenta().getTipo().toLowerCase().equals("credito")) {
		cuenta.getVenta().setTipo("CREDITO");
		System.out.println("entro verificacion de cuenta credito");
	}
	return cuenta;

}

@RequestMapping(method = RequestMethod.GET, value="/reporteCuentaClienteCabecera/rango/{id}/{tipo}/{detallado}/{fechaI}/{fechaF}")
public  ResponseEntity<?> getReporteCuentaClienteRango(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id, @PathVariable int tipo, @PathVariable int detallado, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException, ParseException{
	List<CuentaCobrarCabecera> lis =new ArrayList<>();
	
	Calendar cc= Calendar.getInstance();
	SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
	Date fecI;
	System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
	fecI = formater.parse(fechaI);
	Date fecF=formater.parse(fechaF);
	System.out.println(fecF.getDate());
	fecF.setHours(23);
	fecF.setSeconds(59);
	fecI.setHours(0);
	fecI.setSeconds(1);
	System.out.println("hora final fechas::: "+fecF+ " hora inicio finbal: "+fecI);

	if(tipo == 1){
		lis= entityRepository.findByCuentaPorIdTodoRango(id, fecI, fecF);
	}
	if(tipo == 2){
		lis= entityRepository.findByCuentaPorIdACobrarRango(id,fecI, fecF);
	}
	if(tipo == 3){
		lis= entityRepository.findByCuentaPorIdCobradoRango(id,fecI, fecF);

	}
	List<CuentaCobrarCabecera> listado= listado(lis);
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
		map.put("desde", fechaI);
		map.put("hasta", fechaF);

		report = new Reporte();
		if(detallado==1) {
			report.reportPDFDescarga(listado, map, "ReporteCuentaClienteRangoFecha", response);
		}else {
			report.reportPDFDescarga(listado, map, "ReporteCuentaClienteDetalladoRangoFecha", response);
		}

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
}
@RequestMapping(method = RequestMethod.GET, value="/reporteCuentaClienteGeneral")
public  ResponseEntity<?> getReporteCuentaClienteGeneral(HttpServletResponse response, OAuth2Authentication authentication ) throws IOException, ParseException{
	List<Object[]> lis =new ArrayList<>();
	List<CuentaCobrarCabecera> listadoRetorno= new ArrayList<>();
	lis= entityRepository.getCLienteCuentaACobrar();
	listadoRetorno = cuentaListado(lis);
	if(listadoRetorno.size()>0) {
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

		report = new Reporte();
		report.reportPDFDescarga(listadoRetorno, map, "ReporteCuentaClienteGeneral", response);

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
}

}
