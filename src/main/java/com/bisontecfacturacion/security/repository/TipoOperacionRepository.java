package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.Moneda;
import com.bisontecfacturacion.security.model.TipoOperacion;

public interface TipoOperacionRepository extends JpaRepository<TipoOperacion, Serializable> {
	public abstract List<TipoOperacion> findByOrderByIdAsc(); 

}
