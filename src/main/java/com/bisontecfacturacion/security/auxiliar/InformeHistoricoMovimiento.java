package com.bisontecfacturacion.security.auxiliar;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.bisontecfacturacion.security.model.Funcionario;

public class InformeHistoricoMovimiento {
	private Integer aperturaId;
    private LocalDateTime fechaApertura;
    private String horaApertura;
    private String clienteNombre; // o razon social
    
    private Double saldoInicialEfe;
    private Double saldoInicialChe;
    private Double saldoInicialTar;

    private Double totalEntradaEfe;
    private Double totalEntradaChe;
    private Double totalEntradaTar;


    private Double totalSalidaEfe;
    private Double totalSalidaChe;
    private Double totalSalidaTar;
    
    private Double saldoFinalEfe;
    private Double saldoFinalChe;
    private Double saldoFinalTar;
	public Integer getAperturaId() {
		return aperturaId;
	}
	public void setAperturaId(Integer aperturaId) {
		this.aperturaId = aperturaId;
	}
	
	public LocalDateTime getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(LocalDateTime fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public String getHoraApertura() {
		return horaApertura;
	}
	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}
	public String getClienteNombre() {
		return clienteNombre;
	}
	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}
	public Double getSaldoInicialEfe() {
		return saldoInicialEfe;
	}
	public void setSaldoInicialEfe(Double saldoInicialEfe) {
		this.saldoInicialEfe = saldoInicialEfe;
	}
	public Double getSaldoInicialChe() {
		return saldoInicialChe;
	}
	public void setSaldoInicialChe(Double saldoInicialChe) {
		this.saldoInicialChe = saldoInicialChe;
	}
	public Double getSaldoInicialTar() {
		return saldoInicialTar;
	}
	public void setSaldoInicialTar(Double saldoInicialTar) {
		this.saldoInicialTar = saldoInicialTar;
	}
	public Double getTotalEntradaEfe() {
		return totalEntradaEfe;
	}
	public void setTotalEntradaEfe(Double totalEntradaEfe) {
		this.totalEntradaEfe = totalEntradaEfe;
	}
	public Double getTotalEntradaChe() {
		return totalEntradaChe;
	}
	public void setTotalEntradaChe(Double totalEntradaChe) {
		this.totalEntradaChe = totalEntradaChe;
	}
	public Double getTotalEntradaTar() {
		return totalEntradaTar;
	}
	public void setTotalEntradaTar(Double totalEntradaTar) {
		this.totalEntradaTar = totalEntradaTar;
	}
	public Double getTotalSalidaEfe() {
		return totalSalidaEfe;
	}
	public void setTotalSalidaEfe(Double totalSalidaEfe) {
		this.totalSalidaEfe = totalSalidaEfe;
	}
	public Double getTotalSalidaChe() {
		return totalSalidaChe;
	}
	public void setTotalSalidaChe(Double totalSalidaChe) {
		this.totalSalidaChe = totalSalidaChe;
	}
	public Double getTotalSalidaTar() {
		return totalSalidaTar;
	}
	public void setTotalSalidaTar(Double totalSalidaTar) {
		this.totalSalidaTar = totalSalidaTar;
	}
	public Double getSaldoFinalEfe() {
		return saldoFinalEfe;
	}
	public void setSaldoFinalEfe(Double saldoFinalEfe) {
		this.saldoFinalEfe = saldoFinalEfe;
	}
	public Double getSaldoFinalChe() {
		return saldoFinalChe;
	}
	public void setSaldoFinalChe(Double saldoFinalChe) {
		this.saldoFinalChe = saldoFinalChe;
	}
	public Double getSaldoFinalTar() {
		return saldoFinalTar;
	}
	public void setSaldoFinalTar(Double saldoFinalTar) {
		this.saldoFinalTar = saldoFinalTar;
	}
	
}
