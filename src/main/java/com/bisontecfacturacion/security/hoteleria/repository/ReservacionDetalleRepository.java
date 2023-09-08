package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.hoteleria.model.ReservacionDetalle;

public interface ReservacionDetalleRepository extends JpaRepository<ReservacionDetalle, Serializable>{
	@Query(value="select dp.id as detalleId, dp.producto_id as productoId ,dp.descripcion, dp.cantidad,dp.iva, dp.precio, dp.sub_total, dp.reservacion_cabecera_id, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, dp.descuento, unidad_medida.descripcion as unidad, p.existencia, dp.is_balanza,p.codbar as procodbar, m.descripcion as descrimarca, dp.monto_iva as monIva, dp.costo, dp.tipo_precio as tpPrecio from reservacion_detalle dp  " + 
				"inner join producto p on dp.producto_id=p.id  " + 
				"inner join unidad_medida on p.unidad_medida_id=unidad_medida.id  " + 
				"inner join marca m on p.marca_id=m.id where dp.reservacion_cabecera_id=:id",nativeQuery=true)
	List<Object[]> getDetallePorIdCabecera(@Param("id") int id);
	
	@Query(value = "DELETE FROM DetalleProducto where venta_id =:idVenta")
	public void eliminarDetallePorVentaId(@Param("idVenta") int idVenta );
}
