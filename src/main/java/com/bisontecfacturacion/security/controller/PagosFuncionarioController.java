package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.AnticipoReferenciaCajaChica;
import com.bisontecfacturacion.security.model.AnticipoReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.AnulacionesAnticipo;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.PagosFuncionario;
import com.bisontecfacturacion.security.model.PagosFuncionarioDetalle;
import com.bisontecfacturacion.security.model.PagosFuncionarioReferenciaCajaChica;
import com.bisontecfacturacion.security.model.PagosFuncionarioReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AnticipoReferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.AnticipoReferenciaOperacionCajaRepository;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PagosFuncionarioDetalleRepository;
import com.bisontecfacturacion.security.repository.PagosFuncionarioReferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.PagosFuncionarioReferenciaOperacionCajaRepository;
import com.bisontecfacturacion.security.repository.PagosFuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("pagosFuncionario")
public class PagosFuncionarioController {
	@Autowired
	private PagosFuncionarioRepository entityRepository;
	

	
	@Autowired
	private AnticipoRepository anticipoRepository;
	
	
	@Autowired
	private PagosFuncionarioDetalleRepository detalleRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;
	
	@Autowired
	private PagosFuncionarioReferenciaOperacionCajaRepository pagosFuncionarioReferenciaOperacionCajaRepository;
	
	@Autowired
	private PagosFuncionarioReferenciaCajaChicaRepository  pagosFuncionarioReferenciaCajaChicaRepository;
	
