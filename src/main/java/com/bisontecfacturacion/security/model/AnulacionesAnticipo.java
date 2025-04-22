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
public class AnulacionesAnticipo {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Concepto concepto;
	@ManyToOne
	private TipoOperacion TipoOperacion;
	@ManyToOne
	private Anticipo anticipo;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NotNull
	private String motivo;
	
	
	public AnulacionesAnticipo() {
		this.id=0;
		this.funcionario= new  Funcionario();
		this.concepto= new  Concepto();
		this.anticipo= new Anticipo();
		this.TipoOperacion= new TipoOperacion();
		this.fecha= new Date();
		this.motivo="";
		
	}
	
	public TipoOperacion getTipoOperacion() {
		return TipoOperacion;
	}

	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		TipoOperacion = tipoOperacion;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Anticipo getAnticipo() {
		return anticipo;
	}
	public void setAnticipo(Anticipo anticipo) {
		this.anticipo = anticipo;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public Concepto getConcepto() {
		return concepto;
	}
	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}
	
	

}
