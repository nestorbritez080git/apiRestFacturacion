package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.UnidadMedida;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Serializable>{
	@Query("select d from UnidadMedida d where d.descripcion like :descripcion%")
	List<UnidadMedida> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract UnidadMedida findByDescripcion(String descripcion);
	public abstract List<UnidadMedida>findTop100ByOrderByIdDesc();
}
