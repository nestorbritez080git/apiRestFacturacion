package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;

public interface PresupuestoDetalleProductoRepository extends JpaRepository<DetallePresupuestoProducto, Serializable>{
	@Query(value="select dp.id as detalleId, dp.producto_id as productoId ,dp.descripcion, dp.cantidad,dp.iva, dp.precio, dp.sub_total, dp.presupuesto_id, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, dp.descuento, unidad_medida.descripcion as unidad, p.existencia, dp.is_balanza,p.codbar as procodbar, m.descripcion as descrimarca, dp.monto_iva as monIva from detalle_presupuesto_producto dp inner join producto p on dp.producto_id=p.id inner join unidad_medida on p.unidad_medida_id=unidad_medida.id inner join marca m on p.marca_id=m.id where dp.presupuesto_id=:id",nativeQuery=true)
	List<Object[]> lista(@Param("id") int id);
	
}
