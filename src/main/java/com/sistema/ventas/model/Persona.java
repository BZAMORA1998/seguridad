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
@Table(name = "USUARIO_SISTEMA")
public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECUENCIA_PERSONA")
    private Integer secuenciaPersona;
	
	@Column(name = "NUMERO_IDENTIFICACION")
	private String numeroIdentificacion;
	 
    @Column(name = "PRIMER_NOMBRE")
    private String primerNombre;
	
    @Column(name = "SEGUNDO_NOMBRE")
    private String segundoNombre;
		
    @Column(name = "PRIMER_APELLIDO")
    private String primerApellido;
	
    @Column(name = "SEGUNDO_APELLIDO")
    private String segundoApellido;
	
    @Column(name = "FECHA_NACIMIENTO")
    private Date fechaNacimiento;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_TIPO_IDENTIFICACION", referencedColumnName = "CODIGO_TIPO_IDENTIFICACION", insertable = true, updatable = true)
	private TiposIdentificacion tiposIdentificacion;
}

