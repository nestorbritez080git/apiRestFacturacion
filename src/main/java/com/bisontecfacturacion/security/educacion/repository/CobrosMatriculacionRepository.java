package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.CobrosMatriculacion;
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Venta;

public interface CobrosMatriculacionRepository extends JpaRepository<CobrosMatriculacion, Serializable> {
//	@Query(value="select * from categoria_habitaciones p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
//	List<TipoCarrera>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
	@Query("select  c from CobrosMatriculacion c  order by c.id desc")
	public List<CobrosMatriculacion> getAll();
	public abstract CobrosMatriculacion findTop1ByOrderByIdDesc();
	@Query(value="select * from cobros_matriculacion v order by v.id desc limit 1", nativeQuery = true)
	CobrosMatriculacion getUltimaMatriculacion();
	@Query(value="select * from cobros_matriculacion m INNER JOIN matriculacion mat ON m.matriculacion_id=mat.id INNER JOIN carrera ca ON mat.carrera_id=ca.id INNER JOIN alumno a ON a.id=m.alumno_id inner join persona p ON p.id=a.persona_id where p.nombre like :filtro or p.apellido like :filtro  OR p.cedula like :filtro OR ca.descripcion like :filtro order by p.id desc",nativeQuery=true)
	List<CobrosMatriculacion>  getBuscarPorFiltro(@Param("filtro") String filtro);
	
	

	
}
