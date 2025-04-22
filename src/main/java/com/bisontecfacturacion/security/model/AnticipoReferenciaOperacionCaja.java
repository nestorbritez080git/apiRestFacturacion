package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AnticipoReferenciaOperacionCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Anticipo anticipo;
	@ManyToOne
	private OperacionCaja operacionCaja;

		
	public AnticipoReferenciaOperacionCaja() {
		this.anticipo= new Anticipo();
		this.id=0;
		this.operacionCaja=new OperacionCaja();	
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

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	


	
	
	
}
