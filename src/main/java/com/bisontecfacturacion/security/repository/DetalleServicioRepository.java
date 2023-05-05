package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DetalleServicios;

public interface DetalleServicioRepository extends JpaRepository<DetalleServicios, Serializable> {
	
	@Query(value="select detalle_servicios.id as detalleServicioId,"
			+ "detalle_servicios.servicio_id,"
			+ "detalle_servicios.descripcion,"
			+ "detalle_servicios.cantidad,"
			+ "detalle_servicios.precio,"
			+ "detalle_servicios.sub_total,"
			+ " detalle_servicios.venta_id, "
			+ "detalle_servicios.iva, "
			+ "fun.id, "
			+ "pf.nombre, "
			+ "pf.apellido, "
			+ "detalle_servicios.monto_iva, "
			+ "detalle_servicios.obs  from detalle_servicios inner join funcionario fun on detalle_servicios.funcionario_id=fun.id inner join persona pf on pf.id=fun.persona_id  where detalle_servicios.venta_id=:id",nativeQuery=true)
	List<Object[]> lista(@Param("id") int id);
	
	@Query(value = " select det.id as detalleServicioId, det.servicio_id, det.descripcion, det.cantidad, det.precio, det.sub_total, det.venta_id, det.iva, fun.id, pf.nombre, pf.apellido,  det.monto_iva, det.obs  "
			+ "from detalle_servicios  det "
			+ "inner join venta v on v.id=det.venta_id "
			+ "inner join funcionario fun on det.funcionario_id=fun.id "
			+ "inner join persona pf on pf.id=fun.persona_id "
			+ "where v.estado='FACTURADO' AND  ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura<= :fecha_fin ))", nativeQuery = true)
    List<Object []> getResumenVentaServicioRagoFechaDetallado( @Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    
	@Query(value = " select det.id as detalleServicioId, det.servicio_id, det.descripcion, det.cantidad, det.precio, det.sub_total, det.venta_id, det.iva, fun.id, pf.nombre, pf.apellido,  det.monto_iva, det.obs  "
			+ "from detalle_servicios  det "
			+ "inner join venta v on v.id=det.venta_id "
			+ "inner join funcionario fun on det.funcionario_id=fun.id "
			+ "inner join persona pf on pf.id=fun.persona_id "
			+ "where fun.id=:idFunc and   v.estado='FACTURADO' AND  ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura<= :fecha_fin ))", nativeQuery = true)
    List<Object []> getResumenVentaServicioRagoFechaDetalladoPorFuncionario( @Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idFunc") int idFunc);
    
    
    
    
    @Query(value = "select SUM(v.total) as totalVenta "
    		+ "from detalle_servicios  det "
			+ "inner join venta v on v.id=det.venta_id "
			+ "inner join funcionario fun on det.funcionario_id=fun.id "
			+ "inner join persona pf on pf.id=fun.persona_id "
			+ "where v.estado='FACTURADO' AND  ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <= :fecha_fin ))", nativeQuery = true)
    Object [][] getResumenVentaServicioRagoFecha( @Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    

}
