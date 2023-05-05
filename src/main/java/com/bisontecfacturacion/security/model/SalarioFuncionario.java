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
public class SalarioFuncionario {
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
	private Funcionario funcionarioAutorizado;
	@NotNull
	private Double totalSalario;
	private Double totalAnticipo;
	private Double totalPagado;
	@ManyToOne
	private PlanillaSalarioFuncionario planillaSalarioFuncionario;
	@ManyToOne
	private Periodo periodo;
	
	private boolean estado;
	
	@OneToMany(mappedBy="salarioFuncionario")
	private List<SalarioFuncionarioDetalle> salarioFuncionarioDetalles;
	
	public SalarioFuncionario() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.fecha= new Date();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioAutorizado= new Funcionario();
		this.totalSalario=0.0;
		this.totalAnticipo=0.0;
		this.totalPagado=0.0;
		this.planillaSalarioFuncionario= new PlanillaSalarioFuncionario();
		this.periodo= new Periodo();
		this.estado=false;
		this.salarioFuncionarioDetalles = new  ArrayList<SalarioFuncionarioDetalle>();
	}

	public Double getTotalPagado() {
		return totalPagado;
	}

	public void setTotalPagado(Double totalPagado) {
		this.totalPagado = totalPagado;
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

	public Funcionario getFuncionarioAutorizado() {
		return funcionarioAutorizado;
	}

	public void setFuncionarioAutorizado(Funcionario funcionarioAutorizado) {
		this.funcionarioAutorizado = funcionarioAutorizado;
	}

	public Double getTotalSalario() {
		return totalSalario;
	}

	public void setTotalSalario(Double totalSalario) {
		this.totalSalario = totalSalario;
	}

	public Double getTotalAnticipo() {
		return totalAnticipo;
	}

	public void setTotalAnticipo(Double totalAnticipo) {
		this.totalAnticipo = totalAnticipo;
	}

	public PlanillaSalarioFuncionario getPlanillaSalarioFuncionario() {
		return planillaSalarioFuncionario;
	}

	public void setPlanillaSalarioFuncionario(PlanillaSalarioFuncionario planillaSalarioFuncionario) {
		this.planillaSalarioFuncionario = planillaSalarioFuncionario;
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

	public List<SalarioFuncionarioDetalle> getSalarioFuncionarioDetalles() {
		return salarioFuncionarioDetalles;
	}

	public void setSalarioFuncionarioDetalles(List<SalarioFuncionarioDetalle> salarioFuncionarioDetalles) {
		this.salarioFuncionarioDetalles = salarioFuncionarioDetalles;
	}

	


}
