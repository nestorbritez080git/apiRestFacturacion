package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.TipoInventario;

public interface TipoInventarioRepository extends JpaRepository<TipoInventario, Serializable> {

}

