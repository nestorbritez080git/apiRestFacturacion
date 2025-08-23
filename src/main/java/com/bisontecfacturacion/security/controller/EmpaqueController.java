package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;

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
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.AnticipoReferenciaCajaChica;
import com.bisontecfacturacion.security.model.AnticipoReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.AnulacionesAnticipo;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.CajaChica;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
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
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PresupuestoRepository;
import com.bisontecfacturacion.security.repository.TransferenciaAnticipoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("empaque")
public class EmpaqueController {
	private static Formatter ft;
	private Reporte report;
	@Autowired
	private EmpaqueCabeceraRepository entityRepository;
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
			pre.setFechaArreglo(p.getFechaArreglo());
			pre.setTotal(p.getTotal());
			pre.setTotalLetras(p.getTotalLetras());
			pre.setTotalDevolucion(p.getTotalDevolucion());
			pre.setFuncionarioEmpaque(p.getFuncionarioEmpaque());
			pre.setFuncionarioRegistro(p.getFuncionarioRegistro());
			pre.setZona(p.getZona());
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
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Anticipo entity){
		try {
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
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
	
	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueCabecera pre= new EmpaqueCabecera(); 
		pre=entityRepository.getOne(id);
		List<AnticipoReferenciaCajaChica> listadoCajaChica= new ArrayList<AnticipoReferenciaCajaChica>();
		List<AnticipoReferenciaOperacionCaja> listadoOperacionCaja= new ArrayList<AnticipoReferenciaOperacionCaja>();

		
		try {
		
							//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	
}
