package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.ProduccionCostoCabecera;

@Repository
public interface ProduccionCostoCabeceraRepository extends JpaRepository<ProduccionCostoCabecera, Serializable> {
	public abstract ProduccionCostoCabecera findTop1ByOrderByIdDesc();
	
	@Query(value ="select * from Produccion_Costo_Cabecera c where producto_id=:id",nativeQuery = true)
	public abstract ProduccionCostoCabecera getPorIdProd(@Param("id") int id);
}
