package com.bisontecfacturacion.security.config;

public class Utilidades {
	
	public static String eliminaCaracterIzqDer(String cad){
		String cadenaRetorno="";
		if(cad.equals("") || cad == null){
			cadenaRetorno="";
		}else {
			String  cadena="";
			String[] acad=cad.split("");
			int posL = 0,posR = acad.length;
			for(int i=0;i<acad.length;i++){
				if(!acad[i].equals(Character.toString(' '))){posL=i;break;}
			}
			for(int i=acad.length;i>0;i--){
				if(!acad[i-1].equals(Character.toString(' '))){posR=i;break;}            
			}
			cadena = cad.substring(posL,posR);
			cadenaRetorno = cadena.replaceAll(" +", " ");			
		}
		System.out.println(cadenaRetorno+ " limpiado");
	    return cadenaRetorno;
	}

	public static Double calcularIvaCinco(Double monto) {
		Double res=0.0;
		if(monto>0) {
		res= monto/21;
		return res;
		}else {
		res = 0.0;};
		return res;
	}
	public static Double calcularIvaDies(Double monto) {
		Double res=0.0;
		if(monto>0) {
		res= monto/11;
		return res;
		}else {
		res = 0.0;};
		return res;
	}
}
