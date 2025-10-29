package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PagosFuncionarioReferenciaOperacionCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private PagosFuncionario pagosFuncionario;
	@ManyToOne
	private OperacionCaja operacionCaja;

		
	public PagosFuncionarioReferenciaOperacionCaja() {
		this.pagosFuncionario= new PagosFuncionario();
		this.id=0;
		this.operacionCaja=new OperacionCaja();	
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

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	


	
	
	
}
