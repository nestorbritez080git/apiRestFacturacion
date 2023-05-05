package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class DevolucionCompraDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private int cantidad;
	private double precio;
	private String descripcion;
	private double subTotal;
	
	@ManyToOne
	private DetalleCompra detalleCompra;
	@ManyToOne
	private DevolucionCompra devolucionCompra;
	
	
	public DevolucionCompraDetalle() {
		super();
		detalleCompra=new DetalleCompra();
		devolucionCompra=new DevolucionCompra();
	}


	public int getId() {
		return id;
	}


	public int getCantidad() {
		return cantidad;
	}


	public double getPrecio() {
		return precio;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public double getSubTotal() {
		return subTotal;
	}


	public DetalleCompra getDetalleCompra() {
		return detalleCompra;
	}


	public DevolucionCompra getDevolucionCompra() {
		return devolucionCompra;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}


	public void setDetalleCompra(DetalleCompra detalleCompra) {
		this.detalleCompra = detalleCompra;
	}


	public void setDevolucionCompra(DevolucionCompra devolucionCompra) {
		this.devolucionCompra = devolucionCompra;
	}

	
	
}
