package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bisontecfacturacion.security.hoteleria.model.Habitaciones;
import com.bisontecfacturacion.security.hoteleria.model.HabitacionesCategoriaCombo;

public interface HabitacionesCategoriaComboRepository extends JpaRepository<HabitacionesCategoriaCombo, Serializable> {
	@Query(value="select c.id as id, c.aplicacion as apl, c.precio_minimo as min, c.precio_normal as normal, h.id as idHab, h.descripcion as desHab, cta.id as idCta, cta.descripcion as desCta, h.estado_disponibilidad as dispo, h.estado_reservacion as reserv from habitaciones_categoria_combo c inner join habitaciones h ON h.id=c.habitaciones_id inner join categoria_habitaciones cta ON cta.id=c.categoria_habitaciones_id order by h.id ASC",nativeQuery=true)
	List<Object[]>  getAll();
	@Query(value="select c.id as id, c.aplicacion as apl, c.precio_minimo as min, c.precio_normal as normal, h.id as idHab, h.descripcion as desHab, cta.id as idCta, cta.descripcion as desCta, h.estado_disponibilidad as dispo, h.estado_reservacion as reserv from habitaciones_categoria_combo c inner join habitaciones h ON h.id=c.habitaciones_id inner join categoria_habitaciones cta ON cta.id=c.categoria_habitaciones_id where h.descripcion ilike :descripcion OR cta.descripcion ilike :descripcion order by  h.id ASC",nativeQuery=true)
	List<Object[]>  getAllDescripcion(@Param("descripcion") String descripcion);
	
	@Query(value="SELECT * FROM habitaciones_categoria_combo where categoria_habitaciones_id= :idCat AND habitaciones_id= :idHab ",nativeQuery=true)
	HabitacionesCategoriaCombo  getHabitacionesComboRegistrado(@Param("idCat") int idCat, @Param("idHab") int idHab);
	
	@Query(value="SELECT * FROM habitaciones_categoria_combo where id=:idHba",nativeQuery=true)
	HabitacionesCategoriaCombo  getComboHabitacionCategoriaPorId(@Param("idHba") int idHba);
	
	
	
	@Query(value="SELECT * FROM habitaciones_categoria_combo where categoria_habitaciones_id= :idCat AND habitaciones_id= :idHab AND id<>:idCombo AND id<>:idCombo",nativeQuery=true)
	HabitacionesCategoriaCombo  getHabitacionesComboRegistradoEditar(@Param("idCat") int idCat, @Param("idHab") int idHab,  @Param("idCombo") int idCombo );
	
	@Query(value="select c.id as id, c.aplicacion as apl, c.precio_minimo as min, c.precio_normal as normal, h.id as idHab, h.descripcion as desHab, cta.id as idCta, cta.descripcion as desCta, h.estado_disponibilidad as dispo, h.estado_reservacion as reserv from habitaciones_categoria_combo c inner join habitaciones h ON h.id=c.habitaciones_id inner join categoria_habitaciones cta ON cta.id=c.categoria_habitaciones_id order by h.id ASC",nativeQuery=true)
	List<Object[]>  getAllComboDisponilidadHabitacion();
	
	@Query(value="select c.id as id, c.aplicacion as apl, c.precio_minimo as min, c.precio_normal as normal, h.id as idHab, h.descripcion as desHab, cta.id as idCta, cta.descripcion as desCta, h.estado_disponibilidad as dispo, h.estado_reservacion as reserv from habitaciones_categoria_combo c inner join habitaciones h ON h.id=c.habitaciones_id inner join categoria_habitaciones cta ON cta.id=c.categoria_habitaciones_id where h.descripcion ilike :descripcion OR cta.descripcion ilike :descripcion order by h.id ASC",nativeQuery=true)
	List<Object[]>  getAllDescripcionDisponilidad(@Param("descripcion") String descripcion);
}
