package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Proveedor;


public interface ClienteRepository extends JpaRepository<Cliente, Serializable> {
	
	 public abstract List<Cliente>findTop100ByOrderByIdDesc();
	 
	 @Query(value = "select cliente.id, persona.nombre, persona.apellido, persona.direccion,persona.tipo,  persona.telefono, persona.email, persona.cedula, cliente.estado_bloqueo from cliente inner join persona on cliente.persona_id=persona.id where persona.cedula=:ruc", nativeQuery = true)
	 List<Object[]> findeByClienteRuc(@Param("ruc") String ruc);
 
	 public abstract Cliente findTop1ByOrderByIdAsc();
	 
	 @Query("select c from Cliente c where persona_id=:id ")
	 public Cliente getIdPersona(@Param("id") int id);
	 
	 @Query("select c from Cliente c where id=:id ")
	 public Cliente getIdCliente(@Param("id") int id);
	 
	 @Query("select c from Cliente c where persona_id=:id and c.id <>:idCliente")
	 public Proveedor getIdPersonaEditar(@Param("id") int id, @Param("idCliente") int idCliente);
	 
	 @Query(value = "select count(c.id) FROM Cliente c",nativeQuery = true)
	 Object[] findByCliente();

		@Query(value="select cliente.id, persona.nombre, persona.apellido, cliente.limite_credito, persona.cedula, persona.telefono,  cliente.estado_bloqueo from cliente inner join persona on cliente.persona_id=persona.id where persona.nombre ilike :descripcion or persona.apellido ilike :descripcion order by id desc limit 100",nativeQuery=true)
		List<Object[]>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
}
