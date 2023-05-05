package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.FechaSerial;

@Repository
public interface FechaSerialRepository extends JpaRepository<FechaSerial, Serializable> {
	
	public abstract FechaSerial findTop1ByOrderByIdDesc();

}
