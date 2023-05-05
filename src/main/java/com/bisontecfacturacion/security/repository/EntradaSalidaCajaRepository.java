package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.EntradaSalidaCaja;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
@Repository
public interface EntradaSalidaCajaRepository extends JpaRepository<EntradaSalidaCaja, Serializable> {

	@Query(value="select * from entrada_salida_caja v inner join funcionario fcargo on fcargo.id=v.funcionario_id inner join persona pcargo on pcargo.id=fcargo.persona_id inner join tipo_operacion op on op.id=v.tipo_operacion_id inner join tipo_movimiento mov on mov.id=v.tipo_movimiento_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc",nativeQuery=true)
	List<EntradaSalidaCaja> getEntradaSalidaCajaFecha(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	public abstract EntradaSalidaCaja findTop1ByOrderByIdDesc();
	@Modifying
	@Transactional(readOnly=false)
	@Query("update EntradaSalidaCaja set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarEntradaSalidaCajaCabeceraOperacion(@Param("id") int id, @Param("operacionCaja") int operacionCaja);
	@Query("select c from EntradaSalidaCaja c where c.id=:id ")
	public EntradaSalidaCaja getEntradaSalidaPorId(@Param("id") int id);

}
