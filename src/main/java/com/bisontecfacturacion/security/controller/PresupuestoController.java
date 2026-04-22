package com.bisontecfacturacion.security.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.ParametroTipoHoja;
import com.bisontecfacturacion.security.config.NumerosALetras;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.TerminalConfigImpresora;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.ReporteConfig;
import com.bisontecfacturacion.security.model.ReporteFormatoDatos;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.Zona;
import com.bisontecfacturacion.security.repository.AnticipoReferenciaCajaChicaRepository;
import com.bisontecfacturacion.security.repository.ClienteRepository;
import com.bisontecfacturacion.security.repository.FuncionarioRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.PagosFuncionarioReferenciaOperacionCajaRepository;
import com.bisontecfacturacion.security.repository.ParametroTipoHojaRepository;
import com.bisontecfacturacion.security.repository.PresupuestoDetalleProductoRepository;
import com.bisontecfacturacion.security.repository.PresupuestoDetalleServicioRepository;
import com.bisontecfacturacion.security.repository.PresupuestoRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ReporteConfigRepository;
import com.bisontecfacturacion.security.repository.ReporteFormatoDatosRepository;
import com.bisontecfacturacion.security.repository.TerminalConfigImpresoraRepository;
import com.bisontecfacturacion.security.repository.ZonaRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.IUsuarioService;

import groovyjarjarcommonscli.ParseException;

@Transactional
@RestController
@RequestMapping("presupuesto")
public class PresupuestoController {

	@Autowired
	private PresupuestoRepository entityRepository;

	@Autowired
	private ProductoRepository productoRepository;
	@Autowired
	private ProductoCardexRepository compuestoRepository;

	@Autowired
	private PresupuestoDetalleProductoRepository detalleProductoRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository; 

	@Autowired
	private ClienteRepository clienteRepository; 

	@Autowired
	private ZonaRepository zonaRepository;
	@Autowired
	private ImpresoraRepository impresoraRepository;
	@Autowired
	private ReporteConfigRepository reporteConfigRepository;

	@Autowired
	private PresupuestoDetalleServicioRepository detalleServicioRepository;
	
	@Autowired
	private ParametroTipoHojaRepository parametroTipoHoja;
	
	@Autowired
	private TerminalConfigImpresoraRepository terminalRepository;
	
	@Autowired
	private ReporteFormatoDatosRepository reporteFormatoDatosRepository;
	
	@Autowired
	private IUsuarioService usuarioService;
	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private PagosFuncionarioReferenciaOperacionCajaRepository pagosFuncionarioReferenciaOperacionCajaRepository;
	
	@Autowired
	private AnticipoReferenciaCajaChicaRepository  anticipoReferenciaCajaChicaRepository;
	
	private Reporte report;
	


