package com.bisontecfacturacion.security.contabilidad.repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaDTO;
import com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaPorConceptoDTO;
import com.bisontecfacturacion.security.educacion.model.Alumno;
import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.model.Venta;

public interface AsientoContableRepository extends JpaRepository<AsientoContable, Serializable> {

	public abstract AsientoContable findTop1ByOrderByIdDesc();
	
	@Query("SELECT new com.bisontecfacturacion.security.contabilidad.model.ResumenCuentaDTO(" +
		       "tcc.nombre, cc.nombre, SUM(d.debe), SUM(d.haber), SUM(d.debe) - SUM(d.haber)) " +
		       "FROM AsientoContableDetalle d " +
		       "JOIN d.cuentaContable cc " +
		       "JOIN cc.tipoCuentaContable tcc " +
		       "JOIN d.asientoContable a " +
		       "WHERE a.fechaRegistro BETWEEN :inicio AND :fin " +
		       "GROUP BY tcc.nombre, cc.nombre " +
		       "ORDER BY tcc.nombre, cc.nombre")
	List<ResumenCuentaDTO> obtenerResumenContable(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

	@Query(value = "SELECT \r\n" + 
			"  cc.nombre AS cuenta,\r\n" + 
			"  tcc.nombre AS tipoCuenta,\r\n" + 
			"  COUNT(*) AS cantidadConcepto,\r\n" + 
			"  SUM(d.debe) AS totalDebe,\r\n" + 
			"  SUM(d.haber) AS totalHaber,\r\n" + 
			"  SUM(d.debe) - SUM(d.haber) AS saldo\r\n" + 
			"FROM detalle_asiento d\r\n" + 
			"JOIN cuenta_contable cc ON d.cuenta_contable_id = cc.id\r\n" + 
			"JOIN tipo_cuenta_contable tcc ON cc.tipo_cuenta_contable_id = tcc.id\r\n" + 
			"JOIN asiento_contable a ON d.asiento_contable_id = a.id\r\n" + 
			"WHERE a.fecha_registro BETWEEN :inicio AND :fin\r\n" + 
			"GROUP BY cc.nombre, cc.codigo, tcc.nombre\r\n" + 
			"ORDER BY tcc.nombre, cc.nombre", nativeQuery = true)
	List<Object[]> obtenerResumenContablePorConcepto(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

}
