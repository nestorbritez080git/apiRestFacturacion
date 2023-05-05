package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class LoteBoleta {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private Long numeroInicial;
	private String numeroActual;
	public LoteBoleta() {
		super();
		fecha=new Date();
	}
	public int getId() {
		return id;
	}
	public Date getFecha() {
		return fecha;
	}
	public Long getNumeroInicial() {
		return numeroInicial;
	}
	public String getNumeroActual() {
		return numeroActual;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setNumeroInicial(Long numeroInicial) {
		this.numeroInicial = numeroInicial;
	}
	public void setNumeroActual(String numeroActual) {
		this.numeroActual = numeroActual;
	}
	
	

	
	
	
	

}
