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
import com.bisontecfacturacion.security.educacion.model.CobrosCuota;
import com.bisontecfacturacion.security.educacion.model.CobrosCuotaDetalle;
import com.bisontecfacturacion.security.educacion.model.CobrosMatriculacion;
import com.bisontecfacturacion.security.educacion.model.ConceptoMatriculacion;
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;
import com.bisontecfacturacion.security.educacion.model.Procedencia;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.model.Turno;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.CobrosCuotaDetalleRepository;
import com.bisontecfacturacion.security.educacion.repository.CobrosCuotaRepository;
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
@RequestMapping("cobrosCuota")
public class CobrosCuotaController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private CobrosCuotaRepository entityRepository;
	@Autowired
	private CobrosCuotaDetalleRepository detalleRepository;
	@Autowired
	private MatriculacionDetalleRepository matriculacionDetalleRepository;
	@Autowired
	private OrgRepository orgRepository;
	
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
	public List<CobrosCuota> getAll(){
		 return listSer(entityRepository.getAll());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/procedencia")
	public List<Procedencia> getAllProcedencia(){
		 return procedenciaRepository.findAll();
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/{numeroTerminal}")
	public  ResponseEntity<?> guardar(@RequestBody CobrosCuota entity, @PathVariable int numeroTerminal){
		try {
			OperacionCaja op=entity.getOperacionCaja();
			op.getConcepto().setId(18);
			op.getAperturaCaja().setId(entity.getOperacionCaja().getAperturaCaja().getId());
			op.getTipoOperacion().setId(entity.getOperacionCaja().getTipoOperacion().getId());
			op.setTipo("ENTRADA");
			operacionCajaRepository.save(op);
			OperacionCaja opRetorno= new OperacionCaja();
			opRetorno= operacionCajaRepository.findTop1ByOrderByIdDesc();
			entity.getOperacionCaja().setId(opRetorno.getId());
			if(entity.getFuncionario().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getAlumno().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL ALUMNO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getOperacionCaja().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("LA OPERACIÓN CAJA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getCobrosCuotaDetalles().size()<=0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE GENERAR EL DETALLE DE PAGO DE CUOTA!"), HttpStatus.CONFLICT);
			}
			entityRepository.save(entity);
			CobrosCuota cob=new CobrosCuota();
			cob= entityRepository.findTop1ByOrderByIdDesc();
			for (CobrosCuotaDetalle cc: entity.getCobrosCuotaDetalles()) {
				cc.getCobrosCuota().setId(cob.getId());
				System.out.println("IMP ANR: "+cc.getImporteAnterior());
				matriculacionDetalleRepository.findByActualizaActualizarImporteDetalleMatriculacion(cc.getMatriculacionDetalle().getId(), cc.getImporte());
				detalleRepository.save(cc);
			}
			Concepto c = new Concepto();
			c = conceptoRepository.findById(18).get();
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
		CobrosCuota m = entityRepository.getOne(id);
		if(m !=null) {	
			entityRepository.deleteById(id);
		}else {
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public CobrosCuota consultarPorId(@PathVariable int id){
		return entityRepository.getOne(id);
	}sdefgdfsag
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion")
	public List<CobrosCuota> consultarPorDescripcion(@RequestBody String descripcion){
		List<CobrosCuota> objeto=entityRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	
	}
	
	
	@RequestMapping(value="/cobrosCuotaPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarCuotaPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		CobrosCuota pre= new CobrosCuota(); 
		pre=entityRepository.getOne(id);
		System.out.println(pre.getMatriculacion().getTurno().getDescripcion());
		List<CobrosCuota> listado= new ArrayList<CobrosCuota>();
		listado.add(pre);
		try {	
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", "Reporte Cobros Cuota");
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		
				report = new Reporte();
				report.reportPDFDescarga(listado, map, "ReporteCobroCuotaPdf", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");		
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	public List<CobrosCuota> listSer(List<CobrosCuota> objeto) {
		List<CobrosCuota> servi=new ArrayList<>();
		for(CobrosCuota ob:objeto){
			CobrosCuota s=new CobrosCuota();
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
		  
			servi.add(s);
		}
		return servi;
	}

}
