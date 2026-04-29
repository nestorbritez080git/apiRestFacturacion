package com.bisontecfacturacion.security.auxiliar;

public class VentaMensualDTO {
	 	private String mes;
	    private Double totalVenta;
	    private Double totalDevolucion;
	    private Double neto;

	    public VentaMensualDTO(String mes, Double venta, Double devolucion) {
	        this.mes = mes;
	        this.totalVenta = venta;
	        this.totalDevolucion = devolucion;
	        this.neto = venta - devolucion;
	    }
	    
		public Double getNeto() {
			return neto;
		}

		public void setNeto(Double neto) {
			this.neto = neto;
		}

		public String getMes() {
			return mes;
		}
		public void setMes(String mes) {
			this.mes = mes;
		}
		public Double getTotalVenta() {
			return totalVenta;
		}
		public void setTotalVenta(Double totalVenta) {
			this.totalVenta = totalVenta;
		}
		public Double getTotalDevolucion() {
			return totalDevolucion;
		}
		public void setTotalDevolucion(Double totalDevolucion) {
			this.totalDevolucion = totalDevolucion;
		}
	    
}
