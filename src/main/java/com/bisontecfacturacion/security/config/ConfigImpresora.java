package com.bisontecfacturacion.security.config;

import java.awt.print.PrinterJob;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;


import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.BarcodePrintRequest;
import com.bisontecfacturacion.security.auxiliar.PrintResponse;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.UtilidadPrecio;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.repository.UtilidadPrecioRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


@RestController
@RequestMapping("config")
public class ConfigImpresora {

	@Autowired
	private ImpresoraRepository repository;
	@Autowired
	private UtilidadPrecioRepository utilidadPreciorepository;

	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;

	@Autowired
	private ReporteConfigRepository reporteConfigRepository;
	
	@RequestMapping(method=RequestMethod.GET, value="/terminalAll")
	public List<TerminalConfigImpresora> consultarTerminalBD(){
		List<TerminalConfigImpresora> listRetorno= new ArrayList<>();
		List<TerminalConfigImpresora> listObj= terminalRepository.findAll();
		for (TerminalConfigImpresora ter : listObj) {
			ter.setAutoImpresor(null);
			listRetorno.add(ter);
		}
		return listRetorno;
	}
	@RequestMapping(method = RequestMethod.PUT, value = "/terminal/conexion")
	public ResponseEntity<?> registrarConexionTerminal(
		        @RequestBody TerminalConexionRequest request) {
		    try {
		       Optional<TerminalConfigImpresora> terminalOptional= terminalRepository.findByNumeroTerminal(request.getNumeroTerminal());
		        if (!terminalOptional.isPresent()) {
		            return ResponseEntity.badRequest().body("No existe la terminal: "+ request.getNumeroTerminal());
		        }
		        TerminalConfigImpresora terminal = terminalOptional.get();
		        // Actualizar IP
		        terminal.setIp(request.getIp());
		        // Actualizar nombre del equipo
		        terminal.setNombreEquipo(request.getNombreEquipo());
		        // Actualizar última conexión
		        terminal.setUltimaConexion(new Date());
		        terminalRepository.save(terminal);
		        return ResponseEntity.ok(terminal);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar conexión de terminal");
		    }
	}
	private String[] dividirDescripcion(
	        String texto,
	        int maxCaracteres) {

	    if (texto == null ||
	            texto.trim().isEmpty()) {

	        return new String[]{""};
	    }

	    texto = texto.trim();


	    /*
	     * Si entra en una sola línea
	     */
	    if (texto.length() <= maxCaracteres) {

	        return new String[]{
	                texto
	        };
	    }


	    /*
	     * PRIMERA PARTE
	     */
	    String primera =
	            texto.substring(
	                    0,
	                    maxCaracteres
	            );


	    /*
	     * Cortar por espacio
	     */
	    int espacio =
	            primera.lastIndexOf(" ");


	    if (espacio > 0) {

	        primera =
	                primera.substring(
	                        0,
	                        espacio
	                );
	    }


	    /*
	     * Segunda parte
	     */
	    int inicio =
	            primera.length();


	    while (
	            inicio < texto.length()
	            &&
	            texto.charAt(inicio) == ' '
	    ) {

	        inicio++;
	    }


	    if (inicio >= texto.length()) {

	        return new String[]{
	                primera
	        };
	    }


	    String segunda =
	            texto.substring(
	                    inicio
	            );


	    /*
	     * SEGUNDA LINEA MAXIMO
	     */

	    if (segunda.length() > maxCaracteres) {

	        segunda =
	                segunda.substring(
	                        0,
	                        maxCaracteres
	                );


	        int ultimoEspacio =
	                segunda.lastIndexOf(" ");


	        if (ultimoEspacio > 0) {

	            segunda =
	                    segunda.substring(
	                            0,
	                            ultimoEspacio
	                    );
	        }
	    }


	    return new String[]{
	            primera,
	            segunda
	    };
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/impresion/barcode")
	public ResponseEntity<?> imprimirCodigoBarras(@RequestBody BarcodePrintRequest request) {
	    System.out.println("==============================================" );
	    System.out.println("===== IMPRESION 3 ETIQUETAS SAT TT448-2 =====" );
	    System.out.println("=============================================="  );
	    System.out.println("Impresora: " + request.getPrinter());
	    System.out.println("Descripcion: " + request.getDescripcion());
	    System.out.println("Codigo: " + request.getCodigo());
	    System.out.println("Precio: " + request.getPrecio());
	    System.out.println("Cantidad: " + request.getCantidad());
	    try {
	        // =====================================================
	        // BUSCAR IMPRESORA
	        // =====================================================
	        PrintService impresora = buscarImpresora(request.getPrinter());
	        if (impresora == null) {
	            return ResponseEntity
	                    .badRequest()
	                    .body(
	                            new PrintResponse(
	                                    false,
	                                    "No se encontró la impresora: "
	                                            + request.getPrinter()
	                            )
	                    );
	        }

	        System.out.println(
	                "IMPRESORA ENCONTRADA: "
	                        + impresora.getName()
	        );


	        // =====================================================
	        // CONFIGURACION SAT TT448-2
	        // =====================================================

	        int dpi = 203;

	        /*
	         * 101.6 mm x 203 DPI
	         * aproximadamente 812 dots
	         */

	      
	        /*
	         * POSICION X DE CADA ETIQUETA
	         */

	        int anchoBanda = 864;
	        int altoBanda = 812;

	        int anchoEtiqueta =	 anchoBanda / 3; // 270

	        int margenIzquierdoGeneral = 50;																																																																																																																																																																																																									

	        int x1 = margenIzquierdoGeneral;

	        int x2 = margenIzquierdoGeneral
	                + anchoEtiqueta;
	        int x3 = margenIzquierdoGeneral
	                + (anchoEtiqueta * 2);

	        System.out.println(
	                "----------------------------------------------"
	        );
	        System.out.println(
	                "CONFIGURACION ZPL"
	        );

	        System.out.println(								
	                "DPI: " + dpi
	        );

	        System.out.println(
	                "Ancho banda: "
	                        + anchoBanda
	        );

	        System.out.println(
	                "Alto banda: "
	                        + altoBanda
	        );

	        System.out.println(
	                "Ancho etiqueta: "
	                        + anchoEtiqueta
	        );

	        System.out.println(
	                "X1: " + x1
	        );

	        System.out.println(
	                "X2: " + x2
	        );

	        System.out.println(
	                "X3: " + x3
	        );


	        // =====================================================
	        // CREAR ZPL
	        // =====================================================

	        StringBuilder zpl =
	                new StringBuilder();


	        // =====================================================
	        // INICIO ZPL
	        // =====================================================

	        zpl.append("^XA\n");

	        /*
	         * Ancho total
	         */

	        zpl.append("^PW")
	                .append(anchoBanda)
	                .append("\n");

	        /*
	         * Alto total
	         */

	        zpl.append("^LL")
	                .append(altoBanda)
	                .append("\n");

	        /*
	         * Origen
	         */

	        zpl.append("^LH0,0\n");


	        // =====================================================
	        // POSICIONES DE LAS 3 ETIQUETAS
	        // =====================================================

	        int[] posicionesX = {
	                x1,
	                x2,
	                x3
	        };


	        // =====================================================
	        // GENERAR LAS 3 ETIQUETAS
	        // =====================================================

	        for (int i = 0;i < posicionesX.length; i++) {
	           
	            int x = posicionesX[i];
                 /*
	             * POSICION VERTICAL INICIAL
	             */
	            int y = 15;
	            System.out.println("POSICION FOR X : "+i + "xxxxx: "+x+ "  yyyyy: "+y);

	            // =================================================
	            // DESCRIPCION
	            // =================================================
	           
	            
	         // =================================================
	         // DESCRIPCION - MAXIMO 2 LINEAS
	         // =================================================

	         if (request.getIsDescripcion()) {

	             String descripcion =
	                     limpiarZpl(
	                             request.getDescripcion()
	                     );

	             /*
	              * Dividir manualmente en máximo 2 líneas
	              */
	             String[] lineas =
	                     dividirDescripcion(
	                             descripcion,
	                             20
	                     );

	             /*
	              * MARGEN IZQUIERDO
	              */
	             int margenDescripcion = 10;

	             int xDescripcion =
	                     x + margenDescripcion;

	             /*
	              * TAMAÑO
	              */
	             int fuenteDescripcion = 20;

	             /*
	              * PRIMERA LINEA
	              */
	             zpl.append("^FO")
	                     .append(xDescripcion)
	                     .append(",")
	                     .append(y)
	                     .append("\n");

	             zpl.append("^A0N,")
	                     .append(fuenteDescripcion)
	                     .append(",")
	                     .append(fuenteDescripcion)
	                     .append("\n");

	             zpl.append("^FD")
	                     .append(lineas[0])
	                     .append("^FS\n");


	             /*
	              * SEGUNDA LINEA
	              */
	             if (lineas.length > 1) {

	                 zpl.append("^FO")
	                         .append(xDescripcion)
	                         .append(",")
	                         .append(y + 22)
	                         .append("\n");

	                 zpl.append("^A0N,")
	                         .append(fuenteDescripcion)
	                         .append(",")
	                         .append(fuenteDescripcion)
	                         .append("\n");

	                 zpl.append("^FD")
	                         .append(lineas[1])
	                         .append("^FS\n");
	             }


	             /*
	              * RESERVAR SIEMPRE EL ESPACIO
	              * PARA LAS 2 LINEAS
	              */
	             y += 48;
	         }
	            
	            // =================================================
	            // CODIGO DE BARRAS
	            // =================================================

	            /*
	             * Dejamos un pequeño margen
	             */

	            int barcodeX =  x + 5;


	            /*
	             * POSICION BARCODE
	             */

	            zpl.append("^FO")
	                    .append(barcodeX)
	                    .append(",")
	                    .append(y)
	                    .append("\n");


	            /*
	             * ANCHO DE BARRA
	             *
	             * 1 = pequeño
	             */

	            zpl.append("^BY1,2,42\n");


	            /*
	             * CODE 128
	             */

	            zpl.append("^BCN,42,N,N,N\n");


	            /*
	             * CODIGO
	             */

	            zpl.append("^FD")
	                    .append(
	                            limpiarZpl(
	                                    request.getCodigo()
	                            )
	                    )
	                    .append("^FS\n");


	            /*
	             * ESPACIO DESPUES BARCODE
	             */

	            y += 48;


	            // =================================================
	            // CODIGO NUMERICO
	            // =================================================

	            if (request.getIsCodigo()) {
	                 String codigo =  limpiarZpl(request.getCodigo() );
	                 int margenCodigo = 5;
	                 int xCodigo = x + margenCodigo;

	                int anchoCodigo = anchoEtiqueta - margenCodigo - 10;
	                int fuenteCodigo = 20;
	                zpl.append("^FO")
	                        .append(xCodigo)
	                        .append(",")
	                        .append(y)
	                        .append("\n");
	                zpl.append("^A0N,")
	                        .append(fuenteCodigo)
	                        .append(",")
	                        .append(fuenteCodigo)
	                        .append("\n");
	                zpl.append("^FB")
	                        .append(anchoCodigo)
	                        .append(",1,0,L,0\n");
	                zpl.append("^FD")
	                        .append(codigo)
	                        .append("^FS\n");

	                /*
	                 * ESPACIO PARA PRECIO
	                 */
	                y += 23;
	            }
	            // =================================================
	            // PRECIO
	            // =================================================
	            if (request.getIsPrecio()) {
	                String textoPrecio =
	                        "Gs. " + String.format( new java.util.Locale( "es", "PY" ), "%,.0f",request.getPrecio());
                int margenPrecio = 5;
	            int xPrecio = x + margenPrecio;
	            int anchoPrecio = anchoEtiqueta - margenPrecio - 10;

	             int fuentePrecio = 20;

	             zpl.append("^FO")
	                        .append(xPrecio)
	                        .append(",")
	                        .append(y)
	                        .append("\n");
	                zpl.append("^A0N,")
	                        .append(fuentePrecio)
	                        .append(",")
	                        .append(fuentePrecio)
	                        .append("\n");
	                zpl.append("^FB")
	                        .append(anchoPrecio)
	                        .append(",1,0,L,0\n");
	                zpl.append("^FD")
	                        .append(textoPrecio)
	                        .append("^FS\n");
	            }
	        }


	        // =====================================================
	        // FIN ZPL
	        // =====================================================

	        zpl.append("^XZ");


	        // =====================================================
	        // MOSTRAR ZPL
	        // =====================================================

	        System.out.println(
	                "----------------------------------------------"
	        );

	        System.out.println(
	                "ZPL GENERADO"
	        );

	        System.out.println(
	                "----------------------------------------------"
	        );

	        System.out.println(
	                zpl.toString()
	        );


	        // =====================================================
	        // ENVIAR A IMPRESORA
	        // =====================================================

	        System.out.println(
	                "----------------------------------------------"
	        );

	        System.out.println(
	                "===== ENVIANDO ZPL ====="
	        );


	        enviarZpl(
	                impresora,
	                zpl.toString()
	        );


	        System.out.println(
	                "===== ZPL ENVIADO ====="
	        );


	        return ResponseEntity.ok(
	                new PrintResponse(
	                        true,
	                        "Impresión enviada correctamente"
	                )
	        );


	    } catch (Exception e) {

	        e.printStackTrace();

	        return ResponseEntity
	                .badRequest()
	                .body(
	                        new PrintResponse(
	                                false,
	                                "Error al imprimir: "
	                                        + e.getMessage()
	                        )
	                );
	    }
	}


	/*
	 * =========================================================
	 * LIMPIAR ZPL
	 * =========================================================
	 */

	private String limpiarZpl(String texto) {

	    if (texto == null) {
	        return "";
	    }

	    return texto
	            .replace("^", "")
	            .replace("~", "")
	            .replace("\\", "")
	            .replace("\n", " ")
	            .replace("\r", " ")
	            .trim();
	}


	/*
	 * =========================================================
	 * DESCRIPCION MAXIMO 2 LINEAS
	 * =========================================================
	 */

	private String descripcionMaximoDosLineas(
	        String texto,
	        int maxCaracteresPorLinea) {

	    if (texto == null ||
	            texto.trim().isEmpty()) {

	        return "";
	    }


	    texto =
	            texto.trim();


	    /*
	     * PRIMERA LINEA
	     */

	    String primeraLinea =
	            texto;


	    if (primeraLinea.length()
	            > maxCaracteresPorLinea) {

	        primeraLinea =
	                texto.substring(
	                        0,
	                        maxCaracteresPorLinea
	                );


	        int ultimoEspacio =
	                primeraLinea.lastIndexOf(" ");


	        if (ultimoEspacio > 0) {

	            primeraLinea =
	                    primeraLinea.substring(
	                            0,
	                            ultimoEspacio
	                    );
	        }
	    }


	    /*
	     * BUSCAR SEGUNDA LINEA
	     */

	    int inicioSegunda =
	            primeraLinea.length();


	    while (
	            inicioSegunda < texto.length()
	            &&
	            texto.charAt(inicioSegunda) == ' '
	    ) {

	        inicioSegunda++;
	    }


	    /*
	     * NO HAY SEGUNDA LINEA
	     */

	    if (inicioSegunda >= texto.length()) {

	        return primeraLinea;
	    }


	    /*
	     * SEGUNDA LINEA
	     */

	    String segundaLinea =
	            texto.substring(
	                    inicioSegunda
	            );


	    if (segundaLinea.length()
	            > maxCaracteresPorLinea) {

	        segundaLinea =
	                segundaLinea.substring(
	                        0,
	                        maxCaracteresPorLinea
	                );


	        int ultimoEspacio =
	                segundaLinea.lastIndexOf(" ");


	        if (ultimoEspacio > 0) {

	            segundaLinea =
	                    segundaLinea.substring(
	                            0,
	                            ultimoEspacio
	                    );
	        }
	    }


	    /*
	     * SALTO DE LINEA ZPL
	     */

	    return primeraLinea
	            + "\\&"
	            + segundaLinea;
	}


	/*
	 * =========================================================
	 * ENVIAR ZPL A IMPRESORA
	 * =========================================================
	 */

	private void enviarZpl(
	        PrintService impresora,
	        String zpl) throws Exception {

	    /*
	     * CREAR DOC FLAVOR RAW
	     */

	    DocFlavor flavor =
	            DocFlavor.BYTE_ARRAY.AUTOSENSE;


	    /*
	     * CONVERTIR ZPL A BYTES
	     */

	    byte[] datos =
	            zpl.getBytes(
	                    java.nio.charset.StandardCharsets.US_ASCII
	            );


	    /*
	     * CREAR DOCUMENTO
	     */

	    Doc doc =
	            new SimpleDoc(
	                    datos,
	                    flavor,
	                    null
	            );


	    /*
	     * CREAR JOB
	     */

	    DocPrintJob job =  impresora.createPrintJob();


	    /*
	     * ENVIAR DIRECTAMENTE
	     */

	    job.print(
	            doc,
	            null
	    );
	}
	
	
	
	 public void listarImpresoras() {

		    PrintService[] impresoras =
		            PrintServiceLookup.lookupPrintServices(null, null);

		    System.out.println("===== IMPRESORAS DISPONIBLES =====");

		    for (PrintService impresora : impresoras) {

		        System.out.println(
		            "IMPRESORA: " + impresora.getName()
		        );
		    }
		}
	 
	 private PrintService buscarImpresora(String nombre) {
		    PrintService[] impresoras =
		            PrintServiceLookup.lookupPrintServices(null, null);

		    for (PrintService impresora : impresoras) {
		        if (impresora.getName().equalsIgnoreCase(nombre)) {
		            return impresora;
		        }
		    }

		    return null;
		}
	@RequestMapping(method=RequestMethod.GET, value="/test/api")
	public String ping() {
		return "pong";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/listImpresoraLocal")
	public List<Impresora> getListImpresoraLocal() {
	        List<Impresora> lista = new ArrayList<>();
	        PrintService[] services =    PrintServiceLookup.lookupPrintServices(null, null);
	        for (PrintService service : services) {
	            Impresora impresora = new Impresora();
	            impresora.setDescripcion(service.getName());
	            impresora.setEstado(false);
	            lista.add(impresora);
	        }

	        return lista;
	 }
	@RequestMapping(method=RequestMethod.GET, value="/listImpresora")
	public List<Impresora> getAllImpresora(){
		Impresora impre=repository.findById(8).get();
		List<Impresora> lista=new ArrayList<>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for (int i = 0; i < services.length; i++) {
			Impresora impresora=new Impresora();
			impresora.setDescripcion(services[i].getName());
			impresora.setEstado(false);
			if (impre.getDescripcion().equals(services[i].getName())) {
				impresora.setEstado(true);
			}
			lista.add(impresora);
		}
		return lista;
	}
	@RequestMapping(method=RequestMethod.GET, value="/utilidadPrecio")
	public UtilidadPrecio getAllUtilidad(){
		return utilidadPreciorepository.findById(1).get();
	}
	@RequestMapping(method=RequestMethod.GET, value="/impresora/{id}")
	public Impresora getAll(@PathVariable int id){
		return repository.findById(id).get();
	}
	@RequestMapping(method=RequestMethod.GET, value="/terminalAutoImpresor/{numeroTerminal}")
	public TerminalConfigImpresora getTerminalAutoImpresorPorNumeroTerminal(@PathVariable int numeroTerminal){
		return terminalRepository.consultarTerminalEmisonFacturaPorTerminales(numeroTerminal);
	}

	@RequestMapping(method=RequestMethod.GET, value="/terminal/{numeroTerminal}")
	public TerminalConfigImpresora getTerminalPorNumnero(@PathVariable int numeroTerminal){
		TerminalConfigImpresora ter = terminalRepository.consultarTerminalPorNumeros(numeroTerminal);
		TerminalConfigImpresora dto = new TerminalConfigImpresora();

		if (ter == null) {
			dto= null;
		}else {
			// ⚙️ Construir el DTO solo con los campos que necesitás
			dto.setId(ter.getId());
			dto.setNumeroTerminal(ter.getNumeroTerminal());;
			dto.setEstadoAdicionArtVarios(ter.getEstadoAdicionArtVarios());
			dto.setEstadoEdicionZona(ter.getEstadoEdicionZona());
			dto.setEstadoEmisionFactura(ter.getEstadoEmisionFactura());
			dto.getAutoImpresor().setId(ter.getAutoImpresor().getId());
			dto.setEstadoListadoGrigImagen(ter.getEstadoListadoGrigImagen());
			dto.setImpresora(ter.getImpresora());
			dto.setIp(ter.getIp());
			dto.setNombreEquipo(ter.getNombreEquipo());
			dto.setNombreImpresora(ter.getNombreImpresora());
			dto.setNombreImpresoraBarcode(ter.getNombreImpresoraBarcode());
			dto.setIsCentralizadoImpresion(ter.getIsCentralizadoImpresion());
			
			
		}
		System.out.println("*/*/*/: "+dto.getImpresora());
		return dto;

	}
	@RequestMapping(method=RequestMethod.GET, value="/terminal/sql/{numeroTerminal}")
	public TerminalConfigImpresora getTerminalPorNumeroSql(@PathVariable int numeroTerminal){
		TerminalConfigImpresora ter = terminalRepository.consultarTerminalPorNumeroTerminalSql(numeroTerminal);
		TerminalConfigImpresora dto = new TerminalConfigImpresora();

		if (ter == null) {
			dto= null;
		}else {
			// ⚙️ Construir el DTO solo con los campos que necesitás
			dto.setId(ter.getId());
			dto.setNumeroTerminal(ter.getNumeroTerminal());;
			dto.setEstadoAdicionArtVarios(ter.getEstadoAdicionArtVarios());
			dto.setEstadoEdicionZona(ter.getEstadoEdicionZona());
			dto.setEstadoEmisionFactura(ter.getEstadoEmisionFactura());
			dto.getAutoImpresor().setId(ter.getAutoImpresor().getId());
			dto.setEstadoListadoGrigImagen(ter.getEstadoListadoGrigImagen());
			dto.setImpresora(ter.getImpresora());
			dto.setNombreImpresora(ter.getNombreImpresora());
			dto.setId(ter.getId());
			dto.setNombreEquipo(ter.getNombreEquipo());
			dto.setNombreImpresora(ter.getNombreImpresora());
			dto.setNombreImpresoraBarcode(ter.getNombreImpresoraBarcode());
			dto.setIsCentralizadoImpresion(ter.getIsCentralizadoImpresion());
		}
		System.out.println("*/*/*/: "+dto.getImpresora());
		return dto;

	}


	@RequestMapping(method=RequestMethod.GET, value="/terminal")
	public List<TerminalConfigImpresora> getAllTerminal(){
		return terminalRepository.getAllTerminal();
	}

	@RequestMapping(method=RequestMethod.PUT, value="/terminal")
	public TerminalConfigImpresora guardarTerminalConfigImpresora(@RequestBody TerminalConfigImpresora imp){
		return terminalRepository.save(imp);
	}

	@RequestMapping(method=RequestMethod.GET, value="/impresora_activo")
	public Impresora getImpresoraActivo(){
		return repository.findTop1ByOrderByIdAsc();
	}

	@RequestMapping(method=RequestMethod.PUT, value="/actualizar")
	public Impresora editar(@RequestBody Impresora entity){
		return repository.save(entity);
	}

	@RequestMapping(method=RequestMethod.POST, value="/actualizarReporteConfig")
	public ReporteConfig asdf(@RequestBody ReporteConfig entity){

		if(entity.getNombreReporte() != null){
			String [] datos1= entity.getNombreReporte().split(Pattern.quote("."));
			entity.setNombreReporte(datos1 [0]);
		}
		if(entity.getNombreSubReporte1() != null){
			String [] datos1= entity.getNombreSubReporte1().split(Pattern.quote("."));
			entity.setNombreSubReporte1(datos1[0]);
		}
		if(entity.getNombreSubReporte2() != null){
			String [] datos1= entity.getNombreSubReporte2().split(Pattern.quote("."));
			entity.setNombreSubReporte2(datos1[0]);
		}

		return reporteConfigRepository.save(entity);
	}

	@RequestMapping(method=RequestMethod.GET, value="/getReporteConfig")
	public List<ReporteConfig> getReporteConfig(){

		return reporteConfigRepository.findByOrderByIdAsc();
	}

	private List<Impresora> listaNombreReporte(String dir) {
		List<Impresora> lista=new ArrayList<>();

		File f = new File(dir);
		File [] fiche= f.listFiles();		
		for (int i = 0; i < fiche.length; i++) {
			Impresora im=new Impresora();
			String datos = fiche[i].getName();
			im.setDescripcion(datos);
			lista.add(im);
		}
		return lista;
	}
	private List<Impresora> listaNombreReporte1(String dir) {

	    List<Impresora> lista = new ArrayList<>();

	    File f = new File(dir);

	    System.out.println("Existe: " + f.exists());
	    System.out.println("Ruta: " + f.getAbsolutePath());

	    File[] fiche = f.listFiles();

	    if (fiche == null) {
	        return lista;
	    }

	    for (File archivo : fiche) {
	    	System.out.println("Archivo: " + archivo.getName());
	        Impresora im = new Impresora();
	        im.setDescripcion(archivo.getName());
	        lista.add(im);
	    }

	    return lista;
	}

	@RequestMapping(method=RequestMethod.GET, value="/listaNombreReporte")
	public List<Impresora> listReporteNombre(){
		List<Impresora> lista=new ArrayList<>();
		String path = new File("").getAbsolutePath();
		String dir = path+"\\src\\main\\resources\\reporte";
		String dir2 = path+"\\webapps\\apiRestFacturacion\\WEB-INF\\classes\\reporte";

		File f = new File(dir);
		if (f.exists()) {
			lista = listaNombreReporte1(dir);
		}else {
			lista = listaNombreReporte1(dir2);
		}
		return lista;

	}

	/*
public void PrintReportToPrinter(JasperPrint jasperPrint, String tipo) throws JRException {
	//Consigue los nombres de las impresoras.
	PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
	//Permite establecer el nombre de la impresora según el nombre del controlador de impresoras registradas (puede ver los nombres de las impresoras en la variable de servicios en la depuración)
	String selectedPrinter = tipo;   
	//String selectedPrinter = "\\\\S-BPPRINT\\HP Color LaserJet 4700"; //examlpe to network shared printer
	System.out.println("Number of print services: " + services.length);
	PrintService selectedService = null;
	//Establezca la configuración de impresión.
	PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
	printRequestAttributeSet.add(MediaSizeName.ISO_A4);
	printRequestAttributeSet.add(new Copies(1));
	if (jasperPrint.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE) { 
	printRequestAttributeSet.add(OrientationRequested.LANDSCAPE); 
	} else { 
	printRequestAttributeSet.add(OrientationRequested.PORTRAIT); 
	} 
	PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
	printServiceAttributeSet.add(new PrinterName(selectedPrinter, null));
	JRPrintServiceExporter exporter = new JRPrintServiceExporter();
	SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
	configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
	configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
	configuration.setDisplayPageDialog(false);
	configuration.setDisplayPrintDialog(false);
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	exporter.setConfiguration(configuration);
	//Iterate through available printer, and once matched with our <selectedPrinter>, go ahead and print!
	if(services != null && services.length != 0){
	for(PrintService service : services){
	String existingPrinter = service.getName();
	if(existingPrinter.equals(selectedPrinter))
	{
	selectedService = service;
	break;
	}
	}
	}
	if(selectedService != null)
	{   
	try{
	//Lets the printer do its magic!
	exporter.exportReport();
	}catch(Exception e){
	System.out.println("JasperReport Error: "+e.getMessage());
	}
	}else{
	System.out.println("JasperReport Error: Printer not found!");
	}}
	 */
}
