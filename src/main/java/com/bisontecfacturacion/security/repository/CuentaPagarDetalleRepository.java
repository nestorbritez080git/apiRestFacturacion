package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CuentaPagarDetalle;


@Repository
public interface CuentaPagarDetalleRepository extends JpaRepository<CuentaPagarDetalle, Serializable> {
	
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update CuentaPagarDetalle set importe=:importe where cuenta_pagar_cabecera_id=:id")
	 public void cancelarCuentaDetalle(@Param("importe") double importe, @Param("id") int id);
	 
	 @Query("select c from CuentaPagarDetalle c where cuenta_pagar_cabecera_id=:id order by id asc")
	 public List<CuentaPagarDetalle> getDetalleXIdCabecera(@Param("id") int id);

	 @Query(value= "select det.numero_cuota as cuota, det.fecha_vencimiento as vencimiento, det.monto as monto, det.importe as importe, det.sub_total as subtotal, det.cuenta_pagar_cabecera_id from cuenta_pagar_detalle det where det.cuenta_pagar_cabecera_id=:id",nativeQuery = true)
	 List<Object[]> getCuentaDetallePorCabecera(@Param("id")int id);
	 
	 @Query(value= "select det.descripcion as desc, det.cantidad as cant, det.precio_costo as costo, det.sub_total as subtotal from detalle_compra det inner join compra  c on det.compra_id=c.id where c.id=:idCompra",nativeQuery = true)
	 List<Object[]> getDetalleCompraCuenta(@Param("idCompra")int idCompra);
	 
	 @Query(value ="select dv.descripcion as des, dv.precio_costo as precio, dv.cantidad as cantidad, dv.sub_total as subtotal, um.descripcion, dv.id as idDetalle, p.id as idProducto from cuenta_pagar_cabecera ccc inner join compra v on ccc.compra_id=v.id inner join detalle_compra dv on dv.compra_id=v.id inner join producto p on dv.producto_id=p.id inner join unidad_medida um on p.unidad_medida_id=um.id where ccc.id=:idCabecera", nativeQuery = true)
	 List<Object[]> getDetalleProductoCompraCuenta(@Param("idCabecera") int idCabecera);
	 
	 @Query(value ="select cuenta_pagar_cabecera.fraccion_cuota, cuenta_pagar_detalle.numero_cuota, cuenta_pagar_detalle.fecha_vencimiento, cuenta_pagar_detalle.monto, cuenta_pagar_detalle.importe, cuenta_pagar_detalle.id, cuenta_pagar_detalle.sub_total, cuenta_pagar_detalle.cuenta_pagar_cabecera_id from cuenta_pagar_detalle inner join cuenta_pagar_cabecera on cuenta_pagar_cabecera.id=cuenta_pagar_detalle.cuenta_pagar_cabecera_id  where cuenta_pagar_detalle.cuenta_pagar_cabecera_id=:idCabecera order by cuenta_pagar_detalle.numero_cuota ASC", nativeQuery = true)
	 List<Object[]> consultarDetalleCuentaPorIdCabecera(@Param("idCabecera") int idCabecera);
		
	 
}
