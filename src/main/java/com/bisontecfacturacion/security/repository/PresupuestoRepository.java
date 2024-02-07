package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Presupuesto;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Serializable>{
	public abstract Presupuesto findTop1ByOrderByIdDesc();
	
	@Query(value="select * from presupuesto v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id order by v.id desc",nativeQuery=true)
	List<Presupuesto> getPresupuestoAll();
	
	
	@Query(value="select * from presupuesto v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id where v.estado='ABIERTO' order by v.id desc",nativeQuery=true)
	List<Presupuesto> getPresupuestoActivo();
	
	
	@Query(value="select * from presupuesto v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join cliente cl on v.cliente_id=cl.id inner join persona cp on cl.persona_id=cp.id where v.estado='CERRADO' order by v.id desc",nativeQuery=true)
	List<Presupuesto> getPresupuestoCerrado();
	
	
	
	@Query(value="select dp.id as detalleId, dp.producto_id as productoId ,dp.descripcion, dp.cantidad,dp.iva, dp.precio, dp.sub_total, dp.presupuesto_id, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, dp.descuento, unidad_medida.descripcion as unidad, p.existencia, dp.is_balanza,p.codbar as procodbar, m.descripcion as descrimarca from detalle_presupuesto_producto dp inner join producto p on dp.producto_id=p.id inner join unidad_medida on p.unidad_medida_id=unidad_medida.id inner join marca m on p.marca_id=m.id where dp.presupuesto_id=:id",nativeQuery=true)
	List<Object[]> listaDetallePresupuestoProducto(@Param("id") int id);
	
	@Query(value="select dp.servicio_id as idServ, "
			+ "dp.descripcion as desServ, "
			+ "dp.cantidad as cantDet, "
			+ "dp.precio as precio, "
			+ "dp.sub_total as subtotal, "
			+ "dp.iva as iva, "
			+ "dp.monto_iva as montoIva, "
			+ "fun.id as idFunc, "
			+ "pf.nombre as nomFunc, "
			+ "pf.apellido as apeFunc, "
			+ "dp.id as idd, "
			+ "dp.obs from detalle_presupuesto_servicio dp inner join funcionario fun on dp.funcionario_id=fun.id inner join persona pf on pf.id=fun.persona_id where dp.presupuesto_id=:id",nativeQuery=true)
	List<Object[]> listaDetallePresupuestoServicio(@Param("id") int id);
	
	
	@Query(value = "select pc.nombre || ' ' || pc.apellido as nombreCliente, pc.cedula, pc.direccion, v.fecha, v.hora, pv.nombre || ' ' || pv.apellido as nombreVendedor, v.total_iva_cinco, v.total_iva_dies, v.total, v.total_letra, v.nro_documento from presupuesto v  inner join funcionario fv on v.funcionario_id=fv.id inner join persona pv on fv.persona_id=pv.id inner join cliente c on v.cliente_id=c.id inner join persona pc on c.persona_id=pc.id where v.id=:id", nativeQuery = true)
	List<Object[]> getPresupuestoId(@Param("id") int id);
	
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Presupuesto set estado=:estado where id=:id")
    public void findByActualizaEstado(@Param("id") int id, @Param("estado") String estado);
	
}
