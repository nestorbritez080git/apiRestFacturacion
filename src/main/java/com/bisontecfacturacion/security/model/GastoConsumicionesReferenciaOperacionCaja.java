package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GastoConsumicionesReferenciaOperacionCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private GastoConsumicionesCabecera gastoConsumicionesCabecera;
	@ManyToOne
	private OperacionCaja operacionCaja;

		
	public GastoConsumicionesReferenciaOperacionCaja() {
		this.gastoConsumicionesCabecera= new GastoConsumicionesCabecera();
		this.id=0;
		this.operacionCaja=new OperacionCaja();	
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public GastoConsumicionesCabecera getGastoConsumicionesCabecera() {
		return gastoConsumicionesCabecera;
	}


	public void setGastoConsumicionesCabecera(GastoConsumicionesCabecera gastoConsumicionesCabecera) {
		this.gastoConsumicionesCabecera = gastoConsumicionesCabecera;
	}

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	


	
	
	
}
