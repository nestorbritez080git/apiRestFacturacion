package com.bisontecfacturacion.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);
    
	Usuario findByPassword(String password);
	 
  	public abstract Usuario findTop1ByOrderByIdDesc();
  	
  	@Modifying 
	@Transactional(readOnly=false)
	@Query("UPDATE Usuario SET enabled=:enabled, username=:username, funcionario_id=:funcionario_id, administrador=:administrador WHERE id=:id")
	public void findByActualizarUsuario(@Param("enabled") boolean enabled, @Param("username") String username,  @Param("funcionario_id") int funcionario_id,  @Param("administrador") boolean administrador, @Param("id") Long id);
  	
  	@Modifying 
	@Transactional(readOnly=false)
	@Query("UPDATE Usuario SET password=:password WHERE id=:id")
	public void findByActualizarPassword(@Param("password") String password, @Param("id") Long id);
}
