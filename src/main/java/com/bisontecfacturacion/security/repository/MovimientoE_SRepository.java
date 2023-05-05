package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;

@Repository
public interface MovimientoE_SRepository extends JpaRepository<MovimientoEntradaSalida, Serializable> {
    @Query(value="select * from movimiento_entrada_salida mov inner join tipo_movimiento t on t.id=mov.tipo_movimiento_id inner join producto p on p.id= mov.producto_id inner join funcionario f on f.id= mov.funcionario_id where mov.producto_id=:idProducto",nativeQuery=true)
    List<MovimientoEntradaSalida>  getMovimientoPorIdProducto(@Param("idProducto") int  idProducto);
    @Query(value="select * from movimiento_entrada_salida mov inner join  tipo_movimiento tipo on mov.tipo_movimiento_id=tipo.id inner join funcionario f on f.id=mov.funcionario_id inner join persona pf on pf.id=f.persona_id where mov.producto_id=:id and  ((mov.fecha >= :fecha_inicio) AND (mov.fecha <=  :fecha_fin)) and tipo.id=:tipo",nativeQuery=true)
    List<MovimientoEntradaSalida>  getMovimientoPorRango(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("tipo") int tipo, @Param("id") int id);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id where m.fecha >= :fecha_inicio AND m.fecha <=  :fecha_fin order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaTodo(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id where (m.ingreso = 0 and m.fecha >= :fecha_inicio) AND (m.ingreso = 0 and m.fecha <=  :fecha_fin) order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaEgreso(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario FROM movimiento_entrada_salida m where inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id  (m.egreso = 0 AND m.fecha >= :fecha_inicio) AND (m.egreso = 0 and m.fecha <=  :fecha_fin) order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaIngreso(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id inner join tipo_movimiento tipo on tipo.id=m.tipo_movimiento_id where m.fecha >= :fecha_inicio AND m.fecha <=  :fecha_fin AND m.marca=:marc order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaTodoMarca(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("marc") String marca);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario, tipo.descripcion as tipo FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id inner join tipo_movimiento tipo on tipo.id=m.tipo_movimiento_id WHERE (m.ingreso is null AND m.marca=:marc AND m.fecha >=:fecha_inicio) AND (m.ingreso is null AND m.marca=:marc AND m.fecha <=:fecha_fin) order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaEgresoMarca(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("marc") String marca);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario, tipo.descripcion as tipo FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id inner join tipo_movimiento tipo on tipo.id=m.tipo_movimiento_id WHERE (m.egreso is null AND m.marca=:marc AND m.fecha >=:fecha_inicio) AND (m.egreso is null AND m.marca=:marc AND m.fecha <=:fecha_fin) order by m.fecha desc",nativeQuery=true)
    List<Object[]>  getMovEntradaSalidaListaIngresoMarca(@Param("fecha_inicio") Date fecha_inicio, @Param("fecha_fin") Date fecha_fin, @Param("marc") String marca);
    
    @Query(value="SELECT m.descripcion, m.cantidad, m.egreso, m.ingreso, m.referencia, m.fecha, m.hora, m.marca, m.costo_salida, pf.nombre || ' ' || pf.apellido AS funcionario, tipo.descripcion as tipo, tipo.id as idTipo FROM movimiento_entrada_salida m inner join funcionario fun on fun.id=m.funcionario_id inner join persona pf on pf.id=fun.persona_id inner join tipo_movimiento tipo on tipo.id=m.tipo_movimiento_id where m.producto_id=:id order by m.id DESC",nativeQuery=true)
    List<Object[]>  getMovimientoProductoPorIdPRoducto(@Param("id") int id);
    
}
