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
public class EntregaProduccion {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@NotNull
	private Double cantidadProduccion;
	@NotNull
	private Double costoTotalManoObra;
	@NotNull
	private Double costoTotalFabricacion;
	@NotNull
	private Double costoTotalMateriaPrima;
	private Boolean estado;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Funcionario funcionarioEntrega;
	@ManyToOne
	private OrdenProduccion ordenProduccion;
	
	public EntregaProduccion() {
		this.funcionario= new Funcionario();
		this.funcionarioEntrega= new Funcionario();
		this.ordenProduccion = new OrdenProduccion();
		this.costoTotalManoObra=0.0;
		this.costoTotalFabricacion=0.0;
		this.cantidadProduccion = 0.0;
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

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Double getCantidadProduccion() {
		return cantidadProduccion;
	}

	public void setCantidadProduccion(Double cantidadProduccion) {
		this.cantidadProduccion = cantidadProduccion;
	}

	public Double getCostoTotalManoObra() {
		return costoTotalManoObra;
	}

	public void setCostoTotalManoObra(Double costoTotalManoObra) {
		this.costoTotalManoObra = costoTotalManoObra;
	}

	public Double getCostoTotalFabricacion() {
		return costoTotalFabricacion;
	}

	public void setCostoTotalFabricacion(Double costoTotalFabricacion) {
		this.costoTotalFabricacion = costoTotalFabricacion;
	}

	public Double getCostoTotalMateriaPrima() {
		return costoTotalMateriaPrima;
	}

	public void setCostoTotalMateriaPrima(Double costoTotalMateriaPrima) {
		this.costoTotalMateriaPrima = costoTotalMateriaPrima;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionarioEntrega() {
		return funcionarioEntrega;
	}

	public void setFuncionarioEntrega(Funcionario funcionarioEntrega) {
		this.funcionarioEntrega = funcionarioEntrega;
	}

	public OrdenProduccion getOrdenProduccion() {
		return ordenProduccion;
	}

	public void setOrdenProduccion(OrdenProduccion ordenProduccion) {
		this.ordenProduccion = ordenProduccion;
	}
	
}
