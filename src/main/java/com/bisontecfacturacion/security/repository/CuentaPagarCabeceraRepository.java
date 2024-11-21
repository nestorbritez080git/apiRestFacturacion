package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaPagarCabecera;


public interface CuentaPagarCabeceraRepository extends JpaRepository<CuentaPagarCabecera, Serializable>{

	 public abstract CuentaPagarCabecera findTop1ByOrderByIdDesc();
	 
	 @Query(value= "select sum(cc.total) as total, sum(cc.pagado) as pagado, sum(cc.saldo) as saldoPendiente, pe.nombre, pe.apellido,pro.id, pe.cedula from cuenta_pagar_cabecera cc inner join proveedor pro on cc.proveedor_id=pro.id inner join persona pe on pro.persona_id=pe.id where cc.saldo > 0 group by pe.nombre, pe.apellido, pro.id , pe.cedula limit 50",nativeQuery = true)
	 List<Object[]> getCuentaAPagarAll();

	 @Query(value= "select sum(cc.total) as total, sum(cc.pagado) as pagado, sum(cc.saldo) as saldoPendiente, pe.nombre, pe.apellido,pro.id, pe.cedula from cuenta_pagar_cabecera cc inner join proveedor pro on cc.proveedor_id=pro.id inner join persona pe on pro.persona_id=pe.id where cc.pagado = cc.total group by pe.nombre, pe.apellido, pro.id , pe.cedula limit 50",nativeQuery = true)
	 List<Object[]> getCuentaPagadoAll();
	 
	 @Query(value= "select cab.id as id, pp.nombre || ' ' || pp.apellido as proveedor, pf.nombre || ' ' || pf.apellido as funcionario, cab.total, cab.pagado, cab.saldo, cab.compra_credito_id, cab.despacho_entrada_credito_id, c.fecha from cuenta_pagar_cabecera cab inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id inner join proveedor p on p.id=cab.proveedor_id inner join persona pp on pp.id=p.persona_id inner join compra c on cab.compra_credito_id=c.id where cab.proveedor_id=:id order by c.fecha desc",nativeQuery = true)
	 List<Object[]> getCuentaPagarPorProveeedorTodo(@Param("id")int id);
	 
	 @Query(value= "select cab.id as id, pp.nombre || ' ' || pp.apellido as proveedor, pf.nombre || ' ' || pf.apellido as funcionario, cab.total, cab.pagado, cab.saldo, cab.compra_credito_id, cab.despacho_entrada_credito_id, c.fecha from cuenta_pagar_cabecera cab inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id inner join proveedor p on p.id=cab.proveedor_id inner join persona pp on pp.id=p.persona_id inner join compra c on cab.compra_credito_id=c.id where cab.proveedor_id=:id and cab.saldo >0 order by c.fecha desc",nativeQuery = true)
	 List<Object[]> getCuentaPagarPorProveeedoAPagar(@Param("id")int id);
	 
	 @Query(value= "select cab.id as id, pp.nombre || ' ' || pp.apellido as proveedor, pf.nombre || ' ' || pf.apellido as funcionario, cab.total, cab.pagado, cab.saldo, cab.compra_credito_id, cab.despacho_entrada_credito_id, c.fecha from cuenta_pagar_cabecera cab inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id inner join proveedor p on p.id=cab.proveedor_id inner join persona pp on pp.id=p.persona_id inner join compra c on cab.compra_credito_id=c.id where cab.proveedor_id=:id and cab.saldo <=0 order by c.fecha desc",nativeQuery = true)
	 List<Object[]> getCuentaPagarPorProveeedorPagado(@Param("id")int id);
	 
	 @Query(value= "select cab.id as numero, cab.fraccion_cuota, pf.nombre || ' ' || pf.apellido as proveedor, cab.total as total, cab.pagado as pagado, cab.saldo as saldo, cab.compra_credito_id as compraId, cab.despacho_entrada_credito_id from cuenta_pagar_cabecera cab  inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id  where cab.compra_credito_id !=0 and cab.proveedor_id=:id",nativeQuery = true)
	 List<Object[]> getReporteExtractoCuentaPagarTodo(@Param("id")int id);
	 
	 @Query(value= "select cab.id as numero, cab.fraccion_cuota, pf.nombre || ' ' || pf.apellido as proveedor, cab.total as total, cab.pagado as pagado, cab.saldo as saldo, cab.compra_credito_id as compraId, cab.despacho_entrada_credito_id from cuenta_pagar_cabecera cab inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id  where cab.compra_credito_id!=0 and cab.proveedor_id=:id and cab.saldo >0",nativeQuery = true)
	 List<Object[]> getReporteExtractoCuentaPagarApagar(@Param("id")int id);
	 
	 @Query(value= "select cab.id as numero, cab.fraccion_cuota, pf.nombre || ' ' || pf.apellido as proveedor, cab.total as total, cab.pagado as pagado, cab.saldo as saldo, cab.compra_credito_id as compraId, cab.despacho_entrada_credito_id from cuenta_pagar_cabecera cab inner join funcionario f on f.id=cab.funcionario_id inner join persona pf on pf.id=f.persona_id  where cab.compra_credito_id!=0 and cab.proveedor_id=:id and cab.saldo <=0",nativeQuery = true)
	 List<Object[]> getReporteExtractoCuentaPagarPagado(@Param("id")int id);
	
	 @Query(value= "select det.descripcion as desc, det.cantidad as cant, det.precio_costo as precio, det.sub_total as subtotal from detalle_compra det where det.compra_id=:idCompra",nativeQuery = true)
	 List<Object[]> getReporteExtractoCuentaPagarDetalleCompra(@Param("idCompra")int idCompra);
	 
