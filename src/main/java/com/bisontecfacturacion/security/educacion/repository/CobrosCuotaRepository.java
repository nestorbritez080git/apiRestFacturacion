package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.CobrosCuota;
import com.bisontecfacturacion.security.educacion.model.CobrosMatriculacion;
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Venta;

public interface CobrosCuotaRepository extends JpaRepository<CobrosCuota, Serializable> {
//	@Query(value="select * from categoria_habitaciones p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
//	List<TipoCarrera>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
	@Query("select  c from CobrosCuota c  order by c.id desc")
	public List<CobrosCuota> getAll();
	public abstract CobrosCuota findTop1ByOrderByIdDesc();
	@Query(value="select * from cobros_cuota v order by v.id desc limit 1", nativeQuery = true)
	CobrosCuota getUltimaCobrosCuota();
	@Query(value="select * from cobros_cuota m  INNER JOIN alumno a ON a.id=m.alumno_id inner join persona p ON p.id=a.persona_id where p.nombre like :filtro or p.apellido like :filtro  OR p.cedula like :filtro order by p.id desc",nativeQuery=true)
	List<CobrosCuota>  getBuscarPorFiltro(@Param("filtro") String filtro);
	
}
