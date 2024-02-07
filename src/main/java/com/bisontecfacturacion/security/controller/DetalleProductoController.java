package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.ResumenTesoreriaUtilidad;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.CierreCaja;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.DetalleProductoRepository;
import com.bisontecfacturacion.security.repository.DetalleServicioRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("detalleProducto")
public class DetalleProductoController {
	private Reporte report;
	@Autowired
	private DetalleProductoRepository entityRepository;
	@Autowired
	private DetalleServicioRepository detalleServicioRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	
	
	
	@RequestMapping(method=RequestMethod.GET, value = "/buscarDesc/{fechaI}/{fechaF}/{desc}")
	public List<DetalleProducto> getDetalleProductoDesc(@PathVariable String desc, @PathVariable String fechaI, @PathVariable String fechaF){
		//System.out.println("entrooo metodo donde se cunsultarafasfaf");
		List<DetalleProducto> listRetorno=new ArrayList<>();
		try {
		Calendar cc= Calendar.getInstance();
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI;
		//System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
		fecI = formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		System.out.println(fecF.getDate());
		fecF.setHours(23);
		fecI.setHours(1);
		//System.out.println("entrooo rango por descripcion::: "+fecF+ " hora inicio finbal: "+fecI);
		List<Object[]> objeto=entityRepository.listaDetalleProductoDesc(fecI, fecF,"%"+Utilidades.eliminaCaracterIzqDer(desc)+"%");
		for(Object[] ob:objeto){
			DetalleProducto dp=new DetalleProducto();
			dp.getVenta().setId(Integer.parseInt(ob[0].toString()));
			dp.setDescripcion(ob[1].toString());
			dp.getProducto().setCodbar(ob[2].toString());
			dp.getProducto().getMarca().setDescripcion(ob[3].toString());
			dp.setCantidad(Double.parseDouble(ob[4].toString()));
			dp.setPrecio(Double.parseDouble(ob[5].toString()));
			dp.setDescuento(Double.parseDouble(ob[6].toString()));
			dp.setSubTotal(Double.parseDouble(ob[7].toString()));
			listRetorno.add(dp);
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println(listRetorno.size());
		return listRetorno;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value = "/buscarRangoVentaFecha/{fechaI}/{fechaF}")
	public List<DetalleProducto> getDetalleProductoPorRangoVentaFecha(@PathVariable String fechaI, @PathVariable String fechaF){
		
		List<DetalleProducto> listRetorno=new ArrayList<>();
		try {
		Calendar cc= Calendar.getInstance();
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI;
		System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
		fecI = formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		System.out.println(fecF.getDate());
		fecF.setHours(23);
		fecI.setHours(1);
		System.out.println("hora final fechas::: "+fecF+ " hora inicio finbal: "+fecI);
		
		List<Object[]> objeto=entityRepository.listaDetalleProductoAll(fecI, fecF);
		
		
		for(Object[] ob:objeto){
			DetalleProducto dp=new DetalleProducto();
			dp.getVenta().setId(Integer.parseInt(ob[0].toString()));
			dp.setDescripcion(ob[1].toString());
			dp.getProducto().setCodbar(ob[2].toString());
			dp.getProducto().getMarca().setDescripcion(ob[3].toString());
			dp.setCantidad(Double.parseDouble(ob[4].toString()));
			dp.setPrecio(Double.parseDouble(ob[5].toString()));
			dp.setDescuento(Double.parseDouble(ob[6].toString()));
			dp.setSubTotal(Double.parseDouble(ob[7].toString()));
			listRetorno.add(dp);
		}
		System.out.println(listRetorno.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listRetorno;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public List<DetalleProducto> getAllId(@PathVariable int id){
		List<Object[]> objeto=entityRepository.lista(id);	
		List<DetalleProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetalleProducto detalleProductos=new DetalleProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getVenta().setId(Integer.parseInt(ob[7].toString()));
			
			detalleProductos.getProducto().setPrecioVenta_1(Double.parseDouble(ob[8].toString()));
			detalleProductos.getProducto().setPrecioVenta_2(Double.parseDouble(ob[9].toString()));
			detalleProductos.getProducto().setPrecioVenta_3(Double.parseDouble(ob[10].toString()));
			detalleProductos.getProducto().setPrecioVenta_4(Double.parseDouble(ob[11].toString()));
			detalleProductos.setDescuento(Double.parseDouble((ob[12].toString())));
			detalleProductos.getProducto().getUnidadMedida().setDescripcion(ob[13].toString());
			if (ob[14].toString()==null) { detalleProductos.getProducto().setExistencia(0.0); } else { detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[14].toString())); }
			detalleProductos.setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			detalleProductos.getProducto().setCodbar(ob[16].toString());
			detalleProductos.getProducto().getMarca().setDescripcion(ob[17].toString());
			detalleProductos.setMontoIva(Double.parseDouble(ob[18].toString()));
			detalleProductos.setCosto(Double.parseDouble(ob[19].toString()));
			detalleProductos.setTipoPrecio(ob[20].toString());
			detalleProducto.add(detalleProductos);
		}
		
		return detalleProducto;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/detalleServicio/{id}")
	public List<DetalleServicios> getAllIdServicio(@PathVariable int id){
		List<Object[]> objeto=detalleServicioRepository.lista(id);
		List<DetalleServicios> detalleServicio=new ArrayList<>();
		for(Object[] ob:objeto){
			DetalleServicios detalleServicios=new DetalleServicios();
			detalleServicios.setId(Integer.parseInt(ob[0].toString()));
			detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
			detalleServicios.setDescripcion(ob[2].toString());
			detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
			detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
			detalleServicios.getVenta().setId(Integer.parseInt(ob[6].toString()));
			detalleServicios.setIva(ob[7].toString());
			detalleServicios.getFuncionario().setId(Integer.parseInt(ob[8].toString()));
			detalleServicios.getFuncionario().getPersona().setNombre(ob[9].toString());
			detalleServicios.getFuncionario().getPersona().setApellido(ob[10].toString());
			detalleServicios.setMontoIva(Double.parseDouble(ob[11].toString()));
			detalleServicios.setObs(ob[12].toString());
			detalleServicio.add(detalleServicios);
			
		}
		System.out.println("lista detalle Servicio: "+detalleServicio.size());
		return detalleServicio;
	}

	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/prueba")
	public void pruebaGuardarDetalle(@RequestBody List<DetalleServicios> entity) throws ParseException{
		detalleServicioRepository.saveAll(entity);
	}
	@RequestMapping(method=RequestMethod.GET, value="/rankingProductoVenta/{fechaI}/{fechaF}/{order}/{limite}")
	public ResponseEntity<?> rankingProductoVenta(@PathVariable String fechaI, @PathVariable String fechaF,@PathVariable String order, @PathVariable int limite) throws ParseException{
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		Date fecI=formater.parse(fechaI);
		Date fecF=formater.parse(fechaF);
		Date fechaFi = sumarDia(fecF, 24);
		List<Object[]> lista = new ArrayList<>();
		if(order.equals("1")) {
			List<Object[]> c=entityRepository.rankingProductoVentaCantidad(fecI, fechaFi, limite);
			if(c.size() < 1) {
				return new ResponseEntity<>(new CustomerErrorType("NO EXISTE EL PRODUCTO!!"), HttpStatus.CONFLICT);
			} else {
				lista = c;
			}
		} else if(order.equals("2")) {
			List<Object[]> c=entityRepository.rankingProductoVentaPrecio(fecI, fechaFi, limite);
		if(c.size() < 1) {
			return new ResponseEntity<>(new CustomerErrorType("NO EXISTE EL PRODUCTO!!"), HttpStatus.CONFLICT);
		} else {
			lista = c;
		}
		} else if(order.equals("3")) {
			List<Object[]> c=entityRepository.rankingProductoVentaUtilidad(fecI, fechaFi, limite);
		if(c.size() < 1) {
			return new ResponseEntity<>(new CustomerErrorType("NO EXISTE EL PRODUCTO!!"), HttpStatus.CONFLICT);
		} else {
			lista = c;
		}
		}

		return new ResponseEntity<List<Object[]>>(lista, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value="/resumenVentaServcioRangoFecha/{fechaInicio}/{fechaFin}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenServicioRangoFecha(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaInicio, @PathVariable String fechaFin) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI, fechaFi;
			fecI = FechaUtil.setFechaHoraInicial(fechaInicio);
			fechaFi = FechaUtil.setFechaHoraFinal(fechaFin);
			List<Object []> obb =detalleServicioRepository.getResumenVentaServicioRagoFechaDetallado(fecI, fechaFi);
			List<DetalleServicios> det=new ArrayList<>();

			for(Object[] ob: obb) {
				DetalleServicios detalleServicios = new  DetalleServicios();
				detalleServicios.setId(Integer.parseInt(ob[0].toString()));
				detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
				detalleServicios.setDescripcion(ob[2].toString());
				detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
				detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
				detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
				detalleServicios.getVenta().setId(Integer.parseInt(ob[6].toString()));
				detalleServicios.setIva(ob[7].toString());
				detalleServicios.getFuncionario().setId(Integer.parseInt(ob[8].toString()));
				detalleServicios.getFuncionario().getPersona().setNombre(ob[9].toString());
				detalleServicios.getFuncionario().getPersona().setApellido(ob[10].toString());
				detalleServicios.setMontoIva(Double.parseDouble(ob[11].toString()));
				detalleServicios.setObs(ob[12].toString());
				det.add(detalleServicios);
			}
			Object [][] objeto=detalleServicioRepository.getResumenVentaServicioRagoFecha(fecI, fechaFi);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fechaFi);
			map.put("totalCosto", 0.0);
			map.put("totalVenta", 0.0);
			map.put("totalUtilidad", 0.0);
			//map.put("proveedor", proveedor.getPersona().getNombre()+ " "+ proveedor.getPersona().getApellido());
			Compra com= new  Compra();
			report = new Reporte();
			report.reportPDFDescarga(det, map, "ReporteVentaServicioRango", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value="/resumenVentaServcioRangoFecha/{fechaInicio}/{fechaFin}/{idFunc}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenServicioRangoFechaPorFuncionario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable String fechaInicio, @PathVariable String fechaFin,  @PathVariable int idFunc) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Funcionario f = funcionarioRepository.getIdFuncionario(idFunc);
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI, fechaFi;
			fecI = FechaUtil.setFechaHoraInicial(fechaInicio);
			fechaFi = FechaUtil.setFechaHoraFinal(fechaFin);
			List<Object []> obb =detalleServicioRepository.getResumenVentaServicioRagoFechaDetalladoPorFuncionario(fecI, fechaFi, idFunc);
			List<DetalleServicios> det=new ArrayList<>();

			for(Object[] ob: obb) {
				DetalleServicios detalleServicios = new  DetalleServicios();
				detalleServicios.setId(Integer.parseInt(ob[0].toString()));
				detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
				detalleServicios.setDescripcion(ob[2].toString());
				detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
				detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
				detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
				detalleServicios.getVenta().setId(Integer.parseInt(ob[6].toString()));
				detalleServicios.setIva(ob[7].toString());
				detalleServicios.getFuncionario().setId(Integer.parseInt(ob[8].toString()));
				detalleServicios.getFuncionario().getPersona().setNombre(ob[9].toString());
				detalleServicios.getFuncionario().getPersona().setApellido(ob[10].toString());
				detalleServicios.setMontoIva(Double.parseDouble(ob[11].toString()));
				detalleServicios.setObs(ob[12].toString());
				det.add(detalleServicios);
			}
			Object [][] objeto=detalleServicioRepository.getResumenVentaServicioRagoFecha(fecI, fechaFi);
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("funcionarioS", ""+f.getPersona().getNombre()+" "+f.getPersona().getApellido());
			map.put("desde", fecI);
			map.put("hasta", fechaFi);
			map.put("totalCosto", 0.0);
			map.put("totalVenta", 0.0);
			map.put("totalUtilidad", 0.0);
			//map.put("proveedor", proveedor.getPersona().getNombre()+ " "+ proveedor.getPersona().getApellido());
			Compra com= new  Compra();
			report = new Reporte();
			report.reportPDFDescarga(det, map, "ReporteVentaServicioRangoPorFuncionario", response);
			//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	
}