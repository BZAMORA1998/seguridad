package com.sistema.ventas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

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
public class RutasXRolesCPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @NotNull
    @Column(name = "secuencia_rol")
    private Integer secuenciaRol;
    
    @NotNull
    @Column(name = "secuencia_ruta")
    private Integer secuenciaRuta;


}
