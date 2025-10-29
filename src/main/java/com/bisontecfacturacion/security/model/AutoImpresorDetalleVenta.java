package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class AutoImpresorDetalleVenta {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@ManyToOne
	private Venta venta;
	@ManyToOne
	private AutoImpresor autoImpresor;
	@NotNull
	private String numeroFactura;
	private Integer terminalGrabado;
	private String estado;
	private String estadoSincronizacion;
	public AutoImpresorDetalleVenta() {
		this.id=0;
		this.fecha= LocalDateTime.now();
		this.autoImpresor= new AutoImpresor();
		this.venta= new Venta();
		this.numeroFactura="";
		this.terminalGrabado=0;
		this.estado="ACTIVO";
		this.estadoSincronizacion="PENDIENTE";
	}
	


	public String getEstadoSincronizacion() {
		return estadoSincronizacion;
	}



	public void setEstadoSincronizacion(String estadoSincronizacion) {
		this.estadoSincronizacion = estadoSincronizacion;
	}



	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getTerminalGrabado() {
		return terminalGrabado;
	}
	public void setTerminalGrabado(Integer terminalGrabado) {
		this.terminalGrabado = terminalGrabado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public AutoImpresor getAutoImpresor() {
		return autoImpresor;
	}
	public void setAutoImpresor(AutoImpresor autoImpresor) {
		this.autoImpresor = autoImpresor;
	}
	public String getNumeroFactura() {
		return numeroFactura;
	}
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
}
