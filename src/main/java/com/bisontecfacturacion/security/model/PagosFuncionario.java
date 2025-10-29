package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;
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
public class PagosFuncionario {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime periodoInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime periodoFin;
	@NotNull
	private Double importeBruto;
	private Double descuentoLiquidacion;
	@NotNull
	private Double importeNeto;
	private String obs;
	
	private String estado;
	
	private String tipo;

	
	@ManyToOne
	private TipoPago tipoPago;
	@ManyToOne
	private Funcionario funcionarioRegistro;
	@ManyToOne
	private Funcionario funcionarioPago;
	@ManyToOne
	private Concepto  concepto;
	
	@ManyToOne
	private TipoOperacion  TipoOperacion;
	
	@JsonIgnoreProperties("pagosFuncionario")
	@OneToMany(mappedBy="pagosFuncionario")
	private List<PagosFuncionarioDetalle> pagosFuncionarioDetalles;
	
	
	public PagosFuncionario() {
		this.id=0;
		this.fecha= LocalDateTime.now();
		this.periodoInicio= LocalDateTime.now();
		this.periodoFin= LocalDateTime.now();
		this.importeBruto=0.0;
		this.descuentoLiquidacion=0.0;
		this.importeNeto=0.0;
		this.obs="";
		this.estado="";
		this.tipo="";
		this.tipoPago= new TipoPago();
		this.TipoOperacion= new  TipoOperacion();
		this.concepto= new Concepto();
		this.funcionarioRegistro=new Funcionario();
		this.funcionarioPago=new Funcionario();
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
	public LocalDateTime getPeriodoInicio() {
		return periodoInicio;
	}
	public void setPeriodoInicio(LocalDateTime periodoInicio) {
		this.periodoInicio = periodoInicio;
	}
	public LocalDateTime getPeriodoFin() {
		return periodoFin;
	}
	public void setPeriodoFin(LocalDateTime periodoFin) {
		this.periodoFin = periodoFin;
	}
	public Double getImporteBruto() {
		return importeBruto;
	}
	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
	}
	public Double getDescuentoLiquidacion() {
		return descuentoLiquidacion;
	}
	public void setDescuentoLiquidacion(Double descuentoLiquidacion) {
		this.descuentoLiquidacion = descuentoLiquidacion;
	}
	public Double getImporteNeto() {
		return importeNeto;
	}
	public void setImporteNeto(Double importeNeto) {
		this.importeNeto = importeNeto;
	}
	public String getObs() {
		return obs;
	}
	
	public String getEstado() {
		return estado;
	}
	
	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}
	
	public Concepto getConcepto() {
		return concepto;
	}
	

	public TipoOperacion getTipoOperacion() {
		return TipoOperacion;
	}

	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		TipoOperacion = tipoOperacion;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}
	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}
	public Funcionario getFuncionarioPago() {
		return funcionarioPago;
	}
	public void setFuncionarioPago(Funcionario funcionarioPago) {
		this.funcionarioPago = funcionarioPago;
	}

	public List<PagosFuncionarioDetalle> getPagosFuncionarioDetalles() {
		return pagosFuncionarioDetalles;
	}

	public void setPagosFuncionarioDetalles(List<PagosFuncionarioDetalle> pagosFuncionarioDetalles) {
		this.pagosFuncionarioDetalles = pagosFuncionarioDetalles;
	}
	
	
	
}
