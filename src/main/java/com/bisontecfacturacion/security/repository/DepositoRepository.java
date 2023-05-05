package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Deposito;

public interface DepositoRepository extends JpaRepository<Deposito, Serializable>{
	@Query("select d from Deposito d where d.descripcion like :descripcion%")
	List<Deposito> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Deposito findByDescripcion(String descripcion);
	public abstract List<Deposito>findTop100ByOrderByIdDesc();
}

