package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;

public interface TipoCarreraRepository extends JpaRepository<TipoCarrera, Serializable> {
	@Query(value="select * from tipo_carrera p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
	List<TipoCarrera>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
}
