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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Venta {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String tipo;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NotNull
	private Double total;
	@NotNull
	private String nroDocumento;
	private String estado;
	private String timbrado;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoFin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFactura;
	
	private String hora;
	
	private String obs;
	@JsonIgnoreProperties("venta")
	@OneToMany(mappedBy="venta")
	private List<DetalleProducto> detalleProducto; 
	
	@OneToMany(mappedBy="venta")
	private List<DetalleServicios> detalleServicio; 
	
	@ManyToOne
	private Documento documento;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Funcionario funcionarioV;
	
	
	@ManyToOne
	private Cliente cliente;
	private int operacionCaja;
	private Double entrega;
	
	private Double totalDescuento;
	private Double totalIvaCinco;
	private Double totalIvaDies;
	private String totalLetra;
	private Double totalExcenta;
	private Double totalIva;

	
	public Venta() {
		this.id=0;
		this.tipo="";
		this.estado="FACTURAR";
		this.total=0.00;
		this.hora = "";
		this.timbrado = ""; 
		this.fecha=new Date();
		this.detalleServicio = new ArrayList<DetalleServicios>();
		this.detalleProducto = new ArrayList<DetalleProducto>();

		this.funcionarioV=new Funcionario();
		this.documento=new Documento();
		this.funcionario= new Funcionario();
		this.cliente= new Cliente();
		this.nroDocumento="";
		this.totalDescuento =0.0;
		this.operacionCaja=0;
		this.totalIvaCinco=0.0;
		this.totalIvaDies=0.0;
		this.totalExcenta=0.0;
		this.totalIva=0.0;
		this.totalLetra="";
		this.entrega=0.0;
		this.obs="";
	}
	

	public String getObs() {
		return obs;
	}


	public void setObs(String obs) {
		this.obs = obs;
	}


	public Double getTotalIva() {
		return totalIva;
	}


	public void setTotalIva(Double totalIva) {
		this.totalIva = totalIva;
	}


	public Double getEntrega() {
		return entrega;
	}


	public void setEntrega(Double entrega) {
		this.entrega = entrega;
	}


	public Double getTotalDescuento() {
		return totalDescuento;
	}


	public void setTotalDescuento(Double totalDescuento) {
		this.totalDescuento = totalDescuento;
	}


	public Double getTotalExcenta() {
		return totalExcenta;
	}


	public void setTotalExcenta(Double totalExcenta) {
		this.totalExcenta = totalExcenta;
	}


	public String getTotalLetra() {
		return totalLetra;
	}


	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
	}


	public Double getTotalIvaCinco() {
		return totalIvaCinco;
	}


	public void setTotalIvaCinco(Double totalIvaCinco) {
		this.totalIvaCinco = totalIvaCinco;
	}


	public Double getTotalIvaDies() {
		return totalIvaDies;
	}


	public void setTotalIvaDies(Double totalIvaDies) {
		this.totalIvaDies = totalIvaDies;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTimbrado() {
		return timbrado;
	}

	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}

	public Date getTimbradoInicio() {
		return timbradoInicio;
	}

	public void setTimbradoInicio(Date timbradoInicio) {
		this.timbradoInicio = timbradoInicio;
	}

	public Date getTimbradoFin() {
		return timbradoFin;
	}

	public void setTimbradoFin(Date timbradoFin) {
		this.timbradoFin = timbradoFin;
	}

	public List<DetalleProducto> getDetalleProducto() {
		return detalleProducto;
	}

	public void setDetalleProducto(List<DetalleProducto> detalleProducto) {
		this.detalleProducto = detalleProducto;
	}

	public List<DetalleServicios> getDetalleServicio() {
		return detalleServicio;
	}

	public void setDetalleServicio(List<DetalleServicios> detalleServicio) {
		this.detalleServicio = detalleServicio;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionarioV() {
		return funcionarioV;
	}

	public void setFuncionarioV(Funcionario funcionarioV) {
		this.funcionarioV = funcionarioV;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public int getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(int operacionCaja) {
		this.operacionCaja = operacionCaja;
	}

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	
	
}
