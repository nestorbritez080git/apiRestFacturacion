package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.AjusteInventario;
import com.bisontecfacturacion.security.model.DevolucionVenta;
@Repository
public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Serializable>{
	
	@Query(value="select v.id from ajuste_inventario v order by v.id desc limit 1", nativeQuery = true)
	int getUltimaDevolucion();
	@Query(value="select * from ajuste_inventario v order by v.id desc limit 1", nativeQuery = true)
	AjusteInventario getAjusteUlt();
	
	@Query(value="select ajuste_inventario.id,persona.nombre,persona.apellido,producto.descripcion,ajuste_inventario.cantidad,ajuste_inventario.tipo,ajuste_inventario.fecha,ajuste_inventario.motivo from ajuste_inventario inner join producto on ajuste_inventario.producto_id=producto.id inner join funcionario on ajuste_inventario.funcionario_id=funcionario.id inner join persona on funcionario.persona_id=persona.id where extract(year from cast(ajuste_inventario.fecha as Date))=:ano AND extract(month from cast(ajuste_inventario.fecha as Date))=:mes AND extract(day from cast(ajuste_inventario.fecha as Date))=:dia",nativeQuery=true)
	List<Object[]> listaAjusteInventario(@Param("dia") int dia, @Param("mes") int mes, @Param("ano") int ano);
	
}

