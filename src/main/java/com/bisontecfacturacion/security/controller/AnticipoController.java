package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;

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

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.AnticipoReferenciaCajaChica;
import com.bisontecfacturacion.security.model.AnticipoReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.AnulacionesAnticipo;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.GastoConsumicionesReferenciaCajaChica;
import com.bisontecfacturacion.security.model.GastoConsumicionesReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Periodo;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.TransferenciaAnticipo;
import com.bisontecfacturacion.security.model.TransferenciaGastos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnticipoReferenciaOperacionCajaRepository;
import com.bisontecfacturacion.security.repository.AnticipoReferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.AnulacionesAnticipoRepository;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CajaChicaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("anticipo")
public class AnticipoController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private AnticipoRepository entityRepository;
	
	@Autowired
	private AnulacionesAnticipoRepository anulacionAnticipoRepository;
	
	@Autowired
	private OperacionCajaRepository operacionRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private CajaMayorRepository cajaMayorRepository;
	@Autowired
	private CajaChicaRepository cajaChicaRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private AnticipoReferenciaOperacionCajaRepository anticipoReferenciaOperacionCajaRepository;
	
	@Autowired
	private AnticipoReferenciaCajaChicaRepository  anticipoReferenciaCajaChicaRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	
	
	@Autowired
	private TransferenciaAnticipoRepository transferenciaAnticipoRepository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Anticipo> getAnticipo(){
		return cargarListado(entityRepository.consultarTodo());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/consultarId/{id}")
	public Anticipo getId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/consultarId/referenciaCajaChica/{id}")
	public AnticipoReferenciaCajaChica getIdReferenciaCajaChica(@PathVariable int id){
		return anticipoReferenciaCajaChicaRepository.getReferenciaCajaChicaPorIdAnticipo(id);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/consultarId/referenciaOperacionCaja/{id}")
	public AnticipoReferenciaOperacionCaja getIdReferenciaOperacionCaja(@PathVariable int id){
		return anticipoReferenciaOperacionCajaRepository.getReferenciaOperacionCajaPorIdAnticipo(id);
	}
	
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable int id){
		
		entityRepository.deleteById(id);
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Anticipo entity){
		try {
			//entity.getFuncionarioAutorizado().setId(0);
			Integer apertura=0, cajaChica=0;
			if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==true) {
				cajaChica= entity.getConcepto().getId();
				entity.setTipo("T-C");
				CajaChica XX = new CajaChica();
				XX=cajaChicaRepository.getCajaChicaPorIdCaja(cajaChica);
				if(XX==null) {
					System.out.println("entrooo null caja chiac");
					return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE CAJA CHICA A SU NOMBRE!"), HttpStatus.CONFLICT);
				}else {
					if(XX.getMonto() < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
						System.out.println("entrooo monto superaod efe");
						return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if(XX.getMonto()< entity.getMonto()&& entity.getTipoOperacion().getId()==2){
						System.out.println("entrooo monto superaod che");
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if(XX.getMonto()< entity.getMonto() && entity.getTipoOperacion().getId()==3){
						System.out.println("entrooo monto superaod tarj");
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else{
						
					}
				}
			}
			if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==false) {
				apertura= entity.getConcepto().getId();
				entity.setTipo("T-A");
				System.out.println("GASTOS POR APERTURA DE CAJA "+ +apertura);
				AperturaCaja XX = new AperturaCaja();
				XX=aperturaCajaRepository.getAperturaCajaPorIdCaja(apertura);
				if(XX==null) {
					System.out.println("entrooo null caja chiac");
					return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
				}else {
					if((XX.getSaldoActual()) < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
						System.out.println("entrooo monto superaod efe");
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if((XX.getSaldoActualCheque()) < entity.getMonto()&& entity.getTipoOperacion().getId()==2){
						System.out.println("entrooo monto superaod che");
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else if((XX.getSaldoActualTarjeta())< entity.getMonto() && entity.getTipoOperacion().getId()==3){
						System.out.println("entrooo monto superaod tarj");
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
					}else{
						
					}
				}
			}
			entity.getConcepto().setId(21);
			
			if(entity.getFuncionarioRegistro().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioAutorizado().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO AUTORIZADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioEncargado().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENCARGADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
			}else if(entity.getMonto()<= 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO DEBE SER MAYOR A CERO PARA PODER GUARDAR ANTICIPO.!"), HttpStatus.CONFLICT);
			}
			if(entity.getId() !=0) {
				
			}else {
				entityRepository.save(entity);
				Anticipo an= entityRepository.findTop1ByOrderByIdDesc();
				if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==true) {
					System.out.println("GASTOS POR CAJA CHICA "+cajaChica);
					System.out.println("ENTROO PARA CONFIRMAR");
					CajaChica cC = new CajaChica();
					cC=cajaChicaRepository.getCajaChicaPorIdCaja(cajaChica);
					if(cC==null) {
						System.out.println("entrooo null caja chiac");
						return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE CAJA CHICA A SU NOMBRE!"), HttpStatus.CONFLICT);
					}else {
						System.out.println("entrooo ACTUVO CAJA CHICA");
						System.out.println(cC.getMonto()+ "-"+entity.getMonto());
						if(cC.getMonto() < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
							System.out.println("entrooo monto superaod efe");
							return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if(cC.getMonto()< entity.getMonto()&& entity.getTipoOperacion().getId()==2){
							System.out.println("entrooo monto superaod che");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if(cC.getMonto()< entity.getMonto() && entity.getTipoOperacion().getId()==3){
							System.out.println("entrooo monto superaod tarj");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else{
							Concepto cc = new Concepto();
							cc = conceptoRepository.findById(21).get();
							TransferenciaAnticipo tgasto= new TransferenciaAnticipo();
							tgasto.getCajaChica().setId(cC.getId());
							tgasto.getFuncionario().setId(entity.getFuncionarioRegistro().getId());
							tgasto.getAnticipo().setId(an.getId());
							tgasto.setReferencia(cc.getDescripcion() + " REF.: " + an.getId());
							tgasto.setFecha(new Date());
							if (entity.getTipoOperacion().getId()==1) {
								tgasto.setMonto(entity.getMonto());
								cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(cC.getId(), tgasto.getMonto(), 0.0, 0.0);

							}	
							if (entity.getTipoOperacion().getId()==2) {
								tgasto.setMontoCheque(entity.getMonto());
								cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(cC.getId(), 0.0,tgasto.getMonto(), 0.0);

							}	
							if (entity.getTipoOperacion().getId()==3) {
								tgasto.setMontoTarjeta(entity.getMonto());
								cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(cC.getId(), 0.0, 0.0, tgasto.getMonto());
							}
							transferenciaAnticipoRepository.save(tgasto);
							TransferenciaAnticipo tRetorno= transferenciaAnticipoRepository.findTop1ByOrderByIdDesc();
							AnticipoReferenciaCajaChica rfe= new AnticipoReferenciaCajaChica();
							rfe.getTransferenciaAnticipo().setId(tRetorno.getId());
							rfe.getAnticipo().setId(an.getId());
							anticipoReferenciaCajaChicaRepository.save(rfe);
						}
					}
					entity.getConcepto().setId(9);
					entityRepository.save(entity);
					return new ResponseEntity<>(HttpStatus.CREATED);
				}
				if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==false) {
					System.out.println("GASTOS POR APERTURA DE CAJA "+ +apertura);
					AperturaCaja cC = new AperturaCaja();
					cC=aperturaCajaRepository.getAperturaCajaPorIdCaja(apertura);
					if(cC==null) {
						System.out.println("entrooo null caja chiac");
						return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
					}else {
						if((cC.getSaldoActual()) < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
							System.out.println("entrooo monto superaod efe");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((cC.getSaldoActualCheque()) < entity.getMonto()&& entity.getTipoOperacion().getId()==2){
							System.out.println("entrooo monto superaod che");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((cC.getSaldoActualTarjeta())< entity.getMonto() && entity.getTipoOperacion().getId()==3){
							System.out.println("entrooo monto superaod tarj");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else{
							
							OperacionCaja op= new OperacionCaja();
							op.getAperturaCaja().setId(cC.getId());
							op.getConcepto().setId(21);
							op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
							op.setTipo("SALIDA");
							op.setEfectivo(0.0);
							op.setVuelto(0.0);
							op.setMonto(entity.getMonto());
							
							Concepto c = new Concepto();
							c = conceptoRepository.findById(21).get();
							op.setMotivo(c.getDescripcion() + " REF.: " + an.getId());
							operacionCajaRepository.save(op);
							if (op.getTipoOperacion().getId() == 1) {
								aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(op.getAperturaCaja().getId(), entity.getMonto());
							}
							if (op.getTipoOperacion().getId() == 2) {
								aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(op.getAperturaCaja().getId(), entity.getMonto());
							}
							if (op.getTipoOperacion().getId() == 3) {
								aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(op.getAperturaCaja().getId(), entity.getMonto());

							}
							OperacionCaja opA= new  OperacionCaja();
							opA= operacionCajaRepository.findTop1ByOrderByIdDesc();
							AnticipoReferenciaOperacionCaja rfe= new AnticipoReferenciaOperacionCaja();
							rfe.getOperacionCaja().setId(opA.getId());
							rfe.getAnticipo().setId(an.getId());
							anticipoReferenciaOperacionCajaRepository.save(rfe);
							
						}
					}
					entity.getConcepto().setId(9);
					entityRepository.save(entity);
					return new ResponseEntity<>(HttpStatus.CREATED);
				}
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/buscarAnticipoActivo/{id}")
	public List<Anticipo> buscarAnticipoActivoPorFuncionarioId(@PathVariable int id){
		return entityRepository.buscarAnticipoActivoPorFuncionario(id);
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<Anticipo> getPorFiltro(@RequestBody String descripcion){
		return cargarListado(entityRepository.consultarTodoPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));
	}
	private List<Anticipo>cargarListado(List<Object[]> lista){
		List<Anticipo> listaRetrono= new  ArrayList<Anticipo>();
		for (Object[] ob: lista) {
			Anticipo a = new Anticipo();
			a.setId(Integer.parseInt(ob[0].toString()));
			a.getFuncionarioRegistro().getPersona().setNombre(ob[1].toString() + " "+ob[2].toString());
			a.getFuncionarioAutorizado().getPersona().setNombre(ob[3].toString() + " "+ob[4].toString());
			a.getFuncionarioEncargado().getPersona().setNombre(ob[5].toString() + " "+ob[6].toString());
			a.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[7].toString()));
			a.setMonto(Double.parseDouble(ob[8].toString()));
			a.setEstado(Boolean.parseBoolean(ob[9].toString()));
			a.getFuncionarioRegistro().setId(Integer.parseInt(ob[10].toString()));
			a.getFuncionarioAutorizado().setId(Integer.parseInt(ob[11].toString()));
			a.getFuncionarioEncargado().setId(Integer.parseInt(ob[12].toString()));
			a.getTipoOperacion().setId(Integer.parseInt(ob[13].toString()));
			a.setTipo(ob[14].toString());
			a.setDisponibilidad(ob[15].toString());
			
			listaRetrono.add(a);
		}
		return listaRetrono;
	}
	@RequestMapping(method = RequestMethod.POST, value = "/anticipo")
	public void confirmar(@RequestBody Anticipo entity){
		System.out.println("en5troooo confirmarafasdfasf");
		entityRepository.save(entity);
		
		
	}
	@RequestMapping(method = RequestMethod.GET, value = "/pruebaHql/{id}")
	public AnticipoReferenciaOperacionCaja prueba(@PathVariable int id){
		
		AnticipoReferenciaOperacionCaja c = new AnticipoReferenciaOperacionCaja();
		c=anticipoReferenciaOperacionCajaRepository.getReferenciaOperacionCajaPorIdAnticipo(id);
		System.out.println(""+c.getOperacionCaja().getId());
		return c;
		
	}
	
	
	
	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Anticipo pre= new Anticipo(); 
		pre=entityRepository.getOne(id);
		List<AnticipoReferenciaCajaChica> listadoCajaChica= new ArrayList<AnticipoReferenciaCajaChica>();
		List<AnticipoReferenciaOperacionCaja> listadoOperacionCaja= new ArrayList<AnticipoReferenciaOperacionCaja>();

		
		if(pre.getTipo().equals("T-C")) {
			AnticipoReferenciaCajaChica c= new AnticipoReferenciaCajaChica();
			c=anticipoReferenciaCajaChicaRepository.getReferenciaCajaChicaPorIdAnticipo(pre.getId());
			listadoCajaChica.add(c);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
	
			report = new Reporte();
			report.reportPDFDescarga(listadoCajaChica, map, "ReporteAnticipoFuncionarioTipoCajaChica", response);

			
		}
		if(pre.getTipo().equals("T-A")) {
			AnticipoReferenciaOperacionCaja c = new AnticipoReferenciaOperacionCaja();
			c=anticipoReferenciaOperacionCajaRepository.getReferenciaOperacionCajaPorIdAnticipo(pre.getId());
			listadoOperacionCaja.add(c);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
	
			report = new Reporte();
			report.reportPDFDescarga(listadoOperacionCaja, map, "ReporteAnticipoFuncionarioTipoOperacionCaja", response);

		}
		//listado.add(pre);
		try {
		
							//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value="/anularAnticipo")
	public ResponseEntity<?> anularAnticipo(@RequestBody AnulacionesAnticipo entity, OAuth2Authentication authentication){

		try {
			entityRepository.anularAnticipo(entity.getAnticipo().getId(), "ANULADO");
			Anticipo v = entityRepository.findById(entity.getAnticipo().getId()).get();
			if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==false) {
				Usuario usuario = usuarioService.findByUsername(authentication.getName());
				Funcionario funcionario= funcionarioRepository.getIdFuncionario(usuario.getFuncionario().getId());
				AperturaCaja aper= aperturaCajaRepository.getAperturaActivoCajaIdFuncionario(funcionario.getId());
				
				entity.setFecha(new Date());
				entity.getFuncionario().setId(usuario.getFuncionario().getId());
				entity.setMotivo("ANULACIÓN ANTICIPO REF.:  "+v.getId());
				entity.getAnticipo().setId(v.getId());
				entity.getConcepto().setId(22);
				anulacionAnticipoRepository.save(entity);
				AnulacionesAnticipo anulacionRetorno= anulacionAnticipoRepository.getAnulacionUlt();
				OperacionCaja op  = new OperacionCaja();
				op.getAperturaCaja().setId(aper.getId());
				op.getConcepto().setId(22);
				op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
				op.setTipo("ENTRADA");
				op.setEfectivo(0.0);
				op.setVuelto(0.0);
				op.setMonto(v.getMonto());
				
				Concepto c = new Concepto();
				//CONCEPTO ID=22, ANULACIONES ANTICIPO
				c = conceptoRepository.findById(22).get();
				op.setMotivo(c.getDescripcion() + " REF.: " + anulacionRetorno.getId());
				operacionCajaRepository.save(op);
				if (op.getTipoOperacion().getId() == 1) {
					aperturaCajaRepository.findByActualizarAperturaSaldo(aper.getId(), v.getMonto());
				}
				if (op.getTipoOperacion().getId() == 2) {
					aperturaCajaRepository.findByActualizarAperturaSaldoCheque(aper.getId(), v.getMonto());
				}
				if (op.getTipoOperacion().getId() == 3) {
					aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(aper.getId(), v.getMonto());
				}
				new ResponseEntity<>(HttpStatus.CREATED);
			}
			if(v.getTipo().equals("T-C")) {
				 new ResponseEntity<>(new CustomerErrorType("ESTE OPERACIÒN NO TIENE SOPORTE PARA ANULACIÒN ANTICIPO TIPO OPERACIÒN T-A"), HttpStatus.CONFLICT);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
}
