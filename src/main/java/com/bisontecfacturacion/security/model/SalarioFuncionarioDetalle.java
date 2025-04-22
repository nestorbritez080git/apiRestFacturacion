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
	private Anticipo anticipo;
	@ManyToOne
	private SalarioFuncionario salarioFuncionario;
	

	
	public SalarioFuncionarioDetalle() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.anticipo= new Anticipo();
		this.salarioFuncionario= new SalarioFuncionario();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Anticipo getAnticipo() {
		return anticipo;
	}
	public void setAnticipo(Anticipo anticipo) {
		this.anticipo = anticipo;
	}
	public SalarioFuncionario getSalarioFuncionario() {
		return salarioFuncionario;
	}
	public void setSalarioFuncionario(SalarioFuncionario salarioFuncionario) {
		this.salarioFuncionario = salarioFuncionario;
	}
}
