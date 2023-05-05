package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.Moneda;

public interface MonedaRepsitory extends JpaRepository<Moneda, Serializable>{
	public abstract List<Moneda> findByOrderByIdAsc(); 
}
