package com.bisontecfacturacion.security.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.bisontecfacturacion.ApiRestApplication;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Component
public class ImportarExcel {
	@Autowired
	private ProductoRepository productoRepository;
	Workbook wwk;
	public String importar(File archivo, JTable tablaD) {
		String respuesta="No se pudo abrir el archivo.!";
		
		return respuesta;
	}
	public String exportar(File archivo, JTable tablaD) {
		String respuesta="No se pudo abrir el archivo.!";
		
		int numFila= tablaD.getRowCount(), numColumna= tablaD.getColumnCount();
		if(archivo.getName().endsWith("xls")) {
			wwk = new HSSFWorkbook();
		}else {
			wwk = new XSSFWorkbook();
		}
		Sheet hoja = wwk.createSheet("PRUEBA");
		try {
			for (int i = 0; i < numFila; i++) {
			Row fila = hoja.createRow(i+1);
			for (int j = 0; j < numColumna; j++) {
				Cell celda = fila.createCell(j);
				if(i==-1) {
					celda.setCellValue(String.valueOf(tablaD.getColumnName(j)));
				}else {
					celda.setCellValue(String.valueOf(tablaD.getValueAt(i, j)));
				}
				wwk.write(new FileOutputStream(archivo));
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return respuesta;
	}
	 private static void printCellValue(Cell cell) {
	        switch (cell.getCellTypeEnum()) {
	            case BOOLEAN:
	                System.out.print(cell.getBooleanCellValue());
	                break;
	            case STRING:
	                System.out.print(cell.getRichStringCellValue().getString());
	                break;
	            case FORMULA:
	                System.out.print(cell.getCellFormula());
	                break;
	            case BLANK:
	                System.out.print("");
	                break;
	            default:
	                System.out.print("");
	        }
	 }
			 private String limpiarDescripcion(String descripcion) {

			     if (descripcion == null) {
			         return "";
			     }

			     // elimina números decimales al final
			     descripcion = descripcion.replaceAll(
			             "\\s+\\d+[.]\\d+",
			             ""
			     );

			     // elimina cero suelto al final
			     descripcion = descripcion.replaceAll(
			             "\\s+0$",
			             ""
			     );

			     // espacios duplicados
			     descripcion = descripcion.replaceAll(
			             "\\s+",
			             " "
			     );

			     return descripcion.trim();

			 }
			 

			 public List<Producto> leerArchivoCSV(String rutaArchivo) {

			     List<Producto> listado = new ArrayList<>();

			     try {

			         Reader reader = Files.newBufferedReader(Paths.get(rutaArchivo));

			         CSVReader csvReader = new CSVReaderBuilder(reader)
			                 .withSkipLines(1) // saltar cabecera
			                 .build();

			         String[] line;

			         while ((line = csvReader.readNext()) != null) {

			             try {

			                 Producto p = new Producto();

			                 // CSV generado:
			                 // codoriginal,descripcion,existencia,
			                 // precio_costo,precio_venta_1,
			                 // habilitado,iva,volumen,
			                 // stock_minimo,estado_compuesto

			                 p.setCodoriginal(line[0]);
			                 p.setDescripcion(limpiarDescripcion(line[1]));

			                 p.setExistencia(parseDouble(line[2]));

			                 p.setPrecioCosto(parseDouble(line[3]));

			                 p.setPrecioVenta_1(parseDouble(line[4]));
			                 p.setPrecioVenta_2(parseDouble(line[4]));
			                 p.setPrecioVenta_3(parseDouble(line[4]));
			                 p.setPrecioVenta_4(parseDouble(line[4]));

			                 p.setHabilitado(Boolean.parseBoolean(line[5]));

			                 p.setIva(line[6]);

			                 p.setVolumen(0);

			                 p.setStock_minimo(0.0);

			                 p.setEstadoCompuesto(false);
			                 p.getMarca().setId(1);
			                 p.getGrupo().setId(1);
			                 p.getProveedor().setId(1);
			                 p.getSubGrupo().setId(1);
			                 p.getUnidadMedida().setId(1);
			                 p.getDeposito().setId(1);
			                 p.setAplicacion("");
			                 p.setNombreImagen("");
			                 p.setFabricante("");
			                 p.setFechaVencimiento(new Date());
			                 p.setIsBalanza(false);
			                 p.setStockPresupuesto(0.0);
			                 listado.add(p);
			             } catch (Exception e) {
			                 System.out.println(
			                         "Error procesando línea CSV: "
			                         + Arrays.toString(line)
			                 );

			                 e.printStackTrace();

			             }

			         }

			         csvReader.close();

			     } catch (Exception e) {

			         e.printStackTrace();

			     }

			     return listado;

			 }

			 private Double parseDouble(String value) {

			     try {

			         if (value == null || value.trim().isEmpty()) {
			             return 0D;
			         }

			         return Double.parseDouble(value);

			     } catch (Exception e) {

			         return 0D;

			     }

			 }
			

	 public List<RucFormato> leerArchivoExcel(String nombreArchivo) {
		 List<RucFormato> listado= new ArrayList<RucFormato>();
		 try {
			
			 String rutaArchivoExcel = "rucccc.xls";
	         Workbook wb = WorkbookFactory.create(new File(nombreArchivo+".xls"));
	         Sheet firstSheet = wb.getSheetAt(0);
				Iterator iterator = firstSheet.iterator();
	         StringBuilder text = new StringBuilder();
	         DataFormatter forma = new DataFormatter();
	         
	         List<String> rucS= new  ArrayList<>();
	         List<String> dv= new  ArrayList<>();
	         List<String> razon = new  ArrayList<>();
	         FormulaEvaluator la= wb.getCreationHelper().createFormulaEvaluator();
	         for (int i = 0; i < firstSheet.getLastRowNum()+1; i++) {
					Row row = firstSheet.getRow(i);
					if((row != null) && (row.getRowNum() != 0)) {
						
						Cell cell = row.getCell(0);
						if(cell!=null) {
							//System.out.println(cell.toString()+"**********************");
							rucS.add(cell.toString());
							
							Cell cell1 = row.getCell(1);
							//System.out.println(cell1.toString()+"**********************");
							dv.add(cell1.toString());
							
							Cell cell2 = row.getCell(2);
							//System.out.println(cell2.toString()+"**********************");
							razon.add(cell2.toString());
						}else {
							System.out.println("COLUMNA INDICE VACIOS");
						}
						
					}else {
						System.out.println("FILA VACIO");
					}
					
				}
	         for (int i = 0; i < rucS.size(); i++) {
	         	System.out.println(rucS.get(i).toString()+"  ASFSAD");
	         	System.out.println(dv.get(i).toString()+"  ASFSAD");
	         	System.out.println(razon.get(i).toString()+"  ASFSAD");
	         	RucFormato f = new RucFormato();
//	         	String caRuc= rucS.get(i);
//	         	//int nuRuc= Integer.parseInt(caRuc.substring(0, caRuc.indexOf('.')));
//	         	
//	         	String caDv= dv.get(i);
	         	int nuDv= Integer.parseInt(dv.get(i).toString().substring(0, dv.get(i).toString().indexOf('.')));
	         	int nuRuc= Integer.parseInt(rucS.get(i).toString().substring(0, rucS.get(i).toString().indexOf('.')));
	         	System.out.println("NUM dv "+nuDv);
	         	System.out.println("NUM ruc "+nuRuc);
	         	System.out.println("index "+i);
	         	
	         	f.setRuc(nuRuc+"-"+nuDv);
	         	f.setDv(nuDv);
	         	f.setRazonSocial(razon.get(i));
	         	listado.add(f);
	         }
	         System.out.println("Arregloss Ruc : "+ listado.size());
	         for (int i = 0; i < listado.size(); i++) {
					System.out.println("   RAZON SOCIAL: "+listado.get(i).getRazonSocial()+"   RUC: "+ listado.get(i).getRuc()+ " DV:  "+listado.get(i).getDv());
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		 return listado;
	 }
	 
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ApiRestApplication.class, args ); ImportarExcel v = context.getBean(ImportarExcel.class); 
		List<Producto> productos = v.leerArchivoCSV( "C:/productos_importacion.csv" ); 
		System.out.println( "TOTAL PRODUCTOS: " + productos.size() ); 
		for (Producto p : productos) { 
			System.out.println( p.getCodoriginal() + " - " + p.getDescripcion() ); 
			} 
		v.productoRepository.saveAll(productos); 
		System.out.println( "PRODUCTOS GUARDADOS" );
	
	}

}
