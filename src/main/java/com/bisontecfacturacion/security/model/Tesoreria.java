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
public class Tesoreria {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private double importe;
	@NotNull
	private double importeCheque;
	@NotNull
	private double importeTarjeta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	private boolean estado;
	private boolean estadoAnulacion;
	private boolean estadoTransferencia;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private CierreCaja cierreCaja; 
	public Tesoreria() {
		this.id=0;
		this.importe=0.00;
		this.importeCheque=0.00;
		this.importeTarjeta=0.00;
		this.fecha=new Date();
		this.hora="";
		this.estado=false;
		this.estadoAnulacion=false;
		this.estadoTransferencia=false;
		this.funcionario=new Funcionario();
		this.cierreCaja=new CierreCaja();
	}
	
	public boolean isEstadoTransferencia() {
		return estadoTransferencia;
	}

	public void setEstadoTransferencia(boolean estadoTransferencia) {
		this.estadoTransferencia = estadoTransferencia;
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
	public double getImporte() {
		return importe;
	}
	public void setImporte(double importe) {
		this.importe = importe;
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
	public CierreCaja getCierreCaja() {
		return cierreCaja;
	}
	public void setCierreCaja(CierreCaja cierreCaja) {
		this.cierreCaja = cierreCaja;
	}
	public double getImporteCheque() {
		return importeCheque;
	}
	public void setImporteCheque(double importeCheque) {
		this.importeCheque = importeCheque;
	}
	public double getImporteTarjeta() {
		return importeTarjeta;
	}
	public void setImporteTarjeta(double importeTarjeta) {
		this.importeTarjeta = importeTarjeta;
	}
	
}
