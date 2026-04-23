package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
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
public class OperacionCajaCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	private Double monto;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Concepto concepto;
	@ManyToOne
	private AperturaCaja aperturaCaja;
	
	private String motivo;
	private Integer referenciaOperacion;
	private String estado;
	private String tipo;
	
	
	@JsonIgnoreProperties("operacionCajaCabecera")
	@OneToMany(mappedBy="operacionCajaCabecera")
	private List<OperacionCaja> operacionCajas; 
	public OperacionCajaCabecera() {
		this.id=0;
		this.estado="ACTIVO";
		this.monto=0.00;
		this.fecha=new Date();
		this.concepto=new Concepto();
		this.aperturaCaja= new AperturaCaja(); 
		this.motivo="";
		this.referenciaOperacion=0;
		this.tipo = "";
		this.operacionCajas = new ArrayList<>();
		
	}
	
	

	public List<OperacionCaja> getOperacionCajas() {
		return operacionCajas;
	}



	public void setOperacionCajas(List<OperacionCaja> operacionCajas) {
		this.operacionCajas = operacionCajas;
	}



	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public AperturaCaja getAperturaCaja() {
		return aperturaCaja;
	}

	public void setAperturaCaja(AperturaCaja aperturaCaja) {
		this.aperturaCaja = aperturaCaja;
	}
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Integer getReferenciaOperacion() {
		return referenciaOperacion;
	}

	public void setReferenciaOperacion(Integer referenciaOperacion) {
		this.referenciaOperacion = referenciaOperacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	
}
