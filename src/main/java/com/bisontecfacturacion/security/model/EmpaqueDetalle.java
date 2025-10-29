package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class EmpaqueDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonIgnoreProperties("empaqueCabecera")
	@ManyToOne
	private EmpaqueCabecera empaqueCabecera;
	@ManyToOne
	private Presupuesto presupuesto;
	private Double subtotalPresupuesto;
	private Double subtotalVenta;
	private Integer itemsVentaDetalle;

	private int itemsPedidoDetalle;

	private int ventaReferencia;
	private String condicion;
	private String estado;
	@ManyToOne
	private Documento documento;

	@ManyToOne(optional = true)   // 👈 permite null
	@JoinColumn(name = "venta_id", nullable = true)
	private Venta venta;
	public EmpaqueDetalle() {
		this.id=0;
		this.presupuesto= new Presupuesto();
		this.empaqueCabecera = new EmpaqueCabecera();
		this.subtotalPresupuesto =0.0;
		this.itemsPedidoDetalle=0;
		this.ventaReferencia=0;
		this.condicion="1";
		this.estado="PENDIENTE";
		this.documento = new Documento();
		this.subtotalVenta=0.0;
		this.itemsVentaDetalle=0;
	}
	
	
	public Double getSubtotalVenta() {
		return subtotalVenta;
	}


	public void setSubtotalVenta(Double subtotalVenta) {
		this.subtotalVenta = subtotalVenta;
	}




	public Integer getItemsVentaDetalle() {
		return itemsVentaDetalle;
	}


	public void setItemsVentaDetalle(Integer itemsVentaDetalle) {
		this.itemsVentaDetalle = itemsVentaDetalle;
	}


	public Documento getDocumento() {
		return documento;
	}
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}
	public String getEstado() {
		return estado;
	}





	public void setEstado(String estado) {
		this.estado = estado;
	}





	public String getCondicion() {
		return condicion;
	}





	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}





	public Venta getVenta() {
		return venta;
	}





	public void setVenta(Venta venta) {
		this.venta = venta;
	}





	public int getVentaReferencia() {
		return ventaReferencia;
	}



	public void setVentaReferencia(int ventaReferencia) {
		this.ventaReferencia = ventaReferencia;
	}



	public int getItemsPedidoDetalle() {
		return itemsPedidoDetalle;
	}



	public void setItemsPedidoDetalle(int itemsPedidoDetalle) {
		this.itemsPedidoDetalle = itemsPedidoDetalle;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EmpaqueCabecera getEmpaqueCabecera() {
		return empaqueCabecera;
	}

	public void setEmpaqueCabecera(EmpaqueCabecera empaqueCabecera) {
		this.empaqueCabecera = empaqueCabecera;
	}

	public Presupuesto getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}

	public Double getSubtotalPresupuesto() {
		return subtotalPresupuesto;
	}

	public void setSubtotalPresupuesto(Double subtotalPresupuesto) {
		this.subtotalPresupuesto = subtotalPresupuesto;
	}

	
	
}
