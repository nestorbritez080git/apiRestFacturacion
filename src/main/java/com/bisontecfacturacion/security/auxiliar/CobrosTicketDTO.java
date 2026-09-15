package com.bisontecfacturacion.security.auxiliar;

public class CobrosTicketDTO {
	 private Integer  idCuenta;
	 private Integer idVenta;
	    private Double total;
	    private Double totalInteres;
	    private Double pagado;
	    private Double importe;
	    private Double saldo;
	    private String nroDocumento;
	    private String tipoDocumento;
	    private String tipoPago;
	    private Double montoCobrado;
	    
	    
		public Double getImporte() {
			return importe;
		}
		public void setImporte(Double importe) {
			this.importe = importe;
		}
		public Double getTotalInteres() {
			return totalInteres;
		}
		public void setTotalInteres(Double totalInteres) {
			this.totalInteres = totalInteres;
		}
		public Integer getIdCuenta() {
			return idCuenta;
		}
		public void setIdCuenta(Integer idCuenta) {
			this.idCuenta = idCuenta;
		}
		public Integer getIdVenta() {
			return idVenta;
		}
		public void setIdVenta(Integer idVenta) {
			this.idVenta = idVenta;
		}
		public Double getTotal() {
			return total;
		}
		public void setTotal(Double total) {
			this.total = total;
		}
		public Double getPagado() {
			return pagado;
		}
		public void setPagado(Double pagado) {
			this.pagado = pagado;
		}
		public Double getSaldo() {
			return saldo;
		}
		public void setSaldo(Double saldo) {
			this.saldo = saldo;
		}
		public String getNroDocumento() {
			return nroDocumento;
		}
		public void setNroDocumento(String nroDocumento) {
			this.nroDocumento = nroDocumento;
		}
		public String getTipoDocumento() {
			return tipoDocumento;
		}
		public void setTipoDocumento(String tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}
		public String getTipoPago() {
			return tipoPago;
		}
		public void setTipoPago(String tipoPago) {
			this.tipoPago = tipoPago;
		}
		public Double getMontoCobrado() {
			return montoCobrado;
		}
		public void setMontoCobrado(Double montoCobrado) {
			this.montoCobrado = montoCobrado;
		}
	    
	 
	    
}
