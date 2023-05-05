package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.record.ObjRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.model.AperturaCaja;
// import com.bisontecfacturacion.security.JwtTokenUtil;
// import com.bisontecfacturacion.security.JwtUser;
import com.bisontecfacturacion.security.model.CierreCaja;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Tesoreria;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaRepository;
import com.bisontecfacturacion.security.repository.CierreCajaRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.TesoreriaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;


@RestController
@RequestMapping("cierreCaja")
public class CierreCajaController {
	@Autowired
	private CierreCajaRepository entityRepository;
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private CajaRepository cajaRepository;
	@Autowired
	private TesoreriaRepository tesoreriaRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/idCierre")
	public Map<String, Integer> getIdCierre(){
		CierreCaja c=entityRepository.findTop1ByOrderByIdDesc();
		Map<String, Integer> number = new HashMap<>();
		number.put("idCierre", c.getId());
		return number;
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<CierreCaja> getPAll(OAuth2Authentication authentication){

		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		
		List<CierreCaja> list= new ArrayList<>();
		if(usuario.getAdministrador() == true) {
			list = entityRepository.findTop100ByOrderByIdDesc();
		}
		else{
			list = entityRepository.getCierreCajaPorFuncionario(usuario.getFuncionario().getId());
		}
		return list;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/lista")
	public ResponseEntity<?> guardarLista(@RequestBody CierreCaja entity){
		System.out.println("Entro desde lista");
		int idape=0;
		int idFun=0;
		double imp_ingreso=0.00, imp_egreso=0.00, saldoCajaIni=0.00,saldoCajaAct=0.00, montoCierre=0.00;
		double imp_ingresoCheque=0.00, imp_egresoCheqe=0.00, saldoCajaIniCheque=0.00,saldoCajaActCheque=0.00, montoCierreCheque=0.00;
		double imp_ingresoTarjeta=0.00, imp_egresoTarjeta=0.00, saldoCajaIniTarjeta=0.00,saldoCajaActTarjeta=0.00, montoCierreTarjeta=0.00;
		idape= entity.getAperturaCaja().getId();
		idFun=entity.getAperturaCaja().getFuncionario().getId();
		saldoCajaIni= entity.getAperturaCaja().getSaldoInicial();
		saldoCajaAct= entity.getAperturaCaja().getSaldoActual();
		saldoCajaIniCheque= entity.getAperturaCaja().getSaldoInicialCheque();
		saldoCajaActCheque= entity.getAperturaCaja().getSaldoActualCheque();
		saldoCajaIniTarjeta= entity.getAperturaCaja().getSaldoInicialTarjeta();
		saldoCajaActTarjeta= entity.getAperturaCaja().getSaldoActualTarjeta();
		
		if(entity.getMonto()>(saldoCajaAct+saldoCajaIni)){
			imp_ingreso=entity.getMonto()-(saldoCajaAct+saldoCajaIni);
		}
		if(entity.getMonto()<(saldoCajaAct+saldoCajaIni)){
			imp_egreso= (saldoCajaAct+saldoCajaIni)-entity.getMonto();
		}
		if(entity.getMontoCheque()>(saldoCajaActCheque+saldoCajaIniCheque)){
			imp_ingresoCheque=entity.getMontoCheque()-(saldoCajaActCheque+saldoCajaIniCheque);
		}
		if(entity.getMontoCheque()<(saldoCajaActCheque+saldoCajaIniCheque)){
			imp_egresoCheqe= (saldoCajaActCheque+saldoCajaIniCheque)-entity.getMontoCheque();
		}
		if(entity.getMontoTarjeta()>(saldoCajaActTarjeta+saldoCajaIniTarjeta)){
			imp_ingresoTarjeta=entity.getMontoTarjeta()-(saldoCajaActTarjeta+saldoCajaIniTarjeta);
		}
		if(entity.getMontoTarjeta()<(saldoCajaActTarjeta+saldoCajaIniTarjeta)){
			imp_egresoTarjeta= (saldoCajaActTarjeta+saldoCajaIniTarjeta)-entity.getMontoTarjeta();
		}
		entity.setImputacionEgreso(imp_egreso);
		entity.setImputacionIngreso(imp_ingreso);
		entity.setImputacionEgresoCheque(imp_egresoCheqe);
		entity.setImputacionIngresoCheque(imp_ingresoCheque);
		entity.setImputacionEgresoTarjeta(imp_egresoTarjeta);
		entity.setImputacionIngresoTarjeta(imp_ingresoTarjeta);
		
		entity.setEstadoEntrega(false);
		entity.setEstadoRecibo(false);
		
		entity.setFecha(new Date());
		entity.setHora(hora());
		entityRepository.save(entity);
		
		Tesoreria tesoreria= new Tesoreria();
		tesoreria.getCierreCaja().setId(entityRepository.findTop1ByOrderByIdDesc().getId());
		tesoreria.setImporte(entity.getMonto());
		tesoreria.setImporteCheque(entity.getMontoCheque());
		tesoreria.setImporteTarjeta(entity.getMontoTarjeta());
		tesoreria.setFecha(entity.getFecha());
		tesoreria.setHora(entity.getHora());
		tesoreria.getFuncionario().setId(entity.getFuncionario().getId());
		tesoreriaRepository.save(tesoreria);
		funcionarioRepository.findByActualizarFuncionario(idFun, false);
		aperturaCajaRepository.findByActualizarFuncionario(idape, false);
		aperturaCajaRepository.findByActualizarEstadoAnulacionApertura(idape, false);
		cajaRepository.findByActualizaEstado(entity.getAperturaCaja().getCaja().getId(), false);
		
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody CierreCaja entity){
		System.out.println("Entro cierre normal");
		List<Object[]> apertura =this.aperturaCajaRepository.getAperturaActivo(entity.getAperturaCaja().getCaja().getId()); 	
		int idape=0;
		int idFun=0;
		double imp_ingreso=0.00, imp_egreso=0.00, saldoCajaIni=0.00,saldoCajaAct=0.00, montoCierre=0.00;
		double imp_ingresoCheque=0.00, imp_egresoCheqe=0.00, saldoCajaIniCheque=0.00,saldoCajaActCheque=0.00, montoCierreCheque=0.00;
		double imp_ingresoTarjeta=0.00, imp_egresoTarjeta=0.00, saldoCajaIniTarjeta=0.00,saldoCajaActTarjeta=0.00, montoCierreTarjeta=0.00;
		
		
		for(Object[] ob:apertura){
			idape= Integer.parseInt(ob[0].toString());
			idFun= Integer.parseInt(ob[1].toString());
			saldoCajaIni= Double.parseDouble(ob[2].toString());
			saldoCajaAct= Double.parseDouble(ob[3].toString());
			saldoCajaIniCheque= Double.parseDouble(ob[5].toString());
			saldoCajaActCheque= Double.parseDouble(ob[6].toString());
			saldoCajaIniTarjeta= Double.parseDouble(ob[7].toString());
			saldoCajaActTarjeta= Double.parseDouble(ob[8	].toString());
			
		}
		entity.getAperturaCaja().setId(idape);
		Funcionario funcionario=funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId());
		if (funcionario == null) {
			return new ResponseEntity<>(new CustomerErrorType("El usuario aun no esta relacionado con el funcionario..."), HttpStatus.CONFLICT);
		} else {
		
		int funcionarioId=funcionario.getId();
		
		if (funcionarioId==idFun) {
			entity.getFuncionario().setId(idFun);
			if(entity.getMonto()>(saldoCajaAct+saldoCajaIni)){
				imp_ingreso=entity.getMonto()-(saldoCajaAct+saldoCajaIni);
			}
			if(entity.getMonto()<(saldoCajaAct+saldoCajaIni)){
				imp_egreso= (saldoCajaAct+saldoCajaIni)-entity.getMonto();
			}
			if(entity.getMontoCheque()>(saldoCajaActCheque+saldoCajaIniCheque)){
				imp_ingresoCheque=entity.getMontoCheque()-(saldoCajaActCheque+saldoCajaIniCheque);
			}
			if(entity.getMontoCheque()<(saldoCajaActCheque+saldoCajaIniCheque)){
				imp_egresoCheqe= (saldoCajaActCheque+saldoCajaIniCheque)-entity.getMontoCheque();
			}
			if(entity.getMontoTarjeta()>(saldoCajaActTarjeta+saldoCajaIniTarjeta)){
				imp_ingresoTarjeta=entity.getMontoTarjeta()-(saldoCajaActTarjeta+saldoCajaIniTarjeta);
			}
			if(entity.getMontoTarjeta()<(saldoCajaActTarjeta+saldoCajaIniTarjeta)){
				imp_egresoTarjeta= (saldoCajaActTarjeta+saldoCajaIniTarjeta)-entity.getMontoTarjeta();
			}
			
			
			entity.setImputacionEgreso(imp_egreso);
			entity.setImputacionIngreso(imp_ingreso);
			entity.setImputacionEgresoCheque(imp_egresoCheqe);
			entity.setImputacionIngresoCheque(imp_ingresoCheque);
			entity.setImputacionEgresoTarjeta(imp_egresoTarjeta);
			entity.setImputacionIngresoTarjeta(imp_ingresoTarjeta);
			
			entity.setEstadoEntrega(false);
			entity.setEstadoRecibo(false);
			
			entity.setFecha(new Date());
			entity.setHora(hora());
			entityRepository.save(entity);
			
			Tesoreria tesoreria= new Tesoreria();
			tesoreria.getCierreCaja().setId(entityRepository.findTop1ByOrderByIdDesc().getId());
			tesoreria.setImporte(entity.getMonto());
			tesoreria.setImporteCheque(entity.getMontoCheque());
			tesoreria.setImporteTarjeta(entity.getMontoTarjeta());
			tesoreria.setFecha(entity.getFecha());
			tesoreria.setHora(entity.getHora());
			tesoreria.getFuncionario().setId(funcionarioId);
			tesoreriaRepository.save(tesoreria);
			funcionarioRepository.findByActualizarFuncionario(idFun, false);
			aperturaCajaRepository.findByActualizarFuncionario(idape, false);
			cajaRepository.findByActualizaEstado(entity.getAperturaCaja().getCaja().getId(), false);
		}else {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO QUE ESTA TRATANDO DE CERRAR LA PAERTURA DE LA CAJA NO CONICIDE CON EL FUNCIONARIO QUE DIO ApERTURA A LA CAJA SELECCIONADA"), HttpStatus.CONFLICT);
		}
		}
	
		
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	@RequestMapping(method=RequestMethod.GET, value="/test/{id}")
	public List<Object[]> getPAlls(@PathVariable int id){
		return aperturaCajaRepository.getAperturaActivo(id);
	}
	@RequestMapping(method=RequestMethod.GET, value="/cierreCaja/updateEntregado/{id}")
	public void updateCierreEstadoEntregado(@PathVariable int id){
		entityRepository.findByActualizarRecibido(id, true);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cierreCaja/updateEntregados/{id}")
	public void updateCierreEstadoEntregados(@PathVariable int id){
		entityRepository.findByActualizarEntregado(id, true);
	}
	@RequestMapping(method=RequestMethod.GET, value="/cierreCaja/updateRecibido/{id}")
	public void updateCierreEstadoRecibido(@PathVariable int id){
		entityRepository.findByActualizarRecibido(id, true);
	}


	@RequestMapping(method =  RequestMethod.GET, value="/fecha/{fecha}")
	public ResponseEntity<?> getAperturaPorFecha(@PathVariable String fecha){
		List<CierreCaja> listado= new ArrayList<>();
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]); 
		listado = entityRepository.getCierreCajaPorFecha(ano, mes, dia);
		List<CierreCaja> listadoRetorno= new ArrayList<>();
		if(listado.size() < 1) {
			return new ResponseEntity<>(new CustomerErrorType("AUN NO EXISTE EL CIERRE DE CAJA PARA LA FECHA: "+dia+"-"+mes+"-"+ano), HttpStatus.CONFLICT);
		}
		for(CierreCaja ob:listado){
			CierreCaja cierre= new CierreCaja();
			cierre.getAperturaCaja().getCaja().setDescripcion(ob.getAperturaCaja().getCaja().getDescripcion());
			
			cierre.setFecha(ob.getFecha());
			cierre.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre());
			cierre.getFuncionario().getPersona().setApellido(ob.getFuncionario().getPersona().getApellido());
			cierre.getAperturaCaja().setSaldoActual(ob.getAperturaCaja().getSaldoActual());
			cierre.setMonto(ob.getMonto());
			cierre.setEstadoEntrega(ob.isEstadoEntrega());
			cierre.setEstadoRecibo(ob.isEstadoRecibo());
			cierre.setImputacionIngreso(ob.getImputacionIngreso());
			cierre.setImputacionEgreso(ob.getImputacionEgreso());
			cierre.setHora(ob.getHora());
			listadoRetorno.add(cierre);
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	
	private List< DetalleServicios> listadoDetalleServicios(List<Object[]> obj) {
		List< DetalleServicios> listaRetrono= new ArrayList<DetalleServicios>();
		
		for(Object[]o: obj){
			DetalleServicios  c= new DetalleServicios();
			c.setId(Integer.parseInt(o[0].toString()));
			c.setDescripcion(o[1].toString());
			c.setCantidad(Double.parseDouble(o[2].toString()));
			c.setPrecio(Double.parseDouble(o[3].toString()));
			c.setSubTotal(Double.parseDouble(o[4].toString()));
			c.getFuncionario().getPersona().setNombre(o[5].toString()+" "+o[6].toString());
			listaRetrono.add(c);
		}
		
		return listaRetrono;
	}
	private List<DetalleProducto> listadoDetalleProducto(List<Object[]> obj) {
		List<DetalleProducto> listaRetrono= new ArrayList<DetalleProducto>();
		
		for(Object[]o: obj){
			DetalleProducto  c= new DetalleProducto();
			c.setId(Integer.parseInt(o[0].toString()));
			c.setDescripcion(o[1].toString());
			c.setCantidad(Double.parseDouble(o[2].toString()));
			c.setPrecio(Double.parseDouble(o[3].toString()));
			c.setSubTotal(Double.parseDouble(o[4].toString()));
			listaRetrono.add(c);
		}
		
		return listaRetrono;
	}
	private CierreCaja listadoCierre(Object[][] ob) {
			
			CierreCaja c= new CierreCaja();
			c.getAperturaCaja().getCaja().setDescripcion(ob[0][0].toString());
			c.getFuncionario().getPersona().setNombre(ob[0][1].toString());
			c.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[0][2].toString()));
			c.getAperturaCaja().setSaldoInicial(Double.parseDouble(ob[0][3].toString()));
			c.getAperturaCaja().setSaldoInicialCheque(Double.parseDouble(ob[0][4].toString()));
			c.getAperturaCaja().setSaldoInicialTarjeta(Double.parseDouble(ob[0][5].toString()));
			c.getAperturaCaja().setSaldoActual(Double.parseDouble(ob[0][6].toString()));
			c.getAperturaCaja().setSaldoActualCheque(Double.parseDouble(ob[0][7].toString()));
			c.getAperturaCaja().setSaldoActualTarjeta(Double.parseDouble(ob[0][8].toString()));
			c.setMonto(Double.parseDouble(ob[0][9].toString()));
			c.setMontoCheque(Double.parseDouble(ob[0][10].toString()));
			c.setMontoTarjeta(Double.parseDouble(ob[0][11].toString()));
			c.setImputacionIngreso(Double.parseDouble(ob[0][12].toString()));
			c.setImputacionEgreso(Double.parseDouble(ob[0][13].toString()));
			c.setImputacionIngresoCheque(Double.parseDouble(ob[0][14].toString()));
			c.setImputacionEgresoCheque(Double.parseDouble(ob[0][15].toString()));
			c.setImputacionIngresoTarjeta(Double.parseDouble(ob[0][16].toString()));
			c.setImputacionEgresoTarjeta(Double.parseDouble(ob[0][17].toString()));
			c.getAperturaCaja().setId(Integer.parseInt(ob[0][18].toString()));
			c.setEstadoAnulacion(Boolean.parseBoolean(ob[0][19].toString()));
			c.setId(Integer.parseInt(ob[0][20].toString()));
		
		return c;
	}
	@RequestMapping(method =  RequestMethod.GET, value="/resumenCierre/{idCierre}")
	public CierreCaja getAperturaPorFecha(@PathVariable int idCierre){
		return listadoCierre(entityRepository.getResumenCierreCaja(idCierre));
	}
	@RequestMapping(method =  RequestMethod.GET, value="/resumenCierre/detalleServicios/{idApertura}")
	public List<DetalleServicios> getDetalleServicioPorAperturaId(@PathVariable int idApertura){
		return listadoDetalleServicios(entityRepository.getDetalleServicioPorApertura(idApertura));
	}
	@RequestMapping(method =  RequestMethod.GET, value="/resumenCierre/detalleServicios/totalizados/{idApertura}")
	public Double getDetalleServicioTotalPorAperturaId(@PathVariable int idApertura){
		Double retorno=0.0;
		retorno=entityRepository.getDetalleServicioPorAperturaTotal(idApertura);
		return retorno;
	}
	
	@RequestMapping(method =  RequestMethod.GET, value="/resumenCierre/detalleProducto/{idApertura}")
	public List<DetalleProducto> getDetalleProductoPorAperturaId(@PathVariable int idApertura){
		return listadoDetalleProducto(entityRepository.getDetalleProductoPorApertura(idApertura));
	}
	
	@RequestMapping(method =  RequestMethod.GET, value="/resumenCierre/detalleProducto/totalizados/{idApertura}")
	public Double getDetalleProductoTotalPorAperturaId(@PathVariable int idApertura){
		
		Double retorno=0.0;
		retorno=entityRepository.getDetalleProductoPorAperturaTotal(idApertura);
		//if(res[0][0]==null) {retorno=0.0;}else {retorno=Double.parseDouble(res[0][0].toString());}
		return retorno;
	}
	
	
	
	
}
