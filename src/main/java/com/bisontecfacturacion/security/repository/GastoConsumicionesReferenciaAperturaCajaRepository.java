package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.GastoConsumicionesCabecera;
import com.bisontecfacturacion.security.model.GastoConsumicionesReferenciaCajaChica;
import com.bisontecfacturacion.security.model.GastoConsumicionesReferenciaOperacionCaja;
import com.bisontecfacturacion.security.model.Venta;
@Transactional(readOnly=true)
@Repository
public interface GastoConsumicionesReferenciaAperturaCajaRepository extends JpaRepository<GastoConsumicionesReferenciaOperacionCaja, Serializable> {
		
}
