package com.bisontecfacturacion.security.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.NroDocumento;
import com.bisontecfacturacion.security.model.AutoImpresor;
import com.bisontecfacturacion.security.model.LoteBoleta;
import com.bisontecfacturacion.security.model.LoteFactura;
import com.bisontecfacturacion.security.model.LoteNotaPedido;
import com.bisontecfacturacion.security.model.LoteTicket;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.repository.AutoImpresorRepository;
import com.bisontecfacturacion.security.repository.LoteBoletaRepository;
import com.bisontecfacturacion.security.repository.LoteFacturaRepository;
import com.bisontecfacturacion.security.repository.LoteNotaPedidoRepository;
import com.bisontecfacturacion.security.repository.LoteTicketRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;


@RestController
@RequestMapping("/loteDocumento")
public class LoteDocumentoController {
	
	@Autowired
	private LoteFacturaRepository loteFacturaRepository;
	@Autowired
	private AutoImpresorRepository autoImpresorRepository;
	@Autowired
	private LoteBoletaRepository loteBoletaRepository;
	@Autowired
	private LoteTicketRepository loteTicketRepository;
	@Autowired
	private LoteNotaPedidoRepository loteNotaPedidoRepository;
	@Autowired
	private IUsuarioService usuarioService;
	
	private static Formatter ft;
	
