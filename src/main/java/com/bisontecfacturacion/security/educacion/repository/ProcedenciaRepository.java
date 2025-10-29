package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.educacion.model.Procedencia;

public interface ProcedenciaRepository extends JpaRepository<Procedencia, Serializable> {
	
}
