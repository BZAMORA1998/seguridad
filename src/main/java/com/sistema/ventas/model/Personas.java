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

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "tbl_personas")
public class Personas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "secuencia_persona")
    private Integer secuenciaPersona;
	
	@Column(name = "numero_identificacion")
	private String numeroIdentificacion;
	 
    @Column(name = "primer_nombre")
    private String primerNombre;
	
    @Column(name = "segundo_nombre")
    private String segundoNombre;
		
    @Column(name = "primer_apellido")
    private String primerApellido;
	
    @Column(name = "segundo_apellido")
    private String segundoApellido;
	
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    
	@Column(name = "es_activo")
	private String esActivo;
	
	@Column(name = "foto")
	private byte[] foto;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "secuencia_tipo_identificacion", referencedColumnName = "secuencia_tipo_identificacion", insertable = true, updatable = true)
	private TiposIdentificacion tiposIdentificacion;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "secuencia_GENERO", referencedColumnName = "secuencia_GENERO", insertable = true, updatable = true)
	private Generos genero;
}

