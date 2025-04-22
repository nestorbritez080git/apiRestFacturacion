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

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.EntradaSalidaCaja;
import com.bisontecfacturacion.security.model.EntregaProduccion;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.EntradaSalidaCajaRepository;
import com.bisontecfacturacion.security.repository.EntregaProduccionRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("entradaSalidaCaja")
public class EntradaSalidaCajaController {
	@Autowired
	private EntradaSalidaCajaRepository entityRepository;
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	@Autowired
	private AperturaCajaRepository aperturaRepository;
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;

	
	
	@Autowired
	private ConceptoRepository conceptoRepository;
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody EntradaSalidaCaja entity){
		 
		//uso entit.getOperacionCaja.id para recibir numero de apertura para poder consultar por id Apertura
		AperturaCaja XX = new AperturaCaja();
		XX=aperturaCajaRepository.getAperturaCajaPorIdCaja(entity.getOperacionCaja().getConcepto().getId());
		if(entity.getFuncionario().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else if(entity.getTipoOperacion().getId() == 0) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN TIPO DE OPERACIÓN!"), HttpStatus.CONFLICT);
		} else if(entity.getTipoMovimiento().getId()==0) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN TIPO DE MOVIMIENTO PARA LA OPERACIÒN!"), HttpStatus.CONFLICT);
		} else if(entity.getMonto()<= 0) {
			return new ResponseEntity<>(new CustomerErrorType("EL MONTO DE LA OPERCIÓN DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
		} else if(entity.getMotivo().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("EL MOTIVO DE LA OPERACIÓN NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		} else	if(entity.getTipoMovimiento().getId()==2 && XX.getSaldoActual() < entity.getMonto() && entity.getTipoOperacion().getId()==1) {
			System.out.println("entrooo monto superaod efe");
			return new ResponseEntity<>(new CustomerErrorType("EL EFECTIVO DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
		}else if(entity.getTipoMovimiento().getId()==2 && XX.getSaldoActualCheque() < entity.getMonto()&& entity.getTipoOperacion().getId()==2){
			System.out.println("entrooo monto superaod che");
			return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN CHEQUE DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
		}else if(entity.getTipoMovimiento().getId()==2 && XX.getSaldoInicialTarjeta() < entity.getMonto() && entity.getTipoOperacion().getId()==3){
			System.out.println("entrooo monto superaod tarj");
			return new ResponseEntity<>(new CustomerErrorType("EL MONTO EN TARJETA DISPONIBLE EN LA CAJA CHICA SUPERA EL MONTO A PAGAR!"), HttpStatus.CONFLICT);
		}
		
		entity.setHora(hora());
		OperacionCaja op= new OperacionCaja();
		op.setMonto(entity.getMonto());
		op.getTipoOperacion().setId(entity.getTipoOperacion().getId());
		Integer idApe= aperturaRepository.getAperturaActivoCajaId(entity.getFuncionario().getId());
		System.out.println("idApe edddiiitaaerr: "+idApe);
		System.out.println(aperturaRepository.getAperturaActivoCajaId(entity.getFuncionario().getId()));
		op.getAperturaCaja().setId(idApe);
		
		op.setEfectivo(0.0);
		op.setVuelto(0.0);
		Concepto c= new Concepto();
//		c= conceptoRepository.findById(9).get();
//		op.setMotivo(c.getDescripcion()+" REF.: "+ entity.getId());
		
		if(entity.getTipoMovimiento().getId()==1) {
			op.getConcepto().setId(10);
			c= conceptoRepository.findById(10).get();
			op.setMotivo(c.getDescripcion()+" REF.: "+ entity.getId());
			op.setTipo("ENTRADA");
			System.out.println("Entrada");
			if (op.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldo(idApe, entity.getMonto());
			}
			if (op.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoCheque(idApe, entity.getMonto());
			}
			if (op.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoTarjeta(idApe, entity.getMonto());
			}
			
		}else if(entity.getTipoMovimiento().getId()==2) {
			System.out.println("Salida");
			op.getConcepto().setId(11);
			c= conceptoRepository.findById(11).get();
			op.setMotivo(c.getDescripcion()+" REF.: "+ entity.getId());
			op.setTipo("SALIDA");
			if (op.getTipoOperacion().getId() == 1) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(idApe, entity.getMonto());
			}
			if (op.getTipoOperacion().getId() == 2) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(idApe, entity.getMonto());
			}
			if (op.getTipoOperacion().getId() == 3) {
				aperturaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(idApe, entity.getMonto());
			}
		}
		operacionCajaRepository.save(op);
		
		OperacionCaja opera =  operacionCajaRepository.findTop1ByOrderByIdDesc();
		

		entity.setOperacionCaja(opera);
		entityRepository.save(entity);
		EntradaSalidaCaja entSalCaja= entityRepository.findTop1ByOrderByIdDesc();
		Concepto cc= new Concepto();

		if(entity.getTipoMovimiento().getId()==1) {
			opera.getConcepto().setId(10);
			cc= conceptoRepository.findById(10).get();
			opera.setMotivo(cc.getDescripcion()+" REF.: "+ entSalCaja.getId());
			opera.setTipo("ENTRADA");
			System.out.println("Entrada");
			
			
		}else if(entity.getTipoMovimiento().getId()==2) {
			System.out.println("Salida");
			opera.getConcepto().setId(11);
			cc= conceptoRepository.findById(11).get();
			opera.setMotivo(cc.getDescripcion()+" REF.: "+ entSalCaja.getId());
			opera.setTipo("SALIDA");
			
		}
		operacionCajaRepository.save(opera);
		
//		entityRepository.findByActualizarEntradaSalidaCajaCabeceraOperacion(entSalCaja.getId(), opera.getId());
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	public List<EntradaSalidaCaja> listadosss(List<Object[]> lista) {
		List<EntradaSalidaCaja> listadoRetorno = new ArrayList<EntradaSalidaCaja>();
		//List<Object> listadoRetorno = new ArrayList<EntradaSalidaCaja>();
		for(Object[] l: lista) {
			EntradaSalidaCaja o =new EntradaSalidaCaja();
			o.setId(Integer.parseInt(l[0].toString()));
			o.setFecha(FechaUtil.convertirFechaStringADateUtil(l[0].toString()));
			o.getFuncionario().setId(Integer.parseInt(l[0].toString()));
			o.getFuncionario().getPersona().setNombre(l[0].toString());
			o.getTipoOperacion().setId(Integer.parseInt(l[0].toString()));
			o.getTipoOperacion().setDescripcion(l[0].toString());;
			o.getTipoMovimiento().setId(Integer.parseInt(l[0].toString()));
			o.getTipoMovimiento().setDescripcion(l[0].toString());
			o.setMonto(Double.parseDouble(l[0].toString()));
			o.setMotivo(l[0].toString());
//			o.setOperacionCaja(Integer.parseInt(l[0].toString()));
			listadoRetorno.add(o);
		}
		
		return listadoRetorno;
	}
	
	@RequestMapping(method=RequestMethod.GET,value = "/updateReferencia/{id}/{refere}")
	public ResponseEntity<?> updateReferencia(@PathVariable int id, @PathVariable String refere){
		entityRepository.findByActualizaReferencia(refere, id);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<EntradaSalidaCaja> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<EntradaSalidaCaja> objeto=entityRepository.getEntradaSalidaCajaFecha(ano, mes, dia);
		List<EntradaSalidaCaja> venta=new ArrayList<>();
		for(EntradaSalidaCaja ob:objeto){
			EntradaSalidaCaja retorno=new EntradaSalidaCaja();
			retorno.setId(ob.getId());
			retorno.setFecha(ob.getFecha());
			retorno.getFuncionario().setId(ob.getFuncionario().getId());
			retorno.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+ " "+ob.getFuncionario().getPersona().getApellido());
			retorno.getTipoOperacion().setId(ob.getTipoOperacion().getId());
			retorno.getTipoOperacion().setDescripcion(ob.getTipoOperacion().getDescripcion());;
			retorno.getTipoMovimiento().setId(ob.getTipoMovimiento().getId());
			retorno.getTipoMovimiento().setDescripcion(ob.getTipoMovimiento().getDescripcion());
			retorno.setMonto(ob.getMonto());
			retorno.setMotivo(ob.getMotivo());
			retorno.setHora(ob.getHora());
			retorno.getOperacionCaja().setId(ob.getOperacionCaja().getId());
			retorno.setReferencia(ob.getReferencia());
			System.out.println(retorno.getOperacionCaja().getId());
			venta.add(retorno); 
		}
		return venta;
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{idMovimiento}")
	public EntradaSalidaCaja getEntradaSalidaPorId(@PathVariable int idMovimiento){
		return entityRepository.getEntradaSalidaPorId(idMovimiento);
	}
	
	
}
