package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class DevolucionVentaDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;

	@NotNull
	private Double cantidad;
	@NotNull
	private Double precio;
	private String iva;
	@NotNull
	private Double subTotal;
	
	private Double montoIva;
	
	@ManyToOne
	private DetalleProducto detalleProducto;
	
	@ManyToOne
	@JsonIgnore
	private DevolucionVenta devolucionVenta;

	
	public DevolucionVentaDetalle() {
		this.id=0;
		this.descripcion="";
		this.cantidad=0.0;
		this.precio=0.0;
		this.iva="";
		this.subTotal=0.0;
		this.montoIva=0.0;
		this.detalleProducto= new DetalleProducto();
		this.devolucionVenta=new DevolucionVenta();
				
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Double getCantidad() {
		return cantidad;
	}


	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	public String getIva() {
		return iva;
	}


	public void setIva(String iva) {
		this.iva = iva;
	}


	public Double getSubTotal() {
		return subTotal;
	}


	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}


	public Double getMontoIva() {
		return montoIva;
	}


	public void setMontoIva(Double montoIva) {
		this.montoIva = montoIva;
	}


	public DetalleProducto getDetalleProducto() {
		return detalleProducto;
	}


	public void setDetalleProducto(DetalleProducto detalleProducto) {
		this.detalleProducto = detalleProducto;
	}


	public DevolucionVenta getDevolucionVenta() {
		return devolucionVenta;
	}


	public void setDevolucionVenta(DevolucionVenta devolucionVenta) {
		this.devolucionVenta = devolucionVenta;
	}
	
}
