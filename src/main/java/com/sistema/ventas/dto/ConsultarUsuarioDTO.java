package com.sistema.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsultarUsuarioDTO {
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
}
