package com.bisontecfacturacion.security.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(
    name = "persona",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "cedula")
    }
)
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
    
    @NotNull
    @Column(nullable = false)
    private String nombre;

    private String apellido;
    private String direccion;
    private String telefono;

    @Column(nullable = false, unique = true)
    private String cedula;

    private String email;

    @NotNull
    @Column(nullable = false)
    private String tipo;

    @OneToMany(mappedBy = "persona")
    @JsonBackReference
    private List<Funcionario> funcionario;

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "persona"
    )
    private Proveedor proveedor;

    public Persona() {
    	this.id=0;
    	this.nombre="";
    	this.apellido="";
    	this.direccion="";
    	this.telefono="";
    	this.cedula="";
    	this.email="";
    	this.tipo="FISICA";
    	
        // ❌ NO crear valores por defecto que generen personas vacías
    }

    // ===== getters y setters =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}