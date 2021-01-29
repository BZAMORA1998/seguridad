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

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_opciones_menu")
public class OpcionesMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    @EqualsAndHashCode.Include
    private  OpcionesMenuCPK opcionesMenuCPK;
	
	@Column(name = "descripcion")
	private String descripcion;
	 
    @Column(name = "url")
    private String url;
    
    @Column(name = "es_activo")
    private String esActivo;
    
    @Column(name = "orden")
    private Integer orden;

}
