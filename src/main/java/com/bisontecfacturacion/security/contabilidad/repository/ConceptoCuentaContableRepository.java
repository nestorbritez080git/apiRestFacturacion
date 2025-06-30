package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDetalle;
import com.bisontecfacturacion.security.contabilidad.model.ConceptoCuentaContable;
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;

@Repository
public interface ConceptoCuentaContableRepository extends JpaRepository<ConceptoCuentaContable, Serializable> {
	@Query("SELECT c FROM ConceptoCuentaContable  c WHERE c.concepto.id=:id ORDER BY id DESC")
	public List<ConceptoCuentaContable> consultarConceptoCuentaContablePorIdConcepto(@Param("id") int id);
	
}
