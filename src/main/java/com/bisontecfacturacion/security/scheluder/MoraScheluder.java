package com.bisontecfacturacion.security.scheluder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bisontecfacturacion.security.hoteleria.model.SetingRecepciones;
import com.bisontecfacturacion.security.hoteleria.repository.CuentaCobrarCabeceraReservacionesRepository;
import com.bisontecfacturacion.security.hoteleria.repository.SetingRecepcionesRepository;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.CuentaCobrarMora;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.CuentaCobrarMoraRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;

@Component
public class MoraScheluder {
	@Autowired
	private CuentaAcobrarDetalleRepository cuentaCobrarDetalleRepository;

	@Autowired
	private CuentaAcobrarRepository cuentaCobrarCabeceraRepository;

	@Autowired
	private CuentaCobrarMoraRepository cuentaCobrarMoraRepository;

	@Autowired
	private SetingRecepcionesRepository setingRepository;  

	@Autowired
	private ImpresoraRepository impresoraRepository;  


	@Scheduled(fixedRate = 600000) // cada 10 minuto
	@Transactional
	public void generarMoraDiaria() {
		SetingRecepciones seting = setingRepository.findFirstByOrderByIdAsc();
		if (seting == null) {
			System.out.println("No existe configuración.");
			return;
		}
		Impresora ipm= null;
		ipm = impresoraRepository.getOne(27);
		if (ipm == null || !ipm.isEstado()) {
			System.out.println("Configuración de tasa de interés deshabilitada.");
			return;
		}
		LocalTime horaCorte = seting.getHoraFinalizacionDiaria();
		LocalDate hoyy = LocalDate.now();
		LocalTime ahoraHora = LocalTime.now();

		// 🔹 Última ejecución registrada
		LocalDate ultimaEjecucion = seting.getFechaUltimaActualizacionMora();
		// 🔹 Ejecutar si no se hizo hoy
		boolean noEjecutadoHoy = (ultimaEjecucion == null || !ultimaEjecucion.equals(hoyy));
		boolean pasoHoraCorte = ahoraHora.isAfter(horaCorte);

		if (!noEjecutadoHoy || !pasoHoraCorte) {
			System.out.println("⏳ Aún no corresponde ejecutar o ya se ejecutó hoy.");
			return;
		}
		System.out.println("✔ Ejecutando actualización de cuenta vencida...");
		List<CuentaCobrarDetalle> cuotas = cuentaCobrarDetalleRepository.obtenerCuotasPendientesVencida();
		System.out.println("cuot size: "+cuotas.size());
		for (CuentaCobrarDetalle detalle : cuotas) {
			try {
				// Evitar generar dos veces el mismo día
				if (detalle.getFechaUltimaGeneracion() != null) {
					LocalDate ultima = detalle.getFechaUltimaGeneracion().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if (ultima.equals(LocalDate.now())) {
						continue;
					} 
				}
				Integer porcentaje = detalle.getCuentaCobrarCabecera().getPorcentajeInteresMora();
				// No generar mora si no tiene interés configurado
				if (porcentaje == null || porcentaje <= 0) {
					System.out.println("Cuenta sin porcentaje de mora.");
					continue;
				}
				LocalDate fechaBase;

				if (detalle.getFechaUltimaGeneracion() != null) {

					fechaBase = detalle.getFechaUltimaGeneracion()
							.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();

				} else {

					fechaBase = detalle.getFechaVencimiento()
							.toInstant()
							.atZone(ZoneId.systemDefault())
							.toLocalDate();
				}
				Double saldoPendiente = (detalle.getMonto() == null ? 0D : detalle.getMonto())
						- (detalle.getImporte() == null ? 0D : detalle.getImporte());

				if (saldoPendiente <= 0) {
					continue;
				}

				long diasAtraso = ChronoUnit.DAYS.between(
						fechaBase,
						LocalDate.now()
						);
				if (diasAtraso <= 0) {
				    continue;
				}

				System.out.println("saldoPendiente: " + saldoPendiente);
				System.out.println("porcentaje: " + porcentaje);
				System.out.println("diasAtraso: " + diasAtraso);
				Double montoMora = saldoPendiente * (porcentaje / 100.0)  * ((int) diasAtraso / 30.0);
				System.out.println(" PORCENTAJE INTERES : QUE VIENE DE CADA CUENTA QUE SE CARGA EN DETALLE "+ porcentaje);
				System.out.println("monto mora: " + montoMora);
				// guardar histórico de mora
				CuentaCobrarMora mora = new CuentaCobrarMora();

				mora.setCuentaCobrarDetalle(detalle);
				mora.setFechaGeneracion(new Date());
				mora.setPorcentajeMora(porcentaje.doubleValue());
				mora.setMontoBase(saldoPendiente);
				mora.setMontoMora(montoMora);
				mora.setDiaAtraso((int) diasAtraso);

				cuentaCobrarMoraRepository.save(mora);
				// ACTUALIZAR DETALLE
				detalle.setSubTotal(detalle.getSubTotal() + montoMora);
				detalle.setInteresMora((detalle.getInteresMora() == null ? 0 : detalle.getInteresMora()) + montoMora);
				detalle.setFechaUltimaGeneracion(new Date());
				cuentaCobrarDetalleRepository.save(detalle);
				cuentaCobrarCabeceraRepository.actualziarInteresTotalMoratoria(detalle.getCuentaCobrarCabecera().getId(), montoMora);





			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error procesando detalle ID "+ detalle.getId()+ ": "+ e.getMessage());
				e.printStackTrace();
			}
		}
		// 🔹 Guardar la fecha de ejecución para evitar recalcular el mismo día
		seting.setFechaUltimaActualizacionMora((hoyy));
		setingRepository.save(seting);
		System.out.println("✔ Actualización de cuentas vencida completada.");


	}

}
