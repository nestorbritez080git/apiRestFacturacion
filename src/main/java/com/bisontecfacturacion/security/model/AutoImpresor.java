package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class AutoImpresor {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@NotNull
	private String timbrado;
	@NotNull
	private String numeroAutorizacion;
	@NotNull
	private String codigoEstablecimiento;
	@NotNull
	private String puntoExpedicion;
	@NotNull
	private int cantidadExpedicion;
	@NotNull
	private int rangoInicio;
	@NotNull
	private int rangoFin;
	@NotNull
	private int numeroActual;
	@NotNull
	private int numeroTerminal;
	@NotNull
	private boolean estado;
	@ManyToOne
	private Funcionario funcionario;
	public AutoImpresor() {
		this.funcionario= new Funcionario();
		this.fecha= LocalDateTime.now();
		this.id=0;
		this.estado=false;
	}
	
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public String getTimbrado() {
		return timbrado;
	}
	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}
	public String getPuntoExpedicion() {
		return puntoExpedicion;
	}
	public void setPuntoExpedicion(String puntoExpedicion) {
		this.puntoExpedicion = puntoExpedicion;
	}
	public int getCantidadExpedicion() {
		return cantidadExpedicion;
	}
	public void setCantidadExpedicion(int cantidadExpedicion) {
		this.cantidadExpedicion = cantidadExpedicion;
	}
	public int getRangoInicio() {
		return rangoInicio;
	}
	public void setRangoInicio(int rangoInicio) {
		this.rangoInicio = rangoInicio;
	}
	public int getRangoFin() {
		return rangoFin;
	}
	public void setRangoFin(int rangoFin) {
		this.rangoFin = rangoFin;
	}
	public int getNumeroActual() {
		return numeroActual;
	}
	public void setNumeroActual(int numeroActual) {
		this.numeroActual = numeroActual;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public int getNumeroTerminal() {
		return numeroTerminal;
	}
	public void setNumeroTerminal(int numeroTerminal) {
		this.numeroTerminal = numeroTerminal;
	}
	
}
