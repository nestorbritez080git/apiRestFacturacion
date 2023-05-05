package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class ProduccionFabricacion {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@NotNull
	private Double costoPromedioMensual;
	@NotNull
	private Double produccionPromedioMensual;
	@NotNull
	private Double costoUnitario;
	@NotNull
	private Double costoTotal;
	
	@NotNull
	private Double cantidadProduccion;
	
	@NotNull
	private Double descripcionCostoFabricacion;
	
	/*
	@ManyToOne
	private CostoIndirectoFacbricacion costoIndirectoFacbricacion;
	
	@ManyToOne
	private ProduccionCostoCabecera produccionCostoCabecera;

	public ProduccionFabricacion() {
		this.produccionCostoCabecera= new ProduccionCostoCabecera();
		this.costoIndirectoFacbricacion = new CostoIndirectoFacbricacion();
		
	}

	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getCostoPromedioMensual() {
		return costoPromedioMensual;
	}

	public void setCostoPromedioMensual(Double costoPromedioMensual) {
		this.costoPromedioMensual = costoPromedioMensual;
	}

	public Double getProduccionPromedioMensual() {
		return produccionPromedioMensual;
	}

	public void setProduccionPromedioMensual(Double produccionPromedioMensual) {
		this.produccionPromedioMensual = produccionPromedioMensual;
	}

	public Double getCostoUnitario() {
		return costoUnitario;
	}

	public void setCostoUnitario(Double costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public Double getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(Double costoTotal) {
		this.costoTotal = costoTotal;
	}

	public Double getCantidadProduccion() {
		return cantidadProduccion;
	}

	public void setCantidadProduccion(Double cantidadProduccion) {
		this.cantidadProduccion = cantidadProduccion;
	}

	public Double getDescripcionCostoFabricacion() {
		return descripcionCostoFabricacion;
	}

	public void setDescripcionCostoFabricacion(Double descripcionCostoFabricacion) {
		this.descripcionCostoFabricacion = descripcionCostoFabricacion;
	}

	/*
	public CostoIndirectoFacbricacion getCostoIndirectoFacbricacion() {
		return costoIndirectoFacbricacion;
	}

	public void setCostoIndirectoFacbricacion(CostoIndirectoFacbricacion costoIndirectoFacbricacion) {
		this.costoIndirectoFacbricacion = costoIndirectoFacbricacion;
	}

	public ProduccionCostoCabecera getProduccionCostoCabecera() {
		return produccionCostoCabecera;
	}

	public void setProduccionCostoCabecera(ProduccionCostoCabecera produccionCostoCabecera) {
		this.produccionCostoCabecera = produccionCostoCabecera;
	}
	*/
}
