

package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;

public interface PresupuestoDetalleServicioRepository  extends JpaRepository<DetallePresupuestoServicio, Serializable>{
	@Query(value="select "
			+ "detalle_servicios.id as detalleServicioId, "
			+ "detalle_servicios.servicio_id, "
			+ "detalle_servicios.descripcion, "
			+ "detalle_servicios.cantidad, "
			+ "detalle_servicios.precio, "
			+ "detalle_servicios.sub_total, "
			+ "detalle_servicios.presupuesto_id, "
			+ "detalle_servicios.iva, "
			+ "fun.id, "
			+ "pf.nombre, "
			+ "pf.apellido, "
			+ "detalle_servicios.obs  from detalle_presupuesto_servicio as detalle_servicios inner join funcionario fun on detalle_servicios.funcionario_id=fun.id inner join persona pf on pf.id=fun.persona_id  where detalle_servicios.presupuesto_id=:id",nativeQuery=true)
	List<Object[]> listarDetallePresupuesto(@Param("id") int id);

}
