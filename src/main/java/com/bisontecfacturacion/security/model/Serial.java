package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Serial {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String identificador;
	@Column(length = 40)
	private String ultRegistro;
	private String validacion;
	@NotNull
	private String codigoCliente;
	@OneToMany(mappedBy="serial")
	@JsonBackReference
	private List<SerialDetalle> serialDetalle;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public List<SerialDetalle> getSerialDetalle() {
		return serialDetalle;
	}

	public void setSerialDetalle(List<SerialDetalle> serialDetalle) {
		this.serialDetalle = serialDetalle;
	}

	public String getUltRegistro() {
		return ultRegistro;
	}

	public void setUltRegistro(String ultRegistro) {
		this.ultRegistro = ultRegistro;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getValidacion() {
		return validacion;
	}

	public void setValidacion(String validacion) {
		this.validacion = validacion;
	} 
	
	
	
	
	
}
