package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaUltimaGeneracion;
	

	@ManyToOne
	private CuentaCobrarCabecera cuentaCobrarCabecera;
	
	@OneToMany(mappedBy="cuentaCobrarDetalle")
	private List<CuentaCobrarMora> cuentaCobrarMora; 

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
		cuentaCobrarMora= new ArrayList<>();

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

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
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

	public Date getFechaUltimaGeneracion() {
		return fechaUltimaGeneracion;
	}

	public void setFechaUltimaGeneracion(Date fechaUltimaGeneracion) {
		this.fechaUltimaGeneracion = fechaUltimaGeneracion;
	}

	public CuentaCobrarCabecera getCuentaCobrarCabecera() {
		return cuentaCobrarCabecera;
	}

	public void setCuentaCobrarCabecera(CuentaCobrarCabecera cuentaCobrarCabecera) {
		this.cuentaCobrarCabecera = cuentaCobrarCabecera;
	}

	public List<CuentaCobrarMora> getCuentaCobrarMora() {
		return cuentaCobrarMora;
	}

	public void setCuentaCobrarMora(List<CuentaCobrarMora> cuentaCobrarMora) {
		this.cuentaCobrarMora = cuentaCobrarMora;
	}

	
}
