package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

// import com.bisontecfacturacion.security.JwtTokenUtil;
// import com.bisontecfacturacion.security.JwtUser;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
import com.sun.javafx.beans.IDProperty;

@Transactional()
@RestController
@RequestMapping("aperturaCaja")
public class AperturaCajaController {
	
//	@Autowired
  //  private PlatformTransactionManager txManager;
	
	@Autowired
	private AperturaCajaRepository entityRepository;
	
	@Autowired
	private CajaRepository cajaRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	

	@RequestMapping(method=RequestMethod.GET)
	public List<AperturaCaja> getAll(OAuth2Authentication authentication){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
	
		List<AperturaCaja> list= new ArrayList<>();
		if(usuario.getAdministrador() == true){
			list = entityRepository.findTop100ByOrderByIdDesc();
		}
		else{
			list = entityRepository.getAperturaCajaPorFuncionario(usuario.getFuncionario().getId());
		}
		return list;
	}

	
	@RequestMapping(method=RequestMethod.GET, value="/arqueoCaja")
	public List<AperturaCaja> getListaArqueoCajaActivo(){
		List<AperturaCaja> a= entityRepository.getAperturaCajaActivo();
		List<AperturaCaja> apertura = new ArrayList<>();
		for(AperturaCaja ap: a) {
			AperturaCaja c=new AperturaCaja();
			c.setId(ap.getId());
			c.setFecha(ap.getFecha());
			c.setHora(ap.getHora());
			c.setSaldoInicial(ap.getSaldoInicial());
			c.setSaldoActual(ap.getSaldoActual());
			c.getCaja().setDescripcion(ap.getCaja().getDescripcion());
			c.getFuncionario().getPersona().setNombre(ap.getFuncionario().getPersona().getNombre());
			c.getFuncionario().getPersona().setApellido(ap.getFuncionario().getPersona().getApellido());
			c.setEstado(ap.isEstado());
			c.setSaldoInicialCheque(ap.getSaldoInicialCheque());
			c.setSaldoActualCheque(ap.getSaldoActualCheque());
			c.setSaldoInicialTarjeta(ap.getSaldoInicialTarjeta());
			c.setSaldoActualTarjeta(ap.getSaldoActualTarjeta());
			apertura.add(c);
		}

		return apertura;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/false")
	public List<AperturaCaja> getFalse(){
		return entityRepository.listAperturaFuncionarioFalse();
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarcaja/{id}")
	public ResponseEntity<?> getBuscarCaja(@PathVariable int id){
		int va=0;
		Funcionario funcionario=funcionarioRepository.getIdFuncionario(id);
		if (funcionario != null) {
			va=funcionario.getId();
			return new ResponseEntity<>(entityRepository.getAperturaActivoCaja(va), HttpStatus.OK);	
		} else {
			return new ResponseEntity<>(new CustomerErrorType("El usuario aun no esta relacionado con el funcionario..."), HttpStatus.CONFLICT);
		}
		
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarcaja/idFuncionario/{id}")
	public Integer getBuscarCajaPorIdFuncionario(@PathVariable int id){
		Integer idApe=0;
		idApe = entityRepository.getAperturaActivoCajaId(id);
		return idApe;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarcajaBalcon")
	public ResponseEntity<?> getBuscarCajaBalcon(){
		return new ResponseEntity<>(entityRepository.getAperturaActivoCajaBalcon(), HttpStatus.OK);	
	}
	
	
	@RequestMapping(method =  RequestMethod.GET, value="/funcionarioCaja")
	public List<AperturaCaja> getFuncionarioCajaActivo(){
		List<Object[]> listado= new ArrayList<>();
		listado = entityRepository.getFuncionarioCajaActivo();
		List<AperturaCaja> listadoRetorno= new ArrayList<>();
		for(Object[] ob:listado){
			AperturaCaja apertura= new AperturaCaja();
			apertura.setId(Integer.parseInt(ob[0].toString()));
			apertura.getFuncionario().getPersona().setNombre(ob[1].toString());
			apertura.getFuncionario().getPersona().setApellido(ob[2].toString());
			apertura.getCaja().setDescripcion(ob[3].toString());
			listadoRetorno.add(apertura);
		}
		return listadoRetorno;
	}
	@RequestMapping(method =  RequestMethod.GET, value="/fecha/{fecha}")
	public ResponseEntity<?> getAperturaPorFecha(@PathVariable String fecha){
		List<AperturaCaja> listado= new ArrayList<>();
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]); 
		listado = entityRepository.getAperturaPorFecha(ano, mes, dia);
		if(listado.size() < 1) {
			return new ResponseEntity<>(new CustomerErrorType("NO EXISTE APERTURA PARA LA FECHA: "+dia+"-"+mes+"-"+ano+""), HttpStatus.CONFLICT);
		}
		List<AperturaCaja> listadoRetorno= new ArrayList<>();
		for(AperturaCaja ob:listado){
			AperturaCaja apertura= new AperturaCaja();
			apertura.setId(ob.getId());
			apertura.setSaldoInicial(ob.getSaldoInicial());
			apertura.setSaldoActual(ob.getSaldoActual());
			apertura.setFecha(ob.getFecha());
			apertura.getCaja().setDescripcion(ob.getCaja().getDescripcion());
			apertura.getCaja().setId(ob.getCaja().getId());
			apertura.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre());
			apertura.getFuncionario().getPersona().setApellido(ob.getFuncionario().getPersona().getApellido());
			listadoRetorno.add(apertura);
		}
		return new ResponseEntity<>(listadoRetorno, HttpStatus.OK);
	}
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public AperturaCaja getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
/*
	 private TransactionStatus getTransactionStatus() {
	        DefaultTransactionDefinition dtd = new DefaultTransactionDefinition();
	        dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	        dtd.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
	        dtd.setReadOnly(false);

	        return txManager.getTransaction(dtd);
		}
		*/
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody AperturaCaja entity) throws Exception {
		try {
			int idU=entity.getFuncionario().getId();
			Funcionario funcionario=funcionarioRepository.getIdFuncionario(idU);
			if (funcionario == null) {
				return new ResponseEntity<>(new CustomerErrorType("El usuario aun no esta relacionado con el funcionario..."), HttpStatus.CONFLICT);
			} else {
				List<Object[]> objeto=entityRepository.getAperturaActivoCaja(funcionario.getId());
				List<AperturaCaja> producto=new ArrayList<>();
				
				for(Object[] ob:objeto){
					AperturaCaja pa= new AperturaCaja();
					if(ob[0].equals("")){
						pa.setId(0);
						producto.add(pa);
					}else{
			
					pa.setId(Integer.parseInt(ob[0].toString()));
					pa.getCaja().setDescripcion(ob[2].toString());
					producto.add(pa);
					}
				}
				if(producto.size()>0){
				
					return new ResponseEntity<>(new CustomerErrorType("No se puede guardar Apertura de Caja \nLas creedenciales de este Usuario Tiene activo a su nombre la Caja Nª : "+producto.get(0).getId()+""), HttpStatus.CONFLICT);
				}else {

//					si tiene que guardar apertura
					  entity.getFuncionario().setId(funcionario.getId());
					  entity.setFecha(new Date());
					  entity.setHora(hora());
					  entityRepository.save(entity);
					  cajaRepository.findByActualizaEstado(entity.getCaja().getId(), true);
					  funcionarioRepository.findByActualizarFuncionario(entity.getFuncionario().getId(),true);
					
					  return new  ResponseEntity<String>(HttpStatus.CREATED);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.CONFLICT);
			
			
		}
		
		
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
}
