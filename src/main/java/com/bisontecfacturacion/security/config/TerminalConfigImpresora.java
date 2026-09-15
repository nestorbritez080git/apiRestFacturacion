package com.bisontecfacturacion.security.config;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.AutoImpresor;

@Entity
public class TerminalConfigImpresora {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	@NotNull
	@Column(unique = true) // 🔹 Esto ya asegura que sea único en la tabla
	private int numeroTerminal;
	@NotNull
	private String impresora;
	@NotNull
	private String nombreImpresora;

	private Boolean estadoEmisionFactura;
	private Boolean estadoAdicionArtVarios;
	private Boolean estadoEdicionZona;
	private Boolean estadoListadoGrigImagen;
	private String nombreImpresoraBarcode;
	private String isCentralizadoImpresion;
	@ManyToOne
	private AutoImpresor autoImpresor;
	
	private String ip;

	private String nombreEquipo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimaConexion;
	
	
	
	public TerminalConfigImpresora() {
		// TODO Auto-generated constructor stub
		this.id=0;
		this.numeroTerminal=0;
		this.impresora="";
		this.nombreImpresora="";
		this.estadoEmisionFactura=false;
		this.estadoAdicionArtVarios=false;
		this.estadoEdicionZona= false;
		this.autoImpresor = new AutoImpresor();
		this.estadoListadoGrigImagen=false;
		this.nombreImpresoraBarcode="";
		this.ip = "";
		this.nombreEquipo="";
		this.ultimaConexion= new Date();
		this.isCentralizadoImpresion="";
	}
	
	
	
	public String getIsCentralizadoImpresion() {
		return isCentralizadoImpresion;
	}



	public void setIsCentralizadoImpresion(String isCentralizadoImpresion) {
		this.isCentralizadoImpresion = isCentralizadoImpresion;
	}



	public String getIp() {
		return ip;
	}



	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getNombreEquipo() {
		return nombreEquipo;
	}



	public void setNombreEquipo(String nombreEquipo) {
		this.nombreEquipo = nombreEquipo;
	}



	public Date getUltimaConexion() {
		return ultimaConexion;
	}



	public void setUltimaConexion(Date ultimaConexion) {
		this.ultimaConexion = ultimaConexion;
	}



	public String getNombreImpresoraBarcode() {
		return nombreImpresoraBarcode;
	}



	public void setNombreImpresoraBarcode(String nombreImpresoraBarcode) {
		this.nombreImpresoraBarcode = nombreImpresoraBarcode;
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

	
	public Boolean getEstadoEmisionFactura() {
		return estadoEmisionFactura;
	}

	public void setEstadoEmisionFactura(Boolean estadoEmisionFactura) {
		this.estadoEmisionFactura = estadoEmisionFactura;
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

	public Boolean getEstadoListadoGrigImagen() {
		return estadoListadoGrigImagen;
	}

	public void setEstadoListadoGrigImagen(Boolean estadoListadoGrigImagen) {
		this.estadoListadoGrigImagen = estadoListadoGrigImagen;
	}
	
	
}
