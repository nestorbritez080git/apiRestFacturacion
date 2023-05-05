package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Producto;

@Transactional(readOnly=true)
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Serializable>, PagingAndSortingRepository<Producto, Serializable>{
	
	public abstract Page<Producto> findAll(Pageable pageable); 
	
	public abstract Producto findByDescripcion(String descripcion);
	public abstract Producto findByCodbar(String codBar);
	public abstract Producto findTop1ByOrderByIdDesc();
	
	@Query(value="select * from producto order by id desc limit 30", nativeQuery = true)
	List<Producto> lista();
	
	@Query(value="select * from producto where estado_compuesto=true order by id desc limit 30", nativeQuery = true)
	List<Producto> listarInventario();
	
	@Query(value="select * from producto order by existencia asc limit 30", nativeQuery = true)
	List<Producto> listaAjusteStock();

	@Query(value="select * from producto  where existencia > 0 order by id desc limit 50", nativeQuery = true)
	List<Producto> listaStockBajo();
	
	@Query(value="select * from producto p inner join marca on marca.id=p.marca_id inner join grupo on grupo.id=p.grupo_id inner join sub_grupo on sub_grupo.id=p.sub_grupo_id inner join unidad_medida on unidad_medida.id=p.unidad_medida_id inner join deposito on deposito.id=p.deposito_id where p.descripcion like :descripcion or p.codbar like :descripcion or p.codoriginal like :descripcion or fabricante like :descripcion or aplicacion like :descripcion or marca.descripcion like :descripcion  or grupo.descripcion like :descripcion or cast(p.id AS VARCHAR)   like :descripcion   order by p.id desc  limit 30",nativeQuery=true)
	List<Producto>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
	
	@Query(value="select * from producto p inner join marca on marca.id=p.marca_id inner join grupo on grupo.id=p.grupo_id inner join sub_grupo on sub_grupo.id=p.sub_grupo_id inner join unidad_medida on unidad_medida.id=p.unidad_medida_id inner join deposito on deposito.id=p.deposito_id where  p.estado_compuesto=true and p.descripcion like :descripcion or p.codbar like :descripcion or p.codoriginal like :descripcion or fabricante like :descripcion or aplicacion like :descripcion or marca.descripcion like :descripcion  or grupo.descripcion like :descripcion OR cast(p.id AS VARCHAR) like :descripcion order by p.id desc limit 30",nativeQuery=true)
	List<Producto>  getBuscarPorDescripcionListadoInventario(@Param("descripcion") String descripcion);
	
	@Query(value="select * from producto p inner join marca on marca.id=p.marca_id inner join grupo on grupo.id=p.grupo_id inner join sub_grupo on sub_grupo.id=p.sub_grupo_id inner join unidad_medida on unidad_medida.id=p.unidad_medida_id inner join deposito on deposito.id=p.deposito_id where p.descripcion like :descripcion or p.codbar like :descripcion or p.codoriginal like :descripcion or fabricante like :descripcion or aplicacion like :descripcion or marca.descripcion like :descripcion  or grupo.descripcion like :descripcion OR cast(p.id AS VARCHAR) like :descripcion order by p.existencia asc limit 30",nativeQuery=true)
	List<Producto>  getBuscarPorDescripcionAjusteStock(@Param("descripcion") String descripcion);
	
	@Query(value="select * from producto p inner join marca on marca.id=p.marca_id inner join grupo on grupo.id=p.grupo_id inner join sub_grupo on sub_grupo.id=p.sub_grupo_id inner join unidad_medida on unidad_medida.id=p.unidad_medida_id inner join deposito on deposito.id=p.deposito_id where (existencia > 0 and p.descripcion like :descripcion) or (existencia > 0 and p.codbar like :descripcion) or (existencia > 0 and marca.descripcion like :descripcion) or (existencia > 0 and p.codoriginal like :descripcion) or (existencia > 0 and cast(p.id AS VARCHAR) like :descripcion) order by p.id desc limit 30",nativeQuery=true)
	List<Producto>  getBuscarPorDescripcionStockBajo(@Param("descripcion") String descripcion);
	
	@Modifying
    @Transactional(readOnly=false)
	@Query(value = "UPDATE producto SET proveedor_id=:id WHERE id=:idProducto", nativeQuery = true)
	public void updateProveedorId(@Param("id") int stock,@Param("idProducto") int proid);
	
	 	@Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set existencia=existencia+:stock where id=:proid")
	    public void findByActualizaA(@Param("stock") double stock,@Param("proid") int proid);
	 	
	 	@Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set existencia=:stock where id=:proid")
	    public void findByActualizaCantidadInventario(@Param("stock") double stock,@Param("proid") int proid);
	 	
	 	@Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set existencia=:stock where id=:proid")
	    public void findByActualizaE(@Param("stock") double stock,@Param("proid") int proid);
	 	
		@Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set precio_costo=:costo, precio_venta_1=:precio1,precio_venta_2=:precio2, precio_venta_3=:precio3, precio_venta_4=:precio4 where id=:proid")
	    public void gestionPrecio(@Param("costo") double costo,@Param("precio1") double precio1,@Param("precio2") double precio2,@Param("precio3") double precio3,@Param("precio4") double precio4,@Param("proid") int proid);
	    
	    @Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set existencia=existencia-:stock where id=:proid")
	    public void findByActualizaD(@Param("stock") double stock,@Param("proid") int proid);
	    
	    @Modifying
	    @Transactional(readOnly=false)
	    @Query("update Producto set is_balanza=:balanza where id=:proid")
	    public void findByActualizarBalanza(@Param("balanza") boolean balanza,@Param("proid") int proid);
	
		
	    @Query(value = "select count(p.id) FROM Producto p",nativeQuery = true)
		Object[] findByProducto();
	   
	    @Query(value = "select grupo.descripcion, count(grupo.id) as cantidad from producto inner join grupo on producto.grupo_id=grupo.id group by grupo.descripcion",nativeQuery = true)
		List<Object[]> findByProductoGrupo();
		
		@Query(value = " select count(p.id) from producto p where p.existencia <=  p.stock_minimo",nativeQuery = true)
		Object[] findByStockBajo();

		@Query(value="select * from producto p inner join detalle_producto  det on det.producto_id = p.id where p.id=:idProducto",nativeQuery=true)
		List<Producto>  getVerificarProductoEliminar(@Param("idProducto") int  idProducto);

		@Query(value="select * from producto where proveedor_id=:id order by producto.id limit 50",nativeQuery=true)
		List<Producto> getProductoIdProveedor(@Param("id") int  idProducto);
		
		@Query(value="select * from producto p inner join marca on marca.id=p.marca_id inner join grupo on grupo.id=p.grupo_id inner join sub_grupo on sub_grupo.id=p.sub_grupo_id inner join unidad_medida on unidad_medida.id=p.unidad_medida_id inner join deposito on deposito.id=p.deposito_id where (p.descripcion like :descripcion AND p.proveedor_id=:idProveedor) or (p.codbar like :descripcion AND p.proveedor_id=:idProveedor) or (p.codoriginal like :descripcion AND p.proveedor_id=:idProveedor) or (fabricante like :descripcion AND p.proveedor_id=:idProveedor) or (aplicacion like :descripcion AND p.proveedor_id=:idProveedor) or (marca.descripcion like :descripcion AND p.proveedor_id=:idProveedor)  or (grupo.descripcion like :descripcion AND p.proveedor_id=:idProveedor) order by p.id desc limit 50",nativeQuery=true)
		List<Producto>  getBuscarPorDescripcionIdProveedor(@Param("descripcion") String descripcion, @Param("idProveedor") int idProveedor);
	
	
		
		@Query(value = "SELECT p. descripcion as descr, p.existencia as existencia, p.precio_costo as costo, p.precio_venta_1 as venta1, ud.descripcion as unidadMedida FROM producto p inner join unidad_medida ud on ud.id=p.unidad_medida_id",nativeQuery = true)
		List<Object[]> getInventarioGeneralTodo();
		
		@Query(value = "SELECT p. descripcion as descr, p.existencia as existencia, p.precio_costo as costo, p.precio_venta_1 as venta1, ud.descripcion as unidadMedida  FROM producto p inner join unidad_medida ud on ud.id=p.unidad_medida_id where p.marca_id=:proid",nativeQuery = true)
		List<Object[]> getInventarioGeneralPorMarca(@Param("proid") int proid);
		
		@Query(value = "SELECT p. descripcion as descr, p.existencia as existencia, p.precio_costo as costo, p.precio_venta_1 as venta1, ud.descripcion as unidadMedida  FROM producto p inner join unidad_medida ud on ud.id=p.unidad_medida_id where p.grupo_id=:proid",nativeQuery = true)
		List<Object[]> getInventarioGeneralPorGrupo(@Param("proid") int proid);
		
		@Query(value = "SELECT p. descripcion as descr, p.existencia as existencia, p.precio_costo as costo, p.precio_venta_1 as venta1, ud.descripcion as unidadMedida  FROM producto p inner join unidad_medida ud on ud.id=p.unidad_medida_id where p.proveedor_id=:proid",nativeQuery = true)
		List<Object[]> getInventarioGeneralPorProveedor(@Param("proid") int proid);
		
		@Query(value="SELECT p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, m.descripcion, g.descripcion, s.descripcion FROM producto p INNER JOIN marca m ON p.marca_id=m.id INNER JOIN grupo g ON p.grupo_id=g.id INNER JOIN sub_grupo s ON p.sub_grupo_id = s.id", nativeQuery = true)
		List<Object[]> getInventarioAll();
		
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4,( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoAll();
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true and p.marca_id =:id order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoMarca(@Param("id") int id);
		
		
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true and p.grupo_id = :id order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoGrupo(@Param("id") int id);
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true and p.sub_grupo_id = :id order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoSubGrupo(@Param("id") int id);
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true and  p.marca_id = :id1 AND p.grupo_id = :id2 order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoMarcaGrupo(@Param("id1") int Marca, @Param("id2") int Grupo);
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.estado_compuesto=true and  p.grupo_id = :id1 AND p.sub_grupo_id = :id2 order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoGrupoSubGrupo(@Param("id1") int idGrupo, @Param("id2") int idSubGrupo);
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, codbar from producto p where p.estado_compuesto=true and  p.marca_id = :id1 AND p.grupo_id = :id2 AND p.sub_grupo_id = :id3 order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoMarcaGrupoSubGrupo(@Param("id1") int idMarca, @Param("id2") int idGrupo, @Param("id3") int idSubGrupo);
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, codbar from producto p where p.estado_compuesto=true and p.marca_id = :id1 AND p.sub_grupo_id = :id2 order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoMarcaSubGrupo(@Param("id1") int idMarca, @Param("id2") int idSubGrupo);
		
		
		
		
		
		@Query(value="select p.descripcion, p.existencia, p.precio_costo, p.precio_venta_1, p.precio_venta_2, p.precio_venta_3, p.precio_venta_4, ( p.existencia * p.precio_costo) as totalCosto, ( p.existencia * p.precio_venta_1) as totalPrecioVenta1, ( p.existencia * p.precio_venta_2) as totalPrecioVenta2, ( p.existencia * p.precio_venta_3) as totalPrecioVenta3, ( p.existencia * p.precio_venta_4) as totalPrecioVenta4, p.codbar as codbar, p.id as idd from producto p where p.existencia < 0 order by p.descripcion ASC", nativeQuery = true)
		List<Object[]> getProductoStockNegativo();
}
