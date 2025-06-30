package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.PagosFuncionario;

public interface PagosFuncionarioRepository extends JpaRepository<PagosFuncionario, Serializable> {
	@Query(value="select *\r\n" + 
			"from pagos_funcionario a \r\n" + 
			"inner join funcionario fr on fr.id=a.funcionario_registro_id \r\n" + 
			"inner join persona pr on pr.id=fr.persona_id \r\n" + 
			"inner join funcionario fp on fp.id=a.funcionario_pago_id \r\n" + 
			"inner join persona pp on pp.id=fp.persona_id \r\n" + 
			"inner join tipo_pago tp on  tp.id=a.tipo_pago_id \r\n" + 
			"where pr.nombre ilike :filtro or pr.apellido ilike :filtro or pr.cedula ilike :filtro or pp.nombre ilike :filtro or pp.apellido ilike :filtro or pp.cedula ilike :filtro ORDER BY a.id desc", nativeQuery = true)
	List<PagosFuncionario> consultarTodoPorFiltro(@Param("filtro") String filtro);
	
	@Query(value="select *\r\n" + 
			"from pagos_funcionario a \r\n" + 
			"inner join funcionario fr on fr.id=a.funcionario_registro_id \r\n" + 
			"inner join persona pr on pr.id=fr.persona_id \r\n" + 
			"inner join funcionario fp on fp.id=a.funcionario_pago_id \r\n" + 
			"inner join persona pp on pp.id=fp.persona_id \r\n" + 
			"inner join tipo_pago tp on  tp.id=a.tipo_pago_id ORDER BY a.id desc", nativeQuery = true)
	List<PagosFuncionario> consultarTodo();
	
	public abstract PagosFuncionario findTop1ByOrderByIdDesc();

	@Modifying 
	@Transactional(readOnly=false)
	@Query("update PagosFuncionario set estado=:estado where id=:id")
	public void anularPagosFuncionario(@Param("id") int id, @Param("estado") String estado);
	
	
}
