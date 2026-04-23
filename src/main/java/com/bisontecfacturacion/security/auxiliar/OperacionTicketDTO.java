package com.bisontecfacturacion.security.auxiliar;

public class OperacionTicketDTO {
	 
	    private String descripcion;
	    private Double monto;
	    private Double efectivo;
	    private Double vuelto;
	    private int id;
	    
	    
	    
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public Double getMonto() {
			return monto;
		}
		public void setMonto(Double monto) {
			this.monto = monto;
		}
		public Double getEfectivo() {
			return efectivo;
		}
		public void setEfectivo(Double efectivo) {
			this.efectivo = efectivo;
		}
		public Double getVuelto() {
			return vuelto;
		}
		public void setVuelto(Double vuelto) {
			this.vuelto = vuelto;
		}
		
		
	    
}
