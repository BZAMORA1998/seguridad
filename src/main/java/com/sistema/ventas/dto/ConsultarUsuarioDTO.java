package com.sistema.ventas.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsultarUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String numeroIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String usuario;
    private String fechaNacimiento;
    private Integer codigoTipoIdentificacion;
    private Integer codigoGenero;
	public Integer secuenciaUsuarioSistema;
    private String rolSistema;
}
