package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class PagosProveedoresReferenciaOperacionCaja {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@ManyToOne
	private PagosProveedorCabecera pagosProveedorCabecera;
	@ManyToOne
	private OperacionCaja operacionCaja;

		
	public PagosProveedoresReferenciaOperacionCaja() {
		this.pagosProveedorCabecera= new PagosProveedorCabecera();
		this.id=0;
		this.operacionCaja=new OperacionCaja();	
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

	public OperacionCaja getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(OperacionCaja operacionCaja) {
		this.operacionCaja = operacionCaja;
	}


	


	
	
	
}
