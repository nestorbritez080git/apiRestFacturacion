package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.AnticipoAuxiliar;
import com.bisontecfacturacion.security.auxiliar.AnticipoDetalleAuxiliar;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.PlanillaSalarioFuncionario;
import com.bisontecfacturacion.security.model.PlanillaSalarioFuncionarioDetalle;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.PlanillaSalarioFuncionarioDetalleRepository;
import com.bisontecfacturacion.security.repository.PlanillaSalarioFuncionarioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("planillaSalarioFuncionario")
public class PlanillaSalarioFuncionarioController {

	@Autowired
	private PlanillaSalarioFuncionarioRepository entityRepository;
	
	@Autowired
	private PlanillaSalarioFuncionarioDetalleRepository detalleRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<PlanillaSalarioFuncionario> getAll(){
		List<PlanillaSalarioFuncionario> liRetorno = new ArrayList<PlanillaSalarioFuncionario>();
		List<PlanillaSalarioFuncionario> li = entityRepository.findAll();
		PlanillaSalarioFuncionario p = null;
		for (PlanillaSalarioFuncionario o: li) {
			p = new PlanillaSalarioFuncionario();
			p.setId(o.getId());
			p.getFuncionario().getPersona().setNombre(o.getFuncionario().getPersona().getNombre());
			p.getFuncionario().getPersona().setApellido(o.getFuncionario().getPersona().getApellido());
			p.setTotal(o.getTotal());
			p.setPlanilla(o.getPlanilla());
			liRetorno.add(p);
		}
		return liRetorno;
	}
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public PlanillaSalarioFuncionario getPlanillaSalario(@PathVariable int id){
		PlanillaSalarioFuncionario v = entityRepository.findById(id).orElse(null);
		PlanillaSalarioFuncionario ret = new PlanillaSalarioFuncionario();
		ret=v;
		ret.setPlanillaSalarioFuncionarioDetalles(null);
		
		return ret;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "consultarDetalleAnticipo/{id}/{idPer}")
	public List<AnticipoDetalleAuxiliar> getDetalleAnticipo(@PathVariable int id, @PathVariable int idPer){
		List<AnticipoDetalleAuxiliar> detalle = new ArrayList<>();
		List<Object []> v = detalleRepository.detalleAnticipo(id, idPer);
		
		for(Object [] d: v) {
			AnticipoDetalleAuxiliar deta=new AnticipoDetalleAuxiliar();
			deta.setFecha(null);
			deta.setMonto(Double.parseDouble(d[1].toString()));
			detalle.add(deta);
		}
		
		return detalle;
	}
	@RequestMapping(method = RequestMethod.GET, value = "consultarDetalle/edit/{id}")
	public List<PlanillaSalarioFuncionarioDetalle> getDetallePlanillaPorIdCabeceraPlanilla(@PathVariable int id){
		
		List<Object []> v = detalleRepository.listaPorIdCabecera(id);
		List<PlanillaSalarioFuncionarioDetalle> ret = new ArrayList<PlanillaSalarioFuncionarioDetalle>();
		for(Object [] o: v) {
			PlanillaSalarioFuncionarioDetalle p=new PlanillaSalarioFuncionarioDetalle();
			p.setId(Integer.parseInt(o[0].toString()));
			p.getFuncionario().setId(Integer.parseInt(o[1].toString()));
			p.getFuncionario().getPersona().setNombre(o[2].toString());
			p.getFuncionario().getPersona().setApellido(o[3].toString());
			p.getFuncionario().setSueldoBruto(Double.parseDouble(o[4].toString()));
			p.getPlanillaSalarioFuncionario().setId(Integer.parseInt(o[5].toString()));
		    ret.add(p);
		}
		
	
		return ret;
	}
	@RequestMapping(method = RequestMethod.GET, value = "consultarDetalle/{id}/{idPeriodo}")
	public List<AnticipoAuxiliar> getDetallePlanillaPorIdCabecera(@PathVariable int id, @PathVariable int idPeriodo){
		
		List<Object[]> anticipo = detalleRepository.listadoAnticipo(idPeriodo);
		List<Object []> v = detalleRepository.listaPorIdCabecera(id);
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
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteDetalle/{id}")
	public void deletePlanillaId(@PathVariable int id){
		detalleRepository.deleteById(id);
	}
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deletePlanillaDetallePorIdCabece(@PathVariable int id){
		Optional<PlanillaSalarioFuncionario> planilla = entityRepository.findById(id);
		if(planilla.isPresent()) {
			detalleRepository.deleteAll(planilla.get().getPlanillaSalarioFuncionarioDetalles());
			entityRepository.delete(planilla.get());
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/actualizarTotal/{monto}/{id}")
	public void actualizarTotal(@PathVariable double monto, @PathVariable int id){
		entityRepository.findByActualizaMontoCabecera(monto, id);
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/buscarPlanilla")
	public List<PlanillaSalarioFuncionario> consultarPlanilla(@RequestBody String filtro){
		
		return null;
	}
	
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody PlanillaSalarioFuncionario entity){
		try {
			if(entity.getFuncionario().getId()==0) {
				new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO"),HttpStatus.CONFLICT);
			}else if(entity.getTotal()<0) {
				new ResponseEntity<>(new CustomerErrorType("EL DEBES TOTAL DE LA PLANILLA DE SUELDO FUNCIONARIO DEBE SER MAYOR A CERO "), HttpStatus.CONFLICT);
			}else if(entity.getPlanillaSalarioFuncionarioDetalles().size()==0) {
				new ResponseEntity<>(new CustomerErrorType("DEBES AGREGAR POR LO UN DETALLE EN LA PLANILLA DE SUELDO FUNCIONARIO"), HttpStatus.CONFLICT);
			}else if(entity.getPlanilla().equals("") ){
				new ResponseEntity<>(new CustomerErrorType("SE DEBES AGREGAR PLANILLA DE FUNCIONARIO"), HttpStatus.CONFLICT);
			}else {
				List<PlanillaSalarioFuncionarioDetalle> detalle = entity.getPlanillaSalarioFuncionarioDetalles();
				entity.setPlanillaSalarioFuncionarioDetalles(null);
				entityRepository.save(entity);
				for(PlanillaSalarioFuncionarioDetalle o : detalle) {
					o.getPlanillaSalarioFuncionario().setId(entity.getId());
				}
				detalleRepository.saveAll(detalle);
				entity.setPlanillaSalarioFuncionarioDetalles(detalle);
			}
			
			/*if (entity.getId()!=0) {
				System.out.println("Editar");
				int idVent=entity.getId();
				for (PlanillaSalarioFuncionarioDetalle d : entity.getPlanillaSalarioFuncionarioDetalles()) {
					d.getPlanillaSalarioFuncionario().setId(idVent);
					detalleRepository.save(d);
				}
				
			}else {
				System.out.println("Nuevo");
				entityRepository.save(entity);
				PlanillaSalarioFuncionario id = entityRepository.getUltimaPlanilla();
				for (PlanillaSalarioFuncionarioDetalle d : entity.getPlanillaSalarioFuncionarioDetalles()) {
					d.getPlanillaSalarioFuncionario().setId(id.getId());
					detalleRepository.save(d);
				}
				
			}*/
			entityRepository.save(entity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(HttpStatus.CREATED);
		
	}
	
	
}
