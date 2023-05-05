package com.bisontecfacturacion.security.auxiliar;

import java.util.List;

public class AnticipoAuxiliar {
	
	private int id;
	private int idSalario;
	private int idCabecera;
	private String funcionario;
	private double montoSalario;
	private double montoAnticipo;
	private double montoApagar;
	private List<AnticipoDetalleAuxiliar> anticipoDetalleAuxiliar;
	
	
	public int getIdCabecera() {
		return idCabecera;
	}
	public void setIdCabecera(int idCabecera) {
		this.idCabecera = idCabecera;
	}
	public int getIdSalario() {
		return idSalario;
	}
	public void setIdSalario(int idSalario) {
		this.idSalario = idSalario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}
	public double getMontoSalario() {
		return montoSalario;
	}
	public void setMontoSalario(double montoSalario) {
		this.montoSalario = montoSalario;
	}
	public double getMontoAnticipo() {
		return montoAnticipo;
	}
	public void setMontoAnticipo(double montoAnticipo) {
		this.montoAnticipo = montoAnticipo;
	}
	public double getMontoApagar() {
		return montoApagar;
	}
	public void setMontoApagar(double montoApagar) {
		this.montoApagar = montoApagar;
	}
	public List<AnticipoDetalleAuxiliar> getAnticipoDetalleAuxiliar() {
		return anticipoDetalleAuxiliar;
	}
	public void setAnticipoDetalleAuxiliar(List<AnticipoDetalleAuxiliar> anticipoDetalleAuxiliar) {
		this.anticipoDetalleAuxiliar = anticipoDetalleAuxiliar;
	}

	

}


