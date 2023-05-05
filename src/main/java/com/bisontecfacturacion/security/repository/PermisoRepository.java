package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Serializable>{
	
	public abstract Permiso findByDescripcion(String descripcion);
}


