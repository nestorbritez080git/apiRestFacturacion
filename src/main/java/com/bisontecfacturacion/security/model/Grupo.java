package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Grupo {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	
	@OneToMany(mappedBy="grupo")
	@JsonBackReference
	private List<Producto> productos;

	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	} 
	

}
