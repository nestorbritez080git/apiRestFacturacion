package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.SubGrupo;

public interface SubGrupoRepository extends JpaRepository<SubGrupo, Serializable>{
	
	public abstract List<SubGrupo> findByOrderByIdDesc();
	
	@Query("select d from SubGrupo d where d.descripcion like :descripcion%")
	List<SubGrupo> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract SubGrupo findByDescripcion(String descripcion);
	public abstract List<SubGrupo>findTop100ByOrderByIdDesc();
}

