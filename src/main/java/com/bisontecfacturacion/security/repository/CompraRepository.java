package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Venta;


public interface CompraRepository extends JpaRepository<Compra,  Serializable>{
    public abstract Compra findTop1ByOrderByIdDesc();
    
    @Query(value="select * from Compra v order by v.id desc limit 1", nativeQuery = true)
	Compra getUltimaCompra();
    
    @Query(value = "select * from compra c where proveedor_id =:id order by c.id desc limit 1",nativeQuery = true)
    Compra getUltimoDocumento(@Param("id") int id);
    		
    @Query(value = "select sum(total) as total from compra v where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia",nativeQuery = true)
    Object[] findByTotalCompra(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
 
    @Query(value="select * from compra v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc",nativeQuery=true)
	List<Compra> getCompra(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
    
    @Query(value="select * from compra v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id where extract(year from cast(v.fecha_factura as Date))=:ano AND extract(month from cast(v.fecha_factura as Date))=:mes AND extract(day from cast(v.fecha_factura as Date))=:dia order by v.id desc",nativeQuery=true)
   	List<Compra> getCompraFechaFactura(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
       
    
    @Transactional(readOnly = true)
    public Page<Compra> findByOrderByIdDesc(Pageable pageable);		
   
    @Query(value = "select sum(c.total)as costoTotal from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and pro.id=:idPro and ((c.fecha >=:fecha_inicio) AND (c.fecha<=:fecha_fin))", nativeQuery = true)
    Object [][] getResumenCompraRagoFechaProveedor(@Param("idPro") int idPro, @Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    @Query(value = "select pfun.nombre || ' ' || pfun.apellido as funcionario, pprov.nombre || ' ' || pprov.apellido as proveedor, doc.descripcion, c.nro_documento, c.fecha, c.total, c.tipo  from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and pro.id=:idPro and ((c.fecha >= :fecha_inicio) AND (c.fecha<= :fecha_fin ))", nativeQuery = true)
    List<Object []> getResumenCompraRagoFechaProveedorDetallado(@Param("idPro") int idPro, @Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    
    
    
    @Query(value = "select sum(c.total)as costoTotal from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and pro.id=:idPro", nativeQuery = true)
    Object [][] getResumenCompraProveedor(@Param("idPro") int idPro);
    @Query(value = "select pfun.nombre || ' ' || pfun.apellido as funcionario, pprov.nombre || ' ' || pprov.apellido as proveedor, doc.descripcion, c.nro_documento, c.fecha, c.total, c.tipo  from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and pro.id=:idPro", nativeQuery = true)
    List<Object []> getResumenCompraProveedorDetallado(@Param("idPro") int idPro);
    
    
   
    @Query(value = "select sum(c.total)as costoTotal from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and ((c.fecha >= :fecha_inicio) AND (c.fecha<= :fecha_fin  ))", nativeQuery = true)
    Object [][] getResumenCompraFecha(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);   
    @Query(value = "select pfun.nombre || ' ' || pfun.apellido as funcionario, pprov.nombre || ' ' || pprov.apellido as proveedor, doc.descripcion, c.nro_documento, c.fecha, c.total, c.tipo from compra c inner join proveedor pro on pro.id=c.proveedor_id inner join persona pprov on pprov.id=pro.persona_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pfun on pfun.id=fun.persona_id inner join documento doc on doc.id=c.documento_id where c.estado='FACTURADO' and ((c.fecha >= :fecha_inicio) AND (c.fecha<= :fecha_fin  ))", nativeQuery = true)
    List<Object []> getResumenCompraFechaDetallado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);   

    @Query(value = "select det.id as id, det.descripcion as descr, det.cantidad as cant,  per.nombre || ' ' || per.apellido as proveedor, det.precio_costo, c.valor_cotizacion as valor, c.fecha_factura as fec  from detalle_compra det INNER JOIN producto p ON p.id=det.producto_id INNER JOIN compra c ON c.id= det.compra_id INNER JOIN proveedor pro ON pro.id=c.proveedor_id INNER JOIN persona per ON per.id= pro.persona_id where p.id=:idProducto AND  c.estado='FACTURADO' ORDER BY det.precio_costo ASC ", nativeQuery = true)
    List<Object []> getRastreoProductoProveedorOrderPrecio(@Param("idProducto") int id);
    @Query(value = "select det.id as id, det.descripcion as descr, det.cantidad as cant,  per.nombre || ' ' || per.apellido as proveedor, det.precio_costo, c.valor_cotizacion as valor, c.fecha_factura as fec  from detalle_compra det INNER JOIN producto p ON p.id=det.producto_id INNER JOIN compra c ON c.id= det.compra_id INNER JOIN proveedor pro ON pro.id=c.proveedor_id INNER JOIN persona per ON per.id= pro.persona_id where p.id=:idProducto AND  c.estado='FACTURADO' ORDER BY c.fecha_factura ASC ", nativeQuery = true)
    List<Object []> getRastreoProductoProveedorOrderFecha(@Param("idProducto") int id);
    

    
    
}