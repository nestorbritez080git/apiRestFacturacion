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
public class CuentaCobrarDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private int numeroCuota;
	@NotNull
	private double monto;
	private double importe;
	private double subTotal;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaVencimiento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaPago;
	private double interesMora;
	@NotNull
	private boolean estado;

	@ManyToOne
	private CuentaCobrarCabecera cuentaCobrarCabecera;

	public CuentaCobrarDetalle() {
		super();
		id=0;
		monto=0.0;
		interesMora= 0.0;
		importe= 0.0;
		fechaVencimiento=new Date();
		numeroCuota=0;
		estado=false;
		cuentaCobrarCabecera= new CuentaCobrarCabecera();
		subTotal=0.0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(int numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public double getInteresMora() {
		return interesMora;
	}

	public void setInteresMora(double interesMora) {
		this.interesMora = interesMora;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public CuentaCobrarCabecera getCuentaCobrarCabecera() {
		return cuentaCobrarCabecera;
	}

	public void setCuentaCobrarCabecera(CuentaCobrarCabecera cuentaCobrarCabecera) {
		this.cuentaCobrarCabecera = cuentaCobrarCabecera;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal; 
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	
}
