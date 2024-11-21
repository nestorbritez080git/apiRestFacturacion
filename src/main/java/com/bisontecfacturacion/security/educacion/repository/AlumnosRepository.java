package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Alumnos;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.Funcionario;

public interface AlumnosRepository extends JpaRepository<Alumnos, Serializable> {

	@Query("select f from  Alumnos f")
	public List<Alumnos> getAlumnos();
	
	@Query("select f from Alumnos f where persona_id=:id ")
	public Alumnos getIdPersona(@Param("id") int id);
	
	@Query("select f from Alumnos f where persona_id=:id and f.id <>:idA")
	public Alumnos getIdPersonaEditar(@Param("id") int id, @Param("idA") int idA);
	
	 public abstract List<Alumnos>findTop100ByOrderByIdDesc();
	 public abstract Alumnos findTop1ByOrderByIdAsc();
	 
	

}
