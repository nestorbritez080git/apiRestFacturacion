package com.bisontecfacturacion.security.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisontecfacturacion.security.model.Org;

public interface OrgRepository extends JpaRepository<Org, Serializable>{
	
	public abstract Org findTop1ByOrderByIdDesc();

}

