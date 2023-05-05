package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.Serial;
@Transactional(readOnly=true)
@Repository
public interface SerialRepository  extends JpaRepository<Serial, Serializable>{

	@Query(value="SELECT * FROM serial s where extract(month from cast(s.fecha_inicio as Date))=:mes AND extract(year from cast(s.fecha_inicio as Date))=:anho limit 1",nativeQuery=true)
	Serial getSerieFechaActual(@Param("anho") int anho, @Param("mes") int mes);

	@Modifying
    @Transactional(readOnly=false)
    @Query("update Serial set cantidadDia=:cantidadDia where id=:id")
    public void findByActualizaE(@Param("cantidadDia") int cantidadDia,@Param("id") int id);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Serial set codigoVeri=:cod where id=:id")
    public void cargarCodigoVerificador(@Param("cod") String cod,@Param("id") int id);
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update Serial set ultRegistro=:fecha where id=:id")
    public void actualizarUltRegistro(@Param("fecha") String ultRegistro,@Param("id") int id);
	
	@Modifying
    @Transactional(readOnly=false)
	@Query(value = "UPDATE serial_detalle SET clave=:clave WHERE periodo =:periodo", nativeQuery = true)
	public void updateClave(@Param("clave") String clave, @Param("periodo") String periodo);
}
