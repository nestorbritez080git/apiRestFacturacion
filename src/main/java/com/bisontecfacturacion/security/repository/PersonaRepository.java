package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Venta;
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Serializable>{
	
	public abstract List<Persona> findTop100ByOrderByIdDesc();
	public abstract Persona findByCedula(String cedula);
	
	public abstract Persona findTop1ByOrderByIdDesc();
	
	@Query("select p from Persona p where p.cedula like :descripcion% or p.nombre like :descripcion% or p.apellido like :descripcion%")
	List<Persona> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	


	@Query(value = "select * from persona p where (not exists (select null from proveedor t2 where p.id=t2.persona_id) and p.nombre ilike :descripcion) OR (not exists (select null from proveedor t2 where p.id=t2.persona_id) and p.apellido ILIKE :descripcion) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaProveedor(@Param("descripcion") String descripcion);

	@Query(value = "select * from persona p where not exists (select null from proveedor t2 where p.id=t2.persona_id) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaProveedorAll();

	@Query(value = "select * from persona p where not exists (select null from alumno t2 where p.id=t2.persona_id) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaAlumnoAll();
	
	@Query(value = "select * from persona p where (not exists (select null from alumno t2 where p.id=t2.persona_id) and p.nombre ilike :descripcion) OR (not exists (select null from alumno t2 where p.id=t2.persona_id) and p.apellido ILIKE :descripcion) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaAlumno(@Param("descripcion") String descripcion);
	
	@Query(value = "select * from persona p where not exists (select null from docente t2 where p.id=t2.persona_id) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaDocenteAll();
	
	@Query(value = "select * from persona p where (not exists (select null from docente t2 where p.id=t2.persona_id) and p.nombre ilike :descripcion) OR (not exists (select null from docente t2 where p.id=t2.persona_id) and p.apellido ILIKE :descripcion) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaDocente(@Param("descripcion") String descripcion);
	
	
	@Query(value = "select * from persona p where (not exists (select null from cliente t2 where p.id=t2.persona_id) and p.nombre ilike :descripcion) OR (not exists (select null from cliente t2 where p.id=t2.persona_id) and p.apellido ILIKE :descripcion) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaCliente(@Param("descripcion") String descripcion);
	
	

	@Query(value = "select * from persona p where not exists (select null from cliente t2 where p.id=t2.persona_id) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaClienteAll();

	
	

	@Query(value = "select * from persona p where not exists (select null from funcionario t2 where p.id=t2.persona_id) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaFuncionarioAll();

	@Query(value = "select * from persona p where (not exists (select null from funcionario t2 where p.id=t2.persona_id) and p.nombre ilike :descripcion) OR (not exists (select null from funcionario t2 where p.id=t2.persona_id) and p.apellido ILIKE :descripcion) order by id desc", nativeQuery = true)
	public List<Persona> getListadoPersonaFuncionario(@Param("descripcion") String descripcion);


}

