package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.dto.ConsultarRolesRutaUsuarioDTO;
import com.sistema.ventas.exceptions.BOException;

public interface IRolesBO {

	List<ConsultarRolesDTO> consultarRolesUsuario(String username)throws BOException;
	
	List<ConsultarRolesDTO> consultarRolesNoUsuario(Integer intIdUsuario,String username)throws BOException;

	List<ConsultarRolesDTO> consultarRolesRuta(String strRuta)throws BOException;

	List<ConsultarRolesRutaUsuarioDTO> consultarRolesRutaUsuario(String username, Integer intSecuenciaRol)throws BOException;

	List<ConsultarRolesDTO> consultarRoles()throws BOException;

	void guardaRolesPorUrl(List<Integer> lsSecuenciaRutas, Integer intSecuenciaRol, String username) throws BOException;

	void crearRol(String strNombre, String username)throws BOException;

}
