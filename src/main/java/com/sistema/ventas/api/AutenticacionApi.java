package com.sistema.ventas.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacion")
public class AutenticacionApi {

	@RequestMapping(value = "/iniciar_sesion", method = RequestMethod.GET)
	public String inactivaAfiliado(){
		return "Hola";
	}
}
