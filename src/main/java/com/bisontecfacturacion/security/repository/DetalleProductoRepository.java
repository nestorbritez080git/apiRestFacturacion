package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DetalleProducto;

public interface DetalleProductoRepository extends JpaRepository<DetalleProducto, Serializable> {
	@Query(value="select dp.id as detalleId, dp.producto_id as productoId ,dp.descripcion, dp.cantidad,dp.iva, dp.precio, dp.sub_total, dp.venta_id, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, dp.descuento, unidad_medida.descripcion as unidad, p.existencia, dp.is_balanza,p.codbar as procodbar, m.descripcion as descrimarca, dp.monto_iva as monIva, dp.costo, dp.tipo_precio as tpPrecio from detalle_producto dp inner join producto p on dp.producto_id=p.id inner join unidad_medida on p.unidad_medida_id=unidad_medida.id inner join marca m on p.marca_id=m.id where dp.venta_id=:id",nativeQuery=true)
	List<Object[]> lista(@Param("id") int id);
	
	
	
	@Query(value = "DELETE FROM DetalleProducto where venta_id =:idVenta")
	public void eliminarDetallePorVentaId(@Param("idVenta") int idVenta );

	@Query(value="select detalle_producto.descripcion as desc,"
	+ "marca.descripcion as descriMarca ,"
	+ "sum(detalle_producto.cantidad) as cantidad,"
	+ "unidad_medida.descripcion as descu,"
	+ "sum(detalle_producto.precio)/sum(detalle_producto.cantidad) as precio_costo," 
	+ "sum(detalle_producto.sub_total) as precio,"
	+ "(sum(detalle_producto.precio)-sum(producto.precio_costo)) as utilidad from detalle_producto "
	+ "inner join producto on detalle_producto.producto_id=producto.id "
	+ "inner join marca on producto.marca_id=marca.id "
	+ "inner join unidad_medida on producto.unidad_medida_id=unidad_medida.id "
	+ "inner join venta as v on detalle_producto.venta_id=v.id "
	+ "where (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO') group by detalle_producto.descripcion, unidad_medida.descripcion," 
	+ "marca.descripcion, unidad_medida.descripcion order by cantidad desc limit :limite",nativeQuery=true)
	List<Object[]> rankingProductoVentaCantidad(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("limite") int limite);
	
	@Query(value="select detalle_producto.descripcion as desc,"
	+ "marca.descripcion as descriMarca ,"
	+ "sum(detalle_producto.cantidad) as cantidad,"
	+ "unidad_medida.descripcion as descu,"
	+ "sum(detalle_producto.precio)/sum(detalle_producto.cantidad) as precio_costo," 
	+ "sum(detalle_producto.sub_total) as precio,"
	+ "(sum(detalle_producto.precio)-sum(producto.precio_costo)) as utilidad from detalle_producto "
	+ "inner join producto on detalle_producto.producto_id=producto.id "
	+ "inner join marca on producto.marca_id=marca.id "
	+ "inner join unidad_medida on producto.unidad_medida_id=unidad_medida.id "
	+ "inner join venta as v on detalle_producto.venta_id=v.id "
	+ "where (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO') group by detalle_producto.descripcion, unidad_medida.descripcion," 
	+ "marca.descripcion, unidad_medida.descripcion order by precio_costo desc limit :limite",nativeQuery=true)
	List<Object[]> rankingProductoVentaPrecio(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("limite") int limite);
	
