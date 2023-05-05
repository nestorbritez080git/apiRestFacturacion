package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CierreCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private double monto;
	@NotNull
	private double montoCheque;
	@NotNull
	private double montoTarjeta;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NotNull
	private String hora;

	@NotNull
	private double imputacionEgreso;
	@NotNull
	private double imputacionIngreso;
	@NotNull
	private double imputacionEgresoCheque;
	@NotNull
	private double imputacionEgresoTarjeta;
	@NotNull
	private double imputacionIngresoCheque;
	@NotNull
	private double imputacionIngresoTarjeta;
	@NotNull
	private boolean estadoEntrega;//Estado entregado del cierre del cajero a cargo
	@NotNull
	private boolean estadoRecibo;//Estado recibido del cierre en la tesoreria
	@NotNull
	private boolean estadoAnulacion;
	@ManyToOne
	private Funcionario funcionario;

	@ManyToOne
	@JoinColumn(name="apertura_caja_id", nullable=true, insertable=true, updatable=true)
	private AperturaCaja aperturaCaja;

	public CierreCaja() {
		this.id=0;
		this.monto=0;
		this.montoCheque=0;
		this.montoTarjeta=0;
		this.fecha= new Date();
		this.hora="";
		this.imputacionEgreso=0.00;
		this.imputacionIngreso=0.00;
		this.imputacionEgresoCheque=0.00;
		this.imputacionEgresoTarjeta=0.00;
		this.imputacionIngresoCheque=0.00;
		this.imputacionIngresoTarjeta=0.00;
		this.estadoEntrega=false;
		this.estadoRecibo=false;
		this.estadoAnulacion=false;
		this.funcionario=new Funcionario();
		this.aperturaCaja=new AperturaCaja();
	}

	public CierreCaja(@NotNull double monto, @NotNull double montoCheque, @NotNull double montoTarjeta) {
		super();
		
		this.monto = monto;
		this.montoCheque = montoCheque;
		this.montoTarjeta = montoTarjeta;
	
	}

	public boolean isEstadoAnulacion() {
		return estadoAnulacion;
	}

	public void setEstadoAnulacion(boolean estadoAnulacion) {
		this.estadoAnulacion = estadoAnulacion;
	}

	public double getImputacionEgresoCheque() {
		return imputacionEgresoCheque;
	}

	public void setImputacionEgresoCheque(double imputacionEgresoCheque) {
		this.imputacionEgresoCheque = imputacionEgresoCheque;
	}

	public double getImputacionEgresoTarjeta() {
		return imputacionEgresoTarjeta;
	}

	public void setImputacionEgresoTarjeta(double imputacionEgresoTarjeta) {
		this.imputacionEgresoTarjeta = imputacionEgresoTarjeta;
	}

	public double getImputacionIngresoCheque() {
		return imputacionIngresoCheque;
	}

	public void setImputacionIngresoCheque(double imputacionIngresoCheque) {
		this.imputacionIngresoCheque = imputacionIngresoCheque;
	}

	public double getImputacionIngresoTarjeta() {
		return imputacionIngresoTarjeta;
	}

	public void setImputacionIngresoTarjeta(double imputacionIngresoTarjeta) {
		this.imputacionIngresoTarjeta = imputacionIngresoTarjeta;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public double getImputacionEgreso() {
		return imputacionEgreso;
	}

	public void setImputacionEgreso(double imputacionEgreso) {
		this.imputacionEgreso = imputacionEgreso;
	}

	public double getImputacionIngreso() {
		return imputacionIngreso;
	}

	public void setImputacionIngreso(double imputacionIngreso) {
		this.imputacionIngreso = imputacionIngreso;
	}

	public boolean isEstadoEntrega() {
		return estadoEntrega;
	}

	public void setEstadoEntrega(boolean estadoEntrega) {
		this.estadoEntrega = estadoEntrega;
	}

	public boolean isEstadoRecibo() {
		return estadoRecibo;
	}

	public void setEstadoRecibo(boolean estadoRecibo) {
		this.estadoRecibo = estadoRecibo;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public AperturaCaja getAperturaCaja() {
		return aperturaCaja;
	}

	public void setAperturaCaja(AperturaCaja aperturaCaja) {
		this.aperturaCaja = aperturaCaja;
	}

	public double getMontoCheque() {
		return montoCheque;
	}

	public void setMontoCheque(double montoCheque) {
		this.montoCheque = montoCheque;
	}

	public double getMontoTarjeta() {
		return montoTarjeta;
	}

	public void setMontoTarjeta(double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}
	
	
}
