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
public class TransferenciaCajaMayor {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@ManyToOne
	private Funcionario funcionarioT;
	
	@ManyToOne
	private CajaMayor cajaMayor;
	@ManyToOne
	private CajaChica cajaChica;
	@NotNull
	private Double monto;
	@NotNull
	private Double montoCheque;
	@NotNull
	private Double montoTarjeta;
	
	public TransferenciaCajaMayor() {
		this.id=0;
		this.fecha= new Date();
		this.funcionarioT= new Funcionario();
		this.cajaMayor = new CajaMayor();
		this.cajaChica = new CajaChica();
		this.monto=0.0;
		this.montoCheque=0.0;
		this.montoTarjeta=0.0;
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

	public Funcionario getFuncionarioT() {
		return funcionarioT;
	}

	public void setFuncionarioT(Funcionario funcionarioT) {
		this.funcionarioT = funcionarioT;
	}

	public CajaMayor getCajaMayor() {
		return cajaMayor;
	}

	public void setCajaMayor(CajaMayor cajaMayor) {
		this.cajaMayor = cajaMayor;
	}

	public CajaChica getCajaChica() {
		return cajaChica;
	}

	public void setCajaChica(CajaChica cajaChica) {
		this.cajaChica = cajaChica;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Double getMontoCheque() {
		return montoCheque;
	}

	public void setMontoCheque(Double montoCheque) {
		this.montoCheque = montoCheque;
	}

	public Double getMontoTarjeta() {
		return montoTarjeta;
	}

	public void setMontoTarjeta(Double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}

	

	
	
}
