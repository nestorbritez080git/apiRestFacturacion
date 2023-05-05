package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.LoteTicket;

@Repository
public interface LoteTicketRepository extends JpaRepository<LoteTicket, Serializable>{
	 public abstract LoteTicket findTop1ByOrderByIdAsc();
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update LoteTicket set numeroActual=:numeroActual where id=:id")
	 public void actualizarNumeroActual(@Param("numeroActual") String numeroActual,@Param("id") int id);
}
