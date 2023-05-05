package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Impresora {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	private boolean estado;
	public int getId() {
		return id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
}