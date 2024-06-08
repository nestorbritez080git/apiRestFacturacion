package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.LoteFactura;

public interface AutoImpresorRepository extends JpaRepository<AutoImpresor, Serializable> {
	public abstract List<AutoImpresor>findByOrderByIdDesc();
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update AutoImpresor set numeroActual=:numeroActual where id=:id")
	 public void actualizarNumeroActual(@Param("numeroActual") String numeroActual,@Param("id") int id);
	 
	 @Query(value = "select * from auto_impresor f where numero_terminal=:ter and estado=true", nativeQuery = true )
	public AutoImpresor consultarAutoImpresorTerminal(@Param("ter") int ter);
}
