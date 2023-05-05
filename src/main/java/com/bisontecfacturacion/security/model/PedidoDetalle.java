package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class PedidoDetalle{
    @Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
    private int id;
    @NotNull
    private String descripcion;
    @NotNull
    private Double cantidad;
    @NotNull
    private Double precio;
    @NotNull
    private Double subTotal;
    @NotNull
    private String iva;
    @ManyToOne
    private Producto producto;
    @ManyToOne
    private Pedido pedido;
    private String tipoPrecio;
    private double volumenAcumulado;
    
    public PedidoDetalle(){
        this.id=0;
        this.descripcion="";
        this.cantidad=0.0;
        this.precio=0.0;
        this.subTotal=0.0;
        this.iva="";
        this.volumenAcumulado=0.0;
        this.producto = new Producto();
        this.pedido = new Pedido();
        this.tipoPrecio="";
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

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public double getVolumenAcumulado() {
        return volumenAcumulado;
    }

    public void setVolumenAcumulado(double volumenAcumulado) {
        this.volumenAcumulado = volumenAcumulado;
    }


    
}