package com.bisontecfacturacion.security.model;

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
public class Presupuesto {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@NotNull
	private Double total;
	@NotNull
	private String nroDocumento;
	
	@OneToMany(mappedBy="presupuesto")
	private List<DetallePresupuestoProducto> detallePresupuestoProducto; 
	
	@OneToMany(mappedBy="presupuesto")
	private List<DetallePresupuestoServicio> detallePresupuestoServicio; 
	
	
	@ManyToOne
	private Funcionario funcionario;
	private String obs;

	
	
	@ManyToOne
	private Cliente cliente;

	private Double totalIvaCinco;
	private Double totalIvaDies;
	private Double totalIva;
	private String totalLetra;
	private Double totalExcenta;
	
	private String estado;
	
	public Presupuesto() {
		this.id=0;
		this.total=0.00;
		this.hora = "";
		this.fecha=new Date();
		this.funcionario= new Funcionario();
		this.cliente= new Cliente();
		this.nroDocumento="";
		this.totalIvaCinco=0.0;
		this.totalIvaDies=0.0;
		this.totalIva=0.0;
		this.totalLetra="";
		this.totalExcenta=0.0;
		this.estado="";
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


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
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


	public String getNroDocumento() {
		return nroDocumento;
	}


	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}


	public List<DetallePresupuestoProducto> getDetallePresupuestoProducto() {
		return detallePresupuestoProducto;
	}


	public void setDetallePresupuestoProducto(List<DetallePresupuestoProducto> detallePresupuestoProducto) {
		this.detallePresupuestoProducto = detallePresupuestoProducto;
	}


	public List<DetallePresupuestoServicio> getDetallePresupuestoServicio() {
		return detallePresupuestoServicio;
	}


	public void setDetallePresupuestoServicio(List<DetallePresupuestoServicio> detallePresupuestoServicio) {
		this.detallePresupuestoServicio = detallePresupuestoServicio;
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
	
	
}
