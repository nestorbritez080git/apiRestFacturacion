package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CuentaCobrarMora {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaGeneracion;
	@ManyToOne
	@JsonIgnore
	private CuentaCobrarDetalle cuentaCobrarDetalle;
	private Integer diaAtraso;
	private Double porcentajeMora;
	private Double montoMora;
	private Double montoBase;
	public CuentaCobrarMora() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.fechaGeneracion=new Date();
		this.cuentaCobrarDetalle = new  CuentaCobrarDetalle();
		this.diaAtraso=0;
		this.porcentajeMora=0.0;
		this.montoBase=0.0;
		this.montoMora=0.0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public CuentaCobrarDetalle getCuentaCobrarDetalle() {
		return cuentaCobrarDetalle;
	}
	public void setCuentaCobrarDetalle(CuentaCobrarDetalle cuentaCobrarDetalle) {
		this.cuentaCobrarDetalle = cuentaCobrarDetalle;
	}
	public Integer getDiaAtraso() {
		return diaAtraso;
	}
	public void setDiaAtraso(Integer diaAtraso) {
		this.diaAtraso = diaAtraso;
	}
	
	public Double getPorcentajeMora() {
		return porcentajeMora;
	}
	public void setPorcentajeMora(Double porcentajeMora) {
		this.porcentajeMora = porcentajeMora;
	}
	public Double getMontoMora() {
		return montoMora;
	}
	public void setMontoMora(Double montoMora) {
		this.montoMora = montoMora;
	}
	public Double getMontoBase() {
		return montoBase;
	}
	public void setMontoBase(Double montoBase) {
		this.montoBase = montoBase;
	}
	

	
}
