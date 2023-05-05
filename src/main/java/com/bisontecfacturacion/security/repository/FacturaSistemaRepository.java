package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.controller.FacturaSistema;

public interface FacturaSistemaRepository extends JpaRepository<FacturaSistema, Serializable> {

	@Query(value="SELECT * FROM factura_sistema fs where extract(month from cast(fs.fecha_factura as Date))=:mes AND extract(year from cast(fs.fecha_factura as Date))=:anho limit 1",nativeQuery=true)
	FacturaSistema getFacturaSistemaFechaActual(@Param("anho") int anho, @Param("mes") int mes);
}

