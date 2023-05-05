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
public class TransferenciaTesoreria {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Tesoreria tesoreria;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private CajaMayor cajaMayor;
	@NotNull
	private Double monto;
	@NotNull
	private Double montoCheque;
	@NotNull
	private Double montoTarjeta;
	public TransferenciaTesoreria() {
		this.id=0;
		this.fecha= new Date();
		this.funcionario = new Funcionario();
		this.tesoreria = new Tesoreria();
		this.cajaMayor= new CajaMayor();
		this.monto=0.0;
		this.montoCheque=0.0;
		this.montoTarjeta=0.0;
	}
	
	public CajaMayor getCajaMayor() {
		return cajaMayor;
	}

	public void setCajaMayor(CajaMayor cajaMayor) {
		this.cajaMayor = cajaMayor;
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
	public Tesoreria getTesoreria() {
		return tesoreria;
	}
	public void setTesoreria(Tesoreria tesoreria) {
		this.tesoreria = tesoreria;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
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
	
}
