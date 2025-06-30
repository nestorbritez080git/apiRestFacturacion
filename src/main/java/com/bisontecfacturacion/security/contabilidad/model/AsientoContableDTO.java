package com.bisontecfacturacion.security.contabilidad.model;

import java.math.BigDecimal;
import java.util.Map;

public class AsientoContableDTO {
	private Integer conceptoId;
    private Integer referenciaId;
    private String tipoReferencia;
    private Integer funcionarioRegistroId;
    private Integer funcionarioModificacionId;
    private Map<String, BigDecimal> montos;
    // Constructores
    public AsientoContableDTO() {
    	
    }

   
    // Getters y Setters
   

    public AsientoContableDTO(Integer conceptoId, Integer referenciaId, String descripcion,
			Integer funcionarioRegistroId, Integer funcionarioModificacionId, Map<String, BigDecimal> montos) {
		super();
		this.conceptoId = conceptoId;
		this.referenciaId = referenciaId;
		this.tipoReferencia = descripcion;
		this.funcionarioRegistroId = funcionarioRegistroId;
		this.funcionarioModificacionId = funcionarioModificacionId;
		this.montos = montos;
	}


	public Integer getFuncionarioRegistroId() {
		return funcionarioRegistroId;
	}


	public void setFuncionarioRegistroId(Integer funcionarioRegistroId) {
		this.funcionarioRegistroId = funcionarioRegistroId;
	}


	public Integer getFuncionarioModificacionId() {
		return funcionarioModificacionId;
	}


	public void setFuncionarioModificacionId(Integer funcionarioModificacionId) {
		this.funcionarioModificacionId = funcionarioModificacionId;
	}


    public Integer getConceptoId() {
		return conceptoId;
	}

	public void setConceptoId(Integer conceptoId) {
		this.conceptoId = conceptoId;
	}

	public Integer getReferenciaId() {
		return referenciaId;
	}

	public void setReferenciaId(Integer referenciaId) {
		this.referenciaId = referenciaId;
	}

    public Map<String, BigDecimal> getMontos() {
        return montos;
    }

    public void setMontos(Map<String, BigDecimal> montos) {
        this.montos = montos;
    }


	public String getTipoReferencia() {
		return tipoReferencia;
	}


	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}
    
    
    
}
