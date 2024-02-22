package com.bisontecfacturacion.security.auxiliar;

import java.util.Date;
import java.util.List;

import com.bisontecfacturacion.security.model.Funcionario;

public class InformeBalanceReservacionAuxiliar {
	private Funcionario funcionarioEncargado;
	private Date fechaInicial;
	private Date fechaFinal;
	
	private List<MovimientoCajaAuxiliar> movimientoCajaAuxiliar;
	private List<MovimientoGastosAuxiliar> movimientoGastosAuxiliar;
	private List<MovimientoPorConceptosAuxiliar> movimientoPorConceptosAuxiliar;
	public Funcionario getFuncionarioEncargado() {
		return funcionarioEncargado;
	}
	public void setFuncionarioEncargado(Funcionario funcionarioEncargado) {
		this.funcionarioEncargado = funcionarioEncargado;
	}
	public Date getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public List<MovimientoCajaAuxiliar> getMovimientoCajaAuxiliar() {
		return movimientoCajaAuxiliar;
	}
	public void setMovimientoCajaAuxiliar(List<MovimientoCajaAuxiliar> movimientoCajaAuxiliar) {
		this.movimientoCajaAuxiliar = movimientoCajaAuxiliar;
	}
	public List<MovimientoGastosAuxiliar> getMovimientoGastosAuxiliar() {
		return movimientoGastosAuxiliar;
	}
	public void setMovimientoGastosAuxiliar(List<MovimientoGastosAuxiliar> movimientoGastosAuxiliar) {
		this.movimientoGastosAuxiliar = movimientoGastosAuxiliar;
	}
	public List<MovimientoPorConceptosAuxiliar> getMovimientoPorConceptosAuxiliar() {
		return movimientoPorConceptosAuxiliar;
	}
	public void setMovimientoPorConceptosAuxiliar(List<MovimientoPorConceptosAuxiliar> movimientoPorConceptosAuxiliar) {
		this.movimientoPorConceptosAuxiliar = movimientoPorConceptosAuxiliar;
	}
	
}
