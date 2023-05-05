package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class DevolucionCompra {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private double total;
	
	@ManyToOne
	private Compra compra;
	
	@ManyToOne
	private Funcionario funcionario;
	
	

	public DevolucionCompra() {
		super();
		compra=new Compra();
		funcionario=new Funcionario();
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



	public Compra getCompra() {
		return compra;
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



	public void setCompra(Compra compra) {
		this.compra = compra;
	}



	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

}
