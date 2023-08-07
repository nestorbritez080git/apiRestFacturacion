package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CobrosCliente {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private double total;
	@ManyToOne
	private CuentaCobrarCabecera cuentaCobrarCabecera;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private CobrosClienteCabecera cobrosClienteCabecera;
	@ManyToOne
	private OperacionCaja operacionCaja;
	public CobrosCliente() {
		this.id=0;
		this.fecha= new Date();
		this.total=0.0;
		this.cuentaCobrarCabecera = new CuentaCobrarCabecera();
		this.funcionario = new Funcionario();
		this.operacionCaja= new OperacionCaja();
		this.cobrosClienteCabecera= new CobrosClienteCabecera();
	}

	public int getId() {
		return id;
	}

	public Date getFecha() {
		return fecha;
	}

	public double getTotal() {
		return total;
	}

	public CuentaCobrarCabecera getCuentaCobrarCabecera() {
		return cuentaCobrarCabecera;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void setCuentaCobrarCabecera(CuentaCobrarCabecera cuentaCobrarCabecera) {
		this.cuentaCobrarCabecera = cuentaCobrarCabecera;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}

	public CobrosClienteCabecera getCobrosClienteCabecera() {
		return cobrosClienteCabecera;
	}

	public void setCobrosClienteCabecera(CobrosClienteCabecera cobrosClienteCabecera) {
		this.cobrosClienteCabecera = cobrosClienteCabecera;
	}


	
}
