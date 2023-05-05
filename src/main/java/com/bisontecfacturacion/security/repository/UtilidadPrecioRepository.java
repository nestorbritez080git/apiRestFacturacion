package com.bisontecfacturacion.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.UtilidadPrecio;

@Repository
public interface UtilidadPrecioRepository extends JpaRepository<UtilidadPrecio, Integer> {
    
}
