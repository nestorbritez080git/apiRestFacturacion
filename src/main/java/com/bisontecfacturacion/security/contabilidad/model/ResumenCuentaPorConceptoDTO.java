package com.bisontecfacturacion.security.contabilidad.model;

import java.util.List;

public class ResumenCuentaPorConceptoDTO {
	    
	private List<ResumenCuentaPorConceptoDTODetalle> ingreso;
    private List<ResumenCuentaPorConceptoDTODetalle> costo;
	public List<ResumenCuentaPorConceptoDTODetalle> getIngreso() {
		return ingreso;
	}
	
	public void setIngreso(List<ResumenCuentaPorConceptoDTODetalle> ingreso) {
		this.ingreso = ingreso;
	}
	public List<ResumenCuentaPorConceptoDTODetalle> getCosto() {
		return costo;
	}
	public void setCosto(List<ResumenCuentaPorConceptoDTODetalle> costo) {
		this.costo = costo;
	}
	    
		

		
}
