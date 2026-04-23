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
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;

public interface OperacionCajaCabeceraRepository extends JpaRepository<OperacionCajaCabecera, Serializable> {
	@Query("SELECT DISTINCT c FROM OperacionCajaCabecera c " +
		       "LEFT JOIN FETCH c.operacionCajas o " +
		       "LEFT JOIN FETCH o.tipoOperacion " +
		       "LEFT JOIN FETCH o.aperturaCaja " +
		       "LEFT JOIN FETCH o.concepto " +
		       "WHERE c.id = :id")
	OperacionCajaCabecera consultarOperacionCajaCabeceraPorId(@Param("id") Integer id);
//	select op.id as id, op.fecha as fecha, op.monto as monto, op.motivo as motivo, op.tipo as tipo, top.descripcion as tipooperacion, top.id as tipoId, con.id as idConceptos, pc.nombre as nom, pc.apellido as apeC from cobros_cliente c inner join operacion_caja op on c.operacion_caja=op.id inner join funcionario fc on fc.id=c.funcionario_id inner join persona pc on pc.id=fc.persona_id inner join tipo_operacion top on op.tipo_operacion_id=top.id inner join concepto con on con.id = op.concepto_id inner join apertura_caja ap on op.apertura_caja_id= ap.id where ap.id=?
}


