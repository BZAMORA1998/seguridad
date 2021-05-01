package com.sistema.ventas.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IModulosBO;
import com.sistema.ventas.bo.IRolesBO;
import com.sistema.ventas.dao.ModulosDAO;
import com.sistema.ventas.dao.RolesDAO;
import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.dto.ConsultarModulosDTO;
import com.sistema.ventas.dto.ConsultarRolesDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Usuarios;

@Service
public class RolesBOImpl implements IRolesBO{

	@Autowired
	private RolesDAO objRolesDAO;
	@Autowired
	private UsuariosDAO objUsuariosDAO;
	

	@Override
	public List<ConsultarRolesDTO> consultarRoles(String username) throws BOException {
		
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistemaPorCorreo(username);
		
		return objRolesDAO.consultarRoles(objUsuario.getSecuenciaUsuario());
	}


}
