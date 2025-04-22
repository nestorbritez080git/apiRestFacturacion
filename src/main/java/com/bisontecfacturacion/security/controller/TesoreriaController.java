package com.bisontecfacturacion.security.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.bisontecfacturacion.security.auxiliar.InformeVentaTotalAuxiliar;
import com.bisontecfacturacion.security.auxiliar.ResumenEntradaSalidaTipoOperacion;
import com.bisontecfacturacion.security.auxiliar.ResumenTesoreriaPorConceptoUtilidad;
import com.bisontecfacturacion.security.auxiliar.ResumenTesoreriaUtilidad;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.model.CajaMayor;
import com.bisontecfacturacion.security.model.CierreCaja;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.Tesoreria;
import com.bisontecfacturacion.security.model.TransferenciaAnticipo;
import com.bisontecfacturacion.security.model.TransferenciaAperturaCaja;
import com.bisontecfacturacion.security.model.TransferenciaCajaChica;
import com.bisontecfacturacion.security.model.TransferenciaCajaMayor;
import com.bisontecfacturacion.security.model.TransferenciaGastos;
import com.bisontecfacturacion.security.model.TransferenciaPagosProveedor;
import com.bisontecfacturacion.security.model.TransferenciaTesoreria;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.CierreCajaRepository;
import com.bisontecfacturacion.security.repository.DetalleProductoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.TesoreriaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAnticipoRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAperturaCajaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaCajaMayorRepository;
import com.bisontecfacturacion.security.repository.TransferenciaGastoRepository;
import com.bisontecfacturacion.security.repository.TransferenciaPagosProveedorRepository;
import com.bisontecfacturacion.security.repository.TransferenciaTesoreriaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@Transactional
@RestController
@RequestMapping("tesoreria")
public class TesoreriaController {
	@Autowired
	private TesoreriaRepository entityRepository;

	@Autowired
	private TransferenciaTesoreriaRepository transferenciaTesoreriaRepository;

	@Autowired
	private TransferenciaCajaChicaRepository transferenciaCajaChicaRepository;

	@Autowired
	private TransferenciaCajaMayorRepository transferenciaCajaMayorRepository;

	@Autowired
	private TransferenciaAnticipoRepository transferenciaAnticipoRepository;

	@Autowired
	private TransferenciaGastoRepository transferenciaGastoRepository;
	
	@Autowired
	private TransferenciaAperturaCajaRepository transferenciaAperturaCajaRepository;
	

	@Autowired
	private TransferenciaPagosProveedorRepository transferenciaPagosProveedorRepository;


	@Autowired
	private CajaMayorRepository cajaMayorRepository;

	@Autowired
	private DetalleProductoRepository detalleRepository;

