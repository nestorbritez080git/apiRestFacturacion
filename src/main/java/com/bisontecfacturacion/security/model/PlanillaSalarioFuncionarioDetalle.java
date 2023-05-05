package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class PlanillaSalarioFuncionarioDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Funcionario funcionario;
	

	@ManyToOne
	private PlanillaSalarioFuncionario planillaSalarioFuncionario;
	
	public PlanillaSalarioFuncionarioDetalle() {
		this.id=0;
		this.funcionario=new Funcionario();
		this.planillaSalarioFuncionario = new PlanillaSalarioFuncionario(); 
	}
	
	public PlanillaSalarioFuncionario getPlanillaSalarioFuncionario() {
		return planillaSalarioFuncionario;
	}

	public void setPlanillaSalarioFuncionario(PlanillaSalarioFuncionario planillaSalarioFuncionario) {
		this.planillaSalarioFuncionario = planillaSalarioFuncionario;
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
	
}
