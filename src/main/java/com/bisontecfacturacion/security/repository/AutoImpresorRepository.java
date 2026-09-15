package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.AutoImpresor;

public interface AutoImpresorRepository extends JpaRepository<AutoImpresor, Serializable> {
	public abstract List<AutoImpresor> findByOrderByIdDesc();
	@Query("SELECT a FROM AutoImpresor a ORDER BY a.id DESC")
	public List<AutoImpresor> getListaAutoImpresor();
	 
	 
	 
	 
	 @Query("SELECT DISTINCT a FROM AutoImpresor a " +
		       "LEFT JOIN FETCH a.autoImpresorDetalleVentas d " +
		       "LEFT JOIN FETCH d.venta v " +
		       "LEFT JOIN FETCH v.cliente c " +
		       "LEFT JOIN FETCH c.persona p " +
		       "WHERE a.id = :idAuto " +
		       "AND v.fecha BETWEEN :fechaInicio AND :fechaFin " +
		       "ORDER BY a.id DESC")
	List<AutoImpresor> consultarRemisionesFacturaRangoFecha(
		        @Param("idAuto") int idAuto,
		        @Param("fechaInicio") Date fechaInicio,
		        @Param("fechaFin") Date fechaFin);	 
	 
	 
	 
	 @Query("SELECT c FROM AutoImpresor c JOIN FETCH c.autoImpresorTipoRemision tipo WHERE c.id= :id")
	 public AutoImpresor getAutoImpresorPorId(@Param("id") int id);
	
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query("update AutoImpresor set numeroActual=:numeroActual where id=:id")
	 public void actualizarNumeroActualAutoImpresor(@Param("numeroActual") Integer numeroActual, @Param("id") int id);
	 
	@Query(value = "select * from auto_impresor f where numero_autorizacion=:autorizacion", nativeQuery = true )
	public AutoImpresor consultarAutoImpresorTerminales(@Param("autorizacion") int autorizacion);
}
