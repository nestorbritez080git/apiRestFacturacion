package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.TransferenciaTesoreria;

public interface TransferenciaTesoreriaRepository extends JpaRepository<TransferenciaTesoreria, Serializable> {
//	 
	@Query(value="select tf.id as id, tf.fecha as fecha,tf.tesoreria_id as idTesoreria, pft.nombre as nom, pft.apellido as ape, tf.monto as mon, tf.monto_cheque as monChe, tf.monto_tarjeta as monTarj from transferencia_tesoreria tf inner join funcionario ft on ft.id=tf.funcionario_id inner join persona pft on pft.id=ft.persona_id where caja_mayor_id=1",nativeQuery=true)
	List<Object[]>  consultarDetalleTransferenciTesoreria();
    
}
