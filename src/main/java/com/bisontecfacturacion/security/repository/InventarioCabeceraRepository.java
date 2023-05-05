package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.InventarioCabecera;

@Repository
public interface InventarioCabeceraRepository extends JpaRepository<InventarioCabecera, Serializable> {

	public abstract List<InventarioCabecera> findTop50ByOrderByIdDesc();
	public abstract InventarioCabecera findTop1ByOrderByIdDesc();
}