	@Query(value="select detalle_producto.descripcion as desc,"
	+ "marca.descripcion as descriMarca ,"
	+ "sum(detalle_producto.cantidad) as cantidad,"
	+ "unidad_medida.descripcion as descu,"
	+ "sum(detalle_producto.precio)/sum(detalle_producto.cantidad) as precio_costo," 
	+ "sum(detalle_producto.sub_total) as precio,"
	+ "(sum(detalle_producto.precio)-sum(producto.precio_costo)) as utilidad from detalle_producto "
	+ "inner join producto on detalle_producto.producto_id=producto.id "
	+ "inner join marca on producto.marca_id=marca.id "
	+ "inner join unidad_medida on producto.unidad_medida_id=unidad_medida.id "
	+ "inner join venta as v on detalle_producto.venta_id=v.id "
	+ "where (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO') group by detalle_producto.descripcion, unidad_medida.descripcion," 
	+ "marca.descripcion, unidad_medida.descripcion order by utilidad desc limit :limite",nativeQuery=true)
	List<Object[]> rankingProductoVentaUtilidad(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("limite") int limite);
	
	
	
	@Query(value="select dp.venta_id nroventa, dp.descripcion as descripcion, p.codbar as codigobarra, m.descripcion as marca, dp.cantidad as cantidad, dp.precio as precio, dp.descuento as descuento, dp.sub_total as subtotal  \n" + 
			"from detalle_producto dp " + 
			"inner join producto p on dp.producto_id=p.id " + 
			"inner join venta  v on v.id=dp.venta_id " + 
			"inner join unidad_medida on p.unidad_medida_id=unidad_medida.id " + 
			"inner join marca m on p.marca_id=m.id " + 
			"where (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO')",nativeQuery=true)
	List<Object[]> listaDetalleProductoAll(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	@Query(value="select p.id as idP, dp.descripcion as descripcion, p.codbar as codigobarra, m.descripcion as marca, dp.cantidad as cantidad, dp.precio as precio, dp.descuento as descuento, dp.sub_total as subtotal, p.iva as iva  \n" + 
			"from detalle_producto dp " + 
			"inner join producto p on dp.producto_id=p.id " + 
			"inner join venta  v on v.id=dp.venta_id " +
			"inner join cliente cl ON cl.id= v.cliente_id " + 
			"inner join unidad_medida on p.unidad_medida_id=unidad_medida.id " + 
			"inner join marca m on p.marca_id=m.id " + 
			"where cl.id=:idC AND (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO')",nativeQuery=true)
	List<Object[]> listaDetalleProductoAllPorCliente(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idC") int id);
	
	
	
	@Query(value="SELECT ser.id as idS, dp.descripcion as des, dp.cantidad as cant, dp.precio as precio, dp.sub_total as subtotal from detalle_servicios dp " + 
			"inner join venta  v on v.id=dp.venta_id " + 
			"inner join servicio ser ON ser.id= dp.servicio_id " +
			"inner join cliente cl ON cl.id= v.cliente_id " +
			"where cl.id=:idC AND (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO')",nativeQuery=true)
	List<Object[]> listaDetalleServicioAllPorCliente(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idC") int id);
	
	
	
	
	@Query(value="select dp.venta_id nroventa, dp.descripcion as descripcion, p.codbar as codigobarra, m.descripcion as marca, dp.cantidad as cantidad, dp.precio as precio, dp.descuento as descuento, dp.sub_total as subtotal  from detalle_producto dp inner join producto p on dp.producto_id=p.id inner join unidad_medida on p.unidad_medida_id=unidad_medida.id inner join marca m on p.marca_id=m.id inner join venta  v on v.id=dp.venta_id where (v.fecha >= :fecha_inicio AND v.estado='FACTURADO') AND (v.fecha <=  :fecha_fin AND v.estado='FACTURADO') AND dp.descripcion ilike :desc order by dp.venta_id desc",nativeQuery=true)
	List<Object[]> listaDetalleProductoDesc(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("desc") String desc);
	
	@Query(value="SELECT sum(det.costo)as costoTotal, sum(det.sub_total)as subtotal, sum(det.sub_total - det.costo) as utilidad  from detalle_producto det  inner join venta v on v.id=det.venta_id inner join operacion_caja op on op.id=v.operacion_caja inner join apertura_caja ap on ap.id=op.apertura_caja_id where ap.id=:id and v.tipo='1' and v.estado='FACTURADO'",nativeQuery=true)
	Object[][] getResumenUtilidad(@Param("id") int id);
	
	
}
