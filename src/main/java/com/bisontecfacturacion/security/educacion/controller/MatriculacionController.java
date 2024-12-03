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
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;
import com.bisontecfacturacion.security.educacion.model.Procedencia;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.model.Turno;
import com.bisontecfacturacion.security.educacion.repository.CarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.MatriculacionDetalleRepository;
import com.bisontecfacturacion.security.educacion.repository.MatriculacionRepository;
import com.bisontecfacturacion.security.educacion.repository.ProcedenciaRepository;
import com.bisontecfacturacion.security.educacion.repository.TipoCarreraRepository;
import com.bisontecfacturacion.security.educacion.repository.TurnoRepository;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.hoteleria.repository.CategoriaHabitacionesRepository;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Servicio;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;
@RestController
@RequestMapping("matriculacion")
public class MatriculacionController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private MatriculacionRepository entityRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private MatriculacionDetalleRepository detalleRepository;
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private TurnoRepository turnoRepository;
	@Autowired
	private ProcedenciaRepository procedenciaRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Matriculacion> getAllCarrera(){
		 return listSer(entityRepository.getAll());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/turno")
	public List<Turno> getAllTurno(){
		 return turnoRepository.findAll();
	}
	@RequestMapping(method = RequestMethod.GET, value = "/procedencia")
	public List<Procedencia> getAllProcedencia(){
		 return procedenciaRepository.findAll();
	}
	@RequestMapping(method = RequestMethod.GET, value = "/generar/detalle/{montoCuota}/{aumento}/{duracion}/{fechaIncio}")
	public List<MatriculacionDetalle> generarDetalleMAtricula(@PathVariable Double montoCuota, @PathVariable Double aumento, @PathVariable int duracion, @PathVariable  String fechaIncio){
		List<MatriculacionDetalle> listaTorno=new ArrayList<MatriculacionDetalle>();
		 try {
			 	SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	            // Parsear la fecha de inicio
	            Date fecInicio = dFormat.parse(fechaIncio);
	            // Crear calendario para manejar las fechas
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(fecInicio);  // Establecer la fecha de inicio
	            // Variable para determinar si el primer año ya pasó
	            boolean primerAñoCompletado = false;
	            // Iterar mes a mes para la duración en meses
	            int ultimoAñoAumentado=0;
	            int añoInicial = cal.get(Calendar.YEAR);
	            String fechaDia1="", fechaDia5=""; 
	            for (int i = 0; i < duracion; ) {
	            	 cal.add(Calendar.MONTH, 1);
	                // Imprimir el número de la cuota (mes) y el monto de la cuota
	                String fechaMes = dFormat.format(cal.getTime());
	                if (cal.get(Calendar.MONTH) == Calendar.JANUARY) {
	                    System.out.println("Sin registro de cuota en enero.");
	                    // Termina el método o salta el procesamiento
	                }else {
	                	System.out.print("Cuota #" + (i + 1) + " - Mes: " + fechaMes + " - ");
		                // Si ya pasó un año completo, aplicar el aumento (al iniciar el segundo año)
		                int añoActual = cal.get(Calendar.YEAR);
		                if (añoActual > añoInicial && añoActual > ultimoAñoAumentado) { 
		                    // Si el año actual es mayor al año inicial y mayor al último año donde se aplicó el aumento
		                    montoCuota += aumento;  // Aplica el aumento
		                    ultimoAñoAumentado = añoActual;      // Actualiza el año del último aumento
		                    System.out.println("Cuota con aumento: " + montoCuota);
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                String fechaDia11 = dFormat.format(cal.getTime()); // Fecha del día 1

			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                String fechaDia55 = dFormat.format(cal.getTime()); // Fecha del día 5
			                MatriculacionDetalle det = new MatriculacionDetalle();
			                det.setId(0);
			                det.setMontoCuota(montoCuota);
			                det.setNumeroCuota(i+1);
			                det.setMontoAumento(aumento);
			                det.setFechaRangoIncioVencimiento(fechaDia11);
			                det.setFechaRangoFinVencimiento(fechaDia55);
			                det.setSubtotal(montoCuota);
			                listaTorno.add(det);
			                // Imprimir las cuotas con sus fechas
			                System.out.println("Cuota: " + montoCuota);
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia11 + "  Fecha día 5 de la cuota: " + fechaDia55);
			  //			  // Imprimir cuota con aumento
		                } else {
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                String fechaDia11 = dFormat.format(cal.getTime()); // Fecha del día 1

			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                String fechaDia55 = dFormat.format(cal.getTime()); // Fecha del día 5
			                MatriculacionDetalle det = new MatriculacionDetalle();
			                det.setId(0);
			                det.setMontoCuota(montoCuota);
			                det.setNumeroCuota(i+1);
			                det.setMontoAumento(aumento);
			                det.setFechaRangoIncioVencimiento(fechaDia11);
			                det.setFechaRangoFinVencimiento(fechaDia55);
			                det.setSubtotal(montoCuota);
			                listaTorno.add(det);
			                // Imprimir las cuotas con sus fechas
			                System.out.println("Cuota: " + montoCuota);
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia11 + "  Fecha día 5 de la cuota: " + fechaDia55);
			  // Imprimir cuota sin aumento
		                }

		                // Avanzar al siguiente mes
		                i=i+1;
	                }
	                             System.out.println();
	               
	                // Imprimir el número de cuota, mes y monto
	                
	            }
	            /*
	            for (int i = 0; i < duracion; i++) {
	           
	                // Imprimir el número de la cuota (mes) y el monto de la cuota
	                String fechaMes = dFormat.format(cal.getTime());
	                if (cal.get(Calendar.MONTH) == Calendar.JANUARY) {
	                    System.out.println("Sin registro de cuota en enero.");
	                    // Termina el método o salta el procesamiento
	                }else {

		                // Si ya pasó un año completo, aplicar el aumento (al iniciar el segundo año)
		                int añoActual = cal.get(Calendar.YEAR);
		                if (añoActual > añoInicial && añoActual > ultimoAñoAumentado) { 
		                	MatriculacionDetalle det = new MatriculacionDetalle();
		                    // Si el año actual es mayor al año inicial y mayor al último año donde se aplicó el aumento
		                	montoCuota += aumento;  // Aplica el aumento
			               
		                    ultimoAñoAumentado = añoActual;      // Actualiza el año del último aument
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                fechaDia1 = dFormat.format(cal.getTime()); // Fecha del día 1
			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                fechaDia5 = dFormat.format(cal.getTime()); // Fecha del día 5
			                // Imprimir las cuotas con sus fechas
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia1 + "  Fecha día 5 de la cuota: " + fechaDia5);
			               
			                // Imprimir cuota con aumento
		                } else {
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                fechaDia1 = dFormat.format(cal.getTime()); // Fecha del día 1
			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                fechaDia5 = dFormat.format(cal.getTime()); // Fecha del día 5
			                // Imprimir las cuotas con sus fechas
			                System.out.println("Cuota: " + montoCuota);
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia1 + "  Fecha día 5 de la cuota: " + fechaDia5);
			                MatriculacionDetalle det = new MatriculacionDetalle();
			                det.setNumeroCuota(i+1);
			                det.setMontoCuota(montoCuota);
			                det.setMontoAumento(aumento);
			                det.setFechaRangoIncioVencimiento(FechaUtil.convertirFechaStringADateUtil(fechaDia1));
			                det.setFechaRangoFinVencimiento(FechaUtil.convertirFechaStringADateUtil(fechaDia5));
			                det.setSubtotal(montoCuota);
			                listaTorno.add(det);
			                // Imprimir cuota sin aumento
		                }

		                // Avanzar al siguiente mes
		                
	                }
	                             System.out.println();
	                cal.add(Calendar.MONTH, 1);
	               
	                // Imprimir el número de cuota, mes y monto
	                
	            }*/

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		 return listaTorno;
	}
	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Matriculacion entity){
		try {
			System.out.println(entity.getFechaInicio());
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getAlumno().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL ALUMNO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getCarrera().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("LA CARRERA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getTurno().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR EL TURNO!"), HttpStatus.CONFLICT);
			} else if(entity.getProcedencia().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("SE DEBE SELECCIONAR LA PROCEDENCIA!"), HttpStatus.CONFLICT);
			} else if(entity.getFechaInicio()==null) {
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA INICIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getMatriculacionDetalles().size()<=0) {
				return new ResponseEntity<>(new CustomerErrorType("LA MATRICUALACION DETALLE DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			} else  {
				double montoCuotaTotal=0;
				for(MatriculacionDetalle det: entity.getMatriculacionDetalles()) {
					montoCuotaTotal = montoCuotaTotal + det.getMontoCuota();
				}
				entity.setMontoCuotaTotal(montoCuotaTotal);
				
				
				if(entity.getId()!=0) {
					entityRepository.save(entity);
					for(MatriculacionDetalle det: entity.getMatriculacionDetalles()) {
						montoCuotaTotal = montoCuotaTotal + det.getMontoCuota();
						det.getMatriculacion().setId(entity.getId());
						detalleRepository.save(det);
					}
					return  new  ResponseEntity<String>(HttpStatus.CREATED);
				}else {
					entityRepository.save(entity);
					Matriculacion id = entityRepository.getUltimaMatriculacion();
					for(MatriculacionDetalle det: entity.getMatriculacionDetalles()) {
						montoCuotaTotal = montoCuotaTotal + det.getMontoCuota();
						det.getMatriculacion().setId(id.getId());
						detalleRepository.save(det);
					}
					entityRepository.save(entity);
					return  new  ResponseEntity<String>(HttpStatus.CREATED);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		Matriculacion m = entityRepository.getOne(id);
		if(m !=null) {
			for (MatriculacionDetalle de : m.getMatriculacionDetalles()) {
				System.out.println("ID ELIMINADO : "+de.getId());
				detalleRepository.deleteById(de.getId());
			}
			entityRepository.deleteById(id);
		}else {
			
		}
	}
//	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
//	public List<Carrera> consultarPorDescripcion(@PathVariable String descripcion){
//		List<Carrera> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
//		return listSer(objeto);
//	}
	@RequestMapping(method=RequestMethod.GET, value="/buscarId/{id}")
	public Matriculacion consultarPorId(@PathVariable int id){
		return entityRepository.findById(id).orElse(null);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/matriculacionDetalle/buscarId/{id}")
	public List<MatriculacionDetalle> consultarMatriculacionDetallePorIdCabecera(@PathVariable int id){
		return detalleRepository.consultarMatriculacionDetallePorIdCabecera(id);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion")
	public List<Matriculacion> consultarPorDescripcion(@RequestBody String descripcion){
		List<Matriculacion> objeto=entityRepository.getBuscarPorFiltro("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
		return listSer(objeto);
	
	}
	
	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarCobroMatriculacionPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Matriculacion pre= new Matriculacion(); 
		pre=entityRepository.getOne(id);
		pre.setMatriculacionDetalles(detalleRepository.consultarMatriculacionDetallePorIdCabeceraOrderNumeroCuota(pre.getId()));
		
		List<Matriculacion> listado= new ArrayList<Matriculacion>();
		listado.add(pre);
		try {
		
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", "Reporte Cobranza Matricula");
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
		
				report = new Reporte();
				report.reportPDFDescarga(listado, map, "ReporteMatriculacionPdf", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	public List<Matriculacion> listSer(List<Matriculacion> objeto) {
		List<Matriculacion> servi=new ArrayList<>();
		for(Matriculacion ob:objeto){
			Matriculacion s=new Matriculacion();
			s.setId(ob.getId());
			s.getFuncionario().setId(ob.getFuncionario().getId());
		    s.getFuncionario().getPersona().setNombre(ob.getFuncionario().getPersona().getNombre());
		    s.getFuncionario().getPersona().setApellido(ob.getFuncionario().getPersona().getApellido());
		    s.getAlumno().setId(ob.getAlumno().getId());
		    s.getAlumno().getPersona().setNombre(ob.getAlumno().getPersona().getNombre());
		    s.getAlumno().getPersona().setApellido(ob.getAlumno().getPersona().getApellido());
		    s.getAlumno().getPersona().setCedula(ob.getAlumno().getPersona().getCedula());
		    s.getAlumno().getPersona().setTelefono(ob.getAlumno().getPersona().getTelefono());
		    s.getAlumno().getPersona().setDireccion(ob.getAlumno().getPersona().getDireccion());
		    s.getTurno().setDescripcion(ob.getTurno().getDescripcion());
		    s.getProcedencia().setDescripcion(ob.getProcedencia().getDescripcion());
		    s.setReferenciaProcedencia(ob.getReferenciaProcedencia());
		    s.setMontoMatricula(ob.getMontoMatricula());
		    s.setDuracion(ob.getDuracion());
		    s.setMontoCuotaTotal(ob.getMontoCuotaTotal());
		    s.getCarrera().setDescripcion(ob.getCarrera().getDescripcion());
		    s.getCarrera().setId(ob.getCarrera().getId());
			servi.add(s);
		}
		return servi;
	}
}
