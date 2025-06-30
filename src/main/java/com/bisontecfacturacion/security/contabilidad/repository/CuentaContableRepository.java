package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDetalle;
import com.bisontecfacturacion.security.contabilidad.model.ConceptoCuentaContable;
import com.bisontecfacturacion.security.contabilidad.model.CuentaContable;
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;

public interface CuentaContableRepository extends JpaRepository<CuentaContable, Serializable> {


}
