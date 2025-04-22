package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.LiquidacionAnticipoDetalle;
import com.bisontecfacturacion.security.model.LiquidacionServicioDetalle;

public interface LiquidacionAnticipoDetalleRepository extends JpaRepository<LiquidacionAnticipoDetalle, Serializable> {

}
