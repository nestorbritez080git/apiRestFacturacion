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
public class LiquidacionAnticipo {
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
	private Funcionario funcionarioLiquidacion;
	@NotNull
	private Double total;
	private String resumen;
	@OneToMany(mappedBy="liquidacionAnticipo")
	private List<LiquidacionAnticipoDetalle> liquidacionAnticipoDetalles;
	private String estado;
	
	public LiquidacionAnticipo() {
		super();
		this.id=0;
		this.fecha= new Date();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioLiquidacion= new Funcionario();
		this.total=0.0;
		this.resumen="";
		this.liquidacionAnticipoDetalles= new ArrayList<LiquidacionAnticipoDetalle>();
		this.estado= "ABIERTO";
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

	public Funcionario getFuncionarioLiquidacion() {
		return funcionarioLiquidacion;
	}

	public void setFuncionarioLiquidacion(Funcionario funcionarioLiquidacion) {
		this.funcionarioLiquidacion = funcionarioLiquidacion;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	public List<LiquidacionAnticipoDetalle> getLiquidacionAnticipoDetalles() {
		return liquidacionAnticipoDetalles;
	}

	public void setLiquidacionAnticipoDetalles(List<LiquidacionAnticipoDetalle> liquidacionAnticipoDetalles) {
		this.liquidacionAnticipoDetalles = liquidacionAnticipoDetalles;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	
}
