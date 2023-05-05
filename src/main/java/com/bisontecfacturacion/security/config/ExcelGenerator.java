package com.bisontecfacturacion.security.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bisontecfacturacion.security.model.Venta;
 
 
public class ExcelGenerator {
  
  public static ByteArrayInputStream ventaToExcel(List<Venta> ventas) throws IOException {
    String[] COLUMNs = {"N° FACTURA", "FECHA FACTURA", "CLIENTE", "R.C.U", "TIMBRADO", "VENC. TIMBRADO", "MONTO", "IVA 5%", "IVA 10%", "EXCENTA"};
    try(
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ){
      CreationHelper createHelper = workbook.getCreationHelper();
   
      Sheet sheet = workbook.createSheet("Ventas");
   
      Font headerFont = workbook.createFont();
      headerFont.setBold(true);
      headerFont.setColor(IndexedColors.BLUE.getIndex());
   
      CellStyle headerCellStyle = workbook.createCellStyle();
      headerCellStyle.setFont(headerFont);
   
      // Row for Header
      Row headerRow = sheet.createRow(0);
   
      // Header
      for (int col = 0; col < COLUMNs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(COLUMNs[col]);
        cell.setCellStyle(headerCellStyle);
      }
   
      // CellStyle for Age
      CellStyle ageCellStyle = workbook.createCellStyle();
      ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
   
      int rowIdx = 1;
      for (Venta v : ventas) {
        Row row = sheet.createRow(rowIdx++);
   
        row.createCell(0).setCellValue(v.getNroDocumento());
        row.createCell(1).setCellValue(v.getFechaFactura());
        row.createCell(2).setCellValue(v.getCliente().getPersona().getNombre());
        row.createCell(3).setCellValue(v.getCliente().getPersona().getCedula());
        row.createCell(4).setCellValue(v.getTimbrado());
        row.createCell(5).setCellValue(v.getTimbradoFin());
        row.createCell(6).setCellValue(v.getTotal());
        row.createCell(7).setCellValue(v.getTotalIvaCinco());
        row.createCell(8).setCellValue(v.getTotalIvaDies());
        row.createCell(9).setCellValue(0);
   
        

      }
   
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }
}
