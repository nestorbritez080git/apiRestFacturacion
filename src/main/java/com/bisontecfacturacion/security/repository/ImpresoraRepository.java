package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bisontecfacturacion.security.model.Impresora;

@Repository
public interface ImpresoraRepository extends JpaRepository<Impresora, Serializable>{
	 public abstract Impresora findTop1ByOrderByIdAsc();
}

