package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.dto.ConsultarRolesRutaUsuarioDTO;
import com.sistema.ventas.dto.GuardarRolesDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IRolesBO {

	List<ConsultarRolesDTO> consultarRolesUsuario(String username)throws BOException;

	List<ConsultarRolesDTO> consultarRolesRuta(String strRuta)throws BOException;

	List<ConsultarRolesRutaUsuarioDTO> consultarRolesRutaUsuario(String username, Integer intSecuenciaRol)throws BOException;

	List<ConsultarRolesDTO> consultarRoles()throws BOException;

	void guardaRolesPorUrl(GuardarRolesDTO objGuardarRolesDTO, String username) throws BOException;

}
