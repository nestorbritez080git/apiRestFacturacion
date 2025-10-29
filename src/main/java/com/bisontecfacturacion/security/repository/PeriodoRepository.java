package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Serializable>{

	public abstract Periodo findByDescripcion(String descripcion);
}
