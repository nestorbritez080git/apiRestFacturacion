package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.DevolucionVenta;
import com.bisontecfacturacion.security.model.Venta;
@Repository
public interface AnulacionesVentaRepository extends JpaRepository<AnulacionesVenta, Serializable>{
	@Query(value="select v.id from anulaciones_venta v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaDevolucion();
	@Query(value="select * from anulaciones_venta v order by v.id desc limit 1", nativeQuery = true)
	AnulacionesVenta getAnulacionVentaUlt();
	
	@Query(value="select v.id, v.fecha, v.total, pf.nombre || ' ' || pf.apellido as nombreFuncionario, v.motivo, v.venta_id from anulaciones_venta v inner join funcionario fun on fun.id=v.funcionario_id  inner join persona pf on pf.id=fun.persona_id", nativeQuery = true)
	List<Object[]> getListadoAnulaciones();

}
