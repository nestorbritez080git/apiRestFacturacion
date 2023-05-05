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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ProduccionCostoCabecera {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@NotNull
	private String produccionDescripcion;
	@NotNull
	private Double cantidadProduccion;
	
	private Double costoTotalManoObra;
	
	private Double costoTotalFabricacion;
	@NotNull
	private Double costoTotalMateriaPrima;
	@NotNull
	private String codigoInterno;
	@NotNull
	private Boolean estado;
	
	@ManyToOne
	private Producto producto;
	
	@ManyToOne
	private Funcionario funcionario;
	
	@OneToMany(mappedBy="produccionCostoCabecera")
	@JsonBackReference
	private List<ProduccionMateriaPrima> produccionMateriaPrimas;
	/*
	@OneToMany(mappedBy="produccionCostoCabecera")
	@JsonBackReference
	private List<ProduccionManoObra> produccionManoObras;

	@OneToMany(mappedBy="produccionCostoCabecera")
	@JsonBackReference
	private List<ProduccionFabricacion> produccionFabricacions;
	*/
	public ProduccionCostoCabecera() {
		this.estado = false;
		this.producto= new Producto();
		this.funcionario= new Funcionario();
		this.costoTotalFabricacion=0.0;
		this.costoTotalManoObra=0.0;
		this.costoTotalMateriaPrima=0.0;
		this.produccionMateriaPrimas = new ArrayList<ProduccionMateriaPrima>();
		//this.produccionFabricacions = new ArrayList<ProduccionFabricacion>();
		//this.produccionManoObras = new ArrayList<ProduccionManoObra>();
		
		
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

	public String getProduccionDescripcion() {
		return produccionDescripcion;
	}

	public void setProduccionDescripcion(String produccionDescripcion) {
		this.produccionDescripcion = produccionDescripcion;
	}

	public Double getCantidadProduccion() {
		return cantidadProduccion;
	}

	public void setCantidadProduccion(Double cantidadProduccion) {
		this.cantidadProduccion = cantidadProduccion;
	}

	public Double getCostoTotalManoObra() {
		return costoTotalManoObra;
	}

	public void setCostoTotalManoObra(Double costoTotalManoObra) {
		this.costoTotalManoObra = costoTotalManoObra;
	}

	public Double getCostoTotalFabricacion() {
		return costoTotalFabricacion;
	}

	public void setCostoTotalFabricacion(Double costoTotalFabricacion) {
		this.costoTotalFabricacion = costoTotalFabricacion;
	}

	public Double getCostoTotalMateriaPrima() {
		return costoTotalMateriaPrima;
	}

	public void setCostoTotalMateriaPrima(Double costoTotalMateriaPrima) {
		this.costoTotalMateriaPrima = costoTotalMateriaPrima;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<ProduccionMateriaPrima> getProduccionMateriaPrimas() {
		return produccionMateriaPrimas;
	}

	public void setProduccionMateriaPrimas(List<ProduccionMateriaPrima> produccionMateriaPrimas) {
		this.produccionMateriaPrimas = produccionMateriaPrimas;
	}
/*
	public List<ProduccionManoObra> getProduccionManoObras() {
		return produccionManoObras;
	}

	public void setProduccionManoObras(List<ProduccionManoObra> produccionManoObras) {
		this.produccionManoObras = produccionManoObras;
	}

	public List<ProduccionFabricacion> getProduccionFabricacions() {
		return produccionFabricacions;
	}

	public void setProduccionFabricacions(List<ProduccionFabricacion> produccionFabricacions) {
		this.produccionFabricacions = produccionFabricacions;
	}
*/
	
	
	
}
