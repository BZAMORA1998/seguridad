package com.sistema.ventas.segurity;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.sistema.ventas.dao.UsuarioSistemaDAO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.UsuariosSistema;
import com.sistema.ventas.util.GeneralUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuarioSistemaDAO objUsuarioSistemaDAO;
	
	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String username){
		// TODO Auto-generated method stub
		UsuariosSistema objUsuario=objUsuarioSistemaDAO.consultarUsuarioSistema(username);
		
		try {
			return new User(objUsuario.getUsuario(),GeneralUtil.decodificaBase64(objUsuario.getContraseña()),new ArrayList());
		} catch (BOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
