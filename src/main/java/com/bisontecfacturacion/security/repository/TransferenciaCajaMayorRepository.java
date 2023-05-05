package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaCajaChica;
import com.bisontecfacturacion.security.model.TransferenciaCajaMayor;

public interface TransferenciaCajaMayorRepository extends JpaRepository<TransferenciaCajaMayor,Serializable> {
	@Query(value="select t.id as id, t.fecha as fec, cm.descripcion as origen, ch.descripcion as destino, pt.nombre as nomT, pt.apellido as apeT,  t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_caja_mayor t inner join funcionario ft on ft.id=t.funcionariot_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join caja_chica  ch on ch.id=t.caja_chica_id where t.caja_mayor_id=1",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaCajaMayor();
	
	@Query(value="select t.id as id, t.fecha as fec, cm.descripcion as origen, ch.descripcion as destino, pt.nombre as nomT, pt.apellido as apeT,  t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_caja_mayor t inner join funcionario ft on ft.id=t.funcionariot_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join caja_chica  ch on ch.id=t.caja_chica_id where t.caja_chica_id=:id",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaCajaMayorPorIdCajaChica(@Param("id")int id);
}
