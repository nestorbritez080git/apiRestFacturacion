package com.bisontecfacturacion.security.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bisontecfacturacion.security.model.ReporteConfig;

public interface ReporteConfigRepository  extends JpaRepository<ReporteConfig, Serializable>{

	 public abstract List<ReporteConfig> findByOrderByIdAsc();
}
