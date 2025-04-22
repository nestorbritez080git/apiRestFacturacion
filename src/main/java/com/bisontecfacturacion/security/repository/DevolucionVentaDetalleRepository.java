package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;

public interface DevolucionVentaDetalleRepository extends JpaRepository<DevolucionVentaDetalle, Serializable>{
	@Query(value = "select det.id as iddet,det.descripcion as des, ud.descripcion as ud, detprod.cantidad as cant, detprod.precio as precio, detprod.iva as iva, detprod.monto_iva as monIva, detprod.sub_total as subtotal, det.cantidad as cantidadDevol, det.sub_total as subTotalDevol, detprod.id as idDetalle, p.id as idProducto   from devolucion_venta_detalle det inner join detalle_producto detprod on detprod.id=det.detalle_producto_id inner join producto p on p.id= detprod.producto_id inner join unidad_medida ud on ud.id=p.unidad_medida_id where det.devolucion_venta_id=:id", nativeQuery = true)
	List<Object[]> getDetalleDevolucionPorIdCabecera(@Param("id") int id);
	

}
