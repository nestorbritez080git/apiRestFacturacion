package com.bisontecfacturacion.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.model.Pedido;
import com.bisontecfacturacion.security.repository.PedidoRepository;

@Transactional()
@RestController
@RequestMapping("pedido")
public class PedidoController {
	
	@Autowired
	private PedidoRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.POST)
	public void guardarDetallePedido(@RequestBody Pedido entity){	
		entityRepository.save(entity);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Pedido> getPedidoAll(){
		return entityRepository.findAll();
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
