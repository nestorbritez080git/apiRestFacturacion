package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class OrdenProduccionDetalle {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private Double precioUnitario;
	@NotNull
	private Double cantidad;
	@NotNull
	private Double subTotal;
	@NotNull
	private String descripcionIngrediente;
	@NotNull
	private String descripcionUnidadMedida;
	
	@ManyToOne
	private Producto producto;
	
	@ManyToOne
	private UnidadMedida unidadMedida;
	
	@ManyToOne
	private OrdenProduccion ordenProduccion;
	
	public OrdenProduccionDetalle() {
		// TODO Auto-generated constructor stub
		this.ordenProduccion = new OrdenProduccion();
		
		this.unidadMedida= new UnidadMedida();
		
		this.producto = new  Producto();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescripcionIngrediente() {
		return descripcionIngrediente;
	}

	public void setDescripcionIngrediente(String descripcionIngrediente) {
		this.descripcionIngrediente = descripcionIngrediente;
	}

	public String getDescripcionUnidadMedida() {
		return descripcionUnidadMedida;
	}

	public void setDescripcionUnidadMedida(String descripcionUnidadMedida) {
		this.descripcionUnidadMedida = descripcionUnidadMedida;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public OrdenProduccion getOrdenProduccion() {
		return ordenProduccion;
	}

	public void setOrdenProduccion(OrdenProduccion ordenProduccion) {
		this.ordenProduccion = ordenProduccion;
	}
	
	
}
