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
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class PagosProveedorCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NonNull
	private Double total;
	@ManyToOne
	private Proveedor proveedor;
	@JsonIgnoreProperties("compra")
	@OneToMany(mappedBy="pagosProveedorCabecera")
	private List<PagosProveedor> pagosProveedors;
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
	private Concepto concepto;
	
	@ManyToOne
	private TipoOperacion tipoOperacion;
	private String referencia;
	private String tipo;

	
	public PagosProveedorCabecera() {
		this.id=0;
		this.total=0.0;		
		this.funcionarioA= new  Funcionario();
		this.funcionarioR= new  Funcionario();
		this.proveedor=new Proveedor();
		this.comprobante= "";
		this.fechaRegistro= new  Date();
		this.fechaPagos= new Date();
		this.concepto= new Concepto();
		this.tipoOperacion= new TipoOperacion();
		this.referencia="";
		this.tipo = "";
		this.pagosProveedors= new ArrayList<PagosProveedor>();
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public Proveedor getProveedor() {
		return proveedor;
	}


	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}


	public List<PagosProveedor> getPagosProveedors() {
		return pagosProveedors;
	}


	public void setPagosProveedors(List<PagosProveedor> pagosProveedors) {
		this.pagosProveedors = pagosProveedors;
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


	public Concepto getConcepto() {
		return concepto;
	}


	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
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


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	
	
}
