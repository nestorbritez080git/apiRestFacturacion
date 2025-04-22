package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ReporteConfig {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String nombreReferencia;
	private String nombreReporte;
	private String nombreSubReporte1;
	private String nombreSubReporte2;
	private int pageHeigth;
	private int pageWidth;
	public ReporteConfig() {
		this.id=0;
		this.nombreReferencia="";
		this.nombreReporte="";
		this.nombreSubReporte1="";
		this.nombreSubReporte2="";
		this.pageHeigth=0;
		this.pageWidth=0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombreReporte() {
		return nombreReporte;
	}
	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}
	public String getNombreSubReporte1() {
		return nombreSubReporte1;
	}
	public void setNombreSubReporte1(String nombreSubReporte1) {
		this.nombreSubReporte1 = nombreSubReporte1;
	}
	public String getNombreSubReporte2() {
		return nombreSubReporte2;
	}
	public void setNombreSubReporte2(String nombreSubReporte2) {
		this.nombreSubReporte2 = nombreSubReporte2;
	}
	public String getNombreReferencia() {
		return nombreReferencia;
	}
	public void setNombreReferencia(String nombreReferencia) {
		this.nombreReferencia = nombreReferencia;
	}
	public int getPageHeigth() {
		return pageHeigth;
	}
	public void setPageHeigth(int pageHeigth) {
		this.pageHeigth = pageHeigth;
	}
	public int getPageWidth() {
		return pageWidth;
	}
	public void setPageWidth(int pageWidth) {
		this.pageWidth = pageWidth;
	}
	
	

}
