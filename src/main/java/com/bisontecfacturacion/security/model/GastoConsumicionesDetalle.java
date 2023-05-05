package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class GastoConsumicionesDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	private Double monto;
	@ManyToOne
	private GastoConsumicionesCabecera gastoConsumicionesCabecera;
	@ManyToOne
	private Consumiciones consumiciones;
	private String observacion;
	
	private String comprobante;
	
	public GastoConsumicionesDetalle() {
		this.consumiciones= new Consumiciones();
		this.gastoConsumicionesCabecera= new GastoConsumicionesCabecera();
		this.descripcion="";
		this.monto=0.0;
		this.observacion="";
		this.comprobante="";
		
	}
	
	public String getComprobante() {
		return comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public GastoConsumicionesCabecera getGastoConsumicionesCabecera() {
		return gastoConsumicionesCabecera;
	}
	public void setGastoConsumicionesCabecera(GastoConsumicionesCabecera gastoConsumicionesCabecera) {
		this.gastoConsumicionesCabecera = gastoConsumicionesCabecera;
	}
	public Consumiciones getConsumiciones() {
		return consumiciones;
	}
	public void setConsumiciones(Consumiciones consumiciones) {
		this.consumiciones = consumiciones;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
}
