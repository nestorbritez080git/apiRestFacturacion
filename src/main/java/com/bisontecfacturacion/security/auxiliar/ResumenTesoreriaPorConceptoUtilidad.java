package com.bisontecfacturacion.security.auxiliar;

public class ResumenTesoreriaPorConceptoUtilidad {
	private String descripcion;
	private Double total;
	public ResumenTesoreriaPorConceptoUtilidad() {
		this.descripcion="";
		this.total=0.0;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
}
