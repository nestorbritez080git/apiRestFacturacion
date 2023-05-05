package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class LoteFactura {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String timbrado;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoFin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@Column(unique=true)
	private String serieInicial;
	private String serieActual;
	private int cantidadExpedicion;
	private int cantidadActual;
	private boolean estado;
	
	
	public LoteFactura() {
		estado=true;
		fecha=new Date();
		serieActual="";
	}
	public int getId() {
		return id;
	}
	public String getTimbrado() {
		return timbrado;
	}
	public Date getTimbradoInicio() {
		return timbradoInicio;
	}
	public Date getTimbradoFin() {
		return timbradoFin;
	}
	public Date getFecha() {
		return fecha;
	}
	public String getSerieInicial() {
		return serieInicial;
	}
	public String getSerieActual() {
		return serieActual;
	}
	public int getCantidadExpedicion() {
		return cantidadExpedicion;
	}
	public int getCantidadActual() {
		return cantidadActual;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}
	public void setTimbradoInicio(Date timbradoInicio) {
		this.timbradoInicio = timbradoInicio;
	}
	public void setTimbradoFin(Date timbradoFin) {
		this.timbradoFin = timbradoFin;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public void setSerieInicial(String serieInicial) {
		this.serieInicial = serieInicial;
	}
	public void setSerieActual(String serieActual) {
		this.serieActual = serieActual;
	}
	public void setCantidadExpedicion(int cantidadExpedicion) {
		this.cantidadExpedicion = cantidadExpedicion;
	}
	public void setCantidadActual(int cantidadActual) {
		this.cantidadActual = cantidadActual;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	

}
