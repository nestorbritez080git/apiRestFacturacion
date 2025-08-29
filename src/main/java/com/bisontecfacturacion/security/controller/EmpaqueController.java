package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.ResumenEmpaqueDetalle;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.NumerosALetras;
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
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
import com.bisontecfacturacion.security.model.EmpaqueDetalle;
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
import com.bisontecfacturacion.security.repository.EmpaqueCabeceraRepository;
import com.bisontecfacturacion.security.repository.EmpaqueDetalleRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PresupuestoRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAnticipoRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
import com.itextpdf.text.pdf.codec.Base64.InputStream;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("empaque")
public class EmpaqueController {
	private static Formatter ft;
	private Reporte report;
	
	@Autowired
	private EmpaqueCabeceraRepository entityRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaRepository;
	
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@Autowired
	private VentaRepository ventaRepository;
	
	@Autowired
	private VentaController ventaServiceController;
	
	@Autowired
	private PresupuestoController presupuestoServiceController;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	@Autowired
	private EmpaqueDetalleRepository entityDetalleRepository;
	@Autowired
	private PresupuestoRepository presupuestoRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private OrgRepository orgRepository;


	private List<EmpaqueCabecera>listar(List<EmpaqueCabecera> obj){
		List<EmpaqueCabecera> listaRetorno=new ArrayList<>();
		for(EmpaqueCabecera p:obj){
			EmpaqueCabecera pre=new EmpaqueCabecera();
			pre.setId(p.getId());
			pre.setFechaRegistro(p.getFechaRegistro());
			pre.setFechaEntrega(p.getFechaEntrega());
			pre.setTotal(p.getTotal());
			pre.setTotalLetras(p.getTotalLetras());
			pre.setTotalDevolucion(p.getTotalDevolucion());
			pre.setFuncionarioEmpaque(p.getFuncionarioEmpaque());
			pre.setFuncionarioRegistro(p.getFuncionarioRegistro());
			pre.setZona(p.getZona());
			pre.setItemsPedido(p.getItemsPedido());
			pre.setEstado(p.getEstado());
			listaRetorno.add(pre);
		}
		return listaRetorno;
	}


