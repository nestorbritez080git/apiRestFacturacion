package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Funcionario {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private boolean estado;
	private boolean estadoServicio;
	private Double sueldoBruto;
	
	
	public boolean isEstadoServicio() {
		return estadoServicio;
	}

	public void setEstadoServicio(boolean estadoServicio) {
		this.estadoServicio = estadoServicio;
	}

	@ManyToOne
    private Persona persona;
	
	@OneToMany(mappedBy="funcionario")
	@JsonBackReference
	private List<Compra> compra;
	

	public Funcionario(int id) {
		this.id=id;
	}

	public Funcionario() {
		estado=false;
		persona=new Persona();
		this.estadoServicio=false;
		this.estado=false;
		this.sueldoBruto=0.0;
		
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public List<Compra> getCompra() {
		return compra;
	}
	public void setCompra(List<Compra> compra) {
		this.compra = compra;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public Double getSueldoBruto() {
		return sueldoBruto;
	}

	public void setSueldoBruto(Double sueldoBruto) {
		this.sueldoBruto = sueldoBruto;
	}
	
	
	
}
