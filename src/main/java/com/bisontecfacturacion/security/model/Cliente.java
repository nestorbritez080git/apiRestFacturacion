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
public class Cliente {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private double limiteCredito;
	private int diaLimite;
	private boolean estadoBloqueo;
	
	@ManyToOne
    private Persona persona;
	
	@OneToMany(mappedBy="cliente")
	@JsonBackReference
	private List<Venta> venta; 
	
	public Cliente() {
		super();
		id=0;
		limiteCredito=0.0;
		diaLimite=0;
		persona=new Persona();
		estadoBloqueo=false;
	}
	
	public boolean isEstadoBloqueo() {
		return estadoBloqueo;
	}

	public void setEstadoBloqueo(boolean estadoBloqueo) {
		this.estadoBloqueo = estadoBloqueo;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public int getDiaLimite() {
		return diaLimite;
	}

	public void setDiaLimite(int diaLimite) {
		this.diaLimite = diaLimite;
	}



	public Persona getPersona() {
		return persona;
	}



	public void setPersona(Persona persona) {
		this.persona = persona;
	}



	public List<Venta> getVenta() {
		return venta;
	}



	public void setVenta(List<Venta> venta) {
		this.venta = venta;
	}


	
	

}
