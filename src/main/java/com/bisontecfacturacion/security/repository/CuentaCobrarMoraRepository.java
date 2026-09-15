package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.CuentaCobrarMora;

public interface CuentaCobrarMoraRepository  extends JpaRepository<CuentaCobrarMora, Serializable>{
	@Query("select a from CuentaCobrarMora a where cuenta_cobrar_detalle_id=:idDetalle order by id asc")
	public List<CuentaCobrarMora> getCuentaCobrarMoraPorDetalle(@Param("idDetalle")int idDetalle);
}
