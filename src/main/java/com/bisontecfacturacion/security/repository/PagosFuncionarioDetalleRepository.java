package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.PagosFuncionario;
import com.bisontecfacturacion.security.model.PagosFuncionarioDetalle;

public interface PagosFuncionarioDetalleRepository extends JpaRepository<PagosFuncionarioDetalle, Serializable> {

}
