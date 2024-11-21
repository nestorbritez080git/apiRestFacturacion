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
public class NotaCredito {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@NotNull
	private Double total;
	private String totalLetra;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Cliente cliente;
	

	
	@ManyToOne
	private DevolucionVenta devolucionVenta;
	
	public NotaCredito() {
		this.id=0;
		this.fecha=new  Date();
		this.hora="";
		this.total=0.0;
		this.totalLetra="";
		this.funcionario= new Funcionario();
		this.cliente = new Cliente();
		this.devolucionVenta = new DevolucionVenta();
		
		
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
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getTotalLetra() {
		return totalLetra;
	}
	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
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
	public DevolucionVenta getDevolucionVenta() {
		return devolucionVenta;
	}
	public void setDevolucionVenta(DevolucionVenta devolucionVenta) {
		this.devolucionVenta = devolucionVenta;
	}
}
