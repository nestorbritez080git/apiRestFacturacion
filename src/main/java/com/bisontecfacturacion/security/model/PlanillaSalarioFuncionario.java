package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
@Entity
public class PlanillaSalarioFuncionario {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Funcionario funcionario;
	@NotNull
	private Double total;
	private String planilla;
	
	
	@OneToMany(mappedBy="planillaSalarioFuncionario")
	private List<PlanillaSalarioFuncionarioDetalle> planillaSalarioFuncionarioDetalles; 
	
	public PlanillaSalarioFuncionario() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.funcionario=new Funcionario();
		this.fecha= new Date();
		this.total=0.0;
		this.planillaSalarioFuncionarioDetalles = new ArrayList<PlanillaSalarioFuncionarioDetalle>();
		this.planilla ="";

	}
	
	public String getPlanilla() {
		return planilla;
	}

	public void setPlanilla(String planilla) {
		this.planilla = planilla;
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
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public List<PlanillaSalarioFuncionarioDetalle> getPlanillaSalarioFuncionarioDetalles() {
		return planillaSalarioFuncionarioDetalles;
	}
	public void setPlanillaSalarioFuncionarioDetalles(
			List<PlanillaSalarioFuncionarioDetalle> planillaSalarioFuncionarioDetalles) {
		this.planillaSalarioFuncionarioDetalles = planillaSalarioFuncionarioDetalles;
	}
	
	
}