	@Autowired
	private CierreCajaRepository cierreCajaRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private IUsuarioService usuarioService;
	
	
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Tesoreria> getAllTopCien(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.GET, value = "/all")
	public List<Tesoreria> getAll(){
		return entityRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Tesoreria getPorId(@PathVariable int id){
		return entityRepository.findById(id).orElse(null);
	}

	@RequestMapping(method=RequestMethod.GET, value="/tesoreria/updateEstado/{id}")
	public void verificarRecibido(@PathVariable int id){
		entityRepository.findByActualizarEstado(id, true);
	}
	@RequestMapping(method=RequestMethod.GET, value="/tesoreria/updateEstadoAnulacion/{id}/{estadoAnulacion}")
	public void actualizarEstadoAnulacionTesoreria(@PathVariable int id, @PathVariable Boolean estadoAnulacion){
		entityRepository.findByActualizarEstadoAnulacionTesoreria(id, estadoAnulacion);
	}
	@RequestMapping(method=RequestMethod.GET, value="/tesoreria/updateEstadoTransferencia/{id}/{estadoAnulacion}")
	public void actualizarEstadoTransferenciaTesoreria(@PathVariable int id, @PathVariable Boolean estadoAnulacion){
		entityRepository.findByActualizarEstadoTransferenciaTesoreria(id, estadoAnulacion);
	}
	@RequestMapping(method=RequestMethod.GET, value="/anularCierre/{id}")
	public void anularCierreCaja(@PathVariable int id){
		System.out.println("entrooo anular cierreeeeeee");
		cierreCajaRepository.findByAnularCierreCaja(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/reactivarAperturaCaja/{id}")
	public void reactivarAperturaCaja(@PathVariable int id){
		System.out.println("entrooo anular cierreeeeeee");
		aperturaCajaRepository.findByReactivarAperturaCaja(id);
		//operacionCajaRepository.borraDatosSalidaCapital(id, 16);//16 es el concepto
		aperturaCajaRepository.findByActualizarEstadoAnulacionApertura(id, true);
	}
	



	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	@RequestMapping(method=RequestMethod.GET, value="/informeCierre/{fechaI}/{fechaF}/{id}")
	public List<Tesoreria> getRangoMovimiento(@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int id) throws ParseException{
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI=formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		Date fechaFi = sumarDia(fecF, 24);
		List<Object []> lis= entityRepository.getInoformeCierrePorFuncionario(fecI, fechaFi, id);
		List<Tesoreria> listaRetrono= new  ArrayList<>();
		for(Object [] ob : lis) {
			Tesoreria t = new Tesoreria();
			t.getCierreCaja().setId(Integer.parseInt(ob[0].toString()));
			t.setId(Integer.parseInt(ob[1].toString()));
			t.getCierreCaja().setMonto(Double.parseDouble(ob[2].toString()));
			t.getCierreCaja().getAperturaCaja().setSaldoActual(Double.parseDouble(ob[3].toString()));
			t.getCierreCaja().getAperturaCaja().setSaldoInicial(Double.parseDouble(ob[4].toString()));
			t.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[5].toString()));
			t.setHora(ob[6].toString());

			t.setImporte(Double.parseDouble(ob[7].toString()));	




			listaRetrono.add(t);
		}
		return listaRetrono;
	}
	@RequestMapping(method=RequestMethod.GET, value="/informeCierre/{fechaI}/{fechaF}")
	public List<Tesoreria> getRangoMovimiento(@PathVariable String fechaI, @PathVariable String fechaF) throws ParseException{
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI=formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		Date fechaFi = sumarDia(fecF, 24);
		List<Object []> lis= entityRepository.getInoformeCierre(fecI, fechaFi);
		List<Tesoreria> listaRetrono= new  ArrayList<>();
		for(Object [] ob : lis) {
			Tesoreria t = new Tesoreria();
			t.getCierreCaja().setId(Integer.parseInt(ob[0].toString()));
			t.setId(Integer.parseInt(ob[1].toString()));
			t.getCierreCaja().setMonto(Double.parseDouble(ob[2].toString()));
			t.getCierreCaja().getAperturaCaja().setSaldoActual(Double.parseDouble(ob[3].toString()));
			t.getCierreCaja().getAperturaCaja().setSaldoInicial(Double.parseDouble(ob[4].toString()));
			t.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[5].toString()));
			t.setHora(ob[6].toString());
			listaRetrono.add(t);
		}
		return listaRetrono;
	}
	@RequestMapping(method=RequestMethod.GET, value="/resumenCostoUtilidad/{id}")
	public ResumenTesoreriaUtilidad getResumenCostoUtilidad(@PathVariable int id){
		System.out.println(id+ "idiaddididiiddi");
		ResumenTesoreriaUtilidad v = new ResumenTesoreriaUtilidad();
		Object [][] ob= detalleRepository.getResumenUtilidad(id);
		if(ob[0][0]== null) {v.setTotalCosto(0.0);}else{v.setTotalCosto(Double.parseDouble(ob[0][0].toString()));}
		if(ob[0][1]== null) {v.setTotalVenta(0.0);}else{v.setTotalVenta(Double.parseDouble(ob[0][1].toString()));}
		if(ob[0][2]== null) {v.setTotalUtlidad(0.0);}else{v.setTotalUtlidad(Double.parseDouble(ob[0][2].toString()));}
		return v;
	}
	@RequestMapping(method=RequestMethod.GET, value="/resumenTesoreriaPorConcepto/{id}")
	public List<ResumenTesoreriaPorConceptoUtilidad> getResumenTesoreriaPorConcepto(@PathVariable int id){
		System.out.println(id+ "idiaddididiiddi");
		List<Object[]> lista= entityRepository.getResumenDetalleTesoreriaPorConceptos(id);
		List<ResumenTesoreriaPorConceptoUtilidad> listaRetorno= new ArrayList<ResumenTesoreriaPorConceptoUtilidad>();
		for (Object [] ob :lista) {
			ResumenTesoreriaPorConceptoUtilidad vv = new ResumenTesoreriaPorConceptoUtilidad ();
			vv.setDescripcion(ob[0].toString());
			vv.setTotal(Double.parseDouble(ob[1].toString()));
			listaRetorno.add(vv);
		}
		return listaRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/informeVentaTotalMes/{fechaI}/{fechaF}/{id}/{comision}")
	public InformeVentaTotalAuxiliar getInformeVentaTotalMes(@PathVariable String fechaI, @PathVariable String fechaF, @PathVariable int id, @PathVariable int comision ){
		InformeVentaTotalAuxiliar v = new InformeVentaTotalAuxiliar();
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI=formater.parse(fechaI);
			Date fecF=formater.parse(fechaF);
			Date fechaFi = sumarDia(fecF, 24);
			Object [][] ob= entityRepository.getInformeVentaFacturadoPorFuncionario(fecI, fechaFi, id);
			v.setTotalVenta(Double.parseDouble(ob[0][0].toString()));
			double comi =0.0;
			comi = (v.getTotalVenta()*comision)/100;
			v.setTotalComision(comi);
		} catch (Exception e) {
			// TODO: handle exception

		}	
		return v;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/transferencia")
	public ResponseEntity<?> guardarTransferenciaTesoreria(@RequestBody TransferenciaTesoreria transferencia){

		if(transferencia.getFuncionario().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(transferencia.getTesoreria().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}

		try {
			transferenciaTesoreriaRepository.save(transferencia);
			cajaMayorRepository.findByActualizaCajaMayor(1, transferencia.getMonto(), transferencia.getMontoCheque(), transferencia.getMontoTarjeta());
		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("ERROR:"+e.getMessage()), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@RequestMapping(method=RequestMethod.GET, value = "/cajaMayor/{id}")
	public CajaMayor consultarCajaMayor(@PathVariable int id) {
		return cajaMayorRepository.consultarIDCajaMayor(id);
	}
	@RequestMapping(method=RequestMethod.GET, value = "/transferencia/consultaTodo")
	public List<TransferenciaTesoreria> consultarTransferenciaTesoreria() {
		List<Object []> lisObj= transferenciaTesoreriaRepository.consultarDetalleTransferenciTesoreria();
		List<TransferenciaTesoreria> listRetorno= new ArrayList<TransferenciaTesoreria>();
		for(Object [] ob :lisObj) {
			TransferenciaTesoreria tf= new TransferenciaTesoreria();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getTesoreria().setId(Integer.parseInt(ob[2].toString()));
			tf.getFuncionario().getPersona().setNombre(ob[3].toString());
			tf.getFuncionario().getPersona().setApellido(ob[4].toString());
			tf.setMonto(Double.parseDouble(ob[5].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[6].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[7].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}

	@RequestMapping(method=RequestMethod.POST, value = "/transferenciaCajaChica")
	public ResponseEntity<?> guardarTransferenciaCajaChica(@RequestBody TransferenciaCajaChica transferencia){

		if(transferencia.getFuncionarioE().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(transferencia.getFuncionarioT().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO TRANSFERENCIA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(transferencia.getCajaChica().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("ES NECESARIO EL IDENTIFICADOR DE LA CAJA CHICA!"), HttpStatus.CONFLICT);
		}else if(transferencia.getCajaMayor().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("ES NECESARIO EL IDENTIFICADOR DE LA CAJA MAYOR!"), HttpStatus.CONFLICT);
		}
		//		
		try {
			transferenciaCajaChicaRepository.save(transferencia);
			cajaMayorRepository.findByActualizaTransferenciaCajaChicaPositivo(transferencia.getCajaChica().getId(), transferencia.getMonto(), transferencia.getMontoCheque(), transferencia.getMontoTarjeta());
			cajaMayorRepository.findByActualizaCajaMayorNegativo(1, transferencia.getMonto(), transferencia.getMontoCheque(), transferencia.getMontoTarjeta());
		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("ERROR:"+e.getMessage()), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}
	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaCajaChica/consultaTodo")
	public List<TransferenciaCajaChica> consultarTransferenciaCajaChica() {
		List<Object []> lisObj= transferenciaCajaChicaRepository.consultarDetalleTransferenciaCajaChica();
		List<TransferenciaCajaChica> listRetorno= new ArrayList<TransferenciaCajaChica>();
		for(Object [] ob :lisObj) {
			TransferenciaCajaChica tf= new TransferenciaCajaChica();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getCajaMayor().setDescripcion(ob[2].toString());
			tf.getCajaChica().setDescripcion(ob[3].toString());
			tf.getFuncionarioT().getPersona().setNombre(ob[4].toString()+", "+ob[5].toString());
			tf.getFuncionarioE().getPersona().setNombre(ob[6].toString()+", "+ob[7].toString());
			tf.setMonto(Double.parseDouble(ob[8].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[9].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[10].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/transferenciaCajaMayor")
	public ResponseEntity<?> guardarTransferenciaCajaMayor(@RequestBody TransferenciaCajaMayor transferencia){

		if(transferencia.getFuncionarioT().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO TRANSFERENCIA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		}else if(transferencia.getCajaChica().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("ES NECESARIO EL IDENTIFICADOR DE LA CAJA CHICA!"), HttpStatus.CONFLICT);
		}else if(transferencia.getCajaMayor().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("ES NECESARIO EL IDENTIFICADOR DE LA CAJA MAYOR!"), HttpStatus.CONFLICT);
		}
		//		
		try {
			transferenciaCajaMayorRepository.save(transferencia);
			cajaMayorRepository.findByActualizaCajaMayor(1, transferencia.getMonto(),transferencia.getMontoCheque(), transferencia.getMontoTarjeta());
			cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(transferencia.getCajaChica().getId(), transferencia.getMonto(), transferencia.getMontoCheque(), transferencia.getMontoTarjeta());

		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("ERROR:"+e.getMessage()), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.OK);

	}
	@RequestMapping(method=RequestMethod.POST, value = "/transferencia/all")
	public ResponseEntity<?> guardarTransferenciaCajaMayorTodos(OAuth2Authentication authentication, @RequestBody List<Tesoreria>  listaTesoreriaAtransferir){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		double totalTranferido=0.0;
		double totalTranferidoCheque=0.0;
		double totalTranferidoTarjeta=0.0;
		try {
			for (int i = 0; i < listaTesoreriaAtransferir.size(); i++) {
				Tesoreria teso= listaTesoreriaAtransferir.get(i);
				if (teso.isEstadoAnulacion()==false && teso.isEstadoTransferencia()==false) {
				TransferenciaTesoreria v = new TransferenciaTesoreria();
				v.setFecha(new Date());
				v.getCajaMayor().setId(1);
				v.getFuncionario().setId(usuario.getFuncionario().getId());
				v.setMonto(teso.getImporte());
				v.setMontoCheque(teso.getImporteCheque());
				v.setMontoTarjeta(teso.getImporteTarjeta());
				v.getTesoreria().setId(teso.getId());
				totalTranferido = totalTranferido + v.getMonto();
				totalTranferidoCheque = totalTranferidoCheque + v.getMontoCheque();
				totalTranferidoTarjeta = totalTranferidoTarjeta + v.getMontoTarjeta();
				if(v.getCajaMayor().getId()==0) {
					return new ResponseEntity<>(new CustomerErrorType("EL <1> DE LA CAJA MAYOR NO ESTÁ PRESENTE EN LA BASE DE DATOS EN LA POSICIÓN: "+i), HttpStatus.CONFLICT);	
				}else if(v.getFuncionario().getId()==0){
					return new ResponseEntity<>(new CustomerErrorType("NO SE PUDO OBTENER EL FUNCIONARIO REGISTO EN LA POSICIÓN: "+i), HttpStatus.CONFLICT);	
				}else if(v.getTesoreria().getId()==0){
					return new ResponseEntity<>(new CustomerErrorType("NO SE PUDO CARGAR EL IDENTIFICACDOR DE LA TESORIA A SER TRANSFERIDO EN LA POSICION: "+i), HttpStatus.CONFLICT);	
				}else {
					teso.setEstadoTransferencia(true);
					entityRepository.save(teso);
					transferenciaTesoreriaRepository.save(v);
					cajaMayorRepository.findByActualizaCajaMayor(1, teso.getImporte(), teso.getImporteCheque(), teso.getImporteTarjeta());
		
				}
				
				}else {
					System.out.println("YA SE ANULO O YA SE TRANSFIRIÓ "+teso.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR:"+e.getMessage()), HttpStatus.CONFLICT);
		}
		Map<String, String> map = new HashMap<>();
		
		map.put("monto", totalTranferido+"");
		map.put("montoCheque", totalTranferidoCheque+"");
		map.put("montoTarjeta", totalTranferidoTarjeta+"");
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);

	}

	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaCajaMayor/consultaTodo")
	public List<TransferenciaCajaMayor> consultarTransferenciaCajaMayor() {
		List<Object []> lisObj= transferenciaCajaMayorRepository.consultarDetalleTransferenciaCajaMayor();
		List<TransferenciaCajaMayor> listRetorno= new ArrayList<TransferenciaCajaMayor>();
		for(Object [] ob :lisObj) {
			TransferenciaCajaMayor tf= new TransferenciaCajaMayor();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getCajaChica().setDescripcion(ob[2].toString());
			tf.getCajaMayor().setDescripcion(ob[3].toString());
			tf.getFuncionarioT().getPersona().setNombre(ob[4].toString());
			tf.getFuncionarioT().getPersona().setApellido(ob[5].toString());
			tf.setMonto(Double.parseDouble(ob[6].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[7].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[8].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaAnticipo/consultaTodo")
	public List<TransferenciaAnticipo> consultarTransferenciaAnticipo() {
		List<Object []> lisObj= transferenciaAnticipoRepository.consultarDetalleTransferenciaAnticipos();
		List<TransferenciaAnticipo> listRetorno= new ArrayList<TransferenciaAnticipo>();
		for(Object [] ob :lisObj) {
			TransferenciaAnticipo tf= new TransferenciaAnticipo();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getAnticipo().setId(Integer.parseInt(ob[2].toString()));
			tf.getFuncionario().getPersona().setNombre(ob[3].toString()+ " "+ob[4].toString());
			tf.getAnticipo().getFuncionarioEncargado().getPersona().setNombre(ob[5].toString()+" "+ob[6].toString());
			tf.setMonto(Double.parseDouble(ob[7].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[8].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[9].toString()));
			listRetorno.add(tf);

		}
		return listRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaPagosProveedor/consultaTodo")
	public List<TransferenciaPagosProveedor> consultarTransferenciaPagosProveedor() {
		List<Object []> lisObj= transferenciaPagosProveedorRepository.consultarDetalleTransferenciaPagosProveedorss();
		List<TransferenciaPagosProveedor> listRetorno= new ArrayList<TransferenciaPagosProveedor>();
		for(Object [] ob :lisObj) {
			TransferenciaPagosProveedor tf= new TransferenciaPagosProveedor();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getPagosProveedorCabecera().setId(Integer.parseInt(ob[2].toString()));
			tf.getFuncionario().getPersona().setNombre(ob[3].toString()+ " "+ob[4].toString());
			tf.getPagosProveedorCabecera().getFuncionarioA().getPersona().setNombre(ob[5].toString()+" "+ob[6].toString());
			tf.setMonto(Double.parseDouble(ob[7].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[8].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[9].toString()));
			listRetorno.add(tf);

		}
		return listRetorno;
	}
	//	/tesoreria/transferenciaAnticipo/consultaTodo
	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaCajaMayor/{id}")
	public List<TransferenciaCajaMayor> consultarTransferenciaCajaMayorPorIdCajaChica(@PathVariable int id) {
		List<Object []> lisObj= transferenciaCajaMayorRepository.consultarDetalleTransferenciaCajaMayorPorIdCajaChica(id);
		List<TransferenciaCajaMayor> listRetorno= new ArrayList<TransferenciaCajaMayor>();
		for(Object [] ob :lisObj) {
			TransferenciaCajaMayor tf= new TransferenciaCajaMayor();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getCajaChica().setDescripcion(ob[2].toString());
			tf.getCajaMayor().setDescripcion(ob[3].toString());
			tf.getFuncionarioT().getPersona().setNombre(ob[4].toString());
			tf.getFuncionarioT().getPersona().setApellido(ob[5].toString());
			tf.setMonto(Double.parseDouble(ob[6].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[7].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[8].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaCajaChica/consultaTodo/{idCaja}")
	public List<TransferenciaCajaChica> consultarTransferenciaCajaChicaPorIdCaja(@PathVariable int idCaja) {
		List<TransferenciaCajaChica> lisObj = transferenciaCajaChicaRepository.getTransferenciaPorIdCajaChica(idCaja);
		List<TransferenciaCajaChica> listRetorno = new ArrayList<>();
		for(TransferenciaCajaChica ob :lisObj) {
			TransferenciaCajaChica tf= new TransferenciaCajaChica();
			tf.setId(ob.getId());
			tf.setFecha(ob.getFecha());
			tf.getCajaMayor().setDescripcion(ob.getCajaMayor().getDescripcion());
			tf.getCajaChica().setDescripcion(ob.getCajaChica().getDescripcion());
			tf.getFuncionarioT().getPersona().setNombre(ob.getFuncionarioT().getPersona().getNombre()+ ", "+ob.getFuncionarioT().getPersona().getApellido());
			tf.getFuncionarioE().getPersona().setNombre(ob.getFuncionarioE().getPersona().getNombre()+ ", "+ob.getFuncionarioE().getPersona().getApellido());
			tf.setMonto(ob.getMonto());
			tf.setMontoCheque(ob.getMontoCheque());
			tf.setMontoTarjeta(ob.getMontoTarjeta());
			listRetorno.add(tf);
		}

		return listRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaGasto/{id}")
	public List<TransferenciaGastos> consultarTransferenciaGastoPorIdCajaChica(@PathVariable int id) {
		List<Object []> lisObj= transferenciaGastoRepository.consultarTranferenciaGastoPorIdCajaChica(id);
		List<TransferenciaGastos> listRetorno= new ArrayList<TransferenciaGastos>();
		for(Object [] ob :lisObj) {
			TransferenciaGastos tf= new TransferenciaGastos();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getFuncionario().getPersona().setNombre(ob[2].toString()+" "+ob[3].toString());
			tf.getCajaChica().getFuncionarioE().getPersona().setNombre(ob[4].toString()+" "+ob[5].toString());
			tf.getGastoConsumicionesCabecera().setId(Integer.parseInt(ob[6].toString()));
			tf.setMonto(Double.parseDouble(ob[7].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[8].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[9].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}
	/*
	@RequestMapping(method=RequestMethod.GET, value = "/transferenciaAperturaCaja/{id}")
	public List<TransferenciaAperturaCaja> consultarTransferenciaAperturaCajaPorIdCajaChica(@PathVariable int id) {
		List<Object []> lisObj= transferenciaAperturaCajaRepository.consultarTranferenciaAperturaCajaPorIdCajaChica(id);
		List<TransferenciaAperturaCaja> listRetorno= new ArrayList<TransferenciaAperturaCaja>();
		for(Object [] ob :lisObj) {
			TransferenciaAperturaCaja tf= new TransferenciaAperturaCaja();
			tf.setId(Integer.parseInt(ob[0].toString()));
			tf.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[1].toString()));
			tf.getFuncionario().getPersona().setNombre(ob[2].toString());
			tf.getAperturaCaja().setId(Integer.parseInt(ob[3].toString()));
			tf.getCajaChica().getFuncionarioE().getPersona().setNombre(ob[4].toString());
			
			tf.setMonto(Double.parseDouble(ob[5].toString()));
			tf.setMontoCheque(Double.parseDouble(ob[6].toString()));
			tf.setMontoTarjeta(Double.parseDouble(ob[7].toString()));
			listRetorno.add(tf);
		}

		return listRetorno;
	}
	*/

	@RequestMapping(method=RequestMethod.GET, value="/pruebaaa/{fechaI}/{fechaF}")
	public List<ResumenEntradaSalidaTipoOperacion> getResumenEntradaSalidaTipoOperacion(@PathVariable String fechaI, @PathVariable String fechaF){
		List<ResumenEntradaSalidaTipoOperacion> listRetorno=new ArrayList<>();
		try {
			Calendar cc= Calendar.getInstance();
			SimpleDateFormat formater=new SimpleDateFormat("dd-MM-");
			Date fecI;
			System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
			fecI = formater.parse(fechaI);
			Date fecF=formater.parse(fechaF);
			System.out.println(fecF.getDate());
			fecF.setHours(23);
			fecI.setHours(1);
			//			System.out.println("hora INCIAL::: "+fec+ " hora inicio finbal: "+fecF);

			System.out.println("hora INCIAL::: "+fecI+ " hora inicio finbal: "+fecF);

			Object[][] objetoVentaContadoEfectivo=entityRepository.getResumenEntradaSalidaCajaTipoOperacionVentaContadoEfectivo(fecI, fecF);

			if (objetoVentaContadoEfectivo[0][0]==null) {
				System.out.println("VENTA CONTADO EFECTIVO: "+objetoVentaContadoEfectivo[0][0]);
			}else {
				System.out.println("NO HAY REGISTRO QUE MOSTRAR");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listRetorno;
	}


}
