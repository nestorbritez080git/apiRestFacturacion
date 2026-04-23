package com.bisontecfacturacion.security.hoteleria.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.hoteleria.model.SetingRecepciones;
import com.bisontecfacturacion.security.hoteleria.repository.ReservacionCabeceraRepository;
import com.bisontecfacturacion.security.hoteleria.repository.SetingRecepcionesRepository;
@Service
public class EstadiaSheluderServices {
	@Autowired
	private ReservacionCabeceraRepository entityRepository;
	@Autowired
	private SetingRecepcionesRepository setingRepository;
	
	@Scheduled(fixedRate = 60000) // cada 1 minuto
	@Transactional
	public void verificarYActualizarEstadias() {
	    // 🔹 Obtener configuración
	    SetingRecepciones seting = setingRepository.findFirstByOrderByIdAsc();
	    LocalTime horaCorte = seting.getHoraFinalizacionDiaria();
	    LocalDate hoy = LocalDate.now();
	    LocalTime ahoraHora = LocalTime.now();

	    // 🔹 Última ejecución registrada
	    LocalDate ultimaEjecucion = seting.getFechaUltimaActualizacion();
	    // 🔹 Ejecutar si no se hizo hoy
	    boolean noEjecutadoHoy = (ultimaEjecucion == null || !ultimaEjecucion.equals(hoy));
	    boolean pasoHoraCorte = ahoraHora.isAfter(horaCorte);

	    if (noEjecutadoHoy && pasoHoraCorte) {

	        System.out.println("✔ Ejecutando actualización de estadías...");

	        // 🔹 Obtener todas las reservas activas
	        List<ReservacionCabecera> reservas = listar(entityRepository.getReservacionActivo());

	        for (ReservacionCabecera r : reservas) {

	            // 🔹 Calcular estadía total hasta hoy
	            int estadia = calcularEstadia(r.getFechaEntrada(), LocalDateTime.now(), horaCorte);

	            // 🔹 Actualizar totales de la reserva
	            r.setEstadia(estadia);
	            r.setTotalHabitacion(estadia * r.getPrecio());
	            r.setTotal(r.getTotalHabitacion() + r.getTotalProducto());

	            // 🔹 Persistir cambios
	            entityRepository.actualizarEstadiaModificacionDiaria(
	                r.getTotal(),
	                r.getTotalHabitacion(),
	                r.getEstadia(),
	                r.getId()
	            );
	        }

	        // 🔹 Guardar la fecha de ejecución para evitar recalcular el mismo día
	        seting.setFechaUltimaActualizacion(hoy);
	        setingRepository.save(seting);

	        System.out.println("✔ Actualización de estadías completada.");

	    } else {
	        System.out.println("⏳ Aún no corresponde ejecutar o ya se ejecutó hoy");
	    }
	}

	/**
	 * 🔹 Calcula la estadía tipo hotel hasta el momento actual
	 * @param fechaEntrada Fecha y hora de ingreso del huésped
	 * @param ahora Fecha y hora actual (o de cálculo)
	 * @param horaCorte Hora de corte diaria (por ejemplo, 12:00)
	 * @return Días de estadía contabilizados
	 */
	private int calcularEstadia(LocalDateTime fechaEntrada, LocalDateTime ahora, LocalTime horaCorte) {
	    LocalDate entrada = fechaEntrada.toLocalDate();
	    LocalDate actual = ahora.toLocalDate();

	    long dias = ChronoUnit.DAYS.between(entrada, actual);

	    // Siempre al menos 1 día
	    dias = Math.max(dias, 1);

	    // ⚠️ Sumar 1 día extra solo si la fecha es distinta a la de entrada y ya pasó la hora de corte
	    if (!entrada.equals(actual) && ahora.toLocalTime().isAfter(horaCorte)) {
	        dias++;
	    }

	    return (int) dias;
	}
	private List<ReservacionCabecera>listar(List<ReservacionCabecera> obj){
		List<ReservacionCabecera> res=new ArrayList<>();
		for(ReservacionCabecera ob:obj){
			ReservacionCabecera r=new ReservacionCabecera();
			r.setId(ob.getId());
			r.getFuncionarioRegistro().setId(ob.getFuncionarioRegistro().getId());
			r.getFuncionarioRegistro().getPersona().setNombre(ob.getFuncionarioRegistro().getPersona().getNombre()+ " "+ob.getFuncionarioRegistro().getPersona().getApellido() );
			r.getFuncionarioRegistro().getPersona().setCedula(ob.getFuncionarioRegistro().getPersona().getCedula());
			r.getFuncionarioFinalizacion().setId(ob.getFuncionarioRegistro().getId());
			r.getFuncionarioFinalizacion().getPersona().setNombre(ob.getFuncionarioFinalizacion().getPersona().getNombre()+" "+ ob.getFuncionarioRegistro().getPersona().getApellido());
			r.getFuncionarioFinalizacion().getPersona().setCedula(ob.getFuncionarioFinalizacion().getPersona().getCedula());
			r.getCliente().setId(ob.getCliente().getId());
			r.getCliente().getPersona().setNombre(ob.getCliente().getPersona().getNombre()+" "+ob.getCliente().getPersona().getApellido());
			r.getCliente().getPersona().setCedula(ob.getCliente().getPersona().getCedula());
			r.getDocumento().setId(ob.getDocumento().getId());
			r.getDocumento().setDescripcion(ob.getDocumento().getDescripcion());
			r.setEntrega(ob.getEntrega());
			r.setEstado(ob.getEstado());
			r.setTipo(ob.getTipo());
			r.setTotalHabitacion(ob.getTotalHabitacion());
			r.setTotalProducto(ob.getTotalProducto());
			r.setTotalLetra(ob.getTotalLetra());
			r.setDescripcionCombo(ob.getDescripcionCombo());
			r.setPrecio(ob.getPrecio());
			r.setOperacionCajaEntrega(ob.getOperacionCajaEntrega());
			r.setOperacionCaja(ob.getOperacionCaja());
			r.setEstadia(ob.getEstadia());
			r.setTotal(ob.getTotal());
			r.setFechaRegistro(ob.getFechaRegistro());
			//r.setFechaReservacion(ob.getFechaReservacion());
			r.setFechaReservacion(ob.getFechaReservacion());
			r.setFechaEntrada(ob.getFechaEntrada());
			r.setFechaFactura(ob.getFechaFactura());
			r.setReferenciaOperacion(ob.getReferenciaOperacion());

			res.add(r);
		}
		return res;
	}
}
