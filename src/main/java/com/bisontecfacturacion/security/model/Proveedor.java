package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Proveedor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String numeroCuenta;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "persona_id")
	private Persona persona;
	

	@OneToMany(mappedBy="proveedor")
	@JsonBackReference
	private List<Producto> producto; 
	
	public Proveedor() {
		super();
		id=0;
		numeroCuenta="";
		persona=new Persona();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public List<Producto> getProducto() {
		return producto;
	}
	public void setProducto(List<Producto> producto) {
		this.producto = producto;
	}
	
	

}
