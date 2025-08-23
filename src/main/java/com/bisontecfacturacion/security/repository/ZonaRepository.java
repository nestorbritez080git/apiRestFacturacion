package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Zona;

public interface ZonaRepository extends JpaRepository<Zona, Serializable>{
	
	public abstract List<Zona> findByOrderByIdAsc();
	
	@Query("select d from Zona d where d.descripcion like :descripcion%")
	List<Zona> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Zona findByDescripcion(String descripcion);
	public abstract List<Zona>findTop100ByOrderByIdAsc();
}

