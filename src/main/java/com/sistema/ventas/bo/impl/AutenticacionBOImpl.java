package com.sistema.ventas.bo.impl;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IAutenticacionBO;
import com.sistema.ventas.dao.UsuarioSistemaDAO;
import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.UsuariosSistema;
import com.sistema.ventas.util.AuthenticationScheme;
import com.sistema.ventas.util.GeneralUtil;
import com.sistema.ventas.util.SeguridadUtil;

@Service
public class AutenticacionBOImpl implements IAutenticacionBO{

	@Autowired
	private UsuarioSistemaDAO objUsuarioSistemaDAO;
	
	@Override
	@Transactional
	public AutenticacionDTO login(String strBasic) throws BOException {
				
		String[] strAuth=SeguridadUtil.obtenerBasicAuth(strBasic,AuthenticationScheme.BASIC.name());
		AutenticacionDTO objAut=null; 
		
		UsuariosSistema objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(strAuth[0]);
		
		if(objUsuario==null)
			throw new BOException("ven.warn.usuarioNoExiste", new Object[] {strAuth[0]});
		
		if(objUsuario!=null && strAuth[1].equals(GeneralUtil.decodificaBase64(objUsuario.getContraseña()))) {
			String strToken=UUID.randomUUID().toString();
			objUsuario.setToken(strToken);
			objUsuarioSistemaDAO.update(objUsuario);
			
			objAut=new AutenticacionDTO();
			objAut.setSecuenciaSistemaUsuario(objUsuario.getSecuenciaUsuarioSistema());
			objAut.setPrimerApellido(objUsuario.getPersonas().getPrimerApellido());
			objAut.setPrimerNombre(objUsuario.getPersonas().getPrimerNombre());
			objAut.setUsuario(objUsuario.getUsuario());
			objAut.setToken(strToken);
		}else {
			throw new BOException("ven.warn.usuarioIncorrecto");
		}
		
		return objAut;
	}

}