	@RequestMapping(method=RequestMethod.GET, value="/tipo/{filtro}")
	public List<EmpaqueCabecera> getAlls(@PathVariable int filtro){
		List<EmpaqueCabecera> lisRetorno= new ArrayList<EmpaqueCabecera>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getEmpaqueAll());}
		if(filtro==2) { lisRetorno= listar(entityRepository.getEmpaqueAbierto());}
		if(filtro==3) { lisRetorno= listar(entityRepository.getEmpaqueCerrado());}

		return lisRetorno;

	}
	@RequestMapping(method=RequestMethod.POST, value="/tipo/{filtro}")
	public List<EmpaqueCabecera> getAllsPorDescripcion(@RequestBody String descripcion, @PathVariable int filtro){
		List<EmpaqueCabecera> lisRetorno= new ArrayList<EmpaqueCabecera>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getEmpaqueAllDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==2) { lisRetorno= listar(entityRepository.getEmpaqueAbiertoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==3) { lisRetorno= listar(entityRepository.getEmpaqueCerradoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		return lisRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value="/presupuestoIdZona/{id}")
	public List<Presupuesto> getPresupuestoPorZonaPreVenta( @PathVariable int id){
		return listarPresupuestoEmpaque(listarPresupuestoEmpaque(presupuestoRepository.getPresupuestoPreVentaPorZona(id)));
	}
	private List<Presupuesto>listarPresupuestoEmpaque(List<Presupuesto> obj){
		List<Presupuesto> res=new ArrayList<>();
		for(Presupuesto p:obj){
			Presupuesto pre=new Presupuesto();
			pre.setId(p.getId());
			pre.getFuncionario().getPersona().setNombre(p.getFuncionario().getPersona().getNombre()+" "+p.getFuncionario().getPersona().getApellido());
			pre.getFuncionario().getPersona().setCedula(p.getFuncionario().getPersona().getCedula());
			pre.getCliente().getPersona().setNombre(p.getCliente().getPersona().getNombre()+" "+ p.getCliente().getPersona().getApellido());
			pre.getCliente().getPersona().setCedula(p.getCliente().getPersona().getCedula());
			pre.setTotal(p.getTotal());
			pre.setFecha(p.getFecha());
			pre.setHora(p.getHora());
			pre.setEstado(p.getEstado());
			pre.setZona(p.getZona());
			pre.setDetallePresupuestoProducto(p.getDetallePresupuestoProducto());
			pre.setDetallePresupuestoServicio(p.getDetallePresupuestoServicio());
			res.add(pre);
		}
		return res;
	}



	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/finalizarEmpaque/{id}")
	public ResponseEntity<?> finalizarEmpaque(@PathVariable int id){
		try {
			return new ResponseEntity<>(new CustomerErrorType("Empaque Generado"),HttpStatus.CREATED);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody EmpaqueCabecera entity){
		try {
			System.out.println("empaque total: "+ entity.getTotal());
			if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
			} else if(entity.getFuncionarioEmpaque().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getZona().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA ZONA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getEmpaqueDetalle().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getObs() != null){
				entity.setObs(entity.getObs().toUpperCase());
			} else if(entity.getTotalLetras() == null || entity.getTotalLetras().trim().equals("")){
				entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(entity.getTotal()));
				System.out.println("nuep total letras: "+ entity.getTotalLetras());
			} else if(entity.getFechaRegistro() == null){
				entity.setFechaRegistro(LocalDateTime.now());
			} else if(entity.getFechaEntrega() == null) {//falta agregar eliminar ddettllae si se eliminaa
				entity.setFechaEntrega(new Date());
			} else{
				for(int ind=0; ind < entity.getEmpaqueDetalle().size(); ind++) {
					EmpaqueDetalle pro = entity.getEmpaqueDetalle().get(ind);
					pro.setItemsPedidoDetalle(pro.getPresupuesto().getDetallePresupuestoProducto().size()+ pro.getPresupuesto().getDetallePresupuestoServicio().size());

					if(pro.getPresupuesto().getId() == 0) {
						return new ResponseEntity<>(new CustomerErrorType("EL PRESUPUESTO NO SE LLEGÓ A CARGAR CORRECTAMENTE ITEM N°: "+(ind+1)+""), HttpStatus.CONFLICT);
					}else if(pro.getPresupuesto().getDetallePresupuestoProducto().size() <= 0 ){
						return new ResponseEntity<>(new CustomerErrorType("EL PRESUPUESTO DEBE TENER AL MENOS UN DETALLE ITEM N°: "+(ind+1)+""), HttpStatus.CONFLICT);
					}
				}
			}
			if(entity.getId() !=0) {
				System.out.println("entro edit lote empaque");
				if(entity.getEstado().equals("CERRADO")) {

				}
				int idpent=entity.getId();
				for(int i=0; i < entity.getEmpaqueDetalle().size(); i++) {
					EmpaqueDetalle detEmpaque = entity.getEmpaqueDetalle().get(i);
					detEmpaque.getEmpaqueCabecera().setId(idpent);
					for(int idetPresu=0; idetPresu < detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size(); idetPresu++) {
						DetallePresupuestoProducto detPresu = detEmpaque.getPresupuesto().getDetallePresupuestoProducto().get(idetPresu);
						System.out.println("DETALLE: "+detPresu.getDescripcion()+"CAN: "+detPresu.getCantidad());
					}
					detEmpaque.setSubtotalPresupuesto(detEmpaque.getPresupuesto().getTotal());
					detEmpaque.setItemsPedidoDetalle(detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size());
					entityDetalleRepository.save(detEmpaque);
				}
				entityRepository.save(entity);
				return new ResponseEntity<>(new CustomerErrorType("Empaque Actualizado"),HttpStatus.CREATED);
			}else {
				System.out.println("entro nuevo lote empaque");
				entityRepository.save(entity);
				EmpaqueCabecera idpent = entityRepository.findTop1ByOrderByIdDesc();
				for(int i=0; i < entity.getEmpaqueDetalle().size(); i++) {
					EmpaqueDetalle detEmpaque = entity.getEmpaqueDetalle().get(i);
					detEmpaque.getEmpaqueCabecera().setId(idpent.getId());
					for(int idetPresu=0; idetPresu < detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size(); idetPresu++) {
						DetallePresupuestoProducto detPresu = detEmpaque.getPresupuesto().getDetallePresupuestoProducto().get(idetPresu);
						System.out.println("DETALLE: "+detPresu.getDescripcion()+"CAN: "+detPresu.getCantidad());
					}
					detEmpaque.setSubtotalPresupuesto(detEmpaque.getPresupuesto().getTotal());
					detEmpaque.setItemsPedidoDetalle(detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size());
					entityDetalleRepository.save(detEmpaque);
				}
				entityRepository.save(entity);
				return new ResponseEntity<>(new CustomerErrorType("Empaque Generado"),HttpStatus.CREATED);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}

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
			a.setMontoLiquidado(Double.parseDouble(ob[16].toString()));
			listaRetrono.add(a);
		}
		return listaRetrono;
	}

	@RequestMapping(method=RequestMethod.GET, value="/consultar/{id}")
	public EmpaqueCabecera getEmpaque(@PathVariable int id){
		EmpaqueCabecera v=entityRepository.getEmpaque(id);
		//EmpaqueCabecera pre=new EmpaqueCabecera();
		return v;
	}

	@RequestMapping(method=RequestMethod.POST, value="/eliminarEmpaqueDetalle")
	public ResponseEntity<?> eliminarDetalleProducto(@RequestBody List<EmpaqueDetalle> detalle){
		try {
			System.out.println(detalle.size()+ " lista size");
			if(detalle.size()!=-1) {
				System.out.println("con listado lista");
				for (EmpaqueDetalle de : detalle) {		
					System.out.println("entroo eliminar detalle for empaque");
					//this.actualizarProductoBasePresupuestoDescontar(de.getProducto().getId(), de.getCantidad());
					entityDetalleRepository.deleteById(de.getId());
				}
			}else {
				return new ResponseEntity<>(new CustomerErrorType("NO HAY LISTA PARA ELIMINAR"), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueCabecera ep= entityRepository.getEmpaque(id);
		List<Object[]> lisResumen = entityRepository.getResumenProductosPorEmpaque(id);
		List<ResumenEmpaqueDetalle> lisResumenRetorno =  new ArrayList<ResumenEmpaqueDetalle>();

		for (int i = 0; i < lisResumen.size(); i++) {
			ResumenEmpaqueDetalle d = new ResumenEmpaqueDetalle();
			d.setIdProducto(Integer.parseInt(lisResumen.get(i)[0].toString()));
			d.setDescripcion(lisResumen.get(i)[1].toString());
			d.setMarca(lisResumen.get(i)[2].toString());
			d.setCantidad(Double.parseDouble(lisResumen.get(i)[3].toString()));
			lisResumenRetorno.add(d);
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			//Datos del empaque
			map.put("funcionarioEmpaque", "[ "+ep.getFuncionarioRegistro().getPersona().getCedula()+" ] "+ep.getFuncionarioRegistro().getPersona().getNombre()+ " "+ep.getFuncionarioRegistro().getPersona().getApellido());

			map.put("fechaEmpaque", ep.getFechaRegistro());
			map.put("zonaEmpaque", "[ "+ep.getZona().getId()+" ] "+ep.getZona().getDescripcion());
			map.put("itemsEmpaque", ep.getItemsPedido());
			map.put("totalEmpaque", ep.getTotal());
			map.put("obsEmpaque", ep.getObs()+"");
			map.put("letrasEmpaque", ep.getTotalLetras()+"");
			map.put("idEmpaque", ep.getId());



			report = new Reporte();
			report.reportPDFDescarga(lisResumenRetorno, map, "ReporteEmpaqueResumenPdf", response);

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET, value="/finalizar/{idEmpaque}/{idApertura}/{idTipoOperacion}/{numeroTerminal}")
	public ResponseEntity<?> finalizarEmpaque(@PathVariable int idEmpaque, @PathVariable int idApertura, @PathVariable int idTipoOperacion, @PathVariable int numeroTerminal){
		try {
			EmpaqueCabecera emp = entityRepository.findById(idEmpaque).orElse(null);
			AperturaCaja apertura =  aperturaCajaRepository.findById(idApertura).orElse(null);
			if(emp==null) {
				return  new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRÓ EMPAQUE CON EL NÚMERO: "+idEmpaque), HttpStatus.CONFLICT);
			}
			if(apertura==null) {
				return  new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRÓ APERTURA CAJA CON EL NÚMERO: "+idApertura), HttpStatus.CONFLICT);
			}
			
			List<EmpaqueDetalle> detalleEmpaque = emp.getEmpaqueDetalle();
			for (EmpaqueDetalle det: detalleEmpaque) {
				Presupuesto p= det.getPresupuesto();
				Venta preVenta= new Venta();
				preVenta.setCliente(p.getCliente());
				preVenta.setFuncionarioR(emp.getFuncionarioRegistro());
				preVenta.setFuncionarioV(p.getFuncionario());
				preVenta.setFuncionario(emp.getFuncionarioRegistro());
				preVenta.setFecha(new Date());
				preVenta.getDocumento().setId(1);//TICKET DOCUMENTO;
				preVenta.setZona(p.getZona());
				preVenta.setTotal(p.getTotal());
				preVenta.setTotalDescuento(0.0);
				preVenta.setTotalIva(p.getTotalIva());
				preVenta.setTotalIvaCinco(p.getTotalIvaCinco());
				preVenta.setTotalIvaDies(p.getTotalIvaDies());
				preVenta.setTotalExcenta(p.getTotalExcenta());
				preVenta.setTotalLetra(p.getTotalLetra());
				preVenta.setEstado("FACTURADO");
				preVenta.setTipo("CONTADO");
				preVenta.getDocumento().setDescripcion("false");
				
		        //preVenta = ventaRepository.save(preVenta);

				List<DetalleProducto> dpRetorno = new ArrayList<DetalleProducto>();
				List<DetalleServicios> dsRetorno = new ArrayList<DetalleServicios>();

				for (DetallePresupuestoProducto detPresupuesto : p.getDetallePresupuestoProducto()) {
					DetalleProducto dp = new DetalleProducto();
		            dp.setVenta(preVenta);
					dp.setProducto(detPresupuesto.getProducto());
					dp.setCantidad(detPresupuesto.getCantidad());
					dp.setDescripcion(detPresupuesto.getDescripcion());
					dp.setCosto(detPresupuesto.getProducto().getPrecioCosto());
					dp.setCostoPromedio(detPresupuesto.getProducto().getPrecioCosto());
					dp.setSubTotal(detPresupuesto.getSubTotal());
					dp.setDescuento(detPresupuesto.getDescuento());
					dp.setIsBalanza(detPresupuesto.getIsBalanza());
					dp.setIva(detPresupuesto.getIva());
					dp.setPrecio(detPresupuesto.getPrecio());
					dp.setMontoIva(detPresupuesto.getMontoIva());
					dp.setTipoPrecio(detPresupuesto.getTipoPrecio());
					dpRetorno.add(dp);
					//detalleVentaRepository.save(dp);
				}

				// Detalle de servicios
				for (DetallePresupuestoServicio ds : p.getDetallePresupuestoServicio()) {
					DetalleServicios dps = new DetalleServicios();
					//dps.setVenta(venta);
					dps.setServicio(ds.getServicio());
					dps.setCantidad(ds.getCantidad());
					dps.setPrecio(ds.getPrecio());
					dps.setDescripcion(ds.getDescripcion());
					dps.setIva(ds.getIva());
					dps.setMontoIva(ds.getMontoIva());
					dps.setSubTotal(ds.getSubTotal());
					dps.setFuncionario(ds.getFuncionario());
					dps.setObs(ds.getObs());
					
					dsRetorno.add(dps);
				}
				preVenta.setDetalleProducto(dpRetorno);
				preVenta.setDetalleServicio(dsRetorno);
				
				ResponseEntity<?> response = ventaServiceController.guardar(preVenta, numeroTerminal);
				Map<String, String> map = (Map<String, String>) response.getBody(); 
				if(map!=null) {
					if(Integer.parseInt(map.get("id").toString())>0) {
						System.out.println("ACA ENTRO POR QUE GNERRO LA VENTA Y DEVUELVE ID VENTA");
						det.setVentaReferencia(Integer.parseInt(map.get("id")));
					}else {
						return  new  ResponseEntity<>(new CustomerErrorType("HUBO UN ERRO AL GENERAR VENTA POR EL EMPAQUE DETALLE NÚMERO: "+det.getId()), HttpStatus.CONFLICT);
						//System.out.println("NO ENTRO ACA POR QUE NO SE GENERO LA VENTA Y SE TIENE ANULAR LA FINALIZACION DEL EMPQUE");
					}
				}else {
					return  new  ResponseEntity<>(new CustomerErrorType("HUBO UN ERRO AL GENERAR VENTA POR EL EMPAQUE DETALLE NÚMERO: "+det.getId()), HttpStatus.CONFLICT);

				}
				p.setEstado("CERRADO");
				presupuestoServiceController.guardar(p);
				
				OperacionCaja op= new OperacionCaja();
				op.getAperturaCaja().setId(idApertura);//numero apertura caja
				op.getConcepto().setId(1);//venta contado
				op.getTipoOperacion().setId(idTipoOperacion);
				op.setFecha(new Date());
				op.setMonto(p.getTotal());
				op.setEfectivo(p.getTotal());
				Concepto c = new Concepto();
				c= conceptoRepository.getOne(1);
				op.setMotivo(c.getDescripcion()+ " Ref.: "+det.getVentaReferencia()+", POR EMPAQUE NÚMERO: "+emp.getId());
				//se creo un objeto venta por cada empaqueDetralle ya en ese detalle viene los pedido opresupuesto
				op.setTipo("ENTRADA");
				if (idTipoOperacion == 1) {
					aperturaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(), op.getMonto());
				}
				
				if (idTipoOperacion == 2) {
					aperturaRepository.findByActualizarAperturaSaldoCheque(op.getAperturaCaja().getId(), op.getMonto());
				}
				
				if (idTipoOperacion == 3) {
					aperturaRepository.findByActualizarAperturaSaldoTarjeta(op.getAperturaCaja().getId(), op.getMonto());
				}
				
				
				OperacionCaja opRetorno = operacionCajaRepository.save(op);
				ventaRepository.findByActualizarVentaOperacion(det.getVentaReferencia(),opRetorno.getId());

				entityRepository.cambiarEstadoEmpaque(emp.getId(), "CERRADO");
				presupuestoRepository.cambiarEstado(p.getId(), "CERRADO");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return  new  ResponseEntity<String>(HttpStatus.CREATED);

	}
	
	
	@RequestMapping(value="/descargarResumenDetalladoPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarDetalleEmpaquePdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueCabecera ep= entityRepository.getEmpaque(id);
		

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			//Datos del empaque
			/*map.put("funcionarioEmpaque", "[ "+ep.getFuncionarioRegistro().getPersona().getCedula()+" ] "+ep.getFuncionarioRegistro().getPersona().getNombre()+ " "+ep.getFuncionarioRegistro().getPersona().getApellido());

			map.put("fechaEmpaque", ep.getFechaRegistro());
			map.put("zonaEmpaque", "[ "+ep.getZona().getId()+" ] "+ep.getZona().getDescripcion());
			map.put("itemsEmpaque", ep.getItemsPedido());
			map.put("totalEmpaque", ep.getTotal());
			map.put("obsEmpaque", ep.getObs()+"");
			map.put("letrasEmpaque", ep.getTotalLetras()+"");
			map.put("idEmpaque", ep.getId());
			*/


			report = new Reporte();
			report.reportPDFDescarga(ep.getEmpaqueDetalle(), map, "ReporteEmpaqueDetalleResumenPdf", response);

		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

}
