package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.AperturaCaja;
@Transactional
@Repository
public interface AperturaCajaRepository extends JpaRepository<AperturaCaja, Serializable>{

	@Query("select a from AperturaCaja a where a.estado = true and a.estadoAnulacion = false")
	public List<AperturaCaja> getAperturaCajaActivo();
	@Query("select a from AperturaCaja a INNER JOIN a.funcionario as fun where a.estado = true and a.estadoAnulacion = false AND fun.id<>:idDistinto")
	public List<AperturaCaja> getAperturaCajaActivoIdDistinto(@Param("idDistinto")int idDistinto);

	@Query("select a from AperturaCaja a where funcionario_id=:idFuncionario order by id desc")
	public List<AperturaCaja> getAperturaCajaPorFuncionario(@Param("idFuncionario")int idFuncionario);
	
	public abstract List<AperturaCaja>findTop100ByOrderByIdDesc();
	
	@Query(value = "select * from apertura_caja v inner join caja c on c.id= v.caja_id inner join funcionario fp on fp.id= v.funcionario_id inner join persona pf on pf.id = fp.persona_id where extract(year from cast(v.fecha as Date))=:anho AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia  order by v.id desc", nativeQuery = true)
	List<AperturaCaja>getAperturaPorFecha(@Param("anho") int ano, @Param("mes") int mes, @Param("dia") int dia); 
	
	@Query(value="select id, funcionario_id, saldo_inicial, saldo_actual, caja_id, saldo_inicial_cheque, saldo_actual_cheque, saldo_inicial_tarjeta, saldo_actual_tarjeta from apertura_caja where caja_id =:id and estado=true ",nativeQuery=true )
	public List<Object[]> getAperturaActivo(@Param("id") int id);
	
	@Query(value = "select  ac.id, pf.nombre,pf.apellido, c.descripcion from apertura_caja  ac inner join caja c on c.id= ac.caja_id inner join funcionario f on f.id=ac.funcionario_id inner join persona pf on pf.id= f.persona_id where ac.estado=true", nativeQuery = true)
	List<Object[]>getFuncionarioCajaActivo(); 

	@Query(value = "select oc.fecha,  oc.monto, oc.efectivo, oc.vuelto, oc.motivo  from operacion_caja oc inner join apertura_caja ac on ac.id=oc.apertura_caja_id inner join funcionario fc on fc.id= ac.funcionario_id inner join persona pf on pf.id= fc.persona_id where ac.id=:idApertura", nativeQuery = true)
	List<Object[]>arqueoCajaActivo(@Param("idApertura") int idApertura); 
	
	@Query(value="select apertura_caja.id as aperid, caja_id, caja.descripcion, persona.nombre || ', ' ||  persona.apellido as nombre "
			+ "from apertura_caja inner "
			+ "join caja on caja.id= apertura_caja.caja_id "
			+ "inner join funcionario on apertura_caja.funcionario_id=funcionario.id "
			+ "inner join persona on funcionario.persona_id= persona.id "
			+ "where funcionario_id =:id and apertura_caja.estado=true and apertura_caja.estado_anulacion=false",nativeQuery=true )
	public List<Object[]> getAperturaActivoCaja(@Param("id") int id);
	
	@Query(value="SELECT * "
			+ "from apertura_caja inner "
			+ "join caja on caja.id= apertura_caja.caja_id "
			+ "inner join funcionario on apertura_caja.funcionario_id=funcionario.id "
			+ "inner join persona on funcionario.persona_id= persona.id "
			+ "where funcionario_id =:id and apertura_caja.estado=true and apertura_caja.estado_anulacion=false",nativeQuery=true )
	AperturaCaja getAperturaActivoCajaIdFuncionario(@Param("id") int id);
	
	@Query(value="select apertura_caja.id as aperid, caja_id, caja.descripcion, persona.nombre || ', ' ||  persona.apellido as nombre "
			+ "from apertura_caja inner "
			+ "join caja on caja.id= apertura_caja.caja_id "
			+ "inner join funcionario on apertura_caja.funcionario_id=funcionario.id "
			+ "inner join persona on funcionario.persona_id= persona.id "
			+ "where apertura_caja.estado=true and caja.id = 1",nativeQuery=true )
	public List<Object[]> getAperturaActivoCajaBalcon();
	
	@Query(value="select apertura_caja.id as aperid "
	+ "from apertura_caja inner "
	+ "join caja on caja.id= apertura_caja.caja_id "
	+ "inner join funcionario on apertura_caja.funcionario_id=funcionario.id "
	+ "inner join persona on funcionario.persona_id= persona.id "
	+ "where funcionario_id =:id and apertura_caja.estado=true",nativeQuery=true )
	public Integer getAperturaActivoCajaId(@Param("id") int id);


	@Query("select c from AperturaCaja c where c.estado=false")
	List<AperturaCaja> listAperturaFuncionarioFalse();

	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AperturaCaja set saldoActual=saldoActual-:monto WHERE id=:id")
	public void findByActualizarAperturaSaldoActualAnulacionVenta(@Param("id") int id, @Param("monto") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AperturaCaja set saldoActualCheque=saldoActualCheque-:monto WHERE id=:id")
	public void findByActualizarAperturaSaldoActualAnulacionVentaCheque(@Param("id") int id, @Param("monto") double monto);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("UPDATE AperturaCaja set saldoActualTarjeta=saldoActualTarjeta-:monto WHERE id=:id")
	public void findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(@Param("id") int id, @Param("monto") double monto);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set saldoActual=saldoActual+:monto where id=:id")
    public void findByActualizarAperturaSaldo(@Param("id") int id, @Param("monto")double estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set saldoActualCheque=saldoActualCheque+:monto where id=:id")
    public void findByActualizarAperturaSaldoCheque(@Param("id") int id, @Param("monto")double estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set saldoActualTarjeta=saldoActualTarjeta+:monto where id=:id")
    public void findByActualizarAperturaSaldoTarjeta(@Param("id") int id, @Param("monto")double estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set estado=:estado where id=:id")
    public void findByActualizarFuncionario(@Param("id") int id, @Param("estado")Boolean estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set estadoAnulacion=:estado where id=:id")
    public void findByActualizarEstadoAnulacionApertura(@Param("id") int id, @Param("estado")Boolean estado );
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update AperturaCaja set estado=true where id=:id")
    public void findByReactivarAperturaCaja(@Param("id") int id );
	
	
   
	@Query("select c from AperturaCaja c where id=:id")
	AperturaCaja getAperturaCajaPorIdCaja(@Param("id") int id);
	
	
	@Query(value="select v.id from apertura_caja v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaAperturaCaja();
	
	
}
