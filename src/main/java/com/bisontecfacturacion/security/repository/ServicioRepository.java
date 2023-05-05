package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Serializable>{
	@Query("select d from Servicio d where d.descripcion like :descripcion%")
	List<Servicio> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract List<Servicio>findTop100ByOrderByIdDesc();
	@Query(value="select * from servicio p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
	List<Servicio>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);

}

