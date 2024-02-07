package com.bisontecfacturacion.security.hoteleria.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.hoteleria.model.CuentaCobrarCabeceraReservaciones;
import com.bisontecfacturacion.security.hoteleria.model.CuentaCobrarDetalleReservaciones;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.repository.CuentaCobrarCabeceraReservacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.CuentaCobrarDetalleReservacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;


@RestController
@RequestMapping("cuentaCobrarCabeceraReservaciones")
public class CuentaCobrarReservacionesController {
	private Reporte report;
	@Autowired
	private CuentaCobrarCabeceraReservacionesRepository entityRepository;
	
	@Autowired
	private CuentaCobrarDetalleReservacionesRepository detalleRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private ReservacionCabeceraRepository reservacionesRepository;
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/detalle")
	public void saveDetalle(@RequestBody List<CuentaCobrarDetalleReservaciones> detalle){
//		System.out.println("asdfasdfasdf ********* adasfsadfa s 0+"+detalle.get(0).getCuentaCobrarCabecera().getId());
		int idCabecera=0;
		
		//CuentaCobrarCabecera cuu= entityRepository.getCuentaCabecera(detalle.get(0).getCuentaCobrarCabecera().getId());
		CuentaCobrarCabeceraReservaciones cuc = entityRepository.findTop1ByOrderByIdDesc();
		Double totalImportePorCuenta=0.0;
		for(CuentaCobrarDetalleReservaciones ob: detalle){
			if(ob.getId() != 0){
//				ob.getCuentaCobrarCabecera().setId(ob.getCuentaCobrarCabecera().getId());
				totalImportePorCuenta = totalImportePorCuenta + ob.getImporte();
				idCabecera=ob.getCuentaCobrarCabeceraReservaciones().getId();
				
				detalleRepository.save(ob);
			}else {
				ob.setSubTotal(ob.getMonto());
				ob.getCuentaCobrarCabeceraReservaciones().setId(cuc.getId());
				idCabecera=cuc.getId();
				totalImportePorCuenta = totalImportePorCuenta + ob.getImporte();
				detalleRepository.save(ob);
			}

		}
		System.out.println("idCab: "+idCabecera+"   motoCabeceraCobrado:  "+totalImportePorCuenta);
		System.out.println();
		entityRepository.findByActualizarPagadoCuentaReservaciones(idCabecera, totalImportePorCuenta);

	}

	@RequestMapping(method=RequestMethod.GET, value = "/liquidarDetalle/{id}")
	public void liquidarDetalle(@PathVariable int id){
		this.detalleRepository.liquidarDetalle(id, new Date(), true);
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody CuentaCobrarCabeceraReservaciones entity){
		try {
			entity.setFecha(LocalDateTime.now());
			if(entity.getCliente().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getInteresCuota().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UN INTERES CUOTA DESDE LA OPCIÓN DE FACTURACIÒN A CREDITO"), HttpStatus.CONFLICT);
			}else if(entity.getInteresMora().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR UN INTERES MORATORIA DESDE LA OPCIÓN DE FACTURACIÓN A CREDITO!"), HttpStatus.CONFLICT);
			}else if(entity.getTotal()<=0) {
				return new ResponseEntity<>(new CustomerErrorType("EL MONTO TOTAL DE LA CUENTA A GENERAR DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionario().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new  ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value="/lista/{tipo}")
	public List<CuentaCobrarCabeceraReservaciones> getAll(@PathVariable int tipo){
		if (tipo == 1) {
			List<Object []> lis= entityRepository.getCLienteCuentaACobrar();
			return cuentaListado(lis);
		} else {
			List<Object []> lis= entityRepository.getCLienteCuentaCobrado();
			return cuentaListado(lis);
		}
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{desc}/{tipo}")
	public List<CuentaCobrarCabeceraReservaciones> getCuentaFiltro(@PathVariable String desc, @PathVariable int tipo) {
		if (tipo == 1) {
			List<Object[]> object = entityRepository.getAllCuentaACobrar("%" + desc.toUpperCase() + "%");
			return cuentaListado(object);
		} else {
			List<Object[]> object = entityRepository.getAllCuentaCobrado("%" + desc.toUpperCase() + "%");
			return cuentaListado(object);
		}
	}
	
	public List<CuentaCobrarCabeceraReservaciones> cuentaListado(List<Object[]> object) {
		List<CuentaCobrarCabeceraReservaciones> listadoRetorno= new ArrayList<>();
		for (Object[] cue: object) {
			CuentaCobrarCabeceraReservaciones cuenta= new CuentaCobrarCabeceraReservaciones();
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

	@RequestMapping(method = RequestMethod.GET, value="/buscarCuenta/{id}/{filtro}")
	public List<CuentaCobrarCabeceraReservaciones> getCuentaPorClienteId(@PathVariable int id, @PathVariable int filtro){
		List<CuentaCobrarCabeceraReservaciones> lis =new ArrayList<>();
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
	
	public List<CuentaCobrarCabeceraReservaciones> listado(List<CuentaCobrarCabeceraReservaciones> lis){
		List<CuentaCobrarCabeceraReservaciones> listadoRetorno = new ArrayList<>();
		for(CuentaCobrarCabeceraReservaciones cue :lis) {
			CuentaCobrarCabeceraReservaciones cuenta= new CuentaCobrarCabeceraReservaciones();
			cuenta.setId(cue.getId());
			cuenta.setTotal(cue.getTotal()); 
			cuenta.setPagado(cue.getPagado());
			cuenta.setSaldo(cue.getSaldo());
			cuenta.getCliente().getPersona().setNombre(cue.getCliente().getPersona().getNombre());
			cuenta.getCliente().getPersona().setApellido(cue.getCliente().getPersona().getApellido());
			cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
			cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
			cuenta.setFecha(cue.getFecha());

			cuenta.getReservacionCabecera().setFechaFactura(cue.getReservacionCabecera().getFechaFactura());
			//cuenta.getTipoPlazo().setValor(validarDiaAtraso(cuenta.getVenta().getFecha()));
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
	
	@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
	public CuentaCobrarCabeceraReservaciones  getCuentaCobrarID(@PathVariable int id){
		CuentaCobrarCabeceraReservaciones c=entityRepository.findById(id).get();
		CuentaCobrarCabeceraReservaciones cuenta=new CuentaCobrarCabeceraReservaciones();
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
		cuenta.getReservacionCabecera().getFuncionarioFinalizacion().getPersona().setNombre(c.getReservacionCabecera().getFuncionarioFinalizacion().getPersona().getNombre() + " " + c.getReservacionCabecera().getFuncionarioFinalizacion().getPersona().getApellido());
		cuenta.getReservacionCabecera().setOperacionCaja(c.getReservacionCabecera().getOperacionCaja());
		cuenta.getReservacionCabecera().getDocumento().setDescripcion(c.getReservacionCabecera().getDocumento().getDescripcion());
		if (Integer.parseInt(c.getReservacionCabecera().getTipo()) == 1) {
			cuenta.getReservacionCabecera().setTipo("CONTADO");
		} else {
			cuenta.getReservacionCabecera().setTipo("CREDITO");
		}
		return cuenta;

	}
}
