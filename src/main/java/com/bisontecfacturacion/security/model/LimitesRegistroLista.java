package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class LimitesRegistroLista {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	@NotNull
	private int limite;
	public LimitesRegistroLista() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.descripcion="";
		this.limite=0;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public int getId() {
		return id;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLimite() {
		return limite;
	}
	public void setLimite(int limite) {
		this.limite = limite;
	}
	
	
}
