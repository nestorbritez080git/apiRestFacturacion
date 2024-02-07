package com.bisontecfacturacion.security.hoteleria.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.InteresCuota;
import com.bisontecfacturacion.security.model.InteresMora;
import com.bisontecfacturacion.security.model.TipoPlazo;
import com.bisontecfacturacion.security.model.Venta;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class CuentaCobrarCabeceraReservaciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@NotNull
	private Double total;
	private Double pagado;
	private Double saldo;
	private int fraccionCuota;
	private boolean estado;
	private Double entrega;
	@ManyToOne
	private TipoPlazo tipoPlazo;
	@ManyToOne
	private ReservacionCabecera reservacionCabecera;
	@ManyToOne
	private InteresCuota interesCuota;
	@ManyToOne
	private InteresMora interesMora;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private Cliente cliente;

	@OneToMany(mappedBy="cuentaCobrarCabeceraReservaciones")
	private List<CuentaCobrarDetalleReservaciones> cuentaCobrarDetalleReservaciones; 
	public CuentaCobrarCabeceraReservaciones() {
		this.id=0;
		this.funcionario= new Funcionario();
		this.cliente= new Cliente();
		this.reservacionCabecera= new ReservacionCabecera();
		this.interesCuota= new InteresCuota();
		this.interesMora=new InteresMora();
		this.tipoPlazo= new TipoPlazo();
		this.total=0.0;
		this.pagado=0.0;
		this.saldo=0.0;
		this.entrega=0.0;
		this.estado=false;
		this.fraccionCuota=0;
		this.cuentaCobrarDetalleReservaciones= new ArrayList<CuentaCobrarDetalleReservaciones>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Double getPagado() {
		return pagado;
	}
	public void setPagado(Double pagado) {
		this.pagado = pagado;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public int getFraccionCuota() {
		return fraccionCuota;
	}
	public void setFraccionCuota(int fraccionCuota) {
		this.fraccionCuota = fraccionCuota;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public Double getEntrega() {
		return entrega;
	}
	public void setEntrega(Double entrega) {
		this.entrega = entrega;
	}
	public TipoPlazo getTipoPlazo() {
		return tipoPlazo;
	}
	public void setTipoPlazo(TipoPlazo tipoPlazo) {
		this.tipoPlazo = tipoPlazo;
	}
	public ReservacionCabecera getReservacionCabecera() {
		return reservacionCabecera;
	}
	public void setReservacionCabecera(ReservacionCabecera reservacionCabecera) {
		this.reservacionCabecera = reservacionCabecera;
	}
	public InteresCuota getInteresCuota() {
		return interesCuota;
	}
	public void setInteresCuota(InteresCuota interesCuota) {
		this.interesCuota = interesCuota;
	}
	public InteresMora getInteresMora() {
		return interesMora;
	}
	public void setInteresMora(InteresMora interesMora) {
		this.interesMora = interesMora;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<CuentaCobrarDetalleReservaciones> getCuentaCobrarDetalleReservaciones() {
		return cuentaCobrarDetalleReservaciones;
	}
	public void setCuentaCobrarDetalleReservaciones(
			List<CuentaCobrarDetalleReservaciones> cuentaCobrarDetalleReservaciones) {
		this.cuentaCobrarDetalleReservaciones = cuentaCobrarDetalleReservaciones;
	}
	
}
