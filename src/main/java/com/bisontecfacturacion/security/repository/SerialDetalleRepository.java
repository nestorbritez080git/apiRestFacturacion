package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.SerialDetalle;

@Repository
public interface SerialDetalleRepository extends JpaRepository<SerialDetalle, Serializable>{

	@Query(value="SELECT * FROM serial_detalle d where extract(month from cast(d.fecha_inicio as Date))=:mes AND extract(year from cast(d.fecha_inicio as Date))=:anho limit 1",nativeQuery=true)
	SerialDetalle getPeriodoAcceso(@Param("anho") int anho, @Param("mes") int mes);
	@Modifying
    @Transactional(readOnly=false)
    @Query("delete from SerialDetalle where serial_id=1 ")
    public void eliminarDetalleTodos();
}
