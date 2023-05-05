package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;

@Transactional(readOnly=true)
@Repository
public interface CuentaAcobrarDetalleRepository extends JpaRepository<CuentaCobrarDetalle, Serializable> {
	@Query(value ="select cuenta_cobrar_cabecera.fraccion_cuota, cuenta_cobrar_detalle.numero_cuota, cuenta_cobrar_detalle.fecha_vencimiento, cuenta_cobrar_detalle.monto, cuenta_cobrar_detalle.importe, cuenta_cobrar_detalle.interes_mora, cuenta_cobrar_detalle.id, cuenta_cobrar_detalle.sub_total, cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id from cuenta_cobrar_detalle inner join cuenta_cobrar_cabecera on cuenta_cobrar_cabecera.id=cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id  where cuenta_cobrar_detalle.cuenta_cobrar_cabecera_id=:idCabecera order by cuenta_cobrar_detalle.numero_cuota ASC ", nativeQuery = true)
	List<Object[]> consultarDetalleCuentaPorIdCabecera(@Param("idCabecera") int idCabecera);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CuentaCobrarDetalle set importe=subTotal, fechaPago=:fecha, estado=:est where cuenta_cobrar_cabecera_id=:id")
	public void liquidarDetalle(@Param("id") int id, @Param("fecha") Date fecha, @Param("est") Boolean est);
	
	@Query(value ="select dv.descripcion as des, dv.precio as precio, dv.cantidad as cantidad, dv.sub_total as subtotal, um.descripcion from cuenta_cobrar_cabecera ccc inner join venta v on ccc.venta_id=v.id inner join detalle_producto dv on dv.venta_id=v.id inner join producto p on dv.producto_id=p.id inner join unidad_medida um on p.unidad_medida_id=um.id where ccc.id=:idCabecera", nativeQuery = true)
	List<Object[]> getDetalleProductoVentaCuenta(@Param("idCabecera") int idCabecera);
	
	@Query(value ="select ds.descripcion as des, ds.precio as precio, ds.cantidad as cantidad, ds.sub_total as subtotal from cuenta_cobrar_cabecera ccc inner join venta v on ccc.venta_id=v.id inner join detalle_servicios ds on ds.venta_id=v.id where ccc.id=:idCabecera", nativeQuery = true)
	List<Object[]> getDetalleServiciosVentaCuenta(@Param("idCabecera") int idCabecera);
//	
	@Modifying
	@Transactional(readOnly=false)
	@Query(value = "DELETE FROM CuentaCobrarDetalle where cuenta_cobrar_cabecera_id =:idVenta")
	public void eliminarDetalleCuentaPorCabeceraId(@Param("idVenta") int idVenta );
	
	

}
