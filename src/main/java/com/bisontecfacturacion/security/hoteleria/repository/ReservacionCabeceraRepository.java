package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
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
			"where extract(year from cast(v.fecha_registro as Date))=:ano AND extract(month from cast(v.fecha_registro as Date))=:mes AND extract(day from cast(v.fecha_registro as Date))=:dia order by v.id desc ",nativeQuery=true)
	List<ReservacionCabecera> getReservacionesAll (@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

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

}
