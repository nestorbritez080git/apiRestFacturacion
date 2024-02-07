package com.bisontecfacturacion.security.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class Pedido {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;
	private Double total;
	private String estado;

	private Double valorCotizacion;
	
	@OneToMany(mappedBy="pedido")
	@JsonBackReference
	private List<PedidoDetalle> pedidoDetalle; 
	
	private Double totalIvaCinco;
	private Double totalIvaDies;
	private Double totalExcenta;
	private String totalLetra;
	private String obs;
	
	@ManyToOne
	private Proveedor proveedor;
	@ManyToOne
	private Funcionario funcionario;
	
	

    public Pedido(){
            this.id=0;
            this.obs="";
            this.estado= "";
            this.total=0.0;
            this.obs="";
            this.funcionario= new Funcionario();
            this.proveedor = new Proveedor();
            this.estado="ABIERTO";
            this.totalIvaCinco=0.0;
    		this.totalIvaDies=0.0;
    		this.totalExcenta=0.0;
    		this.totalLetra="";
    		this.fecha= new Date();
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



	public Double getTotal() {
		return total;
	}



	public void setTotal(Double total) {
		this.total = total;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public Double getValorCotizacion() {
		return valorCotizacion;
	}



	public void setValorCotizacion(Double valorCotizacion) {
		this.valorCotizacion = valorCotizacion;
	}



	public List<PedidoDetalle> getPedidoDetalle() {
		return pedidoDetalle;
	}



	public void setPedidoDetalle(List<PedidoDetalle> pedidoDetalle) {
		this.pedidoDetalle = pedidoDetalle;
	}



	public Double getTotalIvaCinco() {
		return totalIvaCinco;
	}



	public void setTotalIvaCinco(Double totalIvaCinco) {
		this.totalIvaCinco = totalIvaCinco;
	}



	public Double getTotalIvaDies() {
		return totalIvaDies;
	}



	public void setTotalIvaDies(Double totalIvaDies) {
		this.totalIvaDies = totalIvaDies;
	}



	public Double getTotalExcenta() {
		return totalExcenta;
	}



	public void setTotalExcenta(Double totalExcenta) {
		this.totalExcenta = totalExcenta;
	}



	public String getTotalLetra() {
		return totalLetra;
	}



	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
	}



	public String getObs() {
		return obs;
	}



	public void setObs(String obs) {
		this.obs = obs;
	}



	public Proveedor getProveedor() {
		return proveedor;
	}



	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}



	public Funcionario getFuncionario() {
		return funcionario;
	}



	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
    
}
	
	
	