package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;
import com.bisontecfacturacion.security.model.PagosProveedor;


public interface PagoProveedorRepository extends JpaRepository<PagosProveedor, Serializable>{
	
	@Query(value = "select c from PagosProveedor c where cuenta_cabecera_id=:idCuenta")
	public List<PagosProveedor> getPagosPorIdCuenta(@Param("idCuenta") int idCuenta);
	
	@Query(value = "SELECT p.nombre || ' ' || p.apellido as nombres, to_char(cc.fecha, 'TMDay') || ' ' || extract(day from cast(cc.fecha as Date)) || ' ' || to_char(cc.fecha, 'TMMonth') || ' de ' || extract(year from cast(cc.fecha as Date)) as fecha, cc.fecha as fehc FROM cobros_cliente cc inner join cuenta_cobrar_cabecera ccc on cc.cuenta_cobrar_cabecera_id=ccc.id inner join cliente c on ccc.cliente_id=c.id inner join persona p on c.persona_id=p.id where ccc.cliente_id=:id order by cc.fecha desc limit 1",nativeQuery=true)
	public List<Object[]> getCabeceraCuentaClienteId(@Param("id") int id);
	
	@Query(value="select * from pagos_proveedor v order by v.id desc limit 1", nativeQuery = true)
	PagosProveedor getUltimoPagos();
	
	@Query(value = "select ppro.fecha_pagos, ppro.fecha_registro, pa.nombre as nombreFunAut, pa.apellido as apellidoFunAut, pr.nombre as nomPre, pr.apellido as apFuncR, ppro.importe, ppro.comprobante  from pagos_proveedor ppro inner join cuenta_pagar_cabecera ccab on ccab.id=ppro.cuenta_cabecera_id inner join proveedor p on p.id=ccab.proveedor_id inner join persona pp on pp.id= p.persona_id inner join funcionario fa on fa.id=ppro.funcionarioa_id  inner join persona pa on pa.id=fa.persona_id inner join funcionario fr on fr.id=ppro.funcionarior_id  inner join persona pr on pr.id=fr.persona_id where p.id=:id order by ppro.id desc",nativeQuery=true)
	public List<Object[]> getPagosPorIdProveedor(@Param("id") int id);
}
