package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaAnticipo;
import com.bisontecfacturacion.security.model.TransferenciaAperturaCaja;

public interface TransferenciaAperturaCajaRepository  extends JpaRepository<TransferenciaAperturaCaja, Serializable>{
	@Query(value="select t.id as id, t.fecha as fecha, pt.nombre || ' ' || pt.apellido as ptransaccion, ape.id as idAper, pc.nombre || ' ' || pc.apellido as perCajaChica, t.monto, t.monto_cheque, t.monto_tarjeta " + 
			"from transferencia_apertura_caja t " + 
			"inner join funcionario ft on ft.id=t.funcionario_id " + 
			"inner join persona pt on pt.id= ft.persona_id " + 
			"inner join caja_chica ch on t.caja_chica_id=ch.id " + 
			"inner join funcionario fc on fc.id= ch.funcionarioe_id " + 
			"inner join persona pc on pc.id=fc.persona_id " + 
			"inner join apertura_caja ape on ape.id= t.apertura_caja_id where ch.id=:id",nativeQuery=true)
	List<Object[]>  consultarTranferenciaAperturaCajaPorIdCajaChica(@Param("id") int id);
	
	@Query(value="select v.id from transferencia_apertura_caja v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaTransferenciaAperturaCaja();
}
