package com.bisontecfacturacion.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.ModeloRuc;
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.ModeloRucRepository;
import com.bisontecfacturacion.security.repository.PersonaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("cliente")
public class ClienteController {
	@Autowired
	private ClienteRepository entityRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private ModeloRucRepository modeloRepository;

	@RequestMapping(method=RequestMethod.GET, value = "/traerTodo")
	public List<Cliente> getAllListado(){	
		return entityRepository.findAll();
	
		
	}
	@RequestMapping(method=RequestMethod.GET)
	public List<Cliente> getAll(){
		List<Cliente> lista=entityRepository.getClienteAllLimites();
		List<Cliente> cliente=new ArrayList<>();
		for(Cliente c: lista) {
			Cliente clientes=new Cliente();
			clientes.setId(c.getId());
			clientes.getPersona().setId(c.getId());
			clientes.getPersona().setNombre(c.getPersona().getNombre());
			clientes.getPersona().setApellido(c.getPersona().getApellido());
			clientes.getPersona().setCedula(c.getPersona().getCedula());
			clientes.getPersona().setDireccion(c.getPersona().getDireccion());
			clientes.getPersona().setTelefono(c.getPersona().getTelefono());
			clientes.setEstadoBloqueo(c.isEstadoBloqueo());
			clientes.setLimiteCredito(c.getLimiteCredito());
			clientes.setDiaLimite(c.getDiaLimite());
			cliente.add(clientes);
		}
		for (int i = 0; i < cliente.size(); i++) {
			System.out.println(cliente.get(i).getPersona().getNombre());
			System.out.println(cliente.get(i).getPersona().getApellido());
			System.out.println(cliente.get(i).getPersona().getCedula());

		}
		return cliente;
	}

	@RequestMapping(method=RequestMethod.GET, value="/primerId")
	public int getPrimerId(){
		Cliente  c=new Cliente();
		c=entityRepository.findTop1ByOrderByIdAsc();
		int id=c.getId();
		return id;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/id/{id}")
	public Cliente getId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}

	@RequestMapping(method=RequestMethod.GET, value="/totalcliente")
	public Object[] getAllTotales(){
		return entityRepository.findByCliente();
	}

	@RequestMapping(method=RequestMethod.GET,value="/{ruc}")
	public ResponseEntity<?> getClientePorRuc(@PathVariable String ruc){
		System.out.println(ruc);
		if(ruc.isEmpty() || ruc.equals(null) || ruc.equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("DEBE AGREGAR RUC O CEDULA!!"), HttpStatus.CONFLICT);
		}
		List<Object[]> objeto=entityRepository.findeByClienteRuc(ruc);
		Cliente clientes=new Cliente();
		for(Object[] ob:objeto){
			clientes.setId(Integer.parseInt(ob[0].toString()));
			clientes.getPersona().setNombre(ob[2].toString()+", "+ob[3].toString());
			clientes.getPersona().setDireccion(ob[4].toString());;
			clientes.getPersona().setTipo(ob[5].toString());
			clientes.getPersona().setTelefono(ob[6].toString());
			clientes.getPersona().setEmail(ob[7].toString());
			if(ob[8] == null){
				clientes.getPersona().setCedula("");
			} else {
				clientes.getPersona().setCedula(ob[8].toString());
			}
			clientes.setEstadoBloqueo(Boolean.parseBoolean(ob[9].toString()));
		}
			
