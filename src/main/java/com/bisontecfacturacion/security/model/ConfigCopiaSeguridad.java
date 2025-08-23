package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ConfigCopiaSeguridad {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String nombrePendrive;
	private String ruta;
	private String nombreBaseDatos;
	private String puerto;
	private String password;
	private String host;
	private String hora1;
	private String hora2;
	private String hora3;
	
	
	
	public String getHora1() {
		return hora1;
	}
	public void setHora1(String hora1) {
		this.hora1 = hora1;
	}
	public String getHora2() {
		return hora2;
	}
	public void setHora2(String hora2) {
		this.hora2 = hora2;
	}
	public String getHora3() {
		return hora3;
	}
	public void setHora3(String hora3) {
		this.hora3 = hora3;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPuerto() {
		return puerto;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombrePendrive() {
		return nombrePendrive;
	}
	public void setNombrePendrive(String nombrePendrive) {
		this.nombrePendrive = nombrePendrive;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public String getNombreBaseDatos() {
		return nombreBaseDatos;
	}
	public void setNombreBaseDatos(String nombreBaseDatos) {
		this.nombreBaseDatos = nombreBaseDatos;
	}

	
}
