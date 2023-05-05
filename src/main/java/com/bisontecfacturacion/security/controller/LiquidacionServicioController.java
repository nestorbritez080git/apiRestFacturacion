package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.LiquidacionServicio;
import com.bisontecfacturacion.security.model.LiquidacionServicioDetalle;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.SalarioFuncionario;
import com.bisontecfacturacion.security.model.SalarioFuncionarioDetalle;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.LiquidacionServicioDetalleRepository;
import com.bisontecfacturacion.security.repository.LiquidacionServicioRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@Transactional
@RequestMapping("liquidacionServicio")
public class LiquidacionServicioController {
	@Autowired
	private LiquidacionServicioRepository entityRepository;
	
	@Autowired
	private LiquidacionServicioDetalleRepository detalleRepository;
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private OrgRepository orgRepository;
	private Reporte report;
	
	
	
	
	
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<LiquidacionServicio> get(){
		return listar(entityRepository.findAll());
	}
	@RequestMapping(method=RequestMethod.GET,value = "/{id}")
	public LiquidacionServicio getLiquidacionId(@PathVariable int id){
		return cargar(entityRepository.getOne(id));
	}
	
	@RequestMapping(method=RequestMethod.GET,value = "/detalle/{id}")
	public List<LiquidacionServicioDetalle> getLiquidacionDetallePorIdCabecera(@PathVariable int id){
		return listarDetalleLiquidacion(entityRepository.getDetalleLiquidacionPorIdCabecera(id));
	}
	
