package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class InventarioDetalle {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@Column(length = 50)
	private String hora;
	private Double existencia;
	private Double cantidad;
	private String descripcion;
	private Double precioCosto;
	private Double precioVenta1;
	private Double precioVenta2;
	private Double precioVenta3;
	private Double precioVenta4;
	
	@ManyToOne
	private InventarioCabecera inventarioCabecera;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Producto producto;

	public InventarioDetalle() {
		super();
		this.existencia=0.0;
		this.cantidad=0.0;
		this.descripcion="";
		this.precioCosto=0.0;
		this.precioVenta1=0.0;
		this.precioVenta2=0.0;
		this.precioVenta3=0.0;
		this.precioVenta4=0.0;
		this.inventarioCabecera = new InventarioCabecera();
		this.funcionario = new Funcionario();
		this.producto = new Producto();
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

	public Double getExistencia() {
		return existencia;
	}

	public void setExistencia(Double existencia) {
		this.existencia = existencia;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(Double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public Double getPrecioVenta1() {
		return precioVenta1;
	}

	public void setPrecioVenta1(Double precioVenta1) {
		this.precioVenta1 = precioVenta1;
	}

	public Double getPrecioVenta2() {
		return precioVenta2;
	}

	public void setPrecioVenta2(Double precioVenta2) {
		this.precioVenta2 = precioVenta2;
	}

	public Double getPrecioVenta3() {
		return precioVenta3;
	}

	public void setPrecioVenta3(Double precioVenta3) {
		this.precioVenta3 = precioVenta3;
	}

	public Double getPrecioVenta4() {
		return precioVenta4;
	}

	public void setPrecioVenta4(Double precioVenta4) {
		this.precioVenta4 = precioVenta4;
	}

	public InventarioCabecera getInventarioCabecera() {
		return inventarioCabecera;
	}

	public void setInventarioCabecera(InventarioCabecera inventarioCabecera) {
		this.inventarioCabecera = inventarioCabecera;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	
	
	
}
