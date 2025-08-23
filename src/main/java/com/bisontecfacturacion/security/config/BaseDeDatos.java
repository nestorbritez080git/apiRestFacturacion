package com.bisontecfacturacion.security.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;

import javax.swing.filechooser.FileSystemView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

	
	 private Set<String> backupsEjecutadosHoy = new HashSet<>(); // Para evitar repetir backup en el mismo minuto

	
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
	public static void main(String[] args) {
		  ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Asuncion"));
	        System.out.println("Hora actual Asunción: " + ahora);
	}
	 @Scheduled(fixedDelay = 30000) // Cada 60 segundos
	    public void revisarHorarioBackup() {
	        try {
	            ConfigCopiaSeguridad config = configCopiaSeguridadRepository.findById(1).orElse(null);
	            if (config == null) {
	                System.err.println("No se encontró configuración para copia de seguridad");
	                return;
	            }

	            // Supongamos que en ConfigCopiaSeguridad guardás las horas en formato "HH:mm"
	            // Por ejemplo: "11:50,17:00"
	            String horasBackup = config.getHora1(); // ej: "11:50,17:00"
	            System.out.println(horasBackup);
	            if (horasBackup == null || horasBackup.isEmpty()) {
	                System.err.println("No hay horarios configurados para backup");
	                return;
	            }

	            String[] horarios = horasBackup.split(",");
	            System.out.println(horarios);
	            ZonedDateTime ahora = ZonedDateTime.now(ZoneId.of("America/Asuncion"));
	            System.out.println("Hora exacta: " + ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

	            for (String horario : horarios) {
	                LocalTime horaConfig = LocalTime.parse(horario.trim()); // parsea "HH:mm"

	                // Comparamos horas y minutos con la hora actual
	                if (ahora.getHour() == horaConfig.getHour() && ahora.getMinute() == horaConfig.getMinute()) {
	                    // Para evitar que se ejecute múltiples veces en ese minuto, usamos un Set con fecha + hora
	                    String claveEjecucion = LocalDate.now().toString() + "-" + horario;

	                    if (!backupsEjecutadosHoy.contains(claveEjecucion)) {
	                        System.out.println("Ejecutando backup programado para " + horario);
	                        backupsEjecutadosHoy.add(claveEjecucion);

	                        // Ejecutar tu método de backup
	                        //ejecutarBackup();
	                        crearCopia();
	                    }else {
							System.out.println("else de contais");
						}
	                }else {
	                	System.out.println("LA HORA NO COINCIDE: "+ahora.getHour() + " : "+horaConfig.getHour());
	                	System.out.println("LA minutos NO COINCIDE: "+ahora.getMinute() + " : "+horaConfig.getMinute());

	                }
	            }

	            // Limpiar el Set para el día siguiente (podés hacerlo con otra tarea programada a medianoche)
	            if (backupsEjecutadosHoy.size() > 1000) { // por ejemplo, para evitar crecer indefinidamente
	                backupsEjecutadosHoy.clear();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 @Scheduled(cron = "0 0 0 * * *") // A las 00:00 todos los días
	    public void resetearEjecuciones() {
	        backupsEjecutadosHoy.clear();
	        System.out.println("Reseteo de registros de backups ejecutados");
	 }
	@Scheduled(cron = "0 59 2 * * *")
	public void crearCopiaAutomaticoo() {
		try {
	    	ConfigCopiaSeguridad copia = configCopiaSeguridadRepository.findById(1).orElse(null);
	        if (copia == null) {
	           // new ResponseEntity<>(new CustomerErrorType("No se encontró configuración de copia de seguridad."), HttpStatus.CONFLICT);
	        }
	        // Buscar pendrive conectado con nombre coincidente
	        File[] unidades = File.listRoots();
	        String unidadPendrive = null;

	        for (File unidad : unidades) {
	            String nombre = FileSystemView.getFileSystemView().getSystemDisplayName(unidad);
	            if (nombre.startsWith(copia.getNombrePendrive())) {
	                unidadPendrive = unidad.getAbsolutePath();
	                break;
	            }
	        }

	        if (unidadPendrive == null) {
	        	System.out.println("No se encontro un pendrive conectado con el nombre:" + copia.getNombrePendrive());
	           // return new ResponseEntity<>(new CustomerErrorType("No se encontró un pendrive conectado con el nombre: " + copia.getNombrePendrive()), HttpStatus.CONFLICT);
	        }

	        // Verificar ruta del pg_dump.exe
	        String rutaPgDump = copia.getRuta() + "pg_dump.exe";
	        File pgDump = new File(rutaPgDump);
	        if (!pgDump.exists()) {
	        	System.out.println("No se encontró pg_dump.exe en la ruta configurada: " + rutaPgDump);
	            //return new ResponseEntity<>(new CustomerErrorType("No se encontró pg_dump.exe en la ruta configurada: " + rutaPgDump), HttpStatus.CONFLICT);
	        }

// Construir nombre del archivo de backup
	        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	        String nombreArchivo = "BACKUP-" + formatoFecha.format(new Date()) + "-" + copia.getNombrePendrive() + ".backup";
	        String pathBackup = unidadPendrive + nombreArchivo;

	        File archivoBackup = new File(pathBackup);
	        if (archivoBackup.exists()) archivoBackup.delete();
	        String pgDumpPath = rutaPgDump;
        
        String host = copia.getHost();
        String puerto = copia.getPuerto();
        String usuario = "postgres";
        String baseDatos = copia.getNombreBaseDatos();
        String archivoSalida = pathBackup;
        String password = ""+copia.getPassword()+"";
        System.out.println(copia.getPassword()+ " passswww");
        ProcessBuilder pb = new ProcessBuilder(
            pgDumpPath,
            "-h", host,
            "-p", puerto,
            "-U", usuario,
            "-F", "c",    // formato personalizado
            "-b",         // incluir blobs
            "-v",         // verbose
            "-f", archivoSalida,
            baseDatos
        );

        pb.environment().put("PGPASSWORD", password);

        Process process = pb.start();

        // Hilo para leer stdout
        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[pg_dump stdout] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Hilo para leer stderr
        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("[pg_dump stderr] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stdoutThread.start();
        stderrThread.start();

        // Esperar que termine el proceso
        int exitCode = process.waitFor();

        // Asegurarse de que ambos hilos terminen
        stdoutThread.join();
        stderrThread.join();

        if (exitCode == 0) {
	       System.out.println("Backup generado correctamente en: " + archivoSalida);
        	// return new ResponseEntity<>(new CustomerErrorType("Backup generado correctamente en: " + archivoSalida), HttpStatus.CREATED);
        } else {
            System.err.println("❌ Error al generar el backup. Código de salida: " + exitCode);
	        //return new ResponseEntity<>(new CustomerErrorType("❌ Error al generar el backup. Código de salida: " + exitCode), HttpStatus.CONFLICT);
        }

	    } catch (Exception e) {
	    	e.printStackTrace();
	        //return new ResponseEntity<>(new CustomerErrorType("Error al generar backup del sistema: " + e.getMessage()), HttpStatus.CONFLICT);
	    }
	}
	@RequestMapping(method = RequestMethod.GET, value = "copia")
	public ResponseEntity<?> crearCopia() {
	    try {
	    	ConfigCopiaSeguridad copia = configCopiaSeguridadRepository.findById(1).orElse(null);
	        if (copia == null) {
	           return new ResponseEntity<>(new CustomerErrorType("No se encontró configuración de copia de seguridad."), HttpStatus.CONFLICT);
	        }
	        // Buscar pendrive conectado con nombre coincidente
	        File[] unidades = File.listRoots();
	        String unidadPendrive = null;

	        for (File unidad : unidades) {
	            String nombre = FileSystemView.getFileSystemView().getSystemDisplayName(unidad);
	            if (nombre.startsWith(copia.getNombrePendrive())) {
	                unidadPendrive = unidad.getAbsolutePath();
	                break;
	            }
	        }

	        if (unidadPendrive == null) {
	            return new ResponseEntity<>(new CustomerErrorType("No se encontró un pendrive conectado con el nombre: " + copia.getNombrePendrive()), HttpStatus.CONFLICT);
	        }

	        // Verificar ruta del pg_dump.exe
	        String rutaPgDump = copia.getRuta() + "pg_dump.exe";
	        File pgDump = new File(rutaPgDump);
	        if (!pgDump.exists()) {
	            return new ResponseEntity<>(new CustomerErrorType("No se encontró pg_dump.exe en la ruta configurada: " + rutaPgDump), HttpStatus.CONFLICT);
	        }

// Construir nombre del archivo de backup
	        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
	        String nombreArchivo = "BACKUP-" + formatoFecha.format(new Date()) + "-" + copia.getNombrePendrive() + ".backup";
	        String pathBackup = unidadPendrive + nombreArchivo;

	        File archivoBackup = new File(pathBackup);
	        if (archivoBackup.exists()) archivoBackup.delete();
	        String pgDumpPath = rutaPgDump;
        
        String host = copia.getHost();
        String puerto = copia.getPuerto();
        String usuario = "postgres";
        String baseDatos = copia.getNombreBaseDatos();
        String archivoSalida = pathBackup;
        String password = ""+copia.getPassword()+"";
        System.out.println(copia.getPassword()+ " passswww");
        ProcessBuilder pb = new ProcessBuilder(
            pgDumpPath,
            "-h", host,
            "-p", puerto,
            "-U", usuario,
            "-F", "c",    // formato personalizado
            "-b",         // incluir blobs
            "-v",         // verbose
            "-f", archivoSalida,
            baseDatos
        );

        pb.environment().put("PGPASSWORD", password);

        Process process = pb.start();

        // Hilo para leer stdout
        Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[pg_dump stdout] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Hilo para leer stderr
        Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("[pg_dump stderr] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stdoutThread.start();
        stderrThread.start();

        // Esperar que termine el proceso
        int exitCode = process.waitFor();

        // Asegurarse de que ambos hilos terminen
        stdoutThread.join();
        stderrThread.join();

        if (exitCode == 0) {
	        return new ResponseEntity<>(new CustomerErrorType("Backup generado correctamente en: " + archivoSalida), HttpStatus.CREATED);
        } else {
            System.err.println("❌ Error al generar el backup. Código de salida: " + exitCode);
	        return new ResponseEntity<>(new CustomerErrorType("❌ Error al generar el backup. Código de salida: " + exitCode), HttpStatus.CONFLICT);
        }

	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>(new CustomerErrorType("Error al generar backup del sistema: " + e.getMessage()), HttpStatus.CONFLICT);
	    }
	    
	}

	/*
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
	*/


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