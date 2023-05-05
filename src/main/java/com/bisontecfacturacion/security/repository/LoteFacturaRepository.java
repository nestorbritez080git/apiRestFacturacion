package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.LoteFactura;

@Repository
public interface LoteFacturaRepository extends JpaRepository<LoteFactura, Serializable> {
	public abstract List<LoteFactura>findByOrderByIdDesc();
	
	public abstract LoteFactura findTop1ByOrderByIdAsc();
	
	@Query("select l from LoteFactura l where l.estado=:estado and l.cantidadActual < l.cantidadExpedicion ")
	LoteFactura getEstadoLoteFactura(@Param("estado") boolean estado);
	@Query("select l from LoteFactura l where l.estado=:estado")
	LoteFactura getEstadoLoteFacturaEstadoInactivo(@Param("estado") boolean estado);
	@Modifying
    @Transactional(readOnly=false)
    @Query("update LoteFactura set serieActual=:serieActual where id=:id")
    public void actualizarSeriaActual(@Param("serieActual") String seriaActual,@Param("id") int id);
	@Query("update LoteFactura set estado=:estado where id=:id")
	public void actualizarEstadoLoteFactura(@Param("estado") Boolean estado,@Param("id") int id);
}
