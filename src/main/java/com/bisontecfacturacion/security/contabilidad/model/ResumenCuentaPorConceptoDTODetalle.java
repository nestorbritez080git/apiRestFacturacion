package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;

public class ResumenCuentaPorConceptoDTODetalle {
	    private String concepto;
	    private Long items;
	    private String tipoCuenta;
	    private BigDecimal totalDebe;
	    private BigDecimal totalHaber;
	    private BigDecimal saldo;
	  
	    
		public ResumenCuentaPorConceptoDTODetalle() {
			super();
			this.concepto = "";
	        this.items = 0L;
	        this.tipoCuenta = "";
	        this.totalDebe = BigDecimal.ZERO;
	        this.totalHaber = BigDecimal.ZERO;
	        this.saldo = BigDecimal.ZERO;
		}


		


		public String getTipoCuenta() {
			return tipoCuenta;
		}





		public void setTipoCuenta(String tipoCuenta) {
			this.tipoCuenta = tipoCuenta;
		}





		public Long getItems() {
			return items;
		}


		public void setItems(Long items) {
			this.items = items;
		}


		public String getConcepto() {
			return concepto;
		}


		public void setConcepto(String concepto) {
			this.concepto = concepto;
		}


		public BigDecimal getTotalDebe() {
			return totalDebe;
		}
		public void setTotalDebe(BigDecimal totalDebe) {
			this.totalDebe = totalDebe;
		}
		public BigDecimal getTotalHaber() {
			return totalHaber;
		}
		public void setTotalHaber(BigDecimal totalHaber) {
			this.totalHaber = totalHaber;
		}
		public BigDecimal getSaldo() {
			return saldo;
		}
		public void setSaldo(BigDecimal saldo) {
			this.saldo = saldo;
		}
	   	 
}
