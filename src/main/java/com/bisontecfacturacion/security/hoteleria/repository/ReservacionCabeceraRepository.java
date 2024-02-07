package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.model.Venta;

public interface ReservacionCabeceraRepository extends JpaRepository<ReservacionCabecera, Serializable> {
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado='FINALIZADO') AND extract(year from cast(v.fecha_registro as Date))=:ano AND extract(month from cast(v.fecha_registro as Date))=:mes AND extract(day from cast(v.fecha_registro as Date))=:dia  order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesFinalizadaRengoFechaAll (@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado='FINALIZADO') order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesFinalizadaAll();
	
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado='RESERVADO') AND extract(year from cast(v.fecha_registro as Date))=:ano AND extract(month from cast(v.fecha_registro as Date))=:mes AND extract(day from cast(v.fecha_registro as Date))=:dia  order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesActivoRangoFechaAll(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado='RESERVADO') order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesActivoAll();
	
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado= 'RESERVADO' OR v.estado='FINALIZADO') AND extract(year from cast(v.fecha_registro as Date))=:ano AND extract(month from cast(v.fecha_registro as Date))=:mes AND extract(day from cast(v.fecha_registro as Date))=:dia  order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesRangoFechaAll(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where (v.estado= 'RESERVADO' OR v.estado='FINALIZADO' OR v.estado='ANULADO') order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesAll();
	
	
	
	
	@Query(value="select * from reservacion_cabecera v \r\n" + 
			"inner join funcionario freg on v.funcionario_registro_id=freg.id \r\n" + 
			"inner join persona preg on freg.persona_id=preg.id \r\n" + 
			"inner join funcionario ffin on v.funcionario_finalizacion_id= ffin.id \r\n" + 
			"inner join persona pfin on ffin.persona_id=pfin.id \r\n" + 
			"inner join cliente cl on v.cliente_id=cl.id \r\n" + 
			"inner join persona cp on cl.persona_id=cp.id \r\n" + 
			"inner join documento doc on doc.id=v.documento_id \r\n" + 
			"where v.estado='PRE-RESERVADO'   order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesAllPreReservado (@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value = "select  cab.descripcion_combo as des, perFin.nombre || ' ' || perFin.apellido as funFin, perCli.nombre || ' ' || perCli.apellido as perCli, cab.entrega as entrega, cab.precio as precio, cab.total_producto as totalProd, cab.total_habitacion as total, cab.fecha_registro as fecReg, cab.hora as horaReg, cab.fecha_factura as fecFin, cab.hora_finalizacion as horFin, cab.estadia as estadia, cab.total as totales \r\n" + 
			"from reservacion_cabecera  cab \r\n" + 
			"inner join funcionario funReg on funReg.id=cab.funcionario_registro_id\r\n" + 
			"inner join persona perReg ON perReg.id=funReg.persona_id\r\n" + 
			"inner join funcionario funFin on funFin.id=cab.funcionario_finalizacion_id\r\n" + 
			"inner join persona perFin on perFin.id=funFin.persona_id\r\n" + 
			"inner join cliente cli on cli.id=cab.cliente_id \r\n" + 
			"inner join persona perCli on perCli.id=cli.persona_id\r\n" + 
			"where cab.estado='FINALIZADO' AND  ((cab.fecha_registro >= :fecha_inicio) AND (cab.fecha_registro <= :fecha_fin ))", nativeQuery = true)
    List<Object []> getResumenRecepcionesRagoFecha( @Param("fecha_inicio") LocalDateTime fecha_inicio, @Param("fecha_fin") LocalDateTime fecha_fin);
    
    @Query(value = "select  cab.descripcion_combo as des, perFin.nombre || ' ' || perFin.apellido as funFin, perCli.nombre || ' ' || perCli.apellido as perCli, cab.entrega as entrega, cab.precio as precio, cab.total_producto as totalProd, cab.total_habitacion as total, cab.fecha_registro as fecReg, cab.hora as horaReg, cab.fecha_factura as fecFin, cab.hora_finalizacion as horFin, cab.estadia as estadia, cab.total as totales \r\n" + 
			"from reservacion_cabecera  cab \r\n" + 
			"inner join funcionario funReg on funReg.id=cab.funcionario_registro_id\r\n" + 
			"inner join persona perReg ON perReg.id=funReg.persona_id\r\n" + 
			"inner join funcionario funFin on funFin.id=cab.funcionario_finalizacion_id\r\n" + 
			"inner join persona perFin on perFin.id=funFin.persona_id\r\n" + 
			"inner join cliente cli on cli.id=cab.cliente_id \r\n" + 
			"inner join persona perCli on perCli.id=cli.persona_id\r\n" + 
			"where cab.estado='FINALIZADO' AND  ((cab.fecha_registro >= :fecha_inicio) AND (cab.fecha_registro <= :fecha_fin )) AND funFin.id= :idFun", nativeQuery = true)
    List<Object []> getResumenRecepcionesRagoFechaFuncionario( @Param("fecha_inicio") LocalDateTime fecha_inicio, @Param("fecha_fin") LocalDateTime fecha_fin, @Param("idFun") int idFun);
    
	
	@Query(value="select * from reservacion_cabecera v order by v.id desc limit 1", nativeQuery = true)
	 ReservacionCabecera getUltimaReservacion();
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update ReservacionCabecera set operacionCajaEntrega =:operacionCaja where id=:id")
	public void findByActualizarReservacionOperacionEntrega(@Param("id") int id, @Param("operacionCaja") int operacionCaja);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update ReservacionCabecera set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarReservacionOperacion(@Param("id") int id, @Param("operacionCaja") int operacionCaja);

	@Modifying
    @Transactional(readOnly=false)
    @Query("update ReservacionCabecera set estado=:est where id=:id")
    public void findByActualizaEstado(@Param("id") int id, @Param("est") String est);
}
