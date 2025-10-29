package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.PagosFuncionarioReferenciaCajaChica;
@Transactional(readOnly=true)
@Repository
public interface PagosFuncionarioReferenciaCajaChicaRepository extends JpaRepository<PagosFuncionarioReferenciaCajaChica, Serializable> {
	
	@Query("select c from PagosFuncionarioReferenciaCajaChica c  where pagos_funcionario_id=:id")
	PagosFuncionarioReferenciaCajaChica getPagosFuncionarioReferenciaCajaChicaPorIdPagos(@Param("id") int id);

}
