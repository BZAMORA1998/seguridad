package com.sistema.ventas.bo;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IUsuariosBO {

	public Map<String, Object> crearUsuario(UsuariosDTO objUsuariosDTO,String strUsuario) throws BOException;

	public void eliminarUsuario(Integer intIdUsuario, String strUsuario)  throws BOException;

	public ConsultarUsuarioDTO consultarUsuarioXId(Integer intIdUsuario)  throws BOException;

	public void actualizarUsuario(Integer intIdUsuario, UsuariosDTO objUsuariosDTO, String strUsuario)throws BOException;

	public void guardarPhoto(MultipartFile photo, Integer intIdPersona, String strUsuario)throws BOException, IOException;

	public Map<String, Object> consultarUsuarios(Integer intPage, Integer intPerPage, String strCedulaCodigoUsuario, String strEstado)throws BOException;

}
