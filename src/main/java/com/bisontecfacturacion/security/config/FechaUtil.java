package com.bisontecfacturacion.security.config;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	public static Date convertirFechaStringADateUtil(String v) {
		 SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		 String strFecha = v;
		 Date fecha = null;
		 try {
		     fecha = formatoDelTexto.parse(strFecha);
		 } catch (ParseException ex) {
		     ex.printStackTrace();
		 }
		 return fecha;
	}
	public static Date fechaHoraInicial(String fecInicial) {
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
			// TODO: handle exception
		}
		
		return fecI;
	}
	public static Date fechaHoraFinal(String fecInicial) {
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
			// TODO: handle exception
		}
		
		return fecI;
	}
}
