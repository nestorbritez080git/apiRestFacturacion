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
public class PagosProveedorCompra {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NonNull
	private String  documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFactura;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private TipoOperacion tipoOperacion;
	@ManyToOne
	private Proveedor proveedor;
	
	@NotNull
	private double monto;
	@NotNull
	private double montoCheque;
	@NotNull
	private double montoTarjeta;
	
	@NotNull
	private int operacionCaja;
	
	public PagosProveedorCompra() {
		this.id=0;
		this.proveedor= new Proveedor();
		this.funcionario= new Funcionario();
		this.tipoOperacion = new TipoOperacion();
		this.documento="";
		this.fecha= new Date();
		this.fechaFactura= new Date();
		this.monto=0.0;
		this.montoCheque=0.0;
		this.montoTarjeta=0.0;
		this.operacionCaja=0;
	}
	
	
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}


	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public int getOperacionCaja() {
		return operacionCaja;
	}


	public void setOperacionCaja(int operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Proveedor getProveedor() {
		return proveedor;
	}
	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public double getMontoCheque() {
		return montoCheque;
	}
	public void setMontoCheque(double montoCheque) {
		this.montoCheque = montoCheque;
	}
	public double getMontoTarjeta() {
		return montoTarjeta;
	}
	public void setMontoTarjeta(double montoTarjeta) {
		this.montoTarjeta = montoTarjeta;
	}
	
}
