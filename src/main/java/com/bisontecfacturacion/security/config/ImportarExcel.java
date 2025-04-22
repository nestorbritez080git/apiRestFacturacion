package com.bisontecfacturacion.security.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.javafx.collections.MappingChange.Map;

import net.sf.jasperreports.components.table.Column;

public class ImportarExcel {
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
		ImportarExcel v = new ImportarExcel();
		v.leerArchivoExcel("ruc11");
	/*	
		try {
			
			
			String rutaArchivoExcel = "ruc.xls";
            FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
            System.out.println(inputStream.toString());
            Workbook wb = WorkbookFactory.create(new File("ruc.xls"));
            Sheet firstSheet = wb.getSheetAt(0);
			Iterator iterator = firstSheet.iterator();
            StringBuilder text = new StringBuilder();
            DataFormatter forma = new DataFormatter();
            
//            firstSheet.forEach(row -> {
////            	Column column=columnIndexColumnMap.get(cell.getColumnIndex());
////                if (column==null){
////                  return;
////                }
//            	row .forEach(cell -> {
//            		text.append(forma.formatCellValue(cell)+"\t");
//            	});
//            	text.append("\n");
//            });
//            int row = firstSheet.getLastRowNum();
//            System.out.println("row : " +row);
////            for (int i = 0; i < row ; i++) {
//            	
//            	Row r= firstSheet.getRow(i);
//            	if(r.getCell(0)!=null & r.getCell(1)!=null & r.getCell(2)!=null) {
//            		System.out.println("CEL: "+ r.getCell(0)+ "  "+r.getCell(1)+ "  "+r.getCell(2) );
//            	}
//            	
//				
//			}
           
            firstSheet.forEach(row -> {
            	row .forEach(cell -> {
            		text.append(forma.formatCellValue(cell)+"\t");
            	});
            	text.append("\n");
            });
            
            System.out.println(text);
            
          //  Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

            // Retrieving the number of sheets in the Workbook
            System.out.println("Workbook has " + wb.getNumberOfSheets() + " Sheets : ");
            
               =============================================================
               Iterating over all the sheets in the workbook (Multiple ways)
               =============================================================
            

            // 1. You can obtain a sheetIterator and iterate over it
            Iterator<Sheet> sheetIterator = wb.sheetIterator();
            
            
            List<RucFormato> listado= new ArrayList<RucFormato>();
            
            
            List<String> rucS= new  ArrayList<>();
            List<String> dv= new  ArrayList<>();
            List<String> razon = new  ArrayList<>();
            FormulaEvaluator la= wb.getCreationHelper().createFormulaEvaluator();
            for (int i = 0; i < firstSheet.getLastRowNum()+1; i++) {
				Row row = firstSheet.getRow(i);
				if((row != null) && (row.getRowNum() != 0)) {
					
					Cell cell = row.getCell(0);
					if(cell!=null) {
						System.out.println(cell.toString()+"**********************");
						rucS.add(cell.toString());
						
						Cell cell1 = row.getCell(1);
						System.out.println(cell1.toString()+"**********************");
						dv.add(cell1.toString());
						
						Cell cell2 = row.getCell(2);
						System.out.println(cell2.toString()+"**********************");
						razon.add(cell2.toString());
					}else {
						System.out.println("DATOS VACIOS");
					}
					
				}else {
					System.out.println("No hay version anterior");
				}
				
			}
            for (int i = 0; i < rucS.size(); i++) {
//            	System.out.println(rucS.get(i).toString()+"  ASFSAD");
//            	System.out.println(dv.get(i).toString()+"  ASFSAD");
//            	System.out.println(razon.get(i).toString()+"  ASFSAD");
            	RucFormato f = new RucFormato();
            	
            	f.setRuc(rucS.get(i));
            	f.setDv(dv.get(i));
            	f.setRazonSocial(razon.get(i));
            	listado.add(f);
            }
            System.out.println("Arregloss Ruc : "+ listado.size());
            for (int i = 0; i < listado.size(); i++) {
				System.out.println("   RAZON SOCIAL: "+listado.get(i).getRazonSocial()+"   RUC: "+ listado.get(i).getRuc());
			}
            System.out.println("Retrieving Sheets using Iterator");
//            while (sheetIterator.hasNext()) {
//                Sheet sheet = sheetIterator.next();
//                System.out.println("=> " + sheet.getSheetName());
//            }

            // 2. Or you can use a for-each loop
            System.out.println("Retrieving Sheets using for-each loop");
            for(Sheet sheet: wb) {
                System.out.println("=> " + sheet.getSheetName());
            }

            // 3. Or you can use a Java 8 forEach with lambda
            System.out.println("Retrieving Sheets using Java 8 forEach with lambda");
            wb.forEach(sheet -> {
                System.out.println("=> " + sheet.getSheetName());
            });

            
               ==================================================================
               Iterating over all the rows and columns in a Sheet (Multiple ways)
               ==================================================================
          
          
          
          
      
          
          
            // 1. You can obtain a rowIterator and columnIterator and iterate over them
            System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row roww = rowIterator.next();

                // Now let's iterate over the columns of the current row
                Iterator<Cell> cellIterator = roww.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = dataFormatter.formatCellValue(cell);
                    System.out.print(cellValue +"\t");
                }
                System.out.println();
            }

            // 2. Or you can use a for-each loop to iterate over the rows and columns
            
            System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
            for (Row rows: sheet) {
                for(Cell cell: rows) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    System.out.print(cellValue + "\t");
                }
                System.out.println();
            }

            // 3. Or you can use Java 8 forEach loop with lambda
            System.out.println("\n\nIterating over Rows and Columns using Java 8 forEach with lambda\n");
            sheet.forEach(rowa -> {
                rowa.forEach(cell -> {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    System.out.print(cellValue + "\t");
                });
                System.out.println();
            });

            // Closing the workbook
            wb.close();
        
			
            -
            -
            -
            -
            -
            -
            -
             
			
            String rutaArchivoExcel = "ruc.xls";
            FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
            System.out.println(inputStream.toString());
            Workbook wb = WorkbookFactory.create(new File("ruc.xls"));
            Sheet firstSheet = wb.getSheetAt(0);
			Iterator iterator = firstSheet.iterator();
            
            DataFormatter formatter = new DataFormatter();
            while (iterator.hasNext()) {
                Row nextRow = (Row) iterator.next();
				Iterator cellIterator = nextRow.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = (Cell) cellIterator.next();
                    String contenidoCelda = formatter.formatCellValue(cell);
                    System.out.println("celda: " + contenidoCelda);
                }
                
            }
           
			
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
	}

}
