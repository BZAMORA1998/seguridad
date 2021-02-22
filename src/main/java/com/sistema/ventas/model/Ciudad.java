package com.sistema.ventas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;

public class Ciudad implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@EqualsAndHashCode.Include
    private CiudadCPK ciudadCPK;
	
	@Size(max=50)
	@Column(name = "nombre")
	private String nombre;
	
	@Size(max=50)
	@Column(name = "es_activo")
	private String esActivo;

}
