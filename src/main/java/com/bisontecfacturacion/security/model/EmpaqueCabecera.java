package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class EmpaqueCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaRegistro;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaEntrega;
	
	@ManyToOne
	private Funcionario funcionarioRegistro;
	@ManyToOne
	private Funcionario funcionarioEmpaque;
	@ManyToOne
	private Zona zona;
	private String estado;
	private String obs;
	@NotNull
	private Double total;
	@NotNull
	private String totalLetras;
	private Double totalDevolucion;
	@OneToMany(mappedBy="empaqueCabecera")
	@JsonIgnoreProperties("empaqueCabecera")
	private List<EmpaqueDetalle> empaqueDetalle;
	private int itemsPedido;

	
	public EmpaqueCabecera() {
		this.id=0;
		this.fechaRegistro= LocalDateTime.now();
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioEmpaque= new Funcionario();
		this.zona = new Zona();
		this.estado = "ABIERTO";
		this.obs="";
		this.total=0.0;
		this.totalLetras="";
		this.totalDevolucion=0.0;
		this.itemsPedido=0;
	}
	
	public int getItemsPedido() {
		return itemsPedido;
	}

	public void setItemsPedido(int itemsPedido) {
		this.itemsPedido = itemsPedido;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}
	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}
	public Funcionario getFuncionarioEmpaque() {
		return funcionarioEmpaque;
	}
	public void setFuncionarioEmpaque(Funcionario funcionarioEmpaque) {
		this.funcionarioEmpaque = funcionarioEmpaque;
	}
	public Zona getZona() {
		return zona;
	}
	public void setZona(Zona zona) {
		this.zona = zona;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getTotalLetras() {
		return totalLetras;
	}
	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}
	public Double getTotalDevolucion() {
		return totalDevolucion;
	}
	public void setTotalDevolucion(Double totalDevolucion) {
		this.totalDevolucion = totalDevolucion;
	}

	public List<EmpaqueDetalle> getEmpaqueDetalle() {
		return empaqueDetalle;
	}

	public void setEmpaqueDetalle(List<EmpaqueDetalle> empaqueDetalle) {
		this.empaqueDetalle = empaqueDetalle;
	}
	
	

}
