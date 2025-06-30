package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bisontecfacturacion.security.model.Concepto;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "detalle_asiento")
public class AsientoContableDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaRegistro;

    @Column(precision = 18, scale = 2)
    private BigDecimal debe = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal haber = BigDecimal.ZERO;

    private String descripcion;
    
 
    @ManyToOne
    @JoinColumn(name = "asiento_contable_id")
    private AsientoContable asientoContable;

    @ManyToOne()
    private CuentaContable cuentaContable;

    public AsientoContableDetalle() {
		// TODO Auto-generated constructor stub
    	this.asientoContable= new AsientoContable();
    	this.cuentaContable= new CuentaContable();
    	
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

	public BigDecimal getDebe() {
		return debe;
	}

	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}

	public BigDecimal getHaber() {
		return haber;
	}

	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public AsientoContable getAsientoContable() {
		return asientoContable;
	}

	public void setAsientoContable(AsientoContable asientoContable) {
		this.asientoContable = asientoContable;
	}

	public CuentaContable getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(CuentaContable cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	
  
}
