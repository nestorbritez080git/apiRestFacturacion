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
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("cuentaCobrar")
public class CuentaCobrarController {
	private Reporte report;
	@Autowired
	private CuentaAcobrarRepository entityRepository;
	
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
				System.out.println("entrooosadfoasofdoasd ********* adasfsadfa s 0+");
				
				Object[][] obResult = new Object[1][5];
				List<Object[][]> listRes = new ArrayList<>();
				int contador = 0;			
				
		Funcionario f = funcionarioRepository.getIdFuncionario(idUser);
		int idApertura = aperturaCajaRepository.getAperturaActivoCajaId(f.getId());

		//		cobros clientes

		//		operac
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
		}
		if (tipoOpera == 2) {
			aperturaCajaRepository.findByActualizarAperturaSaldoCheque(idApertura, importe);
		}
		if (tipoOpera == 3) {
			aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(idApertura, importe);
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
		obResult[0][0] = cue.getId();
		obResult[0][1] = cue.getTotal();
		obResult[0][2] = cue.getPagado();
		obResult[0][3] = importe;
		obResult[0][4] = cue.getCliente().getId();
		
		listRes.add(obResult);
		return listRes;
	}


@RequestMapping(method=RequestMethod.GET, value = "/liquidarDetalle/{id}")
public void liquidarDetalle(@PathVariable int id){
	this.detalleRepository.liquidarDetalle(id, new Date(), true);
}

@RequestMapping(method=RequestMethod.POST)
public CuentaCobrarCabecera guardar(@RequestBody CuentaCobrarCabecera entity){
	Venta v =null;
	if(entity.getVenta().getId()==0) {v = ventaRepository.getUltimaVenta();}
	else {v = entity.getVenta();}

	Funcionario funcionario=funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId());
	entity.getVenta().setId(v.getId());
	entity.getFuncionario().setId(funcionario.getId());
	return entityRepository.save(entity);
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
		cuenta.getCliente().getPersona().setNombre(cue[3].toString()+", "+ cue[4].toString());
		cuenta.getCliente().setId(Integer.parseInt(cue[5].toString()));
		cuenta.getCliente().getPersona().setCedula(cue[6].toString());
		listadoRetorno.add(cuenta);
	}
	return listadoRetorno;
}

@RequestMapping(method = RequestMethod.GET, value="/validacionBloqueo/{id}/{montoFactura}")
public ResponseEntity<?> validacionBloqueCliente(@PathVariable int id,  @PathVariable double montoFactura){
	List<Object[]> lis= entityRepository.getCLienteCuentaACobrarPorIdCliente(id);
	String msg="";
	if (lis != null) {
		System.out.println("TIENE CUENTA");
		for(Object[] ob: lis) {
			if (ob != null) {
				if(Boolean.parseBoolean(ob[2].toString())==true) {
					if((Double.parseDouble(ob[0].toString())+ montoFactura) > Double.parseDouble(ob[1].toString())){
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
		Cliente cliente = clienteRepository.findById(id).get();
		if (cliente !=null) {
			if(cliente.isEstadoBloqueo()==true) {
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
	return listado(lis);
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

public List<CuentaCobrarCabecera> listado(List<CuentaCobrarCabecera> lis){
	List<CuentaCobrarCabecera> listadoRetorno = new ArrayList<>();
	for(CuentaCobrarCabecera cue :lis) {
		CuentaCobrarCabecera cuenta= new CuentaCobrarCabecera();
		cuenta.setId(cue.getId());
		cuenta.setTotal(cue.getTotal()); 
		cuenta.setPagado(cue.getPagado());
		cuenta.setSaldo(cue.getSaldo());
		cuenta.getCliente().getPersona().setNombre(cue.getCliente().getPersona().getNombre());
		cuenta.getCliente().getPersona().setApellido(cue.getCliente().getPersona().getApellido());
		cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
		cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
		cuenta.setFecha(cue.getFecha());
		cuenta.getVenta().setId(cue.getVenta().getId());
		cuenta.getVenta().setDetalleProducto(cue.getVenta().getDetalleProducto());
		if(cuenta.getVenta().getDetalleServicio().size()!=-1) {
			for(DetalleServicios det: cuenta.getVenta().getDetalleServicio()) {
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
		}
		
		cuenta.getVenta().setFecha(sumarDia(cue.getFecha(), (24 * cue.getTipoPlazo().getValor())));
		cuenta.getTipoPlazo().setValor(validarDiaAtraso(cuenta.getVenta().getFecha()));
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
	if (Integer.parseInt(c.getVenta().getTipo()) == 1) {
		cuenta.getVenta().setTipo("CONTADO");
	} else {
		cuenta.getVenta().setTipo("CREDITO");
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
	if (Integer.parseInt(c.getVenta().getTipo()) == 1) {
		cuenta.getVenta().setTipo("CONTADO");
	} else {
		cuenta.getVenta().setTipo("CREDITO");
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

}
