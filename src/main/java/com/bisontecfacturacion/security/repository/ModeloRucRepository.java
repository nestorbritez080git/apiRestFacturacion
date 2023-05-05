package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



import com.bisontecfacturacion.security.model.ModeloRuc;


public interface ModeloRucRepository extends JpaRepository<ModeloRuc, Serializable> {
	
	@Query("select c from ModeloRuc c where ruc=:ruc and dv=:dv")
	public ModeloRuc getModeloRucDv(@Param("ruc") int ruc, @Param("dv") int dv);
	
}
