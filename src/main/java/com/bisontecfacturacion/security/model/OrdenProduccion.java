package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
public class OrdenProduccion {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaEntrega;
	private String hora;
	@NotNull
	private Double cantidad;
	@NotNull
	private Double cantidadEntregada;
	private Double costoTotalProduccion;
	
	@NotNull
	@Column(name = "estado")
	private String estado;
	
	@ManyToOne
	private ProduccionCostoCabecera produccionCostoCabecera;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Funcionario funcionarioA;
	
	
	@OneToMany(mappedBy="ordenProduccion")
	@JsonIgnoreProperties("ordenProduccion")
	private List<OrdenProduccionDetalle> ordenProduccionDetalles;
	
	public OrdenProduccion() {
		this.cantidad = 0.0;
		this.produccionCostoCabecera= new ProduccionCostoCabecera();
		this.funcionario= new Funcionario();
		this.funcionarioA= new Funcionario();
		this.ordenProduccionDetalles = new ArrayList<OrdenProduccionDetalle>();
		this.costoTotalProduccion = 0.0;
		this.estado = "PENDIENTE";
	}

	
	public List<OrdenProduccionDetalle> getOrdenProduccionDetalles() {
		return ordenProduccionDetalles;
	}


	public void setOrdenProduccionDetalles(List<OrdenProduccionDetalle> ordenProduccionDetalles) {
		this.ordenProduccionDetalles = ordenProduccionDetalles;
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

	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	
	public Double getCostoTotalProduccion() {
		return costoTotalProduccion;
	}


	public void setCostoTotalProduccion(Double costoTotalProduccion) {
		this.costoTotalProduccion = costoTotalProduccion;
	}


	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getCantidadEntregada() {
		return cantidadEntregada;
	}

	public void setCantidadEntregada(Double cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public ProduccionCostoCabecera getProduccionCostoCabecera() {
		return produccionCostoCabecera;
	}

	public void setProduccionCostoCabecera(ProduccionCostoCabecera produccionCostoCabecera) {
		this.produccionCostoCabecera = produccionCostoCabecera;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionarioA() {
		return funcionarioA;
	}

	public void setFuncionarioA(Funcionario funcionarioA) {
		this.funcionarioA = funcionarioA;
	}




}
