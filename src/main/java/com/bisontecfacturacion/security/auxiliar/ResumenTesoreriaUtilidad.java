package com.bisontecfacturacion.security.auxiliar;

public class ResumenTesoreriaUtilidad {
private Double totalCosto;
private Double totalVenta;
private Double totalUtlidad;
private Double totalDevolucion;
public ResumenTesoreriaUtilidad() {
	// TODO Auto-generated constructor stub
	this.totalCosto=0.0;
	this.totalVenta=0.0;
	this.totalUtlidad=0.0;
	this.totalDevolucion=0.0;
}

public Double getTotalDevolucion() {
	return totalDevolucion;
}

public void setTotalDevolucion(Double totalDevolucion) {
	this.totalDevolucion = totalDevolucion;
}

public Double getTotalCosto() {
	return totalCosto;
}
public void setTotalCosto(Double totalCosto) {
	this.totalCosto = totalCosto;
}
public Double getTotalVenta() {
	return totalVenta;
}
public void setTotalVenta(Double totalVenta) {
	this.totalVenta = totalVenta;
}
public Double getTotalUtlidad() {
	return totalUtlidad;
}
public void setTotalUtlidad(Double totalUtlidad) {
	this.totalUtlidad = totalUtlidad;
}

}
