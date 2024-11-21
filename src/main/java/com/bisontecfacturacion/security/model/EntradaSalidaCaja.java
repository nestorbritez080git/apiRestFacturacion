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
public class EntradaSalidaCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private TipoOperacion tipoOperacion;
	@ManyToOne
	private TipoMovimiento tipoMovimiento;
	@ManyToOne
	private Funcionario funcionario;
	@NotNull
	private Double monto;
	private String hora;
	@ManyToOne
	private OperacionCaja operacionCaja;
	
	
	private String motivo;
	private String referencia;
	public EntradaSalidaCaja() {
		this.funcionario= new Funcionario();
		this.tipoMovimiento= new TipoMovimiento();
		this.tipoOperacion= new TipoOperacion();
		this.operacionCaja= new OperacionCaja();
		this.monto=0.0;
		this.motivo="";
		this.fecha= new Date();
		this.id=0;
		this.referencia="";
		this.hora="";
	}

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}


	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	public String getReferencia() {
		return referencia;
	}


	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}




	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
