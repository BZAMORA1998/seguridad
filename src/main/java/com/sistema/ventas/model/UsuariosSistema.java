package com.sistema.ventas.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "USUARIOS_SISTEMA")
public class UsuariosSistema  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECUENCIA_USUARIO_SISTEMA")
    private Integer secuenciaUsuarioSistema;
	
	 @Column(name = "USUARIO")
	 private String usuario;
		
	 @Column(name = "CONTRASEÑA")
	 private String contraseña;
	 
	 @Column(name = "ES_ACTIVO")
	 private String esActivo;
	 
	 @Column(name = "TOKEN")
	 private String token;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "SECUENCIA_PERSONA", referencedColumnName = "SECUENCIA_PERSONA")
	private Personas personas;
}
