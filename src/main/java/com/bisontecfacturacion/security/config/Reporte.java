package com.bisontecfacturacion.security.config;

import java.io.IOException;
import java.io.InputStream;
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
	public void reportPDFImprimir(List<?> lista, Map<String, Object> map, String nombreReporte, String tipo) throws JRException {
		try {
			
			InputStream jasperStream = this.getClass().getResourceAsStream("/reporte/"+nombreReporte + ".jrxml");
			JasperDesign design = JRXmlLoader.load(jasperStream);
			JasperReport report = JasperCompileManager.compileReport(design);
			JRDataSource jRDataSource = new JRBeanCollectionDataSource(lista);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, map, jRDataSource);
			PrintReportToPrinter(jasperPrint, tipo);
			
		} catch (JRException e) {
			System.out.println("tidak bisa membaca file jrml : "+e.getMessage());
			e.printStackTrace();
		}
	}
	public void PrintReportToPrinter(JasperPrint jasperPrint, String tipo) throws JRException {
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

}
