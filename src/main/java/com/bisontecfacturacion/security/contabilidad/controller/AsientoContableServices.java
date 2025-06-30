package com.bisontecfacturacion.security.contabilidad.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bisontecfacturacion.security.contabilidad.model.AsientoContable;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDTO;
import com.bisontecfacturacion.security.contabilidad.model.AsientoContableDetalle;
import com.bisontecfacturacion.security.contabilidad.model.ConceptoCuentaContable;
import com.bisontecfacturacion.security.contabilidad.repository.AsientoContableDetalleRepositor;
import com.bisontecfacturacion.security.contabilidad.repository.AsientoContableRepository;
import com.bisontecfacturacion.security.contabilidad.repository.ConceptoCuentaContableRepository;
import com.bisontecfacturacion.security.contabilidad.repository.CuentaContableRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;


@Service
public class AsientoContableServices {
	 	@Autowired
	    private AsientoContableRepository asientoContableRepository;

	    @Autowired
	    private AsientoContableDetalleRepositor detalleAsientoRepository;

	    @Autowired
	    private ConceptoCuentaContableRepository conceptoCuentaContableRepository;
	    @Autowired
	    private CuentaContableRepository cuentaContableRepository;
	    
	   @SuppressWarnings("unused")
	public ResponseEntity<?> guardarAsiento(AsientoContableDTO request) throws Exception {	
	        // Obtener las reglas contables asociadas al concepto
	        List<ConceptoCuentaContable> reglas = conceptoCuentaContableRepository.consultarConceptoCuentaContablePorIdConcepto(request.getConceptoId());
	        if (reglas == null || reglas.isEmpty()) {
				return new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRO NINGUNA REGLAS APLICABLE PARA GENERAR DETALLE ASIENTO"),HttpStatus.CONFLICT);
	        }
	          Set<String> basesRequeridas = reglas.stream()
	        	    .map(ConceptoCuentaContable::getBase)
	        	    .collect(Collectors.toSet());
	        	List<String> basesFaltantes = basesRequeridas.stream()
	        	    .filter(base -> !request.getMontos().containsKey(base))
	        	    .collect(Collectors.toList());
	        	if (!basesFaltantes.isEmpty()) {
	        	    String mensajeError = "FALTAN LAS SIGUIENTES BASES EN LA PETICIÓN: " + String.join(", ", basesFaltantes);
	        	    return new ResponseEntity<>(new CustomerErrorType(mensajeError), HttpStatus.BAD_REQUEST);
	        	}
	            System.out.println("Bases requeridas: " + basesRequeridas);
		        System.out.println("Bases en request: " + request.getMontos().keySet());
		        Set<String> basesEnviadas = request.getMontos().keySet();

		     // Bases que faltan en el request
		     Set<String> faltantes = new HashSet<>(basesRequeridas);
		     faltantes.removeAll(basesEnviadas);

		     // Bases extras que no se esperan
		     Set<String> extras = new HashSet<>(basesEnviadas);
		     extras.removeAll(basesRequeridas);

		     if (!faltantes.isEmpty() || !extras.isEmpty()) {
		         String mensaje = "";
		         if (!faltantes.isEmpty()) {
		             mensaje += "Faltan bases requeridas: " + String.join(", ", faltantes) + ". ";
		         }
		         if (!extras.isEmpty()) {
		             mensaje += "Bases no esperadas: " + String.join(", ", extras) + ".";
		         }
		         return new ResponseEntity<>(new CustomerErrorType(mensaje.trim()), HttpStatus.BAD_REQUEST);
		     }
	        // Variables para control
	        BigDecimal totalDebe = BigDecimal.ZERO;
	        BigDecimal totalHaber = BigDecimal.ZERO;
	        List<AsientoContableDetalle> detallesPendientes = new ArrayList<>();
	        // Crear los detalles del asiento
	        for (ConceptoCuentaContable regla : reglas) {
	        	String base = regla.getBase();
	        	BigDecimal baseValor = request.getMontos().get(base);
	            BigDecimal porcentaje = regla.getPorcentaje();
	            BigDecimal montoCalculado = baseValor.multiply(porcentaje).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
	         // ⛔ Evitar generar detalle si el monto es cero
	            if (montoCalculado.compareTo(BigDecimal.ZERO) == 0) {
	                System.out.println("⚠️ Monto cero omitido para base: " + base + ", cuenta: " + regla.getCuentaContable().getNombre());
	                continue;
	            }
	            
	            AsientoContableDetalle detalle = new AsientoContableDetalle();
	            //detalle.setAsientoContable(a);
	            detalle.setFechaRegistro(LocalDateTime.now());
	            detalle.setCuentaContable(regla.getCuentaContable());
	            detalle.setDescripcion(regla.getNaturaleza());
	            if ("DEBE".equalsIgnoreCase(regla.getNaturaleza())) {
	                detalle.setDebe(montoCalculado);
	                detalle.setHaber(BigDecimal.ZERO);
	                totalDebe = totalDebe.add(montoCalculado);
	            } else if ("HABER".equalsIgnoreCase(regla.getNaturaleza())) {
	                detalle.setHaber(montoCalculado);
	                detalle.setDebe(BigDecimal.ZERO);
	                totalHaber = totalHaber.add(montoCalculado);
	            } else {
	        	    return new ResponseEntity<>(new CustomerErrorType("Naturaleza desconocida: " + regla.getNaturaleza()+ ", en la intercepcion entre el "+regla.getConcepto().getDescripcion()+ " y "+regla.getCuentaContable().getNombre()), HttpStatus.BAD_REQUEST);
	            }
	            detallesPendientes.add(detalle);
	        }
	     // Validar y ajustar diferencia por redondeo
	        BigDecimal diferencia = totalDebe.subtract(totalHaber);
	        if (diferencia.abs().compareTo(new BigDecimal("0.01")) > 0) {
	            return new ResponseEntity<>(new CustomerErrorType("Diferencia entre DEBE y HABER supera el límite permitido"), HttpStatus.BAD_REQUEST);
	        }
	        if (diferencia.compareTo(BigDecimal.ZERO) != 0 && !detallesPendientes.isEmpty()) {
	            // Ajustar el último detalle (puedes elegir DEBE o HABER según el caso)
	            AsientoContableDetalle ultimo = detallesPendientes.get(detallesPendientes.size() - 1);
	            if (diferencia.compareTo(BigDecimal.ZERO) > 0) {
	                // DEBE > HABER → aumentar el HABER
	                ultimo.setHaber(ultimo.getHaber().add(diferencia));
	            } else {
	                // HABER > DEBE → aumentar el DEBE
	                ultimo.setDebe(ultimo.getDebe().add(diferencia.abs()));
	            }
	        }
	        AsientoContable asiento = new AsientoContable();
	        asiento.setFechaRegistro(LocalDateTime.now());
	        asiento.setFechaModificacion(LocalDateTime.now());
	        asiento.setTipoReferencia(request.getTipoReferencia());
	        asiento.setIdReferencia(request.getReferenciaId());
	        asiento.getConcepto().setId(request.getConceptoId());
	        asiento.getFuncionarioRegistro().setId(request.getFuncionarioRegistroId());
	        asiento.getFuncionarioModificacion().setId(request.getFuncionarioModificacionId());
	        asiento.setTotalDebe(totalDebe);
	        asiento.setTotalHaber(totalHaber);
	        AsientoContable a = asientoContableRepository.save(asiento);
	        if (a == null) {
	            System.out.println("NO SE PUDO GUARDAR CABECERA DE ASIENTO");
	            return new ResponseEntity<>(new CustomerErrorType("NO-SAVE"), HttpStatus.CONFLICT);
	        }
	        // Guardar detalleccoçps si la cabecera se guardó correctamente
	        for (AsientoContableDetalle detalle : detallesPendientes) {
	            detalle.setAsientoContable(a);
	            detalleAsientoRepository.save(detalle);
	        }
	        System.out.println("ASIENTO GUARDADO: " + a.getIdReferencia());
	        return new ResponseEntity<>(new CustomerErrorType("SAVE"), HttpStatus.CREATED);
	    }
}
