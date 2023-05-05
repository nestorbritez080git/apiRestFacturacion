package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bisontecfacturacion.security.model.TransferenciaAnticipo;

public interface TransferenciaAnticipoRepository  extends JpaRepository<TransferenciaAnticipo, Serializable>{
	@Query(value="select t.id as id, t.fecha as fec,an.id as idAn ,  pt.nombre as nomT, pt.apellido as apeT, panti.nombre as noPanti, panti.apellido as apePanti, t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_anticipo t inner join funcionario ft on ft.id=t.funcionario_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join anticipo an on an.id=t.anticipo_id inner join funcionario fan on fan.id= an.funcionario_encargado_id inner join persona panti on panti.id=fan.persona_id where t.caja_mayor_id=1",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaAnticipo();
}
