package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class CobrosClienteCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NonNull
	private double total;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Cliente cliente;
	@JsonIgnoreProperties("venta")
	@OneToMany(mappedBy="cobrosClienteCabecera")
	private List<CobrosCliente> cobrosClientes;
	
    public CobrosClienteCabecera() {
		this.id=0;
		this.fecha= new Date();
		this.total=0.0;
		this.funcionario = new Funcionario();
		this.cliente = new Cliente();
		this.cobrosClientes = new ArrayList<CobrosCliente>();
		

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
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<CobrosCliente> getCobrosClientes() {
		return cobrosClientes;
	}
	public void setCobrosClientes(List<CobrosCliente> cobrosClientes) {
		this.cobrosClientes = cobrosClientes;
	}
    
	
}
