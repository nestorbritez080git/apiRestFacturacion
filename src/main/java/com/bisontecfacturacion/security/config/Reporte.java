package com.bisontecfacturacion.security.config;

import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
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
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;


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
	public void reportPDFImprimirLibreCorte(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo, int paginaWhdth, int paginaHeigth) throws JRException {
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
			PrintReportToPrinterConWidthyHeidth(jasperPrint, tipo);
			
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

}
