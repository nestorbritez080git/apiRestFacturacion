package com.bisontecfacturacion.security.reportes;

public class ReporteConsumoBobeda {

	private Double cantidadDispachada;
	private String descripcion;
	private String nombre;
	private Double consumoTotal;
	private Double precio;
	private Double subTotal;
	private String nombreCliente;
	public ReporteConsumoBobeda() {
		this.cantidadDispachada=0.0;
		this.descripcion="";
		this.nombre="";
		this.consumoTotal=0.0;
		this.precio=0.0;
		this.subTotal=0.0;
		this.nombreCliente="";
	}
	
	public Double getCantidadDispachada() {
		return cantidadDispachada;
	}
	public void setCantidadDispachada(Double cantidadDispachada) {
		this.cantidadDispachada = cantidadDispachada;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getConsumoTotal() {
		return consumoTotal;
	}
	public void setConsumoTotal(Double consumoTotal) {
		this.consumoTotal = consumoTotal;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	
	

}
