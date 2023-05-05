package com.bisontecfacturacion.security.auxiliar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AddListRouterCampos {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String descripcion;
	private boolean estado;
	@ManyToOne
	private AddListRouter addListRouter;
	public AddListRouterCampos() {
		this.id=0;
		this.descripcion="";
		this.estado=false;
		this.addListRouter= new AddListRouter();
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
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public AddListRouter getAddListRouter() {
		return addListRouter;
	}
	public void setAddListRouter(AddListRouter addListRouter) {
		this.addListRouter = addListRouter;
	}
	

}
