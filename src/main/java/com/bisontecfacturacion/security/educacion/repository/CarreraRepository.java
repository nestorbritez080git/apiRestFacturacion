package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;

public interface CarreraRepository extends JpaRepository<Carrera, Serializable> {
	@Query(value="select * from carrera p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
	List<Carrera>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
}
