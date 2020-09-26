package com.sistema.ventas.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.ventas.dao.ITiposIdentificacionDAO;
import com.sistema.ventas.dao.TiposIdentificacionDAO;
import com.sistema.ventas.model.TiposIdentificacion;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionApi {

//	@Autowired
//	private TiposIdentificacionDAO objTiposIdentificacionDAO;
//	@Autowired
//	private ITiposIdenticacionDAO objITiposIdenticacionDAO;
	
	
	@RequestMapping(value = "/iniciar_sesion", method = RequestMethod.GET)
	public void inactivaAfiliado(){
		
	}
}
