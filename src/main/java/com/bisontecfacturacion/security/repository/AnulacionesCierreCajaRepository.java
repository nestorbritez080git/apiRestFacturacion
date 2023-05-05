package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.AnulacionesCierreCaja;

@Repository
public interface AnulacionesCierreCajaRepository extends JpaRepository<AnulacionesCierreCaja, Serializable> {
	@Query("select f from AnulacionesCierreCaja f where cierre_caja_id=:id ")
	public AnulacionesCierreCaja getIdCierreCabecera(@Param("id") int id);
}
