package com.sistema.ventas.bo;

import java.util.Map;

import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IUsuariosSistemaBO {

	public void crearUsuario(UsuariosDTO objUsuariosDTO) throws BOException;

	public Map<String, Object> consultarUsuarios(Integer intPage, Integer intPerPage) throws BOException;

	public void eliminarUsuario(Integer intIdUsuario)  throws BOException;

	public ConsultarUsuarioDTO consultarUsuarioXId(Integer intIdUsuario)  throws BOException;

	public void actualizarUsuario(Integer intIdUsuario, UsuariosDTO objUsuariosDTO)throws BOException;

}
