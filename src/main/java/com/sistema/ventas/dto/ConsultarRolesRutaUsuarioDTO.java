package com.sistema.ventas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConsultarRolesRutaUsuarioDTO {
	private Integer secuenciaRuta;
	private String nombre;
	private Boolean esSelect;
	private List<ConsultarRolesRutaUsuarioDTO> ruta;
}
