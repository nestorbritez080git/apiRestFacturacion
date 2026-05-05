package com.bisontecfacturacion.security.modeloIA;

import java.util.List;

import com.bisontecfacturacion.security.auxiliar.VentaMensualDTO;

public class VentaPrediccionPorProductoIA {
    private List<ProductoVentaIA> historico;

    private List<ProductoVentaIA> prediccion;
    
    

	public List<ProductoVentaIA> getHistorico() {
		return historico;
	}

	public void setHistorico(List<ProductoVentaIA> historico) {
		this.historico = historico;
	}

	public List<ProductoVentaIA> getPrediccion() {
		return prediccion;
	}

	public void setPrediccion(List<ProductoVentaIA> prediccion) {
		this.prediccion = prediccion;
	}
    
    

	  

}
