package com.bisontecfacturacion.security.modeloIA;

import java.util.List;

public class ProductoVentaIA {
	private Integer id;
    private String nombre;
    private List<Double> ventas;
    private Double prediccion;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public List<Double> getVentas() {
		return ventas;
	}
	public void setVentas(List<Double> ventas) {
		this.ventas = ventas;
	}
	public Double getPrediccion() {
		return prediccion;
	}
	public void setPrediccion(Double prediccion) {
		this.prediccion = prediccion;
	}
    
    
}
