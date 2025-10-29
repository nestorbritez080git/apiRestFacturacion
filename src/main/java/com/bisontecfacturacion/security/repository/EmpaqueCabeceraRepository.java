package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.EmpaqueCabecera;

public interface EmpaqueCabeceraRepository extends JpaRepository<EmpaqueCabecera, Serializable>{
	public abstract EmpaqueCabecera findTop1ByOrderByIdDesc();
	@Modifying 
	@Transactional(readOnly=false)
	@Query("update EmpaqueCabecera set estado=:estado where id=:id")
	public void cambiarEstadoEmpaque(@Param("id") int id, @Param("estado") String estado);
	
	
	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id order by v.id desc",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueAll();
	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id WHERE pr.nombre ilike :des OR pr.apellido ilike :des OR pr.cedula ilike :des OR pe.nombre ilike :des OR pe.apellido ilike :des OR pe.cedula ilike :des OR z.descripcion ilike :des order by v.id desc",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueAllDescripcion(@Param("des")  String des);
	
	
	
	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id WHERE v.estado= 'ABIERTO' order by v.id desc",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueAbierto();
	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id WHERE (v.estado='ABIERTO' and pr.nombre ilike :des) OR (v.estado='ABIERTO' and pr.apellido ilike :des) OR (v.estado='ABIERTO' and pr.cedula ilike :des) OR (v.estado='ABIERTO' and pe.nombre ilike :des) OR (v.estado='ABIERTO' and pe.apellido ilike :des) OR (v.estado='ABIERTO' and pe.cedula ilike :des) OR (v.estado='ABIERTO' and z.descripcion ilike :des)",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueAbiertoDescripcion(@Param("des")  String des);
	
	

	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id WHERE v.estado= 'CERRADO' order by v.id desc",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueCerrado();
	@Query(value="select * from empaque_cabecera v INNER JOIN funcionario f on v.funcionario_registro_id=f.id INNER JOIN persona pr on f.persona_id=pr.id INNER JOIN funcionario fe ON v.funcionario_empaque_id=fe.id INNER JOIN persona pe on f.persona_id=pe.id  inner join zona z on z.id=v.zona_id WHERE (v.estado='CERRADO' and pr.nombre ilike :des) OR (v.estado='CERRADO' and pr.apellido ilike :des) OR (v.estado='CERRADO' and pr.cedula ilike :des) OR (v.estado='CERRADO' and pe.nombre ilike :des) OR (v.estado='CERRADO' and pe.apellido ilike :des) OR (v.estado='CERRADO' and pe.cedula ilike :des) OR (v.estado='CERRADO' and z.descripcion ilike :des)",nativeQuery=true)
	List<EmpaqueCabecera> getEmpaqueCerradoDescripcion(@Param("des")  String des);
	
	
	@Query("SELECT DISTINCT c FROM EmpaqueCabecera c " + 
			"        INNER JOIN FETCH c.empaqueDetalle det " + 
			"        INNER JOIN FETCH det.venta ven " + 
			"        LEFT JOIN FETCH ven.cliente cli " + 
			"        LEFT JOIN FETCH cli.persona perCli " + 
			"        LEFT JOIN FETCH ven.funcionario fun " + 
			"        LEFT JOIN FETCH fun.persona perFun  " + 
			"        LEFT JOIN FETCH ven.funcionarioV funV " + 
			"        LEFT JOIN FETCH funV.persona perFunv " + 
			"        LEFT JOIN FETCH ven.funcionarioR funR " + 
			"        LEFT JOIN FETCH funR.persona perFunR " + 
			"        WHERE c.id = :id AND (ven.estado = 'PREVENTA' OR ven.estado = 'FACTURADO') " + 
			"        ORDER BY ven.id ASC")
	public EmpaqueCabecera getEmpaqueVenta(@Param("id") int id);
	@Query("SELECT DISTINCT c FROM EmpaqueCabecera c " +
		       "LEFT JOIN FETCH c.empaqueDetalle det " +
		       "LEFT JOIN FETCH det.presupuesto pre " +
		       "WHERE c.id = :id ORDER BY pre.id ASC")
	public EmpaqueCabecera getEmpaquePresupuesto(@Param("id") int id);
	
	@Query("SELECT DISTINCT c FROM EmpaqueCabecera c " +
		       "JOIN FETCH c.empaqueDetalle det " +
		       "JOIN FETCH det.venta ven " +
		       "LEFT JOIN FETCH ven.cliente cli " +
		       "LEFT JOIN FETCH cli.persona perCli " +
		       "LEFT JOIN FETCH ven.funcionario fun " +
		       "LEFT JOIN FETCH fun.persona perFun " +
		       "LEFT JOIN FETCH ven.funcionarioV funV " +
		       "LEFT JOIN FETCH funV.persona perFunv " +
		       "LEFT JOIN FETCH ven.funcionarioR funR " +
		       "LEFT JOIN FETCH funR.persona perFunR " +
		       "WHERE ven.id = :idVenta " +
		       "AND (ven.estado = 'PREVENTA' OR ven.estado = 'FACTURADO') " +
		       "ORDER BY ven.id ASC")
		EmpaqueCabecera getEmpaquePorVentaCabecerass(@Param("idVenta") int idVenta);
	
	
	@Query("SELECT dvp.producto.id AS productoId, " +
		       "       dvp.producto.descripcion AS proDescripcion, " +
		       "       mp.descripcion AS desMarca, " +
		       "       SUM(dvp.cantidad) AS totalCantidad " +
		       "FROM EmpaqueCabecera ec " +
		       "JOIN ec.empaqueDetalle ed " +
		       "JOIN ed.venta v " +
		       "JOIN v.detalleProducto dvp " +
		       "JOIN dvp.producto pro " +
		       "JOIN pro.marca mp " +
		       "WHERE ec.id = :id " +
		       "  AND (v.estado = 'PREVENTA'  OR v.estado = 'FACTURADO') " +
		       "GROUP BY dvp.producto.id, dvp.producto.descripcion, mp.descripcion")
		List<Object[]> getResumenProductosPorEmpaque(@Param("id") int id);
	
}
