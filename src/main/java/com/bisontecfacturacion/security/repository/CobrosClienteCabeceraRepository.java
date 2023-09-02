package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;

public interface CobrosClienteCabeceraRepository extends JpaRepository<CobrosClienteCabecera, Serializable>{
	@Query(value="select * from cobros_cliente_cabecera v order by v.id desc limit 1", nativeQuery = true)
	CobrosClienteCabecera getUltimoCobrosClienteCab();
	
	@Query("select  c from CobrosClienteCabecera c where cliente_id=:id order by id desc")
	public List<CobrosClienteCabecera> findByCuentaPorIdCliente(@Param("id") int id);
	@Query("select  c from CobrosClienteCabecera c where cliente_id=:id and ((fecha >=:fecha_inicio) and (fecha<=:fecha_fin)) order by id desc")
	public List<CobrosClienteCabecera> findByCobrosClientePorRango(@Param("id") int id, @Param("fecha_inicio") Date fecInicio, @Param("fecha_fin") Date fecFin );
	
	
}
