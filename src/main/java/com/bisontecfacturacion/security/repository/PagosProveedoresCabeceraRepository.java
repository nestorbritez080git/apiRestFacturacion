package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.PagosProveedorCabecera;


public interface PagosProveedoresCabeceraRepository extends JpaRepository<PagosProveedorCabecera, Serializable>{

	@Query(value="select * from pago_proveedor_cabecera v order by v.id desc limit 1", nativeQuery = true)
	PagosProveedorCabecera getUltimoPagosProveedorCab();
	public abstract PagosProveedorCabecera findTop1ByOrderByIdDesc();
	
	@Query(value = "select c from PagosProveedorCabecera c order by id desc")
	public List<PagosProveedorCabecera> getPagosProveedoresCabeceraAll();
	
	@Query("select  c from PagosProveedorCabecera c where id=:id order by id desc")
	public PagosProveedorCabecera buscarPagosCabeceraPorId(@Param("id") int id);

	
	
}
