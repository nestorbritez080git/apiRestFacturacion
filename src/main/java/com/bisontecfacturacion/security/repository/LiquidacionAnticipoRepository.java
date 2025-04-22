package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;
import com.bisontecfacturacion.security.model.LiquidacionAnticipo;
import com.bisontecfacturacion.security.model.LiquidacionServicio;
@Transactional(readOnly=true)
@Repository
public interface LiquidacionAnticipoRepository extends JpaRepository<LiquidacionAnticipo, Serializable>{
	public abstract LiquidacionAnticipo findTop1ByOrderByIdDesc();
	@Query("select SUM(c.total) as total, fun.id, per.nombre, per.apellido, per.cedula from LiquidacionAnticipo c INNER JOIN c.funcionarioLiquidacion fun INNER JOIN fun.persona per group by fun.id, per.nombre, per.apellido, per.cedula")
	public List<Object[]> getAllAgrupadoPorFuncionario();
	
//			select c from CuentaCobrarCabecera c where cliente_id=:idCliente order by c.id desc
	@Query("select c from LiquidacionAnticipo c WHERE funcionario_liquidacion_id=:id ORDER BY c.id desc")
	public List<LiquidacionAnticipo> getLiquidacionListaPorIdFuncionario(@Param("id") int id);

	
	

	
	
}
