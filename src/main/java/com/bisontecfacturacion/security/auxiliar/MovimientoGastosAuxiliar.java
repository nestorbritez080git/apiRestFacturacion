package com.bisontecfacturacion.security.auxiliar;

import java.util.Date;

public class MovimientoGastosAuxiliar {
	private int id;
	private Double monto;
	private String gastosDetalle;
	private String gastosConsumicion;
	private String gastosCategoria;
	private String comprobante;
	private Date fecha;
	private String funcionario;
	
	
	public String getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getGastosDetalle() {
		return gastosDetalle;
	}
	public void setGastosDetalle(String gastosDetalle) {
		this.gastosDetalle = gastosDetalle;
	}
	public String getGastosConsumicion() {
		return gastosConsumicion;
	}
	public void setGastosConsumicion(String gastosConsumicion) {
		this.gastosConsumicion = gastosConsumicion;
	}
	public String getGastosCategoria() {
		return gastosCategoria;
	}
	public void setGastosCategoria(String gastosCategoria) {
		this.gastosCategoria = gastosCategoria;
	}
	public String getComprobante() {
		return comprobante;
	}
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
		
	

}
