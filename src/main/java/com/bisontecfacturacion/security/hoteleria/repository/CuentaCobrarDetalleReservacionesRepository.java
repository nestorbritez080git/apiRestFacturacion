package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.hoteleria.model.CuentaCobrarDetalleReservaciones;

public interface CuentaCobrarDetalleReservacionesRepository extends JpaRepository<CuentaCobrarDetalleReservaciones, Serializable>{

	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CuentaCobrarDetalleReservaciones set importe=subTotal, fechaPago=:fecha, estado=:est where cuenta_cobrar_cabecera_reservaciones_id=:id")
	public void liquidarDetalle(@Param("id") int id, @Param("fecha") Date fecha, @Param("est") Boolean est);
	
}
