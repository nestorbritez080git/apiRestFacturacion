package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
@Entity
public class CajaChica {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	@NotNull
	private Double monto;
	@NotNull
	private Double montoCheque;
	@NotNull
	private Double montoTarjeta;
	
	
	@ManyToOne
	@JoinColumn(name="funcionarior_id", nullable=true, insertable=true, updatable=true)
	private Funcionario funcionarioR;
	
	@ManyToOne
	@JoinColumn(name="funcionarioe_id", nullable=true, insertable=true, updatable=true)
	private Funcionario funcionarioE;
	
	public CajaChica() {
		this.id=0;
		this.descripcion="";
		this.monto=0.0;
		this.montoCheque=0.0;
		this.montoTarjeta=0.0;
		this.funcionarioE= new Funcionario();
		this.funcionarioR= new Funcionario();
	}
	
	public Funcionario getFuncionarioR() {
		return funcionarioR;
	}

	public void setFuncionarioR(Funcionario funcionarioR) {
		this.funcionarioR = funcionarioR;
	}

	public Funcionario getFuncionarioE() {
		return funcionarioE;
	}

	public void setFuncionarioE(Funcionario funcionarioE) {
		this.funcionarioE = funcionarioE;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getMontoCheque() {
		return montoCheque;
	}
	public void setMontoCheque(Double montoCheque) {
		this.montoCheque = montoCheque;
	}
	public Double getMontoTarjeta() {
		return montoTarjeta;
	}
	public void setMontoTarjeta(Double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}
	
}
