package com.bisontecfacturacion.security.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FechaUtil {
	public static Date convertirFechaUtilATimeZone(Date date){
		 Timestamp ts=new Timestamp(System.currentTimeMillis());  
         date=ts;  
         return date;
	}
	public static Date convertirFechaStringADateUtil(String v) {
		// el que parsea
		SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// el que formatea
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		Calendar calendar = Calendar.getInstance(); 
		try {
			date = parseador.parse(v);
			
			calendar.setTime(date);
			System.out.println(calendar.getTime());
			//System.out.println(formateador.);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//System.out.println(formato.format(calendar.getTime()));
		return calendar.getTime();
		
		}
}
