package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.EmpaqueDetalle;

public interface EmpaqueDetalleRepository extends JpaRepository<EmpaqueDetalle, Serializable>{
	public abstract EmpaqueDetalle findTop1ByOrderByIdDesc();
	@Modifying
	@Query("UPDATE EmpaqueDetalle e SET e.estado = :estado WHERE e.id = :id")
	int findByActualizarEstadoDetalleEmpaque(@Param("id") int id, @Param("estado") String estado);
	

}
