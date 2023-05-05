package com.bisontecfacturacion.security.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.ConfigCopiaSeguridad;
import com.bisontecfacturacion.security.repository.ConfigCopiaSeguridadRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@RestController
@RequestMapping("backup")
public class BaseDeDatos {
	
	@Autowired
	private ConfigCopiaSeguridadRepository configCopiaSeguridadRepository;

public ResponseEntity<?> db(String datos) {
try{
    Runtime r =Runtime.getRuntime();
    //Path to the place we store our backup
    String rutaCT = "C:\\Program Files (x86)\\PostgreSQL\\9.5\\bin\\";
    String rutaCT1 = datos;
    //PostgreSQL variables
    String IP = "localhost";
    String user = "postgres";
    String dbase = "db_bisontec_salina";
    String password = "NC198168ab";
    Process p;
    ProcessBuilder pb;
    InputStreamReader reader;
    BufferedReader buf_reader;
    String line;
    //We build a string with today's date (This will be the backup's filename)
    java.util.TimeZone zonah = java.util.TimeZone.getTimeZone("GMT+1");
    java.util.Calendar Calendario = Calendar.getInstance( zonah, new java.util.Locale("es"));
    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    StringBuffer date = new StringBuffer();
    date.append(df.format(Calendario.getTime()));
    java.io.File file = new java.io.File(rutaCT);
    // We test if the path to our programs exists
    if(file.exists()){
        // We then test if the file we're going to generate exist too. If so we will delete it
        StringBuffer fechafile = new StringBuffer();
        fechafile.append(rutaCT1);
        fechafile.append("BACKUP-");
        fechafile.append(date.toString());
        fechafile.append("-SALINA");
        fechafile.append(".backup");
        java.io.File ficherofile = new java.io.File(fechafile.toString());
        if(ficherofile.exists()){
            ficherofile.delete();
        }
        r =Runtime.getRuntime();
        pb = new ProcessBuilder(rutaCT + "pg_dump.exe", "-f", fechafile.toString(),
                "-F", "c", "-Z", "9", "-v", "-o", "-h",IP, "-U", user, dbase);
        pb.environment().put("PGPASSWORD", password);
        pb.redirectErrorStream(true);
        p = pb.start();
        try{
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String ll;
            while ((ll = br.readLine()) != null) {
                System.out.println(ll);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new CustomerErrorType("HUBO UN PROBLEMA: " + e.getMessage() + e), HttpStatus.CONFLICT);
           // log("ERROR "+e.getMessage(), e);
        }
    }
} catch(IOException x) {
    System.err.println("Could not invoke browser, command=");
    return new ResponseEntity<>(new CustomerErrorType("HUBO UN PROBLEMA LA CAUSA ES: " + x.getMessage()), HttpStatus.CONFLICT);
 //   System.err.println("Caught: " + x.getMessage());
}
        return  new  ResponseEntity<String>(HttpStatus.CREATED);
}

/*
public static void main(String[] args) {
	BaseDeDatos b=new BaseDeDatos();
	File unidades[] = File.listRoots();
	for(int i=0;i<unidades.length;i++) {
		
		String separar = FileSystemView.getFileSystemView().getSystemDisplayName (unidades[i]);
		String[] separado = separar.split(" ");
	//	System.out.println(separado[0]);
		if (separado[0].equals("SALINA")) {
	    b.db(unidades[i]+"");
	    System.out.println(unidades[i]+"");
	    System.out.println(FileSystemView.getFileSystemView().getSystemDisplayName (unidades[i]));
			 } 	
		}
}
*/

@RequestMapping(method=RequestMethod.GET)
	public List<Backup> getDiscoExterno() {
        List<Backup> lista = new ArrayList<>();
        File unidades[] = File.listRoots();
        for(int i=0;i<unidades.length;i++) {
            Backup b=new Backup();
            b.setDescripcion(FileSystemView.getFileSystemView().getSystemDisplayName (unidades[i]));
            lista.add(b);
        }
        return lista;

    }

