package com.bisontecfacturacion.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Service
public class MCValidaciones {
	@Autowired
	private AperturaCajaRepository aperturaRepository;
	private ResponseEntity<CustomerErrorType> error(String mensaje) {
		return new ResponseEntity<>(new CustomerErrorType(mensaje), HttpStatus.CONFLICT);
	}
	public ResponseEntity<?> validarCajaSalida(List<OperacionCaja> lista) {

	    if (lista == null || lista.isEmpty()) {
	        return error("No existen formas de pago para validar caja");
	    }

	    // 🔹 Obtener apertura una sola vez (asumo misma caja)
	    AperturaCaja aper = aperturaRepository
	            .getAperturaCajaPorIdCaja(lista.get(0).getAperturaCaja().getId());

	    if (aper == null) {
	        return error("EL FUNCIONARIO NO POSEE UNA APERTURA DE CAJA");
	    }

	    double totalEfectivo = 0;
	    double totalCheque = 0;
	    double totalTarjeta = 0;

	    for (OperacionCaja op : lista) {

	        if (op.getMonto() <= 0) {
	            return error("EL MONTO DE LA OPERACIÓN DEBE SER MAYOR A CERO");
	        }

	        // 🔹 Solo validar SALIDAS (ajustá según tu lógica)

	            switch (op.getTipoOperacion().getId()) {
	                case 1:
	                    totalEfectivo += op.getMonto();
	                    break;
	                case 2:
	                    totalCheque += op.getMonto();
	                    break;
	                case 3:
	                    totalTarjeta += op.getMonto();
	                    break;
	            }
	        
	    }

	    // 🔴 VALIDACIÓN FINAL (clave)
	    if (totalEfectivo > aper.getSaldoActual()) {
	        return error("NO HAY SALDO SUFICIENTE EN EFECTIVO");
	    }

	    if (totalCheque > aper.getSaldoActualCheque()) {
	        return error("NO HAY SALDO SUFICIENTE EN CHEQUE");
	    }

	    if (totalTarjeta > aper.getSaldoActualTarjeta()) {
	        return error("NO HAY SALDO SUFICIENTE EN TARJETA");
	    }

	    return null;
	} 
	public ResponseEntity<?> validarCajaEntrada(List<OperacionCaja> operacionCajaLista) {
	      System.out.println("entro validacion de cajas ");
		    if (operacionCajaLista == null || operacionCajaLista.isEmpty()) {
		        return error("No existen formas de pago para validar caja");
		    }
		    for (OperacionCaja op : operacionCajaLista) {
		    	System.out.println("apertura : " +  op.getAperturaCaja().getId());
		        if (op.getAperturaCaja() == null ||
		            op.getAperturaCaja().getId() <= 0) {
		            return error("El funcionario no posee una apertura de caja asignada");
		        }

		        AperturaCaja aper = aperturaRepository.getAperturaCajaPorIdCaja(op.getAperturaCaja().getId()
		                );

		        if (aper == null) {
		            return error("EL FUNCIONARIO NO POSEE UNA APERTURA CAJA A SU NOMBRE");
		        }
		    }

		    return null;
		}
	
	

}
