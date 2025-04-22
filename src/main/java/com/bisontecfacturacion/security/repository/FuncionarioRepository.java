package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Funcionario;
@Transactional
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Serializable> {
	
	@Query("select f from Funcionario f where f.estado=false")
	public List<Funcionario> getFuncionarioFalse();
	
	@Query("select f from Funcionario f where f.estadoServicio=true")
	public List<Funcionario> getFuncionarioServicio();
	
	@Query("select f from  Funcionario f")
	public List<Funcionario> getFuncionarios();
	
	@Query(value ="select f.id, persona.direccion, persona.nombre, persona.apellido, persona.cedula, persona.telefono as telefono, f.sueldo_bruto as sueldo from funcionario f inner join persona on persona.id=f.persona_id where persona.nombre like :nombre or persona.apellido like :nombre or persona.cedula ilike :nombre", nativeQuery = true)
	List<Object[]> findByFuncionarioNombrePost(@Param("nombre") String descripcion);
	
	public abstract List<Funcionario> findTop100ByOrderByIdDesc();

	@Query("select f.id from Funcionario f where f.id=:id ")
	public int getId(@Param("id") int id);
	
	@Query("select f from Funcionario f where f.id=:id")
	public Funcionario getIdFuncionario(@Param("id") int id);
	
	@Query("select f from Funcionario f where users_id=:id and f.id <>:idFuncionario ")
	public Funcionario getIdFuncionarioEditar(@Param("id") int id, @Param("idFuncionario") int idFuncionario);
	
	@Query("select f from Funcionario f where persona_id=:id ")
	public Funcionario getIdPersona(@Param("id") int id);
	
	@Query("select f from Funcionario f where persona_id=:id and f.id <>:idFuncionario")
	public Funcionario getIdPersonaEditar(@Param("id") int id, @Param("idFuncionario") int idFuncionario);
	
	@Query(value = "select count(f.id) FROM Funcionario f",nativeQuery = true)
	Object[] findByFuncionario();
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Funcionario set estado=:estado where id=:id")
    public void findByActualizarFuncionario(@Param("id") int id, @Param("estado")Boolean estado );
	
}
