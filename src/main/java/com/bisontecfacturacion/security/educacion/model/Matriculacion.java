package com.bisontecfacturacion.security.educacion.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.poi.ss.formula.functions.Now;
import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Persona;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Matriculacion {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	private String estado;
	private Double costo;
	@ManyToOne
    private Carrera carrera;
	@ManyToOne
    private Alumnos alumnos;
	@ManyToOne
    private Funcionario funcionario;
	
	public Matriculacion() {
		this.id=0;
		this.fecha=LocalDateTime.now();
		this.costo=0.0;
		this.estado="";
		this.carrera=new Carrera();
		this.alumnos=new Alumnos();
		this.funcionario=new  Funcionario();
		
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Carrera getCarrera() {
		return carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public Alumnos getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(Alumnos alumnos) {
		this.alumnos = alumnos;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	
}