	@RequestMapping(method=RequestMethod.GET,value = "/detalleServicio/{fechaInicio}/{fechaFin}/{id}")
	public List<DetalleServicios> getDetalleServicioPorFuncionario(@PathVariable String fechaInicio, @PathVariable String fechaFin, @PathVariable int id){
		return listarDetalleServicios(entityRepository.getDetalleServicioFacturadoPorIdFuncionario(FechaUtil.fechaHoraInicial(fechaInicio), FechaUtil.fechaHoraFinal(fechaFin), id));
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody LiquidacionServicio entity){
		try {
			if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioServicio().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO SERVICIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getLiquidacionServicioDetalle().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DETALLE DE LA LIQUIDACION NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else {
				for (int i = 0; i < entity.getLiquidacionServicioDetalle().size(); i++) {
					LiquidacionServicioDetalle pro = entity.getLiquidacionServicioDetalle().get(i);
					if(pro.getCliente().getId()==0) {
						return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO ESTA ESPECIFICADO EN EL DETALLE N°: "+(i++)+""), HttpStatus.CONFLICT);
					}
					if(pro.getDetalleServicios().getId()==0) {
						return new ResponseEntity<>(new CustomerErrorType("EL DETALLE DE VENTA DEL SERVICIO NO ESTA ESPECIFICADO EN EL DETALLE N°: "+(i++)+""), HttpStatus.CONFLICT);
					}
				
					
				}
				List<LiquidacionServicioDetalle> detalle = entity.getLiquidacionServicioDetalle();
				entity.setLiquidacionServicioDetalle(null);
				entityRepository.save(entity);
				for(LiquidacionServicioDetalle o : detalle) {
					o.getLiquidacionServicio().setId(entity.getId());
				}
				detalleRepository.saveAll(detalle);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deletePlanillaDetallePorIdCabece(@PathVariable int id){
		Optional<LiquidacionServicio> planilla = entityRepository.findById(id);
		if(planilla.isPresent()) {
			detalleRepository.deleteAll(planilla.get().getLiquidacionServicioDetalle());
			entityRepository.delete(planilla.get());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reporteLiquidacion/{id}")
	public  ResponseEntity<?> getReporteSalarioFuncionario(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id) throws IOException{
		List<LiquidacionServicioDetalle> lis =listarDetalleLiquidacion(entityRepository.getDetalleLiquidacionPorIdCabecera(id));
		
		if(lis.size()>0) {
			
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			Org org = orgRepository.findById(1).get();
			LiquidacionServicio s =	new LiquidacionServicio();
			s=cargar(entityRepository.getOne(id));

			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("funcionarioServicio", ""+s.getFuncionarioServicio().getPersona().getNombre());
			map.put("inicio", ""+FechaUtil.convertirFechaUtilAString(s.getFechaInicio()));
			map.put("fin", ""+FechaUtil.convertirFechaUtilAString(s.getFechaFin()));
			report = new Reporte();
			report.reportPDFDescarga(lis, map, "ReporteLiquidacionServicio", response);

			return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("NO HAY LISTA PARA MOSTRAR"), HttpStatus.CONFLICT);
		}



	}
	
	private List<LiquidacionServicio> listar(List<LiquidacionServicio> ob){
		List<LiquidacionServicio> listRetorno= new ArrayList<LiquidacionServicio>();
		for(LiquidacionServicio lq: ob) {
			LiquidacionServicio d = new LiquidacionServicio();
			d.setId(lq.getId());
			d.getFuncionarioRegistro().setId(lq.getFuncionarioRegistro().getId());
			d.getFuncionarioRegistro().getPersona().setNombre(lq.getFuncionarioRegistro().getPersona().getNombre()+" "+lq.getFuncionarioRegistro().getPersona().getApellido());
			d.getFuncionarioServicio().setId(lq.getFuncionarioServicio().getId());
			d.getFuncionarioServicio().getPersona().setNombre(lq.getFuncionarioServicio().getPersona().getNombre()+" "+lq.getFuncionarioServicio().getPersona().getApellido() );
			d.setFechaInicio(lq.getFechaInicio());
			d.setFechaFin(lq.getFechaFin());
			d.setResumen(lq.getResumen());
			d.setTotalServicio(lq.getTotalServicio());
			d.setTotalComision(lq.getTotalComision());
			listRetorno.add(d);
			
		}
		
		return listRetorno;
	}
	private LiquidacionServicio cargar(LiquidacionServicio lq){
			LiquidacionServicio d = new LiquidacionServicio();
			d.setId(lq.getId());
			d.getFuncionarioRegistro().setId(lq.getFuncionarioRegistro().getId());
			d.getFuncionarioRegistro().getPersona().setNombre(lq.getFuncionarioRegistro().getPersona().getNombre()+" "+lq.getFuncionarioRegistro().getPersona().getApellido());
			d.getFuncionarioServicio().setId(lq.getFuncionarioServicio().getId());
			d.getFuncionarioServicio().getPersona().setNombre(lq.getFuncionarioServicio().getPersona().getNombre()+" "+lq.getFuncionarioServicio().getPersona().getApellido() );
			d.setFechaInicio(lq.getFechaInicio());
			d.setFechaFin(lq.getFechaFin());
			d.setResumen(lq.getResumen());
			d.setTotalServicio(lq.getTotalServicio());
			d.setTotalComision(lq.getTotalComision());
			
			
		
		return d;
	}
	private List<DetalleServicios> listarDetalleServicios(List<Object[]> ob) {
		List<DetalleServicios> dt= new ArrayList<>();
		for(Object[] c: ob) {
			DetalleServicios detSer = new DetalleServicios();
			detSer.setId(Integer.parseInt(c[0].toString()));
			detSer.getServicio().setDescripcion(c[1].toString());
			detSer.setSubTotal(Double.parseDouble(c[2].toString()));
			detSer.getServicio().setPorcentaje(Integer.parseInt(c[3].toString()));
			detSer.setPrecio(Double.parseDouble(c[4].toString()));
			detSer.getVenta().getCliente().getPersona().setNombre(c[5].toString());
			detSer.getVenta().getCliente().setId(Integer.parseInt(c[6].toString()));
			dt.add(detSer);
		}
		return  dt;
	}
	private List<LiquidacionServicioDetalle> listarDetalleLiquidacion(List<Object[]> ob) {
		List<LiquidacionServicioDetalle> dt= new ArrayList<>();
		for(Object[] c: ob) {
			LiquidacionServicioDetalle detSer = new LiquidacionServicioDetalle();
			detSer.setId(Integer.parseInt(c[0].toString()));
			detSer.setComision(Double.parseDouble(c[1].toString()));
			detSer.setMonto(Double.parseDouble(c[2].toString()));
			detSer.setPorcentaje(Integer.parseInt(c[3].toString()));
			detSer.getLiquidacionServicio().setId(Integer.parseInt(c[4].toString()));
			detSer.getCliente().setId(Integer.parseInt(c[5].toString()));
			detSer.getCliente().getPersona().setNombre(c[6].toString());
			detSer.getDetalleServicios().setDescripcion(c[7].toString());
			dt.add(detSer);
		}
		return  dt;
	}
	
}
