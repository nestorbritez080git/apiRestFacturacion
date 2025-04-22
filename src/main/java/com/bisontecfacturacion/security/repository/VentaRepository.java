package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.Documento;
import com.bisontecfacturacion.security.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Serializable>{

	public abstract List<Venta>findTop100ByOrderByIdDesc();

	public abstract Venta findTop1ByOrderByIdDesc();

	@Query(value="select * from venta v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join funcionario fv on v.funcionariov_id=fv.id inner join persona pfv on f.persona_id=pfv.id inner join funcionario fr on v.funcionarior_id=fr.id inner join persona pfr on fr.persona_id=pfr.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id inner join documento doc on doc.id=v.documento_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc ",nativeQuery=true)
	List<Venta> getVenta(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="select * from venta v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join funcionario fv on v.funcionariov_id=fv.id inner join persona pfv on f.persona_id=pfv.id inner join funcionario fr on v.funcionarior_id=fr.id inner join persona pfr on fr.persona_id=pfr.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id inner join documento doc on doc.id=v.documento_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc limit :limite",nativeQuery=true)
	List<Venta> getVentaLimitessssss(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia,@Param("limite") int limite);

	@Query(value="select * from detalle_producto det inner join  venta  v on v.id=det.venta_id inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id inner join documento doc on doc.id=v.documento_id where ((extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia) and det.descripcion ILIKE :desc) OR ((extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia) and cp.nombre ILIKE :desc) OR ((extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia) and cp.apellido ILIKE :desc) order by v.id desc",nativeQuery=true)
	List<Venta> getVentaDescripcion(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia, @Param("desc") String desc);

	@Query("select v from Venta v where id=:id")
	List<Venta> getVenta(@Param("id") int id);

	@Query(value = "select pc.nombre || ' ' || pc.apellido as nombreCliente, pc.cedula, pc.direccion, v.fecha, v.hora, pv.nombre || ' ' || pv.apellido as nombreVendedor, v.tipo, v.total_iva_cinco, v.total_iva_dies, v.total, v.total_letra, v.nro_documento, doc.descripcion from Venta v inner join documento doc on v.documento_id=doc.id inner join funcionario fv on v.funcionariov_id=fv.id inner join persona pv on fv.persona_id=pv.id inner join cliente c on v.cliente_id=c.id inner join persona pc on c.persona_id=pc.id where v.id=:id", nativeQuery = true)
	List<Object[]> getVentaId(@Param("id") int id);

	@Query(value="select * from Venta v order by v.id desc limit 1", nativeQuery = true)
	Venta getUltimaVenta();


	@Modifying 
	@Transactional(readOnly=false)
	@Query("update Venta set estado=:estado where id=:id")
	public void findByActualizarFacturas(@Param("id") int id, @Param("estado") String estado);
	

	@Modifying
	@Transactional(readOnly=false)
	@Query("update Venta set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarVentaOperacion(@Param("id") int id, @Param("operacionCaja") int operacionCaja);

	@Query("select v from Venta v where v.id= :operacionCaja")
	public Venta getVentaPorOperacionId(@Param("operacionCaja") int operacionCaja);
	
	@Query("select ccc from Venta ccc where ccc.id=:id AND ccc.estado='FACTURADO'")
	public Venta getVentaIdFacturado(@Param("id") int id);

	@Query("select v from Venta v where ((v.fechaFactura >= :fecha_inicio) AND (v.fechaFactura <=  :fecha_fin))  and v.estado ='FACTURADO'")
	public List<Venta> getVentaPorRangoFechaHql(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	

	@Query(value="SELECT  * FROM venta v where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO' AND v.cliente_id=:idCli", nativeQuery = true)
	public List<Venta> getVentaPorRangoFechaClienteHql(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idCli") int idCli);
	
	
	@Query(value = "select sum(total) as total from venta v where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia",nativeQuery = true)
	Object[] findByTotalVenta(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

	@Query(value = "select sum(sub_total) as totales, producto_id, descripcion from detalle_producto group by producto_id, descripcion order by totales desc limit 5",nativeQuery = true)
	List<Object[]> findByUtilidad();

	@Query(value = "select to_char(venta.fecha_factura, 'TMMonth') as mess, extract(month from cast(venta.fecha_factura as Date)) as mes, sum(total) as total from venta where venta.estado='FACTURADO' AND extract(year from cast(venta.fecha_factura as Date))=:ano group by mes, mess order by mes asc",nativeQuery = true)
	List<Object[]> findByTotalVentaXmes(@Param("ano") int ano);

	@Query(value = "SELECT to_char(v.fecha_factura, 'HH24') as hora, COUNT(*) as total FROM venta v where extract(year from cast(v.fecha_factura as Date))=:ano AND extract(month from cast(v.fecha_factura as Date))=:mes AND extract(day from cast(v.fecha_factura as Date))=:dia GROUP BY to_char(v.fecha_factura, 'HH24') ORDER BY to_char(v.fecha_factura, 'HH24') ASC",nativeQuery = true)
	List<Object[]> getMovimientoDelDia(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

	@Query(value = "SELECT extract(day from cast(v.fecha_factura as Date)) AS dia, COUNT(v.id) AS total, to_char(v.fecha_factura, 'TMDay') || ' ' || extract(day from cast(v.fecha_factura as Date)) || ' ' || to_char(v.fecha_factura, 'TMMonth') || ' de ' || extract(year from cast(v.fecha_factura as Date)) as fec FROM venta v WHERE extract(year from cast(v.fecha_factura as Date))=:ano AND extract(month from cast(v.fecha_factura as Date))=:mes GROUP BY fec,extract(day from cast(v.fecha_factura as Date)) ORDER BY dia ASC",nativeQuery = true)
	List<Object[]> getMovimientoPorMes(@Param("ano") int ano, @Param("mes") int mes);


	@Query(value="select v.nro_documento as nroDocu, v.fecha_factura as fecha, pc.nombre || ' ' || pc.apellido as apeCli, pc.cedula as ruc, v.timbrado as timbrado, v.timbrado_fin vtoTimbrado, v.total as total, v.total_iva_cinco as iva5, v.total_iva_dies as ivaDies, v.total_excenta as totalExcenta from venta  v inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where  ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) and doc.id = 1 and v.estado ='FACTURADO' order by v.fecha_factura asc",nativeQuery=true)
	List<Object []> getLibroVenta(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	
	@Query(value="select pc.nombre || ' ' || pc.apellido as cliente, pf.nombre || ' ' || pf.apellido as vendedor, doc.descripcion as doc, v.nro_documento, v.fecha_factura ,v.total as total  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO' group by  pc.nombre || ' ' || pc.apellido, pf.nombre || ' ' || pf.apellido , doc.descripcion , v.nro_documento, v.fecha_factura ,v.total",nativeQuery=true)
	List<Object []> getReporteVentaRangoDetallado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	

	@Query(value="select sum(det.sub_total) as ventaTotal from detalle_producto det inner join venta v on v.id=det.venta_id  inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO'  and cli.id=:idC",nativeQuery=true)
	Double getReporteVentaRangoPorClienteProductoDetalle(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idC") int id);
	
	@Query(value="select sum(det.sub_total) as totalServicio from detalle_servicios det inner join venta v on v.id=det.venta_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO'  and cli.id=:idC ",nativeQuery=true)
	Double getReporteVentaRangoPorClienteServicioDetalle(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idC") int id);
	
	@Query(value="select v.id as id, v.fecha_factura as ultimaFecha from venta v where v.cliente_id=:idC AND v.estado='FACTURADO' order by id DESC LIMIT 2",nativeQuery=true)
	List<Object[]> getReporteVentaRangoPorClienteultimaVenta(@Param("idC") int id);
	
	
	@Query(value="select sum(dtp.costo)as costoTotal, sum(dtp.sub_total) as ventaTotal, sum(dtp.sub_total - dtp.costo)as utilidad " + 
			"from detalle_producto dtp  " + 
			"INNER JOIN venta v ON dtp.venta_id=v.id " + 
			"INNER JOIN producto p ON p.id=dtp.producto_id " + 
			"INNER JOIN grupo g ON g.id=p.grupo_id " + 
			"INNER JOIN sub_grupo sg ON sg.id=p.sub_grupo_id " + 
			"where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO'",nativeQuery=true)
	Object [][] getReporteVentaRangoGrupoAll(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	

//	@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin,
//	((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))
	@Query(value="SELECT  * FROM venta v where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO' AND v.funcionariov_id=:idFuncionario", nativeQuery = true)
	public List<Venta> getReporteVentaRangoPorFuncionarios(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idFuncionario") int idFuncionario);
	
	@Query(value="select sum(det.costo)as costoTotal, sum(det.sub_total) as ventaTotal, sum(det.sub_total - det.costo)as utilidad  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO' and f.id=:idFuncionario",nativeQuery=true)
	Object [][] getReporteVentaRangoFuncionarioCabecera(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idFuncionario") int idFuncionario);
	
	
	@Query(value="select dtp.descripcion as des, dtp.precio as precio, dtp.cantidad, dtp.sub_total as subtotal,  dtp.costo as costo, dtp.tipo_precio as tipo " + 
				"from detalle_producto dtp  " + 
				"INNER JOIN venta v ON dtp.venta_id=v.id " + 
				"INNER JOIN producto p ON p.id=dtp.producto_id " + 
				"INNER JOIN grupo g ON g.id=p.grupo_id " + 
				"INNER JOIN sub_grupo sg ON sg.id=p.sub_grupo_id " + 
				"where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO' AND g.id=:idGrupo",nativeQuery=true)
	List<Object []> getReporteVentaRangoGrupoDetallado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idGrupo") int idGrupo);
	
	@Query(value="select dtp.descripcion as des, dtp.precio as precio, dtp.cantidad, dtp.sub_total as subtotal,  dtp.costo as costo, dtp.tipo_precio as tipo " + 
			"from detalle_producto dtp  " + 
			"INNER JOIN venta v ON dtp.venta_id=v.id " + 
			"INNER JOIN producto p ON p.id=dtp.producto_id " + 
			"INNER JOIN grupo g ON g.id=p.grupo_id " + 
			"INNER JOIN sub_grupo sg ON sg.id=p.sub_grupo_id " + 
			"where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO'",nativeQuery=true)
List<Object []> getReporteVentaRangoGrupoDetalladoAll(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);


@Query(value="select  sum(v.total) as total, v.tipo as  concepto from venta v \r\n" + 
			"where v.estado='FACTURADO' AND v.tipo='1' AND ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))"+ 
			"group by v.tipo", nativeQuery = true)
public List<Object []> getResumenVentaContado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select  sum(v.total) as total, v.tipo as  concepto from venta v \r\n" + 
		"where v.estado='FACTURADO' AND v.tipo='2' AND ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))"+ 
		"group by v.tipo", nativeQuery = true)
public List<Object []> getResumenVentaCredito(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select  sum(v.total)as total, c.descripcion as concepto from compra v INNER JOIN concepto c on c.id=v.concepto_id " + 
		"where v.estado='FACTURADO' AND v.tipo='CONTADO' AND ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))"+ 
		"group by c.id", nativeQuery = true)
public List<Object []> getResumenCompraContado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);


@Query(value="select  sum(v.total)as total, c.descripcion as concepto from compra v INNER JOIN concepto c on c.id=v.concepto_id " + 
		"where v.estado='FACTURADO' AND v.tipo='CREDITO' AND ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))"+ 
		"group by c.id", nativeQuery = true)
public List<Object []> getResumenCompraCredito(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select sum(v.total) as total,  c.descripcion  from cobros_cliente v " + 
			"inner join operacion_caja op on op.id=v.operacion_caja_id " + 
			"inner join concepto c on c.id=op.concepto_id " + 
			"where ((v.fecha >= :fecha_inicio) AND (v.fecha <=  :fecha_fin))"+ 
			"group by c.id", nativeQuery = true)
public List<Object []> getResumenCobros(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);



@Query(value="select sum(v.total) as total, c.descripcion from pagos_proveedor_cabecera v " + 
			   "inner join concepto c on c.id= v.concepto_id " +
			  "where ((v.fecha_pagos >= :fecha_inicio) AND (v.fecha_pagos <=  :fecha_fin)) "+ 
			  "group by c.id", nativeQuery = true)
public List<Object []> getResumenPagos(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select sum(v.saldo) as total, c.descripcion as iddd from cuenta_cobrar_cabecera v " + 
			"inner join concepto c on c.id=v.concepto_id " + 
			"where v.saldo > 0 AND ((v.fecha >= :fecha_inicio) AND (v.fecha <=  :fecha_fin)) " + 
			"group by  c.id", nativeQuery = true)
public List<Object []> getResumenCuentaCobrar(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select sum(v.saldo) as total, c.descripcion as iddd from cuenta_pagar_cabecera v " + 
		"inner join concepto c on c.id=v.concepto_id " + 
		"where v.saldo > 0 AND ((v.fecha >= :fecha_inicio) AND (v.fecha <=  :fecha_fin))" + 
		"group by  c.id", nativeQuery = true)
public List<Object []> getResumenCuentaPagar(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);

@Query(value="select sum(v.monto) as total, c.descripcion as iddd from operacion_caja v " + 
		"inner join concepto c on c.id=v.concepto_id where c.id=24 " + 
		"group by  c.id", nativeQuery = true)
public List<Object []> getResumenEntregaInicialVentaCredito(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fi);





@Query(value="select * from venta v inner join funcionario f on v.funcionariov_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id where  cp.nombre ilike :filtro OR   cp.apellido ilike :filtro OR   cp.cedula like :filtro order by v.id desc limit 100",nativeQuery=true)
	List<Venta> getVentaAllFiltroCliente(@Param("filtro") String filtro);

}
