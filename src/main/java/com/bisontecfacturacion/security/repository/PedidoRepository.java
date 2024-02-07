package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Pedido;
import com.bisontecfacturacion.security.model.Venta;

@Transactional(readOnly=true)
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Serializable>{
    public abstract Pedido findTop1ByOrderByIdDesc();

    @Query(value="select * from pedido v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc",nativeQuery=true)
	List<Pedido> getPedidoFecha(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);

    @Query(value="select * from pedido v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id where v.estado ='ABIERTO' ORDER BY v.id desc",nativeQuery=true)
	List<Pedido> getPedidoActivo();
    @Query(value="select * from pedido v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id where v.estado ='CERRADO' ORDER BY v.id desc",nativeQuery=true)
	List<Pedido> getPedidoCerrado();
    @Query(value="select * from pedido v inner join funcionario f on v.funcionario_id=f.id inner join persona pf on f.persona_id=pf.id inner join proveedor cl on v.proveedor_id=cl.id inner join persona cp on cl.persona_id=cp.id ORDER BY v.id desc",nativeQuery=true)
	List<Pedido> getPedidoAll();
    

    @Query(value="SELECT d.id as detId, p.id as proId, p.codbar, d.descripcion,d.cantidad,d.precio_costo,d.iva,d.sub_total, d.precio_venta_1, d.precio_venta_2, d.precio_venta_3, d.precio_venta_4, d.pedido_id, u.descripcion as descripcionDescrip, m.descripcion as marcadesc FROM pedido_detalle d INNER JOIN producto p ON d.producto_id=p.id INNER JOIN unidad_medida u ON p.unidad_medida_id=u.id INNER JOIN marca m ON p.marca_id=m.id  where d.pedido_id=:id",nativeQuery=true)
	List<Object[]> listaDetallePedidoProducto(@Param("id") int id);
    

	 @Modifying
	 @Transactional(readOnly=false)
	 @Query(value="UPDATE pedido SET estado=:est WHERE id=:idPedido", nativeQuery = true)
	 public void finalizarPedido( @Param("idPedido") int idPedido, @Param("est") String est);

    
    @Modifying
    @Transactional(readOnly=false)
    @Query(value="UPDATE pedido SET obs=:obs, total=:total, volumen_total=:volumenTotal, cliente_id=:idCliente, documento_id=:idDoc, funcionario_id=:idFunc, funcionariov_id=:idFuncV WHERE id=:id", nativeQuery = true)
    public void actualizarCabecera(@Param("obs") String obs,@Param("total") Double total, @Param("volumenTotal") Double volumenTotal,@Param("idCliente") int idCliente,@Param("idDoc") int idDoc,@Param("idFunc") int idFunc,@Param("idFuncV") int idFuncV,@Param("id") int id);
}  