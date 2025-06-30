package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "asiento_contable")
public class AsientoContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaModificacion;
	
	@Column(name = "tipo_referencia", length = 50)
	private String tipoReferencia; // Ej: "VENTA", "COMPRA", etc.

	@Column(name = "id_referencia")
	private Integer idReferencia;// Ej: "VENTA_CONTADO", "COMPRA_CREDITO", etc.
	@ManyToOne()
	private Concepto concepto;
	@ManyToOne()
	private Funcionario funcionarioRegistro;
	@ManyToOne()
	private Funcionario funcionarioModificacion;
	@Column(name = "creado_en")
	private LocalDateTime creadoEn = LocalDateTime.now();
	@OneToMany(mappedBy = "asientoContable", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AsientoContableDetalle> detalles = new ArrayList<>();
	private BigDecimal totalDebe;
	private BigDecimal totalHaber;
    public AsientoContable() {
    	this.detalles= new ArrayList<AsientoContableDetalle>();
    	this.concepto= new Concepto();
    	this.funcionarioRegistro = new Funcionario();
    	this.funcionarioModificacion = new Funcionario();
    	this.totalDebe=BigDecimal.ZERO;
    	this.totalHaber=BigDecimal.ZERO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	
	public String getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public Integer getIdReferencia() {
		return idReferencia;
	}

	public void setIdReferencia(Integer idReferencia) {
		this.idReferencia = idReferencia;
	}

	public LocalDateTime getCreadoEn() {
		return creadoEn;
	}

	public void setCreadoEn(LocalDateTime creadoEn) {
		this.creadoEn = creadoEn;
	}

	public List<AsientoContableDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<AsientoContableDetalle> detalles) {
		this.detalles = detalles;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}

	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}

	public Funcionario getFuncionarioModificacion() {
		return funcionarioModificacion;
	}

	public void setFuncionarioModificacion(Funcionario funcionarioModificacion) {
		this.funcionarioModificacion = funcionarioModificacion;
	}

	public BigDecimal getTotalDebe() {
		return totalDebe;
	}

	public void setTotalDebe(BigDecimal totalDebe) {
		this.totalDebe = totalDebe;
	}

	public BigDecimal getTotalHaber() {
		return totalHaber;
	}

	public void setTotalHaber(BigDecimal totalHaber) {
		this.totalHaber = totalHaber;
	}

	
	
  
}
