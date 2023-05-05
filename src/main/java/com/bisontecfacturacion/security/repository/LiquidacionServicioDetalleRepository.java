package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.LiquidacionServicioDetalle;

public interface LiquidacionServicioDetalleRepository extends JpaRepository<LiquidacionServicioDetalle, Serializable> {

}
