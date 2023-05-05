package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.UbicacionPermiso;

public  interface UbicacionPermisoRepository extends JpaRepository<UbicacionPermiso, Serializable>{
    public abstract List<UbicacionPermiso> findByOrderByPosicionAsc();



}