package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.OrdenProduccion;

@Repository
public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, Serializable> {

	@Query(value="select * from orden_produccion o inner join produccion_costo_cabecera pc on pc.id=o.produccion_costo_cabecera_id inner join funcionario fun on fun.id= o.funcionario_id inner join persona pfr on fun.persona_id = pfr.id inner join funcionario funa on funa.id=o.funcionarioa_id inner join persona pfa on funa.persona_id=pfa.id where  extract(year from cast(o.fecha as Date))=:ano AND extract(month from cast(o.fecha as Date))=:mes AND extract(day from cast(o.fecha as Date))=:dia order by o.id desc",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccion(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	public abstract OrdenProduccion findTop1ByOrderByIdDesc();
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update OrdenProduccion set estado=:estado, cantidadEntregada=:cantidadEntrega where id=:id")
    public void actualizarEstadoEntrega(@Param("id") int id, @Param("estado")Boolean estado, @Param("cantidadEntrega")Double cantidadEntrega);
	
}
