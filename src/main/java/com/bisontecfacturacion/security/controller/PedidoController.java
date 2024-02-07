package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.hoteleria.model.ReservacionCabecera;
import com.bisontecfacturacion.security.model.Compra;
import com.bisontecfacturacion.security.model.DetalleCompra;
import com.bisontecfacturacion.security.model.DetallePresupuestoProducto;
import com.bisontecfacturacion.security.model.DetallePresupuestoServicio;
import com.bisontecfacturacion.security.model.Pedido;
import com.bisontecfacturacion.security.model.PedidoDetalle;
import com.bisontecfacturacion.security.model.Presupuesto;
import com.bisontecfacturacion.security.repository.PedidoDetalleRepository;
import com.bisontecfacturacion.security.repository.PedidoRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;

@Transactional()
@RestController
@RequestMapping("pedido")
public class PedidoController {
	
	@Autowired
	private PedidoRepository entityRepository;
	
	@Autowired
	private PedidoDetalleRepository detalleRepository;
	
	@Transactional
	@RequestMapping(method=RequestMethod.GET, value = "/finalizarPedido/{idPedido}/{est}")
	public  void finalizarPedido(@PathVariable int idPedido, @PathVariable String est){
		System.out.println("FINALIZADO PEDIDO");
		this.entityRepository.finalizarPedido(idPedido, est);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/eliminarDetalle")
	public ResponseEntity<?> eliminarDetalle(@RequestBody List<PedidoDetalle> detalle){
		System.out.println("ELIMINAR DETALLE PEDIDO NUEVO" +detalle.size());
		try {
				System.out.println("ELIMINAR DETALLE PEDIDO NUEVO LIST");
				for (PedidoDetalle de : detalle) {	
					System.out.println("ELIMINAR DETALLE PEDIDO NUEVO FOR");
					System.out.println(de.getId());
					detalleRepository.deleteById(de.getId());
				}
				System.out.println("sin lista");
				return  new  ResponseEntity<String>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@Transactional
	@RequestMapping(method=RequestMethod.POST)
	public  ResponseEntity<?> guardarPedido(@RequestBody Pedido entity){	
		try {
			entity.setFecha(new Date());
			if(entity.getFuncionario().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL FUNCIONARIO NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT); 
			} else if(entity.getProveedor().getId() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("EL PROVEEDOR NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else if(entity.getPedidoDetalle().size() == 0) {
				return new ResponseEntity<>(new CustomerErrorType("LA GRILLA NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
			} else 
			{
				for(int ind=0; ind < entity.getPedidoDetalle().size(); ind++) {
					PedidoDetalle pro = entity.getPedidoDetalle().get(ind);
					if(pro.getCantidad() == null) {
						return new ResponseEntity<>(new CustomerErrorType("LA CANTIDAD DEL DETALLE PRODUCTO ITEM N°: "+(ind++)+", NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getDescripcion() == null){
						return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}else if(pro.getPrecioCosto() == null){
						return new ResponseEntity<>(new CustomerErrorType("EL PRECIO DEL DETALLE PRODUCTO ITEM N°: "+ind+1+" NO DEBE QUEDAR VACIO!"), HttpStatus.CONFLICT);
					}
				}
				
				if(entity.getId() !=0) {
					entity.setHora(hora());
					entityRepository.save(entity);
					int idVent=entity.getId();
					if(entity.getPedidoDetalle().size()>0){
						for(PedidoDetalle d: entity.getPedidoDetalle()) {
							d.getPedido().setId(idVent);
							if(d.getIva().equals("10 %")) {
								d.setMontoIva(Utilidades.calcularIvaDies(d.getSubTotal()));
							}
							if(d.getIva().equals("5 %")) {
								d.setMontoIva(Utilidades.calcularIvaCinco(d.getSubTotal()));
							}
							if(d.getIva().equals("Exenta")) {
								d.setMontoIva(0.0);
							}
							detalleRepository.save(d);	
						}
					}		
				}else {
					entity.setHora(hora());
					entityRepository.save(entity);
					Pedido id = entityRepository.findTop1ByOrderByIdDesc();
					int idVent=0;
					if(id == null){idVent=1;}else{idVent=id.getId();}
					//eliminarDetallePorCabecera(entity.getId());
					if(entity.getPedidoDetalle().size()>0){
						//	actualizarLoteDocumento(entity.getDocumento().getId(), entity.getNroDocumento());
						for(PedidoDetalle detalleProducto: entity.getPedidoDetalle()) {
							detalleProducto.getPedido().setId(idVent);
							if(detalleProducto.getIva().equals("10 %")) {
								detalleProducto.setMontoIva(Utilidades.calcularIvaDies(detalleProducto.getSubTotal()));
							}
							if(detalleProducto.getIva().equals("5 %")) {
								detalleProducto.setMontoIva(Utilidades.calcularIvaCinco(detalleProducto.getSubTotal()));
							}
							if(detalleProducto.getIva().equals("Exenta")) {
								detalleProducto.setMontoIva(0.0);
							}
							
							detalleRepository.save(detalleProducto);
						
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Pedido>(entity, HttpStatus.CREATED);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Pedido> getPedidoAll(){
		return entityRepository.findAll();
	}
	private List<Pedido>listar(List<Pedido> obj){
		List<Pedido> res=new ArrayList<>();
		for(Pedido p:obj){
			Pedido pe=new Pedido();
			pe.setId(p.getId());
			pe.getFuncionario().getPersona().setNombre(p.getFuncionario().getPersona().getNombre()+" "+p.getFuncionario().getPersona().getApellido());
			pe.getFuncionario().setId(p.getFuncionario().getId());
			pe.getProveedor().getPersona().setNombre(p.getProveedor().getPersona().getNombre()+" "+p.getProveedor().getPersona().getApellido());
			pe.getProveedor().setId(p.getProveedor().getId());
			pe.setTotal(p.getTotal());
			pe.setHora(p.getHora());
			pe.setEstado(p.getEstado());
			pe.setFecha(p.getFecha());
			res.add(pe);
		}
		return res;
	}
	@RequestMapping(method=RequestMethod.GET, value = "/activo/{filtro}")
	public List<Pedido> getPedidoAllActivo(@PathVariable int filtro){
		List<Pedido> lisRetorno= new ArrayList<Pedido>();
		if(filtro==1) { lisRetorno= listar(entityRepository.getPedidoAll());}
		if(filtro==2) { lisRetorno= listar(entityRepository.getPedidoActivo());}
		if(filtro==3) { lisRetorno= listar(entityRepository.getPedidoCerrado());}

		return lisRetorno;
	}
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public Pedido getPedidoId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	private List<PedidoDetalle> cargarDetallePedidoPorCabecera(List<Object []> list) {
		List<PedidoDetalle> listaRetorno= new ArrayList<PedidoDetalle>();
		for(Object[] d: list) {
			PedidoDetalle det=new PedidoDetalle();
			det.setId(Integer.parseInt(d[0].toString()));
			det.getProducto().setId(Integer.parseInt(d[1].toString()));
			det.getProducto().setCodbar(d[2].toString());
			det.setDescripcion(d[3].toString());
			det.setCantidad(Double.parseDouble(d[4].toString()));
			det.setPrecioCosto(Double.parseDouble(d[5].toString()));
			det.setIva(d[6].toString());
			det.setSubTotal(Double.parseDouble(d[7].toString()));
			det.setPrecioVenta_1(Double.parseDouble(d[8].toString()));
			det.setPrecioVenta_2(Double.parseDouble(d[9].toString()));
			det.setPrecioVenta_3(Double.parseDouble(d[10].toString()));
			det.setPrecioVenta_4(Double.parseDouble(d[11].toString()));
			det.getPedido().setId(Integer.parseInt(d[12].toString()));
			det.getProducto().getUnidadMedida().setDescripcion(d[13].toString());
			det.getProducto().getMarca().setDescripcion(d[14].toString());

			listaRetorno.add(det);
		}
		return listaRetorno;

	}
	@RequestMapping(method=RequestMethod.GET, value="/detallePedido/{id}")
	public List<PedidoDetalle> getDetallePedidoProductoId(@PathVariable int id){
		return cargarDetallePedidoPorCabecera(entityRepository.listaDetallePedidoProducto(id));
	}
	
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/detalleProducto/{id}")
	public List<PedidoDetalle> getDetallePedidoPorIdCabecera(@PathVariable int id){
		
		return null;
		
	}
	
	}
	/*
	@Autowired
	private PedidoRepository entityRepository;
	@Autowired
	private PedidoDetalleRepository pedidoDetalleRepository;
	@Autowired
	private ProductoRepository productoRepository;
	
	public static int idVenta;
	
	@RequestMapping(method=RequestMethod.POST)
	public void  guardarPedidoCabecera(@RequestBody Pedido entity){
		idVenta = entity.getId();
		if (entity.getId() == 0) {
			entityRepository.save(entity);
		} else {
			entityRepository.actualizarCabecera(entity.getObs(), entity.getTotal(), entity.getVolumenTotal(), entity.getCliente().getId(), entity.getDocumento().getId(), entity.getFuncionario().getId(), entity.getFuncionarioV().getId(), entity.getId());
		}
	}


	@RequestMapping(method=RequestMethod.GET, value = "/{fecha}")
	public List<Pedido> getPedidoAll(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Pedido> lista=entityRepository.getPedidoFecha(ano, mes, dia);
		List<Pedido> pedido=new ArrayList<>();
		for(Pedido p: lista) {
			Pedido pe=new Pedido();
			pe.setId(p.getId());
			pe.getFuncionario().getPersona().setNombre(p.getFuncionario().getPersona().getNombre());
			pe.getFuncionarioV().getPersona().setNombre(p.getFuncionarioV().getPersona().getNombre());
			pe.getCliente().getPersona().setNombre(p.getCliente().getPersona().getNombre());
			pe.setTotal(p.getTotal());
			pe.setFecha(p.getFecha());
			pe.setVolumenTotal(p.getVolumenTotal());
			pedido.add(pe);
		}
		return pedido;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/detallePedido")
	public void guardarDetallePedido(@RequestBody List<PedidoDetalle> entity){	
		
		Pedido dt= new Pedido();
		dt= entityRepository.findTop1ByOrderByIdDesc();
			for (PedidoDetalle pedidoDetalle : entity) {
				if(pedidoDetalle.getId() == 0) {
				pedidoDetalle.getPedido().setId(dt.getId());
				pedidoDetalle.setTipoPrecio(validarPrecio(pedidoDetalle.getProducto().getId(), pedidoDetalle.getPrecio()));
				pedidoDetalleRepository.save(pedidoDetalle);
				} else {
					pedidoDetalle.setTipoPrecio(validarPrecio(pedidoDetalle.getProducto().getId(), pedidoDetalle.getPrecio()));
					actualizarDetallePedido(entity);
				}
		}


		}
		

	public void actualizarDetallePedido(List<PedidoDetalle> entity){	
			for (PedidoDetalle pedidoDetalle : entity) {
				pedidoDetalle.setTipoPrecio(validarPrecio(pedidoDetalle.getProducto().getId(), pedidoDetalle.getPrecio()));
				pedidoDetalleRepository.actualizarDetalle(pedidoDetalle.getCantidad(), pedidoDetalle.getDescripcion(), pedidoDetalle.getIva(), pedidoDetalle.getPrecio(), pedidoDetalle.getSubTotal(), pedidoDetalle.getTipoPrecio(), pedidoDetalle.getVolumenAcumulado(), pedidoDetalle.getProducto().getId(), pedidoDetalle.getId());
				}
			}
	
	public String validarPrecio(int id, double precio) {
		Producto pro=productoRepository.findById(id).get();
		String op="";
		if (pro.getPrecioVenta_1() == precio) {
			op="P1";
		} 
		if (pro.getPrecioVenta_2() == precio) {
			op= "P2";
		}
		if (pro.getPrecioVenta_3() == precio) {
			op= "P3";
		}
		if (pro.getPrecioVenta_4() == precio) {
			op= "P4";
		}
		return op;
	}

	@RequestMapping(method=RequestMethod.GET, value="/pedidoId/{id}")
	public Pedido getPedidoId(@PathVariable int id){
        Pedido v=entityRepository.findById(id).get();
		Pedido pedido=new Pedido();
		pedido.setId(v.getId());
        pedido.setEstado(v.getEstado());
		pedido.setNroDocumento(v.getNroDocumento());
        pedido.setTotal(v.getTotal());
        pedido.getDocumento().setId(v.getDocumento().getId());
        pedido.getCliente().setId(v.getCliente().getId());
		pedido.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre());
		pedido.getCliente().getPersona().setApellido(v.getCliente().getPersona().getApellido());
		pedido.getDocumento().setId(v.getDocumento().getId());
		pedido.getFuncionario().setId(v.getFuncionario().getId());
		pedido.getFuncionarioV().setId(v.getFuncionarioV().getId());
		pedido.setVolumenTotal(v.getVolumenTotal());
		return pedido;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/detallePedido/{id}")
	public List<PedidoDetalle> getPedidoDetalleXId(@PathVariable int id){
		List<PedidoDetalle> objeto=pedidoDetalleRepository.getDetalleXId(id);
		List<PedidoDetalle> detallePedido=new ArrayList<>();
		for(PedidoDetalle ob: objeto){
			PedidoDetalle pedido=new PedidoDetalle();
			pedido.setId(ob.getId());
			pedido.getProducto().setId(ob.getProducto().getId());
			pedido.setDescripcion(ob.getDescripcion());
			pedido.setCantidad(ob.getCantidad());
			pedido.setIva(ob.getIva());
			pedido.setPrecio(ob.getPrecio());
			pedido.setSubTotal(ob.getSubTotal());
			pedido.getPedido().setId(ob.getPedido().getId());
			pedido.getProducto().setPrecioVenta_1(ob.getProducto().getPrecioVenta_1());
			pedido.getProducto().setPrecioVenta_2(ob.getProducto().getPrecioVenta_2());
			pedido.getProducto().setPrecioVenta_3(ob.getProducto().getPrecioVenta_3());
			pedido.getProducto().setPrecioVenta_4(ob.getProducto().getPrecioVenta_4());
			pedido.getProducto().getUnidadMedida().setDescripcion(ob.getProducto().getUnidadMedida().getDescripcion());

			detallePedido.add(pedido);
		}
		return detallePedido;
	}

	@RequestMapping(method=RequestMethod.POST, value="/deletePedido")
	public ResponseEntity<?> eliminarProducto(@RequestBody PedidoDetalle detalle){
	try {
		pedidoDetalleRepository.deleteById(detalle.getId());
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	} catch (Exception e) {
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}

	/*
    @Autowired
    private PedidoRepository entityRepository;
    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;
    @Autowired 
    private ProductoRepository productoRepository;


    @RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Pedido getPorId(@PathVariable int id){
		return entityRepository.findOne(id);
    }
    @RequestMapping(method=RequestMethod.POST)
	public void  guardar(@RequestBody Pedido entity){
         entityRepository.save(entity);
    }
   
    @RequestMapping(method=RequestMethod.GET, value="/pedidoId/{id}")
	public Pedido getPedidoId(@PathVariable int id){
        Pedido v=entityRepository.findOne(id);
		Pedido pedido=new Pedido();
		pedido.setId(v.getId());
        pedido.setEstado(v.getEstado());
		//pedido.setCotizacion(v.getCotizacion());
		pedido.setNro_documento(v.getNro_documento());
        pedido.setTotal(v.getTotal());
       // pedido.getMoneda().setId(v.getMoneda().getId());
        pedido.getDocumento().setId(v.getDocumento().getId());
        
        pedido.getCliente().setId(v.getCliente().getId());
		pedido.getCliente().getPersona().setNombre(v.getCliente().getPersona().getNombre());
		pedido.getCliente().getPersona().setApellido(v.getCliente().getPersona().getApellido());


		
		pedido.getDocumento().setId(v.getDocumento().getId());
		pedido.getFuncionarioV().setId(v.getFuncionarioV().getId());
		
		return pedido;
    }
     /*


	@RequestMapping(method=RequestMethod.POST, value="/detalleProducto")
	public void guardarDetalleProducto(@RequestBody List<PedidoDetalle> entity){	
		Pedido dt= new Pedido();
		dt= entityRepository.findTop1ByOrderByIdDesc();
			for (PedidoDetalle pedidoDetalle : entity) {
				pedidoDetalle.getPedido().setId(dt.getId());
				pedidoDetalleRepository.save(pedidoDetalle);
				//productoRepository.findByActualizaD(pedidoDetalle.getCantidad(), pedidoDetalle.getProducto().getId());	
			}
        }
        @RequestMapping(method=RequestMethod.POST, value="/detalleProducto/modificar")
	public void modificarDetalleProducto(@RequestBody List<PedidoDetalle> entity){
		if(entity.size() != -1){
			
			int idPedido=0;
			System.out.println(idPedido);
			idPedido = entity.get(0).getPedido().getId();
			Pedido dt=entityRepository.getOne(idPedido);
			
			for (PedidoDetalle detalleProducto : entity) {
					detalleProducto.getPedido().setId(idPedido);
					pedidoDetalleRepository.save(detalleProducto);
					//productoRepository.findByActualizaD(detalleProducto.getCantidad(), detalleProducto.getProducto().getId());	
		    }
        }	
}   
    @RequestMapping(method=RequestMethod.GET, value="det")
    public List<PedidoDetalle> getAllsss(){	
	return pedidoDetalleRepository.findAll();
    }
    @RequestMapping(value="/v", method=RequestMethod.GET)
    public List<Pedido> getall() {
        return entityRepository.findAll();
    }
    */
