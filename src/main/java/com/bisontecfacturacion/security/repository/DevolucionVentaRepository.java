package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.DevolucionVenta;

@Transactional(readOnly=true)
public interface DevolucionVentaRepository extends JpaRepository<DevolucionVenta, Serializable>{
	@Query(value="select v.id from devolucion_venta v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaDevolucion();
	@Query(value="select * from devolucion_venta v order by v.id desc limit 1", nativeQuery = true)
	DevolucionVenta getDevolucionUlt();
	
	@Query(value="SELECT ccc FROM DevolucionVenta ccc  INNER JOIN ccc.venta as ven INNER JOIN ven.cliente as cli WHERE ccc.id=:id")
	DevolucionVenta getDevolucionPorId(@Param("id") int id);
	
	
	
	public abstract List<DevolucionVenta> findTop50ByOrderByIdDesc();
	
	@Query(value="SELECT d.id, pf.nombre || ' ' || pf.apellido AS funcionario, pc.nombre || ' ' || pc.apellido AS cliente, pc.cedula, d.total, t.descripcion, to_char(d.fecha, 'TMDay') || ' ' || extract(day from cast(d.fecha as Date)) || ' ' || to_char(d.fecha, 'TMMonth') || ' de ' || extract(year from cast(d.fecha as Date)) || ' ' || d.hora as fecha, d.estado, t.id " + 
			"FROM devolucion_venta d " + 
			"INNER JOIN funcionario f ON d.funcionario_id = f.id " + 
			"INNER JOIN persona pf ON f.persona_id=pf.id " + 
			"INNER JOIN venta v ON d.venta_id = v.id " + 
			"INNER JOIN cliente c ON v.cliente_id = c.id " + 
			"INNER JOIN persona pc ON c.persona_id=pc.id " + 
			"INNER JOIN tipo_devolucion t ON d.tipo_devolucion_id = t.id " + 
			"where extract(year from cast(d.fecha as Date))=:ano AND extract(month from cast(d.fecha as Date))=:mes AND extract(day from cast(d.fecha as Date))=:dia order by d.id desc",nativeQuery=true)
	List<Object[]> getVentaFecha(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="SELECT d.id, pf.nombre || ' ' || pf.apellido AS funcionario, pc.nombre || ' ' || pc.apellido AS cliente, pc.cedula, d.total, t.descripcion, to_char(d.fecha, 'TMDay') || ' ' || extract(day from cast(d.fecha as Date)) || ' ' || to_char(d.fecha, 'TMMonth') || ' de ' || extract(year from cast(d.fecha as Date)) || ' ' || d.hora as fecha, d.estado, t.id " + 
			"FROM devolucion_venta d " + 
			"INNER JOIN funcionario f ON d.funcionario_id = f.id " + 
			"INNER JOIN persona pf ON f.persona_id=pf.id " + 
			"INNER JOIN venta v ON d.venta_id = v.id " + 
			"INNER JOIN cliente c ON v.cliente_id = c.id " + 
			"INNER JOIN persona pc ON c.persona_id=pc.id " + 
			"INNER JOIN tipo_devolucion t ON d.tipo_devolucion_id = t.id " + 
			"WHERE pf.nombre  ILIKE :desc OR pf.apellido  ILIKE :desc OR pc.nombre  ILIKE :desc OR pc.apellido  ILIKE :desc OR pc.cedula  ILIKE :desc order by d.id desc",nativeQuery=true)
	List<Object[]> getVentaFiltro(@Param("desc") String desc);
	
	
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update DevolucionVenta set estado='CERRADO' where id=:proid")
    public void confirmarDevolucion(@Param("proid") int proid);
	
	@Query(value = "select v.id as idVenta, pc.nombre || ' ' || pc.apellido as cliente, v.total as totalVenta, dv.id as idDev, dv.fecha as fecDev, dv.total as totalDev, tp.descripcion as tipoDev, tp.id as idTipo  from devolucion_venta dv inner join venta v on dv.venta_id=v.id inner join cliente cl on cl.id=v.cliente_id inner join persona pc on pc.id=cl.persona_id inner join tipo_devolucion tp on tp.id=dv.tipo_devolucion_id where dv.id=:id", nativeQuery = true)
	List<Object[]> getDevolucionId(@Param("id") int id);
	
	@Query("SELECT COUNT(d) FROM DevolucionVentaDetalle d " +
		       "WHERE d.detalleProducto.id = :detalleProductoId " +
		       "AND d.devolucionVenta.venta.id = :ventaId " +
		       "AND d.devolucionVenta.estado = 'CERRADO'")
		int countDevolucionesConfirmadasPorProductoYVenta(@Param("detalleProductoId") int detalleProductoId,
		                                                  @Param("ventaId") int ventaId);

	
	@Query(value = "select n.id as idNota,  pc.nombre || ' ' || pc.apellido as cliente, n.total as totalNota,  fp.nombre || ' ' || fp.apellido as funcionario, n.total_letra as totalnotaletra, dev.id as idDevol, dev.fecha as fechadevol, dev.total as totaldevol from nota_credito n inner join devolucion_venta dev on dev.id=n.devolucion_venta_id inner join cliente c on c.id=n.cliente_id inner join persona pc on pc.id=c.persona_id inner join funcionario f on f.id=n.funcionario_id  inner join persona fp on fp.id=f.persona_id where dev.id=:id", nativeQuery = true)
	Object[][] getNotaCreditoPorIdDevolucion(@Param("id") int id);
	
}
