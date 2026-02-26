package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.EmpaqueCabecera;
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
	
	
	
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id  order by v.id desc",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionAll();
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id  WHERE pr.nombre ilike :des OR pr.apellido ilike :des OR pr.cedula ilike :des OR pe.nombre ilike :des OR pe.apellido ilike :des OR pe.cedula ilike :des order by v.id desc",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionAllDescripcion(@Param("des")  String des);
	
	
	
	
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id  WHERE v.estado= 'PENDIENTE' order by v.id desc",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionAllPendiente();
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id  WHERE (v.estado='PENDIENTE' and pr.nombre ilike :des) OR (v.estado='PENDIENTE' and pr.apellido ilike :des) OR (v.estado='PENDIENTE' and pr.cedula ilike :des) OR (v.estado='PENDIENTE' and pe.nombre ilike :des) OR (v.estado='PENDIENTE' and pe.apellido ilike :des) OR (v.estado='PENDIENTE' and pe.cedula ilike :des)",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionPendienteDescripcion(@Param("des")  String des);
	
	
	
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id WHERE v.estado= 'ENTREGADO' order by v.id desc",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionAllEntregado();
	@Query(value="select * from orden_produccion v INNER JOIN funcionario f on v.funcionario_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionarioa_id=fe.id INNER JOIN persona pe on fe.persona_id=pe.id WHERE (v.estado='ENTREGADO' and pr.nombre ilike :des) OR (v.estado='ENTREGADO' and pr.apellido ilike :des) OR (v.estado='ENTREGADO' and pr.cedula ilike :des) OR (v.estado='ENTREGADO' and pe.nombre ilike :des) OR (v.estado='ENTREGADO' and pe.apellido ilike :des) OR (v.estado='ENTREGADO' and pe.cedula ilike :des)",nativeQuery=true)
	List<OrdenProduccion> getOrdenProduccionEntregadoDescripcion(@Param("des")  String des);
	
	
	

    @Query("SELECT DISTINCT o FROM OrdenProduccion o " +
           "INNER JOIN FETCH o.ordenProduccionDetalles det " +
           "LEFT JOIN FETCH o.produccionCostoCabecera pcc " +
           "LEFT JOIN FETCH o.funcionario fun " +
           "LEFT JOIN FETCH fun.persona perFun " +
           "LEFT JOIN FETCH o.funcionarioA funA " +
           "LEFT JOIN FETCH funA.persona perFunA " +
           "WHERE o.id = :id AND (o.estado = 'PENDIENTE' OR o.estado = 'ENTREGADO' OR o.estado = 'ANULADO') " +
           "ORDER BY det.id ASC")
    OrdenProduccion getOrdenProduccionPorId(@Param("id") int id);
	
	
}
