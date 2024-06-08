package com.bisontecfacturacion.security.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class TerminalConfigImpresora {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@NotNull
	private int numeroTerminal;
	@NotNull
	private String impresora;
	@NotNull
	private String nombreImpresora;

	private Boolean estadoAutoImpresor;
		
	
	public TerminalConfigImpresora() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.numeroTerminal=0;
		this.impresora="";
		this.nombreImpresora="";
		this.estadoAutoImpresor=false;
	}
	
	public Boolean getEstadoAutoImpresor() {
		return estadoAutoImpresor;
	}

	public void setEstadoAutoImpresor(Boolean estadoAutoImpresor) {
		this.estadoAutoImpresor = estadoAutoImpresor;
	}

	public String getNombreImpresora() {
		return nombreImpresora;
	}

	public void setNombreImpresora(String nombreImpresora) {
		this.nombreImpresora = nombreImpresora;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumeroTerminal() {
		return numeroTerminal;
	}
	public void setNumeroTerminal(int numeroTerminal) {
		this.numeroTerminal = numeroTerminal;
	}
	public String getImpresora() {
		return impresora;
	}
	public void setImpresora(String impresora) {
		this.impresora = impresora;
	}
	
}
