package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CobrosCliente;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;

public interface CobrosClienteRepository extends JpaRepository<CobrosCliente, Serializable>{
	@Query(value="select c.id, pf.nombre || ', ' || pf.apellido as pf, pc.nombre || ', ' || pc.apellido as pc, c.total,c.fecha "
			+ "from cobros_cliente c "
			+ "inner join cuenta_cobrar_cabecera cl on c.cuenta_cobrar_cabecera_id=cl.id  "
			+ "inner join funcionario f on c.funcionario_id=f.id "
			+ "inner join persona pf on f.persona_id=pf.id "
			+ "inner join cliente cli on cl.cliente_id=cli.id "
			+ "inner join persona pc on cli.persona_id=pc.id "
			+ "where extract(year from cast(c.fecha as Date))= :ano AND extract(month from cast(c.fecha as Date))=:mes AND extract(day from cast(c.fecha as Date))=:dia order by id desc",nativeQuery=true)
	List<Object[]> getCobros(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CobrosCliente set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarCobrosOperacion(@Param("id") int id, @Param("operacionCaja") int operacionCaja);
	public abstract CobrosCliente findTop1ByOrderByIdDesc();
	@Query(value="select c.id, pf.nombre || ', ' || pf.apellido as pf, pc.nombre || ', ' || pc.apellido as pc, c.total,c.fecha "
			+ "from cobros_cliente c "
			+ "inner join cuenta_cobrar_cabecera cl on c.cuenta_cobrar_cabecera_id=cl.id  "
			+ "inner join funcionario f on c.funcionario_id=f.id "
			+ "inner join persona pf on f.persona_id=pf.id "
			+ "inner join cliente cli on cl.cliente_id=cli.id "
			+ "inner join persona pc on cli.persona_id=pc.id "
			+ "where pc.nombre ilike :descripcion or pc.apellido ilike :descripcion order by id desc",nativeQuery=true)
	List<Object[]> getBuscarClienteNombreApellido(@Param("descripcion") String descripcion);
	
	@Query(value = "select c from CobrosCliente c where cuenta_cobrar_cabecera_id=:idCuenta")
	public List<CobrosCliente> getCobrosPorIdCuenta(@Param("idCuenta") int idCuenta);
	@Query(value = "select c from CobrosClienteCabecera c where cliente_id=:idCliente")
	public List<CobrosClienteCabecera> getCobrosClienteCabeceraPorIdCliente(@Param("idCliente") int idCliente);
	
	@Query(value = "SELECT p.nombre || ' ' || p.apellido as nombres, to_char(cc.fecha, 'TMDay') || ' ' || extract(day from cast(cc.fecha as Date)) || ' ' || to_char(cc.fecha, 'TMMonth') || ' de ' || extract(year from cast(cc.fecha as Date)) as fecha, cc.fecha as fehc FROM cobros_cliente cc inner join cuenta_cobrar_cabecera ccc on cc.cuenta_cobrar_cabecera_id=ccc.id inner join cliente c on ccc.cliente_id=c.id inner join persona p on c.persona_id=p.id where ccc.cliente_id=:id order by cc.fecha desc limit 1",nativeQuery=true)
	public List<Object[]> getCabeceraCuentaClienteId(@Param("id") int id);
	
	@Query(value = "SELECT * FROM cobros_cliente cc inner join cuenta_cobrar_cabecera ccc on cc.cuenta_cobrar_cabecera_id=ccc.id inner join cliente c on ccc.cliente_id=c.id inner join persona p on c.persona_id=p.id where ccc.cliente_id=:id and ccc.saldo > 0 order by cc.fecha desc limit 1",nativeQuery=true)
	public CobrosCliente getCabeceraCuentaClienteIdDeta(@Param("id") int id);
	
	@Query(value = "SELECT * FROM cobros_cliente cc inner join cuenta_cobrar_cabecera ccc on cc.cuenta_cobrar_cabecera_id=ccc.id where ccc.id=:id",nativeQuery=true)
	public CobrosCliente getCobroPorCuentaId(@Param("id") int id);
	
	@Query(value = "SELECT ccc.fecha, p.nombre, p.apellido FROM cuenta_cobrar_cabecera ccc INNER JOIN cliente c ON ccc.cliente_id=c.id INNER JOIN persona p ON c.persona_id=p.id  WHERE c.id = :id order by ccc.fecha desc limit 1",nativeQuery=true)
	public List<Object[]> getCuentaCobrarCabeceraIdCliente(@Param("id") int id);
	
	@Query(value = "select pfun.nombre || ' ' || pfun.apellido as funcionarioCobros, cob.fecha as fec, cob.total as totalCob, cob.id as idCob  from cobros_cliente_cabecera cob  inner join funcionario fun on fun.id= cob.funcionario_id inner join persona pfun ON pfun.id= fun.persona_id where cliente_id =:id ORDER BY cob.id ASC",nativeQuery=true)
	public List<Object[]> getCobrosClienteCabecera(@Param("id") int id);
	
	@Query(value = "SELECT to_char(ccc.fecha, 'TMMonth')  || '-' || date_part('year', ccc.fecha ) as fecha, sum(ccc.total) as total, sum(ccc.pagado) as pagado, sum(ccc.saldo) as saldo, extract(month from cast(ccc.fecha as Date)) as mes FROM cuenta_cobrar_cabecera ccc  where cliente_id = :id and  extract(year from cast(ccc.fecha as Date))=:anho and ccc.pagado = ccc.total group by to_char(ccc.fecha, 'TMMonth'), date_part('year', ccc.fecha), mes order by mes desc",nativeQuery=true)
	public List<Object[]> getCuentaClienteIdCobrado(@Param("id") int id, @Param("anho") int anho);
	
	@Query(value = "SELECT to_char(ccc.fecha, 'TMMonth')  || '-' || date_part('year', ccc.fecha ) as fecha, sum(ccc.total) as total, sum(ccc.pagado) as pagado, sum(ccc.saldo) as saldo, extract(month from cast(ccc.fecha as Date)) as mes FROM cuenta_cobrar_cabecera ccc  where cliente_id = :id and  extract(year from cast(ccc.fecha as Date))=:anho and ccc.saldo > 0 group by to_char(ccc.fecha, 'TMMonth'), date_part('year', ccc.fecha), mes order by mes desc",nativeQuery=true)
	public List<Object[]> getCuentaClienteIdACobrar(@Param("id") int id, @Param("anho") int anho);
	
	@Query(value = "SELECT ccc.cliente_id AS cc, ccc.cliente_id as oo FROM cuenta_cobrar_cabecera ccc where ccc.saldo > 0 GROUP BY ccc.cliente_id",nativeQuery=true)
	public List<Object[]> getListadoClienteCobrosCliente();
	
	@Query(value = "SELECT sum(saldo) FROM cuenta_cobrar_cabecera where cliente_id=:id",nativeQuery=true)
	public Double getSaldoIdCliente(@Param("id") int id);
	
	@Query("SELECT c FROM CobrosClienteCabecera c WHERE id=:id")
	public CobrosClienteCabecera getCobrosCabeceraPorId(@Param("id") int id);

	

}
