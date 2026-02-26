package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.ProduccionCostoCabecera;
import com.bisontecfacturacion.security.model.Producto;

@Repository
public interface ProduccionCostoCabeceraRepository extends JpaRepository<ProduccionCostoCabecera, Serializable> {
	public abstract ProduccionCostoCabecera findTop1ByOrderByIdDesc();
	
	@Query(value ="select * from Produccion_Costo_Cabecera c where producto_id=:id",nativeQuery = true)
	public abstract ProduccionCostoCabecera getPorIdProd(@Param("id") int id);
	
	@Query(value="select * from produccion_costo_cabecera p where p.produccion_descripcion ilike :descripcion or cast(p.id AS VARCHAR)   ilike :descripcion   order by p.id desc  limit 50",nativeQuery=true)
	List<ProduccionCostoCabecera>  getBuscarProductoProduccionPorDescripcion(@Param("descripcion") String descripcion);
}
