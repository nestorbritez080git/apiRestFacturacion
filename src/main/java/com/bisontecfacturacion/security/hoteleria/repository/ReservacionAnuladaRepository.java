package com.bisontecfacturacion.security.hoteleria.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.hoteleria.model.ReservacionAnulada;

public interface ReservacionAnuladaRepository extends JpaRepository<ReservacionAnulada, Serializable>{

}
