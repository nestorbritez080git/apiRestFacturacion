package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.OperacionCaja;

public interface OperacionCajaRepository extends JpaRepository<OperacionCaja, Serializable> {
	public abstract List<OperacionCaja>findTop100ByOrderByIdDesc();
	@Query(value="select oc.id as id, oc.fecha as fecha, oc.monto as monto, oc.motivo as motivo, oc.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId from operacion_caja oc inner join tipo_operacion top on oc.tipo_operacion_id=top.id inner join apertura_caja ap on oc.apertura_caja_id= ap.id inner join concepto con on con.id = oc.concepto_id  where ap.id=:idApertura ",nativeQuery=true)
	List<Object[]> getOperacionProIdApertura(@Param("idApertura") int idApertura);
	@Query(value="select op.id as id, op.fecha as fecha, op.monto as monto, op.motivo as motivo, op.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, con.id as idConceptos, pcl.nombre as nom, pcl.apellido as apeC from cobros_cliente c inner join operacion_caja op on c.operacion_caja_id=op.id inner join funcionario fc on fc.id=c.funcionario_id inner join persona pc on pc.id=fc.persona_id inner join tipo_operacion top on op.tipo_operacion_id=top.id inner join concepto con on con.id = op.concepto_id inner join apertura_caja ap on op.apertura_caja_id= ap.id INNER JOIN cuenta_cobrar_cabecera cue on cue.id=c.cuenta_cobrar_cabecera_id inner join cliente cl on cl.id=cue.cliente_id inner join persona pcl on pcl.id=cl.persona_id where ap.id=:idApertura ",nativeQuery=true)
	List<Object[]> getOperacionCobrosClienteProIdApertura(@Param("idApertura") int idApertura);
	public abstract OperacionCaja findTop1ByOrderByIdDesc();
	@Query("select f.monto from OperacionCaja f where f.id=:idVenta")
	Double getMontoEntregaPorVenta(@Param("idVenta") int idVenta);
	
	
//	select op.id as id, op.fecha as fecha, op.monto as monto, op.motivo as motivo, op.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, con.id as idConceptos, pc.nombre as nom, pc.apellido as apeC from cobros_cliente c inner join operacion_caja op on c.operacion_caja=op.id inner join funcionario fc on fc.id=c.funcionario_id inner join persona pc on pc.id=fc.persona_id inner join tipo_operacion top on op.tipo_operacion_id=top.id inner join concepto con on con.id = op.concepto_id inner join apertura_caja ap on op.apertura_caja_id= ap.id where ap.id=?
}


