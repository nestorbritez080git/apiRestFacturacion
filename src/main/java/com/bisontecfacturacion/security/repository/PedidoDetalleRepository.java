package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.PedidoDetalle;

@Transactional(readOnly=true)
@Repository
public interface PedidoDetalleRepository extends  JpaRepository <PedidoDetalle, Serializable>{
    @Query("select c from PedidoDetalle c where pedido_id=:id")
    List<PedidoDetalle> getDetalleXId(@Param("id") int id);
    
    @Modifying
    @Transactional(readOnly=false)
    @Query(value="UPDATE pedido_detalle SET cantidad=:cantidad, descripcion=:descripcion, iva=:iva, precio=:precio, sub_total=:subTotal, tipo_precio=:tipoPrecio, volumen_acumulado=:volumenAcumulado, producto_id=:idProducto WHERE id=:id", nativeQuery = true)
    public void actualizarDetalle(@Param("cantidad") double cantidad,@Param("descripcion") String descripcion, @Param("iva") String iva,@Param("precio") double precio,@Param("subTotal") double subTotal,@Param("tipoPrecio") String tipoPrecio,@Param("volumenAcumulado") double volumenAcumulado,@Param("idProducto") int idProducto, @Param("id") int id);
}