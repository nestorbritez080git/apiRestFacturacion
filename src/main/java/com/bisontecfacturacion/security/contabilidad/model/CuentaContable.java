package com.bisontecfacturacion.security.contabilidad.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuenta_contable")
public class CuentaContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String codigo;

    @Column(length = 100, nullable = false)
    private String nombre;

    @ManyToOne()
    private TipoCuentaContable tipoCuentaContable;

    @Column(name = "es_movimiento")
    private Boolean esMovimiento = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private CuentaContable padre;

    private Boolean estado = true;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    
    public CuentaContable() {
		// TODO Auto-generated constructor stub
    	this.tipoCuentaContable = new TipoCuentaContable();
	}
    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

  
    public TipoCuentaContable getTipoCuentaContable() {
		return tipoCuentaContable;
	}

	public void setTipoCuentaContable(TipoCuentaContable tipoCuentaContable) {
		this.tipoCuentaContable = tipoCuentaContable;
	}

	public Boolean getEsMovimiento() {
        return esMovimiento;
    }

    public void setEsMovimiento(Boolean esMovimiento) {
        this.esMovimiento = esMovimiento;
    }

    public CuentaContable getPadre() {
        return padre;
    }

    public void setPadre(CuentaContable padre) {
        this.padre = padre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
