package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Consumiciones;
import com.bisontecfacturacion.security.model.Grupo;

public interface ConsumicionesRepository extends JpaRepository<Consumiciones, Serializable> {
	@Query("select d from Consumiciones d where d.descripcion like :descripcion")
	List<Consumiciones> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Consumiciones findByDescripcion(String descripcion);
	public abstract List<Consumiciones>findTop100ByOrderByIdDesc();
}
