package com.bisontecfacturacion.security.modeloIA;

public class VentaModeloIA {
	private String mes;
    private Double neto;
    
    public VentaModeloIA() {
    	this.mes="";
    	this.neto=0.0;
    }

	public VentaModeloIA(String mes, Double neto) {
		super();
		this.mes = mes;
		this.neto = neto;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public Double getNeto() {
		return neto;
	}
	public void setNeto(Double neto) {
		this.neto = neto;
	}
    
}
