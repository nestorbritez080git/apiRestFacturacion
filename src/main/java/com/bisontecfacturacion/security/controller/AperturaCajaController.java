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

import com.bisontecfacturacion.security.config.Utilidades;
// import com.bisontecfacturacion.security.JwtTokenUtil;
// import com.bisontecfacturacion.security.JwtUser;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CajaMayor;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;
import com.bisontecfacturacion.security.model.TransferenciaAperturaCaja;
import com.bisontecfacturacion.security.model.TransferenciaCajaMayorCajaAperturaInicialCajero;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.CajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaCabeceraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAperturaCajaRepository;
import com.bisontecfacturacion.security.repository.TransferenciaCajaMayorCajaAperturaInicialCajeroRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

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
	private CajaMayorRepository cajaMayorRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OperacionCajaCabeceraRepository cabeceraRepository;
	@Autowired
	private ConceptoRepository conceptoRepository;
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@Autowired
	private TransferenciaAperturaCajaRepository transferenciaAperturaCajaRepository;
	@Autowired
	private TransferenciaCajaMayorCajaAperturaInicialCajeroRepository trCajaMayorCajaAperturaInicialCajeroRepository;
	


	@RequestMapping(method=RequestMethod.GET)
	public List<AperturaCaja> getAll(OAuth2Authentication authentication){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
	
		List<AperturaCaja> list= new ArrayList<>();
		if(usuario.getAdministrador() == true){
			list = entityRepository.findTop20ByOrderByIdDesc();
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
	@RequestMapping(method=RequestMethod.GET, value="/arqueoCaja/idDistinto/{idDistinto}")
	public List<AperturaCaja> getListaArqueoCajaActivoIdDistinto(@PathVariable int idDistinto){
		List<AperturaCaja> a= entityRepository.getAperturaCajaActivoIdDistinto(idDistinto);
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
	@RequestMapping(method=RequestMethod.GET,value="/buscarcaja/idFuncionario/id/{id}")
	public AperturaCaja getApèrturaCajaPorIdFuncionario(@PathVariable int id){
		return entityRepository.getAperturaActivoCajaIdFuncionario(id);
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
					CajaMayor cajaOrigen = cajaMayorRepository.findById(1).orElse(null);
					if (cajaOrigen == null | cajaOrigen.getId()==0) {
					    return new ResponseEntity<>(new CustomerErrorType("No se encontró la caja mayor de origen."), HttpStatus.CONFLICT);
					}
					// Validaciones de montos disponibles
					if (entity.getSaldoInicial() > cajaOrigen.getMonto()) {
					    System.out.println("Monto efectivo no disponible en la caja mayor para procesar inicio de caja");
					    return new ResponseEntity<>(new CustomerErrorType("Monto efectivo no disponible en la caja mayor para procesar de  de caja"), HttpStatus.CONFLICT);
					}

					if (entity.getSaldoInicialCheque() > cajaOrigen.getMontoCheque()) {
					    return new ResponseEntity<>(new CustomerErrorType("Monto cheque no disponible en la caja mayor para procesar de caja"), HttpStatus.CONFLICT);
					}

					if (entity.getSaldoInicialTarjeta() > cajaOrigen.getMontoTarjeta()) {
					    System.out.println("Monto en transferencia/QR supera el disponible");
					    return new ResponseEntity<>(new CustomerErrorType("Monto tarjeta no disponible en la caja mayor para procesar  de caja"), HttpStatus.CONFLICT);
					}
						
//					si tiene que guardar apertura
					  entity.getFuncionario().setId(funcionario.getId());
					  entity.setFecha(new Date());
					  entity.setSaldoActual(entity.getSaldoInicial());
					  entity.setSaldoActualCheque(entity.getSaldoInicialCheque());
					  entity.setSaldoActualTarjeta(entity.getSaldoInicialTarjeta());
					  entity.setHora(hora());
					  AperturaCaja aper = entityRepository.save(entity);
					  cajaRepository.findByActualizaEstado(entity.getCaja().getId(), true);
					  funcionarioRepository.findByActualizarFuncionario(entity.getFuncionario().getId(),true);
					  if(entity.getSaldoInicial()>0 || entity.getSaldoInicialCheque()>0 || entity.getSaldoInicialTarjeta()>0) {
						  TransferenciaCajaMayorCajaAperturaInicialCajero tr = new  TransferenciaCajaMayorCajaAperturaInicialCajero();
						  tr.getAperturaCaja().setId(aper.getId());
						  tr.getFuncionario().setId(aper.getFuncionario().getId());
						  tr.getCajaMayor().setId(cajaOrigen.getId());
						  tr.setFecha(new Date());
						  if(entity.getSaldoInicial()>0) {tr.setMonto(entity.getSaldoInicial());}
						  if(entity.getSaldoInicialCheque()>0) {tr.setMontoCheque(entity.getSaldoInicialCheque());}
						  if(entity.getSaldoInicialTarjeta()>0) {tr.setMontoTarjeta(entity.getSaldoInicialTarjeta());}
						  trCajaMayorCajaAperturaInicialCajeroRepository.save(tr);
						  cajaMayorRepository.findByActualizaCajaMayorNegativo(tr.getCajaMayor().getId(), tr.getMonto(), tr.getMontoCheque(), tr.getMontoTarjeta());

					  }
					  List<OperacionCaja> listOperacion = new ArrayList<OperacionCaja>();
					  
					  if(entity.getSaldoInicial()>0) {
						  
						  
						  OperacionCaja op= new OperacionCaja();
						  op.getAperturaCaja().setId(aper.getId());
						  op.getConcepto().setId(15);
						  op.getTipoOperacion().setId(1);//Efectivo
						  op.setEfectivo(0.0);
						  op.setFecha(new Date());
						  op.setMonto(entity.getSaldoInicial());
						  op.setTipo("ENTRADA");
						  op.setMotivo("APERTURA CAJA REF.: "+aper.getId());
						  //operacionCajaRepository.save(op);
						  listOperacion.add(op);
					  }
					  if(entity.getSaldoInicialCheque()>0) {
						  OperacionCaja op= new OperacionCaja();
						  op.getAperturaCaja().setId(aper.getId());
						  op.getConcepto().setId(15);
						  op.getTipoOperacion().setId(2);//Cheque
						  op.setEfectivo(0.0);
						  op.setFecha(new Date());
						  op.setMonto(entity.getSaldoInicialCheque());
						  op.setTipo("ENTRADA");
						  op.setMotivo("APERTURA CAJA REF.: "+aper.getId());
						  listOperacion.add(op);
						  //operacionCajaRepository.save(op);
					  }
					  if(entity.getSaldoInicialTarjeta()>0) {
						  OperacionCaja op= new OperacionCaja();
						  op.getAperturaCaja().setId(aper.getId());
						  op.getConcepto().setId(15);
						  op.getTipoOperacion().setId(3);
						  op.setEfectivo(0.0);
						  op.setFecha(new Date());
						  op.setMonto(entity.getSaldoInicialTarjeta());
						  op.setTipo("ENTRADA");
						  op.setMotivo("APERTURA CAJA REF.: "+aper.getId());
						  //operacionCajaRepository.save(op);
						  listOperacion.add(op);
					  }
					  if(listOperacion.size()>0) {
						 procesarOperacionCajaApertura(aper, listOperacion);
					  }
					  
					  
					  
					  return new  ResponseEntity<String>(HttpStatus.CREATED);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.CONFLICT);
			
			
		}
		
		
	}
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaApertura(AperturaCaja ent, List<OperacionCaja> listaOperacion) {
	    if (listaOperacion == null || listaOperacion.isEmpty()) {
	        throw new RuntimeException("No existen operaciones de caja para procesar");
	    }
	    List<OperacionCaja> resultado = new ArrayList<>();
	    OperacionCaja primera = listaOperacion.get(0);
	    Concepto concepto = conceptoRepository.findById(primera.getConcepto().getId())
	            .orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
	    // 🔹 Crear cabecera
	    OperacionCajaCabecera cab = new OperacionCajaCabecera();
	    cab.setFecha(new Date());
	    cab.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
	    cab.setReferenciaOperacion(ent.getId());
	    cab.setTipo("ENTRADA");
	    cab.setMotivo(concepto.getDescripcion()+"REF.: "+ent.getId());
	    cab.setConcepto(concepto);
	    cab.setAperturaCaja(primera.getAperturaCaja());
	    OperacionCajaCabecera savedCabecera = cabeceraRepository.save(cab);
	    // 🔹 Procesar operaciones
	    for (OperacionCaja ope : listaOperacion) {
	            System.out.println("EJECUTO OPERACION APERTURA PROCEDIMIENTO");
	            
	            ope.setOperacionCajaCabecera(savedCabecera);
	            OperacionCaja saveOperacion = operacionCajaRepository.save(ope);
	            int tipoOperacion = saveOperacion.getTipoOperacion().getId();
	            int idApertura = saveOperacion.getAperturaCaja().getId();
	            double monto = saveOperacion.getMonto();
	            // 🔹 Actualizar saldo según tipo
	            // 🔥 Actualizar saldos según tipo operación
		        if (tipoOperacion == 1) {
		            //entityRepository.findByActualizarAperturaSaldo(idApertura,monto);
		        }
		        if (tipoOperacion == 2) {
		        	//entityRepository.findByActualizarAperturaSaldoCheque(idApertura,monto);
		        }
		        if (tipoOperacion == 3) {
		        	//entityRepository.findByActualizarAperturaSaldoTarjeta(idApertura,monto);
		        }
		        //OperacionCaja saved = entityRepository.save(ope);

		        resultado.add(saveOperacion);
	    }
	    //ent.setEstado("FACTURADO");
		//ventaRepositoty.findByActualizarVentaOperacion(ent.getId(),savedCabecera.getId());
		//ventaRepositoty.findByActualizarFacturas(ent.getId(), "FACTURADO");
	 
	    
	    return resultado;
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value = "/guardarTransferencia")
	public ResponseEntity<?>  guardarTrasnferenciaAperturaCaja(@RequestBody TransferenciaAperturaCaja entity) throws Exception {
		try {
			/*
			AperturaCaja ape= entityRepository.getAperturaCajaPorIdCaja(entity.getAperturaCaja().getId());
			if((ape.getSaldoActual()< entity.getMonto())|| (+ape.getSaldoActualCheque()<entity.getMontoCheque()) || (ape.getSaldoActualTarjeta()<entity.getMontoTarjeta())) {
				return new ResponseEntity<>(new CustomerErrorType("NO SE PUEDE TRANSFERIR, MONTO SOBREPASADO EN CAJA!"), HttpStatus.CONFLICT);
			}
			
			transferenciaAperturaCajaRepository.save(entity);
			
			OperacionCaja o = new OperacionCaja();
			o.getAperturaCaja().setId(entity.getAperturaCaja().getId());
			o.getConcepto().setId(14);//transderencia caja chica;
			if(entity.getMonto()>0) {
				o.getTipoOperacion().setId(1);	
				o.setMonto(entity.getMonto());
				entityRepository.findByActualizarAperturaSaldoActualAnulacionVenta(entity.getAperturaCaja().getId(), entity.getMonto());
			}
			if(entity.getMontoCheque()>0){
				o.getTipoOperacion().setId(2);
				o.setMonto(entity.getMontoCheque());
				entityRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(entity.getAperturaCaja().getId(), entity.getMontoCheque());
			}
			if(entity.getMontoTarjeta()>0){
				o.getTipoOperacion().setId(3);
				o.setMonto(entity.getMontoTarjeta());
				entityRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(entity.getAperturaCaja().getId(),entity.getMontoTarjeta());
			}
			o.setReferenciaTipoOperacion(entity.getCajaChica().getDescripcion());//se unsa este enlace para recibir referncia operacion
			o.setTipo("SALIDA");
			Concepto c= new Concepto();
			c = conceptoRepository.getOne(14);
			o.setMotivo(c.getDescripcion()+" REF.: "+transferenciaAperturaCajaRepository.getUltimaTransferenciaAperturaCaja());
			
			operacionCajaRepository.save(o);
			cajaMayorRepository.findByActualizaTransferenciaCajaChicaPositivo(entity.getCajaChica().getId(), entity.getMonto(), entity.getMontoCheque(), entity.getMontoTarjeta());

			*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.CONFLICT);
			
			
		}
		return new ResponseEntity<>(HttpStatus.OK);	
		
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}
	
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value = "/buscar/filtro")
	public List<AperturaCaja> getAperturaCajaFiltro(OAuth2Authentication authentication, @RequestBody String descripcion){
		
		List<AperturaCaja> objeto= new ArrayList<>();
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario.getAdministrador() == true){
			//list = entityRepository.findTop20ByOrderByIdDesc();
			if (descripcion.equals("9999999999")) {
				objeto=entityRepository.findTop20ByOrderByIdDesc();
				//return product(objeto);
			} else {
				 String filtro = "%" + Utilidades.eliminaCaracterIzqDer(descripcion.trim().toLowerCase()) + "%";
				 System.out.println("FILTRO: "+filtro);
				 objeto=entityRepository.getBuscarPorDescripcion(filtro);
			}
		}else{
			objeto = entityRepository.getAperturaCajaPorFuncionario(usuario.getFuncionario().getId());
			//list = entityRepository.getAperturaCajaPorFuncionario(usuario.getFuncionario().getId());
		}
			
		
		return product(objeto);
	}

	public List<AperturaCaja> product(List<AperturaCaja> objeto) {
		List<AperturaCaja> producto=new ArrayList<>();
		for(AperturaCaja ob:objeto){
			AperturaCaja pp=new AperturaCaja();
			pp.setId(ob.getId());
			pp.setFuncionario(ob.getFuncionario());
			pp.getFuncionario().setPersona(ob.getFuncionario().getPersona());
			pp.setFecha(ob.getFecha());
			pp.setEstado(ob.isEstado());
			pp.setEstadoAnulacion(ob.isEstadoAnulacion());
			pp.setHora(ob.getHora());
			pp.setCaja(ob.getCaja());
			pp.setSaldoInicial(ob.getSaldoInicial());
			pp.setSaldoInicialCheque(ob.getSaldoInicialCheque());
			pp.setSaldoInicialTarjeta(ob.getSaldoInicialTarjeta());
			producto.add(pp);
		}


		return producto;
	}

	
}
