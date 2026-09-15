package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;

public interface OperacionCajaRepository extends JpaRepository<OperacionCaja, Serializable> {
	public abstract List<OperacionCaja>findTop100ByOrderByIdDesc();
	@Query(value="select oc.id as id, oc.fecha as fecha, oc.monto as monto, oc.motivo as motivo, oc.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, oc.concepto_id as idConcepto, oc.referencia_operacion as refe, oc.apertura_caja_id as idAper from operacion_caja oc inner join tipo_operacion top on oc.tipo_operacion_id=top.id inner join apertura_caja ap on oc.apertura_caja_id= ap.id inner join concepto con on con.id = oc.concepto_id  where ap.id=:idApertura order by oc.id desc ",nativeQuery=true)
	List<Object[]> getOperacionProIdApertura(@Param("idApertura") int idApertura);
	
	@Query(value="select oc.id as id, oc.fecha as fecha, oc.monto as monto, oc.motivo as motivo, oc.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, oc.concepto_id as idConcepto, oc.referencia_operacion as refe, oc.apertura_caja_id as idAper from operacion_caja oc inner join tipo_operacion top on oc.tipo_operacion_id=top.id inner join apertura_caja ap on oc.apertura_caja_id= ap.id inner join concepto con on con.id = oc.concepto_id  where ap.id=:idApertura order by oc.id desc ",nativeQuery=true)
	List<Object[]> getOperacionTipoMovimientoPorIdApertura(@Param("idApertura") int idApertura);
	
	
	@Query(value="select op.id as id, op.fecha as fecha, op.monto as monto, op.motivo as motivo, op.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, con.id as idConceptos, pcl.nombre as nom, pcl.apellido as apeC from cobros_cliente c inner join operacion_caja op on c.operacion_caja_id=op.id inner join funcionario fc on fc.id=c.funcionario_id inner join persona pc on pc.id=fc.persona_id inner join tipo_operacion top on op.tipo_operacion_id=top.id inner join concepto con on con.id = op.concepto_id inner join apertura_caja ap on op.apertura_caja_id= ap.id INNER JOIN cuenta_cobrar_cabecera cue on cue.id=c.cuenta_cobrar_cabecera_id inner join cliente cl on cl.id=cue.cliente_id inner join persona pcl on pcl.id=cl.persona_id where ap.id=:idApertura ",nativeQuery=true)
	List<Object[]> getOperacionCobrosClienteProIdApertura(@Param("idApertura") int idApertura);
	public abstract OperacionCaja findTop1ByOrderByIdDesc();
	@Query("select f.monto from OperacionCaja f where f.id=:idVenta")
	Double getMontoEntregaPorVenta(@Param("idVenta") int idVenta);
	
	@Modifying
	@Query(value = "DELETE FROM operacion_caja where apertura_caja_id=:idApertura AND concepto_id=:idConcepto", nativeQuery = true)
	public void borraDatosSalidaCapital(@Param("idApertura") int idApertura, @Param("idConcepto") int idConcepto);
	
	@Query("SELECT c  FROM OperacionCaja c JOIN FETCH c.aperturaCaja a JOIN FETCH a.funcionario f JOIN FETCH f.persona p WHERE c.id = :id")
	OperacionCaja getOperacionCajaPorId(@Param("id") int id);
	
	@Modifying
	@Query(value = "DELETE FROM operacion_caja where monto=monto-:mon WHERE id=:idOperacion", nativeQuery = true)
	public void descontarMontoOperacionCajaPorAnulacionVenta(@Param("idOperacion") int idOperacion, @Param("mon") Double mon);
	
