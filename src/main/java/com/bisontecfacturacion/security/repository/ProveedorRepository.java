package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Serializable> {

	public abstract List<Proveedor>findTop100ByOrderByIdDesc();
	
	public abstract Proveedor findTop1ByOrderByIdAsc();
	@Query("select p from Proveedor p where persona_id=:id ")
	public Proveedor getIdPersona(@Param("id") int id);
	
	@Query("select p from Proveedor p where persona_id=:id and p.id <>:idProveedor")
	public Proveedor getIdPersonaEditar(@Param("id") int id, @Param("idProveedor") int idProveedor);
	
	@Query(value = "select count(p.id) FROM Proveedor p",nativeQuery = true)
	Object[] findByProveedor();
 
	@Query(value="select proveedor.id, persona.nombre, persona.apellido, persona.cedula from proveedor inner join persona on proveedor.persona_id=persona.id where persona.nombre ilike :descripcion or persona.apellido ilike :descripcion or persona.cedula ilike :descripcion order by id desc limit 100",nativeQuery=true)
	List<Object[]>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
}