	 @Query(value= "select det.numero_cuota as cuota, det.monto as monto, det.importe as imorte, det.sub_total as subtotal, det.fecha_vencimiento as venci from cuenta_pagar_detalle det where det.cuenta_pagar_cabecera_id=:idCuenta",nativeQuery = true)
	 List<Object[]> getReporteExtractoCuentaPagarDetalleCuenta(@Param("idCuenta")int idCuenta);
	 
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update CuentaPagarCabecera set pagado=:pagado, saldo=:saldo where id=:id")
	 public void findByCancelarCuentaCabecera(@Param("pagado") double pagado, @Param("saldo") double saldo, @Param("id") int id);
	 
	 
	 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where c.saldo > 0 group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getProveedorCuentaPagarAll();
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.saldo > 0) and (c.compra_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getCompraCreditoAll();
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.saldo > 0) and (c.despacho_entrada_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getDespachoEntradaCreditoAll();
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where c.pagado = c.total  group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getProveedorCuentaPagado();
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.pagado = c.total) and (c.compra_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getProveedorCompraCuentaPagado();
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.pagado = c.total) and (c.despacho_entrada_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id , persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getProveedorDespEntradaCuentaPagado();
	
	///////////////////////filtro descripcion///////////////////////////
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.saldo > 0 and persona.cedula like :desc) or (c.saldo > 0 and persona.apellido like :desc) or (c.saldo > 0 and persona.nombre like :desc) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getAllCuentaAPagar(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.saldo > 0 and persona.cedula like :desc and c.compra_credito_id !=0) or (c.saldo > 0 and persona.apellido like :desc and c.compra_credito_id !=0) or (c.saldo > 0 and persona.nombre like :desc and c.compra_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getCompraCreditoFiltro(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.saldo > 0 and persona.cedula like :desc and c.despacho_entrada_credito_id !=0) or (c.saldo > 0 and persona.apellido like :desc and c.despacho_entrada_credito_id !=0) or (c.saldo > 0 and persona.nombre like :desc and c.despacho_entrada_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getDespEntradaCreditoFiltro(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.pagado = c.saldo and persona.cedula like '%') or (c.pagado = c.saldo and persona.apellido like :desc) or (c.pagado = c.saldo and persona.nombre like :desc) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getAllCuentaPagado(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.pagado = c.total and persona.cedula like :desc and c.compra_credito_id !=0) or (c.pagado = c.total and persona.apellido like :desc and c.compra_credito_id !=0) or (c.pagado = c.total and persona.nombre like :desc and c.compra_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getCompraCreditoFiltroPagado(@Param("desc") String des);
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where (c.pagado = c.total and persona.cedula like :desc and c.despacho_entrada_credito_id !=0) or (c.pagado = c.total and persona.apellido like :desc and c.despacho_entrada_credito_id !=0) or (c.pagado = c.total and persona.nombre like :desc and c.despacho_entrada_credito_id !=0) group by persona.nombre, persona.apellido, proveedor.id, persona.cedula limit 50",nativeQuery = true)
	List<Object[]> getDespSalidaCreditoFiltroPagado(@Param("desc") String des);

	
	//SERVICIO REPORTE CUENTA PROVEEDOR POR RANGI DE FECHA POR TIPO TODOS, PAGADO, A PAGAR
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun WHERE pro.id = :id AND (com.fechaFactura >=:fecIni AND com.fechaFactura <=:fecFin) order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdProveedorRangoFechaTodos(@Param("id") int id, @Param("fecIni") Date fecIni, @Param("fecFin") Date fecFin);
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun where cpc.saldo > 0 and pro.id = :id AND (com.fechaFactura >=:fecIni AND com.fechaFactura <=:fecFin) order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdProveedorRangoFechaAPagar(@Param("id") int id, @Param("fecIni") Date fecIni, @Param("fecFin") Date fecFin);
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun where cpc.saldo = 0 and pro.id = :id AND (com.fechaFactura >=:fecIni AND com.fechaFactura <=:fecFin) order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdProveedorRangoFechaPagado(@Param("id") int id, @Param("fecIni") Date fecIni, @Param("fecFin") Date fecFin);
	
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun WHERE pro.id = :id order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdTodo(@Param("id") int id);
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun where cpc.saldo > 0 and pro.id = :id order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdAPagarListas(@Param("id") int id);
	@Query("SELECT cpc FROM CuentaPagarCabecera cpc  INNER JOIN cpc.compra com INNER JOIN cpc.proveedor pro INNER JOIN cpc.funcionario fun where cpc.saldo = 0 and pro.id = :id order by cpc.id desc")
	public List<CuentaPagarCabecera> findByCuentaPorIdPagado(@Param("id") int id);
	
	
	
	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor  inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where c.saldo > 0 group by persona.nombre, persona.apellido, proveedor.id , persona.cedula",nativeQuery = true)
	List<Object[]> getProveedorCuentaACobrar();

	@Query(value= "select sum(total) as totalizdos, sum(pagado) as pagado, sum(saldo) as saldoPendiente, persona.nombre, persona.apellido, proveedor.id, persona.cedula from proveedor inner join cuenta_pagar_cabecera c on c.proveedor_id = proveedor.id inner join persona on persona.id=proveedor.persona_id where c.pagado = c.total  group by persona.nombre, persona.apellido, proveedor.id , persona.cedula",nativeQuery = true)
	List<Object[]> getProveedorCuentaCobrado();
	

}
