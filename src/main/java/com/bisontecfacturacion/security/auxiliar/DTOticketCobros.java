package com.bisontecfacturacion.security.auxiliar;

import java.util.List;

public class DTOticketCobros {
	private List<CobrosTicketDTO> listCobros;
	private List<OperacionTicketDTO> listOperacion;
	public List<CobrosTicketDTO> getListCobros() {
		return listCobros;
	}
	public void setListCobros(List<CobrosTicketDTO> listCobros) {
		this.listCobros = listCobros;
	}
	public List<OperacionTicketDTO> getListOperacion() {
		return listOperacion;
	}
	public void setListOperacion(List<OperacionTicketDTO> listOperacion) {
		this.listOperacion = listOperacion;
	}
	
	
	
}
