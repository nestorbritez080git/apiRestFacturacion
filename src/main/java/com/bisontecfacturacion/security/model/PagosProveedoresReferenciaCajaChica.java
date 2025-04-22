package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PagosProveedoresReferenciaCajaChica {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private PagosProveedorCabecera pagosProveedorCabecera;
	
	public PagosProveedoresReferenciaCajaChica() {
		this.pagosProveedorCabecera= new PagosProveedorCabecera();
		this.id=0;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	

	public PagosProveedorCabecera getPagosProveedorCabecera() {
		return pagosProveedorCabecera;
	}

	public void setPagosProveedorCabecera(PagosProveedorCabecera pagosProveedorCabecera) {
		this.pagosProveedorCabecera = pagosProveedorCabecera;
	}

	

	
	
	
	
	
}
