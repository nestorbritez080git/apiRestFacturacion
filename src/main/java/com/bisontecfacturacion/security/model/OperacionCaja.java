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
public class OperacionCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String tipo;
	@NotNull
	private String motivo;
	@NotNull
	private double monto;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Concepto concepto;
	@ManyToOne
	private AperturaCaja aperturaCaja;
	@ManyToOne
	private TipoOperacion tipoOperacion;
	private String referenciaTipoOperacion;
	private Double efectivo;
	private Double vuelto;
	
	public OperacionCaja() {
		this.id=0;
		this.tipo="";
		this.motivo="";
		this.monto=0.00;
		this.fecha=new Date();
		this.concepto=new Concepto();
		this.aperturaCaja= new AperturaCaja(); 
		this.tipoOperacion= new TipoOperacion();
		this.referenciaTipoOperacion="";
		this.vuelto=0.0;
		this.efectivo=0.0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Concepto getConcepto() {
		return concepto;
	}
	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}
	public AperturaCaja getAperturaCaja() {
		return aperturaCaja;
	}
	public void setAperturaCaja(AperturaCaja aperturaCaja) {
		this.aperturaCaja = aperturaCaja;
	}
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getReferenciaTipoOperacion() {
		return referenciaTipoOperacion;
	}
	public void setReferenciaTipoOperacion(String referenciaTipoOperacion) {
		this.referenciaTipoOperacion = referenciaTipoOperacion;
	}

	public Double getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Double efectivo) {
		this.efectivo = efectivo;
	}

	public Double getVuelto() {
		return vuelto;
	}

	public void setVuelto(Double vuelto) {
		this.vuelto = vuelto;
	}
}
