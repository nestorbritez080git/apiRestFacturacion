package com.bisontecfacturacion.security.hoteleria.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class HabitacionesCategoriaCombo {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private double precioMinimo;
	@NotNull
	private double precioNormal;
	@NotNull
	private String aplicacion;
	@ManyToOne
	private Habitaciones habitaciones;
	@ManyToOne
	private CategoriaHabitaciones categoriaHabitaciones;
	public HabitacionesCategoriaCombo() {
		this.id=0;
		this.precioMinimo=0.0;
		this.precioNormal=0.0;
		this.aplicacion="";
		this.habitaciones= new Habitaciones();
		this.categoriaHabitaciones= new CategoriaHabitaciones();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrecioMinimo() {
		return precioMinimo;
	}
	public void setPrecioMinimo(double precioMinimo) {
		this.precioMinimo = precioMinimo;
	}
	public double getPrecioNormal() {
		return precioNormal;
	}
	public void setPrecioNormal(double precioNormal) {
		this.precioNormal = precioNormal;
	}
	public String getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public Habitaciones getHabitaciones() {
		return habitaciones;
	}
	public void setHabitaciones(Habitaciones habitaciones) {
		this.habitaciones = habitaciones;
	}
	public CategoriaHabitaciones getCategoriaHabitaciones() {
		return categoriaHabitaciones;
	}
	public void setCategoriaHabitaciones(CategoriaHabitaciones categoriaHabitaciones) {
		this.categoriaHabitaciones = categoriaHabitaciones;
	}


}
