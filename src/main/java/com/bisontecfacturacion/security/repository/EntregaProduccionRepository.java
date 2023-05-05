package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.EntregaProduccion;

public interface EntregaProduccionRepository extends JpaRepository<EntregaProduccion, Serializable>{
	public abstract EntregaProduccion findTop1ByOrderByIdDesc();
	@Query(value="select * from entrega_produccion ep inner join funcionario fr on ep.funcionario_id=fr.id inner join persona pr on fr.persona_id=pr.id inner join funcionario fe on ep.funcionario_entrega_id= fe.id inner join persona pe on fe.persona_id = pe.id inner join orden_produccion op on ep.orden_produccion_id= op.id inner join produccion_costo_cabecera pccab on op.produccion_costo_cabecera_id=pccab.id  where  extract(year from cast(ep.fecha as Date))=:ano AND extract(month from cast(ep.fecha as Date))=:mes AND extract(day from cast(ep.fecha as Date))=:dia order by ep.id desc",nativeQuery=true)
	List<EntregaProduccion> getEntregaProduccion(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	
}

