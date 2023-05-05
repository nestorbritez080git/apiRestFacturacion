package com.bisontecfacturacion.security.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.DetalleCompra;


public interface CompraDetalleRepository extends JpaRepository<DetalleCompra, Serializable>{
    @Query(value = "SELECT d.id as detId, p.id as proId, p.codbar, d.descripcion,d.cantidad,d.precio_costo,d.iva,d.sub_total, d.precio_venta_1, d.precio_venta_2, d.precio_venta_3, d.precio_venta_4, d.compra_id, u.descripcion as descripcionDescrip, m.descripcion as marcadesc FROM detalle_compra d INNER JOIN producto p ON d.producto_id=p.id INNER JOIN unidad_medida u ON p.unidad_medida_id=u.id INNER JOIN marca m ON p.marca_id=m.id where d.compra_id=:id order by d.id desc", nativeQuery = true)
    public List<Object[]> getDetallePorCabecera(@Param("id") int id);
}
