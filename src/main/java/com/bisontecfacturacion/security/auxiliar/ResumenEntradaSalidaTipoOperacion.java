package com.bisontecfacturacion.security.auxiliar;

public class ResumenEntradaSalidaTipoOperacion {
	private String descripcion;
	private double montoEfectivo;
	private double montoCheque;
	private double montoTarjeta;
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getMontoEfectivo() {
		return montoEfectivo;
	}
	public void setMontoEfectivo(double montoEfectivo) {
		this.montoEfectivo = montoEfectivo;
	}
	public double getMontoCheque() {
		return montoCheque;
	}
	public void setMontoCheque(double montoCheque) {
		this.montoCheque = montoCheque;
	}
	public double getMontoTarjeta() {
		return montoTarjeta;
	}
	public void setMontoTarjeta(double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}
	
}