	private Reporte report;
	
	
	private List<PagosFuncionario> cargarListadoPagos(List<PagosFuncionario> lista){
		List<PagosFuncionario> listaRetrono= new  ArrayList<PagosFuncionario>();
		for (PagosFuncionario ob: lista) {
			PagosFuncionario a = new PagosFuncionario();
			a.setId(ob.getId());
			a.setFecha(ob.getFecha());
			a.setImporteBruto(ob.getImporteBruto());
			a.setDescuentoLiquidacion(ob.getDescuentoLiquidacion());
			a.setImporteNeto(ob.getImporteNeto());
			a.setEstado(ob.getEstado());
			a.setTipoPago(ob.getTipoPago());
			a.setFuncionarioPago(ob.getFuncionarioPago());
			a.setFuncionarioRegistro(ob.getFuncionarioRegistro());
			listaRetrono.add(a);
		}
		return listaRetrono;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<PagosFuncionario> getAll(){
		return cargarListadoPagos(entityRepository.consultarTodo());
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar")
	public List<PagosFuncionario> getPorFiltro(@RequestBody String descripcion){
		return cargarListadoPagos(entityRepository.consultarTodoPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));
	}
	/*
	@RequestMapping(method=RequestMethod.POST, value="/buscar/{tipo}")
	public List<PagosFuncionario> getPorFiltro(@RequestBody String descripcion, @PathVariable int tipo){
		List<Anticipo> listaRetrono= new  ArrayList<Anticipo>();
		List<Object[]> listObject= new  ArrayList<Object[]>();
		if(tipo==1) {
			listObject= entityRepository.consultarTodoPorFiltro("");
		}
		if(tipo==2) {}
		if(tipo==3) {}
		
		return cargarListado(listObject);
	}


	*/

	@Transactional
	@RequestMapping(method=RequestMethod.GET, value="/anularPagosFuncionario/{idPagos}")
	public ResponseEntity<?> anularPagosFuncionario(OAuth2Authentication authentication, @PathVariable int idPagos){
		try {
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			Funcionario funcionario= funcionarioRepository.getIdFuncionario(usuario.getFuncionario().getId());
			AperturaCaja aper= aperturaCajaRepository.getAperturaActivoCajaIdFuncionario(funcionario.getId());
			PagosFuncionario v = entityRepository.findById(idPagos).get();
			if(aper==null){
				 return new ResponseEntity<>(new CustomerErrorType("ESTE FUNCIONARIO NO POSEE APERTURA DE CAJA PARA PODER PROCESAR ENTRADA DE CAPITAL"), HttpStatus.CONFLICT);	
			}else {
				System.out.println("REGSITRO ID F= "+v.getFuncionarioRegistro().getId()+" apertura id f = "+aper.getFuncionario().getId());
				if(aper.getFuncionario().getId()==v.getFuncionarioRegistro().getId()) {

					if(v.getTipo().equals("T-A")) {
						//APERTURA CAJA ANULACION PAGOS FUNCIONARIO
						PagosFuncionarioReferenciaOperacionCaja rfOperacionCaja=pagosFuncionarioReferenciaOperacionCajaRepository.getPagosFuncionarioReferenciaOperacionCajaPorIdPagos(idPagos);
						if(v.getImporteNeto()>0) {
							OperacionCaja op  = new OperacionCaja();
							op.getAperturaCaja().setId(aper.getId());
							op.getConcepto().setId(26);//concepto de anulacion de pagos funcionario
							op.getTipoOperacion().setId(v.getTipoOperacion().getId());
							op.setTipo("ENTRADA");
							op.setEfectivo(0.0);
							op.setVuelto(0.0);
							op.setMonto(v.getImporteNeto());
							
							Concepto c = new Concepto();
							//CONCEPTO ID=26, ANULACIONES PAGOS FUNCIONARIO
							c = conceptoRepository.findById(26).get();
							op.setMotivo(c.getDescripcion() + " REF.: " + v.getId());
							operacionCajaRepository.save(op);
							if (op.getTipoOperacion().getId() == 1) {
								aperturaCajaRepository.findByActualizarAperturaSaldo(aper.getId(), v.getImporteNeto());
							}
							if (op.getTipoOperacion().getId() == 2) {
								aperturaCajaRepository.findByActualizarAperturaSaldoCheque(aper.getId(), v.getImporteNeto());
							}
							if (op.getTipoOperacion().getId() == 3) {
								aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(aper.getId(), v.getImporteNeto());
							}	
						}else {
							System.out.println("NO SE REGISTRO OPERACION DE CAJA POR ESO NO PUEDE GENERAR MOMOVIMIENTO DE ENTRADA");
						}
						
						entityRepository.anularPagosFuncionario(idPagos, "ANULADO");
						return	new ResponseEntity<>(HttpStatus.OK);
					}
					if(v.getTipo().equals("T-C")) {
						return	 new ResponseEntity<>(new CustomerErrorType("ESTE OPERACIÒN NO TIENE SOPORTE PARA ANULACIÒN TIPO OPERACIÒN T-C"), HttpStatus.CONFLICT);
					}
				}else {
					return new ResponseEntity<>(new CustomerErrorType("NO PUEDE ANULAR PAGOS QUE OTROS FUNCIONARIO HAYA REALIZADOS"), HttpStatus.CONFLICT);	
				}
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody PagosFuncionario entity){
			try {
				//entity.getFuncionarioAutorizado().setId(0);
				Integer apertura=0, cajaChica=0;
				if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==true) {
					/*
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
					}*/
				}
				if(Boolean.parseBoolean(entity.getConcepto().getDescripcion())==false) {
					
					apertura= entity.getConcepto().getId();
					entity.setTipo("T-A");
					System.out.println("PAGOS POR APERTURA DE CAJA "+ +apertura);
					AperturaCaja XX = new AperturaCaja();
					XX=aperturaCajaRepository.getAperturaCajaPorIdCaja(apertura);
					if(XX==null) {
						System.out.println("entrooo null caja chiac");
						return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE UNA APERTURA CAJA A SU NOMBRE!"), HttpStatus.CONFLICT);
					}else {
						if((XX.getSaldoActual()) < entity.getImporteNeto() && entity.getTipoOperacion().getId()==1) {
							System.out.println("entrooo monto superaod efe");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN EFECTIVO DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((XX.getSaldoActualCheque()) < entity.getImporteNeto()&& entity.getTipoOperacion().getId()==2){
							System.out.println("entrooo monto superaod che");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else if((XX.getSaldoActualTarjeta())< entity.getImporteNeto() && entity.getTipoOperacion().getId()==3){
							System.out.println("entrooo monto superaod tarj");
							return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
						}else {
						
							
							
							if(entity.getFuncionarioRegistro().getId()==0) {
								return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
							}else if(entity.getFuncionarioRegistro().getId()==0) {
								return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO AUTORIZADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
							}else if(entity.getFuncionarioPago().getId()==0) {
								return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO ENCARGADO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
							}else if(entity.getTipoPago().getId()== 0) {
								return new ResponseEntity<>(new CustomerErrorType("EL TIPO DE PAGO ES OBLIGATORIO.!"), HttpStatus.CONFLICT);
							}else if(entity.getImporteBruto()<=0) {
								return new ResponseEntity<>(new CustomerErrorType("EL IMPORTE BRUTO DEBE SER MAYOR A CERO.!"), HttpStatus.CONFLICT);
							}else {
								entity.getConcepto().setId(25);//CONCEPTO DE PAGOS FUNCIONARIO
								entityRepository.save(entity);
								PagosFuncionario an= entityRepository.findTop1ByOrderByIdDesc();
								if(entity.getImporteNeto()>0) {
									OperacionCaja op= new OperacionCaja();
									op.getAperturaCaja().setId(XX.getId());
									op.getConcepto().setId(21);
									op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
									op.setTipo("SALIDA");
									op.setEfectivo(0.0);
									op.setVuelto(0.0);
									op.setMonto(entity.getImporteNeto());
									Concepto c = new Concepto();
									c = conceptoRepository.findById(25).get();
									op.setMotivo(c.getDescripcion() + " REF.: "+an.getId() );
									operacionCajaRepository.save(op);
									if (op.getTipoOperacion().getId() == 1) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(op.getAperturaCaja().getId(), entity.getImporteNeto());
									}
									if (op.getTipoOperacion().getId() == 2) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(op.getAperturaCaja().getId(), entity.getImporteNeto());
									}
									if (op.getTipoOperacion().getId() == 3) {
										aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(op.getAperturaCaja().getId(), entity.getImporteNeto());
									}
									OperacionCaja opA= new  OperacionCaja();
									opA= operacionCajaRepository.findTop1ByOrderByIdDesc();
									PagosFuncionarioReferenciaOperacionCaja rfe= new PagosFuncionarioReferenciaOperacionCaja();
									rfe.getOperacionCaja().setId(opA.getId());
									rfe.getPagosFuncionario().setId(an.getId());
									pagosFuncionarioReferenciaOperacionCajaRepository.save(rfe);	
								}
								
								
								
								if(entity.getPagosFuncionarioDetalles().size()>0) {
									for (PagosFuncionarioDetalle ob : entity.getPagosFuncionarioDetalles()) {
										 ob.getPagosFuncionario().setId(an.getId());

									        // Solo proceder si el monto del detalle es mayor a cero
									        if (ob.getMonto() > 0) {
									            Anticipo anticipo = ob.getAnticipo();
									            double totalLiquidado = anticipo.getMontoLiquidado() + ob.getMonto();

									            if (totalLiquidado >= anticipo.getMonto()) {
									                // Se liquida completamente el anticipo
									                anticipoRepository.liquidarEstadoAnticipo(anticipo.getId(), true);
									            } else {
									                // Se actualiza el monto liquidado parcial
									                anticipoRepository.updateMontoLiquidado(anticipo.getId(), ob.getMonto());
									            }
									            // Guardar el detalle
									            detalleRepository.save(ob);
									}
								}
								
								
							}
							
						}
					}
					
					
				}
				/*
				
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
					
				}*/
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
			}
			return new ResponseEntity<>(HttpStatus.CREATED);
			
}
				
		
@RequestMapping(method = RequestMethod.GET, value="/reporteSalarioFuncionario/{id}")
public  ResponseEntity<?> getReporteSalarioFuncionario(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id) throws IOException{
	
	/*
	List<PagosFuncionario> lis =listar(entityRepository.getSalarioFuncionarioDetallePorIdLista(id));
	
	if(lis.size()>0) {
		
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
		report.reportPDFDescarga(lis, map, "ReporteSalarioFuncionario", response);

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
*/
	return null;


}


@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
	Usuario usuario = usuarioService.findByUsername(authentication.getName());
	Org org = orgRepository.findById(1).get();
	PagosFuncionario pre= new PagosFuncionario(); 
	pre=entityRepository.getOne(id);
	List<PagosFuncionarioReferenciaCajaChica> listadoCajaChica= new ArrayList<PagosFuncionarioReferenciaCajaChica>();
	List<PagosFuncionarioReferenciaOperacionCaja> listadoOperacionCaja= new ArrayList<PagosFuncionarioReferenciaOperacionCaja>();

	
	if(pre.getTipo().equals("T-C")) {
		PagosFuncionarioReferenciaCajaChica c= new PagosFuncionarioReferenciaCajaChica();
		c=pagosFuncionarioReferenciaCajaChicaRepository.getPagosFuncionarioReferenciaCajaChicaPorIdPagos(pre.getId());
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
		PagosFuncionarioReferenciaOperacionCaja c = new PagosFuncionarioReferenciaOperacionCaja();
		c=pagosFuncionarioReferenciaOperacionCajaRepository.getPagosFuncionarioReferenciaOperacionCajaPorIdPagos(pre.getId());
		if(c==null) {
			c= new PagosFuncionarioReferenciaOperacionCaja();
			c.setPagosFuncionario(pre);
		}else {
			c.setPagosFuncionario(pre);
		}
		
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
		report.reportPDFDescarga(listadoOperacionCaja, map, "ReportePagosFcuncionarioPorOperacionCajaPdf", response);

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

	
	
	
}
