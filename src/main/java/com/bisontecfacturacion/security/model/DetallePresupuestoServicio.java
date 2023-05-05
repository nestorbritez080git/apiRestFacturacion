package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class DetallePresupuestoServicio {
	
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
		private int id;

		@ManyToOne
		private Servicio servicio;
		
		@NotNull
		private String descripcion;
		
		@NotNull
		private Double cantidad;
		
		@NotNull
		private Double subTotal;
		@NotNull
		private Double precio;
		
		private Double montoIva;
		
		private String iva;
		private String obs;
		@ManyToOne
		private Funcionario funcionario;
		@ManyToOne
		private Presupuesto presupuesto;

		public DetallePresupuestoServicio() {
			super();
			id=0;
			presupuesto=new Presupuesto();
			servicio=new Servicio();
			descripcion="";
			cantidad=0.0;
			subTotal=0.0;
			precio=0.0;
			iva="10 %";
			montoIva=0.0;
			obs="";
			this.funcionario= new Funcionario();
		}

		public String getObs() {
			return obs;
		}

		public void setObs(String obs) {
			this.obs = obs;
		}

		public Funcionario getFuncionario() {
			return funcionario;
		}

		public void setFuncionario(Funcionario funcionario) {
			this.funcionario = funcionario;
		}

		public Double getMontoIva() {
			return montoIva;
		}

		public void setMontoIva(Double montoIva) {
			this.montoIva = montoIva;
		}

		public String getIva() {
			return iva;
		}

		public void setIva(String iva) {
			this.iva = iva;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public Servicio getServicio() {
			return servicio;
		}

		public void setServicio(Servicio servicio) {
			this.servicio = servicio;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
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

		public Double getPrecio() {
			return precio;
		}

		public void setPrecio(Double precio) {
			this.precio = precio;
		}

		public Presupuesto getPresupuesto() {
			return presupuesto;
		}

		public void setPresupuesto(Presupuesto presupuesto) {
			this.presupuesto = presupuesto;
		}
		
		
}
