package com.bisontecfacturacion.security.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaUtil {
	public static Date convertirFechaUtilATimeZone(Date date){
		 Timestamp ts=new Timestamp(System.currentTimeMillis());  
         date=ts;  
         return date;
	}
	public static Date convertirFechaStringADateUtil(String v) {
		// TODO Auto-generated method stub
		System.out.println(v);
		 SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		 String strFecha = v;
		 Date fecha = null;
		 System.out.println(fecha+ "sadfasfdasfa");
		 try {
			 //System.out.println("ffffff"+formatoDelTexto.format(strFecha));
		     fecha = formatoDelTexto.parse(strFecha);
		     System.out.println("fecahsss"+fecha);
		 } catch (ParseException ex) {
		     ex.printStackTrace();
		 }
		 return fecha;
	}
}
