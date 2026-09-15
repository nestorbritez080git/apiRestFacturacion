package com.bisontecfacturacion.security.auxiliar;

public class BarcodePrintRequest {
	private String printer;
    private String descripcion;
    private String codigo;
    private Double precio;
    private Integer cantidad;
    private Boolean isDescripcion;
    private Boolean isPrecio;
    private Boolean isCodigo;
    private String tipoCodificacion;

    
    public String getTipoCodificacion() {
		return tipoCodificacion;
	}

	public void setTipoCodificacion(String tipoCodificacion) {
		this.tipoCodificacion = tipoCodificacion;
	}

	public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

	public Boolean getIsDescripcion() {
		return isDescripcion;
	}

	public void setIsDescripcion(Boolean isDescripcion) {
		this.isDescripcion = isDescripcion;
	}

	public Boolean getIsPrecio() {
		return isPrecio;
	}

	public void setIsPrecio(Boolean isPrecio) {
		this.isPrecio = isPrecio;
	}

	public Boolean getIsCodigo() {
		return isCodigo;
	}

	public void setIsCodigo(Boolean isCodigo) {
		this.isCodigo = isCodigo;
	}
    
}
