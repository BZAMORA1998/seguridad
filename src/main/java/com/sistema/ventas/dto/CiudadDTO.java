package com.sistema.ventas.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CiudadDTO {
	 private Integer secuenciaCiudad;
	 private String nombre;
	 private String esActivo;
}
