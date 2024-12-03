package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;

public interface AlumnoRepository extends JpaRepository<Alumno, Serializable> {

	@Query("select f from  Alumno f")
	public List<Alumno> getAlumno();
	
	@Query("select f from Alumno f where persona_id=:id ")
	public Alumno getIdPersona(@Param("id") int id);
	
	@Query("select f from Alumno f where persona_id=:id and f.id <>:idA")
	public Alumno getIdPersonaEditar(@Param("id") int id, @Param("idA") int idA);
	
	 public abstract List<Alumno>findTop100ByOrderByIdDesc();
	 
	 public abstract Alumno findTop1ByOrderByIdAsc();
	 
	 @Query(value="select * from alumno a inner join persona p ON p.id=a.persona_id where p.nombre like :filtro or p.apellido like :filtro  OR p.cedula like :filtro order by p.id desc",nativeQuery=true)
	 List<Alumno>  getBuscarPorFiltro(@Param("filtro") String filtro);
	 
	

}
