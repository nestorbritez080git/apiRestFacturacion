package com.bisontecfacturacion.security.educacion.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.educacion.model.Carrera;
import com.bisontecfacturacion.security.educacion.model.Matriculacion;
import com.bisontecfacturacion.security.educacion.model.MatriculacionDetalle;
import com.bisontecfacturacion.security.educacion.model.TipoCarrera;
import com.bisontecfacturacion.security.hoteleria.model.CategoriaHabitaciones;
import com.bisontecfacturacion.security.model.CobrosClienteCabecera;
import com.bisontecfacturacion.security.model.Venta;

public interface MatriculacionDetalleRepository extends JpaRepository<MatriculacionDetalle, Serializable> {
//	@Query(value="select * from categoria_habitaciones p where p.descripcion like :descripcion order by p.id desc",nativeQuery=true)
//	List<TipoCarrera>  getBuscarPorDescripcion(@Param("descripcion") String descripcion);
	
	@Query("SELECT c FROM MatriculacionDetalle  c WHERE matriculacion_id=:id order by c.id ASC")
	public abstract List<MatriculacionDetalle> consultarMatriculacionDetallePorIdCabecera(@Param("id") int id);
	
	
	@Query("SELECT c FROM MatriculacionDetalle  c WHERE matriculacion_id=:id order by c.numeroCuota ASC")
	public abstract List<MatriculacionDetalle> consultarMatriculacionDetallePorIdCabeceraOrderNumeroCuota(@Param("id") int id);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update MatriculacionDetalle set importe= importe + :importe where id=:id")
    public void findByActualizaActualizarImporteDetalleMatriculacion(@Param("id") int id, @Param("importe") Double importe);
}