    @RequestMapping(method=RequestMethod.GET, value = "copia")
	public ResponseEntity<?> crearCopia() {
    	
    	ConfigCopiaSeguridad copia=new ConfigCopiaSeguridad();
    	copia=configCopiaSeguridadRepository.findById(1).get();
    	
        BaseDeDatos b=new BaseDeDatos();
        File unidades[] = File.listRoots();
        
        boolean ePendirive=false;
        for(int i=0;i<unidades.length;i++) {
            System.out.println("*****************entro");
            String separar = FileSystemView.getFileSystemView().getSystemDisplayName (unidades[i]);
            String[] separado = separar.split(" ");
            if (separado[0].equals(copia.getNombrePendrive())) {
            	ePendirive = true;
                System.out.println("entro en if ************");
                try{
                    Runtime r =Runtime.getRuntime();
                    //Path to the place we store our backup
                    String rutaCT = copia.getRuta();
                    String rutaCT1 = unidades[i]+"";
                    //PostgreSQL variables
                    String IP = "localhost";
                    String user = "postgres";
                    String dbase = copia.getNombreBaseDatos();
                    String password = "NC198168ab";
                    Process p;
                    ProcessBuilder pb;
                    InputStreamReader reader;
                    BufferedReader buf_reader;
                    String line;
                    //We build a string with today's date (This will be the backup's filename)
                    java.util.TimeZone zonah = java.util.TimeZone.getTimeZone("GMT+1");
                    java.util.Calendar Calendario = Calendar.getInstance( zonah, new java.util.Locale("es"));
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    java.text.SimpleDateFormat dfhora = new java.text.SimpleDateFormat("HH");
                    java.text.SimpleDateFormat dfminute = new java.text.SimpleDateFormat("mm");
                    StringBuffer date = new StringBuffer();
                    StringBuffer hora = new StringBuffer();
                    StringBuffer minuto = new StringBuffer();
                    date.append(df.format(Calendario.getTime()));
                    hora.append(dfhora.format(Calendario.getTime()));
                    minuto.append(dfminute.format(Calendario.getTime()));
                    java.io.File file = new java.io.File(rutaCT);
                    // We test if the path to our programs exists
                    if(file.exists()){
                        // We then test if the file we're going to generate exist too. If so we will delete it
                        StringBuffer fechafile = new StringBuffer();
                        fechafile.append(rutaCT1);
                        fechafile.append("BACKUP-");
                        fechafile.append(date.toString());
                        fechafile.append("-");
                        fechafile.append(hora.toString()+"H"+minuto.toString()+"M");
                        fechafile.append("-" + copia.getNombrePendrive());
                        fechafile.append(".backup");
                        java.io.File ficherofile = new java.io.File(fechafile.toString());
                        if(ficherofile.exists()){
                            ficherofile.delete();
                        }
                        r =Runtime.getRuntime();
                        pb = new ProcessBuilder(rutaCT + "pg_dump.exe", "-f", fechafile.toString(),
                                "-F", "c", "-Z", "9", "-v", "-o", "-h",IP, "-U", user, dbase);
                        pb.environment().put("PGPASSWORD", password);
                        pb.redirectErrorStream(true);
                        p = pb.start();
                        try{
                            InputStream is = p.getInputStream();
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader br = new BufferedReader(isr);
                            String ll;
                            String retorno ="";
                            while ((ll = br.readLine()) != null) {
                                String [] vector = ll.toString().split(":");
                                for (int in = 0; in < vector.length; in++) {
                                    System.out.println("asfsadfasfasdfasdf vectorororoororo : "+ vector [in]);
                                    if (in > 2 ){
                                        retorno =retorno.concat(vector[in].toString());
                                    }
                                }
                                if(!retorno.equals("")){
                                    return new ResponseEntity<>(new CustomerErrorType("HUBO UN PROBLEMA AL GENERAR EL BACKUP DETALLE DEL PROBLEMA: "+retorno), HttpStatus.CONFLICT);
                                }
                            }
                        } catch (IOException e) {
                            return new ResponseEntity<>(new CustomerErrorType(" HUBO UN PROBLEMA: " + e.getMessage() + e), HttpStatus.CONFLICT);
                           // log("ERROR "+e.getMessage(), e);
                        }
                    }
                } catch(IOException x) {
                    System.err.println("Could not invoke browser, command=");
                    return new ResponseEntity<>(new CustomerErrorType("HUBO UN PROBLEMA LA CAUSA ES: " + x.getMessage()), HttpStatus.CONFLICT);
                 //   System.err.println("Caught: " + x.getMessage());
                }
                 }	
            }
        if(ePendirive==false) { return new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRÓ NINGUN PENDRIVE CONECTADO AL SERVIDOR CON EL NOMBRE CORRECTO DEL DISPOSITIVO: "), HttpStatus.CONFLICT);
}
       
        return  new  ResponseEntity<String>(HttpStatus.CREATED);

    }

    
    @RequestMapping(method=RequestMethod.GET, value = "copiaAutomatica")
  	public ResponseEntity<?> crearCopiaAutomatica() {
    	
        Date horaDespertar = new Date(System.currentTimeMillis());
        
        Calendar c = Calendar.getInstance();
        c.setTime(horaDespertar);
       
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        // Si la hora es posterior a las 8am se programa la alarma para el dia siguiente
        if (c.get(Calendar.HOUR_OF_DAY) >= 22) {
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + 1);
        }
        
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 35);
        c.set(Calendar.SECOND, 0);
        
        horaDespertar = c.getTime();
        System.out.println(horaDespertar);
        System.out.println(c.get(Calendar.DAY_OF_WEEK));
        // El despertador suena cada 24h (una vez al dia)
        int tiempoRepeticion = 86400000; 
        
        // Programamos el despertador para que "suene" a las 8am todos los dias 
        Timer temporizador = new Timer();
        temporizador.schedule(new Temporizador(), horaDespertar, tiempoRepeticion);
       
        
    	return  new  ResponseEntity<String>(HttpStatus.CREATED);
    }
    

}