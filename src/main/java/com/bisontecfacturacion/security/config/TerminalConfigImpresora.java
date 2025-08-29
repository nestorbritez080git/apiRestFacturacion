package com.bisontecfacturacion.security.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.AutoImpresorTipoRemision;

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
	private Boolean estadoAdicionArtVarios;
	private Boolean estadoEdicionZona;
		
	@NotNull
	private AutoImpresor autoImpresor;
	public TerminalConfigImpresora() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.numeroTerminal=0;
		this.impresora="";
		this.nombreImpresora="";
		this.estadoAutoImpresor=false;
		this.estadoAdicionArtVarios=false;
		this.estadoEdicionZona= false;
		this.autoImpresor = new AutoImpresor();
	}
	
	public AutoImpresor getAutoImpresor() {
		return autoImpresor;
	}

	public void setAutoImpresor(AutoImpresor autoImpresor) {
		this.autoImpresor = autoImpresor;
	}

	public Boolean getEstadoEdicionZona() {
		return estadoEdicionZona;
	}

	public void setEstadoEdicionZona(Boolean estadoEdicionZona) {
		this.estadoEdicionZona = estadoEdicionZona;
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

	public Boolean getEstadoAdicionArtVarios() {
		return estadoAdicionArtVarios;
	}

	public void setEstadoAdicionArtVarios(Boolean estadoAdicionArtVarios) {
		this.estadoAdicionArtVarios = estadoAdicionArtVarios;
	}
	
	
}
