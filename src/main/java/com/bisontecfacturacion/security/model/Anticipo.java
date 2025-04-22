package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Anticipo {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Funcionario funcionarioRegistro;
	@ManyToOne
	private Funcionario funcionarioEncargado;
	@ManyToOne
	private Funcionario funcionarioAutorizado;
	
	@ManyToOne
	private TipoOperacion tipoOperacion;
	@ManyToOne
	private Concepto concepto;
	@NotNull
	private double monto;
	private boolean estado;
	private String tipo;
	private String disponibilidad;

	
	public Anticipo() {
		this.id=0;
		this.fecha= new Date();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioEncargado= new Funcionario();
		this.funcionarioAutorizado= new Funcionario();
		this.tipoOperacion = new TipoOperacion();
		this.concepto= new Concepto();
		this.monto=0.0;
		this.estado=false;
		this.tipo="";
		this.disponibilidad="";
	}

	
	public String getDisponibilidad() {
		return disponibilidad;
	}


	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public Concepto getConcepto() {
		return concepto;
	}


	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}


	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Funcionario getFuncionarioEncargado() {
		return funcionarioEncargado;
	}

	public void setFuncionarioEncargado(Funcionario funcionarioEncargado) {
		this.funcionarioEncargado = funcionarioEncargado;
	}

	public Funcionario getFuncionarioAutorizado() {
		return funcionarioAutorizado;
	}

	public void setFuncionarioAutorizado(Funcionario funcionarioAutorizado) {
		this.funcionarioAutorizado = funcionarioAutorizado;
	}

	

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	

}