	@Query(value = "SELECT sum(op.monto), c.descripcion, op.tipo  FROM operacion_caja op inner join concepto c on c.id=op.concepto_id  where op.apertura_caja_id=:id group by c.id, op.tipo ", nativeQuery = true)
    List<Object []> getResumenConceptoPorApertura( @Param("id") int id);
    
    
    @Query(value = "SELECT " +
    	    "    COALESCE(per.nombre,'') || ' ' || COALESCE(per.apellido,'') AS aperturafuncionario, " + 
    	    "    ac.id AS apertura_id, " + 
    	    "    ac.fecha AS fecha_apertura, " + 
    	    "    ac.hora AS hora_apertura, " + 
    	    "    ac.saldo_inicial AS saldo_inicial_efectivo, " + 
    	    "    ac.saldo_inicial_cheque AS saldo_inicial_cheque, " + 
    	    "    ac.saldo_inicial_tarjeta AS saldo_inicial_tarjeta, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 1 THEN oc.monto END),0) AS total_entrada_efectivo, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 2 THEN oc.monto END),0) AS total_entrada_cheque, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 3 THEN oc.monto END),0) AS total_entrada_tarjeta, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 1 THEN oc.monto END),0) AS total_salida_efectivo, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 2 THEN oc.monto END),0) AS total_salida_cheque, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 3 THEN oc.monto END),0) AS total_salida_tarjeta, " + 
    	    "    "
    	    + 	"COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 1 THEN oc.monto END),0) " + 
    	    "        - COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 1 THEN oc.monto END),0) AS saldo_final_efectivo, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 2 THEN oc.monto END),0) " + 
    	    "        - COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 2 THEN oc.monto END),0) AS saldo_final_cheque, " + 
    	    "    COALESCE(SUM(CASE WHEN oc.tipo = 'ENTRADA' AND oc.tipo_operacion_id = 3 THEN oc.monto END),0) " + 
    	    "        - COALESCE(SUM(CASE WHEN oc.tipo = 'SALIDA' AND oc.tipo_operacion_id = 3 THEN oc.monto END),0) AS saldo_final_tarjeta " + 
    	    "FROM apertura_caja ac " + 
    	    "LEFT JOIN operacion_caja oc ON oc.apertura_caja_id = ac.id " +
    	    "LEFT JOIN funcionario fun on fun.id = ac.funcionario_id " + 
    	    "LEFT JOIN persona per on per.id = fun.persona_id " + 
    	    "LEFT JOIN concepto con on con.id = oc.concepto_id " +  
    	    "WHERE  con.id NOT IN (15,16,23,33,34) " + 
    	    "  AND  (ac.estado_anulacion IS NULL OR ac.estado_anulacion = false) " + 
    	    "  AND oc.fecha >= :fecha_inicio AND oc.fecha <= :fecha_fin " + 
    	    "GROUP BY ac.id, ac.fecha, ac.hora, " + 
    	    "ac.saldo_inicial, ac.saldo_inicial_cheque, ac.saldo_inicial_tarjeta, " +
    	    "per.nombre, per.apellido " +
    	    "ORDER BY ac.fecha, ac.hora", nativeQuery = true)
    	List<Object[]> getResumenHistoricoAperturaCaja(@Param("fecha_inicio") LocalDateTime fecha_inicio, @Param("fecha_fin") LocalDateTime fecha_fin);

    	
    	@Query("SELECT o FROM OperacionCaja o " +
    		       "WHERE o.operacionCajaCabecera.id = :idCabecera " +
    		       "ORDER BY o.id ASC")
      List<OperacionCaja> getOperacionesPorCabecera( @Param("idCabecera") Integer idCabecera);
//	select op.id as id, op.fecha as fecha, op.monto as monto, op.motivo as motivo, op.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, con.id as idConceptos, pc.nombre as nom, pc.apellido as apeC from cobros_cliente c inner join operacion_caja op on c.operacion_caja=op.id inner join funcionario fc on fc.id=c.funcionario_id inner join persona pc on pc.id=fc.persona_id inner join tipo_operacion top on op.tipo_operacion_id=top.id inner join concepto con on con.id = op.concepto_id inner join apertura_caja ap on op.apertura_caja_id= ap.id where ap.id=?
}


