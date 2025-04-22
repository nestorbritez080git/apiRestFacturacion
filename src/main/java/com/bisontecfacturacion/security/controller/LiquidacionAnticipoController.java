package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.LiquidacionAnticipo;
import com.bisontecfacturacion.security.model.LiquidacionAnticipoDetalle;
import com.bisontecfacturacion.security.model.LiquidacionServicioDetalle;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AnticipoRepository;
import com.bisontecfacturacion.security.repository.LiquidacionAnticipoDetalleRepository;
import com.bisontecfacturacion.security.repository.LiquidacionAnticipoRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@Transactional
@RequestMapping("liquidacionAnticipo")
public class LiquidacionAnticipoController {
	@Autowired
	private LiquidacionAnticipoRepository entityRepository;
	
	@Autowired
	private LiquidacionAnticipoDetalleRepository detalleRepository;
	
	@Autowired
	private AnticipoRepository anticipoRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private OrgRepository orgRepository;
	private Reporte report;

	
	@RequestMapping(method=RequestMethod.GET)
	public List<LiquidacionAnticipo> get(){
		return listar(entityRepository.findAll());
	}
	@RequestMapping(method=RequestMethod.GET, value = "/todos/agrupado")
	public List<LiquidacionAnticipo> getAllAgrupado(){
		List<LiquidacionAnticipo> listRetorn= new ArrayList<LiquidacionAnticipo>();
		List<Object[]> list= entityRepository.getAllAgrupadoPorFuncionario();
//		SUM(c.total) as total, fun.id, per.nombre, per.apellido, per.cedula
		for (Object[] cue: list) {
			LiquidacionAnticipo d= new LiquidacionAnticipo();
			d.setTotal(Double.parseDouble(cue[0].toString()));
			d.getFuncionarioLiquidacion().setId(Integer.parseInt(cue[1].toString()));
			d.getFuncionarioLiquidacion().getPersona().setNombre(cue[2].toString()+ " "+cue[3].toString());
			d.getFuncionarioLiquidacion().getPersona().setCedula(cue[4].toString());
			listRetorn.add(d);
		}
		return listRetorn;
	}
	@RequestMapping(method=RequestMethod.GET,value = "/{id}")
	public LiquidacionAnticipo getLiquidacionId(@PathVariable int id){
		return cargar(entityRepository.getOne(id));

	}
	@RequestMapping(method=RequestMethod.GET,value = "/funcionario/{id}")
	public List<LiquidacionAnticipo> getLiquidacionListaPorIdFuncionario(@PathVariable int id){
		
		List<LiquidacionAnticipo> list = entityRepository.getLiquidacionListaPorIdFuncionario(id);;
		List<LiquidacionAnticipo> cuentaCabecera=new ArrayList<>();
		for(LiquidacionAnticipo c: list) {
			LiquidacionAnticipo liq = new LiquidacionAnticipo();
			liq.setId(c.getId());
			liq.setFecha(c.getFecha());
			liq.setTotal(c.getTotal());
			liq.getFuncionarioLiquidacion().setId(c.getFuncionarioLiquidacion().getId());
			liq.getFuncionarioLiquidacion().getPersona().setNombre(c.getFuncionarioLiquidacion().getPersona().getNombre()+ " "+c.getFuncionarioLiquidacion().getPersona().getApellido());
			liq.getFuncionarioLiquidacion().getPersona().setCedula(c.getFuncionarioLiquidacion().getPersona().getCedula());
			liq.getFuncionarioRegistro().setId(c.getFuncionarioRegistro().getId());
			liq.getFuncionarioRegistro().getPersona().setNombre(c.getFuncionarioRegistro().getPersona().getNombre()+ " "+c.getFuncionarioRegistro().getPersona().getApellido());
			liq.getFuncionarioRegistro().getPersona().setCedula(c.getFuncionarioRegistro().getPersona().getCedula());
			liq.setResumen(c.getResumen());
			
			cuentaCabecera.add(liq);
		}
		return cuentaCabecera;
	
	}
	
