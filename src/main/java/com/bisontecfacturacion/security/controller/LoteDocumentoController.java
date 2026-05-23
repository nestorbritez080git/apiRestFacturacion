package com.bisontecfacturacion.security.controller;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.NroDocumento;
import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.AutoImpresorDetalleVenta;
import com.bisontecfacturacion.security.model.LoteBoleta;
import com.bisontecfacturacion.security.model.LoteFactura;
import com.bisontecfacturacion.security.model.LoteNotaPedido;
import com.bisontecfacturacion.security.model.LoteTicket;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AutoImpresorDetalleVentaRepository;
import com.bisontecfacturacion.security.repository.AutoImpresorRepository;
import com.bisontecfacturacion.security.repository.LoteBoletaRepository;
import com.bisontecfacturacion.security.repository.LoteFacturaRepository;
import com.bisontecfacturacion.security.repository.LoteNotaPedidoRepository;
import com.bisontecfacturacion.security.repository.LoteTicketRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;



@RestController
@RequestMapping("/loteDocumento")
public class LoteDocumentoController {
	
	@Autowired
	private LoteFacturaRepository loteFacturaRepository;
	@Autowired
	private AutoImpresorRepository autoImpresorRepository;
	
	@Autowired
	private AutoImpresorDetalleVentaRepository autoImpresorDetalleRepository;
	
	@Autowired
	private LoteBoletaRepository loteBoletaRepository;
	@Autowired
	private LoteTicketRepository loteTicketRepository;
	@Autowired
	private LoteNotaPedidoRepository loteNotaPedidoRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	private static Formatter ft;
	
