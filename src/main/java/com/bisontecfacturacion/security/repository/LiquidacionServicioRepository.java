package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.LiquidacionServicio;
@Transactional(readOnly=true)
@Repository
public interface LiquidacionServicioRepository extends JpaRepository<LiquidacionServicio, Serializable>{
	@Query(value="select dt.id as det, " + 
			"ser.descripcion as desc, " + 
			"dt.sub_total as monto, " + 
			"ser.porcentaje as porcentaje, " + 
			"((dt.sub_total*ser.porcentaje)/100)as comision, " + 
			"pc.nombre || ' ' || pc.apellido as cliente, " +
			"cl.id as idCli " + 
			"from detalle_servicios dt " + 
			"INNER JOIN servicio ser ON ser.id=dt.servicio_id " + 
			"INNER JOIN funcionario fun ON fun.id=dt.funcionario_id " + 
			"INNER JOIN persona pf ON pf.id=fun.persona_id " + 
			"INNER JOIN venta v ON v.id=dt.venta_id " + 
			"INNER JOIN cliente cl ON cl.id=v.cliente_id " + 
			"INNER JOIN persona pc ON pc.id= cl.persona_id " + 
			"WHERE (v.fecha_factura >= :fecha_inicio) AND (v.fecha_factura <= :fecha_fin) and v.estado ='FACTURADO'  and fun.id=:idF  ",nativeQuery=true)
	List<Object []> getDetalleServicioFacturadoPorIdFuncionario(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("idF") int id);
	
	@Query(value="select " + 
			"det.id as id, det.comision as comi, det.monto as mon, det.porcentaje as porce, det.liquidacion_servicio_id as idCab, cl.id as idCli, pc.nombre || ' ' || pc.apellido as cliente, dtSer.descripcion as descrip " + 
			"from liquidacion_servicio_detalle det " + 
			"INNER JOIN detalle_servicios dtSer ON dtSer.id=det.detalle_servicios_id " + 
			"INNER JOIN servicio ser ON ser.id=dtSer.servicio_id " + 
			"INNER JOIN funcionario fun ON fun.id=dtSer.funcionario_id " + 
			"INNER JOIN persona pf ON pf.id=fun.persona_id  " + 
			"INNER JOIN cliente cl ON cl.id=det.cliente_id " + 
			"INNER JOIN persona pc ON pc.id= cl.persona_id " + 
			"WHERE det.liquidacion_servicio_id=:id",nativeQuery=true)
	List<Object []> getDetalleLiquidacionPorIdCabecera(@Param("id") int id);
	
	
}
