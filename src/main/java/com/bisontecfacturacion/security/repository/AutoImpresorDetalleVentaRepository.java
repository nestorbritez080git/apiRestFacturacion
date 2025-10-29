package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.AutoImpresorDetalleVenta;

public interface AutoImpresorDetalleVentaRepository extends JpaRepository<AutoImpresorDetalleVenta, Serializable>{
			
	@Query("SELECT c FROM AutoImpresorDetalleVenta c JOIN c.venta ven WHERE ven.id =:id")
	public AutoImpresorDetalleVenta consultaDetalleAutoImpresorPorVentaId(@Param("id") int id);
	
	@Query("SELECT c FROM AutoImpresorDetalleVenta c JOIN c.autoImpresor auto WHERE auto.id =:id")
	public List<AutoImpresorDetalleVenta> consultaDetalleAutoImpresorPorCabeceraId(@Param("id") int id);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AutoImpresorDetalleVenta set estado=:estado WHERE id=:id")
	public void findByActualizarEstadoFacturaEmitida(@Param("id") int id, @Param("estado") String estado);
	
}
