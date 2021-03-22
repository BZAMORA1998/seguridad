
package com.sistema.ventas.bo;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.sistema.ventas.dto.ConsultarUsuarioDTO;
import com.sistema.ventas.dto.UsuariosDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IUsuariosBO {

	/**
	 * Crea un nuevo usuario
	 * 
	 * @author Bryan Zamora
	 * @param objUsuariosDTO
	 * @param  strUsuario
	 * @return
	 * @throws BOException
	 */
	public Map<String, Object> crearUsuario(UsuariosDTO objUsuariosDTO,String strUsuario) throws BOException;

	/**
	 * Elimina un usuario de manera logica
	 * 
	 * @author Bryan Zamora
	 * @param  intIdUsuario
	 * @param  strUsuario
	 * @return
	 * @throws BOException
	 */
	public Map<String, Object> eliminarUsuario(Integer intIdUsuario, String strUsuario)  throws BOException;

	/**
	 * Consulta Usuario por id
	 * 
	 * @author Bryan Zamora
	 * @param  intIdUsuario
	 * @return
	 * @throws BOException
	 */
	public ConsultarUsuarioDTO consultarUsuarioXId(Integer intIdUsuario)  throws BOException;

	/**
	 * Actualiza el usuario
	 * 
	 * @author Bryan Zamora
	 * @param  intIdUsuario
	 * @return
	 * @throws BOException
	 */
	public void actualizarUsuario(Integer intIdUsuario, UsuariosDTO objUsuariosDTO, String strUsuario)throws BOException;

	/**
	 * Guarda la foto de una persona
	 * 
	 * @author Bryan Zamora
	 * @param  photo
	 * @param  intIdPersona
	 * @param  strUsuario
	 * @return
	 * @throws BOException
	 */
	public void guardarPhoto(MultipartFile photo, Integer intIdPersona, String strUsuario)throws BOException, IOException;

	
	/**
	 * Consulta los usuarios para pagineo
	 * 
	 * @author Bryan Zamora
	 * @param  intPage
	 * @param  intPerPage
	 * @param  strCedulaCodigoUsuario
	 * @param  strEstado
	 * @param strUser 
	 * @return
	 * @throws BOException
	 */
	public Map<String, Object> consultarUsuarios(Integer intPage, Integer intPerPage, String strCedulaCodigoUsuario, String strEstado, String strUser)throws BOException;

	/**
	 * Consulta los usuario disponible
	 * 
	 * @author Bryan Zamora
	 * @param  intPage
	 * @param  intPerPage
	 * @param  strCedulaCodigoUsuario
	 * @param  strEstado
	 * @param strUser 
	 * @return
	 * @throws BOException
	 */
	public Map<String, Object> consultarUsuarioDisponible(String strPrimerNombre, String strSegundoNombre,
			String stPrimerApellido, String strSegundoApellido)throws BOException;

}
