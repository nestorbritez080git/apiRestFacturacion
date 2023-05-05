package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.ProductoCardex;

@Transactional(readOnly=true)
@Repository
public interface ProductoCardexRepository  extends JpaRepository<ProductoCardex, Serializable>{
	
	public abstract ProductoCardex findTop1ByOrderByIdDesc();

	@Query(value="select * from producto_cardex pc  inner join producto pb on pc.producto_base_id=pb.id inner join producto pcom on pc.producto_compuesto_id=pcom.id inner join marca pbmarca on pb.marca_id=pbmarca.id inner join marca pcommarca on pcom.marca_id=pcommarca.id", nativeQuery = true)
	List<ProductoCardex> lista();
	
	@Query(value="select * from producto_cardex pc  inner join producto pb on pc.producto_base_id=pb.id inner join producto pcom on pc.producto_compuesto_id=pcom.id inner join marca pbmarca on pb.marca_id=pbmarca.id inner join marca pcommarca on pcom.marca_id=pcommarca.id where pb.descripcion like :descripcion or pcom.descripcion like :descripcion or pbmarca.descripcion like :descripcion or pcommarca.descripcion like :descripcion",nativeQuery=true)
	List<ProductoCardex>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
	
	@Query(value="select * from producto_cardex p where p.producto_base_id=:id or p.producto_compuesto_id=:id limit 1", nativeQuery = true)
	ProductoCardex consProBase(@Param("id") int id);
	
	@Query(value="select * from producto_cardex p where p.producto_compuesto_id=:id or p.producto_base_id=:id limit 1", nativeQuery = true)
	ProductoCardex consProCompu(@Param("id") int id);
	
	@Query(value="select * from producto_cardex cardex inner join producto pb on cardex.producto_base_id=pb.id where pb.id=:id", nativeQuery = true)
	List<ProductoCardex>  getBase(@Param("id") int id);
	
	@Query(value="select * from producto_cardex p where p.producto_compuesto_id=:id limit 1", nativeQuery = true)
	ProductoCardex getProductoPorIdCompuesto(@Param("id") int id);
	
	@Query(value="select * from producto_cardex p where p.producto_base_id=:id limit 1", nativeQuery = true)
	ProductoCardex getProductoPorIdBase(@Param("id") int id);
	
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update Producto set estadoCompuesto =:estado where id=:proid")
	 public void findByActualizarProductoEstadoCompuesto(@Param("estado") boolean esatado, @Param("proid") int proid);
	
}
