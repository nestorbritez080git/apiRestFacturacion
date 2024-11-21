package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.FechaUtil;
import com.bisontecfacturacion.security.model.AperturaCaja;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.CuentaCobrarCabecera;
import com.bisontecfacturacion.security.model.CuentaCobrarDetalle;
import com.bisontecfacturacion.security.model.DetalleProducto;
import com.bisontecfacturacion.security.model.DevolucionVenta;
import com.bisontecfacturacion.security.model.DevolucionVentaDetalle;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.InteresCuota;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.NotaCredito;
import com.bisontecfacturacion.security.model.OperacionCaja;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.TipoPlazo;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AperturaCajaRepository;
import com.bisontecfacturacion.security.repository.CierreCajaRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarDetalleRepository;
import com.bisontecfacturacion.security.repository.CuentaAcobrarRepository;
import com.bisontecfacturacion.security.repository.DevolucionVentaDetalleRepository;
import com.bisontecfacturacion.security.repository.DevolucionVentaRepository;
import com.bisontecfacturacion.security.repository.InteresCuotaRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.NotaCreditoRepository;
import com.bisontecfacturacion.security.repository.OperacionCajaRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.TesoreriaRepository;
import com.bisontecfacturacion.security.repository.TipoPlazoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

import sun.util.logging.resources.logging;


@Transactional
@RestController
@RequestMapping("devolucionVenta")
public class DevoluconVentaController {
	@Autowired
	private DevolucionVentaRepository entityRepository;

	@Autowired
	private OperacionCajaRepository operacionCajaRepository;

	@Autowired
	private ConceptoRepository conceptoRepository;


	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;

	@Autowired
	private CierreCajaRepository cierreCajaRepository;


	@Autowired
	private CuentaAcobrarRepository cuentaCobrarRepository;


	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private CuentaAcobrarDetalleRepository cuentaCobrarDetalleRepository;

	@Autowired
	private NotaCreditoRepository notaCreditoRepository;

	@Autowired
	private DevolucionVentaDetalleRepository detalleRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private AperturaCajaRepository aperturaCajaRepository;

	@Autowired
	private InteresCuotaRepository interesCuotaRepository;

	@Autowired
	private TipoPlazoRepository tipoPlazoRepository;


	@Autowired
	private TesoreriaRepository tesoreriaRepository;


