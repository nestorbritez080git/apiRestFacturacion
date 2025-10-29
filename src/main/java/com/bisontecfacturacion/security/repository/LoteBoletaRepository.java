package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.LoteBoleta;

@Repository
public interface LoteBoletaRepository extends JpaRepository<LoteBoleta, Serializable> {
	 public abstract LoteBoleta findTop1ByOrderByIdAsc();
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update LoteBoleta lb set lb.numeroActual = :numeroActual where lb.id = :id")
	 public void actualizarNumeroActual(@Param("numeroActual") String numeroActual,@Param("id") int id);
	 @Query("SELECT l FROM LoteBoleta l ORDER BY l.id DESC")
	 @Lock(LockModeType.PESSIMISTIC_WRITE)
	 LoteBoleta findTop1ByOrderByIdDescForUpdate();
}
