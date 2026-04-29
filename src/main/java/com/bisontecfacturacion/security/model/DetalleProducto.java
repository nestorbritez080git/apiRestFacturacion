package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"i"})
@Entity
public class DetalleProducto {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer  id;
	
	@ManyToOne
	private Producto producto;
	@NotNull
	private String descripcion;
	@NotNull
	private Double cantidad;
	private Double cantidadDevolucion;
	
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
	@JsonIgnoreProperties
	private Venta venta;
	
	private Double costo;
	private Double subTotalCosto;
	private Double costoPromedio;
	private Double subTotalCostoPromedio;

	
	public DetalleProducto() {
		this.id=0;
		this.descuento = 0.0;
		this.venta = new Venta();
		this.producto=new Producto();
		this.cantidad=0.0;
		this.cantidadDevolucion=0.0;
		this.precio=0.0;
		this.subTotal=0.0;
		this.iva="";
		this.isBalanza=false;
		this.tipoPrecio="";
		this.costo=0.0;
		this.montoIva=0.0;
		this.costoPromedio=0.0;
		this.subTotalCostoPromedio=0.0;
		this.subTotalCosto=0.0;
	}


	public Double getSubTotalCosto() {
		return subTotalCosto;
	}


	public void setSubTotalCosto(Double subTotalCosto) {
		this.subTotalCosto = subTotalCosto;
	}


	public Double getCantidadDevolucion() {
		return cantidadDevolucion;
	}


	public void setCantidadDevolucion(Double cantidadDevolucion) {
		this.cantidadDevolucion = cantidadDevolucion;
	}


	public Double getCosto() {
		return costo;
	}


	public void setCosto(Double costo) {
		this.costo = costo;
	}


	public Double getMontoIva() {
		return montoIva;
	}


	public void setMontoIva(Double montoIva) {
		this.montoIva = montoIva;
	}


	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
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


	public Venta getVenta() {
		return venta;
	}


	public void setVenta(Venta venta) {
		this.venta = venta;
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


	public Double getCostoPromedio() {
		return costoPromedio;
	}


	public void setCostoPromedio(Double costoPromedio) {
		this.costoPromedio = costoPromedio;
	}


	public Double getSubTotalCostoPromedio() {
		return subTotalCostoPromedio;
	}


	public void setSubTotalCostoPromedio(Double subTotalCostoPromedio) {
		this.subTotalCostoPromedio = subTotalCostoPromedio;
	}



	

}
