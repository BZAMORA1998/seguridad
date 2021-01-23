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
@Table(name = "tbl_fotos")
public class Fotos implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    @EqualsAndHashCode.Include
    private  FotosCPK fotosCPK;
	
    @Column(name = "foto")
	private byte[] foto;
    
    @Column(name = "es_activo")
   	private String esActivo;
    
}
