package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class DetalleCompra {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	@NotNull
	private double cantidad;
	@NotNull
	private Double precioCosto;
	@NotNull
	private double subTotal;
	private String iva;
	
	private Double precioVenta_1;
	private Double precioVenta_2;
	private Double precioVenta_3;
	private Double precioVenta_4;
	private Double montoIva;
	
	@ManyToOne
	private Compra compra;
	@ManyToOne
	private Producto producto;

	
	
	public DetalleCompra() {
		id=0;
		cantidad=0.0;
		subTotal=0.0;
		iva="";
		descripcion="";
		compra=new Compra();
		producto=new Producto();
		montoIva=0.0;
	}

	public Double getMontoIva() {
		return montoIva;
	}

	public void setMontoIva(Double montoIva) {
		this.montoIva = montoIva;
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

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public Double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(Double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public Double getPrecioVenta_1() {
		return precioVenta_1;
	}

	public void setPrecioVenta_1(Double precioVenta_1) {
		this.precioVenta_1 = precioVenta_1;
	}

	public Double getPrecioVenta_2() {
		return precioVenta_2;
	}

	public void setPrecioVenta_2(Double precioVenta_2) {
		this.precioVenta_2 = precioVenta_2;
	}

	public Double getPrecioVenta_3() {
		return precioVenta_3;
	}

	public void setPrecioVenta_3(Double precioVenta_3) {
		this.precioVenta_3 = precioVenta_3;
	}

	public Double getPrecioVenta_4() {
		return precioVenta_4;
	}

	public void setPrecioVenta_4(Double precioVenta_4) {
		this.precioVenta_4 = precioVenta_4;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	
	

}
