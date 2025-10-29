package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.contabilidad.model.ConceptoCuentaContable;

@Repository
public interface ConceptoCuentaContableRepository extends JpaRepository<ConceptoCuentaContable, Serializable> {
	@Query("SELECT c FROM ConceptoCuentaContable  c WHERE c.concepto.id=:id ORDER BY id DESC")
	public List<ConceptoCuentaContable> consultarConceptoCuentaContablePorIdConcepto(@Param("id") int id);
	
}
