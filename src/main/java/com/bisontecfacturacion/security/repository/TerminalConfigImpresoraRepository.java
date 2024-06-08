package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.model.Concepto;

public interface TerminalConfigImpresoraRepository  extends JpaRepository<TerminalConfigImpresora, Serializable> {
	@Query(value = "select * from terminal_config_impresora f where numero_terminal=:id ", nativeQuery = true )
	public TerminalConfigImpresora consultarTerminal(@Param("id") int id);
}
