package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class EmpaqueDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonIgnoreProperties("empaqueCabecera")
	@ManyToOne
	private EmpaqueCabecera empaqueCabecera;
	@ManyToOne
	private Presupuesto presupuesto;
	private Double subtotalPresupuesto;
	private int itemsPedidoDetalle;
	private int ventaReferencia;
	public EmpaqueDetalle() {
		this.id=0;
		this.presupuesto= new Presupuesto();
		this.empaqueCabecera = new EmpaqueCabecera();
		this.subtotalPresupuesto =0.0;
		this.itemsPedidoDetalle=0;
		this.ventaReferencia=0;
	}

	

	public int getVentaReferencia() {
		return ventaReferencia;
	}



	public void setVentaReferencia(int ventaReferencia) {
		this.ventaReferencia = ventaReferencia;
	}



	public int getItemsPedidoDetalle() {
		return itemsPedidoDetalle;
	}



	public void setItemsPedidoDetalle(int itemsPedidoDetalle) {
		this.itemsPedidoDetalle = itemsPedidoDetalle;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EmpaqueCabecera getEmpaqueCabecera() {
		return empaqueCabecera;
	}

	public void setEmpaqueCabecera(EmpaqueCabecera empaqueCabecera) {
		this.empaqueCabecera = empaqueCabecera;
	}

	public Presupuesto getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}

	public Double getSubtotalPresupuesto() {
		return subtotalPresupuesto;
	}

	public void setSubtotalPresupuesto(Double subtotalPresupuesto) {
		this.subtotalPresupuesto = subtotalPresupuesto;
	}

	
	
}
