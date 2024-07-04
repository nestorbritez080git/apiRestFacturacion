package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.PagosProveedorCompra;

public interface PagosProveedorCompraRepository extends JpaRepository<PagosProveedorCompra, Serializable> {
	public abstract PagosProveedorCompra findTop1ByOrderByIdDesc();
	@Modifying
	@Transactional(readOnly=false)
	@Query("update PagosProveedorCompra set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarPagosCompraCajaCabeceraOperacion(@Param("id") int id, @Param("operacionCaja") int operacionCaja);
	@Query(value = "SELECT  ppro.id, ppro.nombre, ppro.apellido, ppro.cedula, sum(pag.monto) as monEfe,  sum(pag.monto_cheque) as monChe, sum(pag.monto_tarjeta) as monTar, pag.id as idPag " + 
			" FROM pagos_proveedor_compra  pag " + 
			" INNER JOIN proveedor p ON p.id=pag.proveedor_id INNER JOIN persona ppro ON ppro.id=p.persona_id " + 
			" INNER JOIN funcionario f ON  f.id= pag.funcionario_id INNER JOIN persona pfun ON pfun.id=f.persona_id GROUP BY pag.id, ppro.id, ppro.nombre, ppro.apellido, ppro.cedula " + 
			" ORDER BY pag.id desc", nativeQuery = true)
	public List<Object[]> getPagosProveedorCompras();
	@Query(value = "SELECT pag.id as id, ppro.nombre, ppro.apellido, ppro.cedula, pfun.nombre as nomFun, pfun.apellido as apFunc, pag.fecha_factura as fec, pag.documento as doc,  pag.monto as monEfe, pag.monto_cheque as monChe, pag.monto_tarjeta as monTar " + 
			"		FROM pagos_proveedor_compra  pag  " + 
			"		INNER JOIN proveedor p ON p.id=pag.proveedor_id INNER JOIN persona ppro ON ppro.id=p.persona_id  " + 
			"		INNER JOIN funcionario f ON  f.id= pag.funcionario_id INNER JOIN persona pfun ON pfun.id=f.persona_id where  ppro.id=:id ORDER BY pag.id DESC" + 
			" ", nativeQuery = true)
	public List<Object[]> getPagosDetalleProveedorCompras(@Param("id") int id);

}
