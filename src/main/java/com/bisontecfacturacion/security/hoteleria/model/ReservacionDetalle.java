package com.bisontecfacturacion.security.hoteleria.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.Venta;

@Entity
public class ReservacionDetalle {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@ManyToOne
	private Producto producto;
	@NotNull
	private String descripcion;
	@NotNull
	private Double cantidad;
	@NotNull
	private Double precio;
	private Double descuento;
	private String iva;
	@NotNull
	private Double subTotal;
	private Boolean isBalanza;
	private String tipoPrecio;
	private Double montoIva;

	@ManyToOne
	private ReservacionCabecera reservacionCabecera;
	
	private Double costo;
	public ReservacionDetalle() {
		this.id=0;
		this.descuento = 0.0;
		this.reservacionCabecera = new ReservacionCabecera();
		this.producto=new Producto();
		this.cantidad=0.00;
		this.precio=0.00;
		this.subTotal=0.00;
		this.iva="";
		this.isBalanza=false;
		this.tipoPrecio="";
		this.costo=0.0;
		this.montoIva=0.0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	public Double getDescuento() {
		return descuento;
	}
	public void setDescuento(Double descuento) {
		this.descuento = descuento;
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
	public Boolean getIsBalanza() {
		return isBalanza;
	}
	public void setIsBalanza(Boolean isBalanza) {
		this.isBalanza = isBalanza;
	}
	public String getTipoPrecio() {
		return tipoPrecio;
	}
	public void setTipoPrecio(String tipoPrecio) {
		this.tipoPrecio = tipoPrecio;
	}
	public Double getMontoIva() {
		return montoIva;
	}
	public void setMontoIva(Double montoIva) {
		this.montoIva = montoIva;
	}
	
	public ReservacionCabecera getReservacionCabecera() {
		return reservacionCabecera;
	}
	public void setReservacionCabecera(ReservacionCabecera reservacionCabecera) {
		this.reservacionCabecera = reservacionCabecera;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	
}
