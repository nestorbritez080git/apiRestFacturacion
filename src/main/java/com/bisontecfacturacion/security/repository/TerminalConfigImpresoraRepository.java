package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.config.TerminalConfigImpresora;

public interface TerminalConfigImpresoraRepository  extends JpaRepository<TerminalConfigImpresora, Serializable> {
//	@Query(value = "select * from terminal_config_impresora f where numero_terminal=:id ", nativeQuery = true )
//	public TerminalConfigImpresora consultarTerminal(@Param("id") int id);
//	
	@Query("SELECT c FROM TerminalConfigImpresora c LEFT JOIN FETCH c.autoImpresor WHERE c.numeroTerminal = :idTerminal")
	public TerminalConfigImpresora consultarTerminalEmisonFacturaPorTerminales(@Param("idTerminal") int idTerminal);
	
	@Query("SELECT c FROM TerminalConfigImpresora c WHERE c.numeroTerminal = :idTerminal")
	public TerminalConfigImpresora consultarTerminalPorNumeros(@Param("idTerminal") int idTerminal);

	
	@Query(value="SELECT * FROM terminal_config_impresora WHERE numero_terminal = :idTerminal", nativeQuery = true)
	public TerminalConfigImpresora consultarTerminalPorNumeroTerminalSql(@Param("idTerminal") int idTerminal);

	@Query("SELECT c FROM TerminalConfigImpresora c LEFT JOIN FETCH c.autoImpresor")
	public List<TerminalConfigImpresora> getAllTerminal();
	
	@Query("SELECT c FROM TerminalConfigImpresora c WHERE c.numeroTerminal = :idTerminal")
	public TerminalConfigImpresora consultarTerminalPorNumerosSql(@Param("idTerminal") int idTerminal);
	

}
