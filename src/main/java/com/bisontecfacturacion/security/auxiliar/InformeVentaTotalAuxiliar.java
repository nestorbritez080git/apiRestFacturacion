package com.bisontecfacturacion.security.auxiliar;

public class InformeVentaTotalAuxiliar {
	private Double totalComision;
	private Double totalVenta;
	private Double totalCosto;
	public InformeVentaTotalAuxiliar() {
		// TODO Auto-generated constructor stub
		this.totalComision=0.0;
		this.totalVenta=0.0;
		this.totalCosto=0.0;
	}
	
	public Double getTotalCosto() {
		return totalCosto;
	}

	public void setTotalCosto(Double totalCosto) {
		this.totalCosto = totalCosto;
	}

	public Double getTotalComision() {
		return totalComision;
	}
	public void setTotalComision(Double totalComision) {
		this.totalComision = totalComision;
	}
	public Double getTotalVenta() {
		return totalVenta;
	}
	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}
	
}
