package com.bisontecfacturacion.security.model;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Compra {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String timbrado;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date timbradoFin;
	private String tipo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFactura;
	
	private String hora;
	private double total;
	private String nroDocumento;
	private String estado;

	private Double valorCotizacion;
	
	@OneToMany(mappedBy="compra")
	@JsonBackReference
	private List<DetalleCompra> detalleCompra; 
	
	private Double totalIvaCinco;
	private Double totalIvaDies;
	private Double totalExcenta;
	private String totalLetra;
	
	@ManyToOne
	private Proveedor proveedor;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private Documento documento;

	public Compra() {
		super();
		id=0;
		timbrado="";
		tipo="";
		nroDocumento="";
		fecha=new Date();
		estado="FACTURAR";
		total=0.0;
		hora="";
		proveedor=new Proveedor();
		funcionario=new Funcionario();
		documento = new Documento();
		this.totalIvaCinco=0.0;
		this.totalIvaDies=0.0;
		this.totalExcenta=0.0;
		totalLetra="";
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

	public Double getTotalExcenta() {
		return totalExcenta;
	}

	public void setTotalExcenta(Double totalExcenta) {
		this.totalExcenta = totalExcenta;
	}

	public int getId() {
		return id;
	}

	public String getTimbrado() {
		return timbrado;
	}

	public Date getTimbradoInicio() {
		return timbradoInicio;
	}

	public Date getTimbradoFin() {
		return timbradoFin;
	}

	public String getTipo() {
		return tipo;
	}

	public Date getFecha() {
		return fecha;
	}

	public double getTotal() {
		return total;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public String getEstado() {
		return estado;
	}

	public List<DetalleCompra> getDetalleCompra() {
		return detalleCompra;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTimbrado(String timbrado) {
		this.timbrado = timbrado;
	}

	public void setTimbradoInicio(Date timbradoInicio) {
		this.timbradoInicio = timbradoInicio;
	}

	public void setTimbradoFin(Date timbradoFin) {
		this.timbradoFin = timbradoFin;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public void setDetalleCompra(List<DetalleCompra> detalleCompra) {
		this.detalleCompra = detalleCompra;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public Double getValorCotizacion() {
		return valorCotizacion;
	}

	public void setValorCotizacion(Double valorCotizacion) {
		this.valorCotizacion = valorCotizacion;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	
}
