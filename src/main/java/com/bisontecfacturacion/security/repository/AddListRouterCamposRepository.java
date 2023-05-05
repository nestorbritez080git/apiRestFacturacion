package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.auxiliar.AddListRouterCampos;
import com.bisontecfacturacion.security.model.Venta;

public interface AddListRouterCamposRepository extends JpaRepository<AddListRouterCampos, Serializable> {
	@Query(value="select * from add_list_router_campos det inner join add_list_router cab on cab.id=det.add_list_router_id where cab.id=:id", nativeQuery=true)
	List<AddListRouterCampos> getRouterCampos(@Param("id") int id);
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AddListRouterCampos set estado=true WHERE id=:id")
	public void activarCampoVisiblidad(@Param("id") int id);
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AddListRouterCampos set estado=false WHERE id=:id")
	public void desactivarCampoVisiblidad(@Param("id") int id);

}
