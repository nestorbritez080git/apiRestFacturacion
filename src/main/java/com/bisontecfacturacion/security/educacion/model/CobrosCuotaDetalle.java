package com.bisontecfacturacion.security.educacion.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class CobrosCuotaDetalle {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private Double importe;
	private Double importeAnterior;
	@ManyToOne
    private MatriculacionDetalle matriculacionDetalle;
	@ManyToOne
    private CobrosCuota cobrosCuota;
	
	public CobrosCuotaDetalle() {
		this.id=0;
		this.importe=0.0;
		this.importeAnterior=0.0;
		this.cobrosCuota= new CobrosCuota();
		this.matriculacionDetalle=new MatriculacionDetalle();	
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

	public MatriculacionDetalle getMatriculacionDetalle() {
		return matriculacionDetalle;
	}

	public void setMatriculacionDetalle(MatriculacionDetalle matriculacionDetalle) {
		this.matriculacionDetalle = matriculacionDetalle;
	}

	public CobrosCuota getCobrosCuota() {
		return cobrosCuota;
	}

	public void setCobrosCuota(CobrosCuota cobrosCuota) {
		this.cobrosCuota = cobrosCuota;
	}

	public Double getImporteAnterior() {
		return importeAnterior;
	}

	public void setImporteAnterior(Double importeAnterior) {
		this.importeAnterior = importeAnterior;
	}
	
	
}

	


