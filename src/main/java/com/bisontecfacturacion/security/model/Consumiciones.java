package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Consumiciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String descripcion;
	
	@OneToOne
	private CategoriaConsumiciones categoriaConsumiciones;
	
	public Consumiciones() {
		this.id=0;
		this.descripcion="";
		this.categoriaConsumiciones= new CategoriaConsumiciones();
		
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
	public CategoriaConsumiciones getCategoriaConsumiciones() {
		return categoriaConsumiciones;
	}
	public void setCategoriaConsumiciones(CategoriaConsumiciones categoriaConsumiciones) {
		this.categoriaConsumiciones = categoriaConsumiciones;
	}
	
	
	
	
}
