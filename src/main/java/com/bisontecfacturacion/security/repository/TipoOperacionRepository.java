package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.TipoOperacion;

public interface TipoOperacionRepository extends JpaRepository<TipoOperacion, Serializable> {

}
