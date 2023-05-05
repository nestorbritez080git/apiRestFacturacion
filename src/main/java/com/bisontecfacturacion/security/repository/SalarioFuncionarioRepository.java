package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.SalarioFuncionario;
import com.bisontecfacturacion.security.model.SalarioFuncionarioDetalle;
import com.bisontecfacturacion.security.model.Venta;

public interface SalarioFuncionarioRepository extends JpaRepository<SalarioFuncionario, Serializable> {
	@Query(value="select * from salario_funcionario v order by v.id desc limit 1", nativeQuery = true)
	SalarioFuncionario getUltimoRegistro();
	
	@Query(value=
			"select sfunc.id as idSalario, per.id as idPer, per.descripcion as desPer, planilla.id as idPlanilla, planilla.planilla as desPlanilla, freg.id as idFunReg, preg.nombre as nomPreg, preg.apellido as apePreg,  faut.id as idFuncAut, paut.nombre as nomPaut, paut.apellido as apePaut, sfunc.total_salario as toSal, sfunc.total_anticipo as toAnti, sfunc.total_pagado as toPag from salario_funcionario sfunc \r\n" + 
			"inner join funcionario freg on freg.id=sfunc.funcionario_registro_id inner join persona preg on preg.id=freg.persona_id \r\n" + 
			"inner join funcionario faut on faut.id=sfunc.funcionario_autorizado_id inner join persona paut on paut.id=faut.persona_id \r\n" + 
			"inner join planilla_salario_funcionario planilla on planilla.id=sfunc.planilla_salario_funcionario_id\r\n" + 
			"inner join periodo per on per.id= sfunc.periodo_id WHERE sfunc.id=:id", nativeQuery = true)
	Object[] []  getSalarioFuncionarioPorId(@Param("id") int id);
	
	@Query(value=" select sal.id as id, pf.nombre as nomF, pf.apellido as apeF, sal.monto_salario as monSal, sal.monto_anticipo as monAnti, sal.monto_pagado as monPag from salario_funcionario_detalle sal inner join funcionario f on f.id=sal.funcionario_id inner join persona pf on pf.id=f.persona_id where sal.salario_funcionario_id=:id", nativeQuery = true)
	List<Object[]>  getSalarioFuncionarioDetallePorIdLista(@Param("id") int id);
}
