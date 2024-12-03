package com.bisontecfacturacion.security.educacion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.hoteleria.model.Habitaciones;

@Entity
public class Carrera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	private Double costoMatricula;
	private Double costoMensual;
	private Double costoAumentoAnual;
	private Double costoDerechoExamen;
	private int duracion;
	private String aplicacion;
	@ManyToOne
	private TipoCarrera tipoCarrera;
	public Carrera() {
		this.id=0;
		this.descripcion="";
		this.costoMatricula=0.0;
		this.costoMensual=0.0;
		this.costoAumentoAnual=0.0;
		this.costoDerechoExamen=0.0;  
		this.duracion=0;
		this.aplicacion="";
		this.tipoCarrera= new TipoCarrera();
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
	public Double getCostoMatricula() {
		return costoMatricula;
	}
	public void setCostoMatricula(Double costoMatricula) {
		this.costoMatricula = costoMatricula;
	}
	public Double getCostoMensual() {
		return costoMensual;
	}
	public void setCostoMensual(Double costoMensual) {
		this.costoMensual = costoMensual;
	}
	public Double getCostoAumentoAnual() {
		return costoAumentoAnual;
	}
	public void setCostoAumentoAnual(Double costoAumentoAnual) {
		this.costoAumentoAnual = costoAumentoAnual;
	}
	public Double getCostoDerechoExamen() {
		return costoDerechoExamen;
	}
	public void setCostoDerechoExamen(Double costoDerechoExamen) {
		this.costoDerechoExamen = costoDerechoExamen;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public TipoCarrera getTipoCarrera() {
		return tipoCarrera;
	}
	public void setTipoCarrera(TipoCarrera tipoCarrera) {
		this.tipoCarrera = tipoCarrera;
	}
	
}
