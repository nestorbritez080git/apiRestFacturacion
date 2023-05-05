package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CajaMayor;
import com.bisontecfacturacion.security.model.Concepto;

public interface ConceptoRepository extends JpaRepository<Concepto, Serializable> {
	
	@Query("select f from Concepto f where id=:id")
	public Concepto consultarIDConcepto(@Param("id") int id);
}
