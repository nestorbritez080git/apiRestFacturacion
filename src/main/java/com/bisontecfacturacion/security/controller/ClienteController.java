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
import com.bisontecfacturacion.security.repository.CompraRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.ModeloRucRepository;
import com.bisontecfacturacion.security.repository.PersonaRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("cliente")
public class ClienteController {
	@Autowired
	private ClienteRepository entityRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private ProveedorRepository proveedorRepository;
	
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private VentaRepository ventaRepository;
	
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
			clientes.getPersona().setId(c.getPersona().getId());
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

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Cliente entity) {
	    try {
	        Persona p = entity.getPersona();

	        if (p.getCedula() == null || p.getCedula().trim().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("EL N° DE CÉDULA Y/O RUC NO DEBE QUEDAR VACÍO"),
	                HttpStatus.CONFLICT
	            );
	        }

	        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("EL NOMBRE NO DEBE QUEDAR VACÍO"),
	                HttpStatus.CONFLICT
	            );
	        }

	        // 🧹 NORMALIZACIÓN DE DATOS
	        p.setNombre(Utilidades.eliminaCaracterIzqDer(p.getNombre().trim().toUpperCase()));

	        if (p.getApellido() != null)
	            p.setApellido(Utilidades.eliminaCaracterIzqDer(p.getApellido().trim().toUpperCase()));

	        if (p.getDireccion() != null)
	            p.setDireccion(Utilidades.eliminaCaracterIzqDer(p.getDireccion().trim().toUpperCase()));

	        if (p.getEmail() != null)
	            p.setEmail(p.getEmail().trim().toUpperCase());

	        if (p.getTipo() != null)
	            p.setTipo(p.getTipo().trim().toUpperCase());
