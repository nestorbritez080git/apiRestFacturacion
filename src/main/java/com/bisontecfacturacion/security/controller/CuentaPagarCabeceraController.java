package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarDetalle;
import com.bisontecfacturacion.security.model.DetalleCompra;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.CompraRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarCabeceraRepository;
import com.bisontecfacturacion.security.repository.CuentaPagarDetalleRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;


@Transactional
@RestController
@RequestMapping("cuentaPagar")
public class CuentaPagarCabeceraController {
	
	@Autowired
	private CuentaPagarCabeceraRepository entityRepository;
	
	@Autowired
	private CuentaPagarDetalleRepository detalleRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
    private OrgRepository orgRepository;
	
	@Autowired
    private ProveedorRepository proveedorRepository;
	
	private Reporte report;
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody CuentaPagarCabecera entity){
		entity.setFecha(new Date());
		entity.setHora(hora());
		if(entity.getCompra().getId()==0) {
			Compra v = compraRepository.getUltimaCompra();
			entity.getCompra().setId(v.getId());
		}
		try {
			 if(entity.getFuncionario().getId() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		     } else if(entity.getTipoPlazo().getId() == 0) {
		            return new ResponseEntity<>(new CustomerErrorType("EL TIPO PLAZO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		     } else if(entity.getProveedor().getId()==0) {
		    	  	return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
		     } else if(entity.getTotal() <=0 ) {
		    	 	return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA CUENTA DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
		     } else if(entity.getFraccionCuota() <=0) {
		    	 	return new ResponseEntity<>(new CustomerErrorType("EL NÚMERO DE CUOTA O FRACCIÓN DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
		     } else if(entity.getCuentaPagarDetalle().size() <=0){
	                return new ResponseEntity<>(new CustomerErrorType("DEBES AGREGAR POR LO MENO UN DETALLE DE CUENTA A PAGAR!"), HttpStatus.CONFLICT);
	         } if(entity.getCompra().getId() ==0){
	                return new ResponseEntity<>(new CustomerErrorType("LA REFERENCIA DE LA COMPRA EN!"), HttpStatus.CONFLICT);
	         }  else {
	        	  for(int ind=0; ind < entity.getCuentaPagarDetalle().size(); ind++) {
	                  CuentaPagarDetalle det = entity.getCuentaPagarDetalle().get(ind);
	                  if(det.getNumeroCuota() <=0 ) {
	                      return new ResponseEntity<>(new CustomerErrorType("EL NÚMERO DE CUOTA DEL DETALLE ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	                  }else if(det.getMonto() <= 0){
	                      return new ResponseEntity<>(new CustomerErrorType("EL MONTO DE LA CUOTA DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	                  }else if(det.getSubTotal() <= 0){
	                      return new ResponseEntity<>(new CustomerErrorType("EL SUBTOTAL DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	                  }else if(det.getFechaVencimiento() == null) {
	                      return new ResponseEntity<>(new CustomerErrorType("LA FECHA DEL DETALLE FLETE ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);  
	                  }
	              }
	        	  
	        	  if(entity.getId() !=0) {
	        		  entityRepository.save(entity);
	                  if(entity.getCuentaPagarDetalle().size()>0){
	                      for(CuentaPagarDetalle det: entity.getCuentaPagarDetalle()) {
	                    	  CuentaPagarDetalle detalle=new CuentaPagarDetalle();
	                          detalle.setId(det.getId());
	                          detalle.setNumeroCuota(det.getNumeroCuota());
	                          detalle.setMonto(det.getMonto());
	                          detalle.setSubTotal(det.getSubTotal());
	                          detalle.setFechaVencimiento(det.getFechaVencimiento());
	                          detalle.setEstado(det.isEstado());
	                          detalle.setImporte(det.getImporte());
	                          detalle.getCuentaPagarCabecera().setId(entity.getId());
	                          detalleRepository.save(detalle);
	                          
	                      }
	                  }else{}
	        	  } else {
	        		  entityRepository.save(entity);
	                  CuentaPagarCabecera id = entityRepository.findTop1ByOrderByIdDesc();
	                  int idFle=0;
	                  if(id == null){idFle=1;}else{idFle=id.getId();}
	                  //eliminarDetallePorCabecera(entity.getId());
	                  if(entity.getCuentaPagarDetalle().size()>0){
	                      for(CuentaPagarDetalle det: entity.getCuentaPagarDetalle()) {
	                    	  CuentaPagarDetalle detalle=new CuentaPagarDetalle();
	                    	  detalle.setId(det.getId());
	                          detalle.setNumeroCuota(det.getNumeroCuota());
	                          detalle.setMonto(det.getMonto());
	                          detalle.setSubTotal(det.getSubTotal());
	                          detalle.setFechaVencimiento(det.getFechaVencimiento());
	                          detalle.setEstado(det.isEstado());
	                          detalle.setImporte(det.getImporte());
	                          detalle.getCuentaPagarCabecera().setId(idFle);
	                          detalleRepository.save(detalle);
	                      }
	                  }else {}
	        	  }         
	         }
		} catch (Exception e) {
			 return new ResponseEntity<>(new CustomerErrorType(""+e.getCause().getMessage()), HttpStatus.CONFLICT);
	    }
	        
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.GET, value="/buscar/compra/detalleCompra/{idCuenta}")
	public List<DetalleCompra> consultarCompraDetalleCuentaPorIdCabecera(@PathVariable int idCuenta){
		List<Object[]> object = detalleRepository.getDetalleProductoCompraCuenta(idCuenta);
		List<DetalleCompra> lista= new ArrayList<>();		  
		for(Object[] ob: object){
			DetalleCompra cue= new DetalleCompra();
			cue.setDescripcion(ob[0].toString());
			cue.setPrecioCosto(Double.parseDouble(ob[1].toString()));
			cue.setCantidad(Double.parseDouble(ob[2].toString()));
			cue.setSubTotal(Double.parseDouble(ob[3].toString()));
			cue.getProducto().getUnidadMedida().setDescripcion(ob[4].toString());
			lista.add(cue);
		}

		return lista;
	}

	
	@RequestMapping(method=RequestMethod.POST, value="/cancelarCuenta")
	public ResponseEntity<?> cancelarCuenta(@RequestBody CuentaPagarCabecera entity){
		entityRepository.findByCancelarCuentaCabecera(entity.getSaldo(), entity.getPagado(), entity.getId());
		List<CuentaPagarDetalle> detalle = detalleRepository.getDetalleXIdCabecera(entity.getId());
		detalleRepository.cancelarCuentaDetalle(detalle.get(0).getMonto(), entity.getId());	
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	public String hora() {
		 return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	@RequestMapping(method = RequestMethod.GET, value="/lista/{tipo}/{tipoCuenta}")
	public List<CuentaPagarCabecera> getAll(@PathVariable int tipo, @PathVariable int tipoCuenta){
		List<CuentaPagarCabecera> cuenta = new ArrayList<>();
		
		if (tipo == 1) {
			if(tipoCuenta == 1) {
				List<Object []> lis= entityRepository.getProveedorCuentaPagarAll();
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 2) {
				List<Object []> lis= entityRepository.getCompraCreditoAll();
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 3) {
				List<Object []> lis= entityRepository.getDespachoEntradaCreditoAll();
				cuenta = cuentaListado(lis);
			}
			
		} else {
			if(tipoCuenta == 1) {
				List<Object []> lis= entityRepository.getProveedorCuentaPagado();
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 2) {
				List<Object []> lis= entityRepository.getProveedorCompraCuentaPagado();
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 3) {
				List<Object []> lis= entityRepository.getProveedorDespEntradaCuentaPagado();
				cuenta = cuentaListado(lis);
			}

		}
		
		return cuenta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{desc}/{tipo}/{tipoCuenta}")
	public List<CuentaPagarCabecera> getCuenta(@PathVariable String desc, @PathVariable int tipo, @PathVariable int tipoCuenta) {
		List<CuentaPagarCabecera> cuenta = new ArrayList<>();
		if (tipo == 1) {
			if(tipoCuenta == 1) {
				List<Object []> lis= entityRepository.getAllCuentaAPagar("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 2) {
				List<Object []> lis= entityRepository.getCompraCreditoFiltro("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 3) {
				List<Object []> lis= entityRepository.getDespEntradaCreditoFiltro("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
		} else {
			if(tipoCuenta == 1) {
				List<Object []> lis= entityRepository.getAllCuentaPagado("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 2) {
				List<Object []> lis= entityRepository.getCompraCreditoFiltroPagado("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
			if(tipoCuenta == 3) {
				List<Object []> lis= entityRepository.getDespSalidaCreditoFiltroPagado("%" + desc.toUpperCase() + "%");
				cuenta = cuentaListado(lis);
			}
		}
		
		return cuenta;
	}

	public List<CuentaPagarCabecera> cuentaListado(List<Object[]> object) {
		List<CuentaPagarCabecera> listadoRetorno= new ArrayList<>();
		for (Object[] cue: object) {
			CuentaPagarCabecera cuenta= new CuentaPagarCabecera();
			//cuenta.setId(cue[0].toString();
			cuenta.setTotal(Double.parseDouble(cue[0].toString()));
			cuenta.setPagado(Double.parseDouble(cue[1].toString()));
			cuenta.setSaldo(Double.parseDouble(cue[2].toString()));
			cuenta.getProveedor().getPersona().setNombre(cue[3].toString()+" "+ cue[4].toString());
			cuenta.getProveedor().setId(Integer.parseInt(cue[5].toString()));
			cuenta.getProveedor().getPersona().setCedula(cue[6].toString());
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
	}
	@RequestMapping(method = RequestMethod.GET, value="/lista/{tipo}")
	public List<CuentaPagarCabecera> getAll(@PathVariable int tipo){
		if (tipo == 1) {
			List<Object []> lis= entityRepository.getProveedorCuentaACobrar();
			return cuentaListadosss(lis);
		} else {
			List<Object []> lis= entityRepository.getProveedorCuentaCobrado();
			return cuentaListadosss(lis);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{desc}/{tipo}")
	public List<CuentaPagarCabecera> getCuenta(@PathVariable String desc, @PathVariable int tipo) {
		if (tipo == 1) {
			List<Object[]> object = entityRepository.getAllCuentaAPagar("%" + desc.toUpperCase() + "%");
			return cuentaListado(object);
		} else {
			List<Object[]> object = entityRepository.getAllCuentaPagado("%" + desc.toUpperCase() + "%");
			return cuentaListado(object);
		}
	}
	public List<CuentaPagarCabecera> cuentaListadosss(List<Object[]> object) {
		List<CuentaPagarCabecera> listadoRetorno= new ArrayList<>();
		for (Object[] cue: object) {
			CuentaPagarCabecera cuenta= new CuentaPagarCabecera();
			//cuenta.setId(cue[0].toString();
			cuenta.setTotal(Double.parseDouble(cue[0].toString()));
			cuenta.setPagado(Double.parseDouble(cue[1].toString()));
			cuenta.setSaldo(Double.parseDouble(cue[2].toString()));
			cuenta.getProveedor().getPersona().setNombre(cue[3].toString()+", "+ cue[4].toString());
			cuenta.getProveedor().setId(Integer.parseInt(cue[5].toString()));
			cuenta.getProveedor().getPersona().setCedula(cue[6].toString());
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
	}
	public List<CuentaPagarCabecera> listadoCuentaProveedor(List<CuentaPagarCabecera> lis){
		List<CuentaPagarCabecera> listadoRetorno = new ArrayList<>();
		for(CuentaPagarCabecera cue :lis) {
			CuentaPagarCabecera cuenta= new CuentaPagarCabecera();
			cuenta.setId(cue.getId());
			cuenta.setTotal(cue.getTotal()); 
			cuenta.setPagado(cue.getPagado());
			cuenta.setSaldo(cue.getSaldo());
			cuenta.getProveedor().getPersona().setNombre(cue.getProveedor().getPersona().getNombre());
			cuenta.getProveedor().getPersona().setApellido(cue.getProveedor().getPersona().getApellido());
			cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
			cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
			cuenta.getCompra().setFechaFactura(cue.getFecha());

			//cuenta.getVenta().setFecha(sumarDia(cue.getFecha(), (24 * cue.getTipoPlazo().getValor())));
			//cuenta.getTipoPlazo().setValor(validarDiaAtraso(cuenta.getVenta().getFecha()));
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
	}

	public List<CuentaPagarCabecera> listadoDetalleCuenta(List<CuentaPagarCabecera> lis){
		List<CuentaPagarCabecera> listadoRetorno = new ArrayList<>();
		for(CuentaPagarCabecera cue :lis) {
			CuentaPagarCabecera cuenta= new CuentaPagarCabecera();
			cuenta.setId(cue.getId());
			cuenta.setTotal(cue.getTotal()); 
			cuenta.setPagado(cue.getPagado());
			cuenta.setSaldo(cue.getSaldo());
			cuenta.getProveedor().getPersona().setNombre(cue.getProveedor().getPersona().getNombre());
			cuenta.getProveedor().getPersona().setApellido(cue.getProveedor().getPersona().getApellido());
			cuenta.getFuncionario().getPersona().setNombre(cue.getFuncionario().getPersona().getNombre());
			cuenta.getFuncionario().getPersona().setApellido(cue.getFuncionario().getPersona().getApellido());
			cuenta.setFecha(cue.getFecha());

			cuenta.getCompra().setFecha(sumarDia(cue.getFecha(), (24 * cue.getTipoPlazo().getValor())));
			cuenta.getTipoPlazo().setValor(validarDiaAtraso(cuenta.getCompra().getFecha()));
			System.out.println("Cuneta dia atraso:  "+cuenta.getTipoPlazo().getValor());
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
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
	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	public List<CuentaPagarCabecera> listado(List<Object[]> object) {
		List<CuentaPagarCabecera> listadoRetorno= new ArrayList<>();
		for (Object[] cue: object) {
			CuentaPagarCabecera cuenta= new CuentaPagarCabecera();
			//cuenta.setId(cue[0].toString();
			cuenta.setId(Integer.parseInt(cue[0].toString()));
			cuenta.getProveedor().getPersona().setNombre(cue[1].toString());
			cuenta.getFuncionario().getPersona().setNombre(cue[2].toString());
			cuenta.setTotal(Double.parseDouble(cue[3].toString()));
			cuenta.setPagado(Double.parseDouble(cue[4].toString()));
			cuenta.setSaldo(Double.parseDouble(cue[5].toString()));
			cuenta.setFecha(FechaUtil.convertirFechaStringADateUtil(cue[8].toString()));
			listadoRetorno.add(cuenta);
		}
		return listadoRetorno;
	}
	@RequestMapping(method = RequestMethod.GET, value="/buscarCuenta/{id}/{filtro}")
	public List<CuentaPagarCabecera> getCuentaPorClienteId(@PathVariable int id, @PathVariable int filtro){
		List<CuentaPagarCabecera> lis =new ArrayList<>();
		if(filtro == 1){
			lis= entityRepository.findByCuentaPorIdTodo(id);
		}
		if(filtro == 2){
			lis= entityRepository.findByCuentaPorIdAPagarListas(id);
		}
		if(filtro == 3){
			lis= entityRepository.findByCuentaPorIdAPagarListas(id);
		}
		return listadoDetalleCuenta(lis);
	}

	@RequestMapping(method = RequestMethod.GET, value="/buscarCuenta/{id}")
	public CuentaPagarCabecera getCuentaId(@PathVariable int id){
		CuentaPagarCabecera c = entityRepository.getOne(id);
		CuentaPagarCabecera retorno= null;
		if (c!=null) {
			retorno = new CuentaPagarCabecera();
			retorno.setId(c.getId());
			retorno.setTotal(c.getTotal()); 
			retorno.setPagado(c.getPagado());
			retorno.setSaldo(c.getSaldo());
			retorno.getProveedor().getPersona().setNombre(c.getProveedor().getPersona().getNombre());
			retorno.getProveedor().getPersona().setApellido(c.getProveedor().getPersona().getApellido());
			retorno.getFuncionario().getPersona().setNombre(c.getFuncionario().getPersona().getNombre());
			retorno.getFuncionario().getPersona().setApellido(c.getFuncionario().getPersona().getApellido());
			retorno.setFecha(c.getFecha());

			retorno.getCompra().setFecha(sumarDia(c.getFecha(), (24 * c.getTipoPlazo().getValor())));
			retorno.getTipoPlazo().setValor(validarDiaAtraso(c.getCompra().getFecha()));
					System.out.println("Cuneta dia atraso:  "+c.getTipoPlazo().getValor());
		}
		
		return retorno;
	}
	@RequestMapping(method = RequestMethod.GET, value="/listado/{idProveedor}/{tipo}")
	public List<CuentaPagarCabecera> getCuentaCabeceraPorProveedor(@PathVariable int idProveedor, @PathVariable int tipo){
		List<Object []> lis = new ArrayList<>();
		
		if (tipo == 1) {
			//todo
				lis= entityRepository.getCuentaPagarPorProveeedorTodo(idProveedor);
		}
		if (tipo == 2) {
			//cuenta a pagar
				lis= entityRepository.getCuentaPagarPorProveeedoAPagar(idProveedor);
		}
		if (tipo == 3) {
			//cuenta pagado
				lis= entityRepository.getCuentaPagarPorProveeedorPagado(idProveedor);
		}
		
		
		return listado(lis);
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/detalle/{idCuenta}")
	public List<CuentaPagarDetalle> consultarCuentaDetallePorIdCabecera(@PathVariable int idCuenta){
		List<Object []> lis = new ArrayList<>();
		List<CuentaPagarDetalle> lisRetorno = new ArrayList<>();
		lis=detalleRepository.consultarDetalleCuentaPorIdCabecera(idCuenta);
		for (Object [] ob: lis) {
			CuentaPagarDetalle cuDet= new CuentaPagarDetalle();
			cuDet.getCuentaPagarCabecera().setFraccionCuota(Integer.parseInt(ob[0].toString()));
			cuDet.setNumeroCuota(Integer.parseInt(ob[1].toString()));
			cuDet.setFechaVencimiento(FechaUtil.convertirFechaStringADateUtil(ob[2].toString()));
			cuDet.setMonto(Double.parseDouble(ob[3].toString()));
			cuDet.setImporte(Double.parseDouble(ob[4].toString()));
			cuDet.setId(Integer.parseInt(ob[5].toString()));
			cuDet.setSubTotal(Double.parseDouble(ob[6].toString()));
			cuDet.getCuentaPagarCabecera().setId(Integer.parseInt(ob[7].toString()));
			lisRetorno.add(cuDet);
			
		}
		return lisRetorno;
	}
	@RequestMapping(method = RequestMethod.GET, value="/detalleCompra/{idCompra}")
	public List<DetalleCompra> getDetalleCompraCuenta(@PathVariable int idCompra){
		List<Object []> lis = new ArrayList<>();
		List<DetalleCompra> lisRetorno = new ArrayList<>();
		lis=detalleRepository.getDetalleCompraCuenta(idCompra);
		for (Object [] ob: lis) {
			DetalleCompra det= new DetalleCompra();
			det.setDescripcion(ob[0].toString());
			det.setCantidad(Double.parseDouble(ob[1].toString()));
			det.setPrecioCosto(Double.parseDouble(ob[2].toString()));
			det.setSubTotal(Double.parseDouble(ob[3].toString()));
			lisRetorno.add(det);
		}
		return lisRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
	public CuentaPagarCabecera  getCuentaCobrarID(@PathVariable int id){
		CuentaPagarCabecera c=entityRepository.findById(id).get();
		CuentaPagarCabecera cuenta=new CuentaPagarCabecera();
		cuenta.setId(c.getId());
		System.out.println("iiid  proveeedoorroror :   "+c.getProveedor().getId());
		cuenta.getProveedor().setId(c.getProveedor().getId());
		cuenta.getProveedor().getPersona().setNombre(c.getProveedor().getPersona().getNombre());
		cuenta.getProveedor().getPersona().setApellido(c.getProveedor().getPersona().getApellido());
		cuenta.setTotal(c.getTotal());
		cuenta.getTipoPlazo().setDescripcion(c.getTipoPlazo().getDescripcion());
		cuenta.setPagado(c.getPagado());
	//  cuenta.getInteresMora().setDescripcion(c.getInteresMora().getDescripcion());
		cuenta.setSaldo(c.getSaldo());
		cuenta.setFecha(c.getFecha());
		cuenta.setHora(c.getHora());
		cuenta.setFraccionCuota(c.getFraccionCuota());
		return cuenta;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reporteCuentaProveedor/{id}/{filtro}")
	public  ResponseEntity<?> getReporteCuentaProveedor(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id, @PathVariable int filtro) throws IOException{
		List<CuentaPagarCabecera> lis =new ArrayList<>();
		if(filtro == 1){
		System.out.println("Entro cuenta todo");
			lis= entityRepository.findByCuentaPorIdTodo(id);
		System.out.println(lis.size());
		}
		if(filtro == 2){
			System.out.println("Entro cuenta por pagar");
			lis= entityRepository.findByCuentaPorIdAPagarListas(id);
			System.out.println(lis.size());
		}
		if(filtro == 3){
			System.out.println("Entro cuenta pagadp");
			lis= entityRepository.findByCuentaPorIdPagado(id);
			System.out.println(lis.size());
		}
	List<CuentaPagarCabecera> listado= listadoCuentaProveedor(lis);
	String nombreCliente="";
	if(listado.size()>0) {
		nombreCliente = listado.get(0).getProveedor().getPersona().getNombre()+" "+listado.get(0).getProveedor().getPersona().getApellido();
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
			report.reportPDFDescarga(listado, map, "ReporteCuentaProveedor", response);
		
			return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}
	
		
		
	}
	
	
}
