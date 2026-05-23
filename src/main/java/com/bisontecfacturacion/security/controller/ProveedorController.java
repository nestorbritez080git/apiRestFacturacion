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
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.CompraRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.PersonaRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("proveedor")
public class ProveedorController {
	@Autowired
	private ProveedorRepository entityRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CompraRepository compraRepository;
	
	@Autowired
	private ProductoRepository productoRepository;
	
	@Autowired
	private VentaRepository ventaRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PersonaRepository personaRepository;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Proveedor> getAll(){
		return entityRepository.findTop100ByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.GET, value="/primerId")
	public int getPrimerId(){
		Proveedor p=new Proveedor();
		p=entityRepository.findTop1ByOrderByIdAsc();
		int id=p.getId();
		return id;
	}
	@RequestMapping(method=RequestMethod.GET, value="totalproveedor")
	public Object[] getAllTotales(){
		return entityRepository.findByProveedor();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Proveedor getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}		
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Proveedor entity) {
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

	        // NORMALIZACIÓN
	        p.setNombre(Utilidades.eliminaCaracterIzqDer(p.getNombre().trim().toUpperCase()));

	        if (p.getApellido() != null)
	            p.setApellido(Utilidades.eliminaCaracterIzqDer(p.getApellido().trim().toUpperCase()));

	        if (p.getDireccion() != null)
	            p.setDireccion(Utilidades.eliminaCaracterIzqDer(p.getDireccion().trim().toUpperCase()));

	        if (p.getEmail() != null)
	            p.setEmail(p.getEmail().trim().toUpperCase());

	        if (p.getTipo() != null)
	            p.setTipo(p.getTipo().trim().toUpperCase());

	        // BUSCAR PERSONA EXISTENTE
	        String cedulaNormalizada = normalizar(p.getCedula());
	        String cedulaRuc = p.getCedula();

	        Persona existente = personaRepository.findAll().stream()
	            .filter(x -> normalizar(x.getCedula()).equals(cedulaNormalizada))
	            .findFirst()
	            .orElse(null);

	        if (existente != null) {
	            entity.setPersona(existente);
	        } else {
	            p.setId(null);
	            p.setCedula(cedulaRuc);

	            // GUARDAR PERSONA PRIMERO
	            Persona personaGuardada = personaRepository.save(p);

	            entity.setPersona(personaGuardada);
	        }

	        // GUARDAR proveedor
	        entity.setId(null);
	        entityRepository.save(entity);
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@RequestMapping(method=RequestMethod.POST, value = "/compra")
	public ResponseEntity<?> guardarNuevoCompra(@RequestBody Proveedor entity){
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

		        // 🔍 VALIDAR DUPLICADO
		        if (siExiste(p)) {
		            return new ResponseEntity<>(
		                new CustomerErrorType("El N° DE CEDULA " + p.getCedula() + " YA EXISTE."),
		                HttpStatus.CONFLICT
		            );
		        }

		        // 💾 UN SOLO SAVE
		        entityRepository.save(entity);

		        return new ResponseEntity<>(HttpStatus.CREATED);

		    } catch (Exception e) {
		        e.printStackTrace();
		        return new ResponseEntity<>(
		            new CustomerErrorType(e.getMessage()),
		            HttpStatus.INTERNAL_SERVER_ERROR
		        );
		    }
	}
	
	public boolean siExiste(Persona entity){
		return personaRepository.findByCedula(entity.getCedula())!=null;
	}
	
	
	public boolean siExistePersona(Persona entity){
		if(entityRepository.getIdPersona(entity.getId())!=null){
			return true;
		}
		return false;
	}
		public boolean siExistePersonaEditar(Proveedor entity){
		    String cedulaNormalizada = normalizar(entity.getPersona().getCedula());
		    return entityRepository.findAll().stream()
		        .anyMatch(x ->
		            !x.getPersona().getId().equals(entity.getPersona().getId()) && // 👈 EXCLUIR EL MISMO
		            normalizar(x.getPersona().getCedula()).equals(cedulaNormalizada)
		        );
		
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<?> editar(@RequestBody Proveedor entity){
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
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		try {

	        Proveedor pro = entityRepository.findById(id).orElse(null);

	        if (pro == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        if (compraRepository.existsByProveedorId(id)) {
	            return new ResponseEntity<>("TIENE COMPRAS", HttpStatus.CONFLICT);
	        }

	        if (productoRepository.existsByProveedorId(id)) {
	            return new ResponseEntity<>("TIENE PRODUCTOS", HttpStatus.CONFLICT);
	        }

	        // ✔ GUARDAR PERSONA ANTES DE BORRAR
	        Integer personaId = pro.getPersona().getId();

	        // ✔ BORRAR PROVEEDOR
	        entityRepository.deleteById(id);

	        // ✔ VALIDAR DESPUÉS DEL DELETE
	        boolean usadoEnCliente = clienteRepository.existsByPersonaId(personaId);
	        boolean usadoEnProveedor = entityRepository.existsByPersonaId(personaId);
	        boolean usadoEnFuncionario = funcionarioRepository.existsByPersonaId(personaId);

	        if (!usadoEnCliente && !usadoEnProveedor && !usadoEnFuncionario) {
	            personaRepository.deleteById(personaId);
	        }

	        return new ResponseEntity<>(HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	private void eliminarPersonaSiNoSeUsa(Integer personaId) {

	    boolean usadoEnCliente = clienteRepository.existsByPersonaId(personaId);
	    boolean usadoEnProveedor = entityRepository.existsByPersonaId(personaId);
	    boolean usadoEnFuncionario = funcionarioRepository.existsByPersonaId(personaId);

	    if (!usadoEnCliente && !usadoEnProveedor && !usadoEnFuncionario) {
	        personaRepository.deleteById(personaId);
	    }
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/buscar/{descripcion}")
	public List<Proveedor> consultarPorDescripcion(@PathVariable String descripcion){
		List<Object[]> objeto=entityRepository.getBuscarPorDescripcion("%"+descripcion.toUpperCase()+"%");
		List<Proveedor> cliente=new ArrayList<>();
		for(Object[] ob:objeto){
			Proveedor pro=new Proveedor();
			pro.setId(Integer.parseInt(ob[0].toString()));
			pro.getPersona().setNombre(ob[1].toString());
			pro.getPersona().setApellido(ob[2].toString());
			pro.getPersona().setCedula(ob[3].toString());
			pro.getPersona().setId(Integer.parseInt(ob[4].toString()));
			cliente.add(pro);
		}
		return cliente;
	}

}
