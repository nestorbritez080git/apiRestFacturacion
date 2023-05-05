package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;
import com.bisontecfacturacion.security.model.GastoConsumicionesDetalle;
import com.bisontecfacturacion.security.model.TransferenciaGastos;
import com.bisontecfacturacion.security.repository.CajaChicaRepository;
import com.bisontecfacturacion.security.repository.CajaMayorRepository;
import com.bisontecfacturacion.security.repository.GastoConsumicionesCabeceraRepository;
import com.bisontecfacturacion.security.repository.GastoConsumicionesDetalleRepository;
import com.bisontecfacturacion.security.repository.TransferenciaGastoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@Transactional
@RequestMapping("gastoConsumiciones")
public class GastoConsumicionesCabeceraController {
	@Autowired
	private GastoConsumicionesCabeceraRepository entityRepository;

	@Autowired
	private GastoConsumicionesDetalleRepository detalleRepository;
	
	@Autowired
	private CajaChicaRepository cajaChicaRepository;
	@Autowired
	private CajaMayorRepository cajaMayorRepository;
	
	@Autowired
	private TransferenciaGastoRepository transferenciaGastoRepository; 

	@RequestMapping(method=RequestMethod.GET, value="/actualizarTotal/{monto}/{id}")
	public void actualizarTotal(@PathVariable double monto, @PathVariable int id){
		entityRepository.findByActualizaMontoCabecera(monto, id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/ventas")
	public List<GastoConsumicionesCabecera> get(){
		return entityRepository.findAll();
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<GastoConsumicionesCabecera> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<GastoConsumicionesCabecera> objeto=entityRepository.getGastoConsumicionDelDia(ano, mes, dia);
		List<GastoConsumicionesCabecera> venta=new ArrayList<>();
		for(GastoConsumicionesCabecera ob:objeto){
			GastoConsumicionesCabecera ventas=new GastoConsumicionesCabecera();
			ventas.setId(ob.getId());
			ventas.getFuncionarioCargo().getPersona().setNombre(ob.getFuncionarioCargo().getPersona().getNombre()+" "+ob.getFuncionarioCargo().getPersona().getApellido());
			ventas.getFuncionarioRegistro().getPersona().setNombre(ob.getFuncionarioRegistro().getPersona().getNombre()+" "+ob.getFuncionarioRegistro().getPersona().getApellido());
			ventas.setTotal(ob.getTotal());
			ventas.setFecha(ob.getFecha());
			ventas.setHora(ob.getHora());	
			ventas.setEstado(ob.getEstado());
			venta.add(ventas);
		}
		return venta;
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody GastoConsumicionesCabecera entity){
		//Usuario usuario = usuarioService.findByUsername(authentication.getName());
		System.out.println(entity.getEstado());
		try {
			
			if(entity.getFuncionarioCargo().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO GASTO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getTotal() <=0 || entity.getTotal()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DEL GASTO DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			} else if(entity.getGastoConsumicionesDetalles().size()<=0){
				return new ResponseEntity<>(new CustomerErrorType("DEBES CARGAR POR LO MENO UN DETALLE!"), HttpStatus.CONFLICT);
			}else if(entity.getTipoOperacion().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR TIPO OPERACION A EFECTUAR!"), HttpStatus.CONFLICT);
			}else if (entity.getCajaChica().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA CAJA CHICA PARA EFECTUAR GASTO"), HttpStatus.CONFLICT);
			}else if (entity.getConcepto().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN CONCEPTO PARA EFECTUAR GASTO"), HttpStatus.CONFLICT);
			}else{
				for(int ind=0; ind < entity.getGastoConsumicionesDetalles().size(); ind++) {
					GastoConsumicionesDetalle pro = entity.getGastoConsumicionesDetalles().get(ind);
					if(pro.getDescripcion() == null || pro.getDescripcion().equals("")) {
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE GASTO ITEM N°: "+(ind+1)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getMonto() <=0 || pro.getMonto()==null){
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO DEL DETALLE GASTO ITEM N°: "+(ind+1)+" DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
					}else if(pro.getConsumiciones().getId() == 0  ){
						return new ResponseEntity<>(new CustomerErrorType("LA CONSUMICIÓN DETALLE GASTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}
				if(entity.getId() !=0) {
					entity.setFecha(new Date());
					entity.setHora(hora());
					
					int idVent=entity.getId();
					if(entity.getGastoConsumicionesDetalles().size()>0){
						for(GastoConsumicionesDetalle det: entity.getGastoConsumicionesDetalles()) {
							det.getGastoConsumicionesCabecera().setId(idVent);
							detalleRepository.save(det);
						}
					}
					if(entity.getEstado().equals("SIN CONFIRMAR")) {
						System.out.println("eNTROO SNI CONFI");
					}else if(entity.getEstado().equals("CONFIRMADO")) {
						System.out.println("ENTROO PARA CFIRMAR");
						CajaChica cC = new CajaChica();
						cC=cajaChicaRepository.getCajaChicaPorIdCaja(entity.getCajaChica().getId());
						if(cC==null) {
							System.out.println("entrooo null caja chiac");
							return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE CAJA CHICA A SU NOMBRE!"), HttpStatus.CONFLICT);
						}else {
							if(cC.getMonto() < entity.getTotal() && entity.getTipoOperacion().getId()==1) {
								System.out.println("entrooo monto superaod efe");
								return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if(cC.getMonto()< entity.getTotal()&& entity.getTipoOperacion().getId()==2){
								System.out.println("entrooo monto superaod che");

								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if(cC.getMonto()< entity.getTotal() && entity.getTipoOperacion().getId()==3){
								System.out.println("entrooo monto superaod tarj");

								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else{
								TransferenciaGastos tgasto= new TransferenciaGastos();
								tgasto.getCajaChica().setId(cC.getId());
								tgasto.getFuncionario().setId(entity.getFuncionarioRegistro().getId());
								tgasto.getGastoConsumicionesCabecera().setId(entity.getId());
								tgasto.setReferencia(entity.getTipoOperacion().getDescripcion());
								tgasto.setFecha(new Date());
								if (entity.getTipoOperacion().getId()==1) {
									tgasto.setMonto(entity.getTotal());
									
								}	
								if (entity.getTipoOperacion().getId()==2) {
									tgasto.setMontoCheque(entity.getTotal());
								}	
								if (entity.getTipoOperacion().getId()==3) {
									tgasto.setMontoTarjeta(entity.getTotal());
								}
								cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(cC.getId(), tgasto.getMonto(), tgasto.getMontoCheque(), tgasto.getMontoTarjeta());
								transferenciaGastoRepository.save(tgasto);
							}
						}
					}
					entityRepository.save(entity);
				}else {
					
					if(entity.getEstado().equals("SIN CONFIRMAR")) {
						System.out.println("eNTROO SNI CONFI");
						entity.setFecha(new Date());
						entity.setHora(hora());
						entityRepository.save(entity);
						GastoConsumicionesCabecera id = entityRepository.getUltimaGastoConsumicionCab();
						int idVent=0;
						if(id == null){idVent=1;}else{idVent=id.getId();}
						if(entity.getGastoConsumicionesDetalles().size()>0){
							for(GastoConsumicionesDetalle det: entity.getGastoConsumicionesDetalles()) {
								det.getGastoConsumicionesCabecera().setId(idVent);
								detalleRepository.save(det);
							}
						}
					}else if(entity.getEstado().equals("CONFIRMADO")) {
						CajaChica cC = new CajaChica();
						cC=cajaChicaRepository.getCajaChicaPorIdCaja(entity.getCajaChica().getId());
						if(cC==null) {
							return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO POSEE CAJA CHICA A SU NOMBRE!"), HttpStatus.CONFLICT);
						}else {
							if(cC.getMonto() < entity.getTotal() && entity.getTipoOperacion().getId()==1) {
								return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if(cC.getMonto()< entity.getTotal()&& entity.getTipoOperacion().getId()==2){
								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else if(cC.getMonto()< entity.getTotal() && entity.getTipoOperacion().getId()==3){
								return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
							}else{
								entity.setFecha(new Date());
								entity.setHora(hora());
								entityRepository.save(entity);
								GastoConsumicionesCabecera id = entityRepository.getUltimaGastoConsumicionCab();
								int idVent=0;
								if(id == null){idVent=1;}else{idVent=id.getId();}
								if(entity.getGastoConsumicionesDetalles().size()>0){
									for(GastoConsumicionesDetalle det: entity.getGastoConsumicionesDetalles()) {
										det.getGastoConsumicionesCabecera().setId(idVent);
										detalleRepository.save(det);
									}
								}
								TransferenciaGastos tgasto= new TransferenciaGastos();
								tgasto.getCajaChica().setId(cC.getId());
								tgasto.getFuncionario().setId(entity.getFuncionarioRegistro().getId());
								tgasto.getGastoConsumicionesCabecera().setId(idVent);
								tgasto.setReferencia(entity.getTipoOperacion().getDescripcion());
								tgasto.setFecha(new Date());
								if (entity.getTipoOperacion().getId()==1) {
									tgasto.setMonto(entity.getTotal());
									
								}	
								if (entity.getTipoOperacion().getId()==2) {
									tgasto.setMontoCheque(entity.getTotal());
								}	
								if (entity.getTipoOperacion().getId()==3) {
									tgasto.setMontoTarjeta(entity.getTotal());
								}
								cajaMayorRepository.findByActualizaTransferenciaCajaChicaNegativo(cC.getId(), tgasto.getMonto(), tgasto.getMontoCheque(), tgasto.getMontoTarjeta());
								transferenciaGastoRepository.save(tgasto);
								
								
							}
						}
					}
					
				}	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	@RequestMapping(method=RequestMethod.GET, value="/gastoId/{id}")
	public GastoConsumicionesCabecera getGastoCabeceraId(@PathVariable int id){

		GastoConsumicionesCabecera v=entityRepository.findById(id).orElse(null);

		GastoConsumicionesCabecera venta=new GastoConsumicionesCabecera();
		venta.setEstado(v.getEstado());
		venta.setId(v.getId());
		venta.setTotal(v.getTotal());
		venta.getFuncionarioCargo().setId(v.getFuncionarioCargo().getId());
		venta.getFuncionarioCargo().getPersona().setNombre(v.getFuncionarioCargo().getPersona().getNombre());
		venta.getFuncionarioCargo().getPersona().setApellido(v.getFuncionarioCargo().getPersona().getApellido());
		venta.getCajaChica().setId(v.getCajaChica().getId());
		venta.getConcepto().setId(v.getConcepto().getId());
		venta.getTipoOperacion().setId(v.getTipoOperacion().getId());
		venta.setFecha(v.getFecha());
		venta.setHora(v.getHora());
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/gastoId/all/{id}")
	public GastoConsumicionesCabecera getGastoCabeceraIdTodo(@PathVariable int id){

		GastoConsumicionesCabecera v=entityRepository.findById(id).orElse(null);

		GastoConsumicionesCabecera venta=new GastoConsumicionesCabecera();
		venta.setEstado(v.getEstado());
		venta.setId(v.getId());
		venta.setTotal(v.getTotal());
		venta.getFuncionarioCargo().setId(v.getFuncionarioCargo().getId());
		venta.getFuncionarioCargo().getPersona().setNombre(v.getFuncionarioCargo().getPersona().getNombre());
		venta.getFuncionarioCargo().getPersona().setApellido(v.getFuncionarioCargo().getPersona().getApellido());
		venta.getTipoOperacion().setId(v.getTipoOperacion().getId());
		venta.setFecha(v.getFecha());
		venta.setHora(v.getHora());
		venta.getConcepto().setId(v.getConcepto().getId());
		venta.getCajaChica().setId(v.getCajaChica().getId());
		venta.setGastoConsumicionesDetalles(this.detalleGasto(id));
		System.out.println("listaod:   :::  : "+venta.getGastoConsumicionesDetalles().size());
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/detalleGasto/{id}")
	public List<GastoConsumicionesDetalle> getGastoDetallePorIdCabecera(@PathVariable int id){
		return detalleGasto(id);
	}
	public List<GastoConsumicionesDetalle> detalleGasto(int idCab) {
		List<Object[]> objeto=detalleRepository.lista(idCab);
		List<GastoConsumicionesDetalle> detalle=new ArrayList<>();
		for(Object[] ob:objeto){
			GastoConsumicionesDetalle det=new GastoConsumicionesDetalle();
			det.setId(Integer.parseInt(ob[0].toString()));
			det.getGastoConsumicionesCabecera().setId(Integer.parseInt(ob[1].toString()));
			det.getConsumiciones().setId(Integer.parseInt(ob[2].toString()));
			det.setDescripcion(ob[3].toString());
			det.setMonto(Double.parseDouble(ob[4].toString()));
			det.setObservacion(ob[5].toString());
			det.setComprobante(ob[6].toString());
			detalle.add(det);
		}
		return detalle;
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void deleteDetalle(@PathVariable int id){
		this.detalleRepository.deleteById(id);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/eliminarDetalle")
	public void deleteDetalleLista(@RequestBody List<GastoConsumicionesDetalle> lista){
		for(GastoConsumicionesDetalle ob: lista) {
			this.detalleRepository.deleteById(ob.getId());
		}
	}
}
