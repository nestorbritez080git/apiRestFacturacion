package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;

public interface EmpaqueCabeceraRepository extends JpaRepository<EmpaqueCabecera, Serializable>{
	public abstract EmpaqueCabecera findTop1ByOrderByIdDesc();
	@Modifying 
	@Transactional(readOnly=false)
	@Query("update EmpaqueCabecera set estado=:estado where id=:id")
	public void CambiarEstadoEmpaque(@Param("id") int id, @Param("estado") String estado);
	
	
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
	
	
}
