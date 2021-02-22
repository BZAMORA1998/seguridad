package com.sistema.ventas.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProvinciaDTO {
	 private Integer secuenciaPais;
	 private Integer secuenciaProvincia;
	 private String nombre;
	 private String esActivo;
}
