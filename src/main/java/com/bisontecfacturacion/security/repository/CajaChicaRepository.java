package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Caja;
import com.bisontecfacturacion.security.model.CajaChica;

public interface CajaChicaRepository extends JpaRepository<CajaChica, Serializable> {

	
	@Query(value="select fe.id as id, pe.nombre as nombre, pe.apellido as apell, c.id as idCaja from caja_chica c inner join funcionario fe on fe.id=c.funcionarioe_id inner join persona pe on pe.id=fe.persona_id", nativeQuery = true)
	List<Object[]> listarFuncionarioCajaChicaActivoGasto();
	
	@Query(value="select fe.id as id, pe.nombre as nombre, pe.apellido as apell, c.id as idCaja from caja_chica c inner join funcionario fe on fe.id=c.funcionarioe_id inner join persona pe on pe.id=fe.persona_id", nativeQuery = true)
	List<Object[]> listarFuncionarioCajaChicaActivo();
	
	
	@Query(value="SELECT f.id as id, pe.nombre as nom, pe.apellido as apeell FROM funcionario f inner join persona pe on pe.id=f.persona_id where not exists (select null from caja_chica c2 where f.id=c2.funcionarioe_id) ", nativeQuery = true)
	List<Object[]> listarFuncionarioCajaChicaInactivo();
	
	@Query("select c from CajaChica c where funcionarioe_id=:id")
	CajaChica getCajaChicaPorIddFuncionario(@Param("id") int id);
	
	
	
	@Query("select c from CajaChica c where id=:id")
	CajaChica getCajaChicaPorIdCaja(@Param("id") int id);
	
	
	
	
	@Query("select a from CajaChica a where funcionarioe_id=:idFuncionario order by id desc")
	public List<CajaChica> getCajaChicaPorFuncionario(@Param("idFuncionario")int idFuncionario);
	public abstract List<CajaChica>findTop100ByOrderByIdDesc();
}
