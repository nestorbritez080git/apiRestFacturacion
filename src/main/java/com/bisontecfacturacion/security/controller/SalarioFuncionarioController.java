package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.bisontecfacturacion.security.auxiliar.AnticipoAuxiliar;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.SalarioFuncionario;
import com.bisontecfacturacion.security.model.SalarioFuncionarioDetalle;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PlanillaSalarioFuncionarioDetalleRepository;
import com.bisontecfacturacion.security.repository.SalarioFuncionarioDetalleRepository;
import com.bisontecfacturacion.security.repository.SalarioFuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("salarioFuncionario")
public class SalarioFuncionarioController {
	@Autowired
	private SalarioFuncionarioRepository entityRepository;
	
	@Autowired
	private SalarioFuncionarioDetalleRepository detalleRepository;
	@Autowired
	private PlanillaSalarioFuncionarioDetalleRepository detallePlanillaRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private OrgRepository orgRepository;
	
	private Reporte report;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public List<SalarioFuncionario> getAll(){
		List<SalarioFuncionario> liRetorno = new ArrayList<SalarioFuncionario>();
		List<SalarioFuncionario> li = entityRepository.findAll();
		SalarioFuncionario p = null;
		for (SalarioFuncionario ob: li) {
			/*
			p = new SalarioFuncionario();
			p.setId(ob.getId());
			p.getFuncionarioRegistro().setId(ob.getFuncionarioRegistro().getId());
			p.getFuncionarioRegistro().getPersona().setNombre(ob.getFuncionarioRegistro().getPersona().getNombre()+" "+ob.getFuncionarioRegistro().getPersona().getApellido());
			p.getFuncionarioAutorizado().setId(ob.getFuncionarioAutorizado().getId());
			p.getFuncionarioAutorizado().getPersona().setNombre(ob.getFuncionarioAutorizado().getPersona().getNombre()+" "+ob.getFuncionarioAutorizado().getPersona().getApellido());
			p.getPeriodo().setId(ob.getPeriodo().getId());
			p.getPeriodo().setDescripcion(ob.getPeriodo().getDescripcion());
			p.getPlanillaSalarioFuncionario().setId(ob.getPlanillaSalarioFuncionario().getId());
			p.setTotalSalario(ob.getTotalSalario());
			p.setTotalAnticipo(ob.getTotalAnticipo());
			p.setEstado(ob.isEstado());
			p.setFecha(ob.getFecha());
			*/
			liRetorno.add(p);
		}
		return liRetorno;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public  SalarioFuncionario getSalarioFuncionario(@PathVariable int id){	
		/*
		SalarioFuncionario v = entityRepository.findById(id).orElse(null);
		SalarioFuncionario ret = new SalarioFuncionario();
		ret=v;
		ret.setSalarioFuncionarioDetalles(null);
		ret.setPlanillaSalarioFuncionario(null);
		*/
		Object [][] obj = entityRepository.getSalarioFuncionarioPorId(id);
		SalarioFuncionario s = null;
		if (obj[0][0].toString() == null) {
			System.out.println("null");
		}else {
			/*
			s= new SalarioFuncionario();
			s.setId(Integer.parseInt(obj[0][0].toString()));
			s.getPeriodo().setId(Integer.parseInt(obj[0][1].toString()));
			s.getPeriodo().setDescripcion(obj[0][2].toString());
			s.getPlanillaSalarioFuncionario().setId(Integer.parseInt(obj[0][3].toString()));
			s.getPlanillaSalarioFuncionario().setPlanilla(obj[0][4].toString());
			s.getFuncionarioRegistro().setId(Integer.parseInt(obj[0][5].toString()));
			s.getFuncionarioRegistro().getPersona().setNombre(obj[0][6].toString()+" "+obj[0][7].toString());
			s.getFuncionarioAutorizado().setId(Integer.parseInt(obj[0][8].toString()));
			s.getFuncionarioAutorizado().getPersona().setNombre(obj[0][9].toString()+" "+obj[0][10].toString() );
			s.setTotalSalario(Double.parseDouble(obj[0][11].toString()));
			s.setTotalAnticipo(Double.parseDouble(obj[0][12].toString()));
			s.setTotalPagado(Double.parseDouble(obj[0][13].toString()));
			*/
		}
		
		
		return s;
	}
	@RequestMapping(method = RequestMethod.GET, value = "consultarDetalle/{id}/{idPeriodo}")
	public List<AnticipoAuxiliar> getDetallePorIdCabecera(@PathVariable int id, @PathVariable int idPeriodo){
		List<Object []> v = detalleRepository.listaPorIdCabecera(id);
		List<Object[]> anticipo = detallePlanillaRepository.listadoAnticipo(idPeriodo);

		List<AnticipoAuxiliar> ret = new ArrayList<AnticipoAuxiliar>();
		for(Object [] ob: v) {
			AnticipoAuxiliar p=new AnticipoAuxiliar();
			p.setIdSalario(Integer.parseInt(ob[0].toString()));
			p.setId(Integer.parseInt(ob[1].toString()));
			p.setIdCabecera((Integer.parseInt(ob[2].toString())));
			p.setFuncionario(ob[3].toString()+" "+ob[4].toString());
			p.setMontoSalario(Double.parseDouble(ob[5].toString()));
		    //p.setMontoAnticipo(Double.parseDouble(ob[6].toString()));
		    p.setMontoApagar(Double.parseDouble(ob[6].toString()));
		    
			ret.add(p);
		}
		for(AnticipoAuxiliar ant: ret ) {
			for(Object [] a: anticipo ) {
				if (ant.getId() == Integer.parseInt(a[0].toString())) {
					ant.setMontoAnticipo(ant.getMontoAnticipo()+Double.parseDouble(a[1].toString()));
				}
			} 
			System.out.println("REG: "+ant.getMontoSalario());
			System.out.println("REG: "+ant.getMontoAnticipo());
			System.out.println("REG: "+ant.getMontoApagar());
			
		}
		return ret;
		/*
		List<AnticipoAuxiliar> ret = new ArrayList<AnticipoAuxiliar>();
		for(Object [] o: v) {
			AnticipoAuxiliar p=new AnticipoAuxiliar();
		    // p.setId(Integer.parseInt(o[0].toString()));
		    p.setId(Integer.parseInt(o[1].toString()));
		    p.setFuncionario(o[2].toString()+ " "+o[3].toString());
		    
		    p.setMontoSalario(Double.parseDouble(o[4].toString()));
		    ret.add(p);
		}
		for(AnticipoAuxiliar ant: ret ) {
			for(Object [] a: anticipo ) {
				if (ant.getId() == Integer.parseInt(a[0].toString())) {
					ant.setMontoAnticipo(ant.getMontoAnticipo()+Double.parseDouble(a[1].toString()));
				}
			} 
			System.out.println("REG: "+ant.getMontoSalario());
			System.out.println("REG: "+ant.getMontoAnticipo());
			System.out.println("REG: "+ant.getMontoApagar());
			
		}
		
		return ret;
		*/
	}
	/*
	@RequestMapping(method = RequestMethod.GET, value = "consultarAnticipo/{id}")
	public List<SalarioFuncionarioAnticipo> getAnticipoPorIdCabecera(@PathVariable int id){
		List<Object []> v = anticipoRepository.listaPorIdCabecera(id);
		List<SalarioFuncionarioAnticipo> ret = new ArrayList<SalarioFuncionarioAnticipo>();
		for(Object [] ob: v) {
			SalarioFuncionarioAnticipo p = new SalarioFuncionarioAnticipo();
			p.setId(Integer.parseInt(ob[0].toString()));
			p.getSalarioFuncionario().setId(Integer.parseInt(ob[1].toString()));
		    p.getFuncionario().setId(Integer.parseInt(ob[2].toString()));
		    p.getAnticipo().setId(Integer.parseInt(ob[3].toString()));
		    p.getFuncionario().getPersona().setNombre(ob[4].toString()+ " "+ob[5].toString());
		    p.setMonto(Double.parseDouble(ob[6].toString()));
			ret.add(p);
		}
		return ret;
	}
	*/
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deletePlanillaDetallePorIdCabece(@PathVariable int id){
		Optional<SalarioFuncionario> planilla = entityRepository.findById(id);
		if(planilla.isPresent()) {
			detalleRepository.deleteAll(planilla.get().getSalarioFuncionarioDetalles());
			//anticipoRepository.deleteAll(planilla.get().getSalarioFuncionarioAnticipo());
			entityRepository.delete(planilla.get());
		}
	}
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody SalarioFuncionario entity ){
		/*
		 try {
			if (entity.getFuncionarioRegistro().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if (entity.getFuncionarioAutorizado().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO AUTORIZADO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if (entity.getPeriodo().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PERIODO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if (entity.getTotalSalario()==0) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DE SALARIO DEBE SER MAYOR A CERO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if (entity.getSalarioFuncionarioDetalles().size()<=0) {
				return new ResponseEntity<>(new CustomerErrorType("EL DETALLE SALARIO FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else {
				Double totalAnt=0.0, totalSue=0.0;
				for (int ind=0; ind < entity.getSalarioFuncionarioDetalles().size(); ind++) {
					SalarioFuncionarioDetalle ser=entity.getSalarioFuncionarioDetalles().get(ind);
					if(ser.getFuncionario().getId() == 0) {
						return new ResponseEntity<>(new CustomerErrorType("EL IDENTIFICADOR DEL DETALLE DEL ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getMontoSalario() == 0) {
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO SALARIO DEL DETALLE DEL ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getMontoPagado() == 0) {
						return new ResponseEntity<>(new CustomerErrorType("EL MONTO A PAGAR DEL DETALLE DEL ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getFuncionario().getId()==0) {
						return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO DEL ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
					totalAnt = totalAnt + ser.getMontoAnticipo();
					totalSue = totalSue + ser.getMontoSalario();
				}
				entity.setTotalSalario(totalSue);
				entity.setTotalAnticipo(totalAnt);
				entity.setTotalPagado(totalSue - totalAnt);
				if(entity.getId()!=0) {
					System.out.println("Emtro edit sala");
					
					int idVent=entity.getId();
					for (SalarioFuncionarioDetalle ov: entity.getSalarioFuncionarioDetalles()) {
						System.out.println("ID DET EDITT: "+ov.getId());
						ov.getSalarioFuncionario().setId(idVent);
						detalleRepository.save(ov);
						
					}
					entity.setFecha(new Date());
					if(entity.isEstado()==true) {System.out.println("entity confir true edit");}
				}else {
					System.out.println("entroo nuevo sala");
					entityRepository.save(entity);
					SalarioFuncionario id = entityRepository.getUltimoRegistro();
					int idSala=0;
					if(id == null){idSala=1;}else{idSala=id.getId();}
					for (SalarioFuncionarioDetalle ov: entity.getSalarioFuncionarioDetalles()) {
						System.out.println("ID DET NUE: "+ov.getId());
						ov.getSalarioFuncionario().setId(idSala);
						System.out.println("*8*8* : "+ov.getFuncionario().getId());
						detalleRepository.save(ov);
						
					}
					entity.setFecha(new Date());
					if(entity.isEstado()==false) {System.out.println("entity confir true nuevo");}
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		*/
		return new ResponseEntity<>(HttpStatus.OK);
				
		
	}
	
private List<SalarioFuncionarioDetalle> listar(List<Object[]> obj) {
	List<SalarioFuncionarioDetalle> listRetorno = new ArrayList<>();
	/*
	for(Object[] o: obj) 
		SalarioFuncionarioDetalle s = new SalarioFuncionarioDetalle();

		
		s.setId(Integer.parseInt(o[0].toString()));
		s.getFuncionario().getPersona().setNombre(o[1].toString()+" "+o[2].toString());
		s.setMontoSalario(Double.parseDouble(o[3].toString()));
		s.setMontoAnticipo(Double.parseDouble(o[4].toString()));
		s.setMontoPagado(Double.parseDouble(o[5].toString()));
		
		listRetorno.add(s);
		*/
	return listRetorno;
	}
	

@RequestMapping(method = RequestMethod.GET, value="/reporteSalarioFuncionario/{id}")
public  ResponseEntity<?> getReporteSalarioFuncionario(HttpServletResponse response, OAuth2Authentication authentication,@PathVariable int id) throws IOException{
	List<SalarioFuncionarioDetalle> lis =listar(entityRepository.getSalarioFuncionarioDetallePorIdLista(id));
	
	if(lis.size()>0) {
		
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
		

		report = new Reporte();
		report.reportPDFDescarga(lis, map, "ReporteSalarioFuncionario", response);

		return  new ResponseEntity<>(new CustomerErrorType(""), HttpStatus.OK);
	}else {
		return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
	}



}
	
	
	
}
