package com.bisontecfacturacion.security.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class AutoImpresor {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDate fechaInicioVigencia;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDate fechaFinVigencia;
	@NotNull
	private String timbrado;
	@NotNull
	private String numeroAutorizacion;
	@NotNull
	private String codigoEstablecimiento;
	@NotNull
	private String puntoExpedicion;
	@NotNull
	private int cantidadExpedicion;
	@NotNull
	private int rangoInicio;
	@NotNull
	private int rangoFin;
	@NotNull
	private int numeroActual;
	
	@NotNull
	private boolean estado;
	private String ruc;
	private String selloDigital;
	@ManyToOne
	private AutoImpresorTipoRemision autoImpresorTipoRemision;
	
	@OneToMany(mappedBy = "autoImpresor", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("autoImpresor")
	@OrderBy("id DESC")
	private List<AutoImpresorDetalleVenta> autoImpresorDetalleVentas; 
	
	@ManyToOne
	private Funcionario funcionario;
	public AutoImpresor() {
		this.funcionario= new Funcionario();
		this.fecha= LocalDateTime.now();
		this.id=0;
		this.estado=false;
		this.ruc="";
		this.selloDigital="";
		this.autoImpresorTipoRemision= new AutoImpresorTipoRemision();
		this.autoImpresorDetalleVentas = new ArrayList<AutoImpresorDetalleVenta>();
	}
	
	public List<AutoImpresorDetalleVenta> getAutoImpresorDetalleVentas() {
		return autoImpresorDetalleVentas;
	}

	public void setAutoImpresorDetalleVentas(List<AutoImpresorDetalleVenta> autoImpresorDetalleVentas) {
		this.autoImpresorDetalleVentas = autoImpresorDetalleVentas;
	}

	public LocalDate getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	public void setFechaInicioVigencia(LocalDate fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	public LocalDate getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	public void setFechaFinVigencia(LocalDate fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getSelloDigital() {
		return selloDigital;
	}

	public void setSelloDigital(String selloDigital) {
		this.selloDigital = selloDigital;
	}

	public AutoImpresorTipoRemision getAutoImpresorTipoRemision() {
		return autoImpresorTipoRemision;
	}

	public void setAutoImpresorTipoRemision(AutoImpresorTipoRemision autoImpresorTipoRemision) {
		this.autoImpresorTipoRemision = autoImpresorTipoRemision;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
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
	public String getTimbrado() {
		return timbrado;
	}
	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	public String getCodigoEstablecimiento() {
		return codigoEstablecimiento;
	}
	public void setCodigoEstablecimiento(String codigoEstablecimiento) {
		this.codigoEstablecimiento = codigoEstablecimiento;
	}
	public String getPuntoExpedicion() {
		return puntoExpedicion;
	}
	public void setPuntoExpedicion(String puntoExpedicion) {
		this.puntoExpedicion = puntoExpedicion;
	}
	public int getCantidadExpedicion() {
		return cantidadExpedicion;
	}
	public void setCantidadExpedicion(int cantidadExpedicion) {
		this.cantidadExpedicion = cantidadExpedicion;
	}
	public int getRangoInicio() {
		return rangoInicio;
	}
	public void setRangoInicio(int rangoInicio) {
		this.rangoInicio = rangoInicio;
	}
	public int getRangoFin() {
		return rangoFin;
	}
	public void setRangoFin(int rangoFin) {
		this.rangoFin = rangoFin;
	}
	public int getNumeroActual() {
		return numeroActual;
	}
	public void setNumeroActual(int numeroActual) {
		this.numeroActual = numeroActual;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	
}
