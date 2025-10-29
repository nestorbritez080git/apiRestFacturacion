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

import com.bisontecfacturacion.security.model.LoteTicket;

@Repository
public interface LoteTicketRepository extends JpaRepository<LoteTicket, Serializable>{
	 
	 @Query("SELECT l FROM LoteTicket l ORDER BY l.id DESC")
	 @Lock(LockModeType.PESSIMISTIC_WRITE)
	 LoteTicket findTop1ByOrderByIdDescForUpdate();
	
	public abstract LoteTicket findTop1ByOrderByIdAsc();
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update LoteTicket lt set lt.numeroActual = :numeroActual where lt.id = :id")
	 public void actualizarNumeroActual(@Param("numeroActual") String numeroActual,@Param("id") int id);
}
