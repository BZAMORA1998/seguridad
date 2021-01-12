package com.sistema.ventas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity implementation class for Entity: Empresas
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_datos_empresa")
public class DatosEmpresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    @EqualsAndHashCode.Include
    private  DatosEmpresaCPK datosEmpresaCPK;
	
	@Column(name = "descripcion")
    private String descripcion;
	
	@Column(name = "es_activo")
    private String esActivo;
}
