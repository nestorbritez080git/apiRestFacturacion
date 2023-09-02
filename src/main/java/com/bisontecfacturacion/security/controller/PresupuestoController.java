package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.PresupuestoDetalleProductoRepository;
import com.bisontecfacturacion.security.repository.PresupuestoDetalleServicioRepository;
import com.bisontecfacturacion.security.repository.PresupuestoRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional
@RestController
@RequestMapping("presupuesto")
public class PresupuestoController {

	@Autowired
	private PresupuestoRepository entityRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private PresupuestoDetalleProductoRepository detalleProductoRepository;

	@Autowired
	private ImpresoraRepository impresoraRepository;
	@Autowired
	private ReporteConfigRepository reporteConfigRepository;

	@Autowired
	private PresupuestoDetalleServicioRepository detalleServicioRepository;
	
	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;
	
	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	


	
	@RequestMapping(method=RequestMethod.GET, value="/{fecha}")
	public List<Presupuesto> getAlls(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Presupuesto> objeto=entityRepository.getPresupuesto(ano, mes, dia);
		List<Presupuesto> presupuesto=new ArrayList<>();
		for(Presupuesto ob:objeto){
			Presupuesto pre=new Presupuesto();
			pre.setId(ob.getId());
			pre.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre()+", "+ob.getFuncionario().getPersona().getApellido());
			pre.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+", "+ ob.getCliente().getPersona().getApellido());
			pre.setTotal(ob.getTotal());
			pre.setFecha(ob.getFecha());
			pre.setHora(ob.getHora());
			pre.setEstado(ob.getEstado());
			presupuesto.add(pre);
		}
		return presupuesto;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/nroDocumento")
	public Map<String, String> getPresupuestoNro(){
		Presupuesto v=entityRepository.findTop1ByOrderByIdDesc();
		Map<String, String> number = new HashMap<>();
		number.put("nroDocumento", v.getNroDocumento());
		return number;
	}


	@RequestMapping(method=RequestMethod.GET, value="/presupuestoId/{id}")
	public Presupuesto getPresupuestoId(@PathVariable int id){
		Presupuesto v=entityRepository.findById(id).orElse(null);
		Presupuesto pre=new Presupuesto();
		pre.setId(v.getId());
		pre.setNroDocumento(v.getNroDocumento());
		pre.setTotal(v.getTotal());
		pre.getFuncionario().setId(v.getFuncionario().getId());
		pre.getCliente().setId(v.getCliente().getId());
		pre.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre());
		pre.getCliente().getPersona().setApellido(v.getCliente().getPersona().getApellido());
		pre.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre() +", "+ v.getFuncionario().getPersona().getApellido());
		pre.setEstado(v.getEstado());
		return pre;
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleProducto/{id}")
	public List<DetallePresupuestoProducto> getDetalleProductoId(@PathVariable int id){
		List<Object[]> objeto=entityRepository.listaDetallePresupuestoProducto(id);
		List<DetallePresupuestoProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoProducto detalleProductos=new DetallePresupuestoProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getPresupuesto().setId(Integer.parseInt(ob[7].toString()));

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

			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleServicio/{id}")
	public List<DetallePresupuestoServicio> getAllIdServicio(@PathVariable int id){
		List<DetallePresupuestoServicio> detalleServicio=new ArrayList<>();
		try {
			List<Object[]> objeto=entityRepository.listaDetallePresupuestoServicio(id);
			for(Object[] ob:objeto){
				DetallePresupuestoServicio detalleServicios=new DetallePresupuestoServicio();
				detalleServicios.getServicio().setId(Integer.parseInt(ob[0].toString()));
				detalleServicios.setDescripcion(ob[1].toString());
				detalleServicios.setCantidad(Double.parseDouble(ob[2].toString()));
				detalleServicios.setPrecio(Double.parseDouble(ob[3].toString()));
				detalleServicios.setSubTotal(Double.parseDouble(ob[4].toString()));
				detalleServicios.setIva(ob[5].toString());
				detalleServicios.setMontoIva(Double.parseDouble(ob[6].toString()));
				detalleServicios.getFuncionario().setId(Integer.parseInt(ob[7].toString()));
				detalleServicios.getFuncionario().getPersona().setNombre(ob[8].toString());
				detalleServicios.getFuncionario().getPersona().setApellido(ob[9].toString());
				detalleServicios.setId(Integer.parseInt(ob[10].toString()));
				detalleServicios.setObs(ob[11].toString());
				
				detalleServicio.add(detalleServicios);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return detalleServicio;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/actualizarEstado/{id}")
	public void updateEstado(@PathVariable int id){
		try {
			entityRepository.findByActualizaEstado(id, "CERRADO");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}


	private static String padF(int numero, int size) {
		Formatter ft= new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}

	private String generarCodigo() {
		Presupuesto p=entityRepository.findTop1ByOrderByIdDesc();
		if (p !=null) {
			return padF(p.getId(), 12);
		} else {
			return padF(1, 12);
		}

	}

	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody Presupuesto entity){
		try {
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
			} else if(entity.getCliente().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDetallePresupuestoProducto().size() == 0 && entity.getDetallePresupuestoServicio().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else 
			{
				for(int ind=0; ind < entity.getDetallePresupuestoProducto().size(); ind++) {
					DetallePresupuestoProducto pro = entity.getDetallePresupuestoProducto().get(ind);
					if(pro.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE PRODUCTO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}
				for(int ind=0; ind < entity.getDetallePresupuestoServicio().size(); ind++) {
					DetallePresupuestoServicio ser=entity.getDetallePresupuestoServicio().get(ind);
					if(ser.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE SERVICIO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE SERVICIO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE SERVICIO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}


				if(entity.getId() !=0) {
					entity.setHora(hora());
					entityRepository.save(entity);
					int idVent=entity.getId();
					if(entity.getDetallePresupuestoProducto().size()>0){

						for(DetallePresupuestoProducto detalleProducto: entity.getDetallePresupuestoProducto()) {
							detalleProducto.getPresupuesto().setId(idVent);
							detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							detalleProductoRepository.save(detalleProducto);	
						}


					}
					if(entity.getDetallePresupuestoServicio().size()>0){
						for(DetallePresupuestoServicio detalleServicio: entity.getDetallePresupuestoServicio()) {
							detalleServicio.getPresupuesto().setId(idVent);
							if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
							detalleServicioRepository.save(detalleServicio);
						}
					}
					///////////////////////////////////////
					//pdfPrintPresupuesto(entity.getId());

				}else {
					entity.setHora(hora());
					entity.setNroDocumento(generarCodigo());
					entityRepository.save(entity);
					Presupuesto id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					//eliminarDetallePorCabecera(entity.getId());
					if(entity.getDetallePresupuestoProducto().size()>0){
						//	actualizarLoteDocumento(entity.getDocumento().getId(), entity.getNroDocumento());
						for(DetallePresupuestoProducto detalleProducto: entity.getDetallePresupuestoProducto()) {
							detalleProducto.getPresupuesto().setId(idVent);
							detalleProducto.setTipoPrecio(validarPrecio(detalleProducto.getProducto().getId(), detalleProducto.getPrecio()));
							if(detalleProducto.getIva().equals("10 %")) {
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if(detalleProducto.getIva().equals("5 %")) {
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if(detalleProducto.getIva().equals("Exenta")) {
								detalleProducto.setMontoIva(0.0);
							}
							
							detalleProductoRepository.save(detalleProducto);
						
						}
					}

					if(entity.getDetallePresupuestoServicio().size()>0){
						for(DetallePresupuestoServicio detalleServicio: entity.getDetallePresupuestoServicio()) {
							detalleServicio.getPresupuesto().setId(idVent);
							if(detalleServicio.getIva().equals("10 %")) {
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("5 %")) {
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("Exenta")) {
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);	
						}

					}
					
				//pdfPrintPresupuesto(idVent);
				}
			}
			/*
		entityRepository.save(entity);
		this.estado=entity.getEstado();
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Presupuesto>(entity, HttpStatus.CREATED);
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	public String validarPrecio(int id, double precio) {
		Producto pro=productoRepository.findById(id).get();
		String op="";
		if (pro.getPrecioVenta_4() == precio) {
			op="P4";
		} 
		if (pro.getPrecioVenta_3() == precio) {
			op= "P3";
		}
		if (pro.getPrecioVenta_2() == precio) {
			op= "P2";
		}
		if (pro.getPrecioVenta_1() == precio) {
			op= "P1";
		}
		return op;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/pre/{numeroTerminal}")
	public void pdfPrintPresupuesto(@RequestBody Presupuesto pres, @PathVariable int numeroTerminal) {
		
		Reporte report = new Reporte();
		TerminalConfigImpresora t = new TerminalConfigImpresora();
		t= terminalRepository.consultarTerminal(numeroTerminal);
		if (t==null) {
			System.out.println("Se debe cargar numero terminal dentro de la base de datos");
		}else {
			ReporteConfig reportConfig = reporteConfigRepository.getOne(2);
			List<Presupuesto> venta = getListasss(pres.getId());
			Map<String, Object> map = new HashMap<>();
			
	
				if (t.getImpresora().equals("matricial")) {
				ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
				String urlReporte ="\\reporte\\"+reportConfig.getNombreSubReporte1()+".jasper";
				map.put("urlSubRepor", urlReporte);
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				try {
				report.reportPDFImprimir(venta, map, reportConfig.getNombreReporte(), t.getNombreImpresora());	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {System.out.println("Impresora ticket seleccionada");}
		}
	
	}
	public List<DetallePresupuestoServicio> getDetalleServ(int idVenta) {
		List<Object[]> objeto=detalleServicioRepository.listarDetallePresupuesto(idVenta);
		List<DetallePresupuestoServicio> detalleServicio=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoServicio detalleServicios=new DetallePresupuestoServicio();
			detalleServicios.setId(Integer.parseInt(ob[0].toString()));
			detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
			detalleServicios.setDescripcion(ob[2].toString());
			detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
			detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
			detalleServicios.getPresupuesto().setId(Integer.parseInt(ob[6].toString()));
			detalleServicios.setIva(ob[7].toString());
			detalleServicios.getFuncionario().setId(Integer.parseInt(ob[8].toString()));
			detalleServicios.setObs(ob[11].toString());
			detalleServicio.add(detalleServicios);
		}
		return detalleServicio;
	}

	public List<DetallePresupuestoProducto> getDetalleProducto(List<Object[]> objeto) {
		List<DetallePresupuestoProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoProducto detalleProductos=new DetallePresupuestoProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getPresupuesto().setId(Integer.parseInt(ob[7].toString()));

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

			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}
	public Presupuesto presupuestosss(int idPresupuesto) {
		Presupuesto pre = null;
		List<Object[]> v=entityRepository.getPresupuestoId(idPresupuesto);
		for(Object[] ob: v) {
			pre = new Presupuesto();
			pre.getCliente().getPersona().setNombre(ob[0].toString());
			pre.getCliente().getPersona().setCedula(ob[1].toString());
			if (ob[2].toString().equals("")) {pre.getCliente().getPersona().setDireccion("xxx");} else {pre.getCliente().getPersona().setDireccion(ob[2].toString());}
			pre.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[3].toString()));
			pre.setHora(ob[4].toString());
			pre.getFuncionario().getPersona().setNombre(ob[5].toString());

			pre.setTotalIvaCinco(Double.parseDouble(ob[6].toString()));
			pre.setTotalIvaDies(Double.parseDouble(ob[7].toString()));
			pre.setTotal(Double.parseDouble(ob[8].toString()));
			pre.setTotalLetra(ob[9].toString());
			pre.setNroDocumento(ob[10].toString());
			//pre.getDocumento().setDescripcion(ob[12].toString());
		}
		return pre;
	}

	public List<Presupuesto> getListasss(int idPresupuesto ) {


		Presupuesto presu = new Presupuesto();
		List<Presupuesto> lista = new ArrayList<>();
		List<DetallePresupuestoProducto> detProducto = new ArrayList<>();
		List<DetallePresupuestoServicio> detServicio = new ArrayList<>();


		presu = presupuestosss(idPresupuesto);
		detProducto = getDetalleProducto(detalleProductoRepository.lista(idPresupuesto));
		detServicio = getDetalleServ(idPresupuesto);


		for (int i = 0; i < 1; i++) {
			Presupuesto v = new Presupuesto();
			v.getCliente().getPersona().setNombre(presu.getCliente().getPersona().getNombre());
			v.getCliente().getPersona().setCedula(presu.getCliente().getPersona().getCedula());
			v.getCliente().getPersona().setDireccion(presu.getCliente().getPersona().getDireccion());
			v.setFecha(presu.getFecha());
			v.setHora(presu.getHora());
			v.getFuncionario().getPersona().setNombre(presu.getFuncionario().getPersona().getNombre());
			v.setTotalIvaCinco(presu.getTotalIvaCinco());
			v.setTotalIvaDies(presu.getTotalIvaDies());
			v.setTotal(presu.getTotal());
			v.setTotalLetra(presu.getTotalLetra());
			v.setNroDocumento(presu.getNroDocumento());
			//v.getDocumento().setDescripcion(venta.getDocumento().getDescripcion());


			v.setDetallePresupuestoProducto(detProducto);


			for(DetallePresupuestoServicio det: detServicio) {
				DetallePresupuestoProducto detalleProducto = new DetallePresupuestoProducto();
				detalleProducto.getProducto().setId(det.getServicio().getId());
				detalleProducto.setDescripcion("SER - "+det.getDescripcion());
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				System.out.println(""+det.getSubTotal());
				System.out.println(detalleProducto.getSubTotal());
				v.getDetallePresupuestoProducto().add(detalleProducto);
				detalleProducto.getSubTotal();
			}
			lista.add(v);
		}
		
		return lista;

	}
	

}
