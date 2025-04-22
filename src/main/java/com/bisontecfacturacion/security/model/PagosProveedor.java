package com.bisontecfacturacion.security.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PagosProveedor {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private Double importe;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	
	
	@ManyToOne
	private CuentaPagarCabecera cuentaPagarCabecera;
	@ManyToOne
	private PagosProveedorCabecera pagosProveedorCabecera;
	
	@ManyToOne
	private Funcionario funcionario;
	
	
	
	
	
	public PagosProveedor() {
		this.id=0;
		this.importe=0.0;
		this.cuentaPagarCabecera= new CuentaPagarCabecera();
		this.pagosProveedorCabecera= new PagosProveedorCabecera();
		this.funcionario = new Funcionario();
		this.fecha= new Date();
	}





	public Date getFecha() {
		return fecha;
	}





	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}





	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public Double getImporte() {
		return importe;
	}





	public void setImporte(Double importe) {
		this.importe = importe;
	}





	public CuentaPagarCabecera getCuentaPagarCabecera() {
		return cuentaPagarCabecera;
	}





	public void setCuentaPagarCabecera(CuentaPagarCabecera cuentaPagarCabecera) {
		this.cuentaPagarCabecera = cuentaPagarCabecera;
	}





	public PagosProveedorCabecera getPagosProveedorCabecera() {
		return pagosProveedorCabecera;
	}





	public void setPagosProveedorCabecera(PagosProveedorCabecera pagosProveedorCabecera) {
		this.pagosProveedorCabecera = pagosProveedorCabecera;
	}





	public Funcionario getFuncionario() {
		return funcionario;
	}





	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	

	
	
	
}
