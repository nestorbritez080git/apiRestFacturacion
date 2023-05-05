package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.GastoConsumicionesDetalle;

public interface GastoConsumicionesDetalleRepository extends JpaRepository<GastoConsumicionesDetalle, Serializable> {
	@Query(value="select det.id as idDet, cab.id as idCab, con.id as idCon, det.descripcion, det.monto, det.observacion, det.comprobante as comp from gasto_consumiciones_detalle det inner join gasto_consumiciones_cabecera cab on det.gasto_consumiciones_cabecera_id=cab.id inner join consumiciones con on con.id=det.consumiciones_id where cab.id=:id",nativeQuery=true)
	List<Object[]> lista(@Param("id") int id);
}
