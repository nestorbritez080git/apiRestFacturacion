package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.AnticipoReferenciaOperacionCaja;
@Transactional(readOnly=true)
@Repository
public interface AnticipoReferenciaOperacionCajaRepository extends JpaRepository<AnticipoReferenciaOperacionCaja, Serializable> {
	@Query("select c from AnticipoReferenciaOperacionCaja c INNER JOIN c.operacionCaja operacion where anticipo_id=:id")
	AnticipoReferenciaOperacionCaja getReferenciaOperacionCajaPorIdAnticipo(@Param("id") int id);
	
}
