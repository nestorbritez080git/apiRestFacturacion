package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Grupo;


public interface GrupoRepository extends JpaRepository<Grupo, Serializable>{
	
	public abstract List<Grupo> findByOrderByIdDesc();
	
	
	@Query("select d from Grupo d where d.descripcion like :descripcion%")
	List<Grupo> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Grupo findByDescripcion(String descripcion);
	public abstract List<Grupo>findTop100ByOrderByIdDesc();
	
	@Query(value = "select count(m.id) FROM Grupo m",nativeQuery = true)
	Object[] findByGrupo();
	
}

