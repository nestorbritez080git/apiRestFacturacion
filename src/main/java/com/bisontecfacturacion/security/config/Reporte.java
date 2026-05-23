package com.bisontecfacturacion.security.config;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.annotation.Transactional;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextExporterConfiguration;
import net.sf.jasperreports.export.SimpleTextReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;


@Transactional
public class Reporte {
	/*	
	public void report(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo) throws IOException{
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);
			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
		//	ConfigImpresora config=new ConfigImpresora();
		//	config.PrintReportToPrinter(jasperPrint, tipo);

		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml");
			e.printStackTrace();
		}
	}*/

	//	public void reportPDFDescarga(List<?> lista, Map<String, Object> map, String nombreReporte, HttpServletResponse response, String tipo) throws IOException{
	//		try {
	//			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
	//			JasperDesign design = JRXmlLoader.load(jasperStream);
	//			JasperReport report = JasperCompileManager.compileReport(design);
	//			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
	//			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
	////			ConfigImpresora config=new ConfigImpresora();
	////	
	////			PrintReportToPrinter(jasperPrint, tipo);
	//			OutputStream outputStream=response.getOutputStream();
	//			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	//			
	//		} catch (JRException e) {
	//			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
	//			e.printStackTrace();
	//		}
	//	}
	//	
	public void reportPDFDescarga(List<?> lista, Map<String, Object> map, String nombreReporte, HttpServletResponse response) throws IOException{
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			if (jasperStream == null) {
			    throw new RuntimeException("No se encontró el reporte: /reporte/" + nombreReporte + ".jrxml");
			}
			JasperDesign design = JRXmlLoader.load(jasperStream);
			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
			OutputStream outputStream=response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			System.out.println("exooooortttadooo");
		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}
	private void resetTopOfForm(String nombreImpresora) {
		try {
			// Buscar la impresora por nombre
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService impresora = null;
			for (PrintService s : services) {
				if (s.getName().equalsIgnoreCase(nombreImpresora)) {
					impresora = s;
					break;
				}
			}

			if (impresora == null) {
				System.out.println("No se encontró la impresora: " + nombreImpresora);
				return;
			}

			// Comando ESC/P para reset Top of Form
			byte[] escReset = new byte[] { 27, 64 }; // ESC @

			DocPrintJob job = impresora.createPrintJob();
			Doc doc = new SimpleDoc(escReset, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
			job.print(doc, null);

			System.out.println("✅ Se envió comando ESC @ para reset Top of Form a: " + nombreImpresora);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void retrocederHojaEpsonLX350(String nombreImpresora, int milimetros) {
		try {
			// Buscar la impresora por nombre
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService impresora = null;
			for (PrintService s : services) {
				if (s.getName().toLowerCase().contains(nombreImpresora.toLowerCase())) {
					impresora = s;
					break;
				}
			}

			if (impresora == null) {
				System.out.println("❌ No se encontró la impresora: " + nombreImpresora);
				return;
			}

			// 🔹 Comando ESC/P para reiniciar la impresora (vuelve al Top of Form)
			byte[] escReset = new byte[] { 27, 64 }; // ESC @

			// 🔹 Comando para retroceder el papel (ESC j n => avanza, ESC J n => retrocede)
			// Cada unidad 'n' ≈ 1/216 pulgadas ≈ 0.12 mm
			// Entonces 100 mm ≈ 100 / 0.12 = 833 pasos aprox.
			int pasos = (int) (milimetros / 0.12);
			byte[] escRetroceso = new byte[] { 27, 74, (byte) pasos };

			// Combinar ambos
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(escReset);
			baos.write(escRetroceso);

			// Enviar a la impresora
			DocPrintJob job = impresora.createPrintJob();
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
			Doc doc = new SimpleDoc(baos.toByteArray(), flavor, null);
			job.print(doc, null);

			System.out.println("✅ Se envió comando de retroceso (" + milimetros + " mm) a la impresora " + nombreImpresora);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("⚠️ Error al enviar comando ESC/P: " + e.getMessage());
		}
	}

	private void avanzarHojaContinua(String nombreImpresora) {
		try {
			PrintService service = null;
			for (PrintService ps : PrintServiceLookup.lookupPrintServices(null, null)) {
				if (ps.getName().equalsIgnoreCase(nombreImpresora)) {
					service = ps;
					break;
				}
			}
			if (service != null) {
				DocPrintJob job = service.createPrintJob();

				// 🔹 ESC @ → reset printer
				byte[] reset = new byte[]{27, '@'};

				// 🔹 FF (Form Feed) → avanzar a nueva hoja (inicio de formulario continuo)
				byte[] formFeed = new byte[]{12};

				job.print(new SimpleDoc(reset, DocFlavor.BYTE_ARRAY.AUTOSENSE, null), null);
				job.print(new SimpleDoc(formFeed, DocFlavor.BYTE_ARRAY.AUTOSENSE, null), null);

				System.out.println("Hoja continua alineada (Top of Form) para impresora: " + nombreImpresora);
			} else {
				System.out.println("Impresora no encontrada: " + nombreImpresora);
			}
		} catch (Exception e) {
			System.out.println("Error al enviar comando ESC/P a la impresora: " + e.getMessage());
		}
	}
	public void reportXLSescarga(List<?> lista, Map<String, Object> map, String nombreReporte, HttpServletResponse response) throws IOException{
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");

			JasperDesign design = JRXmlLoader.load(jasperStream);
			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
			OutputStream outputStream=response.getOutputStream();
			JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);

		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}
	public void reportPDFImprimirLibreCorte(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo, int paginaWhdth, int paginaHeigth) throws Exception {
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);
			int pageWidth = design.getPageWidth();   // en puntos
			int pageHeight = design.getPageHeight(); // en puntos
			double widthMM = pageWidth * 25.4 / 72;
			double heightMM = pageHeight * 25.4 / 72;
			System.out.printf("Ancho en mm: %.2f mm\n", widthMM);
			System.out.printf("Alto en mm: %.2f mm\n", heightMM);

			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
			// 🧩 Aquí agregamos el control de hoja continua
			//retrocederHojaEpsonLX350(tipo, 100); // retrocede 10 mm aprox.
			//retrocederHojaEpsonLX350(tipo, 0); // Solo reset Top of Form
			//controlarPapelGenericTextOnly(tipo);
			PrintReportToPrinterConWidthyHeidth(jasperPrint, tipo);
			//imprimirTicketJasperConRetroceso(jasperPrint, tipo);

		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}



	public void reportPDFImprimirA4(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo, int paginaWhdth, int paginaHeigth) throws JRException {
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);
			//			design.setPageWidth(paginaWhdth);  // 80mm ≈ 226 puntos
			//			design.setPageHeight(paginaHeigth);  // 80mm ≈ 226 puntos

			int pageWidth = design.getPageWidth();   // en puntos
			int pageHeight = design.getPageHeight(); // en puntos

			double widthMM = pageWidth * 25.4 / 72;
			double heightMM = pageHeight * 25.4 / 72;

			System.out.printf("Ancho en mm: %.2f mm\n", widthMM);
			System.out.printf("Alto en mm: %.2f mm\n", heightMM);

			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);

