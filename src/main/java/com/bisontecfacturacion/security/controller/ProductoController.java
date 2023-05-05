package com.bisontecfacturacion.security.controller;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bisontecfacturacion.security.auth.InventarioReporteModelAux;
import com.bisontecfacturacion.security.auxiliar.ProductoAuxiliar;
import com.bisontecfacturacion.security.config.Reporte;
import com.bisontecfacturacion.security.config.Utilidades;
import com.bisontecfacturacion.security.model.AjusteInventario;
import com.bisontecfacturacion.security.model.Concepto;
import com.bisontecfacturacion.security.model.Funcionario;
import com.bisontecfacturacion.security.model.Grupo;
import com.bisontecfacturacion.security.model.Impresora;
import com.bisontecfacturacion.security.model.Marca;
import com.bisontecfacturacion.security.model.MovimientoEntradaSalida;
import com.bisontecfacturacion.security.model.Org;
import com.bisontecfacturacion.security.model.Producto;
import com.bisontecfacturacion.security.model.ProductoCardex;
import com.bisontecfacturacion.security.model.Proveedor;
import com.bisontecfacturacion.security.model.Usuario;
import com.bisontecfacturacion.security.model.UtilidadPrecio;
import com.bisontecfacturacion.security.model.Venta;
import com.bisontecfacturacion.security.repository.AjusteInventarioRepository;
import com.bisontecfacturacion.security.repository.ConceptoRepository;
import com.bisontecfacturacion.security.repository.GrupoRepository;
import com.bisontecfacturacion.security.repository.ImpresoraRepository;
import com.bisontecfacturacion.security.repository.MarcaRepository;
import com.bisontecfacturacion.security.repository.MovimientoE_SRepository;
import com.bisontecfacturacion.security.repository.OrgRepository;
import com.bisontecfacturacion.security.repository.ProductoCardexRepository;
import com.bisontecfacturacion.security.repository.ProductoRepository;
import com.bisontecfacturacion.security.repository.ProveedorRepository;
import com.bisontecfacturacion.security.repository.SubGrupoRepository;
import com.bisontecfacturacion.security.repository.UtilidadPrecioRepository;
import com.bisontecfacturacion.security.service.CustomerErrorType;
import com.bisontecfacturacion.security.service.FechaUtil;
import com.bisontecfacturacion.security.service.IUsuarioService;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;


@Transactional
@RestController
@RequestMapping("producto")
public class ProductoController {
	@Autowired
	private OrgRepository orgRepository;
	
	private Reporte report;
	
	@Autowired
	private ProductoRepository entityRepository;
	
	@Autowired
	private MovimientoE_SRepository movEntradaSalidaRepository;
	
	@Autowired
	private ConceptoRepository conceptoRepository;

	@Autowired
	private AjusteInventarioRepository ajusteInventarioRepository;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ImpresoraRepository repository;

	@Autowired
	private UtilidadPrecioRepository utilidadPrecioRepository;

	@Autowired
	private ProductoCardexRepository compuestoRepository;
	
	@Autowired
	private MarcaRepository marcaRepository;

	@Autowired
	private GrupoRepository grupoRepository;

