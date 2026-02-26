package com.bisontecfacturacion.security.auxiliar;

import java.util.Date;
import java.util.List;

import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Zona;

public class InformeCierreEmpaqueAuxiliar {
	
	private Integer idEmpaque;
	private String funcionarioEncargado;
	private String funcionarioRepartidor;
	private String zona;
	private Integer itemPedido;
	private Double totalPedido;
	private Integer itemVenta;
	private Double totalVenta;
	private Double totalDevolucion;
	
	private Double ventaContado;
	private Double ventaCredito;
	private Double totalEfectivo;
	private Double totalCheque;
	private Double totalTarjeta;
	private Date fechaEntrega;
	public Integer getIdEmpaque() {
		return idEmpaque;
	}
	public void setIdEmpaque(Integer idEmpaque) {
		this.idEmpaque = idEmpaque;
	}
	public String getFuncionarioEncargado() {
		return funcionarioEncargado;
	}
	public void setFuncionarioEncargado(String funcionarioEncargado) {
		this.funcionarioEncargado = funcionarioEncargado;
	}
	public String getFuncionarioRepartidor() {
		return funcionarioRepartidor;
	}
	public void setFuncionarioRepartidor(String funcionarioRepartidor) {
		this.funcionarioRepartidor = funcionarioRepartidor;
	}
	public String getZona() {
		return zona;
	}
	public void setZona(String zona) {
		this.zona = zona;
	}
	public Integer getItemPedido() {
		return itemPedido;
	}
	public void setItemPedido(Integer itemPedido) {
		this.itemPedido = itemPedido;
	}
	public Double getTotalPedido() {
		return totalPedido;
	}
	public void setTotalPedido(Double totalPedido) {
		this.totalPedido = totalPedido;
	}
	public Integer getItemVenta() {
		return itemVenta;
	}
	public void setItemVenta(Integer itemVenta) {
		this.itemVenta = itemVenta;
	}
	public Double getTotalVenta() {
		return totalVenta;
	}
	public void setTotalVenta(Double totalVenta) {
		this.totalVenta = totalVenta;
	}
	public Double getTotalDevolucion() {
		return totalDevolucion;
	}
	public void setTotalDevolucion(Double totalDevolucion) {
		this.totalDevolucion = totalDevolucion;
	}
	public Double getVentaContado() {
		return ventaContado;
	}
	public void setVentaContado(Double ventaContado) {
		this.ventaContado = ventaContado;
	}
	public Double getVentaCredito() {
		return ventaCredito;
	}
	public void setVentaCredito(Double ventaCredito) {
		this.ventaCredito = ventaCredito;
	}
	public Double getTotalEfectivo() {
		return totalEfectivo;
	}
	public void setTotalEfectivo(Double totalEfectivo) {
		this.totalEfectivo = totalEfectivo;
	}
	public Double getTotalCheque() {
		return totalCheque;
	}
	public void setTotalCheque(Double totalCheque) {
		this.totalCheque = totalCheque;
	}
	public Double getTotalTarjeta() {
		return totalTarjeta;
	}
	public void setTotalTarjeta(Double totalTarjeta) {
		this.totalTarjeta = totalTarjeta;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
}
