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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CuentaPagarCabecera{
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
    private Date fecha;
    private String hora;
    @NotNull
    private Double total;
    private Double pagado;
    private Double saldo;
    private double entrega;
    @NotNull
    private int fraccionCuota;
	private boolean estado;
	@ManyToOne
	private TipoPlazo tipoPlazo;
	@ManyToOne
	private Proveedor proveedor;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Compra compra;
	
	
	
	@OneToMany(mappedBy="cuentaPagarCabecera")
	private List<CuentaPagarDetalle> cuentaPagarDetalle; 
	
    public CuentaPagarCabecera(){
    	this.pagado = 0.0;
    	this.tipoPlazo= new TipoPlazo();
    	this.proveedor=new Proveedor();
    	this.funcionario= new Funcionario();
    	this.compra= new Compra();
    	
    	this.cuentaPagarDetalle = new  ArrayList<CuentaPagarDetalle>();
    	
    }



	public Compra getCompra() {
		return compra;
	}



	public void setCompra(Compra compra) {
		this.compra = compra;
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

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getPagado() {
		return pagado;
	}

	public void setPagado(Double pagado) {
		this.pagado = pagado;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public double getEntrega() {
		return entrega;
	}

	public void setEntrega(double entrega) {
		this.entrega = entrega;
	}

	public int getFraccionCuota() {
		return fraccionCuota;
	}

	public void setFraccionCuota(int fraccionCuota) {
		this.fraccionCuota = fraccionCuota;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	public TipoPlazo getTipoPlazo() {
		return tipoPlazo;
	}

	public void setTipoPlazo(TipoPlazo tipoPlazo) {
		this.tipoPlazo = tipoPlazo;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<CuentaPagarDetalle> getCuentaPagarDetalle() {
		return cuentaPagarDetalle;
	}

	public void setCuentaPagarDetalle(List<CuentaPagarDetalle> cuentaPagarDetalle) {
		this.cuentaPagarDetalle = cuentaPagarDetalle;
	}


	
}