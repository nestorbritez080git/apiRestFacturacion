package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Marca;



public interface MarcaRepository extends JpaRepository<Marca, Serializable>{
	public abstract List<Marca> findByOrderByIdDesc();
	public abstract Marca findTop1ByOrderByIdDesc();
	@Query("select d from Marca d where d.descripcion like :descripcion%")
	List<Marca> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Marca findByDescripcion(String descripcion);
	public abstract List<Marca>findTop30ByOrderByIdDesc();
	
	@Query(value = "select count(m.id) FROM Marca m",nativeQuery = true)
	Object[] findByMarca();
}