			//retrocederHojaEpsonLX350(tipo, 100); // retrocede 10 mm aprox.
			retrocederHojaEpsonLX350(tipo, 0); // Solo reset Top of Form
			PrintReportToPrinterA4(jasperPrint, tipo);

		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}


	public void PrintReportToPrinterA4(JasperPrint jasperPrint, String tipo) throws JRException {
		//Consigue los nombres de las impresoras.
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		//Permite establecer el nombre de la impresora según el nombre del controlador de impresoras registradas (puede ver los nombres de las impresoras en la variable de servicios en la depuración)
		String selectedPrinter = tipo;
		for (int i = 0; i < services.length; i++) {
			System.out.println(" impres" + services[i].getName());
			//			System.out.println(" impres adfsfd" + services[i].toString());
			//			System.out.println(" impres" + services[i]);
			//			
		}

		//String selectedPrinter = "\\\\S-BPPRINT\\HP Color LaserJet 4700"; //examlpe to network shared printer
		System.out.println("Number of print services: " + services.length);
		PrintService selectedService = null;
		//Establezca la configuración de impresión.
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		//printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		System.out.println("page width priint: "+jasperPrint.getPageWidth());
		System.out.println("page heith priint: "+jasperPrint.getPageHeight());

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
		}
	}

	public void PrintReportToPrinterConWidthyHeidth(JasperPrint jasperPrint, String tipo) throws JRException {
		//Consigue los nombres de las impresoras.
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		//Permite establecer el nombre de la impresora según el nombre del controlador de impresoras registradas (puede ver los nombres de las impresoras en la variable de servicios en la depuración)
		String selectedPrinter = tipo;
		for (int i = 0; i < services.length; i++) {
			System.out.println(" impres" + services[i].getName());
			//			System.out.println(" impres adfsfd" + services[i].toString());
			//			System.out.println(" impres" + services[i]);
			//			
		}

		//String selectedPrinter = "\\\\S-BPPRINT\\HP Color LaserJet 4700"; //examlpe to network shared printer
		System.out.println("Number of print services: " + services.length);
		PrintService selectedService = null;
		//Establezca la configuración de impresión.
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
		//printRequestAttributeSet.add(MediaSizeName.ISO_A4);
		System.out.println("page width priint: "+jasperPrint.getPageWidth());
		System.out.println("page heith priint: "+jasperPrint.getPageHeight());
		System.out.println("MARGIN LEF: "+jasperPrint.getLeftMargin());
		System.out.println("MARGIN RIG: "+jasperPrint.getRightMargin());
		System.out.println("MARGIN TOP: "+jasperPrint.getTopMargin());
		System.out.println("MARGIN BOTON: "+jasperPrint.getBottomMargin());
		float mmPerPoint = 25.4f / 72f; // ≈ 0.35277 mm por punto

		float pageWidthMM = jasperPrint.getPageWidth() * mmPerPoint;
		float pageHeightMM = jasperPrint.getPageHeight() * mmPerPoint;
		printRequestAttributeSet.remove(MediaPrintableArea.class);


		printRequestAttributeSet.add(new MediaPrintableArea(0, 0, pageWidthMM, pageHeightMM, MediaPrintableArea.MM));

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
		}
	}
	public void reportPDFImprimirLibreCorte(List<?> lista, Map<String, Object> map,
			String nombreReporte, String impresoraSeleccionada) throws JRException {

		try {
			// 1️⃣ Cargar diseño Jasper
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/" + nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);

			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);

			// 2️⃣ Mapear impresora Generic / Text Only automáticamente
			String impresoraGeneric = impresoraSeleccionada + " Generic"; // ejemplo de convención
			controlarPapelGenericTextOnly(impresoraGeneric);

			// 3️⃣ Imprimir PDF con la impresora seleccionada por el usuario
			PrintReportToPrinterConWidthyHeidth(jasperPrint, impresoraSeleccionada);

		} catch (JRException e) {
			System.out.println("Error al procesar JRXML: " + e.getMessage());
			e.printStackTrace();
		}
	}
	private void controlarPapelGenericTextOnly(String nombreImpresora) {
		try {
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService impresora = Arrays.stream(services)
					.filter(s -> s.getName().equalsIgnoreCase(nombreImpresora))
					.findFirst()
					.orElse(null);

			if (impresora == null) {
				System.out.println("No se encontró la impresora Generic: " + nombreImpresora);
				return;
			}

			// ESC @ -> Reset Top of Form
			byte[] escReset = new byte[]{27, 64};

			// ESC J 30 -> Avanza papel 30 puntos (aprox 10 mm)
			byte[] escAvance = new byte[]{27, 74, 30};

			// Combinar comandos si querés reset + avance
			byte[] comando = new byte[escReset.length + escAvance.length];
			System.arraycopy(escReset, 0, comando, 0, escReset.length);
			System.arraycopy(escAvance, 0, comando, escReset.length, escAvance.length);

			DocPrintJob job = impresora.createPrintJob();
			Doc doc = new SimpleDoc(comando, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
			job.print(doc, null);

			System.out.println("✅ Se envió comando ESC @ + avance a Generic / Text Only: " + nombreImpresora);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void reportPDFImprimirPrueba(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo, int paginaWhdth, int paginaHeigth) throws JRException {
		try {
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);
			//			design.setPageWidth(paginaWhdth);  // 80mm ≈ 226 puntos
			//			design.setPageHeight(paginaHeigth);  // 80mm ≈ 226 puntos

			int pageWidth = design.getPageWidth();   // en puntos
			int pageHeight = design.getPageHeight(); // en puntos

			double widthMM = pageWidth * 25.4 / 72;
			double heightMM = pageHeight * 25.4 / 72;

			System.out.printf("Ancho en mm: %.2f mm\n", widthMM);
			System.out.printf("Alto en mm: %.2f mm\n", heightMM);

			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
			//retrocederHojaEpsonLX350(tipo, 100); // retrocede 10 mm aprox.
			retrocederHojaEpsonLX350(tipo, 0); // Solo reset Top of Form
			printReportConPrinterJob(jasperPrint, tipo);

		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}

	public void printReportConPrinterJob(JasperPrint jasperPrint, String nombreImpresora) {
		try{
			//Permite establecer el nombre de la impresora según el nombre del controlador de impresoras registradas (puede ver los nombres de las impresoras en la variable de servicios en la depuración)


			//String selectedPrinter = "\\\\S-BPPRINT\\HP Color LaserJet 4700"; //examlpe to network shared printer
			//Establezca la configuración de impresión.
			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			//printRequestAttributeSet.add(MediaSizeName.ISO_A4);
			System.out.println("page width priint: "+jasperPrint.getPageWidth());
			System.out.println("page heith priint: "+jasperPrint.getPageHeight());
			System.out.println("MARGIN LEF: "+jasperPrint.getLeftMargin());
			System.out.println("MARGIN RIG: "+jasperPrint.getRightMargin());
			System.out.println("MARGIN TOP: "+jasperPrint.getTopMargin());
			System.out.println("MARGIN BOTON: "+jasperPrint.getBottomMargin());
			float mmPerPoint = 25.4f / 72f; // ≈ 0.35277 mm por punto

			float pageWidthMM = jasperPrint.getPageWidth() * mmPerPoint;
			float pageHeightMM = jasperPrint.getPageHeight() * mmPerPoint;
			printRequestAttributeSet.remove(MediaPrintableArea.class);


			printRequestAttributeSet.add(new MediaPrintableArea(0, 0, pageWidthMM, pageHeightMM, MediaPrintableArea.MM));

			printRequestAttributeSet.add(new Copies(1));
			if (jasperPrint.getOrientationValue() == net.sf.jasperreports.engine.type.OrientationEnum.LANDSCAPE) { 
				printRequestAttributeSet.add(OrientationRequested.LANDSCAPE); 
			} else { 
				printRequestAttributeSet.add(OrientationRequested.PORTRAIT); 
			}
			String selectedPrinter = "";
			PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
			PrintService selectedService = null;

			for (PrintService service : services) {
				if (service.getName().equalsIgnoreCase(nombreImpresora)) {
					System.out.println("IMPRESORA SLEECIONADA : "+nombreImpresora);
					selectedPrinter= nombreImpresora;
					selectedService = service;
					System.out.println("IMPRESORA SLEECIONADA : "+selectedService);
					System.out.println("IMPRESORA SLEECIONADA : "+selectedPrinter);
					String command = "powershell.exe Get-PrintJob -PrinterName " + nombreImpresora + " | Remove-PrintJob";


					ProcessBuilder processBuilder = new ProcessBuilder(command);

					// Iniciar el proceso
					Process process = processBuilder.start();

					// Obtener la salida del comando (si la hay)
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
					BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String errorLine;
					while ((errorLine = errorReader.readLine()) != null) {
						System.out.println("Error: " + errorLine);  // Aquí verás los mensajes de error
					}
					// Esperar a que el proceso termine
					int exitCode = process.waitFor();
					System.out.println("El proceso terminó con el código de salida: " + exitCode);

					break;
				}
			}

			if (selectedService == null) {
				System.out.println("Impresora no encontrada: " + nombreImpresora);
				return;
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

			System.out.println("anes de imprmir");
			//Lets the printer do its magic!
			exporter.exportReport();
			
		}catch(Exception e){
			System.out.println("JasperReport Error: "+e.getMessage());
		}
	}

	public void agregarSecuenciaRetroceso(String outputFilePath) {
		try (FileOutputStream fos = new FileOutputStream(outputFilePath, true)) { // append = true
	        // Secuencia retroceso papel (ajustar según impresora)
	        byte[] retroceso = new byte[]{0x1B, 0x65};  // ESC e = retroceso (depende modelo)
	        fos.write(retroceso);

	        // Secuencia corte papel (corte completo)
	        byte[] corte = new byte[]{0x1D, 0x56, 0x00}; // GS V 0
	        fos.write(corte);

	        fos.flush();
	        System.out.println("✅ Secuencias ESC/POS agregadas al final del archivo.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void imprimirTicketJasperConRetroceso(JasperPrint jasperPrint, String nombreImpresora) throws JRException, IOException, PrintException {
	    // 1️⃣ Buscar impresora por nombre
	    PrintService impresora = null;
	    PrintService[] servicios = PrintServiceLookup.lookupPrintServices(null, null);
	    for (PrintService ps : servicios) {
	        if (ps.getName().equalsIgnoreCase(nombreImpresora)) {
	            impresora = ps;
	            break;
	        }
	    }
	    if (impresora == null) {
	        throw new RuntimeException("Impresora no encontrada: " + nombreImpresora);
	    }

	    // 2️⃣ Exportar JasperPrint a texto en memoria
	    JRTextExporter exporter = new JRTextExporter();

	    SimpleTextReportConfiguration reportConfig = new SimpleTextReportConfiguration();
	    reportConfig.setCharWidth(7f);
	    reportConfig.setCharHeight(12f);
	    reportConfig.setPageWidthInChars(80);
	    reportConfig.setPageHeightInChars(60);

	    SimpleTextExporterConfiguration exportConfig = new SimpleTextExporterConfiguration();
	    exportConfig.setLineSeparator("\r\n");

	    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

	    StringWriter writer = new StringWriter();
	    exporter.setExporterOutput(new SimpleWriterExporterOutput(writer));
	    exporter.setConfiguration(reportConfig);
	    exporter.setConfiguration(exportConfig);
	    exporter.exportReport();

	    String texto = writer.toString();

	    // 3️⃣ Preparar buffer de impresión
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();

	    // 🔹 Retroceso inicial del papel (tractor)
	    baos.write(new byte[]{0x1B, 0x4A, 0x20}); // ESC J 32 puntos hacia atrás

	    // 🔹 Contenido Jasper
	    baos.write(texto.getBytes("CP437")); // o el encoding que soporte la impresora

	    // 🔹 Avanza papel + corte parcial al final
	    baos.write(new byte[]{0x1D, 0x56, 0x41, 0x10}); // ESC/POS corte parcial

	    byte[] datos = baos.toByteArray();

	    // 4️⃣ Crear trabajo de impresión
	    DocPrintJob job = impresora.createPrintJob();
	    Doc doc = new SimpleDoc(datos, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

	    PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
	    attrs.add(new JobName("Ticket Jasper", null));
	    attrs.add(OrientationRequested.PORTRAIT);

	    // 5️⃣ Enviar a la impresora
	    job.print(doc, attrs);

	    System.out.println("✅ Ticket impreso con Jasper, retroceso inicial y corte final");
	}
    /**
     * Genera el archivo de texto y lo imprime en la Epson LX-350.
     */
	public void imprimirTicketEpson(JasperPrint jasperPrint, String nombreImpresora) throws Exception {
	    // 🔹 Abrimos conexión a la impresora
	    PrintService impresora = encontrarImpresoraPorNombre(nombreImpresora);
	    if (impresora == null) {
	        throw new RuntimeException("Impresora no encontrada: " + nombreImpresora);
	    }
	    DocPrintJob job = impresora.createPrintJob();
	    // 🔹 Usamos OutputStream de la impresora
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    byte[] bytes = outputStream.toByteArray();
	    Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
	    job.print(doc, null);
	    System.out.println("✅ Ticket enviado a impresora: " + nombreImpresora);
	}
	  private PrintService encontrarImpresoraPorNombre(String nombre) {
	        PrintService[] servicios = PrintServiceLookup.lookupPrintServices(null, null);
	        for (PrintService servicio : servicios) {
	            if (servicio.getName().equalsIgnoreCase(nombre)) {
	                return servicio;
	            }
	        }
	        return null;
	    }
    public void imprimirReporteConRetroceso(JasperPrint jasperPrint, OutputStream outputStream) throws JRException, IOException {
        // 1️⃣ Reset y retroceso inicial (ESC @)
        byte[] retrocesoInicial = new byte[]{0x1B, 0x40}; // ESC @: reset impresora
        outputStream.write(retrocesoInicial);

        // 2️⃣ Exporta JasperPrint a texto en memoria
        JRTextExporter exporter = new JRTextExporter();

        SimpleTextReportConfiguration reportConfig = new SimpleTextReportConfiguration();
        reportConfig.setCharWidth(7f);
        reportConfig.setCharHeight(12f);
        reportConfig.setPageWidthInChars(80);
        reportConfig.setPageHeightInChars(60);

        SimpleTextExporterConfiguration exportConfig = new SimpleTextExporterConfiguration();
        exportConfig.setLineSeparator("\r\n"); // saltos de línea Windows

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        // Exportación a memoria
        StringWriter writer = new StringWriter();
        exporter.setExporterOutput(new SimpleWriterExporterOutput(writer));
        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport(); // genera el texto

        // 3️⃣ Envía el contenido a la impresora con encoding compatible
        String texto = writer.toString();
        outputStream.write(texto.getBytes("CP437")); // CP437 recomendado para Epson

        // 4️⃣ Avanza papel y corta al final
        byte[] corte = new byte[]{0x1D, 0x56, 0x41, 0x10}; // GS V A n: corte parcial
        outputStream.write(corte);

        outputStream.flush();
        // 🔹 No cerramos el OutputStream para poder reutilizarlo si hace falta
        System.out.println("✅ Impresión completada con retroceso y corte manteniendo el formato Jasper");
    }
    /**
     * Envía el archivo .txt a la impresora Epson con comandos ESC/P.
     */
    private void imprimirArchivoTexto(String rutaTxt, String nombreImpresora) throws Exception {
        FileInputStream fis = new FileInputStream(rutaTxt);

        // ⚙️ Buscar la impresora
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService impresora = null;
        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(nombreImpresora)) {
                impresora = service;
                break;
            }
        }

        if (impresora == null) {
            throw new RuntimeException("No se encontró la impresora: " + nombreImpresora);
        }

        // 💡 Comando ESC/P para retroceder al Top of Form (inicio de hoja)
        // ESC + FF (Form Feed) o ESC + EM según modelo
        byte[] topOfForm = new byte[]{0x1B, 0x63, 0x30, 0x02}; // Epson ESC/P "Select Top of Form"

        // Crear stream combinado: comando + texto
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(topOfForm);

        // Agregar contenido del archivo
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        fis.close();
        byte[] datosAImprimir = baos.toByteArray();

        // Enviar a la impresora
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(datosAImprimir, flavor, null);
        DocPrintJob job = impresora.createPrintJob();
        job.print(doc, null);

        System.out.println("🖨️ Impresión enviada correctamente a: " + impresora.getName());
    }

	
}
