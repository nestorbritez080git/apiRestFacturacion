package com.bisontecfacturacion.security.auxiliar;

public class InformeVentaTotalPorProducto {
	private String funcionario;
	private String descripcion;
	private Double precio;
	private Double cantidadVenta;
	private Double costo;
	private Double subTotalVenta;
	private Double cantidadDevuelta;
	private Double costoDevuelta;
	private Double subTotalDevuelto;
	private Double subTotalNeta;
	public String getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Double getCantidadVenta() {
		return cantidadVenta;
	}
	public void setCantidadVenta(Double cantidadVenta) {
		this.cantidadVenta = cantidadVenta;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Double getSubTotalVenta() {
		return subTotalVenta;
	}
	public void setSubTotalVenta(Double subTotalVenta) {
		this.subTotalVenta = subTotalVenta;
	}
	public Double getCantidadDevuelta() {
		return cantidadDevuelta;
	}
	public void setCantidadDevuelta(Double cantidadDevuelta) {
		this.cantidadDevuelta = cantidadDevuelta;
	}
	public Double getCostoDevuelta() {
		return costoDevuelta;
	}
	public void setCostoDevuelta(Double costoDevuelta) {
		this.costoDevuelta = costoDevuelta;
	}
	public Double getSubTotalDevuelto() {
		return subTotalDevuelto;
	}
	public void setSubTotalDevuelto(Double subTotalDevuelto) {
		this.subTotalDevuelto = subTotalDevuelto;
	}
	public Double getSubTotalNeta() {
		return subTotalNeta;
	}
	public void setSubTotalNeta(Double subTotalNeta) {
		this.subTotalNeta = subTotalNeta;
	}

	
	
	
}
