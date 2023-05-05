package com.bisontecfacturacion.security.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

	@Entity
public class AperturaCaja {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private double saldoInicial;
	@NotNull
	private double saldoActual;
	@NotNull
	private double saldoInicialCheque;
	@NotNull
	private double saldoActualCheque;
	@NotNull
	private double saldoInicialTarjeta;
	@NotNull
	private double saldoActualTarjeta;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NotNull
	private String hora;
	@NotNull
	private boolean estado;
	@NotNull
	private boolean estadoAnulacion;
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Caja caja;
	@OneToMany(mappedBy="aperturaCaja")
	@JsonBackReference
	private List<OperacionCaja> operacionCajas;
	public AperturaCaja() {
		this.id=0;
		this.saldoInicial=0.00;
		this.saldoActual=0.00;
		this.saldoInicialCheque=0.00;
		this.saldoActualCheque=0.00;
		this.saldoInicialTarjeta=0.00;
		this.saldoActualTarjeta=0.00;
		this.fecha=new Date();
		this.hora="";
		this.estado=true;
		this.estadoAnulacion=false;
		this.funcionario= new Funcionario();
		this.caja= new Caja();
	}
	
	public boolean isEstadoAnulacion() {
		return estadoAnulacion;
	}

	public void setEstadoAnulacion(boolean estadoAnulacion) {
		this.estadoAnulacion = estadoAnulacion;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSaldoInicial() {
		return saldoInicial;
	}
	public void setSaldoInicial(double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}
	public double getSaldoActual() {
		return saldoActual;
	}
	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Caja getCaja() {
		return caja;
	}
	public void setCaja(Caja caja) {
		this.caja = caja;
	}
	public double getSaldoInicialCheque() {
		return saldoInicialCheque;
	}
	public void setSaldoInicialCheque(double saldoInicialCheque) {
		this.saldoInicialCheque = saldoInicialCheque;
	}
	public double getSaldoActualCheque() {
		return saldoActualCheque;
	}
	public void setSaldoActualCheque(double saldoActualCheque) {
		this.saldoActualCheque = saldoActualCheque;
	}
	public double getSaldoInicialTarjeta() {
		return saldoInicialTarjeta;
	}
	public void setSaldoInicialTarjeta(double saldoInicialTarjeta) {
		this.saldoInicialTarjeta = saldoInicialTarjeta;
	}
	public double getSaldoActualTarjeta() {
		return saldoActualTarjeta;
	}
	public void setSaldoActualTarjeta(double saldoActualTarjeta) {
		this.saldoActualTarjeta = saldoActualTarjeta;
	}
	
	
}
