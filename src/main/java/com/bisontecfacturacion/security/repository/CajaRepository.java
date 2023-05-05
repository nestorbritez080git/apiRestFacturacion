package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Caja;
@Transactional
public interface CajaRepository extends JpaRepository<Caja, Serializable> {
	@Query("select c from Caja c where c.estado=false")
	List<Caja> listCajaFalse();
	@Query("select c from Caja c where c.estado=true")
	List<Caja> listCajaTrue();
	
	@Query("select c from Caja c where c.descripcion like :descripcion%")
	List<Caja> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
	public abstract Caja findByDescripcion(String descripcion);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Caja set estado=:estado where id=:id")
    public void findByActualizaEstado(@Param("id") int id, @Param("estado") Boolean estado);
	
	
}
