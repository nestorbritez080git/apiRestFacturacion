package com.bisontecfacturacion.security.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
public class MovimientoSalidaEntrada {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;
	@NotNull
	private Double cantidad;
	private Double costoEntrada;
	private Double costoEntradaAnterior;
	private Double ingreso;
	private Double egreso;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "es-PY", timezone = "America/Asuncion")
	private Date fecha;
	private String hora;

	@NotNull
	private Double venta_1;
	private Double venta_2;
	private Double venta_3;
	private Double venta_4;
	private Double venta_1_anterior;
	private Double venta_2_anterior;
	private Double venta_3_anterior;
	private Double venta_4_anterior;
	private Double ventaSalida;
	private Double costoSalida;
	private String marca;

	private String referencia;
	

	@ManyToOne
	private TipoMovimiento tipoMovimiento;
	
	@ManyToOne
	private Producto producto;
	
	@ManyToOne
	private Funcionario funcionario;
	
	public MovimientoSalidaEntrada() {
		id=0;
		cantidad=0.0;
		descripcion="";
		fecha=new Date();
		hora = "";
		tipoMovimiento=new TipoMovimiento();
		producto=new Producto();
		funcionario=new Funcionario();
		marca = "";
		venta_1=0.0;
		venta_1_anterior=0.0;
		venta_2=0.0;
		venta_2_anterior=0.0;
		venta_3=0.0;
		venta_3_anterior=0.0;
		venta_4=0.0;
		venta_4_anterior=0.0;
		costoEntrada=0.0;
		costoEntradaAnterior=0.0;
		costoSalida=0.0;
		ingreso =0.0;
		egreso=0.0;
		
		
		
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

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getCostoEntrada() {
		return costoEntrada;
	}

	public void setCostoEntrada(Double costoEntrada) {
		this.costoEntrada = costoEntrada;
	}



	public Double getIngreso() {
		return ingreso;
	}

	public void setIngreso(Double ingreso) {
		this.ingreso = ingreso;
	}

	public Double getEgreso() {
		return egreso;
	}

	public void setEgreso(Double egreso) {
		this.egreso = egreso;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getVenta_1() {
		return venta_1;
	}

	public void setVenta_1(Double venta_1) {
		this.venta_1 = venta_1;
	}

	public Double getVenta_2() {
		return venta_2;
	}

	public void setVenta_2(Double venta_2) {
		this.venta_2 = venta_2;
	}

	public Double getVenta_3() {
		return venta_3;
	}

	public void setVenta_3(Double venta_3) {
		this.venta_3 = venta_3;
	}

	public Double getVenta_4() {
		return venta_4;
	}

	public void setVenta_4(Double venta_4) {
		this.venta_4 = venta_4;
	}

	public Double getVentaSalida() {
		return ventaSalida;
	}

	public void setVentaSalida(Double ventaSalida) {
		this.ventaSalida = ventaSalida;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Double getCostoEntradaAnterior() {
		return costoEntradaAnterior;
	}

	public void setCostoEntradaAnterior(Double costoEntradaAnterior) {
		this.costoEntradaAnterior = costoEntradaAnterior;
	}

	public Double getVenta_1_anterior() {
		return venta_1_anterior;
	}

	public void setVenta_1_anterior(Double venta_1_anterior) {
		this.venta_1_anterior = venta_1_anterior;
	}

	public Double getVenta_2_anterior() {
		return venta_2_anterior;
	}

	public void setVenta_2_anterior(Double venta_2_anterior) {
		this.venta_2_anterior = venta_2_anterior;
	}

	public Double getVenta_3_anterior() {
		return venta_3_anterior;
	}

	public void setVenta_3_anterior(Double venta_3_anterior) {
		this.venta_3_anterior = venta_3_anterior;
	}

	public Double getVenta_4_anterior() {
		return venta_4_anterior;
	}

	public void setVenta_4_anterior(Double venta_4_anterior) {
		this.venta_4_anterior = venta_4_anterior;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Double getCostoSalida() {
		return costoSalida;
	}

	public void setCostoSalida(Double costoSalida) {
		this.costoSalida = costoSalida;
	}




	
}
