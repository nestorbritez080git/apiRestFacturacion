package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
public class PagosFuncionarioDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private Double monto;
	@ManyToOne
	private Anticipo anticipo;
	@ManyToOne
	private PagosFuncionario pagosFuncionario;
	public PagosFuncionarioDetalle() {
		this.id=0;
		this.monto=0.0;
		this.anticipo=new Anticipo();
		this.pagosFuncionario=new PagosFuncionario();

		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Anticipo getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(Anticipo anticipo) {
		this.anticipo = anticipo;
	}

	public PagosFuncionario getPagosFuncionario() {
		return pagosFuncionario;
	}

	public void setPagosFuncionario(PagosFuncionario pagosFuncionario) {
		this.pagosFuncionario = pagosFuncionario;
	}

	
}
