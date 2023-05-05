package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class LiquidacionServicio {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaInicio;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFin;

	@ManyToOne
	private Funcionario funcionarioRegistro;
	@ManyToOne
	private Funcionario funcionarioServicio;
	@NotNull
	private Double totalServicio;
	@NotNull
	private Double totalComision;
	private String resumen;
	private boolean estado;
	@OneToMany(mappedBy="liquidacionServicio")
	private List<LiquidacionServicioDetalle> liquidacionServicioDetalle;
	
	public LiquidacionServicio() {
		this.id=0;
		this.fechaInicio= new Date();
		this.fechaFin= new Date();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioServicio= new Funcionario();
		this.totalServicio=0.0;
		this.totalComision=0.0;
		this.resumen="";
		this.liquidacionServicioDetalle= new ArrayList<LiquidacionServicioDetalle>();
		this.estado= false;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public List<LiquidacionServicioDetalle> getLiquidacionServicioDetalle() {
		return liquidacionServicioDetalle;
	}

	public void setLiquidacionServicioDetalle(List<LiquidacionServicioDetalle> liquidacionServicioDetalle) {
		this.liquidacionServicioDetalle = liquidacionServicioDetalle;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Funcionario getFuncionarioServicio() {
		return funcionarioServicio;
	}

	public void setFuncionarioServicio(Funcionario funcionarioServicio) {
		this.funcionarioServicio = funcionarioServicio;
	}

	public Double getTotalServicio() {
		return totalServicio;
	}

	public void setTotalServicio(Double totalServicio) {
		this.totalServicio = totalServicio;
	}

	public Double getTotalComision() {
		return totalComision;
	}

	public void setTotalComision(Double totalComision) {
		this.totalComision = totalComision;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	
}
