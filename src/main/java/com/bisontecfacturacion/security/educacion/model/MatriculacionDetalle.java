package com.bisontecfacturacion.security.educacion.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.apache.poi.ss.formula.functions.Now;
import org.hibernate.annotations.GenericGenerator;

import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Persona;
import com.bisontecfacturacion.security.model.Venta;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class MatriculacionDetalle {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private int numeroCuota;
	private Double montoCuota;
	private Double montoAumento;
	
	private String fechaRangoFinVencimiento;
	private String fechaRangoIncioVencimiento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "es-PY", timezone = "America/Asuncion")
	private LocalDateTime fechaPago;
	private Double subtotal;
	private Double importe;
	private Boolean estado;
	@ManyToOne
	@JsonIgnoreProperties("matriculacion")
	private Matriculacion matriculacion;

	public MatriculacionDetalle() {
		this.id=0;
		this.numeroCuota=0;
		this.montoCuota=0.0;
		this.montoAumento=0.0;
		this.subtotal=0.0;
		this.importe=0.0;
		this.matriculacion= new Matriculacion();
		this.estado=false;
	}
	
	 public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumeroCuota() {
		return numeroCuota;
	}

	public void setNumeroCuota(int numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public Double getMontoCuota() {
		return montoCuota;
	}

	public void setMontoCuota(Double montoCuota) {
		this.montoCuota = montoCuota;
	}

	public Double getMontoAumento() {
		return montoAumento;
	}

	public void setMontoAumento(Double montoAumento) {
		this.montoAumento = montoAumento;
	}

	

	

	public String getFechaRangoFinVencimiento() {
		return fechaRangoFinVencimiento;
	}

	public void setFechaRangoFinVencimiento(String fechaRangoFinVencimiento) {
		this.fechaRangoFinVencimiento = fechaRangoFinVencimiento;
	}

	public String getFechaRangoIncioVencimiento() {
		return fechaRangoIncioVencimiento;
	}

	public void setFechaRangoIncioVencimiento(String fechaRangoIncioVencimiento) {
		this.fechaRangoIncioVencimiento = fechaRangoIncioVencimiento;
	}

	public LocalDateTime getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDateTime fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Matriculacion getMatriculacion() {
		return matriculacion;
	}

	public void setMatriculacion(Matriculacion matriculacion) {
		this.matriculacion = matriculacion;
	}

	public void calcularCuotaConAumento(Double montoCuota, Double montoAumentoSaltoAño, int duracionMeses, String fechaInicio) {
	        try {
	            // Formato de fecha
	            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	            // Parsear la fecha de inicio
	            Date fecInicio = dFormat.parse(fechaInicio);
	            // Crear calendario para manejar las fechas
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(fecInicio);  // Establecer la fecha de inicio
	            // Variable para determinar si el primer año ya pasó
	            boolean primerAñoCompletado = false;
	            // Iterar mes a mes para la duración en meses
	            int ultimoAñoAumentado=0;
                int añoInicial = cal.get(Calendar.YEAR);// Obtén el año actual
	            for (int i = 0; i < duracionMeses;) {
	            	 cal.add(Calendar.MONTH, 1);
	                // Imprimir el número de la cuota (mes) y el monto de la cuota
	                String fechaMes = dFormat.format(cal.getTime());
	                if (cal.get(Calendar.MONTH) == Calendar.JANUARY) {
	                    System.out.println("Sin registro de cuota en enero.");
	                    // Termina el método o salta el procesamiento
	                }else {
	                	 
	                	System.out.print("Cuota #" + (i + 1) + " - Mes: " + fechaMes + " - ");
		                // Si ya pasó un año completo, aplicar el aumento (al iniciar el segundo año)
		                int añoActual = cal.get(Calendar.YEAR);
		                if (añoActual > añoInicial && añoActual > ultimoAñoAumentado) { 
		                    // Si el año actual es mayor al año inicial y mayor al último año donde se aplicó el aumento
		                    montoCuota += montoAumentoSaltoAño;  // Aplica el aumento
		                    ultimoAñoAumentado = añoActual;      // Actualiza el año del último aumento
		                    System.out.println("Cuota con aumento: " + montoCuota);
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                String fechaDia1 = dFormat.format(cal.getTime()); // Fecha del día 1

			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                String fechaDia5 = dFormat.format(cal.getTime()); // Fecha del día 5
			                // Imprimir las cuotas con sus fechas
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia1 + "  Fecha día 5 de la cuota: " + fechaDia5);
			  // Imprimir cuota con aumento
		                } else {
		                    cal.set(Calendar.DAY_OF_MONTH, 1);
			                String fechaDia1 = dFormat.format(cal.getTime()); // Fecha del día 1

			                cal.set(Calendar.DAY_OF_MONTH, 5);
			                String fechaDia5 = dFormat.format(cal.getTime()); // Fecha del día 5

			                // Imprimir las cuotas con sus fechas
			                System.out.println("Cuota: " + montoCuota);
			                System.out.println("Fecha día 1 de la cuota: " + fechaDia1 + "  Fecha día 5 de la cuota: " + fechaDia5);
			  // Imprimir cuota sin aumento
		                }
		                
		                // Avanzar al siguiente mes
		                
	                }
	                             System.out.println();
	               
	                // Imprimir el número de cuota, mes y monto
	                
	            }

	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	public static void main(String[] args) {
		 try{
			MatriculacionDetalle m = new MatriculacionDetalle();
			m.calcularCuotaConAumento(250000.0, 50000.0, 10, "2024-11-28");
	        }catch(Exception e){
	            e.printStackTrace();
	        }
		
	}

	
}
