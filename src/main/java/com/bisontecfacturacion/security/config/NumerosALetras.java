package com.bisontecfacturacion.security.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class NumerosALetras {
	 private static final String[] UNIDADES = {"", "UNO", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE"};
	    private static final String[] DECENAS = {"", "DIEZ", "VEINTE", "TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
	    private static final String[] DIEZ_A_DIECINUEVE = {"DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE"};
	    private static final String[] CENTENAS = {"", "CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};

	    public static String convertirNumeroALetras(double numero) {
	        // Convertir solo la parte entera
	        long parteEntera = (long) numero;

	        // Convertir la parte entera a letras
	        String resultado = convertir(parteEntera).trim();

	        return resultado + " GUARANÍES";
	    }

	    private static String convertir(long numero) {
	        if (numero < 10) {
	            return UNIDADES[(int) numero];
	        } else if (numero < 20) {
	            return DIEZ_A_DIECINUEVE[(int) (numero - 10)];
	        } else if (numero < 100) {
	            return DECENAS[(int) (numero / 10)] + (numero % 10 != 0 ? " Y " + UNIDADES[(int) (numero % 10)] : "");
	        } else if (numero < 1000) {
	            return (numero == 100 ? "CIEN" : CENTENAS[(int) (numero / 100)]) + " " + convertir(numero % 100);
	        } else if (numero < 1_000_000) {
	            return (numero / 1000 == 1 ? "MIL" : convertir(numero / 1000) + " MIL") + " " + convertir(numero % 1000);
	        } else if (numero < 1_000_000_000) {
	            return (numero / 1_000_000 == 1 ? "UN MILLÓN" : convertir(numero / 1_000_000) + " MILLONES") + " " + convertir(numero % 1_000_000);
	        } else {
	            return (numero / 1_000_000_000 == 1 ? "MIL MILLONES" : convertir(numero / 1_000_000_000) + " MIL MILLONES") + " " + convertir(numero % 1_000_000_000);
	        }
	    }

	    public static void main(String[] args) {
	        Random random = new Random();
	        
	        // Generar 10 números aleatorios en el rango de 1 a 1.000.000.000
	        for (int i = 0; i < 10; i++) {
	            double numeroAleatorio = random.nextInt(1_000_000_000);  // Genera un número aleatorio entre 0 y 999.999.999
	            System.out.println(String.format("%.0f", numeroAleatorio) + " -> " + convertirNumeroALetras(numeroAleatorio));
	        }
	    }
}
