package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Org {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @NotNull
    @Column(length=45)
    private String nombre;
    @Lob
	private  byte[] foto;
    @NotNull
    @Column(length=45, unique=true)
	private String ruc;
    @NotNull
    @Column(length=45)
	private String telefono;
    @NotNull
    @Column(length=100)
	private String direccion;
    @NotNull
	private Date alta;
    private String ciudad;
    private String pais;
	
	

	public Org() {
		super();
		id=0;
		nombre="";
		ruc="";
		telefono="";
		direccion="";
		alta=new Date();
		ciudad="";
		pais="";
	}



	public String getCiudad() {
		return ciudad;
	}



	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}



	public String getPais() {
		return pais;
	}



	public void setPais(String pais) {
		this.pais = pais;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public byte[] getFoto() {
		return foto;
	}



	public void setFoto(byte[] foto) {
		this.foto = foto;
	}



	public String getRuc() {
		return ruc;
	}



	public void setRuc(String ruc) {
		this.ruc = ruc;
	}



	public String getTelefono() {
		return telefono;
	}



	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}



	public String getDireccion() {
		return direccion;
	}



	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}



	public Date getAlta() {
		return alta;
	}



	public void setAlta(Date alta) {
		this.alta = alta;
	}


	
	

}