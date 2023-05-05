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
public class SalarioFuncionarioDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Funcionario funcionario;
	
	@NotNull
	private Double montoSalario;
	
	private Double montoAnticipo;
	
	@NotNull
	private Double montoPagado;
	
	@ManyToOne
	private SalarioFuncionario salarioFuncionario;
	

	
	public SalarioFuncionarioDetalle() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.funcionario= new Funcionario();
		this.montoSalario=0.0;
		this.montoAnticipo=0.0;
		this.montoPagado=0.0;
		this.salarioFuncionario= new SalarioFuncionario();
	}

	public Double getMontoAnticipo() {
		return montoAnticipo;
	}

	public void setMontoAnticipo(Double montoAnticipo) {
		this.montoAnticipo = montoAnticipo;
	}



	public Double getMontoPagado() {
		return montoPagado;
	}



	public void setMontoPagado(Double montoPagado) {
		this.montoPagado = montoPagado;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	

	public Double getMontoSalario() {
		return montoSalario;
	}

	public void setMontoSalario(Double montoSalario) {
		this.montoSalario = montoSalario;
	}

	public SalarioFuncionario getSalarioFuncionario() {
		return salarioFuncionario;
	}

	public void setSalarioFuncionario(SalarioFuncionario salarioFuncionario) {
		this.salarioFuncionario = salarioFuncionario;
	}

	
	


}
