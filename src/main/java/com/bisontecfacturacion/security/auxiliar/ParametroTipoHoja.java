package com.bisontecfacturacion.security.auxiliar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ParametroTipoHoja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	private String descripcion;

public ParametroTipoHoja() {
	this.id=0;
	this.descripcion="";
}

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getDescripcion() {
	return descripcion;
}

public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}

}
