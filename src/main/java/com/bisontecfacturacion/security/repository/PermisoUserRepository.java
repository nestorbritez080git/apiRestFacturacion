package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.PermisoUser;
@Transactional(readOnly=true)
public interface PermisoUserRepository extends JpaRepository<PermisoUser, Serializable>{
	
	@Query("select c from PermisoUser c where user_id=:idUser")
	List<PermisoUser> getListPermisoUser(@Param("idUser") long id);
	
	@Query("select pu from PermisoUser pu where user_id=:idUser")
	List<PermisoUser> getListPermisoUserAll(@Param("idUser") long id);
	
	
	@Query(value="select pu.id AS puid, p.descripcion AS pdescrip, pu.estado, pu.user_id, ub.descripcion, p.text, p.id, p.icono AS picono, ub.icono, ub.posicion from permiso_user pu inner join permiso p  on pu.permiso_id=p.id inner join ubicacion_permiso ub on p.ubicacion_permiso_id= ub.id where pu.user_id=:idUser order by ub.posicion asc", nativeQuery=true)
	List<Object []> getListPermisoUserSql(@Param("idUser") long id);
	
	@Query(value="select pu.id AS puId, pe.descripcion AS pdescr, pu.estado, pu.user_id, ub.descripcion, pe.text, pe.id, pe.icono AS picono, ub.icono, ub.posicion from permiso_user pu inner join permiso pe on pu.permiso_id=pe.id inner join ubicacion_permiso ub on pe.ubicacion_permiso_id= ub.id where pu.user_id=:idUser order by pe.id desc", nativeQuery = true)
	List<Object []> getListPermisoUserAllSql(@Param("idUser") long id);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("UPDATE PermisoUser SET estado=:estado WHERE id=:id")
    public void actualizarPermiso(@Param("id") int id,@Param("estado") boolean estado);
	
}
