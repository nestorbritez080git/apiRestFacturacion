package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDetalle;
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;

public interface AsientoContableDetalleRepositor extends JpaRepository<AsientoContableDetalle, Serializable> {

	List<AsientoContableDetalle> findByAsientoContableId(Integer asientoId);
	@Query("SELECT c FROM AsientoContableDetalle  c WHERE asiento_id=:id ORDER BY id DESC")
	public abstract List<AsientoContableDetalle> consultarAsientoCuentaContable(@Param("id") int id);
	
}
