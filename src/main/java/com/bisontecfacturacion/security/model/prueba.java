package com.bisontecfacturacion.security.model;

import java.util.Scanner;

public class prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int n =0, men=0, may=0, posmenor=0, posmayor=0;
		for (int i = 0; i < 5; i++) {
			System.out.println("Ingrese valor");
			n=sc.nextInt();
			if (i==0) {
				men=n; may=n;
				posmayor=i;
				posmenor=i;
			}
			if (n>may) {
				may=n;
				posmayor=i;
			}
			if (n<men) {
				men=n;
				posmenor=i;
			}
		}
		System.out.println("El menor es : "+men+" posicion="+posmenor+" y el mayor es :" + may+ "posicion="+posmayor);
	}

}
