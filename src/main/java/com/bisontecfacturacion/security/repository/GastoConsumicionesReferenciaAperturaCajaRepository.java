package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.GastoConsumicionesReferenciaOperacionCaja;
@Transactional(readOnly=true)
@Repository
public interface GastoConsumicionesReferenciaAperturaCajaRepository extends JpaRepository<GastoConsumicionesReferenciaOperacionCaja, Serializable> {
		
}
