package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.Docente;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;

public interface DocenteRepository extends JpaRepository<Docente, Serializable> {
	@Query("select f from  Docente f")
	public List<Alumno> getAlumno();
	
	@Query("select f from Docente f where persona_id=:id ")
	public Alumno getIdPersona(@Param("id") int id);
	
	@Query("select f from Docente f where persona_id=:id and f.id <>:idA")
	public Alumno getIdPersonaEditar(@Param("id") int id, @Param("idA") int idA);
	
	 public abstract List<Docente>findTop100ByOrderByIdDesc();
	 
	 public abstract Docente findTop1ByOrderByIdAsc();
	 
	 @Query(value="select * from docente a inner join persona p ON p.id=a.persona_id where p.nombre like :filtro or p.apellido like :filtro  OR p.cedula like :filtro order by p.id desc",nativeQuery=true)
	 List<Docente>  getBuscarPorFiltro(@Param("filtro") String filtro);
	 
}
