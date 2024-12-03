package com.bisontecfacturacion.security.educacion.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Matriculacion {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaInicio;
	
	private Double montoMatricula;
	private Double montoCuotaTotal;
	private String referenciaProcedencia;
	private String estado;
	private Double costoMatriculaCarrera;
	private Double costoMensualCarrera;
	private Double costoAumentoAnualCarrera;
	private Double costoDerechoExamenCarrera;
	private int duracion;

	
	@ManyToOne
    private Carrera carrera;
	@ManyToOne
    private Procedencia procedencia;
	@ManyToOne
    private Turno turno;
	@ManyToOne
    private Alumno alumno;
	@ManyToOne
    private Funcionario funcionario;
	
	@JsonIgnoreProperties("matriculacion")
	@OneToMany(mappedBy="matriculacion")
	private List<MatriculacionDetalle> matriculacionDetalles; 
	
	public Matriculacion() {
		this.id=0;
		this.fechaRegistro = LocalDateTime.now();
		this.montoMatricula=0.0;
		this.montoCuotaTotal=0.0;
		this.referenciaProcedencia="";
		this.estado="";
		this.costoMatriculaCarrera=0.0;
		this.costoMensualCarrera=0.0;
		this.costoAumentoAnualCarrera=0.0;
		this.costoDerechoExamenCarrera=0.0;  
		this.duracion=0;
		
		this.carrera=new Carrera();
		this.alumno=new Alumno();
		this.funcionario=new  Funcionario();
		this.turno=new Turno();
		this.procedencia= new Procedencia();
		
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

	

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Double getMontoMatricula() {
		return montoMatricula;
	}

	public void setMontoMatricula(Double montoMatricula) {
		this.montoMatricula = montoMatricula;
	}

	public Double getMontoCuotaTotal() {
		return montoCuotaTotal;
	}

	public void setMontoCuotaTotal(Double montoCuotaTotal) {
		this.montoCuotaTotal = montoCuotaTotal;
	}

	public String getReferenciaProcedencia() {
		return referenciaProcedencia;
	}

	public void setReferenciaProcedencia(String referenciaProcedencia) {
		this.referenciaProcedencia = referenciaProcedencia;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Double getCostoMatriculaCarrera() {
		return costoMatriculaCarrera;
	}

	public void setCostoMatriculaCarrera(Double costoMatriculaCarrera) {
		this.costoMatriculaCarrera = costoMatriculaCarrera;
	}

	public Double getCostoMensualCarrera() {
		return costoMensualCarrera;
	}

	public void setCostoMensualCarrera(Double costoMensualCarrera) {
		this.costoMensualCarrera = costoMensualCarrera;
	}

	public Double getCostoAumentoAnualCarrera() {
		return costoAumentoAnualCarrera;
	}

	public void setCostoAumentoAnualCarrera(Double costoAumentoAnualCarrera) {
		this.costoAumentoAnualCarrera = costoAumentoAnualCarrera;
	}

	public Double getCostoDerechoExamenCarrera() {
		return costoDerechoExamenCarrera;
	}

	public void setCostoDerechoExamenCarrera(Double costoDerechoExamenCarrera) {
		this.costoDerechoExamenCarrera = costoDerechoExamenCarrera;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public Carrera getCarrera() {
		return carrera;
	}

	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}

	public Procedencia getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(Procedencia procedencia) {
		this.procedencia = procedencia;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<MatriculacionDetalle> getMatriculacionDetalles() {
		return matriculacionDetalles;
	}

	public void setMatriculacionDetalles(List<MatriculacionDetalle> matriculacionDetalles) {
		this.matriculacionDetalles = matriculacionDetalles;
	}



	
	
	
}
