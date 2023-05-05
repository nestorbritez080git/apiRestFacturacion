package com.bisontecfacturacion.security.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class GastoConsumicionesCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	@ManyToOne
	private Funcionario funcionarioRegistro;
	
	@ManyToOne
	private Funcionario funcionarioCargo;
	private Double total;
	private String estado;
	
	
	@ManyToOne
	private TipoOperacion tipoOperacion;
	
	@ManyToOne
	private CajaChica  cajaChica;
	
	@ManyToOne
	private Concepto concepto;
	
	@OneToMany(mappedBy="gastoConsumicionesCabecera")
	private List<GastoConsumicionesDetalle> gastoConsumicionesDetalles ; 
	public GastoConsumicionesCabecera() {
		this.id=0;
		this.fecha= new Date();
		this.hora="";
		this.funcionarioCargo= new Funcionario();
		this.funcionarioRegistro= new Funcionario();
		this.total=0.0;
		this.estado="";
		this.gastoConsumicionesDetalles= new ArrayList<GastoConsumicionesDetalle>();
		this.tipoOperacion= new TipoOperacion();
		this.cajaChica = new CajaChica();
		this.concepto = new Concepto();
	}
	public CajaChica getCajaChica() {
		return cajaChica;
	}
	public void setCajaChica(CajaChica cajaChica) {
		this.cajaChica = cajaChica;
	}
	public TipoOperacion getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(TipoOperacion tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}
	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}
	public Funcionario getFuncionarioCargo() {
		return funcionarioCargo;
	}
	public void setFuncionarioCargo(Funcionario funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}
	
	


	public Double getTotal() {
		return total;
	}


	public void setTotal(Double total) {
		this.total = total;
	}


	public List<GastoConsumicionesDetalle> getGastoConsumicionesDetalles() {
		return gastoConsumicionesDetalles;
	}


	public void setGastoConsumicionesDetalles(List<GastoConsumicionesDetalle> gastoConsumicionesDetalles) {
		this.gastoConsumicionesDetalles = gastoConsumicionesDetalles;
	}
	public Concepto getConcepto() {
		return concepto;
	}
	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}
	
	

}
