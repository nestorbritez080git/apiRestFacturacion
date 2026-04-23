package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.InformeCierreEmpaqueAuxiliar;
import com.bisontecfacturacion.security.auxiliar.ResumenEmpaqueDetalle;
import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.Anticipo;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DetalleServicios;
import com.bisontecfacturacion.security.model.Documento;
import com.bisontecfacturacion.security.model.EmpaqueCabecera;
import com.bisontecfacturacion.security.model.EmpaqueDetalle;
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.OperacionCajaCabecera;
import com.bisontecfacturacion.security.model.OrdenPagare;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.EmpaqueCabeceraRepository;
import com.bisontecfacturacion.security.repository.EmpaqueDetalleRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaCabeceraRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PresupuestoRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.VentaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

@RestController
@RequestMapping("empaque")
public class EmpaqueController {
	private static Formatter ft;
	private Reporte report;


	@Autowired
	private EmpaqueCabeceraRepository entityRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private AperturaCajaRepository aperturaRepository;

	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private OperacionCajaCabeceraRepository operacionCajaCabeceraRepository;

	@Autowired
	private VentaRepository ventaRepository;

	@Autowired
	private VentaController ventaServiceController;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private PresupuestoController presupuestoServiceController;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;
	@Autowired
	private EmpaqueDetalleRepository entityDetalleRepository;
	@Autowired
	private PresupuestoRepository presupuestoRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private OrgRepository orgRepository;


	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;




	private List<EmpaqueCabecera>listar(List<EmpaqueCabecera> obj){
		List<EmpaqueCabecera> listaRetorno=new ArrayList<>();
		for(EmpaqueCabecera p:obj){
			EmpaqueCabecera pre=new EmpaqueCabecera();
			pre.setId(p.getId());
			pre.setFechaRegistro(p.getFechaRegistro());
			pre.setFechaEntrega(p.getFechaEntrega());
			pre.setTotal(p.getTotal());
			pre.setTotalLetras(p.getTotalLetras());
			pre.setTotalDevolucion(p.getTotalDevolucion());
			pre.setFuncionarioEmpaque(p.getFuncionarioEmpaque());
			pre.setFuncionarioRegistro(p.getFuncionarioRegistro());
			pre.setZona(p.getZona());
			pre.setItemsPedido(p.getItemsPedido());
			pre.setEstado(p.getEstado());
			pre.setTotalFinalizado(p.getTotalFinalizado());
			pre.setItemsVenta(p.getItemsVenta());
			listaRetorno.add(pre);
		}
		return listaRetorno;
	}