	@Autowired
	private SubGrupoRepository subGrupoRepository;

	
	@RequestMapping(method=RequestMethod.GET)
	public List<Producto> getAll(){
		Impresora conf=repository.findById(2).get();
		if(conf.isEstado() == true) {
			List<Producto> objeto=entityRepository.lista();
			return product(objeto);
		} else {
			List<Producto> objeto=entityRepository.listaStockBajo();
			return product(objeto);
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value = "/listarInventario")
	public List<Producto> getAllInventario(){
	
			List<Producto> objeto=entityRepository.listarInventario();
			return product(objeto);
	
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/producto/descripcion")
	public List<Producto> getAllProducto(@RequestBody String descripcion){
		if (descripcion.equals("9999999999")) {
			List<Producto> objeto=entityRepository.lista();
			return product(objeto);
		} else {
			List<Producto> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value = "/producto/descripcion/ajusteStock")
	public List<Producto> getAllProductoAjusteStock(@RequestBody String descripcion){
		if (descripcion.equals("9999999999")) {
			List<Producto> objeto=entityRepository.listaAjusteStock();
			return product(objeto);
		} else {
			List<Producto> objeto=entityRepository.getBuscarPorDescripcionAjusteStock("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
		}
	}

	
	public String generarNroAleatorio() {
		Random random=new Random();
		String valor = "";

		for (int i = 0; i < 10; i++) {
			valor = valor.concat(random.nextInt(9)+"");
		}

		return valor;
	}
	@RequestMapping(method=RequestMethod.GET, value = "/producto/generardorBarCode")
	public Map<String, String> getGeneradorBarCode(){
		List<Producto > lis= entityRepository.findAll();
		boolean ope=false;
		String barCode="";
		do {
			barCode= generarNroAleatorio();
			for (Producto p: lis) {
				if(barCode.equals(p.getCodbar())) {
					ope = true;
				}else {
					ope = false;
				}
			}
			
		} while (ope == true);
		
		Map<String, String> map = new HashMap<>();
		map.put("codBar", barCode);
		
		
		return map;
		
	}
	
	
	public List<Producto> product(List<Producto> objeto) {
		List<Producto> producto=new ArrayList<>();
		for(Producto ob:objeto){
			Producto productos=new Producto();
			productos.setId(ob.getId());
			productos.setDescripcion(ob.getDescripcion());
			productos.getMarca().setDescripcion(ob.getMarca().getDescripcion());
			productos.getMarca().setId(ob.getMarca().getId());
			productos.setExistencia(ob.getExistencia());
			productos.setStock_minimo(ob.getStock_minimo());
			productos.getGrupo().setDescripcion(ob.getGrupo().getDescripcion());
			if (ob.getCodoriginal() == null) {productos.setCodoriginal("");
			} else {productos.setCodoriginal(ob.getCodoriginal());}
			if (ob.getCodbar() == null) {productos.setCodbar("");
			} else {productos.setCodbar(ob.getCodbar());}
			productos.setPrecioCosto(ob.getPrecioCosto());
			productos.setPrecioVenta_1(ob.getPrecioVenta_1());
			productos.setPrecioVenta_2(ob.getPrecioVenta_2());
			productos.setPrecioVenta_3(ob.getPrecioVenta_3());
			productos.setPrecioVenta_4(ob.getPrecioVenta_4());
			productos.getUnidadMedida().setDescripcion(ob.getUnidadMedida().getDescripcion());
			productos.getUnidadMedida().setId(ob.getUnidadMedida().getId());
			productos.getGrupo().setId(ob.getGrupo().getId());
			productos.getGrupo().setDescripcion(ob.getGrupo().getDescripcion());
			producto.add(productos);
		}


		return producto;
	}

	public Producto producto(Producto pro) {
		Producto producto=new Producto();
		producto.setId(pro.getId());
		producto.setDescripcion(pro.getDescripcion());
		producto.setCodbar(pro.getCodbar());
		producto.getMarca().setDescripcion(pro.getMarca().getDescripcion());
		producto.getUnidadMedida().setDescripcion(pro.getUnidadMedida().getDescripcion());
		producto.setPrecioVenta_1(pro.getPrecioVenta_1());
		producto.setPrecioVenta_2(pro.getPrecioVenta_2());
		producto.setPrecioVenta_3(pro.getPrecioVenta_3());
		producto.setPrecioVenta_4(pro.getPrecioVenta_4());
		producto.setExistencia(pro.getExistencia());
		producto.setIva(pro.getIva());
		producto.setIsBalanza(pro.getIsBalanza());
		producto.setPrecioCosto(pro.getPrecioCosto());
		producto.getMarca().setDescripcion(pro.getMarca().getDescripcion());
		return producto;
	}
	@RequestMapping(method=RequestMethod.GET,value="/{id}")
	public Producto getPorId(@PathVariable int id){
		return entityRepository.findById(id).get();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/interno/{id}")
	public ResponseEntity<?> getPorIdInterno(@PathVariable int id){
		Producto prod = new Producto();
		prod = entityRepository.findById(id).orElse(null);
		if (prod == null) {
			return new ResponseEntity<>(new CustomerErrorType("El CÓDIGO INTERNO: "+id+" NO EXISTE.!"), HttpStatus.CONFLICT);	
		}else {
			return new ResponseEntity<>(producto(prod), HttpStatus.OK);
		}
	}

	@RequestMapping(method=RequestMethod.GET,value="/ult")
	public Producto getUlt(){
		return entityRepository.findTop1ByOrderByIdDesc();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="subirImagen")
	public Marca guardarImagen(@RequestParam("image") MultipartFile imagen) {
		
		Marca nombreEntity = new Marca();
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append(UUID.randomUUID().toString().replace("-", ""));
		String type = imagen.getContentType();
		if("image/png".equals(type)) {
			fileName.append(".png");
		} else
		if("image/jpeg".equals(type)) {
			fileName.append(".jpeg");
		} else
		if("image/gif".equals(type)) {
			fileName.append(".git");
		} else {
			nombreEntity.setId(504);
			nombreEntity.setDescripcion("Elige un tipo de imagen valido...");
		}
		
		
		// En produccion
		  Path directorioImagen = Paths.get("webapps//imagen");
		//desarrollo	
		  //Path directorioImagen = Paths.get("src//main//resources//static//imagen");
			String rutaAbsoluta = directorioImagen.toFile().getAbsolutePath();
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + fileName.toString());
				Files.write(rutaCompleta, bytesImg);
				nombreEntity.setId(204);
				nombreEntity.setDescripcion(fileName.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return nombreEntity;
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> guardar(@RequestBody Producto entity){
	
		if(entity.getStock_minimo()==null) {
			entity.setStock_minimo(0.0);
		}else
		if(entity.getExistencia()== null) {
			entity.setExistencia(0.0);
		}else if(entity.getDescripcion()!=null){
			entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().trim().toUpperCase()));			
		}else {
			return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL PRODUCTO ES OBLIGATORIO"), HttpStatus.CONFLICT);
		}
		if(entity.getCodoriginal()!=null){
			entity.setCodoriginal(Utilidades.eliminaCaracterIzqDer(entity.getCodoriginal().trim().toUpperCase()));
		}  
		if(entity.getFabricante()!=null){
			entity.setFabricante(Utilidades.eliminaCaracterIzqDer(entity.getFabricante().trim().toUpperCase()));
		} 
		if(entity.getAplicacion()!=null){
			entity.setAplicacion(Utilidades.eliminaCaracterIzqDer(entity.getAplicacion().trim().toUpperCase()));
		}			
		if(entity.getMarca().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA MARCA PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getGrupo().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN GRUPO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getSubGrupo().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR SUBGRUPO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getUnidadMedida().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA UNIDAD MEDIDA PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getDeposito().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN DEPOSITO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getProveedor().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN PROVEEDOR PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getIva().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR TIPO IVA"), HttpStatus.CONFLICT);
		}
		if(entity.getCodbar()!= ""){
			if (siExiste(entity)) {
				return new ResponseEntity<>(new CustomerErrorType("EL CÓDIGO DE BARRA:  "+entity.getCodbar()+", YA EXISTE EN EL SISTEMA"), HttpStatus.CONFLICT);
			}
		}
		

		entityRepository.save(entity);
		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}

	public boolean siExiste(Producto entity){
		return entityRepository.findByCodbar(entity.getCodbar())!=null;
	}
	public Producto siExisteEditar(Producto entity){
		if (entity.getCodbar() == null) {
			entity.setCodbar("");
		}
		return entityRepository.findByCodbar(entity.getCodbar());
	}

	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<?> editar(@RequestBody Producto entity){

		if(entity.getDescripcion()!=null){
			entity.setDescripcion(Utilidades.eliminaCaracterIzqDer(entity.getDescripcion().trim().toUpperCase()));			
		}else {
			return new ResponseEntity<>(new CustomerErrorType("LA DESCRIPCIÓN DEL PRODUCTO ES OBLIGATORIO"), HttpStatus.CONFLICT);
		}
		if(entity.getCodoriginal()!=null){
			entity.setCodoriginal(Utilidades.eliminaCaracterIzqDer(entity.getCodoriginal().trim().toUpperCase()));
		}  
		if(entity.getFabricante()!=null){
			entity.setFabricante(Utilidades.eliminaCaracterIzqDer(entity.getFabricante().trim().toUpperCase()));
		} 
		if(entity.getAplicacion()!=null){
			entity.setAplicacion(Utilidades.eliminaCaracterIzqDer(entity.getAplicacion().trim().toUpperCase()));
		}			
		if(entity.getMarca().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA MARCA PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getGrupo().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN GRUPO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getSubGrupo().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR SUBGRUPO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getUnidadMedida().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UNA UNIDAD MEDIDA PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getDeposito().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN DEPOSITO PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getProveedor().getId()<=0){
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR UN PROVEEDOR PARA EL PRODUCTO"), HttpStatus.CONFLICT);
		}
		if(entity.getIva().equals("")) {
			return new ResponseEntity<>(new CustomerErrorType("DEBES SELECCIONAR TIPO IVA"), HttpStatus.CONFLICT);
		}
		if(entity.getCodbar()!= ""){
			Producto p = new Producto();
			p=siExisteEditar(entity);
			if (p!=null) {
				if(entity.getId()==p.getId()){
					entityRepository.save(entity);
				}else {
					return new ResponseEntity<>(new CustomerErrorType("EL CÓDIGO DE BARRA:  "+entity.getCodbar()+", PERTENECE A UN PRODUCTO YA REGISTRADO ANTERIORMENTE"), HttpStatus.CONFLICT);
				}
			}else {
				entityRepository.save(entity);
			}
		}else {
			entityRepository.save(entity);
		}

		return  new  ResponseEntity<String>(HttpStatus.CREATED);
	}
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id){
		List<Producto> p = entityRepository.getVerificarProductoEliminar(id);
		if(p.size()>0){
			return new ResponseEntity<>(new CustomerErrorType("NO SE PUEDE ELIMINAR EL PRODUCTO  : "+p.get(0).getDescripcion()+ ", ES PROBABLE QUE ESTÉ RELACIONADO CON UNA TRANSACCIÓN DENTRO DEL SISTEMA"), HttpStatus.CONFLICT);
		}else {
			entityRepository.deleteById(id);
			//return new ResponseEntity<String>("REGISTRO ELIMINADO", HttpStatus.OK); 
		}
		return new ResponseEntity<>(HttpStatus.CREATED);

	}


	@SuppressWarnings("unused")
	@RequestMapping(method=RequestMethod.GET, value="/codbar/{codbar}")
	public ResponseEntity<?> buscarCodigoBarra(@PathVariable String codbar){
		Producto producto=new Producto();
		producto=entityRepository.findByCodbar(codbar);
		if(producto==null){
			if(codbar.length() < 8) {
				return new ResponseEntity<>(new CustomerErrorType("El tipo de código barra : "+codbar+" no permitido."), HttpStatus.CONFLICT);	
			} else {
				String codigo = codbar.substring(1, 7);
				Producto p = new Producto();
				p=entityRepository.findById(Integer.parseInt(codigo)).orElse(null);
				if (p==null) {
					return new ResponseEntity<>(new CustomerErrorType("El código barra: "+codbar+" no existe."), HttpStatus.CONFLICT);	
				} else {
					producto = p;
				}
			}
			if(producto !=null) {
				if(producto.getIsBalanza() == true) {
					return new ResponseEntity<>(producto(producto), HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(new CustomerErrorType("El código barra: "+codbar+" no existe."), HttpStatus.CONFLICT);	
			}
		}

		return new ResponseEntity<>(producto(producto), HttpStatus.OK);

	}


	@RequestMapping(method=RequestMethod.GET, value="/codbarProducto/{codbar}")
	public ResponseEntity<?> buscarCodigoBarraProducto(@PathVariable String codbar){
		if(entityRepository.findByCodbar(codbar)==null) {
			return new ResponseEntity<>(new CustomerErrorType("EL CÓDIGO DE BARRA: "+codbar+" NO EXISTE."), HttpStatus.CONFLICT);	
		}	
		Producto producto=new Producto();
		producto=entityRepository.findByCodbar(codbar);
		return new ResponseEntity<>(producto(producto), HttpStatus.OK);

	}


	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion")
	public List<Producto> consultarPorDescripcion(@RequestBody String descripcion){
		Impresora conf=repository.findById(2).get();
		if(conf.isEstado() == true) {
			List<Producto> objeto=entityRepository.getBuscarPorDescripcion("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
		} else {
			List<Producto> objeto=entityRepository.getBuscarPorDescripcionStockBajo("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
		}
	}
	@RequestMapping(method=RequestMethod.POST, value="/buscar/descripcion/inventario")
	public List<Producto> consultarPorDescripcionListadoInventario(@RequestBody String descripcion){
		
			List<Producto> objeto=entityRepository.getBuscarPorDescripcionListadoInventario("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%");
			return product(objeto);
	
	}

	public void actualizarProductoBaseAumentar(int id , double cantidad,  int idfuncio, int idCab) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			System.out.println("CANT. ACT. : "+existenciaBase);
			entityRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());

			Producto p = entityRepository.getOne( ca.getProductoBase().getId());
			MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

			mov.setDescripcion(p.getDescripcion());
			mov.setCantidad(existenciaBase);
			mov.setFecha(new  Date());
			mov.setHora(hora());

			mov.setIngreso(0.0);
			mov.setEgreso(0.0);
			mov.setVentaSalida(0.0);

			mov.setCostoEntrada(p.getPrecioCosto());
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
			mov.getFuncionario().setId(idfuncio);
			mov.setMarca(p.getMarca().getDescripcion());
			Concepto c= new Concepto();
			c= conceptoRepository.findById(8).get();
			mov.setReferencia(c.getDescripcion()+" REF.: "+ idCab);
			movEntradaSalidaRepository.save(mov);
			
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("compra - entro tiene compusto actuliza todas los compuesto por base relacionado :");
				Double exi=0.0;
				exi=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				
				entityRepository.findByActualizaA(exi, ob.getProductoCompuesto().getId());
				
				System.out.println("CANT. ACT. : "+exi);
				
				Producto pp = entityRepository.getOne(ob.getProductoCompuesto().getId());
				System.out.println("CANT DESPUES: "+pp.getExistencia());
				MovimientoEntradaSalida movEntr = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				System.out.println("compra - entro tiene compusto actuliza todas los compuesto por base relacionado : :"+exi+ " "+pp.getDescripcion());
				movEntr.setDescripcion(pp.getDescripcion());
				movEntr.setCantidad(exi);
				movEntr.setFecha(new  Date());
				movEntr.setHora(hora());

				movEntr.setIngreso(0.0);
				movEntr.setEgreso(0.0);
				movEntr.setVentaSalida(0.0);

				movEntr.setCostoEntrada(pp.getPrecioCosto());
				movEntr.setCostoEntradaAnterior(0.0);
				movEntr.setCostoSalida(pp.getPrecioCosto());

				movEntr.setVenta_1(pp.getPrecioVenta_1());
				movEntr.setVenta_2(pp.getPrecioVenta_2());
				movEntr.setVenta_3(pp.getPrecioVenta_3());
				movEntr.setVenta_4(pp.getPrecioVenta_4());

				movEntr.setVenta_1_anterior(0.0);
				movEntr.setVenta_2_anterior(0.0);
				movEntr.setVenta_3_anterior(0.0);
				movEntr.setVenta_4_anterior(0.0);

				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(pp.getMarca().getDescripcion());
				Concepto ccc= new Concepto();
				ccc= conceptoRepository.findById(8).get();
				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCab);
				movEntradaSalidaRepository.save(movEntr);
				
			
				
			}
		}else {
			System.out.println("entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("Producto relacio0nado con un base");
				
				entityRepository.findByActualizaA(cantidad, id);
				Producto pp = entityRepository.getOne(id);
				MovimientoEntradaSalida movEntr = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				movEntr.setDescripcion(pp.getDescripcion());
				movEntr.setCantidad(cantidad);
				movEntr.setFecha(new  Date());
				movEntr.setHora(hora());
				movEntr.setVentaSalida(0.0);

				movEntr.setEgreso(0.0);
				movEntr.setVentaSalida(0.0);

				movEntr.setCostoEntrada(pp.getPrecioCosto());
				movEntr.setCostoEntradaAnterior(0.0);
				movEntr.setCostoSalida(pp.getPrecioCosto());

				movEntr.setVenta_1(pp.getPrecioVenta_1());
				movEntr.setVenta_2(pp.getPrecioVenta_2());
				movEntr.setVenta_3(pp.getPrecioVenta_3());
				movEntr.setVenta_4(pp.getPrecioVenta_4());

				movEntr.setVenta_1_anterior(0.0);
				movEntr.setVenta_2_anterior(0.0);
				movEntr.setVenta_3_anterior(0.0);
				movEntr.setVenta_4_anterior(0.0);

				movEntr.getTipoMovimiento().setId(1);
				movEntr.getProducto().setId(pp.getId());
				movEntr.getFuncionario().setId(idfuncio);
				movEntr.setMarca(pp.getMarca().getDescripcion());
				Concepto ccc= new Concepto();
				ccc= conceptoRepository.findById(8).get();
				movEntr.setReferencia(ccc.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movEntr);
				
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					entityRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());
					
					Producto prod = entityRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//					, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
					
					mov.setDescripcion(prod.getDescripcion());
					mov.setCantidad(existenciaActual);
					mov.setFecha(new  Date());
					mov.setHora(hora());
					mov.setVentaSalida(0.0);

					mov.setEgreso(0.0);
					mov.setVentaSalida(0.0);

					mov.setCostoEntrada(prod.getPrecioCosto());
					mov.setCostoEntradaAnterior(0.0);
					mov.setCostoSalida(prod.getPrecioCosto());

					mov.setVenta_1(prod.getPrecioVenta_1());
					mov.setVenta_2(prod.getPrecioVenta_2());
					mov.setVenta_3(prod.getPrecioVenta_3());
					mov.setVenta_4(prod.getPrecioVenta_4());

					mov.setVenta_1_anterior(0.0);
					mov.setVenta_2_anterior(0.0);
					mov.setVenta_3_anterior(0.0);
					mov.setVenta_4_anterior(0.0);

					mov.getTipoMovimiento().setId(1);
					mov.getProducto().setId(prod.getId());
					mov.getFuncionario().setId(idfuncio);
					mov.setMarca(prod.getMarca().getDescripcion());
					Concepto conn= new Concepto();
					conn= conceptoRepository.findById(8).get();
					mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
					
					movEntradaSalidaRepository.save(mov);
					
					
										
				}
			}else {
				System.out.println("Producto unitario");
				
				entityRepository.findByActualizaA(cantidad, id);
				Producto p = entityRepository.getOne(id);
				MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
				//System.out.println(p.getDescripcion()+" costo: "+p.getPrecioCosto()+ " venta 1"+ p.getPrecioVenta_1()+" venta 1: "+p.getPrecioVenta_2()+ " marca: "+p.getMarca().getDescripcion());
//				, double subtotal, double precio, int idFuncionario, String tipo, int idVenta
				
				mov.setDescripcion(p.getDescripcion());
				mov.setCantidad(cantidad);
				mov.setFecha(new  Date());
				mov.setHora(hora());
				mov.setVentaSalida(0.0);
				mov.setEgreso(0.0);
				mov.setVentaSalida(0.0);

				mov.setCostoEntrada(p.getPrecioCosto());
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
				mov.getFuncionario().setId(idfuncio);
				mov.setMarca(p.getMarca().getDescripcion());
				Concepto conn= new Concepto();
				conn= conceptoRepository.findById(8).get();
				mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(mov);
			
				
			}
			
		}
	}
	public void actualizarProductoBaseDescontar(int id , double cantidad,  int idfuncio, int idCab) {
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			System.out.println("tiene compuesto y actualiza base unica : ");
			double cant=0.0;
			cant= cantidad * ca.getCantidadAplicacion();
			entityRepository.findByActualizaD(cant, ca.getProductoBase().getId());
			Producto p = entityRepository.getOne(ca.getProductoBase().getId());
			MovimientoEntradaSalida mov = new MovimientoEntradaSalida();

			mov.setDescripcion(p.getDescripcion());
			mov.setCantidad(cant);
			mov.setFecha(new  Date());
			mov.setHora(hora());
			mov.setVentaSalida(0.0);
			mov.setEgreso(0.0);
			mov.setVentaSalida(0.0);

			mov.setCostoEntrada(p.getPrecioCosto());
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

			mov.getTipoMovimiento().setId(2);
			mov.getProducto().setId(p.getId());
			mov.getFuncionario().setId(idfuncio);
			mov.setMarca(p.getMarca().getDescripcion());
			Concepto conn= new Concepto();
			conn= conceptoRepository.findById(8).get();
			mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
			
			movEntradaSalidaRepository.save(mov);
			
			//venta tipo, subtotl, precio, funcionario id, tipo, idVenta
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				System.out.println("tiene compuesto y actualiza compuesto varios : ");

				Double existenciaActual=0.0;
				existenciaActual=  (cantidad * ca.getCantidadAplicacion() )/ob.getCantidadAplicacion();
				entityRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
				Producto pro = entityRepository.getOne(ob.getProductoCompuesto().getId());
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(existenciaActual);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);

				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());

				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movv);
			}
		}else {
			System.out.println("venta - entrooo else no tiene compusto el id: "+id);
			ProductoCardex pBase = compuestoRepository.getProductoPorIdBase(id);
			if(pBase != null) {
				System.out.println("venta - Producto relacio0nado con un base");
				entityRepository.findByActualizaD(cantidad, id);
				Producto pro = entityRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();

				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);

				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());

				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movv);
				
				
				
				List<ProductoCardex> list = compuestoRepository.getBase(id);
				for(ProductoCardex ob: list) {
					System.out.println("venta - producto base relacion");
					Double existenciaActual=0.0;
					existenciaActual= cantidad / ob.getCantidadAplicacion();
					entityRepository.findByActualizaD(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
					Producto p = entityRepository.getOne(ob.getProductoCompuesto().getId());
					MovimientoEntradaSalida mov = new MovimientoEntradaSalida();
					
					mov.setDescripcion(p.getDescripcion());
					mov.setCantidad(existenciaActual);
					mov.setFecha(new  Date());
					mov.setHora(hora());
					mov.setVentaSalida(0.0);
					mov.setEgreso(0.0);
					mov.setVentaSalida(0.0);

					mov.setCostoEntrada(p.getPrecioCosto());
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

					mov.getTipoMovimiento().setId(2);
					mov.getProducto().setId(p.getId());
					mov.getFuncionario().setId(idfuncio);
					mov.setMarca(p.getMarca().getDescripcion());
					Concepto conn= new Concepto();
					conn= conceptoRepository.findById(8).get();
					mov.setReferencia(conn.getDescripcion()+" REF.: "+ idCab);
					
					movEntradaSalidaRepository.save(mov);
					
				}
			}else {
				System.out.println("venta - Producto unitario");
				entityRepository.findByActualizaD(cantidad, id);
				Producto pro = entityRepository.getOne(id);
				MovimientoEntradaSalida movv = new MovimientoEntradaSalida();
				movv.setDescripcion(pro.getDescripcion());
				movv.setCantidad(cantidad);
				movv.setFecha(new  Date());
				movv.setHora(hora());
				movv.setVentaSalida(0.0);
				movv.setEgreso(0.0);
				movv.setVentaSalida(0.0);

				movv.setCostoEntrada(pro.getPrecioCosto());
				movv.setCostoEntradaAnterior(0.0);
				movv.setCostoSalida(pro.getPrecioCosto());

				movv.setVenta_1(pro.getPrecioVenta_1());
				movv.setVenta_2(pro.getPrecioVenta_2());
				movv.setVenta_3(pro.getPrecioVenta_3());
				movv.setVenta_4(pro.getPrecioVenta_4());

				movv.setVenta_1_anterior(0.0);
				movv.setVenta_2_anterior(0.0);
				movv.setVenta_3_anterior(0.0);
				movv.setVenta_4_anterior(0.0);

				movv.getTipoMovimiento().setId(2);
				movv.getProducto().setId(pro.getId());
				movv.getFuncionario().setId(idfuncio);
				movv.setMarca(pro.getMarca().getDescripcion());
				Concepto cn= new Concepto();
				cn= conceptoRepository.findById(8).get();
				movv.setReferencia(cn.getDescripcion()+" REF.: "+ idCab);
				
				movEntradaSalidaRepository.save(movv);
			}
			
		}
	}
	public String hora() {
		return new SimpleDateFormat("HH:mm:ss a", Locale.US).format(new Date());
	}
	@RequestMapping(method=RequestMethod.POST, value="/ajuste")
	public void actualizarStock(@RequestBody Producto entity, OAuth2Authentication authentication){
		AjusteInventario ajuste=new AjusteInventario();
		ajuste.setTipo(entity.getUnidadMedida().getId()+"");
		ajuste.getProducto().setId(entity.getId());
		ajuste.setCantidad(entity.getExistencia());
		ajuste.setMotivo(entity.getCodbar());

		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		ajuste.getFuncionario().setId(usuario.getFuncionario().getId());
		ajusteInventarioRepository.save(ajuste);
		if (entity.getUnidadMedida().getId()==1) {
			
			//entityRepository.findByActualizaA(entity.getExistencia(), entity.getId());
			this.actualizarProductoBaseAumentar(entity.getId(), entity.getExistencia(), usuario.getFuncionario().getId(), ajusteInventarioRepository.getAjusteUlt().getId());
		} 
		if (entity.getUnidadMedida().getId()==2) {
			
			this.actualizarProductoBaseDescontar(entity.getId(), entity.getExistencia(), usuario.getFuncionario().getId(), ajusteInventarioRepository.getAjusteUlt().getId());
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="totalproducto")
	public Object[] getAllTotales(){
		return entityRepository.findByProducto();
	}
	@RequestMapping(method=RequestMethod.GET, value="stockBajo")
	public Object[] getStockBajo(){
		return entityRepository.findByStockBajo();
	}
	@RequestMapping(method=RequestMethod.GET, value="/productogrupo")
	public List<Object[]> getProductoGrupo(){
		return entityRepository.findByProductoGrupo();
	}

	@RequestMapping(method=RequestMethod.GET, value="/ajusteInventario/{fecha}")
	public List<AjusteInventario> getAjusteInventario(@PathVariable String fecha){
		String[] fec=fecha.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		List<Object[]> objeto=ajusteInventarioRepository.listaAjusteInventario(dia, mes, ano);
		List<AjusteInventario> ajuste=new ArrayList<>();
		for(Object[] ob:objeto){
			AjusteInventario ajustes=new AjusteInventario();
			ajustes.setId(Integer.parseInt(ob[0].toString()));
			ajustes.getFuncionario().getPersona().setNombre(ob[1].toString());
			ajustes.getFuncionario().getPersona().setApellido(ob[2].toString());
			ajustes.getProducto().setDescripcion(ob[3].toString());
			ajustes.setCantidad(Double.parseDouble(ob[4].toString()));
			ajustes.setTipo(ob[5].toString());
			String fech=ob[6].toString();
			ajustes.setFecha(FechaUtil.convertirFechaStringADateUtil(fech));
			ajustes.setMotivo(ob[7].toString());
			ajuste.add(ajustes);
		}
		return ajuste;
	}
	/*
	@RequestMapping(method=RequestMethod.GET, value="/paginacion")
	public List<Producto> obtenerPaginacion(Pageable pageable){
		return entityRepository.findAll(pageable).getContent();
	}
	 */

	@RequestMapping(method=RequestMethod.GET, value = "/precio/{id}")
	public Object getPrecioDetalle(@PathVariable int id){
		Producto pro=entityRepository.findById(id).get();
		Object[] ob=new Object[4];
		ob[0]= pro.getPrecioVenta_1();
		ob[1]= pro.getPrecioVenta_2();
		ob[2]= pro.getPrecioVenta_3();
		ob[3]= pro.getPrecioVenta_4();
		return ob;

	}


	@RequestMapping(method=RequestMethod.GET, value="/utilidadPrecio")
	public UtilidadPrecio getUtilidadPrecio() {
		UtilidadPrecio utilidad=utilidadPrecioRepository.findById(1).get();
		if(utilidad==null){
			UtilidadPrecio util=new UtilidadPrecio();
			util.setPrecioVenta1(0);
			util.setPrecioVenta2(0);
			util.setPrecioVenta3(0);
			util.setPrecioVenta4(0);
			return util;
		}
		return utilidad;
	}

	@RequestMapping(method=RequestMethod.POST, value="/utilidadPrecio")
	public UtilidadPrecio saveUtilidadPrecio(@RequestBody UtilidadPrecio entity) {
		return utilidadPrecioRepository.save(entity);
	}

	@RequestMapping(method=RequestMethod.POST, value="/gestionPrecio")
	public void gestionPrecioController(@RequestBody Producto entity) {
		entityRepository.gestionPrecio(entity.getPrecioCosto(), entity.getPrecioVenta_1(), entity.getPrecioVenta_2(), entity.getPrecioVenta_3(), entity.getPrecioVenta_4(), entity.getId());
	}

	@RequestMapping(method=RequestMethod.POST, value="/actualizar")
	public void actualizarIsBalanza(@RequestBody List<Producto> entity) {
		for(Producto en:entity) {
			entityRepository.findByActualizarBalanza(true, en.getId());			
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/proveedorId/{descripcion}/{idProveedor}")
	public List<Producto> getProductoIdProveedor(@PathVariable String descripcion, @PathVariable int idProveedor){
		if (descripcion.equals("9999999999")) {
			return product(entityRepository.getProductoIdProveedor(idProveedor));			
		} else {
			return product(entityRepository.getBuscarPorDescripcionIdProveedor("%"+Utilidades.eliminaCaracterIzqDer(descripcion.toUpperCase())+"%", idProveedor));	
		}
	}

	@RequestMapping(method=RequestMethod.GET, value="/ejecutarLimpieza")
	public void ejecutarLimpieza() {
		List<Producto> lis =  entityRepository.findAll();
		for(Producto en:lis) {
			en.setDescripcion(Utilidades.eliminaCaracterIzqDer(en.getDescripcion()));
			en.setAplicacion(Utilidades.eliminaCaracterIzqDer(en.getAplicacion()));
			en.setCodoriginal(Utilidades.eliminaCaracterIzqDer(en.getCodoriginal()));
			en.setFabricante(Utilidades.eliminaCaracterIzqDer(en.getFabricante()));

			entityRepository.save(en);		
		}
	}


	@RequestMapping(method=RequestMethod.GET, value="/actualizarProductoCompuesto/{id}/{cantidad}")
	public void actualizarProductoCompuesto(@PathVariable int id , @PathVariable double cantidad) {

		List<ProductoCardex> list = compuestoRepository.getBase(id);
		entityRepository.findByActualizaA(cantidad, id);//actualiza producto base
		for(ProductoCardex ob: list) {
			Double existenciaActual=0.0;
			existenciaActual= cantidad / ob.getCantidadAplicacion();
			entityRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
		}

	}
	@RequestMapping(method=RequestMethod.GET, value="/actualizarProductoBase/{id}/{cantidad}")
	public void actualizarProductoBase(@PathVariable int id , @PathVariable double cantidad) {
		//entityRepository.findByActualizaA(cantidad, id);//actualiza producto base
		ProductoCardex ca = compuestoRepository.getProductoPorIdCompuesto(id);
		if(ca!=null) {
			double existenciaBase=0.0;
			existenciaBase= cantidad * ca.getCantidadAplicacion();
			entityRepository.findByActualizaA(existenciaBase, ca.getProductoBase().getId());
			List<ProductoCardex> list = compuestoRepository.getBase(ca.getProductoBase().getId());
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual=  ca.getCantidadAplicacion()/(cantidad *  ob.getCantidadAplicacion());
				entityRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}else {
			List<ProductoCardex> list = compuestoRepository.getBase(id);
			entityRepository.findByActualizaA(cantidad, id);//actualiza producto base
			for(ProductoCardex ob: list) {
				Double existenciaActual=0.0;
				existenciaActual= cantidad / ob.getCantidadAplicacion();
				entityRepository.findByActualizaA(existenciaActual, ob.getProductoCompuesto().getId());// actualiza pro compuesto
			}
		}


	}


	public List<Producto> cargarInventarioGeneral(List<Object[]> list){
		List<Producto> listaRetrono= new ArrayList<Producto>();
		for(Object [] ob: list) {
			Producto pro = new Producto();
			pro.setDescripcion(ob[0].toString()+"/"+ob[4].toString());
			pro.setExistencia(Double.parseDouble(ob[1].toString()));
			pro.setPrecioCosto(Double.parseDouble(ob[2].toString()));
			pro.setPrecioVenta_1(Double.parseDouble(ob[3].toString()));
			listaRetrono.add(pro);
		}
		return listaRetrono;
	}
	@RequestMapping(method=RequestMethod.GET, value="/inventarioGeneral/{tipo}/{id}")
	public List<Producto> getInventarioGeneral(@PathVariable int tipo , @PathVariable int id) {
		List<Producto> listaRetorno= new ArrayList<Producto>();
		if (tipo==1) {
			listaRetorno= cargarInventarioGeneral(entityRepository.getInventarioGeneralTodo());
		}
		if (tipo==2 && id > 0) {
			listaRetorno= cargarInventarioGeneral(entityRepository.getInventarioGeneralPorMarca(id));
		}
		if (tipo==3 && id > 0) {
			listaRetorno= cargarInventarioGeneral(entityRepository.getInventarioGeneralPorGrupo(id));
		}
		if (tipo==4 && id > 0) {
			listaRetorno= cargarInventarioGeneral(entityRepository.getInventarioGeneralPorProveedor(id));
		}
		return listaRetorno;
	}
	
	private List<ProductoAuxiliar> getLista(List<Object[]> obj) {
		List<ProductoAuxiliar> listado = new ArrayList<>();
		for(Object[] o: obj) {
			ProductoAuxiliar p=new ProductoAuxiliar();
			p.setDescripcion(o[0].toString());
			p.setExistencia(Double.parseDouble(o[1].toString()));
			p.setPrecioCosto(Double.parseDouble(o[2].toString()));
			p.setPrecioVenta_1(Double.parseDouble(o[3].toString()));
			p.setPrecioVenta_2(Double.parseDouble(o[4].toString()));
			p.setPrecioVenta_3(Double.parseDouble(o[5].toString()));
			p.setPrecioVenta_4(Double.parseDouble(o[6].toString()));
			p.setTotalPrecioCosto(Double.parseDouble(o[7].toString()));
			p.setTotalPrecioVenta_1(Double.parseDouble(o[8].toString()));
			p.setTotalPrecioVenta_2(Double.parseDouble(o[9].toString()));
			p.setTotalPrecioVenta_3(Double.parseDouble(o[10].toString()));
			p.setTotalPrecioVenta_4(Double.parseDouble(o[11].toString()));
			p.setCodbar(o[12].toString());
			p.setInterno(Integer.parseInt(o[13].toString()));
			listado.add(p);
		}
		return listado;
	}
	public Date sumarDia(Date fecha, int hora) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.HOUR, hora);
		return calendar.getTime();
	}
	
	
		
	
	@RequestMapping(value="/inventarioGeneralPDF/{idMarca}/{idGrupo}/{idSubGrupo}", method=RequestMethod.GET)
	public ResponseEntity<?>  generarPdfInventarioGeneral(HttpServletResponse response, OAuth2Authentication authentication, @PathVariable int idMarca, @PathVariable int idGrupo, @PathVariable int idSubGrupo) throws IOException {
		List<ProductoAuxiliar> lista = new ArrayList<>();
		String filtroMarca="";
		String filtroGrupo="";
		String filtroSubGrupo="";
		if (idMarca == 0 && idGrupo == 0 && idSubGrupo == 0) {
			filtroMarca="TODOS";
			filtroGrupo="TODOS";
			filtroSubGrupo="TODOS";
			lista = getLista(entityRepository.getProductoAll());
		}
		
		if (idMarca != 0 && idGrupo == 0 && idSubGrupo == 0) {
			filtroMarca=marcaRepository.findById(idMarca).get().getDescripcion();
			filtroGrupo="TODOS";
			filtroSubGrupo="TODOS";
			lista = getLista(entityRepository.getProductoMarca(idMarca));
		}
		
		if (idMarca == 0 && idGrupo != 0 && idSubGrupo == 0) {
			filtroMarca="TODOS";
			filtroGrupo=grupoRepository.findById(idGrupo).get().getDescripcion();
			filtroSubGrupo="TODOS";
			lista = getLista(entityRepository.getProductoGrupo(idGrupo));
		}
		
		if (idMarca == 0 && idGrupo == 0 && idSubGrupo != 0) {
			filtroMarca="TODOS";
			filtroGrupo="TODOS";
			filtroSubGrupo=subGrupoRepository.findById(idSubGrupo).get().getDescripcion();
			
			lista = getLista(entityRepository.getProductoSubGrupo(idSubGrupo));
		}
		
		if (idMarca != 0 && idGrupo != 0 && idSubGrupo == 0) {
			filtroMarca=marcaRepository.findById(idMarca).get().getDescripcion();
			filtroGrupo=grupoRepository.findById(idGrupo).get().getDescripcion();
			filtroSubGrupo="TODOS";
			lista = getLista(entityRepository.getProductoMarcaGrupo(idMarca, idGrupo));
		}
		
		if (idMarca == 0 && idGrupo != 0 && idSubGrupo != 0) {
			filtroMarca="TODOS";
			filtroGrupo=grupoRepository.findById(idGrupo).get().getDescripcion();
			filtroSubGrupo=subGrupoRepository.findById(idSubGrupo).get().getDescripcion();
			lista = getLista(entityRepository.getProductoGrupoSubGrupo(idGrupo, idSubGrupo));
		}
		
		if (idMarca != 0 && idGrupo != 0 && idSubGrupo != 0) {
			filtroMarca=marcaRepository.findById(idMarca).get().getDescripcion();
			filtroGrupo=grupoRepository.findById(idGrupo).get().getDescripcion();
			filtroSubGrupo=subGrupoRepository.findById(idSubGrupo).get().getDescripcion();
			
			lista = getLista(entityRepository.getProductoMarcaGrupoSubGrupo(idMarca, idGrupo, idSubGrupo));
		}
		
		if (idMarca != 0 && idGrupo == 0 && idSubGrupo != 0) {
			filtroMarca=marcaRepository.findById(idMarca).get().getDescripcion();
			filtroGrupo="TODOS";
			filtroSubGrupo=subGrupoRepository.findById(idSubGrupo).get().getDescripcion();
			lista = getLista(entityRepository.getProductoMarcaSubGrupo(idMarca, idSubGrupo));
		}
		if(lista.size()<1) {
			return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar!!"), HttpStatus.CONFLICT);
		}
		double totalCosto=0.0, totalVenta1=0.0, totalVenta2=0.0, totalVenta3=0.0, totalVenta4=0.0;
		for (ProductoAuxiliar p: lista) {
//			System.out.println(" costoosto: "+p.getPrecioCosto());
//			System.out.println(" total costo: "+p.getExistencia());
		}
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			Org org = orgRepository.findById(1).get();
			
			
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			map.put("marca", ""+filtroMarca);
			map.put("grupo", ""+filtroGrupo);
			map.put("subGrupo", ""+filtroSubGrupo);
			
			
			report = new Reporte();
			report.reportPDFDescarga(lista, map, "ReporteInventarioGeneral", response);
			return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	@RequestMapping(value="/reporteStockNegativo", method=RequestMethod.GET)
	public ResponseEntity<?>  generarPdfStockNegativo(HttpServletResponse response, OAuth2Authentication authentication) throws IOException {
		List<ProductoAuxiliar> lista = new ArrayList<>();
		lista = getLista(entityRepository.getProductoStockNegativo());
		
		if(lista.size()<1) {
			return new ResponseEntity<>(new CustomerErrorType("No hay lista para mostrar!!"), HttpStatus.CONFLICT);
		}
		double totalCosto=0.0, totalVenta1=0.0, totalVenta2=0.0, totalVenta3=0.0, totalVenta4=0.0;
		for (ProductoAuxiliar p: lista) {
//			System.out.println(" costoosto: "+p.getPrecioCosto());
//			System.out.println(" total costo: "+p.getExistencia());
		}
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			Org org = orgRepository.findById(1).get();
			
			
			Map<String, Object> map = new HashMap<>();
			map.put("org", ""+org.getNombre());
			map.put("direccion", ""+org.getDireccion());
			map.put("ruc", ""+org.getRuc());
			map.put("telefono", ""+org.getTelefono());
			map.put("ciudad", ""+org.getCiudad());
			map.put("pais", ""+org.getPais());
			map.put("funcionario", ""+usuario.getFuncionario().getPersona().getNombre()+" "+usuario.getFuncionario().getPersona().getApellido());
			
			
			
			report = new Reporte();
			report.reportPDFDescarga(lista, map, "ReporteStockNegativo", response);
			return  new  ResponseEntity<String>(HttpStatus.OK);
	}
	
	private void verificarProductoCompuesto(List<Producto> listado) {
		List<ProductoCardex> litCompuesto = compuestoRepository.findAll();
		for (ProductoCardex p: litCompuesto) {
			for (Producto prod: listado) {
				
			}
			
		}
	}
	
	@RequestMapping(value="/generarCodigoBarra", method=RequestMethod.POST)
	public void generarCodigoBarra(@RequestBody List<Producto> producto) throws IOException, JRException {
		for(Producto p: producto) {
		//	System.out.println(p.getCodbar());
		}
		
		report = new Reporte();
		report.reportPDFImprimir(producto, null, "Blank_A4", "Microsoft Print to PDF");			
	
	}
	
	@RequestMapping(value="/generarCodigoBarra1", method=RequestMethod.POST)
	public void generarCodigoBarra1(@RequestBody List<Producto> producto) throws IOException, JRException {
		
		List<Producto> listAuxiliar= new ArrayList<>();

		int c=0;
		while (c < producto.size()) {
			if (c==3) {
//				System.out.println("ejecuto primera consultar");
//				System.out.println("imprimir : "+listAuxiliar.get(c-1));
				c=0;
				listAuxiliar.clear();
				
			}else {
				listAuxiliar.add(producto.get(c));
//				System.out.println(c+" contador");
				c++;
			}
			
		}
		
		
	}
	
	@RequestMapping(value="/inventarioReporte", method=RequestMethod.GET)
	public Map<String, String> getInventarioReporte() throws IOException, JRException {
		Map<String, String> map = new HashMap<>();
		List<Object[]> obj = entityRepository.getInventarioAll();
		for(Object[] ob: obj) {
			InventarioReporteModelAux v = new InventarioReporteModelAux();
			v.setDescripcion(ob[0].toString());
			v.setCantidad(Double.parseDouble(ob[0].toString()));
			v.setPrecioCosto(Double.parseDouble(ob[0].toString()));
		}
		
		return map;
	}
	


}

