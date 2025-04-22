package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Producto;

public interface AnticipoRepository extends JpaRepository<Anticipo, Serializable>{
	@Query(value="select a.id as id, pr.nombre as nomPr, pr.apellido as apePr, pa.nombre as nomPa, pa.apellido as apePa, pe.nombre as nomPe, pe.apellido as apePe, a.fecha as fecha, a.monto as monto, a.estado as esta, fr.id as idFr, fa.id as idFa, fe.id as idFe, tp.id as idTP, a.tipo as tipoAnt, a.disponibilidad as diss from anticipo a inner join funcionario fr on fr.id=a.funcionario_registro_id inner join persona pr on pr.id=fr.persona_id inner join funcionario fa on fa.id=a.funcionario_autorizado_id inner join persona pa on pa.id=fa.persona_id inner join funcionario fe on fe.id=a.funcionario_encargado_id inner join persona pe on pe.id=fe.persona_id inner join tipo_operacion tp on  tp.id=a.tipo_operacion_id where pr.nombre like :filtro or pr.apellido like :filtro or pa.nombre like :filtro or pa.apellido like :filtro or pe.nombre like :filtro or pe.apellido like :filtro", nativeQuery = true)
	List<Object[]> consultarTodoPorFiltro(@Param("filtro") String filtro);
	
	@Query(value="select a.id as id, pr.nombre as nomPr, pr.apellido as apePr, pa.nombre as nomPa, pa.apellido as apePa, pe.nombre as nomPe, pe.apellido as apePe, a.fecha as fecha, a.monto as monto, a.estado as esta, fr.id as idFr, fa.id as idFa, fe.id as idFe, tp.id as idTP, a.tipo as tipoAnt, a.disponibilidad as diss from anticipo a inner join funcionario fr on fr.id=a.funcionario_registro_id inner join persona pr on pr.id=fr.persona_id inner join funcionario fa on fa.id=a.funcionario_autorizado_id inner join persona pa on pa.id=fa.persona_id inner join funcionario fe on fe.id=a.funcionario_encargado_id inner join persona pe on pe.id=fe.persona_id inner join tipo_operacion tp on  tp.id=a.tipo_operacion_id ORDER BY a.id DESC", nativeQuery = true)
	List<Object[]> consultarTodo();
	public abstract Anticipo findTop1ByOrderByIdDesc();
	@Modifying 
	@Transactional(readOnly=false)
	@Query("update Anticipo set disponibilidad=:estado where id=:id")
	public void anularAnticipo(@Param("id") int id, @Param("estado") String estado);
	
	
	@Modifying 
	@Transactional(readOnly=false)
	@Query("update Anticipo set estado=:estado where id=:id")
	public void liquidarEstadoAnticipo(@Param("id") int id, @Param("estado") Boolean estado);
	
	@Query("SELECT cpc FROM Anticipo cpc  INNER JOIN cpc.funcionarioRegistro fRegistro INNER JOIN cpc.funcionarioEncargado fanticipo INNER JOIN fanticipo.persona panticipo WHERE  fanticipo.id=:id   AND  cpc.disponibilidad='CONFIRMADO' AND cpc.estado=false order by cpc.id desc")
	public List<Anticipo> buscarAnticipoActivoPorFuncionario(@Param("id") int id);
	

}
