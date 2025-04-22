package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CuentaCobrarCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaVencimiento;
	
	private double total;
	private String totalLetra;
	private double pagado;
	private double saldo;
	private int fraccionCuota;
	private boolean estado;
	private double entrega;
	@ManyToOne
	private TipoPlazo tipoPlazo;
	@ManyToOne
	private Concepto concepto;
	@ManyToOne
	private Venta venta;
	@ManyToOne
	private InteresCuota interesCuota;
	@ManyToOne
	private InteresMora interesMora;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Cliente cliente;

	@OneToMany(mappedBy="cuentaCobrarCabecera")
	private List<CuentaCobrarDetalle> cuentaCobrarDetalle; 
		
	public CuentaCobrarCabecera() {
		super();
		id=0;
		total=0.0;
		totalLetra="";
		fecha=new Date();
		fechaVencimiento=new Date();

		pagado=0.0;
		saldo=0.0;
		fraccionCuota=0;
		estado= false;
		entrega= 0.0;
		concepto= new Concepto();
		tipoPlazo= new TipoPlazo();
		interesCuota = new InteresCuota();
		interesMora = new InteresMora();
		funcionario = new Funcionario();
		venta=new Venta();
		cliente = new Cliente();
		cuentaCobrarDetalle= new ArrayList<>();
		
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
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

	public double getTotal() {
		return total;
	}

	public String getTotalLetra() {
		return totalLetra;
	}

	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPagado() {
		return pagado;
	}

	public void setPagado(double pagado) {
		this.pagado = pagado;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
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

	public double getEntrega() {
		return entrega;
	}

	public void setEntrega(double entrega) {
		this.entrega = entrega;
	}

	public TipoPlazo getTipoPlazo() {
		return tipoPlazo;
	}

	public void setTipoPlazo(TipoPlazo tipoPlazo) {
		this.tipoPlazo = tipoPlazo;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public InteresCuota getInteresCuota() {
		return interesCuota;
	}

	public void setInteresCuota(InteresCuota interesCuota) {
		this.interesCuota = interesCuota;
	}

	public InteresMora getInteresMora() {
		return interesMora;
	}

	public void setInteresMora(InteresMora interesMora) {
		this.interesMora = interesMora;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<CuentaCobrarDetalle> getCuentaCobrarDetalle() {
		return cuentaCobrarDetalle;
	}

	public void setCuentaCobrarDetalle(List<CuentaCobrarDetalle> cuentaCobrarDetalle) {
		this.cuentaCobrarDetalle = cuentaCobrarDetalle;
	}



	
}
