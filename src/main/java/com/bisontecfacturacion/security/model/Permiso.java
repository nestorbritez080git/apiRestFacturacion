package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Permiso {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@Column(unique = true, length = 100)
	private String descripcion;
	@Column(length = 800)
	private String text;
	@Column(length = 800)
	private String icono;

	@ManyToOne
    private UbicacionPermiso ubicacionPermiso; 
	
	@OneToMany(mappedBy="permiso")
	@JsonBackReference
	private List<PermisoUser> permisoUser;

	public Permiso(){
		this.ubicacionPermiso=new UbicacionPermiso();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public List<PermisoUser> getPermisoUser() {
		return permisoUser;
	}

	public void setPermisoUser(List<PermisoUser> permisoUser) {
		this.permisoUser = permisoUser;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public UbicacionPermiso getUbicacionPermiso() {
		return ubicacionPermiso;
	}

	public void setUbicacionPermiso(UbicacionPermiso ubicacionPermiso) {
		this.ubicacionPermiso = ubicacionPermiso;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	
	

}
