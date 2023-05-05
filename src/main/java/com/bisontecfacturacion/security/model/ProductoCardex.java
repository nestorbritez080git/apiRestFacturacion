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
public class ProductoCardex {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@NotNull
	private double cantidadAplicacion;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Producto productoBase;
	@ManyToOne
	private Producto productoCompuesto;
	
	public ProductoCardex() {
		this.funcionario = new Funcionario();
		
		this.productoBase = new Producto();
		
		this.productoCompuesto = new Producto();
		this.cantidadAplicacion = 0.0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public double getCantidadAplicacion() {
		return cantidadAplicacion;
	}

	public void setCantidadAplicacion(double cantidadAplicacion) {
		this.cantidadAplicacion = cantidadAplicacion;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Producto getProductoBase() {
		return productoBase;
	}

	public void setProductoBase(Producto productoBase) {
		this.productoBase = productoBase;
	}

	public Producto getProductoCompuesto() {
		return productoCompuesto;
	}

	public void setProductoCompuesto(Producto productoCompuesto) {
		this.productoCompuesto = productoCompuesto;
	}
	
}
