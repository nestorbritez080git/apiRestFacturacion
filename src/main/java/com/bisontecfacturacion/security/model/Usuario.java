package com.bisontecfacturacion.security.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "users")
public class Usuario implements Serializable {

    @Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
    //@Column(name = "ID")
    private Long id;

   // @Column(unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

   // @Column(length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private Boolean enabled;
    
    private Boolean administrador;

    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Role> roles;
    
    @ManyToOne
   	private Funcionario funcionario;
       
       public Usuario() {
    	   this.administrador = false;
   			funcionario = new Funcionario();
       	
   	}

    
    /**
     *
     */
    private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Boolean getEnabled() {
		return enabled;
	}


	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


	public List<Role> getRoles() {
		return roles;
	}


	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	public Funcionario getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Boolean getAdministrador() {
		return administrador;
	}


	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	
 

}