package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CategoriaConsumiciones;

public interface CategoriaConsumicionesRepository extends JpaRepository<CategoriaConsumiciones, Serializable> {
	public abstract List<CategoriaConsumiciones> findByOrderByIdDesc();
	
	
	@Query("select d from CategoriaConsumiciones d where d.descripcion like :descripcion%")
	List<CategoriaConsumiciones> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract CategoriaConsumiciones findByDescripcion(String descripcion);
	public abstract List<CategoriaConsumiciones>findTop100ByOrderByIdDesc();
}
