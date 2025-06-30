package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;

public class ResumenCuentaDTO {
	 private String tipoCuenta;
	    private String cuenta;
	    private BigDecimal totalDebe;
	    private BigDecimal totalHaber;
	    private BigDecimal saldo;
	   	 public ResumenCuentaDTO(String tipoCuenta, String cuenta, BigDecimal totalDebe, BigDecimal totalHaber, BigDecimal saldo) {
	        this.tipoCuenta = tipoCuenta;
	        this.cuenta = cuenta;
	        this.totalDebe = totalDebe;
	        this.totalHaber = totalHaber;
	        this.saldo = saldo;
	    }
		public String getTipoCuenta() {
			return tipoCuenta;
		}
		public void setTipoCuenta(String tipoCuenta) {
			this.tipoCuenta = tipoCuenta;
		}
		public String getCuenta() {
			return cuenta;
		}
		public void setCuenta(String cuenta) {
			this.cuenta = cuenta;
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
