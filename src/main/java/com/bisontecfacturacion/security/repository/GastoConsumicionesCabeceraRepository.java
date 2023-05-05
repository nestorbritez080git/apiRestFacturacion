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
import com.bisontecfacturacion.security.model.Venta;
@Transactional(readOnly=true)
@Repository
public interface GastoConsumicionesCabeceraRepository extends JpaRepository<GastoConsumicionesCabecera, Serializable> {
	@Query(value="select * from gasto_consumiciones_cabecera v inner join funcionario fcargo on fcargo.id=v.funcionario_cargo_id inner join persona pcargo on pcargo.id=fcargo.persona_id inner join funcionario fregistro on fregistro.id=v.funcionario_registro_id inner join persona pregistro on pregistro.id=fregistro.persona_id where extract(year from cast(v.fecha as Date))=:ano AND extract(month from cast(v.fecha as Date))=:mes AND extract(day from cast(v.fecha as Date))=:dia order by v.id desc",nativeQuery=true)
	List<GastoConsumicionesCabecera> getGastoConsumicionDelDia(@Param("ano") int ano, @Param("mes") int mes, @Param("dia") int dia);
	
	@Query(value="select * from gasto_consumiciones_cabecera v order by v.id desc limit 1", nativeQuery = true)
	GastoConsumicionesCabecera getUltimaGastoConsumicionCab();
	
	@Modifying
    @Transactional(readOnly=false)
    @Query("update GastoConsumicionesCabecera set total=total-:monto where id=:id")
    public void findByActualizaMontoCabecera(@Param("monto") double stock,@Param("id") int id);
	
	@Modifying
	@Transactional(readOnly=false)
	@Query("update GastoConsumicionesCabecera set operacionCaja =:operacionCaja where id=:id")
	public void findByActualizarGastoCabeceraOperacionnn(@Param("id") int id, @Param("operacionCaja") int operacionCaja);
	
}
