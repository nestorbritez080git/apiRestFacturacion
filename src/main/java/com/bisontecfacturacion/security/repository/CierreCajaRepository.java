package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CierreCaja;


public interface CierreCajaRepository extends JpaRepository<CierreCaja, Serializable>{
	
//	List<Caja> findByTop100DescripcionLike(@Param("descripcion") String descripcion);
//	public abstract Caja findByDescripcion(String descripcion);
	public abstract List<CierreCaja>findTop100ByOrderByIdDesc();
	@Query("select a from CierreCaja a where funcionario_id=:idFuncionario order by id desc")
	public List<CierreCaja> getCierreCajaPorFuncionario(@Param("idFuncionario")int idFuncionario);

	@Query("update Caja set estado=true where id=:id")
    public void findByActualizaEstado(@Param("id") int id);
	public abstract CierreCaja findTop1ByOrderByIdDesc();
	@Query(value = "select * from cierre_caja v inner join apertura_caja ac on ac.id = v.apertura_caja_id inner join caja c on c.id =ac.caja_id inner join funcionario fp on fp.id= v.funcionario_id inner join persona pf on pf.id = fp.persona_id where extract(year from cast(v.fecha as Date))=:anho AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia  order by v.id desc", nativeQuery = true)
	List<CierreCaja> getCierreCajaPorFecha(@Param("anho") int ano, @Param("mes") int mes, @Param("dia") int dia);
	@Modifying
    @Transactional(readOnly=false)
	@Query("update CierreCaja set estadoEntrega=:estado where id=:id")
    public void findByActualizarEntregado(@Param("id") int id, @Param("estado")Boolean estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update CierreCaja set estadoAnulacion=true where id=:id")
    public void findByAnularCierreCaja(@Param("id") int id );

	@Modifying
    @Transactional(readOnly=false)
	@Query("update CierreCaja set estadoRecibo=:estado where id=:id")
    public void findByActualizarRecibido(@Param("id") int id, @Param("estado")Boolean estado );
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE CierreCaja set monto=monto-:montoC WHERE apertura_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVenta(@Param("id") int id, @Param("montoC") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE CierreCaja set montoCheque=montoCheque-:montoC WHERE apertura_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVentaCheque(@Param("id") int id, @Param("montoC") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE CierreCaja set montoTarjeta=montoTarjeta-:montoC WHERE apertura_caja_id=:id")
	public void findByActualizarCierreMontoAnulacionVentaTarjeta(@Param("id") int id, @Param("montoC") double monto);
	
	
	@Query(value="select caj.descripcion, pf.nombre || ' ' || pf.apellido as nombre, c.fecha, a.saldo_inicial, a.saldo_inicial_cheque, a.saldo_inicial_tarjeta, a.saldo_actual, a.saldo_actual_cheque, a.saldo_actual_tarjeta, c.monto, c.monto_cheque, c.monto_tarjeta, c.imputacion_ingreso, c.imputacion_egreso, c.imputacion_ingreso_cheque, c.imputacion_egreso_cheque , c.imputacion_ingreso_tarjeta, c.imputacion_egreso_tarjeta, a.id as idApertura, c.estado_anulacion as esta, c.id as idCierre, c.hora as horaCierre, a.fecha as aperFecha, a.hora as horEper  from cierre_caja c inner join apertura_caja a on c.apertura_caja_id=a.id inner join caja caj on caj.id=a.caja_id inner join funcionario fun on fun.id= c.funcionario_id inner join persona pf on pf.id=fun.persona_id  where c.id=:id",nativeQuery=true)
	Object [][] getResumenCierreCaja(@Param("id") int id);
	
	@Query(value = "SELECT c.id FROM cierre_caja c WHERE apertura_caja_id=:id",nativeQuery = true)
	int IdCierreCaja(@Param("id") int id);
	
	
	
	@Query(value="select dt.id as id, dt.descripcion as des, dt.cantidad as cant, dt.precio as precio, dt.sub_total as subTotal, pf.nombre as nom, pf.apellido as ape from  detalle_servicios dt " + 
			" inner join funcionario fun on fun.id=dt.funcionario_id " + 
			" inner join persona pf on pf.id=fun.persona_id " + 
			" inner join venta v on v.id=dt.venta_id " + 
			" inner join operacion_caja op on op.id=v.operacion_caja " + 
			" inner join apertura_caja ap on ap.id=op.apertura_caja_id " + 
			" where ap.id = :id",nativeQuery=true)
	List<Object[]> getDetalleServicioPorApertura(@Param("id") int id);
	
	@Query(value="select sum(dt.sub_total) as subTotal from  detalle_servicios dt " + 
			" inner join funcionario fun on fun.id=dt.funcionario_id " + 
			" inner join persona pf on pf.id=fun.persona_id " + 
			" inner join venta v on v.id=dt.venta_id " + 
			" inner join operacion_caja op on op.id=v.operacion_caja " + 
			" inner join apertura_caja ap on ap.id=op.apertura_caja_id " + 
			" where ap.id = :id",nativeQuery=true)
	Double getDetalleServicioPorAperturaTotal(@Param("id") int id);
	
	@Query(value="select dt.id as id, dt.descripcion as des, dt.cantidad as cant, dt.precio as precio, dt.sub_total as subTotal from detalle_producto dt " + 
			" inner join venta v on v.id=dt.venta_id " + 
			" inner join operacion_caja op on op.id=v.operacion_caja " + 
			" inner join apertura_caja ap on ap.id=op.apertura_caja_id " + 
			" where ap.id = :id",nativeQuery=true)
	List<Object[]> getDetalleProductoPorApertura(@Param("id") int id);
	
	@Query(value="select sum(dt.sub_total) as subTotal from detalle_producto dt " + 
			" inner join venta v on v.id=dt.venta_id " + 
			" inner join operacion_caja op on op.id=v.operacion_caja " + 
			" inner join apertura_caja ap on ap.id=op.apertura_caja_id " + 
			" where ap.id = :id",nativeQuery=true)
	Double getDetalleProductoPorAperturaTotal(@Param("id") int id);
	
	
	@Query(value="select v.id from cierre_caja v order by v.id desc limit 1", nativeQuery = true)
	int getUltimoCierreCaja();
	
	
	
	
	
}

