package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class TipoMovimiento {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String descripcion;
	
	@OneToMany(mappedBy="tipoMovimiento")
	@JsonBackReference
	private List<MovimientoEntradaSalida> movimientoEntradaSalida;

	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public List<MovimientoEntradaSalida> getMovimientoEntradaSalida() {
		return movimientoEntradaSalida;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setMovimientoEntradaSalida(List<MovimientoEntradaSalida> movimientoEntradaSalida) {
		this.movimientoEntradaSalida = movimientoEntradaSalida;
	}
	
	
	
	
}
