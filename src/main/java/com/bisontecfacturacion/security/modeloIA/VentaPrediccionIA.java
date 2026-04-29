package com.bisontecfacturacion.security.modeloIA;

import java.util.List;

import com.bisontecfacturacion.security.auxiliar.VentaMensualDTO;

public class VentaPrediccionIA {
		private List<VentaModeloIA> historico;
	    private List<VentaModeloIA> prediccion;
		public List<VentaModeloIA> getHistorico() {
			return historico;
		}
		public void setHistorico(List<VentaModeloIA> historico) {
			this.historico = historico;
		}
		public List<VentaModeloIA> getPrediccion() {
			return prediccion;
		}
		public void setPrediccion(List<VentaModeloIA> prediccion) {
			this.prediccion = prediccion;
		}

	  

}
