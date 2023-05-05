package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PagosProveedor {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private double importe;
	private double descuento;
	@NonNull
	private String  comprobante;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaPagos;
	@ManyToOne
	private Funcionario funcionarioA;
	@ManyToOne
	private Funcionario funcionarioR;
	@ManyToOne
	private CuentaPagarCabecera cuentaCabecera;
	
	@ManyToOne
	private Concepto concepto;
	
	@ManyToOne
	private TipoOperacion tipoOperacion;
	@ManyToOne
	private CajaMayor cajaMayor;
	
	private String referencia;
	
	public PagosProveedor() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.importe=0.0;
		this.descuento=0.0;
		this.comprobante="";
		this.fechaPagos=new Date();
		this.fechaRegistro= new Date();
		this.funcionarioA=new Funcionario();
		this.funcionarioR=new Funcionario();
		this.concepto = new Concepto();
		this.cuentaCabecera= new CuentaPagarCabecera();
		this.cajaMayor = new CajaMayor();
		this.referencia = "";
	}


	public CajaMayor getCajaMayor() {
		return cajaMayor;
	}


	public void setCajaMayor(CajaMayor cajaMayor) {
		this.cajaMayor = cajaMayor;
	}


	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}


	public Concepto getConcepto() {
		return concepto;
	}


	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public double getImporte() {
		return importe;
	}


	public void setImporte(double importe) {
		this.importe = importe;
	}


	public double getDescuento() {
		return descuento;
	}


	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}


	public String getComprobante() {
		return comprobante;
	}


	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}


	public Date getFechaRegistro() {
		return fechaRegistro;
	}


	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	public Date getFechaPagos() {
		return fechaPagos;
	}


	public void setFechaPagos(Date fechaPagos) {
		this.fechaPagos = fechaPagos;
	}


	public Funcionario getFuncionarioA() {
		return funcionarioA;
	}


	public void setFuncionarioA(Funcionario funcionarioA) {
		this.funcionarioA = funcionarioA;
	}


	public Funcionario getFuncionarioR() {
		return funcionarioR;
	}


	public void setFuncionarioR(Funcionario funcionarioR) {
		this.funcionarioR = funcionarioR;
	}


	public CuentaPagarCabecera getCuentaCabecera() {
		return cuentaCabecera;
	}


	public void setCuentaCabecera(CuentaPagarCabecera cuentaCabecera) {
		this.cuentaCabecera = cuentaCabecera;
	}
	
}
