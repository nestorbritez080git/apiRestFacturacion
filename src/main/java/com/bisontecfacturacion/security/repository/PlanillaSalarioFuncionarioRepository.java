package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.PlanillaSalarioFuncionario;

public interface PlanillaSalarioFuncionarioRepository extends JpaRepository<PlanillaSalarioFuncionario, Serializable>{
	@Modifying
    @Transactional(readOnly=false)
    @Query("update PlanillaSalarioFuncionario set total=total-:monto where id=:id")
    public void findByActualizaMontoCabecera(@Param("monto") double stock,@Param("id") int id);
	@Query(value="select * from planilla_salario_funcionario v order by v.id desc limit 1", nativeQuery = true)
	PlanillaSalarioFuncionario getUltimaPlanilla();
	
	@Query(value="select * from planilla_salario_funcionario where planilla like:filtro", nativeQuery = true)
	List<PlanillaSalarioFuncionario> consultarPlanilla(@Param("filtro") String filtro);
}
