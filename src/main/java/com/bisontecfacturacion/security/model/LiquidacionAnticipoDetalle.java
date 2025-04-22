package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class LiquidacionAnticipoDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private Double monto;
	@ManyToOne
	private LiquidacionAnticipo liquidacionAnticipo;
	@ManyToOne
	private Anticipo anticipo;
	public LiquidacionAnticipoDetalle() {
		this.id=0;
		this.monto=0.0;
		this.liquidacionAnticipo= new LiquidacionAnticipo();
		this.anticipo= new Anticipo();
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public LiquidacionAnticipo getLiquidacionAnticipo() {
		return liquidacionAnticipo;
	}
	public void setLiquidacionAnticipo(LiquidacionAnticipo liquidacionAnticipo) {
		this.liquidacionAnticipo = liquidacionAnticipo;
	}
	public Anticipo getAnticipo() {
		return anticipo;
	}
	public void setAnticipo(Anticipo anticipo) {
		this.anticipo = anticipo;
	}	
}
