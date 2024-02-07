package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class CategoriaConsumiciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String descripcion;
	@OneToMany(mappedBy="categoriaConsumiciones")
	@JsonBackReference
	private List<Consumiciones> consumicioness; 
	
	public CategoriaConsumiciones() {
		this.id=0;
		this.descripcion="";
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
	

}
