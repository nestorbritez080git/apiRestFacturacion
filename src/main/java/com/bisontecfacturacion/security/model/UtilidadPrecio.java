package com.bisontecfacturacion.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class UtilidadPrecio {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
    private double precioVenta1;
    private double precioVenta2;
    private double precioVenta3;
    private double precioVenta4;

    public UtilidadPrecio() {
        this.precioVenta1 = 0.0;
        this.precioVenta2 = 0.0;
        this.precioVenta3 = 0.0;
        this.precioVenta4 = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecioVenta1() {
        return precioVenta1;
    }

    public void setPrecioVenta1(double precioVenta1) {
        this.precioVenta1 = precioVenta1;
    }

    public double getPrecioVenta2() {
        return precioVenta2;
    }

    public void setPrecioVenta2(double precioVenta2) {
        this.precioVenta2 = precioVenta2;
    }

    public double getPrecioVenta3() {
        return precioVenta3;
    }

    public void setPrecioVenta3(double precioVenta3) {
        this.precioVenta3 = precioVenta3;
    }

    public double getPrecioVenta4() {
        return precioVenta4;
    }

    public void setPrecioVenta4(double precioVenta4) {
        this.precioVenta4 = precioVenta4;
    }
    
    

}
