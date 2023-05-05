package com.bisontecfacturacion.security.contabilidad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class PlanCuentaCodigo {
		@Id
		@GeneratedValue(generator = "increment")
		@GenericGenerator(name = "increment", strategy = "increment")
		private int id;
		
		@Column(length = 100)
		private String cuenta;
		private String descripcion;
		private boolean asentable;
		private int nivel;
		private int integradora;
		

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getCuenta() {
			return cuenta;
		}

		public void setCuenta(String cuenta) {
			this.cuenta = cuenta;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}

		public boolean isAsentable() {
			return asentable;
		}

		public void setAsentable(boolean asentable) {
			this.asentable = asentable;
		}

		public int getNivel() {
			return nivel;
		}

		public void setNivel(int nivel) {
			this.nivel = nivel;
		}

		public int getIntegradora() {
			return integradora;
		}

		public void setIntegradora(int integradora) {
			this.integradora = integradora;
		}

		
		
}
