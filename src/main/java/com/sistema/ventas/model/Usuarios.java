package com.sistema.ventas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tbl_usuarios")
public class Usuarios  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "SECUENCIA_USUARIO")
    private Integer secuenciaUsuario;
	
	 @Column(name = "usuario")
	 private String usuario;
		
	 @Column(name = "contrasenia")
	 private String contrasenia;
	 
	 @Column(name = "es_activo")
	 private String esActivo;
	 
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "SECUENCIA_PERSONA", referencedColumnName = "SECUENCIA_PERSONA")
	private Personas personas;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "SECUENCIA_ROL", referencedColumnName = "SECUENCIA_ROL")
	private Roles roles;
}
