package com.sistema.ventas.bo.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.sistema.ventas.bo.IAutenticacionBO;
import com.sistema.ventas.dao.UsuarioSistemaDAO;
import com.sistema.ventas.dto.AutenticacionDTO;
import com.sistema.ventas.enumm.AuthenticationScheme;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.UsuariosSistema;
import com.sistema.ventas.segurity.JwtUtil;
import com.sistema.ventas.util.SeguridadUtil;

@Service
public class AutenticacionBOImpl implements IAutenticacionBO{

	@Autowired
	private UsuarioSistemaDAO objUsuarioSistemaDAO;
	@Autowired
	private JwtUtil jwUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	@Transactional
	public AutenticacionDTO login(String strBasic) throws BOException {
				
		String[] strAuth=SeguridadUtil.obtenerBasicAuth(strBasic,AuthenticationScheme.BASIC.name());
		AutenticacionDTO objAut=null; 
		
		try {
			UsuariosSistema objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(strAuth[0]);
			
			if(objUsuario==null) {
				throw new BOException("ven.warn.usuarioNoExiste", new Object[] {strAuth[0]});
			}else {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(strAuth[0],
						strAuth[1]));
				
				objAut=new AutenticacionDTO();
				objAut.setSecuenciaSistemaUsuario(objUsuario.getSecuenciaUsuarioSistema());
				objAut.setPrimerApellido(objUsuario.getPersonas().getPrimerApellido());
				objAut.setPrimerNombre(objUsuario.getPersonas().getPrimerNombre());
				objAut.setUsuario(objUsuario.getUsuario());
				objAut.setToken(jwUtil.generateToken(strAuth[0]));
			}
		
		} catch (BadCredentialsException e) {
			throw new BOException("ven.warn.credencialesInvalidas");
		}
		
		return objAut;
	}

}
