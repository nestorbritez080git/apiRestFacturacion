package com.bisontecfacturacion.security.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.ModeloRuc;
import com.bisontecfacturacion.security.model.Serial;
import com.bisontecfacturacion.security.model.SerialDetalle;
import com.bisontecfacturacion.security.repository.ModeloRucRepository;
import com.bisontecfacturacion.security.repository.SerialDetalleRepository;
import com.bisontecfacturacion.security.repository.SerialRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional()
@RestController
@RequestMapping("controlSerial")
public class SerialController {

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private SerialRepository entityRepository;

	@Autowired
	private SerialDetalleRepository detalleRepository;
	@Autowired
	private ModeloRucRepository modeloRucRepository;

	@RequestMapping(method=RequestMethod.GET , value="/actualizarFecha")
	public ResponseEntity<?> actualizarUltFecha() {
		Date fecha=new Date();
		Serial ul=entityRepository.findById(1).get();
		if(ul == null) {
			return new ResponseEntity<>(new CustomerErrorType("No Existe serial en base de datos"), HttpStatus.CONFLICT);
		}else {
			//Long fechaBD = Long.parseLong(ul.getUltRegistro());
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			String fechass = formater.format(fecha);
			Long fechaSistema = FechaUtil.convertirFechaStringADateUtil(fechass).getTime();
			Serial u = new Serial();
			u.setId(1);
			u.setUltRegistro(fechaSistema+"");
			u.setCodigoCliente(ul.getCodigoCliente());
			u.setIdentificador(ul.getIdentificador());
			entityRepository.save(u);
			return new ResponseEntity<String>(HttpStatus.OK);
		}

	}
	@RequestMapping(method=RequestMethod.GET , value="/actualizar/identificador/m")
	public ResponseEntity<?> actualizarIdnetificador() {
		Serial s=entityRepository.getOne(1);
		if (s == null) {
			return new ResponseEntity<>(new CustomerErrorType("NO SE ENCUENTRA REGISTRO DEL SISTEMA POR FAVOR COMUNICAR CON EL ADMINISTRADOR.!"), HttpStatus.CONFLICT);
		} else {
			if (validacion(s) == true) {
				return new ResponseEntity<>(new CustomerErrorType("IDENTIFICADOR DEL SERVIDOR ACTUALIZADO CORRECTAMENTE!"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CustomerErrorType("NO SE HA PODIDO ACTUALIZAR EL IDENTIFICADOR DEL SERVIDOR!"), HttpStatus.OK);
			}
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/{codigo}")
	public ResponseEntity<?> saveSerial(@PathVariable String codigo){
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		SerialDetalle serie= null;
		if(verificarVencimiento() == false) {	
			serie=detalleRepository.getPeriodoAcceso(anho, mes);
		}else {
			diaActual = sumarMes(diaActual, 1);
			String fechaPos = formater.format(diaActual);
			String[] fecPos=fechaPos.split("-");
			Integer mesPos=Integer.parseInt(fecPos[1]);
			Integer anhoPos=Integer.parseInt(fecPos[2]);
			serie=detalleRepository.getPeriodoAcceso(anhoPos, mesPos);
		}
		if (serie == null) {
			return new ResponseEntity<>(new CustomerErrorType("NO EXISTE NINGUN PERIODO EN ESTA FECHA: " + formater.format(diaActual)), HttpStatus.CONFLICT);
		} else {
			String existingPassword = codigo;
			String dbPassword  =  serie.getClavedb();

			if (checkPassword(existingPassword , dbPassword ) == true) {
				List<SerialDetalle> listaValidacion= detalleRepository.findAll();
				boolean operacionControl=false;
				System.out.println("size"+ listaValidacion.size());
				for (int i = 0; i < listaValidacion.size(); i++) {
					if (listaValidacion.size()==50) {
						operacionControl=true;
					}else {
//						false
						operacionControl=false;
					}
				}
				if(operacionControl==true) {
					entityRepository.updateClave(codigo, serie.getPeriodo());
					return new ResponseEntity<>(null, HttpStatus.OK);
				}else {
					return new ResponseEntity<>(new CustomerErrorType("SE HA MODIFICADO EL NUMERO DE REGISTRO DE SERIAL DETALLE"), HttpStatus.CONFLICT);
				}
			} else {
				return new ResponseEntity<>(new CustomerErrorType("LA CLAVE QUE INGRESASTE " + codigo + " NO ES VÁLIDO!"), HttpStatus.CONFLICT);
			}
		}
	}

	private boolean validacionIdentificador() {
		Serial serial= entityRepository.findById(1).orElse(null);
		if (checkPassword(getSerialNumber("C"), serial.getIdentificador())) {
			return true;
		}
		return false;
	}

	@RequestMapping(method=RequestMethod.GET, value="/generarImportacionRuc")
	public List<RucFormato>  saveSerial(){
		ImportarExcel im = new ImportarExcel(); 
		return im.leerArchivoExcel();
	}
	@RequestMapping(method=RequestMethod.GET , value="/borrarDetalle")
	public ResponseEntity<?> limpiarDetalle() {
		detalleRepository.eliminarDetalleTodos();
		return new ResponseEntity<>("SE HA LIMPIADO LOS DETALLE DEL CONTROL SERIAL", HttpStatus.OK);
	}
	@RequestMapping(method=RequestMethod.GET , value="/validacionEntrada")
	public ResponseEntity<?> validacionClaveEntrada() {
		return new ResponseEntity<>(true, HttpStatus.OK);
		/*
		if (validacionIdentificador()) {
			Date diaActual = new Date();
			String fecha = formater.format(diaActual);
			String[] fec=fecha.split("-");
			Integer mes=Integer.parseInt(fec[1]);
			Integer anho=Integer.parseInt(fec[2]);
			SerialDetalle serie= detalleRepository.getPeriodoAcceso(anho, mes);
			if (serie == null) {
				return new ResponseEntity<>(new CustomerErrorType("PERIODO DE VENCIMIENTO CONTRATO, SE DEBE AGREGAR NUEVO CONTRATO CON EL PROVEEDOR DEL SISTEMA " + formater.format(diaActual)), HttpStatus.CONFLICT);
			} else {

				if (serie.getClave() == null) {
					return new ResponseEntity<>(new CustomerErrorType("FACTURA VENCIDA PERIODO "+generarPeriodo(new Date())+", CONSULTAR CON EL PROVEEDOR"), HttpStatus.CONFLICT);
				} else {
					String existingPassword = serie.getClave();
					
					String dbPassword       =  serie.getClavedb();
					
					System.out.println("CLAVE"+existingPassword+" DB: "+dbPassword);
					if (checkPassword(existingPassword , dbPassword ) == true) {
						if(validacionFechaRegsitro()==true) {
							entityRepository.actualizarUltRegistro(String.valueOf(new Date().getTime()), 1);
							return new ResponseEntity<>(true, HttpStatus.OK);							
						}else {
							
							Serial serial= entityRepository.findById(1).orElse(null);
							Long fechaUlt= Long.parseLong(serial.getUltRegistro());
							Date fechas = new Date(fechaUlt);
							//entityRepository.updateClave(null, generarPeriodo(fechas));
							return new ResponseEntity<>(new CustomerErrorType("SE MODIFICÓ LA FECHA DEL SISTEMA ,CONSULTE CON EL PROVEEDOR PARA SOLUCIONAR EL INCONVENIENTES "), HttpStatus.CONFLICT);
						}
					} else {
						return new ResponseEntity<>(new CustomerErrorType("LA CLAVE INGRESADA PARA EL PERIODO "+generarPeriodo(new Date())+" ES INCORRECTA"), HttpStatus.CONFLICT);
					}				
				}
			}
		} else {
			return new ResponseEntity<>(new CustomerErrorType("EL IDENTIFICADOR NO ES VÁLIDO!!"), HttpStatus.CONFLICT);
		}
		*/
		
		
	}


	private boolean validacionFechaRegsitro() {
		Serial serial= entityRepository.findById(1).orElse(null);
		Long fechaUltt= Long.parseLong(serial.getUltRegistro()); 
		Date fe= new Date();
		SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
		String fechass = formater.format(fe);

		//Long fechRegActual = FechaUtil.convertirFechaStringADateUtil(fechass).getTime();
		Calendar ca = Calendar.getInstance();
		Date fecUlt = new Date(fechaUltt);
		ca.setTime(fecUlt);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		System.out.println(fecUlt+ " fecha ult reg");
		System.out.println(fe+ " feca actual");

		if(fe.getTime() >= ca.getTimeInMillis()) {
			return true;
		}else {
			return false;
		}
	}


	private boolean verificarVencimiento() {
		Date diaActual = new Date();
		Date diaultimo = new Date();
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date fechaRangoInicial = sumarDia(ca.getTime(), -120);
		diaultimo.setTime(ca.getTimeInMillis());
		if(diaActual.getTime() >= fechaRangoInicial.getTime() && diaActual.getTime() <= diaultimo.getTime()) {
			return true;

		}
		else {
			return false;
		}
	}
	@RequestMapping(method=RequestMethod.GET , value="/validarDiasRestante")
	public boolean validarDiasRestante(){
		boolean ope= false;
		if(verificarVencimiento()==true) {

			Date diaActual = new Date();
			String fecha = formater.format(sumarMes(diaActual, 1));
			String[] fec=fecha.split("-");
			Integer mes=Integer.parseInt(fec[1]);
			Integer anho=Integer.parseInt(fec[2]);
			SerialDetalle serie= detalleRepository.getPeriodoAcceso(anho, mes);
			if (serie == null) {
				return false;
			} else {
				if (serie.getClave() == null) {
					ope=true;
				}else {
					ope= false;
				}
			}
		}
		return ope;
	}

	public boolean checkPassword(String password, String hashedPassword) { 
		return passwordEncoder.matches(password, hashedPassword);
	}


	@RequestMapping(method=RequestMethod.GET , value="/generadorClave/m")
	public ResponseEntity<?> generarClave() {

		Serial s=entityRepository.getOne(1);
		if (s == null) {
			return new ResponseEntity<>(new CustomerErrorType("No hay datos en la cabecera serial!!"), HttpStatus.CONFLICT);
		} else {
			if (validacion(s) == true) {
				List<SerialDetalle> detalle =detalleRepository.findAll();
				if (detalle.size() != 0) {
					return new ResponseEntity<>(new CustomerErrorType("Ya existe datos en la grilla serial detalle!!"), HttpStatus.CONFLICT);
				} else {
					detalleRepository.saveAll(listDatosGenerados());
				}
				return new ResponseEntity<>("Registro generado con exito!!", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("La validacion no son validdddddddddddo!!", HttpStatus.OK);
			}
		}

	}

	private boolean validacion(Serial s) {
		if (checkPassword("$2a$10$GYbbCvsXMM/7S09oMqRBg.3Uw6nVI/YyaIJFZcoQTgGAv7QH4bwfq", "$2a$10$UTHJ28FgYLbpxL8azjd99OvwUhqezZB.9RZrBhHo5SAwLad4Hk66O")) {
			Date fecha=new Date();
			Serial entity=new Serial();
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			String fechass = formater.format(fecha);
			Long fechaSistema = FechaUtil.convertirFechaStringADateUtil(fechass).getTime();
			entity = s;
			entity.setUltRegistro(fechaSistema+"");
			entity.setIdentificador(passwordEncoder.encode(getSerialNumber("C")));
			entity.setValidacion("$2a$10$UTHJ28FgYLbpxL8azjd99OvwUjqezZB.9RZrBhHo5SAwLad4Hk66O");
			entityRepository.save(entity);

			return true;
		}

		return false;
	}

	public String generarNroAleatorio() {
		Random random=new Random();
		String valor = "";

		for (int i = 0; i < 6; i++) {
			valor = valor.concat(random.nextInt(9)+"");
		}

		return valor;
	}

	public String generarPeriodo(Date diaActual) {
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		return fec[2]+fec[1];
	}


	public List<SerialDetalle> listDatosGenerados() {
		Date diaActual = new Date();
		List<SerialDetalle> listado = new ArrayList<SerialDetalle>();
		for (int i = 0; i < 50 ; i++) {
			SerialDetalle serial = new SerialDetalle();
			Calendar ca = Calendar.getInstance();
			ca.set(Calendar.DAY_OF_MONTH, 1);
			diaActual.setTime(ca.getTimeInMillis());
			Date fechaI = sumarMes(diaActual, i);
			ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
			long date = ca.getTimeInMillis();
			Date fechaF = sumarMes(new Date(date), i);
			String clave = generarNroAleatorio();
			// System.out.println("clave: " + clave+ " fechaInicio: " + fechaI + " fechaFinal: " + fechaF + " periodo: " +generarPeriodo(sumarMes(diaActual, i)));
			System.out.println("clave: " + clave + " periodo: " + generarPeriodo(sumarMes(diaActual, i)));

			serial.getSerial().setId(1);
			serial.setClavedb(passwordEncoder.encode(clave));
			serial.setFechaInicio(fechaI);
			serial.setFechaFin(fechaF);
			serial.setPeriodo(generarPeriodo(sumarMes(diaActual, i)));
			listado.add(serial);

		}
		return listado;
	}

	public static Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}

	public static Date sumarMes(Date fecha, int mes) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, mes);
		return calendar.getTime();
	}

	public String getSerialNumber(String drive)
	{
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + "C:\\myscript.vbs");
			BufferedReader input =
					new BufferedReader
					(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result.trim();
	}

	@RequestMapping(method=RequestMethod.GET , value="/verificadorDiaRestante")
	public Map<String, String> verificarDiaRestante() {

		Map<String, String> map = new HashMap<>();
		Date diaultimo = new Date();
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date fechaRangoInicial = sumarDia(ca.getTime(), -120);
		diaultimo.setTime(ca.getTimeInMillis());
		map.put("fechaI", formater.format(fechaRangoInicial));
		map.put("fechaF", formater.format(diaultimo.getTime()));

		return map;
	}


	@RequestMapping(method=RequestMethod.POST, value = "/exportarRuc")
	public ResponseEntity<?> guardar(){
		ImportarExcel v= new ImportarExcel();
		List<RucFormato> lis= v.leerArchivoExcel();
		List<ModeloRuc> lisRetorno= new ArrayList<ModeloRuc>();
		System.out.println("Entroooo metood exportar ruc");
		for (RucFormato ob: lis) {
			ModeloRuc m = new ModeloRuc();
			m.setRuc(ob.getRuc());
			m.setDv(ob.getDv());
			m.setRazonSocial(ob.getRazonSocial());
			lisRetorno.add(m);
		}
		try {
			for (int i = 0; i < lisRetorno.size(); i++) {
				ModeloRuc m = lisRetorno.get(i);
				modeloRucRepository.save(m);
				System.out.println("guardo0 "+i);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("No se ha podido completar las actualizaciones de la contización del día", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	//	public static void main(String[] args) {
	//
	//
	//		Date diaActual = new Date();
	//		System.out.println(diaActual.getTime());
	//		Date diaultimo = new Date();
	//		Calendar ca = Calendar.getInstance();
	//		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
	//		System.out.println(ca.getTime()+ " fecha rango limite");
	//		Date fechaRangoInicial = sumarDia(ca.getTime(), -120);
	//		System.out.println(fechaRangoInicial.getDate()+ "  fecha rango inical");
	//		diaultimo.setTime(ca.getTimeInMillis());
	//		System.out.println(diaultimo.getDate()+"fecha del dia acceso");
	//
	//		if(diaActual.getTime() >= fechaRangoInicial.getTime() && diaActual.getTime() <= diaultimo.getTime()) {
	//			System.out.println("Esta en el rango de validarcion de notificacion");
	//		}
	//		else {
	//			System.out.println("Esta fuera del rango de validacion de notificaciones");
	//		}
	//	}



	//	public static void main(String[] args) {
	//		SerialController s = new SerialController();
	//		BCryptPasswordEncoder encode=new BCryptPasswordEncoder();
	//		Random random=new Random();
	//			SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
	//			Date diaActual = new Date();
	//			for (int i = 0; i < 10 ; i++) {
	//				Calendar ca = Calendar.getInstance();
	//				ca.set(Calendar.DAY_OF_MONTH, 1);
	//				diaActual.setTime(ca.getTimeInMillis());
	//				String fechaI = formater.format(s.sumarMes(diaActual, i));
	//				ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
	//				long date = ca.getTimeInMillis();
	//				String fechaF = formater.format(s.sumarMes(new Date(date), i));
	//				
	//				System.out.println("clave: " + s.generarNroAleatorio()+ " fechaInicio: " + fechaI + " fechaFinal: " + fechaF + " periodo: " +s.generarPeriodo(s.sumarMes(diaActual, i)));
	//				
	//			}
	//	}


}

/*
BCryptPasswordEncoder encode=new BCryptPasswordEncoder();
Random random=new Random();
System.out.println("ejecuto metodosss");
List<SerialDetalle> detalle =detalleRepository.findAll();

	for (SerialDetalle serialDetalle : detalle) {
		detalleRepository.deleteById(serialDetalle.getId());
	}
	SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
	Date diaActual = new Date();
	for (int i = 0; i < 2 ; i++) {
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		Calendar ca = Calendar.getInstance();
		System.out.println("PERIODO: "+fec[2]+""+fec[1]);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println("FE INICIAL: "+formater.format(ca.getTime()));
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println("FE FINAL: "+formater.format(ca.getTime()));

		SerialDetalle entity = new SerialDetalle();
		System.out.println("FECHA INICIO MES: ");


		SerialDetalle entity = new SerialDetalle();
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		Date diaActual = new Date();

		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		System.out.println("MES "+fec[1]+ "ANHO "+ fec[2]);
		Calendar ca = Calendar.getInstance();

		ca.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println("Primer Dia del Mes del periodo :: :  "+ fec[2]+""+fec[1]);
		System.out.println(formater.format(ca.getTime()));
		ca.getActualMinimum(Calendar.DAY_OF_MONTH);
		System.out.println("Ultimo dia del mes del pèriodo ::::  "+ fec[2]+""+fec[1]);
		System.out.println(ca.getActualMaximum(Calendar.DAY_OF_MONTH));

		entity.setClavedb(encode.encode(generarNroAleatorio()+getSerialNumber("C")));


		System.out.println(generarNroAleatorio());
		entity.setPeriodo(fec[2]+""+fec[1]);
		System.out.println(formater.format(ca.getTime())+" ******primera efecha");
		entity.setFechaInicio(ca.getTime());
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println(formater.format(ca.getTime())+" ******ultima fecha");

		//entity.setFechaFin(formater.format(fefin));

		//detalleRepository.save(entity);

	@RequestMapping(method=RequestMethod.GET , value="/actualizarFecha")
	public ResponseEntity<?> actualizarUltFecha() {
		Date fecha=new Date();
		Serial ul=entityRepository.findById(1).get();
		Long fechaBD = Long.parseLong(ul.getUltRegistro());
		Long fechaSistema = Long.parseLong(fecha.getTime()+"");
		Date f = new Date(fechaBD);
		if (fechaBD <= fechaSistema) {
			Serial u = new Serial();
			u.setId(1);
			u.setUltRegistro(fechaSistema+"");
			u.setCodigoCliente(ul.getCodigoCliente());
			u.setIdentificador(ul.getIdentificador());
			entityRepository.save(u);
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CustomerErrorType("HUBO UN CAMBIO DE FECHA!"), HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(method=RequestMethod.GET , value="/verificarEntrada/{clave}")
	public ResponseEntity<?> verificarEntrada(@PathVariable String clave) {
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");

		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		String identificador="";
		identificador= getSerialNumber("C");
		SerialDetalle serie=detalleRepository.getPeriodoAcceso(anho, mes);
		Serial seriales = entityRepository.findById(1).get();
		if(serie==null){
			return new ResponseEntity<>(new CustomerErrorType("CONTRATO VENCIDO. DEBES CONTACTAR CON EL PROVEEDOR DEL SISTEMA!"), HttpStatus.CONFLICT);
		}else{
			if(serie.getClave()!=null){
				if(serie.getClave().equals(clave)) {
					if(seriales.getIdentificador().equals(identificador)) {
						return new ResponseEntity<String>(HttpStatus.OK);
					}else {
					return new ResponseEntity<>(new CustomerErrorType("ERROR AL CONECTAR CON EL SERVIDOR. ES PROBABLE QUE HAYA HABIDO CAMBIO EN EL HARWARE DEL SISTEMA. CONTACTE CON EL PROVEEDOR DEL SISTEMA!"), HttpStatus.CONFLICT);
					}
				}else {return new ResponseEntity<>(new CustomerErrorType("CLAVE DE SERIE NO COINCIDE!"), HttpStatus.CONFLICT);
				}
			}else {
				return new ResponseEntity<>(new CustomerErrorType("ENTRADA INVALIDA!"), HttpStatus.CONFLICT);
			}

		}

	}

	@RequestMapping(method=RequestMethod.GET , value="/periodoSiguiente/{clave}")
	public void getPeriodo(@PathVariable String clave) {
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		SerialDetalle serie=detalleRepository.getPeriodoAcceso(anho, mes);
		SerialDetalle serie1=detalleRepository.findById(serie.getId()+1).get();
		entityRepository.updateClave(clave, serie1.getPeriodo());
	}

	@RequestMapping(method=RequestMethod.GET , value="/verificadorDiaRestante")
	public Serial verificarDiaRestante() {
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		SerialDetalle serie=detalleRepository.getPeriodoAcceso(anho, mes);
		SerialDetalle mesSiguiente=detalleRepository.findById(serie.getId()+1).get();
		Serial ss = new Serial();
		if (mesSiguiente != null) {
		SerialDetalle s=new SerialDetalle();
		Date fechF=serie.getFechaFin();	
		Date fechaInicioRestante = sumarDia(fechF, -120);
		s.setFechaInicio(fechaInicioRestante);
		s.setFechaFin(serie.getFechaFin());


		if (diaActual.getTime() >= fechaInicioRestante.getTime() && diaActual.getTime() <= serie.getFechaFin().getTime()) {
			ss.setIdentificador("aler");
		}
		if (mesSiguiente.getClave() == null) {
			ss.setId(245);
		}
		}

		return ss;
	}

	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}

	public Date sumarMes(Date fecha, int mes) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MONTH, mes);
		return calendar.getTime();
	}

	@RequestMapping(method=RequestMethod.GET , value="/periodo")
	public SerialDetalle getPeriodo() {
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		System.out.println(anho+ " "+ mes);
		SerialDetalle serie=detalleRepository.getPeriodoAcceso(anho, mes);
		SerialDetalle s = new SerialDetalle();
		s.setPeriodo(serie.getPeriodo());
		s.setClave(serie.getClave());
		System.out.println(s.getPeriodo()+ " - "+s.getClave());
		return s;
	}

	@RequestMapping(method=RequestMethod.GET , value="/periodoSiguiente")
	public SerialDetalle getPeriodoSiquiente() {
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		Date diaActual = new Date();
		String fecha = formater.format(diaActual);
		String[] fec=fecha.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer anho=Integer.parseInt(fec[2]);
		SerialDetalle serie=detalleRepository.getPeriodoAcceso(anho, mes);
		SerialDetalle mesSiguiente=detalleRepository.findById(serie.getId()+1).get();
		SerialDetalle s = new SerialDetalle();
		s.setPeriodo(mesSiguiente.getPeriodo());
		s.setClave(mesSiguiente.getClave());
		return s;
	}


	@RequestMapping(method=RequestMethod.GET , value="/updateClave/{periodo}/{clave}")
	public void saveClave(@PathVariable String periodo, @PathVariable String clave) {



//		if () {
//			
//		} else {
//			return new ResponseEntity<>(new CustomerErrorType("La clave que ingresaste no son correcto!!"), HttpStatus.CONFLICT);
//		}


		entityRepository.updateClave(clave, periodo);
	}

	public static String getSerialNumber(String drive)
	    {
		 String result = "";
		    try {
		      File file = File.createTempFile("realhowto",".vbs");
		      file.deleteOnExit();
		      FileWriter fw = new java.io.FileWriter(file);

		      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
		                  +"Set colDrives = objFSO.Drives\n"
		                  +"Set objDrive = colDrives.item(\"" +drive+ "\")\n"
		                  +"Wscript.Echo objDrive.SerialNumber";  // see note
		      fw.write(vbs);
		      fw.close();
		      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
		      BufferedReader input =
		        new BufferedReader
		          (new InputStreamReader(p.getInputStream()));
		      String line;
		      while ((line = input.readLine()) != null) {
		         result += line;
		      }
		      input.close();
		    }
		    catch(Exception e){
		        e.printStackTrace();
		    }
		    return result.trim();
	 }

	@RequestMapping(method=RequestMethod.GET , value="/codigoCliente")
	public String getCodigoCliente() {
	Serial serial = entityRepository.findById(1).get();
		return serial.getCodigoCliente();
	}
	@RequestMapping(method=RequestMethod.GET , value="/actualizarIdentificador")
	public String actualizarIdentificador() {
		Serial s = entityRepository.findById(1).get();
		String identificador = getSerialNumber("C");
		Serial se = new Serial();
		se.setIdentificador(identificador);
		se.setId(s.getId());
		se.setUltRegistro(s.getUltRegistro());
		se.setCodigoCliente(s.getCodigoCliente());
		entityRepository.save(se);
		return "d";
	}

	@RequestMapping(method=RequestMethod.GET , value="/generarFechaActual")
	public String getFechaActual() {
		Date fe=new Date();
		entityRepository.actualizarUltRegistro(fe.getTime()+"", 1);
		return "Fecha Actualizado...";
	}

	@RequestMapping(method=RequestMethod.GET , value="/pruebas")
	public String getFechaActualPrueba() {

		BCryptPasswordEncoder password=new BCryptPasswordEncoder();
		System.out.println(password.encode("hola"));
		System.out.println(password.encode("hola"));
		System.out.println(password.encode("hola1"));

		String hashedPassword = "$2a$10$5ShIUk6Nj/iK19Znj6UXG.VezAEEiZccp7fzAZnUkQz/OT.eEx.Ia";

		// boolean isPasswordMatched = passwordEncoder.matches("$2a$10$5ShIUk6Nj/iK19Znj6UXG.VezAEEiZccp7fzAZnUkQz/OT.eEx.Ia", hashedPassword);
		System.out.println("");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String existingPassword = "hola"; // Password entered by user
		String dbPassword       = "$2a$10$qE7N2Y.O3.zKs1MxBpHqjuS8hqLkerRlOLnXL3yKMMgeXIQ.bDqk6"; // Load hashed DB password


		System.out.println(checkPassword(existingPassword, dbPassword));

		return "454";
	}


	public boolean checkPassword(String password, String hashedPassword) { 
		       return passwordEncoder.matches(password, hashedPassword);
		} 

 */
