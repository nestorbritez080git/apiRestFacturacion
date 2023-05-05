package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.ConfigCopiaSeguridad;

@Repository
public interface ConfigCopiaSeguridadRepository  extends JpaRepository<ConfigCopiaSeguridad, Serializable> {

}
