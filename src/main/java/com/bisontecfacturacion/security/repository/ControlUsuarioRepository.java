package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.ControlUsuario;

public interface ControlUsuarioRepository extends JpaRepository<ControlUsuario, Serializable> {

	public abstract List<ControlUsuario> findTop50ByOrderByIdDesc();
}
