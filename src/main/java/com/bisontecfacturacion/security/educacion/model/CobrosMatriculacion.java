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
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class CobrosMatriculacion {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaCobro;
	
	private Double importe;
	

	
	@ManyToOne
    private Matriculacion matriculacion;
	@ManyToOne
    private Alumno alumno;
	@ManyToOne
    private Funcionario funcionario;
	@ManyToOne
    private OperacionCaja operacionCaja;
	@ManyToOne
    private ConceptoMatriculacion conceptoMatriculacion;
	
	public CobrosMatriculacion() {
		this.id=0;
		this.fechaRegistro= LocalDateTime.now();
		this.fechaCobro=new Date();
		this.importe=0.0;
		this.matriculacion=new Matriculacion();
		this.alumno=new Alumno();
		this.funcionario=new Funcionario();
		this.operacionCaja=new OperacionCaja();
		this.conceptoMatriculacion= new ConceptoMatriculacion();
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

	public Date getFechaCobro() {
		return fechaCobro;
	}

	public void setFechaCobro(Date fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Matriculacion getMatriculacion() {
		return matriculacion;
	}

	public void setMatriculacion(Matriculacion matriculacion) {
		this.matriculacion = matriculacion;
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

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}

	public ConceptoMatriculacion getConceptoMatriculacion() {
		return conceptoMatriculacion;
	}

	public void setConceptoMatriculacion(ConceptoMatriculacion conceptoMatriculacion) {
		this.conceptoMatriculacion = conceptoMatriculacion;
	}

	
}
