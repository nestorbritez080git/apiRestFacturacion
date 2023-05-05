package com.bisontecfacturacion.security.auxiliar;

import java.util.Date;

public class AnticipoDetalleAuxiliar {
	private Date fecha;
	private Double monto;
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}

}
