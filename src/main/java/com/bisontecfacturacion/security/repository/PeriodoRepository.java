package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Consumiciones;
import com.bisontecfacturacion.security.model.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Serializable>{

	public abstract Periodo findByDescripcion(String descripcion);
}