	@RequestMapping(method=RequestMethod.GET, value="/tipo/{filtro}")
	public List<EmpaqueCabecera> getAlls(@PathVariable int filtro){
		List<EmpaqueCabecera> lisRetorno= new ArrayList<EmpaqueCabecera>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getEmpaqueAll());}
		if(filtro==2) { lisRetorno= listar(entityRepository.getEmpaqueAbierto());}
		if(filtro==3) { lisRetorno= listar(entityRepository.getEmpaqueCerrado());}

		return lisRetorno;

	}
	@RequestMapping(method=RequestMethod.POST, value="/tipo/{filtro}")
	public List<EmpaqueCabecera> getAllsPorDescripcion(@RequestBody String descripcion, @PathVariable int filtro){
		List<EmpaqueCabecera> lisRetorno= new ArrayList<EmpaqueCabecera>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getEmpaqueAllDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==2) { lisRetorno= listar(entityRepository.getEmpaqueAbiertoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==3) { lisRetorno= listar(entityRepository.getEmpaqueCerradoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		return lisRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value="/presupuestoIdZona/{id}")
	public List<Presupuesto> getPresupuestoPorZonaPreVenta( @PathVariable int id){
		return listarPresupuestoEmpaque(listarPresupuestoEmpaque(presupuestoRepository.getPresupuestoPreVentaPorZonas(id)));
	}
	private List<Presupuesto>listarPresupuestoEmpaque(List<Presupuesto> obj){
		List<Presupuesto> res=new ArrayList<>();
		for(Presupuesto ps:obj){
			Presupuesto pre=new Presupuesto();
			pre.setId(ps.getId());
			pre.getFuncionario().setId(ps.getFuncionario().getId());
			pre.getFuncionario().getPersona().setNombre(ps.getFuncionario().getPersona().getNombre()+" "+ps.getFuncionario().getPersona().getApellido());
			pre.getFuncionario().getPersona().setCedula(ps.getFuncionario().getPersona().getCedula());
			pre.getCliente().setId(ps.getCliente().getId());
			pre.getCliente().getPersona().setNombre(ps.getCliente().getPersona().getNombre()+" "+ ps.getCliente().getPersona().getApellido());
			pre.getCliente().getPersona().setCedula(ps.getCliente().getPersona().getCedula());
			pre.setTotal(ps.getTotal());
			pre.setFecha(ps.getFecha());
			pre.setHora(ps.getHora());
			pre.setEstado(ps.getEstado());
			pre.setZona(ps.getZona());
			pre.setDetallePresupuestoProducto(ps.getDetallePresupuestoProducto());
			pre.setDetallePresupuestoServicio(ps.getDetallePresupuestoServicio());
			res.add(pre);
		}
		return res;
	}
	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/actualizarEmpaqueDetalleEstado/{id}/{estado}")
	public ResponseEntity<?> actualizarEstadoEmpaqueDetalle(@PathVariable int id, @PathVariable String estado) {
		try {
			int rows = entityDetalleRepository.findByActualizarEstadoDetalleEmpaque(id, estado);

			if (rows > 0) {
				return new ResponseEntity<>(new CustomerErrorType("Empaque arreglo registrado"),HttpStatus.CREATED);

			} else {
				return new ResponseEntity<>(new CustomerErrorType("No se ha podido actualizar estado"),HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new CustomerErrorType("No se ha podido actualizar estado"),HttpStatus.CONFLICT);


		}
	}
	@Transactional
	public List<OperacionCaja> procesarOperacionCajaVentaPorEmpaque(Venta ent, List<OperacionCaja> listaOperacion) {

		List<OperacionCaja> resultado = new ArrayList<>();

		if (listaOperacion == null || listaOperacion.isEmpty()) {
			throw new RuntimeException("No existen operaciones de caja para procesar");
		}

		// 🔥 Crear cabecera si todavía no existe
		OperacionCajaCabecera cab = new OperacionCajaCabecera();
		cab.setFecha(new Date());
		cab.setMonto(listaOperacion.stream().mapToDouble(OperacionCaja::getMonto).sum());
		cab.setReferenciaOperacion(ent.getId());
		cab.getAperturaCaja().setId((listaOperacion.get(0).getAperturaCaja().getId()));
		Concepto c = conceptoRepository.findById(listaOperacion.get(0).getConcepto().getId()).orElseThrow(() -> new RuntimeException("Concepto no encontrado"));
		cab.setTipo("ENTRADA");
		cab.setMotivo(c.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
		cab.getConcepto().setId(c.getId());
		OperacionCajaCabecera savedCabecera = operacionCajaCabeceraRepository.save(cab);

		for (OperacionCaja ope : listaOperacion) {
			ope.setTipo("ENTRADA");
			ope.setMotivo(c.getDescripcion()+" POR EMPAQUE REF.: "+ent.getId());
			ope.setReferenciaOperacion(ent.getId());
			ope.setFecha(new Date());
			// 🔥 Asociar cabecera
			ope.getOperacionCajaCabecera().setId(savedCabecera.getId());
			// 🔥 Actualizar saldos según tipo operación
			if (ope.getTipoOperacion().getId() == 1) {
				aperturaCajaRepository.findByActualizarAperturaSaldo(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			if (ope.getTipoOperacion().getId() == 2) {
				aperturaCajaRepository.findByActualizarAperturaSaldoCheque(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			if (ope.getTipoOperacion().getId() == 3) {
				aperturaCajaRepository.findByActualizarAperturaSaldoTarjeta(ope.getAperturaCaja().getId(),ope.getMonto()
						);
			}
			OperacionCaja saved = operacionCajaRepository.save(ope);

			resultado.add(saved);
		}
		//actualiza el id de la operacion en referencia
		ventaRepository.findByActualizarVentaOperacion(ent.getId(), savedCabecera.getId()
				);
		return resultado;
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST, value = "/saveArregloFinalizacion/{idDetalleEmpaque}")
	public ResponseEntity<?> guardarArregloFinalizacion(
			@RequestPart("venta") Venta venta,
			@RequestPart("operacionCaja") List<OperacionCaja> operacionCajaLista,
			@RequestPart("cuentaCobrar") CuentaCobrarCabecera cuentaCobrarCabecera,
			@PathVariable int idDetalleEmpaque) {
		Venta savedRetotno = null;
		OrdenPagare orRetotno = null;
		CuentaCobrarCabecera cuRetotno= null;
		List<CuentaCobrarCabecera> listRetorno= new ArrayList<>();
		List<OperacionCaja> opRetotno = new ArrayList<>();
		System.out.println("tipo: "+venta.getTipo()+" estado: "+ venta.getEstado());
		// 1. Validar compra y detalles
		ResponseEntity<?> validacionVenta = ventaServiceController.validarVenta(venta);
		if (validacionVenta != null) return validacionVenta;

		if (("1".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("contado")) && "FACTURADO".equals(venta.getEstado())) {
			System.out.println("entroo sistema de verificacion venta contado: ");
			ResponseEntity<?> validacionCaja = ventaServiceController.validarCaja(operacionCajaLista);
			if (validacionCaja != null) return validacionCaja;
		} else if (("2".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("credito")) && "FACTURADO".equals(venta.getEstado())) {
			if (venta.getEntrega() > 0) {
				ResponseEntity<?> validacionCajaEntrega = ventaServiceController.validarCaja(operacionCajaLista);
				if (validacionCajaEntrega != null) return validacionCajaEntrega;
			}
			ResponseEntity<?> validacionCuenta = ventaServiceController.validarCuentaCobrar(cuentaCobrarCabecera);
			if (validacionCuenta != null) return validacionCuenta;

		}

		savedRetotno = ventaRepository.save(venta);

		if (("1".equals(savedRetotno.getTipo()) || savedRetotno.getTipo().toLowerCase().equals("contado")) && "FACTURADO".equals(venta.getEstado())) 
		{
			opRetotno =  procesarOperacionCajaVentaPorEmpaque(savedRetotno, operacionCajaLista);
		} else if (("2".equals(venta.getTipo()) || venta.getTipo().toLowerCase().equals("credito")) && "FACTURADO".equals(venta.getEstado())) 
		{
			if (venta.getEntrega() > 0) {
				opRetotno= procesarOperacionCajaVentaPorEmpaque(savedRetotno, operacionCajaLista);      	
			}
			cuRetotno = ventaServiceController.procesarCuentaCobrar(savedRetotno, cuentaCobrarCabecera);
			orRetotno = ventaServiceController.procesarOrdenPagared(cuRetotno);
			//listRetorno=  listadoCargarCuenta(cuentaCobrarRepository.findByCuentaPorIdClienteACobrarListasss(cuRetotno.getCliente().getId()));
		}
		//actualzia el estado del empaque detalle
		entityDetalleRepository.findByActualizarEstadoDetalleEmpaque(idDetalleEmpaque, "TERMINADO");
		return new ResponseEntity<>(savedRetotno, HttpStatus.CREATED);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody EmpaqueCabecera entity){
		try {
			System.out.println("empaque total: "+ entity.getTotal());
			if(entity.getFuncionarioRegistro().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
			} else if(entity.getFuncionarioEmpaque().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getZona().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA ZONA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getEmpaqueDetalle().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getObs() != null){
				entity.setObs(entity.getObs().toUpperCase());
			} else if(entity.getTotalLetras() == null || entity.getTotalLetras().trim().equals("")){
				entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(entity.getTotal()));
				System.out.println("nuep total letras: "+ entity.getTotalLetras());
			} else if(entity.getFechaRegistro() == null){
				entity.setFechaRegistro(LocalDateTime.now());
			} else if(entity.getFechaEntrega() == null) {//falta agregar eliminar ddettllae si se eliminaa
				entity.setFechaEntrega(new Date());
			} else{
				for(int ind=0; ind < entity.getEmpaqueDetalle().size(); ind++) {
					EmpaqueDetalle pro = entity.getEmpaqueDetalle().get(ind);
					pro.setItemsPedidoDetalle(pro.getPresupuesto().getDetallePresupuestoProducto().size()+ pro.getPresupuesto().getDetallePresupuestoServicio().size());

					if(pro.getPresupuesto().getId() == 0) {
						return new ResponseEntity<>(new CustomerErrorType("EL PRESUPUESTO NO SE LLEGÓ A CARGAR CORRECTAMENTE ITEM N°: "+(ind+1)+""), HttpStatus.CONFLICT);
					}else if(pro.getPresupuesto().getDetallePresupuestoProducto().size() <= 0 ){
						return new ResponseEntity<>(new CustomerErrorType("EL PRESUPUESTO DEBE TENER AL MENOS UN DETALLE ITEM N°: "+(ind+1)+""), HttpStatus.CONFLICT);
					}
				}
			}
			if(entity.getId() !=0) {
				System.out.println("entro edit lote empaque");
				if(entity.getEstado().equals("CERRADO")) {

				}
				int idpent=entity.getId();
				for(int i=0; i < entity.getEmpaqueDetalle().size(); i++) {
					EmpaqueDetalle detEmpaque = entity.getEmpaqueDetalle().get(i);
					detEmpaque.getEmpaqueCabecera().setId(idpent);
					for(int idetPresu=0; idetPresu < detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size(); idetPresu++) {
						DetallePresupuestoProducto detPresu = detEmpaque.getPresupuesto().getDetallePresupuestoProducto().get(idetPresu);
						System.out.println("DETALLE: "+detPresu.getDescripcion()+"CAN: "+detPresu.getCantidad());
					}
					detEmpaque.setSubtotalPresupuesto(detEmpaque.getPresupuesto().getTotal());
					detEmpaque.setItemsPedidoDetalle(detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size());
					entityDetalleRepository.save(detEmpaque);
				}
				entityRepository.save(entity);
				return new ResponseEntity<>(new CustomerErrorType("Empaque Actualizado"),HttpStatus.CREATED);
			}else {
				System.out.println("entro nuevo lote empaque");
				entityRepository.save(entity);
				EmpaqueCabecera idpent = entityRepository.findTop1ByOrderByIdDesc();
				for(int i=0; i < entity.getEmpaqueDetalle().size(); i++) {
					EmpaqueDetalle detEmpaque = entity.getEmpaqueDetalle().get(i);
					detEmpaque.getEmpaqueCabecera().setId(idpent.getId());
					for(int idetPresu=0; idetPresu < detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size(); idetPresu++) {
						DetallePresupuestoProducto detPresu = detEmpaque.getPresupuesto().getDetallePresupuestoProducto().get(idetPresu);
						System.out.println("DETALLE: "+detPresu.getDescripcion()+"CAN: "+detPresu.getCantidad());
					}
					detEmpaque.setSubtotalPresupuesto(detEmpaque.getPresupuesto().getTotal());
					detEmpaque.setItemsPedidoDetalle(detEmpaque.getPresupuesto().getDetallePresupuestoProducto().size());
					entityDetalleRepository.save(detEmpaque);
				}
				entityRepository.save(entity);
				return new ResponseEntity<>(new CustomerErrorType("Empaque Generado"),HttpStatus.CREATED);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}

	}
	/*
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/finalizarEmpaque/{numeroTerminal}")
	public ResponseEntity<?> finalizarEmpaqueRefactorizado(@RequestBody EmpaqueCabecera entity, @PathVariable int numeroTerminal){
	    try {
	        System.out.println("empaque total: " + entity.getTotal());

	        // 🔹 Validaciones básicas
	        if(entity.getFuncionarioRegistro().getId() == 0) {
	            return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
	        } 
	        if(entity.getFuncionarioEmpaque().getId() == 0) {
	            return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO EMPAQUE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	        }
	        if(entity.getZona().getId() == 0) {
	            return new ResponseEntity<>(new CustomerErrorType("LA ZONA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	        } 
	        if(entity.getEmpaqueDetalle() == null || entity.getEmpaqueDetalle().isEmpty()) {
	            return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
	        }

	        // 🔹 Ajustes de datos
	        if(entity.getObs() != null) entity.setObs(entity.getObs().toUpperCase());
	        if(entity.getTotalLetras() == null || entity.getTotalLetras().trim().isEmpty()) {
	            entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(entity.getTotal()));
	        }
	        if(entity.getFechaRegistro() == null) entity.setFechaRegistro(LocalDateTime.now());
	        if(entity.getFechaEntrega() == null) entity.setFechaEntrega(new Date());

	        // 🔹 Validar cada detalle del empaque
	        for(int ind=0; ind < entity.getEmpaqueDetalle().size(); ind++) {
	            EmpaqueDetalle pro = entity.getEmpaqueDetalle().get(ind);
	            pro.setItemsPedidoDetalle(
	                pro.getPresupuesto().getDetallePresupuestoProducto().size() + 
	                pro.getPresupuesto().getDetallePresupuestoServicio().size()
	            );

	            if(pro.getPresupuesto().getId() == 0) {
	                return new ResponseEntity<>(new CustomerErrorType(
	                    "EL PRESUPUESTO NO SE LLEGÓ A CARGAR CORRECTAMENTE ITEM N°: "+(ind+1)
	                ), HttpStatus.CONFLICT);
	            }
	            if(pro.getPresupuesto().getDetallePresupuestoProducto().isEmpty()) {
	                return new ResponseEntity<>(new CustomerErrorType(
	                    "EL PRESUPUESTO DEBE TENER AL MENOS UN DETALLE ITEM N°: "+(ind+1)
	                ), HttpStatus.CONFLICT);
	            }
	        }

	        List<EmpaqueDetalle> detallesParaGuardar = new ArrayList<>();
	        double totalGeneralEmpaque = 0.0;
	        int totalItemsEmpaque = 0;
	        boolean algunDetalleGenerado = false;

	        // 🔹 Procesar cada detalle del empaque
	        for(EmpaqueDetalle deta : entity.getEmpaqueDetalle()) {

	            Presupuesto p = deta.getPresupuesto();

	            if(deta.getEmpaqueCabecera() == null) deta.setEmpaqueCabecera(entity);
	            deta.getEmpaqueCabecera().setId(entity.getId());

	            Venta preVenta = new Venta();
	            preVenta.setCliente(p.getCliente());
	            preVenta.setFuncionarioR(entity.getFuncionarioEmpaque());
	            preVenta.setFuncionarioV(p.getFuncionario());
	            preVenta.setFuncionario(entity.getFuncionarioRegistro());
	            preVenta.setFecha(new Date());
	            Documento doc = new Documento();
	            doc.setId(3); // TICKET
	            doc.setDescripcion("false");
	            preVenta.setDocumento(doc);
	            preVenta.setZona(p.getZona());
	            preVenta.setEstado("PREVENTA");
	            preVenta.setTipo(deta.getCondicion());

	            List<DetalleProducto> dpRetorno = new ArrayList<>();
	            List<DetalleServicios> dsRetorno = new ArrayList<>();
	            double subTotalDetalleFacturado = 0.0;
	            int cantidadItemsDetalle = 0;

	            // Productos con stock
	            for(DetallePresupuestoProducto detPresupuesto : p.getDetallePresupuestoProducto()) {
	                double stockActual = productoRepository.getStockActual(detPresupuesto.getProducto().getId());
	                double cantidadAFacturar = Math.min(detPresupuesto.getCantidad(), stockActual);
	                if(cantidadAFacturar <= 0) continue;

	                DetalleProducto dp = new DetalleProducto();
	                dp.setVenta(preVenta);
	                dp.setProducto(detPresupuesto.getProducto());
	                dp.setCantidad(cantidadAFacturar);
	                dp.setDescripcion(detPresupuesto.getDescripcion());
	                dp.setCosto(detPresupuesto.getProducto().getPrecioCosto());
	                dp.setCostoPromedio(detPresupuesto.getProducto().getPrecioCosto());
	                dp.setSubTotal(cantidadAFacturar * detPresupuesto.getPrecio());
	                dp.setDescuento(detPresupuesto.getDescuento());
	                dp.setIsBalanza(detPresupuesto.getIsBalanza());
	                dp.setIva(detPresupuesto.getIva());
	                dp.setPrecio(detPresupuesto.getPrecio());
	                dp.setMontoIva(detPresupuesto.getMontoIva());
	                dp.setTipoPrecio(detPresupuesto.getTipoPrecio());

	                subTotalDetalleFacturado += dp.getSubTotal();
	                cantidadItemsDetalle++;
	                dpRetorno.add(dp);
	            }

	            // Servicios
	            for(DetallePresupuestoServicio ds : p.getDetallePresupuestoServicio()) {
	                if(ds.getCantidad() <= 0) continue;

	                DetalleServicios dps = new DetalleServicios();
	                dps.setServicio(ds.getServicio());
	                dps.setCantidad(ds.getCantidad());
	                dps.setPrecio(ds.getPrecio());
	                dps.setDescripcion(ds.getDescripcion());
	                dps.setIva(ds.getIva());
	                dps.setMontoIva(ds.getMontoIva());
	                dps.setSubTotal(ds.getSubTotal());
	                dps.setFuncionario(ds.getFuncionario());
	                dps.setObs(ds.getObs());

	                subTotalDetalleFacturado += ds.getCantidad() * ds.getPrecio();
	                cantidadItemsDetalle++;
	                dsRetorno.add(dps);
	            }

	            if(dpRetorno.isEmpty() && dsRetorno.isEmpty()) {
	                if(deta.getId() != 0 && entityDetalleRepository.existsById(deta.getId())) {
	                    entityDetalleRepository.deleteById(deta.getId());
	                }
	                continue; // saltar detalle sin stock
	            }

	            preVenta.setTotal(subTotalDetalleFacturado);
	            preVenta.setDetalleProducto(dpRetorno);
	            preVenta.setDetalleServicio(dsRetorno);
	            preVenta.setTotalLetra(NumerosALetras.convertirNumeroALetras(subTotalDetalleFacturado));

	            // Guardar venta
	            ResponseEntity<?> response = ventaServiceController.guardar(preVenta, numeroTerminal);
	            Object body = response.getBody();
	            if(body instanceof Venta) {
	                Venta vcv = (Venta) body;
	                if(vcv.getId() <= 0) {
	                    return new ResponseEntity<>(new CustomerErrorType(
	                        "ERROR AL GENERAR VENTA EN EMPAQUE DETALLE: " + deta.getId()
	                    ), HttpStatus.CONFLICT);
	                }

	                deta.setVentaReferencia(vcv.getId());
	                deta.setVenta(vcv);
	                deta.setSubtotalPresupuesto(subTotalDetalleFacturado);
	                deta.setItemsPedidoDetalle(cantidadItemsDetalle);

	                detallesParaGuardar.add(deta);

	                p.setEstado("CERRADO");
	                presupuestoServiceController.guardar(p);

	                totalGeneralEmpaque += subTotalDetalleFacturado;
	                totalItemsEmpaque++;
	                algunDetalleGenerado = true;

	            } else if(body instanceof CustomerErrorType) {
	                return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	            } else {
	                return new ResponseEntity<>(new CustomerErrorType(
	                    "RESPUESTA DESCONOCIDA AL GENERAR VENTA"
	                ), HttpStatus.INTERNAL_SERVER_ERROR);
	            }
	        }

	        if(!algunDetalleGenerado) {
	            return new ResponseEntity<>(new CustomerErrorType(
	                "NO SE PUDO FINALIZAR NINGUNA VENTA PARA ESTE EMPAQUE POR FALTA DE STOCK"
	            ), HttpStatus.CONFLICT);
	        }

	        // 🔹 Guardar todos los detalles juntos al final
	        entityDetalleRepository.saveAll(detallesParaGuardar);

	        // 🔹 Guardar cabecera
	        entity.setTotal(totalGeneralEmpaque);
	        entity.setItemsPedido(totalItemsEmpaque);
	        entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(totalGeneralEmpaque));
	        entity.setEstado("CERRADO");
	        entity = entityRepository.save(entity);

	        return new ResponseEntity<>(entity, HttpStatus.CREATED);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
	    }
	}
	 */


	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/finalizarEmpaque/{numeroTerminal}")
	public ResponseEntity<?> finalizarEmpaqueRefactorizado(@RequestBody EmpaqueCabecera entity, @PathVariable int numeroTerminal) {
		try {
			// Validaciones iniciales
			if(entity.getFuncionarioRegistro().getId() == 0) 
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO REGISTRO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			if(entity.getFuncionarioEmpaque().getId() == 0) 
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO EMPAQUE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			if(entity.getZona().getId() == 0) 
				return new ResponseEntity<>(new CustomerErrorType("LA ZONA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			if(entity.getEmpaqueDetalle().isEmpty()) 
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);

			// 🔹 Validar cada detalle del empaque
			for(int ind = 0; ind < entity.getEmpaqueDetalle().size(); ind++) {
				EmpaqueDetalle pro = entity.getEmpaqueDetalle().get(ind);

				System.out.println("PEDIDO ORDEN : "+(ind+1)+ " orden numero: "+pro.getPresupuesto().getTotal());
				pro.setItemsPedidoDetalle(
						pro.getPresupuesto().getDetallePresupuestoProducto().size() + 
						pro.getPresupuesto().getDetallePresupuestoServicio().size()
						);

				if(pro.getPresupuesto().getId() == 0) {
					return new ResponseEntity<>(new CustomerErrorType(
							"EL PRESUPUESTO NO SE LLEGÓ A CARGAR CORRECTAMENTE ITEM N°: " + (ind+1)
							), HttpStatus.CONFLICT);
				}
				if(pro.getPresupuesto().getDetallePresupuestoProducto().isEmpty()) {
					return new ResponseEntity<>(new CustomerErrorType(
							"EL PRESUPUESTO DEBE TENER AL MENOS UN DETALLE ITEM N°: " + (ind+1)
							), HttpStatus.CONFLICT);
				}
			}

			// Ajustes de campos
			if(entity.getObs() != null) entity.setObs(entity.getObs().toUpperCase());
			if(entity.getTotalLetras() == null || entity.getTotalLetras().trim().equals("")) 
				entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(entity.getTotal()));
			if(entity.getFechaRegistro() == null) entity.setFechaRegistro(LocalDateTime.now());
			if(entity.getFechaEntrega() == null) entity.setFechaEntrega(new Date());

			double totalGeneralEmpaque = 0.0;
			int totalItemsEmpaque = 0;
			boolean algunDetalleGenerado = false;

			// Procesar cada detalle de empaque
			for(EmpaqueDetalle deta : entity.getEmpaqueDetalle()) {
				Presupuesto p = deta.getPresupuesto();
				deta.getEmpaqueCabecera().setId(entity.getId());

				// Crear venta parcial solo si hay stock
				Venta preVenta = new Venta();
				preVenta.setCliente(p.getCliente());
				preVenta.setFuncionarioR(entity.getFuncionarioEmpaque());
				preVenta.setFuncionarioV(p.getFuncionario());
				preVenta.setFuncionario(entity.getFuncionarioRegistro());
				preVenta.setFecha(new Date());
				Documento doc = new Documento();
				doc.setId(3); // TICKET
				doc.setDescripcion("false");
				preVenta.setDocumento(doc);
				preVenta.setZona(p.getZona());
				preVenta.setEstado("PREVENTA");
				preVenta.setTipo(deta.getCondicion());

				List<DetalleProducto> dpRetorno = new ArrayList<>();
				List<DetalleServicios> dsRetorno = new ArrayList<>();
				double subTotalDetalleFacturados = 0.0;
				int cantidadItemsDetalles = 0;

				// Productos
				for(DetallePresupuestoProducto detPresupuesto : p.getDetallePresupuestoProducto()) {
					double stockActual = productoRepository.getStockActual(detPresupuesto.getProducto().getId());
					double cantidadAFacturar = Math.min(detPresupuesto.getCantidad(), stockActual);
					if(cantidadAFacturar <= 0) continue;

					DetalleProducto dp = new DetalleProducto();
					dp.setVenta(preVenta);
					dp.setProducto(detPresupuesto.getProducto());
					dp.setCantidad(cantidadAFacturar);
					dp.setDescripcion(detPresupuesto.getDescripcion());
					dp.setCosto(detPresupuesto.getProducto().getPrecioCosto());
					dp.setCostoPromedio(detPresupuesto.getProducto().getPrecioCosto());
					dp.setSubTotal((cantidadAFacturar * detPresupuesto.getPrecio())-(detPresupuesto.getDescuento()*cantidadAFacturar));
					dp.setDescuento(detPresupuesto.getDescuento());
					dp.setIsBalanza(detPresupuesto.getIsBalanza());
					dp.setIva(detPresupuesto.getIva());
					dp.setPrecio(detPresupuesto.getPrecio());
					dp.setMontoIva(detPresupuesto.getMontoIva());
					dp.setTipoPrecio(detPresupuesto.getTipoPrecio());
					subTotalDetalleFacturados += dp.getSubTotal();
					cantidadItemsDetalles++;
					dpRetorno.add(dp);
				}

				// Servicios
				for(DetallePresupuestoServicio ds : p.getDetallePresupuestoServicio()) {
					if(ds.getCantidad() <= 0) continue;

					DetalleServicios dps = new DetalleServicios();
					dps.setServicio(ds.getServicio());
					dps.setCantidad(ds.getCantidad());
					dps.setPrecio(ds.getPrecio());
					dps.setDescripcion(ds.getDescripcion());
					dps.setIva(ds.getIva());
					dps.setMontoIva(ds.getMontoIva());
					dps.setSubTotal(ds.getSubTotal());
					dps.setFuncionario(ds.getFuncionario());
					dps.setObs(ds.getObs());

					subTotalDetalleFacturados += ds.getCantidad() * ds.getPrecio();
					cantidadItemsDetalles++;
					dsRetorno.add(dps);
				}

				// Guardar venta solo si hay productos o servicios
				if(!dpRetorno.isEmpty() || !dsRetorno.isEmpty()) {
					preVenta.setTotal(subTotalDetalleFacturados);
					preVenta.setDetalleProducto(dpRetorno);
					preVenta.setDetalleServicio(dsRetorno);
					preVenta.setTotalLetra(NumerosALetras.convertirNumeroALetras(subTotalDetalleFacturados));

					ResponseEntity<?> response = ventaServiceController.guardar(preVenta, numeroTerminal);
					Object body = response.getBody();
					if(body instanceof Venta) {
						Venta vcv = (Venta) body;
						if(vcv.getId() <= 0) 
							return new ResponseEntity<>(new CustomerErrorType("ERROR AL GENERAR VENTA EN EMPAQUE DETALLE: " + deta.getId()), HttpStatus.CONFLICT);

						// Actualizar detalle con subtotal real y referencia a la venta
						deta.setVentaReferencia(vcv.getId());
						deta.setVenta(vcv);
						deta.setSubtotalVenta(subTotalDetalleFacturados);
						deta.setItemsVentaDetalle(cantidadItemsDetalles);
						entityDetalleRepository.save(deta);

						// Cerrar presupuesto
						//p.setEstado("CERRADO");
						presupuestoRepository.cambiarEstado(p.getId(),"CERRADO");

						totalGeneralEmpaque += subTotalDetalleFacturados;
						totalItemsEmpaque++;
						deta.setSubtotalVenta(subTotalDetalleFacturados);
						deta.setItemsVentaDetalle(totalItemsEmpaque);
						algunDetalleGenerado = true;
					} else if(body instanceof CustomerErrorType) {
						return new ResponseEntity<>(body, HttpStatus.CONFLICT);
					} else {
						return new ResponseEntity<>(new CustomerErrorType("RESPUESTA DESCONOCIDA AL GENERAR VENTA"), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					// No se generó venta, mantener subtotal en 0 y guardar detalle
					//deta.setSubtotalPresupuesto(0.0);
					//deta.setItemsPedidoDetalle(0);
					//entityDetalleRepository.save(deta);
				}
			}

			if(!algunDetalleGenerado) {
				return new ResponseEntity<>(new CustomerErrorType(
						"NO SE PUDO FINALIZAR NINGUNA VENTA PARA ESTE EMPAQUE POR FALTA DE STOCK"), HttpStatus.CONFLICT);
			}

			// Guardar cabecera con totales finales
			//entity.setTotal(totalGeneralEmpaque);
			//entity.setItemsPedido(totalItemsEmpaque);
			entity.setTotalFinalizado(totalGeneralEmpaque);
			entity.setItemsVenta(totalItemsEmpaque);
			entity.setTotalLetras(NumerosALetras.convertirNumeroALetras(totalGeneralEmpaque));
			entity.setEstado("CERRADO");
			entity = entityRepository.save(entity);

			return new ResponseEntity<>(entity, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("ERROR: "+e.getMessage()), HttpStatus.CONFLICT);
		}
	}

	private List<Anticipo>cargarListado(List<Object[]> lista){
		List<Anticipo> listaRetrono= new  ArrayList<Anticipo>();
		for (Object[] ob: lista) {
			Anticipo a = new Anticipo();
			a.setId(Integer.parseInt(ob[0].toString()));
			a.getFuncionarioRegistro().getPersona().setNombre(ob[1].toString() + " "+ob[2].toString());
			a.getFuncionarioAutorizado().getPersona().setNombre(ob[3].toString() + " "+ob[4].toString());
			a.getFuncionarioEncargado().getPersona().setNombre(ob[5].toString() + " "+ob[6].toString());
			a.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[7].toString()));
			a.setMonto(Double.parseDouble(ob[8].toString()));
			a.setEstado(Boolean.parseBoolean(ob[9].toString()));
			a.getFuncionarioRegistro().setId(Integer.parseInt(ob[10].toString()));
			a.getFuncionarioAutorizado().setId(Integer.parseInt(ob[11].toString()));
			a.getFuncionarioEncargado().setId(Integer.parseInt(ob[12].toString()));
			a.getTipoOperacion().setId(Integer.parseInt(ob[13].toString()));
			a.setTipo(ob[14].toString());
			a.setDisponibilidad(ob[15].toString());
			a.setMontoLiquidado(Double.parseDouble(ob[16].toString()));
			listaRetrono.add(a);
		}
		return listaRetrono;
	}

	@RequestMapping(method=RequestMethod.GET, value="/consultar/empaqueVenta/{id}")
	public EmpaqueCabecera getEmpaqueVenta(@PathVariable int id){
		EmpaqueCabecera v=entityRepository.getEmpaqueVenta(id);
		//EmpaqueCabecera pre=new EmpaqueCabecera();
		return v;
	}
	@RequestMapping(method=RequestMethod.GET, value="/consultar/empaquePresupuesto/{id}")
	public ResponseEntity<?> getEmpaquePresupuesto(@PathVariable int id){
		EmpaqueCabecera v=null;
		try {
			v=entityRepository.getEmpaquePresupuesto(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(v==null) {
			return new ResponseEntity<>(new CustomerErrorType("Registro no encotrado"), HttpStatus.OK); 
		}else {
			return new ResponseEntity<>(v, HttpStatus.OK); 
		}

	}

	@RequestMapping(method=RequestMethod.DELETE, value="/eliminar/{id}")
	public ResponseEntity<?> eliminarEmpaqueCabecera(@PathVariable int id){
		EmpaqueCabecera v=null;
		try {
			v=entityRepository.getEmpaquePresupuesto(id);
			if(v != null) {
				for (EmpaqueDetalle det: v.getEmpaqueDetalle()) {
					entityDetalleRepository.delete(det);
				}
				entityRepository.delete(v);
			}else {
				return new ResponseEntity<>(new CustomerErrorType("Registro no encotrado"), HttpStatus.OK); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(v, HttpStatus.OK); 

	}

	@RequestMapping(method=RequestMethod.POST, value="/eliminarEmpaqueDetalle")
	public ResponseEntity<?> eliminarDetalleProducto(@RequestBody List<EmpaqueDetalle> detalle){
		try {
			System.out.println(detalle.size()+ " lista size");
			if(detalle.size()!=-1) {
				System.out.println("con listado lista");
				for (EmpaqueDetalle de : detalle) {		
					System.out.println("entroo eliminar detalle for empaque");
					//this.actualizarProductoBasePresupuestoDescontar(de.getProducto().getId(), de.getCantidad());
					entityDetalleRepository.deleteById(de.getId());
				}
			}else {
				return new ResponseEntity<>(new CustomerErrorType("NO HAY LISTA PARA ELIMINAR"), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueCabecera ep= entityRepository.getEmpaqueVenta(id);
		List<Object[]> lisResumen = entityRepository.getResumenProductosPorEmpaque(id);
		List<ResumenEmpaqueDetalle> lisResumenRetorno =  new ArrayList<ResumenEmpaqueDetalle>();
		if(lisResumen.size()>0){
			for (int i = 0; i < lisResumen.size(); i++) {
				ResumenEmpaqueDetalle d = new ResumenEmpaqueDetalle();
				d.setIdProducto(Integer.parseInt(lisResumen.get(i)[0].toString()));
				d.setDescripcion(lisResumen.get(i)[1].toString());
				d.setMarca(lisResumen.get(i)[2].toString());
				d.setCantidad(Double.parseDouble(lisResumen.get(i)[3].toString()));
				lisResumenRetorno.add(d);
			}
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
				//Datos del empaque
				map.put("funcionarioEmpaque", "[ "+ep.getFuncionarioRegistro().getPersona().getCedula()+" ] "+ep.getFuncionarioRegistro().getPersona().getNombre()+ " "+ep.getFuncionarioRegistro().getPersona().getApellido());

				map.put("fechaEmpaque", ep.getFechaRegistro());
				map.put("zonaEmpaque", "[ "+ep.getZona().getId()+" ] "+ep.getZona().getDescripcion());
				map.put("itemsEmpaque", ep.getItemsPedido());
				map.put("totalEmpaque", ep.getTotal());
				map.put("obsEmpaque", ep.getObs()+"");
				map.put("letrasEmpaque", ep.getTotalLetras()+"");
				map.put("idEmpaque", ep.getId());
				report = new Reporte();
				report.reportPDFDescarga(lisResumenRetorno, map, "ReporteEmpaqueResumenPdf", response);
			} catch (Exception e) {
				e.printStackTrace();
				return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
			}
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET, value="/finalizar/{idEmpaque}/{idApertura}/{idTipoOperacion}/{numeroTerminal}")
	public ResponseEntity<?> finalizarEmpaque(
			@PathVariable int idEmpaque,
			@PathVariable int idApertura,
			@PathVariable int idTipoOperacion,
			@PathVariable int numeroTerminal) {

		try {
			EmpaqueCabecera emp = entityRepository.findById(idEmpaque).orElse(null);
			AperturaCaja apertura = aperturaCajaRepository.findById(idApertura).orElse(null);

			if (emp == null) {
				return new ResponseEntity<>(new CustomerErrorType(
						"NO SE ENCONTRÓ EMPAQUE CON EL NÚMERO: " + idEmpaque), HttpStatus.CONFLICT);
			}
			if (apertura == null) {
				return new ResponseEntity<>(new CustomerErrorType(
						"NO SE ENCONTRÓ APERTURA CAJA CON EL NÚMERO: " + idApertura), HttpStatus.CONFLICT);
			}
			List<EmpaqueDetalle> detalleEmpaque = new ArrayList<>(emp.getEmpaqueDetalle()); // Evitar ConcurrentModificationException
			double totalGeneralEmpaque = 0.0;
			int totalItemsEmpaque = 0;
			boolean algunaVentaGenerada = false;

			Iterator<EmpaqueDetalle> iteratorDetalle = detalleEmpaque.iterator();
			while (iteratorDetalle.hasNext()) {
				EmpaqueDetalle det = iteratorDetalle.next();
				Presupuesto p = det.getPresupuesto();

				Venta preVenta = new Venta();
				preVenta.setCliente(p.getCliente());
				preVenta.setFuncionarioR(emp.getFuncionarioEmpaque());
				preVenta.setFuncionarioV(p.getFuncionario());
				preVenta.setFuncionario(emp.getFuncionarioRegistro());
				preVenta.setFecha(new Date());
				Documento doc = new Documento();
				doc.setId(3); // TICKET
				doc.setDescripcion("false");
				preVenta.setDocumento(doc); // TICKET DOCUMENTO
				preVenta.setZona(p.getZona());
				preVenta.setEstado("FACTURADO");
				preVenta.setTipo("CONTADO");
				//.getDocumento().setDescripcion("false");

				List<DetalleProducto> dpRetorno = new ArrayList<>();
				List<DetalleServicios> dsRetorno = new ArrayList<>();
				double subTotalDetalleFacturado = 0.0;
				int cantidadItemsDetalle = 0;

				// Productos con stock
				for (DetallePresupuestoProducto detPresupuesto : p.getDetallePresupuestoProducto()) {
					double stockActual = productoRepository.getStockActual(detPresupuesto.getProducto().getId());
					double cantidadAFacturar = Math.min(detPresupuesto.getCantidad(), stockActual);

					if (cantidadAFacturar <= 0) continue;

					DetalleProducto dp = new DetalleProducto();
					dp.setVenta(preVenta);
					dp.setProducto(detPresupuesto.getProducto());
					dp.setCantidad(cantidadAFacturar);
					dp.setDescripcion(detPresupuesto.getDescripcion());
					dp.setCosto(detPresupuesto.getProducto().getPrecioCosto());
					dp.setCostoPromedio(detPresupuesto.getProducto().getPrecioCosto());
					dp.setSubTotal(cantidadAFacturar * detPresupuesto.getPrecio());
					dp.setDescuento(detPresupuesto.getDescuento());
					dp.setIsBalanza(detPresupuesto.getIsBalanza());
					dp.setIva(detPresupuesto.getIva());
					dp.setPrecio(detPresupuesto.getPrecio());
					dp.setMontoIva(detPresupuesto.getMontoIva());
					dp.setTipoPrecio(detPresupuesto.getTipoPrecio());

					subTotalDetalleFacturado += dp.getSubTotal();
					cantidadItemsDetalle++;
					dpRetorno.add(dp);
				}

				// Servicios
				for (DetallePresupuestoServicio ds : p.getDetallePresupuestoServicio()) {
					if (ds.getCantidad() <= 0) continue; // Solo considerar servicios con cantidad > 0

					DetalleServicios dps = new DetalleServicios();
					dps.setServicio(ds.getServicio());
					dps.setCantidad(ds.getCantidad());
					dps.setPrecio(ds.getPrecio());
					dps.setDescripcion(ds.getDescripcion());
					dps.setIva(ds.getIva());
					dps.setMontoIva(ds.getMontoIva());
					dps.setSubTotal(ds.getSubTotal());
					dps.setFuncionario(ds.getFuncionario());
					dps.setObs(ds.getObs());

					subTotalDetalleFacturado += ds.getCantidad() * ds.getPrecio();
					cantidadItemsDetalle++;
					dsRetorno.add(dps);
				}

				// Si no hay productos ni servicios con stock, eliminar detalle de empaque
				if (dpRetorno.isEmpty() && dsRetorno.isEmpty()) {
					iteratorDetalle.remove();
					continue;
				}

				preVenta.setTotal(subTotalDetalleFacturado);
				preVenta.setDetalleProducto(dpRetorno);
				preVenta.setDetalleServicio(dsRetorno);
				preVenta.setTotalLetra(NumerosALetras.convertirNumeroALetras(subTotalDetalleFacturado));
				//System.out.println("nuep total letras: "+ entity.getTotalLetra());

				// Guardar venta
				ResponseEntity<?> response = ventaServiceController.guardar(preVenta, numeroTerminal);
				Object body = response.getBody();
				Venta vcv = null;
				if (body instanceof Venta) {
					vcv= (Venta) body;
					if (vcv.getId() <= 0) {
						return new ResponseEntity<>(new CustomerErrorType(
								"HUBO UN ERROR AL GENERAR VENTA POR EL EMPAQUE DETALLE NÚMERO: " + det.getId()),
								HttpStatus.CONFLICT);
					}
					det.setVentaReferencia(vcv.getId());
					if (det.getVenta() == null) det.setVenta(new Venta());
					det.setVenta(vcv);
					System.out.println("numero documento generado:  "+vcv.getNroDocumento());
					det.setSubtotalPresupuesto(subTotalDetalleFacturado);
					det.setItemsPedidoDetalle(cantidadItemsDetalle);
					entityDetalleRepository.save(det);

					// Actualizar presupuesto
					p.setEstado("CERRADO");
					presupuestoServiceController.guardar(p);

					// Registrar operación caja
					OperacionCaja op = new OperacionCaja();
					op.getAperturaCaja().setId(idApertura);
					op.getConcepto().setId(1);
					op.getTipoOperacion().setId(idTipoOperacion);
					op.setFecha(new Date());
					op.setMonto(subTotalDetalleFacturado);
					op.setEfectivo(subTotalDetalleFacturado);
					Concepto c = conceptoRepository.getOne(1);
					op.setMotivo(c.getDescripcion() + " Ref.: " + det.getVentaReferencia() + ", POR EMPAQUE NÚMERO: " + emp.getId());
					op.setTipo("ENTRADA");

					if (idTipoOperacion == 1) aperturaRepository.findByActualizarAperturaSaldo(op.getAperturaCaja().getId(), op.getMonto());
					if (idTipoOperacion == 2) aperturaRepository.findByActualizarAperturaSaldoCheque(op.getAperturaCaja().getId(), op.getMonto());
					if (idTipoOperacion == 3) aperturaRepository.findByActualizarAperturaSaldoTarjeta(op.getAperturaCaja().getId(), op.getMonto());

					OperacionCaja opRetorno = operacionCajaRepository.save(op);
					ventaRepository.findByActualizarVentaOperacion(det.getVentaReferencia(), opRetorno.getId());

					totalGeneralEmpaque += subTotalDetalleFacturado;
					totalItemsEmpaque = totalItemsEmpaque + 1 ;
					algunaVentaGenerada = true; // al menos una venta se generó

					// continuar con la lógica normal
				} else if (body instanceof CustomerErrorType) {
					// reenvía el error recibido
					return new ResponseEntity<>(body, HttpStatus.CONFLICT);
				} else {
					// error inesperado
					return new ResponseEntity<>(new CustomerErrorType(
							"TIPO DE RESPUESTA DESCONOCIDO AL GENERAR VENTA"),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}

			}
			// Al finalizar el recorrido de todos los detalles
			if (!algunaVentaGenerada) {
				return new ResponseEntity<>(new CustomerErrorType(
						"NO SE PUDO GENERAR NINGUNA VENTA PARA ESTE EMPAQUE, POR LO TANTO NO SE FINALIZA"),
						HttpStatus.CONFLICT);
			}
			// Actualizar cabecera con totales generales
			emp.setTotal(totalGeneralEmpaque);
			emp.setItemsPedido(totalItemsEmpaque);
			emp.setTotalLetras(NumerosALetras.convertirNumeroALetras(totalGeneralEmpaque));
			emp.setEstado("CERRADO");
			entityRepository.save(emp);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(new CustomerErrorType("Error al finalizar empaque: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value="/descargarResumenDetalladoPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarDetalleEmpaquePdf(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueCabecera ep= entityRepository.getEmpaqueVenta(id);
		ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
		if(ep!=null) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());

				report = new Reporte();
				report.reportPDFDescarga(ep.getEmpaqueDetalle(), map, "ReporteEmpaqueDetalleResumenPdf", response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);

		}


		return  new  ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value="/descargarPdfEmpaqueUnitario/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarPdfEmpaqueUnitario(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		EmpaqueDetalle ep= entityDetalleRepository.getOne(id);
		ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
		if(ep!=null) {
			try {
				Map<String, Object> map = new HashMap<>();
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("org", ""+org.getNombre());
				map.put("direccion", ""+org.getDireccion());
				map.put("ruc", ""+org.getRuc());
				map.put("telefono", ""+org.getTelefono());
				map.put("ciudad", ""+org.getCiudad());
				map.put("pais", ""+org.getPais());
				map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());

				report = new Reporte();
				report.reportPDFDescarga(Arrays.asList(ep), map, "ReporteEmpaqueDetalleResumenPdf", response);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);

		}


		return  new  ResponseEntity<String>(HttpStatus.OK);
	}


	@RequestMapping(value="/descargarPdfCierreEmpaque/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  descargarCierreEmpaque(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
		try {

			List<Object[]> rows = entityRepository.getReporteResumenEmpaqueRaw(id);
			if (rows == null || rows.isEmpty()) {
				return  new ResponseEntity<>(new CustomerErrorType("No se encontró información para el empaque " + id), HttpStatus.CONFLICT);

			}

			Object[] r = rows.get(0); // 👈 LA FILA REAL
			InformeCierreEmpaqueAuxiliar dto = new InformeCierreEmpaqueAuxiliar();

			dto.setIdEmpaque(((Number) r[0]).intValue());
			dto.setFechaEntrega((Date) r[1]);
			dto.setZona((String) r[3]);

			dto.setFuncionarioEncargado((String) r[4]);
			dto.setFuncionarioRepartidor((String) r[5]);

			dto.setItemPedido(((Number) r[6]).intValue());
			dto.setItemVenta(((Number) r[7]).intValue());

			dto.setVentaContado(((Number) r[8]).doubleValue());
			dto.setVentaCredito(((Number) r[9]).doubleValue());
			dto.setTotalDevolucion(((Number) r[10]).doubleValue());

			dto.setTotalEfectivo(((Number) r[11]).doubleValue());
			dto.setTotalCheque(((Number) r[12]).doubleValue());
			dto.setTotalTarjeta(((Number) r[13]).doubleValue());

			dto.setTotalPedido(((Number) r[14]).doubleValue());
			dto.setTotalVenta(((Number) r[15]).doubleValue());

			Map<String, Object> map = new HashMap<>();
			map.put("tituloReporte", f.getTitulo());
			map.put("razonSocialReporte", f.getRazonSocial());
			map.put("descripcionMovimiento", f.getDescripcion());
			map.put("direccionReporte", f.getDireccion());
			map.put("telefonoReporte", f.getTelefono());
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());

			report = new Reporte();
			report.reportPDFDescarga(Arrays.asList(dto), map, "ReporteCierreEmpaque", response);

		} catch (Exception e) {
			e.printStackTrace();
		}



		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
}
