package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.PagosFuncionarioReferenciaOperacionCaja;
@Transactional(readOnly=true)
@Repository
public interface PagosFuncionarioReferenciaOperacionCajaRepository extends JpaRepository<PagosFuncionarioReferenciaOperacionCaja, Serializable> {
	@Query("select c from PagosFuncionarioReferenciaOperacionCaja c INNER JOIN c.operacionCaja operacion INNER JOIN c.pagosFuncionario pagos where pagos.id=:id")
	PagosFuncionarioReferenciaOperacionCaja getPagosFuncionarioReferenciaOperacionCajaPorIdPagos(@Param("id") int id);
	
}
