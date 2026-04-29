package com.bisontecfacturacion.security.auxiliar;

import java.util.List;

public class ComparativaVentasDTO {
	private List<VentaMensualDTO> actual;
    private List<VentaMensualDTO> anterior;

    public List<VentaMensualDTO> getActual() {
        return actual;
    }

    public void setActual(List<VentaMensualDTO> actual) {
        this.actual = actual;
    }

    public List<VentaMensualDTO> getAnterior() {
        return anterior;
    }

    public void setAnterior(List<VentaMensualDTO> anterior) {
        this.anterior = anterior;
    }
}
