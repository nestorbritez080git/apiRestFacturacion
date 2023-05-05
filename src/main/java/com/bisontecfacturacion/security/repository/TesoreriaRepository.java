package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Tesoreria;

@Repository
public interface TesoreriaRepository extends JpaRepository<Tesoreria, Serializable> {
	
//	@Query("select c from caja d where c.descripcion like :descripcion%")
//	List<Caja> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
//	public abstract Caja findByDescripcion(String descripcion);
	public abstract List<Tesoreria>findTop100ByOrderByIdDesc();
//	@Query(value="UPDATE cierre_caja SET estado_recibido=? WHERE id=? ",nativeQuery=true)
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Tesoreria set estado=:estado where id=:id")
    public void findByActualizarEstado(@Param("id") int id, @Param("estado")Boolean estadoRecibido );

	@Modifying
    @Transactional(readOnly=false)
    @Query("update Tesoreria set estadoAnulacion=:estado where id=:id")
    public void findByActualizarEstadoAnulacionTesoreria(@Param("id") int id, @Param("estado")Boolean estadoRecibido );

	
	
	@Query(value="SELECT cj.id as numerocierre, t.id numerorecibo, cj.monto as montocierre, ac.saldo_actual as montooperacion, ac.saldo_inicial as montoinicial,  t.fecha as fecha, t.hora as hora, cj.monto - (ac.saldo_actual + ac.saldo_inicial) as diferencia FROM tesoreria t inner join funcionario fun on fun.id=t.funcionario_id inner join persona per on per.id=fun.persona_id inner join cierre_caja cj on cj.id=t.cierre_caja_id inner join apertura_caja ac on ac.id= cj.apertura_caja_id where t.fecha >= :fecha_inicio AND t.fecha <= :fecha_fin and fun.id = :idFun AND cj.estado_anulacion=false order by t.id desc",nativeQuery=true)
	List<Object[]>  getInoformeCierrePorFuncionario(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idFun") int idFunc);
    
	@Query(value="SELECT cj.id as numerocierre,t.id numerorecibo, cj.monto as montocierre, ac.saldo_actual as montooperacion, ac.saldo_inicial as montoinicial,  t.fecha as fecha, t.hora as hora FROM tesoreria t inner join funcionario fun on fun.id=t.funcionario_id inner join persona per on per.id=fun.persona_id inner join cierre_caja cj on cj.id=t.cierre_caja_id inner join apertura_caja ac on ac.id= cj.apertura_caja_id where t.fecha >= :fecha_inicio AND t.fecha <= :fecha_fin order by t.id desc",nativeQuery=true)
	List<Object[]>  getInoformeCierre(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	   
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE Tesoreria set importe=importe-:importeC WHERE cierre_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVenta(@Param("id") int id, @Param("importeC") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE Tesoreria set importeCheque=importeCheque-:importeC WHERE cierre_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVentaCheque(@Param("id") int id, @Param("importeC") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE Tesoreria set importeTarjeta=importeTarjeta-:importeC WHERE cierre_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVentaTarjeta(@Param("id") int id, @Param("importeC") double monto);
	
	
	
	@Query(value="select sum(v.total) as totalVenta from venta v inner join funcionario fun on fun.id=v.funcionario_id where v.estado='FACTURADO' and fun.id=:idFun and  v.fecha_factura >= :fecha_inicio AND v.fecha_factura <= :fecha_fin",nativeQuery=true)
	Object[][]  getInformeVentaFacturadoPorFuncionario(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idFun") int idFun);
	
	@Query(value="select c.descripcion, sum(v.monto)as totalmontoconcepto from operacion_caja  v inner join concepto c on c.id=v.concepto_id inner join tipo_operacion tp on tp.id=v.tipo_operacion_id  where v.apertura_caja_id=:idApertura group by c.id",nativeQuery=true)
	List<Object[]>  getResumenDetalleTesoreriaPorConceptos(@Param("idApertura") int idApertura);
	
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Tesoreria set estadoTransferencia=:estado where id=:id")
    public void findByActualizarEstadoTransferenciaTesoreria(@Param("id") int id, @Param("estado")Boolean estado );

	
	@Query(value="select tp.descripcion as des, sum(op.monto) from venta v inner join operacion_caja op on op.id=v.operacion_caja inner join concepto c on c.id=op.concepto_id inner join tipo_operacion  tp on tp.id=op.tipo_operacion_id where v.estado='FACTURADO' AND v.tipo='1' AND tp.id=1 AND (v.fecha_factura >= :fecha_inicio AND v.fecha_factura <= :fecha_fin) group by tp.id ",nativeQuery=true)
	Object[][] getResumenEntradaSalidaCajaTipoOperacionVentaContadoEfectivo(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
	
	
	
	
	
}