	@RequestMapping(method=RequestMethod.GET, value="/loteFactura")
	public List<LoteFactura> getAllLoteFactura(){
		return loteFacturaRepository.findByOrderByIdDesc();
	}
	@RequestMapping(method=RequestMethod.GET, value="/autoImpresor")
	public List<AutoImpresor> getAllLoteAutoImpresor(){
		return autoImpresorRepository.findByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/autoImpresor/buscarId/{id}")
	public AutoImpresor getAllLoteAutoImpresor(@PathVariable int id){
		return autoImpresorRepository.findById(id).orElse(null);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/autoImpresor/eliminarId/{id}")
	public void eliminarAutoImpresorPorId(@PathVariable int id){
		 autoImpresorRepository.deleteById(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteFacturaEstado")
	public ResponseEntity<?> getUltLoteFactura(){
		LoteFactura ff= new LoteFactura();
		try {
			ff= loteFacturaRepository.getEstadoLoteFactura(true);
			if (ff == null) {
				return new ResponseEntity<>(new CustomerErrorType("La cantidad de expedición de la factura ha alcanzado su límite.!!\nPor favor verifique le timbrado\nSi es requerido favor agregar nuevo número de timbrado válido "), HttpStatus.CONFLICT);
			}else {
				Date fec1= new Date();
				Date fec2= new Date();		
				Date fecHoy= new Date();
				DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		        String fechaInicioString = df.format(ff.getTimbradoInicio());
		        try {
		         fec1 = df.parse(fechaInicioString);
		        } catch (ParseException ex) {
		        }
		        String fechaFinalString = df.format(ff.getTimbradoFin());
		        try {
		            fec2 = df.parse(fechaFinalString);
		        } catch (ParseException ex) {
		        }
		        @SuppressWarnings("unused")
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				Date fechaAux= new Date();
				fechaAux = FechaUtil.convertirFechaUtilATimeZone(new Date());
		        String fechaHoyRec= df.format(fechaAux );
		        try {
		           fecHoy = df.parse(fechaHoyRec);
		        } catch (ParseException ex) {
		        }

		        long fechaInicialMs = fec1.getTime();
		        long fechaFinalMs = fec2.getTime();
		        long fechaReferencia = fecHoy.getTime();
		        if( (fechaReferencia >= fechaInicialMs) && (fechaReferencia <= fechaFinalMs)){
					return new ResponseEntity<>(ff, HttpStatus.OK);	
				}else {
					return new ResponseEntity<>(new CustomerErrorType("No se puede Facturar.!!\nEl número de timbrado está vencido"), HttpStatus.CONFLICT);	
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(method=RequestMethod.POST, value="/loteFactura")
	public ResponseEntity<?> saveLoteFactura(@RequestBody LoteFactura entity){
		LoteFactura lFac= new LoteFactura();
		lFac =loteFacturaRepository.getEstadoLoteFacturaEstadoInactivo(true);
		if(lFac ==  null) {
			entity.setSerieActual(entity.getSerieInicial());
			return new ResponseEntity<>(loteFacturaRepository.save(entity), HttpStatus.OK);
		}
			if (lFac.getCantidadActual() >= lFac.getCantidadExpedicion()) {	
				lFac.setEstado(false);
				loteFacturaRepository.save(lFac);
				entity.setSerieActual(entity.getSerieInicial());
				return new ResponseEntity<>(loteFacturaRepository.save(entity), HttpStatus.OK);	
			}else {
				return new ResponseEntity<>(new CustomerErrorType("No se puede guardar un nuevo lote de Documento Factura, el número de Timbrado esta en vigencia todavia.! ."), HttpStatus.CONFLICT);
			}
	}
	@RequestMapping(method=RequestMethod.POST, value="/loteFactura/actualizarSerieActual")
	public void actualizarSerieActual(@RequestBody LoteFactura entity){
		loteFacturaRepository.actualizarSeriaActual(entity.getSerieActual(), entity.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/loteAutoImpresor")
	public ResponseEntity<?> saveLoteAutoImpresor(@RequestBody AutoImpresor entity, OAuth2Authentication authentication){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		entity.getFuncionario().setId(usuario.getFuncionario().getId());
		
		return new ResponseEntity<>(autoImpresorRepository.save(entity), HttpStatus.OK);	
	}
	
//	@RequestMapping(method=RequestMethod.POST, value="/loteTicket/actualizarNroTicket")
//	public void actualizarNroTicket(@RequestBody LoteTicket entity){
//		loteTicketRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//
//	@RequestMapping(method=RequestMethod.POST, value="/loteNotaPedido/actualizarNroNotaPedido")
//	public void actualizarNroNotaPedido(@RequestBody LoteTicket entity){
//		loteNotaPedidoRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//	
//	@RequestMapping(method=RequestMethod.POST, value="/loteBoleta/actualizarNroBoleta")
//	public void actualizarNroBoleta(@RequestBody LoteBoleta entity){
//		loteBoletaRepository.actualizarNumeroActual(entity.getNumeroActual(), entity.getId());
//	}
//	
	@RequestMapping(method=RequestMethod.POST, value="/loteBoleta")
	public LoteBoleta saveLoteBoleta(@RequestBody LoteBoleta entity){
		return loteBoletaRepository.save(entity);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteBoleta")
	public ResponseEntity<?> getLoteBoleta(){
		LoteBoleta l=loteBoletaRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de boleta en el --Menu Configuracion Lote Documento!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteBoletaRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/loteTicket")
	public LoteTicket saveLoteTicket(@RequestBody LoteTicket entity){
		return loteTicketRepository.save(entity);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/loteTicket")
	public ResponseEntity<?> getLoteTicket(){
		LoteTicket l=loteTicketRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de ticket en el --Menu Configuracion Lote Documento!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteTicketRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}

	@RequestMapping(method=RequestMethod.GET, value="/loteNotaPedido")
	public ResponseEntity<?> getLoteNotaPedido(){
		LoteNotaPedido l=loteNotaPedidoRepository.findTop1ByOrderByIdAsc();
		if(l==null) {
			return new ResponseEntity<>(new CustomerErrorType("Debe agregar numero inicial de la nota pedido en el --Menu Configuracion Lote Nota Pedido!!"), HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<>(loteNotaPedidoRepository.findTop1ByOrderByIdAsc(), HttpStatus.OK);	

		}
		
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/obtenerLoteDocumento")
	public List<NroDocumento> getNroLoteDocumento(){
		List<NroDocumento> lista = new ArrayList<>();
		int tipo0 = 0;
		int tipo1 = 1;
		int tipo2 = 2;
		
		LoteBoleta loteBoleta = loteBoletaRepository.findTop1ByOrderByIdAsc();
		LoteTicket loteTicket = loteTicketRepository.findTop1ByOrderByIdAsc();
		LoteFactura loteFactura = loteFacturaRepository.findTop1ByOrderByIdAsc();
		
		for (int i = 0; i < 3; i++) {
			NroDocumento n = new NroDocumento();
			if (i == tipo0) {
				n.setDescripcion("1");
				
				String [] part= loteFactura.getSerieActual().split("-");
				String cod= part[2];
				String codActual=part[0]+"-"+part[1]+"-" + padF(Integer.parseInt(cod),7);
				n.setNro(codActual);
			}
			if (i == tipo1) {
				n.setDescripcion("2");
				n.setNro(padF(Integer.parseInt(loteBoleta.getNumeroActual()),12));
			}
			if (i == tipo2) {
				n.setDescripcion("3");
				n.setNro(padF(Integer.parseInt(loteTicket.getNumeroActual()),12));
			}
			
			lista.add(n);
		}
		
		return lista;
	}
	
	private static String padF(int numero, int size) {
		ft = new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}
}
