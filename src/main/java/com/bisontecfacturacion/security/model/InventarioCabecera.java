package com.bisontecfacturacion.security.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class InventarioCabecera {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFin;
	private Double totalCantidadCosto;
	private Double totalExistenciaCosto;
	private Double totalDiferenciaCosto;
	@Column(length = 100, nullable = true)
	private String periodo;
	private int itemCantidad;
	private Boolean estado;
	@ManyToOne
	private Funcionario funcionarioR;
	
	@ManyToOne
	private Funcionario funcionarioA;
	
	@ManyToOne
	private TipoInventario tipoInventario;

	@OneToMany(mappedBy="inventarioCabecera")
	@JsonBackReference
	private List<InventarioDetalle> inventarioDetalle; 
	
	public InventarioCabecera() {
		super();
		this.totalDiferenciaCosto=0.0;
		this.totalCantidadCosto=0.0;
		this.totalExistenciaCosto=0.0;
		this.itemCantidad=0;
		this.funcionarioR = new Funcionario();
		this.funcionarioA = new Funcionario();
		this.tipoInventario = new TipoInventario();
		this.periodo = "";
		this.estado=false;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Double getTotalCantidadCosto() {
		return totalCantidadCosto;
	}

	public void setTotalCantidadCosto(Double totalCantidadCosto) {
		this.totalCantidadCosto = totalCantidadCosto;
	}

	public Double getTotalExistenciaCosto() {
		return totalExistenciaCosto;
	}

	public void setTotalExistenciaCosto(Double totalExistenciaCosto) {
		this.totalExistenciaCosto = totalExistenciaCosto;
	}

	public Double getTotalDiferenciaCosto() {
		return totalDiferenciaCosto;
	}

	public void setTotalDiferenciaCosto(Double totalDiferenciaCosto) {
		this.totalDiferenciaCosto = totalDiferenciaCosto;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public int getItemCantidad() {
		return itemCantidad;
	}

	public void setItemCantidad(int itemCantidad) {
		this.itemCantidad = itemCantidad;
	}

	public Funcionario getFuncionarioR() {
		return funcionarioR;
	}

	public void setFuncionarioR(Funcionario funcionarioR) {
		this.funcionarioR = funcionarioR;
	}

	public Funcionario getFuncionarioA() {
		return funcionarioA;
	}

	public void setFuncionarioA(Funcionario funcionarioA) {
		this.funcionarioA = funcionarioA;
	}

	public TipoInventario getTipoInventario() {
		return tipoInventario;
	}

	public void setTipoInventario(TipoInventario tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

	public List<InventarioDetalle> getInventarioDetalle() {
		return inventarioDetalle;
	}

	public void setInventarioDetalle(List<InventarioDetalle> inventarioDetalle) {
		this.inventarioDetalle = inventarioDetalle;
	}

	
}
