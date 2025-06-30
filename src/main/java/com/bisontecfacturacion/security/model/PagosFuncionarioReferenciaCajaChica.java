package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PagosFuncionarioReferenciaCajaChica {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private PagosFuncionario pagosFuncionario;
	@ManyToOne
	private TransferenciaAnticipo transferenciaAnticipo;
		
	public PagosFuncionarioReferenciaCajaChica() {
		this.pagosFuncionario= new PagosFuncionario();
		this.id=0;
		this.transferenciaAnticipo= new TransferenciaAnticipo();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	

	public PagosFuncionario getPagosFuncionario() {
		return pagosFuncionario;
	}

	public void setPagosFuncionario(PagosFuncionario pagosFuncionario) {
		this.pagosFuncionario = pagosFuncionario;
	}

	public TransferenciaAnticipo getTransferenciaAnticipo() {
		return transferenciaAnticipo;
	}

	public void setTransferenciaAnticipo(TransferenciaAnticipo transferenciaAnticipo) {
		this.transferenciaAnticipo = transferenciaAnticipo;
	}

	
	
	
	
	
}
