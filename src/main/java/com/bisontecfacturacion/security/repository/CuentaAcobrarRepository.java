package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;


@Transactional(readOnly=true)
@Repository
public interface CuentaAcobrarRepository extends JpaRepository<CuentaCobrarCabecera, Serializable> {
	public abstract CuentaCobrarCabecera findTop1ByOrderByIdDesc();
	@Query(value = "select c from CuentaCobrarCabecera c where cliente_id=:idCliente order by c.id desc ")
	public List<CuentaCobrarCabecera> consultarCuentaIdCliente(@Param("idCliente") int idCliente);

	@Query(value = "select c from CuentaCobrarCabecera c where venta_id=:id")
	CuentaCobrarCabecera getCuentaCabeceraPorVentaId(@Param("id") int id);

	@Modifying
    @Transactional(readOnly=false)
	@Query(value = "update cuenta_cobrar_cabecera set pagado=pagado+:monto, saldo = saldo-:monto where id=:id", nativeQuery = true)
	public void findByActualizarPagadoCuenta(@Param("id") int id, @Param("monto")double monto );
	
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where (c.saldo > 0 and persona.cedula like :desc) or (c.saldo > 0 and persona.apellido like :desc) or (c.saldo > 0 and persona.nombre like :desc) group by persona.nombre, persona.apellido, cliente.id, persona.cedula",nativeQuery = true)
	List<Object[]> getAllCuentaACobrar(@Param("desc") String des);

	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where (c.pagado = c.saldo and persona.cedula like '%') or (c.pagado = c.saldo and persona.apellido like :desc) or (c.pagado = c.saldo and persona.nombre like :desc) group by persona.nombre, persona.apellido, cliente.id, persona.cedula",nativeQuery = true)
	List<Object[]> getAllCuentaCobrado(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.saldo > 0 group by persona.nombre, persona.apellido, cliente.id , persona.cedula",nativeQuery = true)
	List<Object[]> getCLienteCuentaACobrar();
	
	
	@Query(value= "select sum(saldo) as saldoPendiente,  cliente.limite_credito as limite, cliente.estado_bloqueo from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.saldo > 0 and cliente.id=:id group by persona.nombre, persona.apellido, cliente.id , persona.cedula ",nativeQuery = true)
	List<Object[]> getCLienteCuentaACobrarPorIdCliente(@Param("id") int id);
	

	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.pagado = c.total  group by persona.nombre, persona.apellido, cliente.id , persona.cedula",nativeQuery = true)
	List<Object[]> getCLienteCuentaCobrado();

	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.saldo > 0 and cliente.id =:id group by persona.nombre, persona.apellido, cliente.id , persona.cedula",nativeQuery = true)
	List<Object[]> getCLienteCuentaPorIdCliente(@Param("id") int id);
	public abstract List<CuentaCobrarCabecera> findTop100ByOrderByIdDesc();


    @Query("select  c from CuentaCobrarCabecera c where cliente_id=:id order by id desc")
	public List<CuentaCobrarCabecera> findByCuentaPorIdTodo(@Param("id") int id);

	@Query("select  c from CuentaCobrarCabecera c where saldo > 0 and cliente_id=:id order by id desc")
	public List<CuentaCobrarCabecera> findByCuentaPorIdACobrarListas(@Param("id") int id);
	
	@Query("select  c from CuentaCobrarCabecera c where saldo > 0 and cliente_id=:id order by id asc")
	public List<CuentaCobrarCabecera> findByCuentaPorIdACobrars(@Param("id") int id);

	@Query("select  c from CuentaCobrarCabecera c where saldo = 0 and cliente_id=:id order by id desc")
	public List<CuentaCobrarCabecera> findByCuentaPorIdCobrar(@Param("id") int id);
	
	@Query(value = "select c.id as id from cuenta_cobrar_cabecera c where c.venta_id =:id", nativeQuery = true)
	public int findByCuentaPorIdVenta(@Param("id") int id);
	
	
	
}
