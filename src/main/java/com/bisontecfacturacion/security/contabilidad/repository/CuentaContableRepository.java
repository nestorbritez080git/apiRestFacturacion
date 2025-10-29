package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.contabilidad.model.CuentaContable;

public interface CuentaContableRepository extends JpaRepository<CuentaContable, Serializable> {


}
