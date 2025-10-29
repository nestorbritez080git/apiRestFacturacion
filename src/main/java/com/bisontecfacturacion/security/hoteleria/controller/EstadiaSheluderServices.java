package com.bisontecfacturacion.security.hoteleria.controller;

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
	@Scheduled(fixedRate = 60000)
	@Transactional
	public void verificarYActualizarEstadias() {
		SetingRecepciones seting= new SetingRecepciones();
		seting = setingRepository.findFirstByOrderByIdAsc();
		
		LocalTime horaBD = seting.getHoraFinalizacionDiaria();
		LocalTime horaActualVerificar = LocalTime.now();

		 if (horaBD.getHour() == horaActualVerificar.getHour() &&
				horaBD.getMinute() == horaActualVerificar.getMinute()) {
		    System.out.println("Es exactamente la hora configurada. "+horaBD);
		    List<ReservacionCabecera> lisRetorno= new ArrayList<ReservacionCabecera>();
			lisRetorno= listar(entityRepository.getReservacionActivo());
			for (ReservacionCabecera f : lisRetorno) {
				System.out.println("impr estadia "+f.getDescripcionCombo()+ " fehca entrada: "+f.getFechaEntrada());
				// Fecha y hora de entrada
		        LocalDateTime fechaEntrada = f.getFechaEntrada();
		        System.out.println("DIA ENTRADA: "+fechaEntrada);

		        // Fecha y hora actual (ahora)
		        LocalDateTime ahora = LocalDateTime.now();
		        System.out.println("hora ahora : "+ahora);

		        // Calcular diferencia en días completos
		        long diasEntre = ChronoUnit.DAYS.between(fechaEntrada.toLocalDate(), ahora.toLocalDate());

		        // Si pasó del mediodía del último día, se suma un día más
		        LocalTime horaActual = ahora.toLocalTime();
		        if (horaActual.isAfter(LocalTime.NOON)) { // después de las 12:00
		            diasEntre++;
		        }

		        // Si por algún motivo da 0, al menos es 1 día
		        if (diasEntre < 1) {
		            diasEntre = 1;
		        }
		        System.out.println("DIA ENTERO: "+diasEntre);

		        // Actualizar estadía y totales
		        f.setEstadia((int) diasEntre);
		        f.setTotalHabitacion(f.getEstadia() * f.getPrecio());
		        f.setTotal(f.getTotalHabitacion() + f.getTotalProducto());
				entityRepository.actualizarEstadiaModificacionDiaria(f.getTotal(),f.getTotalHabitacion(), f.getEstadia(), f.getId());

			}
		} else {
		    System.out.println("no conicide la hora configurada. "+ horaBD);
		}
		
		
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
