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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class OrdenPagare {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaVencimiento;
	private Double total;
	private String totalLetra;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private CuentaCobrarCabecera cuentaCobrarCabecera;
	@ManyToOne
	private Funcionario funcionario;
	private String estado;

	
	

    public OrdenPagare(){
            this.id=0;
            this.fecha=new Date();
            this.fechaVencimiento= new Date();
            this.total=0.0;
    		this.totalLetra="";
            this.estado= "PENDIENTE";
            this.cliente= new Cliente();
            this.funcionario= new Funcionario();
            this.cuentaCobrarCabecera= new CuentaCobrarCabecera();
    }




	public CuentaCobrarCabecera getCuentaCobrarCabecera() {
		return cuentaCobrarCabecera;
	}




	public void setCuentaCobrarCabecera(CuentaCobrarCabecera cuentaCobrarCabecera) {
		this.cuentaCobrarCabecera = cuentaCobrarCabecera;
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




	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}




	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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




	public Cliente getCliente() {
		return cliente;
	}




	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}




	public Funcionario getFuncionario() {
		return funcionario;
	}




	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}




	public String getEstado() {
		return estado;
	}




	public void setEstado(String estado) {
		this.estado = estado;
	}

    
}
	
	
	