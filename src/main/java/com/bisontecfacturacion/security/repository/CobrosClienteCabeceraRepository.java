package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;

public interface CobrosClienteCabeceraRepository extends JpaRepository<CobrosClienteCabecera, Serializable>{
	@Query(value="select * from cobros_cliente_cabecera v order by v.id desc limit 1", nativeQuery = true)
	CobrosClienteCabecera getUltimoCobrosClienteCab();
	
	@Query("select  c from CobrosClienteCabecera c where id=:id order by id desc")
	public CobrosClienteCabecera buscarCobrosCabeceraPorId(@Param("id") int id);
	public abstract CobrosClienteCabecera findTop1ByOrderByIdDesc();

	
	@Query("select  c from CobrosClienteCabecera c where cliente_id=:id order by id desc")
	public List<CobrosClienteCabecera> findByCuentaPorIdCliente(@Param("id") int id);
	@Query("select  ccc from CobrosClienteCabecera ccc INNER JOIN ccc.cliente cli INNER JOIN ccc.funcionario fun   where cli.id=:id and ((ccc.fecha >=:fecha_inicio) and (ccc.fecha<=:fecha_fin)) order by ccc.id desc")
	public List<CobrosClienteCabecera> findByCobrosClientePorRango(@Param("id") int id, @Param("fecha_inicio") Date fecInicio, @Param("fecha_fin") Date fecFin );
//	SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun WHERE pro.id = :id order by cpc.id desc
	@Query("select  ccc from CobrosCliente ccc INNER JOIN ccc.cobrosClienteCabecera cab  INNER JOIN ccc.cuentaCobrarCabecera cue INNER JOIN cue.venta ven  where cab.id=:id order by ccc.id desc")
	public List<CobrosCliente> findByCobrosDetalladoPorIdCabecera(@Param("id") int id);
}
