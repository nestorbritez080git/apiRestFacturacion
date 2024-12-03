package com.bisontecfacturacion.security.educacion.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
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
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.CobrosMatriculacion;
import com.bisontecfacturacion.security.educacion.model.ConceptoMatriculacion;
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;
import com.bisontecfacturacion.security.educacion.model.Procedencia;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.model.Turno;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.CobrosMatriculacionRepository;
import com.bisontecfacturacion.security.educacion.repository.ConceptoMatriculacionRepository;
import com.bisontecfacturacion.security.educacion.repository.MatriculacionDetalleRepository;
import com.bisontecfacturacion.security.educacion.repository.MatriculacionRepository;
import com.bisontecfacturacion.security.educacion.repository.ProcedenciaRepository;
import com.bisontecfacturacion.security.educacion.repository.TipoCarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.TurnoRepository;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
@RestController
@RequestMapping("cobrosMatriculacion")
public class CobrosMatriculacionController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private CobrosMatriculacionRepository entityRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private MatriculacionDetalleRepository detalleRepository;
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private ConceptoMatriculacionRepository conceptoMatriculacionRepository;
	@Autowired
	private ProcedenciaRepository procedenciaRepository;
	
	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;
	@Autowired
	private OperacionCajaRepository operacionCajaRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<CobrosMatriculacion> getAll(){
		 return listSer(entityRepository.getAll());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/conceptoMatriculacion")
	public List<ConceptoMatriculacion> getAllConceptoMatriculacion(){
		 return conceptoMatriculacionRepository.findAll();
	}
	@RequestMapping(method = RequestMethod.GET, value = "/procedencia")
	public List<Procedencia> getAllProcedencia(){
		 return procedenciaRepository.findAll();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/{numeroTerminal}")
	public  ResponseEntity<?> guardar(@RequestBody CobrosMatriculacion entity, @PathVariable int numeroTerminal){
		try {
			OperacionCaja op=entity.getOperacionCaja();
			op.getConcepto().setId(17);
			op.getAperturaCaja().setId(entity.getOperacionCaja().getAperturaCaja().getId());
			op.getTipoOperacion().setId(entity.getOperacionCaja().getTipoOperacion().getId());
			op.setTipo("ENTRADA");
			operacionCajaRepository.save(op);
			OperacionCaja opRetorno= new OperacionCaja();
			opRetorno= operacionCajaRepository.findTop1ByOrderByIdDesc();
			entity.getOperacionCaja().setId(opRetorno.getId());
			if(entity.getFuncionario().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getMatriculacion().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR LA MATRICULA!"), HttpStatus.CONFLICT);
			}else if(entity.getAlumno().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL ALUMNO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getOperacionCaja().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("LA OPERACIÓN CAJA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}
			entityRepository.save(entity);
			CobrosMatriculacion cob=new CobrosMatriculacion();
			cob= entityRepository.findTop1ByOrderByIdDesc();
			
			Concepto c = new Concepto();
			c = conceptoRepository.findById(17).get();
			opRetorno.setMotivo(c.getDescripcion() + " REF.: " + cob.getId());
			
			if (opRetorno.getTipoOperacion().getId() == 1) {
				aperturaCajaRepository.findByActualizarAperturaSaldo(opRetorno.getAperturaCaja().getId(), opRetorno.getMonto());
			}
			if (opRetorno.getTipoOperacion().getId() == 2) {
				aperturaCajaRepository.findByActualizarAperturaSaldoCheque(opRetorno.getAperturaCaja().getId(), opRetorno.getMonto());
			}
			if (opRetorno.getTipoOperacion().getId() == 3) {
				aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(opRetorno.getAperturaCaja().getId(), opRetorno.getMonto());
			}
			operacionCajaRepository.save(opRetorno);
			return  new  ResponseEntity<String>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		CobrosMatriculacion m = entityRepository.getOne(id);
		if(m !=null) {	
			entityRepository.deleteById(id);
		}else {
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public CobrosMatriculacion consultarPorId(@PathVariable int id){
		return entityRepository.findById(id).orElse(null);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion")
	public List<CobrosMatriculacion> consultarPorDescripcion(@RequestBody String descripcion){
		List<CobrosMatriculacion> objeto=entityRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion/conceptoMatriculaciones")
	public List<ConceptoMatriculacion> consultarPorDescripcionConcepto(@RequestBody String descripcion){
		List<ConceptoMatriculacion> objeto=conceptoMatriculacionRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listConcepto(objeto);
	
	}
	
	@RequestMapping(value="/cobrosMatriculacionPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarMatriculacionPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		CobrosMatriculacion pre= new CobrosMatriculacion(); 
		pre=entityRepository.getOne(id);
		
		List<CobrosMatriculacion> listado= new ArrayList<CobrosMatriculacion>();
		listado.add(pre);
		try {
		
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", "Reporte Cobros Matricula");
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		
				report = new Reporte();
				report.reportPDFDescarga(listado, map, "ReporteCobroMatriculacionPdf", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	public List<CobrosMatriculacion> listSer(List<CobrosMatriculacion> objeto) {
		List<CobrosMatriculacion> servi=new ArrayList<>();
		for(CobrosMatriculacion ob:objeto){
			CobrosMatriculacion s=new CobrosMatriculacion();
			s.setId(ob.getId());
			s.getFuncionario().setId(ob.getFuncionario().getId());
		    s.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre());
		    s.getFuncionario().getPersona().setApellido(ob.getFuncionario().getPersona().getApellido());
		    s.getAlumno().setId(ob.getAlumno().getId());
		    s.getAlumno().getPersona().setNombre(ob.getAlumno().getPersona().getNombre());
		    s.getAlumno().getPersona().setApellido(ob.getAlumno().getPersona().getApellido());
		    s.getAlumno().getPersona().setCedula(ob.getAlumno().getPersona().getCedula());
		    s.setImporte(ob.getImporte());
		    s.setFechaRegistro(ob.getFechaRegistro());
		    s.setFechaCobro(ob.getFechaCobro());
		    s.getMatriculacion().getCarrera().setDescripcion(ob.getMatriculacion().getCarrera().getDescripcion());
		    s.getConceptoMatriculacion().setDescripcion(ob.getConceptoMatriculacion().getDescripcion());
		   
			servi.add(s);
		}
		return servi;
	}
	
	public List<ConceptoMatriculacion> listConcepto(List<ConceptoMatriculacion> objeto) {
		List<ConceptoMatriculacion> servi=new ArrayList<>();
		for(ConceptoMatriculacion ob: objeto){
			ConceptoMatriculacion s=new ConceptoMatriculacion();
			s.setId(ob.getId());
			s.setDescripcion(ob.getDescripcion());
		    s.setMonto(ob.getMonto());
			servi.add(s);
		}
		return servi;
	}
}
