package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class TransferenciaAperturaCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private AperturaCaja aperturaCajaOrigen;
	@ManyToOne
	private AperturaCaja aperturaCajaDestino;
	@NotNull 
	private Double monto;
	@NotNull
	private Double montoCheque;
	@NotNull
	private Double montoTarjeta;
	
	private String referencia;
	@ManyToOne
	private Concepto concepto;
	@ManyToOne
	private OperacionCaja operacionCaja;
	
	
	
	public TransferenciaAperturaCaja() {
		this.id=0;
		this.fecha= new Date();
		this.funcionario= new Funcionario();
		this.aperturaCajaOrigen = new AperturaCaja();
		this.aperturaCajaDestino = new AperturaCaja();
		this.concepto= new Concepto();
		this.operacionCaja= new OperacionCaja();
		this.monto=0.0;
		this.montoCheque=0.0;
		this.montoTarjeta=0.0;
		this.referencia="";
	}
	

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}


	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	public Concepto getConcepto() {
		return concepto;
	}


	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public AperturaCaja getAperturaCajaOrigen() {
		return aperturaCajaOrigen;
	}

	public void setAperturaCajaOrigen(AperturaCaja aperturaCajaOrigen) {
		this.aperturaCajaOrigen = aperturaCajaOrigen;
	}

	public AperturaCaja getAperturaCajaDestino() {
		return aperturaCajaDestino;
	}

	public void setAperturaCajaDestino(AperturaCaja aperturaCajaDestino) {
		this.aperturaCajaDestino = aperturaCajaDestino;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getMontoCheque() {
		return montoCheque;
	}

	public void setMontoCheque(Double montoCheque) {
		this.montoCheque = montoCheque;
	}

	public Double getMontoTarjeta() {
		return montoTarjeta;
	}

	public void setMontoTarjeta(Double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	
}
