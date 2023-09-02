package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Serializable>{

	public abstract List<Venta>findTop100ByOrderByIdDesc();

	public abstract Venta findTop1ByOrderByIdDesc();

	@Query(value="select * from venta v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id inner join documento doc on doc.id=v.documento_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc ",nativeQuery=true)
	List<Venta> getVenta(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="select * from venta v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id inner join documento doc on doc.id=v.documento_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc limit :limite",nativeQuery=true)
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
	
	@Query(value="select sum(det.costo)as costoTotal, sum(det.sub_total) as ventaTotal, sum(det.sub_total - det.costo)as utilidad  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO' ",nativeQuery=true)
	Object [][] getReporteVentaRango(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	@Query(value="select pc.nombre || ' ' || pc.apellido as cliente, pf.nombre || ' ' || pf.apellido as vendedor, doc.descripcion as doc, v.nro_documento, v.fecha_factura ,v.total as total  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO' group by  pc.nombre || ' ' || pc.apellido, pf.nombre || ' ' || pf.apellido , doc.descripcion , v.nro_documento, v.fecha_factura ,v.total",nativeQuery=true)
	List<Object []> getReporteVentaRangoDetallado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	
	@Query(value="select sum(det.costo)as costoTotal, sum(det.sub_total) as ventaTotal, sum(det.sub_total - det.costo)as utilidad  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO'  and f.id=:idF ",nativeQuery=true)
	Object [][] getReporteVentaRangoPorFuncionarios(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idF") int id);
	
	@Query(value="select pc.nombre || ' ' || pc.apellido as cliente, pf.nombre || ' ' || pf.apellido as vendedor, doc.descripcion as doc, v.nro_documento, v.fecha_factura ,v.total as total  from detalle_producto det inner join venta v on v.id=det.venta_id inner join funcionario f on  f.id=v.funcionariov_id inner join persona pf on pf.id=f.persona_id inner join cliente cli on cli.id=v.cliente_id inner join persona pc on pc.id=cli.persona_id inner join documento doc on doc.id=v.documento_id where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin))  and v.estado ='FACTURADO'  and f.id=:idF group by pc.nombre || ' ' || pc.apellido , pf.nombre || ' ' || pf.apellido , doc.descripcion , v.nro_documento, v.fecha_factura ,v.total  ",nativeQuery=true)
	List<Object []> getReporteVentaRangoPorFuncionariosDetallado(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idF") int id);
	
	@Query(value="select sum(dtp.costo)as costoTotal, sum(dtp.sub_total) as ventaTotal, sum(dtp.sub_total - dtp.costo)as utilidad " + 
			"from detalle_producto dtp  " + 
			"INNER JOIN venta v ON dtp.venta_id=v.id " + 
			"INNER JOIN producto p ON p.id=dtp.producto_id " + 
			"INNER JOIN grupo g ON g.id=p.grupo_id " + 
			"INNER JOIN sub_grupo sg ON sg.id=p.sub_grupo_id " + 
			"where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO'",nativeQuery=true)
Object [][] getReporteVentaRangoGrupoAll(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);

	@Query(value="select sum(dtp.costo)as costoTotal, sum(dtp.sub_total) as ventaTotal, sum(dtp.sub_total - dtp.costo)as utilidad " + 
				"from detalle_producto dtp  " + 
				"INNER JOIN venta v ON dtp.venta_id=v.id " + 
				"INNER JOIN producto p ON p.id=dtp.producto_id " + 
				"INNER JOIN grupo g ON g.id=p.grupo_id " + 
				"INNER JOIN sub_grupo sg ON sg.id=p.sub_grupo_id " + 
				"where ((v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <=  :fecha_fin)) AND  v.estado='FACTURADO' AND g.id=:idGrupo",nativeQuery=true)
	Object [][] getReporteVentaRangoGrupo(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idGrupo") int idGrupo);
	
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

}