	private List<Presupuesto>listar(List<Presupuesto> obj){
		List<Presupuesto> res=new ArrayList<>();
		for(Presupuesto p:obj){
			Presupuesto pre=new Presupuesto();
			pre.setId(p.getId());
			pre.getFuncionario().getPersona().setNombre(p.getFuncionario().getPersona().getNombre()+" "+p.getFuncionario().getPersona().getApellido());
			pre.getFuncionario().getPersona().setCedula(p.getFuncionario().getPersona().getCedula());
			pre.getCliente().getPersona().setNombre(p.getCliente().getPersona().getNombre()+" "+ p.getCliente().getPersona().getApellido());
			pre.getCliente().getPersona().setCedula(p.getCliente().getPersona().getCedula());
			pre.setTotal(p.getTotal());
			pre.setFecha(p.getFecha());
			pre.setHora(p.getHora());
			pre.setEstado(p.getEstado());
			pre.setZona(p.getZona());
			res.add(pre);
		}
		return res;
	}
	@RequestMapping(method=RequestMethod.GET, value="/activo/{filtro}")
	public List<Presupuesto> getAlls(@PathVariable int filtro){
		List<Presupuesto> lisRetorno= new ArrayList<Presupuesto>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getPresupuestoAll());}
		if(filtro==2) { lisRetorno= listar(entityRepository.getPresupuestoActivo());}
		if(filtro==3) { lisRetorno= listar(entityRepository.getPresupuestoCerrado());}
		if(filtro==4) { lisRetorno= listar(entityRepository.getPresupuestoFinalizado());}

		return lisRetorno;
		
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/buscar/{filtro}")
	public List<Presupuesto> getAllsPorDescripcion(@RequestBody String descripcion, @PathVariable int filtro){
		List<Presupuesto> lisRetorno= new ArrayList<Presupuesto>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getPresupuestoAllDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==2) { lisRetorno= listar(entityRepository.getPresupuestoActivoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==3) { lisRetorno= listar(entityRepository.getPresupuestoCerradoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		if(filtro==4) { lisRetorno= listar(entityRepository.getPresupuestoFinalizadoDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%"));}
		return lisRetorno;
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/eliminarDetalleProducto/{id}")
	public ResponseEntity<?> eliminarDetallePresupuesotProductoPorId(@PathVariable int id){
		DetallePresupuestoProducto det = detalleProductoRepository.getDetalleConPresupuestoCabecera(id);
		if(det ==null){
			return new ResponseEntity<>(new CustomerErrorType("ESTE NUMERO DE DETALLE YA NO EXISTE.!"), HttpStatus.CONFLICT);
		}else {
			detalleProductoRepository.deleteById(id);
			this.actualizarProductoBasePresupuestoDescontar(det.getProducto().getId(), det.getCantidad());
			entityRepository.actualizarTotalDescpuesDeEliminarDetalle(det.getPresupuesto().getId(), det.getSubTotal());
			
			//return new ResponseEntity<String>("REGISTRO ELIMINADO", HttpStatus.OK); 
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/eliminarDetalleProducto")
	public ResponseEntity<?> eliminarDetalleProducto(@RequestBody List<DetallePresupuestoProducto> detalle){
		try {
			System.out.println(detalle.size()+ " lista size");
			if(detalle.size()!=-1) {
				System.out.println("con listado lista");
				for (DetallePresupuestoProducto de : detalle) {		
					System.out.println("entroo eliminar PRODUCTO for presu");
					if (de.getPresupuesto().getEstado().equals("FINALIZADO")) {
						System.out.println("ELIMINAR PRESUPUESOT FINALIZADO DESCUENTO STOCK PRESUPUESTO");
						this.actualizarProductoBasePresupuestoDescontar(de.getProducto().getId(), de.getCantidad());
					}
					detalleProductoRepository.deleteById(de.getId());
				}
			}else {
				return new ResponseEntity<>(new CustomerErrorType("NO HAY LISTA PARA ELIMINAR"), HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.POST, value="/eliminarDetalleServicio")
	public ResponseEntity<?> eliminarDetalleServicio(@RequestBody List<DetallePresupuestoServicio> detalle){
		try {
			System.out.println(detalle.size()+ " lista size");
			if(detalle.size()!=-1) {
				System.out.println("con listado lista SERV PRES");
				for (DetallePresupuestoServicio de :detalle) {		
					System.out.println("entroo eliminar servicio for presu");
					detalleServicioRepository.deleteById(de.getId());
				}
			}else {
				return new ResponseEntity<>(new CustomerErrorType("NO HAY LISTA PARA ELIMINAR"), HttpStatus.CONFLICT);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new  ResponseEntity<String>(HttpStatus.CREATED);

	}
	@RequestMapping(method=RequestMethod.GET, value="/nroDocumento")
	public Map<String, String> getPresupuestoNro(){
		Presupuesto v=entityRepository.findTop1ByOrderByIdDesc();
		Map<String, String> number = new HashMap<>();
		number.put("nroDocumento", v.getNroDocumento());
		return number;
	}


	@RequestMapping(method=RequestMethod.GET, value="/presupuestoId/{id}")
	public Presupuesto getPresupuestoId(@PathVariable int id){
		Presupuesto v=entityRepository.findById(id).orElse(null);
		Presupuesto pre=new Presupuesto();
		pre.setId(v.getId());
		pre.setNroDocumento(v.getNroDocumento());
		pre.setTotal(v.getTotal());
		pre.getFuncionario().setId(v.getFuncionario().getId());
		pre.getFuncionario().getPersona().setNombre(v.getFuncionario().getPersona().getNombre());
		pre.getFuncionario().getPersona().setApellido(v.getFuncionario().getPersona().getApellido());
		pre.getFuncionario().getPersona().setTelefono(v.getFuncionario().getPersona().getTelefono());
		pre.getFuncionario().getPersona().setCedula(v.getFuncionario().getPersona().getCedula());
		pre.getCliente().setId(v.getCliente().getId());
		pre.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre());
		pre.getCliente().getPersona().setApellido(v.getCliente().getPersona().getApellido());
		pre.getCliente().getPersona().setCedula(v.getCliente().getPersona().getCedula());
		pre.setEstado(v.getEstado());
		pre.setTotalIvaCinco(v.getTotalIvaCinco());
		pre.setTotalIvaDies(v.getTotalIvaDies());
		pre.setTotalIva(v.getTotalIva());
		pre.setTotalExcenta(v.getTotalExcenta());
		pre.setTotalLetra(v.getTotalLetra());
		pre.setZona(v.getZona());
		System.out.println(v.getDetallePresupuestoProducto().size()+" lista : persuipeusotroter");
		pre.setObs(v.getObs());
		List<DetallePresupuestoProducto> detProducto = new ArrayList<>();
		List<DetallePresupuestoServicio> detServicio = new ArrayList<>();
		detProducto = getDetalleProducto(detalleProductoRepository.lista(id));
		detServicio = getDetalleServ(id);
		pre.setDetallePresupuestoProducto(detProducto);
		pre.setDetallePresupuestoServicio(detServicio);
		
		return pre;
	}

	@RequestMapping(method=RequestMethod.GET, value="/detalleProducto/{id}")
	public List<DetallePresupuestoProducto> getDetalleProductoId(@PathVariable int id){
		List<Object[]> objeto=entityRepository.listaDetallePresupuestoProducto(id);
		List<DetallePresupuestoProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoProducto detalleProductos=new DetallePresupuestoProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getPresupuesto().setId(Integer.parseInt(ob[7].toString()));

			detalleProductos.getProducto().setPrecioVenta_1(Double.parseDouble(ob[8].toString()));
			detalleProductos.getProducto().setPrecioVenta_2(Double.parseDouble(ob[9].toString()));
			detalleProductos.getProducto().setPrecioVenta_3(Double.parseDouble(ob[10].toString()));
			detalleProductos.getProducto().setPrecioVenta_4(Double.parseDouble(ob[11].toString()));
			detalleProductos.setDescuento(Double.parseDouble((ob[12].toString())));
			detalleProductos.getProducto().getUnidadMedida().setDescripcion(ob[13].toString());
			if (ob[14].toString()==null) { detalleProductos.getProducto().setExistencia(0.0); } else { detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[14].toString())); }
			detalleProductos.setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			detalleProductos.getProducto().setCodbar(ob[16].toString());
			detalleProductos.getProducto().getMarca().setDescripcion(ob[17].toString());
			detalleProductos.getProducto().setPrecioCosto(Double.parseDouble(ob[18].toString()));

			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}
	

	@RequestMapping(method=RequestMethod.GET, value="/detalleServicio/{id}")
	public List<DetallePresupuestoServicio> getAllIdServicio(@PathVariable int id){
		List<DetallePresupuestoServicio> detalleServicio=new ArrayList<>();
		try {
			List<Object[]> objeto=entityRepository.listaDetallePresupuestoServicio(id);
			for(Object[] ob:objeto){
				DetallePresupuestoServicio detalleServicios=new DetallePresupuestoServicio();
				detalleServicios.getServicio().setId(Integer.parseInt(ob[0].toString()));
				detalleServicios.setDescripcion(ob[1].toString());
				detalleServicios.setCantidad(Double.parseDouble(ob[2].toString()));
				detalleServicios.setPrecio(Double.parseDouble(ob[3].toString()));
				detalleServicios.setSubTotal(Double.parseDouble(ob[4].toString()));
				detalleServicios.setIva(ob[5].toString());
				detalleServicios.setMontoIva(Double.parseDouble(ob[6].toString()));
				detalleServicios.getFuncionario().setId(Integer.parseInt(ob[7].toString()));
				detalleServicios.getFuncionario().getPersona().setNombre(ob[8].toString());
				detalleServicios.getFuncionario().getPersona().setApellido(ob[9].toString());
				detalleServicios.setId(Integer.parseInt(ob[10].toString()));
				detalleServicios.setObs(ob[11].toString());
				
				detalleServicio.add(detalleServicios);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return detalleServicio;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/actualizarEstado/{id}")
	public void updateEstadoACerrado(@PathVariable int id){
		try {
			entityRepository.cambiarEstado(id, "CERRADO");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/hablitarEstado/{id}/{estado}")
	public void updateEstado(@PathVariable int id, @PathVariable String estado){
		try {
			List<DetallePresupuestoProducto> det =detalleProductoRepository.getDeallePresupuestoProductoPorIdCabecera(id);
			for (int i = 0; i < det.size(); i++) {
				DetallePresupuestoProducto detNuevo = det.get(i);
                this.actualizarProductoBasePresupuestoDescontar(detNuevo.getProducto().getId(), detNuevo.getCantidad());
			}
			entityRepository.cambiarEstado(id, estado);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	private static String padF(int numero, int size) {
		Formatter ft= new Formatter();
		numero = numero + 1;
		ft.format("%0"+size+"d", numero);
		return ft.toString();
	}

	private String generarCodigo() {
		Presupuesto p=entityRepository.findTop1ByOrderByIdDesc();
		if (p !=null) {
			return padF(p.getId(), 12);
		} else {
			return padF(1, 12);
		}

	}
	public void actualizarProductoBasePresupuestoDescontar(int id, double cantidad) {
	    // Caso 1: Producto compuesto (tiene base)
	    ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
	    if (ca != null) {
	        System.out.println("Presupuesto - tiene compuesto, actualiza base única: " + id);
	        double cant = cantidad * ca.getCantidadAplicacion();

	        // Stock actual del producto base
	        Double stockBase = productoRepository.getStockPresupuestoById(ca.getProductoBase().getId());
	        if (stockBase != null && stockBase > 0) {
	            double descontar = Math.min(cant, stockBase);
	            productoRepository.findByActualizarStockPresupuestoD(descontar, ca.getProductoBase().getId());
	            System.out.println("DESCONTO BASE: " + descontar + " ID: " + ca.getProductoBase().getId());
	        }

	        // Actualizar compuestos relacionados
	        List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
	        for (ProductoCardex ob : list) {
	            double existenciaActual = (cantidad * ca.getCantidadAplicacion()) / ob.getCantidadAplicacion();
	            Double stockCompuesto = productoRepository.getStockPresupuestoById(ob.getProductoCompuesto().getId());
	            if (stockCompuesto != null && stockCompuesto > 0) {
	                double descontar = Math.min(existenciaActual, stockCompuesto);
	                productoRepository.findByActualizarStockPresupuestoD(descontar, ob.getProductoCompuesto().getId());
	                System.out.println("DESCONTO COMPUESTO: " + descontar + " ID: " + ob.getProductoCompuesto().getId());
	            }
	        }

	    } else {
	        // Caso 2: Producto base relacionado
	        ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
	        if (pBase != null) {
	            System.out.println("Presupuesto - producto relacionado con base: " + id);

	            Double stockBase = productoRepository.getStockPresupuestoById(id);
	            if (stockBase != null && stockBase > 0) {
	                double descontar = Math.min(cantidad, stockBase);
	                productoRepository.findByActualizarStockPresupuestoD(descontar, id);
	                System.out.println("DESCONTO BASE RELACIONADO: " + descontar + " ID: " + id);
	            }

	            // Actualizar compuestos relacionados
	            List<ProductoCardex> list = compuestoRepository.getBase(id);
	            for (ProductoCardex ob : list) {
	                double existenciaActual = cantidad / ob.getCantidadAplicacion();
	                Double stockCompuesto = productoRepository.getStockPresupuestoById(ob.getProductoCompuesto().getId());
	                if (stockCompuesto != null && stockCompuesto > 0) {
	                    double descontar = Math.min(existenciaActual, stockCompuesto);
	                    productoRepository.findByActualizarStockPresupuestoD(descontar, ob.getProductoCompuesto().getId());
	                    System.out.println("DESCONTO COMPUESTO RELACIONADO: " + descontar + " ID: " + ob.getProductoCompuesto().getId());
	                }
	            }

	        } else {
	            // Caso 3: Producto unitario
	            System.out.println("Presupuesto - producto unitario: " + id);

	            Double stockUnitario = productoRepository.getStockPresupuestoById(id);
	            if (stockUnitario != null && stockUnitario > 0) {
	                double descontar = Math.min(cantidad, stockUnitario);
	                productoRepository.findByActualizarStockPresupuestoD(descontar, id);
	                System.out.println("DESCONTO UNITARIO: " + descontar + " ID: " + id);
	            }
	        }
	    }
	}
	
	public void actualizarProductoBasePresupuestoAumentar(int id, double cantidad) {
	    // Caso 1: Producto compuesto (tiene base)
	    ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
	    if (ca != null) {
	        System.out.println("Presupuesto - tiene compuesto, actualiza base única: " + id);
	        double cant = cantidad * ca.getCantidadAplicacion();
	        // Actualizar stockPresupuesto de producto base
	        productoRepository.findByActualizarStockPresupuestoA(cant, ca.getProductoBase().getId());

	        // Actualizar stockPresupuesto de compuestos relacionados
	        List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
	        for (ProductoCardex ob : list) {
	            Double existenciaActual = (cantidad * ca.getCantidadAplicacion()) / ob.getCantidadAplicacion();
	            productoRepository.findByActualizarStockPresupuestoA(existenciaActual, ob.getProductoCompuesto().getId());
	            System.out.println("Presupuesto - actualiza compuesto relacionado: " + ob.getProductoCompuesto().getId());
	        }
	    } else {
	        // Caso 2: Producto base relacionado
	        ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
	        if (pBase != null) {
	            System.out.println("Presupuesto - producto relacionado con base: " + id);
	            productoRepository.findByActualizarStockPresupuestoA(cantidad, id);

	            // Actualizar compuestos relacionados
	            List<ProductoCardex> list = compuestoRepository.getBase(id);
	            for (ProductoCardex ob : list) {
	                Double existenciaActual = cantidad / ob.getCantidadAplicacion();
	                productoRepository.findByActualizarStockPresupuestoA(existenciaActual, ob.getProductoCompuesto().getId());
	                System.out.println("Presupuesto - actualiza compuesto relacionado: " + ob.getProductoCompuesto().getId());
	            }

	        } else {
	            // Caso 3: Producto unitario
	            System.out.println("Presupuesto - producto unitario: " + id);
	            productoRepository.findByActualizarStockPresupuestoA(cantidad, id);
	        }
	    }
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/producto")
	public ResponseEntity<?> eliminarProducto(@RequestBody List<DetallePresupuestoProducto> detalles){
		try {
			if(detalles.size()!=-1) {
				System.out.println("con listado lista");
				for (DetallePresupuestoProducto de : detalles) {
					this.actualizarProductoBasePresupuestoDescontar(de.getProducto().getId(), de.getCantidad());
					detalleProductoRepository.deleteById(de.getId());
				}
				System.out.println("sin lista");
				return  new  ResponseEntity<String>(HttpStatus.CREATED);

			}else {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?>  guardar(@RequestBody Presupuesto entity){
		try {
			System.out.println(entity.getFecha()+" fecha ess ");
			System.out.println(entity.getTotalLetra() +"total letras ess ");
			System.out.println(entity.getEstado() +" estado es  ess ");
			Double totalGenerales=0.0;
			Double totalDescuento=0.0;

			if (zonaRepository.count() == 0) {
			    return new ResponseEntity<>(
			        new CustomerErrorType("SE DEBE CARGAR AL MENOS UNA ZONA EN EL SISTEMA"),
			        HttpStatus.CONFLICT
			    );
			}

			// 2. Si no hay zona asignada o id es 0, asignar la primera zona registrada
			if (entity.getZona() == null || entity.getZona().getId() == 0) {
			    // Buscar la primera zona (puede ser la de menor ID)
				System.out.println("entro id 0 o null y asina el primer valor");
			    Optional<Zona> primeraZonaOpt = zonaRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().findFirst();
			    if (!primeraZonaOpt.isPresent()) {
			        return new ResponseEntity<>(
			            new CustomerErrorType("ERROR AL ASIGNAR ZONA POR DEFECTO"),
			            HttpStatus.CONFLICT
			        );
			    }
			    entity.setZona(primeraZonaOpt.get());
			}
			
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
			} else if(entity.getCliente().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL CLIENTE NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getDetallePresupuestoProducto().size() == 0 && entity.getDetallePresupuestoServicio().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getObs() != null){
				entity.setObs(entity.getObs().toUpperCase());
			} else if(entity.getTotalLetra() == null || entity.getTotalLetra().trim().equals("")){
				entity.setTotalLetra(NumerosALetras.convertirNumeroALetras(entity.getTotal()));
				System.out.println("nuep total letras: "+ entity.getTotalLetra());
			} else if(entity.getFecha() == null){
				entity.setFecha(LocalDateTime.now());
			} 
			
				for(int ind=0; ind < entity.getDetallePresupuestoProducto().size(); ind++) {
					DetallePresupuestoProducto pro = entity.getDetallePresupuestoProducto().get(ind);
					if(pro.getCantidad() == null || pro.getCantidad() <=0) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecio() == null || pro.getPrecio() <=0){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getSubTotal() == null || pro.getSubTotal() <=0){
						return new ResponseEntity<>(new CustomerErrorType("EL SUBTOTAL DEL DETALLE PRODUCTO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getIva()==null || !(pro.getIva().equals("10 %") || pro.getIva().equals("5 %") || pro.getIva().equals("Exenta"))){
						return new ResponseEntity<>(new CustomerErrorType("EL CAMPO IVA DEL DETALLE N°: "+(ind+1)+" NO TIENE UN FORMATO VALIDO!"), HttpStatus.CONFLICT);
					}
					totalGenerales = totalGenerales + (pro.getPrecio()*pro.getCantidad());
					totalDescuento = totalDescuento + (pro.getDescuento()*pro.getCantidad());
					System.out.println("TOTAL RECIBIDO: "+entity.getTotal());
					System.out.println("TOTAL RE-CALCULADO: "+entity.getTotal());
					System.out.println("TOTAL RE-CALCULADO: "+totalDescuento);

				}
				for(int ind=0; ind < entity.getDetallePresupuestoServicio().size(); ind++) {
					DetallePresupuestoServicio ser=entity.getDetallePresupuestoServicio().get(ind);
					if(ser.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE SERVICIO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE SERVICIO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getPrecio() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE SERVICIO ITEM N°: "+(ind+1)+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(ser.getIva()==null || !(ser.getIva().equals("10 %") || ser.getIva().equals("5 %") || ser.getIva().equals("Exenta"))){
						return new ResponseEntity<>(new CustomerErrorType("EL CAMPO IVA DEL DETALLE N°: "+(ind+1)+" NO TIENE UN FORMATO VALIDO!"), HttpStatus.CONFLICT);
					}
					totalGenerales = totalGenerales + (ser.getPrecio() * ser.getCantidad());
				}
				if(entity.getId() !=0) {
					entity.setHora(hora());
					int idVent=entity.getId();
					double total10=0, total5=0;
					if (entity.getDetallePresupuestoProducto().size() > 0) {
						List<DetallePresupuestoProducto> detallesAnteriores =
					            detalleProductoRepository.getDeallePresupuestoProductoPorIdCabecera(entity.getId());
						for (DetallePresupuestoProducto detalleNuevo : entity.getDetallePresupuestoProducto()) {
						
						    Optional<DetallePresupuestoProducto> detalleAnteriorOpt = detallesAnteriores.stream()
						            .filter(d -> Objects.equals(d.getProducto().getId(), detalleNuevo.getProducto().getId()))
						            .findFirst();

						    double cantidadNueva = detalleNuevo.getCantidad();

						    if (detalleAnteriorOpt.isPresent()) {
						        DetallePresupuestoProducto detalleAnterior = detalleAnteriorOpt.get();
						        double cantidadAnterior = detalleAnterior.getCantidad();
						        String estadoAnterior = detalleAnterior.getPresupuesto().getEstado(); // asumimos que cada detalle tiene un campo estado
						        System.out.println("ESTADOO ANTERIOR : "+estadoAnterior);
						        
						        if ("FINALIZADO".equals(entity.getEstado())) {
						            System.out.println("finalizado edit detalle");
						            double diferencia = cantidadNueva - cantidadAnterior;
						            this.actualizarProductoBasePresupuestoAumentar(detalleNuevo.getProducto().getId(), cantidadNueva);
						           
						        }else if("CERRADO".equals(entity.getEstado())) {
						            System.out.println("cerrado edit detalle");  
						        	this.actualizarProductoBasePresupuestoDescontar(detalleNuevo.getProducto().getId(), cantidadAnterior);
						            
						        }else if("ABIERTO".equals(entity.getEstado())) {
						        	
						        }

						    } else {
						    	    if ("FINALIZADO".equals(entity.getEstado())) {
							        	System.out.println("finalizado nuevo detalle");
						    	        this.actualizarProductoBasePresupuestoAumentar(detalleNuevo.getProducto().getId(), cantidadNueva);
						    	    }else if("CERRADO".equals(entity.getEstado())) {
							        	System.out.println("CERRADO nuevo detalle");

						    	    }else if("ABIERTO".equals(entity.getEstado())) {
							        	System.out.println("ABIERTO nuevo detalle");
						    	    }
						    }

						    // Resto de lógica: IVA, tipo precio, guardado
						    if ("10 %".equals(detalleNuevo.getIva())) {
						        total10 += Utilidades.calcularIvaDies(detalleNuevo.getSubTotal());
						        detalleNuevo.setMontoIva(Utilidades.calcularIvaDies(detalleNuevo.getSubTotal()));
						    } else if ("5 %".equals(detalleNuevo.getIva())) {
						        total5 += Utilidades.calcularIvaCinco(detalleNuevo.getSubTotal());
						        detalleNuevo.setMontoIva(Utilidades.calcularIvaCinco(detalleNuevo.getSubTotal()));
						    } else {
						        detalleNuevo.setMontoIva(0.0);
						    }

						    detalleNuevo.setTipoPrecio(validarPrecio(detalleNuevo.getProducto().getId(), detalleNuevo.getPrecio()));
						    detalleNuevo.getPresupuesto().setId(idVent);
						    detalleProductoRepository.save(detalleNuevo);
						}
					}
					if(entity.getDetallePresupuestoServicio().size()>0){
						for(DetallePresupuestoServicio detalleServicio: entity.getDetallePresupuestoServicio()) {
							detalleServicio.getPresupuesto().setId(idVent);
							if(detalleServicio.getIva().equals("10 %")) {

								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal()); 
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("5 %")) {
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("Exenta")) {
								detalleServicio.setMontoIva(0.0);
							}
							if(detalleServicio.getObs()!=null) {detalleServicio.setObs(detalleServicio.getObs().toUpperCase());}else {detalleServicio.setObs("");}
							detalleServicioRepository.save(detalleServicio);
						}
					}
					entity.setTotal(totalGenerales - totalDescuento);
					entity.setTotalLetra(NumerosALetras.convertirNumeroALetras((totalGenerales - totalDescuento)));
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalIva(total10+total5);
					entity = entityRepository.save(entity);
					System.out.println("entro udpate edit");
					
				}else {
					entity.setHora(hora());
					entity.setNroDocumento(generarCodigo());
					entityRepository.save(entity);
					Presupuesto id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					//eliminarDetallePorCabecera(entity.getId());
					double total10=0, total5=0;
					if(entity.getDetallePresupuestoProducto().size()>0){
						List<DetallePresupuestoProducto> detallesAnteriores =
					            detalleProductoRepository.getDeallePresupuestoProductoPorIdCabecera(entity.getId());
					    for (DetallePresupuestoProducto detalleNuevo : entity.getDetallePresupuestoProducto()) {

						    Optional<DetallePresupuestoProducto> detalleAnteriorOpt = detallesAnteriores.stream()
						            .filter(d -> Objects.equals(d.getProducto().getId(), detalleNuevo.getProducto().getId()))
						            .findFirst();

						    double cantidadNueva = detalleNuevo.getCantidad();

						    if (detalleAnteriorOpt.isPresent()) {
						        DetallePresupuestoProducto detalleAnterior = detalleAnteriorOpt.get();
						        double cantidadAnterior = detalleAnterior.getCantidad();
						        String estadoAnterior = detalleAnterior.getPresupuesto().getEstado(); // asumimos que cada detalle tiene un campo estado
						        System.out.println("ESTADOO ANTERIOR : "+estadoAnterior);
						        
						        if ("FINALIZADO".equals(entity.getEstado())) {
						            System.out.println("finalizado edit detalle");
						            double diferencia = cantidadNueva - cantidadAnterior;
						            this.actualizarProductoBasePresupuestoAumentar(detalleNuevo.getProducto().getId(), cantidadNueva);
						           
						        }else if("CERRADO".equals(entity.getEstado())) {
						            System.out.println("cerrado edit detalle");  
						        	this.actualizarProductoBasePresupuestoDescontar(detalleNuevo.getProducto().getId(), cantidadAnterior);
						            
						        }else if("ABIERTO".equals(entity.getEstado())) {
						        	
						        }

						    } else {
						    	    if ("FINALIZADO".equals(entity.getEstado())) {
							        	System.out.println("finalizado nuevo detalle");
						    	        this.actualizarProductoBasePresupuestoAumentar(detalleNuevo.getProducto().getId(), cantidadNueva);
						    	    }else if("CERRADO".equals(entity.getEstado())) {
							        	System.out.println("CERRADO nuevo detalle");

						    	    }else if("ABIERTO".equals(entity.getEstado())) {
							        	System.out.println("ABIERTO nuevo detalle");
						    	    }
						    }

						    // Resto de lógica: IVA, tipo precio, guardado
						    if ("10 %".equals(detalleNuevo.getIva())) {
						        total10 += Utilidades.calcularIvaDies(detalleNuevo.getSubTotal());
						        detalleNuevo.setMontoIva(Utilidades.calcularIvaDies(detalleNuevo.getSubTotal()));
						    } else if ("5 %".equals(detalleNuevo.getIva())) {
						        total5 += Utilidades.calcularIvaCinco(detalleNuevo.getSubTotal());
						        detalleNuevo.setMontoIva(Utilidades.calcularIvaCinco(detalleNuevo.getSubTotal()));
						    } else {
						        detalleNuevo.setMontoIva(0.0);
						    }

						    detalleNuevo.setTipoPrecio(validarPrecio(detalleNuevo.getProducto().getId(), detalleNuevo.getPrecio()));
						    detalleNuevo.getPresupuesto().setId(idVent);
						    detalleProductoRepository.save(detalleNuevo);
					    }
					}

					if(entity.getDetallePresupuestoServicio().size()>0){
						for(DetallePresupuestoServicio detalleServicio: entity.getDetallePresupuestoServicio()) {
							detalleServicio.getPresupuesto().setId(idVent);
							if(detalleServicio.getIva().equals("10 %")) {
								total10 = total10 + Utilidades.calcularIvaDies(detalleServicio.getSubTotal()); 
								detalleServicio.setMontoIva(Utilidades.calcularIvaDies(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("5 %")) {
								total5 = total5 + Utilidades.calcularIvaCinco(detalleServicio.getSubTotal());
								detalleServicio.setMontoIva(Utilidades.calcularIvaCinco(detalleServicio.getSubTotal()));
							}
							if(detalleServicio.getIva().equals("Exenta")) {
								detalleServicio.setMontoIva(0.0);
							}
							detalleServicioRepository.save(detalleServicio);	
						}

					}
					entity.setTotal(totalGenerales - totalDescuento);
					entity.setTotalLetra(NumerosALetras.convertirNumeroALetras((totalGenerales - totalDescuento)));
					entity.setTotalIvaDies(total10);
					entity.setTotalIvaCinco(total5);
					entity.setTotalIva(total10+total5);
					entity = entityRepository.save(entity);
					System.out.println("entro insert nuevo");
					
			}
				entity.setFuncionario(funcionarioRepository.getIdFuncionario(entity.getFuncionario().getId()));
				entity.setCliente(clienteRepository.getIdCliente(entity.getCliente().getId()));
				Map<String, Object> mapa = new HashMap<>();
		        mapa.put("presupuesto", entity);
		        
		        // 🔹 Devolver todo junto
		        return new ResponseEntity<>(mapa, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Presupuesto>(entity, HttpStatus.CREATED);
	}

	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}

	public String validarPrecio(int id, double precio) {
		Producto pro=productoRepository.findById(id).get();
		String op="";
		if (pro.getPrecioVenta_4() == precio) {
			op="P4";
		} 
		if (pro.getPrecioVenta_3() == precio) {
			op= "P3";
		}
		if (pro.getPrecioVenta_2() == precio) {
			op= "P2";
		}
		if (pro.getPrecioVenta_1() == precio) {
			op= "P1";
		}
		return op;
	}
	@RequestMapping(method=RequestMethod.POST, value = "/pre/{numeroTerminal}")
	public void pdfPrintPresupuesto(@RequestBody Presupuesto pres, @PathVariable int numeroTerminal) {
		
		Reporte report = new Reporte();
		TerminalConfigImpresora t = new TerminalConfigImpresora();
		t= terminalRepository.consultarTerminalPorNumeros(numeroTerminal);
		if (t==null) {
			System.out.println("Se debe cargar numero terminal dentro de la base de datos");
		}else {
			ReporteConfig reportConfig = reporteConfigRepository.getOne(2);
			List<Presupuesto> venta = getListasss(pres.getId());
			
			report=new Reporte();
			int pageSize = 10;
			int totalPages = (int) Math.ceil((double) venta.get(0).getDetallePresupuestoProducto().size() / pageSize);
			System.out.println("TOTAL DE PAGINAS:"+ totalPages);
			
			List<Presupuesto> listaVentaImpresion= new ArrayList<Presupuesto>();
			
			for (int i = 0; i < totalPages; i++) {	
			    System.out.println("\n--- Página " + (i + 1) + " ---");

			    int start = i * pageSize;
			    int end = Math.min(start + pageSize, venta.get(0).getDetallePresupuestoProducto().size());
			    // Crear una nueva lista con los elementos de la página actual
			    List<DetallePresupuestoProducto> detallesPagina = new ArrayList<>(venta.get(0).getDetallePresupuestoProducto().subList(start, end));
			    Double totalMontoPagina=0.0, totalPaginaIvaCinco=0.0, totalPaginaIvaDies=0.0, totalPaginaIva=0.0,totalPaginaExcenta=0.0;
			    for (int j = 0; j < detallesPagina.size(); j++) {
					totalMontoPagina = totalMontoPagina + detallesPagina.get(j).getSubTotal();
					if(detallesPagina.get(j).getIva().equals("10 %")) {totalPaginaIvaDies = totalPaginaIvaDies +  (detallesPagina.get(j).getSubTotal()/11);}
					if(detallesPagina.get(j).getIva().equals("5 %")) {totalPaginaIvaCinco = totalPaginaIvaCinco +  (detallesPagina.get(j).getSubTotal()/21);}
					if(detallesPagina.get(j).getIva().equals("Excenta")) {totalPaginaExcenta = totalPaginaExcenta +  (detallesPagina.get(j).getSubTotal());}
			    }
			    Presupuesto ventaImpresion = new Presupuesto();
			    ventaImpresion.setId(venta.get(0).getId());
			    ventaImpresion.setFecha(venta.get(0).getFecha());
			    ventaImpresion.setEstado(venta.get(0).getEstado());
			    ventaImpresion.setCliente(venta.get(0).getCliente());
			    ventaImpresion.setFuncionario(venta.get(0).getFuncionario());
			    ventaImpresion.setTotalLetra(venta.get(0).getTotalLetra());
			    ventaImpresion.setHora(venta.get(0).getHora());
			    ventaImpresion.setObs(venta.get(0).getObs());
			    ventaImpresion.setTotal(totalMontoPagina);
			    ventaImpresion.setTotalLetra(NumerosALetras.convertirNumeroALetras(totalMontoPagina));
			    ventaImpresion.setTotalIvaDies(totalPaginaIvaDies);
			    ventaImpresion.setTotalIvaCinco(totalPaginaIvaCinco);
			    ventaImpresion.setTotalExcenta(totalPaginaExcenta);
			    ventaImpresion.setTotalIva(totalPaginaIvaDies +  totalPaginaIvaCinco); 
			    ventaImpresion.setDetallePresupuestoProducto(detallesPagina);
			    listaVentaImpresion.add(ventaImpresion);
			    System.out.println("UNA FILA DE LA PAGINA" +listaVentaImpresion.get(i).getDetallePresupuestoProducto().get(0).getDescripcion());
			  

			}
			Map<String, Object> map = new HashMap<>();
				if (t.getImpresora().equals("matricial")) {
				ReporteFormatoDatos f = reporteFormatoDatosRepository.getOne(1);
				String urlReporte ="\\reporte\\"+reportConfig.getNombreSubReporte1()+".jasper";
				System.out.println("NOMBRE REPORTE: "+reportConfig.getNombreReporte());
				System.out.println("NOMBRE SUBREPORTE: "+urlReporte);
				map.put("urlSubRepor", urlReporte);
				map.put("tituloReporte", f.getTitulo());
				map.put("razonSocialReporte", f.getRazonSocial());
				map.put("descripcionMovimiento", f.getDescripcion());
				map.put("direccionReporte", f.getDireccion());
				map.put("telefonoReporte", f.getTelefono());
				map.put("paginaTotal", totalPages+ "");
				try {
					ParametroTipoHoja p = parametroTipoHoja.getOne(1);
		        	System.out.println("total apartido lista :  "+listaVentaImpresion.size());
		        	for (int i=0; i < listaVentaImpresion.size(); i++) {
		        		map.put("paginaActual", (i +1)+ "");
		        		if(p.getDescripcion().equals("A4")) {
			        		report.reportPDFImprimirA4(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
		        		}
		        		if(p.getDescripcion().equals("CORTE")) {
			        		report.reportPDFImprimirLibreCorte(Arrays.asList(listaVentaImpresion.get(i)), map, reportConfig.getNombreReporte(), t.getNombreImpresora(), reportConfig.getPageWidth(), reportConfig.getPageHeigth());
		        		}
		            }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {System.out.println("Impresora ticket seleccionada");}
		}
	
	}
	public List<DetallePresupuestoServicio> getDetalleServ(int idVenta) {
		List<Object[]> objeto=detalleServicioRepository.listarDetallePresupuesto(idVenta);
		List<DetallePresupuestoServicio> detalleServicio=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoServicio detalleServicios=new DetallePresupuestoServicio();
			detalleServicios.setId(Integer.parseInt(ob[0].toString()));
			detalleServicios.getServicio().setId(Integer.parseInt(ob[1].toString()));
			detalleServicios.setDescripcion(ob[2].toString());
			detalleServicios.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleServicios.setPrecio(Double.parseDouble(ob[4].toString()));
			detalleServicios.setSubTotal(Double.parseDouble(ob[5].toString()));
			detalleServicios.getPresupuesto().setId(Integer.parseInt(ob[6].toString()));
			detalleServicios.setIva(ob[7].toString());
			detalleServicios.getFuncionario().setId(Integer.parseInt(ob[8].toString()));
			detalleServicios.setObs(ob[11].toString());
			detalleServicio.add(detalleServicios);
		}
		return detalleServicio;
	}

	public List<DetallePresupuestoProducto> getDetalleProducto(List<Object[]> objeto) {
		List<DetallePresupuestoProducto> detalleProducto=new ArrayList<>();
		for(Object[] ob:objeto){
			DetallePresupuestoProducto detalleProductos=new DetallePresupuestoProducto();
			detalleProductos.setId(Integer.parseInt(ob[0].toString()));
			detalleProductos.getProducto().setId(Integer.parseInt(ob[1].toString()));
			detalleProductos.setDescripcion(ob[2].toString());
			detalleProductos.setCantidad(Double.parseDouble(ob[3].toString()));
			detalleProductos.setIva(ob[4].toString());
			detalleProductos.setPrecio(Double.parseDouble(ob[5].toString()));
			detalleProductos.setSubTotal(Double.parseDouble(ob[6].toString()));
			detalleProductos.getPresupuesto().setId(Integer.parseInt(ob[7].toString()));

			detalleProductos.getProducto().setPrecioVenta_1(Double.parseDouble(ob[8].toString()));
			detalleProductos.getProducto().setPrecioVenta_2(Double.parseDouble(ob[9].toString()));
			detalleProductos.getProducto().setPrecioVenta_3(Double.parseDouble(ob[10].toString()));
			detalleProductos.getProducto().setPrecioVenta_4(Double.parseDouble(ob[11].toString()));
			detalleProductos.setDescuento(Double.parseDouble((ob[12].toString())));
			detalleProductos.getProducto().getUnidadMedida().setDescripcion(ob[13].toString());
			if (ob[14].toString()==null) { detalleProductos.getProducto().setExistencia(0.0); } else { detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[14].toString())); }
			detalleProductos.setIsBalanza(Boolean.parseBoolean(ob[15].toString()));
			detalleProductos.getProducto().setCodbar(ob[16].toString());
			detalleProductos.getProducto().getMarca().setDescripcion(ob[17].toString());
			detalleProductos.setMontoIva(Double.parseDouble(ob[18].toString()));
			detalleProductos.getProducto().setExistencia(Double.parseDouble(ob[19].toString()));
			detalleProductos.getProducto().setStockPresupuesto(Double.parseDouble(ob[20].toString()));
			detalleProducto.add(detalleProductos);
		}

		return detalleProducto;
	}
	public Presupuesto presupuestosss(int idPresupuesto) {
		Presupuesto cv = null;

		cv=entityRepository.findById(idPresupuesto).orElse(null);
		//		System.out.println(""+cv.getCliente().getPersona().);
		/*
		List<Venta> v= new ArrayList<Venta>();
		v.add(cv);
		for(int i = 0; i < 1; i++) {
			cv = new Venta();
			cv = v.get(i);
			System.out.println(cv.getCliente().getPersona().getNombre()+"asdfadsfasdfadsads");

		}*/

		return cv;
		/*
		Presupuesto pre = null;
		List<Object[]> v=entityRepository.getPresupuestoId(idPresupuesto);
		for(Object[] ob: v) {
			pre = new Presupuesto();
			pre.getCliente().getPersona().setNombre(ob[0].toString());
			pre.getCliente().getPersona().setCedula(ob[1].toString());
			if (ob[2].toString().equals("")) {pre.getCliente().getPersona().setDireccion("xxx");} else {pre.getCliente().getPersona().setDireccion(ob[2].toString());}
			pre.setFecha(FechaUtil.convertirFechaStringADateUtil(ob[3].toString()));
			pre.setHora(ob[4].toString());
			pre.getFuncionario().getPersona().setNombre(ob[5].toString());

			pre.setTotalIvaCinco(Double.parseDouble(ob[6].toString()));
			pre.setTotalIvaDies(Double.parseDouble(ob[7].toString()));
			pre.setTotal(Double.parseDouble(ob[8].toString()));
			pre.setTotalLetra(ob[9].toString());
			pre.setNroDocumento(ob[10].toString());
			//pre.getDocumento().setDescripcion(ob[12].toString());
		}
		return pre;*/
	}

	public List<Presupuesto> getListasss(int idPresupuesto ) {


		Presupuesto presu = new Presupuesto();
		List<Presupuesto> lista = new ArrayList<>();
		List<DetallePresupuestoProducto> detProducto = new ArrayList<>();
		List<DetallePresupuestoServicio> detServicio = new ArrayList<>();


		presu = presupuestosss(idPresupuesto);
		detProducto = getDetalleProducto(detalleProductoRepository.lista(idPresupuesto));
		detServicio = getDetalleServ(idPresupuesto);


		for (int i = 0; i < 1; i++) {
			Presupuesto v = new Presupuesto();
			v.getCliente().getPersona().setNombre(presu.getCliente().getPersona().getNombre()+" "+presu.getCliente().getPersona().getApellido());
			v.getCliente().getPersona().setCedula(presu.getCliente().getPersona().getCedula());
			v.getCliente().getPersona().setDireccion(presu.getCliente().getPersona().getDireccion());
			v.getCliente().getPersona().setTelefono(presu.getCliente().getPersona().getTelefono());
			v.setFecha(presu.getFecha());
			v.setHora(presu.getHora());
			v.getFuncionario().setId(presu.getFuncionario().getId());
			v.getFuncionario().getPersona().setNombre(presu.getFuncionario().getPersona().getNombre());
			v.getFuncionario().getPersona().setCedula(presu.getFuncionario().getPersona().getCedula());
			v.getFuncionario().getPersona().setApellido(presu.getFuncionario().getPersona().getApellido());
			v.getFuncionario().getPersona().setTelefono(presu.getFuncionario().getPersona().getTelefono());
			v.setTotalIvaCinco(presu.getTotalIvaCinco());
			v.setTotalIvaDies(presu.getTotalIvaDies());
			v.setTotal(presu.getTotal());
			v.setTotalLetra(presu.getTotalLetra());
			v.setNroDocumento(presu.getNroDocumento());
			//v.getDocumento().setDescripcion(venta.getDocumento().getDescripcion());

			v.setId(presu.getId());
			v.setEstado(presu.getEstado());
			v.setObs(presu.getObs());
			v.setZona(presu.getZona());
			v.setDetallePresupuestoProducto(detProducto);


			for(DetallePresupuestoServicio det: detServicio) {
				DetallePresupuestoProducto detalleProducto = new DetallePresupuestoProducto();
				detalleProducto.getProducto().setId(det.getServicio().getId());
				detalleProducto.setDescripcion("SER - "+det.getDescripcion());
				detalleProducto.getProducto().getUnidadMedida().setDescripcion("UN");
				detalleProducto.getProducto().setCodbar(det.getServicio().getId()+"");
				detalleProducto.setCantidad(det.getCantidad());
				detalleProducto.setPrecio(det.getPrecio());
				detalleProducto.setIva(det.getIva()+"");
				detalleProducto.setSubTotal(det.getSubTotal());
				detalleProducto.setMontoIva(det.getMontoIva());
				System.out.println(""+det.getSubTotal());
				System.out.println(detalleProducto.getSubTotal());
				v.getDetallePresupuestoProducto().add(detalleProducto);
				detalleProducto.getSubTotal();
			}
			lista.add(v);
		}
		
		return lista;

	}
	
	@RequestMapping(value="/descargarPdf/{id}", method=RequestMethod.GET)
	public ResponseEntity<?>  resumenConcepto(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int id) throws IOException {
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		Org org = orgRepository.findById(1).get();
		Presupuesto pre= new Presupuesto(); 
		pre=entityRepository.getOne(id);
		List<Presupuesto> listado= new ArrayList<Presupuesto>();
		listado.add(pre);
		for (int i = 0; i < listado.size(); i++) {
			
			for (int j = 0; j < listado.get(i).getDetallePresupuestoServicio().size(); j++) {
				DetallePresupuestoServicio detAux = listado.get(i).getDetallePresupuestoServicio().get(j);
				DetallePresupuestoProducto detAgregar =new DetallePresupuestoProducto();
				detAgregar.setDescripcion("SER - "+detAux.getDescripcion());
				detAgregar.getProducto().setCodbar(detAux.getServicio().getId()+"");
				detAgregar.setCantidad(detAux.getCantidad());
				detAgregar.setPrecio(detAux.getPrecio());
				detAgregar.getProducto().getUnidadMedida().setDescripcion("UN");;
				detAgregar.setIva(detAux.getIva()+"");
				detAgregar.setSubTotal(detAux.getSubTotal());
				detAgregar.setMontoIva(detAux.getMontoIva());
				listado.get(i).getDetallePresupuestoProducto().add(detAgregar);
			}
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
		
				report = new Reporte();
				report.reportPDFDescarga(listado, map, "ReportePresupuestoPdf", response);
				//report.reportPDFImprimir(listado, map, "ReporteCompraRangoFecha", "Microsoft Print to PDF");
			
		} catch (Exception e) {
			e.printStackTrace();
			return  new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar"), HttpStatus.CONFLICT);
		}
		return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/reportePresupuestoListado/rango/{id}/{tipo}/{zona}/{fechaI}/{fechaF}")
	public  List<Presupuesto> getReportePresupuestoClienteRangoListado(OAuth2Authentication authentication,@PathVariable int id, @PathVariable int tipo,@PathVariable int zona, @PathVariable String fechaI, @PathVariable String fechaF) throws IOException, ParseException{
		List<Presupuesto> lis =new ArrayList<>();
		List<Presupuesto> listado=new ArrayList<>();
		String estado="";

		try {
			Calendar cc= Calendar.getInstance();
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			Date fecI;
			System.out.println("fecha que viene: "+fechaI+ ", "+fechaF);
			fecI = formater.parse(fechaI);
			Date fecF=formater.parse(fechaF);
			System.out.println(fecF.getDate());
			fecF.setHours(23);
			fecF.setSeconds(59);
			fecI.setHours(0);
			fecI.setSeconds(1);
			System.out.println("hora final fechas::: "+fecF+ " hora inicio finbal: "+fecI);
//			if(tipo == 1 && zona == 1) {
//				lis= entityRepository.findByPrespuestoPorIdClientesTodoRango(id, fecI, fecF);
//			}
//			if(tipo == 1){
//				
//			}
//			if(tipo == 2){
//				lis= entityRepository.findByCuentaPorIdACobrarRango(id,fecI, fecF);
//			}
//			if(tipo == 3){
//				lis= entityRepository.findByCuentaPorIdCobradoRango(id,fecI, fecF);
//			}
//			listado = listadoCargar(lis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listado;
	}
	

}