	private SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");


	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public DevolucionVenta getDevolucionId(@PathVariable int id){
		return cargarEdicionId(entityRepository.getOne(id));
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleDevolucion/{id}")
	public List<DevolucionVentaDetalle> getDevolucionDetallePoCabeceraId(@PathVariable int id){
		List<Object[]> objeto=detalleRepository.getDetalleDevolucionPorIdCabecera(id);
		List<DevolucionVentaDetalle> listaRetorno= new ArrayList<DevolucionVentaDetalle>();
		for(Object[] ob: objeto) {
			DevolucionVentaDetalle det= new DevolucionVentaDetalle();
			det.setId(Integer.parseInt(ob[0].toString()));
			det.setDescripcion(ob[1].toString());
			det.getDetalleProducto().getProducto().getUnidadMedida().setDescripcion(ob[2].toString());
			det.getDetalleProducto().setCantidad(Double.parseDouble(ob[3].toString()));
			det.getDetalleProducto().setPrecio(Double.parseDouble(ob[4].toString()));
			det.getDetalleProducto().setIva(ob[5].toString());
			det.getDetalleProducto().setMontoIva(Double.parseDouble(ob[6].toString()));
			det.getDetalleProducto().setSubTotal(Double.parseDouble(ob[7].toString()));
			det.setCantidad(Double.parseDouble(ob[8].toString()));
			det.setSubTotal(Double.parseDouble(ob[9].toString()));
			listaRetorno.add(det);
		}
		return listaRetorno;
	}

	@RequestMapping(method=RequestMethod.GET, value="/{tipo}/{fecha}")
	public List<DevolucionVenta> getDevolucion(@PathVariable int tipo, @PathVariable String fecha){
		List<DevolucionVenta> listado = new ArrayList<DevolucionVenta>();
		if (tipo == 1) {
			listado = cargarLista();
		}
		if (tipo == 2) {
			String[] fec=fecha.split("-");
			Integer dia=Integer.parseInt(fec[0]);
			Integer mes=Integer.parseInt(fec[1]);
			Integer ano=Integer.parseInt(fec[2]);
			listado = cargarObjetos(entityRepository.getVentaFecha(ano, mes, dia));
		}

		if (tipo == 3) {
			listado = cargarObjetos(entityRepository.getVentaFiltro("%" + fecha + "%"));
		}

		return listado;
	}


	public DevolucionVenta cargarEdicionId(DevolucionVenta objeto) {
		DevolucionVenta dev = new DevolucionVenta();
		dev.getVenta().setId(objeto.getVenta().getId());
		dev.getVenta().getCliente().getPersona().setNombre(objeto.getVenta().getCliente().getPersona().getNombre() + " "+objeto.getVenta().getCliente().getPersona().getApellido());
		dev.getVenta().setTotal(objeto.getVenta().getTotal());
		dev.getVenta().setTipo(objeto.getVenta().getTipo());
		dev.getVenta().setFecha(objeto.getVenta().getFecha());
		dev.getVenta().setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setId(objeto.getId());
		dev.setFecha(objeto.getFecha());
		dev.setTotal(objeto.getTotal());
		dev.getTipoDevolucion().setDescripcion(objeto.getTipoDevolucion().getDescripcion());
		dev.getTipoDevolucion().setId(objeto.getId());
		dev.setFechaFactura(objeto.getVenta().getFechaFactura());
		dev.setNumeroOperacion(objeto.getVenta().getOperacionCaja());

		return dev;
	}

	public List<DevolucionVenta> cargarObjetos(List<Object[]> objeto) {
		List<DevolucionVenta> lista = new ArrayList<DevolucionVenta>();
		for(Object[] o: objeto) {
			DevolucionVenta dev = new DevolucionVenta();
			dev.setId(Integer.parseInt(o[0].toString()));
			dev.getFuncionario().getPersona().setNombre(o[1].toString());
			dev.getVenta().getCliente().getPersona().setNombre(o[2].toString());
			if (o[3].toString() == null) { dev.getVenta().getCliente().getPersona().setCedula(""); } else { dev.getVenta().getCliente().getPersona().setCedula(o[3].toString()); }
			dev.setTotal(Double.parseDouble(o[4].toString()));
			dev.getTipoDevolucion().setDescripcion(o[5].toString());
			dev.setHora(o[6].toString());
			dev.setEstado(o[7].toString());
			dev.getTipoDevolucion().setId(Integer.parseInt(o[8].toString()));
			lista.add(dev);
		}
		return lista;
	}
	public List<DevolucionVenta> cargarLista() {
		List<DevolucionVenta> lista = new ArrayList<DevolucionVenta>();
		List<DevolucionVenta> l = entityRepository.findTop50ByOrderByIdDesc();

		for(DevolucionVenta d: l) {
			DevolucionVenta devol = new DevolucionVenta();
			devol.setId(d.getId());
			devol.getFuncionario().getPersona().setNombre(d.getFuncionario().getPersona().getNombre() + " " + d.getFuncionario().getPersona().getApellido());
			devol.getVenta().getCliente().getPersona().setNombre(d.getVenta().getCliente().getPersona().getNombre() + " " + d.getVenta().getCliente().getPersona().getApellido());
			devol.getVenta().getCliente().getPersona().setCedula(d.getVenta().getCliente().getPersona().getCedula());
			devol.getVenta().setTotal(d.getVenta().getTotal());
			devol.setTotal(d.getTotal());
			devol.getVenta().setId(d.getVenta().getId());
			devol.setFecha(d.getFecha());
			devol.getVenta().setFechaFactura(d.getVenta().getFechaFactura());
			devol.getTipoDevolucion().setDescripcion(d.getTipoDevolucion().getDescripcion());
			devol.getTipoDevolucion().setId(d.getTipoDevolucion().getId());
			devol.setEstado(d.getEstado());
			lista.add(devol);
		}

		return lista;
	}

	@RequestMapping(method=RequestMethod.GET, value = "/ult")
	public int getUltimaDevolucion(){
		return entityRepository.getUltimaDevolucion();
	}

	private List<DevolucionVentaDetalle> listarDetalleDevol(List<Object[]> objeto){
		List<DevolucionVentaDetalle> listaRetorno= new ArrayList<DevolucionVentaDetalle>();
		for(Object[] ob: objeto) {
			DevolucionVentaDetalle det= new DevolucionVentaDetalle();
			det.setId(Integer.parseInt(ob[0].toString()));
			det.setDescripcion(ob[1].toString());
			det.getDetalleProducto().getProducto().getUnidadMedida().setDescripcion(ob[2].toString());
			det.getDetalleProducto().setCantidad(Double.parseDouble(ob[3].toString()));
			det.getDetalleProducto().setPrecio(Double.parseDouble(ob[4].toString()));
			det.getDetalleProducto().setIva(ob[5].toString());
			det.getDetalleProducto().setMontoIva(Double.parseDouble(ob[6].toString()));
			det.getDetalleProducto().setSubTotal(Double.parseDouble(ob[7].toString()));
			det.setCantidad(Double.parseDouble(ob[8].toString()));
			det.setSubTotal(Double.parseDouble(ob[9].toString()));
			det.getDetalleProducto().setId(Integer.parseInt(ob[10].toString()));
			det.getDetalleProducto().getProducto().setId(Integer.parseInt(ob[11].toString()));
			listaRetorno.add(det);
		}
		return listaRetorno;

	}
	@Transactional
	@RequestMapping(method=RequestMethod.GET, value = "/confirmarDevolucion/{id}/{idTipoOperacion}")
	public ResponseEntity<?> confirmarDevolucion (@PathVariable int id, OAuth2Authentication authentication, @PathVariable int idTipoOperacion){
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		entityRepository.confirmarDevolucion(id);
		DevolucionVenta v= entityRepository.getOne(id);
		OperacionCaja operacionAnterior =null;
		if( v.getNumeroOperacion()!=0) {
			operacionAnterior = operacionCajaRepository.getOne(v.getNumeroOperacion());

		}else {
			
		}
		NotaCredito notaCredtio = new NotaCredito();
		notaCredtio.setFecha(new Date());
		notaCredtio.setHora(hora());
		notaCredtio.setTotal(v.getTotal());
		notaCredtio.setTotalLetra(v.getTotalLetra());
		notaCredtio.getCliente().setId(v.getVenta().getCliente().getId());
		notaCredtio.getFuncionario().setId(usuario.getFuncionario().getId());
		notaCredtio.getDevolucionVenta().setId(id);
		notaCreditoRepository.save(notaCredtio);

		System.out.println("tIPO OPERACION: "+idTipoOperacion);


		if(v.getVenta().getTipo().equals("1")) {
			System.out.println("DEVOLUCION TIPO VENTA CONTADO");
			if(idTipoOperacion==1) {
				System.out.println("CON REENVOLSO");
				if(operacionAnterior!=null) {					
					OperacionCaja ope= new OperacionCaja();
					ope.setEfectivo(0.0);
					ope.setFecha(new Date());
					ope.setMonto(v.getTotal());
					ope.setReferenciaTipoOperacion("");
					ope.setVuelto(0.0);
					ope.setTipo("SALIDA");
					Concepto c= new Concepto();
					c = conceptoRepository.getOne(6);
					ope.setMotivo(c.getDescripcion()+" REF.: "+v.getId());
					ope.getTipoOperacion().setId(operacionAnterior.getTipoOperacion().getId());
					ope.getAperturaCaja().setId(operacionAnterior.getAperturaCaja().getId());
					ope.getConcepto().setId(6);
					operacionCajaRepository.save(ope);



					if(operacionAnterior.getTipoOperacion().getId()==1) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVenta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					if(operacionAnterior.getTipoOperacion().getId()==2) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaCheque(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					if(operacionAnterior.getTipoOperacion().getId()==3) {aperturaCajaRepository.findByActualizarAperturaSaldoActualAnulacionVentaTarjeta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());}
					AperturaCaja aper=aperturaCajaRepository.findById(operacionAnterior.getAperturaCaja().getId()).get();
					if (aper.isEstado() == false) {
						int cierreId = cierreCajaRepository.IdCierreCaja(operacionAnterior.getAperturaCaja().getId());
						if(operacionAnterior.getTipoOperacion().getId()==1) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVenta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVenta(cierreId, v.getTotal());
						}
						if(operacionAnterior.getTipoOperacion().getId()==2) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaCheque(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaCheque(cierreId, v.getTotal());
						}
						if(operacionAnterior.getTipoOperacion().getId()==3) {
							cierreCajaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(operacionAnterior.getAperturaCaja().getId(), v.getTotal());
							tesoreriaRepository.findByActualizarCierreMontoAnulacionVentaTarjeta(cierreId, v.getTotal());
						}
					}
					List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
					for (DevolucionVentaDetalle list : lista) {
						System.out.println("entrooo for para aumentar: ");
						this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
						System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
						Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
						MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
						System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

						mov.setDescripcion(list.getDescripcion());
						mov.setCantidad(list.getCantidad());
						mov.setFecha(new  Date());
						mov.setHora(hora());

						mov.setIngreso(list.getSubTotal());
						mov.setEgreso(0.0);
						mov.setVentaSalida(list.getPrecio());

						mov.setCostoEntrada(0.0);
						mov.setCostoEntradaAnterior(0.0);
						mov.setCostoSalida(p.getPrecioCosto());

						mov.setVenta_1(p.getPrecioVenta_1());
						mov.setVenta_2(p.getPrecioVenta_2());
						mov.setVenta_3(p.getPrecioVenta_3());
						mov.setVenta_4(p.getPrecioVenta_4());

						mov.setVenta_1_anterior(0.0);
						mov.setVenta_2_anterior(0.0);
						mov.setVenta_3_anterior(0.0);
						mov.setVenta_4_anterior(0.0);

						mov.getTipoMovimiento().setId(1);
						mov.getProducto().setId(p.getId());
						mov.getFuncionario().setId(v.getFuncionario().getId());
						mov.setMarca(p.getMarca().getDescripcion());
						Concepto ccc= new Concepto();
						ccc= conceptoRepository.findById(6).get();
						mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
						movEntradaSalidaRepository.save(mov);
					}
				}
			}else if(idTipoOperacion==2) {
				System.out.println("SIN REENVOLSO");
				List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
				for (DevolucionVentaDetalle list : lista) {
					System.out.println("entrooo for para aumentar: ");
					this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
					System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
					Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

					mov.setDescripcion(list.getDescripcion());
					mov.setCantidad(list.getCantidad());
					mov.setFecha(new  Date());
					mov.setHora(hora());

					mov.setIngreso(list.getSubTotal());
					mov.setEgreso(0.0);
					mov.setVentaSalida(list.getPrecio());

					mov.setCostoEntrada(0.0);
					mov.setCostoEntradaAnterior(0.0);
					mov.setCostoSalida(p.getPrecioCosto());

					mov.setVenta_1(p.getPrecioVenta_1());
					mov.setVenta_2(p.getPrecioVenta_2());
					mov.setVenta_3(p.getPrecioVenta_3());
					mov.setVenta_4(p.getPrecioVenta_4());

					mov.setVenta_1_anterior(0.0);
					mov.setVenta_2_anterior(0.0);
					mov.setVenta_3_anterior(0.0);
					mov.setVenta_4_anterior(0.0);

					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(v.getFuncionario().getId());
					mov.setMarca(p.getMarca().getDescripcion());
					Concepto ccc= new Concepto();
					ccc= conceptoRepository.findById(6).get();
					mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
					movEntradaSalidaRepository.save(mov);
				}

			}

		}else if (v.getVenta().getTipo().equals("2")) {
			System.out.println("DEVOLUCION TIPO VENTA CREDITO");
			if(idTipoOperacion==1) {
				return new ResponseEntity<>(new CustomerErrorType("DEVOLUCIÓN TIPO CREDITO CON REENVOLSO NO ES SE APLICA  !"), HttpStatus.CONFLICT);
			}else if (idTipoOperacion==2){
				System.out.println("SIN REENVOLSO");

				List<DevolucionVentaDetalle> lista= listarDetalleDevol(detalleRepository.getDetalleDevolucionPorIdCabecera(v.getId()));
				for (DevolucionVentaDetalle list : lista) {
					System.out.println("entrooo for para aumentar venta tipo credito: ");
					this.actualizarProductoBaseA(list.getDetalleProducto().getProducto().getId(), list.getCantidad());
					System.out.println("ID: "+list.getDetalleProducto().getProducto().getId() + " ");
					Producto p = productoRepository.getOne(list.getDetalleProducto().getProducto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());

					mov.setDescripcion(list.getDescripcion());
					mov.setCantidad(list.getCantidad());
					mov.setFecha(new  Date());
					mov.setHora(hora());

					mov.setIngreso(list.getSubTotal());
					mov.setEgreso(0.0);
					mov.setVentaSalida(list.getPrecio());

					mov.setCostoEntrada(0.0);
					mov.setCostoEntradaAnterior(0.0);
					mov.setCostoSalida(p.getPrecioCosto());

					mov.setVenta_1(p.getPrecioVenta_1());
					mov.setVenta_2(p.getPrecioVenta_2());
					mov.setVenta_3(p.getPrecioVenta_3());
					mov.setVenta_4(p.getPrecioVenta_4());

					mov.setVenta_1_anterior(0.0);
					mov.setVenta_2_anterior(0.0);
					mov.setVenta_3_anterior(0.0);
					mov.setVenta_4_anterior(0.0);

					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(v.getFuncionario().getId());
					mov.setMarca(p.getMarca().getDescripcion());
					Concepto ccc= new Concepto();
					ccc= conceptoRepository.findById(6).get();
					mov.setReferencia(ccc.getDescripcion()+" REF.: "+ v.getId());
					movEntradaSalidaRepository.save(mov);

				}


				CuentaCobrarCabecera cu= cuentaCobrarRepository.getCuentaCabeceraPorVentaId(v.getVenta().getId());
				if(cu!=null) {
					System.out.println("ENTRO TIENE CUENTA CONSULTADO POR ID VENTA");
					double totaMontoCeunta = cu.getTotal();
					double totalDevolucion= v.getTotal();
					double totalDevolucionGuardarCabeceraCuenta= v.getTotal();
					System.out.println("TOTAL CUENTA: "+totaMontoCeunta);
					System.out.println("TOTAL devolucion: "+totalDevolucion);		
					for (int  index = 0; index < cu.getCuentaCobrarDetalle().size()  & totalDevolucion > 0; index++) {
						CuentaCobrarDetalle cuentaDetalleGuardar= cu.getCuentaCobrarDetalle().get(index);
						double MONTOCUOTA = cu.getCuentaCobrarDetalle().get(index).getMonto();
						double MONTOIMPORTE = cu.getCuentaCobrarDetalle().get(index).getImporte();
						int cuota=cu.getCuentaCobrarDetalle().get(index).getNumeroCuota();
						
						if (totalDevolucion > (MONTOCUOTA - MONTOIMPORTE)) {
							totalDevolucion =  totalDevolucion - (MONTOCUOTA - MONTOIMPORTE);
							System.out.println("NUMERO CUOTA: "+cuota);
							System.out.println("DESCUENTO APLICADO : " + (MONTOCUOTA - MONTOIMPORTE));
							System.out.println("IMPORTE ANTERIOR : " + MONTOIMPORTE);
							System.out.println("IMPORTE ACTUAL : " + (MONTOIMPORTE + (MONTOCUOTA - MONTOIMPORTE)));
							System.out.println("RESTO TOTAL DEVOLUCION: "+totalDevolucion);
							cuentaDetalleGuardar.setImporte((MONTOIMPORTE + (MONTOCUOTA - MONTOIMPORTE)));

						}else {
							System.out.println("NUMERO CUOTA: "+cuota);
							System.out.println("DESCUENTO APLICADO : " + (totalDevolucion));
							System.out.println("IMPORTE ANTERIOR : " + MONTOIMPORTE);
							System.out.println("IMPORTE ACTUAL : " + (MONTOIMPORTE + (totalDevolucion)));
							System.out.println("RESTO TOTAL DEVOLUCION: "+(totalDevolucion-totalDevolucion));
							cuentaDetalleGuardar.setImporte((MONTOIMPORTE + (totalDevolucion)));
							totalDevolucion =0;
							
						}
						cuentaCobrarDetalleRepository.save(cuentaDetalleGuardar);
					}
					cu.setPagado(cu.getPagado()+totalDevolucionGuardarCabeceraCuenta);
					cu.setSaldo(cu.getSaldo()-totalDevolucionGuardarCabeceraCuenta);
					cuentaCobrarRepository.save(cu);
					
				}else {
					return new ResponseEntity<>(new CustomerErrorType("NO SE ENCONTRO NINGUA CUENTA A ESTA DEVOLUCIÓN TIPO VENTA CREDITO!"), HttpStatus.CONFLICT);
				}
			}
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}


	public Date getFechaPlazo(Date fe, int plazo){

		Date hoy = fe;
		long fechacontresdiasmas = hoy.getTime() + (plazo * 24 * 60 * 60 * 1000);
		Date fechacontresdiasmasformatada = new Date(fechacontresdiasmas);
		System.out.println("Nuva Fecha Plazo: "+fechacontresdiasmasformatada);
		return fechacontresdiasmasformatada;
	}

	public void actualizarProductoBaseA(int id , double cantidad) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}else {
			List<ProductoCardex> list = compuestoRepository.getBase(id);
			productoRepository.findByActualizaA(cantidad, id);//actualiza producto base
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual= cantidad / ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}
	}

	public void actualizarProductoBaseAumentar(int id , double cantidad) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			productoRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}else {
			List<ProductoCardex> list = compuestoRepository.getBase(id);
			productoRepository.findByActualizaA(cantidad, id);//actualiza producto base
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual= cantidad / ob.getCantidadAplicacion();
				productoRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}
	}


	@RequestMapping(method=RequestMethod.GET, value = "/consularNotaCredito/{id}")
	public NotaCredito getNotaDevolucionPorIdDevolucion (@PathVariable int id){

		Object  [][]  objeto =entityRepository.getNotaCreditoPorIdDevolucion(id);
		NotaCredito n = new NotaCredito();
		n.setId(Integer.parseInt(objeto[0][0].toString()));
		n.getCliente().getPersona().setNombre(objeto[0][1].toString());
		n.setTotal(Double.parseDouble(objeto[0][2].toString()));
		n.getFuncionario().getPersona().setNombre(objeto[0][3].toString());
		n.setTotalLetra(objeto[0][4].toString());
		n.getDevolucionVenta().setId(Integer.parseInt(objeto[0][5].toString()));
		n.getDevolucionVenta().setFecha(FechaUtil.convertirFechaStringADateUtil(objeto[0][6].toString()));
		n.getDevolucionVenta().setTotal(Double.parseDouble(objeto[0][7].toString()));
		return n;
	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody DevolucionVenta entity){
		try {

			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getVenta().getId()==0) {
				return new ResponseEntity<>(new CustomerErrorType("DEVES SELECCIONAR UNA VENTA A LA CUAL DEVOLVER PRODUCTO!"), HttpStatus.CONFLICT);
			}else if(entity.getFechaFactura()==null) {
				return new ResponseEntity<>(new CustomerErrorType("LA FECHA FACTURACION DE LA VENTA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getTotal() ==0 | entity.getTotal()==null) {
				return new ResponseEntity<>(new CustomerErrorType("EL TOTAL DEL MONTO A DEVOLVER DEBE SER MAYOR A CERO!"), HttpStatus.CONFLICT);
			}else if(entity.getVenta().getTipo().equals("1") && entity.getNumeroOperacion()==0 ) {
				return new ResponseEntity<>(new CustomerErrorType("EL NUMERO DE OPERACION DE LA FACTURA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			}else if(entity.getTipoDevolucion().getId()==1 && entity.getVenta().getTipo().equals("2")){
				return new ResponseEntity<>(new CustomerErrorType("NO SE PUEDE GUARDR DEVOLUCION DE VENTA A CREDITO CON REENVOLSO!"), HttpStatus.CONFLICT);
			}else
			{
				for(int ind=0; ind < entity.getDevolucionVentaDetalle().size(); ind++) {
					DevolucionVentaDetalle dev = entity.getDevolucionVentaDetalle().get(ind);
					if(dev.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE PRODUCTO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(dev.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE DEVOLUCION ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(dev.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE DEVOLUCION ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(dev.getSubTotal() == null){
						return new ResponseEntity<>(new CustomerErrorType("SUBTOTAL DEL DETALLE DEVOLUCION ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(dev.getDetalleProducto().getId() == 0){
						return new ResponseEntity<>(new CustomerErrorType("EL ID DEL DETALLE PRODUCTO EN DETALLE DEVOLUCION ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}

			}
			if(entity.getId() !=0) {
				entity.setHora(hora());
				entity.setFecha(new Date());
				int idVent=entity.getId();
				entityRepository.save(entity);
				if(entity.getDevolucionVentaDetalle().size()>0){
					for(DevolucionVentaDetalle detDevolucion: entity.getDevolucionVentaDetalle()) {
						detDevolucion.getDevolucionVenta().setId(idVent);
						detalleRepository.save(detDevolucion);
					}
				}
			}else {
				entity.setHora(hora());
				entity.setFecha(new Date());
				entityRepository.save(entity);
				DevolucionVenta id = entityRepository.getDevolucionUlt();
				int idVent=0;
				if(id == null){idVent=1;}else{idVent=id.getId();}
				for(DevolucionVentaDetalle detDevolcion: entity.getDevolucionVentaDetalle()) {
					detDevolcion.getDevolucionVenta().setId(idVent);
					detalleRepository.save(detDevolcion);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}


	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DevolucionVentaDetalle> detalles){
		try {
			if(detalles.size()!=-1) {
				System.out.println("con listado lista "+detalles.size());
				for (DevolucionVentaDetalle de : detalles) {
					System.out.println("ID ELIMINADO : "+de.getId());
					detalleRepository.deleteById(de.getId());
				}
				
				return  new  ResponseEntity<String>(HttpStatus.CREATED);

			}else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
