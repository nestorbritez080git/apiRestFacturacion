package com.bisontecfacturacion.security.hoteleria.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CuentaCobrarDetalleReservaciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private int numeroCuota;
	@NotNull
	private Double monto;
	private Double importe;
	private Double subTotal;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaVencimiento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaPago;

	private Double interesMora;
	@NotNull
	private boolean estado;

	@ManyToOne
	private CuentaCobrarCabeceraReservaciones cuentaCobrarCabeceraReservaciones;
	
	public CuentaCobrarDetalleReservaciones() {
		this.id=0;this.numeroCuota=0;
		this.monto=0.0;
		this.importe=0.0;
		this.subTotal=0.0;
		this.interesMora=0.0;
		this.estado=false;
		this.cuentaCobrarCabeceraReservaciones=new CuentaCobrarCabeceraReservaciones();
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

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	

	
	public Double getInteresMora() {
		return interesMora;
	}

	public void setInteresMora(Double interesMora) {
		this.interesMora = interesMora;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public CuentaCobrarCabeceraReservaciones getCuentaCobrarCabeceraReservaciones() {
		return cuentaCobrarCabeceraReservaciones;
	}

	public void setCuentaCobrarCabeceraReservaciones(CuentaCobrarCabeceraReservaciones cuentaCobrarCabeceraReservaciones) {
		this.cuentaCobrarCabeceraReservaciones = cuentaCobrarCabeceraReservaciones;
	}
	
}
