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
public class DevolucionVenta {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFactura;
	@NotNull
	private Double total;
	private String hora;
	private String estado;
	@ManyToOne
	private Venta venta;
	
	@ManyToOne
	private Funcionario funcionario;
	private Double totalIvaCinco;
	private Double totalIvaDies;
	private String totalLetra;
	private Double totalExcenta;
	@NotNull
	private int numeroOperacion;
	
	@OneToMany(mappedBy="devolucionVenta")
	private List<DevolucionVentaDetalle> devolucionVentaDetalle; 
	
	@ManyToOne
	private TipoDevolucion tipoDevolucion;
	
	public DevolucionVenta() {
		this.id=0;
		this.fecha= new Date();
		this.fechaFactura= new Date();
		this.total=0.0;
		this.hora="";
		this.estado="";
		this.venta= new Venta();
		this.funcionario= new Funcionario();
		this.totalIvaCinco =0.0;
		this.totalIvaDies=0.0;
		this.totalExcenta=0.0;
		this.totalLetra="";
		this.numeroOperacion=0;
		this.devolucionVentaDetalle= new ArrayList<DevolucionVentaDetalle>();
		this.tipoDevolucion= new TipoDevolucion();
	}

	public TipoDevolucion getTipoDevolucion() {
		return tipoDevolucion;
	}

	public void setTipoDevolucion(TipoDevolucion tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<DevolucionVentaDetalle> getDevolucionVentaDetalle() {
		return devolucionVentaDetalle;
	}

	public void setDevolucionVentaDetalle(List<DevolucionVentaDetalle> devolucionVentaDetalle) {
		this.devolucionVentaDetalle = devolucionVentaDetalle;
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

	public Date getFechaFactura() {
		return fechaFactura;
	}

	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
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

	public String getTotalLetra() {
		return totalLetra;
	}

	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
	}

	public Double getTotalExcenta() {
		return totalExcenta;
	}

	public void setTotalExcenta(Double totalExcenta) {
		this.totalExcenta = totalExcenta;
	}

	public int getNumeroOperacion() {
		return numeroOperacion;
	}

	public void setNumeroOperacion(int numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	

}
