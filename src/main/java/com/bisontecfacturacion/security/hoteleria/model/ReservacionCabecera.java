package com.bisontecfacturacion.security.hoteleria.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Cliente;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.Documento;
import com.bisontecfacturacion.security.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ReservacionCabecera {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaFactura;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fechaRegistro;
	@ManyToOne
	private Funcionario funcionarioRegistro;
	@ManyToOne
	private Funcionario funcionarioFinalizacion;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private Documento documento;
	@ManyToOne
	private HabitacionesCategoriaCombo habitacionesCategoriaCombo;
	private Double entrega;
	@NotNull
	private Double totalHabitacion;
	private Double totalProducto;
	private String totalLetra;
	private Double totalDescuento;
    private Double totalIvaCinco;
    private Double totalIvaDies;
	private String estado;
	private String tipo;
	private int operacionCaja;
	private int operacionCajaEntrega;
	private Double precioMinimo;
	private Double precioNormal;
	private Double precio;
	private String descripcionCombo;
	private String hora;
	private String horaFinalizacion;
	private String obs;
	@OneToMany(mappedBy="reservacionCabecera")
	private List<ReservacionDetalle> reservacionDetalles; 
	
	public ReservacionCabecera() {
		this.id=0;
		this.funcionarioRegistro= new Funcionario();
		this.funcionarioFinalizacion= new Funcionario();
		this.cliente= new Cliente();
		this.documento= new Documento();
		this.fechaRegistro= new Date();
		this.fechaFactura= new Date();
		this.entrega=0.0;
		this.totalHabitacion=0.0;
		this.totalProducto=0.0;
		this.totalLetra="";
		this.totalDescuento=0.0;
		this.totalIvaCinco=0.0;
		this.totalIvaDies=0.0;
		this.estado="RESERVADO";
		this.tipo="";
		this.operacionCaja=0;
		this.operacionCajaEntrega=0;
		this.precioMinimo=0.0;
		this.precioNormal=0.0;
		this.descripcionCombo="";
		this.hora="";
		this.horaFinalizacion="";
		this.obs="";
		this.precio=0.0;
	}
	
	public String getHoraFinalizacion() {
		return horaFinalizacion;
	}

	public void setHoraFinalizacion(String horaFinalizacion) {
		this.horaFinalizacion = horaFinalizacion;
	}

	public int getOperacionCajaEntrega() {
		return operacionCajaEntrega;
	}

	public void setOperacionCajaEntrega(int operacionCajaEntrega) {
		this.operacionCajaEntrega = operacionCajaEntrega;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public List<ReservacionDetalle> getReservacionDetalles() {
		return reservacionDetalles;
	}

	public void setReservacionDetalles(List<ReservacionDetalle> reservacionDetalles) {
		this.reservacionDetalles = reservacionDetalles;
	}

	public Double getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(Double totalDescuento) {
		this.totalDescuento = totalDescuento;
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

	public void setPrecioMinimo(Double precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public void setPrecioNormal(Double precioNormal) {
		this.precioNormal = precioNormal;
	}

	public double getPrecioMinimo() {
		return precioMinimo;
	}

	public void setPrecioMinimo(double precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public double getPrecioNormal() {
		return precioNormal;
	}

	public void setPrecioNormal(double precioNormal) {
		this.precioNormal = precioNormal;
	}

	public String getDescripcionCombo() {
		return descripcionCombo;
	}

	public void setDescripcionCombo(String descripcionCombo) {
		this.descripcionCombo = descripcionCombo;
	}

	
	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public int getOperacionCaja() {
		return operacionCaja;
	}

	public void setOperacionCaja(int operacionCaja) {
		this.operacionCaja = operacionCaja;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
	public Date getFechaFactura() {
		return fechaFactura;
	}
	public void setFechaFactura(Date fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Funcionario getFuncionarioRegistro() {
		return funcionarioRegistro;
	}
	public void setFuncionarioRegistro(Funcionario funcionarioRegistro) {
		this.funcionarioRegistro = funcionarioRegistro;
	}
	public Funcionario getFuncionarioFinalizacion() {
		return funcionarioFinalizacion;
	}
	public void setFuncionarioFinalizacion(Funcionario funcionarioFinalizacion) {
		this.funcionarioFinalizacion = funcionarioFinalizacion;
	}
	public HabitacionesCategoriaCombo getHabitacionesCategoriaCombo() {
		return habitacionesCategoriaCombo;
	}
	public void setHabitacionesCategoriaCombo(HabitacionesCategoriaCombo habitacionesCategoriaCombo) {
		this.habitacionesCategoriaCombo = habitacionesCategoriaCombo;
	}
	public Double getEntrega() {
		return entrega;
	}
	public void setEntrega(Double entrega) {
		this.entrega = entrega;
	}
	public Double getTotalHabitacion() {
		return totalHabitacion;
	}
	public void setTotalHabitacion(Double totalHabitacion) {
		this.totalHabitacion = totalHabitacion;
	}
	public Double getTotalProducto() {
		return totalProducto;
	}
	public void setTotalProducto(Double totalProducto) {
		this.totalProducto = totalProducto;
	}
	public String getTotalLetra() {
		return totalLetra;
	}
	public void setTotalLetra(String totalLetra) {
		this.totalLetra = totalLetra;
	}
	

}
