package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.AnulacionesAnticipo;
import com.bisontecfacturacion.security.model.AnulacionesVenta;
import com.bisontecfacturacion.security.model.DevolucionVenta;
import com.bisontecfacturacion.security.model.Venta;
@Repository
public interface AnulacionesAnticipoRepository extends JpaRepository<AnulacionesAnticipo, Serializable>{
	@Query(value="select v.id from anulaciones_anticipo v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaDevolucion();
	@Query(value="select * from anulaciones_anticipo v order by v.id desc limit 1", nativeQuery = true)
	AnulacionesAnticipo getAnulacionUlt();
	
	
}