		return new ResponseEntity<Cliente>(clientes, HttpStatus.OK);
		
		
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Cliente entity){
		try {
			if(entity.getPersona().getId()==0){
				return new ResponseEntity<>(new CustomerErrorType("La persona no debe quedar vacio"), HttpStatus.CONFLICT);		
			}else if (siExistePersona(entity.getPersona())== true) {
				return new ResponseEntity<>(new CustomerErrorType("Esta Persona ya posee credenciales como Cliente dentro del sistema.!\nSi persiste el inconveniente consulte con el administrador  "), HttpStatus.CONFLICT);
//					return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			}else {
				entityRepository.save(entity);
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	public boolean siExistePersona(Persona entity){
		if(entityRepository.getIdPersona(entity.getId())!=null){
			return true;
		}
		return false;
	}
	
	public boolean siExistePersonaEditar(Cliente entity){
		if(entityRepository.getIdPersonaEditar(entity.getPersona().getId(), entity.getId())!=null){
			return true;
		}
		return false;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public Cliente editar(@RequestBody Cliente entity){
		
		return entityRepository.save(entity);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public void eliminar(@PathVariable int id){
		entityRepository.deleteById(id);
	}

	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Cliente> consultarPorDescripcion(@PathVariable String descripcion){
		List<Cliente> objetoResponde=entityRepository.getBuscarPorDescripcion("%"+descripcion.toUpperCase()+"%");
		List<Cliente> cliReturn=new ArrayList<>();
		for(Cliente ob: objetoResponde){
			Cliente c=new Cliente();
			c.setId(ob.getId());
			c.setDiaLimite(ob.getDiaLimite());
			c.setEstadoBloqueo(ob.isEstadoBloqueo());
			c.setPersona(ob.getPersona());
			cliReturn.add(c);
		}
		for (int i = 0; i < cliReturn.size(); i++) {
			System.out.println(cliReturn.get(i).getPersona().getNombre());
			System.out.println(cliReturn.get(i).getPersona().getApellido());
			System.out.println(cliReturn.get(i).getPersona().getCedula());

		}
		return cliReturn;
	}
	@RequestMapping(method=RequestMethod.GET, value="/verificarRuc/{ruc}/{dv}")
	public ModeloRuc  consultarPorDescripcion(@PathVariable int ruc, @PathVariable int dv ){
		return modeloRepository.getModeloRucDv(ruc, dv);
	}
	
	/*
@RequestMapping(value="/clientePDF", method=RequestMethod.GET)
public @ResponseBody void clientePDF() throws IOException{
	List<Cliente> cliente = entityRepository.findAll();
	Impresora impre=new Impresora();
	impre=impreRepository.findTop1ByOrderByIdAsc();
	String tipo = impre.getDescripcion();
	String filtros = "hoalm";
	
	Map<String, Object> map = new HashMap<>();
	map.put("filtros", filtros);
	Reporte report=new Reporte();
	//report.report(cliente, map, "cliente", tipo);
	
}

*/
	
	
	@RequestMapping(method=RequestMethod.POST, value = "/venta")
	public ResponseEntity<?> guardarNuevoCompra(@RequestBody Cliente entity){
		try {
			if(entity.getPersona().getCedula()==null || entity.getPersona().getCedula().equals("")) {
				 return new ResponseEntity<>(new CustomerErrorType("El N° DE CEDULA Y/O RUC NO DEBE QUEDAR VACIO"),HttpStatus.CONFLICT);	
			}else if(entity.getPersona().getNombre()== null || entity.getPersona().getNombre().equals("")) {
				 return new ResponseEntity<>(new CustomerErrorType("El NOMBRE NO DEBE QUEDAR VACIO"),HttpStatus.CONFLICT);	
			}else {
				if(entity.getPersona().getNombre()!=null){
					entity.getPersona().setNombre(Utilidades.eliminaCaracterIzqDer(entity.getPersona().getNombre().trim().toUpperCase()));			
		        }
		        if(entity.getPersona().getApellido()!=null){
					entity.getPersona().setApellido(Utilidades.eliminaCaracterIzqDer(entity.getPersona().getApellido().trim().toUpperCase()));			
		        }
		        if(entity.getPersona().getCedula()!=null){
					entity.getPersona().setCedula(Utilidades.eliminaCaracterIzqDer(entity.getPersona().getCedula().trim().toUpperCase()));			
		        }
		        if(entity.getPersona().getDireccion()!=null){
					entity.getPersona().setDireccion(Utilidades.eliminaCaracterIzqDer(entity.getPersona().getDireccion().trim().toUpperCase()));			
		        }
		        if(entity.getPersona().getEmail()!=null){
					entity.getPersona().setEmail(entity.getPersona().getEmail().trim().toUpperCase());			
		        }
		        if(entity.getPersona().getTipo()!=null){
					entity.getPersona().setTipo(entity.getPersona().getTipo().trim().toUpperCase());			
		        }
		        if(entity.getPersona().getId() != 0){
		               
		        } else {
		            if (siExiste(entity.getPersona())) {
		                return new ResponseEntity<>(new CustomerErrorType("El N° DE CEDULA " + entity.getPersona().getCedula() + " YA EXISTE."),
		                        HttpStatus.CONFLICT);
		            }else {
		                personaRepository.save(entity.getPersona());
		                Persona p = new Persona();
		                p=personaRepository.findTop1ByOrderByIdDesc();
		                entity.getPersona().setId(p.getId());
		                entityRepository.save(entity);
		            }
		        }
			} 
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	public boolean siExiste(Persona entity){
		return personaRepository.findByCedula(entity.getCedula())!=null;
	}

}
