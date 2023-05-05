package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class ProduccionManoObra {
	
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
	
	/*
	@ManyToOne
	private Funcionario funcionario;
	
	@ManyToOne
	private ProduccionCostoCabecera produccionCostoCabecera;
	
	public ProduccionManoObra() {
		this.funcionario= new Funcionario();
		this.produccionCostoCabecera= new ProduccionCostoCabecera();
		
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
/*
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public ProduccionCostoCabecera getProduccionCostoCabecera() {
		return produccionCostoCabecera;
	}

	public void setProduccionCostoCabecera(ProduccionCostoCabecera produccionCostoCabecera) {
		this.produccionCostoCabecera = produccionCostoCabecera;
	}
	
	*/
}
