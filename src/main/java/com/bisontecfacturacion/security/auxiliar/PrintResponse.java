package com.bisontecfacturacion.security.auxiliar;

public class PrintResponse {
	 private boolean success;
     private String message;

     public PrintResponse(boolean success, String message) {
         this.success = success;
         this.message = message;
     }

     public boolean isSuccess() {
         return success;
     }

     public String getMessage() {
         return message;
     }
}
