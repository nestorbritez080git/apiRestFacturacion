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
	private Periodo periodo;
	@ManyToOne
	private TipoOperacion tipoOperacion;
	@NotNull
	private double monto;
	private boolean estado;
	
	public Anticipo() {
		this.id=0;
		this.fecha= new Date();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioEncargado= new Funcionario();
		this.funcionarioAutorizado= new Funcionario();
		this.periodo=new Periodo();
		this.tipoOperacion = new TipoOperacion();
		this.monto=0.0;
		this.estado=false;
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

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}
	

}
