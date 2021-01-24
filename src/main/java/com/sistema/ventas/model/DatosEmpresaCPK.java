package com.sistema.ventas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DatosEmpresaCPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull
    @Column(name = "secuencia_empresa")
    private Integer secuenciaEmpresa;
    
	@NotNull
    @Column(name = "nemonico")
    private String nemonico;
	
    @NotNull
    @Column(name = "nombre")
    private String nombre;
}
