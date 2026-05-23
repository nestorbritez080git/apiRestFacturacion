package com.bisontecfacturacion.security.modeloIA;

import com.bisontecfacturacion.security.model.Producto;

public class ProductoVentaRentabilidad {
	  private Integer productoId;
	    private String nombre;

	    private Double cantidadVendida;
	    private Double ventas;
	    private Double costo;
	    private Double ganancia;
	    private Double margen;

	    private String clasificacion;

		public Integer getProductoId() {
			return productoId;
		}

		public void setProductoId(Integer productoId) {
			this.productoId = productoId;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public Double getCantidadVendida() {
			return cantidadVendida;
		}

		public void setCantidadVendida(Double cantidadVendida) {
			this.cantidadVendida = cantidadVendida;
		}

		public Double getVentas() {
			return ventas;
		}

		public void setVentas(Double ventas) {
			this.ventas = ventas;
		}

		public Double getCosto() {
			return costo;
		}

		public void setCosto(Double costo) {
			this.costo = costo;
		}

		public Double getGanancia() {
			return ganancia;
		}

		public void setGanancia(Double ganancia) {
			this.ganancia = ganancia;
		}

		public Double getMargen() {
			return margen;
		}

		public void setMargen(Double margen) {
			this.margen = margen;
		}

		public String getClasificacion() {
			return clasificacion;
		}

		public void setClasificacion(String clasificacion) {
			this.clasificacion = clasificacion;
		}
	    
	    

	
}
