package com.bisontecfacturacion.security.config;

import java.math.BigDecimal;

public class CotizacionesDTO {
	private String moneda;
    private BigDecimal compra;
    private BigDecimal venta;
    private String fecha;
    public CotizacionesDTO() {
		// TODO Auto-generated constructor stub
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public BigDecimal getCompra() {
		return compra;
	}
	public void setCompra(BigDecimal compra) {
		this.compra = compra;
	}
	public BigDecimal getVenta() {
		return venta;
	}
	public void setVenta(BigDecimal venta) {
		this.venta = venta;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
    
}
