package com.bisontecfacturacion.security.hoteleria.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Habitaciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	@NotNull
	private boolean estadoDisponibilidad;
	@NotNull
	private boolean estadoReservacion;

	public Habitaciones() {
		this.id=0;
		this.descripcion="";
		this.estadoDisponibilidad=false;
		this.estadoReservacion=false;
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

	

	public boolean isEstadoDisponibilidad() {
		return estadoDisponibilidad;
	}

	public void setEstadoDisponibilidad(boolean estadoDisponibilidad) {
		this.estadoDisponibilidad = estadoDisponibilidad;
	}

	public boolean isEstadoReservacion() {
		return estadoReservacion;
	}

	public void setEstadoReservacion(boolean estadoReservacion) {
		this.estadoReservacion = estadoReservacion;
	}
	
}
