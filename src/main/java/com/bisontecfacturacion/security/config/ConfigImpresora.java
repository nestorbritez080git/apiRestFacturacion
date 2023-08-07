package com.bisontecfacturacion.security.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.UtilidadPrecio;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.repository.UtilidadPrecioRepository;


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
	/*
	@RequestMapping(method=RequestMethod.GET, value="/impresora")
	public Impresora getAll(){
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		List<Impresora> lista=new ArrayList<>();
		for(PrintService objeto: services){
			Impresora impre=new Impresora();
			impre.setDescripcion(objeto.getName());
			lista.add(impre);
		}
		return lista;
	}
	*/
	
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
	@RequestMapping(method=RequestMethod.GET, value="/terminal/{numeroTerminal}")
	public TerminalConfigImpresora getTerminalConfigImpresoraPorNumneroTerminal(@PathVariable int numeroTerminal){
		return terminalRepository.consultarTerminal(numeroTerminal);
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
	
	@RequestMapping(method=RequestMethod.GET, value="/listaNombreReporte")
	public List<Impresora> listReporteNombre(){
		List<Impresora> lista=new ArrayList<>();
		String path = new File("").getAbsolutePath();
		String dir = path+"\\src\\main\\java\\reporte";
		String dir2 = path+"\\webapps\\apiRestFacturacion\\WEB-INF\\classes\\reporte";
		
		File f = new File(dir);
		if (f.exists()) {
			lista = listaNombreReporte(dir);
		}else {
			lista = listaNombreReporte(dir2);
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
