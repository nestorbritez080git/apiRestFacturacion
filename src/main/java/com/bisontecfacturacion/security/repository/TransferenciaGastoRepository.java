package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaGastos;

public interface TransferenciaGastoRepository extends JpaRepository<TransferenciaGastos, Serializable> {
	@Query(value="SElecT t.id as id, t.fecha, p.nombre as nomF, p.apellido as apeF, pch.nombre as nomPch, pch.apellido as apPch, g.id as idGas, t.monto, t.monto_cheque, t.monto_tarjeta FROM transferencia_gastos t inner join funcionario f on t.funcionario_id=f.id inner join persona p on p.id=f.persona_id inner join gasto_consumiciones_cabecera g on g.id=t.gasto_consumiciones_cabecera_id inner join caja_chica ch on ch.id=t.caja_chica_id inner join funcionario fch on fch.id=ch.funcionarioe_id inner join persona pch on pch.id=fch.persona_id where ch.id=:id ",nativeQuery=true)
	List<Object[]>  consultarTranferenciaGastoPorIdCajaChica(@Param("id")int id);

}
