package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.bisontecfacturacion.security.model.Concepto;

@Entity
@Table(name = "concepto_cuenta_contable")
public class ConceptoCuentaContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    private Concepto concepto;
    @ManyToOne()
    private CuentaContable cuentaContable;
    
    @Column(length = 5, nullable = false)
    private String naturaleza;
    
    @Column(length = 200)
    private String base;

    @Column(precision = 5, scale = 2)
    private BigDecimal porcentaje = BigDecimal.valueOf(100);
   
    public ConceptoCuentaContable() {
		// TODO Auto-generated constructor stub
    	this.concepto = new Concepto();
    	this.cuentaContable = new CuentaContable();
	}

	public Long getId() {
		return id;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public CuentaContable getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(CuentaContable cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public BigDecimal getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}
    
  
}
