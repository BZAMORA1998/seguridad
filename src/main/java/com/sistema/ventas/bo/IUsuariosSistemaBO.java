package com.sistema.ventas.bo;

import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IUsuariosSistemaBO {

	public void crearUsuario(UsuariosDTO objUsuariosDTO) throws BOException;

}
