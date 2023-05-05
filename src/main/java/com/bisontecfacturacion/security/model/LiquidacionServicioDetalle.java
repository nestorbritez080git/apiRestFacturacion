package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class LiquidacionServicioDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private Double monto;
	@NotNull
	private Double comision;
	@NotNull
	private int porcentaje;
	@ManyToOne
	private LiquidacionServicio liquidacionServicio;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private DetalleServicios detalleServicios;
	public LiquidacionServicioDetalle() {
		this.id=0;
		this.monto=0.0;
		this.comision=0.0;
		this.porcentaje=0;
		this.liquidacionServicio=new LiquidacionServicio();
		this.detalleServicios= new DetalleServicios();
		this.cliente= new Cliente();
		
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	public Double getComision() {
		return comision;
	}
	public void setComision(Double comision) {
		this.comision = comision;
	}
	public int getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}
	public LiquidacionServicio getLiquidacionServicio() {
		return liquidacionServicio;
	}
	public void setLiquidacionServicio(LiquidacionServicio liquidacionServicio) {
		this.liquidacionServicio = liquidacionServicio;
	}
	public DetalleServicios getDetalleServicios() {
		return detalleServicios;
	}
	public void setDetalleServicios(DetalleServicios detalleServicio) {
		this.detalleServicios = detalleServicio;
	}
	
}
