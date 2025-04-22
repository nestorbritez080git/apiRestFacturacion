package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class AnulacionesAnticipoReferenciaCajaChica {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private AnulacionesAnticipo anulacionesAnticipo;
	@ManyToOne
	private TransferenciaAnticipo transferenciaAnticipo;
		
	public AnulacionesAnticipoReferenciaCajaChica() {
		this.anulacionesAnticipo= new AnulacionesAnticipo();
		this.id=0;
		this.transferenciaAnticipo= new TransferenciaAnticipo();
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	public TransferenciaAnticipo getTransferenciaAnticipo() {
		return transferenciaAnticipo;
	}

	public void setTransferenciaAnticipo(TransferenciaAnticipo transferenciaAnticipo) {
		this.transferenciaAnticipo = transferenciaAnticipo;
	}

	
	
	
	
	
}
