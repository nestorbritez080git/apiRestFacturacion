package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.TipoPlazo;


public interface TipoPlazoRepository extends JpaRepository<TipoPlazo, Serializable> {
	public abstract List<TipoPlazo> findByOrderByIdAsc();
}
