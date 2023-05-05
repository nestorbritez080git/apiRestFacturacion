package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.InventarioDetalle;

@Repository
public interface InventarioDetalleRepository extends JpaRepository<InventarioDetalle, Serializable> {

	@Query(value="select * from Inventario_Detalle i_d where inventario_cabecera_id=:id order by i_d.id desc",nativeQuery = true)
	List<InventarioDetalle> getInventarioDetalleIdCabecera(@Param("id") int id);
	
	@Query(value ="select * from Inventario_Detalle i_d where inventario_cabecera_id=:id order by i_d.id desc limit 1",nativeQuery = true)
	List<InventarioDetalle> getInventarioDetalleIdCabeceraUno(@Param("id") int id);
//	select idet.id as id from inventario_detalle idet where idet.inventario_cabecera_id =19 and idet.producto_id=2674 
	public abstract InventarioDetalle findByDescripcion(String descripcion);
	@Query(value ="select * from inventario_detalle idet where idet.inventario_cabecera_id= :idCab and idet.producto_id= :idProd limit 1",nativeQuery = true)
	InventarioDetalle getDetallePorCabProducto(@Param("idCab") int idCab, @Param("idProd") int idProd);
}
