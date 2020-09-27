package com.sistema.ventas.bo.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.ventas.bo.IAutenticacionBO;
import com.sistema.ventas.dao.UsuarioSistemaDAO;
import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.UsuarioSistema;
import com.sistema.ventas.util.AuthenticationScheme;
import com.sistema.ventas.util.GeneralUtil;
import com.sistema.ventas.util.SeguridadUtil;

@Service
public class AutenticacionBOImpl implements IAutenticacionBO{

	@Autowired
	private UsuarioSistemaDAO objUsuarioSistemaDAO;
	
	@Override
	public AutenticacionDTO login(String strBasic, String strApplication) throws BOException {

		String[] strAuth=SeguridadUtil.obtenerBasicAuth(strBasic,AuthenticationScheme.BASIC.name());
		AutenticacionDTO objAut=null; 
		
		UsuarioSistema objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(strAuth[0]);
		
		if(objUsuario!=null && strAuth[1].equals(/*GeneralUtil.decodificaBase64(*/objUsuario.getContraseña())) {
			String strToken=UUID.randomUUID().toString();
			objUsuario.setToken(strToken);
			objUsuarioSistemaDAO.update(objUsuario);
			
			objAut=new AutenticacionDTO();
			objAut.setPrimerApellido(objUsuario.getPersona().getPrimerApellido());
			objAut.setPrimerNombre(objUsuario.getPersona().getPrimerNombre());
			objAut.setPrimerNombre(objUsuario.getPersona().getPrimerNombre());
			objAut.setToken(strToken);
		}
		
		return objAut;
	}

}
