package com.bisontecfacturacion.security.hoteleria.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ReservacionAnulada {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fecha;
	@ManyToOne
	private Funcionario funcionario;
	@ManyToOne
	private ReservacionCabecera reservacionCabecera;
	@NotNull
	private String motivo;
	public ReservacionAnulada() {
		this.id=0;
		this.reservacionCabecera= new ReservacionCabecera();
		this.funcionario= new Funcionario();
		this.motivo= "";
		
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
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public ReservacionCabecera getReservacionCabecera() {
		return reservacionCabecera;
	}
	public void setReservacionCabecera(ReservacionCabecera reservacionCabecera) {
		this.reservacionCabecera = reservacionCabecera;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	
	
}