	@RequestMapping(method=RequestMethod.GET,value = "/buscarLiquidacionActivo/{id}")
	public LiquidacionAnticipo buscarAnticipoActivo(@PathVariable int id){
		return cargar(entityRepository.getOne(id));
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody LiquidacionAnticipo entity){
		try {
			if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getTotal()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE LA LIQUIDACION ANTICIPO DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			}else if(entity.getFuncionarioLiquidacion().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO LIQUIDACION NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getLiquidacionAnticipoDetalles().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DETALLE DE LA LIQUIDACION NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else {
				for (int i = 0; i < entity.getLiquidacionAnticipoDetalles().size(); i++) {
					LiquidacionAnticipoDetalle pro = entity.getLiquidacionAnticipoDetalles().get(i);
					System.out.println("ID ANT:   "+pro.getId());
					if(pro.getId()==0) {
						return new ResponseEntity<>(new CustomerErrorType("EN EL DETALLE DE LA LIQUIDACION NO SE HA ESPECIICADO EL ANTICIPO PARA LIQUIDAR"), HttpStatus.CONFLICT);
					}
					if(pro.getMonto()==0) {
						return new ResponseEntity<>(new CustomerErrorType("EN EL DETALLE DE LA LIQUIDACION EL MONTO DEL DETALLE DEBE SER MAYOR A CERO"), HttpStatus.CONFLICT);
					}
				}
				entityRepository.save(entity);
				LiquidacionAnticipo obRetorno= entityRepository.findTop1ByOrderByIdDesc();
				for(LiquidacionAnticipoDetalle detalleProducto: entity.getLiquidacionAnticipoDetalles()) {
					detalleProducto.getLiquidacionAnticipo().setId(obRetorno.getId());
					detalleProducto.getAnticipo().setId(detalleProducto.getId());
					detalleProducto.setId(0);
					detalleRepository.save(detalleProducto);
					anticipoRepository.liquidarEstadoAnticipo(detalleProducto.getAnticipo().getId(), true);
				}
				
			}
						
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	private LiquidacionAnticipo cargar(LiquidacionAnticipo lq){
		LiquidacionAnticipo d = new LiquidacionAnticipo();
		d.setId(lq.getId());
		d.getFuncionarioRegistro().setId(lq.getFuncionarioRegistro().getId());
		d.getFuncionarioRegistro().getPersona().setNombre(lq.getFuncionarioRegistro().getPersona().getNombre()+" "+lq.getFuncionarioRegistro().getPersona().getApellido());
		d.getFuncionarioLiquidacion().setId(lq.getFuncionarioLiquidacion().getId());
		d.getFuncionarioLiquidacion().getPersona().setNombre(lq.getFuncionarioLiquidacion().getPersona().getNombre()+" "+lq.getFuncionarioLiquidacion().getPersona().getApellido() );
		d.setFecha(lq.getFecha());
		d.setResumen(lq.getResumen());
		d.setTotal(lq.getTotal());
	return d;
}
	private List<LiquidacionAnticipo> listar(List<LiquidacionAnticipo> ob){
		List<LiquidacionAnticipo> listRetorno= new ArrayList<LiquidacionAnticipo>();
		for(LiquidacionAnticipo lq: ob) {
			LiquidacionAnticipo d = new LiquidacionAnticipo();
			d.setId(lq.getId());
			d.getFuncionarioRegistro().setId(lq.getFuncionarioRegistro().getId());
			d.getFuncionarioRegistro().getPersona().setNombre(lq.getFuncionarioRegistro().getPersona().getNombre()+" "+lq.getFuncionarioRegistro().getPersona().getApellido());
			d.getFuncionarioLiquidacion().setId(lq.getFuncionarioLiquidacion().getId());
			d.getFuncionarioLiquidacion().getPersona().setNombre(lq.getFuncionarioLiquidacion().getPersona().getNombre()+" "+lq.getFuncionarioLiquidacion().getPersona().getApellido() );
			d.setFecha(lq.getFecha());
			d.setResumen(lq.getResumen());
			d.setTotal(lq.getTotal());
			listRetorno.add(d);
		}
		return listRetorno;
	}
	
	
	
	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdfLiquidacion(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		LiquidacionAnticipo lq= new LiquidacionAnticipo(); 
		lq=entityRepository.getOne(id);
		
		List<LiquidacionAnticipo> listado= new ArrayList<LiquidacionAnticipo>();
		listado.add(lq);
		System.out.println(" alista size:  "+lq.getLiquidacionAnticipoDetalles().size());
		for (int i = 0; i < lq.getLiquidacionAnticipoDetalles().size(); i++) {
			System.out.println(lq.getLiquidacionAnticipoDetalles().get(i).getMonto()+" monto mmona ");
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
		
				report = new Reporte();
				report.reportPDFDescarga(listado, map, "ReporteLiquidacionAnticipoPdf", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	
}
