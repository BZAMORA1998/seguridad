package com.sistema.ventas.segurity;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.sistema.ventas.dao.UsuariosDAO;
import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Usuarios;
import com.sistema.ventas.util.GeneralUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UsuariosDAO objUsuariosDAO;
	
	@SuppressWarnings("unused")
	@Override
	public UserDetails loadUserByUsername(String username){
		// TODO Auto-generated method stub
		Usuarios objUsuario=objUsuariosDAO.consultarUsuarioSistema(username);
		String strPassword;
		try {
			strPassword = GeneralUtil.decodificaBase64(objUsuario.getContrasenia());
			return new User(objUsuario.getUsuario(),strPassword,new ArrayList());
		} catch (BOException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
