package com.bisontecfacturacion.security.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Producto {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@NotNull
	private String descripcion;

	private String codbar;
	private String codoriginal;
	private String fabricante;
	private Double existencia;
	private Double stock_minimo;
	@NotNull
	private String iva;
	private Double precioCosto;
	private Double precioVenta_1;
	private Double precioVenta_2;
	private Double precioVenta_3;
	private Double precioVenta_4;
	private String aplicacion;
	private Boolean habilitado;
	private Boolean isBalanza;
	private double volumen;
	private String nombreImagen;
	
	private Boolean estadoCompuesto;
	
	private Date fechaVencimiento;
	
	@ManyToOne
	private UnidadMedida unidadMedida;
	@ManyToOne
	private SubGrupo subGrupo;
	@ManyToOne
	private Grupo grupo;
	@ManyToOne
	private Deposito deposito;
	@ManyToOne
	private Marca marca;
	@ManyToOne
	private Proveedor proveedor;
	

	@OneToMany(mappedBy="producto")
	@JsonBackReference
	private List<AjusteInventario> ajusteInventario;
	
	
	@Lob
	private  byte[] foto;	
	
	public Producto() {
		this.codbar ="";
		this.id=0;
		this.existencia=0.0;
		this.stock_minimo=0.0;
		this.iva="";
		this.precioCosto=0.0;
		this.precioVenta_1=0.0;
		this.precioVenta_2=0.0;
		this.precioVenta_3=0.0;
		this.precioVenta_4=0.0;
		this.habilitado=true;
		this.unidadMedida=new UnidadMedida();
		this.subGrupo=new SubGrupo();
		this.grupo=new Grupo();
		this.deposito=new Deposito();
		this.marca=new Marca();
		this.isBalanza = false;
		this.proveedor= new Proveedor();
		this.codoriginal="";
		this.fabricante="";
		this.estadoCompuesto=true;
//		this.nombreImagen = "";
	
	}

	public Boolean getEstadoCompuesto() {
		return estadoCompuesto;
	}

	public void setEstadoCompuesto(Boolean estadoCompuesto) {
		this.estadoCompuesto = estadoCompuesto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodbar() {
		return codbar;
	}

	public void setCodbar(String codbar) {
		this.codbar = codbar;
	}

	public String getCodoriginal() {
		return codoriginal;
	}

	public void setCodoriginal(String codoriginal) {
		this.codoriginal = codoriginal;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Double getExistencia() {
		return existencia; 
	}

	public void setExistencia(Double existencia) {
		this.existencia = existencia;
	}

	public Double getStock_minimo() {
		return stock_minimo;
	}

	public void setStock_minimo(Double stock_minimo) {
		this.stock_minimo = stock_minimo;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public Double getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(Double precioCosto) {
		this.precioCosto = precioCosto;
	}

	public Double getPrecioVenta_1() {
		return precioVenta_1;
	}

	public void setPrecioVenta_1(Double precioVenta_1) {
		this.precioVenta_1 = precioVenta_1;
	}

	public Double getPrecioVenta_2() {
		return precioVenta_2;
	}

	public void setPrecioVenta_2(Double precioVenta_2) {
		this.precioVenta_2 = precioVenta_2;
	}

	public Double getPrecioVenta_3() {
		return precioVenta_3;
	}

	public void setPrecioVenta_3(Double precioVenta_3) {
		this.precioVenta_3 = precioVenta_3;
	}

	public Double getPrecioVenta_4() {
		return precioVenta_4;
	}

	public void setPrecioVenta_4(Double precioVenta_4) {
		this.precioVenta_4 = precioVenta_4;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}





	public double getVolumen() {
		return volumen;
	}

	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}

	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public SubGrupo getSubGrupo() {
		return subGrupo;
	}

	public void setSubGrupo(SubGrupo subGrupo) {
		this.subGrupo = subGrupo;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public List<AjusteInventario> getAjusteInventario() {
		return ajusteInventario;
	}

	public void setAjusteInventario(List<AjusteInventario> ajusteInventario) {
		this.ajusteInventario = ajusteInventario;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Boolean getIsBalanza() {
		return isBalanza;
	}

	public void setIsBalanza(Boolean isBalanza) {
		this.isBalanza = isBalanza;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}


	

}
