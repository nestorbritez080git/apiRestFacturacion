package com.bisontecfacturacion.security.config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class FechaUtil {
	public static Date convertirFechaUtilATimeZone(Date date){
		 Timestamp ts=new Timestamp(System.currentTimeMillis());  
         date=ts;  
         return date;
	}
	public static String convertirFechaUtilAString(Date date){
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	public static LocalDateTime convertirStrinfALocalDateTim(String v) {
		System.out.println("DATE REC : "+v);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
		 String[] partes = v.split("\\.");
		 System.out.println(partes[1]);
		 System.out.println(partes[0]);
		LocalDateTime dateTime = LocalDateTime.parse(partes[0], formato );
		System.out.println("DATE TIME : "+dateTime);
		return dateTime;
	}
	public static Date convertirFechaStringADateUtil(String v) {
		 SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		 
		 Date fecha = null;
		 try {
		     fecha = formatoDelTexto.parse(v);
		 } catch (ParseException ex) {
		     ex.printStackTrace();
		 }
		 return fecha;
	}
	public static Date setFechaHoraInicial(String fecInicial) {
		
		Date fecI = new Date();
		try {
			Calendar cc= Calendar.getInstance();
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			
			//System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
			fecI = formater.parse(fecInicial);
			//Date fecF=formater.parse(fechaF);
			//System.out.println(fecF.getDate());
			//fecF.setHours(23);
			fecI.setHours(1);
			System.out.println("hora final fechas:::hora inicio finbal: "+fecI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fecI;
	}
	public static LocalDate convertirFechaFormato(String fe) {
		String dateString = fe;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(dateString, formatter);
        return dateTime;
	}
	public static LocalDateTime setFechaHoraInicialLocalDateTime(String fecInicial) {
		System.out.println("hora final fechas:::hora inicio finbal: "+fecInicial);

		LocalDateTime fecI = null;
		try {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern( "yyyy-MM-dd"); 
			//LocalDateTime dateTime = LocalDateTime.parse(fecInicial, formato);
			//fecI.of(fecI.getYear(), fecI.getMonth(), fecI.getDayOfMonth(), 0, 1);
			//Date fecF=formater.parse(fechaF);
			//System.out.println(fecF.getDate());
			//fecF.setHours(23)
			LocalDate fechaLocal = LocalDate.parse(fecInicial, formato);
			LocalTime  fechaLocalHora = LocalTime.of(0, 0);
			fecI= fechaLocal.atTime(fechaLocalHora);
			//fecI.setHours(1);
			System.out.println("hora final fechas:::hora inicio finbal: "+fecI+ " == "+fechaLocal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fecI;
	}
	public static LocalDateTime setFechaHoraFinalLocalDateTime(String fecInicial) {
		System.out.println("hora final fechas:::hora inicio finbal: "+fecInicial);

		LocalDateTime fecI = null;
		try {
			DateTimeFormatter formato = DateTimeFormatter.ofPattern( "yyyy-MM-dd"); 
			//LocalDateTime dateTime = LocalDateTime.parse(fecInicial, formato);
			//fecI.of(fecI.getYear(), fecI.getMonth(), fecI.getDayOfMonth(), 0, 1);
			//Date fecF=formater.parse(fechaF);
			//System.out.println(fecF.getDate());
			//fecF.setHours(23)
			LocalDate fechaLocal = LocalDate.parse(fecInicial, formato);
			LocalTime  fechaLocalHora = LocalTime.of(23, 59);
			fecI= fechaLocal.atTime(fechaLocalHora);
			//fecI.setHours(1);
			System.out.println("hora final fechas:::hora inicio finbal: "+fecI+ " == "+fechaLocal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fecI;
	}
	public static Date setFechaHoraFinal(String fecInicial) {
		Date fecI = new Date();
		try {
			Calendar cc= Calendar.getInstance();
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			
			//System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
			fecI = formater.parse(fecInicial);
			//Date fecF=formater.parse(fechaF);
			//System.out.println(fecF.getDate());
			//fecF.setHours(23);
			fecI.setHours(23);
			System.out.println("hora final fechas:::hora inicio finbal: "+fecI);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return fecI;
	}
}
