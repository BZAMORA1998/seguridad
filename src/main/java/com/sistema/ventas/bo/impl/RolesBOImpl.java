package com.sistema.ventas.bo.impl;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IRolesBO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.dto.ConsultarRolesRutaUsuarioDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Usuarios;

@Service
public class RolesBOImpl implements IRolesBO{

	@Autowired
	private RolesDAO objRolesDAO;
	@Autowired
	private UsuariosDAO objUsuariosDAO;
	

	@Override
	public List<ConsultarRolesDTO> consultarRolesUsuario(String username) throws BOException {
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRolesUsuario(objUsuario.getSecuenciaUsuario());
	}


	@Override
	public List<ConsultarRolesDTO> consultarRolesRuta(String strRuta) throws BOException {
		
		if (ObjectUtils.isEmpty(strRuta)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.ruta"});
		
		return objRolesDAO.consultarRolesRuta(strRuta);
	}


	@Override
	public List<ConsultarRolesRutaUsuarioDTO> consultarRolesRutaUsuario(String username, Integer intSecuenciaRol)
			throws BOException {
		
		if (ObjectUtils.isEmpty(intSecuenciaRol)) 
			throw new BOException("ven.warn.campoObligatorio", new Object[] { "ven.campos.secuenciaRol"});
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRolesRutaUsuario(intSecuenciaRol,objUsuario.getSecuenciaUsuario(),true,null);
	}


	@Override
	public List<ConsultarRolesDTO> consultarRoles() throws BOException {
		return objRolesDAO.consultarRoles();
	}


}
