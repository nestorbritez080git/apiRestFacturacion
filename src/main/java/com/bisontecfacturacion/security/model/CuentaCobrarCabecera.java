package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	
	private Double total;
	private String totalLetra;
	private Double pagado;
	private Double saldo;
	private int fraccionCuota;
	private boolean estado;
	private Double entrega;
	private Double totalDevolucion;
	@ManyToOne
	private TipoPlazo tipoPlazo;
	@ManyToOne
	private Concepto concepto;
	@OneToOne
	@JoinColumn(name = "venta_id", unique = true)
	private Venta venta;
	@ManyToOne
	private InteresCuota interesCuota;
	private Integer porcentajeInteresCuota;
	@ManyToOne
	private InteresMora interesMora;
	private Integer porcentajeInteresMora;
	private Double totalInteresMora;

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
		totalDevolucion=0.0;
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
		porcentajeInteresCuota=0;
		porcentajeInteresMora=0;
		totalInteresMora =0.0;
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

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getTotalLetra() {
		return totalLetra;
	}

	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
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

	public Double getEntrega() {
		return entrega;
	}

	public void setEntrega(Double entrega) {
		this.entrega = entrega;
	}

	public Double getTotalDevolucion() {
		return totalDevolucion;
	}

	public void setTotalDevolucion(Double totalDevolucion) {
		this.totalDevolucion = totalDevolucion;
	}

	public TipoPlazo getTipoPlazo() {
		return tipoPlazo;
	}

	public void setTipoPlazo(TipoPlazo tipoPlazo) {
		this.tipoPlazo = tipoPlazo;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
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

	public Integer getPorcentajeInteresCuota() {
		return porcentajeInteresCuota;
	}

	public void setPorcentajeInteresCuota(Integer porcentajeInteresCuota) {
		this.porcentajeInteresCuota = porcentajeInteresCuota;
	}

	public Integer getPorcentajeInteresMora() {
		return porcentajeInteresMora;
	}

	public void setPorcentajeInteresMora(Integer porcentajeInteresMora) {
		this.porcentajeInteresMora = porcentajeInteresMora;
	}

	public Double getTotalInteresMora() {
		return totalInteresMora;
	}

	public void setTotalInteresMora(Double totalInteresMora) {
		this.totalInteresMora = totalInteresMora;
	}


	
}
