package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;

public interface CategoriaHabitacionesRepository extends JpaRepository<CategoriaHabitaciones, Serializable> {
	@Query(value="select * from categoria_habitaciones p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
	List<CategoriaHabitaciones>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
}
