package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisontecfacturacion.security.model.TransferenciaCajaMayorCajaActiva;

public interface TransferenciaCajaMayorCajaActivaRepository extends JpaRepository<TransferenciaCajaMayorCajaActiva,Serializable> {
//	@Query(value="select t.id as id, t.fecha as fec, cm.descripcion as origen, ch.descripcion as destino, pt.nombre as nomT, pt.apellido as apeT,  t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_caja_mayor t inner join funcionario ft on ft.id=t.funcionariot_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join caja_chica  ch on ch.id=t.caja_chica_id where t.caja_mayor_id=1",nativeQuery=true)
//	List<Object[]>  consultarDetalleTransferenciaCajaMayor();
//	
//	@Query(value="select t.id as id, t.fecha as fec, cm.descripcion as origen, ch.descripcion as destino, pt.nombre as nomT, pt.apellido as apeT,  t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_caja_mayor t inner join funcionario ft on ft.id=t.funcionariot_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join caja_chica  ch on ch.id=t.caja_chica_id where t.caja_chica_id=:id",nativeQuery=true)
//	List<Object[]>  consultarDetalleTransferenciaCajaMayorPorIdCajaChica(@Param("id")int id);
	@Query(value="select tf.id as id,tf.fecha as fecha, pft.nombre as nom, pft.apellido as ape, cjm.id as idCjm, cjm.descripcion as desCjm, aper.id as idAper, pfAper.nombre as nomPfaper, pfAper.apellido as apePfaper, tf.monto as mon, tf.monto_cheque as monChe, tf.monto_tarjeta as monTarj  from transferencia_caja_mayor_caja_activa tf inner join funcionario ft on ft.id=tf.funcionario_id inner join persona pft on pft.id=ft.persona_id inner join apertura_caja aper on aper.id=tf.apertura_caja_id inner join funcionario fAper on fAper.id=aper.funcionario_id inner join persona pfAper on pfAper.id=fAper.persona_id inner join caja_mayor cjm on cjm.id=tf.caja_mayor_id where caja_mayor_id=1 ORDER BY tf.id DESC",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaCajaMayorCajaActiva();
}