// 🔍 BUSCAR SI YA EXISTE PERSONA CON ESA CÉDULA
	   
	        String cedulaNormalizada = normalizar(p.getCedula());
	        Persona existente = personaRepository.findAll().stream()
	            .filter(x -> normalizar(x.getCedula()).equals(cedulaNormalizada))
	            .findFirst()
	            .orElse(null);

	        if (existente != null) {
	            entity.setPersona(existente);
	        } else {
	            p.setId(null);
	            p.setCedula(cedulaNormalizada);
	            entity.setPersona(p);
	        }
	        // 💾 GUARDAR CLIENTE
	        entity.setId(null);
	        entityRepository.save(entity);

	        return new ResponseEntity<>(HttpStatus.CREATED);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	public boolean siExistePersonaCedula(Persona p){
		String cedulaNormalizada = normalizar(p.getCedula());
	    return personaRepository.findAll().stream()
	        .anyMatch(x -> normalizar(x.getCedula()).equals(cedulaNormalizada));
	}

	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<?> editar(@RequestBody Cliente entity){
		try {
			if (entity.getPersona() == null) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("La persona no puede ser nula"),
	                HttpStatus.CONFLICT
	            );
	        }
	        // 🔒 VALIDACIONES
	        if (entity.getPersona().getCedula() == null || entity.getPersona().getCedula().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("El N° DE CEDULA Y/O RUC NO DEBE QUEDAR VACIO"),
	                HttpStatus.CONFLICT
	            );
	        }
	        if (entity.getPersona().getNombre() == null || entity.getPersona().getNombre().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("EL NOMBRE NO DEBE QUEDAR VACIO"),
	                HttpStatus.CONFLICT
	            );
	        } 
	        // 🧹 NORMALIZACIÓN
	        Persona p = entity.getPersona();
	        p.setNombre(Utilidades.eliminaCaracterIzqDer(p.getNombre().trim().toUpperCase()));
	        if (p.getApellido() != null)
	            p.setApellido(Utilidades.eliminaCaracterIzqDer(p.getApellido().trim().toUpperCase()));
	        if (p.getDireccion() != null)
	            p.setDireccion(Utilidades.eliminaCaracterIzqDer(p.getDireccion().trim().toUpperCase()));
	        if (p.getEmail() != null)
	            p.setEmail(p.getEmail().trim().toUpperCase());
	        if (p.getTipo() != null)
	            p.setTipo(p.getTipo().trim().toUpperCase());
	        
		    if(entity.getPersona().getId()==0){
					return new ResponseEntity<>(new CustomerErrorType("LA PERSONA NO DEBE QUEDAR VACIO"), HttpStatus.CONFLICT);		
		    }else if (siExistePersonaEditar(entity)) {
					return new ResponseEntity<>(new CustomerErrorType("ESTA PERSONA YA POSEE CREDENCIALES DENTRO DEL SISTEMA.!"), HttpStatus.CONFLICT);
//						return new ResponseEntity<>("Esta Persona ya posee credenciales como funcionario dentro del sistema.!\nSi persiste el inconvenientes consulte con administrador  ", HttpStatus.CONFLICT);
			}else {
					entityRepository.save(entity);
					return  new  ResponseEntity<String>(HttpStatus.CREATED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		    return new ResponseEntity<>(
		            new CustomerErrorType(e.getMessage()),
		            HttpStatus.INTERNAL_SERVER_ERROR
		        );
		}
	}
	public boolean siExistePersonaEditar(Cliente entity){
	    String cedulaNormalizada = normalizar(entity.getPersona().getCedula());
	    return entityRepository.findAll().stream()
	        .anyMatch(x ->
	            !x.getPersona().getId().equals(entity.getPersona().getId()) && // 👈 EXCLUIR EL MISMO
	            normalizar(x.getPersona().getCedula()).equals(cedulaNormalizada)
	        );
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {
		    Cliente cli = entityRepository.findById(id).orElse(null);
		    if (cli == null) {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    }
		    Persona persona = cli.getPersona();
		    // validar ventas
		    if (ventaRepository.existsByClienteId(id)) {
		        return new ResponseEntity<>(
		            new CustomerErrorType("NO SE PUEDE ELIMINAR, EL CLIENTE TIENE VENTAS"),
		            HttpStatus.CONFLICT
		        );
		    }
		    // eliminar cliente
		    entityRepository.deleteById(id);
		    // verificar si la persona sigue siendo usada
		    boolean usadoEnProveedor = proveedorRepository.existsByPersonaId(persona.getId());
		    boolean usadoEnFuncionario = funcionarioRepository.existsByPersonaId(persona.getId());
		    if (!usadoEnProveedor && !usadoEnFuncionario) {
		        if (personaRepository.existsById(persona.getId())) {
		            personaRepository.deleteById(persona.getId());
		        }
		    }
		    return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
		    e.printStackTrace();
		    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/venta")
	public ResponseEntity<?> guardarNuevoCompra(@RequestBody Cliente entity) {

	    try {
	        if (entity.getPersona() == null) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("La persona no puede ser nula"),
	                HttpStatus.CONFLICT
	            );
	        }

	        // 🔒 VALIDACIONES
	        if (entity.getPersona().getCedula() == null || entity.getPersona().getCedula().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("El N° DE CEDULA Y/O RUC NO DEBE QUEDAR VACIO"),
	                HttpStatus.CONFLICT
	            );
	        }

	        if (entity.getPersona().getNombre() == null || entity.getPersona().getNombre().isEmpty()) {
	            return new ResponseEntity<>(
	                new CustomerErrorType("EL NOMBRE NO DEBE QUEDAR VACIO"),
	                HttpStatus.CONFLICT
	            );
	        }

	        // 🧹 NORMALIZACIÓN
	        Persona p = entity.getPersona();
	        p.setNombre(Utilidades.eliminaCaracterIzqDer(p.getNombre().trim().toUpperCase()));
	        if (p.getApellido() != null)
	            p.setApellido(Utilidades.eliminaCaracterIzqDer(p.getApellido().trim().toUpperCase()));
	        if (p.getDireccion() != null)
	            p.setDireccion(Utilidades.eliminaCaracterIzqDer(p.getDireccion().trim().toUpperCase()));
	        if (p.getEmail() != null)
	            p.setEmail(p.getEmail().trim().toUpperCase());
	        if (p.getTipo() != null)
	            p.setTipo(p.getTipo().trim().toUpperCase());
	        // 🚨 CLAVE: fuerza entidad nueva
	        p.setId(null);
	        entity.setId(null);
	        String cedulaOriginal = p.getCedula();
	        String cedulaNormalizada = normalizar(cedulaOriginal);
	        p.setCedula(cedulaNormalizada);

	        // 🔍 VALIDAR DUPLICADO
//	        if (siExiste(p)) {
//	            return new ResponseEntity<>(
//	                new CustomerErrorType("El N° DE CEDULA " + p.getCedula() + " YA EXISTE."),
//	                HttpStatus.CONFLICT
//	            );
//	        }

	        // 💾 UN SOLO SAVE
	        entityRepository.save(entity);

	        return new ResponseEntity<>(HttpStatus.CREATED);

	    } catch (Exception e) {
	    	 e.printStackTrace();
	    	return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	 public static String normalizar(String valor) {
	        if (valor == null) return null;

	        // quitar todo lo que no sea número
	        String limpio = valor.replaceAll("[^0-9]", "");

	        // si tiene más de 7 dígitos, asumimos que el último es DV
	        if (limpio.length() > 7) {
	            return limpio.substring(0, limpio.length() - 1);
	        }

	        return limpio;
	    }
	 }
