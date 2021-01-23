package com.sistema.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InfoEmpresaDTO {
	private String nemonico;
	private String nombre;
	private String descripcion;
}
