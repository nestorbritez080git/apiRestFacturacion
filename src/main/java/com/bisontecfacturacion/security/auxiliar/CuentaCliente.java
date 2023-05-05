package com.bisontecfacturacion.security.auxiliar;

public class CuentaCliente {
	private String cliente;
	private String fecha;
	private String fechas;
	private int diaAtrasado;
	private double total;
	private double pagado;
	private double saldo;
	
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getDiaAtrasado() {
		return diaAtrasado;
	}
	public void setDiaAtrasado(int diaAtrasado) {
		this.diaAtrasado = diaAtrasado;
	}
	public String getFechas() {
		return fechas;
	}
	public void setFechas(String fechas) {
		this.fechas = fechas;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getPagado() {
		return pagado;
	}
	public void setPagado(double pagado) {
		this.pagado = pagado;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	
}
