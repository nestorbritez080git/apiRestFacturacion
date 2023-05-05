package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PermisoUser {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private boolean estado;
	
	@ManyToOne
    private Usuario user;
	
	@ManyToOne
    private Permiso permiso; 
	
	
	public PermisoUser() {
		user=new Usuario();
		permiso=new Permiso();
	}
	
	
	public Permiso getPermiso() {
		return permiso;
	}


	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}


	public Usuario getUser() {
		return user;
	}


	public void setUser(Usuario user) {
		this.user = user;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	

}
