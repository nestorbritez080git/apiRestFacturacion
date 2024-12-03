package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.CobrosMatriculacion;
import com.bisontecfacturacion.security.educacion.model.ConceptoMatriculacion;
import com.bisontecfacturacion.security.educacion.model.Procedencia;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.educacion.model.Turno;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;

public interface ConceptoMatriculacionRepository extends JpaRepository<ConceptoMatriculacion, Serializable> {
	@Query(value="select * from concepto_matriculacion p where p.descripcion like :filtro order by p.id desc",nativeQuery=true)
	List<ConceptoMatriculacion>  getBuscarPorFiltro(@Param("filtro") String filtro);
}