	@RequestMapping(method=RequestMethod.GET, value="/loteFactura")
	public List<LoteFactura> getAllLoteFactura(){
		return loteFacturaRepository.findByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/autoImpresor")
	public List<AutoImpresor> getAllLoteAutoImpresor(){
		 List<AutoImpresor> lista = autoImpresorRepository.getListaAutoImpresor();
		    return lista.stream().map(a -> {

		    	AutoImpresor dto = new AutoImpresor();

		        dto.setId(a.getId());
		        dto.setFecha(a.getFecha());
		        dto.setTimbrado(a.getTimbrado());
		        dto.setNumeroAutorizacion(a.getNumeroAutorizacion());
		        dto.setNumeroActual(a.getNumeroActual());
		        dto.setEstado(a.isEstado());
		        dto.getAutoImpresorTipoRemision().setDescripcion(a.getAutoImpresorTipoRemision().getDescripcion());
		        dto.setRuc(a.getRuc());
		        dto.setCodigoEstablecimiento(a.getCodigoEstablecimiento());
		        dto.setPuntoExpedicion(a.getPuntoExpedicion());
		        dto.setRangoInicio(a.getRangoInicio());
		        dto.setRangoFin(a.getRangoFin());
		        dto.setCantidadExpedicion(a.getCantidadExpedicion());
		        dto.setNumeroActual(a.getNumeroActual());
		        //dto.getAutoImpresorDetalleVentas().set(index, element)
		        return dto;
		    }).collect(Collectors.toList());
	}
	@RequestMapping(method=RequestMethod.GET, value="/autoImpresor/buscarId/{id}")
	public AutoImpresor getAutoImpresorPorId(@PathVariable int id){
		return autoImpresorRepository.getAutoImpresorPorId(id);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/autoImpresor/eliminarId/{id}")
	public void eliminarAutoImpresorPorId(@PathVariable int id){
		 autoImpresorRepository.deleteById(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteFacturaEstado")
	public ResponseEntity<?> getUltLoteFactura(){
		LoteFactura ff= new LoteFactura();
		try {
			ff= loteFacturaRepository.getEstadoLoteFactura(true);
			if (ff == null) {
				return new ResponseEntity<>(new CustomerErrorType("La cantidad de expedición de la factura ha alcanzado su límite.!!\nPor favor verifique le timbrado\nSi es requerido favor agregar nuevo número de timbrado válido "), HttpStatus.CONFLICT);
			}else {
				Date fec1= new Date();
				Date fec2= new Date();		
				Date fecHoy= new Date();
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		        String fechaInicioString = df.format(ff.getTimbradoInicio());
		        try {
		         fec1 = df.parse(fechaInicioString);
		        } catch (ParseException ex) {
		        }
		        String fechaFinalString = df.format(ff.getTimbradoFin());
		        try {
		            fec2 = df.parse(fechaFinalString);
		        } catch (ParseException ex) {
		        }
		        @SuppressWarnings("unused")
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaAux= new Date();
				fechaAux = FechaUtil.convertirFechaUtilATimeZone(new Date());
		        String fechaHoyRec= df.format(fechaAux );
		        try {
		           fecHoy = df.parse(fechaHoyRec);
		        } catch (ParseException ex) {
		        }

		        long fechaInicialMs = fec1.getTime();
		        long fechaFinalMs = fec2.getTime();
		        long fechaReferencia = fecHoy.getTime();
		        if( (fechaReferencia >= fechaInicialMs) && (fechaReferencia <= fechaFinalMs)){
					return new ResponseEntity<>(ff, HttpStatus.OK);	
				}else {
					return new ResponseEntity<>(new CustomerErrorType("No se puede Facturar.!!\nEl número de timbrado está vencido"), HttpStatus.CONFLICT);	
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(method=RequestMethod.POST, value="/loteFactura")
	public ResponseEntity<?> saveLoteFactura(@RequestBody LoteFactura entity){
		LoteFactura lFac= new LoteFactura();
		lFac =loteFacturaRepository.getEstadoLoteFacturaEstadoInactivo(true);
		if(lFac ==  null) {
			entity.setSerieActual(entity.getSerieInicial());
			return new ResponseEntity<>(loteFacturaRepository.save(entity), HttpStatus.OK);
		}
			if (lFac.getCantidadActual() >= lFac.getCantidadExpedicion()) {	
				lFac.setEstado(false);
				loteFacturaRepository.save(lFac);
				entity.setSerieActual(entity.getSerieInicial());
				return new ResponseEntity<>(loteFacturaRepository.save(entity), HttpStatus.OK);	
			}else {
				return new ResponseEntity<>(new CustomerErrorType("No se puede guardar un nuevo lote de Documento Factura, el número de Timbrado esta en vigencia todavia.! ."), HttpStatus.CONFLICT);
			}
	}
	@RequestMapping(method=RequestMethod.POST, value="/loteFactura/actualizarSerieActual")
	public void actualizarSerieActual(@RequestBody LoteFactura entity){
		loteFacturaRepository.actualizarSeriaActual(entity.getSerieActual(), entity.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/loteAutoImpresor")
	public ResponseEntity<?> saveLoteAutoImpresor(@RequestBody AutoImpresor entity, OAuth2Authentication authentication){
		try {
			
			if(entity.getCodigoEstablecimiento().length()<3) {
				return new ResponseEntity<>(new CustomerErrorType("EL CODIGO DEL ESTABLECIMINETO DEBE CONTENER MINIMO 3 DIGITOS"), HttpStatus.CONFLICT);
			}else if(entity.getPuntoExpedicion().length()<3) {
				return new ResponseEntity<>(new CustomerErrorType("EL PUNTO DE EXPEDICION DEBE CONTENER MINIMO 3 DIGITOS"), HttpStatus.CONFLICT);
			}else if(entity.getTimbrado().length()<7) {
				return new ResponseEntity<>(new CustomerErrorType("EL TIMBRADO DEBE TENER UN RANGO DE NUMERO MINIMO DE 7 DIGITOS"), HttpStatus.CONFLICT);
			}else if(entity.getRangoInicio() >= entity.getRangoFin()) {
				return new ResponseEntity<>(new CustomerErrorType("EL RANGO DE INICIO DEBE SER MENOR AL RANGO FIN"), HttpStatus.CONFLICT);
			}else if(entity.getNumeroActual() < entity.getRangoInicio() || entity.getNumeroActual() > entity.getRangoFin()) {
				return new ResponseEntity<>(new CustomerErrorType("EL NÚMERO ACTUAL ESTÁ FUERA DEL RANGO PERMITIDO"), HttpStatus.CONFLICT);
			}else if(entity.getFechaInicioVigencia().isAfter(entity.getFechaFinVigencia())) {
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA DE INICIO VIGENCIA NO PUEDE SER MAYOR QUE LA FECHA VIGENCIA FINAL"), HttpStatus.CONFLICT);
			}else if(entity.getFechaFinVigencia().isBefore(LocalDate.now())) {
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA DE FINAL DE VIGENCIA NO PUEDE ESTAR EN FECHA PASADA"), HttpStatus.CONFLICT);
			}
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			entity.getFuncionario().setId(usuario.getFuncionario().getId());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR AL INTETAR GUARDAR: "+e.getMessage()), HttpStatus.CONFLICT);

			
		}
		
		
		return new ResponseEntity<>(autoImpresorRepository.save(entity), HttpStatus.CREATED);	
	}
	
//	@RequestMapping(method=RequestMethod.POST, value="/loteTicket/actualizarNroTicket")
//	public void actualizarNroTicket(@RequestBody LoteTicket entity){
//		loteTicketRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//
//	@RequestMapping(method=RequestMethod.POST, value="/loteNotaPedido/actualizarNroNotaPedido")
//	public void actualizarNroNotaPedido(@RequestBody LoteTicket entity){
//		loteNotaPedidoRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//	
//	@RequestMapping(method=RequestMethod.POST, value="/loteBoleta/actualizarNroBoleta")
//	public void actualizarNroBoleta(@RequestBody LoteBoleta entity){
//		loteBoletaRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//	
	@RequestMapping(method=RequestMethod.POST, value="/loteBoleta")
	public LoteBoleta saveLoteBoleta(@RequestBody LoteBoleta entity){
		return loteBoletaRepository.save(entity);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteBoleta")
	public ResponseEntity<?> getLoteBoleta(){
		LoteBoleta l=loteBoletaRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de boleta en el --Menu Configuracion Lote Documento!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteBoletaRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/loteTicket")
	public LoteTicket saveLoteTicket(@RequestBody LoteTicket entity){
		return loteTicketRepository.save(entity);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteTicket")
	public ResponseEntity<?> getLoteTicket(){
		LoteTicket l=loteTicketRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de ticket en el --Menu Configuracion Lote Documento!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteTicketRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}

	@RequestMapping(method=RequestMethod.GET, value="/loteNotaPedido")
	public ResponseEntity<?> getLoteNotaPedido(){
		LoteNotaPedido l=loteNotaPedidoRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de la nota pedido en el --Menu Configuracion Lote Nota Pedido!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteNotaPedidoRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/obtenerLoteDocumento")
	public List<NroDocumento> getNroLoteDocumento(){
		List<NroDocumento> lista = new ArrayList<>();
		int tipo0 = 0;
		int tipo1 = 1;
		int tipo2 = 2;
		
		LoteBoleta loteBoleta = loteBoletaRepository.findTop1ByOrderByIdAsc();
		LoteTicket loteTicket = loteTicketRepository.findTop1ByOrderByIdAsc();
		LoteFactura loteFactura = loteFacturaRepository.findTop1ByOrderByIdAsc();
		
		for (int i = 0; i < 3; i++) {
			NroDocumento n = new NroDocumento();
			if (i == tipo0) {
				n.setDescripcion("1");
				
				String [] part= loteFactura.getSerieActual().split("-");
				String cod= part[2];
				String codActual=part[0]+"-"+part[1]+"-" + padF(Integer.parseInt(cod),7);
				n.setNro(codActual);
			}
			if (i == tipo1) {
				n.setDescripcion("2");
				n.setNro(padF(Integer.parseInt(loteBoleta.getNumeroActual()),12));
			}
			if (i == tipo2) {
				n.setDescripcion("3");
				n.setNro(padF(Integer.parseInt(loteTicket.getNumeroActual()),12));
			}
			
			lista.add(n);
		}
		
		return lista;
	}
	
	private static String padF(int numero, int size) {
		ft = new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/descargar/csv/{idAutorizacion}")
	public ResponseEntity<byte[]> exportCsv(@PathVariable int idAutorizacion) {
	    List<AutoImpresorDetalleVenta> lisDetalle =
	            autoImpresorDetalleRepository.consultaDetalleAutoImpresorPorCabeceraId(idAutorizacion);

	    StringBuilder sb = new StringBuilder();
	    DecimalFormat df = new DecimalFormat("#.##"); // sin separador de miles

	    // Encabezados
	    sb.append("ID;FECHA FACTURA;RUC;NUMERO;CONCEPTO;CLIENTE;ESTADO;TOTAL;")
	      .append("GRAVADO 10 %;LIQUIDACION 10 %;GRAVADO 5 %;LIQUIDACION 5 %;")
	      .append("EXENTAS;TOTAL IVA\n");

	    for (AutoImpresorDetalleVenta d : lisDetalle) {
	        String tpVenta = "";
	        if ("1".equals(d.getVenta().getTipo())) tpVenta = "VENTA CONTADO";
	        if ("2".equals(d.getVenta().getTipo())) tpVenta = "VENTA CREDITO";

	        sb.append(d.getId()).append(";")
	          .append(d.getVenta().getFechaFactura()).append(";")
	          .append(d.getVenta().getCliente().getPersona().getCedula()).append(";")
	          .append(d.getVenta().getNroDocumento()).append(";")
	          .append(tpVenta).append(";")
	          .append(d.getVenta().getCliente().getPersona().getNombre()).append(" ")
	          .append(d.getVenta().getCliente().getPersona().getApellido()).append(";")
	          .append(d.getEstado()).append(";")
	          .append(df.format(d.getVenta().getTotal())).append(";")
	          .append(df.format(d.getVenta().getGrabadoIvaDies())).append(";")
	          .append(df.format(d.getVenta().getTotalIvaDies())).append(";")
	          .append(df.format(d.getVenta().getGrabadoIvaCinco())).append(";")
	          .append(df.format(d.getVenta().getTotalIvaCinco())).append(";")
	          .append(df.format(d.getVenta().getGrabadoExcenta())).append(";")
	          .append(df.format(d.getVenta().getTotalIva())).append("\n");
	    }

	    byte[] csvBytes = sb.toString().getBytes(StandardCharsets.UTF_8);

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detalle.csv")
	            .contentType(MediaType.parseMediaType("text/csv"))
	            .body(csvBytes);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/descargar/listado/rangoFecha/{idAutorizacion}/{fechaInicio}/{fechaFin}")
	public ResponseEntity<?> getReporteRemisionesRangoFechaListado(
	        @PathVariable int idAutorizacion,
	        @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fechaInicio,
	        @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fechaFin) throws Exception {
	    
	    // Convertir a LocalDateTime con hora exacta
	    LocalDateTime desde = fechaInicio.atStartOfDay(); // 00:00:00
	    LocalDateTime hasta = fechaFin.atTime(23, 59, 59); // 23:59:59
	 // convertir a java.util.Date
	    Date desdeDate = java.sql.Timestamp.valueOf(desde);
	    Date hastaDate = java.sql.Timestamp.valueOf(hasta);
	    List<AutoImpresor> auto= autoImpresorRepository.consultarRemisionesFacturaRangoFecha(idAutorizacion, desdeDate, hastaDate);	    // Aquí llamarías a tu servicio para filtrar por rango de fechas
	    // ejemplo: List<Remision> remisiones = remisionService.getByRangoFecha(idAutorizacion, desde, hasta);
	    
	    AutoImpresor autoretorno = auto.isEmpty() ? null : auto.get(0);
	    if(autoretorno.getAutoImpresorDetalleVentas().size()>0) {
	    	return ResponseEntity.ok(autoretorno);
	    }else {
			return new ResponseEntity<>(new CustomerErrorType("No hay lista en el rango seleccionada Fecha Inicio: "+desde+", Fecha Fin:"+hasta), HttpStatus.CONFLICT);
	    }
	    // reemplazar con tu lógica real
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/descargar/xls/rangoFecha/{idAutorizacion}/{fechaInicio}/{fechaFin}")
	public ResponseEntity<?> exportXlsPorRangoFecha(
			@PathVariable int idAutorizacion, 
			@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fechaInicio,
	        @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate fechaFin) throws Exception {
	    
	    // Convertir a LocalDateTime con hora exacta
	    LocalDateTime desde = fechaInicio.atStartOfDay(); // 00:00:00
	    LocalDateTime hasta = fechaFin.atTime(23, 59, 59); // 23:59:59
	 // convertir a java.util.Date
	    Date desdeDate = java.sql.Timestamp.valueOf(desde);
	    Date hastaDate = java.sql.Timestamp.valueOf(hasta);
		
	    List<AutoImpresor> auto =  autoImpresorRepository.consultarRemisionesFacturaRangoFecha(idAutorizacion, desdeDate, hastaDate);
	    AutoImpresor autoretorno = auto.isEmpty() ? null : auto.get(0);
	    if(autoretorno.getAutoImpresorDetalleVentas().size()>0) {
	    	 try (Workbook workbook = new XSSFWorkbook()) {
	 	        Sheet sheet = workbook.createSheet("Detalle");

	 	        // Estilos básicos
	 	        CellStyle headerStyle = workbook.createCellStyle();
	 	        Font font = workbook.createFont();
	 	        font.setBold(true);
	 	        headerStyle.setFont(font);

	 	        CellStyle numberStyle = workbook.createCellStyle();
	 	        DataFormat format = workbook.createDataFormat();
	 	        numberStyle.setDataFormat(format.getFormat("#,##0.00")); // formato numérico

	 	        // Encabezados
	 	        String[] headers = {
	 	            "ID", "FECHA FACTURA", "RUC", "NUMERO", "CONCEPTO", "CLIENTE", "ESTADO",
	 	            "TOTAL", "GRAVADO 10 %", "LIQUIDACION 10 %", "GRAVADO 5 %", "LIQUIDACION 5 %",
	 	            "EXENTA", "TOTAL IVA"
	 	        };

	 	        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
	 	        for (int i = 0; i < headers.length; i++) {
	 	            Cell cell = headerRow.createCell(i);
	 	            cell.setCellValue(headers[i]);
	 	            cell.setCellStyle(headerStyle);
	 	        }

	 	        // Data
	 	        int rowIdx = 1;
	 	        for (AutoImpresorDetalleVenta d : autoretorno.getAutoImpresorDetalleVentas()) {
	 	            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
	 	            String tpVenta = "";
	 	            if ("1".equals(d.getVenta().getTipo()) || "CONTADO".equalsIgnoreCase(d.getVenta().getTipo())) tpVenta = "VENTA CONTADO";
	 	            if ("2".equals(d.getVenta().getTipo()) || "CREDITO".equalsIgnoreCase(d.getVenta().getTipo())) tpVenta = "VENTA CREDITO";
	 	            if ("3".equals(d.getVenta().getTipo()) || "NOTA CREDITO".equalsIgnoreCase(d.getVenta().getTipo())) tpVenta = "VENTA NOTA CREDITO";

	 	            int col = 0;
	 	            row.createCell(col++).setCellValue(d.getId());
	 	            row.createCell(col++).setCellValue(d.getVenta().getFechaFactura().toString());
	 	            row.createCell(col++).setCellValue(d.getVenta().getCliente().getPersona().getCedula());
	 	            row.createCell(col++).setCellValue(d.getVenta().getNroDocumento());
	 	            row.createCell(col++).setCellValue(tpVenta);
	 	            row.createCell(col++).setCellValue(d.getVenta().getCliente().getPersona().getNombre() + " " +
	 	                                               d.getVenta().getCliente().getPersona().getApellido());
	 	            row.createCell(col++).setCellValue(d.getEstado());

	 	            // Números con formato
	 	            Cell total = row.createCell(col++);
	 	            total.setCellValue(d.getVenta().getTotal().doubleValue());
	 	            total.setCellStyle(numberStyle);

	 	            Cell g10 = row.createCell(col++);
	 	            g10.setCellValue(d.getVenta().getGrabadoIvaDies().doubleValue());
	 	            g10.setCellStyle(numberStyle);

	 	            Cell l10 = row.createCell(col++);
	 	            l10.setCellValue(d.getVenta().getTotalIvaDies().doubleValue());
	 	            l10.setCellStyle(numberStyle);

	 	            Cell g5 = row.createCell(col++);
	 	            g5.setCellValue(d.getVenta().getGrabadoIvaCinco().doubleValue());
	 	            g5.setCellStyle(numberStyle);

	 	            Cell l5 = row.createCell(col++);
	 	            l5.setCellValue(d.getVenta().getTotalIvaCinco().doubleValue());
	 	            l5.setCellStyle(numberStyle);

	 	            Cell ex = row.createCell(col++);
	 	            ex.setCellValue(d.getVenta().getGrabadoExcenta().doubleValue());
	 	            ex.setCellStyle(numberStyle);

	 	            Cell iva = row.createCell(col++);
	 	            iva.setCellValue(d.getVenta().getTotalIva().doubleValue());
	 	            iva.setCellStyle(numberStyle);
	 	        }

	 	        // Autoajustar columnas
	 	        for (int i = 0; i < headers.length; i++) {
	 	            sheet.autoSizeColumn(i);
	 	        }

	 	        // Escribir a byte[]
	 	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	 	        workbook.write(out);
	 	        byte[] bytes = out.toByteArray();

	 	        return ResponseEntity.ok()
	 	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detalle.xlsx")
	 	                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	 	                .body(bytes);
	 	    }
	    }else {
			return new ResponseEntity<>(new CustomerErrorType("No hay lista en el rango seleccionada Fecha Inicio: "+desde+", Fecha Fin:"+hasta), HttpStatus.CONFLICT);
	    }
	   
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/descargar/xls/{idAutorizacion}")
	public ResponseEntity<byte[]> exportXls(@PathVariable int idAutorizacion) throws Exception {
	    List<AutoImpresorDetalleVenta> lisDetalle =
	            autoImpresorDetalleRepository.consultaDetalleAutoImpresorPorCabeceraId(idAutorizacion);

	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Detalle");

	        // Estilos básicos
	        CellStyle headerStyle = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);

	        CellStyle numberStyle = workbook.createCellStyle();
	        DataFormat format = workbook.createDataFormat();
	        numberStyle.setDataFormat(format.getFormat("#,##0.00")); // formato numérico

	        // Encabezados
	        String[] headers = {
	            "ID", "FECHA FACTURA", "RUC", "NUMERO", "CONCEPTO", "CLIENTE", "ESTADO",
	            "TOTAL", "GRAVADO 10 %", "LIQUIDACION 10 %", "GRAVADO 5 %", "LIQUIDACION 5 %",
	            "EXENTA", "TOTAL IVA"
	        };

	        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(headers[i]);
	            cell.setCellStyle(headerStyle);
	        }

	        // Data
	        int rowIdx = 1;
	        for (AutoImpresorDetalleVenta d : lisDetalle) {
	            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
	            String tpVenta = "";
	            if ("1".equals(d.getVenta().getTipo())) tpVenta = "VENTA CONTADO";
	            if ("2".equals(d.getVenta().getTipo())) tpVenta = "VENTA CREDITO";
	            if ("3".equals(d.getVenta().getTipo())) tpVenta = "VENTA NOTA CREDITO";

	            int col = 0;
	            row.createCell(col++).setCellValue(d.getId());
	            row.createCell(col++).setCellValue(d.getVenta().getFechaFactura().toString());
	            row.createCell(col++).setCellValue(d.getVenta().getCliente().getPersona().getCedula());
	            row.createCell(col++).setCellValue(d.getVenta().getNroDocumento());
	            row.createCell(col++).setCellValue(tpVenta);
	            row.createCell(col++).setCellValue(d.getVenta().getCliente().getPersona().getNombre() + " " +
	                                               d.getVenta().getCliente().getPersona().getApellido());
	            row.createCell(col++).setCellValue(d.getEstado());

	            // Números con formato
	            Cell total = row.createCell(col++);
	            total.setCellValue(d.getVenta().getTotal().doubleValue());
	            total.setCellStyle(numberStyle);

	            Cell g10 = row.createCell(col++);
	            g10.setCellValue(d.getVenta().getGrabadoIvaDies().doubleValue());
	            g10.setCellStyle(numberStyle);

	            Cell l10 = row.createCell(col++);
	            l10.setCellValue(d.getVenta().getTotalIvaDies().doubleValue());
	            l10.setCellStyle(numberStyle);

	            Cell g5 = row.createCell(col++);
	            g5.setCellValue(d.getVenta().getGrabadoIvaCinco().doubleValue());
	            g5.setCellStyle(numberStyle);

	            Cell l5 = row.createCell(col++);
	            l5.setCellValue(d.getVenta().getTotalIvaCinco().doubleValue());
	            l5.setCellStyle(numberStyle);

	            Cell ex = row.createCell(col++);
	            ex.setCellValue(d.getVenta().getGrabadoExcenta().doubleValue());
	            ex.setCellStyle(numberStyle);

	            Cell iva = row.createCell(col++);
	            iva.setCellValue(d.getVenta().getTotalIva().doubleValue());
	            iva.setCellStyle(numberStyle);
	        }

	        // Autoajustar columnas
	        for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        // Escribir a byte[]
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        workbook.write(out);
	        byte[] bytes = out.toByteArray();

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detalle.xlsx")
	                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
	                .body(bytes);
	    }
	}
	/*
	@RequestMapping(method = RequestMethod.GET, value = "/descargar/pdf/{idAutorizacion}")
	public ResponseEntity<byte[]> exportPdf(@PathVariable int idAutorizacion) {
	    List<AutoImpresorDetalleVenta> lisDetalle =
	            autoImpresorDetalleRepository.consultaDetalleAutoImpresorPorCabeceraId(idAutorizacion);

	    DecimalFormat df = new DecimalFormat("#.##");

	    try {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        Document document = new Document(PageSize.A4.rotate());
	        PdfWriter.getInstance(document, baos);
	        document.open();

	        // Fuente básica
	        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);

	        // Tabla con columnas
	        PdfPTable table = new PdfPTable(14); // 14 columnas
	        table.setWidthPercentage(100);

	        // Encabezados
	        String[] headers = {"ID", "FECHA FACTURA", "RUC", "NUMERO", "CONCEPTO",
	                            "CLIENTE", "ESTADO", "TOTAL", "GRAVADO 10%", "LIQUIDACION 10%",
	                            "GRAVADO 5%", "LIQUIDACION 5%", "EXENTAS", "TOTAL IVA"};

	        for (String h : headers) {
	            PdfPCell cell = new PdfPCell(new Phrase(h, font));
	            cell.setBackgroundColor(Color.LIGHT_GRAY);
	            table.addCell(cell);
	        }

	        // Datos
	        for (AutoImpresorDetalleVenta d : lisDetalle) {
	            String tpVenta = "";
	            if ("1".equals(d.getVenta().getTipo())) tpVenta = "VENTA CONTADO";
	            if ("2".equals(d.getVenta().getTipo())) tpVenta = "VENTA CREDITO";

	            table.addCell(new Phrase(String.valueOf(d.getId()), font));
	            table.addCell(new Phrase(String.valueOf(d.getVenta().getFechaFactura()), font));
	            table.addCell(new Phrase(d.getVenta().getCliente().getPersona().getCedula(), font));
	            table.addCell(new Phrase(d.getVenta().getNroDocumento(), font));
	            table.addCell(new Phrase(tpVenta, font));
	            table.addCell(new Phrase(d.getVenta().getCliente().getPersona().getNombre() + " " +
	                                     d.getVenta().getCliente().getPersona().getApellido(), font));
	            table.addCell(new Phrase(d.getEstado(), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getTotal()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getGrabadoIvaDies()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getTotalIvaDies()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getGrabadoIvaCinco()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getTotalIvaCinco()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getGrabadoExcenta()), font));
	            table.addCell(new Phrase(df.format(d.getVenta().getTotalIva()), font));
	        }

	        document.add(table);
	        document.close();

	        byte[] pdfBytes = baos.toByteArray();

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detalle.pdf")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(pdfBytes);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}*/

}
