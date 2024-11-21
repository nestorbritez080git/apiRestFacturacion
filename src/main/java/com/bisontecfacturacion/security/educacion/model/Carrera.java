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
	private Double costo;
	private String aplicacion;
	@ManyToOne
	private TipoCarrera tipoCarrera;
	public Carrera() {
		this.id=0;
		this.descripcion="";
		this.costo=0.0;
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
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
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
