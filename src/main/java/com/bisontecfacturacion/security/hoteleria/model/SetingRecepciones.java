package com.bisontecfacturacion.security.hoteleria.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class SetingRecepciones {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private Boolean estadoSumaManualEstadia;
	private LocalTime horaFinalizacionDiaria;
	
	// Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getEstadoSumaManualEstadia() {
        return estadoSumaManualEstadia;
    }

    public void setEstadoSumaManualEstadia(Boolean estadoSumaManualEstadia) {
        this.estadoSumaManualEstadia = estadoSumaManualEstadia;
    }

    public LocalTime getHoraFinalizacionDiaria() {
        return horaFinalizacionDiaria;
    }

    public void setHoraFinalizacionDiaria(LocalTime horaFinalizacionDiaria) {
        this.horaFinalizacionDiaria = horaFinalizacionDiaria;
    }
}
