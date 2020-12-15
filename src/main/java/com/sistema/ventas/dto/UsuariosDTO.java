package com.sistema.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuariosDTO {

	private Integer secuenciaUsuarioSistema;
	private String numeroIdentificacion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento;
    private Integer codigoTipoIdentificacion;
    private Integer codigoGenero;
    private String user;
    private String password;
    private String rolSistema;    
}
