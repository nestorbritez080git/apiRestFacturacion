package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaCajaChica;

public interface TransferenciaCajaChicaRepository extends JpaRepository<TransferenciaCajaChica,Serializable> {
	@Query(value="select t.id as id, t.fecha as fec, cm.descripcion as origen, ch.descripcion as destino, pt.nombre as nomT, pt.apellido as nopt, pr.nombre as nompr, pr.apellido as apeT,  t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_caja_chica t inner join funcionario ft on ft.id=t.funcionariot_id inner join persona pt on pt.id= ft.persona_id inner join funcionario fe on fe.id=t.funcionarioe_id inner join persona pr on pr.id=fe.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join caja_chica  ch on ch.id=t.caja_chica_id where t.caja_mayor_id=1",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaCajaChica();
	
	@Query("select c from TransferenciaCajaChica c where caja_chica_id=:idCaja")
	public List<TransferenciaCajaChica> getTransferenciaPorIdCajaChica(@Param("idCaja") int idCaja);
}
