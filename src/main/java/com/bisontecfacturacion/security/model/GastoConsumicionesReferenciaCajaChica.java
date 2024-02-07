package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GastoConsumicionesReferenciaCajaChica {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private GastoConsumicionesCabecera gastoConsumicionesCabecera;
	@ManyToOne
	private CajaChica cajaChica;
		
	public GastoConsumicionesReferenciaCajaChica() {
		this.gastoConsumicionesCabecera= new GastoConsumicionesCabecera();
		this.id=0;
		this.cajaChica= new CajaChica();
		
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

	public CajaChica getCajaChica() {
		return cajaChica;
	}

	public void setCajaChica(CajaChica cajaChica) {
		this.cajaChica = cajaChica;
	}
	
	
	
	
}
