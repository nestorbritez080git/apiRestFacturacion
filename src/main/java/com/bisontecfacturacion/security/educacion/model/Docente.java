package com.bisontecfacturacion.security.educacion.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Persona;

@Entity
public class Docente {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String tipoSangre;
	private String datosClinico;
	@ManyToOne
    private Persona persona;
	public Docente() {
		this.id=0;
		this.tipoSangre="";
		this.datosClinico="";
		this.persona=new Persona();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTipoSangre() {
		return tipoSangre;
	}
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}
	public String getDatosClinico() {
		return datosClinico;
	}
	public void setDatosClinico(String datosClinico) {
		this.datosClinico = datosClinico;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	
}
