package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bisontecfacturacion.security.model.OrdenPagare;
import com.bisontecfacturacion.security.model.Pedido;
import com.bisontecfacturacion.security.model.Venta;

@Repository
public interface OrdenPagareRepository extends JpaRepository<OrdenPagare, Serializable>{
    public abstract OrdenPagare findTop1ByOrderByIdDesc();
    @Query(value="SELECT c FROM OrdenPagare c INNER JOIN c.cuentaCobrarCabecera cue INNER JOIN c.cliente cli INNER JOIN cli.persona pCli INNER JOIN c.funcionario fun INNER JOIN fun.persona pFun WHERE cue.id=:idCuenta")
   	OrdenPagare getOrdenPorCuentaId(@Param("idCuenta") int idCuenta);
    
//    select ccc from Venta ccc where ccc.id=:id
    @Query(value="SELECT c FROM OrdenPagare c INNER JOIN c.cuentaCobrarCabecera cue INNER JOIN c.cliente cli INNER JOIN cli.persona pCli INNER JOIN c.funcionario fun INNER JOIN fun.persona pFun WHERE c.id=:id")
   	OrdenPagare getOrdenPagarePorId(@Param("id") int id);
    
    @Modifying
    @Query("DELETE FROM OrdenPagare o WHERE o.cuentaCobrarCabecera.id = :cabeceraId")
    void eliminarPorCuentaCobrarCabeceraId(@Param("cabeceraId") int cabeceraId);
 
	 @Modifying
	 @Transactional(readOnly=false)
	 @Query(value="UPDATE orden_pagare SET estado=:est WHERE id=:id", nativeQuery = true)
	 public void finalizarOrdenPagare( @Param("id") int idPedido, @Param("est") String est);

    
    }  