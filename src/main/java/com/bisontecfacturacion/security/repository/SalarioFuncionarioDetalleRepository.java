package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.SalarioFuncionario;
import com.bisontecfacturacion.security.model.SalarioFuncionarioDetalle;

public interface SalarioFuncionarioDetalleRepository extends JpaRepository<SalarioFuncionarioDetalle, Serializable> {
	@Query(value="select det.id as id, fdet.id as idF, cab.id as idCab, pdet.nombre as nomP, pdet.apellido as apP, det.monto_salario  as montosal, det.monto_anticipo as monAnti, det.monto_pagado as monPag from salario_funcionario_detalle det inner join salario_funcionario cab on det.salario_funcionario_id=cab.id inner join funcionario fdet on fdet.id=det.funcionario_id inner join persona pdet on pdet.id=fdet.persona_id where cab.id=:id",nativeQuery=true)
	List<Object[]> listaPorIdCabecera(@Param("id") int id);
}
