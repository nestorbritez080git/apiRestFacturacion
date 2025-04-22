package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaGastos;
import com.bisontecfacturacion.security.model.TransferenciaPagosProveedor;

public interface TransferenciaPagosProveedorRepository extends JpaRepository<TransferenciaPagosProveedor, Serializable> {
	@Query(value="SElecT t.id as id, t.fecha, p.nombre as nomF, p.apellido as apeF, pch.nombre as nomPch, pch.apellido as apPch, g.id as idGas, t.monto, t.monto_cheque, t.monto_tarjeta FROM transferencia_gastos t inner join funcionario f on t.funcionario_id=f.id inner join persona p on p.id=f.persona_id inner join gasto_consumiciones_cabecera g on g.id=t.gasto_consumiciones_cabecera_id inner join caja_chica ch on ch.id=t.caja_chica_id inner join funcionario fch on fch.id=ch.funcionarioe_id inner join persona pch on pch.id=fch.persona_id where ch.id=:id ",nativeQuery=true)
	List<Object[]>  consultarTranferenciaGastoPorIdCajaChica(@Param("id")int id);
	
	@Query(value="select t.id as id, t.fecha as fec, p.id as idPagos ,  pt.nombre as nomT, pt.apellido as apeT, pa.nombre as nomAut, pa.apellido as apeAut, t.monto as mon, t.monto_cheque as monCheque, t.monto_tarjeta as monTarj from transferencia_pagos_proveedor t inner join funcionario ft on ft.id=t.funcionario_id inner join persona pt on pt.id= ft.persona_id inner join caja_mayor cm on cm.id=t.caja_mayor_id inner join pagos_proveedor_cabecera p on p.id=t.pagos_proveedor_cabecera_id inner join funcionario fa on fa.id=p.funcionarioa_id inner join persona pa on pa.id= fa.persona_id where t.caja_mayor_id=1",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciaPagosProveedorss();
}
