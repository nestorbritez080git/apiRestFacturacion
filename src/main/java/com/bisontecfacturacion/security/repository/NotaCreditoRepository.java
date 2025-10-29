package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.model.NotaCredito;

public interface NotaCreditoRepository extends JpaRepository<NotaCredito, Serializable>{
	@Query(value="select * from nota_credito nota inner join cliente cli on cli.id=nota.cliente_id inner join persona pc on pc.id=cli.persona_id inner join funcionario fun on fun.id=nota.funcionario_id inner join persona pf on pf.id=fun.persona_id where nota.estado = 'ABIERTO' order by nota.id desc  limit 50",nativeQuery=true)
	List<NotaCredito>  getAllLimites();
	@Query(value="select * from nota_credito nota inner join cliente cli on cli.id=nota.cliente_id inner join persona pc on pc.id=cli.persona_id inner join funcionario fun on fun.id=nota.funcionario_id inner join persona pf on pf.id=fun.persona_id where (pc.cedula ilike :descripcion or pf.nombre ilike :descripcion or pf.apellido like :descripcion or pc.cedula ilike :descripcion  or pc.nombre ilike :descripcion or pc.apellido ilike :descripcion) AND nota.estado='ABIERTO'  order by nota.id desc  limit 50",nativeQuery=true)
	List<NotaCredito>  getBuscarPorFiltro(@Param("descripcion") String descripcion);
	
	
	@Query("select v from NotaCredito v where v.numeroVenta= :idVenta")
	public NotaCredito getNotaCreditoPorVentaCabeceraId(@Param("idVenta") Integer idVenta);
	
	@Query("select v from NotaCredito v where v.devolucionVenta.id= :idDevol")
	public NotaCredito getNotaCreditoPorDevolucionId(@Param("idDevol") Integer idDevol);
	
	
	@Transactional
	@Modifying
	@Query("update NotaCredito set estado=:est, numero_venta=:idVenta where id=:id")
    public void findByActualizarEstadoNotaCredito(@Param("id") int id, @Param("idVenta") int idVenta, @Param("est") String estado);
}
