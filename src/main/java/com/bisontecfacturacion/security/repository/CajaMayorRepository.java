package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.CajaMayor;

@Repository
public interface CajaMayorRepository extends JpaRepository<CajaMayor, Serializable> {

	@Modifying
	@Transactional(readOnly=false)
	@Query("update CajaMayor set monto=monto+:mon, montoCheque=montoCheque+:montoChe, montoTarjeta=montoTarjeta+:montoTarj where id=:id")
	public void findByActualizaCajaMayor(@Param("id") int id, @Param("mon") Double mon, @Param("montoChe") Double montoChe, @Param("montoTarj") Double montoTarj);
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CajaMayor set monto=monto-:mon, montoCheque=montoCheque-:montoChe, montoTarjeta=montoTarjeta-:montoTarj where id=:id")
	public void findByActualizaCajaMayorNegativo(@Param("id") int id, @Param("mon") Double mon, @Param("montoChe") Double montoChe, @Param("montoTarj") Double montoTarj);

	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CajaChica set monto=monto+:mon, montoCheque=montoCheque+:montoChe, montoTarjeta=montoTarjeta+:montoTarj where id=:id")
	public void findByActualizaTransferenciaCajaChicaPositivo(@Param("id") int id, @Param("mon") Double mon, @Param("montoChe") Double montoChe, @Param("montoTarj") Double montoTarj);
	@Modifying
	@Transactional(readOnly=false)
	@Query("update CajaChica set monto=monto-:mon, montoCheque=montoCheque-:montoChe, montoTarjeta=montoTarjeta-:montoTarj where id=:id")
	public void findByActualizaTransferenciaCajaChicaNegativo(@Param("id") int id, @Param("mon") Double mon, @Param("montoChe") Double montoChe, @Param("montoTarj") Double montoTarj);

	
	@Query("select f from CajaMayor f where id=:id")
	public CajaMayor consultarIDCajaMayor(@Param("id") int id);

}
