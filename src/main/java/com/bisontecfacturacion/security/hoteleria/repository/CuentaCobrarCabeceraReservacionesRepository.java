package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.hoteleria.model.CuentaCobrarCabeceraReservaciones;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;

@Transactional(readOnly=true)
@Repository
public interface CuentaCobrarCabeceraReservacionesRepository extends JpaRepository<CuentaCobrarCabeceraReservaciones, Serializable>{
	public abstract CuentaCobrarCabeceraReservaciones findTop1ByOrderByIdDesc();
	@Query(value = "select c from CuentaCobrarCabeceraReservaciones c where reservaciones_cabecera_id=:id")
	CuentaCobrarCabecera getCuentaCabeceraPorReservacionesId(@Param("id") int id);

	@Modifying
	@Transactional(readOnly=false)
	@Query(value = "update cuenta_cobrar_cabecera_reservaciones set pagado=pagado+:monto, saldo = saldo-:monto where id=:id", nativeQuery = true)
	public void findByActualizarPagadoCuentaReservaciones(@Param("id") int id, @Param("monto")double monto );


	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera_reservaciones c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where (c.saldo > 0 and persona.cedula like :desc) or (c.saldo > 0 and persona.apellido like :desc) or (c.saldo > 0 and persona.nombre like :desc) group by persona.nombre, persona.apellido, cliente.id, persona.cedula", nativeQuery = true)
	List<Object []> getAllCuentaACobrar(@Param("desc") String des);
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera_reservaciones c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.saldo > 0 group by persona.nombre, persona.apellido, cliente.id , persona.cedula",nativeQuery = true)
	List<Object[]> getCLienteCuentaACobrar();

	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera_reservaciones c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where (c.pagado = c.saldo and persona.cedula like '%') or (c.pagado = c.saldo and persona.apellido like :desc) or (c.pagado = c.saldo and persona.nombre like :desc) group by persona.nombre, persona.apellido, cliente.id, persona.cedula", nativeQuery = true)
	List<Object []> getAllCuentaCobrado(@Param("desc") String des);
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, cliente.id, persona.cedula from cliente inner join cuenta_cobrar_cabecera_reservaciones c on c.cliente_id = cliente.id inner join persona on persona.id=cliente.persona_id where c.pagado = c.total  group by persona.nombre, persona.apellido, cliente.id , persona.cedula",nativeQuery = true)
	List<Object[]> getCLienteCuentaCobrado();


	@Query("select  c from CuentaCobrarCabeceraReservaciones c where cliente_id=:id order by id desc")
	public List<CuentaCobrarCabeceraReservaciones> findByCuentaPorIdTodo(@Param("id") int id);

	@Query("select  c from CuentaCobrarCabeceraReservaciones c where saldo > 0 and cliente_id=:id order by id desc")
	public List<CuentaCobrarCabeceraReservaciones> findByCuentaPorIdACobrarListas(@Param("id") int id);

	@Query("select  c from CuentaCobrarCabeceraReservaciones c where saldo = 0 and cliente_id=:id order by id desc")
	public List<CuentaCobrarCabeceraReservaciones> findByCuentaPorIdCobrar(@Param("id") int id);
}
