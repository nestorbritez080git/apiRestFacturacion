package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.ProduccionMateriaPrima;

public interface ProduccionMateriaPrimaRepository extends JpaRepository<ProduccionMateriaPrima, Serializable>{
	
	@Query("select p from ProduccionMateriaPrima p where produccion_costo_cabecera_id=:id")
	List<ProduccionMateriaPrima> getProduccionMateriaPrimiaIdCabecera(@Param("id") int id);

}

