package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.PlanillaSalarioFuncionarioDetalle;

public interface PlanillaSalarioFuncionarioDetalleRepository extends JpaRepository<PlanillaSalarioFuncionarioDetalle, Serializable>{
	@Query(value = "DELETE FROM PlanillaSalarioFuncionarioDetalle where planilla_salario_funcionario_id=:idCab")
	public void eliminarDetallePorCabeceraId(@Param("idCab") int idCab );

	@Query(value="SELECT f.id, a.monto FROM anticipo a inner join funcionario f on f.id=a.funcionario_encargado_id inner join persona pf on pf.id=f.persona_id inner join periodo per on per.id=a.periodo_id  where per.id =:idPeriodo AND a.estado=true",nativeQuery=true)
	List<Object[]> listadoAnticipo(@Param("idPeriodo") int id);
	
	@Query(value="select p.id as id, f.id as idF, pf.nombre as anop, pf.apellido as apeP, f.sueldo_bruto as sueldo, pl.id as idCab from planilla_salario_funcionario_detalle p  inner join funcionario f on f.id=p.funcionario_id inner join persona pf on pf.id=f.persona_id inner join planilla_salario_funcionario pl on pl.id=p.planilla_salario_funcionario_id where pl.id=:id",nativeQuery=true)
	List<Object[]> listaPorIdCabecera(@Param("id") int id);
	
	@Query(value="SELECT fecha, monto FROM anticipo where funcionario_encargado_id =:id and periodo_id = :idPer",nativeQuery=true)
	List<Object[]> detalleAnticipo(@Param("id") int id, @Param("idPer") int idPer);
	
	
}
